import java.util.*;

public class Main {
    static class Emp implements Comparable<Emp> {
        String name;
        int salary;

        Emp(String name, int salary) {
            this.name = name;
            this.salary = salary;
        }

        public int compareTo(Emp other) {
            return Integer.compare(salary, other.salary);
        }

        public String toString() {
            return name;
        }
    }

    public static void main(String[] args) {
        List<Emp> list = new ArrayList<>();
        list.add(new Emp("A", Integer.MAX_VALUE));
        list.add(new Emp("B", -5));
        list.add(new Emp("C", 100));
        Collections.sort(list);
        System.out.println(list);

        String[] names = {"kim", "al", "joy", "zed"};
        Arrays.sort(names, (x, y) -> {
            if (x.length() != y.length()) {
                return y.length() - x.length();
            }
            return x.compareTo(y);
        });
        System.out.println(Arrays.toString(names));
    }
}
