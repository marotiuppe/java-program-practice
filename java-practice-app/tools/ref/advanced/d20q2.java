import java.util.*;
import java.io.*;

public class Main {
    static long[] readArr(BufferedReader br, int cnt) throws IOException {
        long[] r = new long[cnt];
        if (cnt == 0) {
            br.mark(1 << 20);
            String l = br.readLine(); // the (possibly empty) line for this array
            return r;
        }
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < cnt; i++) r[i] = Long.parseLong(st.nextToken());
        return r;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int m = Integer.parseInt(st.nextToken());
        int n = Integer.parseInt(st.nextToken());
        long[] a = readArr(br, m);
        long[] b = readArr(br, n);
        long[] all = new long[m + n];
        System.arraycopy(a, 0, all, 0, m);
        System.arraycopy(b, 0, all, m, n);
        Arrays.sort(all);
        int t = m + n;
        double med = (t % 2 == 1) ? all[t / 2] : (all[t / 2 - 1] + all[t / 2]) / 2.0;
        System.out.println(String.format(Locale.US, "%.1f", med));
    }
}
