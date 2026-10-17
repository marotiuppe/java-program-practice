import java.util.*;
import java.io.*;

public class Main {
    static long rob(long[] a, int lo, int hi) {
        long take = 0, skip = 0;
        for (int i = lo; i <= hi; i++) {
            long nt = skip + a[i];
            skip = Math.max(skip, take);
            take = nt;
        }
        return Math.max(take, skip);
    }

    public static void main(String[] args) throws IOException {
        StreamTokenizer in = new StreamTokenizer(new BufferedInputStream(System.in));
        in.nextToken();
        int n = (int) in.nval;
        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            in.nextToken();
            a[i] = (long) in.nval;
        }
        if (n == 1) System.out.println(a[0]);
        else System.out.println(Math.max(rob(a, 0, n - 2), rob(a, 1, n - 1)));
    }
}
