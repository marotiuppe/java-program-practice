import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        String[] t = new String(System.in.readAllBytes()).trim().split("\\s+");
        int n = t.length;
        int[] val = new int[n];
        int[] row = new int[n];
        int[] col = new int[n];
        int cnt = 1;
        val[0] = Integer.parseInt(t[0]);
        int[] queue = new int[n];
        int qh = 0;
        int qt = 0;
        queue[qt++] = 0;
        int idx = 1;
        while (qh < qt && idx < n) {
            int u = queue[qh++];
            for (int side = 0; side < 2 && idx < n; side++, idx++) {
                if (!t[idx].equals("null")) {
                    val[cnt] = Integer.parseInt(t[idx]);
                    row[cnt] = row[u] + 1;
                    col[cnt] = col[u] + (side == 0 ? -1 : 1);
                    queue[qt++] = cnt++;
                }
            }
        }
        Integer[] order = new Integer[cnt];
        for (int i = 0; i < cnt; i++) {
            order[i] = i;
        }
        Arrays.sort(order, (a, b) -> {
            if (col[a] != col[b]) {
                return Integer.compare(col[a], col[b]);
            }
            if (row[a] != row[b]) {
                return Integer.compare(row[a], row[b]);
            }
            return Integer.compare(val[a], val[b]);
        });
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cnt; i++) {
            if (i > 0) {
                sb.append(col[order[i]] != col[order[i - 1]] ? '\n' : ' ');
            }
            sb.append(val[order[i]]);
        }
        System.out.println(sb);
    }
}
