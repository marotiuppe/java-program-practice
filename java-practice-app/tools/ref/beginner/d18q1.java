import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[] a = new int[7];
        long total = 0;
        int best = 0;
        for (int i = 0; i < 7; i++) {
            a[i] = sc.nextInt();
            total += a[i];
            if (a[i] > a[best]) best = i;
        }
        System.out.println("Total: " + total);
        System.out.printf("Average: %.2f%n", total / 7.0);
        System.out.println("Best day: " + (best + 1) + " (" + a[best] + ")");
    }
}
