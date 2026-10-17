import java.util.*;

// Time complexity: O(n) sliding window
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt(), k = sc.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = sc.nextInt();
        int cur = 0;
        for (int i = 0; i < k; i++) cur += a[i];
        int best = cur;
        for (int i = k; i < n; i++) { cur += a[i] - a[i - k]; best = Math.max(best, cur); }
        System.out.println(best);
    }
}