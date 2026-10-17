import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int q = Integer.parseInt(br.readLine().trim());
        Map<String, int[]> pool = new HashMap<>();
        PriorityQueue<Integer> free = new PriorityQueue<>();
        int nextId = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < q; i++) {
            String[] p = br.readLine().trim().split("\s+");
            String s = p[1];
            if (p[0].equals("INTERN")) {
                int[] e = pool.get(s);
                if (e == null) {
                    int id = free.isEmpty() ? nextId++ : free.poll();
                    e = new int[]{id, 0};
                    pool.put(s, e);
                }
                e[1]++;
                sb.append("id=").append(e[0]).append(" refs=").append(e[1]).append('\n');
            } else {
                int[] e = pool.get(s);
                if (e == null) {
                    sb.append("MISSING\n");
                } else if (--e[1] == 0) {
                    pool.remove(s);
                    free.add(e[0]);
                    sb.append("freed id=").append(e[0]).append('\n');
                } else {
                    sb.append("refs=").append(e[1]).append('\n');
                }
            }
        }
        System.out.print(sb);
    }
}
