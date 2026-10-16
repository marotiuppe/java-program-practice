/* Registers the service worker and offers "Install app" where the browser supports it. */
(function () {
  if ('serviceWorker' in navigator) {
    window.addEventListener('load', () => {
      navigator.serviceWorker.register('/sw.js').catch(() => {});
    });
    // Drop cached pages (they contain the user's email and code) when logging out.
    document.addEventListener('submit', (e) => {
      if (e.target.matches && e.target.matches('form[action="/logout"]') && navigator.serviceWorker.controller) {
        navigator.serviceWorker.controller.postMessage('clear-pages');
      }
    });
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
