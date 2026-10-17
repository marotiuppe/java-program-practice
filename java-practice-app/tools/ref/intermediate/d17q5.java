public class Main {
    static int maxSumK(int[] a, int k) {
        if (k < 1 || k > a.length) {
            return -1;
        }
        int sum = 0;
        for (int i = 0; i < k; i++) {
            sum += a[i];
        }
        int best = sum;
        for (int i = k; i < a.length; i++) {
            sum += a[i] - a[i - k];
            best = Math.max(best, sum);
        }
        return best;
    }

    public static void main(String[] args) {
        System.out.println(maxSumK(new int[]{1, 4, 2, 10, 2, 3, 1, 0, 20}, 4));
        System.out.println(maxSumK(new int[]{-5, -2, -3, -8}, 2));
        System.out.println(maxSumK(new int[]{1, 2, 3}, 10));
    }
}
