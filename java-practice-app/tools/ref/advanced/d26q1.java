import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        StreamTokenizer st = new StreamTokenizer(new BufferedInputStream(System.in));
        st.nextToken();
        int k = (int) st.nval;
        st.nextToken();
        int n = (int) st.nval;
        int[] ring = new int[k];
        int head = 0, size = 0;
        HashSet<Integer> set = new HashSet<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            st.nextToken();
            int id = (int) st.nval;
            if (i > 0) sb.append(' ');
            if (set.contains(id)) {
                sb.append('D');
            } else {
                sb.append('N');
                if (size == k) {
                    set.remove(ring[head]);
                    ring[head] = id;
                    head = (head + 1) % k;
                } else {
                    ring[(head + size) % k] = id;
                    size++;
                }
                set.add(id);
            }
        }
        System.out.println(sb);
    }
}
