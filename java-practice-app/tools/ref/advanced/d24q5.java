import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Main {
    record Order(int id, int[] prices) {
        int total() {
            int t = 0;
            for (int p : prices) {
                t += p;
            }
            return t;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Order other && id == other.id && Arrays.equals(prices, other.prices);
        }

        @Override
        public int hashCode() {
            return 31 * Integer.hashCode(id) + Arrays.hashCode(prices);
        }
    }

    static Map<Integer, Order> store = new HashMap<>();

    static Optional<Order> findOrder(int id) {
        return Optional.ofNullable(store.get(id));
    }

    public static void main(String[] args) {
        store.put(1, new Order(1, new int[]{10, 20}));
        store.put(2, new Order(2, new int[]{5}));
        System.out.println(findOrder(1).get().total());
        System.out.println(findOrder(3).isPresent() ? findOrder(3).get().total() : "missing");
        System.out.println(findOrder(2).get().total());
        Order copy = new Order(1, new int[]{10, 20});
        System.out.println(findOrder(1).get().equals(copy));
    }
}
