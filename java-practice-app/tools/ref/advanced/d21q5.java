public class Main {
    static int indexOf(String text, String pat) {
        int n = text.length();
        int m = pat.length();
        if (m > n) {
            return -1;
        }
        final int base = 256;
        final int mod = 101;
        long hp = 0;
        long ht = 0;
        long pow = 1;
        for (int i = 0; i < m; i++) {
            hp = (hp * base + pat.charAt(i)) % mod;
            ht = (ht * base + text.charAt(i)) % mod;
            if (i > 0) {
                pow = pow * base % mod;
            }
        }
        for (int i = 0; i + m <= n; i++) {
            if (hp == ht && text.startsWith(pat, i)) {
                return i;
            }
            if (i + m < n) {
                ht = ((ht - text.charAt(i) * pow % mod + mod) * base + text.charAt(i + m)) % mod;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        System.out.println(indexOf("hello world", "world"));
        System.out.println(indexOf("abcabcabd", "abd"));
        System.out.println(indexOf("aaaaa", "ab"));
        System.out.println(indexOf("abc", "abcd"));
        System.out.println(indexOf("xyz", ""));
    }
}
