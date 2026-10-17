import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        String[] a = new String[n];
        for (int i = 0; i < n; i++) a[i] = sc.next();
        Arrays.sort(a, (x, y) -> (y + x).compareTo(x + y));
        if (a[0].equals("0")) {
            System.out.println("0");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (String s : a) sb.append(s);
        System.out.println(sb);
    }
}
