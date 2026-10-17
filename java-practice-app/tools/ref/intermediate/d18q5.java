public class Main {
    static int firstOccurrence(int[] a, int x) {
        int lo = 0, hi = a.length, ans = -1;
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (a[mid] == x) {
                ans = mid;
                hi = mid;
            } else if (a[mid] < x) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return ans;
    }

    static int[] buildPrefix(int[] a) {
        int[] p = new int[a.length + 1];
        for (int i = 0; i < a.length; i++) {
            p[i + 1] = p[i] + a[i];
        }
        return p;
    }

    static int rangeSum(int[] p, int l, int r) {
        return p[r + 1] - p[l];
    }

    public static void main(String[] args) {
        int[] a = {2, 2, 3, 3, 3, 5};
        int[] p = buildPrefix(a);
        System.out.println(firstOccurrence(a, 3) + " " + firstOccurrence(a, 4) + " " + rangeSum(p, 1, 3));
    }
}
