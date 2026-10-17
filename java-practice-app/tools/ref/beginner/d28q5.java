
import java.util.Scanner;

public class Main {
    static double discountFor(double amount) {
        if (amount >= 5000) return 0.15;
        if (amount >= 2000) return 0.10;
        else return 0;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        double total = 0;
        for (int i = 0; i < n; i++) {
            String item = sc.next();
            double price = sc.nextDouble();
            int qty = sc.nextInt();
            total += price * qty;
        }
        double discount = total * discountFor(total);
        double finalAmount = total - discount;
        System.out.printf("Total: %.2f%n", total);
        System.out.printf("Discount: %.2f%n", discount);
        System.out.printf("Final: %.2f%n", finalAmount);
    }
}