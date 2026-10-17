import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String a = sc.next(), b = sc.next();
        try {
            int x = Integer.parseInt(a), y = Integer.parseInt(b);
            System.out.println("Result: " + (x / y));
        } catch (ArithmeticException e) {
            System.out.println("Cannot divide by zero");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input");
        } finally {
            System.out.println("Done");
        }
    }
}