import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int l = sc.nextInt(), w = sc.nextInt(), s = sc.nextInt();
        System.out.println("Tiles: " + (l / s) * (w / s));
        System.out.println("Leftover length: " + l % s + " cm");
        System.out.println("Leftover width: " + w % s + " cm");
    }
}
