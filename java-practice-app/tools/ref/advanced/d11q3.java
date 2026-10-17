import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine().trim());
        StringTokenizer st = new StringTokenizer(br.readLine());
        long[] a = new long[n];
        for (int i = 0; i < n; i++) a[i] = Long.parseLong(st.nextToken());
        int[][] kid = new int[32 * n + 2][2];
        int cnt = 1;
        long best = 0;
        for (int i = 0; i < n; i++) {
            int cur = 0;
            for (int b = 30; b >= 0; b--) {
                int bit = (int) ((a[i] >> b) & 1);
                if (kid[cur][bit] == 0) kid[cur][bit] = cnt++;
                cur = kid[cur][bit];
            }
            if (i == 0) continue;
            cur = 0;
            long x = 0;
            for (int b = 30; b >= 0; b--) {
                int bit = (int) ((a[i] >> b) & 1);
                int want = bit ^ 1;
                if (kid[cur][want] != 0) {
                    x |= 1L << b;
                    cur = kid[cur][want];
                } else {
                    cur = kid[cur][bit];
                }
            }
            best = Math.max(best, x);
        }
        System.out.println(best);
    }
}
