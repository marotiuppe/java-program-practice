import java.util.*;

class Student {
    private int id;
    private String name;

    Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        return this.id == ((Student) o).id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Student[id=" + id + ", name=" + name + "]";
    }
}

public class Main {
    public static void main(String[] args) {
        Set<Student> set = new HashSet<>();
        set.add(new Student(1, "Asha"));
        set.add(new Student(2, "Ravi"));
        set.add(new Student(1, "Asha K"));
        System.out.println(set.size());
        Object a = new Student(1, "X");
        Object b = new Student(1, "Y");
        System.out.println(a.equals(b));
        Student s = new Student(3, "Meena");
        System.out.println(s);
    }
}
