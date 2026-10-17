import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        StringBuilder out = new StringBuilder();
        for (int t = 0; t < n; t++) {
            String s = sc.next();
            int[] last = new int[256];
            Arrays.fill(last, -1);
            int left = 0, bestLen = 0, bestStart = 0;
            for (int r = 0; r < s.length(); r++) {
                char c = s.charAt(r);
                if (last[c] >= left) left = last[c] + 1;
                last[c] = r;
                if (r - left + 1 > bestLen) {
                    bestLen = r - left + 1;
                    bestStart = left;
                }
            }
            out.append(bestLen).append(' ').append(s, bestStart, bestStart + bestLen).append('\n');
        }
        System.out.print(out);
    }
}
