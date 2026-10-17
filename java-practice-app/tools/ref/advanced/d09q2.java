import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        String[] t = new String(System.in.readAllBytes()).trim().split("\\s+");
        int n = t.length;
        long[] val = new long[n];
        int[] left = new int[n];
        int[] right = new int[n];
        Arrays.fill(left, -1);
        Arrays.fill(right, -1);
        int cnt = 1;
        val[0] = Long.parseLong(t[0]);
        int[] queue = new int[n];
        int qh = 0;
        int qt = 0;
        queue[qt++] = 0;
        int idx = 1;
        while (qh < qt && idx < n) {
            int u = queue[qh++];
            for (int side = 0; side < 2 && idx < n; side++, idx++) {
                if (!t[idx].equals("null")) {
                    val[cnt] = Long.parseLong(t[idx]);
                    if (side == 0) {
                        left[u] = cnt;
                    } else {
                        right[u] = cnt;
                    }
                    queue[qt++] = cnt++;
                }
            }
        }
        long[] gain = new long[cnt];
        long best = Long.MIN_VALUE;
        for (int i = cnt - 1; i >= 0; i--) {
            long gl = left[i] >= 0 ? Math.max(0, gain[left[i]]) : 0;
            long gr = right[i] >= 0 ? Math.max(0, gain[right[i]]) : 0;
            best = Math.max(best, val[i] + gl + gr);
            gain[i] = val[i] + Math.max(gl, gr);
        }
        System.out.println(best);
    }
}
