import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        StringTokenizer st = new StringTokenizer(new String(System.in.readAllBytes()));
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        int[] ea = new int[m], eb = new int[m];
        int[] head = new int[n];
        Arrays.fill(head, -1);
        int[] nxt = new int[2 * m];
        int[] to = new int[2 * m];
        for (int i = 0; i < m; i++) {
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            ea[i] = a;
            eb[i] = b;
            to[2 * i] = b;
            nxt[2 * i] = head[a];
            head[a] = 2 * i;
            to[2 * i + 1] = a;
            nxt[2 * i + 1] = head[b];
            head[b] = 2 * i + 1;
        }
        int[] tin = new int[n], low = new int[n];
        Arrays.fill(tin, -1);
        int[] parentEdge = new int[n];
        int[] it = new int[n];
        int[] stack = new int[n];
        boolean[] bridge = new boolean[m];
        int timer = 0;
        for (int r = 0; r < n; r++) {
            if (tin[r] != -1) continue;
            int sp = 0;
            stack[sp++] = r;
            tin[r] = low[r] = timer++;
            parentEdge[r] = -1;
            it[r] = head[r];
            while (sp > 0) {
                int v = stack[sp - 1];
                if (it[v] != -1) {
                    int e = it[v];
                    it[v] = nxt[e];
                    if ((e >> 1) == parentEdge[v]) continue;
                    int u = to[e];
                    if (tin[u] == -1) {
                        tin[u] = low[u] = timer++;
                        parentEdge[u] = e >> 1;
                        it[u] = head[u];
                        stack[sp++] = u;
                    } else {
                        low[v] = Math.min(low[v], tin[u]);
                    }
                } else {
                    sp--;
                    if (sp > 0) {
                        int par = stack[sp - 1];
                        low[par] = Math.min(low[par], low[v]);
                        if (low[v] > tin[par]) bridge[parentEdge[v]] = true;
                    }
                }
            }
        }
        List<int[]> res = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            if (bridge[i]) res.add(new int[]{Math.min(ea[i], eb[i]), Math.max(ea[i], eb[i])});
        }
        res.sort((x, y) -> x[0] != y[0] ? Integer.compare(x[0], y[0]) : Integer.compare(x[1], y[1]));
        StringBuilder sb = new StringBuilder();
        sb.append(res.size()).append('\n');
        for (int[] e : res) sb.append(e[0]).append(' ').append(e[1]).append('\n');
        System.out.print(sb);
    }
}
