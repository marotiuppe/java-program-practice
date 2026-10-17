import java.util.*;
import java.io.*;

public class Main {
    static final long MOD = 1_000_000_007L;

    static long one(char c) {
        if (c == '*') return 9;
        return c == '0' ? 0 : 1;
    }

    static long two(char a, char b) {
        if (a == '*' && b == '*') return 15;
        if (a == '*') return b <= '6' ? 2 : 1;
        if (b == '*') {
            if (a == '1') return 9;
            if (a == '2') return 6;
            return 0;
        }
        if (a == '0') return 0;
        int v = (a - '0') * 10 + (b - '0');
        return v <= 26 ? 1 : 0;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = br.readLine().trim();
        long prev2 = 1, prev1 = one(s.charAt(0));
        for (int i = 1; i < s.length(); i++) {
            long cur = (prev1 * one(s.charAt(i)) + prev2 * two(s.charAt(i - 1), s.charAt(i))) % MOD;
            prev2 = prev1;
            prev1 = cur;
        }
        System.out.println(prev1 % MOD);
    }
}
