public class Main {
    static int linearSearch(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                return i;
            }
        }
        return -1;
    }

    static boolean isSorted(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1]) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        int[] data = {10, 20, 30, 40};
        System.out.println(linearSearch(data, 30));
        System.out.println(linearSearch(data, 50));
        System.out.println(isSorted(data));
        System.out.println(isSorted(new int[]{3, 1, 2}));
    }
}
