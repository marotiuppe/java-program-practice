import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = br.readLine();
        if (s == null) s = "";
        s = s.trim();
        int[] stack = new int[s.length() + 1];
        int top = 0;
        stack[top++] = -1;
        int best = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                stack[top++] = i;
            } else {
                top--;
                if (top == 0) {
                    stack[top++] = i;
                } else {
                    best = Math.max(best, i - stack[top - 1]);
                }
            }
        }
        System.out.println(best);
    }
}
