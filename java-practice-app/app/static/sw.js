/* Service worker: caches the app shell and visited pages so question text can be read offline.
   Running code still needs the server. Bump VERSION to drop old caches after a change here. */
const VERSION = 'v1';
const STATIC = 'static-' + VERSION;
const PAGES = 'pages-' + VERSION;
const SHELL = ['/static/style.css', '/static/editor.js', '/static/javalang.js', '/static/tabs.js',
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
    // network first so progress is always current; fall back to the last copy when offline
    e.respondWith(fetch(req).then((r) => {
      if (r.ok && !r.redirected && /^\/(day\/|dashboard)/.test(url.pathname)) {
        const copy = r.clone();
        caches.open(PAGES).then((c) => c.put(req, copy));
      }
      return r;
    }).catch(async () => (await caches.match(req)) || new Response(
      '<!doctype html><meta name="viewport" content="width=device-width,initial-scale=1"><title>Offline</title>' +
      '<body style="font-family:system-ui;padding:24px"><h1>You are offline</h1>' +
      '<p>This page has not been saved on this device yet. Reconnect and open it once to read it offline.</p></body>',
      { status: 503, headers: { 'Content-Type': 'text/html; charset=utf-8' } })));
  }
});
