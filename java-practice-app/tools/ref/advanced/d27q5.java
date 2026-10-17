import java.util.ArrayList;
import java.util.List;

public class Main {
    static class Pizza {
        private final String size;
        private final List<String> toppings;

        Pizza(String size, List<String> toppings) {
            this.size = size;
            this.toppings = new ArrayList<>(toppings);
        }

        List<String> getToppings() {
            return new ArrayList<>(toppings);
        }

        @Override
        public String toString() {
            return size + " " + toppings;
        }
    }

    static class Builder {
        private String size;
        private final List<String> toppings = new ArrayList<>();

        Builder size(String s) {
            this.size = s;
            return this;
        }

        Builder topping(String t) {
            toppings.add(t);
            return this;
        }

        Pizza build() {
            if (size == null) {
                throw new IllegalStateException("size required");
            }
            return new Pizza(size, toppings);
        }
    }

    public static void main(String[] args) {
        Builder b = new Builder().size("M").topping("olive");
        Pizza p1 = b.build();
        b.topping("onion");
        Pizza p2 = b.build();
        System.out.println(p1);
        System.out.println(p2);
        p1.getToppings().add("anchovy");
        System.out.println(p1);
        try {
            new Builder().topping("x").build();
            System.out.println("built without size");
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
}
