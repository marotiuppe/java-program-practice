import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        long n = Math.abs((long) sc.nextInt());
        int count = 0;
        do {
            count++;
            n /= 10;
        } while (n > 0);
        System.out.println(count);
    }
}
