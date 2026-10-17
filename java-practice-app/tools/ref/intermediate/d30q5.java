import java.util.*;

class OutOfStockException extends RuntimeException {
    OutOfStockException(String msg) {
        super(msg);
    }
}

class Item implements Comparable<Item> {
    String name;
    int qty;
    double price;

    Item(String name, int qty, double price) {
        this.name = name;
        this.qty = qty;
        this.price = price;
    }

    public int compareTo(Item o) {
        return name.compareTo(o.name);
    }

    public boolean equals(Object o) {
        return o instanceof Item && name.equals(((Item) o).name);
    }

    public int hashCode() {
        return name.hashCode();
    }
}

class Inventory<T extends Item> {
    private List<T> items = new ArrayList<>();

    void add(T item) {
        items.add(item);
    }

    void remove(String name, int amount) {
        for (T item : items) {
            if (item.name.equals(name)) {
                if (item.qty < amount) {
                    throw new OutOfStockException(name);
                }
                item.qty -= amount;
                return;
            }
        }
    }

    double totalValue() {
        double total = 0;
        for (T item : items) {
            total += item.qty * item.price;
        }
        return total;
    }

    List<T> sorted() {
        Collections.sort(items);
        return items;
    }
}

public class Main {
    public static void main(String[] args) {
        Inventory<Item> inv = new Inventory<>();
        inv.add(new Item("pen", 10, 2.5));
        inv.add(new Item("box", 4, 10.0));
        try {
            inv.remove("pen", 3);
            inv.remove("box", 9);
        } catch (OutOfStockException e) {
            System.out.println("Out of stock: " + e.getMessage());
        }
        System.out.println("Total: " + inv.totalValue());
        for (Item it : inv.sorted()) {
            System.out.println(it.name + " " + it.qty);
        }
    }
}
