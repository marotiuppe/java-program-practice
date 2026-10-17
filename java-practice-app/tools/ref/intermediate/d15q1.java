import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine().trim());
        Pattern p = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} (INFO|WARN|ERROR) \\[(\\w+)\\] .*");
        int info = 0, warn = 0, err = 0, bad = 0;
        Map<String, Integer> errMods = new TreeMap<>();
        for (int i = 0; i < n; i++) {
            String line = br.readLine();
            if (line == null) line = "";
            Matcher m = p.matcher(line);
            if (!m.matches()) {
                bad++;
                continue;
            }
            switch (m.group(1)) {
                case "INFO": info++; break;
                case "WARN": warn++; break;
                default:
                    err++;
                    errMods.merge(m.group(2), 1, Integer::sum);
            }
        }
        System.out.println("INFO=" + info + " WARN=" + warn + " ERROR=" + err + " MALFORMED=" + bad);
        String best = null;
        int bc = 0;
        for (Map.Entry<String, Integer> e : errMods.entrySet()) {
            if (e.getValue() > bc) {
                bc = e.getValue();
                best = e.getKey();
            }
        }
        System.out.println(best == null ? "Top error module: none" : "Top error module: " + best + " (" + bc + ")");
    }
}
