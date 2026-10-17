import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int p = sc.nextInt();
        double bill = sc.nextDouble();
        double total = bill * 1.05;
        System.out.printf("Total with GST: %.2f%nEach pays: %.2f%n", total, total / p);
    }
}
