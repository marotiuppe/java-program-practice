import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        StringTokenizer st = new StringTokenizer(new String(System.in.readAllBytes()));
        int q = Integer.parseInt(st.nextToken());
        // entry: {severity, arrival, id}
        PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> a[0] != b[0] ? Long.compare(b[0], a[0]) : Long.compare(a[1], b[1]));
        Map<String, Integer> idOf = new HashMap<>();
        List<String> names = new ArrayList<>();
        List<long[]> state = new ArrayList<>(); // {severity, arrival, waiting(1/0)}
        StringBuilder sb = new StringBuilder();
        int arrivals = 0;
        for (int i = 0; i < q; i++) {
            String cmd = st.nextToken();
            if (cmd.equals("ARRIVE")) {
                String name = st.nextToken();
                long sev = Long.parseLong(st.nextToken());
                int id = names.size();
                names.add(name);
                idOf.put(name, id);
                state.add(new long[]{sev, arrivals, 1});
                pq.add(new long[]{sev, arrivals, id});
                arrivals++;
            } else if (cmd.equals("UPGRADE")) {
                String name = st.nextToken();
                long sev = Long.parseLong(st.nextToken());
                Integer id = idOf.get(name);
                if (id != null) {
                    long[] s = state.get(id);
                    if (s[2] == 1 && sev > s[0]) {
                        s[0] = sev;
                        pq.add(new long[]{sev, s[1], id});
                    }
                }
            } else {
                String res = "NONE";
                while (!pq.isEmpty()) {
                    long[] top = pq.poll();
                    long[] s = state.get((int) top[2]);
                    if (s[2] == 1 && s[0] == top[0]) {
                        s[2] = 0;
                        res = names.get((int) top[2]);
                        break;
                    }
                }
                sb.append(res).append('\n');
            }
        }
        System.out.print(sb);
    }
}
