import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine().trim());
        StringBuilder out = new StringBuilder();
        for (int t = 0; t < n; t++) {
            String s = br.readLine();
            if (s == null) s = "";
            s = s.trim();
            StringBuilder sb = new StringBuilder();
            int i = 0;
            while (i < s.length()) {
                int j = i;
                while (j < s.length() && s.charAt(j) == s.charAt(i)) j++;
                sb.append(s.charAt(i)).append(j - i);
                i = j;
            }
            out.append(sb.length() < s.length() ? sb.toString() : s).append('\n');
        }
        System.out.print(out);
    }
}
