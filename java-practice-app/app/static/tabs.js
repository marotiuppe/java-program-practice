/* Phone layout: Problem / Code / Output tabs. On wide screens the CSS ignores them and shows everything. */
(function () {
  const split = document.getElementById('split');
  const bar = document.getElementById('tabs');
  if (!split || !bar) return;
  function showTab(name) {
    split.dataset.tab = name;
    bar.querySelectorAll('button').forEach((b) => b.classList.toggle('on', b.dataset.tab === name));
    window.scrollTo({ top: 0 });
  }
  bar.addEventListener('click', (e) => {
    const b = e.target.closest('button[data-tab]');
    if (b) showTab(b.dataset.tab);
  });
  // Results appear on the Output tab; on desktop this only flips an unused attribute.
  window.showTab = function (name) { if (window.matchMedia('(max-width: 700px)').matches) showTab(name); };
})();
