import java.util.*;

public class Main {
    static char[] c;
    static boolean[] used;
    static StringBuilder cur = new StringBuilder();
    static StringBuilder out = new StringBuilder();
    static int count = 0;

    static void go() {
        if (cur.length() == c.length) {
            out.append(cur).append('\n');
            count++;
            return;
        }
        for (int i = 0; i < c.length; i++) {
            if (used[i]) continue;
            if (i > 0 && c[i] == c[i - 1] && !used[i - 1]) continue;
            used[i] = true;
            cur.append(c[i]);
            go();
            cur.deleteCharAt(cur.length() - 1);
            used[i] = false;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        c = sc.next().toCharArray();
        Arrays.sort(c);
        used = new boolean[c.length];
        go();
        out.append("Total: ").append(count).append('\n');
        System.out.print(out);
    }
}
