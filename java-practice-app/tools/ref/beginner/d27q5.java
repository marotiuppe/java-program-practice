
public class Main {
    public static void main(String[] args) {
        int n = 30;
        boolean[] composite = new boolean[n + 1];
        int count = 0;
        for (int i = 2; i <= n; i++) {
            if (!composite[i]) {
                count++;
                for (int j = i * i; j <= n; j += i) {
                    composite[j] = true;
                }
            }
        }
        System.out.println("Primes up to " + n + ": " + count);
    }
}