import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        StringTokenizer st = new StringTokenizer(new String(System.in.readAllBytes()));
        int k = Integer.parseInt(st.nextToken());
        int limit = Integer.parseInt(st.nextToken());
        int[][] times = new int[k][];
        String[][] ids = new String[k][];
        for (int f = 0; f < k; f++) {
            int m = Integer.parseInt(st.nextToken());
            times[f] = new int[m];
            ids[f] = new String[m];
            for (int j = 0; j < m; j++) {
                times[f][j] = Integer.parseInt(st.nextToken());
                ids[f][j] = st.nextToken();
            }
        }
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] != b[0] ? Integer.compare(b[0], a[0]) : Integer.compare(a[1], b[1]));
        for (int f = 0; f < k; f++) {
            if (times[f].length > 0) pq.add(new int[]{times[f][0], f, 0});
        }
        Set<String> seen = new HashSet<>();
        StringBuilder sb = new StringBuilder();
        int printed = 0;
        while (!pq.isEmpty() && printed < limit) {
            int[] e = pq.poll();
            String id = ids[e[1]][e[2]];
            if (seen.add(id)) {
                if (printed > 0) sb.append(' ');
                sb.append(id);
                printed++;
            }
            if (e[2] + 1 < times[e[1]].length) pq.add(new int[]{times[e[1]][e[2] + 1], e[1], e[2] + 1});
        }
        System.out.println(sb);
    }
}
