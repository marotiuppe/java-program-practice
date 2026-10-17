import java.io.*;
import java.util.*;

public class Main {
    static final class Pizza {
        private final String size, crust;
        private final Set<String> toppings;
        private final int qty;

        Pizza(String size, String crust, Set<String> toppings, int qty) {
            this.size = size;
            this.crust = crust;
            this.toppings = Collections.unmodifiableSet(new TreeSet<>(toppings));
            this.qty = qty;
        }

        long price() {
            long base = size.equals("S") ? 800 : size.equals("M") ? 1000 : 1200;
            base += 150L * toppings.size();
            if (crust.equals("stuffed")) base += 200;
            return base * qty;
        }
    }

    static class PizzaBuilder {
        String size, crust = "thin";
        Set<String> toppings = new HashSet<>();
        String qtyRaw = "1";

        PizzaBuilder size(String s) { size = s; return this; }
        PizzaBuilder crust(String c) { crust = c; return this; }
        PizzaBuilder toppings(String csv) {
            for (String t : csv.split(",")) if (!t.isEmpty()) toppings.add(t);
            return this;
        }
        PizzaBuilder qty(String q) { qtyRaw = q; return this; }

        Pizza build() {
            if (size == null) throw new IllegalStateException("size required");
            if (!(size.equals("S") || size.equals("M") || size.equals("L"))) throw new IllegalStateException("invalid size");
            if (toppings.size() > 4) throw new IllegalStateException("too many toppings");
            if (crust.equals("stuffed") && size.equals("S")) throw new IllegalStateException("stuffed not available in S");
            long q;
            try {
                q = Long.parseLong(qtyRaw);
            } catch (NumberFormatException e) {
                q = 0;
            }
            if (q < 1 || q > 10) throw new IllegalStateException("invalid quantity");
            return new Pizza(size, crust, toppings, (int) q);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine().trim());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            PizzaBuilder b = new PizzaBuilder();
            for (String kv : br.readLine().trim().split("\\s+")) {
                int eq = kv.indexOf('=');
                if (eq < 0) continue;
                String k = kv.substring(0, eq), v = kv.substring(eq + 1);
                switch (k) {
                    case "size" -> b.size(v);
                    case "crust" -> b.crust(v);
                    case "top" -> b.toppings(v);
                    case "qty" -> b.qty(v);
                    default -> { }
                }
            }
            try {
                long price = b.build().price();
                sb.append("OK ").append(price).append('\n');
            } catch (IllegalStateException e) {
                sb.append("ERROR ").append(e.getMessage()).append('\n');
            }
        }
        System.out.print(sb);
    }
}
