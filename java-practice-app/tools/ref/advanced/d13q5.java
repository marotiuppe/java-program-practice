import java.util.ArrayList;
import java.util.List;

public class Main {
    static boolean hasCycle(int n, List<List<Integer>> g) {
        int[] state = new int[n];
        for (int i = 0; i < n; i++) {
            if (state[i] == 0 && dfs(i, g, state)) {
                return true;
            }
        }
        return false;
    }

    static boolean dfs(int u, List<List<Integer>> g, int[] state) {
        state[u] = 1;
        for (int v : g.get(u)) {
            if (state[v] == 1) {
                return true;
            }
            if (state[v] == 0 && dfs(v, g, state)) {
                return true;
            }
        }
        state[u] = 2;
        return false;
    }

    static List<List<Integer>> build(int n, int[][] edges) {
        List<List<Integer>> g = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            g.add(new ArrayList<>());
        }
        for (int[] e : edges) {
            g.get(e[0]).add(e[1]);
        }
        return g;
    }

    public static void main(String[] args) {
        System.out.println(hasCycle(3, build(3, new int[][]{{0, 1}, {1, 2}, {2, 0}})));
        System.out.println(hasCycle(4, build(4, new int[][]{{0, 1}, {0, 2}, {1, 3}, {2, 3}})));
        System.out.println(hasCycle(4, build(4, new int[][]{{0, 1}, {2, 3}, {3, 2}})));
    }
}
