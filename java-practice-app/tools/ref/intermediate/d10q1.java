import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());
        TreeMap<String, Integer> stock = new TreeMap<>();
        for (int i = 0; i < n; i++) {
            String[] p = sc.nextLine().trim().split("\\s+");
            switch (p[0]) {
                case "RECEIVE":
                    stock.merge(p[1], Integer.parseInt(p[2]), Integer::sum);
                    break;
                case "SHIP": {
                    int q = Integer.parseInt(p[2]);
                    int have = stock.getOrDefault(p[1], 0);
                    if (have < q) {
                        System.out.println("REJECTED " + p[1]);
                    } else {
                        if (have == q) stock.remove(p[1]);
                        else stock.put(p[1], have - q);
                        System.out.println("SHIPPED " + p[1] + " " + q);
                    }
                    break;
                }
                default:
                    if (stock.isEmpty()) System.out.println("(empty)");
                    for (Map.Entry<String, Integer> e : stock.entrySet())
                        System.out.println(e.getKey() + ": " + e.getValue());
            }
        }
    }
}
