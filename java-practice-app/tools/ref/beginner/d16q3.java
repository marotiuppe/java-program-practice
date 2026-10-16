import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.next();
        int[] cnt = new int[26];
        for (char c : s.toCharArray()) cnt[c - 'a']++;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 26; i++) {
            if (cnt[i] > 0) {
                if (sb.length() > 0) sb.append(' ');
                sb.append((char) ('a' + i)).append('=').append(cnt[i]);
            }
        }
        System.out.println(sb);
    }
}
