import java.util.*;

class Customer {
    private final String name;
    private final String email;

    Customer(String name, String email) {
        this.name = name;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return email.equalsIgnoreCase(((Customer) o).email);
    }

    @Override
    public int hashCode() {
        return email.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return name + " <" + email.toLowerCase() + ">";
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Set<Customer> seen = new HashSet<>();
        List<Customer> firsts = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Customer c = new Customer(sc.next(), sc.next());
            if (seen.add(c)) firsts.add(c);
        }
        System.out.println("Unique customers: " + seen.size());
        for (Customer c : firsts) System.out.println(c);
    }
}
