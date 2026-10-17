import java.util.*;

public class Main {
    static class Student implements Comparable<Student> {
        String name;
        int marks;

        Student(String n, int m) {
            name = n;
            marks = m;
        }

        public int compareTo(Student o) {
            if (marks != o.marks) return Integer.compare(o.marks, marks);
            return name.compareTo(o.name);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        List<Student> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String name = sc.next();
            int m = sc.nextInt();
            list.add(new Student(name, m));
        }
        Collections.sort(list);
        StringBuilder sb = new StringBuilder();
        int rank = 0;
        for (int i = 0; i < n; i++) {
            if (i == 0 || list.get(i).marks != list.get(i - 1).marks) rank = i + 1;
            sb.append(rank).append(' ').append(list.get(i).name).append(' ').append(list.get(i).marks).append('\n');
        }
        System.out.print(sb);
    }
}
