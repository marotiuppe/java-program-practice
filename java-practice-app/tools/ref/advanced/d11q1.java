import java.util.*;
import java.io.*;

public class Main {
    static class Node {
        Node[] next = new Node[26];
        List<int[]> top = new ArrayList<>(); // not used
        int[] best = new int[3];
        int cnt = 0;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int q = Integer.parseInt(st.nextToken());
        String[] names = new String[n];
        long[] pop = new long[n];
        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            names[i] = st.nextToken();
            pop[i] = Long.parseLong(st.nextToken());
            order[i] = i;
        }
        // sort by popularity desc then name asc; insert in that order so the first 3 per node are the answer
        Arrays.sort(order, (a, b) -> pop[a] != pop[b] ? Long.compare(pop[b], pop[a]) : names[a].compareTo(names[b]));
        Node root = new Node();
        int[][] store = new int[0][];
        Map<Node, List<Integer>> tops = new IdentityHashMap<>();
        for (int idx : order) {
            Node t = root;
            for (int k = 0; k <= names[idx].length(); k++) {
                if (k > 0) {
                    int c = names[idx].charAt(k - 1) - 'a';
                    if (t.next[c] == null) t.next[c] = new Node();
                    t = t.next[c];
                }
                List<Integer> l = tops.computeIfAbsent(t, x -> new ArrayList<>());
                if (l.size() < 3) l.add(idx);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < q; i++) {
            String p = br.readLine().trim();
            Node t = root;
            for (int k = 0; k < p.length() && t != null; k++) t = t.next[p.charAt(k) - 'a'];
            List<Integer> l = t == null ? null : tops.get(t);
            if (l == null || l.isEmpty()) {
                sb.append("NONE\n");
            } else {
                for (int j = 0; j < l.size(); j++) {
                    if (j > 0) sb.append(' ');
                    sb.append(names[l.get(j)]);
                }
                sb.append('\n');
            }
        }
        System.out.print(sb);
    }
}
