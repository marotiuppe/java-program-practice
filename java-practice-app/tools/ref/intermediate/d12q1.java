import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        PriorityQueue<Object[]> pq = new PriorityQueue<>((a, b) -> {
            int c = Integer.compare((int) b[1], (int) a[1]);
            if (c != 0) return c;
            return Long.compare((long) a[2], (long) b[2]);
        });
        long seq = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            String cmd = sc.next();
            if (cmd.equals("ARRIVE")) {
                String name = sc.next();
                int sev = sc.nextInt();
                pq.add(new Object[]{name, sev, seq++});
            } else if (cmd.equals("TREAT")) {
                Object[] p = pq.poll();
                sb.append(p == null ? "No patients" : "Treating " + p[0]).append('\n');
            } else {
                sb.append("Waiting: ").append(pq.size()).append('\n');
            }
        }
        System.out.print(sb);
    }
}
