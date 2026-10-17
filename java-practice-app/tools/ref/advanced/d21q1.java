import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = br.readLine();
        String p = br.readLine();
        if (s == null) s = "";
        if (p == null) p = "";
        int n = s.length(), m = p.length();
        int count = 0;
        StringBuilder sb = new StringBuilder();
        if (m > 0 && m <= n) {
            int[] lps = new int[m];
            int k = 0;
            for (int i = 1; i < m; i++) {
                while (k > 0 && p.charAt(i) != p.charAt(k)) k = lps[k - 1];
                if (p.charAt(i) == p.charAt(k)) k++;
                lps[i] = k;
            }
            k = 0;
            for (int i = 0; i < n; i++) {
                while (k > 0 && s.charAt(i) != p.charAt(k)) k = lps[k - 1];
                if (s.charAt(i) == p.charAt(k)) k++;
                if (k == m) {
                    if (count > 0) sb.append(' ');
                    sb.append(i - m + 1);
                    count++;
                    k = lps[k - 1];
                }
            }
        }
        System.out.println("Count: " + count);
        System.out.println("Positions: " + (count == 0 ? "none" : sb.toString()));
    }
}
