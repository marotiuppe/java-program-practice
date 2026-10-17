public class Main {
    static class Node {
        int val;
        Node next;

        Node(int val, Node next) {
            this.val = val;
            this.next = next;
        }
    }

    static Node reverseKGroup(Node head, int k) {
        Node dummy = new Node(0, head);
        Node groupPrev = dummy;
        while (true) {
            Node kth = groupPrev;
            for (int i = 0; i < k && kth != null; i++) {
                kth = kth.next;
            }
            if (kth == null) {
                break;
            }
            Node groupNext = kth.next;
            Node prev = groupNext;
            Node cur = groupPrev.next;
            while (cur != groupNext) {
                Node nx = cur.next;
                cur.next = prev;
                prev = cur;
                cur = nx;
            }
            Node tmp = groupPrev.next;
            groupPrev.next = kth;
            groupPrev = tmp;
        }
        return dummy.next;
    }

    public static void main(String[] args) {
        Node list = null;
        for (int v = 5; v >= 1; v--) {
            list = new Node(v, list);
        }
        Node r = reverseKGroup(list, 2);
        StringBuilder sb = new StringBuilder();
        for (; r != null; r = r.next) {
            sb.append(r.val).append(' ');
        }
        System.out.println(sb.toString().trim());
    }
}
