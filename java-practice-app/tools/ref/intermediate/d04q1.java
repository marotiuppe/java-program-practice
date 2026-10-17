import java.util.*;

interface PaymentMethod {
    boolean pay(int amount);

    String name();

    long charged(int amount);

    default String format(int amount) {
        if (pay(amount)) return name() + " " + amount + " OK " + charged(amount);
        return name() + " " + amount + " DECLINED";
    }
}

class Card implements PaymentMethod {
    public boolean pay(int amount) {
        return amount > 0 && amount <= 20000;
    }

    public String name() {
        return "CARD";
    }

    public long charged(int amount) {
        return amount + amount * 2L / 100;
    }
}

class Wallet implements PaymentMethod {
    private long balance = 1000;

    public boolean pay(int amount) {
        if (amount <= 0 || amount > balance) return false;
        balance -= amount;
        return true;
    }

    public String name() {
        return "WALLET";
    }

    public long charged(int amount) {
        return amount;
    }
}

class Upi implements PaymentMethod {
    public boolean pay(int amount) {
        return amount > 0 && amount <= 5000;
    }

    public String name() {
        return "UPI";
    }

    public long charged(int amount) {
        return amount;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Map<String, PaymentMethod> m = new HashMap<>();
        m.put("CARD", new Card());
        m.put("WALLET", new Wallet());
        m.put("UPI", new Upi());
        int n = sc.nextInt();
        for (int i = 0; i < n; i++) {
            String k = sc.next();
            int amt = sc.nextInt();
            System.out.println(m.get(k).format(amt));
        }
    }
}
