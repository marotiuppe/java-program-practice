import java.util.HashMap;
import java.util.Map;

public class Main {
    static int longest(String s) {
        Map<Character, Integer> last = new HashMap<>();
        int left = 0;
        int best = 0;
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            if (last.containsKey(c)) {
                left = Math.max(left, last.get(c) + 1);
            }
            last.put(c, right);
            best = Math.max(best, right - left + 1);
        }
        return best;
    }

    public static void main(String[] args) {
        System.out.println(longest("abcabcbb"));
        System.out.println(longest("pwwkew"));
        System.out.println(longest("abba"));
        System.out.println(longest(""));
    }
}
