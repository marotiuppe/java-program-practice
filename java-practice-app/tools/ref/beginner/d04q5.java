import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        if (n % 2 == 0) {
            System.out.println("Even");
        } else {
            System.out.println("Odd");
        }
        if (n > 0 && n < 100) {
            System.out.println("In range 1-99");
        }
        if (n == 0) {
            System.out.println("Zero");
        }
    }
}
