import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int amt = sc.nextInt();
        System.out.println("500 notes: " + amt / 500);
        System.out.println("100 notes: " + amt % 500 / 100);
    }
}
