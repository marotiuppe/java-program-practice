(function () {
  const box = document.getElementById('feedback');
  if (!box) return;
  const msg = document.getElementById('fb-msg');
  const state = { rating: +box.dataset.rating || 0, difficulty: box.dataset.difficulty || '' };

  function paint() {
    box.querySelectorAll('button[data-rating]').forEach((b) => b.setAttribute('aria-pressed', String(+b.dataset.rating === state.rating)));
    box.querySelectorAll('button[data-difficulty]').forEach((b) => b.setAttribute('aria-pressed', String(b.dataset.difficulty === state.difficulty)));
  }

  async function send(payload) {
    msg.textContent = '';
    try {
      const r = await fetch('/api/feedback', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(Object.assign({ day: +box.dataset.day, q: +box.dataset.q }, payload)),
      });
      const res = await r.json();
      if (res.ok) { state.rating = res.rating; state.difficulty = res.difficulty; paint(); msg.textContent = 'Thanks for the feedback.'; }
      else msg.textContent = res.error || 'Could not save.';
    } catch (e) { msg.textContent = 'Could not reach the server.'; }
  }

  box.addEventListener('click', (e) => {
    const b = e.target.closest('button.fb');
    if (!b) return;
    // pressing the selected choice again clears it
    if (b.dataset.rating !== undefined) send({ rating: +b.dataset.rating === state.rating ? 0 : +b.dataset.rating });
    else send({ difficulty: b.dataset.difficulty === state.difficulty ? '' : b.dataset.difficulty });
  });
  paint();
})();
