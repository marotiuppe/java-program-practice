(function () {
  // Day 30 mock assessment timer. State lives in the browser (localStorage), so it survives page changes.
  const bar = document.getElementById('mock-bar');
  if (!bar) return;
  const key = 'mock:' + bar.dataset.level;
  const total = (+bar.dataset.minutes) * 60000;
  const canStart = bar.dataset.start === '1';
  const timeEl = document.getElementById('mock-time');
  const startBtn = document.getElementById('mock-start');
  const stopBtn = document.getElementById('mock-stop');
  const safe = (fn) => { try { return fn(); } catch (e) { return null; } };

  function read() {
    const v = safe(() => JSON.parse(localStorage.getItem(key)));
    return v && v.start ? v : null;
  }

  function render() {
    const st = read();
    if (!st) {
      bar.hidden = !canStart;
      bar.classList.remove('low', 'over');
      timeEl.hidden = true;
      stopBtn.hidden = true;
      startBtn.hidden = false;
      return;
    }
    bar.hidden = false;
    startBtn.hidden = true;
    stopBtn.hidden = false;
    timeEl.hidden = false;
    const left = st.start + total - Date.now();
    if (left <= 0) {
      timeEl.textContent = "Time's up";
      bar.classList.add('over');
    } else {
      const m = Math.floor(left / 60000), s = Math.floor((left % 60000) / 1000);
      timeEl.textContent = String(m).padStart(2, '0') + ':' + String(s).padStart(2, '0');
      bar.classList.toggle('low', left < 10 * 60000);
      bar.classList.remove('over');
    }
  }

  startBtn.addEventListener('click', () => {
    safe(() => localStorage.setItem(key, JSON.stringify({ start: Date.now() })));
    render();
  });
  stopBtn.addEventListener('click', () => {
    if (confirm('End the mock assessment and clear the timer?')) {
      safe(() => localStorage.removeItem(key));
      render();
    }
  });
  setInterval(render, 1000);
  render();
})();
