public class Main {
    static int max(int a, int b) {
        if (a > b) {
            return a;
        } else {
            return b;
        }
    }

    static boolean isEven(int n) {
        return n % 2 == 0;
    }

    public static void main(String[] args) {
        int result = max(4, 9);
        System.out.println("Max: " + result);
        boolean even = isEven(10);
        System.out.println("Even: " + even);
        System.out.println(max(7, 7));
    }
}
