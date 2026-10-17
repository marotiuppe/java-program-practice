import java.io.*;
import java.util.*;

public class Main {
    static class TopK<T extends Comparable<? super T>> {
        private final int k;
        private final PriorityQueue<T> heap = new PriorityQueue<>();

        TopK(int k) {
            this.k = k;
        }

        void add(T v) {
            if (heap.size() < k) {
                heap.add(v);
            } else if (v.compareTo(heap.peek()) > 0) {
                heap.poll();
                heap.add(v);
            }
        }

        List<T> result() {
            List<T> r = new ArrayList<>(heap);
            r.sort(Comparator.reverseOrder());
            return r;
        }
    }

    static <T extends Comparable<? super T>> void run(TopK<T> top, String[] vals, java.util.function.Function<String, T> parse) {
        for (String v : vals) top.add(parse.apply(v));
        StringBuilder sb = new StringBuilder();
        for (T t : top.result()) {
            if (sb.length() > 0) sb.append(' ');
            sb.append(t);
        }
        System.out.println(sb);
    }

    public static void main(String[] args) throws IOException {
        StringTokenizer st = new StringTokenizer(new String(System.in.readAllBytes()));
        String mode = st.nextToken();
        int k = Integer.parseInt(st.nextToken());
        int n = Integer.parseInt(st.nextToken());
        String[] vals = new String[n];
        for (int i = 0; i < n; i++) vals[i] = st.nextToken();
        switch (mode) {
            case "INT" -> run(new TopK<Integer>(k), vals, Integer::valueOf);
            case "STR" -> run(new TopK<String>(k), vals, s -> s);
            default -> run(new TopK<Double>(k), vals, Double::valueOf);
        }
    }
}
