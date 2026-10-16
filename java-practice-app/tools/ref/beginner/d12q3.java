import java.util.*;

public class Main {
    static boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i * i <= n; i++) if (n % i == 0) return false;
        return true;
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt(), b = sc.nextInt();
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (int i = a; i <= b; i++) {
            if (isPrime(i)) {
                if (count > 0) sb.append(' ');
                sb.append(i);
                count++;
            }
        }
        if (count == 0) System.out.println("No primes");
        else {
            System.out.println(sb);
            System.out.println("Count: " + count);
        }
    }
}
