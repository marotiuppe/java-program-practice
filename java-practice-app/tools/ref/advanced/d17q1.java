import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        StreamTokenizer in = new StreamTokenizer(new BufferedInputStream(System.in));
        in.nextToken();
        int n = (int) in.nval;
        in.nextToken();
        int W = (int) in.nval;
        int[] w = new int[n + 1];
        long[] v = new long[n + 1];
        for (int i = 1; i <= n; i++) {
            in.nextToken();
            w[i] = (int) in.nval;
            in.nextToken();
            v[i] = (long) in.nval;
        }
        // f[i][c] = best value using items i..n with capacity c
        long[][] f = new long[n + 2][W + 1];
        for (int i = n; i >= 1; i--) {
            for (int c = 0; c <= W; c++) {
                f[i][c] = f[i + 1][c];
                if (c >= w[i]) f[i][c] = Math.max(f[i][c], f[i + 1][c - w[i]] + v[i]);
            }
        }
        StringBuilder sb = new StringBuilder();
        int c = W;
        for (int i = 1; i <= n; i++) {
            if (c >= w[i] && f[i + 1][c - w[i]] + v[i] == f[i][c]) {
                sb.append(' ').append(i);
                c -= w[i];
            }
        }
        System.out.println(f[1][W]);
        System.out.println(sb.length() == 0 ? "Items: none" : "Items:" + sb);
    }
}
