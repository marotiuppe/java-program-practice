import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        StreamTokenizer in = new StreamTokenizer(new BufferedInputStream(System.in));
        in.nextToken();
        int n = (int) in.nval;
        long[] ev = new long[2 * n]; // time*2 + (0 for end, 1 for start) so ends sort before starts
        for (int i = 0; i < n; i++) {
            in.nextToken();
            long s = (long) in.nval;
            in.nextToken();
            long e = (long) in.nval;
            ev[2 * i] = s * 2 + 1;
            ev[2 * i + 1] = e * 2;
        }
        Arrays.sort(ev);
        int cur = 0, best = 0;
        long peak = 0;
        for (long x : ev) {
            if ((x & 1) == 1) {
                cur++;
                if (cur > best) {
                    best = cur;
                    peak = x / 2;
                }
            } else {
                cur--;
            }
        }
        System.out.println("Rooms: " + best);
        System.out.println("Peak at: " + peak);
    }
}
