import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        StreamTokenizer in = new StreamTokenizer(new BufferedInputStream(System.in));
        in.nextToken();
        int n = (int) in.nval;
        in.nextToken();
        int m = (int) in.nval;
        int[][] f = new int[m][3];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < 3; j++) {
                in.nextToken();
                f[i][j] = (int) in.nval;
            }
        in.nextToken();
        int src = (int) in.nval;
        in.nextToken();
        int dst = (int) in.nval;
        in.nextToken();
        int k = (int) in.nval;
        long INF = Long.MAX_VALUE / 4;
        long[] d = new long[n];
        Arrays.fill(d, INF);
        d[src] = 0;
        for (int round = 0; round <= k; round++) {
            long[] nd = d.clone();
            for (int[] e : f) {
                if (d[e[0]] < INF && d[e[0]] + e[2] < nd[e[1]]) nd[e[1]] = d[e[0]] + e[2];
            }
            d = nd;
        }
        System.out.println(d[dst] >= INF ? -1 : d[dst]);
    }
}
