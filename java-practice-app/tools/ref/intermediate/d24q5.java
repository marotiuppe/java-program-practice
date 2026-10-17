public class Main {
    record Item(String name, int qty) {
    }

    static String tier(int qty) {
        return switch (qty / 10) {
            case 0 -> "small";
            case 1 -> "medium";
            default -> "large";
        };
    }

    public static void main(String[] args) {
        var items = new Item[]{new Item("pen", 5), new Item("box", 15)};
        var msg = """
            Items:
            pen and box""";
        System.out.println(msg);
        for (Item it : items) {
            System.out.println(it.name() + " " + tier(it.qty()));
        }
    }
}
