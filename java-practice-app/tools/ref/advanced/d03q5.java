import java.util.ArrayDeque;
import java.util.Deque;

public class Main {
    static String decode(String s) {
        Deque<Integer> counts = new ArrayDeque<>();
        Deque<StringBuilder> stack = new ArrayDeque<>();
        StringBuilder cur = new StringBuilder();
        int num = 0;
        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) {
                num = num * 10 + (c - '0');
            } else if (c == '[') {
                counts.push(num);
                stack.push(cur);
                cur = new StringBuilder();
                num = 0;
            } else if (c == ']') {
                StringBuilder prev = stack.pop();
                int times = counts.pop();
                for (int i = 0; i < times; i++) {
                    prev.append(cur);
                }
                cur = prev;
            } else {
                cur.append(c);
            }
        }
        return cur.toString();
    }

    public static void main(String[] args) {
        System.out.println(decode("3[a2[c]]"));
        System.out.println(decode("2[ab]3[x]"));
        System.out.println(decode("10[z]"));
        System.out.println(decode("ab2[c]"));
    }
}
