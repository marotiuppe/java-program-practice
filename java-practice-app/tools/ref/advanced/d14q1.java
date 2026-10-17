import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        StreamTokenizer in = new StreamTokenizer(new BufferedInputStream(System.in));
        in.nextToken();
        int n = (int) in.nval;
        in.nextToken();
        int m = (int) in.nval;
        in.nextToken();
        int s = (int) in.nval;
        in.nextToken();
        int t = (int) in.nval;
        final long MOD = 1_000_000_007L;
        List<List<long[]>> g = new ArrayList<>();
        for (int i = 0; i < n; i++) g.add(new ArrayList<>());
        for (int i = 0; i < m; i++) {
            in.nextToken();
            int u = (int) in.nval;
            in.nextToken();
            int v = (int) in.nval;
            in.nextToken();
            long w = (long) in.nval;
            g.get(u).add(new long[]{v, w});
            g.get(v).add(new long[]{u, w});
        }
        long[] dist = new long[n];
        long[] cnt = new long[n];
        Arrays.fill(dist, Long.MAX_VALUE);
        dist[s] = 0;
        cnt[s] = 1;
        PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> Long.compare(a[0], b[0]));
        pq.add(new long[]{0, s});
        while (!pq.isEmpty()) {
            long[] cur = pq.poll();
            int u = (int) cur[1];
            if (cur[0] > dist[u]) continue;
            for (long[] e : g.get(u)) {
                int v = (int) e[0];
                long nd = dist[u] + e[1];
                if (nd < dist[v]) {
                    dist[v] = nd;
                    cnt[v] = cnt[u];
                    pq.add(new long[]{nd, v});
                } else if (nd == dist[v]) {
                    cnt[v] = (cnt[v] + cnt[u]) % MOD;
                }
            }
        }
        if (dist[t] == Long.MAX_VALUE) System.out.println("UNREACHABLE");
        else System.out.println(dist[t] + " " + cnt[t] % MOD);
    }
}
