class Animal {
    String sound() {
        return "...";
    }

    String describe(Animal other) {
        return "Animal meets animal";
    }
}

class Dog extends Animal {
    @Override
    String sound() {
        return "Woof";
    }

    @Override
    String describe(Animal other) {
        if (other instanceof Dog) return "Dog meets dog";
        return "Dog meets other animal";
    }

    void fetch() {
        System.out.println("Fetching");
    }
}

class Cat extends Animal {
    @Override
    String sound() {
        return "Meow";
    }
}

public class Main {
    public static void main(String[] args) {
        Animal[] zoo = { new Dog(), new Cat(), new Dog() };
        for (Animal a : zoo) {
            System.out.println(a.sound());
            if (a instanceof Dog) {
                Dog d = (Dog) a;
                d.fetch();
            }
        }
        Animal first = zoo[0];
        System.out.println(first.describe(zoo[2]));
        System.out.println(first.describe(zoo[1]));
    }
}
