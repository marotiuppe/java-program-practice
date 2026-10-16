(function () {
  const box = document.getElementById('hints');
  const btn = document.getElementById('hint-btn');
  if (!box || !btn) return;
  const list = document.getElementById('hint-list');
  const msg = document.getElementById('hint-msg');
  btn.addEventListener('click', async () => {
    btn.disabled = true;
    msg.textContent = '';
    try {
      const r = await fetch('/api/hint', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ day: +box.dataset.day, q: +box.dataset.q }),
      });
      const res = await r.json();
      if (res.hints) {
        list.textContent = '';
        res.hints.forEach((h) => { const li = document.createElement('li'); li.textContent = h; list.appendChild(li); });
        if (!res.left) btn.hidden = true;
      } else {
        msg.textContent = res.error || 'Could not load a hint.';
      }
    } catch (e) {
      msg.textContent = 'Could not reach the server.';
    }
    btn.disabled = false;
  });
})();
