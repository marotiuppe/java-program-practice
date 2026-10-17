/* "Day complete" / "Daily goal reached" / streak and level moments, shown by editor.js when the server says so.
   Each moment comes with a shareable achievement card (made by share.js and the server). */
(function () {
  function el(tag, cls, text) {
    const e = document.createElement(tag);
    if (cls) e.className = cls;
    if (text !== undefined) e.textContent = text;
    return e;
  }

  window.celebrate = function (info) {
    if (document.getElementById('celebrate')) return;
    const dc = info.day_complete, gr = info.goal_reached, lc = info.level_complete, sm = info.streak_milestone;
    // the biggest moment decides the title and the card
    let title = 'Daily goal reached!', kind = 'goal', day;
    if (lc) { title = lc.level + ' level complete!'; kind = 'level'; }
    else if (dc) { title = 'Day ' + dc.day + ' complete!'; kind = 'day'; day = dc.day; }
    else if (sm) { title = sm.days + '-day streak!'; kind = 'streak'; }
    const lines = [];
    if (lc) lines.push('You solved every question in this level. Excellent work!');
    if (dc) lines.push('You solved all 5 questions of Day ' + dc.day + '.');
    if (sm) lines.push('You have practised ' + sm.days + ' days in a row.');
    if (gr) lines.push('You hit your goal of ' + gr.goal + ' question' + (gr.goal === 1 ? '' : 's') + ' today.');

    const overlay = el('div', 'celebrate-overlay');
    overlay.id = 'celebrate';
    overlay.setAttribute('role', 'dialog');
    overlay.setAttribute('aria-modal', 'true');
    overlay.setAttribute('aria-label', title);
    const box = el('div', 'celebrate-box');
    const preview = el('div', 'muted', 'Making your card…');
    box.appendChild(preview);
    box.appendChild(el('h2', '', title));
    lines.forEach((l) => box.appendChild(el('p', '', l)));
    if (dc && dc.next) box.appendChild(el('p', 'muted', 'Tomorrow’s day is ready: Day ' + dc.next.n + ' — ' + dc.next.title));
    if (dc && !dc.next) box.appendChild(el('p', 'muted', 'That was the last day of this level. Well done!'));
    const note = el('p', 'muted small');
    note.setAttribute('role', 'status');
    const row = el('div', 'bar');
    const closeBtn = el('button', 'primary', 'Keep going');
    const opener = document.activeElement;
    const close = () => {
      overlay.remove();
      document.removeEventListener('keydown', onKey);
      if (opener && opener.focus) opener.focus();
    };
    const onKey = (e) => {
      if (e.key === 'Escape') { close(); return; }
      if (e.key !== 'Tab') return;  // keep Tab inside the dialog
      const items = Array.from(overlay.querySelectorAll('button:not([disabled]), a[href]'));
      const first = items[0], last = items[items.length - 1];
      if (e.shiftKey && document.activeElement === first) { e.preventDefault(); last.focus(); }
      else if (!e.shiftKey && document.activeElement === last) { e.preventDefault(); first.focus(); }
    };
    closeBtn.addEventListener('click', close);
    row.appendChild(closeBtn);
    if (dc || lc) {
      const dash = el('a', '', 'Dashboard');
      dash.href = '/dashboard';
      row.appendChild(dash);
    }
    const shareSlot = el('div');
    box.appendChild(shareSlot);
    box.appendChild(row);
    box.appendChild(note);
    overlay.appendChild(box);
    overlay.addEventListener('click', (e) => { if (e.target === overlay) close(); });
    document.addEventListener('keydown', onKey);
    document.body.appendChild(overlay);
    closeBtn.focus();

    if (!window.javaShare) { preview.remove(); return; }
    window.javaShare.create(kind, day).then((card) => {
      const img = el('img', 'celebrate-card');
      img.alt = card.title + ' — your achievement card';
      img.src = card.card_url;
      box.replaceChild(img, preview);
      shareSlot.appendChild(window.javaShare.actions(card, note));
    }).catch(() => { preview.remove(); });  // the celebration still works without a card
  };
})();
