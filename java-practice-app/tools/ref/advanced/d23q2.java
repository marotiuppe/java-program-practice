import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        StringTokenizer st = new StringTokenizer(new String(System.in.readAllBytes()));
        int n = Integer.parseInt(st.nextToken());
        Map<String, List<String>> groups = new HashMap<>();
        for (int i = 0; i < n; i++) {
            String w = st.nextToken();
            char[] c = w.toCharArray();
            Arrays.sort(c);
            groups.computeIfAbsent(new String(c), k -> new ArrayList<>()).add(w);
        }
        List<List<String>> list = new ArrayList<>(groups.values());
        for (List<String> g : list) Collections.sort(g);
        list.sort((a, b) -> a.size() != b.size() ? b.size() - a.size() : a.get(0).compareTo(b.get(0)));
        StringBuilder sb = new StringBuilder();
        for (List<String> g : list) sb.append(String.join(" ", g)).append('\n');
        System.out.print(sb);
    }
}
