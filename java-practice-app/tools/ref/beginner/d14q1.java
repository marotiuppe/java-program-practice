import java.util.*;

public class Main {
    static double itemTotal(double price, int qty) { return price * qty; }
    static double applyDiscount(double amount) { return amount >= 1000 ? amount * 0.9 : amount; }
    static double addGST(double amount) { return amount * 1.18; }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double p1 = sc.nextDouble(); int q1 = sc.nextInt();
        double p2 = sc.nextDouble(); int q2 = sc.nextInt();
        double sub = itemTotal(p1, q1) + itemTotal(p2, q2);
        System.out.printf("Final amount: %.2f%n", addGST(applyDiscount(sub)));
    }
}
