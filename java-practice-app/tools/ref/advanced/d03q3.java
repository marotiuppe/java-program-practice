import java.io.*;
import java.util.*;

public class Main {
    static int prec(char op) {
        if (op == 'u') {
            return 3;
        }
        return (op == '*' || op == '/') ? 2 : 1;
    }

    static void apply(long[] vals, int[] vp, char op) {
        if (op == 'u') {
            vals[vp[0] - 1] = -vals[vp[0] - 1];
            return;
        }
        long b = vals[--vp[0]];
        long a = vals[--vp[0]];
        long r;
        switch (op) {
            case '+': r = a + b; break;
            case '-': r = a - b; break;
            case '*': r = a * b; break;
            default: r = a / b; break;
        }
        vals[vp[0]++] = r;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = br.readLine();
        if (s == null) {
            s = "";
        }
        int n = s.length();
        long[] vals = new long[n + 2];
        int[] vp = {0};
        char[] ops = new char[n + 2];
        int op = 0;
        boolean expectOperand = true;
        int i = 0;
        while (i < n) {
            char c = s.charAt(i);
            if (c == ' ') {
                i++;
            } else if (Character.isDigit(c)) {
                long v = 0;
                while (i < n && Character.isDigit(s.charAt(i))) {
                    v = v * 10 + (s.charAt(i) - '0');
                    i++;
                }
                vals[vp[0]++] = v;
                expectOperand = false;
            } else if (c == '(') {
                ops[op++] = '(';
                expectOperand = true;
                i++;
            } else if (c == ')') {
                while (ops[op - 1] != '(') {
                    apply(vals, vp, ops[--op]);
                }
                op--;
                expectOperand = false;
                i++;
            } else if (c == '-' && expectOperand) {
                ops[op++] = 'u';
                i++;
            } else {
                while (op > 0 && ops[op - 1] != '(' && prec(ops[op - 1]) >= prec(c)) {
                    apply(vals, vp, ops[--op]);
                }
                ops[op++] = c;
                expectOperand = true;
                i++;
            }
        }
        while (op > 0) {
            apply(vals, vp, ops[--op]);
        }
        System.out.println(vals[0]);
    }
}
