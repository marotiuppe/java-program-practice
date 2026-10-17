import java.util.*;

public class Main {
    static class Node {
        int d;
        Node next;

        Node(int d, Node next) {
            this.d = d;
            this.next = next;
        }
    }

    static Node read(StringTokenizer st) {
        int n = Integer.parseInt(st.nextToken());
        Node dummy = new Node(0, null);
        Node t = dummy;
        for (int i = 0; i < n; i++) {
            t.next = new Node(Integer.parseInt(st.nextToken()), null);
            t = t.next;
        }
        return dummy.next;
    }

    static Deque<Integer> toStack(Node h) {
        Deque<Integer> s = new ArrayDeque<>();
        for (; h != null; h = h.next) {
            s.push(h.d);
        }
        return s;
    }

    public static void main(String[] args) throws Exception {
        StringTokenizer st = new StringTokenizer(new String(System.in.readAllBytes()));
        Deque<Integer> a = toStack(read(st));
        Deque<Integer> b = toStack(read(st));
        Node res = null;
        int carry = 0;
        while (!a.isEmpty() || !b.isEmpty() || carry > 0) {
            int s = carry;
            if (!a.isEmpty()) {
                s += a.pop();
            }
            if (!b.isEmpty()) {
                s += b.pop();
            }
            res = new Node(s % 10, res);
            carry = s / 10;
        }
        StringBuilder sb = new StringBuilder();
        for (; res != null; res = res.next) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(res.d);
        }
        System.out.println(sb);
    }
}
