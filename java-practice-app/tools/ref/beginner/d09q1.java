import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int left = 3;
        while (left > 0) {
            int pin = sc.nextInt();
            if (pin == 4321) {
                System.out.println("Access granted");
                return;
            }
            left--;
            System.out.println("Wrong PIN. Attempts left: " + left);
        }
        System.out.println("Card blocked");
    }
}
