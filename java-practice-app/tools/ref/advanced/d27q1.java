import java.io.*;
import java.util.*;

public class Main {
    interface Coupon {
        long apply(long current, long totalQty);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine().trim());
        long subtotal = 0, qty = 0;
        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            long price = Long.parseLong(st.nextToken());
            long q = Long.parseLong(st.nextToken());
            subtotal += price * q;
            qty += q;
        }
        String ml = br.readLine();
        int m = ml == null ? 0 : Integer.parseInt(ml.trim());
        long total = subtotal;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < m; i++) {
            String line = br.readLine().trim().replaceAll("\s+", " ");
            String[] p = line.split(" ");
            Coupon c;
            switch (p[0]) {
                case "PERCENT": {
                    long pc = Long.parseLong(p[1]);
                    c = (cur, tq) -> cur - cur * pc / 100;
                    break;
                }
                case "FLAT": {
                    long a = Long.parseLong(p[1]);
                    c = (cur, tq) -> Math.max(0, cur - a);
                    break;
                }
                default: {
                    long k = Long.parseLong(p[1]);
                    long pc = Long.parseLong(p[2]);
                    c = (cur, tq) -> tq >= k ? cur - cur * pc / 100 : cur;
                }
            }
            total = c.apply(total, qty);
            sb.append("After ").append(line).append(": ").append(total).append('\n');
        }
        sb.append("Total: ").append(total);
        System.out.println(sb);
    }
}
