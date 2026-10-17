import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int r = sc.nextInt(), c = sc.nextInt();
        char[][] g = new char[r][];
        for (int i = 0; i < r; i++) g[i] = sc.next().toCharArray();
        int q = sc.nextInt();
        StringBuilder sb = new StringBuilder();
        while (q-- > 0) {
            int k = sc.nextInt();
            boolean done = false;
            for (int i = 0; i < r && !done; i++) {
                int run = 0;
                for (int j = 0; j < c; j++) {
                    run = g[i][j] == '.' ? run + 1 : 0;
                    if (run == k) {
                        for (int t = j - k + 1; t <= j; t++) g[i][t] = 'X';
                        sb.append("Row ").append(i + 1).append(" Seats ").append(j - k + 2).append("-").append(j + 1).append("\n");
                        done = true;
                        break;
                    }
                }
            }
            if (!done) sb.append("Not available\n");
        }
        for (char[] row : g) sb.append(new String(row)).append("\n");
        System.out.print(sb);
    }
}
