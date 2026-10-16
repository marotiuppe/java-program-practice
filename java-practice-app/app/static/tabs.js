/* Phone layout: Problem / Code / Output tabs. On wide screens the CSS ignores them and shows everything. */
(function () {
  const split = document.getElementById('split');
  const bar = document.getElementById('tabs');
  if (!split || !bar) return;
  function showTab(name) {
    split.dataset.tab = name;
    bar.querySelectorAll('button').forEach((b) => {
      const on = b.dataset.tab === name;
      b.classList.toggle('on', on);
      b.setAttribute('aria-selected', on ? 'true' : 'false');
      b.tabIndex = on ? 0 : -1;
    });
    window.scrollTo({ top: 0 });
  }
  bar.addEventListener('click', (e) => {
    const b = e.target.closest('button[data-tab]');
    if (b) showTab(b.dataset.tab);
  });
  // Arrow keys move between tabs (standard tab pattern)
  bar.addEventListener('keydown', (e) => {
    const tabs = Array.from(bar.querySelectorAll('button[role=tab]'));
    const i = tabs.indexOf(document.activeElement);
    if (i < 0 || !['ArrowLeft', 'ArrowRight', 'Home', 'End'].includes(e.key)) return;
    e.preventDefault();
    const next = e.key === 'Home' ? 0 : e.key === 'End' ? tabs.length - 1
      : (i + (e.key === 'ArrowRight' ? 1 : tabs.length - 1)) % tabs.length;
    tabs[next].focus();
    showTab(tabs[next].dataset.tab);
  });
  // Results appear on the Output tab; on desktop this only flips an unused attribute.
  window.showTab = function (name) { if (window.matchMedia('(max-width: 700px)').matches) showTab(name); };
})();
