import java.util.*;

public class Main {
    sealed interface Shape permits Circle, Rectangle, Square {}

    record Circle(double r) implements Shape {}

    record Rectangle(double w, double h) implements Shape {}

    record Square(double side) implements Shape {}

    static double area(Shape o) {
        if (o instanceof Circle c) return Math.PI * c.r() * c.r();
        if (o instanceof Rectangle r) return r.w() * r.h();
        if (o instanceof Square s) return s.side() * s.side();
        return 0;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Set<Shape> seen = new HashSet<>();
        double total = 0;
        int dup = 0;
        for (int i = 0; i < n; i++) {
            String t = sc.next();
            Shape s;
            if (t.equals("C")) s = new Circle(Double.parseDouble(sc.next()));
            else if (t.equals("R")) s = new Rectangle(Double.parseDouble(sc.next()), Double.parseDouble(sc.next()));
            else s = new Square(Double.parseDouble(sc.next()));
            double a = area(s);
            total += a;
            if (!seen.add(s)) dup++;
            System.out.println(s + " area=" + String.format(Locale.US, "%.2f", a));
        }
        System.out.println("Total area: " + String.format(Locale.US, "%.2f", total));
        System.out.println("Duplicates: " + dup);
    }
}
