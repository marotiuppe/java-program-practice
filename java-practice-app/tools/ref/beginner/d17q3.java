import java.util.*;

public class Main {
    static boolean anagram(String a, String b) {
        char[] x = a.toLowerCase().toCharArray(), y = b.toLowerCase().toCharArray();
        Arrays.sort(x); Arrays.sort(y);
        return Arrays.equals(x, y);
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String a = sc.next(), b = sc.next(), c = sc.next();
        int n = 0;
        if (anagram(a, b)) n++;
        if (anagram(a, c)) n++;
        if (anagram(b, c)) n++;
        System.out.println("Anagram pairs: " + n);
    }
}
