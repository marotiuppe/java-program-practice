import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double bill = sc.nextDouble();
        char type = sc.next().charAt(0);
        double discount = 0;
        switch (type) {
            case 'G':
                discount = 20;
                break;
            case 'S':
                discount = 10;
                break;
        }
        if (bill > 2000)
            discount += 5;
        double finalBill = bill - bill * discount / 100;
        System.out.printf("Final bill: %.2f%n", finalBill);
    }
}
