import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean member = sc.next().equals("Y");
        int n = sc.nextInt();
        double total = 0;
        for (int i = 1; i <= n; i++) {
            int d = sc.nextInt();
            double fine = d <= 7 ? 2.0 * d : 14 + 5.0 * (d - 7);
            fine = Math.min(fine, 100);
            if (member) fine /= 2;
            total += fine;
            System.out.printf("Book %d fine: %.2f%n", i, fine);
        }
        System.out.printf("Total fine: %.2f%n", total);
    }
}