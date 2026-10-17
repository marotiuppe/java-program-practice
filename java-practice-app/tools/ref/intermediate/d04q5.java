interface Printable {
    void print();
}

abstract class Shape implements Printable {
    abstract double area();

    public String toString() {
        return getClass().getSimpleName() + " " + area();
    }

    public void print() {
        System.out.println(this);
    }
}

class Square extends Shape {
    private double side;

    Square(double side) {
        this.side = side;
    }

    double area() {
        return side * side;
    }
}

class Circle extends Shape {
    private double r;

    Circle(double r) {
        this.r = r;
    }

    double area() {
        return 3.14 * r * r;
    }
}

public class Main {
    public static void main(String[] args) {
        Shape s = new Square(3);
        Printable c = new Circle(1);
        s.print();
        c.print();
    }
}
