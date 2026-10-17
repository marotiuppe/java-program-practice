import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(in.readLine());
        int n = Integer.parseInt(st.nextToken()), k = Integer.parseInt(st.nextToken());
        try (BufferedWriter w = Files.newBufferedWriter(Paths.get("input.txt"))) {
            for (int i = 0; i < n; i++) {
                String line = in.readLine();
                w.write(line == null ? "" : line);
                w.newLine();
            }
        }
        ArrayDeque<String> dq = new ArrayDeque<>();
        try (BufferedReader r = Files.newBufferedReader(Paths.get("input.txt"))) {
            String line;
            while ((line = r.readLine()) != null) {
                dq.addLast(line);
                if (dq.size() > k) dq.removeFirst();
            }
        }
        StringBuilder sb = new StringBuilder();
        for (String s : dq) sb.append(s).append("\n");
        System.out.print(sb);
    }
}
