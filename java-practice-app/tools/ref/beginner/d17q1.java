import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine().replace(" ", "").toLowerCase();
        boolean pal = true;
        for (int i = 0; i < s.length() / 2; i++) if (s.charAt(i) != s.charAt(s.length() - 1 - i)) pal = false;
        System.out.println(pal ? "Palindrome phrase" : "Not a palindrome phrase");
    }
}
