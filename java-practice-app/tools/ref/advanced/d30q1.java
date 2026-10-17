import java.io.*;
import java.util.*;

public class Main {
    static class Order {
        String id, sku;
        long qty, pri;
        int seq;
        boolean pending;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int q = Integer.parseInt(br.readLine().trim());
        Map<String, Long> stock = new HashMap<>();
        Map<String, TreeSet<Order>> pending = new HashMap<>();
        TreeSet<String> skus = new TreeSet<>();
        Map<String, Order> orders = new HashMap<>();
        Comparator<Order> cmp = (a, b) -> a.pri != b.pri ? Long.compare(b.pri, a.pri) : Integer.compare(a.seq, b.seq);
        int seq = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < q; i++) {
            String[] p = br.readLine().trim().split("\\s+");
            switch (p[0]) {
                case "STOCK": {
                    String sku = p[1];
                    long qty = Long.parseLong(p[2]);
                    skus.add(sku);
                    long cur = stock.getOrDefault(sku, 0L) + qty;
                    TreeSet<Order> set = pending.get(sku);
                    if (set != null) {
                        Iterator<Order> it = set.iterator();
                        while (it.hasNext()) {
                            Order o = it.next();
                            if (o.qty <= cur) {
                                cur -= o.qty;
                                o.pending = false;
                                it.remove();
                                sb.append("FILLED ").append(o.id).append('\n');
                            }
                        }
                    }
                    stock.put(sku, cur);
                    break;
                }
                case "ORDER": {
                    Order o = new Order();
                    o.id = p[1];
                    o.sku = p[2];
                    o.qty = Long.parseLong(p[3]);
                    o.pri = Long.parseLong(p[4]);
                    o.seq = seq++;
                    skus.add(o.sku);
                    long cur = stock.getOrDefault(o.sku, 0L);
                    if (cur >= o.qty) {
                        stock.put(o.sku, cur - o.qty);
                        sb.append("FILLED ").append(o.id).append('\n');
                    } else {
                        o.pending = true;
                        orders.put(o.id, o);
                        pending.computeIfAbsent(o.sku, k -> new TreeSet<>(cmp)).add(o);
                        sb.append("BACKORDER ").append(o.id).append('\n');
                    }
                    break;
                }
                case "CANCEL": {
                    Order o = orders.get(p[1]);
                    if (o != null && o.pending) {
                        o.pending = false;
                        pending.get(o.sku).remove(o);
                        sb.append("CANCELLED ").append(o.id).append('\n');
                    } else {
                        sb.append("NOTFOUND ").append(p[1]).append('\n');
                    }
                    break;
                }
                default:
                    for (String sku : skus) {
                        TreeSet<Order> set = pending.get(sku);
                        sb.append(sku).append(" stock=").append(stock.getOrDefault(sku, 0L))
                          .append(" pending=").append(set == null ? 0 : set.size()).append('\n');
                    }
            }
        }
        System.out.print(sb);
    }
}
