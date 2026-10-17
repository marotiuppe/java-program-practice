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
        int l = Integer.parseInt(st.nextToken());
        int r = Integer.parseInt(st.nextToken());
        Node prev = dummy;
        for (int i = 1; i < l; i++) {
            prev = prev.next;
        }
        Node cur = prev.next;
        for (int i = 0; i < r - l; i++) {
            Node nx = cur.next;
            cur.next = nx.next;
            nx.next = prev.next;
            prev.next = nx;
        }
        StringBuilder sb = new StringBuilder();
        for (Node p = dummy.next; p != null; p = p.next) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(p.v);
        }
        System.out.println(sb);
    }
}
