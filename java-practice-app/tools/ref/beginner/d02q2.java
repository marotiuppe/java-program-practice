import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double c = sc.nextDouble();
        System.out.printf("Fahrenheit: %.2f%nKelvin: %.2f%n", c * 9 / 5 + 32, c + 273.15);
    }
}
