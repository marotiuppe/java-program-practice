import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        int vowels = 0;
        for (char ch : s.toLowerCase().toCharArray()) if ("aeiou".indexOf(ch) >= 0) vowels++;
        System.out.println("Characters: " + s.length());
        System.out.println("Words: " + s.split(" ").length);
        System.out.println("Vowels: " + vowels);
    }
}
