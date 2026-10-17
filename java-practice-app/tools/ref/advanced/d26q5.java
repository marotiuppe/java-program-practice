import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Main {
    static final class Schedule {
        private final List<String> slots;
        private final int[] capacity;

        Schedule(List<String> slots, int[] capacity) {
            this.slots = new ArrayList<>(slots);
            this.capacity = capacity.clone();
        }

        List<String> getSlots() {
            return new ArrayList<>(slots);
        }

        int[] getCapacity() {
            return capacity.clone();
        }

        Schedule withSlot(String s) {
            List<String> copy = new ArrayList<>(slots);
            copy.add(s);
            return new Schedule(copy, capacity);
        }
    }

    public static void main(String[] args) {
        List<String> slots = new ArrayList<>(Arrays.asList("9am", "10am"));
        int[] cap = {5, 8};
        Schedule sc = new Schedule(slots, cap);
        slots.add("midnight");
        cap[0] = 0;
        System.out.println(sc.getSlots() + " " + Arrays.toString(sc.getCapacity()));
        sc.getSlots().clear();
        sc.getCapacity()[1] = -1;
        System.out.println(sc.getSlots() + " " + Arrays.toString(sc.getCapacity()));

        Schedule fresh = new Schedule(new ArrayList<>(Arrays.asList("9am", "10am")), new int[]{5, 8});
        Schedule more = fresh.withSlot("11am");
        System.out.println(more.getSlots());
        System.out.println(fresh.getSlots());
    }
}
