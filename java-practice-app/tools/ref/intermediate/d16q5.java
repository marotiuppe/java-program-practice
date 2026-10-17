public class Main {
    static int[] twoSumSorted(int[] a, int target) {
        int i = 0, j = a.length - 1;
        while (i < j) {
            int sum = a[i] + a[j];
            if (sum == target) {
                return new int[]{i, j};
            }
            if (sum < target) {
                i++;
            } else {
                j--;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        int[] a = {2, 3, 5, 8, 11};
        int[] r = twoSumSorted(a, 11);
        System.out.println(r[0] + " " + r[1]);
        r = twoSumSorted(a, 100);
        System.out.println(r == null ? "none" : r[0] + " " + r[1]);
    }
}
