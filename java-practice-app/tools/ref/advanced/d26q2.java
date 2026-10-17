import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int cap = Integer.parseInt(st.nextToken());
        int q = Integer.parseInt(st.nextToken());
        int[] buf = new int[cap];
        int head = 0, size = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < q; i++) {
            String[] p = br.readLine().trim().split("\s+");
            switch (p[0]) {
                case "WRITE": {
                    int x = Integer.parseInt(p[1]);
                    if (size == cap) {
                        buf[head] = x;
                        head = (head + 1) % cap;
                    } else {
                        buf[(head + size) % cap] = x;
                        size++;
                    }
                    break;
                }
                case "READ":
                    if (size == 0) {
                        sb.append("EMPTY\n");
                    } else {
                        sb.append(buf[head]).append('\n');
                        head = (head + 1) % cap;
                        size--;
                    }
                    break;
                default:
                    if (size == 0) {
                        sb.append("EMPTY\n");
                    } else {
                        for (int j = 0; j < size; j++) {
                            if (j > 0) sb.append(' ');
                            sb.append(buf[(head + j) % cap]);
                        }
                        sb.append('\n');
                    }
            }
        }
        System.out.print(sb);
    }
}
