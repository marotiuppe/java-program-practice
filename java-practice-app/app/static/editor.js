(function () {
  const code = document.getElementById('code');
  const hl = document.getElementById('hl');
  const ac = document.getElementById('ac');
  const area = document.getElementById('code-area');
  const gutter = document.getElementById('gutter');
  const stdin = document.getElementById('stdin');
  const out = document.getElementById('output');
  const msg = document.getElementById('msg');
  const runBtn = document.getElementById('run');
  const solveBtn = document.getElementById('solve');
  const day = +code.dataset.day, q = +code.dataset.q, level = code.dataset.level;
  const draftKey = 'draft:' + level + ':' + day + ':' + q;
  const IND = '    ';
  const safe = (fn) => { try { return fn(); } catch (e) { return null; } };

  // The server (SQLite) can be wiped on the free plan, so keep a browser copy as backup.
  if (code.dataset.hasSaved === '0') {
    const d = safe(() => localStorage.getItem(draftKey));
    if (d) code.value = d;
  }

  /* ---------------- rendering: line numbers + syntax colors ---------------- */
  let draftTimer;
  function refresh() {
    hl.innerHTML = JavaLang.highlight(code.value);
    const n = code.value.split('\n').length;
    let s = '';
    for (let i = 1; i <= n; i++) s += i + '\n';
    gutter.textContent = s.trimEnd();
    syncScroll();
    clearTimeout(draftTimer);
    draftTimer = setTimeout(() => safe(() => localStorage.setItem(draftKey, code.value)), 400);
  }
  function syncScroll() {
    hl.scrollTop = code.scrollTop;
    hl.scrollLeft = code.scrollLeft;
    gutter.scrollTop = code.scrollTop;
  }

  /* ---------------- editing helpers ---------------- */
  let programmatic = false; // true while the editor itself changes the text (no suggestions then)

  function insertText(text) {
    programmatic = true;
    try {
      code.focus();
      // execCommand keeps the browser undo stack (Ctrl+Z) working.
      if (!document.execCommand('insertText', false, text)) {
        code.setRangeText(text, code.selectionStart, code.selectionEnd, 'end');
        code.dispatchEvent(new Event('input'));
      }
    } finally { programmatic = false; }
  }
  function setCaret(a, b) { code.setSelectionRange(a, b === undefined ? a : b); }
  function lineStartOf(v, pos) { return v.lastIndexOf('\n', pos - 1) + 1; }

  function indentLines(outdent) {
    const v = code.value;
    const s = code.selectionStart;
    let e = code.selectionEnd;
    if (e > s && v.charAt(e - 1) === '\n') e--;
    const ls = lineStartOf(v, s);
    let le = v.indexOf('\n', e);
    if (le < 0) le = v.length;
    const lines = v.slice(ls, le).split('\n');
    const res = lines.map((l) => (outdent ? l.replace(/^( {1,4}|\t)/, '') : IND + l)).join('\n');
    setCaret(ls, le);
    insertText(res);
    setCaret(ls, ls + res.length);
  }

  function enterKey() {
    const v = code.value, s = code.selectionStart, e = code.selectionEnd;
    const ls = lineStartOf(v, s);
    const lineBefore = v.slice(ls, s);
    const indent = /^[ \t]*/.exec(lineBefore)[0];
    const next = v.charAt(e);
    const opens = /[{(\[]\s*$/.test(lineBefore);
    const caseLine = /^\s*(case\b.*|default)\s*:\s*$/.test(lineBefore);
    if (opens && (next === '}' || next === ')' || next === ']')) {
      insertText('\n' + indent + IND + '\n' + indent);
      const pos = s + 1 + indent.length + IND.length;
      setCaret(pos);
    } else if (opens || caseLine) {
      insertText('\n' + indent + IND);
    } else {
      insertText('\n' + indent);
    }
    scrollCaretIntoView();
  }

  function scrollCaretIntoView() {
    const lh = lineHeight();
    const before = code.value.slice(0, code.selectionStart);
    const line = before.split('\n').length - 1;
    const top = line * lh, bottom = top + lh + 20;
    if (top < code.scrollTop) code.scrollTop = top;
    else if (bottom > code.scrollTop + code.clientHeight) code.scrollTop = bottom - code.clientHeight;
  }

  const PAIRS = { '(': ')', '[': ']', '{': '}', '"': '"', "'": "'" };
  const CLOSERS = new Set([')', ']', '}', '"', "'"]);

  function handlePairs(e) {
    const v = code.value, s = code.selectionStart, en = code.selectionEnd;
    const next = v.charAt(en), prev = v.charAt(s - 1);
    const k = e.key;
    if (s !== en && PAIRS[k]) {                       // wrap the selection
      e.preventDefault();
      const sel = v.slice(s, en);
      insertText(k + sel + PAIRS[k]);
      setCaret(s + 1, s + 1 + sel.length);
      return true;
    }
    if (s !== en) return false;
    if (CLOSERS.has(k) && next === k) {               // type over an existing closer
      e.preventDefault();
      setCaret(s + 1);
      return true;
    }
    if (k === '}' && /^ {4,}$/.test(v.slice(lineStartOf(v, s), s))) {   // outdent a lone closing brace
      e.preventDefault();
      setCaret(s - 4, s);
      insertText('}');
      return true;
    }
    if (PAIRS[k]) {
      const isQuote = k === '"' || k === "'";
      if (isQuote) {
        const lineBefore = v.slice(lineStartOf(v, s), s);
        if (/\w/.test(prev) || /\w/.test(next) || /\/\/.*$/.test(lineBefore)) return false;
      } else if (!/^[\s)\]};,]?$/.test(next)) return false;
      e.preventDefault();
      insertText(k + PAIRS[k]);
      setCaret(s + 1);
      return true;
    }
    return false;
  }

  function handleBackspace(e) {
    const v = code.value, s = code.selectionStart, en = code.selectionEnd;
    if (s !== en || s === 0) return false;
    const pair = v.charAt(s - 1) + v.charAt(s);
    if (pair === '()' || pair === '[]' || pair === '{}' || pair === '""' || pair === "''") {
      e.preventDefault();
      setCaret(s - 1, s + 1);
      programmatic = true;
      try { document.execCommand('delete'); } finally { programmatic = false; }
      return true;
    }
    return false;
  }

  /* ---------------- autocomplete popup ---------------- */
  let acState = null; // { start, prefix, items, sel }
  let charW = 0;

  function lineHeight() {
    return parseFloat(getComputedStyle(code).lineHeight) || 19.5;
  }
  function charWidth() {
    if (!charW) {
      const cs = getComputedStyle(code);
      const ctx = document.createElement('canvas').getContext('2d');
      ctx.font = cs.fontSize + ' ' + cs.fontFamily;
      charW = ctx.measureText('M').width || 7.8;
    }
    return charW;
  }
  function escHtml(s) { return s.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;'); }
  const KIND_LABEL = { var: 'v', snip: 's', type: 'C', kw: 'k', method: 'm', field: 'f' };

  function closeAc() {
    acState = null;
    ac.hidden = true;
  }

  function showSuggest(force) {
    if (code.selectionStart !== code.selectionEnd) return closeAc();
    const r = JavaLang.suggest(code.value, code.selectionStart, force);
    if (!r) return closeAc();
    acState = { start: r.start, prefix: r.prefix, items: r.items, sel: 0 };
    renderAc();
  }

  function renderAc() {
    const st = acState;
    ac.innerHTML = st.items.map((it, i) => {
      const p = st.prefix.length;
      return '<div class="item' + (i === st.sel ? ' sel' : '') + '" data-i="' + i + '">' +
        '<span class="kind kind-' + it.kind + '">' + KIND_LABEL[it.kind] + '</span>' +
        '<span class="lbl"><b>' + escHtml(it.label.slice(0, p)) + '</b>' + escHtml(it.label.slice(p)) + '</span>' +
        '<span class="det">' + escHtml(it.detail || '') + '</span></div>';
    }).join('');
    ac.hidden = false;
    positionAc();
    const sel = ac.querySelector('.sel');
    if (sel) sel.scrollIntoView({ block: 'nearest' });
  }

  function positionAc() {
    const cs = getComputedStyle(code);
    const lh = lineHeight(), cw = charWidth();
    const padL = parseFloat(cs.paddingLeft) || 0, padT = parseFloat(cs.paddingTop) || 0;
    const before = code.value.slice(0, acState.start);
    const line = before.split('\n').length - 1;
    const col = before.length - before.lastIndexOf('\n') - 1;
    let x = padL + col * cw - code.scrollLeft;
    let y = padT + (line + 1) * lh - code.scrollTop;
    const w = ac.offsetWidth, h = ac.offsetHeight;
    if (y + h > area.clientHeight) y = Math.max(0, y - lh - h);
    x = Math.max(0, Math.min(x, area.clientWidth - w - 4));
    ac.style.left = x + 'px';
    ac.style.top = y + 'px';
  }

  function acceptAc(i) {
    const st = acState;
    if (!st) return;
    const it = st.items[i];
    const v = code.value;
    const lineBefore = v.slice(lineStartOf(v, st.start), st.start);
    const indent = /^[ \t]*/.exec(lineBefore)[0];
    let text = it.insert.replace(/\n/g, '\n' + indent);
    const idx = text.indexOf('$0');
    text = text.replace('$0', '');
    closeAc();
    setCaret(st.start, code.selectionStart);
    insertText(text);
    if (idx >= 0) setCaret(st.start + idx);
    scrollCaretIntoView();
  }

  ac.addEventListener('mousedown', (e) => {
    e.preventDefault(); // keep focus in the editor
    const item = e.target.closest('.item');
    if (item) acceptAc(+item.dataset.i);
  });

  /* ---------------- events ---------------- */
  code.addEventListener('input', (e) => {
    refresh();
    if (programmatic) return;
    const typed = e.inputType === 'insertText' && e.data && /[\w$.]/.test(e.data);
    if (typed || (e.inputType === 'deleteContentBackward' && acState)) showSuggest(false);
    else closeAc();
  });
  code.addEventListener('scroll', () => {
    syncScroll();
    if (acState) positionAc();
  });
  code.addEventListener('blur', () => setTimeout(closeAc, 120));
  code.addEventListener('click', closeAc);

  code.addEventListener('keydown', (e) => {
    if (e.isComposing) return;
    const mod = e.ctrlKey || e.metaKey;
    if (mod && e.key === 'Enter') { e.preventDefault(); closeAc(); run(); return; }
    if (mod && e.code === 'Space') { e.preventDefault(); showSuggest(true); return; }

    if (acState) {
      if (e.key === 'ArrowDown' || e.key === 'ArrowUp') {
        e.preventDefault();
        const n = acState.items.length;
        acState.sel = (acState.sel + (e.key === 'ArrowDown' ? 1 : n - 1)) % n;
        renderAc();
        return;
      }
      if (e.key === 'Enter' || e.key === 'Tab') { e.preventDefault(); acceptAc(acState.sel); return; }
      if (e.key === 'Escape') { e.preventDefault(); closeAc(); return; }
      if (['ArrowLeft', 'ArrowRight', 'Home', 'End', 'PageUp', 'PageDown'].indexOf(e.key) !== -1) closeAc();
    }

    if (mod || e.altKey) return;
    if (e.key === 'Tab') {
      e.preventDefault();
      const multi = code.value.slice(code.selectionStart, code.selectionEnd).indexOf('\n') !== -1;
      if (e.shiftKey || multi) indentLines(e.shiftKey);
      else insertText(IND);
    } else if (e.key === 'Enter') {
      e.preventDefault();
      enterKey();
    } else if (e.key === 'Backspace') {
      handleBackspace(e);
    } else if (e.key.length === 1) {
      handlePairs(e);
    }
  });

  stdin.addEventListener('keydown', (e) => {
    if ((e.ctrlKey || e.metaKey) && e.key === 'Enter') { e.preventDefault(); run(); }
  });

  /* ---------------- server calls ---------------- */
  async function post(url, body) {
    const r = await fetch(url, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body) });
    if (r.status === 401) { location.href = '/login'; throw new Error('login'); }
    return r.json();
  }

  function flash(text) {
    msg.textContent = text;
    setTimeout(() => { if (msg.textContent === text) msg.textContent = ''; }, 2500);
  }

  function save(extra) {
    return post('/api/save', Object.assign({ day: day, q: q, code: code.value }, extra || {}));
  }

  const showOutput = () => { if (window.showTab) window.showTab('output'); };

  async function run() {
    showOutput();
    runBtn.disabled = true;
    out.className = 'output';
    out.textContent = 'Compiling and running...';
    try {
      const res = await post('/api/run', { code: code.value, stdin: stdin.value });
      if (res.stage === 'compile') {
        out.className = 'output err';
        out.textContent = 'Compilation failed:\n' + res.error;
      } else if (res.stage === 'run') {
        let text = res.stdout || '';
        if (res.stderr) text += (text && !text.endsWith('\n') ? '\n' : '') + res.stderr;
        out.className = 'output' + (res.ok ? '' : ' err');
        out.textContent = text || (res.ok ? '(no output)' : 'Program exited with code ' + res.exit_code);
      } else {
        out.className = 'output err';
        out.textContent = res.error || 'Something went wrong.';
      }
      if (res.stage === 'compile' || res.stage === 'run') save({ ran: true }).catch(() => {});
    } catch (e) {
      if (e.message !== 'login') {
        out.className = 'output err';
        out.textContent = 'Could not reach the server. It may be waking up - try again in a moment.';
      }
    } finally {
      runBtn.disabled = false;
    }
  }

  /* ---------------- sample tests (coding questions) ---------------- */
  function el(tag, cls, text) {
    const e = document.createElement(tag);
    if (cls) e.className = cls;
    if (text !== undefined) e.textContent = text;
    return e;
  }
  function textBlock(title, text) {
    const d = el('div');
    d.appendChild(el('h4', '', title));
    d.appendChild(el('pre', '', text === '' ? '(empty)' : text));
    return d;
  }
  function markSolvedUi() {
    solveBtn.dataset.status = 'solved';
    solveBtn.textContent = 'Mark unsolved';
  }

  const testBtn = document.getElementById('test');
  const testsBox = document.getElementById('tests');
  async function runSamples() {
    showOutput();
    testBtn.disabled = true;
    testsBox.hidden = false;
    testsBox.textContent = 'Compiling and running the samples...';
    try {
      const res = await post('/api/test', { day: day, q: q, code: code.value });
      testsBox.textContent = '';
      if (res.stage === 'compile') {
        testsBox.appendChild(el('div', 'summary bad', 'Compilation failed'));
        const pre = el('pre', 'output err', res.error);
        testsBox.appendChild(pre);
      } else if (res.stage !== 'tests') {
        testsBox.appendChild(el('div', 'summary bad', res.error || 'Something went wrong.'));
      } else {
        const passed = res.results.filter((r) => r.pass).length;
        const total = res.results.length;
        testsBox.appendChild(el('div', 'summary ' + (res.all_passed ? 'ok' : 'bad'),
          res.all_passed ? 'All ' + total + ' checks passed - marked solved.' : passed + ' of ' + total + ' checks passed'));
        let opened = false;
        res.results.forEach((r) => {
          const d = el('details');
          if (!r.pass && !opened) { d.open = true; opened = true; }
          const s = el('summary');
          s.appendChild(el('span', 'tag ' + (r.pass ? 'pass' : 'fail'), r.pass ? 'PASS' : 'FAIL'));
          s.appendChild(el('span', '', (r.hidden ? 'Hidden test ' : 'Sample ') + r.n));
          d.appendChild(s);
          const detail = el('div', 'detail');
          if (r.hidden) {
            if (!r.pass && !r.stderr) detail.appendChild(textBlock('Result', 'Your output did not match the expected output for this hidden test.'));
          } else {
            detail.appendChild(textBlock('Input', r.input.replace(/\n$/, '')));
            detail.appendChild(textBlock('Expected output', r.expected));
            detail.appendChild(textBlock('Your output', r.actual));
          }
          if (r.stderr) detail.appendChild(textBlock('Errors', r.stderr));
          d.appendChild(detail);
          testsBox.appendChild(d);
        });
        if (res.all_passed) { markSolvedUi(); flash('Marked solved'); }
      }
    } catch (e) {
      if (e.message !== 'login') testsBox.textContent = 'Could not reach the server. It may be waking up - try again in a moment.';
    } finally {
      testBtn.disabled = false;
    }
  }
  if (testBtn) testBtn.addEventListener('click', runSamples);

  /* ---------------- predict-the-output check ---------------- */
  const checkBtn = document.getElementById('check');
  if (checkBtn) {
    const answer = document.getElementById('predict');
    const pmsg = document.getElementById('predict-msg');
    const setMsg = (text, cls) => { pmsg.textContent = text; pmsg.className = 'pmsg ' + (cls || ''); };
    checkBtn.addEventListener('click', async () => {
      checkBtn.disabled = true;
      setMsg('Checking...');
      try {
        const res = await post('/api/predict', { day: day, q: q, answer: answer.value });
        if (res.error) setMsg(res.error, 'bad');
        else if (res.correct) { setMsg('Correct! Marked solved.', 'ok'); markSolvedUi(); }
        else setMsg('Not quite. Re-trace the code line by line and try again.', 'bad');
      } catch (e) {
        if (e.message !== 'login') setMsg('Could not reach the server.', 'bad');
      } finally {
        checkBtn.disabled = false;
      }
    });
    document.getElementById('reveal').addEventListener('click', async () => {
      if (!confirm('Show the real output? This counts as giving up on this question.')) return;
      try {
        const res = await post('/api/predict', { day: day, q: q, reveal: true });
        if (res.error) { setMsg(res.error, 'bad'); return; }
        out.className = 'output';
        out.textContent = 'Actual output:\n' + res.expected;
        setMsg('');
      } catch (e) {
        if (e.message !== 'login') setMsg('Could not reach the server.', 'bad');
      }
    });
  }

  runBtn.addEventListener('click', run);
  document.getElementById('save').addEventListener('click', async () => {
    try { await save(); flash('Saved'); } catch (e) { flash('Save failed'); }
  });
  solveBtn.addEventListener('click', async () => {
    const next = solveBtn.dataset.status === 'solved' ? 'attempted' : 'solved';
    try {
      await save({ status: next });
      solveBtn.dataset.status = next;
      solveBtn.textContent = next === 'solved' ? 'Mark unsolved' : 'Mark solved';
      flash(next === 'solved' ? 'Marked solved' : 'Marked unsolved');
    } catch (e) { flash('Failed'); }
  });
  document.getElementById('reset').addEventListener('click', () => {
    if (confirm('Replace your code with the starter code?')) {
      code.value = STARTER;
      refresh();
      safe(() => localStorage.removeItem(draftKey));
    }
  });

  /* ---------------- touch symbol toolbar ---------------- */
  // Shown only on touch devices (CSS). Keys go through the normal keydown handler so bracket
  // pairing, Tab and autocomplete behave exactly as on a hardware keyboard.
  const symbols = document.getElementById('symbols');
  function moveCaretLine(dir) {
    const v = code.value, pos = code.selectionStart;
    const ls = lineStartOf(v, pos), col = pos - ls;
    if (dir < 0) {
      if (ls === 0) return setCaret(0);
      const ps = lineStartOf(v, ls - 1);
      setCaret(ps + Math.min(col, ls - 1 - ps));
    } else {
      const le = v.indexOf('\n', pos);
      if (le < 0) return setCaret(v.length);
      let ne = v.indexOf('\n', le + 1);
      if (ne < 0) ne = v.length;
      setCaret(le + 1 + Math.min(col, ne - le - 1));
    }
  }
  function pressSymbol(key) {
    code.focus();
    if (key === 'undo') { document.execCommand('undo'); return; }
    if (key === 'ArrowLeft') return setCaret(Math.max(0, code.selectionStart - 1));
    if (key === 'ArrowRight') return setCaret(Math.min(code.value.length, code.selectionEnd + 1));
    if (key === 'ArrowUp') return moveCaretLine(-1);
    if (key === 'ArrowDown') return moveCaretLine(1);
    const ev = new KeyboardEvent('keydown', { key: key, bubbles: true, cancelable: true });
    if (code.dispatchEvent(ev) && key !== 'Tab') insertText(key);
  }
  if (symbols) {
    // pointerdown + preventDefault keeps focus in the editor so the on-screen keyboard stays open
    symbols.addEventListener('pointerdown', (e) => {
      const b = e.target.closest('button[data-k]');
      if (!b) return;
      e.preventDefault();
      pressSymbol(b.dataset.k);
    });
  }

  refresh();
})();
