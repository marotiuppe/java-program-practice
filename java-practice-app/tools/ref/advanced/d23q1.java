import java.io.*;
import java.util.*;

public class Main {
    static class Stat {
        long revenue;
        long units;
        long records;
        TreeMap<String, Long> byProduct = new TreeMap<>();
    }

    public static void main(String[] args) throws IOException {
        StringTokenizer st = new StringTokenizer(new String(System.in.readAllBytes()));
        int n = Integer.parseInt(st.nextToken());
        TreeMap<String, Stat> regions = new TreeMap<>();
        for (int i = 0; i < n; i++) {
            String region = st.nextToken();
            String product = st.nextToken();
            long units = Long.parseLong(st.nextToken());
            long price = Long.parseLong(st.nextToken());
            Stat s = regions.computeIfAbsent(region, k -> new Stat());
            long rev = units * price;
            s.revenue += rev;
            s.units += units;
            s.records++;
            s.byProduct.merge(product, rev, Long::sum);
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Stat> e : regions.entrySet()) {
            Stat s = e.getValue();
            String top = null;
            long best = Long.MIN_VALUE;
            for (Map.Entry<String, Long> pe : s.byProduct.entrySet()) {
                if (pe.getValue() > best) {
                    best = pe.getValue();
                    top = pe.getKey();
                }
            }
            double avg = (double) s.units / s.records;
            sb.append(e.getKey()).append(": revenue=").append(s.revenue).append(" top=").append(top)
              .append(" avgUnits=").append(String.format(Locale.ROOT, "%.2f", avg)).append('\n');
        }
        System.out.print(sb);
    }
}
