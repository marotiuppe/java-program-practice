import java.util.*;

class UniqueList extends ArrayList<Integer> {
    @Override
    public boolean add(Integer x) {
        if (contains(x)) return false;
        return super.add(x);
    }

    @Override
    public boolean addAll(Collection<? extends Integer> c) {
        boolean changed = false;
        for (Integer x : c) {
            if (add(x)) changed = true;
        }
        return changed;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UniqueList u = new UniqueList();
        int n = sc.nextInt();
        for (int i = 0; i < n; i++) u.add(sc.nextInt());
        int m = sc.nextInt();
        List<Integer> in = new ArrayList<>();
        for (int i = 0; i < m; i++) in.add(sc.nextInt());
        System.out.println(u);
        List<Integer> ref = u;
        boolean r = ref.addAll(in);
        System.out.println(u);
        System.out.println(r);
    }
}
