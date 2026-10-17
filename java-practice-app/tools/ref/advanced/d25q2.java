import java.util.*;

public class Main {
    static final Object lock = new Object();
    static int next = 1;
    static int n;
    static final StringBuilder out = new StringBuilder();

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        String[] names = {"A", "B", "C"};
        Thread[] ts = new Thread[3];
        for (int w = 0; w < 3; w++) {
            final int id = w;
            ts[w] = new Thread(() -> {
                synchronized (lock) {
                    while (true) {
                        while (next <= n && (next - 1) % 3 != id) {
                            try {
                                lock.wait();
                            } catch (InterruptedException e) {
                                return;
                            }
                        }
                        if (next > n) {
                            lock.notifyAll();
                            return;
                        }
                        out.append(names[id]).append(':').append(next).append('\n');
                        next++;
                        lock.notifyAll();
                    }
                }
            }, names[w]);
        }
        for (Thread t : ts) t.start();
        for (Thread t : ts) t.join();
        System.out.print(out);
    }
}
