import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        StreamTokenizer in = new StreamTokenizer(new BufferedInputStream(System.in));
        in.nextToken();
        int n = (int) in.nval;
        in.nextToken();
        int D = (int) in.nval;
        long[] w = new long[n];
        long lo = 0, hi = 0;
        for (int i = 0; i < n; i++) {
            in.nextToken();
            w[i] = (long) in.nval;
            lo = Math.max(lo, w[i]);
            hi += w[i];
        }
        while (lo < hi) {
            long mid = lo + (hi - lo) / 2;
            int days = 1;
            long load = 0;
            for (long x : w) {
                if (load + x > mid) {
                    days++;
                    load = 0;
                }
                load += x;
            }
            if (days <= D) hi = mid;
            else lo = mid + 1;
        }
        System.out.println(lo);
    }
}
