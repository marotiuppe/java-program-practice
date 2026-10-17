import java.io.*;
import java.util.*;

interface TextTransformer {
    String apply(String s);
}

class Upper implements TextTransformer {
    public String apply(String s) {
        return s.toUpperCase();
    }
}

class Lower implements TextTransformer {
    public String apply(String s) {
        return s.toLowerCase();
    }
}

class Reverse implements TextTransformer {
    public String apply(String s) {
        return new StringBuilder(s).reverse().toString();
    }
}

class Trim implements TextTransformer {
    public String apply(String s) {
        return s.trim();
    }
}

class DedupeSpaces implements TextTransformer {
    public String apply(String s) {
        return s.replaceAll(" +", " ");
    }
}

class Capitalize implements TextTransformer {
    public String apply(String s) {
        StringBuilder sb = new StringBuilder();
        boolean start = true;
        for (char ch : s.toCharArray()) {
            if (ch == ' ') {
                sb.append(ch);
                start = true;
            } else {
                sb.append(start ? Character.toUpperCase(ch) : Character.toLowerCase(ch));
                start = false;
            }
        }
        return sb.toString();
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String text = br.readLine();
        if (text == null) text = "";
        Map<String, TextTransformer> reg = new HashMap<>();
        reg.put("UPPER", new Upper());
        reg.put("LOWER", new Lower());
        reg.put("REVERSE", new Reverse());
        reg.put("TRIM", new Trim());
        reg.put("DEDUPE_SPACES", new DedupeSpaces());
        reg.put("CAPITALIZE", new Capitalize());
        int k = Integer.parseInt(br.readLine().trim());
        for (int i = 0; i < k; i++) {
            String name = br.readLine().trim();
            text = reg.get(name).apply(text);
            System.out.println(name + ": [" + text + "]");
        }
    }
}
