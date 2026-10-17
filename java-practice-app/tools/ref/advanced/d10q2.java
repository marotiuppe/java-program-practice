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
        // iterative inorder: a tree is a valid BST iff the inorder sequence is strictly increasing
        int[] stack = new int[cnt + 1];
        int sp = 0;
        int cur = 0;
        long prev = Long.MIN_VALUE;
        boolean first = true;
        boolean ok = true;
        while ((cur != -1 || sp > 0) && ok) {
            while (cur != -1) {
                stack[sp++] = cur;
                cur = left[cur];
            }
            cur = stack[--sp];
            if (!first && val[cur] <= prev) {
                ok = false;
            }
            first = false;
            prev = val[cur];
            cur = right[cur];
        }
        System.out.println(ok);
    }
}
