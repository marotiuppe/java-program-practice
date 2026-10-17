import java.util.*;

enum Op {
    ADD("+") {
        int apply(int a, int b) {
            return a + b;
        }
    },
    SUB("-") {
        int apply(int a, int b) {
            return a - b;
        }
    },
    MUL("*") {
        int apply(int a, int b) {
            return a * b;
        }
    },
    DIV("/") {
        int apply(int a, int b) {
            return a / b;
        }

        boolean divides() {
            return true;
        }
    },
    MOD("%") {
        int apply(int a, int b) {
            return a % b;
        }

        boolean divides() {
            return true;
        }
    };

    private final String symbol;
    private static final Map<String, Op> BY_SYMBOL = new HashMap<>();

    static {
        for (Op o : values()) BY_SYMBOL.put(o.symbol, o);
    }

    Op(String symbol) {
        this.symbol = symbol;
    }

    abstract int apply(int a, int b);

    boolean divides() {
        return false;
    }

    static Op bySymbol(String s) {
        return BY_SYMBOL.get(s);
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        for (int i = 0; i < n; i++) {
            int a = sc.nextInt();
            String sym = sc.next();
            int b = sc.nextInt();
            Op op = Op.bySymbol(sym);
            if (op == null) System.out.println("Unknown operator");
            else if (op.divides() && b == 0) System.out.println("Division by zero");
            else System.out.println(op.apply(a, b));
        }
    }
}
