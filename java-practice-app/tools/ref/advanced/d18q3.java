import java.util.*;
import java.io.*;

public class Main {
    static int m, n, free, count;
    static int[][] g;
    static boolean[][] vis;

    static void go(int r, int c, int visitedFree) {
        if (g[r][c] == 2) {
            if (visitedFree == free) count++;
            return;
        }
        int[] dr = {1, -1, 0, 0};
        int[] dc = {0, 0, 1, -1};
        for (int k = 0; k < 4; k++) {
            int nr = r + dr[k], nc = c + dc[k];
            if (nr < 0 || nc < 0 || nr >= m || nc >= n) continue;
            if (g[nr][nc] == -1 || vis[nr][nc]) continue;
            vis[nr][nc] = true;
            go(nr, nc, visitedFree + (g[nr][nc] == 0 ? 1 : 0));
            vis[nr][nc] = false;
        }
    }

    public static void main(String[] args) throws IOException {
        StreamTokenizer in = new StreamTokenizer(new BufferedInputStream(System.in));
        in.nextToken();
        m = (int) in.nval;
        in.nextToken();
        n = (int) in.nval;
        g = new int[m][n];
        vis = new boolean[m][n];
        int sr = 0, sc = 0;
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++) {
                in.nextToken();
                g[i][j] = (int) in.nval;
                if (g[i][j] == 0) free++;
                if (g[i][j] == 1) {
                    sr = i;
                    sc = j;
                }
            }
        vis[sr][sc] = true;
        go(sr, sc, 0);
        System.out.println(count);
    }
}
