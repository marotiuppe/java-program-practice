import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        long cap = Long.parseLong(st.nextToken());
        long rate = Long.parseLong(st.nextToken());
        int m = Integer.parseInt(br.readLine().trim());
        Map<String, long[]> users = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            long t = Long.parseLong(st.nextToken());
            String u = st.nextToken();
            long[] b = users.get(u);
            if (b == null) {
                b = new long[]{cap, t};
                users.put(u, b);
            } else {
                long add = (t - b[1]) * rate;
                b[0] = Math.min(cap, b[0] + add);
                b[1] = t;
            }
            if (b[0] >= 1) {
                b[0]--;
                sb.append("ALLOW\n");
            } else {
                sb.append("DENY\n");
            }
        }
        System.out.print(sb);
    }
}
