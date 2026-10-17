import java.util.*;

public class Main {
    static String join(Set<String> s) {
        return s.isEmpty() ? "none" : String.join(" ", s);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();
        Set<String> d1 = new TreeSet<>();
        for (int i = 0; i < a; i++) d1.add(sc.next());
        int b = sc.nextInt();
        Set<String> d2 = new TreeSet<>();
        for (int i = 0; i < b; i++) d2.add(sc.next());
        Set<String> both = new TreeSet<>(d1);
        both.retainAll(d2);
        Set<String> only1 = new TreeSet<>(d1);
        only1.removeAll(d2);
        Set<String> only2 = new TreeSet<>(d2);
        only2.removeAll(d1);
        Set<String> all = new TreeSet<>(d1);
        all.addAll(d2);
        System.out.println("Both: " + join(both));
        System.out.println("Only Day 1: " + join(only1));
        System.out.println("Only Day 2: " + join(only2));
        System.out.println("Total unique: " + all.size());
    }
}
