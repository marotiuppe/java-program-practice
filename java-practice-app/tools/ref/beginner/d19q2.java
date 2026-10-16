import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = sc.nextInt();
        int t = sc.nextInt();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) if (a[i] == t) sb.append(sb.length() > 0 ? " " : "").append(i);
        System.out.println(sb.length() == 0 ? "Not found" : "Found at: " + sb);
    }
}
