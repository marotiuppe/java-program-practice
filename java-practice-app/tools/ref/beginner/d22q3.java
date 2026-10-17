import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = sc.nextInt();
        int m = sc.nextInt();
        int[] b = new int[m];
        for (int i = 0; i < m; i++) b[i] = sc.nextInt();
        List<Integer> out = new ArrayList<>();
        int i = 0, j = 0;
        while (i < n && j < m) out.add(a[i] <= b[j] ? a[i++] : b[j++]);
        while (i < n) out.add(a[i++]);
        while (j < m) out.add(b[j++]);
        StringBuilder sb = new StringBuilder();
        for (int v : out) { if (sb.length() > 0) sb.append(' '); sb.append(v); }
        System.out.println(sb);
    }
}