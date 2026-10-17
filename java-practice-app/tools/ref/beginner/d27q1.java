import java.util.*;

// Time complexity: O(n) using a HashSet
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Set<Integer> seen = new HashSet<>();
        for (int i = 0; i < n; i++) {
            int id = sc.nextInt();
            if (!seen.add(id)) { System.out.println("Duplicate: " + id); return; }
        }
        System.out.println("No duplicates");
    }
}