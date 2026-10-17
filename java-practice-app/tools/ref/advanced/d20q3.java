import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        StreamTokenizer in = new StreamTokenizer(new BufferedInputStream(System.in));
        in.nextToken();
        int n = (int) in.nval;
        int[] a = new int[n];
        int x = 0;
        for (int i = 0; i < n; i++) {
            in.nextToken();
            a[i] = (int) in.nval;
            x ^= a[i];
        }
        int low = x & -x; // works for Integer.MIN_VALUE too (x & -x == x)
        int p = 0, q = 0;
        for (int v : a) {
            if ((v & low) != 0) p ^= v;
            else q ^= v;
        }
        System.out.println(Math.min(p, q) + " " + Math.max(p, q));
    }
}
