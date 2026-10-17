import java.io.*;

public class Main {
    static int[][] next;
    static int[] link, len;
    static int sz, last;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = br.readLine().trim();
        int n = s.length();
        int cap = 2 * n + 2;
        next = new int[cap][];
        link = new int[cap];
        len = new int[cap];
        int[] cnt = new int[cap];
        next[0] = new int[26];
        java.util.Arrays.fill(next[0], -1);
        link[0] = -1;
        sz = 1;
        last = 0;
        for (int i = 0; i < n; i++) {
            int c = s.charAt(i) - 'a';
            int cur = sz++;
            len[cur] = len[last] + 1;
            next[cur] = new int[26];
            java.util.Arrays.fill(next[cur], -1);
            cnt[cur] = 1;
            int p = last;
            while (p != -1 && next[p][c] == -1) {
                next[p][c] = cur;
                p = link[p];
            }
            if (p == -1) {
                link[cur] = 0;
            } else {
                int q = next[p][c];
                if (len[p] + 1 == len[q]) {
                    link[cur] = q;
                } else {
                    int cl = sz++;
                    len[cl] = len[p] + 1;
                    next[cl] = next[q].clone();
                    link[cl] = link[q];
                    while (p != -1 && next[p][c] == q) {
                        next[p][c] = cl;
                        p = link[p];
                    }
                    link[q] = cl;
                    link[cur] = cl;
                }
            }
            last = cur;
        }
        int[] bucket = new int[n + 2];
        for (int i = 0; i < sz; i++) bucket[len[i]]++;
        for (int i = 1; i <= n; i++) bucket[i] += bucket[i - 1];
        int[] order = new int[sz];
        for (int i = sz - 1; i >= 0; i--) order[--bucket[len[i]]] = i;
        int best = 0;
        for (int i = sz - 1; i > 0; i--) {
            int v = order[i];
            if (cnt[v] >= 2) best = Math.max(best, len[v]);
            if (link[v] >= 0) cnt[link[v]] += cnt[v];
        }
        System.out.println(best);
    }
}
