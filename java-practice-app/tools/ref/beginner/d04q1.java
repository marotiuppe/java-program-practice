import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int amt = sc.nextInt();
        boolean member = sc.nextBoolean();
        if (amt >= 1000 || (member && amt >= 500)) System.out.println("Delivery: Free");
        else System.out.println("Delivery: 40");
    }
}
