import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        long[] a = new long[n];
        for (int i = 0; i < n; i++) a[i] = sc.nextLong();
        long target = sc.nextLong();
        Map<Long, Integer> first = new HashMap<>();
        for (int j = 0; j < n; j++) {
            Integer i = first.get(target - a[j]);
            if (i != null) {
                System.out.println(i + " " + j);
                return;
            }
            first.putIfAbsent(a[j], j);
        }
        System.out.println("No pair");
    }
}
