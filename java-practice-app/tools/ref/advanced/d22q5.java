import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Main {
    static class Item implements Comparable<Item> {
        final int w;

        Item(int w) {
            this.w = w;
        }

        @Override
        public int compareTo(Item o) {
            return Integer.compare(w, o.w);
        }

        @Override
        public String toString() {
            return "Item" + w;
        }
    }

    static class Special extends Item {
        Special(int w) {
            super(w);
        }

        @Override
        public String toString() {
            return "Special" + w;
        }
    }

    static <T extends Comparable<? super T>> T maxOf(List<T> list) {
        if (list.isEmpty()) {
            throw new NoSuchElementException();
        }
        T best = list.get(0);
        for (T x : list) {
            if (x.compareTo(best) > 0) {
                best = x;
            }
        }
        return best;
    }

    static <T> void copy(List<? extends T> src, List<? super T> dst) {
        for (T n : src) {
            dst.add(n);
        }
    }

    static double sum(List<? extends Number> list) {
        double s = 0;
        for (Number x : list) {
            s += x.doubleValue();
        }
        return s;
    }

    public static void main(String[] args) {
        List<Special> specials = new ArrayList<>();
        specials.add(new Special(3));
        specials.add(new Special(9));
        specials.add(new Special(5));
        System.out.println(maxOf(specials));

        List<Integer> ints = new ArrayList<>();
        ints.add(1);
        ints.add(2);
        List<Number> numbers = new ArrayList<>();
        copy(ints, numbers);
        System.out.println(numbers);

        List<Double> ds = new ArrayList<>();
        ds.add(1.5);
        ds.add(2.5);
        System.out.println(sum(ds));
        try {
            System.out.println(maxOf(new ArrayList<Integer>()));
        } catch (NoSuchElementException e) {
            System.out.println("NoSuchElementException");
        }
    }
}
