import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] h = new int[n];
        for (int i = 0; i < n; i++) h[i] = sc.nextInt();
        int l = 0, r = n - 1, lm = 0, rm = 0;
        long water = 0;
        while (l <= r) {
            if (lm <= rm) {
                lm = Math.max(lm, h[l]);
                water += lm - h[l];
                l++;
            } else {
                rm = Math.max(rm, h[r]);
                water += rm - h[r];
                r--;
            }
        }
        System.out.println(water);
    }
}
