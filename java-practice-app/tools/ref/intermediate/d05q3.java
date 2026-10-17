import java.util.*;

interface DiscountRule {
    String name();

    long discount(long total, String customerType, String coupon);
}

class Bulk10 implements DiscountRule {
    public String name() {
        return "BULK10";
    }

    public long discount(long total, String c, String coupon) {
        return total >= 1000 ? total * 10 / 100 : 0;
    }
}

class CouponRule implements DiscountRule {
    public String name() {
        return "COUPON";
    }

    public long discount(long total, String c, String coupon) {
        return coupon.equals("SAVE100") ? 100 : 0;
    }
}

class Gold5 implements DiscountRule {
    public String name() {
        return "GOLD5";
    }

    public long discount(long total, String c, String coupon) {
        return c.equals("GOLD") ? total * 5 / 100 : 0;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        long total = sc.nextLong();
        String type = sc.next();
        String coupon = sc.next();
        List<DiscountRule> rules = List.of(new Bulk10(), new CouponRule(), new Gold5());
        long sum = 0;
        for (DiscountRule r : rules) {
            long d = r.discount(total, type, coupon);
            System.out.println(r.name() + " " + d);
            sum += d;
        }
        long fin = Math.min(sum, total * 30 / 100);
        System.out.println("Discount: " + fin);
        System.out.println("Payable: " + (total - fin));
    }
}
