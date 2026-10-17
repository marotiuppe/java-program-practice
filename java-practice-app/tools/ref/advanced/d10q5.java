public class Main {
    static class Node {
        int val;
        Node left;
        Node right;

        Node(int val) {
            this.val = val;
        }
    }

    static Node insert(Node root, int v) {
        if (root == null) {
            return new Node(v);
        }
        if (v < root.val) {
            root.left = insert(root.left, v);
        } else {
            root.right = insert(root.right, v);
        }
        return root;
    }

    static Node delete(Node root, int key) {
        if (root == null) {
            return null;
        }
        if (key < root.val) {
            root.left = delete(root.left, key);
        } else if (key > root.val) {
            root.right = delete(root.right, key);
        } else {
            if (root.left == null) {
                return root.right;
            }
            if (root.right == null) {
                return root.left;
            }
            Node succ = root.right;
            while (succ.left != null) {
                succ = succ.left;
            }
            root.val = succ.val;
            root.right = delete(root.right, succ.val);
        }
        return root;
    }

    static void inorder(Node n, StringBuilder sb) {
        if (n == null) {
            return;
        }
        inorder(n.left, sb);
        sb.append(n.val).append(' ');
        inorder(n.right, sb);
    }

    public static void main(String[] args) {
        Node root = null;
        for (int v : new int[]{50, 30, 70, 20, 40, 60, 80}) {
            root = insert(root, v);
        }
        root = delete(root, 20);
        root = delete(root, 30);
        root = delete(root, 50);
        StringBuilder sb = new StringBuilder();
        inorder(root, sb);
        System.out.println(sb.toString().trim());
    }
}
