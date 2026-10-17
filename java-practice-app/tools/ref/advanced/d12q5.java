import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Main {
    static int components(int n, int[][] edges) {
        List<List<Integer>> g = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            g.add(new ArrayList<>());
        }
        for (int[] e : edges) {
            g.get(e[0]).add(e[1]);
            g.get(e[1]).add(e[0]);
        }
        boolean[] seen = new boolean[n + 1];
        int count = 0;
        for (int s = 1; s <= n; s++) {
            if (seen[s]) {
                continue;
            }
            count++;
            Deque<Integer> queue = new ArrayDeque<>();
            queue.add(s);
            seen[s] = true;
            while (!queue.isEmpty()) {
                int u = queue.poll();
                for (int v : g.get(u)) {
                    if (!seen[v]) {
                        seen[v] = true;
                        queue.add(v);
                    }
                }
            }
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println(components(5, new int[][]{{1, 2}, {2, 3}, {4, 5}}));
        System.out.println(components(4, new int[][]{}));
        System.out.println(components(6, new int[][]{{1, 2}, {2, 3}, {3, 1}, {5, 6}}));
    }
}
