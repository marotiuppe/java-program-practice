import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = sc.nextInt();
        StringBuilder sb = new StringBuilder();
        int swaps = 0, passes = 0;
        while (true) {
            passes++;
            int s = 0;
            for (int i = 0; i + 1 < n; i++) {
                if (a[i] > a[i + 1]) {
                    int t = a[i];
                    a[i] = a[i + 1];
                    a[i + 1] = t;
                    s++;
                }
            }
            if (s == 0) break;
            swaps += s;
            sb.append("Pass ").append(passes).append(':');
            for (int x : a) sb.append(' ').append(x);
            sb.append('\n');
        }
        sb.append("Swaps: ").append(swaps).append('\n').append("Passes: ").append(passes).append('\n');
        System.out.print(sb);
    }
}
