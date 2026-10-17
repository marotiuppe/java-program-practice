import java.util.*;

public class Main {
    static long pow(long a, long b, long m) {
        long r = 1 % m;
        a %= m;
        while (b > 0) {
            if ((b & 1) == 1) r = r * a % m;
            a = a * a % m;
            b >>= 1;
        }
        return r;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        while (t-- > 0) {
            long a = sc.nextLong(), b = sc.nextLong(), m = sc.nextLong();
            System.out.println(pow(a, b, m));
        }
    }
}
