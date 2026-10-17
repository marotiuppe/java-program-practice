import java.io.*;
import java.util.*;

public class Main {
    sealed interface Payment permits Card, Upi, Cash {
        long amount();
    }

    record Card(long amount) implements Payment {}

    record Upi(long amount) implements Payment {}

    record Cash(long amount) implements Payment {}

    static long fee(Payment p) {
        if (p instanceof Card c) {
            return Math.max(100, c.amount() * 2 / 100);
        } else if (p instanceof Upi u) {
            return u.amount() <= 100000 ? 0 : u.amount() * 5 / 1000;
        } else {
            return 50;
        }
    }

    public static void main(String[] args) throws IOException {
        StringTokenizer st = new StringTokenizer(new String(System.in.readAllBytes()));
        int n = Integer.parseInt(st.nextToken());
        long total = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            String type = st.nextToken();
            long amount = Long.parseLong(st.nextToken());
            Payment p = switch (type) {
                case "CARD" -> new Card(amount);
                case "UPI" -> new Upi(amount);
                default -> new Cash(amount);
            };
            long f = fee(p);
            total += f;
            sb.append(type).append(' ').append(amount).append(" fee=").append(f).append('\n');
        }
        sb.append("Total fees: ").append(total);
        System.out.println(sb);
    }
}
