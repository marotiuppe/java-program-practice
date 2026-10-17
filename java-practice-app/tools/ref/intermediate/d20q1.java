import java.util.*;

public class Main {
    static int[] coins;
    static int count = 0;
    static StringBuilder sb = new StringBuilder();
    static Deque<Integer> cur = new ArrayDeque<>();

    static void go(int remaining, int start) {
        if (remaining == 0) {
            count++;
            boolean first = true;
            for (Iterator<Integer> it = cur.descendingIterator(); it.hasNext(); ) {
                if (!first) sb.append('+');
                sb.append(it.next());
                first = false;
            }
            sb.append('\n');
            return;
        }
        for (int i = start; i < coins.length; i++) {
            if (coins[i] > remaining) break;
            cur.push(coins[i]);
            go(remaining - coins[i], i);
            cur.pop();
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int amount = sc.nextInt();
        int m = sc.nextInt();
        coins = new int[m];
        for (int i = 0; i < m; i++) coins[i] = sc.nextInt();
        Arrays.sort(coins);
        go(amount, 0);
        sb.append("Total ways: ").append(count).append('\n');
        System.out.print(sb);
    }
}
