public class Main {
    static class Node {
        Integer val;
        Node left;
        Node right;

        Node(Integer val, Node left, Node right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    static Node lca(Node root, Integer p, Integer q) {
        if (root == null) {
            return null;
        }
        if (root.val.equals(p) || root.val.equals(q)) {
            return root;
        }
        Node l = lca(root.left, p, q);
        Node r = lca(root.right, p, q);
        if (l != null && r != null) {
            return root;
        }
        return l != null ? l : r;
    }

    public static void main(String[] args) {
        Node root = new Node(300,
                new Node(200, new Node(100, null, null), new Node(250, null, null)),
                new Node(400, null, null));
        System.out.println(lca(root, 100, 250).val);
        System.out.println(lca(root, 100, 400).val);
        System.out.println(lca(root, 200, 250).val);
    }
}
