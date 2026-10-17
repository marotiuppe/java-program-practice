import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int marks = sc.nextInt(), att = sc.nextInt();
        if (marks < 0 || marks > 100) {
            System.out.println("Invalid marks");
        } else {
            String g;
            if (marks >= 90) g = "A";
            else if (marks >= 75) g = "B";
            else if (marks >= 60) g = "C";
            else if (marks >= 40) g = "D";
            else g = "F";
            System.out.println("Grade: " + g);
            System.out.println("Scholarship: " + (g.equals("A") && att >= 85 ? "Yes" : "No"));
        }
    }
}
