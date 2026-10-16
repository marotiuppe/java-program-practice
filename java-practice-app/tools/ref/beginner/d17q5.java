import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String a = sc.next().toLowerCase();
        String b = sc.next().toLowerCase();
        if (a.length() != b.length()) {
            System.out.println("Not anagrams");
            return;
        }
        char[] x = a.toCharArray();
        char[] y = b.toCharArray();
        Arrays.sort(x);
        Arrays.sort(y);
        if (Arrays.equals(x, y)) {
            System.out.println("Anagrams");
        } else {
            System.out.println("Not anagrams");
        }
    }
}
