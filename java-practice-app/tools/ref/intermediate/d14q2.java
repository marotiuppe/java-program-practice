import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Map<Integer, Integer> freq = new HashMap<>();
        for (int i = 0; i < n; i++) freq.merge(sc.nextInt(), 1, Integer::sum);
        int k = sc.nextInt();
        List<Map.Entry<Integer, Integer>> es = new ArrayList<>(freq.entrySet());
        es.sort((a, b) -> {
            int c = Integer.compare(b.getValue(), a.getValue());
            return c != 0 ? c : Integer.compare(a.getKey(), b.getKey());
        });
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < k; i++) {
            if (i > 0) sb.append(' ');
            sb.append(es.get(i).getKey());
        }
        System.out.println(sb);
    }
}
