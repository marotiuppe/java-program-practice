import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        StringTokenizer st = new StringTokenizer(new String(System.in.readAllBytes()));
        int n = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken());
        Map<String, Integer> cnt = new HashMap<>();
        for (int i = 0; i < n; i++) {
            cnt.merge(st.nextToken(), 1, Integer::sum);
        }
        // min-heap whose top is the "worst" word (lowest frequency, then alphabetically last)
        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>((a, b) -> {
            if (!a.getValue().equals(b.getValue())) {
                return Integer.compare(a.getValue(), b.getValue());
            }
            return b.getKey().compareTo(a.getKey());
        });
        for (Map.Entry<String, Integer> e : cnt.entrySet()) {
            pq.add(e);
            if (pq.size() > k) {
                pq.poll();
            }
        }
        LinkedList<String> res = new LinkedList<>();
        while (!pq.isEmpty()) {
            res.addFirst(pq.poll().getKey());
        }
        System.out.println(String.join(" ", res));
    }
}
