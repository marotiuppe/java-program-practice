import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<String> words = new ArrayList<>(Arrays.asList("ok", "spam", "fine", "spam", "good"));
        Iterator<String> it = words.iterator();
        while (it.hasNext()) {
            if (it.next().equals("spam")) {
                it.remove();
            }
        }
        System.out.println(words);

        List<Integer> nums = new ArrayList<>(Arrays.asList(5, 3, 8, 3, 1));
        nums.remove(Integer.valueOf(3));
        System.out.println(nums);

        for (int i = 0; i < nums.size(); i++) {
            System.out.println(nums.get(i));
        }
    }
}
