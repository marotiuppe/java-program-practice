/* Service worker: caches the app shell and visited pages so question text can be read offline.
   Running code still needs the server. Bump VERSION to drop old caches after a change here. */
const VERSION = 'v3';
const STATIC = 'static-' + VERSION;
const PAGES = 'pages-' + VERSION;
const SHELL = ['/static/style.css', '/static/editor.js', '/static/javalang.js', '/static/tabs.js', '/static/celebrate.js',
  '/static/report.js', '/static/mock.js', '/static/pwa.js', '/static/logo.svg', '/static/favicon.svg'];

self.addEventListener('install', (e) => {
  e.waitUntil(caches.open(STATIC).then((c) => c.addAll(SHELL)).then(() => self.skipWaiting()));
});

self.addEventListener('activate', (e) => {
  e.waitUntil(
    caches.keys()
      .then((keys) => Promise.all(keys.filter((k) => k !== STATIC && k !== PAGES).map((k) => caches.delete(k))))
      .then(() => self.clients.claim())
  );
});

// Pages hold the learner's email and saved code, so they are wiped on logout.
self.addEventListener('message', (e) => {
  if (e.data === 'clear-pages') e.waitUntil(caches.delete(PAGES));
});

self.addEventListener('fetch', (e) => {
  const req = e.request;
  if (req.method !== 'GET') return;
  const url = new URL(req.url);
  if (url.origin !== location.origin || url.pathname.startsWith('/api/') || url.pathname === '/health') return;

  if (url.pathname.startsWith('/static/')) {
    // stale-while-revalidate: fast, and refreshed in the background
    e.respondWith(caches.open(STATIC).then(async (cache) => {
      const hit = await cache.match(req);
      const fresh = fetch(req).then((r) => { if (r.ok) cache.put(req, r.clone()); return r; }).catch(() => hit);
      return hit || fresh;
    }));
    return;
  }

  if (req.mode === 'navigate') {
    // network first so progress is always current; fall back to the last copy when offline.
    // If the server takes more than WAKE_AFTER_MS (a sleeping free instance), show a friendly
    // "waking up" page that polls /health and reloads as soon as the server answers.
    const network = fetch(req).then((r) => {
      if (r.ok && !r.redirected && /^\/(day\/|dashboard)/.test(url.pathname)) {
        const copy = r.clone();
        caches.open(PAGES).then((c) => c.put(req, copy));
      }
      return r;
    });
    const wake = new Promise((resolve) => setTimeout(() => resolve(wakingPage()), WAKE_AFTER_MS));
    e.respondWith(Promise.race([network, wake]).catch(async () => (await caches.match(req)) || offlinePage()));
  }
});

const WAKE_AFTER_MS = 5000;
const html = (body, status) => new Response(body, {
  status: status || 200, headers: { 'Content-Type': 'text/html; charset=utf-8', 'Cache-Control': 'no-store' },
});

function offlinePage() {
  return html('<!doctype html><meta name="viewport" content="width=device-width,initial-scale=1"><title>Offline</title>' +
    '<body style="font-family:system-ui;padding:24px"><h1>You are offline</h1>' +
    '<p>This page has not been saved on this device yet. Reconnect and open it once to read it offline.</p></body>', 503);
}

function wakingPage() {
  return html('<!doctype html><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1">' +
    '<title>Waking up…</title><body style="font-family:system-ui;max-width:420px;margin:12vh auto;padding:24px;text-align:center">' +
    '<h1>Waking up the server…</h1>' +
    '<p>The free server naps when nobody is using it. It usually takes under a minute to start.</p>' +
    '<p id="t" style="font-size:32px;font-weight:700;margin:12px 0">0s</p>' +
    '<p id="m" style="color:#667085">Your progress is safe. This page will reload by itself.</p>' +
    '<script>var n=0,t=document.getElementById("t");setInterval(function(){n++;t.textContent=n+"s"},1000);' +
    'function poll(){fetch("/health",{cache:"no-store"}).then(function(r){if(r.ok)location.reload();else setTimeout(poll,3000)})' +
    '.catch(function(){setTimeout(poll,3000)})}poll();' +
    'setTimeout(function(){document.getElementById("m").textContent="Taking longer than usual. Still trying…"},90000);</script></body>');
}
