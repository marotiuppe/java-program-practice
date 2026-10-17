import java.io.*;
import java.util.*;

public class Main {
    static int lower(int[] a, int x) {
        int lo = 0, hi = a.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (a[mid] < x) lo = mid + 1;
            else hi = mid;
        }
        return lo;
    }

    static int upper(int[] a, int x) {
        int lo = 0, hi = a.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (a[mid] <= x) lo = mid + 1;
            else hi = mid;
        }
        return lo;
    }

    public static void main(String[] args) throws IOException {
        StreamTokenizer in = new StreamTokenizer(new BufferedReader(new InputStreamReader(System.in)));
        in.nextToken();
        int n = (int) in.nval;
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            in.nextToken();
            a[i] = (int) in.nval;
        }
        in.nextToken();
        int q = (int) in.nval;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < q; i++) {
            in.nextToken();
            int x = (int) in.nval;
            int lo = lower(a, x), up = upper(a, x);
            int cnt = up - lo;
            sb.append(cnt == 0 ? -1 : lo).append(' ').append(cnt == 0 ? -1 : up - 1).append(' ').append(cnt).append(' ').append(lo).append('\n');
        }
        System.out.print(sb);
    }
}
