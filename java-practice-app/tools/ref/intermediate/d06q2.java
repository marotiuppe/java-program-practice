import java.io.*;
import java.util.*;

final class Point {
    private final int x, y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        Point p = (Point) o;
        return x == p.x && y == p.y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = br.readLine();
        if (s == null) s = "";
        s = s.trim();
        Map<Point, Integer> visits = new HashMap<>();
        int x = 0, y = 0;
        visits.merge(new Point(x, y), 1, Integer::sum);
        for (char c : s.toCharArray()) {
            if (c == 'U') y++;
            else if (c == 'D') y--;
            else if (c == 'L') x--;
            else if (c == 'R') x++;
            visits.merge(new Point(x, y), 1, Integer::sum);
        }
        int rev = 0, max = 0;
        for (int v : visits.values()) {
            if (v >= 2) rev++;
            max = Math.max(max, v);
        }
        System.out.println("Distinct: " + visits.size());
        System.out.println("Revisited: " + rev);
        System.out.println("Max visits: " + max);
    }
}
