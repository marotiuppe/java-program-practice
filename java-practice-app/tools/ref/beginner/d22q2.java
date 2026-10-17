import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        LinkedHashSet<Integer> set = new LinkedHashSet<>();
        for (int i = 0; i < n; i++) set.add(sc.nextInt());
        StringBuilder sb = new StringBuilder();
        for (int v : set) { if (sb.length() > 0) sb.append(' '); sb.append(v); }
        System.out.println(sb);
    }
}