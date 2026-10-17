
import java.util.HashMap;
import java.util.Map;

class Account {
    private String id;
    private double balance;

    Account(String id, double balance) {
        this.id = id;
        this.balance = balance;
    }

    String getId() {
        return id;
    }

    void withdraw(double amount) {
        if (amount > balance) {
            throw new IllegalStateException("Insufficient funds");
        }
        balance -= amount;
    }

    double getBalance() {
        return balance;
    }
}

public class Main {
    public static void main(String[] args) {
        Map<String, Account> accounts = new HashMap<>();
        accounts.put("A1", new Account("A1", 500));
        accounts.put("B2", new Account("B2", 100));
        for (String id : new String[]{"A1", "B2", "C3"}) {
            Account acc = accounts.get(id);
            if (acc == null) {
                System.out.println(id + " not found");
                continue;
            }
            try {
                acc.withdraw(200);
                System.out.println(id + " ok: " + acc.getBalance());
            } catch (IllegalStateException e) {
                System.out.println(id + " failed: " + e.getMessage());
            }
        }
    }
}