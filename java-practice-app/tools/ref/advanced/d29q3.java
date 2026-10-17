import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        StringTokenizer st = new StringTokenizer(new String(System.in.readAllBytes()));
        int n = Integer.parseInt(st.nextToken());
        long k = Long.parseLong(st.nextToken());
        HashMap<Long, Integer> first = new HashMap<>();
        first.put(0L, 0);
        long prefix = 0;
        int best = 0;
        for (int i = 1; i <= n; i++) {
            prefix += Long.parseLong(st.nextToken());
            long r = ((prefix % k) + k) % k;
            Integer j = first.get(r);
            if (j == null) {
                first.put(r, i);
            } else {
                best = Math.max(best, i - j);
            }
        }
        System.out.println(best);
    }
}
