import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String word = sc.next();
        if (word.equals("admin")) {
            System.out.println("Welcome, administrator");
        }
        char first = word.charAt(0);
        char last = word.charAt(word.length() - 1);
        System.out.println("First: " + first + ", Last: " + last);
        word = word.toUpperCase();
        System.out.println("Upper: " + word);
    }
}
