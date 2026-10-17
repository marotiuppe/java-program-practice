import java.util.*;

final class Schedule {
    private final int[] slots;

    Schedule(int[] slots) {
        this.slots = slots.clone();
    }

    int[] getSlots() {
        return slots.clone();
    }

    int total() {
        int s = 0;
        for (int v : slots) s += v;
        return s;
    }

    Schedule withSlot(int index, int value) {
        int[] c = slots.clone();
        c[index] = value;
        return new Schedule(c);
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = sc.nextInt();
        int idx = sc.nextInt();
        int val = sc.nextInt();
        if (idx < 0 || idx >= n) {
            System.out.println("Invalid index");
            return;
        }
        Schedule orig = new Schedule(a);
        Schedule upd = orig.withSlot(idx, val);
        a[0] = 99999;
        orig.getSlots()[0] = 99999;
        System.out.println("Original total: " + orig.total());
        System.out.println("Updated total: " + upd.total());
        System.out.println("Tamper check: " + orig.total());
    }
}
