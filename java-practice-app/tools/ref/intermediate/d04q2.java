import java.util.*;

abstract class Shape {
    abstract double area();

    abstract String name();
}

class Circle extends Shape {
    private final double r;

    Circle(double r) {
        this.r = r;
    }

    double area() {
        return Math.PI * r * r;
    }

    String name() {
        return "Circle";
    }
}

class Rectangle extends Shape {
    private final double w, h;

    Rectangle(double w, double h) {
        this.w = w;
        this.h = h;
    }

    double area() {
        return w * h;
    }

    String name() {
        return "Rectangle";
    }
}

class Triangle extends Shape {
    private final double a, b, c;

    Triangle(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    boolean valid() {
        return a + b > c && a + c > b && b + c > a;
    }

    double area() {
        double s = (a + b + c) / 2;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }

    String name() {
        return "Triangle";
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        double total = 0;
        Shape best = null;
        for (int i = 0; i < n; i++) {
            String t = sc.next();
            Shape s;
            if (t.equals("C")) s = new Circle(sc.nextDouble());
            else if (t.equals("R")) s = new Rectangle(sc.nextDouble(), sc.nextDouble());
            else {
                Triangle tr = new Triangle(sc.nextDouble(), sc.nextDouble(), sc.nextDouble());
                if (!tr.valid() || !(tr.area() > 0)) {
                    System.out.println("Triangle invalid");
                    continue;
                }
                s = tr;
            }
            System.out.println(String.format(Locale.US, "%s area=%.2f", s.name(), s.area()));
            total += s.area();
            if (best == null || s.area() > best.area()) best = s;
        }
        if (best == null) {
            System.out.println("No valid shapes");
        } else {
            System.out.println(String.format(Locale.US, "Total area=%.2f", total));
            System.out.println("Largest: " + best.name());
        }
    }
}
