class Room {
    static int roomCount;
    int capacity;
    String name;

    Room(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        roomCount++;
    }

    String describe() {
        return name + " holds " + capacity;
    }

    void expand(int extra) {
        capacity += extra;
    }
}

public class Main {
    public static void main(String[] args) {
        Room r1 = new Room("Lab", 30);
        Room r2 = new Room("Hall", 100);
        r1.expand(10);
        System.out.println(r1.describe());
        System.out.println(r2.describe());
        System.out.println("Rooms: " + Room.roomCount);
    }
}
