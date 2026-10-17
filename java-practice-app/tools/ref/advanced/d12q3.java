import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        StreamTokenizer in = new StreamTokenizer(new BufferedInputStream(System.in));
        in.nextToken();
        int m = (int) in.nval;
        in.nextToken();
        int n = (int) in.nval;
        in.nextToken();
        int k = (int) in.nval;
        int[][] g = new int[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++) {
                in.nextToken();
                g[i][j] = (int) in.nval;
            }
        k = Math.min(k, m + n);
        int[][][] dist = new int[m][n][k + 1];
        for (int[][] a : dist) for (int[] b : a) Arrays.fill(b, -1);
        ArrayDeque<int[]> q = new ArrayDeque<>();
        dist[0][0][0] = 0;
        q.add(new int[]{0, 0, 0});
        int[] dr = {1, -1, 0, 0};
        int[] dc = {0, 0, 1, -1};
        while (!q.isEmpty()) {
            int[] c = q.poll();
            int d = dist[c[0]][c[1]][c[2]];
            if (c[0] == m - 1 && c[1] == n - 1) {
                System.out.println(d);
                return;
            }
            for (int t = 0; t < 4; t++) {
                int r = c[0] + dr[t], cc = c[1] + dc[t];
                if (r < 0 || cc < 0 || r >= m || cc >= n) continue;
                int used = c[2] + g[r][cc];
                if (used > k || dist[r][cc][used] >= 0) continue;
                dist[r][cc][used] = d + 1;
                q.add(new int[]{r, cc, used});
            }
        }
        System.out.println(-1);
    }
}
