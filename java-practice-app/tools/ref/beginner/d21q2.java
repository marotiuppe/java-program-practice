import java.util.*;

class Shape { double area() { return 0; } }
class Circle extends Shape {
    double r;
    Circle(double r) { this.r = r; }
    @Override double area() { return Math.PI * r * r; }
}
class Rectangle extends Shape {
    double l, w;
    Rectangle(double l, double w) { this.l = l; this.w = w; }
    @Override double area() { return l * w; }
}
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String t = sc.next();
        Shape s = t.equals("circle") ? new Circle(sc.nextDouble()) : new Rectangle(sc.nextDouble(), sc.nextDouble());
        System.out.printf("Area: %.2f%n", s.area());
    }
}