public class Main {
    static int sumDigits(int n) {
        if (n == 0) {
            return 0;
        }
        return n % 10 + sumDigits(n / 10);
    }

    static boolean isPal(String s, int i, int j) {
        if (i >= j) {
            return true;
        }
        if (s.charAt(i) != s.charAt(j)) {
            return false;
        }
        return isPal(s, i + 1, j - 1);
    }

    static int fact(int n) {
        if (n == 0) {
            return 1;
        }
        return n * fact(n - 1);
    }

    static void printBinary(int n) {
        if (n > 0) {
            printBinary(n / 2);
            System.out.print(n % 2);
        }
    }

    public static void main(String[] args) {
        System.out.println(sumDigits(1234));
        System.out.println(isPal("racecar", 0, 6));
        System.out.println(fact(5));
        printBinary(6);
        System.out.println();
    }
}
