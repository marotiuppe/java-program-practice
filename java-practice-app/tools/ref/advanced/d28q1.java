import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String spots = br.readLine().trim();
        int q = Integer.parseInt(br.readLine().trim());
        TreeSet<Integer>[] free = new TreeSet[3];
        for (int i = 0; i < 3; i++) free[i] = new TreeSet<>();
        for (int i = 0; i < spots.length(); i++) free[spots.charAt(i) == 'S' ? 0 : spots.charAt(i) == 'M' ? 1 : 2].add(i);
        Map<String, long[]> parked = new HashMap<>();
        long[] rate = {10, 20, 30};
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < q; i++) {
            String[] p = br.readLine().trim().split("\\s+");
            if (p[0].equals("PARK")) {
                long t = Long.parseLong(p[1]);
                String plate = p[2];
                String type = p[3];
                if (parked.containsKey(plate)) {
                    sb.append("ALREADY PARKED\n");
                    continue;
                }
                int minSize = type.equals("BIKE") ? 0 : type.equals("CAR") ? 1 : 2;
                int chosen = -1;
                for (int s = minSize; s < 3; s++) {
                    if (!free[s].isEmpty()) {
                        chosen = s;
                        break;
                    }
                }
                if (chosen < 0) {
                    sb.append("FULL\n");
                } else {
                    int spot = free[chosen].pollFirst();
                    parked.put(plate, new long[]{spot, chosen, t});
                    sb.append("Parked ").append(plate).append(" at ").append(spot).append('\n');
                }
            } else {
                long t = Long.parseLong(p[1]);
                long[] e = parked.remove(p[2]);
                if (e == null) {
                    sb.append("UNKNOWN\n");
                } else {
                    free[(int) e[1]].add((int) e[0]);
                    long minutes = t - e[2];
                    long hours = Math.max(1, (minutes + 59) / 60);
                    sb.append(p[2]).append(" paid ").append(hours * rate[(int) e[1]]).append('\n');
                }
            }
        }
        System.out.print(sb);
    }
}
