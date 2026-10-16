import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String f = sc.next(), l = sc.next(), roll = sc.next();
        System.out.println((f.substring(0, 1) + l + roll).toLowerCase() + "@school.edu");
    }
}
