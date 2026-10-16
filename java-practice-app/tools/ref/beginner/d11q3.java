import java.util.*;

public class Main {
    static long rev(long x) {
        long r = 0;
        while (x > 0) { r = r * 10 + x % 10; x /= 10; }
        return r;
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        long n = sc.nextLong();
        int steps = 0;
        while (rev(n) != n) { n += rev(n); steps++; }
        System.out.println("Steps: " + steps);
        System.out.println("Palindrome: " + n);
    }
}
