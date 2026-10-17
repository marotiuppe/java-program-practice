public class Main {
    static long uniquePaths(int[][] g) {
        int m = g.length;
        int n = g[0].length;
        long[][] dp = new long[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (g[i][j] == 1) {
                    dp[i][j] = 0;
                } else if (i == 0 && j == 0) {
                    dp[i][j] = 1;
                } else {
                    long up = i > 0 ? dp[i - 1][j] : 0;
                    long left = j > 0 ? dp[i][j - 1] : 0;
                    dp[i][j] = up + left;
                }
            }
        }
        return dp[m - 1][n - 1];
    }

    public static void main(String[] args) {
        System.out.println(uniquePaths(new int[][]{{0, 0, 0}, {0, 1, 0}, {0, 0, 0}}));
        System.out.println(uniquePaths(new int[][]{{0, 1}, {0, 0}}));
        System.out.println(uniquePaths(new int[][]{{0, 0}, {1, 0}}));
        System.out.println(uniquePaths(new int[][]{{1, 0}}));
        System.out.println(uniquePaths(new int[18][18]));
    }
}
