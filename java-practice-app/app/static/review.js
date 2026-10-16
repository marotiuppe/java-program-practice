(function () {
  const bar = document.getElementById('review-bar');
  if (!bar) return;
  const msg = document.getElementById('review-msg');
  bar.addEventListener('click', async (e) => {
    const b = e.target.closest('button[data-rating]');
    if (!b) return;
    bar.querySelectorAll('button').forEach((x) => { x.disabled = true; });
    msg.textContent = 'Saving...';
    try {
      const r = await fetch('/api/review/rate', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ day: +bar.dataset.day, q: +bar.dataset.q, rating: b.dataset.rating }),
      });
      const res = await r.json();
      if (res.ok) { location.href = res.next; return; }
      msg.textContent = res.error || 'Could not save.';
    } catch (err) {
      msg.textContent = 'Could not reach the server.';
    }
    bar.querySelectorAll('button').forEach((x) => { x.disabled = false; });
  });
})();
