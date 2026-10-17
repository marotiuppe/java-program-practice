import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        StringTokenizer st = new StringTokenizer(new String(System.in.readAllBytes()));
        int n = Integer.parseInt(st.nextToken());
        long k = Long.parseLong(st.nextToken());
        long[] p = new long[n + 1];
        for (int i = 0; i < n; i++) {
            p[i + 1] = p[i] + Long.parseLong(st.nextToken());
        }
        int[] dq = new int[n + 1];
        int head = 0;
        int tail = 0;
        int best = Integer.MAX_VALUE;
        for (int i = 0; i <= n; i++) {
            while (tail > head && p[i] - p[dq[head]] >= k) {
                best = Math.min(best, i - dq[head]);
                head++;
            }
            while (tail > head && p[dq[tail - 1]] >= p[i]) {
                tail--;
            }
            dq[tail++] = i;
        }
        System.out.println(best == Integer.MAX_VALUE ? -1 : best);
    }
}
