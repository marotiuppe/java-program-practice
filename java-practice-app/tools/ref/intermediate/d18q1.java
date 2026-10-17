import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        StreamTokenizer in = new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
        in.nextToken();
        int n = (int) in.nval;
        long[] p = new long[n + 1];
        for (int i = 0; i < n; i++) {
            in.nextToken();
            p[i + 1] = p[i] + (long) in.nval;
        }
        in.nextToken();
        int q = (int) in.nval;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < q; i++) {
            in.nextToken();
            int l = (int) in.nval;
            in.nextToken();
            int r = (int) in.nval;
            long s = p[r] - p[l - 1];
            sb.append(s).append(' ').append(String.format("%.2f", (double) s / (r - l + 1))).append('\n');
        }
        System.out.print(sb);
    }
}
