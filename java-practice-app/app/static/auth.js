/* Small helpers for the login, signup, forgot-password and reset pages:
   show/hide password, a Caps Lock warning, a "did you mean gmail.com?" hint for typos in the email, and a guard
   against double-submitting the form. Everything is optional: the forms work without this script. */
(function () {
  'use strict';

  const el = (tag, cls, text) => {
    const e = document.createElement(tag);
    if (cls) e.className = cls;
    if (text !== undefined) e.textContent = text;
    return e;
  };

  /* ---- show / hide password + Caps Lock warning ---- */
  document.querySelectorAll('input[type="password"]').forEach((input) => {
    const wrap = el('span', 'pw-wrap');
    input.parentNode.insertBefore(wrap, input);
    wrap.appendChild(input);
    const toggle = el('button', 'pw-toggle', 'Show');
    toggle.type = 'button';
    toggle.setAttribute('aria-label', 'Show password');
    toggle.setAttribute('aria-pressed', 'false');
    toggle.addEventListener('click', () => {
      const show = input.type === 'password';
      input.type = show ? 'text' : 'password';
      toggle.textContent = show ? 'Hide' : 'Show';
      toggle.setAttribute('aria-label', show ? 'Hide password' : 'Show password');
      toggle.setAttribute('aria-pressed', show ? 'true' : 'false');
      input.focus();
    });
    wrap.appendChild(toggle);
    const caps = el('span', 'caps-warn', 'Caps Lock is on');
    caps.setAttribute('role', 'status');
    caps.hidden = true;
    wrap.parentNode.insertBefore(caps, wrap.nextSibling);
    const check = (e) => {
      caps.hidden = !(e.getModifierState && e.getModifierState('CapsLock'));
    };
    input.addEventListener('keydown', check);
    input.addEventListener('keyup', check);
    input.addEventListener('blur', () => { caps.hidden = true; });
  });

  /* ---- "did you mean ...?" for common email typos ---- */
  const COMMON = ['gmail.com', 'yahoo.com', 'outlook.com', 'hotmail.com', 'icloud.com', 'yahoo.in',
    'rediffmail.com', 'live.com', 'proton.me', 'protonmail.com'];
  // real domains that sit close to a common one, so they must never be "corrected"
  const ALSO_REAL = ['mail.com', 'yahoo.co.in', 'yahoo.co.uk', 'outlook.in', 'hotmail.co.uk', 'msn.com', 'me.com',
    'aol.com', 'gmx.com', 'zoho.com', 'yandex.com', 'pm.me', 'ymail.com', 'rocketmail.com', 'live.in',
    'googlemail.com', 'hotmail.in', 'outlook.co.in'];

  // edit distance where swapping two neighbouring letters counts as one change (gmial -> gmail)
  function distance(a, b) {
    const d = [];
    for (let i = 0; i <= a.length; i++) { d[i] = [i]; }
    for (let j = 0; j <= b.length; j++) { d[0][j] = j; }
    for (let i = 1; i <= a.length; i++) {
      for (let j = 1; j <= b.length; j++) {
        const cost = a[i - 1] === b[j - 1] ? 0 : 1;
        d[i][j] = Math.min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + cost);
        if (i > 1 && j > 1 && a[i - 1] === b[j - 2] && a[i - 2] === b[j - 1]) d[i][j] = Math.min(d[i][j], d[i - 2][j - 2] + 1);
      }
    }
    return d[a.length][b.length];
  }

  function suggest(value) {
    const m = /^([^@\s]+)@([^@\s]+)$/.exec(value.trim());
    if (!m) return null;
    const domain = m[2].toLowerCase();
    if (COMMON.includes(domain) || ALSO_REAL.includes(domain)) return null;
    const hit = COMMON.find((c) => distance(domain, c) <= 1);
    return hit ? m[1] + '@' + hit : null;
  }

  document.querySelectorAll('input[type="email"]').forEach((input) => {
    const hint = el('p', 'email-hint');
    hint.setAttribute('role', 'status');
    hint.hidden = true;
    const label = input.closest('label');
    (label || input).parentNode.insertBefore(hint, (label || input).nextSibling);
    const show = () => {
      const fix = suggest(input.value);
      hint.textContent = '';
      if (!fix) { hint.hidden = true; return; }
      hint.appendChild(document.createTextNode('Did you mean '));
      const use = el('button', 'link', fix);
      use.type = 'button';
      use.addEventListener('click', () => {
        input.value = fix;
        hint.hidden = true;
        const next = input.form && input.form.querySelector('input[type="password"]');
        (next || input).focus();
      });
      hint.appendChild(use);
      hint.appendChild(document.createTextNode('?'));
      hint.hidden = false;
    };
    input.addEventListener('blur', show);
    input.addEventListener('input', () => { hint.hidden = true; });
  });

  /* ---- no double submits ---- */
  document.querySelectorAll('.auth-card form').forEach((form) => {
    const button = form.querySelector('button.primary');
    if (!button) return;
    const path = (form.getAttribute('action') || '').split('/')[1] || '';
    const busy = { signup: 'Creating account…', login: 'Logging in…', forgot: 'Sending…', reset: 'Saving…' }[path] || 'Please wait…';
    const label = button.textContent;
    form.addEventListener('submit', (e) => {
      if (form.dataset.busy) { e.preventDefault(); return; }
      form.dataset.busy = '1';
      setTimeout(() => { button.disabled = true; button.textContent = busy; }, 0);
    });
    // coming back with the browser's Back button must not leave the form stuck
    window.addEventListener('pageshow', (e) => {
      if (!e.persisted) return;
      delete form.dataset.busy;
      button.disabled = false;
      button.textContent = label;
    });
  });
})();
