import java.util.*;

public class Main {
    static void rotate(int[][] m) {
        int n = m.length;
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++) {
                int t = m[i][j]; m[i][j] = m[j][i]; m[j][i] = t;
            }
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n / 2; j++) {
                int t = m[i][j]; m[i][j] = m[i][n - 1 - j]; m[i][n - 1 - j] = t;
            }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        long k = sc.nextLong();
        int[][] m = new int[n][n];
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) m[i][j] = sc.nextInt();
        for (long t = k % 4; t > 0; t--) rotate(m);
        StringBuilder sb = new StringBuilder();
        for (int[] row : m) {
            for (int j = 0; j < n; j++) {
                if (j > 0) sb.append(' ');
                sb.append(row[j]);
            }
            sb.append("\n");
        }
        System.out.print(sb);
    }
}
