import java.util.*;

public class Main {
    static int gcd(int a, int b) { return b == 0 ? a : gcd(b, a % b); }
    static long lcm(int a, int b) { return (a == 0 || b == 0) ? 0 : (long) a / gcd(a, b) * b; }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt(), b = sc.nextInt();
        System.out.println("GCD: " + gcd(a, b));
        System.out.println("LCM: " + lcm(a, b));
    }
}
