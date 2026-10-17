import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int hours = sc.nextInt();
        if (hours <= 0) {
            System.out.println("Invalid");
        } else {
            int fee;
            if (hours <= 1) {
                fee = 20;
            } else if (hours <= 3) {
                fee = 50;
            } else if (hours <= 6) {
                fee = 100;
            } else {
                fee = 150;
            }
            System.out.println("Fee: " + fee);
        }
    }
}
