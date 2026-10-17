import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int u = sc.nextInt();
        int bill;
        if (u <= 100) bill = u * 5;
        else if (u <= 200) bill = 500 + (u - 100) * 7;
        else bill = 500 + 700 + (u - 200) * 10;
        System.out.println("Bill: " + bill);
    }
}
