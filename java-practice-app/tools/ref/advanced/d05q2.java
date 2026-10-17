import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        StringTokenizer st = new StringTokenizer(new String(System.in.readAllBytes()));
        int n = Integer.parseInt(st.nextToken());
        long k = Long.parseLong(st.nextToken());
        Map<Long, Integer> seen = new HashMap<>();
        seen.put(0L, 1);
        long sum = 0;
        long count = 0;
        for (int i = 0; i < n; i++) {
            sum += Long.parseLong(st.nextToken());
            count += seen.getOrDefault(sum - k, 0);
            seen.merge(sum, 1, Integer::sum);
        }
        System.out.println(count);
    }
}
