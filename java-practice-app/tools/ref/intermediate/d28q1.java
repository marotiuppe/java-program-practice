import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Map<Long, Long> cnt = new HashMap<>();
        long pairs = 0;
        for (int i = 0; i < n; i++) {
            long a = sc.nextLong();
            long c = cnt.getOrDefault(a, 0L);
            pairs += c;
            cnt.put(a, c + 1);
        }
        long nn = n;
        System.out.println("Pairs: " + pairs);
        System.out.println("Brute-force comparisons: " + nn * (nn - 1) / 2);
    }
}
