import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (int i = 1; i <= n; i++) {
            if (n % i == 0) {
                if (count > 0) sb.append(' ');
                sb.append(i);
                count++;
            }
        }
        System.out.println("Box sizes: " + sb);
        System.out.println("Options: " + count);
    }
}
