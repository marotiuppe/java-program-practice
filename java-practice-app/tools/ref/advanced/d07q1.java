import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        StringTokenizer st = new StringTokenizer(new String(System.in.readAllBytes()));
        int q = Integer.parseInt(st.nextToken());
        // per item: parallel lists of snapshot stamps and quantities (stamp = snapshots taken so far)
        Map<String, List<long[]>> history = new HashMap<>();
        int snaps = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < q; i++) {
            String cmd = st.nextToken();
            if (cmd.equals("SET")) {
                String item = st.nextToken();
                long qty = Long.parseLong(st.nextToken());
                List<long[]> h = history.computeIfAbsent(item, z -> new ArrayList<>());
                if (!h.isEmpty() && h.get(h.size() - 1)[0] == snaps) {
                    h.get(h.size() - 1)[1] = qty;
                } else {
                    h.add(new long[]{snaps, qty});
                }
            } else if (cmd.equals("SNAPSHOT")) {
                sb.append(snaps++).append('\n');
            } else {
                int id = Integer.parseInt(st.nextToken());
                String item = st.nextToken();
                List<long[]> h = history.get(item);
                long ans = 0;
                if (h != null) {
                    int lo = 0;
                    int hi = h.size() - 1;
                    int found = -1;
                    while (lo <= hi) {
                        int mid = (lo + hi) >>> 1;
                        if (h.get(mid)[0] <= id) {
                            found = mid;
                            lo = mid + 1;
                        } else {
                            hi = mid - 1;
                        }
                    }
                    if (found >= 0) {
                        ans = h.get(found)[1];
                    }
                }
                sb.append(ans).append('\n');
            }
        }
        System.out.print(sb);
    }
}
