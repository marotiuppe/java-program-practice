import java.util.*;
import java.io.*;

public class Main {
    static String s;
    static StringBuilder out = new StringBuilder();
    static List<String> parts = new ArrayList<>();

    static boolean pal(int l, int r) {
        while (l < r) {
            if (s.charAt(l++) != s.charAt(r--)) return false;
        }
        return true;
    }

    static void go(int start) {
        if (start == s.length()) {
            out.append(String.join(" ", parts)).append('\n');
            return;
        }
        for (int e = start; e < s.length(); e++) {
            if (pal(start, e)) {
                parts.add(s.substring(start, e + 1));
                go(e + 1);
                parts.remove(parts.size() - 1);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        s = br.readLine().trim();
        go(0);
        System.out.print(out);
    }
}
