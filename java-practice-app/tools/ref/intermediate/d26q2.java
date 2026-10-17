import java.util.*;

public class Main {
    static class Node {
        int val;
        Node next;
        Node(int v) { val = v; }
    }

    static Node middle(Node head) {
        Node slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    static String removeKthFromEnd(Node head, int k) {
        Node dummy = new Node(0);
        dummy.next = head;
        Node lead = dummy, lag = dummy;
        for (int i = 0; i < k; i++) lead = lead.next;
        // lag stops just before the node to remove
        while (lead.next != null) {
            lead = lead.next;
            lag = lag.next;
        }
        StringBuilder sb = new StringBuilder();
        Node skip = lag.next;
        for (Node c = head; c != null; c = c.next) {
            if (c == skip) continue;
            if (sb.length() > 0) sb.append(' ');
            sb.append(c.val);
        }
        return sb.length() == 0 ? "(empty)" : sb.toString();
    }

    static Node reverse(Node head) {
        Node prev = null;
        while (head != null) {
            Node nx = head.next;
            head.next = prev;
            prev = head;
            head = nx;
        }
        return prev;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Node head = null, tail = null;
        for (int i = 0; i < n; i++) {
            Node nd = new Node(sc.nextInt());
            if (head == null) head = tail = nd;
            else { tail.next = nd; tail = nd; }
        }
        int k = sc.nextInt();
        System.out.println("Middle: " + middle(head).val);
        System.out.println("After removal: " + removeKthFromEnd(head, k));
        head = reverse(head);
        StringBuilder sb = new StringBuilder();
        for (Node c = head; c != null; c = c.next) {
            if (sb.length() > 0) sb.append(' ');
            sb.append(c.val);
        }
        System.out.println("Reversed: " + sb);
    }
}
