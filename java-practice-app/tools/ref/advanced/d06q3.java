import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        StringTokenizer st = new StringTokenizer(new String(System.in.readAllBytes()));
        int q = Integer.parseInt(st.nextToken());
        PriorityQueue<Long> lo = new PriorityQueue<>(Collections.reverseOrder());
        PriorityQueue<Long> hi = new PriorityQueue<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < q; i++) {
            String cmd = st.nextToken();
            if (cmd.equals("ADD")) {
                long x = Long.parseLong(st.nextToken());
                if (lo.isEmpty() || x <= lo.peek()) {
                    lo.add(x);
                } else {
                    hi.add(x);
                }
                if (lo.size() > hi.size() + 1) {
                    hi.add(lo.poll());
                } else if (hi.size() > lo.size()) {
                    lo.add(hi.poll());
                }
            } else {
                long twice = lo.size() > hi.size() ? 2 * lo.peek() : lo.peek() + hi.peek();
                long abs = Math.abs(twice);
                sb.append(twice < 0 ? "-" : "").append(abs / 2).append(abs % 2 == 0 ? ".0" : ".5").append('\n');
            }
        }
        System.out.print(sb);
    }
}
