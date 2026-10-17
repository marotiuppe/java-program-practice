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
        int[] stack = new int[cnt + 1];
        int sp = 0;
        int cur = 0;
        int prev = -1;
        int first = -1;
        int second = -1;
        while (cur != -1 || sp > 0) {
            while (cur != -1) {
                stack[sp++] = cur;
                cur = left[cur];
            }
            cur = stack[--sp];
            if (prev != -1 && val[prev] > val[cur]) {
                if (first == -1) {
                    first = prev;
                }
                second = cur;
            }
            prev = cur;
            cur = right[cur];
        }
        long a = val[first];
        long b = val[second];
        System.out.println(Math.min(a, b) + " " + Math.max(a, b));
    }
}
