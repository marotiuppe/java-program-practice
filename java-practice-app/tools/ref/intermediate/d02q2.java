import java.util.*;

final class Fraction {
    private final long num;
    private final long den;

    Fraction(long numerator, long denominator) {
        if (denominator < 0) {
            numerator = -numerator;
            denominator = -denominator;
        }
        long g = gcd(Math.abs(numerator), denominator);
        if (g == 0) g = 1;
        this.num = numerator / g;
        this.den = denominator / g;
    }

    Fraction(int whole) {
        this(whole, 1);
    }

    private static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    Fraction add(Fraction o) {
        return new Fraction(num * o.den + o.num * den, den * o.den);
    }

    Fraction subtract(Fraction o) {
        return new Fraction(num * o.den - o.num * den, den * o.den);
    }

    Fraction multiply(Fraction o) {
        return new Fraction(num * o.num, den * o.den);
    }

    Fraction divide(Fraction o) {
        return new Fraction(num * o.den, den * o.num);
    }

    boolean isZero() {
        return num == 0;
    }

    @Override
    public String toString() {
        return num + "/" + den;
    }
}

public class Main {
    static Fraction parse(String s) {
        String[] p = s.split("/");
        return new Fraction(Long.parseLong(p[0]), Long.parseLong(p[1]));
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());
        for (int i = 0; i < n; i++) {
            String[] t = sc.nextLine().trim().split(" ");
            Fraction a = parse(t[0]);
            Fraction b = parse(t[2]);
            switch (t[1]) {
                case "+": System.out.println(a.add(b)); break;
                case "-": System.out.println(a.subtract(b)); break;
                case "*": System.out.println(a.multiply(b)); break;
                default:
                    if (b.isZero()) System.out.println("Undefined");
                    else System.out.println(a.divide(b));
            }
        }
    }
}
