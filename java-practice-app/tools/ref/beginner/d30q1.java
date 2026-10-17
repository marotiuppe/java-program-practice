import java.util.*;

class Student {
    String name; int m1, m2, m3;
    Student(String n, int a, int b, int c) { name = n; m1 = a; m2 = b; m3 = c; }
    int total() { return m1 + m2 + m3; }
    double avg() { return total() / 3.0; }
    boolean passed() { return m1 >= 35 && m2 >= 35 && m3 >= 35; }
    char grade() {
        if (!passed()) return 'F';
        double a = avg();
        return a >= 85 ? 'A' : a >= 70 ? 'B' : a >= 50 ? 'C' : 'D';
    }
}
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        List<Student> list = new ArrayList<>();
        for (int i = 0; i < n; i++) list.add(new Student(sc.next(), sc.nextInt(), sc.nextInt(), sc.nextInt()));
        list.sort((x, y) -> y.total() - x.total());
        int p = 0;
        for (int i = 0; i < n; i++) {
            Student s = list.get(i);
            if (s.passed()) p++;
            System.out.printf("%d. %s total=%d avg=%.2f grade=%c%n", i + 1, s.name, s.total(), s.avg(), s.grade());
        }
        System.out.println("Passed: " + p + "/" + n);
    }
}