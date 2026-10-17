import java.util.*;

abstract class Ride {
    abstract long baseFare(int km, int minutes);

    abstract long minFare();

    long fare(int km, int minutes, int surge) {
        long f = Math.max(baseFare(km, minutes), minFare());
        return f * (100 + surge) / 100;
    }
}

class Bike extends Ride {
    long baseFare(int km, int m) {
        return 10 + 6L * km;
    }

    long minFare() {
        return 25;
    }
}

class Auto extends Ride {
    long baseFare(int km, int m) {
        return 20 + 10L * km + m;
    }

    long minFare() {
        return 40;
    }
}

class Cab extends Ride {
    long baseFare(int km, int m) {
        return 50 + 14L * km + 2L * m;
    }

    long minFare() {
        return 100;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int s = sc.nextInt();
        int n = sc.nextInt();
        long total = 0;
        for (int i = 0; i < n; i++) {
            String t = sc.next();
            int km = sc.nextInt();
            int min = sc.nextInt();
            Ride r = t.equals("BIKE") ? new Bike() : t.equals("AUTO") ? new Auto() : new Cab();
            long f = r.fare(km, min, s);
            System.out.println(t + " " + f);
            total += f;
        }
        System.out.println("Total: " + total);
    }
}
