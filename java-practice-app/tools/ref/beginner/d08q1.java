import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int dep = sc.nextInt(), months = sc.nextInt();
        int total = 0;
        for (int i = 1; i <= months; i++) {
            total += dep;
            System.out.println("Month " + i + ": " + total);
        }
        System.out.println("Total saved: " + total);
    }
}
