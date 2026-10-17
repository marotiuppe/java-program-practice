import java.util.*;
import java.io.*;

public class Main {
    static int[] a;
    static List<String> out = new ArrayList<>();
    static int[] cur = new int[32];

    static void go(int idx, int rem, int len) {
        if (rem == 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < len; i++) {
                if (i > 0) sb.append(' ');
                sb.append(cur[i]);
            }
            out.add(sb.toString());
            return;
        }
        for (int i = idx; i < a.length; i++) {
            if (i > idx && a[i] == a[i - 1]) continue;
            if (a[i] > rem) break;
            cur[len] = a[i];
            go(i + 1, rem - a[i], len + 1);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int B = Integer.parseInt(st.nextToken());
        st = new StringTokenizer(br.readLine());
        a = new int[n];
        for (int i = 0; i < n; i++) a[i] = Integer.parseInt(st.nextToken());
        Arrays.sort(a);
        go(0, B, 0);
        if (out.isEmpty()) System.out.println("NONE");
        else for (String s : out) System.out.println(s);
    }
}
