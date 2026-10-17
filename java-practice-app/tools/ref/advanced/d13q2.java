import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine().trim());
        StringTokenizer st = new StringTokenizer(br.readLine());
        String[] w = new String[n];
        for (int i = 0; i < n; i++) w[i] = st.nextToken();
        boolean[] present = new boolean[26];
        for (String s : w) for (char c : s.toCharArray()) present[c - 'a'] = true;
        boolean[][] edge = new boolean[26][26];
        int[] indeg = new int[26];
        for (int i = 0; i + 1 < n; i++) {
            String a = w[i], b = w[i + 1];
            int k = 0;
            int l = Math.min(a.length(), b.length());
            while (k < l && a.charAt(k) == b.charAt(k)) k++;
            if (k == l) {
                if (a.length() > b.length()) {
                    System.out.println("INVALID");
                    return;
                }
                continue;
            }
            int x = a.charAt(k) - 'a', y = b.charAt(k) - 'a';
            if (!edge[x][y]) {
                edge[x][y] = true;
                indeg[y]++;
            }
        }
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        int total = 0;
        for (int i = 0; i < 26; i++) if (present[i]) {
            total++;
            if (indeg[i] == 0) pq.add(i);
        }
        StringBuilder sb = new StringBuilder();
        while (!pq.isEmpty()) {
            int u = pq.poll();
            sb.append((char) ('a' + u));
            for (int v = 0; v < 26; v++) if (edge[u][v] && --indeg[v] == 0) pq.add(v);
        }
        System.out.println(sb.length() == total ? sb.toString() : "INVALID");
    }
}
