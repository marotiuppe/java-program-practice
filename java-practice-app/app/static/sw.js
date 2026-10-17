/* Service worker: caches the app shell and visited pages so question text can be read offline.
   Running code still needs the server. Bump VERSION to drop old caches after a change here. */
const VERSION = 'v11';
const STATIC = 'static-' + VERSION;
const PAGES = 'pages-' + VERSION;

self.addEventListener('install', (e) => {
  // assets are cached as pages request them (see the fetch handler); the waiting-screen script is saved up front
  // because it must be available when the server is asleep
  e.waitUntil(Promise.all([
    caches.open(STATIC).then((c) => c.addAll(['/static/waiting.js', '/static/whats-new.json'])).catch(() => {}),
    self.skipWaiting(),
  ]));
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
    // ?v=<hash> URLs never change: serve from cache, fetch only once. Others: stale-while-revalidate.
    const versioned = url.searchParams.has('v');
    e.respondWith(caches.open(STATIC).then(async (cache) => {
      const hit = await cache.match(req);
      if (hit && versioned) return hit;
      const fresh = fetch(req).then((r) => { if (r.ok) cache.put(req, r.clone()); return r; }).catch(() => hit);
      return hit || fresh;
    }));
    return;
  }

  if (req.mode === 'navigate') {
    // network first so progress is always current; fall back to the last copy when offline.
    // If the page takes more than WAKE_AFTER_MS, probe /health. Only when the probe also fails or hangs
    // (a sleeping free instance) show a friendly "waking up" page that reloads once the server answers.
    // A server that answers /health but is just slow keeps waiting for the real page.
    const network = fetch(req).then((r) => {
      if (r.ok && !r.redirected && /^\/(day\/|dashboard)/.test(url.pathname)) {
        const copy = r.clone();
        caches.open(PAGES).then((c) => c.put(req, copy));
      }
      return r;
    });
    const wake = new Promise((resolve) => setTimeout(async () => {
      const ctl = new AbortController();
      const timer = setTimeout(() => ctl.abort(), PROBE_MS);
      const awake = await fetch('/health', { cache: 'no-store', signal: ctl.signal })
        .then((r) => r.ok).catch(() => false);
      clearTimeout(timer);
      if (!awake) resolve(wakingPage());  // otherwise never resolve: the network response wins the race
    }, WAKE_AFTER_MS));
    e.respondWith(Promise.race([network, wake]).catch(async () => (await caches.match(req)) || offlinePage()));
  }
});

const WAKE_AFTER_MS = 6000;
const PROBE_MS = 4000;
const html = (body, status) => new Response(body, {
  status: status || 200, headers: { 'Content-Type': 'text/html; charset=utf-8', 'Cache-Control': 'no-store' },
});

function offlinePage() {
  return html('<!doctype html><meta name="viewport" content="width=device-width,initial-scale=1"><title>Offline</title>' +
    '<body style="font-family:system-ui;padding:24px"><h1>You are offline</h1>' +
    '<p>This page has not been saved on this device yet. Reconnect and open it once to read it offline.</p></body>', 503);
}

function wakingPage() {
  // Shown while a sleeping free server starts. The timer and the "server is ready" check are inline so they work
  // even if /static/waiting.js is missing; that script adds your progress, a Java game, news and tips.
  return html(`<!doctype html><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1">
<title>Waking up…</title>
<style>
:root{--bg:#f5f6fb;--fg:#1a1d2e;--muted:#586174;--card:#fff;--line:#e3e6f0;--accent:#4f46e5;--accent-fg:#fff;--ok:#16874a;--bad:#c0392b}
@media (prefers-color-scheme:dark){:root{--bg:#0f1220;--fg:#e8eaf4;--muted:#9aa3b8;--card:#181c2e;--line:#2a3047;--accent:#818cf8;--accent-fg:#0f1220;--ok:#4cc27f;--bad:#ff7b6b}}
*{box-sizing:border-box}body{margin:0;background:var(--bg);color:var(--fg);font:16px/1.5 system-ui,-apple-system,"Segoe UI",Roboto,sans-serif}
.wrap{max-width:560px;margin:0 auto;padding:20px 16px 90px}
.hero{text-align:center;padding:18px 0 6px}.hero h1{margin:0 0 4px;font-size:24px}.hero p{margin:4px 0;color:var(--muted)}
#t{font-size:34px;font-weight:700;margin:6px 0}
.sleep{height:6px;border-radius:99px;background:var(--line);overflow:hidden;margin:10px auto;max-width:320px}
.sleep i{display:block;height:100%;width:35%;border-radius:99px;background:var(--accent);animation:slide 1.6s ease-in-out infinite}
@keyframes slide{0%{margin-left:-35%}100%{margin-left:100%}}
@media (prefers-reduced-motion:reduce){.sleep i{animation:none;width:100%;opacity:.5}}
.jp-card{background:var(--card);border:1px solid var(--line);border-radius:14px;padding:16px;margin:14px 0}
.jp-card h2{margin:0 0 8px;font-size:17px}
.jp-stats{display:grid;grid-template-columns:repeat(3,1fr);gap:8px}.jp-stat{background:var(--bg);border-radius:10px;padding:8px;text-align:center}
.jp-stat b{display:block;font-size:22px;color:var(--accent)}.jp-stat span{font-size:12px;color:var(--muted)}
.jp-bar{height:8px;border-radius:99px;background:var(--line);margin:10px 0;overflow:hidden}.jp-bar i{display:block;height:100%;background:var(--accent)}
.jp-board{list-style:none;margin:0;padding:0}.jp-board li{display:flex;gap:10px;align-items:center;padding:7px 8px;border-radius:8px}
.jp-board li.me{background:color-mix(in srgb,var(--accent) 16%,var(--card))}.jp-board .rk{width:28px;text-align:center;font-weight:700}
.jp-board .nm{flex:1;min-width:0;overflow:hidden;text-overflow:ellipsis;white-space:nowrap}.jp-board .pt{font-weight:700;color:var(--accent)}
.jp-line{margin:6px 0}.jp-note{margin:6px 0;font-size:13px;color:var(--muted)}
.jp-code{margin:8px 0;padding:12px;border-radius:10px;background:var(--bg);border:1px solid var(--line);overflow:auto;font:14px/1.45 ui-monospace,Consolas,monospace;white-space:pre}
.jp-opts{display:grid;grid-template-columns:repeat(3,1fr);gap:8px}
.jp-btn{font:inherit;font-weight:600;min-height:44px;padding:8px 10px;border:1px solid var(--line);border-radius:10px;background:var(--card);color:var(--fg);cursor:pointer}
.jp-btn.primary{background:var(--accent);border-color:var(--accent);color:var(--accent-fg)}
.jp-btn.good{border-color:var(--ok);background:color-mix(in srgb,var(--ok) 18%,var(--card))}.jp-btn.bad{border-color:var(--bad);background:color-mix(in srgb,var(--bad) 18%,var(--card))}
.jp-btn:disabled{cursor:default;color:var(--fg);opacity:1}
.jp-feedback{margin:10px 0 6px;min-height:1.4em}
.jp-news{margin:0;padding-left:18px}.jp-news li{margin:6px 0}
.jp-ready{position:fixed;left:0;right:0;bottom:0;display:flex;gap:8px;align-items:center;justify-content:center;flex-wrap:wrap;padding:12px 16px;background:var(--card);border-top:2px solid var(--ok);box-shadow:0 -6px 20px rgba(0,0,0,.15)}
</style>
<body><div class="wrap">
<div class="hero"><h1>Waking up the server…</h1>
<p>The free server naps when nobody is using it. It usually takes under a minute.</p>
<p id="t">0s</p><div class="sleep" aria-hidden="true"><i></i></div>
<p id="m">Your progress is safe. Play a round while you wait.</p></div>
<main id="jp-extra"></main></div>
<script>
var n=0,t=document.getElementById("t");setInterval(function(){n++;t.textContent=n+"s"},1000);
var done=false;
function poll(){if(done)return;fetch("/health",{cache:"no-store"}).then(function(r){
  if(!r.ok){setTimeout(poll,3000);return}
  done=true;if(window.jpReady)window.jpReady();else location.reload()})
 .catch(function(){setTimeout(poll,3000)})}
poll();
setTimeout(function(){document.getElementById("m").textContent="Taking longer than usual. Still trying…"},90000);
</script>
<script src="/static/waiting.js"></script></body>`);
}
