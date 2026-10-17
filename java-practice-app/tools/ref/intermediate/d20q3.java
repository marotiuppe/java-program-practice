import java.util.*;

public class Main {
    static int n;
    static int[] col;
    static boolean[] cols, d1, d2;
    static int count = 0;
    static String first = null;

    static void place(int r) {
        if (r == n) {
            count++;
            if (first == null) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < n; i++) {
                    if (i > 0) sb.append(' ');
                    sb.append(col[i] + 1);
                }
                first = sb.toString();
            }
            return;
        }
        for (int c = 0; c < n; c++) {
            if (cols[c] || d1[r + c] || d2[r - c + n - 1]) continue;
            cols[c] = d1[r + c] = d2[r - c + n - 1] = true;
            col[r] = c;
            place(r + 1);
            cols[c] = d1[r + c] = d2[r - c + n - 1] = false;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        col = new int[n];
        cols = new boolean[n];
        d1 = new boolean[2 * n];
        d2 = new boolean[2 * n];
        place(0);
        System.out.println("Solutions: " + count);
        System.out.println("First: " + (first == null ? "none" : first));
    }
}
