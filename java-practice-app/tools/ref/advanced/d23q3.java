import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        StringTokenizer st = new StringTokenizer(new String(System.in.readAllBytes()));
        int n = Integer.parseInt(st.nextToken());
        TreeMap<String, TreeMap<Long, List<String>>> depts = new TreeMap<>();
        for (int i = 0; i < n; i++) {
            String d = st.nextToken();
            String name = st.nextToken();
            long sal = Long.parseLong(st.nextToken());
            depts.computeIfAbsent(d, k -> new TreeMap<>(Comparator.reverseOrder()))
                 .computeIfAbsent(sal, k -> new ArrayList<>()).add(name);
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, TreeMap<Long, List<String>>> e : depts.entrySet()) {
            sb.append(e.getKey()).append(": ");
            Iterator<Map.Entry<Long, List<String>>> it = e.getValue().entrySet().iterator();
            it.next();
            if (!it.hasNext()) {
                sb.append("N/A\n");
                continue;
            }
            Map.Entry<Long, List<String>> second = it.next();
            List<String> names = second.getValue();
            Collections.sort(names);
            sb.append(String.join(",", names)).append(" (").append(second.getKey()).append(")\n");
        }
        System.out.print(sb);
    }
}
