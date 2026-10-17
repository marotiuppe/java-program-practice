import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        StreamTokenizer in = new StreamTokenizer(new BufferedInputStream(System.in));
        in.nextToken();
        int R = (int) in.nval;
        in.nextToken();
        int C = (int) in.nval;
        int[][] a = new int[R][C];
        for (int i = 0; i < R; i++)
            for (int j = 0; j < C; j++) {
                in.nextToken();
                a[i][j] = (int) in.nval;
            }
        final long MOD = 1_000_000_007L;
        final long INF = Long.MAX_VALUE / 4;
        long[][] cost = new long[R][C];
        long[][] cnt = new long[R][C];
        for (int i = 0; i < R; i++) {
            for (int j = 0; j < C; j++) {
                cost[i][j] = INF;
                if (a[i][j] < 0) continue;
                if (i == 0 && j == 0) {
                    cost[i][j] = a[i][j];
                    cnt[i][j] = 1;
                    continue;
                }
                long best = INF;
                if (i > 0 && cost[i - 1][j] < best) best = cost[i - 1][j];
                if (j > 0 && cost[i][j - 1] < best) best = cost[i][j - 1];
                if (best >= INF) continue;
                long c = 0;
                if (i > 0 && cost[i - 1][j] == best) c += cnt[i - 1][j];
                if (j > 0 && cost[i][j - 1] == best) c += cnt[i][j - 1];
                cost[i][j] = best + a[i][j];
                cnt[i][j] = c % MOD;
            }
        }
        if (cost[R - 1][C - 1] >= INF) System.out.println("IMPOSSIBLE");
        else System.out.println(cost[R - 1][C - 1] + " " + cnt[R - 1][C - 1]);
    }
}
