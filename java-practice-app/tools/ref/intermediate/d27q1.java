import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        long[] c = new long[n];
        for (int i = 0; i < n; i++) c[i] = sc.nextLong();
        long[] f = new long[n];
        f[0] = c[0];
        f[1] = c[1];
        for (int i = 2; i < n; i++) f[i] = c[i] + Math.min(f[i - 1], f[i - 2]);
        System.out.println(Math.min(f[n - 1], f[n - 2]));
    }
}
