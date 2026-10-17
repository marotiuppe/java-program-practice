import java.util.*;

public class Main {
    static class MinStack<T extends Comparable<T>> {
        private final Deque<T> data = new ArrayDeque<>();
        private final Deque<T> mins = new ArrayDeque<>();

        void push(T x) {
            data.push(x);
            if (mins.isEmpty() || x.compareTo(mins.peek()) <= 0) mins.push(x);
        }

        T pop() {
            T x = data.pop();
            if (x.compareTo(mins.peek()) == 0) mins.pop();
            return x;
        }

        T top() {
            return data.peek();
        }

        T min() {
            return mins.peek();
        }

        boolean isEmpty() {
            return data.isEmpty();
        }
    }

    static <T extends Comparable<T>> void run(Scanner sc, int n, java.util.function.Function<String, T> parse) {
        MinStack<T> st = new MinStack<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            String cmd = sc.next();
            if (cmd.equals("PUSH")) {
                st.push(parse.apply(sc.next()));
            } else if (st.isEmpty()) {
                sb.append("EMPTY\n");
            } else if (cmd.equals("POP")) {
                sb.append(st.pop()).append('\n');
            } else if (cmd.equals("TOP")) {
                sb.append(st.top()).append('\n');
            } else {
                sb.append(st.min()).append('\n');
            }
        }
        System.out.print(sb);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String type = sc.next();
        int n = sc.nextInt();
        if (type.equals("INT")) run(sc, n, Integer::valueOf);
        else run(sc, n, s -> s);
    }
}
