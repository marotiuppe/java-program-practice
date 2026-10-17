import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = sc.nextInt();
        Arrays.sort(a);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i + 2 < n; i++) {
            if (i > 0 && a[i] == a[i - 1]) continue;
            int l = i + 1, r = n - 1;
            while (l < r) {
                int s = a[i] + a[l] + a[r];
                if (s == 0) {
                    sb.append(a[i]).append(' ').append(a[l]).append(' ').append(a[r]).append('\n');
                    l++;
                    r--;
                    while (l < r && a[l] == a[l - 1]) l++;
                    while (l < r && a[r] == a[r + 1]) r--;
                } else if (s < 0) {
                    l++;
                } else {
                    r--;
                }
            }
        }
        if (sb.length() == 0) System.out.println("No triplets");
        else System.out.print(sb);
    }
}
