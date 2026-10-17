import java.util.*;

public class Main {
    static void countdown(int n) {
        if (n == 0) { System.out.println("Liftoff!"); return; }
        System.out.println("T-minus " + n);
        countdown(n - 1);
    }
    public static void main(String[] args) {
        countdown(new Scanner(System.in).nextInt());
    }
}