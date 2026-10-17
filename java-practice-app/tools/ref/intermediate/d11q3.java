import java.util.*;

public class Main {
    static class Flight {
        String id;
        int price, duration, dep;

        Flight(String id, int p, int d, int dep) {
            this.id = id;
            price = p;
            duration = d;
            this.dep = dep;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        List<Flight> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            list.add(new Flight(sc.next(), sc.nextInt(), sc.nextInt(), sc.nextInt()));
        }
        Comparator<Flight> cmp = null;
        while (sc.hasNext()) {
            String[] t = sc.next().split(":");
            Comparator<Flight> c;
            switch (t[0]) {
                case "price": c = Comparator.comparingInt((Flight f) -> f.price); break;
                case "duration": c = Comparator.comparingInt((Flight f) -> f.duration); break;
                case "dep": c = Comparator.comparingInt((Flight f) -> f.dep); break;
                default: c = Comparator.comparing((Flight f) -> f.id); break;
            }
            if (t[1].equals("desc")) c = c.reversed();
            cmp = cmp == null ? c : cmp.thenComparing(c);
        }
        if (cmp != null) list.sort(cmp);
        StringBuilder sb = new StringBuilder();
        for (Flight f : list) {
            if (sb.length() > 0) sb.append(' ');
            sb.append(f.id);
        }
        System.out.println(sb);
    }
}
