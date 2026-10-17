import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt(), qty = sc.nextInt();
        String item;
        int price;
        switch (choice) {
            case 1: item = "Tea"; price = 10; break;
            case 2: item = "Coffee"; price = 15; break;
            case 3: item = "Juice"; price = 25; break;
            case 4: item = "Water"; price = 5; break;
            default: item = null; price = 0;
        }
        if (item == null) System.out.println("Invalid choice");
        else System.out.println(item + " x" + qty + " = " + price * qty);
    }
}
