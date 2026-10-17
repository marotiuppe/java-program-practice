/* Shown inside the "Waking up the server" screen (built by sw.js) while a sleeping free server starts.
   Everything here works without the server: your last-known progress (saved on this device by pwa.js), a
   Java quickfire game, what is new (static/whats-new.json, edit it when you ship something) and a tip. */
(function () {
  'use strict';
  const root = document.getElementById('jp-extra');
  if (!root) return;

  const TIPS = [
    'Read compiler errors from the top: the first one is usually the real problem and the rest are knock-on effects.',
    'Name variables for what they hold (totalMarks, not t). Future you will say thanks.',
    'Use equals() to compare Strings. == compares whether two names point at the same object.',
    'Array and String indexes start at 0, so the last index is length - 1.',
    'If a loop never ends, check that something inside it changes the loop condition.',
    'Print intermediate values with System.out.println while debugging. It is the fastest way to see what your code really does.',
    'Integer division throws away the fraction. Make one side a double (10 / 4.0) when you need 2.5.',
    'Small steps win: five questions a day beats fifty questions once a month.',
  ];

  /* Every answer below was checked by compiling and running the snippet. */
  const QUESTIONS = [
  {
    "code": "System.out.println(10 / 4);",
    "options": [
      "2.5",
      "2",
      "3"
    ],
    "answer": "2",
    "why": "Both numbers are ints, so Java drops the fraction: integer division."
  },
  {
    "code": "System.out.println(\"5\" + 3 + 2);",
    "options": [
      "532",
      "10",
      "55"
    ],
    "answer": "532",
    "why": "Once a String is involved, + joins text from left to right: \"5\" + 3 is \"53\", then + 2 gives \"532\"."
  },
  {
    "code": "System.out.println(1 + 2 + \"3\");",
    "options": [
      "33",
      "123",
      "6"
    ],
    "answer": "33",
    "why": "Left to right: 1 + 2 is 3 (a number), and only then is \"3\" joined on."
  },
  {
    "code": "int x = 5;\nSystem.out.println(x++ + ++x);",
    "options": [
      "10",
      "12",
      "11"
    ],
    "answer": "12",
    "why": "x++ gives 5 (then x is 6), ++x makes x 7 and gives 7. 5 + 7 = 12."
  },
  {
    "code": "String a = \"hi\";\nString b = new String(\"hi\");\nSystem.out.println(a == b);",
    "options": [
      "true",
      "false",
      "compile error"
    ],
    "answer": "false",
    "why": "== compares references, and new String(...) creates a separate object. Use equals() to compare text."
  },
  {
    "code": "System.out.println('a' + 1);",
    "options": [
      "a1",
      "b",
      "98"
    ],
    "answer": "98",
    "why": "A char is a number in arithmetic: 'a' is 97, so 'a' + 1 is the int 98."
  },
  {
    "code": "System.out.println(0.1 + 0.2 == 0.3);",
    "options": [
      "true",
      "false",
      "0.3"
    ],
    "answer": "false",
    "why": "0.1 and 0.2 cannot be stored exactly in binary, so the sum is 0.30000000000000004."
  },
  {
    "code": "System.out.println(\"Java\".charAt(2));",
    "options": [
      "a",
      "v",
      "J"
    ],
    "answer": "v",
    "why": "Indexes start at 0: J=0, a=1, v=2, a=3."
  },
  {
    "code": "System.out.println(7 % 3 + 7 / 3);",
    "options": [
      "3",
      "4",
      "2"
    ],
    "answer": "3",
    "why": "7 % 3 is the remainder 1 and 7 / 3 is 2 (integer division). 1 + 2 = 3."
  },
  {
    "code": "System.out.println((int) 3.99);",
    "options": [
      "4",
      "3",
      "3.99"
    ],
    "answer": "3",
    "why": "Casting a double to int cuts off the decimals; it does not round."
  },
  {
    "code": "StringBuilder sb = new StringBuilder(\"abc\");\nsb.reverse();\nSystem.out.println(sb);",
    "options": [
      "abc",
      "cba",
      "bca"
    ],
    "answer": "cba",
    "why": "reverse() flips the characters in place."
  },
  {
    "code": "System.out.println(Integer.MAX_VALUE + 1);",
    "options": [
      "2147483648",
      "-2147483648",
      "compile error"
    ],
    "answer": "-2147483648",
    "why": "int overflows silently and wraps around to the smallest int."
  },
  {
    "code": "for (int i = 0; i < 3; i++) {\n    System.out.print(i);\n}",
    "options": [
      "123",
      "012",
      "0123"
    ],
    "answer": "012",
    "why": "i starts at 0 and the loop stops before it reaches 3."
  },
  {
    "code": "System.out.println(\"hello\".substring(1, 3));",
    "options": [
      "he",
      "el",
      "ell"
    ],
    "answer": "el",
    "why": "substring(1, 3) starts at index 1 and stops before index 3."
  },
  {
    "code": "boolean b = false;\nif (b = true) {\n    System.out.println(\"yes\");\n} else {\n    System.out.println(\"no\");\n}",
    "options": [
      "yes",
      "no",
      "compile error"
    ],
    "answer": "yes",
    "why": "A single = assigns. b becomes true, and that value is the condition."
  },
  {
    "code": "System.out.println(10 == 10.0);",
    "options": [
      "true",
      "false",
      "compile error"
    ],
    "answer": "true",
    "why": "The int is widened to a double before comparing, so 10 == 10.0 is true."
  }
];

  const el = (tag, cls, text) => {
    const e = document.createElement(tag);
    if (cls) e.className = cls;
    if (text !== undefined) e.textContent = text;
    return e;
  };
  const read = (key) => { try { return JSON.parse(localStorage.getItem(key) || 'null'); } catch (e) { return null; } };
  const ago = (t) => {
    const m = Math.max(0, Math.round((Date.now() - t) / 60000));
    if (m < 2) return 'just now';
    if (m < 90) return m + ' minutes ago';
    const h = Math.round(m / 60);
    if (h < 36) return h + ' hours ago';
    return Math.round(h / 24) + ' days ago';
  };
  const section = (title) => {
    const s = el('section', 'jp-card');
    s.appendChild(el('h2', '', title));
    root.appendChild(s);
    return s;
  };

  /* ---- your progress (last known, from this device) ---- */
  (function progress() {
    const snap = read('jp_snapshot'), last = read('jp_last');
    if (!snap) return;
    const s = section('Your progress');
    const row = el('div', 'jp-stats');
    const stats = [[snap.streak, 'day streak'], [snap.solved, 'questions solved']];
    if (snap.goal) stats.push([snap.solved_today || 0, 'solved today']);
    stats.forEach((x) => {
      if (x[0] === undefined || x[0] === null) return;
      const b = el('div', 'jp-stat');
      b.appendChild(el('b', '', String(x[0])));
      b.appendChild(el('span', '', x[1]));
      row.appendChild(b);
    });
    s.appendChild(row);
    if (snap.goal) {
      const bar = el('div', 'jp-bar');
      const fill = el('i');
      fill.style.width = Math.min(100, Math.round(100 * (snap.solved_today || 0) / snap.goal)) + '%';
      bar.appendChild(fill);
      s.appendChild(bar);
    }
    const bits = [];
    if (snap.xp !== undefined) bits.push('Learner level ' + snap.learner + ' · ' + snap.xp + ' XP');
    if (snap.badges !== undefined) bits.push(snap.badges + ' badge' + (snap.badges === 1 ? '' : 's') + ' earned');
    if (bits.length) s.appendChild(el('p', 'jp-line', bits.join(' · ')));
    if (snap.next) s.appendChild(el('p', 'jp-line', 'Up next: Day ' + snap.next.day + ', Question ' + snap.next.q));
    if (last && last.title) s.appendChild(el('p', 'jp-line', 'Latest achievement: ' + last.title + ' (' + ago(last.t) + ')'));
    if (snap.t) s.appendChild(el('p', 'jp-note', 'As of ' + ago(snap.t) + '. It refreshes when the server is back.'));
  })();

  /* ---- top 10 this week (last known, saved when you last opened the dashboard) ---- */
  (function board() {
    const snap = read('jp_snapshot');
    const b = snap && snap.board;
    if (!b || !b.top || !b.top.length) return;
    const s = section('Top 10 this week');
    const ol = el('ol', 'jp-board');
    const medals = ['🥇', '🥈', '🥉'];
    b.top.forEach((r, i) => {
      const li = el('li');
      if (b.me && b.me.rank === i + 1) li.className = 'me';
      li.appendChild(el('span', 'rk', medals[i] || String(i + 1)));
      li.appendChild(el('span', 'nm', r.h));
      li.appendChild(el('span', 'pt', r.n + ' solved'));
      ol.appendChild(li);
    });
    s.appendChild(ol);
    if (b.me && b.me.rank > 10) s.appendChild(el('p', 'jp-line', 'You are #' + b.me.rank + ' with ' + b.me.n + ' solved.'));
    if (!b.on) s.appendChild(el('p', 'jp-note', 'Turn on the leaderboard in your account settings to appear here.'));
    if (snap.t) s.appendChild(el('p', 'jp-note', 'As of ' + ago(snap.t) + '. Solve a few questions and climb.'));
  })();

  /* ---- quickfire game ---- */
  (function game() {
    if (!QUESTIONS.length) return;
    const s = section('Java quickfire while you wait');
    const order = QUESTIONS.map((q, i) => i).sort(() => Math.random() - 0.5);
    let pos = 0, right = 0, streak = 0, best = 0, answered = 0;
    const score = el('p', 'jp-note');
    const body = el('div');
    s.appendChild(score);
    s.appendChild(body);
    const showScore = () => {
      score.textContent = answered ? right + ' of ' + answered + ' right · best streak ' + best : 'What does this program print?';
    };
    const next = () => {
      body.textContent = '';
      const q = QUESTIONS[order[pos % order.length]];
      pos++;
      const pre = el('pre', 'jp-code');
      pre.appendChild(el('code', '', q.code));
      body.appendChild(pre);
      const opts = el('div', 'jp-opts');
      const feedback = el('p', 'jp-feedback');
      feedback.setAttribute('aria-live', 'polite');
      const nextBtn = el('button', 'jp-btn primary', 'Next question');
      nextBtn.type = 'button';
      nextBtn.hidden = true;
      nextBtn.addEventListener('click', next);
      q.options.slice().sort(() => Math.random() - 0.5).forEach((o) => {
        const b = el('button', 'jp-btn', o);
        b.type = 'button';
        b.addEventListener('click', () => {
          const ok = o === q.answer;
          answered++;
          if (ok) { right++; streak++; best = Math.max(best, streak); } else { streak = 0; }
          opts.querySelectorAll('button').forEach((x) => {
            x.disabled = true;
            if (x.textContent === q.answer) x.classList.add('good');
          });
          if (!ok) b.classList.add('bad');
          feedback.textContent = (ok ? 'Correct! ' : 'Not quite. The answer is ' + q.answer + '. ') + q.why;
          nextBtn.hidden = false;
          nextBtn.focus();
          showScore();
        });
        opts.appendChild(b);
      });
      body.appendChild(opts);
      body.appendChild(feedback);
      body.appendChild(nextBtn);
    };
    showScore();
    next();
  })();

  /* ---- what is new (the same list as the login page: /static/whats-new.json, saved by the service worker) ---- */
  (function news() {
    fetch('/static/whats-new.json').then((r) => (r.ok ? r.json() : [])).then((items) => {
      if (!items || !items.length) return;
      const s = section('What is new');
      const ul = el('ul', 'jp-news');
      items.slice(0, 6).forEach((n) => {
        const li = el('li');
        li.appendChild(el('b', '', n.title + ': '));
        li.appendChild(document.createTextNode(n.text));
        ul.appendChild(li);
      });
      s.appendChild(ul);
      const tipCard = root.querySelector('[data-tip]');
      if (tipCard) root.appendChild(tipCard);  // keep the tip last
    }).catch(() => {});
  })();

  (function tip() {
    const s = section('Tip');
    s.setAttribute('data-tip', '1');
    const p = el('p', 'jp-line');
    let i = Math.floor(Math.random() * TIPS.length);
    const show = () => { p.textContent = TIPS[i++ % TIPS.length]; };
    show();
    s.appendChild(p);
    setInterval(show, 12000);
  })();

  /* ---- called by the screen when the server answers /health ---- */
  window.jpReady = function () {
    if (document.querySelector('.jp-ready')) return;
    const bar = el('div', 'jp-ready');
    bar.setAttribute('role', 'status');
    const msg = el('span', '', 'The server is ready!');
    const open = el('button', 'jp-btn primary', 'Open now');
    const stay = el('button', 'jp-btn', 'Keep playing');
    open.type = 'button';
    stay.type = 'button';
    let n = 10, timer;
    const tick = () => {
      msg.textContent = 'The server is ready! Opening in ' + n + 's';
      if (n-- <= 0) location.reload();
    };
    open.addEventListener('click', () => location.reload());
    stay.addEventListener('click', () => {
      clearInterval(timer);
      msg.textContent = 'The server is ready. Reload when you like.';
      stay.remove();
    });
    bar.append(msg, open, stay);
    document.body.appendChild(bar);
    tick();
    timer = setInterval(tick, 1000);
  };
})();
