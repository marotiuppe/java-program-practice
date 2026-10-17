import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        String p = sc.next();
        for (int i = 1; i < n; i++) {
            String w = sc.next();
            int k = 0;
            while (k < p.length() && k < w.length() && p.charAt(k) == w.charAt(k)) k++;
            p = p.substring(0, k);
        }
        System.out.println(p.isEmpty() ? "No common prefix" : p);
    }
}