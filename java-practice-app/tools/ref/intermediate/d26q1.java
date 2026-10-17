import java.io.*;
import java.util.*;

public class Main {
    static class Node {
        String v;
        Node next;
        Node(String v, Node n) { this.v = v; next = n; }
    }

    static class Stack {
        Node head;
        void push(String v) { head = new Node(v, head); }
        String pop() { String v = head.v; head = head.next; return v; }
        boolean isEmpty() { return head == null; }
        void clear() { head = null; }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(in.readLine().trim());
        Stack back = new Stack(), fwd = new Stack();
        String cur = "home";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            String[] p = in.readLine().trim().split("\s+");
            switch (p[0]) {
                case "VISIT" -> { back.push(cur); fwd.clear(); cur = p[1]; }
                case "BACK" -> {
                    if (back.isEmpty()) sb.append("Cannot go back\n");
                    else { fwd.push(cur); cur = back.pop(); sb.append("Current: ").append(cur).append("\n"); }
                }
                case "FORWARD" -> {
                    if (fwd.isEmpty()) sb.append("Cannot go forward\n");
                    else { back.push(cur); cur = fwd.pop(); sb.append("Current: ").append(cur).append("\n"); }
                }
                default -> sb.append("Current: ").append(cur).append("\n");
            }
        }
        System.out.print(sb);
    }
}
