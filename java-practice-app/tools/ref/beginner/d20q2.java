import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int r = sc.nextInt(), c = sc.nextInt();
        int[][] a = new int[r][c];
        for (int i = 0; i < r; i++) for (int j = 0; j < c; j++) a[i][j] = sc.nextInt();
        for (int j = 0; j < c; j++) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < r; i++) {
                if (i > 0) sb.append(' ');
                sb.append(a[i][j]);
            }
            System.out.println(sb);
        }
    }
}
