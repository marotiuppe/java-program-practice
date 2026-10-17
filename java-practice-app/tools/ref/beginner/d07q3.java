import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double w = sc.nextDouble();
        char zone = sc.next().charAt(0);
        boolean express = sc.nextBoolean();
        if (w <= 0 || w > 30) {
            System.out.println("Invalid weight");
        } else {
            double rate;
            switch (zone) {
                case 'L': rate = 5; break;
                case 'N': rate = 8; break;
                case 'I': rate = 20; break;
                default: rate = -1;
            }
            if (rate < 0) System.out.println("Invalid zone");
            else {
                double cost = w * rate;
                if (express) cost *= 1.5;
                System.out.printf("Cost: %.2f%n", cost);
            }
        }
    }
}
