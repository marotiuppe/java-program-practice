import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = sc.nextInt();
        int max = a[0];
        for (int v : a) if (v > max) max = v;
        boolean found = false;
        int second = 0;
        for (int v : a) {
            if (v < max && (!found || v > second)) { second = v; found = true; }
        }
        System.out.println(found ? String.valueOf(second) : "No second largest");
    }
}
