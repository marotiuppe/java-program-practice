import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = br.readLine().trim();
        String t = br.readLine().trim();
        int[] need = new int[128];
        for (char c : t.toCharArray()) need[c]++;
        int missing = t.length();
        int bestLen = Integer.MAX_VALUE, bestStart = 0;
        int left = 0;
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            if (need[c] > 0) missing--;
            need[c]--;
            while (missing == 0) {
                int len = right - left + 1;
                if (len < bestLen) {
                    bestLen = len;
                    bestStart = left;
                }
                char l = s.charAt(left);
                need[l]++;
                if (need[l] > 0) missing++;
                left++;
            }
        }
        System.out.println(bestLen == Integer.MAX_VALUE ? "NONE" : s.substring(bestStart, bestStart + bestLen));
    }
}
