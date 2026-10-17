import java.util.*;

public class Main {
    static Map<Integer, int[]> data = new HashMap<>(); // key -> {value, count}
    static Map<Integer, LinkedHashSet<Integer>> byCount = new HashMap<>();
    static int minCount = 0;

    static void touch(int key) {
        int[] d = data.get(key);
        LinkedHashSet<Integer> old = byCount.get(d[1]);
        old.remove(key);
        if (old.isEmpty()) {
            byCount.remove(d[1]);
            if (minCount == d[1]) {
                minCount++;
            }
        }
        d[1]++;
        byCount.computeIfAbsent(d[1], z -> new LinkedHashSet<>()).add(key);
    }

    public static void main(String[] args) throws Exception {
        StringTokenizer st = new StringTokenizer(new String(System.in.readAllBytes()));
        int cap = Integer.parseInt(st.nextToken());
        int q = Integer.parseInt(st.nextToken());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < q; i++) {
            String cmd = st.nextToken();
            int key = Integer.parseInt(st.nextToken());
            if (cmd.equals("GET")) {
                if (data.containsKey(key)) {
                    touch(key);
                    sb.append(data.get(key)[0]).append('\n');
                } else {
                    sb.append(-1).append('\n');
                }
            } else {
                int val = Integer.parseInt(st.nextToken());
                if (cap == 0) {
                    continue;
                }
                if (data.containsKey(key)) {
                    data.get(key)[0] = val;
                    touch(key);
                } else {
                    if (data.size() == cap) {
                        LinkedHashSet<Integer> set = byCount.get(minCount);
                        int victim = set.iterator().next();
                        set.remove(victim);
                        if (set.isEmpty()) {
                            byCount.remove(minCount);
                        }
                        data.remove(victim);
                    }
                    data.put(key, new int[]{val, 1});
                    byCount.computeIfAbsent(1, z -> new LinkedHashSet<>()).add(key);
                    minCount = 1;
                }
            }
        }
        System.out.print(sb);
    }
}
