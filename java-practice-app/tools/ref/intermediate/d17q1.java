import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt(), k = sc.nextInt();
        long[] a = new long[n];
        for (int i = 0; i < n; i++) a[i] = sc.nextLong();
        long sum = 0;
        for (int i = 0; i < k; i++) sum += a[i];
        long best = sum;
        int start = 0;
        for (int i = k; i < n; i++) {
            sum += a[i] - a[i - k];
            if (sum > best) {
                best = sum;
                start = i - k + 1;
            }
        }
        System.out.println("Start: " + start);
        System.out.println("Sum: " + best);
        System.out.printf("Average: %.2f%n", (double) best / k);
    }
}
