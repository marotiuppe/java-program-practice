import java.util.*;
import java.io.*;

public class Main {
    static int[] p, size;

    static int find(int x) {
        while (p[x] != x) {
            p[x] = p[p[x]];
            x = p[x];
        }
        return x;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int q = Integer.parseInt(st.nextToken());
        p = new int[n + 1];
        size = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            p[i] = i;
            size[i] = 1;
        }
        int circles = n;
        int largest = 1;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < q; i++) {
            st = new StringTokenizer(br.readLine());
            String cmd = st.nextToken();
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            int ra = find(a), rb = find(b);
            if (cmd.equals("FRIEND")) {
                if (ra != rb) {
                    if (size[ra] < size[rb]) {
                        int t = ra;
                        ra = rb;
                        rb = t;
                    }
                    p[rb] = ra;
                    size[ra] += size[rb];
                    circles--;
                    largest = Math.max(largest, size[ra]);
                }
                sb.append(circles).append(' ').append(largest).append('\n');
            } else {
                sb.append(ra == rb ? "YES" : "NO").append('\n');
            }
        }
        System.out.print(sb);
    }
}
