import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int bal = sc.nextInt(), w = sc.nextInt();
        if (w <= 0 || w % 100 != 0) System.out.println("Invalid amount");
        else if (bal - w < 500) System.out.println("Insufficient balance");
        else System.out.println("Withdrawn. New balance: " + (bal - w));
    }
}
