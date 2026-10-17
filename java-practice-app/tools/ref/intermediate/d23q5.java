import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        try (FileWriter fw = new FileWriter("names.txt")) {
            fw.write("asha\nravi\nmeena\n");
        }

        try (BufferedReader br = new BufferedReader(new FileReader("names.txt"))) {
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                System.out.println(line.toUpperCase());
                count++;
            }
            System.out.println("Lines: " + count);
        }
    }
}
