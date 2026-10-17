import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        long[][] s = new long[n][2];
        for (int i = 0; i < n; i++) {
            s[i][0] = sc.nextLong();
            s[i][1] = sc.nextLong();
        }
        Arrays.sort(s, (a, b) -> a[0] != b[0] ? Long.compare(a[0], b[0]) : Long.compare(a[1], b[1]));
        StringBuilder sb = new StringBuilder();
        long total = 0;
        long cs = s[0][0], ce = s[0][1];
        for (int i = 1; i < n; i++) {
            if (s[i][0] <= ce) {
                ce = Math.max(ce, s[i][1]);
            } else {
                sb.append(cs).append(' ').append(ce).append('\n');
                total += ce - cs;
                cs = s[i][0];
                ce = s[i][1];
            }
        }
        sb.append(cs).append(' ').append(ce).append('\n');
        total += ce - cs;
        sb.append("Total: ").append(total).append('\n');
        System.out.print(sb);
    }
}
