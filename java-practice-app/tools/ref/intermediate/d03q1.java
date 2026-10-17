import java.util.*;

abstract class Employee {
    protected final String name;
    protected final int base;

    Employee(String name, int base) {
        this.name = name;
        this.base = base;
    }

    abstract long pay();

    abstract String type();
}

class Manager extends Employee {
    private final int team;

    Manager(String n, int b, int team) {
        super(n, b);
        this.team = team;
    }

    long pay() {
        return base + (long) team * 1000;
    }

    String type() {
        return "Manager";
    }
}

class Developer extends Employee {
    private final int overtime;

    Developer(String n, int b, int ot) {
        super(n, b);
        this.overtime = ot;
    }

    long pay() {
        return base + (long) Math.min(overtime, 40) * 200;
    }

    String type() {
        return "Developer";
    }
}

class Intern extends Employee {
    Intern(String n, int b) {
        super(n, b);
    }

    long pay() {
        return base;
    }

    String type() {
        return "Intern";
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        long total = 0;
        for (int i = 0; i < n; i++) {
            String t = sc.next();
            String name = sc.next();
            int base = sc.nextInt();
            int extra = sc.nextInt();
            Employee e;
            if (t.equals("M")) e = new Manager(name, base, extra);
            else if (t.equals("D")) e = new Developer(name, base, extra);
            else e = new Intern(name, base);
            System.out.println(e.name + " (" + e.type() + "): " + e.pay());
            total += e.pay();
        }
        System.out.println("Total payroll: " + total);
    }
}
