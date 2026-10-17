
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        HashMap<String, Integer> counts = new HashMap<>();
        for (int i = 0; i < n; i++) {
            String w = sc.next();
            if (counts.containsKey(w)) {
                counts.put(w, counts.get(w) + 1);
            } else {
                counts.put(w, 1);
            }
        }
        String best = null;
        for (String key : counts.keySet()) {
            if (best == null || counts.get(key) > counts.get(best)) best = key;
        }
        System.out.println("Most frequent: " + best);
    }
}