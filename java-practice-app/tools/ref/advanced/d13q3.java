import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        StreamTokenizer in = new StreamTokenizer(new BufferedInputStream(System.in));
        in.nextToken();
        int n = (int) in.nval;
        in.nextToken();
        int m = (int) in.nval;
        long[] dur = new long[n + 1];
        for (int i = 1; i <= n; i++) {
            in.nextToken();
            dur[i] = (long) in.nval;
        }
        List<List<Integer>> g = new ArrayList<>();
        for (int i = 0; i <= n; i++) g.add(new ArrayList<>());
        int[] indeg = new int[n + 1];
        for (int i = 0; i < m; i++) {
            in.nextToken();
            int a = (int) in.nval;
            in.nextToken();
            int b = (int) in.nval;
            g.get(a).add(b);
            indeg[b]++;
        }
        long[] start = new long[n + 1];
        long[] fin = new long[n + 1];
        ArrayDeque<Integer> q = new ArrayDeque<>();
        for (int i = 1; i <= n; i++) if (indeg[i] == 0) q.add(i);
        int done = 0;
        while (!q.isEmpty()) {
            int u = q.poll();
            done++;
            fin[u] = start[u] + dur[u];
            for (int v : g.get(u)) {
                start[v] = Math.max(start[v], fin[u]);
                if (--indeg[v] == 0) q.add(v);
            }
        }
        if (done < n) {
            System.out.println("CYCLE");
            return;
        }
        long total = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= n; i++) {
            total = Math.max(total, fin[i]);
            if (i > 1) sb.append(' ');
            sb.append(fin[i]);
        }
        System.out.println("Total: " + total);
        System.out.println("Finish: " + sb);
    }
}
