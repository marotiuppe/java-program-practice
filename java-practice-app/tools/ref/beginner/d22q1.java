import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        ArrayList<String> cart = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String c = sc.next();
            if (c.equals("ADD")) { String it = sc.next(); cart.add(it); System.out.println("Added: " + it); }
            else if (c.equals("REMOVE")) {
                String it = sc.next();
                if (cart.remove(it)) System.out.println("Removed: " + it);
                else System.out.println("Not in cart: " + it);
            } else {
                if (cart.isEmpty()) System.out.println("Cart is empty");
                else System.out.println("Cart: " + cart);
            }
        }
    }
}