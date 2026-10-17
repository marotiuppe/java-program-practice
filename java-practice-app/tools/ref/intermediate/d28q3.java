import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        long sum = 0, sq = 0;
        for (int i = 1; i <= n; i++) {
            long x = sc.nextLong();
            sum += x - i;
            sq += x * x - (long) i * i;
        }
        long dPlusM = sq / sum;
        long d = (dPlusM + sum) / 2;
        long m = dPlusM - d;
        System.out.println("Duplicate: " + d);
        System.out.println("Missing: " + m);
    }
}
