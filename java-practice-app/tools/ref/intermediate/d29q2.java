import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(in.readLine());
        int cap = Integer.parseInt(st.nextToken()), n = Integer.parseInt(st.nextToken());
        LinkedHashMap<Integer, Integer> cache = new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Integer, Integer> e) {
                return size() > cap;
            }
        };
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            String[] p = in.readLine().trim().split("\s+");
            if (p[0].equals("GET")) {
                Integer v = cache.get(Integer.parseInt(p[1]));
                sb.append(v == null ? -1 : v).append("\n");
            } else {
                cache.put(Integer.parseInt(p[1]), Integer.parseInt(p[2]));
            }
        }
        System.out.print(sb);
    }
}
