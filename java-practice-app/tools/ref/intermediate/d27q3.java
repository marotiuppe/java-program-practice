import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int amount = sc.nextInt(), m = sc.nextInt();
        int[] coins = new int[m];
        for (int i = 0; i < m; i++) coins[i] = sc.nextInt();
        int INF = Integer.MAX_VALUE;
        int[] min = new int[amount + 1];
        Arrays.fill(min, INF);
        min[0] = 0;
        for (int a = 1; a <= amount; a++)
            for (int c : coins)
                if (a >= c && min[a - c] != INF) min[a] = Math.min(min[a], min[a - c] + 1);
        long[] ways = new long[amount + 1];
        ways[0] = 1;
        for (int c : coins)
            for (int s = c; s <= amount; s++) ways[s] += ways[s - c];
        System.out.println("Min coins: " + (min[amount] == INF ? -1 : min[amount]));
        System.out.println("Ways: " + ways[amount]);
    }
}
