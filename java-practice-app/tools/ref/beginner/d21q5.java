
class Student {
    private String name;
    private int marks;

    Student(String name, int marks) {
        this.name = name;
        this.marks = marks;
    }

    public String getName() {
        return name;
    }

    public void setMarks(int marks) {
        if (marks >= 0 && marks <= 100) this.marks = marks;
    }

    public String grade() {
        if (marks >= 40) return "Pass";
        else return "Fail";
    }
}

public class Main {
    public static void main(String[] args) {
        Student s = new Student("Isha", 55);
        s.setMarks(30);
        System.out.println(s.getName() + ": " + s.grade());
        Student t = new Student("Ravi", 80);
        System.out.println(t.getName());
    }
}