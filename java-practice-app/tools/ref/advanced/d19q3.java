import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = br.readLine().trim();
        int n = s.length();
        int[] cnt = new int[26];
        for (char c : s.toCharArray()) cnt[c - 'a']++;
        StringBuilder sb = new StringBuilder();
        int last = -1;
        for (int pos = 0; pos < n; pos++) {
            boolean placed = false;
            for (int c = 0; c < 26 && !placed; c++) {
                if (cnt[c] == 0 || c == last) continue;
                cnt[c]--;
                int rem = n - pos - 1;
                boolean ok = true;
                for (int d = 0; d < 26; d++) {
                    int limit = (d == c) ? rem / 2 : (rem + 1) / 2;
                    if (cnt[d] > limit) {
                        ok = false;
                        break;
                    }
                }
                if (ok) {
                    sb.append((char) ('a' + c));
                    last = c;
                    placed = true;
                } else {
                    cnt[c]++;
                }
            }
            if (!placed) {
                System.out.println("IMPOSSIBLE");
                return;
            }
        }
        System.out.println(sb);
    }
}
