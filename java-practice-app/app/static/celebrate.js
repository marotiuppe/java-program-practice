/* "Day complete" / "Daily goal reached" moment, shown by editor.js when the server says so. */
(function () {
  function el(tag, cls, text) {
    const e = document.createElement(tag);
    if (cls) e.className = cls;
    if (text !== undefined) e.textContent = text;
    return e;
  }

  function share(text, done) {
    const url = location.origin;
    if (navigator.share) {
      navigator.share({ title: 'Java Practice', text: text, url: url }).catch(() => {});
      return;
    }
    const full = text + ' ' + url;
    const ok = () => { done.textContent = 'Copied! Paste it anywhere.'; };
    if (navigator.clipboard && navigator.clipboard.writeText) navigator.clipboard.writeText(full).then(ok, () => { done.textContent = full; });
    else done.textContent = full;
  }

  window.celebrate = function (info) {
    if (document.getElementById('celebrate')) return;
    const dc = info.day_complete, gr = info.goal_reached;
    const title = dc ? 'Day ' + dc.day + ' complete!' : 'Daily goal reached!';
    const lines = [];
    if (dc) lines.push('You solved all 5 questions of Day ' + dc.day + '.');
    if (gr) lines.push('You hit your goal of ' + gr.goal + ' question' + (gr.goal === 1 ? '' : 's') + ' today.');
    const shareText = dc ? 'I just completed Day ' + dc.day + ' of my 30-day Java practice plan!'
                         : 'I hit my daily Java practice goal today!';

    const overlay = el('div', 'celebrate-overlay');
    overlay.id = 'celebrate';
    overlay.setAttribute('role', 'dialog');
    overlay.setAttribute('aria-modal', 'true');
    overlay.setAttribute('aria-label', title);
    const box = el('div', 'celebrate-box');
    box.appendChild(el('div', 'celebrate-emoji', dc ? '🎉' : '🎯'));
    box.appendChild(el('h2', '', title));
    lines.forEach((l) => box.appendChild(el('p', '', l)));
    if (dc && dc.next) box.appendChild(el('p', 'muted', 'Tomorrow’s day is ready: Day ' + dc.next.n + ' — ' + dc.next.title));
    if (dc && !dc.next) box.appendChild(el('p', 'muted', 'That was the last day of this level. Well done!'));
    const note = el('p', 'muted small');
    const row = el('div', 'bar');
    const shareBtn = el('button', '', 'Share');
    shareBtn.addEventListener('click', () => share(shareText, note));
    const closeBtn = el('button', 'primary', 'Keep going');
    const close = () => { overlay.remove(); document.removeEventListener('keydown', onKey); };
    const onKey = (e) => { if (e.key === 'Escape') close(); };
    closeBtn.addEventListener('click', close);
    row.appendChild(closeBtn);
    row.appendChild(shareBtn);
    if (dc) {
      const dash = el('a', '', 'Dashboard');
      dash.href = '/dashboard';
      row.appendChild(dash);
    }
    box.appendChild(row);
    box.appendChild(note);
    overlay.appendChild(box);
    overlay.addEventListener('click', (e) => { if (e.target === overlay) close(); });
    document.addEventListener('keydown', onKey);
    document.body.appendChild(overlay);
    closeBtn.focus();
  };
})();
