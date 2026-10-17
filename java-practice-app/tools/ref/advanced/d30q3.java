import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int q = Integer.parseInt(br.readLine().trim());
        TreeMap<Long, long[]> byStart = new TreeMap<>();
        Map<String, Long> startOf = new HashMap<>();
        Map<Long, String> idAt = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < q; i++) {
            String[] p = br.readLine().trim().split("\\s+");
            switch (p[0]) {
                case "BOOK": {
                    long s = Long.parseLong(p[2]);
                    long e = Long.parseLong(p[3]);
                    Map.Entry<Long, long[]> conflict = null;
                    Map.Entry<Long, long[]> fl = byStart.floorEntry(s);
                    if (fl != null && fl.getValue()[0] > s) {
                        conflict = fl;
                    } else {
                        Map.Entry<Long, long[]> ce = byStart.ceilingEntry(s);
                        if (ce != null && ce.getKey() < e) conflict = ce;
                    }
                    if (conflict != null) {
                        sb.append("CONFLICT ").append(idAt.get(conflict.getKey())).append('\n');
                    } else {
                        byStart.put(s, new long[]{e});
                        startOf.put(p[1], s);
                        idAt.put(s, p[1]);
                        sb.append("BOOKED\n");
                    }
                    break;
                }
                case "CANCEL": {
                    Long s = startOf.remove(p[1]);
                    if (s == null) {
                        sb.append("NOTFOUND\n");
                    } else {
                        byStart.remove(s);
                        idAt.remove(s);
                        sb.append("CANCELLED\n");
                    }
                    break;
                }
                default: {
                    long x = Long.parseLong(p[1]);
                    long d = Long.parseLong(p[2]);
                    while (true) {
                        Map.Entry<Long, long[]> fl = byStart.floorEntry(x);
                        if (fl != null && fl.getValue()[0] > x) {
                            x = fl.getValue()[0];
                            continue;
                        }
                        Map.Entry<Long, long[]> ce = byStart.higherEntry(x);
                        if (ce != null && ce.getKey() < x + d) {
                            x = ce.getValue()[0];
                            continue;
                        }
                        break;
                    }
                    sb.append(x).append('\n');
                }
            }
        }
        System.out.print(sb);
    }
}
