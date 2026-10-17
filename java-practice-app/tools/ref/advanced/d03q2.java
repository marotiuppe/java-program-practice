import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        StringTokenizer st = new StringTokenizer(new String(System.in.readAllBytes()));
        int n = Integer.parseInt(st.nextToken());
        long[] h = new long[n + 1];
        for (int i = 0; i < n; i++) {
            h[i] = Long.parseLong(st.nextToken());
        }
        h[n] = 0;
        int[] stack = new int[n + 2];
        int sp = 0;
        long best = 0;
        for (int i = 0; i <= n; i++) {
            while (sp > 0 && h[stack[sp - 1]] >= h[i]) {
                long height = h[stack[--sp]];
                int leftBound = sp == 0 ? -1 : stack[sp - 1];
                best = Math.max(best, height * (i - leftBound - 1));
            }
            stack[sp++] = i;
        }
        System.out.println(best);
    }
}
