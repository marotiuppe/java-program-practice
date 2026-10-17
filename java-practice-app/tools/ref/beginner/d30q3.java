import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Map<String, Integer> st = new TreeMap<>();
        for (int i = 0; i < n; i++) {
            String c = sc.next();
            if (c.equals("ADD")) {
                String t = sc.next(); int q = sc.nextInt();
                st.merge(t, q, Integer::sum);
                System.out.println("Added: " + t + " x" + q);
            } else if (c.equals("BORROW")) {
                String t = sc.next();
                if (!st.containsKey(t)) System.out.println("Unknown: " + t);
                else if (st.get(t) == 0) System.out.println("Unavailable: " + t);
                else { st.put(t, st.get(t) - 1); System.out.println("Borrowed: " + t); }
            } else if (c.equals("RETURN")) {
                String t = sc.next();
                if (!st.containsKey(t)) System.out.println("Unknown: " + t);
                else { st.put(t, st.get(t) + 1); System.out.println("Returned: " + t); }
            } else {
                for (Map.Entry<String, Integer> e : st.entrySet()) System.out.println(e.getKey() + ": " + e.getValue());
            }
        }
    }
}