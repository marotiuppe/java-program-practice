import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        boolean pal = false;
        if (n >= 0) {
            int t = n, rev = 0;
            while (t > 0) { rev = rev * 10 + t % 10; t /= 10; }
            pal = rev == n;
        }
        System.out.println(pal ? "Palindrome" : "Not a palindrome");
    }
}
