import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        StreamTokenizer in = new StreamTokenizer(new BufferedInputStream(System.in));
        in.nextToken();
        int R = (int) in.nval;
        in.nextToken();
        int C = (int) in.nval;
        int[][] a = new int[R][C];
        for (int i = 0; i < R; i++)
            for (int j = 0; j < C; j++) {
                in.nextToken();
                a[i][j] = (int) in.nval;
            }
        int[][] need = new int[R][C];
        for (int i = R - 1; i >= 0; i--) {
            for (int j = C - 1; j >= 0; j--) {
                int next;
                if (i == R - 1 && j == C - 1) next = 1;
                else if (i == R - 1) next = need[i][j + 1];
                else if (j == C - 1) next = need[i + 1][j];
                else next = Math.min(need[i + 1][j], need[i][j + 1]);
                need[i][j] = Math.max(1, next - a[i][j]);
            }
        }
        System.out.println(need[0][0]);
    }
}
