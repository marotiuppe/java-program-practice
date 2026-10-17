import java.util.*;
import java.util.stream.*;

public class Main {
    record Emp(String name, String dept, int salary) {}

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        List<Emp> emps = new ArrayList<>();
        for (int i = 0; i < n; i++) emps.add(new Emp(sc.next(), sc.next(), sc.nextInt()));
        Map<String, List<Emp>> by = emps.stream().collect(Collectors.groupingBy(Emp::dept, TreeMap::new, Collectors.toList()));
        by.forEach((d, l) -> {
            double avg = l.stream().mapToInt(Emp::salary).average().orElse(0);
            Emp top = l.stream().reduce((a, b) -> b.salary() > a.salary() ? b : a).get();
            System.out.println(d + ": count=" + l.size() + " avg=" + String.format(Locale.US, "%.2f", avg) + " top=" + top.name());
        });
        double overall = emps.stream().mapToLong(Emp::salary).sum() / (double) n;
        String above = emps.stream().filter(e -> e.salary() > overall).map(Emp::name).sorted().collect(Collectors.joining(", "));
        System.out.println("Above average: " + (above.isEmpty() ? "none" : above));
    }
}
