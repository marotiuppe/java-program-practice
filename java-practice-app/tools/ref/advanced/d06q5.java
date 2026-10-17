import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Main {
    static List<Integer> merge(int[][] lists) {
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
        for (int i = 0; i < lists.length; i++) {
            if (lists[i].length > 0) {
                pq.add(new int[]{lists[i][0], i, 0});
            }
        }
        List<Integer> out = new ArrayList<>();
        while (!pq.isEmpty()) {
            int[] top = pq.poll();
            out.add(top[0]);
            int next = top[2] + 1;
            if (next < lists[top[1]].length) {
                pq.add(new int[]{lists[top[1]][next], top[1], next});
            }
        }
        return out;
    }

    public static void main(String[] args) {
        try {
            System.out.println(merge(new int[][]{{1, 4, 7}, {2, 5}, {}}));
        } catch (RuntimeException e) {
            System.out.println("crashed: " + e.getClass().getSimpleName());
        }
        System.out.println(merge(new int[][]{{Integer.MIN_VALUE, 5}, {Integer.MAX_VALUE}}));
        System.out.println(merge(new int[][]{{3, 6, 9}, {1, 2}}));
    }
}
