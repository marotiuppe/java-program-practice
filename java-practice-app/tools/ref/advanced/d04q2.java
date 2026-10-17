import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        StringTokenizer st = new StringTokenizer(new String(System.in.readAllBytes()));
        int n = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken());
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = Integer.parseInt(st.nextToken());
        }
        int[] dq = new int[n];
        int head = 0;
        int tail = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            while (tail > head && dq[head] <= i - k) {
                head++;
            }
            while (tail > head && a[dq[tail - 1]] <= a[i]) {
                tail--;
            }
            dq[tail++] = i;
            if (i >= k - 1) {
                if (sb.length() > 0) {
                    sb.append(' ');
                }
                sb.append(a[dq[head]]);
            }
        }
        System.out.println(sb);
    }
}
