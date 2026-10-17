import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());
        LinkedList<String> q = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            String[] p = sc.nextLine().trim().split("\\s+");
            switch (p[0]) {
                case "ADD": q.addLast(p[1]); break;
                case "PLAYNEXT": q.addFirst(p[1]); break;
                case "PLAY":
                    if (q.isEmpty()) System.out.println("Nothing to play");
                    else System.out.println("Playing " + q.removeFirst());
                    break;
                case "REMOVE": {
                    int idx = Integer.parseInt(p[1]);
                    if (idx < 0 || idx >= q.size()) System.out.println("Invalid index");
                    else System.out.println("Removed " + q.remove(idx));
                    break;
                }
                default:
                    System.out.println(q.isEmpty() ? "Queue: empty" : "Queue: " + String.join(", ", q));
            }
        }
    }
}
