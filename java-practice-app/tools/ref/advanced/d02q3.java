import java.util.*;

public class Main {
    static class Node {
        int v;
        Node next;

        Node(int v) {
            this.v = v;
        }
    }

    public static void main(String[] args) throws Exception {
        StringTokenizer st = new StringTokenizer(new String(System.in.readAllBytes()));
        int n = Integer.parseInt(st.nextToken());
        Node dummy = new Node(0);
        Node t = dummy;
        for (int i = 0; i < n; i++) {
            t.next = new Node(Integer.parseInt(st.nextToken()));
            t = t.next;
        }
        Node head = dummy.next;
        if (head.next != null) {
            Node slow = head;
            Node fast = head;
            while (fast.next != null && fast.next.next != null) {
                slow = slow.next;
                fast = fast.next.next;
            }
            Node second = slow.next;
            slow.next = null;
            Node prev = null;
            while (second != null) {
                Node nx = second.next;
                second.next = prev;
                prev = second;
                second = nx;
            }
            Node a = head;
            Node b = prev;
            while (b != null) {
                Node an = a.next;
                Node bn = b.next;
                a.next = b;
                b.next = an;
                a = an;
                b = bn;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (Node p = head; p != null; p = p.next) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(p.v);
        }
        System.out.println(sb);
    }
}
