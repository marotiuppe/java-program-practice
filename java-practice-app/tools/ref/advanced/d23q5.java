import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<String> words = Arrays.asList("Apple", "apple", "Pear", "fig", "pear", "Fig", "apple");
        Map<String, Integer> counts = words.stream().map(String::toLowerCase)
                .collect(Collectors.toMap(w -> w, w -> 1, Integer::sum, TreeMap::new));
        System.out.println(counts);
        long distinct = counts.size();
        System.out.println(distinct);
        double avg = (double) words.stream().mapToInt(String::length).sum() / words.size();
        System.out.printf("%.2f%n", avg);
    }
}
