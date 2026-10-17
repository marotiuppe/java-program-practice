import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Set<Long> set = new HashSet<>();
        for (int i = 0; i < n; i++) set.add(sc.nextLong());
        int best = 0;
        for (long v : set) {
            if (!set.contains(v - 1)) {
                int len = 1;
                long x = v;
                while (set.contains(x + 1)) {
                    x++;
                    len++;
                }
                best = Math.max(best, len);
            }
        }
        System.out.println(best);
    }
}
