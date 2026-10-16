(function () {
  const box = document.getElementById('report');
  if (!box) return;
  const code = document.getElementById('code');
  const send = document.getElementById('report-send');
  const msg = document.getElementById('report-msg');
  send.addEventListener('click', async () => {
    send.disabled = true;
    msg.textContent = 'Sending...';
    try {
      const r = await fetch('/api/report', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          day: +code.dataset.day, q: +code.dataset.q, code: code.value,
          category: document.getElementById('report-cat').value,
          note: document.getElementById('report-note').value,
        }),
      });
      const res = await r.json();
      if (res.ok) {
        msg.textContent = 'Thanks! Your report was sent.';
        document.getElementById('report-note').value = '';
      } else {
        msg.textContent = res.error || 'Could not send the report.';
      }
    } catch (e) {
      msg.textContent = 'Could not reach the server.';
    }
    send.disabled = false;
  });
})();
