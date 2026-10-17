import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        while (n-- > 0) {
            String t = sc.next();
            if (Character.isDigit(t.charAt(0))) {
                long v = Long.parseLong(t);
                StringBuilder sb = new StringBuilder();
                while (v > 0) {
                    v--;
                    sb.append((char) ('A' + v % 26));
                    v /= 26;
                }
                System.out.println(sb.reverse());
            } else {
                long v = 0;
                for (char ch : t.toCharArray()) v = v * 26 + (ch - 'A' + 1);
                System.out.println(v);
            }
        }
    }
}
