import java.util.*;

class Booking {
    private static int nextId = 1001;
    static final int PRICE = 150;
    static int count = 0;
    static long revenue = 0;

    final int id;
    final String name;
    final int seats;
    final int amount;

    private Booking(String name, int seats) {
        this.id = nextId++;
        this.name = name;
        this.seats = seats;
        int total = seats * PRICE;
        if (seats > 4) total = total * 90 / 100;
        this.amount = total;
        count++;
        revenue += total;
    }

    static Booking book(String name, int seats) {
        if (seats < 1 || seats > 10) return null;
        return new Booking(name, seats);
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        for (int i = 0; i < n; i++) {
            String name = sc.next();
            int seats = sc.nextInt();
            Booking b = Booking.book(name, seats);
            if (b == null) System.out.println("REJECTED " + name);
            else System.out.println(b.id + " " + b.name + " " + b.seats + " " + b.amount);
        }
        System.out.println("Total bookings: " + Booking.count);
        System.out.println("Total revenue: " + Booking.revenue);
    }
}
