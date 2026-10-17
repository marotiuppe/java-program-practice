import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());
        try (PrintWriter pw = new PrintWriter(new FileWriter("notes.txt"))) {
            for (int i = 0; i < n; i++) pw.println(sc.nextLine());
        }
        int lines = 0, words = 0, chars = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("notes.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines++;
                chars += line.length();
                words += line.trim().isEmpty() ? 0 : line.trim().split("\\s+").length;
            }
        }
        System.out.println("Lines: " + lines);
        System.out.println("Words: " + words);
        System.out.println("Characters: " + chars);
    }
}