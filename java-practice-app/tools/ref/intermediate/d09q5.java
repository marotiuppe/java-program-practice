import java.util.*;

public class Main {
    public static void main(String[] args) {
        String text = "Apple banana apple Cherry BANANA date";
        String[] words = text.split(" ");
        Set<String> seen = new HashSet<>();
        for (String w : words) {
            seen.add(w.toLowerCase());
        }
        System.out.println("Unique: " + seen.size());
        int duplicates = words.length - seen.size();
        System.out.println("Duplicates: " + duplicates);

        Set<String> sorted = new TreeSet<>(seen);
        for (String w : sorted) {
            System.out.println(w);
        }

        Set<Character> letters = new TreeSet<>();
        for (char ch : "banana".toCharArray()) {
            letters.add(ch);
        }
        System.out.println(letters);
    }
}
