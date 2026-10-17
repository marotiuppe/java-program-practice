import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        long k = sc.nextLong();
        List<Integer> circle = new ArrayList<>();
        for (int i = 1; i <= n; i++) circle.add(i);
        StringBuilder sb = new StringBuilder("Order:");
        int idx = 0;
        while (circle.size() > 1) {
            idx = (int) ((idx + k - 1) % circle.size());
            sb.append(' ').append(circle.remove(idx));
            if (idx == circle.size()) idx = 0;
        }
        System.out.println(sb);
        System.out.println("Survivor: " + circle.get(0));
    }
}
