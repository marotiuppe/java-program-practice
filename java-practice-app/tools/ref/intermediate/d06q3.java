import java.util.*;

final class Money {
    private final String cur;
    private final long minor;

    Money(String cur, long minor) {
        this.cur = cur;
        this.minor = minor;
    }

    static Money parse(String cur, String amt) {
        long whole;
        long frac = 0;
        int dot = amt.indexOf('.');
        if (dot < 0) {
            whole = Long.parseLong(amt);
        } else {
            whole = dot == 0 ? 0 : Long.parseLong(amt.substring(0, dot));
            String f = amt.substring(dot + 1);
            if (f.length() == 1) f = f + "0";
            if (f.length() > 2) f = f.substring(0, 2);
            frac = f.isEmpty() ? 0 : Long.parseLong(f);
        }
        return new Money(cur, whole * 100 + frac);
    }

    Money add(Money o) {
        if (!cur.equals(o.cur)) throw new IllegalArgumentException("Currency mismatch");
        return new Money(cur, minor + o.minor);
    }

    Money subtract(Money o) {
        if (!cur.equals(o.cur)) throw new IllegalArgumentException("Currency mismatch");
        return new Money(cur, minor - o.minor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money)) return false;
        Money m = (Money) o;
        return minor == m.minor && cur.equals(m.cur);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cur, minor);
    }

    @Override
    public String toString() {
        long a = Math.abs(minor);
        return cur + " " + (minor < 0 ? "-" : "") + (a / 100) + "." + String.format("%02d", a % 100);
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());
        for (int i = 0; i < n; i++) {
            String[] t = sc.nextLine().trim().split("\\s+");
            Money a = Money.parse(t[0], t[1]);
            Money b = Money.parse(t[3], t[4]);
            try {
                if (t[2].equals("+")) System.out.println(a.add(b));
                else if (t[2].equals("-")) System.out.println(a.subtract(b));
                else System.out.println(a.equals(b));
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
