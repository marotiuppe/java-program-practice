import java.util.*;

public class Main {
    static int[] pre;
    static int[] in;
    static Map<Integer, Integer> pos = new HashMap<>();
    static int preIdx = 0;
    static StringBuilder sb = new StringBuilder();

    // builds the subtree for inorder range [lo, hi] and appends its postorder
    static void build(int lo, int hi) {
        if (lo > hi) {
            return;
        }
        int root = pre[preIdx++];
        int m = pos.get(root);
        build(lo, m - 1);
        build(m + 1, hi);
        if (sb.length() > 0) {
            sb.append(' ');
        }
        sb.append(root);
    }

    public static void main(String[] args) throws Exception {
        StringTokenizer st = new StringTokenizer(new String(System.in.readAllBytes()));
        int n = Integer.parseInt(st.nextToken());
        pre = new int[n];
        in = new int[n];
        for (int i = 0; i < n; i++) {
            pre[i] = Integer.parseInt(st.nextToken());
        }
        for (int i = 0; i < n; i++) {
            in[i] = Integer.parseInt(st.nextToken());
            pos.put(in[i], i);
        }
        Thread th = new Thread(null, () -> build(0, n - 1), "b", 1L << 28);
        th.start();
        th.join();
        System.out.println(sb);
    }
}
