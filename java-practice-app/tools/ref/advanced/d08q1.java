import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        String[] t = new String(System.in.readAllBytes()).trim().split("\\s+");
        int[] val = new int[t.length];
        int[] left = new int[t.length];
        int[] right = new int[t.length];
        Arrays.fill(left, -1);
        Arrays.fill(right, -1);
        int[] depth = new int[t.length];
        int cnt = 1;
        val[0] = Integer.parseInt(t[0]);
        int[] queue = new int[t.length];
        int qh = 0;
        int qt = 0;
        queue[qt++] = 0;
        int idx = 1;
        while (qh < qt && idx < t.length) {
            int u = queue[qh++];
            if (idx < t.length) {
                if (!t[idx].equals("null")) {
                    val[cnt] = Integer.parseInt(t[idx]);
                    depth[cnt] = depth[u] + 1;
                    left[u] = cnt;
                    queue[qt++] = cnt++;
                }
                idx++;
            }
            if (idx < t.length) {
                if (!t[idx].equals("null")) {
                    val[cnt] = Integer.parseInt(t[idx]);
                    depth[cnt] = depth[u] + 1;
                    right[u] = cnt;
                    queue[qt++] = cnt++;
                }
                idx++;
            }
        }
        // nodes are numbered in level order, so each level is a contiguous block
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < cnt) {
            int j = i;
            int max = Integer.MIN_VALUE;
            while (j < cnt && depth[j] == depth[i]) {
                max = Math.max(max, val[j]);
                j++;
            }
            sb.append("L").append(depth[i]).append(": count=").append(j - i).append(" max=").append(max)
                    .append(" left=").append(val[i]).append(" right=").append(val[j - 1]).append('\n');
            i = j;
        }
        System.out.print(sb);
    }
}
