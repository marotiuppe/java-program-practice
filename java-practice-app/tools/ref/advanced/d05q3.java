import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        StringTokenizer st = new StringTokenizer(new String(System.in.readAllBytes()));
        int q = Integer.parseInt(st.nextToken());
        Map<Integer, Integer> freq = new HashMap<>();
        Map<Integer, ArrayDeque<Integer>> groups = new HashMap<>();
        int maxFreq = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < q; i++) {
            String cmd = st.nextToken();
            if (cmd.equals("PUSH")) {
                int x = Integer.parseInt(st.nextToken());
                int f = freq.merge(x, 1, Integer::sum);
                groups.computeIfAbsent(f, z -> new ArrayDeque<>()).push(x);
                maxFreq = Math.max(maxFreq, f);
            } else {
                ArrayDeque<Integer> g = groups.get(maxFreq);
                int x = g.pop();
                if (g.isEmpty()) {
                    maxFreq--;
                }
                int f = freq.get(x) - 1;
                if (f == 0) {
                    freq.remove(x);
                } else {
                    freq.put(x, f);
                }
                sb.append(x).append('\n');
            }
        }
        System.out.print(sb);
    }
}
