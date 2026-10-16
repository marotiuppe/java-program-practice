# 30-Day Java Practice Workbook — Intermediate

**Java version: 17** | Level: Intermediate | 5 questions per day | 150 questions total | Questions only

## Table of Contents

**Week 1 — Object-Oriented Programming in Depth**
- [Day 1 — Encapsulation and Constructors](#day1)
- [Day 2 — static Members, this, and Constructor Chaining](#day2)
- [Day 3 — Inheritance and Method Overriding](#day3)
- [Day 4 — Abstract Classes and Interfaces](#day4)
- [Day 5 — Polymorphism and Dynamic Dispatch](#day5)
- [Day 6 — equals, hashCode, and toString](#day6)
- [Day 7 — Enums and OOP Review](#day7)

**Week 2 — The Collections Framework**
- [Day 8 — List Variants and Iterator](#day8)
- [Day 9 — Set Variants](#day9)
- [Day 10 — Map Variants](#day10)
- [Day 11 — Comparable and Comparator](#day11)
- [Day 12 — ArrayDeque and PriorityQueue](#day12)
- [Day 13 — Generics Introduction](#day13)
- [Day 14 — Collections Combined (Week 2 Review)](#day14)

**Week 3 — Strings and Algorithms**
- [Day 15 — StringBuilder and Regex Basics](#day15)
- [Day 16 — Two-Pointer Technique](#day16)
- [Day 17 — Sliding Window](#day17)
- [Day 18 — Prefix Sums and Binary Search](#day18)
- [Day 19 — Sorting Algorithms](#day19)
- [Day 20 — Recursion and Backtracking](#day20)
- [Day 21 — Matrices (2D Arrays)](#day21)

**Week 4 — Exceptions, Modern Java, and Core Data Structures**
- [Day 22 — Custom Exceptions and try-with-resources](#day22)
- [Day 23 — File I/O](#day23)
- [Day 24 — Java 17 Features](#day24)
- [Day 25 — Lambdas and Streams](#day25)
- [Day 26 — Hand-Built Linked List, Stack, and Queue](#day26)
- [Day 27 — Introductory Dynamic Programming (1-D)](#day27)
- [Day 28 — Big-O Reasoning and Optimization](#day28)

**Consolidation and Challenge**
- [Day 29 — Mixed Revision](#day29)
- [Day 30 — Mock Assessment](#day30)

**Conventions used in this workbook:** programs read from standard input (`Scanner`) and write to standard output unless a question says otherwise. Code snippets assume Java 17 and a class named `Main`. Where a question asks for several classes, put them in one file with `Main` as the only `public` class. Averages and areas are printed with exactly 2 decimal places unless stated. Input is always valid unless a question says otherwise.

---

<a id="day1"></a>
## Day 1 — Encapsulation and Constructors

**Learning objectives:** Hide state behind `private` fields, enforce rules inside constructors and methods, build immutable classes with defensive copies, and use overloaded constructors.

### Question 1: Mixed Real-World Scenario
**Title:** Savings Account with Minimum Balance

**Problem statement:** Model a bank account as a class `BankAccount` with `private` fields (holder name, balance, minimum balance). The only ways to change the balance must be `deposit(int)` and `withdraw(int)`, and each returns `true` if it succeeded. A deposit is rejected if the amount is `<= 0`. A withdrawal is rejected if the amount is `<= 0` or if it would leave the balance below the minimum balance. Process a list of transactions and print the result of each.

**Real-world context:** Banks enforce minimum-balance rules centrally, so no caller can bypass them by editing a field directly.

**Input requirements:** Line 1: `holder openingBalance minBalance` (name, int, int). Line 2: `n`, the number of transactions. Next `n` lines: `D amount` (deposit) or `W amount` (withdraw).

**Expected output:** One line per transaction: `OK <balance>` or `REJECTED <balance>` (balance after the attempt). Last line: `Final balance: <balance>`.

**Sample cases:**
```
Input:
Asha 1000 500
4
D 500
W 900
W 200
D -5
Output:
OK 1500
OK 600
REJECTED 600
REJECTED 600
Final balance: 600
```
```
Input:
Ravi 0 0
3
W 1
D 10
W 10
Output:
REJECTED 0
OK 10
OK 0
Final balance: 0
```

**Constraints:** `1 <= n <= 1000`; all amounts and balances fit in `int`; the opening balance is at least the minimum balance. No setter or public field may expose the balance.

### Question 2: Interview-Based Coding
**Title:** Immutable Schedule with Defensive Copies

**Problem statement:** Write a `final` class `Schedule` that stores an `int[]` of time-slot durations. It must be truly immutable: the constructor must not keep a reference to the caller's array, and `getSlots()` must not hand out its internal array. Add `total()` (sum of slots) and `withSlot(int index, int value)`, which returns a **new** `Schedule` with one slot replaced and leaves the original unchanged. In `main`, build a schedule from the input, create the updated schedule, then deliberately overwrite element 0 of your original input array and of the array returned by `getSlots()`, and finally print the original schedule's total again.

**Input and output requirements:** Line 1: `n`. Line 2: `n` integers. Line 3: `index value`. If `index` is outside `0..n-1`, print only `Invalid index`. Otherwise print three lines:
```
Original total: <total of original>
Updated total: <total of updated schedule>
Tamper check: <total of original after tampering>
```

**Sample cases:**
```
Input:
3
1 2 3
1 10
Output:
Original total: 6
Updated total: 14
Tamper check: 6
```
```
Input:
2
4 4
0 0
Output:
Original total: 8
Updated total: 4
Tamper check: 8
```

**Constraints:** `1 <= n <= 100`; values are between `-1000` and `1000`. Edge cases: `n = 1`; `index = n` (invalid); negative index (invalid).

### Question 3: Company-Style Programming
**Title:** Parcel Shipping Quotes with Overloaded Constructors

**Problem statement:** A courier service prices parcels. Create a class `Parcel` with three constructors: `Parcel(int grams)`, `Parcel(int grams, boolean express)`, and `Parcel(int grams, boolean express, int insuredValue)`; omitted values default to standard delivery (`false`) and no insurance (`0`). Pricing rules:
- Base charge is `40` for up to 500 grams.
- Every started 100 grams above 500 adds `10` (for example, 501 g to 600 g adds 10).
- Express doubles the (base + weight) amount.
- Insurance adds `insuredValue * 2 / 100` (integer division) after the express step.

**Input and output requirements:** Line 1: `n`. Next `n` lines: `grams`, or `grams E`, or `grams E value`, where `E` is `Y` (express) or `N`. Print the final cost of each parcel on its own line. Each parcel must be created through the constructor that matches the information given.

**Sample cases:**
```
Input:
3
300
1200 N
1200 Y 5000
Output:
40
110
320
```
```
Input:
3
500 Y
501 N
1000 N 99
Output:
80
50
91
```

**Constraints and edge cases:** `1 <= grams <= 50000`; `0 <= insuredValue <= 1000000`. Test exactly 500 g, 501 g, and an insured value below 50 (insurance becomes 0).

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
class Counter {
    private int count;

    Counter(int start) {
        count = start;
    }

    void inc() {
        count++;
    }

    int get() {
        return count;
    }
}

public class Main {
    static void bump(Counter c) {
        c.inc();
        c = new Counter(100);
        c.inc();
    }

    static void bump(int n) {
        n++;
    }

    public static void main(String[] args) {
        Counter a = new Counter(1);
        Counter b = a;
        bump(a);
        int x = 5;
        bump(x);
        b.inc();
        Counter c = new Counter(1);
        System.out.println(a.get() + " " + b.get() + " " + c.get() + " " + x);
        System.out.println(a == b);
        System.out.println(a == c);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** A `Student` stores a name and marks (0 to 100 inclusive). The constructor must throw `IllegalArgumentException("Invalid marks")` for marks outside that range. `getGrade()` returns `"A"` for 90 and above, `"B"` for 75 to 89, and `"C"` otherwise. The program should print `Riya A`, then `Invalid marks`.

The code below has at least four bugs (compile-time and logic). Find all of them, explain each, and give the corrected program.

```java
class Student {
    private String name;
    private int marks;

    Student(String name, int marks) {
        if (marks < 0 && marks > 100) {
            throw new IllegalArgumentException("Invalid marks");
        }
        name = name;
        this.marks = marks;
    }

    public String getGrade() {
        if (marks >= 90) return "A";
        else if (marks >= 75) return "B";
        else "C";
    }
}

public class Main {
    public static void main(String[] args) {
        Student s = new Student("Riya", 92);
        System.out.println(s.name + " " + s.getGrade());
        try {
            Student bad = new Student("Tom", 120);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
```

---

<a id="day2"></a>
## Day 2 — static Members, this, and Constructor Chaining

**Learning objectives:** Tell class-level state from object-level state, chain constructors with `this(...)`, use `final` and static factory methods, and understand why static code cannot touch instance members.

### Question 1: Mixed Real-World Scenario
**Title:** Cinema Ticket Counter

**Problem statement:** A cinema counter issues booking IDs from a shared sequence. Write a class `Booking` with a `static` counter that gives each *accepted* booking the next ID starting at `1001`, a `static final` price of `150` per seat, and `static` totals for the number of bookings and revenue. A booking is rejected if seats is `< 1` or `> 10`; rejected requests must not consume an ID. Bookings of more than 4 seats get a 10% group discount on the total (`total * 90 / 100`, integer division).

**Real-world context:** Ticketing systems need one shared sequence so two customers never receive the same booking ID.

**Input requirements:** Line 1: `n`. Next `n` lines: `name seats`.

**Expected output:** For each accepted booking: `<id> <name> <seats> <amount>`. For a rejected one: `REJECTED <name>`. Then two lines: `Total bookings: <count>` and `Total revenue: <sum>`.

**Sample cases:**
```
Input:
3
Asha 2
Ravi 5
Meena 1
Output:
1001 Asha 2 300
1002 Ravi 5 675
1003 Meena 1 150
Total bookings: 3
Total revenue: 1125
```
```
Input:
3
Kiran 0
Joy 10
Sam 4
Output:
REJECTED Kiran
1001 Joy 10 1350
1002 Sam 4 600
Total bookings: 2
Total revenue: 1950
```

**Constraints:** `1 <= n <= 1000`; names contain no spaces. Edge cases: all requests rejected (totals are 0); exactly 4 and exactly 5 seats.

### Question 2: Interview-Based Coding
**Title:** Fraction Arithmetic

**Problem statement:** Implement an immutable class `Fraction` with `add`, `subtract`, `multiply`, and `divide`. Results must always be in lowest terms with a positive denominator (so `-1/2`, never `1/-2`). Provide the constructors `Fraction(int numerator, int denominator)` and `Fraction(int whole)`, where the second must delegate to the first with `this(...)`. Dividing by a fraction whose value is zero is undefined.

**Input and output requirements:** Line 1: `n`. Next `n` lines: `a/b op c/d` where `op` is one of `+ - * /` (tokens separated by single spaces; every denominator in the input is non-zero). Print the reduced result as `p/q`, or `Undefined` for division by a zero fraction.

**Sample cases:**
```
Input:
4
1/2 + 1/3
3/4 - 3/4
-1/2 * 2/3
2/3 / 4/9
Output:
5/6
0/1
-1/3
3/2
```
```
Input:
2
1/2 / 0/5
5/1 * 1/5
Output:
Undefined
1/1
```

**Constraints:** `|numerator|, denominator <= 1000` in the input; intermediate products fit in `long`. Zero is always printed as `0/1`.

### Question 3: Company-Style Programming
**Title:** Employee ID Allocator

**Problem statement:** An HR system gives each new employee an ID of the form `DEPT-NNN`, where `DEPT` is the upper-cased department code and `NNN` is a per-department sequence number starting at 1, zero-padded to 3 digits (numbers above 999 simply print all their digits). Write a class `Employee` with a **private** constructor and a `static` factory method `create(String name, String dept)` that allocates the ID using static state. Also track the total number of employees created.

**Input and output requirements:** Line 1: `n`. Next `n` lines: `name dept`. Print `<name> -> <id>` for each, then `Total employees: <n>`.

**Sample cases:**
```
Input:
4
Asha ENG
Ravi HR
Meena ENG
Joy ENG
Output:
Asha -> ENG-001
Ravi -> HR-001
Meena -> ENG-002
Joy -> ENG-003
Total employees: 4
```
```
Input:
2
Kim ops
Lee Ops
Output:
Kim -> OPS-001
Lee -> OPS-002
Total employees: 2
```

**Constraints and edge cases:** `1 <= n <= 5000`; department codes have 1 to 6 letters; `ops` and `OPS` are the same department; one department may have more than 999 employees.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
class Box {
    static int created = 0;
    int id;
    int size = 10;

    Box() {
        this(5);
        created++;
    }

    Box(int size) {
        this.size = size * 2;
        id = ++created;
    }

    static String info() {
        return "created=" + created;
    }
}

public class Main {
    public static void main(String[] args) {
        Box a = new Box();
        Box b = new Box(3);
        Box c = new Box();
        System.out.println(a.id + " " + a.size);
        System.out.println(b.id + " " + b.size);
        System.out.println(c.id + " " + c.size);
        System.out.println(Box.info());
        System.out.println(a.created == b.created);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Each `Room` has a name and a fixed-until-expanded capacity, and a shared counter of how many rooms exist. After creating `Lab` (30) and `Hall` (100) and expanding the lab by 10, it should print `Lab holds 40`, `Hall holds 100`, `Rooms: 2`.

The code below has at least four bugs. Identify each one, explain why it is wrong, and write the corrected program.

```java
class Room {
    static int roomCount;
    final int capacity;
    String name;

    Room(String name, int capacity) {
        name = name;
        this.capacity = capacity;
        this.roomCount++;
    }

    static String describe() {
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
```

---

<a id="day3"></a>
## Day 3 — Inheritance and Method Overriding

**Learning objectives:** Extend classes, call `super` constructors and methods, override correctly with `@Override`, and understand object initialization order.

### Question 1: Mixed Real-World Scenario
**Title:** Company Payroll by Employee Type

**Problem statement:** A company has three kinds of employees that all extend a base class `Employee` (name, base salary, and a method `pay()`):
- `Manager`: `base + teamSize * 1000`
- `Developer`: `base + overtimeHours * 200`, where overtime is capped at 40 hours
- `Intern`: just `base`

Read the employees, compute pay through a common `Employee` reference, and print a payroll summary.

**Real-world context:** HR software applies different pay formulas but must process all staff in one loop.

**Input requirements:** Line 1: `n`. Next `n` lines: `T name base extra`, where `T` is `M`, `D`, or `I`. `extra` is team size for managers, overtime hours for developers, and always `0` for interns.

**Expected output:** For each employee `name (Type): pay` where `Type` is `Manager`, `Developer`, or `Intern`; finally `Total payroll: <sum>`.

**Sample cases:**
```
Input:
3
M Asha 50000 4
D Ravi 40000 10
I Meena 15000 0
Output:
Asha (Manager): 54000
Ravi (Developer): 42000
Meena (Intern): 15000
Total payroll: 111000
```
```
Input:
2
D Joy 20000 50
M Lee 60000 0
Output:
Joy (Developer): 28000
Lee (Manager): 60000
Total payroll: 88000
```

**Constraints:** `1 <= n <= 1000`; salaries fit in `int`. Edge cases: overtime exactly 40 and above 40; a manager with a team of 0.

### Question 2: Interview-Based Coding
**Title:** UniqueList That Rejects Duplicates

**Problem statement:** Create `UniqueList extends ArrayList<Integer>` that never stores a duplicate value. `add(Integer)` must return `false` and leave the list unchanged for a duplicate. `addAll(Collection<? extends Integer>)` must also keep the list duplicate-free (including duplicates *within* the incoming collection) and return `true` only if at least one element was added. Test it through the input described below.

**Input and output requirements:** Line 1: `n`; line 2: `n` integers, each added with `add`. Line 3: `m`; line 4: `m` integers, added together with one `addAll` call (in the order given). Print the list after the `add` calls, the list after `addAll`, and the boolean returned by `addAll`.

**Sample cases:**
```
Input:
5
3 1 3 2 1
4
2 9 9 4
Output:
[3, 1, 2]
[3, 1, 2, 9, 4]
true
```
```
Input:
2
7 7
2
7 7
Output:
[7]
[7]
false
```

**Constraints:** `1 <= n, m <= 1000`; values fit in `int`. The class must work correctly when `addAll` is called through a plain `List<Integer>` reference.

### Question 3: Company-Style Programming
**Title:** Notification Dispatcher

**Problem statement:** A messaging service has a base class `Notification` (recipient, message) and subclasses `EmailNotification`, `SmsNotification`, and `PushNotification`, each overriding a `String render()` method:
- Email: `EMAIL to <recipient>: <message>`
- SMS: `SMS to <recipient>: <text>`, where `<text>` is the message if it is at most 20 characters, otherwise its first 17 characters followed by `...`
- Push: `PUSH [<recipient>] <MESSAGE IN UPPER CASE>`

A notification with an empty message is skipped; the base class should decide that once, for all subclasses, by printing `SKIPPED <recipient>`.

**Input and output requirements:** Line 1: `n`. Next `n` lines: `TYPE recipient message...` where the message is the rest of the line and may contain spaces or be missing entirely. Print one result line per notification.

**Sample cases:**
```
Input:
4
EMAIL asha Your order has shipped
SMS ravi Your order number 4821 will arrive tomorrow
PUSH meena Sale starts now
SMS joy
Output:
EMAIL to asha: Your order has shipped
SMS to ravi: Your order number...
PUSH [meena] SALE STARTS NOW
SKIPPED joy
```
```
Input:
2
SMS kim exactly twenty chars
EMAIL lee hi
Output:
SMS to kim: exactly twenty chars
EMAIL to lee: hi
```

**Constraints and edge cases:** `1 <= n <= 500`; messages have at most 200 characters; an SMS of exactly 20 characters is not truncated; a 21-character SMS is.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program. Pay attention to the order of static blocks, instance initializers, constructors, and overridden method calls.

```java
class Base {
    static {
        System.out.println("Base static");
    }

    {
        System.out.println("Base instance");
    }

    Base() {
        System.out.println("Base ctor");
        show();
    }

    void show() {
        System.out.println("Base show");
    }
}

class Derived extends Base {
    static {
        System.out.println("Derived static");
    }

    private int value = 5;

    {
        System.out.println("Derived instance " + value);
    }

    Derived() {
        System.out.println("Derived ctor");
        show();
    }

    @Override
    void show() {
        System.out.println("Derived show " + value);
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("main start");
        new Derived();
        System.out.println("---");
        new Derived();
    }
}
```

### Question 5: Debug the Code
**What the program should do:** A `Vehicle` has a plate and a wheel count. `Bike` (2 wheels) and `Car` (4 wheels plus a trunk size) extend it. The program should print:
```
KA01 has 2 wheels and no roof
MH12 has 4 wheels and trunk 400L
```

The code below has at least five bugs (some compile-time, some about overriding). Identify each, explain it, and provide the corrected program.

```java
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
        wheels = 2;
    }

    String describe() {
        return super.describe() + " and no roof";
    }

    @Override
    public String tostring() {
        return describe();
    }
}

class Car extends Vehicle {
    private int trunk;

    Car(String plate, int trunk) {
        super(plate, 4);
        this.trunk = trunk;
    }

    public String describe() {
        return plate + " has " + wheels + " wheels and trunk " + trunk + "L";
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
```

---

<a id="day4"></a>
## Day 4 — Abstract Classes and Interfaces

**Learning objectives:** Decide between abstract classes and interfaces, implement `default` and `static` interface methods, and program against abstractions.

### Question 1: Mixed Real-World Scenario
**Title:** Checkout Payment Methods

**Problem statement:** An online store accepts three payment methods, all implementing an interface `PaymentMethod` with `boolean pay(int amount)` and `String name()`:
- `CARD`: fee is 2% of the amount (integer division); a single payment may not exceed 20000 (the amount itself, before the fee). Charged amount is `amount + fee`.
- `WALLET`: starts with a balance of 1000; the payment is declined if `amount` exceeds the current balance; on success the balance goes down by `amount`. No fee. The balance persists across requests.
- `UPI`: no fee; a single payment may not exceed 5000.

Any request with `amount <= 0` is declined. Include a `default` method in the interface that formats the result line.

**Real-world context:** Checkout code should work with "a payment method" without knowing which one it is.

**Input requirements:** Line 1: `n`. Next `n` lines: `METHOD amount`.

**Expected output:** `<METHOD> <amount> OK <charged>` or `<METHOD> <amount> DECLINED`.

**Sample cases:**
```
Input:
5
CARD 1000
WALLET 700
WALLET 400
UPI 6000
UPI 5000
Output:
CARD 1000 OK 1020
WALLET 700 OK 700
WALLET 400 DECLINED
UPI 6000 DECLINED
UPI 5000 OK 5000
```
```
Input:
3
CARD 20000
CARD 20001
WALLET 1000
Output:
CARD 20000 OK 20400
CARD 20001 DECLINED
WALLET 1000 OK 1000
```

**Constraints:** `1 <= n <= 1000`; amounts fit in `int`. Edge cases: amount `0`; wallet balance reaching exactly `0`.

### Question 2: Interview-Based Coding
**Title:** Shape Areas through an Abstract Class

**Problem statement:** Create an abstract class `Shape` with abstract methods `area()` and `name()`. Implement `Circle` (radius), `Rectangle` (width, height), and `Triangle` (three sides; area by Heron's formula). A triangle whose sides violate the triangle inequality (or whose area would be zero) is invalid: print `Triangle invalid` for it and exclude it from all totals. Read all shapes, print each area, then the total area and the name of the shape with the largest area (the earliest one if tied).

**Input and output requirements:** Line 1: `n`. Next `n` lines: `C r`, `R w h`, or `T a b c` (doubles). Output per valid shape: `<Name> area=<area>` (2 decimals). Then `Total area=<sum>` and `Largest: <Name>`. If no valid shape exists, print `No valid shapes` instead of the last two lines.

**Sample cases:**
```
Input:
3
C 5
R 4 6
T 3 4 5
Output:
Circle area=78.54
Rectangle area=24.00
Triangle area=6.00
Total area=108.54
Largest: Circle
```
```
Input:
3
R 2 3
T 5 5 6
T 1 2 10
Output:
Rectangle area=6.00
Triangle area=12.00
Triangle invalid
Total area=18.00
Largest: Triangle
```

**Constraints:** `1 <= n <= 200`; all dimensions are positive. Use `Math.PI`.

### Question 3: Company-Style Programming
**Title:** Text Transformation Pipeline

**Problem statement:** Define an interface `TextTransformer` with `String apply(String s)` and implement these transformers as separate classes: `UPPER`, `LOWER`, `REVERSE`, `TRIM`, `DEDUPE_SPACES` (replace every run of spaces with a single space), and `CAPITALIZE` (first letter of each space-separated word upper case, the rest lower case). Apply a requested sequence of transformers to a text, in order, printing the text in square brackets after every step.

**Input and output requirements:** Line 1: the text (may have leading, trailing, and repeated spaces; read the whole line). Line 2: `k`. Next `k` lines: a transformer name. Output `<NAME>: [<text after the step>]` for each step.

**Sample cases:**
```
Input:
  hello   big world 
3
TRIM
DEDUPE_SPACES
CAPITALIZE
Output:
TRIM: [hello   big world]
DEDUPE_SPACES: [hello big world]
CAPITALIZE: [Hello Big World]
```
```
Input:
Java Is Fun
2
LOWER
REVERSE
Output:
LOWER: [java is fun]
REVERSE: [nuf si avaj]
```

**Constraints and edge cases:** Text length `0` to `200` characters; `1 <= k <= 20`; names are valid. An empty text must work. Transformers must be created without a long `if`/`switch` ladder inside the loop that applies them (use a lookup of your choice).

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
interface Greeter {
    String name();

    default String greet() {
        return "Hello, " + name();
    }

    static String shout(String s) {
        return s.toUpperCase() + "!";
    }
}

abstract class Person implements Greeter {
    protected String n;

    Person(String n) {
        this.n = n;
        init();
    }

    abstract void init();

    public String name() {
        return n;
    }
}

class Student extends Person {
    int roll = 7;

    Student(String n) {
        super(n);
        roll++;
    }

    void init() {
        roll = 100;
        System.out.println("init " + roll);
    }

    @Override
    public String greet() {
        return super.greet() + " (roll " + roll + ")";
    }
}

public class Main {
    public static void main(String[] args) {
        Greeter g = new Student("Ana");
        System.out.println(g.greet());
        System.out.println(Greeter.shout(g.name()));
        Person p = (Person) g;
        System.out.println(p instanceof Greeter);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** `Shape` is an abstract class that requires an `area()` method and implements `Printable`. A `Square` of side 3 and a `Circle` of radius 1 (use 3.14 for pi) should each print themselves, producing:
```
Square 9.0
Circle 3.14
```

The code below has at least five bugs. Identify each one, explain why the compiler (or the design) rejects it, and give the corrected program.

```java
interface Printable {
    void print();
}

abstract class Shape implements Printable {
    abstract double area() {
        return 0;
    }

    public String toString() {
        return getClass().getSimpleName() + " " + area();
    }
}

class Square extends Shape {
    private double side;

    Square(double side) {
        this.side = side;
    }

    double area() {
        return side * side;
    }

    void print() {
        System.out.println(this);
    }
}

class Circle extends Shape {
    private double r;

    Circle(double r) {
        this.r = r;
    }

    double area() {
        return 3.14 * r * r;
    }
}

public class Main {
    public static void main(String[] args) {
        Shape s = new Shape();
        Printable c = new Circle(1);
        s.print();
        c.print();
    }
}
```

---

<a id="day5"></a>
## Day 5 — Polymorphism and Dynamic Dispatch

**Learning objectives:** Use superclass references to call subclass behavior, distinguish overloading (compile time) from overriding (run time), and use `instanceof` safely.

### Question 1: Mixed Real-World Scenario
**Title:** Ride-Hailing Fare Estimator

**Problem statement:** A ride-hailing app has an abstract class `Ride` with an abstract `baseFare(km, minutes)` and concrete subclasses:
- `BIKE`: `10 + 6 * km`, minimum fare 25
- `AUTO`: `20 + 10 * km + 1 * minutes`, minimum fare 40
- `CAB`: `50 + 14 * km + 2 * minutes`, minimum fare 100

The minimum fare is applied first. Then a surge percentage `s` is applied to every fare: `fare * (100 + s) / 100` (integer division). Process all rides through `Ride` references.

**Real-world context:** Dispatch software treats every ride the same way while pricing differs per vehicle type.

**Input requirements:** Line 1: surge percent `s`. Line 2: `n`. Next `n` lines: `TYPE km minutes`.

**Expected output:** `<TYPE> <fare>` for each ride, then `Total: <sum>`.

**Sample cases:**
```
Input:
50
3
BIKE 5 10
AUTO 10 25
CAB 3 8
Output:
BIKE 60
AUTO 217
CAB 162
Total: 439
```
```
Input:
0
2
BIKE 1 1
CAB 1 1
Output:
BIKE 25
CAB 100
Total: 125
```

**Constraints:** `0 <= s <= 300`; `1 <= n <= 1000`; `km` and `minutes` are positive and at most 1000. Edge case: a ride whose computed fare is below the minimum, with and without surge.

### Question 2: Interview-Based Coding
**Title:** Postfix Expression Objects

**Problem statement:** Build an expression from a postfix (reverse Polish) line using polymorphic classes: an interface `Expr` with `int eval()` and `String toString()`; implementations `Num`, `Add`, `Sub`, `Mul`, and `Neg`. `toString` must fully parenthesize binary operations as `(left op right)` and unary negation as `(-x)`. Tokens are integers, `+`, `-`, `*`, and `~` (unary negation, takes one operand). Use a stack of `Expr` objects while scanning.

**Input and output requirements:** One line of space-separated tokens (a valid expression). Print `<infix> = <value>`.

**Sample cases:**
```
Input:
3 4 + 2 *
Output:
((3 + 4) * 2) = 14
```
```
Input:
5 ~ 2 -
Output:
((-5) - 2) = -7
```

**Constraints:** At most 100 tokens; integer literals are non-negative; every intermediate value fits in `int`. Edge cases: a single number; `~ ~` applied twice (`4 ~ ~` prints `(-(-4)) = 4`).

### Question 3: Company-Style Programming
**Title:** Discount Rules Engine

**Problem statement:** An order system applies several discount rules, each a class implementing `DiscountRule` with `String name()` and `int discount(int total, String customerType, String coupon)`. All discounts are computed on the original total using integer division:
- `BULK10`: 10% of the total if `total >= 1000`, otherwise 0
- `COUPON`: flat 100 if the coupon is `SAVE100` (and 0 otherwise)
- `GOLD5`: 5% of the total if `customerType` is `GOLD`, otherwise 0

The rules are applied in that order. The combined discount is capped at 30% of the total (`total * 30 / 100`). Print each rule's amount (before capping), the final discount, and the payable amount.

**Input and output requirements:** One line: `total customerType coupon` (`customerType` is `REGULAR` or `GOLD`; `coupon` is a code or `NONE`). Output:
```
BULK10 <amount>
COUPON <amount>
GOLD5 <amount>
Discount: <final discount>
Payable: <total - final discount>
```

**Sample cases:**
```
Input:
1200 GOLD SAVE100
Output:
BULK10 120
COUPON 100
GOLD5 60
Discount: 280
Payable: 920
```
```
Input:
300 GOLD SAVE100
Output:
BULK10 0
COUPON 100
GOLD5 15
Discount: 90
Payable: 210
```

**Constraints and edge cases:** `1 <= total <= 1000000`. Test a total of exactly 1000, no applicable rule at all, and a case where the cap applies. Adding a new rule must not require changing the code that applies the rules.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program. Remember which decisions are made at compile time and which at run time.

```java
class A {
    String name = "A-field";

    String who() {
        return "A.who";
    }

    String call(Object o) {
        return "A.call(Object)";
    }

    String call(String s) {
        return "A.call(String)";
    }

    static String st() {
        return "A.st";
    }
}

class B extends A {
    String name = "B-field";

    @Override
    String who() {
        return "B.who";
    }

    @Override
    String call(Object o) {
        return "B.call(Object)";
    }

    static String st() {
        return "B.st";
    }
}

public class Main {
    public static void main(String[] args) {
        A x = new B();
        Object o = "text";
        System.out.println(x.name);
        System.out.println(((B) x).name);
        System.out.println(x.who());
        System.out.println(x.call("hi"));
        System.out.println(x.call(o));
        System.out.println(x.call(5));
        System.out.println(x.st());
    }
}
```

### Question 5: Debug the Code
**What the program should do:** For an array holding a `Dog`, a `Cat`, and another `Dog`, print each animal's sound (`Woof`, `Meow`, `Woof`); only dogs should also print `Fetching`. Then `Dog.describe(Animal)` must print `Dog meets dog` when the other animal is a dog and `Dog meets other animal` otherwise, so the last two lines should be `Dog meets dog` and `Dog meets other animal`.

The code below crashes at run time and also misbehaves. Find every bug, explain it, and write the corrected program.

```java
class Animal {
    String sound() {
        return "...";
    }

    String describe(Animal other) {
        return "Animal meets animal";
    }
}

class Dog extends Animal {
    String sound() {
        return "Woof";
    }

    String describe(Dog other) {
        return "Dog meets dog";
    }

    void fetch() {
        System.out.println("Fetching");
    }
}

class Cat extends Animal {
    String sound() {
        return "Meow";
    }
}

public class Main {
    public static void main(String[] args) {
        Animal[] zoo = { new Dog(), new Cat(), new Dog() };
        for (Animal a : zoo) {
            System.out.println(a.sound());
            Dog d = (Dog) a;
            d.fetch();
        }
        Animal first = zoo[0];
        System.out.println(first.describe(zoo[2]));
        System.out.println(first.describe(zoo[1]));
    }
}
```

---

<a id="day6"></a>
## Day 6 — equals, hashCode, and toString

**Learning objectives:** Implement the `equals`/`hashCode` contract, understand how hash-based collections use them, write useful `toString` methods, and recognize the dangers of mutable keys.

### Question 1: Mixed Real-World Scenario
**Title:** Duplicate Customer Detection

**Problem statement:** An e-commerce company merges customer lists from two systems. Two customers are the same person if their e-mail addresses are equal ignoring case; the name does not matter. Write a `Customer` class with `name` and `email`, a correct `equals` and `hashCode` based on the lower-cased e-mail, and a `toString` that returns `<name> <email in lower case>` with the e-mail in angle brackets (for example `Asha <asha@mail.com>`). Use a `HashSet` to count unique customers and report the first occurrence of each, in arrival order.

**Real-world context:** Duplicate customer records lead to double marketing mails and wrong sales statistics.

**Input requirements:** Line 1: `n`. Next `n` lines: `name email` (the name contains no spaces).

**Expected output:** `Unique customers: <k>` followed by the first-occurrence customers in arrival order, one per line, using `toString`.

**Sample cases:**
```
Input:
4
Asha Asha@Mail.com
Ravi ravi@mail.com
Kumar asha@mail.com
Meena meena@mail.com
Output:
Unique customers: 3
Asha <asha@mail.com>
Ravi <ravi@mail.com>
Meena <meena@mail.com>
```
```
Input:
2
A a@x.com
B A@X.COM
Output:
Unique customers: 1
A <a@x.com>
```

**Constraints:** `1 <= n <= 10000`; e-mails have no spaces. `equals` must handle `null` and objects of other classes without throwing.

### Question 2: Interview-Based Coding
**Title:** Robot Path with a Custom Point Key

**Problem statement:** A robot starts at `(0, 0)` and follows a string of moves `U` (y+1), `D` (y-1), `L` (x-1), `R` (x+1). Write your own immutable `Point` class (do not use `record`) with hand-written `equals` and `hashCode`, and use it as a `HashMap` key to count how many times each cell is visited. The starting cell counts as one visit.

**Input and output requirements:** One line with the moves (it may be empty). Output three lines: `Distinct: <number of different cells visited>`, `Revisited: <number of cells visited at least twice>`, `Max visits: <highest visit count of any cell>`.

**Sample cases:**
```
Input:
UDUD
Output:
Distinct: 2
Revisited: 2
Max visits: 3
```
```
Input:
RRU
Output:
Distinct: 4
Revisited: 0
Max visits: 1
```

**Constraints:** Move string length `0` to `100000`. Edge cases: empty string (the output is `Distinct: 1`, `Revisited: 0`, `Max visits: 1`); a path that returns to the origin; very long straight lines (no hash-collision shortcuts such as `x * 1000 + y`).

### Question 3: Company-Style Programming
**Title:** Money Value Object

**Problem statement:** Implement an immutable `Money` class holding a currency code and an amount stored as a `long` number of minor units (hundredths). Required behavior: `add` and `subtract` (throw `IllegalArgumentException` for different currencies), `equals`/`hashCode` (same currency and same amount), and `toString` in the form `CUR x.yy` (always two decimals; negative amounts as `CUR -2.50`). The program evaluates lines of the form `CUR amount op CUR amount`, where `op` is `+`, `-`, or `==`.

**Input and output requirements:** Line 1: `n`. Next `n` lines: `CUR amount op CUR amount`. Amounts are decimal numbers with 0, 1, or 2 digits after the point (parse them without using `double`). For `+` and `-` print the resulting `Money`, or `Currency mismatch` if the currencies differ. For `==` print `true` or `false`.

**Sample cases:**
```
Input:
4
INR 10.50 + INR 2.25
INR 1 == INR 1.00
INR 5 - INR 7.5
USD 1 + INR 1
Output:
INR 12.75
true
INR -2.50
Currency mismatch
```
```
Input:
3
USD 0.99 + USD 0.01
USD 2 == INR 2
EUR 100 - EUR 100
Output:
USD 1.00
false
EUR 0.00
```

**Constraints and edge cases:** `1 <= n <= 1000`; amounts are non-negative in the input and below `10^9`. Make sure `INR 0.05 - INR 0.10` prints `INR -0.05` (the sign must not be lost when the whole part is zero), and that `7.5` means 750 minor units, not 705.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.*;

class P {
    int x;

    P(int x) {
        this.x = x;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof P && ((P) o).x == x;
    }
}

class Q {
    int x;

    Q(int x) {
        this.x = x;
    }

    @Override
    public int hashCode() {
        return x % 3;
    }
}

class R {
    int x;

    R(int x) {
        this.x = x;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof R && ((R) o).x == x;
    }

    @Override
    public int hashCode() {
        return x % 3;
    }

    @Override
    public String toString() {
        return "R" + x;
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println(new P(1).equals(new P(1)));
        System.out.println(new Q(1).equals(new Q(1)));
        System.out.println(new Q(1).hashCode() == new Q(4).hashCode());
        Set<R> set = new HashSet<>();
        set.add(new R(1));
        set.add(new R(4));
        set.add(new R(1));
        set.add(new R(7));
        System.out.println(set.size());
        System.out.println(set.contains(new R(4)));
        System.out.println(set.contains(new R(10)));
        List<R> list = new ArrayList<>();
        list.add(new R(2));
        System.out.println(list.contains(new R(2)) + " " + list.indexOf(new R(5)));
        System.out.println(list);
        R r = new R(9);
        set.add(r);
        r.x = 10;
        System.out.println(set.contains(r) + " " + set.contains(new R(9)));
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Two `Student` objects are equal when their ids are equal. A `HashSet` must therefore keep only 2 of the 3 students added. The program should print `2`, then `true`, then `Student[id=3, name=Meena]`.

The code below has at least four bugs (compile-time and logic). Identify each one, explain it, and give the corrected program.

```java
import java.util.*;

class Student {
    private int id;
    private String name;

    Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public boolean equals(Student other) {
        return this.id == other.id;
    }

    public int hashcode() {
        return id;
    }

    public String toString() {
        System.out.println("Student[id=" + id + ", name=" + name + "]");
    }
}

public class Main {
    public static void main(String[] args) {
        Set<Student> set = new HashSet<>();
        set.add(new Student(1, "Asha"));
        set.add(new Student(2, "Ravi"));
        set.add(new Student(1, "Asha K"));
        System.out.println(set.size());
        Object a = new Student(1, "X");
        Object b = new Student(1, "Y");
        System.out.println(a.equals(b));
        Student s = new Student(3, "Meena");
        System.out.println(s);
    }
}
```

---

<a id="day7"></a>
## Day 7 — Enums and OOP Review

**Learning objectives:** Model fixed sets of values with enums (fields, constructors, methods, constant-specific bodies), use `valueOf`, `values`, `EnumMap`, and combine Week 1 concepts.

### Question 1: Mixed Real-World Scenario
**Title:** Order Status Workflow

**Problem statement:** An online order moves through the statuses `NEW`, `PAID`, `SHIPPED`, `DELIVERED`, and `CANCELLED`. Every order starts as `NEW`. Allowed transitions are: `NEW -> PAID`, `NEW -> CANCELLED`, `PAID -> SHIPPED`, `PAID -> CANCELLED`, and `SHIPPED -> DELIVERED`. Everything else is invalid and leaves the order unchanged. Put the transition rule inside the enum itself (for example a method `canMoveTo(Status next)`).

**Real-world context:** Order systems must prevent impossible states, such as shipping an order that was cancelled.

**Input requirements:** Line 1: `n`. Next `n` lines: `orderId STATUS` (a request to move that order to `STATUS`). An order is created as `NEW` the first time its id appears.

**Expected output:** For each request: `<id>: <OLD> -> <NEW>` if it succeeded, otherwise `<id>: INVALID <OLD> -> <REQUESTED>`. After all requests, print each order's final status as `<id> <STATUS>`, in order of first appearance.

**Sample cases:**
```
Input:
5
A1 PAID
A2 CANCELLED
A1 DELIVERED
A1 SHIPPED
A2 PAID
Output:
A1: NEW -> PAID
A2: NEW -> CANCELLED
A1: INVALID PAID -> DELIVERED
A1: PAID -> SHIPPED
A2: INVALID CANCELLED -> PAID
A1 SHIPPED
A2 CANCELLED
```
```
Input:
3
X NEW
X PAID
X PAID
Output:
X: INVALID NEW -> NEW
X: NEW -> PAID
X: INVALID PAID -> PAID
X PAID
```

**Constraints:** `1 <= n <= 1000`; ids contain no spaces; status names are always valid enum names.

### Question 2: Interview-Based Coding
**Title:** Calculator Using an Operator Enum

**Problem statement:** Implement a calculator whose operators are an `enum Op` with the constants `ADD("+")`, `SUB("-")`, `MUL("*")`, `DIV("/")`, and `MOD("%")`. Each constant holds its symbol and its own `apply(int a, int b)` implementation (constant-specific bodies or an abstract method). Provide a static lookup from symbol to constant. Integer division and remainder follow Java rules (truncation toward zero).

**Input and output requirements:** Line 1: `n`. Next `n` lines: `a op b`. Print the result, or `Division by zero` when `op` is `/` or `%` and `b` is `0`, or `Unknown operator` when the symbol is not supported.

**Sample cases:**
```
Input:
5
8 + 3
8 - 10
7 / 2
7 / 0
4 ^ 2
Output:
11
-2
3
Division by zero
Unknown operator
```
```
Input:
3
-7 % 3
6 * -4
-9 / 2
Output:
-1
-24
-4
```

**Constraints:** `1 <= n <= 1000`; operands fit in `int` and results do not overflow. The code that evaluates a line must not contain a `switch` or `if` chain over the operator symbols.

### Question 3: Company-Style Programming
**Title:** Shift Pay with Enum Multipliers

**Problem statement:** A staffing agency pays an hourly rate multiplied by a shift factor: `MORNING` 100%, `EVENING` 125%, `NIGHT` 150%. Work on `SAT` or `SUN` adds 20 percentage points to the factor (so a Saturday night is 170%). Model days and shifts as enums, with the weekend test and the percentage living inside the enums. Pay for a line is `rate * hours * factor / 100` using integer division.

**Input and output requirements:** Line 1: hourly `rate` (int). Line 2: `n`. Next `n` lines: `DAY SHIFT hours` (`DAY` is `MON`...`SUN`). Print `<DAY> <SHIFT> <pay>` per line, then `Total: <sum>`, then `Hours: MORNING=<h> EVENING=<h> NIGHT=<h>` showing the total hours per shift in declaration order (use an `EnumMap`).

**Sample cases:**
```
Input:
200
3
MON MORNING 8
SAT NIGHT 6
SUN EVENING 4
Output:
MON MORNING 1600
SAT NIGHT 2040
SUN EVENING 1160
Total: 4800
Hours: MORNING=8 EVENING=4 NIGHT=6
```
```
Input:
155
2
TUE NIGHT 3
FRI EVENING 5
Output:
TUE NIGHT 697
FRI EVENING 968
Total: 1665
Hours: MORNING=0 EVENING=5 NIGHT=3
```

**Constraints and edge cases:** `1 <= rate <= 10000`; `1 <= hours <= 24`; `1 <= n <= 500`. A shift that never appears must still be printed with `0` hours.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
enum Level {
    LOW(1),
    MEDIUM(5),
    HIGH(10) {
        @Override
        String label() {
            return "!!" + super.label();
        }
    };

    private final int weight;

    Level(int w) {
        weight = w;
        System.out.println("init " + name());
    }

    String label() {
        return name().toLowerCase() + weight;
    }

    int weight() {
        return weight;
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("start");
        Level l = Level.valueOf("MEDIUM");
        System.out.println(l.label());
        System.out.println(Level.HIGH.label());
        System.out.println(l.compareTo(Level.HIGH));
        System.out.println(l.ordinal() + Level.values().length);
        System.out.println(Level.HIGH.getClass() == Level.class);
        System.out.println(Level.HIGH.getDeclaringClass() == Level.class);
        switch (l) {
            case LOW:
                System.out.println("low");
            case MEDIUM:
                System.out.println("medium");
            case HIGH:
                System.out.println("high");
                break;
            default:
                System.out.println("none");
        }
    }
}
```

### Question 5: Debug the Code
**What the program should do:** A coffee shop enum `Size` has three constants with prices 100, 150, and 200. The program looks up the size named `"MEDIUM"` and should print `Medium cup` and then `Price 150`.

The code below has at least five bugs. Identify each, explain why it fails, and give the corrected program.

```java
enum Size {
    SMALL(100), MEDIUM(150), LARGE(200)

    private int price;

    public Size(int price) {
        this.price = price;
    }

    int getPrice() {
        return price;
    }
}

public class Main {
    public static void main(String[] args) {
        Size s = new Size(120);
        Size chosen = Size.valueOf("medium");
        switch (chosen) {
            case Size.SMALL:
                System.out.println("Small cup");
                break;
            case Size.MEDIUM:
                System.out.println("Medium cup");
                break;
            default:
                System.out.println("Large cup");
        }
        if (chosen == "MEDIUM") {
            System.out.println("Price " + chosen.getPrice());
        }
    }
}
```

---

<a id="day8"></a>
## Day 8 — List Variants and Iterator

**Learning objectives:** Choose between `ArrayList` and `LinkedList`, use `Deque`-style methods on `LinkedList`, remove safely while iterating with `Iterator` and `ListIterator`, and avoid the `remove(int)` / `remove(Object)` trap.

### Question 1: Mixed Real-World Scenario
**Title:** Music Play Queue

**Problem statement:** A music app keeps an upcoming-songs queue in a `LinkedList<String>`. Support these commands:
- `ADD title` appends a song (no output)
- `PLAYNEXT title` inserts a song at the front (no output)
- `PLAY` removes the front song and prints `Playing <title>`, or `Nothing to play` if the queue is empty
- `REMOVE idx` removes the song at 0-based index `idx` and prints `Removed <title>`, or `Invalid index`
- `SHOW` prints `Queue: <titles separated by ", ">`, or `Queue: empty`

**Real-world context:** Streaming apps let users queue songs, jump the queue, and remove entries.

**Input requirements:** Line 1: `n`. Next `n` lines: a command. Titles are single words.

**Expected output:** Only the outputs listed above, in order.

**Sample cases:**
```
Input:
7
ADD Alpha
ADD Beta
PLAYNEXT Gamma
SHOW
PLAY
REMOVE 1
SHOW
Output:
Queue: Gamma, Alpha, Beta
Playing Gamma
Removed Beta
Queue: Alpha
```
```
Input:
4
PLAY
REMOVE 0
ADD Solo
SHOW
Output:
Nothing to play
Invalid index
Queue: Solo
```

**Constraints:** `1 <= n <= 1000`. Edge cases: negative index; index equal to the queue size; the same title queued twice.

### Question 2: Interview-Based Coding
**Title:** Filter and Transform with Iterators

**Problem statement:** Given a list of integers and a number `k`, do two passes **in place** on one `ArrayList<Integer>`, without building a second list:
1. Using an `Iterator`, remove every element divisible by `k`.
2. Using a `ListIterator`, replace each remaining element `x` with `2 * x`, and if the new value is greater than `20`, insert `-1` immediately after it (the inserted `-1` must not be processed again).

**Input and output requirements:** Line 1: `n`. Line 2: `n` integers. Line 3: `k`. Output the final list on the first line (`[a, b, c]` format) and `Removed: <count>` on the second.

**Sample cases:**
```
Input:
6
3 8 11 12 7 6
3
Output:
[16, 22, -1, 14]
Removed: 3
```
```
Input:
3
2 4 6
2
Output:
[]
Removed: 3
```

**Constraints:** `1 <= n <= 1000`; `1 <= k <= 100`; elements may be negative or zero (zero is divisible by every `k`). Do not call `list.remove` or `list.add` directly inside the loops.

### Question 3: Company-Style Programming
**Title:** The Circle of Elimination

**Problem statement:** `n` people numbered `1..n` stand in a circle. Starting the count at person 1, every `k`-th person still in the circle is eliminated and counting resumes from the next person. Output the elimination order and the survivor. Implement it with a list and index arithmetic (no manual simulation of every step with a loop that counts one person at a time).

**Input and output requirements:** One line: `n k`. Output two lines: `Order:` followed by each eliminated number preceded by one space (so for `n = 1` the line is just `Order:`), and `Survivor: <number>`.

**Sample cases:**
```
Input:
7 3
Output:
Order: 3 6 2 7 5 1
Survivor: 4
```
```
Input:
5 2
Output:
Order: 2 4 1 5
Survivor: 3
```

**Constraints and edge cases:** `1 <= n <= 5000`; `1 <= k <= 10^6`. Test `k = 1`, `n = 1`, and `k` larger than the current circle size.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>(Arrays.asList(10, 20, 30, 40, 50));
        list.remove(1);
        list.remove(Integer.valueOf(40));
        System.out.println(list);

        list.add(1, 99);
        list.set(0, list.get(0) + 1);
        System.out.println(list + " " + list.indexOf(99));

        List<Integer> sub = list.subList(1, 3);
        sub.set(0, -1);
        sub.add(7);
        System.out.println(list);
        System.out.println(sub);

        LinkedList<String> ll = new LinkedList<>(Arrays.asList("a", "b", "c"));
        ll.addFirst("x");
        ll.removeLast();
        ll.add(2, "m");
        System.out.println(ll + " " + ll.peekFirst() + " " + ll.pollLast());

        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            if (it.next() % 2 == 1) {
                it.remove();
            }
        }
        System.out.println(list);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** (1) Remove every `"spam"` from the word list, printing `[ok, fine, good]`. (2) Remove the **value** `3` (first occurrence only) from `[5, 3, 8, 3, 1]`, printing `[5, 8, 3, 1]`. (3) Print every element of that list, one per line.

The code below has at least three bugs, one of which throws an exception on the first loop. Identify each bug, explain it, and give the corrected program.

```java
import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<String> words = new ArrayList<>(Arrays.asList("ok", "spam", "fine", "spam", "good"));
        for (String w : words) {
            if (w.equals("spam")) {
                words.remove(w);
            }
        }
        System.out.println(words);

        List<Integer> nums = new ArrayList<>(Arrays.asList(5, 3, 8, 3, 1));
        nums.remove(3);
        System.out.println(nums);

        for (int i = 0; i <= nums.size(); i++) {
            System.out.println(nums.get(i));
        }
    }
}
```

---

<a id="day9"></a>
## Day 9 — Set Variants

**Learning objectives:** Use `HashSet`, `LinkedHashSet`, and `TreeSet` and know what ordering each guarantees; perform union, intersection, and difference; use `TreeSet` navigation methods; solve problems that become simple with a set.

### Question 1: Mixed Real-World Scenario
**Title:** Conference Attendance Overlap

**Problem statement:** A conference runs for two days. You get the list of badge names scanned on each day (a person may be scanned several times in one day). Report who attended both days, who attended only day 1, who attended only day 2, and the number of distinct people overall. Names in each output line must be in alphabetical order.

**Real-world context:** Organizers use attendance overlap to plan catering and certificates.

**Input requirements:** Line 1: `a`, the number of scans on day 1. Line 2: `a` names. Line 3: `b`. Line 4: `b` names. (Names are lower-case single words.)

**Expected output:**
```
Both: <names or none>
Only Day 1: <names or none>
Only Day 2: <names or none>
Total unique: <count>
```
Names are separated by single spaces.

**Sample cases:**
```
Input:
4
asha ravi meena joy
3
ravi joy kim
Output:
Both: joy ravi
Only Day 1: asha meena
Only Day 2: kim
Total unique: 5
```
```
Input:
3
a b a
2
c d
Output:
Both: none
Only Day 1: a b
Only Day 2: c d
Total unique: 4
```

**Constraints:** `1 <= a, b <= 10000`. Edge cases: identical lists; one list entirely contained in the other.

### Question 2: Interview-Based Coding
**Title:** Longest Consecutive Run

**Problem statement:** Given an unsorted array of integers, find the length of the longest sequence of values that are consecutive integers (such as `1, 2, 3, 4`), regardless of their positions in the array. Duplicates count once. Your solution should run in expected `O(n)` time (sorting the array is not allowed).

**Input and output requirements:** Line 1: `n`. Line 2: `n` integers. Print a single integer.

**Sample cases:**
```
Input:
6
100 4 200 1 3 2
Output:
4
```
```
Input:
6
-1 0 1 2 10 11
Output:
4
```

**Constraints and edge cases:** `1 <= n <= 100000`; values between `-10^9` and `10^9` (watch for overflow when adding 1). Test `n = 1`, all-equal values (`5 5 5` gives `1`), and no two values consecutive.

### Question 3: Company-Style Programming
**Title:** Suspicious Login Audit

**Problem statement:** A security team reviews login logs. Each log line has a user and an IP address. For each user, in order of the user's first appearance, report how many **distinct** IP addresses they logged in from. A user with 3 or more distinct IPs is flagged `SUSPICIOUS`, otherwise `OK`.

**Input and output requirements:** Line 1: `n`. Next `n` lines: `user ip`. Output one line per user: `<user> <distinctIpCount> <SUSPICIOUS|OK>`.

**Sample cases:**
```
Input:
6
asha 10.0.0.1
ravi 10.0.0.2
asha 10.0.0.2
asha 10.0.0.1
asha 10.0.0.3
ravi 10.0.0.2
Output:
asha 3 SUSPICIOUS
ravi 1 OK
```
```
Input:
3
kim a
kim b
lee a
Output:
kim 2 OK
lee 1 OK
```

**Constraints and edge cases:** `1 <= n <= 100000`; users and IPs are single tokens. The output order must be deterministic (first appearance), so choose your collections accordingly.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Set<Integer> a = new TreeSet<>(Arrays.asList(5, 1, 9, 3, 7));
        Set<Integer> b = new LinkedHashSet<>(Arrays.asList(9, 2, 5, 2, 8));
        System.out.println(a + " " + b);
        System.out.println(a.add(3) + " " + b.add(4) + " " + a.remove(2));

        Set<Integer> c = new TreeSet<>(a);
        c.retainAll(b);
        System.out.println(c);

        b.removeAll(a);
        System.out.println(b);

        TreeSet<Integer> t = new TreeSet<>(a);
        System.out.println(t.first() + " " + t.last() + " " + t.floor(6) + " "
                + t.ceiling(6) + " " + t.higher(9) + " " + t.lower(1));
        System.out.println(t.headSet(5) + " " + t.tailSet(5) + " " + t.descendingSet());
        System.out.println(a.containsAll(c) + " " + c.equals(new HashSet<>(Arrays.asList(9, 5))));
    }
}
```

### Question 5: Debug the Code
**What the program should do:** For the text `"Apple banana apple Cherry BANANA date"`, treating words case-insensitively, print the number of unique words (`4`), the number of duplicate words that were dropped (`2`), the unique words in alphabetical order in lower case (one per line), and finally the distinct letters of `"banana"` in sorted order as `[a, b, n]`.

The code below has at least four bugs. Identify each one, explain it, and give the corrected program.

```java
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String text = "Apple banana apple Cherry BANANA date";
        String[] words = text.split(" ");
        Set<String> seen = new HashSet<>();
        for (String w : words) {
            seen.add(w);
        }
        System.out.println("Unique: " + seen.size());
        int duplicates = seen.size() - words.length;
        System.out.println("Duplicates: " + duplicates);

        Set<String> sorted = new TreeSet<>(seen);
        for (String w : sorted) {
            System.out.println(w);
        }

        Set<char> letters = new TreeSet<>();
        for (char ch : "banana".toCharArray()) {
            letters.add(ch);
        }
        System.out.println(letters);
    }
}
```

---

<a id="day10"></a>
## Day 10 — Map Variants

**Learning objectives:** Use `HashMap`, `LinkedHashMap`, and `TreeMap`; apply `merge`, `getOrDefault`, `computeIfAbsent`, `putIfAbsent`; iterate over entries; and use maps to turn quadratic scans into linear ones.

### Question 1: Mixed Real-World Scenario
**Title:** Warehouse Stock Ledger

**Problem statement:** A warehouse tracks stock per SKU in a `TreeMap<String, Integer>` so reports come out sorted. Commands:
- `RECEIVE sku qty` adds stock (no output)
- `SHIP sku qty` removes stock and prints `SHIPPED <sku> <qty>`; if the SKU is unknown or has less than `qty`, print `REJECTED <sku>` and change nothing
- `REPORT` prints one line `<sku>: <qty>` per SKU in ascending order; SKUs whose quantity reached 0 must not appear; if nothing is in stock print `(empty)`

**Real-world context:** Inventory systems must never ship stock they do not have.

**Input requirements:** Line 1: `n`. Next `n` lines: commands. Quantities are positive integers.

**Expected output:** As described above.

**Sample cases:**
```
Input:
6
RECEIVE B12 10
RECEIVE A07 5
SHIP B12 4
SHIP A07 9
SHIP C99 1
REPORT
Output:
SHIPPED B12 4
REJECTED A07
REJECTED C99
A07: 5
B12: 6
```
```
Input:
4
RECEIVE X1 3
SHIP X1 3
REPORT
REPORT
Output:
SHIPPED X1 3
(empty)
(empty)
```

**Constraints:** `1 <= n <= 10000`; quantities fit in `int` and totals never overflow. Edge cases: shipping exactly the stock held; receiving the same SKU twice.

### Question 2: Interview-Based Coding
**Title:** Two Sum with Indices

**Problem statement:** Given an array and a target, find two different positions `i < j` such that `a[i] + a[j] = target`. If several pairs exist, report the pair with the smallest `j`, and for that `j` the smallest `i`. Your solution must run in expected linear time.

**Input and output requirements:** Line 1: `n`. Line 2: `n` integers. Line 3: `target`. Print `i j` (0-based) or `No pair`.

**Sample cases:**
```
Input:
4
2 7 11 15
9
Output:
0 1
```
```
Input:
3
3 2 4
6
Output:
1 2
```

**Constraints and edge cases:** `1 <= n <= 100000`; values and target fit in `int`, but sums may not (use `long` for the sum or the complement). Test duplicates (`3 3` with target `6` gives `0 1`), `n = 1` (`No pair`), and negative numbers.

### Question 3: Company-Style Programming
**Title:** Anagram Groups in Input Order

**Problem statement:** Group the given words so that words that are anagrams of one another end up on the same line. Words inside a group keep their input order (duplicates are kept), and groups are printed in the order in which their first word appeared.

**Input and output requirements:** Line 1: `n`. Line 2: `n` lower-case words separated by spaces. Output one line per group, with the words separated by single spaces.

**Sample cases:**
```
Input:
6
eat tea tan ate nat bat
Output:
eat tea ate
tan nat
bat
```
```
Input:
3
abc cab abc
Output:
abc cab abc
```

**Constraints and edge cases:** `1 <= n <= 20000`; each word has `1` to `50` lower-case letters. A word is an anagram of itself; single-word groups are printed too.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Map<String, Integer> m = new TreeMap<>();
        String[] words = {"pear", "apple", "fig", "apple", "pear", "apple"};
        for (String w : words) {
            m.merge(w, 1, Integer::sum);
        }
        System.out.println(m);
        System.out.println(m.getOrDefault("kiwi", -1) + " " + m.get("fig"));
        System.out.println(m.put("fig", 10) + " " + m.put("lime", 4) + " " + m.putIfAbsent("pear", 99));

        m.computeIfPresent("apple", (k, v) -> v * 2);
        m.compute("kiwi", (k, v) -> v == null ? 1 : v + 1);
        m.remove("fig");
        System.out.println(m);

        TreeMap<String, Integer> t = (TreeMap<String, Integer>) m;
        System.out.println(t.firstKey() + " " + t.lastEntry().getValue() + " "
                + t.headMap("lime") + " " + t.ceilingKey("l"));

        Map<Character, String> lh = new LinkedHashMap<>();
        lh.put('z', "1");
        lh.put('a', "2");
        lh.put('z', "3");
        System.out.println(lh + " " + lh.keySet());
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Count votes from the ballots `A, B, A, C, A, B`, add a candidate `D` with 0 votes, remove all candidates who have 0 votes, and print the winner with the vote count (`A 3`). The last line should print `true`, since both `Integer` objects hold the same value.

The code below has at least four bugs (a crash on the first loop, a possible exception in the second, a logic error, and a comparison bug). Identify each, explain it, and give the corrected program.

```java
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Map<String, Integer> votes = new HashMap<>();
        String[] ballots = {"A", "B", "A", "C", "A", "B"};
        for (String b : ballots) {
            int current = votes.get(b);
            votes.put(b, current + 1);
        }

        votes.put("D", 0);
        for (String name : votes.keySet()) {
            if (votes.get(name) == 0) {
                votes.remove(name);
            }
        }

        String winner = null;
        int best = 0;
        for (Map.Entry<String, Integer> e : votes.entrySet()) {
            if (e.getValue() > best) {
                winner = e.getKey();
            }
            best = e.getValue();
        }
        System.out.println(winner + " " + best);

        Integer x = 1000, y = 1000;
        System.out.println(x == y);
    }
}
```

---

<a id="day11"></a>
## Day 11 — Comparable and Comparator

**Learning objectives:** Give classes a natural ordering with `Comparable`, build flexible orderings with `Comparator` (`comparing`, `thenComparing`, `reversed`), understand sort stability, and avoid broken comparators.

### Question 1: Mixed Real-World Scenario
**Title:** Exam Rank List with Ties

**Problem statement:** A school publishes a rank list. Students are ordered by marks (highest first); students with equal marks are ordered by name (A to Z). Ranks use competition ranking: students with the same marks share a rank, and the next rank skips accordingly (`1, 1, 3, 3, 5`). Make `Student` implement `Comparable<Student>` with exactly this order.

**Real-world context:** Rank lists must be deterministic, so two students with equal marks always appear in the same order.

**Input requirements:** Line 1: `n`. Next `n` lines: `name marks` (name is a single word; marks is an integer).

**Expected output:** `n` lines `<rank> <name> <marks>` in rank-list order.

**Sample cases:**
```
Input:
5
Asha 90
Ravi 85
Meena 90
Joy 70
Kim 85
Output:
1 Asha 90
1 Meena 90
3 Kim 85
3 Ravi 85
5 Joy 70
```
```
Input:
2
Zed 50
Amy 60
Output:
1 Amy 60
2 Zed 50
```

**Constraints:** `1 <= n <= 10000`; `0 <= marks <= 1000`; names are distinct. Edge case: every student has the same marks (all rank 1).

### Question 2: Interview-Based Coding
**Title:** Arrange Numbers to Form the Largest Value

**Problem statement:** Given non-negative integers, arrange them in an order so that their concatenation forms the largest possible number. Because the result can be huge, print it as a string. Use a custom `Comparator` on the digit strings. If the result would consist only of zeros, print `0`.

**Input and output requirements:** Line 1: `n`. Line 2: `n` non-negative integers. Print the largest concatenation.

**Sample cases:**
```
Input:
5
3 30 34 5 9
Output:
9534330
```
```
Input:
2
10 2
Output:
210
```

**Constraints and edge cases:** `1 <= n <= 10000`; each value is at most `10^9`. Test `0 0` (prints `0`), `n = 1`, and numbers such as `121 12` (`12121`) where one number is a prefix of the other.

### Question 3: Company-Style Programming
**Title:** Configurable Flight Sorting

**Problem statement:** A flight-search page lets users sort by several criteria in priority order. Each flight has an `id` (string), `price` (int), `duration` in minutes (int), and `dep` departure time as an `HHMM` integer. The sort request is a list of tokens `field:direction` where `field` is `price`, `duration`, `dep`, or `id` and `direction` is `asc` or `desc`. Build one `Comparator<Flight>` dynamically from the tokens and sort. Flights that tie on every listed criterion keep their input order (stable sort).

**Input and output requirements:** Line 1: `n`. Next `n` lines: `id price duration dep`. Last line: the sort tokens separated by spaces. Output the ids in sorted order on one line, separated by single spaces.

**Sample cases:**
```
Input:
4
F1 5000 120 900
F2 4500 150 1000
F3 5000 90 800
F4 4500 150 700
price:asc duration:desc id:asc
Output:
F2 F4 F1 F3
```
```
Input:
4
F1 5000 120 900
F2 4500 150 1000
F3 5000 90 800
F4 4500 150 700
dep:asc
Output:
F4 F3 F1 F2
```

**Constraints and edge cases:** `1 <= n <= 5000`; 1 to 4 tokens. Test a single criterion with ties and `id:desc` (string comparison, so `F10` sorts before `F2` ascending).

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.*;

public class Main {
    static class Item implements Comparable<Item> {
        String name;
        int qty;

        Item(String n, int q) {
            name = n;
            qty = q;
        }

        public int compareTo(Item o) {
            return Integer.compare(qty, o.qty);
        }

        public String toString() {
            return name + qty;
        }
    }

    public static void main(String[] args) {
        List<Item> items = new ArrayList<>();
        items.add(new Item("b", 3));
        items.add(new Item("a", 1));
        items.add(new Item("c", 3));
        items.add(new Item("d", 2));

        Collections.sort(items);
        System.out.println(items);

        items.sort(Comparator.comparing((Item i) -> i.name).reversed());
        System.out.println(items);

        items.sort(Comparator.comparingInt((Item i) -> i.qty).reversed().thenComparing(i -> i.name));
        System.out.println(items);

        String[] s = {"Banana", "apple", "Cherry", "apple2", "B"};
        Arrays.sort(s);
        System.out.println(Arrays.toString(s));
        Arrays.sort(s, String.CASE_INSENSITIVE_ORDER);
        System.out.println(Arrays.toString(s));
        Arrays.sort(s, Comparator.comparing(String::length).thenComparing(Comparator.reverseOrder()));
        System.out.println(Arrays.toString(s));

        System.out.println(Collections.max(items) + " " + Collections.min(items));
    }
}
```

### Question 5: Debug the Code
**What the program should do:** (1) Sort employees by salary ascending, printing `[B, C, A]`. (2) Sort the names `kim, al, joy, zed` by length descending (longest first) and, for equal lengths, alphabetically, printing `[joy, kim, zed, al]`.

The code below has at least four bugs (two of them only show up for particular data). Identify each one, explain it, and give the corrected program.

```java
import java.util.*;

public class Main {
    static class Emp implements Comparable<Emp> {
        String name;
        int salary;

        Emp(String name, int salary) {
            this.name = name;
            this.salary = salary;
        }

        public int compareTo(Emp other) {
            return salary - other.salary;
        }

        public String toString() {
            return name;
        }
    }

    public static void main(String[] args) {
        List<Emp> list = new ArrayList<>();
        list.add(new Emp("A", Integer.MAX_VALUE));
        list.add(new Emp("B", -5));
        list.add(new Emp("C", 100));
        Collections.sort(list);
        System.out.println(list);

        String[] names = {"kim", "al", "joy", "zed"};
        Arrays.sort(names, (x, y) -> {
            if (x.length() != y.length()) {
                return x.length() - y.length();
            }
            return x == y ? 0 : 1;
        });
        System.out.println(Arrays.toString(names));
    }
}
```

---

<a id="day12"></a>
## Day 12 — ArrayDeque and PriorityQueue

**Learning objectives:** Use `ArrayDeque` as a stack and a queue, use `PriorityQueue` with natural and custom ordering, and know that a priority queue's iteration order is not sorted order.

### Question 1: Mixed Real-World Scenario
**Title:** Emergency Room Triage

**Problem statement:** An emergency room treats patients by severity: severity `5` is the most urgent and `1` the least. Among patients of equal severity, the one who arrived first is treated first. Commands:
- `ARRIVE name severity` registers a patient (no output)
- `TREAT` prints `Treating <name>` for the next patient, or `No patients` if nobody is waiting
- `WAITING` prints `Waiting: <count>`

Use a `PriorityQueue` with a comparator that includes the arrival order.

**Real-world context:** Triage systems always call the most critical waiting patient first, but must stay fair among equals.

**Input requirements:** Line 1: `n`. Next `n` lines: a command. Names are single words and may repeat.

**Expected output:** The outputs described above, in order.

**Sample cases:**
```
Input:
7
ARRIVE Asha 2
ARRIVE Ravi 5
ARRIVE Meena 5
TREAT
TREAT
WAITING
TREAT
Output:
Treating Ravi
Treating Meena
Waiting: 1
Treating Asha
```
```
Input:
3
TREAT
ARRIVE Kim 1
TREAT
Output:
No patients
Treating Kim
```

**Constraints:** `1 <= n <= 100000`; `1 <= severity <= 5`. Edge cases: `TREAT` on an empty queue; two patients with the same name.

### Question 2: Interview-Based Coding
**Title:** Balanced Brackets with Error Position

**Problem statement:** A string contains only the characters `( ) [ ] { }`. Decide whether it is balanced. If it is not, report the 1-based position of the first problem: the first closing bracket that has no matching opener at the top of the stack (either the stack is empty or the opener is of another type); if no closing bracket is wrong but some openers are never closed, the position of the most recently opened unclosed bracket. Use an `ArrayDeque` as the stack.

**Input and output requirements:** Line 1: `n`. Next `n` lines: one bracket string each (a line may be empty). Print `Balanced` or `Unbalanced at <position>` for each.

**Sample cases:**
```
Input:
4
{[()]}
([)]
((
)
Output:
Balanced
Unbalanced at 3
Unbalanced at 2
Unbalanced at 1
```
```
Input:
3
()

{[}
Output:
Balanced
Balanced
Unbalanced at 3
```

**Constraints:** Each string has `0` to `100000` characters. An empty string is balanced.

### Question 3: Company-Style Programming
**Title:** Merge Sorted Server Timelines

**Problem statement:** `k` servers each produce a list of event timestamps that is already sorted in ascending order. Merge them into a single timeline using a `PriorityQueue` that never holds more than one pending element per server. Each output line shows a timestamp and the 1-based id of the server that produced it. If two events have the same timestamp, the lower server id comes first.

**Input and output requirements:** Line 1: `k`. Next `k` lines: `m t1 t2 ... tm` (`m` may be 0). Output one line `timestamp serverId` for each event in merged order.

**Sample cases:**
```
Input:
3
3 1 4 9
2 2 4
1 3
Output:
1 1
2 2
3 3
4 1
4 2
9 1
```
```
Input:
2
0
2 5 5
Output:
5 2
5 2
```

**Constraints and edge cases:** `1 <= k <= 1000`; total events up to `200000`; timestamps are between `0` and `10^9`. Test all servers having zero events (no output) and repeated timestamps inside one server.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Deque<Integer> dq = new ArrayDeque<>();
        dq.offerFirst(1);
        dq.offerLast(2);
        dq.push(3);
        dq.addLast(4);
        System.out.println(dq);
        System.out.println(dq.pop() + " " + dq.pollLast() + " " + dq.peekFirst() + " " + dq.peekLast());
        System.out.println(dq);

        Queue<String> q = new ArrayDeque<>();
        q.offer("x");
        q.add("y");
        System.out.println(q.poll() + q.peek() + q.size() + q.poll() + q.poll());

        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int v : new int[]{5, 1, 4, 1, 3}) {
            pq.add(v);
        }
        System.out.println(pq.peek() + " " + pq.size());
        StringBuilder sb = new StringBuilder();
        while (pq.size() > 2) {
            sb.append(pq.poll()).append(',');
        }
        System.out.println(sb + " left=" + pq.size());

        PriorityQueue<String> ps = new PriorityQueue<>(
                Comparator.comparing(String::length).thenComparing(Comparator.naturalOrder()));
        ps.addAll(Arrays.asList("ccc", "a", "bb", "aa", "b"));
        StringBuilder out = new StringBuilder();
        while (!ps.isEmpty()) {
            out.append(ps.poll()).append(' ');
        }
        System.out.println(out.toString().trim());

        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        maxHeap.addAll(Arrays.asList(2, 9, 4));
        System.out.println(maxHeap.poll() + " " + maxHeap.poll());
    }
}
```

### Question 5: Debug the Code
**What the program should do:** (1) Print the tasks in ascending order of their priority number, producing `1:200`, `2:300`, `3:100`. (2) Keep a browsing history stack of the two pages `home` then `search` (never storing a `null` page) and print the most recent page, `search`. (3) Print `Next: null` when asking an empty queue for its next element.

The code below has at least five bugs (compile-time and run-time). Identify each, explain it, and give the corrected program.

```java
import java.util.*;

public class Main {
    public static void main(String[] args) {
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> b[0] - a[0]);
        pq.add(new int[]{3, 100});
        pq.add(new int[]{1, 200});
        pq.add(new int[]{2, 300});
        for (int[] t : pq) {
            System.out.println(t[0] + ":" + t[1]);
        }

        Deque<String> history = new ArrayDeque<>();
        history.push("home");
        history.push("search");
        history.push(null);
        System.out.println(history.get(0));

        Queue<Integer> empty = new ArrayDeque<>();
        System.out.println("Next: " + empty.remove());
    }
}
```

---

<a id="day13"></a>
## Day 13 — Generics Introduction

**Learning objectives:** Write generic classes and methods, use bounded type parameters (`extends Comparable<T>`), understand wildcards (`? extends`, `? super`), and know the basic limits caused by type erasure.

### Question 1: Mixed Real-World Scenario
**Title:** Generic Repository for Customers and Products

**Problem statement:** An application stores two kinds of records, `Customer(id, name)` and `Product(id, name, price)`. Both implement an interface `Identifiable` with `int getId()`. Write **one** generic class `Repository<T extends Identifiable>` that supports `save` (fails if the id exists), `find(id)`, `delete(id)`, and `listAll()` (sorted by id). Use two `Repository` instances, one per record type.

**Real-world context:** Data-access layers reuse one generic repository for every entity.

**Input requirements:** Line 1: `n`. Next `n` lines, each one of:
`CUSTOMER SAVE id name`, `PRODUCT SAVE id name price`, `CUSTOMER|PRODUCT FIND id`, `CUSTOMER|PRODUCT DELETE id`, `CUSTOMER|PRODUCT LIST`.

**Expected output:** `SAVE` prints `OK` or `DUPLICATE`. `FIND` prints the record or `NOT FOUND`. `DELETE` prints `DELETED` or `NOT FOUND`. `LIST` prints one record per line in ascending id order, or `(none)` if the repository is empty. Records print as `Customer{id=1, name=Asha}` and `Product{id=5, name=Pen, price=10}`.

**Sample cases:**
```
Input:
6
CUSTOMER SAVE 2 Ravi
CUSTOMER SAVE 1 Asha
CUSTOMER SAVE 1 Dup
PRODUCT SAVE 5 Pen 10
CUSTOMER LIST
PRODUCT FIND 6
Output:
OK
OK
DUPLICATE
OK
Customer{id=1, name=Asha}
Customer{id=2, name=Ravi}
NOT FOUND
```
```
Input:
4
PRODUCT SAVE 7 Cup 25
PRODUCT FIND 7
PRODUCT DELETE 7
PRODUCT LIST
Output:
OK
Product{id=7, name=Cup, price=25}
DELETED
(none)
```

**Constraints:** `1 <= n <= 5000`; ids are positive integers; names are single words; prices are integers. The two entity types may use the same id independently.

### Question 2: Interview-Based Coding
**Title:** Generic Utility Methods

**Problem statement:** Write four generic static methods and use them from `main`: `<T extends Comparable<T>> T maxOf(List<T>)`, `<T extends Comparable<T>> T minOf(List<T>)`, `<T> int countOf(List<T>, T)`, and `<T> List<T> reversed(List<T>)` (returns a new list). The same method code must work for integers, strings, and doubles with no casts and no `instanceof`.

**Input and output requirements:** One line: `TYPE n v1 ... vn` where `TYPE` is `INT`, `STR`, or `DBL`. Output four lines: `Max: ...`, `Min: ...`, `Count of first: ...` (how many times the first value occurs), `Reversed: [...]`.

**Sample cases:**
```
Input:
INT 5 3 9 3 1 7
Output:
Max: 9
Min: 1
Count of first: 2
Reversed: [7, 1, 3, 9, 3]
```
```
Input:
STR 3 pear apple fig
Output:
Max: pear
Min: apple
Count of first: 1
Reversed: [fig, apple, pear]
```

**Constraints and edge cases:** `1 <= n <= 1000`. Strings use natural (lexicographic) order. Doubles print with Java's default `Double.toString` (for example `2.5`). Test `n = 1` and all-equal values.

### Question 3: Company-Style Programming
**Title:** Generic MinStack

**Problem statement:** Implement `MinStack<T extends Comparable<T>>` that supports `push`, `pop`, `top`, and `min` (smallest element currently in the stack), each in constant time. Process commands for either integers or strings.

**Input and output requirements:** Line 1: `INT` or `STR`. Line 2: `n`. Next `n` lines: `PUSH x`, `POP`, `TOP`, or `MIN`. `PUSH` prints nothing. `POP` prints the removed element, `TOP` the top element, `MIN` the current minimum. On an empty stack, `POP`, `TOP`, and `MIN` print `EMPTY`.

**Sample cases:**
```
Input:
INT
7
PUSH 5
PUSH 2
PUSH 8
MIN
POP
POP
MIN
Output:
2
8
2
5
```
```
Input:
STR
7
PUSH pear
PUSH apple
TOP
POP
MIN
POP
POP
Output:
apple
apple
pear
pear
EMPTY
```

**Constraints and edge cases:** `1 <= n <= 100000`. Duplicates of the minimum must be handled (pushing `2`, `2`, then popping once leaves min `2`).

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.*;

public class Main {
    static class Box<T> {
        private T value;

        Box(T v) {
            value = v;
        }

        T get() {
            return value;
        }

        <R> Box<R> map(java.util.function.Function<T, R> f) {
            return new Box<>(f.apply(value));
        }

        public String toString() {
            return "Box(" + value + ")";
        }
    }

    static double sum(List<? extends Number> list) {
        double s = 0;
        for (Number n : list) {
            s += n.doubleValue();
        }
        return s;
    }

    static void fill(List<? super Integer> list) {
        list.add(1);
        list.add(2);
    }

    static <T extends Comparable<T>> T larger(T a, T b) {
        return a.compareTo(b) >= 0 ? a : b;
    }

    static String kind(Object o) {
        return "obj";
    }

    static String kind(String s) {
        return "str";
    }

    static <T> String kindG(T t) {
        return kind(t);
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        Box<Integer> b = new Box<>(21);
        Box<String> s = b.map(x -> "n" + x * 2);
        System.out.println(b + " " + s + " " + s.get().length());

        List<Number> nums = new ArrayList<>();
        fill(nums);
        nums.add(2.5);
        System.out.println(sum(nums) + " " + nums);

        System.out.println(larger("pear", "apple") + " " + larger(3, 7) + " " + larger(2.0, 2.0));
        System.out.println(kind("x") + " " + kindG("x"));

        List<String> ls = new ArrayList<>();
        List<Integer> li = new ArrayList<>();
        System.out.println(ls.getClass() == li.getClass());
        Object raw = ls;
        ((List<Integer>) raw).add(5);
        System.out.println(ls.size());
    }
}
```

### Question 5: Debug the Code
**What the program should do:** A generic `Pair<K, V>` holds a key and a value and can produce a swapped pair. The program should print `30 age`, then `6.0` (the sum of a list of integers using a method that accepts a list of any number type), then `3` (the size of a list holding three integers).

The code below has at least five bugs. Identify each one, explain why the compiler rejects it, and give the corrected program.

```java
import java.util.*;

class Pair<K, V> {
    private K key;
    private V value;
    static K defaultKey;

    Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    K getKey() {
        return key;
    }

    V getValue() {
        return value;
    }

    Pair<V, K> swap() {
        return new Pair<K, V>(value, key);
    }
}

public class Main {
    static double total(List<Number> nums) {
        double s = 0;
        for (Number n : nums) {
            s += n.doubleValue();
        }
        return s;
    }

    static <T> T[] makeArray(int n) {
        return new T[n];
    }

    public static void main(String[] args) {
        Pair<String, Integer> p = new Pair<>("age", 30);
        Pair<Integer, String> q = p.swap();
        System.out.println(q.getKey() + " " + q.getValue());

        List<Integer> ints = Arrays.asList(1, 2, 3);
        System.out.println(total(ints));

        List<int> nums2 = new ArrayList<>();
        nums2.add(1);
        nums2.add(2);
        nums2.add(3);
        System.out.println(nums2.size());
    }
}
```

---

<a id="day14"></a>
## Day 14 — Collections Combined (Week 2 Review)

**Learning objectives:** Combine lists, sets, maps, comparators, and queues to solve multi-step data problems, and know the behavior of common `Collections` and `Arrays` utilities.

### Question 1: Mixed Real-World Scenario
**Title:** Sales Analytics Report

**Problem statement:** An online shop has a list of order lines. For each line you get an order id, the customer, an item, a quantity, and a unit price. Produce (1) each customer's total spend, sorted by total descending and then by customer name ascending; and (2) the best-selling item, which is the item with the greatest total quantity sold (alphabetically smallest item if tied).

**Real-world context:** Business dashboards summarize raw order lines into customer and product rankings.

**Input requirements:** Line 1: `n`. Next `n` lines: `orderId customer item qty price` (all integers except the three words).

**Expected output:** One line `<customer> <total>` per customer in the order described, then `Best seller: <item> (<totalQty>)`.

**Sample cases:**
```
Input:
5
1 asha pen 10 5
2 ravi book 2 100
3 asha book 1 100
4 meena pen 5 5
5 ravi pen 1 5
Output:
ravi 205
asha 150
meena 25
Best seller: pen (16)
```
```
Input:
2
1 kim tea 3 10
2 lee cup 3 20
Output:
lee 60
kim 30
Best seller: cup (3)
```

**Constraints:** `1 <= n <= 100000`; `qty * price` fits in `int` per line, totals fit in `long`. Edge case: a single customer ordering several items.

### Question 2: Interview-Based Coding
**Title:** Top K Frequent Elements

**Problem statement:** Given an array of integers and `k`, print the `k` values that occur most often, ordered by frequency (highest first). Values with the same frequency are ordered by value, smallest first.

**Input and output requirements:** Line 1: `n`. Line 2: `n` integers. Line 3: `k` (`1 <=` `k` `<=` number of distinct values). Print the `k` values on one line, separated by spaces.

**Sample cases:**
```
Input:
6
1 1 1 2 2 3
2
Output:
1 2
```
```
Input:
5
-1 -1 -2 -2 3
1
Output:
-2
```

**Constraints and edge cases:** `1 <= n <= 100000`. Test `k` equal to the number of distinct values, all values equal, and negative values.

### Question 3: Company-Style Programming
**Title:** Merge Booked Time Slots

**Problem statement:** A meeting-room system receives booked slots `[start, end)` as integer minute marks in any order. Merge every group of slots that overlap or touch (a slot ending at `4` and another starting at `4` merge into one). Print the merged slots in ascending order and the total number of booked minutes.

**Input and output requirements:** Line 1: `n`. Next `n` lines: `start end` with `start < end`. Output one line `start end` per merged slot, then `Total: <sum of (end - start)>`.

**Sample cases:**
```
Input:
4
1 3
8 10
2 6
15 18
Output:
1 6
8 10
15 18
Total: 10
```
```
Input:
3
1 4
4 5
6 7
Output:
1 5
6 7
Total: 5
```

**Constraints and edge cases:** `1 <= n <= 100000`; `0 <= start < end <= 10^9`. Test one slot completely inside another (`1 10` and `3 4`), identical slots, and a single slot.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<Integer> fixed = Arrays.asList(3, 1, 2);
        try {
            fixed.add(4);
        } catch (UnsupportedOperationException e) {
            System.out.println("fixed add");
        }
        fixed.set(0, 9);
        Collections.sort(fixed);
        System.out.println(fixed);

        List<Integer> view = Collections.unmodifiableList(new ArrayList<>(fixed));
        try {
            view.set(0, 0);
        } catch (UnsupportedOperationException e) {
            System.out.println("view set");
        }

        Integer[] arr = {4, 2, 7};
        List<Integer> asList = Arrays.asList(arr);
        arr[0] = 100;
        System.out.println(asList);
        Collections.reverse(asList);
        System.out.println(Arrays.toString(arr));

        List<Integer> nums = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));
        nums.removeIf(n -> n % 2 == 0);
        Collections.swap(nums, 0, 2);
        System.out.println(nums + " " + Collections.frequency(nums, 3) + " "
                + Collections.binarySearch(Arrays.asList(1, 3, 5, 7), 4));
        Collections.rotate(nums, 1);
        System.out.println(nums);
        System.out.println(Collections.nCopies(3, "ab") + " " + String.join("-", Collections.nCopies(2, "x")));

        List<List<Integer>> nested = new ArrayList<>();
        List<Integer> row = new ArrayList<>(Arrays.asList(1));
        nested.add(row);
        nested.add(row);
        row.add(2);
        nested.get(0).add(3);
        System.out.println(nested);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Group four students by grade (`A` for marks of 85 or more, otherwise `B`), print each group with its **exact** average, and finally print the student with the **highest** marks. Expected output:
```
A: [Asha, Meena] avg 87.5
B: [Ravi, Joy] avg 75.5
Top: Asha
```

The code below has at least three bugs (one crashes, two give wrong output). Identify each one, explain it, and give the corrected program.

```java
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String[] names = {"Asha", "Ravi", "Meena", "Joy"};
        int[] marks = {90, 71, 85, 80};
        Map<Character, List<String>> byGrade = new TreeMap<>();
        for (int i = 0; i < names.length; i++) {
            char g = marks[i] >= 85 ? 'A' : 'B';
            byGrade.get(g).add(names[i]);
        }

        for (Map.Entry<Character, List<String>> e : byGrade.entrySet()) {
            int sum = 0;
            for (String n : e.getValue()) {
                sum += marks[Arrays.asList(names).indexOf(n)];
            }
            double avg = sum / e.getValue().size();
            System.out.println(e.getKey() + ": " + e.getValue() + " avg " + avg);
        }

        List<String> sorted = new ArrayList<>(Arrays.asList(names));
        sorted.sort((x, y) -> marks[Arrays.asList(names).indexOf(x)]
                - marks[Arrays.asList(names).indexOf(y)]);
        System.out.println("Top: " + sorted.get(0));
    }
}
```

---

<a id="day15"></a>
## Day 15 — StringBuilder and Regex Basics

**Learning objectives:** Build and edit strings efficiently with `StringBuilder`, understand string immutability and interning, and use `Pattern`/`Matcher`, groups, `split`, and `replaceAll` for simple pattern tasks.

### Question 1: Mixed Real-World Scenario
**Title:** Server Log Summary

**Problem statement:** A monitoring tool reads log lines of the form `YYYY-MM-DD HH:MM:SS LEVEL [module] message`. A line is **well-formed** only if it matches this pattern exactly: date as `\d{4}-\d{2}-\d{2}`, time as `\d{2}:\d{2}:\d{2}`, a level that is exactly `INFO`, `WARN`, or `ERROR` (upper case), a module name of word characters in square brackets, and a message of any text. (Only the digit layout is checked, not whether the date really exists.) Count well-formed lines per level, count malformed lines, and find the module with the most `ERROR` lines.

**Real-world context:** Operations teams scan logs daily; malformed lines usually point to a broken logging setup.

**Input requirements:** Line 1: `n`. Next `n` lines: log lines (each may contain spaces).

**Expected output:**
```
INFO=<a> WARN=<b> ERROR=<c> MALFORMED=<d>
Top error module: <module> (<count>)
```
If there are no `ERROR` lines, print `Top error module: none`. On a tie, the alphabetically smallest module wins.

**Sample cases:**
```
Input:
5
2024-03-15 10:23:45 ERROR [auth] Login failed for user=asha
2024-03-15 10:24:00 INFO [auth] Login ok
2024-03-15 10:25:10 ERROR [db] Timeout
bad line here
2024-03-15 10:26:00 ERROR [auth] Locked
Output:
INFO=1 WARN=0 ERROR=3 MALFORMED=1
Top error module: auth (2)
```
```
Input:
2
2024-01-01 00:00:00 INFO [x] hi
2024-13-01 00:00:00 info [x] hi
Output:
INFO=1 WARN=0 ERROR=0 MALFORMED=1
Top error module: none
```

**Constraints:** `1 <= n <= 10000`; lines are at most 200 characters. Edge case: a line that ends right after `]` is malformed, because the pattern requires one space after `]` (a line ending in `] ` with an empty message is well-formed).

### Question 2: Interview-Based Coding
**Title:** Run-Length Compression

**Problem statement:** Compress a string of letters with run-length encoding: each run of identical characters becomes the character followed by its run length (`aaabb` becomes `a3b2`). If the encoded string is **not strictly shorter** than the original, output the original string instead. Build the result with `StringBuilder`.

**Input and output requirements:** Line 1: `n`. Next `n` lines: one string each (may be empty). Print one result line per string.

**Sample cases:**
```
Input:
3
aaaaabbbc
abc
aaaaaaaaaaaa
Output:
a5b3c1
abc
a12
```
```
Input:
3
a
aab

Output:
a
aab

```

**Constraints and edge cases:** Each string has `0` to `100000` letters. Run lengths of 10 or more use all their digits. An empty string stays empty. (The second sample's last line of output is an empty line.)

### Question 3: Company-Style Programming
**Title:** Mask Sensitive Data

**Problem statement:** A support-ticket system must hide personal data before text is stored. Using regular expressions, replace:
- every e-mail address matching `[A-Za-z0-9._]+@[A-Za-z0-9.]+\.[a-z]{2,}` with its first character, then `***`, then everything from the `@` onward (for example `asha.k@mail.com` becomes `a***@mail.com`)
- every run of **exactly** 10 digits (not part of a longer run of digits) with `XXXXXX` followed by its last 4 digits

All other text stays unchanged.

**Input and output requirements:** Line 1: `n`. Next `n` lines: free text. Print the masked text for each line.

**Sample cases:**
```
Input:
2
Contact asha.k@mail.com or 9876543210 now
ids 123456789012 and 5551234567.
Output:
Contact a***@mail.com or XXXXXX3210 now
ids 123456789012 and XXXXXX4567.
```
```
Input:
2
no data here 12345
write to a@b.co or c.d@x.org
Output:
no data here 12345
write to a***@b.co or c***@x.org
```

**Constraints and edge cases:** `1 <= n <= 1000`; lines have up to 300 characters. Test two e-mails in one line, a phone number at the very start or end of the line, and an e-mail directly followed by a full stop.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
public class Main {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder("hello");
        sb.append(' ').append("world").append(1).append('!').append(2.0);
        System.out.println(sb);

        sb.insert(0, "[").append("]");
        sb.setCharAt(1, 'H');
        sb.deleteCharAt(sb.length() - 1);
        sb.replace(6, 11, "J");
        System.out.println(sb + " " + sb.length() + " " + sb.indexOf("o"));

        sb.reverse();
        System.out.println(sb);
        sb.setLength(3);
        System.out.println(sb + "|" + sb.length());

        String s1 = "java";
        String s2 = "ja" + "va";
        String part = "ja";
        String s3 = part + "va";
        System.out.println((s1 == s2) + " " + (s1 == s3) + " " + s1.equals(s3) + " " + (s1 == s3.intern()));

        StringBuilder a = new StringBuilder("x");
        StringBuilder b = a;
        b.append("y");
        System.out.println(a + " " + (a == b) + " " + a.equals(new StringBuilder("xy"))
                + " " + a.toString().equals("xy"));

        String t = "a,b,,c,,";
        System.out.println(t.split(",").length + " " + t.split(",", -1).length);
        System.out.println("  pad ".trim().length() + " " + "abc".compareTo("abd") + " " + "".isEmpty());
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Print `Major: 1` for the version `1.2.10`; print `Words: 3` for the text `"  Hello   Java  World  "`; print the phone number `9876543210` as `987-654-3210`; print `Stopping` when the command equals `stop`; print `JAVA` for the text `java` converted to upper case.

The code below has at least five bugs (some crash, some silently give wrong results). One line is correct as written. Identify each bug, explain it, and give the corrected program.

```java
public class Main {
    public static void main(String[] args) {
        String version = "1.2.10";
        String[] parts = version.split(".");
        System.out.println("Major: " + parts[0]);

        String text = "  Hello   Java  World  ";
        text.trim();
        String[] words = text.split(" ");
        System.out.println("Words: " + words.length);

        String raw = "9876543210";
        StringBuilder sb = new StringBuilder(raw);
        sb.insert(3, "-");
        sb.insert(7, "-");
        System.out.println(sb);

        String cmd = new String("stop");
        if (cmd == "stop") {
            System.out.println("Stopping");
        }

        String upper = "java";
        upper.toUpperCase();
        System.out.println(upper);
    }
}
```

---

<a id="day16"></a>
## Day 16 — Two-Pointer Technique

**Learning objectives:** Use two indices moving through an array or string to replace nested loops, work in place, and reason about why each pointer movement is safe.

### Question 1: Mixed Real-World Scenario
**Title:** Gift Pair Within Budget

**Problem statement:** A shopper wants to buy exactly two different items from a catalog and spend as much of the budget as possible without exceeding it. Given the item prices, find the best pair. If several different price pairs reach the same best total, choose the one whose smaller price is smallest. Print the two prices (smaller first) and their total. Your solution should be faster than checking every pair.

**Real-world context:** Gift cards and vouchers often have a fixed value that shoppers try to use up as fully as possible.

**Input requirements:** Line 1: `n`. Line 2: `n` prices (unsorted). Line 3: `budget`.

**Expected output:** `<a> <b> = <total>` with `a <= b`, or `No pair` if no two items fit in the budget.

**Sample cases:**
```
Input:
6
10 25 15 40 30 5
50
Output:
10 40 = 50
```
```
Input:
4
1 4 2 3
5
Output:
1 4 = 5
```

**Constraints:** `2 <= n <= 100000`; prices are positive and at most `10^9`; the budget is at most `2 * 10^9` (use `long` for sums). Edge cases: two equal prices forming the pair (`5 5 8 9` with budget `10` gives `5 5 = 10`); budget smaller than the two cheapest items (`No pair`).

### Question 2: Interview-Based Coding
**Title:** Zero-Sum Triplets

**Problem statement:** Given an array of integers, find all **unique** triplets of values `(a, b, c)` with `a + b + c = 0`. Two triplets are the same if they contain the same values, regardless of positions. Print each triplet in ascending order of its values, and print the triplets in lexicographic order. Aim for `O(n^2)` time.

**Input and output requirements:** Line 1: `n`. Line 2: `n` integers. Print each triplet on its own line as `a b c`, or `No triplets` if none exists.

**Sample cases:**
```
Input:
6
-1 0 1 2 -1 -4
Output:
-1 -1 2
-1 0 1
```
```
Input:
4
0 0 0 0
Output:
0 0 0
```

**Constraints and edge cases:** `1 <= n <= 3000`; values between `-10^5` and `10^5`. Test fewer than three elements (`No triplets`), all positive numbers, and many duplicates (no duplicate triplet may be printed).

### Question 3: Company-Style Programming
**Title:** Rainwater Reservoir

**Problem statement:** A city engineer models a street as a row of vertical walls with given heights (each wall is 1 unit wide). After heavy rain, water collects between walls. Compute the total volume of water retained. Water above a position is limited by the lower of the tallest wall to its left and the tallest wall to its right (including the position itself). Your solution must run in `O(n)` time.

**Input and output requirements:** Line 1: `n`. Line 2: `n` non-negative integers (wall heights). Print the total water units.

**Sample cases:**
```
Input:
12
0 1 0 2 1 0 1 3 2 1 2 1
Output:
6
```
```
Input:
6
4 2 0 3 2 5
Output:
9
```

**Constraints and edge cases:** `1 <= n <= 100000`; heights at most `10^4`; the answer fits in `long`. Test a strictly increasing row (0 water), a single wall, and a deep valley between two tall walls.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.*;

public class Main {
    static int dedupe(int[] a) {
        int w = 1;
        for (int r = 1; r < a.length; r++) {
            if (a[r] != a[w - 1]) {
                a[w++] = a[r];
            }
        }
        return w;
    }

    static void reverse(int[] a, int i, int j) {
        while (i < j) {
            int t = a[i];
            a[i++] = a[j];
            a[j--] = t;
        }
    }

    static void rotate(int[] a, int k) {
        k %= a.length;
        reverse(a, 0, a.length - 1);
        reverse(a, 0, k - 1);
        reverse(a, k, a.length - 1);
    }

    static boolean isPal(String s) {
        int i = 0, j = s.length() - 1;
        while (i < j) {
            while (i < j && !Character.isLetterOrDigit(s.charAt(i))) {
                i++;
            }
            while (i < j && !Character.isLetterOrDigit(s.charAt(j))) {
                j--;
            }
            if (Character.toLowerCase(s.charAt(i++)) != Character.toLowerCase(s.charAt(j--))) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        int[] a = {1, 1, 2, 2, 2, 3, 5, 5};
        int len = dedupe(a);
        System.out.println(len + " " + Arrays.toString(a));

        int[] b = {1, 2, 3, 4, 5, 6, 7};
        rotate(b, 3);
        System.out.println(Arrays.toString(b));

        System.out.println(isPal("A man, a plan, a canal: Panama") + " "
                + isPal("race a car") + " " + isPal(".,"));

        int[] c = {0, 1, 0, 3, 12};
        int w = 0;
        for (int x : c) {
            if (x != 0) {
                c[w++] = x;
            }
        }
        while (w < c.length) {
            c[w++] = 0;
        }
        System.out.println(Arrays.toString(c));
    }
}
```

### Question 5: Debug the Code
**What the program should do:** `twoSumSorted` receives an array sorted in ascending order and a target, and returns the 0-based indices `{i, j}` with `i < j` and `a[i] + a[j] == target` (or `null` if there is no such pair). For `{2, 3, 5, 8, 11}` with target `11` it should print `1 3`; for target `100` it should print `none`.

The code below has at least four bugs in `twoSumSorted`. Identify each, explain it, and give the corrected program.

```java
public class Main {
    static int[] twoSumSorted(int[] a, int target) {
        int i = 0, j = a.length;
        while (i <= j) {
            int sum = a[i] + a[j];
            if (sum == target) {
                return new int[]{i, j};
            }
            if (sum < target) {
                j++;
            } else {
                i--;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        int[] a = {2, 3, 5, 8, 11};
        int[] r = twoSumSorted(a, 11);
        System.out.println(r[0] + " " + r[1]);
        r = twoSumSorted(a, 100);
        System.out.println(r == null ? "none" : r[0] + " " + r[1]);
    }
}
```

---

<a id="day17"></a>
## Day 17 — Sliding Window

**Learning objectives:** Maintain a moving window with running state (sum, counts, distinct characters), recognize fixed-size versus variable-size windows, and avoid recomputing the window from scratch.

### Question 1: Mixed Real-World Scenario
**Title:** Peak Traffic Window

**Problem statement:** A website records visitors for each hour of the day. Management wants the busiest stretch of exactly `k` consecutive hours. Report the starting hour of that stretch (0-based, the earliest if tied), its total visitors, and its average visitors per hour.

**Real-world context:** Capacity planners schedule extra servers for the busiest window of the day.

**Input requirements:** Line 1: `n k`. Line 2: `n` non-negative integers (visitors per hour).

**Expected output:**
```
Start: <hour>
Sum: <total>
Average: <total / k with 2 decimals>
```

**Sample cases:**
```
Input:
8 3
4 2 10 3 1 8 9 2
Output:
Start: 5
Sum: 19
Average: 6.33
```
```
Input:
4 2
5 5 5 5
Output:
Start: 0
Sum: 10
Average: 5.00
```

**Constraints:** `1 <= k <= n <= 100000`; visitor counts at most `10^6`. Edge cases: `k = n` (one window); `k = 1`.

### Question 2: Interview-Based Coding
**Title:** Longest Substring Without Repeating Characters

**Problem statement:** Find the longest substring of the given string in which every character appears at most once. Print its length and the substring itself. If several substrings share the maximum length, print the one that starts earliest.

**Input and output requirements:** Line 1: `n`. Next `n` lines: one non-empty string each. Print `<length> <substring>` for each.

**Sample cases:**
```
Input:
3
abcabcbb
pwwkew
bbbbb
Output:
3 abc
3 wke
1 b
```
```
Input:
2
abcdef
dvdf
Output:
6 abcdef
3 vdf
```

**Constraints and edge cases:** String length `1` to `100000`; characters are any printable ASCII without spaces. Your solution must be linear in the string length. Test a string where the repeated character is far behind the window start (`abba` gives `2 ab`).

### Question 3: Company-Style Programming
**Title:** Longest Streak with Limited Repairs

**Problem statement:** A factory line produces items; a sensor logs `1` for a good item and `0` for a defective one. A quality manager may mark at most `k` defective items as "repaired" (treat them as good). Find the length of the longest streak of consecutive good items that can be achieved.

**Input and output requirements:** Line 1: `n k`. Line 2: `n` values, each `0` or `1`. Print one integer.

**Sample cases:**
```
Input:
9 2
1 1 0 0 1 1 1 0 1
Output:
7
```
```
Input:
3 1
0 0 0
Output:
1
```

**Constraints and edge cases:** `1 <= n <= 100000`; `0 <= k <= n`. Test `k = 0` (`4 0` / `1 0 1 1` gives `2`), `k >= number of zeros` (answer is `n`), and an all-ones array.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.*;

public class Main {
    static int minLen(int[] a, int target) {
        int best = Integer.MAX_VALUE, sum = 0, left = 0;
        for (int right = 0; right < a.length; right++) {
            sum += a[right];
            while (sum >= target) {
                best = Math.min(best, right - left + 1);
                sum -= a[left++];
            }
        }
        return best == Integer.MAX_VALUE ? 0 : best;
    }

    static int longestAtMostTwo(String s) {
        Map<Character, Integer> cnt = new HashMap<>();
        int left = 0, best = 0;
        for (int right = 0; right < s.length(); right++) {
            cnt.merge(s.charAt(right), 1, Integer::sum);
            while (cnt.size() > 2) {
                char c = s.charAt(left++);
                if (cnt.merge(c, -1, Integer::sum) == 0) {
                    cnt.remove(c);
                }
            }
            best = Math.max(best, right - left + 1);
        }
        return best;
    }

    public static void main(String[] args) {
        System.out.println(minLen(new int[]{2, 3, 1, 2, 4, 3}, 7));
        System.out.println(minLen(new int[]{1, 1, 1}, 5));
        System.out.println(longestAtMostTwo("eceba"));
        System.out.println(longestAtMostTwo("aabbcc"));

        int[] a = {1, -2, 3, 4, -1};
        int k = 2, window = 0, maxW = Integer.MIN_VALUE;
        for (int i = 0; i < a.length; i++) {
            window += a[i];
            if (i >= k - 1) {
                maxW = Math.max(maxW, window);
                window -= a[i - k + 1];
            }
        }
        System.out.println(maxW);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** `maxSumK(a, k)` returns the maximum sum of any `k` consecutive elements, or `-1` if `k` is not between `1` and `a.length`. Expected output for the three calls in `main`: `24`, `-5`, `-1`.

The code below has at least four bugs. Identify each, explain it with a failing input, and give the corrected program.

```java
public class Main {
    static int maxSumK(int[] a, int k) {
        int sum = 0;
        for (int i = 0; i < k; i++) {
            sum += a[i];
        }
        int best = 0;
        for (int i = k; i < a.length; i++) {
            sum += a[i] - a[i - k + 1];
            best = Math.max(best, sum);
        }
        return best;
    }

    public static void main(String[] args) {
        System.out.println(maxSumK(new int[]{1, 4, 2, 10, 2, 3, 1, 0, 20}, 4));
        System.out.println(maxSumK(new int[]{-5, -2, -3, -8}, 2));
        System.out.println(maxSumK(new int[]{1, 2, 3}, 10));
    }
}
```

---

<a id="day18"></a>
## Day 18 — Prefix Sums and Binary Search

**Learning objectives:** Precompute prefix sums for fast range queries, combine prefix sums with hash maps, and write binary search variants (first occurrence, last occurrence, lower bound) without off-by-one errors.

### Question 1: Mixed Real-World Scenario
**Title:** Sales Range Queries

**Problem statement:** A store has the sales amount for each day of a month. Managers ask many questions of the form "what were the total sales and the average daily sales from day `l` to day `r`?" Answer each question quickly by preprocessing the data once. Days are numbered from 1 and the range is inclusive.

**Real-world context:** Dashboards answer many date-range questions over the same data, so the data is summarized once.

**Input requirements:** Line 1: `n`. Line 2: `n` non-negative integers. Line 3: `q`. Next `q` lines: `l r`.

**Expected output:** For each query print `<sum> <average>` where the average is `sum / (r - l + 1)` with exactly 2 decimals.

**Sample cases:**
```
Input:
5
3 1 4 1 5
3
1 5
2 4
3 3
Output:
14 2.80
6 2.00
4 4.00
```
```
Input:
3
10 20 30
2
1 2
2 3
Output:
30 15.00
50 25.00
```

**Constraints:** `1 <= n, q <= 100000`; each amount is at most `10^9` (use `long`); `1 <= l <= r <= n`. Each query must take constant time.

### Question 2: Interview-Based Coding
**Title:** Count Subarrays with Sum K

**Problem statement:** Given an array of integers (which may include negative numbers and zeros) and an integer `k`, count the contiguous subarrays whose elements add up to exactly `k`. A solution that checks every subarray is too slow for the largest inputs.

**Input and output requirements:** Line 1: `n`. Line 2: `n` integers. Line 3: `k`. Print the count.

**Sample cases:**
```
Input:
3
1 1 1
2
Output:
2
```
```
Input:
3
1 -1 0
0
Output:
3
```

**Constraints and edge cases:** `1 <= n <= 100000`; values between `-1000` and `1000`; `k` between `-10^9` and `10^9`. The count can exceed the `int` range for large inputs, so use `long`. Test `k = 0` with many zeros and an array with no matching subarray.

### Question 3: Company-Style Programming
**Title:** Sorted Catalog Lookups

**Problem statement:** A sorted array of product codes (with duplicates) is queried many times. For each query value `x`, print four numbers: the index of the first occurrence of `x`, the index of the last occurrence, the number of occurrences, and the insertion index (the index of the first element that is greater than or equal to `x`, or `n` if there is none). If `x` is absent, the first and last indices are `-1`. Each query must run in `O(log n)`.

**Input and output requirements:** Line 1: `n`. Line 2: `n` integers in non-decreasing order. Line 3: `q`. Line 4: `q` query values. Print one line `first last count insert` per query.

**Sample cases:**
```
Input:
7
1 2 2 2 3 5 5
4
2 5 4 0
Output:
1 3 3 1
5 6 2 5
-1 -1 0 5
-1 -1 0 0
```
```
Input:
3
4 4 4
2
4 9
Output:
0 2 3 0
-1 -1 0 3
```

**Constraints and edge cases:** `1 <= n, q <= 100000`; values between `-10^9` and `10^9`. Test queries smaller than every element, larger than every element, and values that fill the whole array.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.*;

public class Main {
    static int lowerBound(int[] a, int x) {
        int lo = 0, hi = a.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (a[mid] < x) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    static int search(int[] a, int x) {
        int lo = 0, hi = a.length - 1, steps = 0;
        while (lo <= hi) {
            steps++;
            int mid = lo + (hi - lo) / 2;
            if (a[mid] == x) {
                return steps * 100 + mid;
            }
            if (a[mid] < x) {
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return -steps;
    }

    public static void main(String[] args) {
        int[] a = {1, 3, 3, 3, 7, 9, 12};
        System.out.println(lowerBound(a, 3) + " " + lowerBound(a, 4) + " "
                + lowerBound(a, 100) + " " + lowerBound(a, 0));
        System.out.println(search(a, 7) + " " + search(a, 1) + " " + search(a, 8));
        System.out.println(Arrays.binarySearch(a, 12) + " " + Arrays.binarySearch(a, 5)
                + " " + Arrays.binarySearch(a, -4));

        int[] prefix = new int[a.length + 1];
        for (int i = 0; i < a.length; i++) {
            prefix[i + 1] = prefix[i] + a[i];
        }
        System.out.println(Arrays.toString(prefix));
        System.out.println(prefix[5] - prefix[2] + " " + (prefix[7] - prefix[0]));

        int lo = 0, hi = 50;
        while (lo < hi) {
            int mid = (lo + hi + 1) / 2;
            if (mid * mid <= 50) {
                lo = mid;
            } else {
                hi = mid - 1;
            }
        }
        System.out.println(lo);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** For `a = {2, 2, 3, 3, 3, 5}`, `firstOccurrence(a, 3)` must return the index of the first `3` (`2`) and `firstOccurrence(a, 4)` must return `-1`. `rangeSum(p, l, r)` must return the sum of `a[l..r]` (inclusive, 0-based) using a prefix-sum array `p`; for `l = 1, r = 3` the expected result is `8`. The program should print `2 -1 8`.

The code below has at least four bugs (it may crash or loop forever). Identify each, explain it, and give the corrected program.

```java
public class Main {
    static int firstOccurrence(int[] a, int x) {
        int lo = 0, hi = a.length, ans = -1;
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (a[mid] == x) {
                ans = mid;
                hi = mid;
            } else if (a[mid] < x) {
                lo = mid;
            } else {
                hi = mid;
            }
        }
        return ans;
    }

    static int[] buildPrefix(int[] a) {
        int[] p = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            p[i] = p[i - 1] + a[i];
        }
        return p;
    }

    static int rangeSum(int[] p, int l, int r) {
        return p[r] - p[l];
    }

    public static void main(String[] args) {
        int[] a = {2, 2, 3, 3, 3, 5};
        int[] p = buildPrefix(a);
        System.out.println(firstOccurrence(a, 3) + " " + firstOccurrence(a, 4) + " " + rangeSum(p, 1, 3));
    }
}
```

---

<a id="day19"></a>
## Day 19 — Sorting Algorithms

**Learning objectives:** Implement bubble, selection, insertion, merge, and quick-sort ideas by hand, trace them step by step, and know their time complexity and stability.

### Question 1: Mixed Real-World Scenario
**Title:** Conveyor Belt Parcel Sorter

**Problem statement:** A robot arm sorts parcels on a belt by weight using bubble sort. In each pass it scans the belt from left to right comparing neighbours and swapping them if the left one is heavier. After every pass **that performed at least one swap**, print the belt. The robot stops after the first pass that performs no swaps. Also report the total number of swaps and the total number of passes performed (including the final no-swap pass). Every pass scans the entire belt (do not shorten the scan range).

**Real-world context:** Simple machines often use simple, predictable sorting rules, and engineers need to know how many moves they will make.

**Input requirements:** Line 1: `n`. Line 2: `n` parcel weights.

**Expected output:** `Pass <i>: <belt>` for every pass with swaps, then `Swaps: <total>` and `Passes: <total>`.

**Sample cases:**
```
Input:
5
5 1 4 2 8
Output:
Pass 1: 1 4 2 5 8
Pass 2: 1 2 4 5 8
Swaps: 4
Passes: 3
```
```
Input:
4
4 3 2 1
Output:
Pass 1: 3 2 1 4
Pass 2: 2 1 3 4
Pass 3: 1 2 3 4
Swaps: 6
Passes: 4
```

**Constraints:** `1 <= n <= 1000`. Edge cases: an already sorted belt (no `Pass` lines, `Swaps: 0`, `Passes: 1`); `n = 1`; equal weights (equal neighbours are never swapped).

### Question 2: Interview-Based Coding
**Title:** Dutch National Flag

**Problem statement:** An array contains only the values `0`, `1`, and `2`. Sort it in ascending order in a single pass over the array using only `O(1)` extra space, without calling a library sort and without counting the values first.

**Input and output requirements:** Line 1: `n`. Line 2: `n` values from `{0, 1, 2}`. Print the sorted array, values separated by spaces.

**Sample cases:**
```
Input:
6
2 0 2 1 1 0
Output:
0 0 1 1 2 2
```
```
Input:
3
2 2 0
Output:
0 2 2
```

**Constraints and edge cases:** `1 <= n <= 100000`. Test `n = 1`, an array of one repeated value, and an array already sorted in descending order.

### Question 3: Company-Style Programming
**Title:** Out-of-Order Event Counter

**Problem statement:** A monitoring system receives sequence numbers that should arrive in ascending order. An **out-of-order pair** is a pair of positions `i < j` with `a[i] > a[j]`. Count all such pairs and also output the array in sorted order. Implement your own merge sort that counts the pairs while merging; a double loop over all pairs is too slow for the largest inputs.

**Input and output requirements:** Line 1: `n`. Line 2: `n` integers. Output two lines: `Pairs: <count>` and the sorted array separated by spaces.

**Sample cases:**
```
Input:
5
2 4 1 3 5
Output:
Pairs: 3
1 2 3 4 5
```
```
Input:
5
5 4 3 2 1
Output:
Pairs: 10
1 2 3 4 5
```

**Constraints and edge cases:** `1 <= n <= 100000`; values between `-10^9` and `10^9`; the count needs `long`. Equal values are not an out-of-order pair. Test all-equal values and an already sorted array.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.*;

public class Main {
    static int cmp = 0;

    static void insertion(int[] a) {
        for (int i = 1; i < a.length; i++) {
            int key = a[i], j = i - 1;
            while (j >= 0 && a[j] > key) {
                a[j + 1] = a[j];
                j--;
                cmp++;
            }
            a[j + 1] = key;
        }
    }

    static void selection(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            int m = i;
            for (int j = i + 1; j < a.length; j++) {
                if (a[j] < a[m]) {
                    m = j;
                }
            }
            int t = a[i];
            a[i] = a[m];
            a[m] = t;
            System.out.println(i + ": " + Arrays.toString(a));
        }
    }

    static int partition(int[] a, int lo, int hi) {
        int pivot = a[hi], i = lo;
        for (int j = lo; j < hi; j++) {
            if (a[j] < pivot) {
                int t = a[i];
                a[i] = a[j];
                a[j] = t;
                i++;
            }
        }
        int t = a[i];
        a[i] = a[hi];
        a[hi] = t;
        return i;
    }

    public static void main(String[] args) {
        int[] x = {4, 3, 3, 1};
        insertion(x);
        System.out.println(Arrays.toString(x) + " " + cmp);

        int[] y = {3, 1, 3, 2};
        selection(y);

        int[] z = {5, 2, 9, 1, 5, 6};
        int p = partition(z, 0, z.length - 1);
        System.out.println(p + " " + Arrays.toString(z));
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Sort an array in ascending order with a stable top-down merge sort over the inclusive range `[lo, hi]`. For `{5, 2, 4, 7, 1, 3, 2, 6}` the program should print `[1, 2, 2, 3, 4, 5, 6, 7]`.

The code below has at least four bugs (it will crash or never finish). Identify each, explain it, and give the corrected program.

```java
import java.util.*;

public class Main {
    static void mergeSort(int[] a, int lo, int hi) {
        if (lo >= hi) {
            return;
        }
        int mid = (lo + hi) / 2;
        mergeSort(a, lo, mid);
        mergeSort(a, mid, hi);
        merge(a, lo, mid, hi);
    }

    static void merge(int[] a, int lo, int mid, int hi) {
        int[] tmp = new int[hi - lo];
        int i = lo, j = mid + 1, k = 0;
        while (i <= mid && j <= hi) {
            if (a[i] < a[j]) {
                tmp[k++] = a[i++];
            } else {
                tmp[k++] = a[j++];
            }
        }
        while (i <= mid) {
            tmp[k++] = a[i++];
        }
        while (j <= hi) {
            tmp[k++] = a[j++];
        }
        for (k = 0; k < tmp.length; k++) {
            a[k] = tmp[k];
        }
    }

    public static void main(String[] args) {
        int[] a = {5, 2, 4, 7, 1, 3, 2, 6};
        mergeSort(a, 0, a.length - 1);
        System.out.println(Arrays.toString(a));
    }
}
```

---

<a id="day20"></a>
## Day 20 — Recursion and Backtracking

**Learning objectives:** Write recursive solutions with clear base cases, trace call stacks, and use choose/explore/un-choose backtracking to enumerate combinations, permutations, and placements.

### Question 1: Mixed Real-World Scenario
**Title:** Vending Machine Change Combinations

**Problem statement:** A vending machine has coins of several denominations (unlimited supply of each). List every way to pay exactly a given amount. A way is a multiset of coins, so `1+2` and `2+1` are the same way. Print each way with its coins in non-decreasing order, joined by `+`, and print the ways in lexicographic order of their coin sequences (compare coin by coin). Finish with the total count.

**Real-world context:** Cash machines and vending machines must check which coin combinations can make up an amount.

**Input requirements:** Line 1: `amount`. Line 2: `m`, the number of denominations. Line 3: `m` distinct positive denominations in any order.

**Expected output:** One line per way, then `Total ways: <count>`.

**Sample cases:**
```
Input:
5
3
1 2 5
Output:
1+1+1+1+1
1+1+1+2
1+2+2
5
Total ways: 4
```
```
Input:
7
2
3 2
Output:
2+2+3
Total ways: 1
```

**Constraints:** `1 <= amount <= 30`; `1 <= m <= 5`; denominations are at most `30`. Edge case: no combination exists (print only `Total ways: 0`).

### Question 2: Interview-Based Coding
**Title:** Unique Permutations

**Problem statement:** Print every distinct permutation of the given string in lexicographic (dictionary) order. The string may contain repeated characters, and the same permutation must never be printed twice. Use backtracking.

**Input and output requirements:** One line with a string of 1 to 8 lower-case letters. Print each permutation on its own line, followed by `Total: <count>`.

**Sample cases:**
```
Input:
aab
Output:
aab
aba
baa
Total: 3
```
```
Input:
abc
Output:
abc
acb
bac
bca
cab
cba
Total: 6
```

**Constraints and edge cases:** Length at most 8 (at most 40320 permutations). Test a one-letter string and a string of identical letters (`aaa` gives one permutation).

### Question 3: Company-Style Programming
**Title:** Queens on a Board

**Problem statement:** Place `n` queens on an `n x n` chessboard so that no two queens attack each other (no two share a row, a column, or a diagonal). Count all valid placements, and report the first one found when rows are filled from top to bottom and, within a row, columns are tried from left to right. A placement is described by the 1-based column of the queen in each row.

**Input and output requirements:** One integer `n`. Output `Solutions: <count>` and `First: <columns separated by spaces>`, or `First: none` if there is no placement.

**Sample cases:**
```
Input:
4
Output:
Solutions: 2
First: 2 4 1 3
```
```
Input:
3
Output:
Solutions: 0
First: none
```

**Constraints and edge cases:** `1 <= n <= 9`. For `n = 1` the answer is `Solutions: 1` and `First: 1`. Your solution must prune invalid placements early rather than checking complete boards.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program (note the trailing space on the third line).

```java
public class Main {
    static int calls = 0;

    static int f(int n) {
        calls++;
        if (n <= 1) {
            return n;
        }
        return f(n - 1) + f(n - 2);
    }

    static String g(String s) {
        if (s.length() <= 1) {
            return s;
        }
        return g(s.substring(1)) + s.charAt(0);
    }

    static void h(int n) {
        if (n == 0) {
            return;
        }
        System.out.print(n + " ");
        h(n - 1);
        System.out.print(n + " ");
    }

    static int power(int b, int e) {
        if (e == 0) {
            return 1;
        }
        int half = power(b, e / 2);
        return e % 2 == 0 ? half * half : half * half * b;
    }

    static void subsets(String s, int i, String cur) {
        if (i == s.length()) {
            System.out.print("[" + cur + "]");
            return;
        }
        subsets(s, i + 1, cur);
        subsets(s, i + 1, cur + s.charAt(i));
    }

    public static void main(String[] args) {
        System.out.println(f(6) + " " + calls);
        System.out.println(g("abcd"));
        h(3);
        System.out.println();
        System.out.println(power(3, 5));
        subsets("ab", 0, "");
        System.out.println();
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Four recursive helpers must produce: `sumDigits(1234)` returns `10`; `isPal("racecar", 0, 6)` returns `true`; `fact(5)` returns `120`; `printBinary(6)` prints `110`. The program prints these four results, one per line.

The code below has at least four bugs (stack overflows or wrong results). Identify each, explain it, and give the corrected program.

```java
public class Main {
    static int sumDigits(int n) {
        return n % 10 + sumDigits(n / 10);
    }

    static boolean isPal(String s, int i, int j) {
        if (i >= j) {
            return true;
        }
        if (s.charAt(i) != s.charAt(j)) {
            return false;
        }
        return isPal(s, i + 1, j);
    }

    static int fact(int n) {
        if (n == 0) {
            return 1;
        }
        return n * fact(n--);
    }

    static void printBinary(int n) {
        if (n > 0) {
            System.out.print(n % 2);
            printBinary(n / 2);
        }
    }

    public static void main(String[] args) {
        System.out.println(sumDigits(1234));
        System.out.println(isPal("racecar", 0, 6));
        System.out.println(fact(5));
        printBinary(6);
        System.out.println();
    }
}
```

---

<a id="day21"></a>
## Day 21 — Matrices (2D Arrays)

**Learning objectives:** Traverse grids by rows, columns, and layers; transform matrices in place; understand that a 2D array is an array of arrays (reference semantics, jagged arrays).

### Question 1: Mixed Real-World Scenario
**Title:** Cinema Seat Allocator

**Problem statement:** A cinema hall is a grid of seats: `.` is a free seat and `X` is taken. A request asks for `k` seats side by side in the same row. Allocate them in the first row (top to bottom) that has `k` consecutive free seats, using the leftmost such block, and mark them `X`. Print the allocated block or `Not available`. After all requests, print the final seating grid.

**Real-world context:** Online booking sites seat groups together whenever possible.

**Input requirements:** Line 1: `r c`. Next `r` lines: each a string of `c` characters (`.` or `X`). Next line: `q`. Next `q` lines: one integer `k` each.

**Expected output:** For each request `Row <i> Seats <a>-<b>` (1-based row and seat numbers) or `Not available`; then the `r` rows of the final grid.

**Sample cases:**
```
Input:
3 5
..X..
.....
X...X
3
3
4
2
Output:
Row 2 Seats 1-3
Not available
Row 1 Seats 1-2
XXX..
XXX..
X...X
```
```
Input:
1 3
...
2
3
1
Output:
Row 1 Seats 1-3
Not available
XXX
```

**Constraints:** `1 <= r, c <= 100`; `1 <= q <= 1000`; `1 <= k <= c`. Edge cases: `k` larger than any free run; a row that is completely free; a request for `k = 1`.

### Question 2: Interview-Based Coding
**Title:** Spiral Order Traversal

**Problem statement:** Print the elements of an `r x c` matrix in clockwise spiral order, starting at the top-left corner: across the top row, down the right column, back along the bottom row, up the left column, and then inward.

**Input and output requirements:** Line 1: `r c`. Next `r` lines: `c` integers each. Print all `r * c` elements in spiral order on one line, separated by spaces.

**Sample cases:**
```
Input:
3 3
1 2 3
4 5 6
7 8 9
Output:
1 2 3 6 9 8 7 4 5
```
```
Input:
2 3
1 2 3
4 5 6
Output:
1 2 3 6 5 4
```

**Constraints and edge cases:** `1 <= r, c <= 200`. A single row (`1 4`), a single column (`4 1`), and a `1 x 1` matrix must work; no element may be printed twice.

### Question 3: Company-Style Programming
**Title:** Image Rotation by Quarter Turns

**Problem statement:** A photo app stores a square grayscale image as an `n x n` matrix. Rotate it clockwise by `k` quarter turns (90 degrees each) and print it. `k` can be very large, so reduce it sensibly first. Implement a single rotation that works with `O(1)` extra space (in place), and apply it as many times as needed.

**Input and output requirements:** Line 1: `n k`. Next `n` lines: `n` integers each. Print the rotated matrix, one row per line, values separated by single spaces.

**Sample cases:**
```
Input:
3 1
1 2 3
4 5 6
7 8 9
Output:
7 4 1
8 5 2
9 6 3
```
```
Input:
2 2
1 2
3 4
Output:
4 3
2 1
```

**Constraints and edge cases:** `1 <= n <= 200`; `0 <= k <= 10^9`. Test `k = 0`, `k = 4` (unchanged), and `n = 1`.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.*;

public class Main {
    public static void main(String[] args) {
        int[][] g = new int[3][4];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                g[i][j] = i * j + (i > j ? 1 : 0);
            }
        }
        System.out.println(Arrays.toString(g[2]) + " " + g.length + " " + g[0].length);

        int[][] copy = g.clone();
        copy[1][1] = 99;
        copy[0] = new int[]{7};
        System.out.println(g[1][1] + " " + g[0].length + " " + copy[0].length);

        int[][] deep = new int[3][];
        for (int i = 0; i < 3; i++) {
            deep[i] = Arrays.copyOf(g[i], g[i].length);
        }
        deep[2][3] = -1;
        System.out.println(g[2][3] + " " + deep[2][3]);

        int[][] jag = new int[4][];
        for (int i = 0; i < jag.length; i++) {
            jag[i] = new int[i + 1];
            jag[i][0] = jag[i][i] = 1;
            for (int j = 1; j < i; j++) {
                jag[i][j] = jag[i - 1][j - 1] + jag[i - 1][j];
            }
        }
        System.out.println(Arrays.deepToString(jag));

        int sum = 0;
        for (int i = 0; i < 3; i++) {
            sum += g[i][i] + g[i][3 - i];
        }
        System.out.println(sum);

        char[][] board = new char[2][3];
        System.out.println((int) board[1][2] + " " + (board[0][0] == '\u0000'));

        int[][] t = new int[2][2];
        t[0] = t[1];
        t[0][0] = 5;
        System.out.println(Arrays.deepToString(t));
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Transpose a square matrix in place, compute the column sums of a (possibly non-square) matrix, and test whether a square matrix is symmetric. Expected output:
```
[1, 4, 7]
[2, 5, 8]
[3, 6, 9]
[5, 7, 9]
true
false
```

The code below has at least four bugs (some crash, some give wrong answers). Identify each, explain it, and give the corrected program.

```java
import java.util.*;

public class Main {
    static void transpose(int[][] m) {
        int n = m.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int t = m[i][j];
                m[i][j] = m[j][i];
                m[j][i] = t;
            }
        }
    }

    static int[] columnSums(int[][] m) {
        int[] sums = new int[m.length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                sums[j] += m[i][j];
            }
        }
        return sums;
    }

    static boolean isSymmetric(int[][] m) {
        for (int i = 0; i <= m.length; i++) {
            for (int j = 0; j < i; j++) {
                if (m[i][j] != m[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        int[][] a = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        transpose(a);
        for (int[] row : a) {
            System.out.println(Arrays.toString(row));
        }
        int[][] b = {{1, 2, 3}, {4, 5, 6}};
        System.out.println(Arrays.toString(columnSums(b)));
        System.out.println(isSymmetric(new int[][]{{1, 2}, {2, 1}}));
        System.out.println(isSymmetric(new int[][]{{1, 2}, {3, 1}}));
    }
}
```

---

<a id="day22"></a>
## Day 22 — Custom Exceptions and try-with-resources

**Learning objectives:** Design checked and unchecked custom exceptions, choose what to catch and where, use multi-catch and `finally`, and manage resources with `AutoCloseable` and try-with-resources (including suppressed exceptions).

### Question 1: Mixed Real-World Scenario
**Title:** ATM Withdrawals with Custom Exceptions

**Problem statement:** An ATM `withdraw(int amount)` method signals failures with three custom **checked** exceptions (all extending `Exception`), checked in this order:
1. `InvalidAmountException`: the amount is not a positive multiple of 100. Message: `Amount must be a positive multiple of 100`
2. `InsufficientFundsException`: the amount is greater than the balance. Message: `Short by <amount - balance>`
3. `DailyLimitExceededException`: today's withdrawals plus this amount would exceed the daily limit of 3000. Message: `Remaining limit <3000 - withdrawnToday>`

A successful withdrawal reduces the balance and adds to today's total. Failed attempts change nothing. `main` catches the exceptions and prints the result.

**Real-world context:** Banking code reports precisely why a transaction failed, so the customer and support staff can act.

**Input requirements:** Line 1: opening balance. Line 2: `n`. Next `n` lines: the requested amount (may be negative or zero).

**Expected output:** `OK <new balance>` or `ERROR <ExceptionClassName>: <message>` per request.

**Sample cases:**
```
Input:
5000
6
300
250
6000
2000
-100
800
Output:
OK 4700
ERROR InvalidAmountException: Amount must be a positive multiple of 100
ERROR InsufficientFundsException: Short by 1300
OK 2700
ERROR InvalidAmountException: Amount must be a positive multiple of 100
ERROR DailyLimitExceededException: Remaining limit 700
```
```
Input:
100
2
100
100
Output:
OK 0
ERROR InsufficientFundsException: Short by 100
```

**Constraints:** `0 <= balance <= 10^6`; `1 <= n <= 1000`; amounts fit in `int`. Use `getClass().getSimpleName()` for the class name.

### Question 2: Interview-Based Coding
**Title:** Command Interpreter Without Pre-Validation

**Problem statement:** A program holds the array `arr = {10, 20, 30, 40, 50}` and executes commands. Do **not** pre-check indices, numbers, or divisors with `if` statements; instead, let Java throw and translate each exception into a message, using `try`/`catch` (a multi-catch is welcome where two exceptions share a message). Commands:
- `GET i` prints `arr[i]`
- `DIV i d` prints `arr[i] / d` (integer division)
- `SET i v` stores `v` into `arr[i]` and prints `OK`
- anything else throws your own `UnknownCommandException` (an unchecked exception you define)

Messages: `Bad number` (a token that is not an integer), `Bad index` (index outside the array), `Divide by zero`, `Unknown command`. For `DIV` and `SET` the tokens are converted left to right (index first, then the second number); the array is accessed after both conversions, and the division happens last.

**Input and output requirements:** Line 1: `n`. Next `n` lines: a command. Every line has the number of tokens its command needs (unknown commands have one or two tokens). Print one result per command.

**Sample cases:**
```
Input:
6
GET 2
GET 7
DIV 1 0
DIV 4 5
SET 0 abc
SET 0 99
Output:
30
Bad index
Divide by zero
10
Bad number
OK
```
```
Input:
4
GET -1
FOO 1
GET x
SET 4 7
Output:
Bad index
Unknown command
Bad number
OK
```

**Constraints and edge cases:** `1 <= n <= 1000`; numeric tokens fit in `int` or are not numbers (a token such as `99999999999` counts as `Bad number`). The array changes persist between commands. In `SET 0 abc` the array must not be modified.

### Question 3: Company-Style Programming
**Title:** Resource Lifecycle Tracer

**Problem statement:** Write a class `Res implements AutoCloseable` with a name, a flag `failOpen`, and a flag `failClose`. Its constructor throws `RuntimeException("open <name> failed")` **before** printing anything if `failOpen` is set; otherwise it prints `open <name>`. Its `close()` prints `close <name>` and then, if `failClose` is set, throws `RuntimeException("close <name> failed")`. For each test case run exactly this structure:

```
try (Res a = <A>; Res b = <B>) {
    print "body"
    if (bodyFails) throw new IllegalStateException("body failed");
} catch (Exception e) {
    print "caught: " + e.getMessage()
    for each suppressed exception s of e (in order): print "suppressed: " + s.getMessage()
} finally {
    print "finally"
}
```
Resource `A` never fails to open; `B` fails to open when `openBFails` is set. After each test case print `---`.

**Input and output requirements:** Line 1: `t`, the number of test cases. Next `t` lines: `bodyFails openBFails closeAFails closeBFails`, each `0` or `1`. Print the trace for every case.

**Sample cases:**
```
Input:
3
0 0 0 0
1 0 1 1
0 1 0 0
Output:
open A
open B
body
close B
close A
finally
---
open A
open B
body
close B
close A
caught: body failed
suppressed: close B failed
suppressed: close A failed
finally
---
open A
close A
caught: open B failed
finally
---
```
```
Input:
2
0 0 1 0
0 0 1 1
Output:
open A
open B
body
close B
close A
caught: close A failed
finally
---
open A
open B
body
close B
close A
caught: close B failed
suppressed: close A failed
finally
---
```

**Constraints and edge cases:** `1 <= t <= 16`; all 16 combinations of the four flags are possible. If `openBFails = 1`, then `B` is never opened, so its `closeBFails` flag has no effect.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
public class Main {
    static int f() {
        try {
            return 1;
        } finally {
            System.out.println("f finally");
        }
    }

    @SuppressWarnings("finally")
    static int g() {
        try {
            throw new RuntimeException("x");
        } catch (RuntimeException e) {
            return 2;
        } finally {
            return 3;
        }
    }

    static int h() {
        int x = 10;
        try {
            return x;
        } finally {
            x = 20;
        }
    }

    static String k(int n) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("t");
            if (n == 1) {
                throw new IllegalArgumentException("bad");
            }
            if (n == 2) {
                throw new IllegalStateException("worse");
            }
            sb.append("u");
        } catch (IllegalArgumentException e) {
            sb.append("c1");
        } catch (RuntimeException e) {
            sb.append("c2");
            return sb.append("r").toString();
        } finally {
            sb.append("f");
        }
        return sb.toString();
    }

    static class Res implements AutoCloseable {
        String n;

        Res(String n) {
            this.n = n;
            System.out.println("open " + n);
        }

        public void close() {
            System.out.println("close " + n);
            throw new IllegalStateException("close-" + n);
        }
    }

    public static void main(String[] args) {
        System.out.println(f());
        System.out.println(g());
        System.out.println(h());
        System.out.println(k(0) + " " + k(1) + " " + k(2));
        try (Res a = new Res("a"); Res b = new Res("b")) {
            throw new RuntimeException("body");
        } catch (Exception e) {
            System.out.println(e.getMessage() + " " + e.getSuppressed().length + " "
                    + e.getSuppressed()[0].getMessage());
        }
    }
}
```

### Question 5: Debug the Code
**What the program should do:** A `Session` resource must be opened, used, and always closed automatically (try-with-resources). `check(age)` throws a **checked** `InvalidAgeException` (carrying a proper message) for ages below 0 or above 150. Expected output:
```
open
Age ok: 30
close
Invalid: Invalid age: 200
```

The code below has at least four bugs. Identify each, explain it, and give the corrected program.

```java
class InvalidAgeException extends Exception {
    InvalidAgeException(String msg) {
    }
}

class Session {
    void open() {
        System.out.println("open");
    }

    void close() {
        System.out.println("close");
    }
}

public class Main {
    static void check(int age) {
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
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InvalidAgeException e) {
            System.out.println("Invalid: " + e.getMessage());
        }
    }
}
```

---

<a id="day23"></a>
## Day 23 — File I/O

**Learning objectives:** Write and read text files with `BufferedWriter`, `BufferedReader`, `PrintWriter`, and `java.nio.file.Files`; close files with try-with-resources; parse simple CSV and `key=value` data; handle `IOException`. In these exercises the program first **creates its own file** from the data it reads on standard input, so no external file is needed.

### Question 1: Mixed Real-World Scenario
**Title:** Expense CSV Report

**Problem statement:** A personal-finance tool saves expense records to a CSV file and then builds a summary. The program must: (1) read `n` lines from standard input and write them, unchanged, to a file `expenses.csv` after the header line `date,category,amount`; (2) read `expenses.csv` back, skipping the header, and add up the amounts per category (a line is valid only if it has exactly 3 comma-separated fields and the third is an integer; invalid lines are counted and skipped); (3) write the totals to `summary.txt` as `category: total` lines sorted by category; (4) read `summary.txt` back and print it, followed by the skipped count and the number of lines in `expenses.csv`.

**Real-world context:** Budgeting apps keep raw records and generate a separate report file for the user.

**Input requirements:** Line 1: `n`. Next `n` lines: one raw record each (they may be malformed).

**Expected output:** The lines of `summary.txt`, then `Skipped: <count>` and `File lines: <lines in expenses.csv including the header>`.

**Sample cases:**
```
Input:
5
2024-01-02,food,250
2024-01-03,travel,1200
2024-01-05,food,100
bad line
2024-01-06,travel,abc
Output:
food: 350
travel: 1200
Skipped: 2
File lines: 6
```
```
Input:
2
2024-02-01,rent,9000
2024-02-02,rent,9000
Output:
rent: 18000
Skipped: 0
File lines: 3
```

**Constraints:** `1 <= n <= 5000`; amounts fit in `int` and each category total fits in `long`; category names contain no commas. If there are no valid records, `summary.txt` is empty and nothing is printed from it. Use try-with-resources for every reader and writer.

### Question 2: Interview-Based Coding
**Title:** File Tail

**Problem statement:** Implement the equivalent of `tail -n k`: print the last `k` lines of a file. The program writes the `n` input lines to `input.txt`, then reads the file **line by line, once**, keeping at most `k` lines in memory (use an `ArrayDeque`), and prints the last `k` lines (or all lines if the file has fewer).

**Input and output requirements:** Line 1: `n k`. Next `n` lines: the file contents (lines may contain spaces and may be empty). Print the last `min(n, k)` lines in their original order.

**Sample cases:**
```
Input:
5 2
alpha
beta
gamma
delta
epsilon
Output:
delta
epsilon
```
```
Input:
2 10
first line
second line
Output:
first line
second line
```

**Constraints and edge cases:** `1 <= n <= 100000`; `1 <= k <= 100000`. An empty line is a real line and counts. Do not call `Files.readAllLines`.

### Question 3: Company-Style Programming
**Title:** Configuration File Loader

**Problem statement:** An application reads settings from a `key=value` text file. The program writes the `n` input lines to `app.conf`, loads the file, and answers queries. Parsing rules: lines that are blank (after trimming) or start with `#` are ignored; every other line is split at the **first** `=`; the key and the value are trimmed; later definitions of the same key override earlier ones. Lines without `=` are ignored.

**Input and output requirements:** Line 1: `n`. Next `n` lines: the file contents. Next line: `q`. Next `q` lines: `key` or `key default`. For each query print the value, else the default if one was given, else `MISSING`.

**Sample cases:**
```
Input:
5
# server settings
host = localhost
port=8080

host = example.com
4
host
port
timeout 30
mode
Output:
example.com
8080
30
MISSING
```
```
Input:
2
url=http://x.com/?a=1
name =  spaced value  
2
url
name
Output:
http://x.com/?a=1
spaced value
```

**Constraints and edge cases:** `1 <= n, q <= 1000`; keys contain no spaces. Test a value that itself contains `=`, a key with an empty value (`k=` gives an empty line as output), and a comment line that contains `=`.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program. (It creates a temporary file, so assume the file operations succeed. Line separators written by `newLine()` and `%n` are whatever the platform uses; the reading code below treats both styles identically.)

```java
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Path p = Files.createTempFile("demo", ".txt");
        try (BufferedWriter w = Files.newBufferedWriter(p)) {
            w.write("one");
            w.newLine();
            w.write("two\nthree");
            w.newLine();
        }
        List<String> lines = Files.readAllLines(p);
        System.out.println(lines.size() + " " + lines);

        try (BufferedWriter w = Files.newBufferedWriter(p, StandardOpenOption.APPEND)) {
            w.write("four");
        }
        try (BufferedReader r = Files.newBufferedReader(p)) {
            String line;
            int count = 0;
            while ((line = r.readLine()) != null) {
                count += line.length();
                System.out.print(line.charAt(0));
            }
            System.out.println(" " + count);
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(p.toFile()))) {
            pw.printf("%d,%s%n", 7, "x");
            pw.print("end");
        }
        try (Scanner sc = new Scanner(p)) {
            sc.useDelimiter(",|\\R");
            StringBuilder sb = new StringBuilder();
            while (sc.hasNext()) {
                sb.append("<").append(sc.next()).append(">");
            }
            System.out.println(sb);
        }
        System.out.println(Files.exists(p));
        Files.delete(p);
        System.out.println(Files.exists(p));
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Write the three lines `asha`, `ravi`, `meena` to `names.txt`, read the file back, print each name in upper case, and finally print `Lines: 3`. Expected output:
```
ASHA
RAVI
MEENA
Lines: 3
```

The code below has at least four bugs (compile-time and run-time). Identify each, explain it, and give the corrected program.

```java
import java.io.*;

public class Main {
    public static void main(String[] args) {
        FileWriter fw = new FileWriter("names.txt");
        fw.write("asha\nravi\nmeena\n");

        BufferedReader br = new BufferedReader(new FileReader("names.txt"));
        String line;
        int count = 0;
        while (line = br.readLine() != null) {
            System.out.println(line.toUpperCase());
            count++;
        }
        System.out.println("Lines: " + count);
    }
}
```

---

<a id="day24"></a>
## Day 24 — Java 17 Features

**Learning objectives:** Use `record`s (compact constructors, auto-generated `equals`/`hashCode`/`toString`), switch expressions with `->` and `yield`, text blocks, `var`, pattern matching for `instanceof`, and `sealed` interfaces.

### Question 1: Mixed Real-World Scenario
**Title:** Parcel Tracking Events

**Problem statement:** A courier logs tracking events as `Event(id, status, hour)` — define `Event` as a **record** and `Status` as an enum (`PICKED`, `IN_TRANSIT`, `OUT_FOR_DELIVERY`, `DELIVERED`, `FAILED`). For each parcel the **latest** event is the one with the greatest hour (if two events have the same hour, the one that appears later in the input wins). Use a **switch expression** to classify the latest status as `DONE` (`DELIVERED`), `PROBLEM` (`FAILED`), or `PENDING` (everything else). Report each parcel's latest status and the class counts.

**Real-world context:** Tracking pages show only the latest status of each parcel, even when events arrive out of order.

**Input requirements:** Line 1: `n`. Next `n` lines: `id STATUS hour` (hour is an integer).

**Expected output:** One line per parcel in order of first appearance: `<id>: <STATUS> at <hour>`; then `Delivered: <a> Failed: <b> Pending: <c>`.

**Sample cases:**
```
Input:
6
P1 PICKED 8
P2 PICKED 9
P1 IN_TRANSIT 12
P2 FAILED 15
P1 DELIVERED 18
P3 PICKED 10
Output:
P1: DELIVERED at 18
P2: FAILED at 15
P3: PICKED at 10
Delivered: 1 Failed: 1 Pending: 1
```
```
Input:
2
P9 DELIVERED 20
P9 PICKED 5
Output:
P9: DELIVERED at 20
Delivered: 1 Failed: 0 Pending: 0
```

**Constraints:** `1 <= n <= 10000`; `0 <= hour <= 10^6`; ids are single words.

### Question 2: Interview-Based Coding
**Title:** Shapes with Sealed Interface and Records

**Problem statement:** Declare `sealed interface Shape permits Circle, Rectangle, Square` and three records: `Circle(double r)`, `Rectangle(double w, double h)`, `Square(double side)`. Write an `area(Shape)` method that uses `instanceof` pattern matching (`o instanceof Circle c`). For each input shape print the record's own `toString()` and its area; then print the total area and the number of **duplicate** shapes, meaning shapes equal (by record equality) to an earlier shape. Use a `HashSet` for the duplicate check.

**Input and output requirements:** Line 1: `n`. Next `n` lines: `C r`, `R w h`, or `S side` (decimal numbers). Output per shape: `<toString> area=<area with 2 decimals>`. Then `Total area: <sum with 2 decimals>` and `Duplicates: <count>`.

**Sample cases:**
```
Input:
4
C 1
R 2 3
S 2
C 1
Output:
Circle[r=1.0] area=3.14
Rectangle[w=2.0, h=3.0] area=6.00
Square[side=2.0] area=4.00
Circle[r=1.0] area=3.14
Total area: 16.28
Duplicates: 1
```
```
Input:
3
R 0.5 4
R 0.5 4
R 4 0.5
Output:
Rectangle[w=0.5, h=4.0] area=2.00
Rectangle[w=0.5, h=4.0] area=2.00
Rectangle[w=4.0, h=0.5] area=2.00
Total area: 6.00
Duplicates: 1
```

**Constraints and edge cases:** `1 <= n <= 1000`; dimensions are positive. A `Rectangle(2, 3)` and `Rectangle(3, 2)` are different records. Use `Math.PI` for the circle.

### Question 3: Company-Style Programming
**Title:** Invoice Formatter

**Problem statement:** Print an invoice using Java 17 text. Use a **text block** for the fixed header and separator lines, `String.format` (or `formatted`) with the pattern `%-8s x%-2d @ %5d = %6d` for each item line, and `strip()`/`isBlank()` to skip empty input lines. Each input line has the form `item,qty,price` (all integers except the name). The line total is `qty * price`.

**Input and output requirements:** Line 1: `n`. Next `n` lines: `item,qty,price` or a blank line. Output:
```
INVOICE
-------
<one formatted line per item>
-------
TOTAL: <sum of line totals>
```

**Sample cases:**
```
Input:
2
pen,3,10
book,1,250
Output:
INVOICE
-------
pen      x3  @    10 =     30
book     x1  @   250 =    250
-------
TOTAL: 280
```
```
Input:
3
notebook,12,45

cup,2,15
Output:
INVOICE
-------
notebook x12 @    45 =    540
cup      x2  @    15 =     30
-------
TOTAL: 570
```

**Constraints and edge cases:** `1 <= n <= 1000`; names have at most 8 characters in the tests; `qty * price` fits in `int`. If every line is blank, print the header, the two separators, and `TOTAL: 0`.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program. (The text block contains a tab character from `\t`.)

```java
import java.util.*;

public class Main {
    record Point(int x, int y) {
        Point {
            if (x < 0) {
                x = 0;
            }
        }

        Point(int v) {
            this(v, v);
        }

        int dist() {
            return x + y;
        }

        static Point origin() {
            return new Point(0, 0);
        }
    }

    sealed interface Shape permits Sq, Rect {}

    record Sq(int s) implements Shape {}

    record Rect(int w, int h) implements Shape {}

    static String describe(Object o) {
        if (o instanceof Sq sq && sq.s() > 2) {
            return "big square " + sq.s();
        } else if (o instanceof Sq sq) {
            return "square " + sq.s();
        } else if (o instanceof Rect r) {
            return "rect " + r.w() * r.h();
        }
        return "other";
    }

    static int days(String m, boolean leap) {
        return switch (m) {
            case "feb" -> leap ? 29 : 28;
            case "apr", "jun", "sep", "nov" -> 30;
            default -> {
                int d = 31;
                yield d;
            }
        };
    }

    public static void main(String[] args) {
        var p = new Point(-5, 3);
        var q = new Point(4);
        System.out.println(p + " " + q + " " + p.dist() + " " + p.equals(new Point(0, 3)));
        System.out.println(Point.origin().hashCode() == new Point(0, 0).hashCode());
        System.out.println(describe(new Sq(3)) + ", " + describe(new Sq(1)) + ", "
                + describe(new Rect(2, 5)) + ", " + describe("x"));
        System.out.println(days("feb", true) + " " + days("jun", false) + " " + days("jan", false));

        String text = """
            Hello,
              "World"\tend \
            joined
            """;
        System.out.print(text);
        System.out.println(text.length() + " " + text.lines().count());

        var list = new ArrayList<Map<String, Integer>>();
        list.add(Map.of("a", 1));
        for (var m : list) {
            System.out.println(m.get("a") + 1);
        }
        int k = 3;
        String size = switch (k % 3) {
            case 0 -> "zero";
            case 1 -> "one";
            default -> "two";
        };
        System.out.println(size);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** `Item` is an immutable record with a name and a quantity. `tier(qty)` returns `"small"` for quantities below 10, `"medium"` for 10 to 19, and `"large"` for 20 or more. The program prints the text block `Items:` / `pen and box` and then `pen small` and `box medium`.

The code below has at least five bugs (all compile-time). Identify each, explain it, and give the corrected program.

```java
public class Main {
    record Item(String name, int qty) {
        int total;

        void setQty(int q) {
            this.qty = q;
        }
    }

    static String tier(int qty) {
        return switch (qty / 10) {
            case 0 -> "small";
            case 1 -> "medium";
        };
    }

    public static void main(String[] args) {
        var items;
        items = new Item[]{new Item("pen", 5), new Item("box", 15)};
        var msg = """Items:
            pen and box""";
        System.out.println(msg);
        for (Item it : items) {
            System.out.println(it.name() + " " + tier(it.qty()));
        }
    }
}
```

---

<a id="day25"></a>
## Day 25 — Lambdas and Streams

**Learning objectives:** Write lambdas and method references for the core functional interfaces (`Function`, `Predicate`, `Supplier`, `Consumer`, `Comparator`), build stream pipelines (`filter`, `map`, `sorted`, `distinct`, `limit`, `reduce`, `collect`), group and join with `Collectors`, and know the rules (effectively final variables, streams are single-use and lazy).

### Question 1: Mixed Real-World Scenario
**Title:** Department Salary Analytics

**Problem statement:** HR has employee records `name department salary`. Using streams and collectors, report for each department (sorted by department name): the number of employees, the average salary, and the name of the highest-paid employee (the first one in the input if tied). Then list, alphabetically, the employees whose salary is strictly greater than the overall average.

**Real-world context:** Compensation reviews start with department-level summaries and a list of above-average earners.

**Input requirements:** Line 1: `n`. Next `n` lines: `name dept salary` (single words, integer salary).

**Expected output:** One line per department: `<dept>: count=<c> avg=<average with 2 decimals> top=<name>`. Then `Above average: <names joined by ", ">`, or `Above average: none`.

**Sample cases:**
```
Input:
5
Asha ENG 90000
Ravi ENG 70000
Meena HR 50000
Joy HR 60000
Kim OPS 40000
Output:
ENG: count=2 avg=80000.00 top=Asha
HR: count=2 avg=55000.00 top=Joy
OPS: count=1 avg=40000.00 top=Kim
Above average: Asha, Ravi
```
```
Input:
2
Zed A 100
Amy A 100
Output:
A: count=2 avg=100.00 top=Zed
Above average: none
```

**Constraints:** `1 <= n <= 10000`; salaries are positive and fit in `int`. Use a record or class for employees and stream collectors for the grouping.

### Question 2: Interview-Based Coding
**Title:** Stream Pipelines on Words

**Problem statement:** Given a list of words, produce four results, each computed with a stream pipeline (no explicit loops for the computations):
1. all distinct words in natural (alphabetical) order, joined by single spaces;
2. the distinct words grouped by length, lengths ascending, words inside each group in alphabetical order, printed as `length=[w1, w2]` entries separated by single spaces;
3. the longest word (the first one in the input if several are longest);
4. the total number of characters in all words (duplicates included).

**Input and output requirements:** One line of space-separated words (at least one). Output:
```
Distinct sorted: <result 1>
By length: <result 2>
Longest: <result 3>
Total chars: <result 4>
```

**Sample cases:**
```
Input:
pear fig apple fig kiwi plum
Output:
Distinct sorted: apple fig kiwi pear plum
By length: 3=[fig] 4=[kiwi, pear, plum] 5=[apple]
Longest: apple
Total chars: 23
```
```
Input:
a a a
Output:
Distinct sorted: a
By length: 1=[a]
Longest: a
Total chars: 3
```

**Constraints and edge cases:** Up to `100000` words of 1 to 30 lower-case letters. A single word; all words equal; several words tied for longest.

### Question 3: Company-Style Programming
**Title:** Configurable Password Rules

**Problem statement:** A sign-up form validates passwords against a configurable list of rules. Each rule is a `Predicate<String>` created by a lambda from a rule specification. Supported specifications: `MINLEN n` (at least `n` characters), `HASDIGIT` (contains a digit), `HASUPPER` (contains an upper-case letter), `NOSPACE` (contains no space), and `MAXREPEAT k` (no character appears more than `k` times **in a row**). Create the predicates through a lookup table of lambdas (for example a `Map` from rule name to a function that builds the predicate from the optional argument). For each password print `OK` or `FAIL: ` followed by the specifications of all failed rules in configuration order, separated by commas.

**Input and output requirements:** Line 1: `r`. Next `r` lines: a rule specification. Next line: `p`. Next `p` lines: a password (read the whole line; it may contain spaces). Output one result line per password.

**Sample cases:**
```
Input:
3
MINLEN 8
HASDIGIT
HASUPPER
3
Abcdefg1
abcdefgh
Ab1
Output:
OK
FAIL: HASDIGIT,HASUPPER
FAIL: MINLEN 8
```
```
Input:
2
NOSPACE
MAXREPEAT 2
2
ab c
aaab
Output:
FAIL: NOSPACE
FAIL: MAXREPEAT 2
```

**Constraints and edge cases:** `1 <= r <= 10`; `1 <= p <= 1000`; passwords have 1 to 100 characters. Test a password that fails every rule, and a password with exactly `k` repeats (allowed).

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) {
        List<Integer> nums = Arrays.asList(5, 3, 8, 1, 9, 2, 8);
        System.out.println(nums.stream().filter(n -> n % 2 == 0).map(n -> n * n).collect(Collectors.toList()));
        System.out.println(nums.stream().distinct().sorted(Comparator.reverseOrder()).limit(3)
                .collect(Collectors.toList()));
        System.out.println(nums.stream().mapToInt(Integer::intValue).sum() + " "
                + nums.stream().reduce(1, (a, b) -> a * b % 100));
        System.out.println(nums.stream().skip(2).anyMatch(n -> n > 8) + " "
                + nums.stream().allMatch(n -> n > 1) + " " + nums.stream().noneMatch(n -> n > 9));
        Map<Integer, Integer> sums = IntStream.rangeClosed(1, 5).boxed()
                .collect(Collectors.toMap(i -> i % 3, i -> i, Integer::sum, TreeMap::new));
        System.out.println(sums);
        Map<Integer, String> byLen = Stream.of("bb", "a", "ccc", "dd")
                .collect(Collectors.groupingBy(String::length, TreeMap::new, Collectors.joining("+")));
        System.out.println(byLen);
        System.out.println(Stream.of("x", "y", "z").map(String::toUpperCase)
                .collect(Collectors.joining(",", "<", ">")));

        Supplier<List<String>> sup = ArrayList::new;
        List<String> l = sup.get();
        l.add("q");
        BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
        Function<Integer, Integer> twice = x -> x * 2;
        System.out.println(add.andThen(twice).apply(3, 4) + " " + twice.compose(twice).apply(5) + " " + l);

        Predicate<String> empty = String::isEmpty;
        System.out.println(empty.negate().test("") + " " + empty.or(s -> s.length() > 3).test("abcd"));

        int[] counter = {0};
        Runnable r = () -> counter[0]++;
        r.run();
        r.run();
        System.out.println(counter[0]);
        Stream<Integer> st = Stream.of(1, 2, 3).peek(x -> counter[0] += 10);
        System.out.println(counter[0]);
        st.forEach(x -> { });
        System.out.println(counter[0]);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** (1) Print the sum of the squares of the even numbers in `[1, 2, 3, 4, 5, 6]` (`56`). (2) Create a new list of the upper-cased names (`[ASHA, RAVI, MEENA]`) and print it. (3) Print how many names are longer than 4 characters (`1`) and the first such name (`meena`). (4) Define a comparator that orders strings by length.

The code below has at least four bugs (all compile-time or run-time failures). Identify each, explain it, and give the corrected program.

```java
import java.util.*;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) {
        List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5, 6);
        int sum = 0;
        nums.forEach(n -> {
            if (n % 2 == 0) {
                sum += n * n;
            }
        });
        System.out.println(sum);

        List<String> names = Arrays.asList("asha", "ravi", "meena");
        names.stream().map(String::toUpperCase);
        System.out.println(names);

        Stream<String> words = names.stream().filter(w -> w.length() > 4);
        System.out.println(words.count());
        System.out.println(words.findFirst().orElse("none"));

        Comparator<String> byLen = (a, b, c) -> a.length() - b.length();
    }
}
```

---

<a id="day26"></a>
## Day 26 — Hand-Built Linked List, Stack, and Queue

**Learning objectives:** Implement a singly linked list, a stack, and a queue yourself (nodes, head/tail pointers, circular arrays), manipulate references safely (reverse, delete, two-pointer walks), and handle empty and single-element cases.

### Question 1: Mixed Real-World Scenario
**Title:** Browser Back and Forward

**Problem statement:** Simulate a browser's history with **two stacks that you implement yourself** (a `Node`-based stack class; do not use `java.util.Stack`, `Deque`, or `LinkedList`). The browser starts on the page `home`. Commands:
- `VISIT url` goes to a new page: the current page is pushed on the back stack and the forward stack is cleared (no output)
- `BACK` moves to the previous page (the current page is pushed on the forward stack) and prints `Current: <page>`, or `Cannot go back`
- `FORWARD` does the reverse and prints `Current: <page>`, or `Cannot go forward`
- `CURRENT` prints `Current: <page>`

**Real-world context:** Every browser keeps back and forward lists exactly like this.

**Input requirements:** Line 1: `n`. Next `n` lines: a command. URLs are single words.

**Expected output:** Only the outputs described above.

**Sample cases:**
```
Input:
8
VISIT a
VISIT b
BACK
BACK
BACK
FORWARD
VISIT c
FORWARD
Output:
Current: a
Current: home
Cannot go back
Current: a
Cannot go forward
```
```
Input:
3
CURRENT
VISIT x
CURRENT
Output:
Current: home
Current: x
```

**Constraints:** `1 <= n <= 100000`. Edge cases: `BACK` on a fresh browser; `FORWARD` right after a `VISIT`; visiting the same URL twice in a row.

### Question 2: Interview-Based Coding
**Title:** Linked List Toolkit

**Problem statement:** Build a singly linked list from the input using your own `Node` class (no `java.util` list classes for the list itself). Then perform three operations on it, each as its own method:
1. find the **middle** node in a single pass with two pointers (for an even length, the second of the two middle nodes);
2. print the list after removing the `k`-th node **from the end**, also in a single pass, without computing the length first and without changing the original list (build the result through a copy or print directly);
3. reverse the original list in place and print it.

**Input and output requirements:** Line 1: `n`. Line 2: `n` integers. Line 3: `k` (`1 <= k <= n`). Output:
```
Middle: <value>
After removal: <values separated by spaces, or (empty)>
Reversed: <values separated by spaces>
```

**Sample cases:**
```
Input:
5
1 2 3 4 5
2
Output:
Middle: 3
After removal: 1 2 3 5
Reversed: 5 4 3 2 1
```
```
Input:
4
10 20 30 40
4
Output:
Middle: 30
After removal: 20 30 40
Reversed: 40 30 20 10
```

**Constraints and edge cases:** `1 <= n <= 100000`. Test `n = 1` (`After removal: (empty)`), removing the head (`k = n`), and removing the tail (`k = 1`). Your code must not recurse on the list (long lists would overflow the stack).

### Question 3: Company-Style Programming
**Title:** Fixed-Capacity Ticket Queue

**Problem statement:** A support desk has a ticket queue with a fixed capacity. Implement a **circular array queue** yourself (an `int[]` buffer with a head index and a size; no `java.util` queue classes). Commands:
- `ENQ x` adds `x` at the back and prints `OK`, or prints `FULL` if the queue already holds `capacity` items
- `DEQ` removes and prints the front item, or prints `EMPTY`
- `PEEK` prints the front item without removing it, or `EMPTY`
- `SIZE` prints the current number of items

All operations must take constant time, and the buffer must be reused after items are removed (wrap-around).

**Input and output requirements:** Line 1: `capacity`. Line 2: `n`. Next `n` lines: a command. Every command prints exactly one line.

**Sample cases:**
```
Input:
3
9
ENQ 1
ENQ 2
ENQ 3
ENQ 4
DEQ
ENQ 5
PEEK
SIZE
DEQ
Output:
OK
OK
OK
FULL
1
OK
2
3
2
```
```
Input:
2
4
DEQ
ENQ 7
PEEK
DEQ
Output:
EMPTY
OK
7
7
```

**Constraints and edge cases:** `1 <= capacity <= 1000`; `1 <= n <= 100000`; values fit in `int`. Test filling the queue, draining it, and filling it again so the head index wraps around.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
public class Main {
    static class Node {
        int val;
        Node next;

        Node(int v, Node n) {
            val = v;
            next = n;
        }
    }

    static Node push(Node head, int v) {
        return new Node(v, head);
    }

    static Node reverse(Node head) {
        Node prev = null;
        while (head != null) {
            Node nxt = head.next;
            head.next = prev;
            prev = head;
            head = nxt;
        }
        return prev;
    }

    static String str(Node h) {
        StringBuilder sb = new StringBuilder();
        for (; h != null; h = h.next) {
            sb.append(h.val).append(h.next != null ? "->" : "");
        }
        return sb.length() == 0 ? "null" : sb.toString();
    }

    static Node removeAll(Node head, int v) {
        Node dummy = new Node(0, head), cur = dummy;
        while (cur.next != null) {
            if (cur.next.val == v) {
                cur.next = cur.next.next;
            } else {
                cur = cur.next;
            }
        }
        return dummy.next;
    }

    public static void main(String[] args) {
        Node h = null;
        for (int i = 1; i <= 5; i++) {
            h = push(h, i % 4);
        }
        System.out.println(str(h));
        h = removeAll(h, 2);
        System.out.println(str(h));
        Node r = reverse(h);
        System.out.println(str(r) + " " + str(h));

        Node a = new Node(1, null);
        Node b = new Node(2, a);
        a.next = b;
        Node slow = a, fast = a;
        int steps = 0;
        do {
            slow = slow.next;
            fast = fast.next.next;
            steps++;
        } while (slow != fast);
        System.out.println(steps + " " + (slow == fast) + " " + slow.val);
        System.out.println(str(removeAll(null, 1)));
    }
}
```

### Question 5: Debug the Code
**What the program should do:** `ArrayStack` is a fixed-capacity stack of `int`. `push` returns `false` when full. `pop` and `peek` return `-1` on an empty stack. `size()` returns the number of stored items. For capacity 3 the program should print:
```
false
3
2 2
-1 true
```

The code below has at least four bugs (it throws exceptions or returns wrong values). Identify each, explain it, and give the corrected program.

```java
class ArrayStack {
    private int[] data;
    private int top;

    ArrayStack(int capacity) {
        data = new int[capacity];
        top = 0;
    }

    boolean push(int v) {
        if (top == data.length) {
            return false;
        }
        data[top++] = v;
        return true;
    }

    int pop() {
        if (top == 0) {
            return -1;
        }
        return data[top--];
    }

    int peek() {
        return data[top];
    }

    boolean isEmpty() {
        return top == 0;
    }

    int size() {
        return top + 1;
    }
}

public class Main {
    public static void main(String[] args) {
        ArrayStack s = new ArrayStack(3);
        s.push(1);
        s.push(2);
        s.push(3);
        System.out.println(s.push(4));
        System.out.println(s.pop());
        System.out.println(s.peek() + " " + s.size());
        s.pop();
        s.pop();
        System.out.println(s.pop() + " " + s.isEmpty());
    }
}
```

---

<a id="day27"></a>
## Day 27 — Introductory Dynamic Programming (1-D)

**Learning objectives:** Recognize overlapping subproblems, define a state and a recurrence for a one-dimensional table, choose between memoization and bottom-up tabulation, and optimize space when only a few previous states matter.

### Question 1: Mixed Real-World Scenario
**Title:** Cheapest Toll Road Trip

**Problem statement:** A highway has `n` toll plazas in a row; plaza `i` charges `cost[i]`. After paying at a plaza you may drive on to the next plaza or skip exactly one plaza and continue to the one after it (you never pay at a skipped plaza). You may join the highway at plaza `0` or at plaza `1`. The trip ends once you move past the last plaza (you may leave from the last plaza or from the second-to-last plaza). Find the minimum total toll.

**Real-world context:** Drivers choose which toll gates to use on a route to minimize what they pay.

**Input requirements:** Line 1: `n`. Line 2: `n` non-negative integers.

**Expected output:** The minimum total toll.

**Sample cases:**
```
Input:
3
10 15 20
Output:
15
```
```
Input:
10
1 100 1 1 1 100 1 1 100 1
Output:
6
```

**Constraints:** `2 <= n <= 100000`; `0 <= cost[i] <= 1000`. Use `O(n)` time; `O(1)` extra space is possible but not required.

### Question 2: Interview-Based Coding
**Title:** House Robber

**Problem statement:** A row of houses holds known amounts of money. A burglar may not rob two adjacent houses. Find the maximum amount that can be robbed.

**Input and output requirements:** Line 1: `n`. Line 2: `n` non-negative integers. Print the maximum amount.

**Sample cases:**
```
Input:
4
1 2 3 1
Output:
4
```
```
Input:
5
2 7 9 3 1
Output:
12
```

**Constraints and edge cases:** `1 <= n <= 100000`; each amount at most `10^4`. Test `n = 1`, `n = 2`, all zeros, and an array where skipping two houses in a row is best (`5 1 1 5` gives `10`).

### Question 3: Company-Style Programming
**Title:** Coin Change: Fewest Coins and Number of Ways

**Problem statement:** A payment terminal has coins of given denominations (unlimited supply of each). For a target amount, report (1) the **fewest** coins that sum to the amount, or `-1` if it is impossible, and (2) the **number of different combinations** of coins that sum to the amount (the order of coins does not matter).

**Input and output requirements:** Line 1: `amount`. Line 2: `m`. Line 3: `m` distinct positive denominations. Output:
```
Min coins: <count or -1>
Ways: <count>
```

**Sample cases:**
```
Input:
11
3
1 2 5
Output:
Min coins: 3
Ways: 11
```
```
Input:
3
1
2
Output:
Min coins: -1
Ways: 0
```

**Constraints and edge cases:** `1 <= amount <= 1000`; `1 <= m <= 5`; denominations are at most `1000`. The number of ways fits in `long`. Test an amount equal to a single coin, and a denomination larger than the amount.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.*;

public class Main {
    static long[] memo = new long[50];
    static int calls = 0;

    static long fib(int n) {
        calls++;
        if (n < 2) {
            return n;
        }
        if (memo[n] != 0) {
            return memo[n];
        }
        return memo[n] = fib(n - 1) + fib(n - 2);
    }

    static int lis(int[] a) {
        int[] dp = new int[a.length];
        int best = 0;
        for (int i = 0; i < a.length; i++) {
            dp[i] = 1;
            for (int j = 0; j < i; j++) {
                if (a[j] < a[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            best = Math.max(best, dp[i]);
        }
        System.out.println(Arrays.toString(dp));
        return best;
    }

    public static void main(String[] args) {
        System.out.println(fib(10) + " " + calls);

        int[] a = {3, 1, 4, 1, 5, 9, 2, 6};
        System.out.println(lis(a));

        int[] dp = new int[8];
        dp[0] = 1;
        dp[1] = 1;
        for (int i = 2; i < dp.length; i++) {
            dp[i] = dp[i - 1] + dp[i - 2] + (i % 2 == 0 ? 1 : 0);
        }
        System.out.println(Arrays.toString(dp));

        int[] coins = {2, 3};
        int[] ways = new int[7];
        ways[0] = 1;
        for (int c : coins) {
            for (int s = c; s < ways.length; s++) {
                ways[s] += ways[s - c];
            }
        }
        System.out.println(Arrays.toString(ways));
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Three DP helpers: `maxSubarray` returns the largest sum of a non-empty contiguous subarray; `climb(n)` returns the number of ways to climb `n` stairs taking 1 or 2 steps at a time; `minCoins` returns the fewest coins for an amount, or `-1` if impossible. Expected output (one value per line): `-2`, `5`, `1`, `8`, `-1`, `3`.

The code below has at least four bugs (wrong answers or crashes). Identify each, explain it with a failing input, and give the corrected program.

```java
public class Main {
    static int maxSubarray(int[] a) {
        int best = 0, cur = 0;
        for (int x : a) {
            cur = cur + x;
            if (cur < 0) {
                cur = 0;
            }
            best = Math.max(best, cur);
        }
        return best;
    }

    static int climb(int n) {
        int[] dp = new int[n];
        dp[0] = 1;
        dp[1] = 2;
        for (int i = 2; i < n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n];
    }

    static int minCoins(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        for (int a = 1; a <= amount; a++) {
            for (int c : coins) {
                if (a >= c) {
                    dp[a] = Math.min(dp[a], dp[a - c] + 1);
                }
            }
        }
        return dp[amount] == Integer.MAX_VALUE ? -1 : dp[amount];
    }

    public static void main(String[] args) {
        System.out.println(maxSubarray(new int[]{-4, -2, -7}));
        System.out.println(maxSubarray(new int[]{2, -1, 3, -4, 5}));
        System.out.println(climb(1));
        System.out.println(climb(5));
        System.out.println(minCoins(new int[]{2}, 3));
        System.out.println(minCoins(new int[]{1, 2, 5}, 11));
    }
}
```

---

<a id="day28"></a>
## Day 28 — Big-O Reasoning and Optimization

**Learning objectives:** Estimate time and space complexity of loops and recursion, decide when a brute-force approach will be too slow for given constraints, and replace nested loops with counting, hashing, math, or fast exponentiation.

### Question 1: Mixed Real-World Scenario
**Title:** Repeated Transaction Amounts

**Problem statement:** A fraud team wants to know how many **pairs** of transactions in a day have exactly the same amount (positions `i < j` with `a[i] == a[j]`). With up to 200000 transactions, comparing every pair is too slow. Compute the number of matching pairs efficiently, and also print how many pair comparisons the brute-force method would have needed (`n * (n - 1) / 2`).

**Real-world context:** Many identical amounts in a day can indicate automated or fraudulent activity.

**Input requirements:** Line 1: `n`. Line 2: `n` integers (amounts).

**Expected output:**
```
Pairs: <matching pairs>
Brute-force comparisons: <n*(n-1)/2>
```

**Sample cases:**
```
Input:
5
1 2 1 1 2
Output:
Pairs: 4
Brute-force comparisons: 10
```
```
Input:
4
5 5 5 5
Output:
Pairs: 6
Brute-force comparisons: 6
```

**Constraints:** `1 <= n <= 200000`; amounts between `-10^9` and `10^9`. Both counts need `long` (for `n = 200000` they are about `2 * 10^10`). Edge case: `n = 1` gives `0` and `0`.

### Question 2: Interview-Based Coding
**Title:** Fast Modular Exponentiation

**Problem statement:** Compute `a^b mod m` for large exponents. A loop that multiplies `b` times is far too slow when `b` is as large as `10^18`; your solution must take `O(log b)` multiplications. Remember that `a^0 = 1`, except that the answer is always taken modulo `m` (so with `m = 1` the answer is `0`).

**Input and output requirements:** Line 1: `t`. Next `t` lines: `a b m`. Print one result per line.

**Sample cases:**
```
Input:
3
2 10 1000
3 200 13
7 0 5
Output:
24
9
1
```
```
Input:
2
5 3 1
2 1000000000000000000 1000000007
Output:
0
719476260
```

**Constraints and edge cases:** `1 <= t <= 1000`; `0 <= a <= 10^9`; `0 <= b <= 10^18`; `1 <= m <= 10^9` (so a product of two residues fits in `long`). Take `a % m` first. Test `a = 0` with `b = 0` (result `1 % m`).

### Question 3: Company-Style Programming
**Title:** Duplicate and Missing Number

**Problem statement:** An array has `n` elements that should be the numbers `1..n` exactly once each, but one number appears twice and, as a result, one number is missing. Find both. Your solution must run in `O(n)` time and use `O(1)` extra space (no sorting, no hash set, no counting array).

**Input and output requirements:** Line 1: `n`. Line 2: `n` integers. Output:
```
Duplicate: <the repeated number>
Missing: <the missing number>
```

**Sample cases:**
```
Input:
4
1 2 2 4
Output:
Duplicate: 2
Missing: 3
```
```
Input:
3
3 1 3
Output:
Duplicate: 3
Missing: 2
```

**Constraints and edge cases:** `2 <= n <= 100000`; the input is always valid (exactly one duplicated value, which appears exactly twice). Test the missing number being `1` and being `n`. Watch for `int` overflow when summing large `n`.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program. Each line shows a function's return value followed by the number of "operations" (`ops`) counted since the last reset.

```java
public class Main {
    static int ops = 0;

    static int a(int n) {
        int c = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                c++;
                ops++;
            }
        }
        return c;
    }

    static int b(int n) {
        int c = 0;
        for (int i = 1; i < n; i *= 2) {
            c++;
            ops++;
        }
        return c;
    }

    static int c(int n) {
        int c = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < n; j *= 3) {
                c++;
                ops++;
            }
        }
        return c;
    }

    static int d(int n) {
        ops++;
        if (n <= 1) {
            return 1;
        }
        return d(n / 2) + 1;
    }

    static int e(int n) {
        ops++;
        if (n == 0) {
            return 0;
        }
        return e(n / 2) + e(n / 2);
    }

    public static void main(String[] args) {
        ops = 0;
        System.out.println(a(5) + " " + ops);
        ops = 0;
        System.out.println(b(16) + " " + b(17) + " " + ops);
        ops = 0;
        System.out.println(c(4) + " " + ops);
        ops = 0;
        System.out.println(d(16) + " " + ops);
        ops = 0;
        System.out.println(e(8) + " " + ops);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** `countPairs(a, target)` returns the number of pairs of positions `i < j` with `a[i] + a[j] == target`, where the target is a `long` that may exceed the `int` range. Expected output for the three calls: `3`, `3`, `0`.

The code below uses a one-pass hash map and has at least four bugs. Identify each, explain it with a failing input, and give the corrected program.

```java
import java.util.*;

public class Main {
    static int countPairs(int[] a, long target) {
        Map<Integer, Integer> seen = new HashMap<>();
        int count = 0;
        for (int x : a) {
            int need = (int) (target - x);
            if (seen.containsKey(need)) {
                count++;
            }
            seen.put(x, seen.getOrDefault(x, 0) + 1);
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println(countPairs(new int[]{1, 5, 7, -1, 5}, 6));
        System.out.println(countPairs(new int[]{3, 3, 3}, 6));
        System.out.println(countPairs(new int[]{-1294967296, 2000000000}, 5000000000L));
    }
}
```

---

<a id="day29"></a>
## Day 29 — Mixed Revision

**Learning objectives:** Combine OOP design, collections, exceptions, strings, and algorithmic thinking in problems that do not announce their topic, and practice reading code for its exact behavior.

### Question 1: Mixed Real-World Scenario
**Title:** Library Lending Desk

**Problem statement:** Build a small lending system with classes for books and members. Rules: a loan lasts 14 days; every day returned after the due day costs a fine of 2 per day; a member may hold at most 2 books at once; a member cannot borrow a second copy of the same book. Commands:
- `ADD isbn copies` registers a book with the given number of copies (no output)
- `JOIN member` registers a member (no output)
- `BORROW member isbn day` prints `OK due <day + 14>` or the **first** failing reason in this order: `NO SUCH MEMBER`, `NO SUCH BOOK`, `ALREADY BORROWED`, `LIMIT REACHED`, `NO COPIES`
- `RETURN member isbn day` prints `RETURNED fine=<fine>` (fine is `2 * (day - due)` if `day > due`, else `0`) or `NOT BORROWED` (this includes unknown members and books)

Failures change nothing. A returned copy becomes available again.

**Real-world context:** Libraries enforce limits and fines automatically at the desk.

**Input requirements:** Line 1: `n`. Next `n` lines: a command.

**Expected output:** The outputs described above, in order.

**Sample cases:**
```
Input:
10
ADD B1 1
ADD B2 2
JOIN asha
JOIN ravi
BORROW asha B1 1
BORROW ravi B1 2
BORROW asha B1 3
BORROW asha B2 3
RETURN asha B1 20
RETURN ravi B1 5
Output:
OK due 15
NO COPIES
ALREADY BORROWED
OK due 17
RETURNED fine=10
NOT BORROWED
```
```
Input:
5
ADD X 5
JOIN kim
BORROW kim Y 1
BORROW lee X 1
RETURN kim X 3
Output:
NO SUCH BOOK
NO SUCH MEMBER
NOT BORROWED
```

**Constraints:** `1 <= n <= 5000`; days are integers between `0` and `10^6`; ids are single words. Edge cases: a member at the limit asking for a book with no copies left (the limit message wins); returning a book and borrowing it again.

### Question 2: Interview-Based Coding
**Title:** LRU Cache

**Problem statement:** Implement a cache with a fixed capacity that evicts the **least recently used** key when a new key is inserted into a full cache. `GET k` returns the value (or `-1` if absent) and counts as a use. `PUT k v` inserts or updates the key and counts as a use. Both operations must take `O(1)` time on average. You may use `LinkedHashMap` or build your own doubly linked list with a `HashMap`.

**Input and output requirements:** Line 1: `capacity n`. Next `n` lines: `GET k` or `PUT k v`. Print the result of every `GET`.

**Sample cases:**
```
Input:
2 9
PUT 1 1
PUT 2 2
GET 1
PUT 3 3
GET 2
PUT 4 4
GET 1
GET 3
GET 4
Output:
1
-1
-1
3
4
```
```
Input:
1 5
PUT 1 10
PUT 1 20
GET 1
PUT 2 5
GET 1
Output:
20
-1
```

**Constraints and edge cases:** `1 <= capacity <= 1000`; `1 <= n <= 100000`; keys and values fit in `int`. Updating an existing key must not evict anything and must make that key the most recently used.

### Question 3: Company-Style Programming
**Title:** Spreadsheet Column Titles

**Problem statement:** Spreadsheet columns are named `A`, `B`, ..., `Z`, `AA`, `AB`, ..., `AZ`, `BA`, ..., `ZZ`, `AAA`, and so on. Convert in both directions: a positive integer to its column title, and a column title to its number. Each input token is either an integer or an upper-case title; decide which, and convert it to the other form.

**Input and output requirements:** Line 1: `n`. Next `n` lines: one token each. Print the converted value for each.

**Sample cases:**
```
Input:
5
1
28
701
ZY
AB
Output:
A
AB
ZY
701
28
```
```
Input:
3
26
27
FXSHRXW
Output:
Z
AA
2147483647
```

**Constraints and edge cases:** `1 <= n <= 1000`; numbers up to `2147483647`; titles have at most 7 letters. Watch for `Z` (26) and multiples of 26, where a naive base-26 conversion goes wrong.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.*;

public class Main {
    interface Op {
        int apply(int x);
    }

    static abstract class Base {
        static int count = 0;
        final int id = ++count;

        abstract int run(int x);

        public String toString() {
            return getClass().getSimpleName() + "#" + id;
        }
    }

    static class Inc extends Base {
        int run(int x) {
            return x + 1;
        }
    }

    static class Dbl extends Base {
        int run(int x) {
            return x * 2;
        }

        public String toString() {
            return "D" + super.toString();
        }
    }

    static int safe(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return -1;
        } finally {
            Base.count += 10;
        }
    }

    public static void main(String[] args) {
        List<Base> ops = new ArrayList<>(Arrays.asList(new Inc(), new Dbl(), new Inc()));
        int v = 3;
        for (Base b : ops) {
            v = b.run(v);
        }
        System.out.println(v + " " + ops);
        System.out.println(safe("12") + safe("x1") + " " + Base.count);
        Base extra = new Dbl();
        System.out.println(extra + " " + extra.id);

        Map<String, List<Integer>> m = new TreeMap<>();
        String[] keys = {"b", "a", "b", "c", "a", "b"};
        for (int i = 0; i < keys.length; i++) {
            m.computeIfAbsent(keys[i], k -> new ArrayList<>()).add(i);
        }
        System.out.println(m);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, List<Integer>> e : m.entrySet()) {
            sb.append(e.getKey()).append(e.getValue().size());
        }
        System.out.println(sb.reverse());

        Op twice = x -> x * 2;
        Op plus3 = x -> x + 3;
        System.out.println(twice.apply(plus3.apply(1)) + " " + plus3.apply(twice.apply(1)));
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Two account types extend an abstract `Account`. A `Savings` account earns 4% **annual** interest, paid monthly (`balance * 0.04 / 12`); a `Current` account earns nothing. The program sums the monthly interest of all accounts, prints `Total interest: 60.00`, and prints `Current account found` for the account with id `C1`.

The code below has at least four bugs (compile-time, run-time, and logic). Identify each, explain it, and give the corrected program.

```java
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
        return balance * (4 / 100) / 12;
    }
}

class Current extends Account {
    Current(String id, double balance) {
        super(id, balance)
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
        for (int i = 0; i <= accounts.size(); i++) {
            total += accounts.get(i).monthlyInterest();
        }
        System.out.printf("Total interest: %.2f%n", total);
        for (Account a : accounts) {
            if (a.getId() == "C1") {
                System.out.println("Current account found");
            }
        }
    }
}
```

---

<a id="day30"></a>
## Day 30 — Mock Assessment

**Learning objectives:** Work through a full assessment under realistic conditions. Suggested time: 150 minutes. Questions 1 and 3 are multi-step scenarios that combine classes, enums, collections, exceptions, parsing, sorting, and formatting; plan your classes before you type, and test with your own edge cases before moving on.

### Question 1: Mixed Real-World Scenario
**Title:** Hotel Booking Desk (Multi-Step Scenario)

**Problem statement:** Build a hotel booking system with these rules.

*Rooms:* `ROOM id TYPE price` adds a room (`TYPE` is `STANDARD`, `DELUXE`, or `SUITE`; model it as an enum; `price` is per night).

*Booking:* `BOOK guest roomId checkIn nights` reserves the room for the nights `checkIn, checkIn + 1, ..., checkIn + nights - 1`. Checks, in this order: `nights < 1` prints `REJECTED INVALID`; an unknown room prints `REJECTED UNKNOWN ROOM`; an overlap with any **active** booking of the same room prints `REJECTED OVERLAP` (a booking that ends the day another starts does not overlap: nights `[10, 13)` and `[13, 14)` are compatible). Otherwise the booking gets the next id (starting at 1, only successful bookings use ids) and prints `BOOKED <id> payable=<amount>`. The cost is `price * nights`. Stays of 3 to 6 nights get 5% off; stays of 7 or more nights get 15% off (the discount amount is `cost * pct / 100` with integer division). `payable = cost - discount`.

*Cancelling:* `CANCEL id day` cancels an active booking and prints `CANCELLED <id> refund=<amount>`. If `checkIn - day >= 2` the refund is the full payable amount, otherwise half of it (integer division). Unknown or already-cancelled ids print `NOT FOUND`. A cancelled booking no longer blocks its room.

*Report:* `REPORT` prints one line `<roomId> <TYPE> <revenue>` per room in ascending numeric room id, where a room's revenue is the sum over all of its bookings (active or cancelled) of `payable - refund`; then `Total revenue: <sum>`.

**Real-world context:** A front desk system must check availability, price stays, and handle cancellations consistently.

**Input requirements:** Line 1: `n`. Next `n` lines: a command. Ids and days are integers; guest names are single words.

**Expected output:** The outputs described above, in order. `ROOM` prints nothing.

**Sample cases:**
```
Input:
9
ROOM 101 STANDARD 1000
ROOM 202 SUITE 3000
BOOK asha 101 10 3
BOOK ravi 101 12 2
BOOK ravi 101 13 1
BOOK kim 202 5 7
CANCEL 1 9
CANCEL 3 2
REPORT
Output:
BOOKED 1 payable=2850
REJECTED OVERLAP
BOOKED 2 payable=1000
BOOKED 3 payable=17850
CANCELLED 1 refund=1425
CANCELLED 3 refund=17850
101 STANDARD 2425
202 SUITE 0
Total revenue: 2425
```
```
Input:
4
ROOM 1 DELUXE 100
BOOK a 2 1 1
BOOK b 1 1 0
CANCEL 9 1
Output:
REJECTED UNKNOWN ROOM
REJECTED INVALID
NOT FOUND
```

**Constraints:** `1 <= n <= 5000`; prices up to `10^5`; nights up to `365`; days between `0` and `10^6`. Amounts fit in `long`. Edge cases: re-booking a room after cancelling; cancelling twice; a report with no rooms (prints only `Total revenue: 0`).

### Question 2: Interview-Based Coding
**Title:** Longest Palindromic Substring

**Problem statement:** Given a string, find its longest palindromic substring (a contiguous part that reads the same forwards and backwards). If several have the same maximum length, return the one that starts earliest. Aim for `O(n^2)` time and `O(1)` extra space.

**Input and output requirements:** Line 1: `t`. Next `t` lines: a non-empty string of lower-case letters. Print the substring for each.

**Sample cases:**
```
Input:
3
babad
cbbd
a
Output:
bab
bb
a
```
```
Input:
2
forgeeksskeegfor
abc
Output:
geeksskeeg
a
```

**Constraints and edge cases:** `1 <= t <= 100`; string length `1` to `2000`. Test even-length palindromes (`cbbd`), a string with no repeated letters (the first character is the answer), and a string that is entirely a palindrome.

### Question 3: Company-Style Programming
**Title:** Wallet Transaction Audit (Multi-Step Scenario)

**Problem statement:** A payments company audits a day of wallet transactions in four steps.

*Step 1 — Parse.* Each input line must match exactly one of these two patterns (single spaces; `HH` and `MM` are any two digits; `user` and `target` are word characters; `amount` is a positive integer without leading zeros):
- `HH:MM user DEPOSIT amount` or `HH:MM user WITHDRAW amount`
- `HH:MM user TRANSFER target amount`

Any other line is **malformed** and is only counted.

*Step 2 — Apply in input order.* Every user named in a well-formed line (as actor or as transfer target) exists from then on with balance 0. `DEPOSIT` adds to the balance. `WITHDRAW` fails with reason `INSUFFICIENT` if the amount exceeds the balance, otherwise subtracts. `TRANSFER` fails with reason `SELF` if `user` equals `target` (checked first), then with `INSUFFICIENT` if the amount exceeds the sender's balance; otherwise it moves the money. A failed transaction changes no balance.

*Step 3 — Statistics.* Among **successful** transactions, find the hour `HH` with the most transactions (the smaller `HH` on a tie).

*Step 4 — Report.* Print every failed transaction as `FAILED <line number> <reason>` (line numbers start at 1 and count malformed lines too), then `Malformed: <count>`, then `Balances:` followed by one `<user> <balance>` line per user sorted by balance (highest first) and then by name (A to Z), and finally `Busiest hour: <HH> (<count>)`, or `Busiest hour: none` if no transaction succeeded.

**Input and output requirements:** Line 1: `n`. Next `n` lines: transaction lines (they may be malformed and contain spaces).

**Sample cases:**
```
Input:
7
09:15 asha DEPOSIT 500
09:40 ravi DEPOSIT 300
10:05 asha TRANSFER ravi 200
10:10 ravi WITHDRAW 600
10:30 kim WITHDRAW 50
bad line
10:45 ravi TRANSFER ravi 10
Output:
FAILED 4 INSUFFICIENT
FAILED 5 INSUFFICIENT
FAILED 7 SELF
Malformed: 1
Balances:
ravi 500
asha 300
kim 0
Busiest hour: 09 (2)
```
```
Input:
2
bad
10:00 x WITHDRAW 5
Output:
FAILED 2 INSUFFICIENT
Malformed: 1
Balances:
x 0
Busiest hour: none
```

**Constraints:** `1 <= n <= 100000`; amounts fit in `long` and balances never exceed `long`. Edge cases: a transfer to a user that does not yet exist (the target is created and receives the money); a line such as `10:00 a DEPOSIT 0` or `10:00 a DEPOSIT 007` (malformed); two hours tied for busiest.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program. Pay attention to initialization order, `char` arithmetic, integer caching, and operator evaluation.

```java
public class Main {
    static int x = init("x");
    int y = init("y");

    static {
        init("static block");
    }

    {
        init("instance block");
    }

    Main() {
        init("ctor");
    }

    static int init(String s) {
        System.out.println(s);
        return 1;
    }

    public static void main(String[] args) {
        System.out.println("main");
        new Main();

        char c = 'a';
        c += 2;
        int i = c + 1;
        System.out.println(c + " " + i + " " + (char) i + " " + (c + 1) + " " + (char) (c + 1));

        Integer p = 127, q = 127, r = 128, s = 128;
        System.out.println((p == q) + " " + (r == s) + " " + r.equals(s));

        Object o = true ? 1 : "s";
        Object o2 = true ? 1 : 2.0;
        System.out.println(o + " " + o2);

        int n = 0;
        outer:
        for (int a = 0; a < 3; a++) {
            for (int b = 0; b < 3; b++) {
                if (b == 2) {
                    continue outer;
                }
                if (a == 2) {
                    break outer;
                }
                n += a * 10 + b;
            }
        }
        System.out.println(n);

        int k = 5;
        k = k++ + ++k;
        System.out.println(k);
        System.out.println(1 + 2 + "3" + 4 + 5);
        System.out.println(10 / 4 * 2.0 + " " + 10 / 4.0 * 2 + " " + 7 % -3 + " " + -7 % 3);

        long big = Integer.MAX_VALUE + 1;
        long big2 = Integer.MAX_VALUE + 1L;
        System.out.println(big + " " + big2);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** A generic `Inventory` stores `Item`s. `remove(name, amount)` reduces the quantity of the named item and throws `OutOfStockException` if there is not enough. `totalValue()` returns the exact total of `qty * price`. Items sort alphabetically by name, and two items are equal when their names are equal. Expected output:
```
Out of stock: box
Total: 57.5
box 4
pen 7
```

The code below has at least five bugs (compile-time, run-time, and logic). Identify each, explain it, and give the corrected program.

```java
import java.util.*;

class OutOfStockException extends RuntimeException {
    OutOfStockException(String msg) {
        super(msg);
    }
}

class Item implements Comparable<Item> {
    String name;
    int qty;
    double price;

    Item(String name, int qty, double price) {
        this.name = name;
        this.qty = qty;
        this.price = price;
    }

    public int compareTo(Item o) {
        return name - o.name;
    }

    public boolean equals(Object o) {
        return name == ((Item) o).name;
    }
}

class Inventory<T extends Item> {
    private List<T> items = new ArrayList<>();

    void add(T item) {
        items.add(item);
    }

    void remove(String name, int amount) {
        for (T item : items) {
            if (item.name.equals(name)) {
                if (item.qty < amount) {
                    throw new OutOfStockException(name);
                }
                item.qty =- amount;
                return;
            }
        }
    }

    double totalValue() {
        int total = 0;
        for (T item : items) {
            total += item.qty * item.price;
        }
        return total;
    }

    List<T> sorted() {
        Collections.sort(items);
        return items;
    }
}

public class Main {
    public static void main(String[] args) {
        Inventory<Item> inv = new Inventory<>();
        inv.add(new Item("pen", 10, 2.5));
        inv.add(new Item("box", 4, 10.0));
        try {
            inv.remove("pen", 3);
            inv.remove("box", 9);
        } catch (OutOfStockException e) {
            System.out.println("Out of stock: " + e.getMessage());
        }
        System.out.println("Total: " + inv.totalValue());
        for (Item it : inv.sorted()) {
            System.out.println(it.name + " " + it.qty);
        }
    }
}
```

---

*End of workbook — 30 days, 150 questions.*
