import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        StringTokenizer st = new StringTokenizer(new String(System.in.readAllBytes()));
        int n = Integer.parseInt(st.nextToken());
        long[] h = new long[n];
        for (int i = 0; i < n; i++) {
            h[i] = Long.parseLong(st.nextToken());
        }
        int[] right = new int[n];
        int[] left = new int[n];
        int[] stack = new int[n];
        int sp = 0;
        for (int i = n - 1; i >= 0; i--) {
            while (sp > 0 && h[stack[sp - 1]] <= h[i]) {
                sp--;
            }
            right[i] = sp == 0 ? 0 : stack[sp - 1] - i;
            stack[sp++] = i;
        }
        sp = 0;
        for (int i = 0; i < n; i++) {
            while (sp > 0 && h[stack[sp - 1]] <= h[i]) {
                sp--;
            }
            left[i] = sp == 0 ? 0 : i - stack[sp - 1];
            stack[sp++] = i;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(right[i]);
        }
        sb.append('\n');
        for (int i = 0; i < n; i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(left[i]);
        }
        System.out.println(sb);
    }
}
