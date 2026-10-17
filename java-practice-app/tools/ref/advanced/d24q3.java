import java.io.*;
import java.util.*;

public class Main {
    sealed interface Expr permits Num, Add, Mul, Neg {}

    record Num(long v) implements Expr {}

    record Add(Expr l, Expr r) implements Expr {}

    record Mul(Expr l, Expr r) implements Expr {}

    record Neg(Expr e) implements Expr {}

    static String[] toks;
    static int pos;

    static Expr parse() {
        String t = toks[pos++];
        switch (t) {
            case "ADD": {
                Expr l = parse();
                Expr r = parse();
                return new Add(l, r);
            }
            case "MUL": {
                Expr l = parse();
                Expr r = parse();
                return new Mul(l, r);
            }
            case "NEG":
                return new Neg(parse());
            default:
                return new Num(Long.parseLong(t));
        }
    }

    static String render(Expr e) {
        if (e instanceof Num n) return Long.toString(n.v());
        if (e instanceof Add a) return "(" + render(a.l()) + " + " + render(a.r()) + ")";
        if (e instanceof Mul m) return "(" + render(m.l()) + " * " + render(m.r()) + ")";
        return "neg(" + render(((Neg) e).e()) + ")";
    }

    static long eval(Expr e) {
        if (e instanceof Num n) return n.v();
        if (e instanceof Add a) return eval(a.l()) + eval(a.r());
        if (e instanceof Mul m) return eval(m.l()) * eval(m.r());
        return -eval(((Neg) e).e());
    }

    public static void main(String[] args) throws Exception {
        String line = new String(System.in.readAllBytes()).trim();
        toks = line.split("\s+");
        Thread t = new Thread(null, () -> {
            Expr e = parse();
            System.out.println(render(e));
            System.out.println(eval(e));
        }, "main2", 512L * 1024 * 1024);
        t.start();
        t.join();
    }
}
