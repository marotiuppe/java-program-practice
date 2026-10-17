import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws Exception {
        StringTokenizer st = new StringTokenizer(new String(System.in.readAllBytes()));
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        int t = Integer.parseInt(st.nextToken());
        long[] bal = new long[n];
        Object[] locks = new Object[n];
        for (int i = 0; i < n; i++) {
            bal[i] = Long.parseLong(st.nextToken());
            locks[i] = new Object();
        }
        ExecutorService pool = Executors.newFixedThreadPool(t);
        for (int i = 0; i < m; i++) {
            final int from = Integer.parseInt(st.nextToken());
            final int to = Integer.parseInt(st.nextToken());
            final long amt = Long.parseLong(st.nextToken());
            pool.execute(() -> {
                if (from == to) return;
                int a = Math.min(from, to), b = Math.max(from, to);
                synchronized (locks[a]) {
                    synchronized (locks[b]) {
                        bal[from] -= amt;
                        bal[to] += amt;
                    }
                }
            });
        }
        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.HOURS);
        StringBuilder sb = new StringBuilder();
        synchronized (Main.class) {
            for (int i = 0; i < n; i++) {
                if (i > 0) sb.append(' ');
                long v;
                synchronized (locks[i]) {
                    v = bal[i];
                }
                sb.append(v);
            }
        }
        System.out.println(sb);
    }
}
