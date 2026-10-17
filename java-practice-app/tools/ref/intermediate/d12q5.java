import java.util.*;

public class Main {
    public static void main(String[] args) {
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
        pq.add(new int[]{3, 100});
        pq.add(new int[]{1, 200});
        pq.add(new int[]{2, 300});
        while (!pq.isEmpty()) {
            int[] t = pq.poll();
            System.out.println(t[0] + ":" + t[1]);
        }

        Deque<String> history = new ArrayDeque<>();
        history.push("home");
        history.push("search");
        System.out.println(history.peek());

        Queue<Integer> empty = new ArrayDeque<>();
        System.out.println("Next: " + empty.peek());
    }
}
