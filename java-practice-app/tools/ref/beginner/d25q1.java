import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        try (PrintWriter pw = new PrintWriter(new FileWriter("students.txt"))) {
            for (int i = 0; i < n; i++) pw.println(sc.next() + " " + sc.nextInt());
        }
        int sum = 0, cnt = 0;
        try (Scanner f = new Scanner(new File("students.txt"))) {
            while (f.hasNextLine()) {
                String[] p = f.nextLine().split(" ");
                int m = Integer.parseInt(p[1]);
                System.out.println(p[0] + ": " + m + " - " + (m >= 40 ? "Pass" : "Fail"));
                sum += m; cnt++;
            }
        }
        System.out.printf("Average: %.2f%n", (double) sum / cnt);
    }
}