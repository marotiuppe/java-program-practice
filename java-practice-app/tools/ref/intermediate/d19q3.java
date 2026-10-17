import java.io.*;
import java.util.*;

public class Main {
    static long pairs = 0;

    static void sort(int[] a, int[] tmp, int lo, int hi) {
        if (hi - lo < 1) return;
        int mid = (lo + hi) >>> 1;
        sort(a, tmp, lo, mid);
        sort(a, tmp, mid + 1, hi);
        int i = lo, j = mid + 1, k = lo;
        while (i <= mid && j <= hi) {
            if (a[i] <= a[j]) {
                tmp[k++] = a[i++];
            } else {
                pairs += mid - i + 1;
                tmp[k++] = a[j++];
            }
        }
        while (i <= mid) tmp[k++] = a[i++];
        while (j <= hi) tmp[k++] = a[j++];
        for (k = lo; k <= hi; k++) a[k] = tmp[k];
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
        sort(a, new int[n], 0, n - 1);
        StringBuilder sb = new StringBuilder("Pairs: " + pairs + "\n");
        for (int i = 0; i < n; i++) {
            if (i > 0) sb.append(' ');
            sb.append(a[i]);
        }
        System.out.println(sb);
    }
}
