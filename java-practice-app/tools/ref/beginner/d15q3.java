import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String u = sc.next();
        boolean ok = u.length() >= 5 && u.length() <= 12 && Character.isLetter(u.charAt(0));
        boolean digit = false;
        for (int i = 0; i < u.length(); i++) if (Character.isDigit(u.charAt(i))) digit = true;
        System.out.println(ok && digit ? "Valid" : "Invalid");
    }
}
