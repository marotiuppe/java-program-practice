import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());
        try (PrintWriter pw = new PrintWriter(new FileWriter("app.log"))) {
            for (int i = 0; i < n; i++) pw.println(sc.nextLine());
        }
        int e = 0, w = 0, inf = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("app.log"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String first = line.split(" ")[0];
                if (first.equals("ERROR")) e++;
                else if (first.equals("WARN")) w++;
                else if (first.equals("INFO")) inf++;
            }
        }
        System.out.println("ERROR: " + e);
        System.out.println("WARN: " + w);
        System.out.println("INFO: " + inf);
    }
}