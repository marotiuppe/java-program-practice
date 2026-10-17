import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        while (t-- > 0) {
            String s = sc.next();
            int n = s.length(), bestStart = 0, bestLen = 1;
            for (int c = 0; c < n; c++) {
                for (int type = 0; type < 2; type++) {
                    int l = c, r = c + type;
                    while (l >= 0 && r < n && s.charAt(l) == s.charAt(r)) {
                        l--;
                        r++;
                    }
                    int len = r - l - 1;
                    if (len > bestLen) {
                        bestLen = len;
                        bestStart = l + 1;
                    }
                }
            }
            System.out.println(s.substring(bestStart, bestStart + bestLen));
        }
    }
}
