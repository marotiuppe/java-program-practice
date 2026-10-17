import java.util.*;

public class Main {
    public static void main(String[] args) {
        String s = new Scanner(System.in).next();
        for (char c : s.toCharArray()) {
            if (s.indexOf(c) == s.lastIndexOf(c)) { System.out.println(c); return; }
        }
        System.out.println("None");
    }
}