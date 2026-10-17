import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int digits = String.valueOf(n).length();
        long sum = 0;
        for (int t = n; t > 0; t /= 10) {
            long p = 1;
            for (int i = 0; i < digits; i++) p *= t % 10;
            sum += p;
        }
        System.out.println(sum == n ? "Armstrong" : "Not Armstrong");
    }
}
