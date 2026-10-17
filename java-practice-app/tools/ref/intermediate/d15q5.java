public class Main {
    public static void main(String[] args) {
        String version = "1.2.10";
        String[] parts = version.split("\\.");
        System.out.println("Major: " + parts[0]);

        String text = "  Hello   Java  World  ";
        text = text.trim();
        String[] words = text.split("\\s+");
        System.out.println("Words: " + words.length);

        String raw = "9876543210";
        StringBuilder sb = new StringBuilder(raw);
        sb.insert(3, "-");
        sb.insert(7, "-");
        System.out.println(sb);

        String cmd = new String("stop");
        if (cmd.equals("stop")) {
            System.out.println("Stopping");
        }

        String upper = "java";
        upper = upper.toUpperCase();
        System.out.println(upper);
    }
}
