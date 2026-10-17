import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int cap = Integer.parseInt(in.readLine().trim());
        int n = Integer.parseInt(in.readLine().trim());
        int[] buf = new int[cap];
        int head = 0, size = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            String[] p = in.readLine().trim().split("\s+");
            switch (p[0]) {
                case "ENQ" -> {
                    if (size == cap) sb.append("FULL\n");
                    else { buf[(head + size) % cap] = Integer.parseInt(p[1]); size++; sb.append("OK\n"); }
                }
                case "DEQ" -> {
                    if (size == 0) sb.append("EMPTY\n");
                    else { sb.append(buf[head]).append("\n"); head = (head + 1) % cap; size--; }
                }
                case "PEEK" -> sb.append(size == 0 ? "EMPTY" : String.valueOf(buf[head])).append("\n");
                default -> sb.append(size).append("\n");
            }
        }
        System.out.print(sb);
    }
}
