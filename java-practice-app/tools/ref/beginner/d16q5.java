import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String text = sc.nextLine();
        int vowels = 0;
        for (int i = 0; i < text.length(); i++) {
            char ch = Character.toLowerCase(text.charAt(i));
            if (ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u') {
                vowels++;
            }
        }
        System.out.println("Vowels: " + vowels);
    }
}
