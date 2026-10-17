import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double km = sc.nextDouble();
        int min = sc.nextInt();
        System.out.printf("Fare: %.2f%n", 40 + 12 * km + 2 * min);
    }
}
