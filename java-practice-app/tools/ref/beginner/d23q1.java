import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Set<String> seen = new HashSet<>();
        int ret = 0;
        for (int i = 0; i < n; i++) if (!seen.add(sc.next())) ret++;
        System.out.println("Total visits: " + n);
        System.out.println("Unique visitors: " + seen.size());
        System.out.println("Returning visits: " + ret);
    }
}