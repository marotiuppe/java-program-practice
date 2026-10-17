import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int q = Integer.parseInt(br.readLine().trim());
        Map<String, LinkedHashMap<String, Boolean>> topics = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < q; i++) {
            String line = br.readLine();
            String[] p = line.split(" ", 3);
            switch (p[0]) {
                case "SUB":
                    topics.computeIfAbsent(p[2], k -> new LinkedHashMap<>()).putIfAbsent(p[1], false);
                    break;
                case "SUBONCE":
                    topics.computeIfAbsent(p[2], k -> new LinkedHashMap<>()).putIfAbsent(p[1], true);
                    break;
                case "UNSUB": {
                    LinkedHashMap<String, Boolean> m = topics.get(p[2]);
                    if (m != null) m.remove(p[1]);
                    break;
                }
                default: {
                    String msg = p.length > 2 ? p[2] : "";
                    LinkedHashMap<String, Boolean> m = topics.get(p[1]);
                    if (m == null || m.isEmpty()) {
                        sb.append("no subscribers\n");
                        break;
                    }
                    List<Map.Entry<String, Boolean>> snapshot = new ArrayList<>(m.entrySet());
                    for (Map.Entry<String, Boolean> e : snapshot) {
                        sb.append(e.getKey()).append(" got ").append(msg).append('\n');
                    }
                    m.entrySet().removeIf(Map.Entry::getValue);
                }
            }
        }
        System.out.print(sb);
    }
}
