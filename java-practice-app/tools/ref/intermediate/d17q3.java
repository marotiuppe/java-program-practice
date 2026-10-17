import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt(), k = sc.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = sc.nextInt();
        int left = 0, zeros = 0, best = 0;
        for (int r = 0; r < n; r++) {
            if (a[r] == 0) zeros++;
            while (zeros > k) {
                if (a[left++] == 0) zeros--;
            }
            best = Math.max(best, r - left + 1);
        }
        System.out.println(best);
    }
}
