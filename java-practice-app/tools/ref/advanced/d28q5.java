import java.util.HashMap;
import java.util.Map;

public class Main {
    static class Library {
        private final Map<String, Integer> loans = new HashMap<>();
        private final Map<String, String> holder = new HashMap<>();
        private final Map<String, Integer> count = new HashMap<>();

        boolean borrow(String member, String book, int day) {
            if (count.getOrDefault(member, 0) >= 3) {
                return false;
            }
            if (loans.containsKey(book)) {
                return false;
            }
            loans.put(book, day);
            holder.put(book, member);
            count.merge(member, 1, Integer::sum);
            return true;
        }

        int giveBack(String book, int day, int priceCents) {
            Integer start = loans.get(book);
            if (start == null) {
                return -1;
            }
            int fine = 25 * Math.max(0, day - (start + 14));
            fine = Math.min(fine, priceCents);
            loans.remove(book);
            String member = holder.remove(book);
            count.merge(member, -1, Integer::sum);
            return fine;
        }
    }

    public static void main(String[] args) {
        Library lib = new Library();
        System.out.println(lib.borrow("ann", "b1", 0));
        System.out.println(lib.borrow("bob", "b1", 1));
        System.out.println(lib.borrow("ann", "b2", 0));
        System.out.println(lib.borrow("ann", "b3", 0));
        System.out.println(lib.borrow("ann", "b4", 0));
        System.out.println(lib.giveBack("b1", 10, 5000));
        System.out.println(lib.giveBack("b2", 24, 5000));
        System.out.println(lib.giveBack("b2", 25, 5000));
        System.out.println(lib.borrow("ann", "b4", 30));
        System.out.println(lib.giveBack("b3", 500, 300));
    }
}
