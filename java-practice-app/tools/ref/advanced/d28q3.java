import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        StringTokenizer st = new StringTokenizer(new String(System.in.readAllBytes()));
        long s = Long.parseLong(st.nextToken());
        String dir = st.nextToken();
        int m = Integer.parseInt(st.nextToken());
        TreeSet<Long> set = new TreeSet<>();
        for (int i = 0; i < m; i++) set.add(Long.parseLong(st.nextToken()));
        List<Long> order = new ArrayList<>();
        if (dir.equals("UP")) {
            order.addAll(set.tailSet(s, true));
            order.addAll(set.headSet(s, false).descendingSet());
        } else {
            order.addAll(set.headSet(s, true).descendingSet());
            order.addAll(set.tailSet(s, false));
        }
        long dist = 0, cur = s;
        StringBuilder sb = new StringBuilder("Order:");
        for (long f : order) {
            dist += Math.abs(f - cur);
            cur = f;
            sb.append(' ').append(f);
        }
        System.out.println(sb);
        System.out.println("Distance: " + dist);
    }
}
