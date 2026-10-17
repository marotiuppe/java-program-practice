import java.util.*;

abstract class Account {
    protected double balance;
    private String id;

    Account(String id, double balance) {
        this.id = id;
        this.balance = balance;
    }

    abstract double monthlyInterest();

    String getId() {
        return id;
    }
}

class Savings extends Account {
    Savings(String id, double balance) {
        super(id, balance);
    }

    double monthlyInterest() {
        return balance * (4 / 100.0) / 12;
    }
}

class Current extends Account {
    Current(String id, double balance) {
        super(id, balance);
    }

    double monthlyInterest() {
        return 0;
    }
}

public class Main {
    public static void main(String[] args) {
        List<Account> accounts = new ArrayList<>();
        accounts.add(new Savings("S1", 12000));
        accounts.add(new Current("C1", 500));
        accounts.add(new Savings("S2", 6000));
        double total = 0;
        for (int i = 0; i < accounts.size(); i++) {
            total += accounts.get(i).monthlyInterest();
        }
        System.out.printf("Total interest: %.2f%n", total);
        for (Account a : accounts) {
            if (a.getId().equals("C1")) {
                System.out.println("Current account found");
            }
        }
    }
}
