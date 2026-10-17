import java.io.*;
import java.util.*;

public class Main {
    enum Type { STANDARD, DELUXE, SUITE }

    static class Room {
        int id;
        Type type;
        long price;
        List<Booking> bookings = new ArrayList<>();
    }

    static class Booking {
        int id;
        Room room;
        long in, nights, payable, refund;
        boolean active = true;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine().trim());
        TreeMap<Integer, Room> rooms = new TreeMap<>();
        Map<Integer, Booking> byId = new HashMap<>();
        int nextId = 1;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            String[] p = br.readLine().trim().split("\\s+");
            switch (p[0]) {
                case "ROOM" -> {
                    Room r = new Room();
                    r.id = Integer.parseInt(p[1]);
                    r.type = Type.valueOf(p[2]);
                    r.price = Long.parseLong(p[3]);
                    rooms.put(r.id, r);
                }
                case "BOOK" -> {
                    int rid = Integer.parseInt(p[2]);
                    long in = Long.parseLong(p[3]), nights = Long.parseLong(p[4]);
                    Room r = rooms.get(rid);
                    if (nights < 1) {
                        sb.append("REJECTED INVALID\n");
                    } else if (r == null) {
                        sb.append("REJECTED UNKNOWN ROOM\n");
                    } else {
                        boolean overlap = false;
                        for (Booking b : r.bookings) {
                            if (b.active && in < b.in + b.nights && b.in < in + nights) overlap = true;
                        }
                        if (overlap) {
                            sb.append("REJECTED OVERLAP\n");
                        } else {
                            Booking b = new Booking();
                            b.id = nextId++;
                            b.room = r;
                            b.in = in;
                            b.nights = nights;
                            long cost = r.price * nights;
                            long pct = nights >= 7 ? 15 : nights >= 3 ? 5 : 0;
                            b.payable = cost - cost * pct / 100;
                            r.bookings.add(b);
                            byId.put(b.id, b);
                            sb.append("BOOKED ").append(b.id).append(" payable=").append(b.payable).append("\n");
                        }
                    }
                }
                case "CANCEL" -> {
                    int id = Integer.parseInt(p[1]);
                    long day = Long.parseLong(p[2]);
                    Booking b = byId.get(id);
                    if (b == null || !b.active) {
                        sb.append("NOT FOUND\n");
                    } else {
                        b.active = false;
                        b.refund = b.in - day >= 2 ? b.payable : b.payable / 2;
                        sb.append("CANCELLED ").append(id).append(" refund=").append(b.refund).append("\n");
                    }
                }
                default -> {
                    long total = 0;
                    for (Room r : rooms.values()) {
                        long rev = 0;
                        for (Booking b : r.bookings) rev += b.payable - b.refund;
                        total += rev;
                        sb.append(r.id).append(" ").append(r.type).append(" ").append(rev).append("\n");
                    }
                    sb.append("Total revenue: ").append(total).append("\n");
                }
            }
        }
        System.out.print(sb);
    }
}
