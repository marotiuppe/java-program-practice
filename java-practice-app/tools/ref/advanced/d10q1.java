import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        StringTokenizer st = new StringTokenizer(new String(System.in.readAllBytes()));
        int q = Integer.parseInt(st.nextToken());
        TreeMap<Integer, Integer> booked = new TreeMap<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < q; i++) {
            st.nextToken();
            int s = Integer.parseInt(st.nextToken());
            int e = Integer.parseInt(st.nextToken());
            boolean ok = true;
            Map.Entry<Integer, Integer> before = booked.floorEntry(s);
            if (before != null && before.getValue() > s) {
                ok = false;
            }
            Map.Entry<Integer, Integer> after = booked.ceilingEntry(s);
            if (after != null && after.getKey() < e) {
                ok = false;
            }
            if (ok) {
                booked.put(s, e);
            }
            sb.append(ok ? "YES" : "NO").append('\n');
        }
        System.out.print(sb);
    }
}
