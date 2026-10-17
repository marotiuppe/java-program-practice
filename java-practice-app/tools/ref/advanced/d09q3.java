import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        String[] t = new String(System.in.readAllBytes()).trim().split("\\s+");
        int n = t.length;
        int[] left = new int[n];
        int[] right = new int[n];
        Arrays.fill(left, -1);
        Arrays.fill(right, -1);
        int cnt = 1;
        int[] queue = new int[n];
        int qh = 0;
        int qt = 0;
        queue[qt++] = 0;
        int idx = 1;
        while (qh < qt && idx < n) {
            int u = queue[qh++];
            for (int side = 0; side < 2 && idx < n; side++, idx++) {
                if (!t[idx].equals("null")) {
                    if (side == 0) {
                        left[u] = cnt;
                    } else {
                        right[u] = cnt;
                    }
                    queue[qt++] = cnt++;
                }
            }
        }
        // state: 0 = not covered, 1 = has a monitor, 2 = covered without a monitor
        int[] state = new int[cnt];
        int monitors = 0;
        for (int i = cnt - 1; i >= 0; i--) {
            int sl = left[i] >= 0 ? state[left[i]] : 2;
            int sr = right[i] >= 0 ? state[right[i]] : 2;
            if (sl == 0 || sr == 0) {
                state[i] = 1;
                monitors++;
            } else if (sl == 1 || sr == 1) {
                state[i] = 2;
            } else {
                state[i] = 0;
            }
        }
        if (state[0] == 0) {
            monitors++;
        }
        System.out.println(monitors);
    }
}
