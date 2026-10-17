import java.util.*;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<String> w = Arrays.asList(sc.nextLine().trim().split("\s+"));
        System.out.println("Distinct sorted: " + w.stream().distinct().sorted().collect(Collectors.joining(" ")));
        Map<Integer, List<String>> g = w.stream().distinct().sorted()
                .collect(Collectors.groupingBy(String::length, TreeMap::new, Collectors.toList()));
        System.out.println("By length: " + g.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining(" ")));
        System.out.println("Longest: " + w.stream().reduce((a, b) -> b.length() > a.length() ? b : a).get());
        System.out.println("Total chars: " + w.stream().mapToInt(String::length).sum());
    }
}
