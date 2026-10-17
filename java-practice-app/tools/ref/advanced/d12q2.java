import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        String begin = st.nextToken();
        String end = st.nextToken();
        int n = Integer.parseInt(br.readLine().trim());
        Set<String> dict = new HashSet<>();
        String line = br.readLine();
        if (line != null) {
            st = new StringTokenizer(line);
            while (st.hasMoreTokens()) dict.add(st.nextToken());
        }
        if (!dict.contains(end)) {
            System.out.println(0);
            return;
        }
        Map<String, Integer> dist = new HashMap<>();
        ArrayDeque<String> q = new ArrayDeque<>();
        dist.put(begin, 1);
        q.add(begin);
        while (!q.isEmpty()) {
            String w = q.poll();
            int d = dist.get(w);
            if (w.equals(end)) {
                System.out.println(d);
                return;
            }
            char[] cs = w.toCharArray();
            for (int i = 0; i < cs.length; i++) {
                char o = cs[i];
                for (char c = 'a'; c <= 'z'; c++) {
                    if (c == o) continue;
                    cs[i] = c;
                    String s = new String(cs);
                    if (dict.contains(s) && !dist.containsKey(s)) {
                        dist.put(s, d + 1);
                        q.add(s);
                    }
                }
                cs[i] = o;
            }
        }
        System.out.println(0);
    }
}
