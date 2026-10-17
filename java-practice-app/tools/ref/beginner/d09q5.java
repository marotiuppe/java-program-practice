import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int sum = 0, count = 0;
        int num = sc.nextInt();
        while (num != 0) {
            sum += num;
            count++;
            num = sc.nextInt();
        }
        if (count == 0) {
            System.out.println("No numbers entered");
        } else {
            System.out.println("Sum: " + sum);
            System.out.printf("Average: %.2f%n", (double) sum / count);
        }
    }
}
