public class Main {
    static int maxProduct(int[] a) {
        int best = a[0];
        int max = a[0];
        int min = a[0];
        for (int i = 1; i < a.length; i++) {
            int x = a[i];
            int hi = Math.max(x, Math.max(max * x, min * x));
            int lo = Math.min(x, Math.min(max * x, min * x));
            max = hi;
            min = lo;
            best = Math.max(best, max);
        }
        return best;
    }

    public static void main(String[] args) {
        System.out.println(maxProduct(new int[]{2, 3, -2, 4}));
        System.out.println(maxProduct(new int[]{-2, 3, -4}));
        System.out.println(maxProduct(new int[]{-3}));
        System.out.println(maxProduct(new int[]{0, 2}));
        System.out.println(maxProduct(new int[]{-2, 0, -1}));
    }
}
