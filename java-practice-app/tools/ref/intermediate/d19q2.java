import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = sc.nextInt();
        int lo = 0, mid = 0, hi = n - 1;
        while (mid <= hi) {
            if (a[mid] == 0) {
                int t = a[lo];
                a[lo++] = a[mid];
                a[mid++] = t;
            } else if (a[mid] == 1) {
                mid++;
            } else {
                int t = a[hi];
                a[hi--] = a[mid];
                a[mid] = t;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            if (i > 0) sb.append(' ');
            sb.append(a[i]);
        }
        System.out.println(sb);
    }
}
