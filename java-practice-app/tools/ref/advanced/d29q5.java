import java.util.Comparator;
import java.util.PriorityQueue;

public class Main {
    static class MedianFinder {
        private final PriorityQueue<Integer> low = new PriorityQueue<>(Comparator.reverseOrder());
        private final PriorityQueue<Integer> high = new PriorityQueue<>();

        void add(int x) {
            low.add(x);
            high.add(low.poll());
            if (high.size() > low.size()) {
                low.add(high.poll());
            }
        }

        double median() {
            if (low.size() > high.size()) {
                return low.peek();
            }
            return ((double) low.peek() + high.peek()) / 2;
        }
    }

    public static void main(String[] args) {
        MedianFinder m = new MedianFinder();
        m.add(1);
        System.out.println(m.median());
        m.add(2);
        System.out.println(m.median());
        m.add(3);
        System.out.println(m.median());
        MedianFinder big = new MedianFinder();
        big.add(Integer.MAX_VALUE);
        big.add(Integer.MAX_VALUE);
        System.out.println(big.median());
    }
}
