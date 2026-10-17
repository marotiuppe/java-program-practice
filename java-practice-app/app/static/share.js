/* Shareable achievement cards (roadmap 4.2).
   javaShare.create(kind, day)   asks the server for a signed card ("day", "goal", "streak", "level", "progress")
   javaShare.actions(info, note) returns the Share / Save image / Copy link buttons for a created card
   Any element with data-share-kind (and optional data-share-day) opens a share dialog when clicked. */
(function () {
  function el(tag, cls, text) {
    const e = document.createElement(tag);
    if (cls) e.className = cls;
    if (text !== undefined) e.textContent = text;
    return e;
  }

  async function create(kind, day) {
    const r = await fetch('/api/share/card', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ kind: kind, day: day === undefined ? null : day }),
    });
    const data = await r.json().catch(() => ({}));
    if (!r.ok) throw new Error(data.error || 'Could not make the card.');
    return data;
  }

  function copy(text, note, message) {
    const ok = () => { note.textContent = message; };
    const fail = () => { note.textContent = text; };
    if (navigator.clipboard && navigator.clipboard.writeText) navigator.clipboard.writeText(text).then(ok, fail);
    else fail();
  }

  function actions(info, note) {
    const row = el('div', 'share-actions');
    const shareBtn = el('button', 'primary', 'Share');
    shareBtn.type = 'button';
    shareBtn.addEventListener('click', async () => {
      try {  // best: the picture itself, with the message and link (phones)
        const blob = await (await fetch(info.card_url)).blob();
        const file = new File([blob], 'java-practice.png', { type: 'image/png' });
        if (navigator.canShare && navigator.canShare({ files: [file] })) {
          await navigator.share({ files: [file], title: info.title, text: info.text + ' ' + info.url });
          return;
        }
      } catch (e) {
        if (e && e.name === 'AbortError') return;  // the learner closed the share sheet
      }
      try {  // next best: the link, which shows the card as its preview
        if (navigator.share) { await navigator.share({ title: info.title, text: info.text, url: info.url }); return; }
      } catch (e) {
        if (e && e.name === 'AbortError') return;
      }
      copy(info.text + ' ' + info.url, note, 'Message and link copied. Paste it anywhere.');
    });
    const saveBtn = el('button', '', 'Save image');
    saveBtn.type = 'button';
    saveBtn.addEventListener('click', () => {
      const a = document.createElement('a');
      a.href = info.card_url; a.download = 'java-practice-achievement.png';
      document.body.appendChild(a); a.click(); a.remove();
      note.textContent = 'Image saved.';
    });
    const copyBtn = el('button', '', 'Copy link');
    copyBtn.type = 'button';
    copyBtn.addEventListener('click', () => copy(info.url, note, 'Link copied. It shows your card when pasted in chats.'));
    [shareBtn, saveBtn, copyBtn].forEach((b) => row.appendChild(b));
    return row;
  }

  function dialog(kind, day, opener) {
    if (document.getElementById('share-sheet')) return;
    const overlay = el('div', 'celebrate-overlay');
    overlay.id = 'share-sheet';
    overlay.setAttribute('role', 'dialog');
    overlay.setAttribute('aria-modal', 'true');
    overlay.setAttribute('aria-label', 'Share your progress');
    const box = el('div', 'share-sheet-box');
    const status = el('p', 'muted', 'Making your card…');
    const note = el('p', 'muted small');
    note.setAttribute('role', 'status');
    const closeBtn = el('button', '', 'Close');
    closeBtn.type = 'button';
    box.appendChild(status);
    const bar = el('div', 'bar');
    bar.appendChild(closeBtn);
    box.appendChild(bar);
    box.appendChild(note);
    overlay.appendChild(box);
    const close = () => {
      overlay.remove();
      document.removeEventListener('keydown', onKey);
      if (opener && opener.focus) opener.focus();
    };
    const onKey = (e) => {
      if (e.key === 'Escape') { close(); return; }
      if (e.key !== 'Tab') return;
      const items = Array.from(overlay.querySelectorAll('button:not([disabled]), a[href]'));
      const first = items[0], last = items[items.length - 1];
      if (e.shiftKey && document.activeElement === first) { e.preventDefault(); last.focus(); }
      else if (!e.shiftKey && document.activeElement === last) { e.preventDefault(); first.focus(); }
    };
    closeBtn.addEventListener('click', close);
    overlay.addEventListener('click', (e) => { if (e.target === overlay) close(); });
    document.addEventListener('keydown', onKey);
    document.body.appendChild(overlay);
    closeBtn.focus();
    create(kind, day).then((info) => {
      const img = el('img', 'celebrate-card');
      img.alt = info.title + ' — your achievement card';
      img.src = info.card_url;
      box.replaceChild(img, status);
      box.insertBefore(actions(info, note), bar);
    }).catch((err) => { status.textContent = err.message; });
  }

  document.addEventListener('click', (e) => {
    const b = e.target.closest && e.target.closest('[data-share-kind]');
    if (!b) return;
    e.preventDefault();
    const d = b.getAttribute('data-share-day');
    dialog(b.getAttribute('data-share-kind'), d ? parseInt(d, 10) : undefined, b);
  });

  window.javaShare = { create: create, actions: actions, dialog: dialog };
})();
