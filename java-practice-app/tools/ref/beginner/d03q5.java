import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int rupees = 1000;
        double rate = 83.5;
        double dollars = rupees / rate;
        long revenue = 5000000000L;
        float tax = 0.18f;
        System.out.println("Dollars: " + dollars);
        System.out.println("Revenue: " + revenue);
        System.out.println("Tax: " + tax);
    }
}
