import java.util.*;

class Notification {
    protected final String recipient;
    protected final String message;

    Notification(String recipient, String message) {
        this.recipient = recipient;
        this.message = message;
    }

    final String process() {
        if (message.isEmpty()) return "SKIPPED " + recipient;
        return render();
    }

    String render() {
        return recipient + ": " + message;
    }
}

class EmailNotification extends Notification {
    EmailNotification(String r, String m) {
        super(r, m);
    }

    @Override
    String render() {
        return "EMAIL to " + recipient + ": " + message;
    }
}

class SmsNotification extends Notification {
    SmsNotification(String r, String m) {
        super(r, m);
    }

    @Override
    String render() {
        String text = message.length() <= 20 ? message : message.substring(0, 17) + "...";
        return "SMS to " + recipient + ": " + text;
    }
}

class PushNotification extends Notification {
    PushNotification(String r, String m) {
        super(r, m);
    }

    @Override
    String render() {
        return "PUSH [" + recipient + "] " + message.toUpperCase();
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());
        for (int i = 0; i < n; i++) {
            String[] p = sc.nextLine().split(" ", 3);
            String msg = p.length > 2 ? p[2] : "";
            Notification x;
            switch (p[0]) {
                case "EMAIL": x = new EmailNotification(p[1], msg); break;
                case "SMS": x = new SmsNotification(p[1], msg); break;
                default: x = new PushNotification(p[1], msg);
            }
            System.out.println(x.process());
        }
    }
}
