import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine().trim());
        StringBuilder out = new StringBuilder();
        for (int t = 0; t < n; t++) {
            String s = br.readLine();
            if (s == null) s = "";
            s = s.replaceAll("([A-Za-z0-9._])[A-Za-z0-9._]*(@[A-Za-z0-9.]+\\.[a-z]{2,})", "$1***$2");
            s = s.replaceAll("(?<!\\d)\\d{6}(\\d{4})(?!\\d)", "XXXXXX$1");
            out.append(s).append('\n');
        }
        System.out.print(out);
    }
}
