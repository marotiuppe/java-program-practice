import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(in.readLine().trim());
        try (BufferedWriter w = Files.newBufferedWriter(Paths.get("app.conf"))) {
            for (int i = 0; i < n; i++) {
                String line = in.readLine();
                w.write(line == null ? "" : line);
                w.newLine();
            }
        }
        Map<String, String> conf = new HashMap<>();
        try (BufferedReader r = Files.newBufferedReader(Paths.get("app.conf"))) {
            String line;
            while ((line = r.readLine()) != null) {
                String t = line.trim();
                if (t.isEmpty() || t.startsWith("#")) continue;
                int eq = line.indexOf('=');
                if (eq < 0) continue;
                conf.put(line.substring(0, eq).trim(), line.substring(eq + 1).trim());
            }
        }
        int q = Integer.parseInt(in.readLine().trim());
        for (int i = 0; i < q; i++) {
            String[] p = in.readLine().trim().split("\s+", 2);
            if (conf.containsKey(p[0])) System.out.println(conf.get(p[0]));
            else if (p.length > 1) System.out.println(p[1]);
            else System.out.println("MISSING");
        }
    }
}
