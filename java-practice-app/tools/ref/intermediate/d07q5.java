enum Size {
    SMALL(100), MEDIUM(150), LARGE(200);

    private int price;

    private Size(int price) {
        this.price = price;
    }

    int getPrice() {
        return price;
    }
}

public class Main {
    public static void main(String[] args) {
        Size chosen = Size.valueOf("MEDIUM");
        switch (chosen) {
            case SMALL:
                System.out.println("Small cup");
                break;
            case MEDIUM:
                System.out.println("Medium cup");
                break;
            default:
                System.out.println("Large cup");
        }
        if (chosen == Size.MEDIUM) {
            System.out.println("Price " + chosen.getPrice());
        }
    }
}
