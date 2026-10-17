import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine().trim());
        StringBuilder sb = new StringBuilder();
        for (int t = 0; t < n; t++) {
            String s = br.readLine();
            if (s == null) s = "";
            s = s.trim();
            Deque<Integer> st = new ArrayDeque<>();
            int bad = -1;
            for (int i = 0; i < s.length() && bad < 0; i++) {
                char c = s.charAt(i);
                if (c == '(' || c == '[' || c == '{') {
                    st.push(i);
                } else {
                    char open = c == ')' ? '(' : c == ']' ? '[' : '{';
                    if (st.isEmpty() || s.charAt(st.peek()) != open) bad = i + 1;
                    else st.pop();
                }
            }
            if (bad < 0 && !st.isEmpty()) bad = st.peek() + 1;
            sb.append(bad < 0 ? "Balanced" : "Unbalanced at " + bad).append('\n');
        }
        System.out.print(sb);
    }
}
