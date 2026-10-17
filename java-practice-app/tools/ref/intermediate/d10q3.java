import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Map<String, List<String>> groups = new LinkedHashMap<>();
        for (int i = 0; i < n; i++) {
            String w = sc.next();
            char[] c = w.toCharArray();
            Arrays.sort(c);
            groups.computeIfAbsent(new String(c), k -> new ArrayList<>()).add(w);
        }
        for (List<String> g : groups.values()) System.out.println(String.join(" ", g));
    }
}
