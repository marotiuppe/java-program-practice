import java.util.*;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) {
        List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5, 6);
        int sum = nums.stream().filter(n -> n % 2 == 0).mapToInt(n -> n * n).sum();
        System.out.println(sum);

        List<String> names = Arrays.asList("asha", "ravi", "meena");
        List<String> upper = names.stream().map(String::toUpperCase).collect(Collectors.toList());
        System.out.println(upper);

        List<String> longNames = names.stream().filter(w -> w.length() > 4).collect(Collectors.toList());
        System.out.println(longNames.size());
        System.out.println(longNames.stream().findFirst().orElse("none"));

        Comparator<String> byLen = (a, b) -> a.length() - b.length();
    }
}
