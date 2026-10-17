import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    static void backtrack(int[] a, int start, List<Integer> cur, List<List<Integer>> res) {
        res.add(new ArrayList<>(cur));
        for (int i = start; i < a.length; i++) {
            if (i > start && a[i] == a[i - 1]) {
                continue;
            }
            cur.add(a[i]);
            backtrack(a, i + 1, cur, res);
            cur.remove(cur.size() - 1);
        }
    }

    static List<List<Integer>> subsetsWithDup(int[] a) {
        int[] sorted = a.clone();
        Arrays.sort(sorted);
        List<List<Integer>> res = new ArrayList<>();
        backtrack(sorted, 0, new ArrayList<>(), res);
        return res;
    }

    public static void main(String[] args) {
        System.out.println(subsetsWithDup(new int[]{2, 1, 2}));
    }
}
