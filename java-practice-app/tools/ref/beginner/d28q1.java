import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String t = sc.next();
        int nights = sc.nextInt();
        String bf = sc.next();
        int rate;
        if (t.equals("S")) rate = 2000;
        else if (t.equals("D")) rate = 3500;
        else if (t.equals("L")) rate = 5000;
        else { System.out.println("Invalid room type"); return; }
        double room = rate * nights;
        double pct = nights >= 7 ? 0.20 : nights >= 3 ? 0.10 : 0;
        double disc = room * pct;
        double b = bf.equals("Y") ? 300.0 * nights : 0;
        double taxable = room - disc + b;
        double gst = taxable * 0.12;
        System.out.printf("Room charge: %.2f%n", room);
        System.out.printf("Discount: %.2f%n", disc);
        System.out.printf("Breakfast: %.2f%n", b);
        System.out.printf("GST: %.2f%n", gst);
        System.out.printf("Total: %.2f%n", taxable + gst);
    }
}