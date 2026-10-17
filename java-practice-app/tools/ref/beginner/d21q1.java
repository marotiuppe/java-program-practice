import java.util.*;

class BankAccount {
    private int balance;
    BankAccount(int b) { balance = b; }
    String deposit(int a) {
        if (a <= 0) return "Invalid amount. Balance: " + balance;
        balance += a;
        return "Deposited " + a + ". Balance: " + balance;
    }
    String withdraw(int a) {
        if (a <= 0) return "Invalid amount. Balance: " + balance;
        if (a > balance) return "Insufficient funds. Balance: " + balance;
        balance -= a;
        return "Withdrawn " + a + ". Balance: " + balance;
    }
}
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BankAccount acc = new BankAccount(sc.nextInt());
        int n = sc.nextInt();
        for (int i = 0; i < n; i++) {
            String op = sc.next();
            int amt = sc.nextInt();
            System.out.println(op.equals("D") ? acc.deposit(amt) : acc.withdraw(amt));
        }
    }
}