import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Map<String, Set<String>> m = new LinkedHashMap<>();
        for (int i = 0; i < n; i++) {
            String u = sc.next();
            String ip = sc.next();
            m.computeIfAbsent(u, k -> new HashSet<>()).add(ip);
        }
        for (Map.Entry<String, Set<String>> e : m.entrySet()) {
            int c = e.getValue().size();
            System.out.println(e.getKey() + " " + c + " " + (c >= 3 ? "SUSPICIOUS" : "OK"));
        }
    }
}
