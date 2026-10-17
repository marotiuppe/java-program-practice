import java.util.ArrayDeque;
import java.util.Deque;

public class Main {
    static class MyQueue {
        private final Deque<Integer> in = new ArrayDeque<>();
        private final Deque<Integer> out = new ArrayDeque<>();

        void push(int x) {
            in.push(x);
        }

        private void shift() {
            if (out.isEmpty()) {
                while (!in.isEmpty()) {
                    out.push(in.pop());
                }
            }
        }

        int pop() {
            shift();
            return out.pop();
        }

        int peek() {
            shift();
            return out.peek();
        }

        boolean empty() {
            return in.isEmpty() && out.isEmpty();
        }
    }

    public static void main(String[] args) {
        MyQueue q = new MyQueue();
        q.push(1);
        q.push(2);
        System.out.println(q.peek());
        System.out.println(q.pop());
        q.push(3);
        System.out.println(q.pop());
        System.out.println(q.pop());
        System.out.println(q.empty());
    }
}
