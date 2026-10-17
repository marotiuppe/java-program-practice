import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int a = n / 1000, b = n % 1000;
        int sa = a / 100 + a / 10 % 10 + a % 10;
        int sb = b / 100 + b / 10 % 10 + b % 10;
        System.out.println(sa == sb ? "Lucky" : "Not lucky");
    }
}
