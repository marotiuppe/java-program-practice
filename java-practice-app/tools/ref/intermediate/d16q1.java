import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        long[] a = new long[n];
        for (int i = 0; i < n; i++) a[i] = sc.nextLong();
        long budget = sc.nextLong();
        Arrays.sort(a);
        int i = 0, j = n - 1;
        long best = -1, ba = 0, bb = 0;
        while (i < j) {
            long s = a[i] + a[j];
            if (s <= budget) {
                if (s > best) {
                    best = s;
                    ba = a[i];
                    bb = a[j];
                }
                i++;
            } else {
                j--;
            }
        }
        if (best < 0) System.out.println("No pair");
        else System.out.println(ba + " " + bb + " = " + best);
    }
}
