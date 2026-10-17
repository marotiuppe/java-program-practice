import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int digit = sc.nextInt();
        int count = 0;
        do {
            int last = n % 10;
            if (last == digit)
                count++;
            n = n / 10;
        } while (n > 0);
        System.out.println("Digit " + digit + " appears " + count + " time(s)");
    }
}
