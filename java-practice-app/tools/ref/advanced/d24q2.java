import java.io.*;
import java.util.*;

public class Main {
    record User(Optional<String> city, Optional<Integer> manager) {}

    public static void main(String[] args) throws IOException {
        StringTokenizer st = new StringTokenizer(new String(System.in.readAllBytes()));
        int n = Integer.parseInt(st.nextToken());
        Map<Integer, User> users = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int id = Integer.parseInt(st.nextToken());
            String city = st.nextToken();
            String mgr = st.nextToken();
            users.put(id, new User(city.equals("-") ? Optional.empty() : Optional.of(city),
                    mgr.equals("-") ? Optional.empty() : Optional.of(Integer.parseInt(mgr))));
        }
        Map<Integer, String> memo = new HashMap<>();
        Set<Integer> onPath = new HashSet<>();
        int q = Integer.parseInt(st.nextToken());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < q; i++) {
            int id = Integer.parseInt(st.nextToken());
            if (!users.containsKey(id)) {
                sb.append("NO SUCH USER\n");
                continue;
            }
            List<Integer> path = new ArrayList<>();
            onPath.clear();
            String result = "UNKNOWN";
            int cur = id;
            while (true) {
                if (memo.containsKey(cur)) {
                    result = memo.get(cur);
                    break;
                }
                User u = users.get(cur);
                if (u == null || !onPath.add(cur)) {
                    result = "UNKNOWN";
                    break;
                }
                path.add(cur);
                if (u.city().isPresent()) {
                    result = u.city().get();
                    break;
                }
                if (u.manager().isEmpty()) {
                    result = "UNKNOWN";
                    break;
                }
                cur = u.manager().get();
            }
            for (int p : path) memo.put(p, result);
            sb.append(result).append('\n');
        }
        System.out.print(sb);
    }
}
