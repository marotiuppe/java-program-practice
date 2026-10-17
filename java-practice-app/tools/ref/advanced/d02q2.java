import java.util.*;

public class Main {
    static class Node {
        int v;
        Node next;

        Node(int v) {
            this.v = v;
        }
    }

    static Node sort(Node head) {
        if (head == null || head.next == null) {
            return head;
        }
        Node slow = head;
        Node fast = head.next;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        Node second = slow.next;
        slow.next = null;
        Node a = sort(head);
        Node b = sort(second);
        Node dummy = new Node(0);
        Node t = dummy;
        while (a != null && b != null) {
            if (Integer.compare(a.v, b.v) <= 0) {
                t.next = a;
                a = a.next;
            } else {
                t.next = b;
                b = b.next;
            }
            t = t.next;
        }
        t.next = a != null ? a : b;
        return dummy.next;
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
        StringBuilder sb = new StringBuilder();
        for (Node p = sort(dummy.next); p != null; p = p.next) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(p.v);
        }
        System.out.println(sb);
    }
}
