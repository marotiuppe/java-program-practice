import java.util.*;

public class Main {
    public static void main(String[] args) {
        Map<String, Integer> votes = new HashMap<>();
        String[] ballots = {"A", "B", "A", "C", "A", "B"};
        for (String b : ballots) {
            votes.merge(b, 1, Integer::sum);
        }

        votes.put("D", 0);
        votes.values().removeIf(v -> v == 0);

        String winner = null;
        int best = 0;
        for (Map.Entry<String, Integer> e : votes.entrySet()) {
            if (e.getValue() > best) {
                winner = e.getKey();
                best = e.getValue();
            }
        }
        System.out.println(winner + " " + best);

        Integer x = 1000, y = 1000;
        System.out.println(x.equals(y));
    }
}
