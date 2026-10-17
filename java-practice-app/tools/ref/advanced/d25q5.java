import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    static AtomicInteger counter = new AtomicInteger();
    static Map<String, Integer> hits = new ConcurrentHashMap<>();
    static int configsCreated = 0;
    static Config config;

    static class Config {
        Config() {
            configsCreated++;
        }
    }

    static synchronized Config getConfig() {
        if (config == null) {
            config = new Config();
        }
        return config;
    }

    public static void main(String[] args) throws Exception {
        List<Thread> workers = new ArrayList<>();
        for (int w = 0; w < 4; w++) {
            Thread t = new Thread(() -> {
                getConfig();
                for (int i = 0; i < 100000; i++) {
                    counter.incrementAndGet();
                    hits.merge("k", 1, Integer::sum);
                }
            });
            workers.add(t);
            t.start();
        }
        for (Thread t : workers) {
            t.join();
        }
        System.out.println(counter.get());
        System.out.println(hits.get("k"));
        System.out.println(configsCreated);
    }
}
