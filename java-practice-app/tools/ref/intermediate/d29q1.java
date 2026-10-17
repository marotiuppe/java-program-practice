import java.io.*;
import java.util.*;

public class Main {
    static class Book {
        int copies;
        Book(int c) { copies = c; }
    }

    static class Member {
        Map<String, Integer> loans = new HashMap<>();
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(in.readLine().trim());
        Map<String, Book> books = new HashMap<>();
        Map<String, Member> members = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            String[] p = in.readLine().trim().split("\s+");
            switch (p[0]) {
                case "ADD" -> books.put(p[1], new Book(Integer.parseInt(p[2])));
                case "JOIN" -> members.put(p[1], new Member());
                case "BORROW" -> {
                    Member m = members.get(p[1]);
                    Book b = books.get(p[2]);
                    int day = Integer.parseInt(p[3]);
                    if (m == null) sb.append("NO SUCH MEMBER\n");
                    else if (b == null) sb.append("NO SUCH BOOK\n");
                    else if (m.loans.containsKey(p[2])) sb.append("ALREADY BORROWED\n");
                    else if (m.loans.size() >= 2) sb.append("LIMIT REACHED\n");
                    else if (b.copies == 0) sb.append("NO COPIES\n");
                    else {
                        b.copies--;
                        m.loans.put(p[2], day + 14);
                        sb.append("OK due ").append(day + 14).append("\n");
                    }
                }
                default -> {
                    Member m = members.get(p[1]);
                    int day = Integer.parseInt(p[3]);
                    if (m == null || !m.loans.containsKey(p[2])) sb.append("NOT BORROWED\n");
                    else {
                        int due = m.loans.remove(p[2]);
                        books.get(p[2]).copies++;
                        sb.append("RETURNED fine=").append(day > due ? 2L * (day - due) : 0).append("\n");
                    }
                }
            }
        }
        System.out.print(sb);
    }
}
