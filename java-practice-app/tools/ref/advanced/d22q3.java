import java.io.*;
import java.util.*;

public class Main {
    static class Registry {
        private final Map<Class<?>, Object> map = new HashMap<>();

        <T> Object put(Class<T> type, T value) {
            return map.put(type, value);
        }

        <T> T get(Class<T> type) {
            return type.cast(map.get(type));
        }

        <T> T remove(Class<T> type) {
            return type.cast(map.remove(type));
        }
    }

    static Class<?> cls(String name) {
        return switch (name) {
            case "Integer" -> Integer.class;
            case "Double" -> Double.class;
            default -> String.class;
        };
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    static Object putRaw(Registry r, String type, String raw) {
        switch (type) {
            case "Integer": return r.put(Integer.class, Integer.valueOf(raw.trim()));
            case "Double": return r.put(Double.class, Double.valueOf(raw.trim()));
            default: return r.put(String.class, raw);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int q = Integer.parseInt(br.readLine().trim());
        Registry r = new Registry();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < q; i++) {
            String line = br.readLine();
            String[] p = line.split(" ", 3);
            String cmd = p[0];
            String type = p[1];
            if (cmd.equals("PUT")) {
                String raw = p.length > 2 ? p[2] : "";
                Object old = putRaw(r, type, raw);
                sb.append(old == null ? "OK" : "REPLACED " + old).append('\n');
            } else if (cmd.equals("GET")) {
                Object v = r.get(cls(type));
                sb.append(v == null ? "MISSING" : v.toString()).append('\n');
            } else {
                Object v = r.remove(cls(type));
                sb.append(v == null ? "MISSING" : v.toString()).append('\n');
            }
        }
        System.out.print(sb);
    }
}
