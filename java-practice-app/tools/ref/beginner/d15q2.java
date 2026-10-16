import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String a = sc.next(), b = sc.next();
        if (a.equals(b)) System.out.println("Equal");
        else if (a.equalsIgnoreCase(b)) System.out.println("Equal ignoring case");
        else System.out.println("Different");
    }
}
