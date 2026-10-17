import java.util.*;

public class Main {
    static class Node {
        int key;
        int val;
        Node prev;
        Node next;
    }

    static Node head = new Node();
    static Node tail = new Node();

    static void unlink(Node n) {
        n.prev.next = n.next;
        n.next.prev = n.prev;
    }

    static void addFront(Node n) {
        n.next = head.next;
        n.prev = head;
        head.next.prev = n;
        head.next = n;
    }

    public static void main(String[] args) throws Exception {
        StringTokenizer st = new StringTokenizer(new String(System.in.readAllBytes()));
        int cap = Integer.parseInt(st.nextToken());
        int q = Integer.parseInt(st.nextToken());
        head.next = tail;
        tail.prev = head;
        Map<Integer, Node> map = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < q; i++) {
            String cmd = st.nextToken();
            int key = Integer.parseInt(st.nextToken());
            if (cmd.equals("GET")) {
                Node n = map.get(key);
                if (n == null) {
                    sb.append(-1).append('\n');
                } else {
                    unlink(n);
                    addFront(n);
                    sb.append(n.val).append('\n');
                }
            } else {
                int val = Integer.parseInt(st.nextToken());
                Node n = map.get(key);
                if (n != null) {
                    n.val = val;
                    unlink(n);
                    addFront(n);
                } else {
                    if (map.size() == cap) {
                        Node last = tail.prev;
                        unlink(last);
                        map.remove(last.key);
                    }
                    n = new Node();
                    n.key = key;
                    n.val = val;
                    map.put(key, n);
                    addFront(n);
                }
            }
        }
        System.out.print(sb);
    }
}
