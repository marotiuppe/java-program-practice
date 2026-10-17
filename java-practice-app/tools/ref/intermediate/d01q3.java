import java.util.*;

class Parcel {
    private final int grams;
    private final boolean express;
    private final int insured;

    Parcel(int grams) {
        this(grams, false);
    }

    Parcel(int grams, boolean express) {
        this(grams, express, 0);
    }

    Parcel(int grams, boolean express, int insured) {
        this.grams = grams;
        this.express = express;
        this.insured = insured;
    }

    long cost() {
        long c = 40;
        if (grams > 500) c += 10L * ((grams - 500 + 99) / 100);
        if (express) c *= 2;
        c += insured * 2L / 100;
        return c;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());
        for (int i = 0; i < n; i++) {
            String[] p = sc.nextLine().trim().split("\\s+");
            int g = Integer.parseInt(p[0]);
            Parcel x;
            if (p.length == 1) x = new Parcel(g);
            else if (p.length == 2) x = new Parcel(g, p[1].equals("Y"));
            else x = new Parcel(g, p[1].equals("Y"), Integer.parseInt(p[2]));
            System.out.println(x.cost());
        }
    }
}
