/* Lightweight Java support for the editor: syntax highlighting + autocomplete suggestions.
   Pure functions only (no DOM) so they can be tested with Node. */
(function (root) {
  'use strict';

  const KEYWORDS = ('abstract assert boolean break byte case catch char class continue default do double else enum ' +
    'extends final finally float for if implements import instanceof int interface long new package private protected ' +
    'public return short static super switch synchronized this throw throws try void volatile while var record sealed ' +
    'permits yield true false null').split(' ');
  const KW = new Set(KEYWORDS);
  const CTRL = new Set('if else for while do switch case default break continue return try catch finally throw throws new yield'.split(' '));

  const TYPES = ('String Integer Double Long Float Short Byte Boolean Character Number Object Math System StringBuilder ' +
    'StringBuffer List ArrayList LinkedList Map HashMap TreeMap LinkedHashMap Set HashSet TreeSet LinkedHashSet Queue Deque ' +
    'ArrayDeque PriorityQueue Stack Vector Scanner Arrays Collections Objects Optional Random Iterator Iterable Comparable ' +
    'Comparator Exception RuntimeException Error Throwable IllegalArgumentException IllegalStateException ArithmeticException ' +
    'NullPointerException ArrayIndexOutOfBoundsException IndexOutOfBoundsException NumberFormatException ' +
    'InputMismatchException ClassCastException UnsupportedOperationException IOException FileNotFoundException File ' +
    'FileReader FileWriter BufferedReader BufferedWriter PrintWriter Thread Runnable Callable Stream IntStream Collectors ' +
    'Function Supplier Consumer Predicate BiFunction AtomicInteger AtomicLong ExecutorService Executors ' +
    'ConcurrentHashMap CompletableFuture Override FunctionalInterface SuppressWarnings').split(' ');

  /* ---------- syntax highlighting ---------- */
  const TOKEN = new RegExp(
    '(\\/\\/[^\\n]*|\\/\\*[\\s\\S]*?(?:\\*\\/|$))' +                       // 1 comment
    '|("""[\\s\\S]*?(?:"""|$)|"(?:\\\\.|[^"\\\\\\n])*"?|\'(?:\\\\.|[^\'\\\\\\n])*\'?)' + // 2 string / char
    '|(@[A-Za-z_]\\w*)' +                                                  // 3 annotation
    '|(\\b0[xX][0-9a-fA-F_]+[lL]?\\b|\\b\\d[\\d_]*(?:\\.\\d+)?(?:[eE][+-]?\\d+)?[LlFfDd]?\\b)' + // 4 number
    '|([A-Za-z_$][\\w$]*)' +                                               // 5 identifier
    '|([\\s\\S])', 'g');                                                   // 6 anything else

  function esc(s) {
    return s.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
  }
  function span(cls, text) {
    return '<span class="t-' + cls + '">' + esc(text) + '</span>';
  }

  function highlight(src) {
    let out = '';
    let m;
    TOKEN.lastIndex = 0;
    while ((m = TOKEN.exec(src)) !== null) {
      const t = m[0];
      if (m[1]) out += span('c', t);
      else if (m[2]) out += span('s', t);
      else if (m[3]) out += span('a', t);
      else if (m[4]) out += span('n', t);
      else if (m[5]) {
        if (CTRL.has(t)) out += span('kc', t);
        else if (KW.has(t)) out += span('k', t);
        else if (/^[A-Z][A-Z0-9_]+$/.test(t)) out += span('cn', t);
        else if (/^[A-Z]/.test(t)) out += span('ty', t);
        else if (src.charAt(TOKEN.lastIndex) === '(') out += span('f', t);
        else out += esc(t);
      } else out += esc(t);
    }
    return out + '\n';
  }

  /* ---------- member tables for dot-completion ---------- */
  // A leading "." marks a field (no parentheses); otherwise "name(params)".
  const M = {
    String: ['length()', 'charAt(index)', 'substring(begin, end)', 'indexOf(str)', 'lastIndexOf(str)', 'equals(other)',
      'equalsIgnoreCase(other)', 'compareTo(other)', 'contains(str)', 'startsWith(prefix)', 'endsWith(suffix)',
      'toUpperCase()', 'toLowerCase()', 'trim()', 'strip()', 'isEmpty()', 'isBlank()', 'split(regex)', 'replace(a, b)',
      'toCharArray()', 'concat(str)', 'repeat(count)', 'chars()', 'matches(regex)', 'hashCode()'],
    StringBuilder: ['append(x)', 'insert(index, x)', 'reverse()', 'toString()', 'length()', 'charAt(index)',
      'deleteCharAt(index)', 'delete(start, end)', 'setCharAt(index, ch)', 'setLength(len)', 'indexOf(str)',
      'replace(start, end, str)', 'isEmpty()'],
    List: ['add(item)', 'get(index)', 'set(index, item)', 'remove(x)', 'size()', 'isEmpty()', 'contains(x)', 'indexOf(x)',
      'clear()', 'addAll(other)', 'sort(cmp)', 'forEach(action)', 'stream()', 'subList(from, to)', 'toArray()',
      'iterator()', 'removeIf(filter)', 'addFirst(x)', 'addLast(x)', 'removeFirst()', 'removeLast()', 'getFirst()',
      'getLast()', 'peek()', 'poll()', 'push(x)', 'pop()'],
    Map: ['put(key, value)', 'get(key)', 'getOrDefault(key, def)', 'containsKey(key)', 'containsValue(value)',
      'remove(key)', 'size()', 'isEmpty()', 'keySet()', 'values()', 'entrySet()', 'putIfAbsent(key, value)',
      'merge(key, value, fn)', 'computeIfAbsent(key, fn)', 'forEach(action)', 'clear()', 'firstKey()', 'lastKey()'],
    Set: ['add(item)', 'remove(item)', 'contains(item)', 'size()', 'isEmpty()', 'clear()', 'addAll(other)',
      'retainAll(other)', 'removeAll(other)', 'iterator()', 'stream()', 'first()', 'last()'],
    Deque: ['push(x)', 'pop()', 'peek()', 'poll()', 'offer(x)', 'add(x)', 'addFirst(x)', 'addLast(x)', 'pollFirst()',
      'pollLast()', 'peekFirst()', 'peekLast()', 'isEmpty()', 'size()', 'clear()', 'contains(x)'],
    Scanner: ['nextInt()', 'nextLine()', 'next()', 'nextDouble()', 'nextLong()', 'nextBoolean()', 'hasNext()',
      'hasNextInt()', 'hasNextLine()', 'close()'],
    Array: ['.length', 'clone()'],
    Object: ['equals(other)', 'hashCode()', 'toString()', 'getClass()'],
    'Math': ['abs(x)', 'max(a, b)', 'min(a, b)', 'pow(a, b)', 'sqrt(x)', 'cbrt(x)', 'floor(x)', 'ceil(x)', 'round(x)',
      'random()', 'sin(x)', 'cos(x)', 'tan(x)', 'log(x)', 'log10(x)', 'floorMod(a, b)', 'hypot(a, b)', '.PI', '.E'],
    Integer: ['parseInt(s)', 'valueOf(x)', 'toString(n)', 'toBinaryString(n)', 'compare(a, b)', 'sum(a, b)', 'max(a, b)',
      'min(a, b)', '.MAX_VALUE', '.MIN_VALUE', 'bitCount(n)'],
    Long: ['parseLong(s)', 'valueOf(x)', 'compare(a, b)', '.MAX_VALUE', '.MIN_VALUE'],
    Double: ['parseDouble(s)', 'valueOf(x)', 'compare(a, b)', '.MAX_VALUE', '.MIN_VALUE', 'isNaN(x)'],
    Character: ['isDigit(c)', 'isLetter(c)', 'isLetterOrDigit(c)', 'isUpperCase(c)', 'isLowerCase(c)', 'toUpperCase(c)',
      'toLowerCase(c)', 'getNumericValue(c)', 'isWhitespace(c)'],
    Arrays: ['sort(arr)', 'toString(arr)', 'fill(arr, v)', 'asList(items)', 'copyOf(arr, n)', 'copyOfRange(arr, a, b)',
      'equals(a, b)', 'binarySearch(arr, key)', 'stream(arr)', 'deepToString(arr)'],
    Collections: ['sort(list)', 'reverse(list)', 'max(c)', 'min(c)', 'shuffle(list)', 'swap(list, i, j)',
      'unmodifiableList(list)', 'frequency(c, x)', 'emptyList()'],
    System: ['.out', '.in', '.err', 'currentTimeMillis()', 'nanoTime()', 'exit(code)', 'arraycopy(src, i, dst, j, n)'],
    'System.out': ['println(x)', 'print(x)', 'printf(format, args)'],
    'System.err': ['println(x)', 'print(x)', 'printf(format, args)'],
    StringStatic: ['valueOf(x)', 'format(fmt, args)', 'join(sep, items)']
  };

  const CATEGORY = {
    String: 'String', StringBuilder: 'StringBuilder', StringBuffer: 'StringBuilder',
    List: 'List', ArrayList: 'List', LinkedList: 'List', Collection: 'List', Vector: 'List',
    Map: 'Map', HashMap: 'Map', TreeMap: 'Map', LinkedHashMap: 'Map', ConcurrentHashMap: 'Map',
    Set: 'Set', HashSet: 'Set', TreeSet: 'Set', LinkedHashSet: 'Set',
    Deque: 'Deque', Queue: 'Deque', ArrayDeque: 'Deque', Stack: 'Deque', PriorityQueue: 'Deque',
    Scanner: 'Scanner'
  };
  const STATIC_RECV = new Set(['Math', 'Integer', 'Long', 'Double', 'Character', 'Arrays', 'Collections', 'System',
    'System.out', 'System.err']);

  function parseMember(def) {
    if (def.charAt(0) === '.') return { name: def.slice(1), label: def.slice(1), insert: def.slice(1), kind: 'field', detail: '' };
    const open = def.indexOf('(');
    const name = def.slice(0, open);
    const params = def.slice(open + 1, def.lastIndexOf(')'));
    return {
      name: name,
      label: name,
      detail: '(' + params + ')',
      insert: name + (params ? '($0)' : '()'),
      kind: 'method'
    };
  }

  /* ---------- snippets ---------- */
  const SNIPPETS = [
    { name: 'sout', detail: 'System.out.println', insert: 'System.out.println($0);' },
    { name: 'souf', detail: 'System.out.printf', insert: 'System.out.printf("$0%n");' },
    { name: 'main', detail: 'main method', insert: 'public static void main(String[] args) {\n    $0\n}' },
    { name: 'psvm', detail: 'main method', insert: 'public static void main(String[] args) {\n    $0\n}' },
    { name: 'fori', detail: 'for loop', insert: 'for (int i = 0; i < $0; i++) {\n    \n}' },
    { name: 'foreach', detail: 'enhanced for', insert: 'for (var item : $0) {\n    \n}' },
    { name: 'ifelse', detail: 'if / else', insert: 'if ($0) {\n    \n} else {\n    \n}' },
    { name: 'while', detail: 'while loop', insert: 'while ($0) {\n    \n}' },
    { name: 'trycatch', detail: 'try / catch', insert: 'try {\n    $0\n} catch (Exception e) {\n    System.out.println(e.getMessage());\n}' },
    { name: 'scanner', detail: 'Scanner on stdin', insert: 'Scanner sc = new Scanner(System.in);$0' },
    { name: 'switch', detail: 'switch statement', insert: 'switch ($0) {\n    case 1:\n        break;\n    default:\n        break;\n}' },
    { name: 'class', detail: 'class declaration', insert: 'class $0 {\n    \n}' }
  ];

  /* ---------- helpers ---------- */
  function inStringOrComment(lineBefore) {
    if (/\/\/.*$/.test(lineBefore.replace(/"(?:\\.|[^"\\])*"/g, '""'))) return true;
    let quotes = 0;
    for (let i = 0; i < lineBefore.length; i++) {
      const c = lineBefore.charAt(i);
      if (c === '\\') { i++; continue; }
      if (c === '"') quotes++;
    }
    return quotes % 2 === 1;
  }

  function declaredTypes(src) {
    const map = {};
    const re = /\b([A-Za-z_][\w]*)(<[^;=(){}]*?>)?(\[\])*\s+([A-Za-z_]\w*)\s*(?=[=;,):])/g;
    let m;
    while ((m = re.exec(src)) !== null) {
      if (KW.has(m[1]) && !/^(int|long|double|float|char|boolean|byte|short|var)$/.test(m[1])) continue;
      if (KW.has(m[4])) continue;
      map[m[4]] = (m[3] ? 'Array' : m[1]);
    }
    return map;
  }

  function receiverOf(src, dotIndex) {
    const before = src.slice(Math.max(0, dotIndex - 80), dotIndex);
    if (/[)\]]\s*$/.test(before)) return null;
    const m = /([A-Za-z_$][\w$]*(?:\s*\.\s*[A-Za-z_$][\w$]*)*)\s*$/.exec(before);
    return m ? m[1].replace(/\s+/g, '') : null;
  }

  function membersFor(recv, src) {
    let defs;
    if (recv && STATIC_RECV.has(recv)) defs = M[recv];
    else if (recv === 'String') defs = M.StringStatic;
    else {
      const t = recv ? declaredTypes(src)[recv] : null;
      if (t === 'Array') defs = M.Array;
      else if (t && CATEGORY[t]) defs = M[CATEGORY[t]];
      else defs = [].concat(M.String, M.List, M.Map, M.Object); // unknown receiver: offer common methods
    }
    const seen = new Set();
    const out = [];
    defs.forEach(function (d) {
      const it = parseMember(d);
      if (!seen.has(it.name)) { seen.add(it.name); out.push(it); }
    });
    return out;
  }

  function docWords(src, skipStart, skipEnd) {
    const words = new Set();
    const re = /[A-Za-z_$][\w$]*/g;
    let m;
    while ((m = re.exec(src)) !== null) {
      if (m.index === skipStart && m.index + m[0].length === skipEnd) continue;
      if (KW.has(m[0]) || m[0].length < 2) continue;
      words.add(m[0]);
    }
    return Array.from(words);
  }

  function generalItems(src, ws, caret) {
    const items = [];
    const have = new Set();
    function add(it) { if (!have.has(it.kind + it.name)) { have.add(it.kind + it.name); items.push(it); } }
    docWords(src, ws, caret).forEach(function (w) {
      if (TYPES.indexOf(w) === -1) add({ name: w, label: w, insert: w, kind: 'var', detail: '' });
    });
    SNIPPETS.forEach(function (s) { add({ name: s.name, label: s.name, insert: s.insert, kind: 'snip', detail: s.detail }); });
    TYPES.forEach(function (t) { add({ name: t, label: t, insert: t, kind: 'type', detail: '' }); });
    KEYWORDS.forEach(function (k) { add({ name: k, label: k, insert: k, kind: 'kw', detail: '' }); });
    return items;
  }

  const KIND_ORDER = { var: 0, snip: 1, field: 1, method: 1, type: 2, kw: 3 };

  /* Returns { start, prefix, items } or null. `force` shows suggestions even for a 0/1-char prefix. */
  function suggest(src, caret, force) {
    let ws = caret;
    while (ws > 0 && /[\w$]/.test(src.charAt(ws - 1))) ws--;
    const prefix = src.slice(ws, caret);
    if (/^\d/.test(prefix)) return null;
    const lineStart = src.lastIndexOf('\n', ws - 1) + 1;
    if (inStringOrComment(src.slice(lineStart, caret))) return null;

    let items;
    const prev = src.charAt(ws - 1);
    if (prev === '.' && /[\w)\]]/.test(src.charAt(ws - 2))) {
      if (/(^|[^\w$])\d[\w]*$/.test(src.slice(Math.max(0, ws - 30), ws - 1))) return null; // decimal number like 1.
      items = membersFor(receiverOf(src, ws - 1), src);
    } else {
      if (prefix.length < (force ? 1 : 2)) return null;
      items = generalItems(src, ws, caret);
    }

    const low = prefix.toLowerCase();
    items = items.filter(function (it) { return it.name.toLowerCase().indexOf(low) === 0; });
    items.sort(function (a, b) {
      const exactA = a.name.indexOf(prefix) === 0 ? 0 : 1;
      const exactB = b.name.indexOf(prefix) === 0 ? 0 : 1;
      if (exactA !== exactB) return exactA - exactB;
      const ka = KIND_ORDER[a.kind], kb = KIND_ORDER[b.kind];
      if (ka !== kb) return ka - kb;
      return a.name.localeCompare(b.name);
    });
    items = items.slice(0, 30);
    if (!items.length) return null;
    if (items.length === 1 && items[0].name === prefix && items[0].kind !== 'snip') return null;
    return { start: ws, prefix: prefix, items: items };
  }

  root.JavaLang = { highlight: highlight, suggest: suggest, declaredTypes: declaredTypes };
  if (typeof module !== 'undefined' && module.exports) module.exports = root.JavaLang;
})(typeof window !== 'undefined' ? window : globalThis);
