import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        StreamTokenizer in = new StreamTokenizer(new BufferedInputStream(System.in));
        in.nextToken();
        int amount = (int) in.nval;
        in.nextToken();
        int k = (int) in.nval;
        int[] coins = new int[k];
        for (int i = 0; i < k; i++) {
            in.nextToken();
            coins[i] = (int) in.nval;
        }
        final long MOD = 1_000_000_007L;
        long[] ways = new long[amount + 1];
        int[] min = new int[amount + 1];
        Arrays.fill(min, Integer.MAX_VALUE);
        ways[0] = 1;
        min[0] = 0;
        for (int c : coins) {
            for (int s = c; s <= amount; s++) {
                ways[s] = (ways[s] + ways[s - c]) % MOD;
                if (min[s - c] != Integer.MAX_VALUE) min[s] = Math.min(min[s], min[s - c] + 1);
            }
        }
        System.out.println(ways[amount] + " " + (min[amount] == Integer.MAX_VALUE ? -1 : min[amount]));
    }
}
