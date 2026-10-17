import java.util.*;

class Pair<K, V> {
    private K key;
    private V value;

    Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    K getKey() {
        return key;
    }

    V getValue() {
        return value;
    }

    Pair<V, K> swap() {
        return new Pair<V, K>(value, key);
    }
}

public class Main {
    static double total(List<? extends Number> nums) {
        double s = 0;
        for (Number n : nums) {
            s += n.doubleValue();
        }
        return s;
    }

    @SuppressWarnings("unchecked")
    static <T> T[] makeArray(int n) {
        return (T[]) new Object[n];
    }

    public static void main(String[] args) {
        Pair<String, Integer> p = new Pair<>("age", 30);
        Pair<Integer, String> q = p.swap();
        System.out.println(q.getKey() + " " + q.getValue());

        List<Integer> ints = Arrays.asList(1, 2, 3);
        System.out.println(total(ints));

        List<Integer> nums2 = new ArrayList<>();
        nums2.add(1);
        nums2.add(2);
        nums2.add(3);
        System.out.println(nums2.size());
    }
}
