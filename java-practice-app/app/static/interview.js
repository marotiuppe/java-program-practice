/* Mock interview page: timer, tabs, autosave, run, samples, submit. Everything typed is also sent to the server
   every few seconds, so closing the tab or losing the connection does not lose answers. */
(function () {
  'use strict';
  const IV = JSON.parse(document.getElementById('iv-data').textContent);
  const offset = IV.now - Date.now() / 1000;  // the server's clock minus ours
  const left = () => Math.max(0, Math.round(IV.deadline - (Date.now() / 1000 + offset)));
  const $ = (sel, root) => (root || document).querySelector(sel);
  const $$ = (sel, root) => Array.from((root || document).querySelectorAll(sel));
  const timerEl = $('#iv-timer'), savedEl = $('#iv-saved'), announce = $('#iv-announce'), submitBtn = $('#iv-submit');
  const safe = (fn) => { try { return fn(); } catch (e) { return null; } };
  const lsKey = 'iv:' + IV.id;
  let finishing = false, current = 0;
  const dirty = new Set();

  /* ---- answers ---- */
  const field = (i, cls) => $('.' + cls + '[data-i="' + i + '"]');
  const answerOf = (i) => {
    const code = field(i, 'iv-code'), ans = field(i, 'iv-answer'), stdin = field(i, 'iv-stdin');
    const o = {};
    if (code) o.c = code.value;
    if (ans) o.a = ans.value;
    if (stdin) o.s = stdin.value;
    return o;
  };
  // a copy kept in this browser in case the connection drops; used only when the server has nothing yet
  const backup = safe(() => JSON.parse(localStorage.getItem(lsKey) || '{}')) || {};
  for (let i = 0; i < IV.n; i++) {
    const b = backup[i];
    if (!b) continue;
    [['iv-code', 'c'], ['iv-answer', 'a'], ['iv-stdin', 's']].forEach(([cls, k]) => {
      const f = field(i, cls);
      if (f && b[k] && !f.dataset.saved) f.value = b[k];  // the server has nothing for this question yet
    });
  }
  const writeBackup = () => safe(() => {
    const all = {};
    for (let i = 0; i < IV.n; i++) all[i] = answerOf(i);
    localStorage.setItem(lsKey, JSON.stringify(all));
  });

  async function post(url, body) {
    const r = await fetch(url, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body) });
    const data = await r.json().catch(() => ({}));
    if (r.status === 401) { location.href = '/login'; throw new Error('login'); }
    return data;
  }

  async function saveOne(i) {
    const data = await post('/api/interview/' + IV.id + '/save', Object.assign({ i: i }, answerOf(i)));
    if (data.finished || data.expired) { location.reload(); return; }
    dirty.delete(i);
  }
  async function flush() {
    const todo = Array.from(dirty);
    if (!todo.length) return;
    try {
      for (const i of todo) await saveOne(i);
      savedEl.textContent = 'Saved';
    } catch (e) { savedEl.textContent = 'Could not save, will retry'; }
  }
  const onEdit = (e) => {
    const i = +e.target.dataset.i;
    dirty.add(i);
    e.target.dataset.touched = '1';
    savedEl.textContent = '';
    writeBackup();
  };
  $$('.iv-code, .iv-answer, .iv-stdin').forEach((f) => f.addEventListener('input', onEdit));
  setInterval(flush, 8000);
  document.addEventListener('visibilitychange', () => {
    if (document.visibilityState === 'hidden' && dirty.size && navigator.sendBeacon) {
      dirty.forEach((i) => navigator.sendBeacon('/api/interview/' + IV.id + '/save',
        new Blob([JSON.stringify(Object.assign({ i: i }, answerOf(i)))], { type: 'application/json' })));
    }
  });

  /* ---- tabs ---- */
  function show(i) {
    current = i;
    $$('.iv-tab').forEach((t) => {
      const on = +t.dataset.i === i;
      t.classList.toggle('on', on);
      t.setAttribute('aria-selected', on ? 'true' : 'false');
    });
    $$('.iv-q').forEach((q) => { q.hidden = +q.dataset.i !== i; });
    flush();
  }
  $$('.iv-tab').forEach((t) => t.addEventListener('click', () => show(+t.dataset.i)));

  /* ---- editor niceties: Tab indents, Esc leaves, Ctrl+Enter runs ---- */
  $$('.iv-code').forEach((ta) => {
    let escaped = false;
    ta.addEventListener('keydown', (e) => {
      if (e.key === 'Escape') { escaped = true; return; }
      if (e.key === 'Tab' && !escaped && !e.shiftKey) {
        e.preventDefault();
        const s = ta.selectionStart;
        ta.value = ta.value.slice(0, s) + '    ' + ta.value.slice(ta.selectionEnd);
        ta.selectionStart = ta.selectionEnd = s + 4;
        onEdit({ target: ta });
      }
      if (e.key !== 'Escape') escaped = false;
      if ((e.ctrlKey || e.metaKey) && e.key === 'Enter') { e.preventDefault(); run(+ta.dataset.i); }
    });
  });

  /* ---- run and samples ---- */
  const out = (i) => $('#iv-out-' + i);
  async function run(i) {
    const o = out(i), btn = $('.iv-run[data-i="' + i + '"]');
    btn.disabled = true; o.className = 'output iv-out'; o.textContent = 'Compiling and running...';
    try {
      const res = await post('/api/run', { code: field(i, 'iv-code').value, stdin: field(i, 'iv-stdin').value });
      if (res.stage === 'compile') { o.className = 'output iv-out err'; o.textContent = 'Compilation failed:\n' + res.error; }
      else if (res.stage === 'run') {
        let t = res.stdout || '';
        if (res.stderr) t += (t && !t.endsWith('\n') ? '\n' : '') + res.stderr;
        o.className = 'output iv-out' + (res.ok ? '' : ' err');
        o.textContent = t || (res.ok ? '(no output)' : 'Program exited with code ' + res.exit_code);
      } else { o.className = 'output iv-out err'; o.textContent = res.error || 'Something went wrong.'; }
    } catch (e) { if (e.message !== 'login') { o.className = 'output iv-out err'; o.textContent = 'Could not reach the server. Try again in a moment.'; } }
    btn.disabled = false;
  }
  async function samples(i) {
    const o = out(i), btn = $('.iv-samples[data-i="' + i + '"]');
    btn.disabled = true; o.className = 'output iv-out'; o.textContent = 'Running the samples...';
    try {
      const res = await post('/api/interview/' + IV.id + '/test', { i: i, code: field(i, 'iv-code').value });
      if (res.stage === 'tests') {
        o.className = 'output iv-out' + (res.all_passed ? '' : ' err');
        o.textContent = res.results.map((r) => 'Sample ' + r.n + ': ' + (r.pass ? 'PASS' : 'FAIL') +
          (r.pass ? '' : '\n  expected: ' + JSON.stringify(r.expected) + '\n  got:      ' + JSON.stringify(r.actual) + (r.stderr ? '\n  ' + r.stderr : ''))).join('\n');
      } else if (res.stage === 'compile') { o.className = 'output iv-out err'; o.textContent = 'Compilation failed:\n' + res.error; }
      else { o.className = 'output iv-out err'; o.textContent = res.error || 'Something went wrong.'; }
    } catch (e) { if (e.message !== 'login') { o.className = 'output iv-out err'; o.textContent = 'Could not reach the server. Try again in a moment.'; } }
    btn.disabled = false;
  }
  $$('.iv-run').forEach((b) => b.addEventListener('click', () => run(+b.dataset.i)));
  $$('.iv-samples').forEach((b) => b.addEventListener('click', () => samples(+b.dataset.i)));

  /* ---- finishing ---- */
  async function finish(auto) {
    if (finishing) return;
    finishing = true;
    submitBtn.disabled = true;
    savedEl.textContent = auto ? 'Time is up. Submitting...' : 'Submitting...';
    const answers = {};
    for (let i = 0; i < IV.n; i++) answers[i] = answerOf(i);
    for (let attempt = 0; attempt < 40; attempt++) {
      try {
        const res = await post('/api/interview/' + IV.id + '/finish', { answers: answers });
        if (res.url) { safe(() => localStorage.removeItem(lsKey)); location.href = res.url; return; }
      } catch (e) { if (e.message === 'login') return; }
      savedEl.textContent = 'Could not submit yet. Retrying...';
      await new Promise((r) => setTimeout(r, 3000));
    }
    finishing = false; submitBtn.disabled = false; savedEl.textContent = 'Could not submit. Check your connection and press Submit.';
  }
  submitBtn.addEventListener('click', () => {
    if (confirm('Submit your answers now? You cannot change them afterwards.')) finish(false);
  });

  /* ---- the clock ---- */
  const said = {};
  const fmt = (s) => String(Math.floor(s / 60)).padStart(2, '0') + ':' + String(s % 60).padStart(2, '0');
  function tick() {
    const s = left();
    timerEl.textContent = fmt(s);
    timerEl.classList.toggle('warn', s <= 300 && s > 60);
    timerEl.classList.toggle('danger', s <= 60);
    [[300, '5 minutes left.'], [60, '1 minute left.']].forEach(([mark, msg]) => {
      if (s <= mark && !said[mark]) { said[mark] = true; announce.textContent = msg; }
    });
    if (s <= 0) finish(true);
  }
  tick();
  setInterval(tick, 500);
  window.addEventListener('beforeunload', (e) => { if (!finishing && dirty.size) { e.preventDefault(); e.returnValue = ''; } });
})();
