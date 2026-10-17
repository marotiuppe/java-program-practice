import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double a = sc.nextDouble();
        char op = sc.next().charAt(0);
        double b = sc.nextDouble();
        switch (op) {
            case '+': System.out.printf("%.2f%n", a + b); break;
            case '-': System.out.printf("%.2f%n", a - b); break;
            case '*': System.out.printf("%.2f%n", a * b); break;
            case '/':
                if (b == 0) System.out.println("Cannot divide by zero");
                else System.out.printf("%.2f%n", a / b);
                break;
            default: System.out.println("Invalid operator");
        }
    }
}
