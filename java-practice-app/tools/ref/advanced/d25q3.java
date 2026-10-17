import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine().trim());
        String[] lines = new String[n];
        for (int i = 0; i < n; i++) {
            String l = br.readLine();
            lines[i] = l == null ? "" : l;
        }
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        Thread[] ts = new Thread[4];
        for (int w = 0; w < 4; w++) {
            final int id = w;
            ts[w] = new Thread(() -> {
                for (int i = id; i < n; i += 4) {
                    StringTokenizer st = new StringTokenizer(lines[i]);
                    while (st.hasMoreTokens()) map.merge(st.nextToken(), 1, Integer::sum);
                }
            });
            ts[w].start();
        }
        for (Thread t : ts) t.join();
        List<Map.Entry<String, Integer>> es = new ArrayList<>(map.entrySet());
        es.sort((a, b) -> !a.getValue().equals(b.getValue()) ? b.getValue() - a.getValue() : a.getKey().compareTo(b.getKey()));
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> e : es) sb.append(e.getKey()).append(' ').append(e.getValue()).append('\n');
        System.out.print(sb);
    }
}
