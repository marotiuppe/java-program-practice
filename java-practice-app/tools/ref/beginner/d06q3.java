import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int m = sc.nextInt(), y = sc.nextInt();
        switch (m) {
            case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                System.out.println(31); break;
            case 4: case 6: case 9: case 11:
                System.out.println(30); break;
            case 2:
                boolean leap = (y % 4 == 0 && y % 100 != 0) || y % 400 == 0;
                System.out.println(leap ? 29 : 28); break;
            default:
                System.out.println("Invalid month");
        }
    }
}
