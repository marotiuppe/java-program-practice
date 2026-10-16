import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int main = 0, anti = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int v = sc.nextInt();
                if (i == j) main += v;
                if (i + j == n - 1) anti += v;
            }
        }
        System.out.println("Main: " + main);
        System.out.println("Anti: " + anti);
    }
}
