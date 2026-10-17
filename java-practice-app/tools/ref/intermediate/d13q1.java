import java.util.*;

interface Identifiable {
    int getId();
}

class Customer implements Identifiable {
    int id;
    String name;

    Customer(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        return "Customer{id=" + id + ", name=" + name + "}";
    }
}

class Product implements Identifiable {
    int id;
    String name;
    int price;

    Product(int id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        return "Product{id=" + id + ", name=" + name + ", price=" + price + "}";
    }
}

class Repository<T extends Identifiable> {
    private final Map<Integer, T> items = new TreeMap<>();

    boolean save(T t) {
        if (items.containsKey(t.getId())) return false;
        items.put(t.getId(), t);
        return true;
    }

    T find(int id) {
        return items.get(id);
    }

    boolean delete(int id) {
        return items.remove(id) != null;
    }

    List<T> listAll() {
        return new ArrayList<>(items.values());
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Repository<Customer> cr = new Repository<>();
        Repository<Product> pr = new Repository<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            String type = sc.next();
            String op = sc.next();
            boolean cust = type.equals("CUSTOMER");
            switch (op) {
                case "SAVE": {
                    int id = sc.nextInt();
                    String name = sc.next();
                    boolean ok = cust ? cr.save(new Customer(id, name)) : pr.save(new Product(id, name, sc.nextInt()));
                    sb.append(ok ? "OK" : "DUPLICATE").append('\n');
                    break;
                }
                case "FIND": {
                    int id = sc.nextInt();
                    Object o = cust ? cr.find(id) : pr.find(id);
                    sb.append(o == null ? "NOT FOUND" : o.toString()).append('\n');
                    break;
                }
                case "DELETE": {
                    int id = sc.nextInt();
                    boolean ok = cust ? cr.delete(id) : pr.delete(id);
                    sb.append(ok ? "DELETED" : "NOT FOUND").append('\n');
                    break;
                }
                default: {
                    List<? extends Identifiable> l = cust ? cr.listAll() : pr.listAll();
                    if (l.isEmpty()) sb.append("(none)\n");
                    for (Identifiable x : l) sb.append(x).append('\n');
                }
            }
        }
        System.out.print(sb);
    }
}
