import java.util.*;

class Employee {
    String name; double base;
    Employee(String n, double b) { name = n; base = b; }
    double totalSalary() { return base; }
}
class Manager extends Employee {
    double bonus;
    Manager(String n, double b, double p) { super(n, b); bonus = p; }
    @Override double totalSalary() { return base + base * bonus / 100; }
}
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        double sum = 0;
        for (int i = 0; i < n; i++) {
            String k = sc.next(), name = sc.next();
            double sal = sc.nextDouble();
            Employee e = k.equals("M") ? new Manager(name, sal, sc.nextDouble()) : new Employee(name, sal);
            System.out.printf("%s: %.2f%n", e.name, e.totalSalary());
            sum += e.totalSalary();
        }
        System.out.printf("Total payroll: %.2f%n", sum);
    }
}