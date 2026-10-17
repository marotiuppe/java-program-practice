import java.util.*;

class Employee {
    private static final Map<String, Integer> seq = new HashMap<>();
    private static int total = 0;
    private final String name;
    private final String id;

    private Employee(String name, String id) {
        this.name = name;
        this.id = id;
        total++;
    }

    static Employee create(String name, String dept) {
        String d = dept.toUpperCase();
        int k = seq.merge(d, 1, Integer::sum);
        return new Employee(name, String.format("%s-%03d", d, k));
    }

    static int getTotal() {
        return total;
    }

    String getName() {
        return name;
    }

    String getId() {
        return id;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        for (int i = 0; i < n; i++) {
            String name = sc.next();
            String dept = sc.next();
            Employee e = Employee.create(name, dept);
            System.out.println(e.getName() + " -> " + e.getId());
        }
        System.out.println("Total employees: " + Employee.getTotal());
    }
}
