import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        StringTokenizer st = new StringTokenizer(new String(System.in.readAllBytes()));
        int n = Integer.parseInt(st.nextToken());
        int[] l = new int[n];
        int[] r = new int[n];
        int[] parent = new int[n];
        Arrays.fill(parent, 0);
        for (int i = 0; i < n; i++) {
            l[i] = Integer.parseInt(st.nextToken());
            r[i] = Integer.parseInt(st.nextToken());
        }
        int log = 1;
        while ((1 << log) < n) {
            log++;
        }
        int[][] up = new int[log + 1][n];
        int[] depth = new int[n];
        int[] queue = new int[n];
        int qh = 0;
        int qt = 0;
        queue[qt++] = 0;
        up[0][0] = 0;
        while (qh < qt) {
            int u = queue[qh++];
            for (int c : new int[]{l[u], r[u]}) {
                if (c >= 0) {
                    up[0][c] = u;
                    depth[c] = depth[u] + 1;
                    queue[qt++] = c;
                }
            }
        }
        for (int j = 1; j <= log; j++) {
            for (int v = 0; v < n; v++) {
                up[j][v] = up[j - 1][up[j - 1][v]];
            }
        }
        int q = Integer.parseInt(st.nextToken());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < q; i++) {
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            int x = a;
            int y = b;
            if (depth[x] < depth[y]) {
                int tmp = x;
                x = y;
                y = tmp;
            }
            int diff = depth[x] - depth[y];
            for (int j = 0; j <= log; j++) {
                if ((diff >> j & 1) != 0) {
                    x = up[j][x];
                }
            }
            if (x != y) {
                for (int j = log; j >= 0; j--) {
                    if (up[j][x] != up[j][y]) {
                        x = up[j][x];
                        y = up[j][y];
                    }
                }
                x = up[0][x];
            }
            sb.append(x).append(' ').append(depth[a] + depth[b] - 2 * depth[x]).append('\n');
        }
        System.out.print(sb);
    }
}
