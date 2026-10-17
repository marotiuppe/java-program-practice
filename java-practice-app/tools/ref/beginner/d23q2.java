import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Map<String, Integer> m = new TreeMap<>();
        for (int i = 0; i < n; i++) m.merge(sc.next(), 1, Integer::sum);
        for (Map.Entry<String, Integer> e : m.entrySet()) System.out.println(e.getKey() + ": " + e.getValue());
    }
}