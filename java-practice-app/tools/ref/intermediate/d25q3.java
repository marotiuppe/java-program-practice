import java.io.*;
import java.util.*;
import java.util.function.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        Map<String, Function<Integer, Predicate<String>>> table = new HashMap<>();
        table.put("MINLEN", n -> s -> s.length() >= n);
        table.put("HASDIGIT", n -> s -> s.chars().anyMatch(Character::isDigit));
        table.put("HASUPPER", n -> s -> s.chars().anyMatch(Character::isUpperCase));
        table.put("NOSPACE", n -> s -> s.indexOf(' ') < 0);
        table.put("MAXREPEAT", k -> s -> {
            int run = 0;
            for (int i = 0; i < s.length(); i++) {
                run = (i > 0 && s.charAt(i) == s.charAt(i - 1)) ? run + 1 : 1;
                if (run > k) return false;
            }
            return true;
        });
        int r = Integer.parseInt(in.readLine().trim());
        List<String> specs = new ArrayList<>();
        List<Predicate<String>> preds = new ArrayList<>();
        for (int i = 0; i < r; i++) {
            String spec = in.readLine().trim();
            String[] p = spec.split("\s+");
            specs.add(spec);
            preds.add(table.get(p[0]).apply(p.length > 1 ? Integer.parseInt(p[1]) : 0));
        }
        int q = Integer.parseInt(in.readLine().trim());
        for (int i = 0; i < q; i++) {
            String pw = in.readLine();
            List<String> failed = new ArrayList<>();
            for (int j = 0; j < r; j++) if (!preds.get(j).test(pw)) failed.add(specs.get(j));
            System.out.println(failed.isEmpty() ? "OK" : "FAIL: " + String.join(",", failed));
        }
    }
}
