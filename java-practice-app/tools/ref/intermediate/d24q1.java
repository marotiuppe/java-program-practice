import java.util.*;

public class Main {
    enum Status { PICKED, IN_TRANSIT, OUT_FOR_DELIVERY, DELIVERED, FAILED }

    record Event(String id, Status status, int hour) {}

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Map<String, Event> latest = new LinkedHashMap<>();
        for (int i = 0; i < n; i++) {
            Event e = new Event(sc.next(), Status.valueOf(sc.next()), sc.nextInt());
            Event cur = latest.get(e.id());
            if (cur == null || e.hour() >= cur.hour()) latest.put(e.id(), e);
        }
        int done = 0, problem = 0, pending = 0;
        for (Event e : latest.values()) {
            System.out.println(e.id() + ": " + e.status() + " at " + e.hour());
            String cls = switch (e.status()) {
                case DELIVERED -> "DONE";
                case FAILED -> "PROBLEM";
                default -> "PENDING";
            };
            switch (cls) {
                case "DONE" -> done++;
                case "PROBLEM" -> problem++;
                default -> pending++;
            }
        }
        System.out.println("Delivered: " + done + " Failed: " + problem + " Pending: " + pending);
    }
}
