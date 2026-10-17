import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        StreamTokenizer in = new StreamTokenizer(new BufferedInputStream(System.in));
        in.nextToken();
        int n = (int) in.nval;
        in.nextToken();
        int m = (int) in.nval;
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
        ArrayDeque<Integer> q = new ArrayDeque<>();
        boolean[] done = new boolean[n + 1];
        for (int i = 1; i <= n; i++) if (indeg[i] == 0) q.add(i);
        while (!q.isEmpty()) {
            int u = q.poll();
            done[u] = true;
            for (int v : g.get(u)) if (--indeg[v] == 0) q.add(v);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= n; i++) if (!done[i]) sb.append(' ').append(i);
        System.out.println(sb.length() == 0 ? "All tasks schedulable" : "Blocked:" + sb);
    }
}
