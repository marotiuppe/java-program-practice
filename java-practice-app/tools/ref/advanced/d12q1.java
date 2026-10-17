import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int R = Integer.parseInt(st.nextToken());
        int C = Integer.parseInt(st.nextToken());
        char[][] g = new char[R][];
        for (int i = 0; i < R; i++) g[i] = br.readLine().trim().toCharArray();
        int[][] dist = new int[R][C];
        for (int[] d : dist) Arrays.fill(d, -1);
        ArrayDeque<int[]> q = new ArrayDeque<>();
        for (int i = 0; i < R; i++)
            for (int j = 0; j < C; j++)
                if (g[i][j] == 'F') {
                    dist[i][j] = 0;
                    q.add(new int[]{i, j});
                }
        int[] dr = {1, -1, 0, 0};
        int[] dc = {0, 0, 1, -1};
        int minutes = 0;
        while (!q.isEmpty()) {
            int[] c = q.poll();
            for (int k = 0; k < 4; k++) {
                int r = c[0] + dr[k], cc = c[1] + dc[k];
                if (r < 0 || cc < 0 || r >= R || cc >= C) continue;
                if (g[r][cc] != 'T' || dist[r][cc] >= 0) continue;
                dist[r][cc] = dist[c[0]][c[1]] + 1;
                minutes = Math.max(minutes, dist[r][cc]);
                q.add(new int[]{r, cc});
            }
        }
        int unburnt = 0;
        for (int i = 0; i < R; i++)
            for (int j = 0; j < C; j++)
                if (g[i][j] == 'T' && dist[i][j] < 0) unburnt++;
        System.out.println("Minutes: " + minutes);
        System.out.println("Unburnt: " + unburnt);
    }
}
