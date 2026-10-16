import java.util.*;

public class Main {
    static boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i * i <= n; i++) if (n % i == 0) return false;
        return true;
    }
    static int reverse(int n) {
        int r = 0;
        while (n > 0) { r = r * 10 + n % 10; n /= 10; }
        return r;
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int r = reverse(n);
        System.out.println(isPrime(n) && r != n && isPrime(r) ? "Emirp" : "Not emirp");
    }
}
