import java.util.*;

public class Main {
    static long pow(long x, int n) { return n == 0 ? 1 : x * pow(x, n - 1); }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        long x = sc.nextLong();
        int n = sc.nextInt();
        System.out.println(pow(x, n));
    }
}