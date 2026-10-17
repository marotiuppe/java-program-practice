import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Main {
    static final Pattern P1 = Pattern.compile("([0-9]{2}):([0-9]{2}) (\\w+) (DEPOSIT|WITHDRAW) ([1-9][0-9]*)");
    static final Pattern P2 = Pattern.compile("([0-9]{2}):([0-9]{2}) (\\w+) TRANSFER (\\w+) ([1-9][0-9]*)");

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine().trim());
        Map<String, Long> bal = new HashMap<>();
        Map<String, Integer> hours = new TreeMap<>();
        StringBuilder sb = new StringBuilder();
        int malformed = 0;
        for (int ln = 1; ln <= n; ln++) {
            String line = br.readLine();
            if (line == null) line = "";
            Matcher m1 = P1.matcher(line), m2 = P2.matcher(line);
            String hh;
            boolean ok = true;
            String reason = null;
            if (m1.matches()) {
                hh = m1.group(1);
                String u = m1.group(3);
                long amt = Long.parseLong(m1.group(5));
                bal.putIfAbsent(u, 0L);
                if (m1.group(4).equals("DEPOSIT")) {
                    bal.merge(u, amt, Long::sum);
                } else if (amt > bal.get(u)) {
                    ok = false;
                    reason = "INSUFFICIENT";
                } else {
                    bal.merge(u, -amt, Long::sum);
                }
            } else if (m2.matches()) {
                hh = m2.group(1);
                String u = m2.group(3), t = m2.group(4);
                long amt = Long.parseLong(m2.group(5));
                bal.putIfAbsent(u, 0L);
                bal.putIfAbsent(t, 0L);
                if (u.equals(t)) {
                    ok = false;
                    reason = "SELF";
                } else if (amt > bal.get(u)) {
                    ok = false;
                    reason = "INSUFFICIENT";
                } else {
                    bal.merge(u, -amt, Long::sum);
                    bal.merge(t, amt, Long::sum);
                }
            } else {
                malformed++;
                continue;
            }
            if (ok) hours.merge(hh, 1, Integer::sum);
            else sb.append("FAILED ").append(ln).append(" ").append(reason).append("\n");
        }
        sb.append("Malformed: ").append(malformed).append("\n");
        sb.append("Balances:\n");
        List<Map.Entry<String, Long>> es = new ArrayList<>(bal.entrySet());
        es.sort((a, b) -> {
            int c = Long.compare(b.getValue(), a.getValue());
            return c != 0 ? c : a.getKey().compareTo(b.getKey());
        });
        for (Map.Entry<String, Long> e : es) sb.append(e.getKey()).append(" ").append(e.getValue()).append("\n");
        String bestH = null;
        int bestC = 0;
        for (Map.Entry<String, Integer> e : hours.entrySet()) {
            if (e.getValue() > bestC) {
                bestC = e.getValue();
                bestH = e.getKey();
            }
        }
        sb.append("Busiest hour: ").append(bestH == null ? "none" : bestH + " (" + bestC + ")").append("\n");
        System.out.print(sb);
    }
}
