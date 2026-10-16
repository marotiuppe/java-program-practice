import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt(), m = sc.nextInt();
        int best = 0, bestTotal = -1;
        for (int i = 0; i < n; i++) {
            int t = 0;
            for (int j = 0; j < m; j++) t += sc.nextInt();
            System.out.println("Student " + (i + 1) + " total: " + t);
            if (t > bestTotal) { bestTotal = t; best = i + 1; }
        }
        System.out.println("Topper: Student " + best);
    }
}
