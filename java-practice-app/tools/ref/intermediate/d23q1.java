import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(in.readLine().trim());
        try (BufferedWriter w = Files.newBufferedWriter(Paths.get("expenses.csv"))) {
            w.write("date,category,amount");
            w.newLine();
            for (int i = 0; i < n; i++) {
                String line = in.readLine();
                if (line == null) line = "";
                w.write(line);
                w.newLine();
            }
        }
        TreeMap<String, Long> totals = new TreeMap<>();
        int skipped = 0, lines = 0;
        try (BufferedReader r = Files.newBufferedReader(Paths.get("expenses.csv"))) {
            String line = r.readLine();
            lines++;
            while ((line = r.readLine()) != null) {
                lines++;
                String[] f = line.split(",", -1);
                boolean ok = f.length == 3;
                long amt = 0;
                if (ok) {
                    try {
                        amt = Integer.parseInt(f[2]);
                    } catch (NumberFormatException e) {
                        ok = false;
                    }
                }
                if (!ok) {
                    skipped++;
                    continue;
                }
                totals.merge(f[1], amt, Long::sum);
            }
        }
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(Paths.get("summary.txt")))) {
            for (Map.Entry<String, Long> e : totals.entrySet()) pw.println(e.getKey() + ": " + e.getValue());
        }
        try (BufferedReader r = Files.newBufferedReader(Paths.get("summary.txt"))) {
            String line;
            while ((line = r.readLine()) != null) System.out.println(line);
        }
        System.out.println("Skipped: " + skipped);
        System.out.println("File lines: " + lines);
    }
}
