class Student {
    private String name;
    private int marks;

    Student(String name, int marks) {
        if (marks < 0 || marks > 100) {
            throw new IllegalArgumentException("Invalid marks");
        }
        this.name = name;
        this.marks = marks;
    }

    public String getName() {
        return name;
    }

    public String getGrade() {
        if (marks >= 90) return "A";
        else if (marks >= 75) return "B";
        else return "C";
    }
}

public class Main {
    public static void main(String[] args) {
        Student s = new Student("Riya", 92);
        System.out.println(s.getName() + " " + s.getGrade());
        try {
            Student bad = new Student("Tom", 120);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
