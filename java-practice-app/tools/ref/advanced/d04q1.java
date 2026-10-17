import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        StringTokenizer st = new StringTokenizer(new String(System.in.readAllBytes()));
        long w = Long.parseLong(st.nextToken());
        int l = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        ArrayDeque<Long> accepted = new ArrayDeque<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < m; i++) {
            long t = Long.parseLong(st.nextToken());
            while (!accepted.isEmpty() && accepted.peekFirst() <= t - w) {
                accepted.pollFirst();
            }
            if (i > 0) {
                sb.append(' ');
            }
            if (accepted.size() < l) {
                accepted.addLast(t);
                sb.append('A');
            } else {
                sb.append('R');
            }
        }
        System.out.println(sb);
    }
}
