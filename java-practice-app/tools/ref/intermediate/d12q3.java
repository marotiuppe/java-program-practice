import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        StreamTokenizer in = new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
        in.nextToken();
        int k = (int) in.nval;
        long[][] lists = new long[k][];
        for (int i = 0; i < k; i++) {
            in.nextToken();
            int m = (int) in.nval;
            lists[i] = new long[m];
            for (int j = 0; j < m; j++) {
                in.nextToken();
                lists[i][j] = (long) in.nval;
            }
        }
        PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> a[0] != b[0] ? Long.compare(a[0], b[0]) : Long.compare(a[1], b[1]));
        for (int i = 0; i < k; i++) {
            if (lists[i].length > 0) pq.add(new long[]{lists[i][0], i, 0});
        }
        StringBuilder sb = new StringBuilder();
        while (!pq.isEmpty()) {
            long[] e = pq.poll();
            int s = (int) e[1];
            int idx = (int) e[2];
            sb.append(e[0]).append(' ').append(s + 1).append('\n');
            if (idx + 1 < lists[s].length) pq.add(new long[]{lists[s][idx + 1], s, idx + 1});
        }
        System.out.print(sb);
    }
}
