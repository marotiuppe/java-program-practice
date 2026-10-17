import java.util.*;

// Time complexity: O(n) using the sum formula
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        long expected = (long) n * (n + 1) / 2, sum = 0;
        for (int i = 0; i < n; i++) sum += sc.nextInt();
        System.out.println("Missing: " + (expected - sum));
    }
}