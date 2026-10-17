import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = sc.nextInt();
        int q = sc.nextInt();
        for (int i = 0; i < q; i++) {
            int idx = sc.nextInt();
            try {
                System.out.println(a[idx]);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Index out of range");
            }
        }
    }
}