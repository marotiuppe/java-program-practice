import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(in.readLine().trim());
        String header = """
                INVOICE
                -------
                """;
        StringBuilder sb = new StringBuilder(header);
        int total = 0;
        for (int i = 0; i < n; i++) {
            String line = in.readLine();
            if (line == null || line.isBlank()) continue;
            String[] p = line.strip().split(",");
            int qty = Integer.parseInt(p[1].strip()), price = Integer.parseInt(p[2].strip());
            int lt = qty * price;
            total += lt;
            sb.append("%-8s x%-2d @ %5d = %6d%n".formatted(p[0].strip(), qty, price, lt));
        }
        sb.append("-------\n");
        sb.append("TOTAL: ").append(total).append("\n");
        System.out.print(sb.toString().replace("\r\n", "\n"));
    }
}
