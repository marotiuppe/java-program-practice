public class Main {
    static int smallestDivisor(int[] a, int threshold) {
        int lo = 1;
        int hi = 0;
        for (int x : a) {
            hi = Math.max(hi, x);
        }
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            long sum = 0;
            for (int x : a) {
                sum += (x + (long) mid - 1) / mid;
            }
            if (sum <= threshold) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }
        return lo;
    }

    public static void main(String[] args) {
        System.out.println(smallestDivisor(new int[]{1, 2, 5, 9}, 6));
        System.out.println(smallestDivisor(new int[]{44, 22, 33, 11, 1}, 5));
        System.out.println(smallestDivisor(new int[]{1000000000, 1000000000, 1000000000, 1000000000, 1000000000}, 5));
    }
}
