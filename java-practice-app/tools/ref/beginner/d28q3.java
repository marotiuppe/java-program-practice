import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int total = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            String name = sc.next();
            int stock = sc.nextInt(), min = sc.nextInt();
            if (stock < min) { sb.append(name + ": order " + (min - stock) + "\n"); total += min - stock; }
        }
        if (total == 0) System.out.println("All stocked");
        else System.out.print(sb + "Total units to order: " + total + "\n");
    }
}