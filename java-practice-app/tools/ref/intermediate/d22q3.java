import java.util.*;

public class Main {
    static class Res implements AutoCloseable {
        String name;
        boolean failClose;

        Res(String name, boolean failOpen, boolean failClose) {
            if (failOpen) throw new RuntimeException("open " + name + " failed");
            this.name = name;
            this.failClose = failClose;
            System.out.println("open " + name);
        }

        @Override
        public void close() {
            System.out.println("close " + name);
            if (failClose) throw new RuntimeException("close " + name + " failed");
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        while (t-- > 0) {
            boolean bodyFails = sc.nextInt() == 1, openBFails = sc.nextInt() == 1;
            boolean closeAFails = sc.nextInt() == 1, closeBFails = sc.nextInt() == 1;
            try (Res a = new Res("A", false, closeAFails); Res b = new Res("B", openBFails, closeBFails)) {
                System.out.println("body");
                if (bodyFails) throw new IllegalStateException("body failed");
            } catch (Exception e) {
                System.out.println("caught: " + e.getMessage());
                for (Throwable s : e.getSuppressed()) System.out.println("suppressed: " + s.getMessage());
            } finally {
                System.out.println("finally");
            }
            System.out.println("---");
        }
    }
}
