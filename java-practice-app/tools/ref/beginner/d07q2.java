import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        char c = sc.next().charAt(0);
        if (Character.isLetter(c)) {
            System.out.println("aeiouAEIOU".indexOf(c) >= 0 ? "Vowel" : "Consonant");
        } else if (Character.isDigit(c)) {
            System.out.println("Digit");
        } else {
            System.out.println("Special character");
        }
    }
}
