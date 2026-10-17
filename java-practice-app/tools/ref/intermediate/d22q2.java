import java.util.*;

class UnknownCommandException extends RuntimeException {
    UnknownCommandException() { super("Unknown command"); }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());
        int[] arr = {10, 20, 30, 40, 50};
        while (n-- > 0) {
            String[] t = sc.nextLine().trim().split("\s+");
            try {
                switch (t[0]) {
                    case "GET" -> {
                        int i = Integer.parseInt(t[1]);
                        System.out.println(arr[i]);
                    }
                    case "DIV" -> {
                        int i = Integer.parseInt(t[1]);
                        int d = Integer.parseInt(t[2]);
                        int v = arr[i];
                        System.out.println(v / d);
                    }
                    case "SET" -> {
                        int i = Integer.parseInt(t[1]);
                        int v = Integer.parseInt(t[2]);
                        arr[i] = v;
                        System.out.println("OK");
                    }
                    default -> throw new UnknownCommandException();
                }
            } catch (NumberFormatException e) {
                System.out.println("Bad number");
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Bad index");
            } catch (ArithmeticException e) {
                System.out.println("Divide by zero");
            } catch (UnknownCommandException e) {
                System.out.println("Unknown command");
            }
        }
    }
}
