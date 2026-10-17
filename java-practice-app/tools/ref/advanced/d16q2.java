import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String a = br.readLine().trim();
        String b = br.readLine().trim();
        int n = a.length(), m = b.length();
        int[][] f = new int[n + 2][m + 2];
        for (int i = n - 1; i >= 0; i--)
            for (int j = m - 1; j >= 0; j--)
                f[i][j] = a.charAt(i) == b.charAt(j) ? f[i + 1][j + 1] + 1 : Math.max(f[i + 1][j], f[i][j + 1]);
        StringBuilder sb = new StringBuilder();
        int i = 0, j = 0;
        while (f[i][j] > 0) {
            char best = 0;
            int bi = -1, bj = -1;
            // try each character in ascending order, using its first occurrence at or after i / j
            for (char c = 'A'; c <= 'z'; c++) {
                int pa = a.indexOf(c, i);
                if (pa < 0) continue;
                int pb = b.indexOf(c, j);
                if (pb < 0) continue;
                if (1 + f[pa + 1][pb + 1] == f[i][j]) {
                    best = c;
                    bi = pa;
                    bj = pb;
                    break;
                }
            }
            sb.append(best);
            i = bi + 1;
            j = bj + 1;
        }
        System.out.println(f[0][0]);
        System.out.println(sb);
    }
}
