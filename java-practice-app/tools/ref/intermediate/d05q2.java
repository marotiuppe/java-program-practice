import java.io.*;
import java.util.*;

interface Expr {
    int eval();

    String toString();
}

class Num implements Expr {
    private final int v;

    Num(int v) {
        this.v = v;
    }

    public int eval() {
        return v;
    }

    public String toString() {
        return Integer.toString(v);
    }
}

abstract class Bin implements Expr {
    protected final Expr l, r;
    private final String op;

    Bin(Expr l, Expr r, String op) {
        this.l = l;
        this.r = r;
        this.op = op;
    }

    public String toString() {
        return "(" + l + " " + op + " " + r + ")";
    }
}

class Add extends Bin {
    Add(Expr l, Expr r) {
        super(l, r, "+");
    }

    public int eval() {
        return l.eval() + r.eval();
    }
}

class Sub extends Bin {
    Sub(Expr l, Expr r) {
        super(l, r, "-");
    }

    public int eval() {
        return l.eval() - r.eval();
    }
}

class Mul extends Bin {
    Mul(Expr l, Expr r) {
        super(l, r, "*");
    }

    public int eval() {
        return l.eval() * r.eval();
    }
}

class Neg implements Expr {
    private final Expr x;

    Neg(Expr x) {
        this.x = x;
    }

    public int eval() {
        return -x.eval();
    }

    public String toString() {
        return "(-" + x + ")";
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();
        Deque<Expr> st = new ArrayDeque<>();
        for (String t : line.trim().split("\\s+")) {
            switch (t) {
                case "+": { Expr b = st.pop(), a = st.pop(); st.push(new Add(a, b)); break; }
                case "-": { Expr b = st.pop(), a = st.pop(); st.push(new Sub(a, b)); break; }
                case "*": { Expr b = st.pop(), a = st.pop(); st.push(new Mul(a, b)); break; }
                case "~": st.push(new Neg(st.pop())); break;
                default: st.push(new Num(Integer.parseInt(t)));
            }
        }
        Expr e = st.pop();
        System.out.println(e + " = " + e.eval());
    }
}
