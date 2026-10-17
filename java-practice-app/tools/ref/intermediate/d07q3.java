import java.util.*;

enum Day {
    MON, TUE, WED, THU, FRI, SAT, SUN;

    boolean weekend() {
        return this == SAT || this == SUN;
    }
}

enum Shift {
    MORNING(100), EVENING(125), NIGHT(150);

    private final int pct;

    Shift(int pct) {
        this.pct = pct;
    }

    int percent(Day d) {
        return pct + (d.weekend() ? 20 : 0);
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        long rate = sc.nextLong();
        int n = sc.nextInt();
        long total = 0;
        EnumMap<Shift, Integer> hours = new EnumMap<>(Shift.class);
        for (Shift s : Shift.values()) hours.put(s, 0);
        for (int i = 0; i < n; i++) {
            Day d = Day.valueOf(sc.next());
            Shift s = Shift.valueOf(sc.next());
            int h = sc.nextInt();
            long pay = rate * h * s.percent(d) / 100;
            System.out.println(d + " " + s + " " + pay);
            total += pay;
            hours.merge(s, h, Integer::sum);
        }
        System.out.println("Total: " + total);
        System.out.println("Hours: MORNING=" + hours.get(Shift.MORNING) + " EVENING=" + hours.get(Shift.EVENING)
                + " NIGHT=" + hours.get(Shift.NIGHT));
    }
}
