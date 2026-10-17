import java.util.*;

public class Main {
    static void transpose(int[][] m) {
        int n = m.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int t = m[i][j];
                m[i][j] = m[j][i];
                m[j][i] = t;
            }
        }
    }

    static int[] columnSums(int[][] m) {
        int[] sums = new int[m[0].length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                sums[j] += m[i][j];
            }
        }
        return sums;
    }

    static boolean isSymmetric(int[][] m) {
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < i; j++) {
                if (m[i][j] != m[j][i]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        int[][] a = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        transpose(a);
        for (int[] row : a) {
            System.out.println(Arrays.toString(row));
        }
        int[][] b = {{1, 2, 3}, {4, 5, 6}};
        System.out.println(Arrays.toString(columnSums(b)));
        System.out.println(isSymmetric(new int[][]{{1, 2}, {2, 1}}));
        System.out.println(isSymmetric(new int[][]{{1, 2}, {3, 1}}));
    }
}
