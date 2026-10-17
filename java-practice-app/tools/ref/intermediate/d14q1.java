import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Map<String, Long> spend = new HashMap<>();
        Map<String, Long> qtyByItem = new HashMap<>();
        for (int i = 0; i < n; i++) {
            sc.nextInt();
            String cust = sc.next();
            String item = sc.next();
            int qty = sc.nextInt();
            int price = sc.nextInt();
            spend.merge(cust, (long) qty * price, Long::sum);
            qtyByItem.merge(item, (long) qty, Long::sum);
        }
        List<Map.Entry<String, Long>> es = new ArrayList<>(spend.entrySet());
        es.sort((a, b) -> {
            int c = Long.compare(b.getValue(), a.getValue());
            return c != 0 ? c : a.getKey().compareTo(b.getKey());
        });
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Long> e : es) sb.append(e.getKey()).append(' ').append(e.getValue()).append('\n');
        String best = null;
        long bq = -1;
        for (Map.Entry<String, Long> e : qtyByItem.entrySet()) {
            if (e.getValue() > bq || (e.getValue() == bq && e.getKey().compareTo(best) < 0)) {
                bq = e.getValue();
                best = e.getKey();
            }
        }
        sb.append("Best seller: ").append(best).append(" (").append(bq).append(")\n");
        System.out.print(sb);
    }
}
