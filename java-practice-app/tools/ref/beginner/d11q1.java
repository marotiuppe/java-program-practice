import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int code = sc.nextInt();
        int t = code, rev = 0;
        while (t > 0) { rev = rev * 10 + t % 10; t /= 10; }
        System.out.println("Reversed: " + rev);
        System.out.println(rev == code ? "Rejected: palindrome code" : "Accepted");
    }
}
