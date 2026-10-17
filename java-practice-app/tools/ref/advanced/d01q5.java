public class Main {
    static class Node {
        int val;
        Node next;

        Node(int val, Node next) {
            this.val = val;
            this.next = next;
        }
    }

    static Node deleteDuplicates(Node head) {
        Node dummy = new Node(0, head);
        Node prev = dummy;
        Node cur = head;
        while (cur != null) {
            if (cur.next != null && cur.val == cur.next.val) {
                int dup = cur.val;
                while (cur != null && cur.val == dup) {
                    cur = cur.next;
                }
                prev.next = cur;
            } else {
                prev = cur;
                cur = cur.next;
            }
        }
        return dummy.next;
    }

    public static void main(String[] args) {
        Node list = new Node(1, new Node(2, new Node(2, new Node(3, new Node(3, new Node(4, null))))));
        Node r = deleteDuplicates(list);
        StringBuilder sb = new StringBuilder();
        for (; r != null; r = r.next) {
            sb.append(r.val).append(' ');
        }
        System.out.println(sb.toString().trim());
    }
}
