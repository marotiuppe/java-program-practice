import java.util.*;

class InvalidAmountException extends Exception {
    InvalidAmountException(String m) { super(m); }
}

class InsufficientFundsException extends Exception {
    InsufficientFundsException(String m) { super(m); }
}

class DailyLimitExceededException extends Exception {
    DailyLimitExceededException(String m) { super(m); }
}

public class Main {
    static long balance, today;

    static void withdraw(int amount) throws Exception {
        if (amount <= 0 || amount % 100 != 0) throw new InvalidAmountException("Amount must be a positive multiple of 100");
        if (amount > balance) throw new InsufficientFundsException("Short by " + (amount - balance));
        if (today + amount > 3000) throw new DailyLimitExceededException("Remaining limit " + (3000 - today));
        balance -= amount;
        today += amount;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        balance = sc.nextLong();
        int n = sc.nextInt();
        while (n-- > 0) {
            int a = sc.nextInt();
            try {
                withdraw(a);
                System.out.println("OK " + balance);
            } catch (Exception e) {
                System.out.println("ERROR " + e.getClass().getSimpleName() + ": " + e.getMessage());
            }
        }
    }
}
