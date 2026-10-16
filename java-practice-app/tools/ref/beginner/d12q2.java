import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        boolean prime = n > 1;
        for (int i = 2; (long) i * i <= n && prime; i++) {
            if (n % i == 0) prime = false;
        }
        System.out.println(prime ? "Prime" : "Not prime");
    }
}
