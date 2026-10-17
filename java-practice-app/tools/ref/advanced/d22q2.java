import java.io.*;
import java.util.*;

public class Main {
    static <T extends Comparable<? super T>> int search(List<? extends T> sorted, T key) {
        int lo = 0, hi = sorted.size();
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            T v = sorted.get(mid);
            if (v.compareTo(key) < 0) lo = mid + 1;
            else hi = mid;
        }
        if (lo < sorted.size() && sorted.get(lo).compareTo(key) == 0) return lo;
        return -lo - 1;
    }

    public static void main(String[] args) throws IOException {
        StringTokenizer st = new StringTokenizer(new String(System.in.readAllBytes()));
        String mode = st.nextToken();
        int n = Integer.parseInt(st.nextToken());
        StringBuilder sb = new StringBuilder();
        if (mode.equals("INT")) {
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < n; i++) list.add(Integer.valueOf(st.nextToken()));
            int q = Integer.parseInt(st.nextToken());
            for (int i = 0; i < q; i++) {
                if (i > 0) sb.append(' ');
                sb.append(search(list, Integer.valueOf(st.nextToken())));
            }
        } else {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < n; i++) list.add(st.nextToken());
            int q = Integer.parseInt(st.nextToken());
            for (int i = 0; i < q; i++) {
                if (i > 0) sb.append(' ');
                sb.append(search(list, st.nextToken()));
            }
        }
        System.out.println(sb);
    }
}
