import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine().trim());
        StringTokenizer st = new StringTokenizer(br.readLine());
        Set<String> set = new HashSet<>();
        List<String> words = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String w = st.nextToken();
            set.add(w);
            words.add(w);
        }
        String best = "";
        for (String w : words) {
            boolean ok = true;
            for (int l = 1; l <= w.length() && ok; l++) {
                ok = set.contains(w.substring(0, l));
            }
            if (!ok) continue;
            if (w.length() > best.length() || (w.length() == best.length() && w.compareTo(best) < 0)) best = w;
        }
        System.out.println(best);
    }
}
