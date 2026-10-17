class Vehicle {
    private String plate;
    protected int wheels;

    Vehicle(String plate, int wheels) {
        this.plate = plate;
        this.wheels = wheels;
    }

    public String describe() {
        return plate + " has " + wheels + " wheels";
    }
}

class Bike extends Vehicle {
    Bike(String plate) {
        super(plate, 2);
    }

    @Override
    public String describe() {
        return super.describe() + " and no roof";
    }

    @Override
    public String toString() {
        return describe();
    }
}

class Car extends Vehicle {
    private int trunk;

    Car(String plate, int trunk) {
        super(plate, 4);
        this.trunk = trunk;
    }

    @Override
    public String describe() {
        return super.describe() + " and trunk " + trunk + "L";
    }
}

public class Main {
    public static void main(String[] args) {
        Vehicle[] list = { new Bike("KA01"), new Car("MH12", 400) };
        for (Vehicle v : list) {
            System.out.println(v.describe());
        }
    }
}
