import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = sc.nextInt();
        long k = sc.nextLong();
        Map<Long, Long> seen = new HashMap<>();
        seen.put(0L, 1L);
        long sum = 0, count = 0;
        for (int x : a) {
            sum += x;
            count += seen.getOrDefault(sum - k, 0L);
            seen.merge(sum, 1L, Long::sum);
        }
        System.out.println(count);
    }
}
