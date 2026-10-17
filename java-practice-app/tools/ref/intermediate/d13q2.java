import java.util.*;

public class Main {
    static <T extends Comparable<T>> T maxOf(List<T> l) {
        T best = l.get(0);
        for (T x : l) if (x.compareTo(best) > 0) best = x;
        return best;
    }

    static <T extends Comparable<T>> T minOf(List<T> l) {
        T best = l.get(0);
        for (T x : l) if (x.compareTo(best) < 0) best = x;
        return best;
    }

    static <T> int countOf(List<T> l, T v) {
        int c = 0;
        for (T x : l) if (x.equals(v)) c++;
        return c;
    }

    static <T> List<T> reversed(List<T> l) {
        List<T> r = new ArrayList<>(l);
        Collections.reverse(r);
        return r;
    }

    static <T extends Comparable<T>> void report(List<T> l) {
        System.out.println("Max: " + maxOf(l));
        System.out.println("Min: " + minOf(l));
        System.out.println("Count of first: " + countOf(l, l.get(0)));
        System.out.println("Reversed: " + reversed(l));
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String type = sc.next();
        int n = sc.nextInt();
        if (type.equals("INT")) {
            List<Integer> l = new ArrayList<>();
            for (int i = 0; i < n; i++) l.add(sc.nextInt());
            report(l);
        } else if (type.equals("STR")) {
            List<String> l = new ArrayList<>();
            for (int i = 0; i < n; i++) l.add(sc.next());
            report(l);
        } else {
            List<Double> l = new ArrayList<>();
            for (int i = 0; i < n; i++) l.add(sc.nextDouble());
            report(l);
        }
    }
}
