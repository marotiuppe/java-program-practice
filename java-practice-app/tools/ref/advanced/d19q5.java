import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    static List<int[]> merge(int[][] intervals) {
        List<int[]> res = new ArrayList<>();
        if (intervals.length == 0) {
            return res;
        }
        int[][] sorted = new int[intervals.length][];
        for (int i = 0; i < intervals.length; i++) {
            sorted[i] = intervals[i].clone();
        }
        Arrays.sort(sorted, (x, y) -> Integer.compare(x[0], y[0]));
        int[] cur = sorted[0];
        for (int i = 1; i < sorted.length; i++) {
            if (sorted[i][0] <= cur[1]) {
                cur[1] = Math.max(cur[1], sorted[i][1]);
            } else {
                res.add(cur);
                cur = sorted[i];
            }
        }
        res.add(cur);
        return res;
    }

    public static void main(String[] args) {
        int[][][] tests = {
            {{1, 3}, {8, 10}, {2, 6}, {15, 18}, {17, 20}},
            {{1, 4}, {4, 5}},
            {{1, 10}, {2, 3}}
        };
        for (int[][] t : tests) {
            StringBuilder sb = new StringBuilder();
            for (int[] p : merge(t)) {
                sb.append('[').append(p[0]).append(',').append(p[1]).append("] ");
            }
            System.out.println(sb.toString().trim());
        }
    }
}
