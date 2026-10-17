import java.util.*;

enum Status {
    NEW, PAID, SHIPPED, DELIVERED, CANCELLED;

    boolean canMoveTo(Status next) {
        switch (this) {
            case NEW: return next == PAID || next == CANCELLED;
            case PAID: return next == SHIPPED || next == CANCELLED;
            case SHIPPED: return next == DELIVERED;
            default: return false;
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Map<String, Status> orders = new LinkedHashMap<>();
        for (int i = 0; i < n; i++) {
            String id = sc.next();
            Status req = Status.valueOf(sc.next());
            Status cur = orders.computeIfAbsent(id, k -> Status.NEW);
            if (cur.canMoveTo(req)) {
                orders.put(id, req);
                System.out.println(id + ": " + cur + " -> " + req);
            } else {
                System.out.println(id + ": INVALID " + cur + " -> " + req);
            }
        }
        for (Map.Entry<String, Status> e : orders.entrySet()) {
            System.out.println(e.getKey() + " " + e.getValue());
        }
    }
}
