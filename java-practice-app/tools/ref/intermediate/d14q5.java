import java.util.*;

public class Main {
    public static void main(String[] args) {
        String[] names = {"Asha", "Ravi", "Meena", "Joy"};
        int[] marks = {90, 71, 85, 80};
        Map<Character, List<String>> byGrade = new TreeMap<>();
        for (int i = 0; i < names.length; i++) {
            char g = marks[i] >= 85 ? 'A' : 'B';
            byGrade.computeIfAbsent(g, k -> new ArrayList<>()).add(names[i]);
        }

        for (Map.Entry<Character, List<String>> e : byGrade.entrySet()) {
            int sum = 0;
            for (String n : e.getValue()) {
                sum += marks[Arrays.asList(names).indexOf(n)];
            }
            double avg = (double) sum / e.getValue().size();
            System.out.println(e.getKey() + ": " + e.getValue() + " avg " + avg);
        }

        List<String> sorted = new ArrayList<>(Arrays.asList(names));
        sorted.sort((x, y) -> marks[Arrays.asList(names).indexOf(y)]
                - marks[Arrays.asList(names).indexOf(x)]);
        System.out.println("Top: " + sorted.get(0));
    }
}
