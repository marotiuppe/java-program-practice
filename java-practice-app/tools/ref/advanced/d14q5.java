import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class Main {
    static long[] dijkstra(int n, List<int[]>[] g, int src) {
        long[] dist = new long[n];
        Arrays.fill(dist, Long.MAX_VALUE);
        dist[src] = 0;
        PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> Long.compare(a[1], b[1]));
        pq.add(new long[]{src, 0});
        while (!pq.isEmpty()) {
            long[] cur = pq.poll();
            int u = (int) cur[0];
            if (cur[1] > dist[u]) {
                continue;
            }
            for (int[] e : g[u]) {
                int v = e[0];
                long w = e[1];
                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    pq.add(new long[]{v, dist[v]});
                }
            }
        }
        for (int i = 0; i < n; i++) {
            if (dist[i] == Long.MAX_VALUE) {
                dist[i] = -1;
            }
        }
        return dist;
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        int n = 6;
        List<int[]>[] g = new List[n];
        for (int i = 0; i < n; i++) {
            g[i] = new ArrayList<>();
        }
        g[0].add(new int[]{1, 1000000000});
        g[1].add(new int[]{2, 1000000000});
        g[2].add(new int[]{3, 1000000000});
        g[0].add(new int[]{4, 5});
        StringBuilder sb = new StringBuilder();
        for (long d : dijkstra(n, g, 0)) {
            sb.append(d).append(' ');
        }
        System.out.println(sb.toString().trim());
    }
}
