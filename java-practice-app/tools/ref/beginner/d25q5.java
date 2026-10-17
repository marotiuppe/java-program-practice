
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        try (FileWriter writer = new FileWriter("numbers.txt")) {
            for (int i = 1; i <= 3; i++) {
                writer.write(String.valueOf(i));
                writer.write("\n");
            }
        }
        int sum = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("numbers.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sum += Integer.parseInt(line);
            }
        }
        System.out.println("Sum: " + sum);
    }
}