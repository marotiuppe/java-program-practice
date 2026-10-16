import java.util.*;

public class Main {
    static boolean anagram(String a, String b) {
        char[] x = a.toLowerCase().toCharArray(), y = b.toLowerCase().toCharArray();
        Arrays.sort(x); Arrays.sort(y);
        return Arrays.equals(x, y);
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String a = sc.next(), b = sc.next();
        System.out.println(anagram(a, b) ? "Anagrams" : "Not anagrams");
    }
}
