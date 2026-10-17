public class Main {
    static class WordDictionary {
        private static class Node {
            Node[] next = new Node[26];
            boolean end;
        }

        private final Node root = new Node();

        void addWord(String word) {
            Node cur = root;
            for (char c : word.toCharArray()) {
                int i = c - 'a';
                if (cur.next[i] == null) {
                    cur.next[i] = new Node();
                }
                cur = cur.next[i];
            }
            cur.end = true;
        }

        boolean search(String word) {
            return dfs(word, 0, root);
        }

        private boolean dfs(String word, int pos, Node node) {
            if (pos == word.length()) {
                return node.end;
            }
            char c = word.charAt(pos);
            if (c == '.') {
                for (Node child : node.next) {
                    if (child != null && dfs(word, pos + 1, child)) {
                        return true;
                    }
                }
                return false;
            }
            Node child = node.next[c - 'a'];
            return child != null && dfs(word, pos + 1, child);
        }
    }

    public static void main(String[] args) {
        WordDictionary d = new WordDictionary();
        d.addWord("bad");
        d.addWord("dad");
        d.addWord("mad");
        String[] queries = {"pad", "bad", ".ad", "b..", "ba", "b.d.", "..."};
        for (String q : queries) {
            System.out.println(q + " -> " + d.search(q));
        }
    }
}
