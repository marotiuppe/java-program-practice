/* Registers the service worker and offers "Install app" where the browser supports it. */
(function () {
  // Tell the server our timezone so "today" and streaks follow the learner's own clock.
  try {
    const tz = Intl.DateTimeFormat().resolvedOptions().timeZone;
    if (tz) document.cookie = 'tz=' + encodeURIComponent(tz) + '; path=/; max-age=31536000; samesite=lax';
  } catch (e) { /* cookie stays unset: server falls back to its default zone */ }

  // Keep a small summary of the learner's progress on this device (pages put it in #jp-snapshot) so the
  // "waking up the server" screen can show it while the server starts. Never leaves the device.
  try {
    const node = document.getElementById('jp-snapshot');
    if (node) {
      const fresh = JSON.parse(node.textContent);
      const old = JSON.parse(localStorage.getItem('jp_snapshot') || 'null');
      const merged = old && old.k === fresh.k ? Object.assign({}, old, fresh) : fresh;
      merged.t = Date.now();
      localStorage.setItem('jp_snapshot', JSON.stringify(merged));
    }
  } catch (e) { /* storage unavailable: the waiting screen simply shows less */ }

  if ('serviceWorker' in navigator) {
    window.addEventListener('load', () => {
      navigator.serviceWorker.register('/sw.js').catch(() => {});
    });
    // Drop cached pages (they contain the user's email and code) when logging out.
    document.addEventListener('submit', (e) => {
      if (e.target.matches && e.target.matches('form[action="/logout"]')) {
        try { localStorage.removeItem('jp_snapshot'); localStorage.removeItem('jp_last'); } catch (err) { /* ignore */ }
        if (navigator.serviceWorker.controller) navigator.serviceWorker.controller.postMessage('clear-pages');
      }
    });
  }
  // Hamburger menu on phones
  const toggle = document.getElementById('nav-toggle');
  const menu = document.getElementById('main-nav');
  if (toggle && menu) {
    const setOpen = (open) => {
      menu.classList.toggle('open', open);
      toggle.setAttribute('aria-expanded', open ? 'true' : 'false');
    };
    toggle.addEventListener('click', () => setOpen(!menu.classList.contains('open')));
    menu.addEventListener('click', (e) => { if (e.target.closest('a')) setOpen(false); });
    document.addEventListener('click', (e) => {
      if (!menu.contains(e.target) && !toggle.contains(e.target)) setOpen(false);
    });
    document.addEventListener('keydown', (e) => { if (e.key === 'Escape') { setOpen(false); toggle.focus(); } });
    window.matchMedia('(min-width: 1021px)').addEventListener('change', () => setOpen(false));
  }
  let deferred = null;
  const btn = document.getElementById('install-app');
  window.addEventListener('beforeinstallprompt', (e) => {
    e.preventDefault();
    deferred = e;
    if (btn) btn.hidden = false;
  });
  if (btn) {
    btn.addEventListener('click', async () => {
      if (!deferred) return;
      deferred.prompt();
      await deferred.userChoice.catch(() => {});
      deferred = null;
      btn.hidden = true;
    });
  }
  window.addEventListener('appinstalled', () => { if (btn) btn.hidden = true; });
})();
