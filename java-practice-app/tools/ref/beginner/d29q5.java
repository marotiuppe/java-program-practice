
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static boolean isPalindrome(String s) {
        s = s.toLowerCase();
        int i = 0, j = s.length() - 1;
        while (i < j) {
            if (s.charAt(i) != s.charAt(j)) return false;
            i++;
            j--;
        }
        return true;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        List<String> pals = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String w = sc.next();
            if (isPalindrome(w)) pals.add(w);
        }
        System.out.println("Palindromes: " + pals.size());
        for (String p : pals) System.out.println(p.toUpperCase());
    }
}