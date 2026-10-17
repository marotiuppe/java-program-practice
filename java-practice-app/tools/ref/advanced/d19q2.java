import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        StreamTokenizer in = new StreamTokenizer(new BufferedInputStream(System.in));
        in.nextToken();
        int n = (int) in.nval;
        long[][] iv = new long[n][2];
        for (int i = 0; i < n; i++) {
            in.nextToken();
            iv[i][0] = (long) in.nval;
            in.nextToken();
            iv[i][1] = (long) in.nval;
        }
        Arrays.sort(iv, (x, y) -> x[1] != y[1] ? Long.compare(x[1], y[1]) : Long.compare(x[0], y[0]));
        int kept = 0;
        long end = Long.MIN_VALUE;
        for (long[] p : iv) {
            if (p[0] >= end) {
                kept++;
                end = p[1];
            }
        }
        System.out.println(n - kept);
    }
}
