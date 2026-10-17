import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.next();
        try {
            int q = Integer.parseInt(s);
            if (q < 1 || q > 6) System.out.println("Allowed: 1 to 6");
            else System.out.println("Booked " + q + " tickets");
        } catch (NumberFormatException e) {
            System.out.println("Invalid number");
        }
    }
}