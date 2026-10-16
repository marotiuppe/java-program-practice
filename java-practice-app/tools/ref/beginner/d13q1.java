import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int rows = sc.nextInt(), seats = sc.nextInt();
        for (int r = 0; r < rows; r++) {
            StringBuilder sb = new StringBuilder();
            for (int s = 1; s <= seats; s++) {
                if (s > 1) sb.append(' ');
                sb.append((char) ('A' + r)).append(s);
            }
            System.out.println(sb);
        }
    }
}
