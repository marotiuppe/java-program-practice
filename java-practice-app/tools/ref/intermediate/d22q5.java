class InvalidAgeException extends Exception {
    InvalidAgeException(String msg) {
        super(msg);
    }
}

class Session implements AutoCloseable {
    void open() {
        System.out.println("open");
    }

    @Override
    public void close() {
        System.out.println("close");
    }
}

public class Main {
    static void check(int age) throws InvalidAgeException {
        if (age < 0 || age > 150) {
            throw new InvalidAgeException("Invalid age: " + age);
        }
        System.out.println("Age ok: " + age);
    }

    public static void main(String[] args) {
        try (Session s = new Session()) {
            s.open();
            check(30);
            check(200);
        } catch (InvalidAgeException e) {
            System.out.println("Invalid: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
