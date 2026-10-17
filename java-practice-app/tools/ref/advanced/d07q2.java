import java.util.*;

public class Main {
    static class Entry {
        int key;
        int val;
        Entry next;

        Entry(int key, int val, Entry next) {
            this.key = key;
            this.val = val;
            this.next = next;
        }
    }

    static Entry[] table = new Entry[16];
    static int size = 0;

    static int index(int key, int len) {
        int h = key * 0x9E3779B9;
        h ^= (h >>> 16);
        h *= 0x85EBCA6B;
        h ^= (h >>> 13);
        return h & (len - 1);
    }

    static void resize() {
        Entry[] nt = new Entry[table.length * 2];
        for (Entry e : table) {
            while (e != null) {
                Entry nx = e.next;
                int i = index(e.key, nt.length);
                e.next = nt[i];
                nt[i] = e;
                e = nx;
            }
        }
        table = nt;
    }

    static void put(int key, int val) {
        int i = index(key, table.length);
        for (Entry e = table[i]; e != null; e = e.next) {
            if (e.key == key) {
                e.val = val;
                return;
            }
        }
        table[i] = new Entry(key, val, table[i]);
        if (++size > table.length * 3 / 4) {
            resize();
        }
    }

    static int get(int key) {
        for (Entry e = table[index(key, table.length)]; e != null; e = e.next) {
            if (e.key == key) {
                return e.val;
            }
        }
        return -1;
    }

    static void remove(int key) {
        int i = index(key, table.length);
        Entry prev = null;
        for (Entry e = table[i]; e != null; prev = e, e = e.next) {
            if (e.key == key) {
                if (prev == null) {
                    table[i] = e.next;
                } else {
                    prev.next = e.next;
                }
                size--;
                return;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        StringTokenizer st = new StringTokenizer(new String(System.in.readAllBytes()));
        int q = Integer.parseInt(st.nextToken());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < q; i++) {
            String cmd = st.nextToken();
            int key = Integer.parseInt(st.nextToken());
            if (cmd.equals("PUT")) {
                put(key, Integer.parseInt(st.nextToken()));
            } else if (cmd.equals("GET")) {
                sb.append(get(key)).append('\n');
            } else {
                remove(key);
            }
        }
        System.out.print(sb);
    }
}
