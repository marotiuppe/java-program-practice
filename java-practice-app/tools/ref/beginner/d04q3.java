import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        long a = sc.nextLong(), b = sc.nextLong(), c = sc.nextLong();
        boolean ok = a > 0 && b > 0 && c > 0 && a + b > c && a + c > b && b + c > a;
        System.out.println(ok ? "Valid" : "Invalid");
    }
}
