import java.util.*;

public class Main {
    static class Node {
        int id;
        Node next;

        Node(int id) {
            this.id = id;
        }
    }

    public static void main(String[] args) throws Exception {
        StringTokenizer st = new StringTokenizer(new String(System.in.readAllBytes()));
        int n = Integer.parseInt(st.nextToken());
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(Integer.parseInt(st.nextToken()));
            if (i > 0) {
                nodes[i - 1].next = nodes[i];
            }
        }
        int pos = Integer.parseInt(st.nextToken());
        if (pos >= 0) {
            nodes[n - 1].next = nodes[pos];
        }
        Node head = nodes[0];
        Node slow = head;
        Node fast = head;
        boolean cycle = false;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                cycle = true;
                break;
            }
        }
        if (!cycle) {
            System.out.println("No cycle");
            return;
        }
        Node a = head;
        while (a != slow) {
            a = a.next;
            slow = slow.next;
        }
        int len = 1;
        for (Node p = a.next; p != a; p = p.next) {
            len++;
        }
        System.out.println("Cycle starts at stop " + a.id + ", length " + len);
    }
}
