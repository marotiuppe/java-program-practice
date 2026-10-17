import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    static class Employee {
        final int id;
        final String name;
        final boolean active;

        Employee(int id, String name, boolean active) {
            this.id = id;
            this.name = name;
            this.active = active;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Employee other && id == other.id;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(id);
        }
    }

    static List<Employee> clean(List<Employee> staff) {
        Set<Employee> seen = new HashSet<>();
        List<Employee> unique = new ArrayList<>();
        for (Employee e : staff) {
            if (seen.add(e)) {
                unique.add(e);
            }
        }
        unique.removeIf(e -> !e.active);
        return unique;
    }

    public static void main(String[] args) {
        List<Employee> staff = new ArrayList<>();
        staff.add(new Employee(1, "Ana", true));
        staff.add(new Employee(2, "Bo", false));
        staff.add(new Employee(1, "Ana2", true));
        staff.add(new Employee(3, "Cy", true));
        staff.add(new Employee(4, "Di", false));
        for (Employee e : clean(staff)) {
            System.out.println(e.name);
        }
    }
}
