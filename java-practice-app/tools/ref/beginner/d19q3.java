import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = sc.nextInt();
        int t = sc.nextInt();
        int lo = 0, hi = n - 1, ans = -1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid] == t) { ans = mid; break; }
            else if (a[mid] < t) lo = mid + 1;
            else hi = mid - 1;
        }
        System.out.println(ans);
    }
}
