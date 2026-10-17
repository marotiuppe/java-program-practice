import java.util.*;

public class Main {
    static int countPairs(int[] a, long target) {
        Map<Long, Integer> seen = new HashMap<>();
        int count = 0;
        for (int x : a) {
            long need = target - x;
            count += seen.getOrDefault(need, 0);
            seen.put((long) x, seen.getOrDefault((long) x, 0) + 1);
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println(countPairs(new int[]{1, 5, 7, -1, 5}, 6));
        System.out.println(countPairs(new int[]{3, 3, 3}, 6));
        System.out.println(countPairs(new int[]{-1294967296, 2000000000}, 5000000000L));
    }
}
