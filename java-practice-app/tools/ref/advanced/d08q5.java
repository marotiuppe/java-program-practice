import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Main {
    static class Node {
        int val;
        Node left;
        Node right;

        Node(int val, Node left, Node right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    static List<List<Integer>> zigzag(Node root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        Queue<Node> q = new LinkedList<>();
        q.add(root);
        boolean leftToRight = true;
        while (!q.isEmpty()) {
            LinkedList<Integer> level = new LinkedList<>();
            int size = q.size();
            for (int i = 0; i < size; i++) {
                Node n = q.poll();
                if (leftToRight) {
                    level.addLast(n.val);
                } else {
                    level.addFirst(n.val);
                }
                if (n.left != null) {
                    q.add(n.left);
                }
                if (n.right != null) {
                    q.add(n.right);
                }
            }
            res.add(level);
            leftToRight = !leftToRight;
        }
        return res;
    }

    public static void main(String[] args) {
        Node root = new Node(1,
                new Node(2, new Node(4, null, null), new Node(5, null, null)),
                new Node(3, new Node(6, null, null), new Node(7, null, null)));
        for (List<Integer> level : zigzag(root)) {
            StringBuilder sb = new StringBuilder();
            for (int v : level) {
                sb.append(v).append(' ');
            }
            System.out.println(sb.toString().trim());
        }
    }
}
