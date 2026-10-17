import java.util.*;

class BankAccount {
    private final String holder;
    private int balance;
    private final int minBalance;

    BankAccount(String holder, int balance, int minBalance) {
        this.holder = holder;
        this.balance = balance;
        this.minBalance = minBalance;
    }

    boolean deposit(int amount) {
        if (amount <= 0) return false;
        balance += amount;
        return true;
    }

    boolean withdraw(int amount) {
        if (amount <= 0 || (long) balance - amount < minBalance) return false;
        balance -= amount;
        return true;
    }

    int getBalance() {
        return balance;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String name = sc.next();
        int open = sc.nextInt();
        int min = sc.nextInt();
        BankAccount acc = new BankAccount(name, open, min);
        int n = sc.nextInt();
        for (int i = 0; i < n; i++) {
            String t = sc.next();
            int amt = sc.nextInt();
            boolean ok = t.equals("D") ? acc.deposit(amt) : acc.withdraw(amt);
            System.out.println((ok ? "OK " : "REJECTED ") + acc.getBalance());
        }
        System.out.println("Final balance: " + acc.getBalance());
    }
}
