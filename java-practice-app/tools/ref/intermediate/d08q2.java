import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < n; i++) list.add(sc.nextInt());
        int k = sc.nextInt();
        int removed = 0;
        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            if (it.next() % k == 0) {
                it.remove();
                removed++;
            }
        }
        ListIterator<Integer> li = list.listIterator();
        while (li.hasNext()) {
            int x = li.next() * 2;
            li.set(x);
            if (x > 20) li.add(-1);
        }
        System.out.println(list);
        System.out.println("Removed: " + removed);
    }
}
