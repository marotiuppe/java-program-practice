# 30-Day Java Practice Workbook — Beginner to Intermediate

**Java version: 17** | Level: Beginner | 5 questions per day | 150 questions total | Questions only

## Table of Contents

**Week 1 — Java Fundamentals and Basic Logic**
- [Day 1 — First Programs: Structure, Variables, Data Types](#day1)
- [Day 2 — Scanner Input and Arithmetic](#day2)
- [Day 3 — Type Conversion and Operator Behavior](#day3)
- [Day 4 — Relational and Logical Operators with if-else](#day4)
- [Day 5 — else-if Ladders and Nested Conditions](#day5)
- [Day 6 — switch Statements](#day6)
- [Day 7 — Validation and Decision-Making (Week 1 Review)](#day7)

**Week 2 — Loops, Methods, and Number Problems**
- [Day 8 — for Loops](#day8)
- [Day 9 — while and do-while Loops](#day9)
- [Day 10 — Digit Extraction and Digit Sums](#day10)
- [Day 11 — Reverse and Palindrome Numbers](#day11)
- [Day 12 — Primes and Factors](#day12)
- [Day 13 — Nested Loops, Patterns, and Tables](#day13)
- [Day 14 — Methods and Reuse](#day14)

**Week 3 — Strings, Arrays, and Object-Oriented Programming**
- [Day 15 — String Basics](#day15)
- [Day 16 — Character Counting and Reversing Strings](#day16)
- [Day 17 — Palindromes and Anagrams](#day17)
- [Day 18 — One-Dimensional Arrays](#day18)
- [Day 19 — Searching, Sorting, and Duplicates](#day19)
- [Day 20 — Two-Dimensional Arrays](#day20)
- [Day 21 — Classes, Objects, and Inheritance](#day21)

**Week 4 — Collections, Exceptions, and Interview Problem-Solving**
- [Day 22 — ArrayList](#day22)
- [Day 23 — HashSet and HashMap](#day23)
- [Day 24 — Exception Handling](#day24)
- [Day 25 — File Handling Basics](#day25)
- [Day 26 — Recursion](#day26)
- [Day 27 — Complexity and Optimization](#day27)
- [Day 28 — Multi-Step Business Scenarios](#day28)

**Consolidation and Challenge**
- [Day 29 — Mixed-Topic Revision](#day29)
- [Day 30 — Mock Coding Assessment](#day30)

**Conventions used in this workbook:** programs read from standard input (`Scanner`) and write to standard output unless a question says otherwise. Money and averages are printed with exactly 2 decimal places unless stated. Code snippets assume Java 17 and a class named `Main`.

---

<a id="day1"></a>
## Day 1 — First Programs: Structure, Variables, Data Types

**Learning objectives:** Understand the structure of a Java program (class, `main`, statements), declare variables of different data types, and print formatted output.

### Question 1: Mixed Real-World Scenario
**Title:** Coffee Shop Receipt

**Problem statement:** A coffee shop wants a tiny program that prints a receipt line for one item. In your code, declare variables for the item name (`String`), quantity (`int`), and unit price (`double`). Calculate the total and print the receipt.

**Real-world context:** Billing counters in small shops print an itemized receipt for every sale.

**Input requirements:** No keyboard input yet. Hard-code the values in variables (change them in your code to test each case).

**Expected output:** Three lines in this format, with the total shown with exactly 2 decimal places (`printf` or `String.format` is allowed):
```
Item: <name>
Quantity: <quantity>
Total: <total>
```

**Sample cases:**
```
Values: Pen, 3, 12.50
Output:
Item: Pen
Quantity: 3
Total: 37.50
```
```
Values: Notebook, 4, 45.25
Output:
Item: Notebook
Quantity: 4
Total: 181.00
```

**Constraints:** Quantity is a whole number; price may have decimals.

### Question 2: Interview-Based Coding
**Title:** Swap Two Numbers

**Problem statement:** Two `int` variables `a` and `b` hold values. Swap their values and print them. Write the swap two ways: (1) using a temporary variable, and (2) without using any third variable.

**Input and output requirements:** Hard-code `a` and `b`. Print one line: `a=<value> b=<value>` after swapping.

**Sample cases:**
```
a=10, b=20  ->  a=20 b=10
```
```
a=-5, b=0   ->  a=0 b=-5
```

**Constraints:** Values fit in `int`; assume the values are small enough that arithmetic does not overflow.

### Question 3: Company-Style Programming
**Title:** Average Marks Report

**Problem statement:** A student has three subject marks stored in `int` variables. Print the total marks and the average marks. The average must be an exact decimal value shown with 2 decimal places.

**Input and output requirements:** Hard-code three marks. Output two lines:
```
Total: <total>
Average: <average>
```

**Sample cases:**
```
Marks: 80, 90, 70
Total: 240
Average: 80.00
```
```
Marks: 99, 100, 100
Total: 299
Average: 99.67
```

**Constraints and edge cases:** Each mark is between 0 and 100. Test with marks `0, 0, 0` and `1, 0, 0` as well.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
public class Main {
    public static void main(String[] args) {
        int a = 7;
        int b = 2;
        System.out.println(a + b);
        System.out.println("a + b = " + a + b);
        System.out.println(a / b);
        System.out.println(a % b);
        System.out.println((double) a / b);
        System.out.println("Sum: " + (a + b));
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Store a student's name (`Riya`), age (`20`, a whole number), and pass status (`true`), then print three lines: `Name: Riya`, `Age: 20`, `Passed: true`.

The code below has several compile-time errors. Identify every error, explain why it occurs, and write the corrected program.

```java
public class Main {
    public static void main(String[] args) {
        int age = 20.5;
        String name = 'Riya';
        boolean passed = "true";
        System.out.println("Name: " + name)
        System.out.println("Age: " + age);
        System.out.println("Passed: " + passed);
    }
}
```

---

<a id="day2"></a>
## Day 2 — Scanner Input and Arithmetic

**Learning objectives:** Read integers, decimals, and text with `Scanner`, and use arithmetic operators to build simple calculators.

### Question 1: Mixed Real-World Scenario
**Title:** Restaurant Bill Splitter

**Problem statement:** A group of friends finishes a meal. Read the number of people and the food bill. A 5% GST is added to the bill. Print the final total and how much each person pays if the total is shared equally.

**Real-world context:** Restaurants add tax to the bill, and friends often split the total evenly.

**Input requirements:** First line: number of people (`int`). Second line: bill amount before tax (`double`).

**Expected output:**
```
Total with GST: <total>
Each pays: <share>
```
(both with 2 decimal places)

**Sample cases:**
```
Input:
3
600
Output:
Total with GST: 630.00
Each pays: 210.00
```
```
Input:
2
250
Output:
Total with GST: 262.50
Each pays: 131.25
```

**Constraints:** Number of people is at least 1; bill is 0 or more.

### Question 2: Interview-Based Coding
**Title:** Celsius to Fahrenheit and Kelvin

**Problem statement:** Read a temperature in Celsius (may be decimal or negative). Convert it to Fahrenheit using `F = C × 9 / 5 + 32` and to Kelvin using `K = C + 273.15`.

**Input and output requirements:** Input: one `double`. Output two lines, each with 2 decimal places:
```
Fahrenheit: <value>
Kelvin: <value>
```

**Sample cases:**
```
Input: 100
Output:
Fahrenheit: 212.00
Kelvin: 373.15
```
```
Input: -40
Output:
Fahrenheit: -40.00
Kelvin: 233.15
```

**Constraints:** Celsius is not below -273.15.

### Question 3: Company-Style Programming
**Title:** Cab Fare Estimator

**Problem statement:** A cab company charges a base fare of 40, plus 12 per kilometre, plus 2 per minute of travel time. Read the distance in kilometres (may be decimal) and the travel time in minutes (whole number), then print the fare.

**Input and output requirements:** Input: distance (`double`) on the first line, minutes (`int`) on the second line. Output: `Fare: <amount>` with 2 decimal places.

**Sample cases:**
```
Input:
10.5
25
Output:
Fare: 216.00
```
```
Input:
3
10
Output:
Fare: 96.00
```

**Constraints and edge cases:** Distance and minutes are 0 or more. For `0` km and `0` minutes the fare is just the base fare (`40.00`).

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
public class Main {
    public static void main(String[] args) {
        int x = 10;
        x += 5;
        x -= 3;
        x *= 2;
        x /= 5;
        x %= 3;
        System.out.println(x);
        int y = x++ + ++x;
        System.out.println(x);
        System.out.println(y);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Read two integers and print their sum and their average. For the input `3` and `4`, the output should be `Sum = 7` and `Average = 3.5`.

The program has compile-time errors and at least one logic error. Identify each bug, explain why it occurs, and give the corrected program.

```java
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter first number: ");
        int a = sc.nextint();
        System.out.print("Enter second number: ");
        int b = sc.nextInt();
        int sum = a + b;
        double average = sum / 2;
        System.out.println("Sum = " + Sum);
        System.out.println("Average = " + average);
    }
}
```

---

<a id="day3"></a>
## Day 3 — Type Conversion and Operator Behavior

**Learning objectives:** Understand integer division, the remainder operator `%`, implicit widening, explicit casting, and overflow.

### Question 1: Mixed Real-World Scenario
**Title:** ATM Note Counter

**Problem statement:** An ATM holds only 500 and 100 rupee notes. Read a withdrawal amount (a multiple of 100) and print how many 500 notes and how many 100 notes are dispensed, using as few notes as possible.

**Real-world context:** ATMs decide the combination of notes to dispense for every withdrawal.

**Input requirements:** One `int` amount.

**Expected output:**
```
500 notes: <count>
100 notes: <count>
```

**Sample cases:**
```
Input: 1700
Output:
500 notes: 3
100 notes: 2
```
```
Input: 400
Output:
500 notes: 0
100 notes: 4
```

**Constraints:** 0 ≤ amount ≤ 100000 and the amount is a multiple of 100. Amount `0` must print `0` for both.

### Question 2: Interview-Based Coding
**Title:** Seconds to Hours, Minutes, Seconds

**Problem statement:** Read a total number of seconds and convert it into hours, minutes, and seconds.

**Input and output requirements:** Input: one non-negative `int`. Output: `<h>h <m>m <s>s`.

**Sample cases:**
```
Input: 3725
Output: 1h 2m 5s
```
```
Input: 59
Output: 0h 0m 59s
```

**Constraints:** 0 ≤ seconds ≤ 1000000. Also test `86399` and `0`.

### Question 3: Company-Style Programming
**Title:** Floor Tiling Planner

**Problem statement:** A rectangular floor is `L` cm long and `W` cm wide. Square tiles of side `S` cm are laid without cutting any tile. Print the total number of whole tiles that fit, and the unused leftover length and width of the floor.

**Input and output requirements:** Input: three `int` values `L W S` on one line. Output:
```
Tiles: <total>
Leftover length: <x> cm
Leftover width: <y> cm
```

**Sample cases:**
```
Input: 250 180 30
Output:
Tiles: 48
Leftover length: 10 cm
Leftover width: 0 cm
```
```
Input: 100 100 40
Output:
Tiles: 4
Leftover length: 20 cm
Leftover width: 20 cm
```

**Constraints and edge cases:** All values are positive. A tile larger than the floor in a direction means no tile fits (e.g., `30 50 40` gives `Tiles: 0`, leftover length `30 cm`, leftover width `10 cm`).

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
public class Main {
    public static void main(String[] args) {
        int i = 130;
        byte b = (byte) i;
        char c = 'A';
        c += 2;
        int sum = c + 1;
        double d = 9 / 2;
        float f = 9 / 2.0f;
        int big = 1000000 * 3000;
        System.out.println(b);
        System.out.println(c);
        System.out.println(sum);
        System.out.println(d);
        System.out.println(f);
        System.out.println(big);
        System.out.println((int) -3.99);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Convert 1000 rupees into dollars at a rate of 83.5 rupees per dollar, keeping the decimal part of the result. Also store the large number `5000000000` as the company's yearly revenue and a tax rate of `0.18` in a `float`, then print all three.

The code does not compile. Identify each error, explain it, and provide the corrected code.

```java
public class Main {
    public static void main(String[] args) {
        int rupees = 1000;
        double rate = 83.5;
        int dollars = rupees / rate;
        long revenue = 5000000000;
        float tax = 0.18;
        System.out.println("Dollars: " + dollars);
        System.out.println("Revenue: " + revenue);
        System.out.println("Tax: " + tax);
    }
}
```

---

<a id="day4"></a>
## Day 4 — Relational and Logical Operators with if-else

**Learning objectives:** Combine conditions with `&&`, `||`, and `!`, and make two-way decisions with `if-else`.

### Question 1: Mixed Real-World Scenario
**Title:** Free Delivery Checker

**Problem statement:** An online store charges a delivery fee of 40. Delivery is free if the order amount is 1000 or more, or if the customer is a member and the order amount is 500 or more. Read the order amount and membership status and print the delivery charge.

**Real-world context:** Shopping apps use rules like these to encourage bigger orders and reward members.

**Input requirements:** First line: order amount (`int`). Second line: membership (`true` or `false`, read as `boolean`).

**Expected output:** `Delivery: Free` or `Delivery: 40`

**Sample cases:**
```
Input:
1200
false
Output: Delivery: Free
```
```
Input:
600
true
Output: Delivery: Free
```
```
Input:
600
false
Output: Delivery: 40
```

**Constraints:** Amount is 0 or more. Test the boundaries `499`, `500`, `999`, and `1000`.

### Question 2: Interview-Based Coding
**Title:** Leap Year Check

**Problem statement:** A year is a leap year if it is divisible by 4, except century years (divisible by 100), which must also be divisible by 400. Read a year and print whether it is a leap year.

**Input and output requirements:** Input: one `int`. Output: `Leap Year` or `Not a Leap Year`.

**Sample cases:**
```
Input: 2000
Output: Leap Year
```
```
Input: 1900
Output: Not a Leap Year
```
```
Input: 2024
Output: Leap Year
```

**Constraints:** 1 ≤ year ≤ 9999.

### Question 3: Company-Style Programming
**Title:** Triangle Validator

**Problem statement:** Three side lengths are given. They form a valid triangle only if all sides are positive and the sum of every pair of sides is strictly greater than the third side. Print whether the triangle is valid.

**Input and output requirements:** Input: three `int` values on one line. Output: `Valid` or `Invalid`.

**Sample cases:**
```
Input: 3 4 5
Output: Valid
```
```
Input: 1 2 3
Output: Invalid
```
```
Input: 0 4 5
Output: Invalid
```

**Constraints and edge cases:** Values may be zero or negative (these are invalid). Sides like `2 2 4` form a degenerate triangle and are invalid.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
public class Main {
    public static void main(String[] args) {
        int a = 5, b = 10, c = 15;
        boolean r1 = a < b && b < c;
        boolean r2 = a > b || c == 15;
        boolean r3 = !(a == 5) || (b != 10);
        boolean r4 = a + b > c ^ true;
        System.out.println(r1);
        System.out.println(r2);
        System.out.println(r3);
        System.out.println(r4);
        if (a > 3)
            if (b > 20)
                System.out.println("X");
            else
                System.out.println("Y");
        int count = 0;
        if (a++ > 5 && b++ > 5) {
            count++;
        }
        System.out.println(a + " " + b + " " + count);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Read an integer `n`. Print `Even` or `Odd`. Print `In range 1-99` only if `n` is between 1 and 99 (inclusive). Print `Zero` only if `n` is exactly 0.

The program has compile-time and logic errors. Identify each, explain why it occurs, and give the corrected program.

```java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        if (n % 2 = 0) {
            System.out.println("Even");
        } else {
            System.out.println("Odd");
        }
        if (n > 0 & < 100) {
            System.out.println("In range 1-99");
        }
        if (n == 0);
        {
            System.out.println("Zero");
        }
    }
}
```

---

<a id="day5"></a>
## Day 5 — else-if Ladders and Nested Conditions

**Learning objectives:** Choose between several outcomes with `else-if` chains, nest conditions, and handle boundary values.

### Question 1: Mixed Real-World Scenario
**Title:** Electricity Slab Bill

**Problem statement:** An electricity board charges by slabs: the first 100 units cost 5 per unit, the next 100 units (101–200) cost 7 per unit, and every unit above 200 costs 10 per unit. Read the units consumed and print the bill.

**Real-world context:** Utility companies use slab (tiered) pricing so heavy users pay a higher rate on the extra units only.

**Input requirements:** One `int` (units consumed).

**Expected output:** `Bill: <amount>`

**Sample cases:**
```
Input: 80
Output: Bill: 400
```
```
Input: 150
Output: Bill: 850
```
```
Input: 250
Output: Bill: 1700
```

**Constraints:** Units are 0 or more. Test the boundaries `0`, `100`, `101`, and `200`.

### Question 2: Interview-Based Coding
**Title:** Largest of Three Numbers

**Problem statement:** Read three integers and print the largest one using only conditions (do not use `Math.max` or arrays).

**Input and output requirements:** Input: three `int` values on one line. Output: the largest value.

**Sample cases:**
```
Input: 10 25 7
Output: 25
```
```
Input: 5 5 5
Output: 5
```

**Constraints:** Values may be negative or equal. Also test `-3 -9 -1`.

### Question 3: Company-Style Programming
**Title:** Grade and Scholarship Eligibility

**Problem statement:** Read a student's marks (0–100) and attendance percentage. Grades are: `A` for 90 and above, `B` for 75–89, `C` for 60–74, `D` for 40–59, and `F` below 40. A student is scholarship-eligible only with grade `A` and attendance of 85 or more. If the marks are below 0 or above 100, print only `Invalid marks`.

**Input and output requirements:** Input: marks and attendance (`int`) on one line. Output:
```
Grade: <grade>
Scholarship: <Yes/No>
```

**Sample cases:**
```
Input: 92 90
Output:
Grade: A
Scholarship: Yes
```
```
Input: 92 70
Output:
Grade: A
Scholarship: No
```
```
Input: 105 80
Output: Invalid marks
```

**Constraints and edge cases:** Test marks `89`, `90`, `59`, `40`, `39`, `0`, `100`, and `-1`.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
public class Main {
    public static void main(String[] args) {
        int score = 72;
        String result;
        if (score >= 90) result = "A";
        else if (score >= 80) result = "B";
        else if (score >= 70) result = "C";
        else result = "D";
        System.out.println(result);

        int x = 15;
        if (x > 10) {
            System.out.println("Big");
            if (x % 2 == 0) System.out.println("Even");
            else System.out.println("Odd");
        } else if (x > 5) {
            System.out.println("Medium");
        } else {
            System.out.println("Small");
        }

        int y = 8;
        if (y > 5) {
            y += 2;
        }
        if (y > 8) {
            y *= 2;
        } else if (y > 5) {
            y = 0;
        }
        System.out.println(y);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Calculate a parking fee from the hours parked. 1 hour or less costs 20, up to 3 hours costs 50, up to 6 hours costs 100, and more than 6 hours costs 150. If the hours are 0 or negative the program must print only `Invalid` and no fee.

The program has compile-time and logic errors. Identify each, explain it, and provide the corrected program.

```java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int hours = sc.nextInt();
        int fee;
        if (hours < 0) {
            System.out.println("Invalid");
        } else if (hours <= 1) {
            fee = 20;
        } else if (hours <= 3) {
            fee = 50;
        } else if (hours <= 6) {
            fee = 100;
        } else {
            fee = 150;
        }
        System.out.println("Fee: " + fee);
    }
}
```

---

<a id="day6"></a>
## Day 6 — switch Statements

**Learning objectives:** Use `switch` with `int`, `char`, and `String`; understand `break`, fall-through, `default`, and the arrow form available in Java 17.

### Question 1: Mixed Real-World Scenario
**Title:** Vending Machine

**Problem statement:** A vending machine sells: 1 = Tea (10), 2 = Coffee (15), 3 = Juice (25), 4 = Water (5). Read the choice number and the quantity, then print the item, quantity, and total price. Any other choice prints `Invalid choice`.

**Real-world context:** Vending machines map button numbers to products and prices.

**Input requirements:** Two `int` values on one line: choice and quantity.

**Expected output:** `<Item> x<quantity> = <total>` or `Invalid choice`.

**Sample cases:**
```
Input: 2 3
Output: Coffee x3 = 45
```
```
Input: 4 1
Output: Water x1 = 5
```
```
Input: 9 2
Output: Invalid choice
```

**Constraints:** Quantity is at least 1.

### Question 2: Interview-Based Coding
**Title:** Switch-Based Calculator

**Problem statement:** Read two numbers and an operator (`+`, `-`, `*`, `/`) and print the result. Use a `switch` on the operator. Division by zero must print `Cannot divide by zero`; an unknown operator prints `Invalid operator`.

**Input and output requirements:** Input: `<double> <operator> <double>` on one line. Output: the result with 2 decimal places, or the message described above.

**Sample cases:**
```
Input: 10 + 5
Output: 15.00
```
```
Input: 7 / 2
Output: 3.50
```
```
Input: 8 / 0
Output: Cannot divide by zero
```

**Constraints:** Operands may be negative or decimal. Also test `6 * 4` and `5 % 2`.

### Question 3: Company-Style Programming
**Title:** Days in a Month

**Problem statement:** Read a month number (1–12) and a year, and print how many days that month has in that year. February has 29 days in a leap year and 28 otherwise (a leap year is divisible by 4, except century years, which must be divisible by 400). An invalid month prints `Invalid month`.

**Input and output requirements:** Input: month and year (`int`) on one line. Output: the number of days, or `Invalid month`.

**Sample cases:**
```
Input: 2 2024
Output: 29
```
```
Input: 4 2023
Output: 30
```
```
Input: 13 2023
Output: Invalid month
```

**Constraints and edge cases:** Test `2 1900` (28), `2 2000` (29), `12 2023`, and `0 2023`.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
public class Main {
    public static void main(String[] args) {
        int day = 3;
        switch (day) {
            case 1:
                System.out.println("Mon");
            case 2:
                System.out.println("Tue");
            case 3:
                System.out.println("Wed");
            case 4:
                System.out.println("Thu");
                break;
            case 5:
                System.out.println("Fri");
            default:
                System.out.println("Weekend");
        }

        String grade = "B";
        int points = switch (grade) {
            case "A" -> 4;
            case "B" -> 3;
            case "C" -> 2;
            default -> 0;
        };
        System.out.println(points);

        char ch = 'x';
        switch (ch) {
            case 'a':
            case 'e':
            case 'i':
            case 'o':
            case 'u':
                System.out.println("Vowel");
                break;
            default:
                System.out.println("Other");
        }

        int n = 7;
        switch (n % 3) {
            default:
                System.out.println("D");
            case 0:
                System.out.println("Zero");
                break;
            case 1:
                System.out.println("One");
        }
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Read a number from 1 to 4 and print `Monday`, `Tuesday`, `Wednesday`, or `Thursday` respectively. Any other number must print only `Invalid day`.

The program has compile-time and logic errors. Identify each, explain why it occurs, and provide the corrected program.

```java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int day = sc.nextInt();
        String name;
        switch (day) {
            case 1:
                name = "Monday";
            case 2:
                name = "Tuesday";
                break;
            case 3:
                name = "Wednesday";
                break;
            case 2:
                name = "Thursday";
                break;
            default:
                System.out.println("Invalid day");
        }
        System.out.println(name);
    }
}
```

---

<a id="day7"></a>
## Day 7 — Validation and Decision-Making (Week 1 Review)

**Learning objectives:** Combine input, operators, conditions, and `switch` to validate data and make multi-rule decisions. This day reviews everything from Week 1.

### Question 1: Mixed Real-World Scenario
**Title:** Bank Withdrawal Validator

**Problem statement:** A savings account must always keep a minimum balance of 500. Read the current balance and the requested withdrawal. Apply the checks in this order: (1) if the amount is 0 or less, or is not a multiple of 100, print `Invalid amount`; (2) if the balance left after withdrawal would be below 500, print `Insufficient balance`; (3) otherwise print `Withdrawn. New balance: <balance>`.

**Real-world context:** Banks validate every ATM request against account rules before releasing cash.

**Input requirements:** Two `int` values on one line: balance and withdrawal amount.

**Expected output:** One of the three messages described above.

**Sample cases:**
```
Input: 5000 1200
Output: Withdrawn. New balance: 3800
```
```
Input: 5000 4600
Output: Insufficient balance
```
```
Input: 5000 250
Output: Invalid amount
```

**Constraints:** Balance is 0 or more. Test `5000 4500` (allowed), `5000 0`, and `5000 -100`.

### Question 2: Interview-Based Coding
**Title:** Character Classifier

**Problem statement:** Read a single character and print whether it is a `Vowel`, `Consonant`, `Digit`, or `Special character`. Letters can be uppercase or lowercase.

**Input and output requirements:** Input: one character. Output: one of the four words/phrases above.

**Sample cases:**
```
Input: a
Output: Vowel
```
```
Input: Z
Output: Consonant
```
```
Input: 7
Output: Digit
```
```
Input: #
Output: Special character
```

**Constraints:** The input is exactly one non-whitespace character.

### Question 3: Company-Style Programming
**Title:** Shipping Cost Calculator

**Problem statement:** A courier charges per kilogram by zone: `L` (Local) 5, `N` (National) 8, `I` (International) 20. Express delivery adds 50% to the cost. Weight must be greater than 0 and at most 30 kg, otherwise print `Invalid weight`. An unknown zone prints `Invalid zone`. Check weight first, then zone.

**Input and output requirements:** Input: weight (`double`), zone (`char`), express (`boolean`) on one line. Output: `Cost: <amount>` with 2 decimal places, or the error message.

**Sample cases:**
```
Input: 2 L false
Output: Cost: 10.00
```
```
Input: 4 N true
Output: Cost: 48.00
```
```
Input: 35 I false
Output: Invalid weight
```
```
Input: 3 X false
Output: Invalid zone
```

**Constraints and edge cases:** Test weights `0`, `30`, and `30.5`.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
public class Main {
    public static void main(String[] args) {
        int a = 17, b = 4;
        double avg = (a + b) / 2;
        double avg2 = (a + b) / 2.0;
        int r = a % b;
        char c = (char) ('a' + r);
        String s = "R" + r + c;
        boolean flag = a > b && r != 1 || b == 4;
        System.out.println(avg);
        System.out.println(avg2);
        System.out.println(s);
        System.out.println(flag);
        System.out.println(a / b * b + a % b);
        System.out.println(1 + 2 + "3" + 4 + 5);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Read a bill amount and a customer type (`G` = Gold gets 20% discount, `S` = Silver gets 10% discount, any other type gets 0%). If the bill is above 2000, an extra 5% is added to the discount. Print the final bill with 2 decimal places. For `3000 G` the output must be `Final bill: 2250.00`; for `1000 S` it must be `Final bill: 900.00`.

The program has compile-time and logic errors. Identify each, explain why it occurs, and provide the corrected program.

```java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double bill = sc.nextDouble();
        char type = sc.next();
        double discount = 0;
        switch (type) {
            case 'G':
                discount = 20;
            case 'S':
                discount = 10;
                break;
        }
        if (bill > 2000)
            discount = 5;
        double finalBill = bill - bill * discount / 100;
        System.out.println("Final bill: " + finalBill);
    }
}
```

---

<a id="day8"></a>
## Day 8 — for Loops

**Learning objectives:** Write counting loops with `for`, accumulate totals, use `break` and `continue`, and loop in steps.

### Question 1: Mixed Real-World Scenario
**Title:** Monthly Savings Tracker

**Problem statement:** A student saves the same amount every month. Read the monthly deposit and the number of months. Print the running total at the end of each month, then the final total.

**Real-world context:** Savings apps show a month-by-month progress report.

**Input requirements:** Two `int` values on one line: monthly deposit and number of months.

**Expected output:** One line per month (`Month <i>: <total so far>`), then `Total saved: <total>`.

**Sample cases:**
```
Input: 500 3
Output:
Month 1: 500
Month 2: 1000
Month 3: 1500
Total saved: 1500
```
```
Input: 250 4
Output:
Month 1: 250
Month 2: 500
Month 3: 750
Month 4: 1000
Total saved: 1000
```

**Constraints:** 1 ≤ deposit ≤ 100000, 1 ≤ months ≤ 60.

### Question 2: Interview-Based Coding
**Title:** Factorial

**Problem statement:** The factorial of `n` (written `n!`) is the product of all whole numbers from 1 to `n`, and `0! = 1`. Read `n` and print `n!`.

**Input and output requirements:** Input: one `int`. Output: the factorial as a single number.

**Sample cases:**
```
Input: 5
Output: 120
```
```
Input: 0
Output: 1
```
```
Input: 10
Output: 3628800
```

**Constraints:** 0 ≤ n ≤ 12. Choose a data type that safely holds the result.

### Question 3: Company-Style Programming
**Title:** Sum of Multiples

**Problem statement:** Read `n` and print the sum of all numbers from 1 to `n` (inclusive) that are divisible by 3 or by 5. A number divisible by both must be counted once.

**Input and output requirements:** Input: one `int`. Output: the sum.

**Sample cases:**
```
Input: 10
Output: 33
```
```
Input: 20
Output: 98
```
```
Input: 2
Output: 0
```

**Constraints and edge cases:** 1 ≤ n ≤ 100000. Test `15`, where 15 is divisible by both.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
public class Main {
    public static void main(String[] args) {
        for (int i = 1; i <= 5; i++) {
            if (i == 3) continue;
            System.out.print(i + " ");
        }
        System.out.println();

        int sum = 0;
        for (int i = 10; i > 0; i -= 3) {
            sum += i;
        }
        System.out.println(sum);

        for (int i = 0, j = 5; i < j; i++, j--) {
            System.out.println(i + "," + j);
        }

        for (int i = 1; i <= 10; i++) {
            if (i * i > 30) break;
            System.out.print(i);
        }
        System.out.println();
    }
}
```

### Question 5: Debug the Code
**What the program should do:** For `n = 10`, print the sum of the even numbers from 1 to `n` and how many even numbers there are. The expected output is `Sum: 30` and `Count: 5`.

The program has compile-time and logic errors. Identify each, explain why it occurs, and provide the corrected program.

```java
public class Main {
    public static void main(String[] args) {
        int n = 10;
        int sum = 0;
        int count;
        for (int i = 1; i <= n; i++); {
            if (i % 2 == 0) {
                sum += i;
                count++;
            }
        }
        System.out.println("Sum: " + sum);
        System.out.println("Count: " + count);
    }
}
```

---

<a id="day9"></a>
## Day 9 — while and do-while Loops

**Learning objectives:** Choose between `while` and `do-while`, loop until a condition changes, and avoid infinite loops.

### Question 1: Mixed Real-World Scenario
**Title:** ATM PIN Attempts

**Problem statement:** The correct PIN is `4321`. The user gets at most 3 attempts. For every wrong PIN, print `Wrong PIN. Attempts left: <n>`. If the correct PIN is entered, print `Access granted` and stop reading. After 3 wrong attempts print `Card blocked`.

**Real-world context:** ATMs block a card after repeated wrong PIN entries to protect the account.

**Input requirements:** A sequence of integers (PIN attempts), one per line. Enough values are always provided.

**Expected output:** Messages as described above.

**Sample cases:**
```
Input:
1111
4321
Output:
Wrong PIN. Attempts left: 2
Access granted
```
```
Input:
1
2
3
Output:
Wrong PIN. Attempts left: 2
Wrong PIN. Attempts left: 1
Wrong PIN. Attempts left: 0
Card blocked
```

**Constraints:** Test a correct PIN on the first attempt and on the third attempt.

### Question 2: Interview-Based Coding
**Title:** Count the Digits

**Problem statement:** Read an integer and print how many digits it has. Negative numbers are counted without the minus sign, and `0` has one digit.

**Input and output requirements:** Input: one `int`. Output: the digit count.

**Sample cases:**
```
Input: 12345
Output: 5
```
```
Input: 0
Output: 1
```
```
Input: -907
Output: 3
```

**Constraints:** Value is between -1000000000 and 1000000000.

### Question 3: Company-Style Programming
**Title:** Steps to Reach One

**Problem statement:** Start with a positive integer `n`. Repeat: if the number is even, divide it by 2; if it is odd, multiply it by 3 and add 1. Count how many repetitions it takes to reach 1.

**Input and output requirements:** Input: one `int`. Output: the number of steps.

**Sample cases:**
```
Input: 6
Output: 8
```
```
Input: 1
Output: 0
```
```
Input: 7
Output: 16
```

**Constraints and edge cases:** 1 ≤ n ≤ 10000. The sequence always reaches 1 for these inputs.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
public class Main {
    public static void main(String[] args) {
        int i = 1;
        while (i < 20) {
            i *= 2;
        }
        System.out.println(i);

        int n = 0;
        do {
            n += 5;
            System.out.print(n + " ");
        } while (n < 12);
        System.out.println();

        int k = 10;
        while (k > 10) {
            k--;
        }
        System.out.println(k);
        do {
            k--;
        } while (k > 10);
        System.out.println(k);

        int count = 0;
        int m = 100;
        while (m > 1) {
            m /= 3;
            count++;
        }
        System.out.println(count + " " + m);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Read integers until `0` is entered (the `0` is not counted). Print the sum and the average with 2 decimal places. For the input `4 6 8 0` the output is `Sum: 18` and `Average: 6.00`. If the first number is `0`, print `No numbers entered`.

The program has compile-time and logic errors, including one that makes it run forever. Identify each, explain why it occurs, and provide the corrected program.

```java
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int sum = 0, count = 0;
        int num = sc.nextInt();
        while (num != 0) {
            sum += num;
            count++;
        }
        System.out.println("Sum: " + sum);
        System.out.println("Average: " + sum / count);
    }
}
```

---

<a id="day10"></a>
## Day 10 — Digit Extraction and Digit Sums

**Learning objectives:** Use `% 10` and `/ 10` inside loops to take numbers apart digit by digit.

### Question 1: Mixed Real-World Scenario
**Title:** Lucky Ticket Checker

**Problem statement:** A bus ticket has a 6-digit number. The ticket is "lucky" if the sum of its first three digits equals the sum of its last three digits. Read the ticket number and print the result.

**Real-world context:** Some transport companies run "lucky ticket" promotions with free rides for matching tickets.

**Input requirements:** One `int` ticket number from 100000 to 999999.

**Expected output:** `Lucky` or `Not lucky`.

**Sample cases:**
```
Input: 123006
Output: Lucky
```
```
Input: 123456
Output: Not lucky
```
```
Input: 100001
Output: Lucky
```

**Constraints:** The ticket always has exactly 6 digits.

### Question 2: Interview-Based Coding
**Title:** Sum of Digits

**Problem statement:** Read an integer and print the sum of its digits. Ignore the sign of negative numbers.

**Input and output requirements:** Input: one `int`. Output: the digit sum.

**Sample cases:**
```
Input: 1234
Output: 10
```
```
Input: -45
Output: 9
```
```
Input: 0
Output: 0
```

**Constraints:** Value is between -1000000000 and 1000000000.

### Question 3: Company-Style Programming
**Title:** Armstrong Number

**Problem statement:** An Armstrong number equals the sum of each of its digits raised to the power of the number of digits. For example, a 3-digit number uses cubes. Read a positive integer and print whether it is an Armstrong number.

**Input and output requirements:** Input: one `int`. Output: `Armstrong` or `Not Armstrong`.

**Sample cases:**
```
Input: 153
Output: Armstrong
```
```
Input: 9474
Output: Armstrong
```
```
Input: 100
Output: Not Armstrong
```

**Constraints and edge cases:** 1 ≤ n ≤ 100000. Single-digit numbers are Armstrong numbers (test `7`).

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
public class Main {
    public static void main(String[] args) {
        int n = 4075;
        int sum = 0, count = 0, max = 0;
        while (n > 0) {
            int d = n % 10;
            sum += d;
            count++;
            if (d > max) max = d;
            n /= 10;
        }
        System.out.println(sum + " " + count + " " + max);
        System.out.println(n);

        int m = 1020;
        int zeros = 0;
        while (m != 0) {
            if (m % 10 == 0) zeros++;
            m = m / 10;
        }
        System.out.println(zeros);

        int x = 9;
        System.out.println(x / 10 + " " + x % 10);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Read a number `n` and a digit `d`, then print how many times `d` appears in `n`. For `1211 1` the output is `Digit 1 appears 3 time(s)`. For `0 0` the output is `Digit 0 appears 1 time(s)`.

The program has logic errors (and one special case it does not handle). Identify each, explain why it occurs, and provide the corrected program.

```java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int digit = sc.nextInt();
        int count = 0;
        while (n > 0) {
            int last = n / 10;
            if (last == digit)
                count++;
            n = n % 10;
        }
        System.out.println("Digit " + digit + " appears " + count + " time(s)");
    }
}
```

---

<a id="day11"></a>
## Day 11 — Reverse and Palindrome Numbers

**Learning objectives:** Build a number backwards digit by digit, compare it with the original, and keep a copy of the original value when needed.

### Question 1: Mixed Real-World Scenario
**Title:** Locker Code Security Check

**Problem statement:** A gym lets members choose a numeric locker code. Codes that read the same forwards and backwards are considered too easy to guess and are rejected. Read a code, print its reverse (as a number, so trailing zeros of the code disappear in the reverse), and then print whether the code is accepted.

**Real-world context:** Access systems often reject predictable codes such as `1221` or `1111`.

**Input requirements:** One positive `int` code.

**Expected output:**
```
Reversed: <number>
Accepted   (or)   Rejected: palindrome code
```

**Sample cases:**
```
Input: 1221
Output:
Reversed: 1221
Rejected: palindrome code
```
```
Input: 4075
Output:
Reversed: 5704
Accepted
```
```
Input: 1200
Output:
Reversed: 21
Accepted
```

**Constraints:** 1 ≤ code ≤ 999999999.

### Question 2: Interview-Based Coding
**Title:** Palindrome Number

**Problem statement:** A palindrome number reads the same forwards and backwards. Read an integer and print whether it is a palindrome. Negative numbers are never palindromes.

**Input and output requirements:** Input: one `int`. Output: `Palindrome` or `Not a palindrome`.

**Sample cases:**
```
Input: 121
Output: Palindrome
```
```
Input: 123
Output: Not a palindrome
```
```
Input: -121
Output: Not a palindrome
```

**Constraints:** Value fits in `int` and is below 1000000000 in magnitude. Test `0` and `10`.

### Question 3: Company-Style Programming
**Title:** Reverse-and-Add

**Problem statement:** Start with a number. While it is not a palindrome, reverse its digits and add the reverse to the number, then repeat. Count the repetitions and print the palindrome you end with.

**Input and output requirements:** Input: one `int` `n`. Output two lines:
```
Steps: <count>
Palindrome: <value>
```

**Sample cases:**
```
Input: 87
Output:
Steps: 4
Palindrome: 4884
```
```
Input: 56
Output:
Steps: 1
Palindrome: 121
```
```
Input: 11
Output:
Steps: 0
Palindrome: 11
```

**Constraints and edge cases:** 1 ≤ n ≤ 80, and the process always ends in a palindrome. Intermediate values can exceed the `int` range for larger inputs in general, so pick a safe data type.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
public class Main {
    public static void main(String[] args) {
        int n = 1234, rev = 0;
        while (n > 0) {
            rev = rev * 10 + n % 10;
            n /= 10;
        }
        System.out.println(rev);

        int a = 121, temp = a, r = 0;
        while (temp != 0) {
            r = r * 10 + temp % 10;
            temp /= 10;
        }
        System.out.println(a == r);

        int x = 1200, y = 0;
        while (x > 0) {
            y = y * 10 + x % 10;
            x /= 10;
        }
        System.out.println(y);

        int p = -123, q = 0;
        while (p != 0) {
            q = q * 10 + p % 10;
            p /= 10;
        }
        System.out.println(q);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Read an integer and print `Palindrome` if it reads the same forwards and backwards, otherwise `Not a palindrome`. For `121` print `Palindrome`; for `123` print `Not a palindrome`.

The program has compile-time and logic errors. Identify each, explain why it occurs, and provide the corrected program.

```java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int rev = 0;
        while (n > 0) {
            rev = rev * 10 + n % 10;
            n = n / 10;
        }
        if (n = rev)
            System.out.println("Palindrome");
        else
            System.out.println("Not a palindrome");
    }
}
```

---

<a id="day12"></a>
## Day 12 — Primes and Factors

**Learning objectives:** Find divisors, test primality efficiently, and generate primes in a range.

### Question 1: Mixed Real-World Scenario
**Title:** Packing Options

**Problem statement:** A warehouse has `N` identical items to pack into boxes. Every box must hold the same number of items and no item may be left over. List every possible box size and print how many options there are.

**Real-world context:** Logistics teams decide on box sizes that pack stock exactly.

**Input requirements:** One positive `int` `N`.

**Expected output:**
```
Box sizes: <sizes in increasing order, separated by spaces>
Options: <count>
```

**Sample cases:**
```
Input: 12
Output:
Box sizes: 1 2 3 4 6 12
Options: 6
```
```
Input: 7
Output:
Box sizes: 1 7
Options: 2
```
```
Input: 1
Output:
Box sizes: 1
Options: 1
```

**Constraints:** 1 ≤ N ≤ 100000.

### Question 2: Interview-Based Coding
**Title:** Prime Check

**Problem statement:** A prime number is greater than 1 and has no divisors other than 1 and itself. Read a number and print whether it is prime.

**Input and output requirements:** Input: one `int`. Output: `Prime` or `Not prime`.

**Sample cases:**
```
Input: 29
Output: Prime
```
```
Input: 91
Output: Not prime
```
```
Input: 1
Output: Not prime
```

**Constraints:** -1000 ≤ n ≤ 1000000. Test `0`, `2`, `-7`, and `97`.

### Question 3: Company-Style Programming
**Title:** Primes in a Range

**Problem statement:** Read two integers `a` and `b` (with `a ≤ b`). Print all prime numbers between `a` and `b`, inclusive, in increasing order on one line separated by spaces, followed by the count. If there are no primes, print `No primes`.

**Input and output requirements:** Input: `a b` on one line. Output: the primes line then `Count: <n>`, or `No primes`.

**Sample cases:**
```
Input: 10 30
Output:
11 13 17 19 23 29
Count: 6
```
```
Input: 1 10
Output:
2 3 5 7
Count: 4
```
```
Input: 24 28
Output: No primes
```

**Constraints and edge cases:** 1 ≤ a ≤ b ≤ 10000. The range may include 1 (not prime) and 2 (the only even prime).

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
public class Main {
    public static void main(String[] args) {
        int n = 28, sum = 0;
        for (int i = 1; i < n; i++) {
            if (n % i == 0) sum += i;
        }
        System.out.println(sum);
        System.out.println(sum == n);

        int count = 0;
        for (int i = 1; i <= 20; i++) {
            if (20 % i == 0) count++;
        }
        System.out.println(count);

        boolean isPrime = true;
        for (int i = 2; i * i <= 49; i++) {
            if (49 % i == 0) {
                isPrime = false;
                break;
            }
        }
        System.out.println(isPrime);

        int g = 0;
        for (int i = 1; i <= 12 && i <= 18; i++) {
            if (12 % i == 0 && 18 % i == 0) g = i;
        }
        System.out.println(g);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Read an integer `n` and print `<n> is prime` or `<n> is not prime`. For `7` print `7 is prime`. For `1`, `0`, and negative numbers print `... is not prime`.

The program gives wrong answers for many inputs. Identify each logic error, explain why it occurs, and provide the corrected program.

```java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        boolean prime = true;
        for (int i = 2; i <= n; i++) {
            if (n % i == 0) {
                prime = false;
            }
        }
        if (prime)
            System.out.println(n + " is prime");
        else
            System.out.println(n + " is not prime");
    }
}
```

---

<a id="day13"></a>
## Day 13 — Nested Loops, Patterns, and Tables

**Learning objectives:** Use a loop inside a loop to print rows and columns, control spacing, and relate loop variables to what is printed.

### Question 1: Mixed Real-World Scenario
**Title:** Cinema Seat Map

**Problem statement:** A cinema hall has rows labelled `A`, `B`, `C`, … and a fixed number of seats per row, numbered from 1. Read the number of rows and seats per row and print the seat labels, one row per line, separated by single spaces.

**Real-world context:** Ticket-booking apps show a seat map like `A1 A2 A3`.

**Input requirements:** Two `int` values on one line: rows and seats per row.

**Expected output:** One line per row.

**Sample cases:**
```
Input: 3 4
Output:
A1 A2 A3 A4
B1 B2 B3 B4
C1 C2 C3 C4
```
```
Input: 2 5
Output:
A1 A2 A3 A4 A5
B1 B2 B3 B4 B5
```

**Constraints:** 1 ≤ rows ≤ 26, 1 ≤ seats ≤ 30.

### Question 2: Interview-Based Coding
**Title:** Star Pyramid

**Problem statement:** Read `n` and print a centered pyramid of `*` characters with `n` rows. Row `i` (starting at 1) has `2i - 1` stars, and the pyramid is aligned with leading spaces (no trailing spaces).

**Input and output requirements:** Input: one `int`. Output: the pyramid.

**Sample cases:**
```
Input: 3
Output:
  *
 ***
*****
```
```
Input: 1
Output:
*
```

**Constraints:** 1 ≤ n ≤ 20.

### Question 3: Company-Style Programming
**Title:** Multiplication Table Grid

**Problem statement:** Read `n` and print an `n × n` multiplication table. Every number must occupy exactly 4 character positions, right-aligned (use `printf` with a width, for example `%4d`).

**Input and output requirements:** Input: one `int`. Output: `n` lines with `n` numbers each.

**Sample cases:**
```
Input: 3
Output:
   1   2   3
   2   4   6
   3   6   9
```
```
Input: 4
Output:
   1   2   3   4
   2   4   6   8
   3   6   9  12
   4   8  12  16
```

**Constraints and edge cases:** 1 ≤ n ≤ 12. For `n = 1` print a single line `   1`.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
public class Main {
    public static void main(String[] args) {
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= i; j++) {
                System.out.print(i * j + " ");
            }
            System.out.println();
        }

        int count = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = i; j < 4; j++) {
                count++;
            }
        }
        System.out.println(count);

        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 4; j++) {
                if (j > i) break;
                if ((i + j) % 2 == 0) System.out.print("*");
                else System.out.print("-");
            }
            System.out.println();
        }
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Read `n` and print a number triangle that shrinks by one each row. For `n = 4` the output is:
```
1 2 3 4
1 2 3
1 2
1
```

The program has compile-time and logic errors. Identify each, explain why it occurs, and provide the corrected program.

```java
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        for (int i = n; i >= 1; i--) {
            for (int j = 1; j <= n; j++) {
                System.out.print(j + " ");
            }
        }
        System.out.println();
    }
}
```

---

<a id="day14"></a>
## Day 14 — Methods and Reuse

**Learning objectives:** Write `static` methods with parameters and return values, reuse them, understand overloading and pass-by-value.

### Question 1: Mixed Real-World Scenario
**Title:** Grocery Bill with Methods

**Problem statement:** A grocery store bills two items. Write and use these methods: `double itemTotal(double price, int qty)`, `double applyDiscount(double amount)` (10% off when the amount is 1000 or more, otherwise no discount), and `double addGST(double amount)` (adds 18%). Read the two items and print the final amount.

**Real-world context:** Billing software reuses small calculation steps for every sale.

**Input requirements:** Two lines: `<price1> <qty1>` and `<price2> <qty2>`.

**Expected output:** `Final amount: <amount>` with 2 decimal places.

**Sample cases:**
```
Input:
200 3
150 2
Output: Final amount: 1062.00
```
```
Input:
500 2
300 1
Output: Final amount: 1380.60
```

**Constraints:** Prices are 0 or more; quantities are at least 1. A subtotal of exactly `1000` gets the discount.

### Question 2: Interview-Based Coding
**Title:** GCD and LCM

**Problem statement:** The GCD of two numbers is the largest number that divides both; the LCM is the smallest positive number divisible by both. Write one method for GCD and one for LCM (the LCM method should reuse the GCD method). Read two non-negative integers and print both results. If either number is `0`, the LCM is `0`.

**Input and output requirements:** Input: two `int` values on one line. Output:
```
GCD: <value>
LCM: <value>
```

**Sample cases:**
```
Input: 12 18
Output:
GCD: 6
LCM: 36
```
```
Input: 7 13
Output:
GCD: 1
LCM: 91
```
```
Input: 0 5
Output:
GCD: 5
LCM: 0
```

**Constraints:** 0 ≤ values ≤ 10000, and both are never `0` at the same time.

### Question 3: Company-Style Programming
**Title:** Emirp Numbers

**Problem statement:** An emirp is a prime number whose digit reversal is a different prime number (so palindromic primes such as 11 are not emirps). Write reusable methods `isPrime(int)` and `reverse(int)`, then read `n` and print whether it is an emirp.

**Input and output requirements:** Input: one `int`. Output: `Emirp` or `Not emirp`.

**Sample cases:**
```
Input: 13
Output: Emirp
```
```
Input: 11
Output: Not emirp
```
```
Input: 19
Output: Not emirp
```

**Constraints and edge cases:** 1 ≤ n ≤ 100000. Test `2`, `1`, and `17`.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
public class Main {
    static int counter = 0;

    static int square(int x) {
        return x * x;
    }

    static void change(int x) {
        x = x + 10;
        System.out.println("In change: " + x);
    }

    static int add(int a, int b) {
        return a + b;
    }

    static double add(double a, double b) {
        return a + b + 0.5;
    }

    static int next() {
        counter++;
        return counter * 2;
    }

    public static void main(String[] args) {
        int v = 5;
        change(v);
        System.out.println(v);
        System.out.println(square(square(2)));
        System.out.println(add(2, 3));
        System.out.println(add(2, 3.0));
        System.out.println(next() + next() + next());
        System.out.println(counter);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** `max` returns the larger of two integers (and the common value if they are equal). `isEven` returns `true` when a number is even. `main` should print `Max: 9`, `Even: true`, and then the maximum of `7` and `7`, which is `7`.

The program has several compile-time errors. Identify each, explain why it occurs, and provide the corrected program.

```java
public class Main {
    static int max(int a, int b) {
        if (a > b) {
            return a;
        } else if (b > a) {
            return b;
        }
    }

    static void isEven(int n) {
        return n % 2 == 0;
    }

    public static void main(String[] args) {
        int result = max(4, 9);
        System.out.println("Max: " + result);
        boolean even = isEven(10);
        System.out.println("Even: " + even);
        System.out.println(max(7));
    }
}
```

---

<a id="day15"></a>
## Day 15 — String Basics

**Learning objectives:** Use `length`, `charAt`, `substring`, `indexOf`, `toUpperCase`, `equals`, and understand that strings are immutable.

### Question 1: Mixed Real-World Scenario
**Title:** Student Email Generator

**Problem statement:** A school creates email IDs from student details. The email is: the first letter of the first name, followed by the full last name, followed by the roll number, all in lowercase, followed by `@school.edu`. Read the first name, last name, and roll number and print the email.

**Real-world context:** Schools and companies generate consistent email IDs automatically.

**Input requirements:** One line: `<firstName> <lastName> <rollNumber>`. Names may be in any letter case.

**Expected output:** The email address.

**Sample cases:**
```
Input: Aarav Sharma 12
Output: asharma12@school.edu
```
```
Input: RAHUL VERMA 7
Output: rverma7@school.edu
```
```
Input: Meera Iyer 105
Output: miyer105@school.edu
```

**Constraints:** Names contain only letters and each has at least one character.

### Question 2: Interview-Based Coding
**Title:** String Equality

**Problem statement:** Read two words. Print `Equal` if they are exactly the same, `Equal ignoring case` if they differ only in letter case, otherwise `Different`.

**Input and output requirements:** Input: two words on one line. Output: one of the three messages.

**Sample cases:**
```
Input: Java Java
Output: Equal
```
```
Input: java JAVA
Output: Equal ignoring case
```
```
Input: Java Jav
Output: Different
```

**Constraints:** Words contain letters and digits only.

### Question 3: Company-Style Programming
**Title:** Username Validator

**Problem statement:** A username is valid only if (1) its length is between 5 and 12 characters inclusive, (2) its first character is a letter, and (3) it contains at least one digit. Read a username and print `Valid` or `Invalid`.

**Input and output requirements:** Input: one word with no spaces. Output: `Valid` or `Invalid`.

**Sample cases:**
```
Input: coder99
Output: Valid
```
```
Input: 9coder
Output: Invalid
```
```
Input: abc1
Output: Invalid
```
```
Input: codingfun
Output: Invalid
```

**Constraints and edge cases:** Test lengths `4`, `5`, `12`, and `13`, and usernames containing only digits.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
public class Main {
    public static void main(String[] args) {
        String s = "Hello, World";
        System.out.println(s.length());
        System.out.println(s.charAt(4));
        System.out.println(s.indexOf("o"));
        System.out.println(s.lastIndexOf('o'));
        System.out.println(s.substring(7));
        System.out.println(s.substring(0, 5).toUpperCase());
        System.out.println(s.replace('l', 'L'));

        String a = "Java";
        String b = "Ja" + "va";
        String c = new String("Java");
        System.out.println(a == b);
        System.out.println(a == c);
        System.out.println(a.equals(c));
        System.out.println("a" + 1 + 2);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Read one word. If it equals `admin`, print `Welcome, administrator`. Then print its first and last characters and its uppercase form. For the input `admin` the output is:
```
Welcome, administrator
First: a, Last: n
Upper: ADMIN
```

The program has compile-time and runtime/logic errors. Identify each, explain why it occurs, and provide the corrected program.

```java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String word = sc.next();
        if (word == "admin") {
            System.out.println("Welcome, administrator");
        }
        char first = word.substring(0, 1);
        String last = word.charAt(word.length());
        System.out.println("First: " + first + ", Last: " + last);
        word.toUpperCase();
        System.out.println("Upper: " + word);
    }
}
```

---

<a id="day16"></a>
## Day 16 — Character Counting and Reversing Strings

**Learning objectives:** Loop over the characters of a string, count characters that match a rule, and build a reversed string.

### Question 1: Mixed Real-World Scenario
**Title:** SMS Message Analyzer

**Problem statement:** A messaging app wants a quick summary of a text message. Read one line of text and print the total number of characters (including spaces), the number of words, and the number of vowels (`a e i o u`, in either letter case).

**Real-world context:** SMS and chat apps show character counts because messages have length limits.

**Input requirements:** One line of text. Words are separated by exactly one space, with no leading or trailing spaces.

**Expected output:**
```
Characters: <n>
Words: <n>
Vowels: <n>
```

**Sample cases:**
```
Input: Hello World
Output:
Characters: 11
Words: 2
Vowels: 3
```
```
Input: Java is fun
Output:
Characters: 11
Words: 3
Vowels: 4
```

**Constraints:** The message has between 1 and 160 characters.

### Question 2: Interview-Based Coding
**Title:** Reverse a String

**Problem statement:** Read a line of text and print it reversed. Do not use `StringBuilder.reverse()` or any other built-in reverse method; build the result yourself.

**Input and output requirements:** Input: one line. Output: the reversed line.

**Sample cases:**
```
Input: hello
Output: olleh
```
```
Input: Java 17
Output: 71 avaJ
```
```
Input: a
Output: a
```

**Constraints:** Length between 1 and 100.

### Question 3: Company-Style Programming
**Title:** Letter Frequency

**Problem statement:** Read a word made only of lowercase letters. Print how many times each letter appears, in alphabetical order, skipping letters that do not appear. Format: `<letter>=<count>` separated by single spaces.

**Input and output requirements:** Input: one word. Output: one line as described.

**Sample cases:**
```
Input: banana
Output: a=3 b=1 n=2
```
```
Input: aab
Output: a=2 b=1
```
```
Input: z
Output: z=1
```

**Constraints and edge cases:** Length between 1 and 100; only the letters `a`–`z` appear.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
public class Main {
    public static void main(String[] args) {
        String s = "programming";
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == 'm') count++;
        }
        System.out.println(count);

        String rev = "";
        for (int i = 0; i < 4; i++) {
            rev = s.charAt(i) + rev;
        }
        System.out.println(rev);

        char[] arr = s.toCharArray();
        arr[0] = 'P';
        System.out.println(new String(arr));
        System.out.println(s);

        StringBuilder sb = new StringBuilder("abc");
        sb.append("def").reverse().insert(0, "-");
        System.out.println(sb);

        System.out.println('a' + 'b' + "c");
        System.out.println((char) ('a' + 2));
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Read a line of text and print how many vowels (`a e i o u`, uppercase or lowercase) it contains. For `Education` the output is `Vowels: 5`.

The program has compile-time errors, a runtime error, and a logic error. Identify each, explain why it occurs, and provide the corrected program.

```java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String text = sc.nextLine();
        int vowels = 0;
        for (int i = 0; i <= text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == "a" || ch == "e" || ch == "i" || ch == "o" || ch == "u") {
                vowels++;
            }
        }
        System.out.println("Vowels: " + vowel);
    }
}
```

---

<a id="day17"></a>
## Day 17 — Palindromes and Anagrams

**Learning objectives:** Compare characters from both ends of a string, ignore spaces and case, and compare strings by their letters using sorting or counting.

### Question 1: Mixed Real-World Scenario
**Title:** Palindrome Phrase Contest

**Problem statement:** A school's word-game contest awards points for palindrome phrases. A phrase counts as a palindrome if it reads the same forwards and backwards after removing spaces and ignoring letter case. Read a phrase and print the result.

**Real-world context:** Word games and puzzle events check phrases such as "Never odd or even".

**Input requirements:** One line containing letters and spaces only.

**Expected output:** `Palindrome phrase` or `Not a palindrome phrase`.

**Sample cases:**
```
Input: Never odd or even
Output: Palindrome phrase
```
```
Input: Hello World
Output: Not a palindrome phrase
```
```
Input: A man a plan a canal Panama
Output: Palindrome phrase
```

**Constraints:** The phrase has at least one letter and at most 100 characters.

### Question 2: Interview-Based Coding
**Title:** Anagram Check

**Problem statement:** Two words are anagrams if one can be formed by rearranging all the letters of the other. Read two words and print whether they are anagrams. Ignore letter case.

**Input and output requirements:** Input: two words on one line. Output: `Anagrams` or `Not anagrams`.

**Sample cases:**
```
Input: listen silent
Output: Anagrams
```
```
Input: Dormitory dirtyroom
Output: Anagrams
```
```
Input: abc abd
Output: Not anagrams
```

**Constraints:** Words contain letters only. Words of different lengths are never anagrams.

### Question 3: Company-Style Programming
**Title:** Anagram Pair Counter

**Problem statement:** Read three words. Among the three possible pairs (word 1 with word 2, word 1 with word 3, word 2 with word 3), count how many pairs are anagrams of each other (ignoring letter case).

**Input and output requirements:** Input: three words on one line. Output: `Anagram pairs: <count>`.

**Sample cases:**
```
Input: listen silent enlist
Output: Anagram pairs: 3
```
```
Input: tea eat tan
Output: Anagram pairs: 1
```
```
Input: abc def ghi
Output: Anagram pairs: 0
```

**Constraints and edge cases:** Words contain letters only. Test three identical words and words of different lengths.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        String s = "Madam";
        String t = s.toLowerCase();
        boolean pal = true;
        for (int i = 0; i < t.length() / 2; i++) {
            if (t.charAt(i) != t.charAt(t.length() - 1 - i)) {
                pal = false;
            }
        }
        System.out.println(pal);

        char[] a = "stop".toCharArray();
        char[] b = "pots".toCharArray();
        Arrays.sort(a);
        Arrays.sort(b);
        System.out.println(new String(a));
        System.out.println(Arrays.equals(a, b));

        System.out.println("abc".compareTo("abd"));
        System.out.println("b".compareTo("a"));
        System.out.println("apple".compareTo("app"));
        System.out.println("Hello".equalsIgnoreCase("hELLO"));

        String x = "level";
        StringBuilder sb = new StringBuilder(x);
        System.out.println(sb.reverse().toString().equals(x));
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Read two words and print `Anagrams` if they contain exactly the same letters (ignoring letter case), otherwise `Not anagrams`. Exactly one message must be printed for each input. `Listen Silent` gives `Anagrams`; `abc abcd` gives `Not anagrams` (once).

The program has logic errors. Identify each, explain why it occurs, and provide the corrected program.

```java
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String a = sc.next();
        String b = sc.next();
        if (a.length() != b.length()) {
            System.out.println("Not anagrams");
        }
        char[] x = a.toCharArray();
        char[] y = b.toCharArray();
        Arrays.sort(x);
        Arrays.sort(y);
        if (x == y) {
            System.out.println("Anagrams");
        } else {
            System.out.println("Not anagrams");
        }
    }
}
```

---

<a id="day18"></a>
## Day 18 — One-Dimensional Arrays

**Learning objectives:** Declare, fill, and traverse arrays; find totals, maximum, minimum, and positions; understand array references and default values.

### Question 1: Mixed Real-World Scenario
**Title:** Weekly Sales Report

**Problem statement:** A shop records its sales for 7 days (Day 1 to Day 7). Read the seven values and print the total sales, the average daily sales, and the best day with its sales amount. If several days tie for the best, report the earliest one.

**Real-world context:** Store managers review weekly summaries to plan stock and staff.

**Input requirements:** Seven `int` values on one line.

**Expected output:**
```
Total: <total>
Average: <average with 2 decimals>
Best day: <day number> (<sales>)
```

**Sample cases:**
```
Input: 120 150 90 200 170 80 160
Output:
Total: 970
Average: 138.57
Best day: 4 (200)
```
```
Input: 100 100 100 100 100 100 100
Output:
Total: 700
Average: 100.00
Best day: 1 (100)
```

**Constraints:** Each value is between 0 and 100000.

### Question 2: Interview-Based Coding
**Title:** Second Largest Element

**Problem statement:** Read an array and print its second largest *distinct* value. If there is no such value (all elements are equal, or only one element exists), print `No second largest`.

**Input and output requirements:** Input: first line `n`, second line `n` integers. Output: the value or the message.

**Sample cases:**
```
Input:
5
10 20 4 45 99
Output: 45
```
```
Input:
4
7 7 7 7
Output: No second largest
```
```
Input:
3
5 9 9
Output: 5
```

**Constraints:** 1 ≤ n ≤ 100. Values may be negative.

### Question 3: Company-Style Programming
**Title:** Rotate Right

**Problem statement:** Rotate an array to the right by `k` positions, where every shift moves the last element to the front. Print the resulting array.

**Input and output requirements:** Input: line 1 `n`; line 2 `n` integers; line 3 `k`. Output: the rotated array on one line, separated by spaces.

**Sample cases:**
```
Input:
5
1 2 3 4 5
2
Output: 4 5 1 2 3
```
```
Input:
4
10 20 30 40
0
Output: 10 20 30 40
```
```
Input:
3
1 2 3
4
Output: 3 1 2
```

**Constraints and edge cases:** 1 ≤ n ≤ 100, 0 ≤ k ≤ 1000. `k` can be larger than `n`.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
public class Main {
    public static void main(String[] args) {
        int[] a = {5, 3, 8, 1};
        int sum = 0;
        for (int v : a) sum += v;
        System.out.println(sum);

        a[1] = a[0] + a[3];
        System.out.println(a[1] + " " + a.length);

        int[] b = a;
        b[0] = 99;
        System.out.println(a[0]);

        int[] c = new int[3];
        c[1] = 7;
        System.out.println(c[0] + c[1] + c[2]);
        System.out.println(java.util.Arrays.toString(c));

        int[] d = a.clone();
        d[3] = -1;
        System.out.println(a[3] + " " + d[3]);

        String[] names = new String[2];
        System.out.println(names[0]);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Read `n` and then `n` integers, and print the maximum and minimum values. For `4` and `7 3 9 5` the output is `Max: 9, Min: 3`. For `3` and `-5 -2 -9` the output is `Max: -2, Min: -9`.

The program has a runtime error and a logic error. Identify each, explain why it occurs, and provide the corrected program.

```java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] arr = new int[n];
        for (int i = 1; i <= n; i++) {
            arr[i] = sc.nextInt();
        }
        int max = 0, min = 0;
        for (int i = 0; i < n; i++) {
            if (arr[i] > max) max = arr[i];
            if (arr[i] < min) min = arr[i];
        }
        System.out.println("Max: " + max + ", Min: " + min);
    }
}
```

---

<a id="day19"></a>
## Day 19 — Searching, Sorting, and Duplicates

**Learning objectives:** Search an array with linear and binary search, sort with a simple algorithm, and detect repeated values.

### Question 1: Mixed Real-World Scenario
**Title:** Student Rank List

**Problem statement:** A class teacher has the marks of `n` students. Sort the marks from highest to lowest and print the top three ranks (or fewer if there are fewer than three students). Students with equal marks still receive different consecutive rank numbers (Rank 1, Rank 2, Rank 3 in sorted order).

**Real-world context:** Schools publish toppers lists after exams.

**Input requirements:** Line 1: `n`. Line 2: `n` integers (marks).

**Expected output:** Lines in the form `Rank <i>: <marks>`.

**Sample cases:**
```
Input:
5
72 95 88 60 95
Output:
Rank 1: 95
Rank 2: 95
Rank 3: 88
```
```
Input:
2
40 70
Output:
Rank 1: 70
Rank 2: 40
```

**Constraints:** 1 ≤ n ≤ 100; marks are between 0 and 100.

### Question 2: Interview-Based Coding
**Title:** Find All Positions

**Problem statement:** Read an array and a target value. Print every index (starting from 0) where the target occurs, in increasing order, or `Not found`.

**Input and output requirements:** Input: line 1 `n`; line 2 `n` integers; line 3 the target. Output: `Found at: <indices separated by spaces>` or `Not found`.

**Sample cases:**
```
Input:
6
4 2 7 2 9 2
2
Output: Found at: 1 3 5
```
```
Input:
6
4 2 7 2 9 2
5
Output: Not found
```

**Constraints:** 1 ≤ n ≤ 100.

### Question 3: Company-Style Programming
**Title:** Quick Lookup

**Problem statement:** An array of distinct integers is already sorted in increasing order. Print the index (starting from 0) of a target value, or `-1` if it is not present. Your solution should not check every element one by one; it should halve the search range at each step.

**Input and output requirements:** Input: line 1 `n`; line 2 `n` sorted integers; line 3 the target. Output: a single integer.

**Sample cases:**
```
Input:
7
2 5 8 12 16 23 38
23
Output: 5
```
```
Input:
7
2 5 8 12 16 23 38
2
Output: 0
```
```
Input:
7
2 5 8 12 16 23 38
7
Output: -1
```

**Constraints and edge cases:** 1 ≤ n ≤ 1000. Test the first element, the last element, and an array with one element.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
public class Main {
    public static void main(String[] args) {
        int[] a = {4, 2, 5, 1};
        for (int i = 0; i < a.length - 1; i++) {
            for (int j = 0; j < a.length - 1 - i; j++) {
                if (a[j] > a[j + 1]) {
                    int t = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = t;
                }
            }
            System.out.println(java.util.Arrays.toString(a));
        }

        int[] b = {3, 1, 3, 2, 1};
        int dup = 0;
        for (int i = 0; i < b.length; i++) {
            for (int j = i + 1; j < b.length; j++) {
                if (b[i] == b[j]) dup++;
            }
        }
        System.out.println(dup);

        int idx = java.util.Arrays.binarySearch(new int[]{1, 3, 5, 7}, 5);
        System.out.println(idx);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** `linearSearch` returns the index of `target` in `arr`, or `-1` if it is absent. `isSorted` returns `true` if the array is in non-decreasing order. The expected output is `2`, `-1`, `true`, `false` (for the arrays used in `main`).

The program has a compile-time error, a runtime error, and logic errors. Identify each, explain why it occurs, and provide the corrected program.

```java
public class Main {
    static int linearSearch(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                return i;
            } else {
                return -1;
            }
        }
    }

    static boolean isSorted(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > arr[i + 1]) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        int[] data = {10, 20, 30, 40};
        System.out.println(linearSearch(data, 30));
        System.out.println(linearSearch(data, 50));
        System.out.println(isSorted(data));
        System.out.println(isSorted(new int[]{3, 1, 2}));
    }
}
```

---

<a id="day20"></a>
## Day 20 — Two-Dimensional Arrays

**Learning objectives:** Store tables in 2D arrays, traverse rows and columns, and work with matrices (transpose and diagonals).

### Question 1: Mixed Real-World Scenario
**Title:** Class Marks Table

**Problem statement:** A teacher stores marks in a table where each row is a student and each column is a subject. Read the table, print each student's total, and then print which student scored the highest total. If there is a tie, report the first such student.

**Real-world context:** Report-card software processes a marks table for the whole class.

**Input requirements:** Line 1: `n m` (students and subjects). Then `n` lines of `m` integers.

**Expected output:**
```
Student <i> total: <total>      (one line per student, starting from 1)
Topper: Student <i>
```

**Sample cases:**
```
Input:
3 3
70 80 90
60 65 70
95 85 90
Output:
Student 1 total: 240
Student 2 total: 195
Student 3 total: 270
Topper: Student 3
```
```
Input:
2 2
50 50
50 50
Output:
Student 1 total: 100
Student 2 total: 100
Topper: Student 1
```

**Constraints:** 1 ≤ n, m ≤ 10; marks between 0 and 100.

### Question 2: Interview-Based Coding
**Title:** Matrix Transpose

**Problem statement:** The transpose of a matrix swaps its rows and columns. Read an `r × c` matrix and print its `c × r` transpose.

**Input and output requirements:** Input: line 1 `r c`, then `r` lines of `c` integers. Output: `c` lines of `r` integers separated by spaces.

**Sample cases:**
```
Input:
2 3
1 2 3
4 5 6
Output:
1 4
2 5
3 6
```
```
Input:
1 1
9
Output: 9
```

**Constraints:** 1 ≤ r, c ≤ 10. The matrix need not be square.

### Question 3: Company-Style Programming
**Title:** Diagonal Sums

**Problem statement:** For a square matrix, print the sum of the main diagonal (top-left to bottom-right) and the sum of the anti-diagonal (top-right to bottom-left). In an odd-sized matrix the center element belongs to both diagonals.

**Input and output requirements:** Input: line 1 `n`, then `n` lines of `n` integers. Output:
```
Main: <sum>
Anti: <sum>
```

**Sample cases:**
```
Input:
3
1 2 3
4 5 6
7 8 9
Output:
Main: 15
Anti: 15
```
```
Input:
2
1 2
3 5
Output:
Main: 6
Anti: 5
```
```
Input:
1
7
Output:
Main: 7
Anti: 7
```

**Constraints and edge cases:** 1 ≤ n ≤ 10. Values may be negative.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
public class Main {
    public static void main(String[] args) {
        int[][] m = {{1, 2, 3}, {4, 5, 6}};
        System.out.println(m.length + " " + m[0].length);
        System.out.println(m[1][2]);

        int sum = 0;
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++) {
                if (i != j) sum += m[i][j];
            }
        }
        System.out.println(sum);

        int[][] t = new int[3][2];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                t[i][j] = i * 2 + j;
            }
        }
        for (int[] row : t) {
            System.out.print(row[1] + " ");
        }
        System.out.println();

        int[][] jag = new int[3][];
        jag[0] = new int[]{1};
        jag[1] = new int[]{2, 3};
        jag[2] = new int[]{4, 5, 6};
        int total = 0;
        for (int[] r : jag) total += r.length;
        System.out.println(total);

        System.out.println(java.util.Arrays.deepToString(new int[][]{{1, 2}, {3}}));
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Read two `r × c` matrices and print their element-wise sum. For `2 2`, then `1 2 3 4`, then `5 6 7 8`, the output is:
```
6 8
10 12
```

The program has a runtime error and a logic error. Identify each, explain why it occurs, and provide the corrected program.

```java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int r = sc.nextInt();
        int c = sc.nextInt();
        int[][] a = new int[r][c];
        int[][] b = new int[r][c];
        int[][] sum = new int[r][c];
        for (int i = 0; i < r; i++)
            for (int j = 0; j < c; j++)
                a[i][j] = sc.nextInt();
        for (int i = 0; i < r; i++)
            for (int j = 0; j < c; j++)
                b[j][i] = sc.nextInt();
        for (int i = 0; i < r; i++) {
            for (int j = 0; j <= c; j++) {
                sum[i][j] = a[i][j] + b[i][j];
                System.out.print(sum[i][j] + " ");
            }
            System.out.println();
        }
    }
}
```

---

<a id="day21"></a>
## Day 21 — Classes, Objects, and Inheritance

**Learning objectives:** Define classes with fields, constructors, and methods; protect data with `private` fields and getters/setters; reuse code with inheritance and method overriding.

### Question 1: Mixed Real-World Scenario
**Title:** Bank Account Operations

**Problem statement:** Create a `BankAccount` class with a private balance, a constructor that sets the opening balance, `deposit(int)`, `withdraw(int)`, and `getBalance()`. A deposit or withdrawal of 0 or less is invalid. A withdrawal larger than the balance is refused. Process a list of operations and print the result of each.

**Real-world context:** Banking software must guard the balance so that only valid operations can change it.

**Input requirements:** Line 1: opening balance. Line 2: number of operations `n`. Then `n` lines, each `D <amount>` (deposit) or `W <amount>` (withdraw).

**Expected output:** One line per operation:
- `Deposited <amount>. Balance: <balance>`
- `Withdrawn <amount>. Balance: <balance>`
- `Insufficient funds. Balance: <balance>`
- `Invalid amount. Balance: <balance>`

**Sample cases:**
```
Input:
1000
3
D 500
W 200
W 5000
Output:
Deposited 500. Balance: 1500
Withdrawn 200. Balance: 1300
Insufficient funds. Balance: 1300
```
```
Input:
0
3
W 10
D -5
D 10
Output:
Insufficient funds. Balance: 0
Invalid amount. Balance: 0
Deposited 10. Balance: 10
```

**Constraints:** Opening balance is 0 or more; 1 ≤ n ≤ 20.

### Question 2: Interview-Based Coding
**Title:** Shapes with Inheritance

**Problem statement:** Create a base class `Shape` with a method `double area()` that returns `0`. Create subclasses `Circle` (radius) and `Rectangle` (length, width) that override `area()`. Read a shape description and print its area using a `Shape` reference.

**Input and output requirements:** Input: `circle <radius>` or `rectangle <length> <width>` (decimals allowed). Output: `Area: <value>` with 2 decimal places (use `Math.PI`).

**Sample cases:**
```
Input: circle 5
Output: Area: 78.54
```
```
Input: rectangle 4 6
Output: Area: 24.00
```
```
Input: circle 1
Output: Area: 3.14
```

**Constraints:** Dimensions are positive.

### Question 3: Company-Style Programming
**Title:** Payroll with Employees and Managers

**Problem statement:** An `Employee` has a name and a base salary and a method `totalSalary()` that returns the base salary. A `Manager` extends `Employee`, has an additional bonus percentage, and overrides `totalSalary()` to add that percentage of the base salary. Read `n` staff records, print each person's total salary and then the total payroll.

**Input and output requirements:** Line 1: `n`. Then `n` lines: `E <name> <salary>` for an employee or `M <name> <salary> <bonusPercent>` for a manager. Output: `<name>: <total>` per person (2 decimal places), then `Total payroll: <sum>`.

**Sample cases:**
```
Input:
3
E Asha 30000
M Ravi 50000 10
E Kiran 25000
Output:
Asha: 30000.00
Ravi: 55000.00
Kiran: 25000.00
Total payroll: 110000.00
```
```
Input:
1
M Neha 40000 0
Output:
Neha: 40000.00
Total payroll: 40000.00
```

**Constraints and edge cases:** 1 ≤ n ≤ 20; names contain no spaces; a bonus of `0` is allowed.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
class Animal {
    String name = "animal";

    Animal() {
        System.out.println("Animal created");
    }

    void speak() {
        System.out.println("...");
    }
}

class Dog extends Animal {
    String name = "dog";

    Dog() {
        super();
        System.out.println("Dog created");
    }

    @Override
    void speak() {
        System.out.println("Woof");
    }
}

class Counter {
    static int total = 0;
    int id;

    Counter() {
        total++;
        id = total;
    }
}

public class Main {
    public static void main(String[] args) {
        Animal a = new Dog();
        a.speak();
        System.out.println(a.name);
        System.out.println(((Dog) a).name);

        Counter c1 = new Counter();
        Counter c2 = new Counter();
        Counter c3 = c2;
        System.out.println(c1.id + " " + c2.id + " " + Counter.total);
        System.out.println(c3 == c2);
        System.out.println(c1 == c2);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** A `Student` stores a private name and marks. Marks can only be changed through `setMarks`, which accepts values from 0 to 100. `grade()` returns `Pass` for marks of 40 or more, otherwise `Fail`. In `main`, create `Isha` with marks 55, change her marks to 30 using the setter, and print `Isha: Fail`. Then create `Ravi` with marks 80 and print `Ravi`.

The program has compile-time errors and a logic error. Identify each, explain why it occurs, and provide the corrected program.

```java
class Student {
    private String name;
    private int marks;

    Student(String name, int marks) {
        name = name;
        this.marks = marks;
    }

    public String getName() {
        return name;
    }

    public void setMarks(int marks) {
        if (marks >= 0 && marks <= 100) this.marks = marks;
    }

    public String grade() {
        if (marks >= 40) return "Pass";
        else "Fail";
    }
}

public class Main {
    public static void main(String[] args) {
        Student s = new Student("Isha", 55);
        s.marks = 30;
        System.out.println(s.getName() + ": " + s.grade());
        Student t = new Student("Ravi");
        System.out.println(t.getName());
    }
}
```

---

<a id="day22"></a>
## Day 22 — ArrayList

**Learning objectives:** Store changing collections of items in `ArrayList`, use `add`, `remove`, `get`, `set`, `size`, `contains`, and sort with `Collections`.

### Question 1: Mixed Real-World Scenario
**Title:** Shopping Cart

**Problem statement:** Build a shopping cart using an `ArrayList<String>`. Process a list of commands: `ADD <item>`, `REMOVE <item>` (removes the first matching item), and `SHOW`.

**Real-world context:** Every e-commerce site keeps a cart that customers add to and remove from.

**Input requirements:** Line 1: number of commands `n`. Then `n` lines, each a command. Item names contain no spaces.

**Expected output:**
- `ADD`: `Added: <item>`
- `REMOVE`: `Removed: <item>` if found, otherwise `Not in cart: <item>`
- `SHOW`: `Cart: [<items separated by comma and space>]`, or `Cart is empty` if there are no items

**Sample cases:**
```
Input:
5
ADD milk
ADD bread
REMOVE eggs
REMOVE milk
SHOW
Output:
Added: milk
Added: bread
Not in cart: eggs
Removed: milk
Cart: [bread]
```
```
Input:
2
SHOW
ADD tea
Output:
Cart is empty
Added: tea
```

**Constraints:** 1 ≤ n ≤ 50. The same item can be added more than once.

### Question 2: Interview-Based Coding
**Title:** Remove Duplicates, Keep Order

**Problem statement:** Read a list of integers and print it with duplicates removed. Each value must stay at the position of its first appearance.

**Input and output requirements:** Input: line 1 `n`; line 2 `n` integers. Output: the remaining values separated by spaces.

**Sample cases:**
```
Input:
6
1 2 2 3 1 4
Output: 1 2 3 4
```
```
Input:
3
5 5 5
Output: 5
```

**Constraints:** 1 ≤ n ≤ 100. Values may be negative.

### Question 3: Company-Style Programming
**Title:** Merge Two Sorted Lists

**Problem statement:** Two lists of integers are each sorted in increasing order. Merge them into one sorted list without sorting the combined list from scratch (use the fact that both are already sorted). Print the merged list.

**Input and output requirements:** Input: line 1 `n`; line 2 `n` integers; line 3 `m`; line 4 `m` integers. Output: the merged list, separated by spaces.

**Sample cases:**
```
Input:
3
1 4 7
4
2 3 5 9
Output: 1 2 3 4 5 7 9
```
```
Input:
2
1 1
2
1 2
Output: 1 1 1 2
```

**Constraints and edge cases:** 1 ≤ n, m ≤ 100. Duplicate values appear in the output as many times as they occur.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(10);
        list.add(20);
        list.add(30);
        list.add(1, 15);
        System.out.println(list);

        list.remove(2);
        System.out.println(list);

        list.remove(Integer.valueOf(10));
        System.out.println(list);

        list.set(0, 99);
        System.out.println(list.get(0) + " " + list.size());
        System.out.println(list.contains(30) + " " + list.indexOf(5));

        List<String> words = new ArrayList<>(List.of("pear", "apple", "fig"));
        Collections.sort(words);
        System.out.println(words);
        Collections.reverse(words);
        System.out.println(words + " " + Collections.max(words));

        for (int i = 0; i < words.size(); i++) {
            if (words.get(i).length() == 3) words.remove(i);
        }
        System.out.println(words);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Read `n` integers into a list, remove every even number, and print the remaining numbers and how many there are. For `6` and `2 4 5 6 7 8` the output is `Odd numbers: [5, 7]` and `Count: 2`.

The program has compile-time errors and a logic error. Identify each, explain why it occurs, and provide the corrected program.

```java
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        ArrayList<int> nums = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            nums.add(sc.nextInt());
        }
        for (int i = 0; i < nums.size(); i++) {
            if (nums.get(i) % 2 == 0) {
                nums.remove(i);
            }
        }
        System.out.println("Odd numbers: " + nums);
        System.out.println("Count: " + nums.length);
    }
}
```

---

<a id="day23"></a>
## Day 23 — HashSet and HashMap

**Learning objectives:** Use `HashSet` to track unique values and `HashMap` to count and look up values by key. Remember that the iteration order of hash-based collections is not guaranteed.

### Question 1: Mixed Real-World Scenario
**Title:** Website Visitor Counter

**Problem statement:** A website logs the name of every visitor each time a page is opened. Given the log, print the total number of visits, the number of unique visitors, and the number of returning visits (visits made by someone who had already visited earlier in the log).

**Real-world context:** Analytics dashboards distinguish unique visitors from repeat visits.

**Input requirements:** Line 1: `n`. Line 2: `n` visitor names (no spaces in a name).

**Expected output:**
```
Total visits: <n>
Unique visitors: <u>
Returning visits: <r>
```

**Sample cases:**
```
Input:
6
amit riya amit sam riya amit
Output:
Total visits: 6
Unique visitors: 3
Returning visits: 3
```
```
Input:
3
a b c
Output:
Total visits: 3
Unique visitors: 3
Returning visits: 0
```

**Constraints:** 1 ≤ n ≤ 100. Names are case-sensitive.

### Question 2: Interview-Based Coding
**Title:** Word Frequency

**Problem statement:** Read `n` words and print how many times each distinct word appears. Print the words in alphabetical order (a `HashMap` does not keep a guaranteed order, so order the output yourself).

**Input and output requirements:** Input: line 1 `n`; line 2 `n` words. Output: one line per distinct word in the form `<word>: <count>`.

**Sample cases:**
```
Input:
5
apple banana apple cherry banana
Output:
apple: 2
banana: 2
cherry: 1
```
```
Input:
1
x
Output: x: 1
```

**Constraints:** 1 ≤ n ≤ 100. Words are lowercase letters only.

### Question 3: Company-Style Programming
**Title:** Pair With Target Sum

**Problem statement:** Given an array of integers and a target, find the two different positions `i < j` whose values add up to the target. The input is guaranteed to contain exactly one such pair. Print the two indices (starting from 0).

**Input and output requirements:** Input: line 1 `n`; line 2 `n` integers; line 3 the target. Output: `<i> <j>`.

**Sample cases:**
```
Input:
4
2 7 11 15
9
Output: 0 1
```
```
Input:
3
3 2 4
6
Output: 1 2
```
```
Input:
2
3 3
6
Output: 0 1
```

**Constraints and edge cases:** 2 ≤ n ≤ 1000. Values may be negative or repeated. The same position cannot be used twice (in `3 2 4` with target `6`, the first `3` alone does not count).

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        Set<Integer> set = new HashSet<>();
        System.out.println(set.add(5));
        System.out.println(set.add(5));
        set.add(7);
        set.add(5);
        System.out.println(set.size());

        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("a", 10);
        System.out.println(map.get("a") + " " + map.size());
        System.out.println(map.get("z"));
        System.out.println(map.getOrDefault("z", 0));
        System.out.println(map.containsKey("b") + " " + map.containsValue(1));
        map.put("b", map.get("b") + 5);
        System.out.println(map.get("b"));

        String text = "abca";
        Map<Character, Integer> freq = new TreeMap<>();
        for (char c : text.toCharArray()) {
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }
        System.out.println(freq);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Read `n` words and print the word that appears most often. The input guarantees there is one clear winner. For `5` and `a b a c a` the output is `Most frequent: a`.

The program has compile-time errors and a runtime error. Identify each, explain why it occurs, and provide the corrected program.

```java
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        HashMap<String, int> counts = new HashMap<>();
        for (int i = 0; i < n; i++) {
            String w = sc.next();
            if (counts.containsKey(w)) {
                counts.put(w, counts.get(w) + 1);
            } else {
                counts.put(w);
            }
        }
        String best = "";
        for (String key : counts) {
            if (counts.get(key) > counts.get(best)) best = key;
        }
        System.out.println("Most frequent: " + best);
    }
}
```

---

<a id="day24"></a>
## Day 24 — Exception Handling

**Learning objectives:** Use `try`, `catch`, and `finally`; recognize common exceptions; throw an exception on invalid data; order `catch` blocks correctly.

### Question 1: Mixed Real-World Scenario
**Title:** Ticket Booking Input Handler

**Problem statement:** A booking counter lets a customer book between 1 and 6 tickets. The quantity arrives as text. Read it and respond: if it is not a whole number print `Invalid number`; if it is a whole number outside 1–6 print `Allowed: 1 to 6`; otherwise print `Booked <n> tickets`. Use exceptions as part of your solution (for example, handle the parsing failure with `try-catch`).

**Real-world context:** Booking systems receive text from forms and must handle bad input without crashing.

**Input requirements:** One word read as a `String`.

**Expected output:** One of the three messages above.

**Sample cases:**
```
Input: 3
Output: Booked 3 tickets
```
```
Input: abc
Output: Invalid number
```
```
Input: 9
Output: Allowed: 1 to 6
```
```
Input: 2.5
Output: Invalid number
```

**Constraints:** Test `0`, `1`, `6`, `7`, and `-2`.

### Question 2: Interview-Based Coding
**Title:** Safe Division with finally

**Problem statement:** Read two values and divide the first by the second using integers. If the second is zero print `Cannot divide by zero`. If either value is not an integer print `Invalid input`. Otherwise print `Result: <quotient>`. In every case, print `Done` as the last line (use a `finally` block).

**Input and output requirements:** Input: two words on one line. Output: the message(s) described above.

**Sample cases:**
```
Input: 10 2
Output:
Result: 5
Done
```
```
Input: 7 0
Output:
Cannot divide by zero
Done
```
```
Input: ten 2
Output:
Invalid input
Done
```

**Constraints:** Integers fit in `int`. Read both values as text first so that invalid input can be detected by your own code.

### Question 3: Company-Style Programming
**Title:** Safe Array Access

**Problem statement:** An array of `n` integers is given, followed by `q` index queries. For each query print the value at that index, or `Index out of range` if the index is not valid for the array. Handle the failure with `try-catch` rather than only with `if` checks.

**Input and output requirements:** Line 1: `n`. Line 2: `n` integers. Line 3: `q`. Then `q` lines, each an integer index. Output: one line per query.

**Sample cases:**
```
Input:
4
10 20 30 40
3
2
7
-1
Output:
30
Index out of range
Index out of range
```
```
Input:
1
5
2
0
0
Output:
5
5
```

**Constraints and edge cases:** 1 ≤ n ≤ 100, 1 ≤ q ≤ 50. Test the index `n` (one past the end) and negative indexes.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
public class Main {
    static int test(int x) {
        try {
            if (x == 0) throw new IllegalArgumentException("zero");
            System.out.println("try " + x);
            return 10 / (x - 1);
        } catch (IllegalArgumentException e) {
            System.out.println("caught " + e.getMessage());
            return -1;
        } catch (ArithmeticException e) {
            System.out.println("arith");
            return -2;
        } finally {
            System.out.println("finally " + x);
        }
    }

    public static void main(String[] args) {
        System.out.println(test(0));
        System.out.println(test(1));
        System.out.println(test(5));
        try {
            String s = null;
            System.out.println(s.length());
        } catch (NullPointerException e) {
            System.out.println("NPE");
        }
        try {
            Integer.parseInt("12a");
        } catch (NumberFormatException e) {
            System.out.println("NFE");
        }
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Read one word as the age. For `25` print `Age: 25` then `Finished`. For `abc` print `Not a number` then `Finished`. For `-4` print `Error: Age cannot be negative` then `Finished`.

The program has compile-time errors. Identify each, explain why it occurs, and provide the corrected program.

```java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            int age = Integer.parseInt(sc.next());
            if (age < 0) {
                throw IllegalArgumentException("Age cannot be negative");
            }
            System.out.println("Age: " + age);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Not a number");
        } finally {
            System.out.println("Finished")
        }
    }
}
```

---

<a id="day25"></a>
## Day 25 — File Handling Basics

**Learning objectives:** Write text to a file and read it back with `FileWriter`/`PrintWriter`, `Scanner`, and `BufferedReader`; close resources safely with try-with-resources; handle `IOException`.

*Note: File terminology — a file path like `students.txt` (no folder) refers to a file in the program's current working directory. Each program below should create its own file, so no existing files are needed.*

### Question 1: Mixed Real-World Scenario
**Title:** Student Records File

**Problem statement:** A school stores results in a text file. Read `n` records from the keyboard and write them to a file named `students.txt` (one record per line). Then read the file back and, for each record, print the student's result (`Pass` for marks of 40 or more, otherwise `Fail`). Finally print the class average from the data you read from the file.

**Real-world context:** Applications save data to files so it is not lost when the program ends.

**Input requirements:** Line 1: `n`. Then `n` lines: `<name> <marks>`.

**Expected output:** One line per student (`<name>: <marks> - <Pass/Fail>`), then `Average: <value>` with 2 decimal places.

**Sample cases:**
```
Input:
3
Asha 78
Ravi 35
Meena 90
Output:
Asha: 78 - Pass
Ravi: 35 - Fail
Meena: 90 - Pass
Average: 67.67
```
```
Input:
1
Tom 40
Output:
Tom: 40 - Pass
Average: 40.00
```

**Constraints:** 1 ≤ n ≤ 20; names contain no spaces; marks are 0–100.

### Question 2: Interview-Based Coding
**Title:** File Statistics

**Problem statement:** Read `n` lines of text from the keyboard and write them to a file `notes.txt`. Read the file back and print the number of lines, the number of words, and the number of characters (not counting the line-break characters).

**Input and output requirements:** Input: line 1 `n`, then `n` lines of text (words separated by single spaces). Output:
```
Lines: <n>
Words: <w>
Characters: <c>
```

**Sample cases:**
```
Input:
2
hello world
java is fun
Output:
Lines: 2
Words: 5
Characters: 22
```
```
Input:
1
a
Output:
Lines: 1
Words: 1
Characters: 1
```

**Constraints:** 1 ≤ n ≤ 20; each line has 1–100 characters and no leading or trailing spaces.

### Question 3: Company-Style Programming
**Title:** Log Level Counter

**Problem statement:** A server writes log lines that start with a level word: `ERROR`, `WARN`, or `INFO`. Read `n` log lines from the keyboard, save them to `app.log`, then read the file and count how many lines start with each level. Lines that start with anything else are ignored. Level words are case-sensitive.

**Input and output requirements:** Input: line 1 `n`, then `n` lines of text. Output:
```
ERROR: <count>
WARN: <count>
INFO: <count>
```

**Sample cases:**
```
Input:
5
INFO Server started
ERROR Disk full
WARN Low memory
ERROR Timeout
INFO Request ok
Output:
ERROR: 2
WARN: 1
INFO: 2
```
```
Input:
2
debug x
INFO y
Output:
ERROR: 0
WARN: 0
INFO: 1
```

**Constraints and edge cases:** 1 ≤ n ≤ 50. A word such as `ERRORS` is not an `ERROR` level (the level must be the first word of the line).

### Question 4: Predict the Output
Predict the exact output of this Java 17 program (it creates and deletes its own file).

```java
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        File f = new File("temp_demo.txt");
        try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
            pw.println("alpha");
            pw.println("beta 2");
            pw.print("gamma");
        }

        try (Scanner sc = new Scanner(f)) {
            int lines = 0;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                lines++;
                System.out.println(lines + ": " + line.toUpperCase());
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String first = br.readLine();
            System.out.println(first.length());
        }

        System.out.println(f.exists());
        System.out.println(f.delete());
        System.out.println(f.exists());
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Write the numbers 1, 2, and 3 to a file `numbers.txt`, one number per line. Read the file back, add the numbers, and print `Sum: 6`.

The program has compile-time errors and logic/runtime problems. Identify each, explain why it occurs, and provide the corrected program.

```java
import java.io.*;

public class Main {
    public static void main(String[] args) {
        FileWriter writer = new FileWriter("numbers.txt");
        for (int i = 1; i <= 3; i++) {
            writer.write(i);
            writer.write("\n");
        }
        BufferedReader reader = new BufferedReader(new FileReader("numbers.txt"));
        String line;
        int sum = 0;
        while ((line = reader.readLine()) != null) {
            sum += line;
        }
        System.out.println("Sum: " + sum);
    }
}
```

---

<a id="day26"></a>
## Day 26 — Recursion

**Learning objectives:** Understand a recursive method (a method that calls itself), identify the base case and the recursive case, and trace calls and returns.

### Question 1: Mixed Real-World Scenario
**Title:** Rocket Launch Countdown

**Problem statement:** A launch control screen counts down from a given number of seconds. Write a recursive method `countdown(int n)` that prints `T-minus <n>` for each value from `n` down to 1 (one per line) and then prints `Liftoff!`.

**Real-world context:** Countdown displays are used in launches, exams, and timers.

**Input requirements:** One non-negative `int` `n`.

**Expected output:** The countdown lines followed by `Liftoff!`.

**Sample cases:**
```
Input: 3
Output:
T-minus 3
T-minus 2
T-minus 1
Liftoff!
```
```
Input: 0
Output: Liftoff!
```

**Constraints:** 0 ≤ n ≤ 100. The countdown itself must be done by recursion, not by a loop.

### Question 2: Interview-Based Coding
**Title:** Fibonacci with Recursion

**Problem statement:** The Fibonacci sequence starts `0, 1, 1, 2, 3, 5, 8, …`, where each term is the sum of the two terms before it (`F(0) = 0`, `F(1) = 1`). Write a recursive method to compute `F(n)` and print it.

**Input and output requirements:** Input: one `int` `n`. Output: `F(n)`.

**Sample cases:**
```
Input: 6
Output: 8
```
```
Input: 0
Output: 0
```
```
Input: 10
Output: 55
```

**Constraints:** 0 ≤ n ≤ 20.

### Question 3: Company-Style Programming
**Title:** Recursive Power

**Problem statement:** Read an integer base `x` and a non-negative integer exponent `n`. Compute `x` raised to the power `n` using a recursive method (do not use `Math.pow` or loops).

**Input and output requirements:** Input: `x n` on one line. Output: the result as a whole number.

**Sample cases:**
```
Input: 2 10
Output: 1024
```
```
Input: 5 0
Output: 1
```
```
Input: -2 3
Output: -8
```

**Constraints and edge cases:** -10 ≤ x ≤ 10, 0 ≤ n ≤ 15. Any number to the power `0` is `1`. Use a data type that holds the largest result.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
public class Main {
    static int f(int n) {
        if (n <= 1) return 1;
        return n * f(n - 1);
    }

    static void print(int n) {
        if (n == 0) return;
        System.out.print(n + " ");
        print(n - 1);
        System.out.print(n + " ");
    }

    static int mystery(int a, int b) {
        if (b == 0) return a;
        return mystery(b, a % b);
    }

    static String rev(String s) {
        if (s.isEmpty()) return "";
        return rev(s.substring(1)) + s.charAt(0);
    }

    public static void main(String[] args) {
        System.out.println(f(5));
        print(3);
        System.out.println();
        System.out.println(mystery(48, 18));
        System.out.println(rev("abc"));
    }
}
```

### Question 5: Debug the Code
**What the program should do:** `sumTo(n)` returns `1 + 2 + … + n` (so `sumTo(4)` is `10`). `countDown(n)` prints `n`, `n-1`, … down to `1`, one per line, and then stops. `main` should print `10`, then `3`, `2`, `1`.

The program has compile-time errors and recursion errors (one of which would crash the program). Identify each, explain why it occurs, and provide the corrected program.

```java
public class Main {
    static int sumTo(int n) {
        if (n == 0) {
            return 1;
        }
        return n + sumTo(n);
    }

    static int countDown(int n) {
        System.out.println(n);
        countDown(n - 1);
    }

    public static void main(String[] args) {
        System.out.println(sumTo(4));
        countDown(3);
    }
}
```

---

<a id="day27"></a>
## Day 27 — Complexity and Optimization

**Learning objectives:** Estimate how many steps a solution takes as the input grows (introductory Big-O: constant, linear `O(n)`, quadratic `O(n²)`, logarithmic `O(log n)`), and replace slow nested loops with a single pass or a `HashSet`/`HashMap`.

For Questions 1–3, also state the time complexity of your solution in a comment at the top of your code. The input sizes are chosen so that very slow solutions will not finish in a reasonable time.

### Question 1: Mixed Real-World Scenario
**Title:** Duplicate Order Detector

**Problem statement:** An online store logs order IDs as they arrive. A repeated ID means an order was submitted twice. Scan the IDs in the order they arrive and print the first ID that has already appeared earlier in the log, or `No duplicates`.

**Real-world context:** Payment systems must catch double submissions quickly, even with large logs.

**Input requirements:** Line 1: `n`. Line 2: `n` positive integers (order IDs).

**Expected output:** `Duplicate: <id>` or `No duplicates`.

**Sample cases:**
```
Input:
6
101 205 333 205 101 999
Output: Duplicate: 205
```
```
Input:
3
1 2 3
Output: No duplicates
```

**Constraints:** 1 ≤ n ≤ 100000; IDs are between 1 and 1000000000.

### Question 2: Interview-Based Coding
**Title:** Best Window Sum

**Problem statement:** Given an array and a window size `k`, find the largest sum of any `k` consecutive elements.

**Input and output requirements:** Input: line 1 `n k`; line 2 `n` integers. Output: the maximum window sum.

**Sample cases:**
```
Input:
6 3
2 1 5 1 3 2
Output: 9
```
```
Input:
4 4
-1 -2 -3 -4
Output: -10
```
```
Input:
5 1
3 -1 7 2 0
Output: 7
```

**Constraints:** 1 ≤ k ≤ n ≤ 100000; each value is between -1000 and 1000.

### Question 3: Company-Style Programming
**Title:** Missing Number

**Problem statement:** An array holds `n` distinct integers taken from the range `0` to `n` (inclusive), so exactly one number from that range is missing. Print the missing number.

**Input and output requirements:** Input: line 1 `n`; line 2 `n` integers (in any order). Output: `Missing: <number>`.

**Sample cases:**
```
Input:
3
3 0 1
Output: Missing: 2
```
```
Input:
2
0 1
Output: Missing: 2
```
```
Input:
1
1
Output: Missing: 0
```

**Constraints and edge cases:** 1 ≤ n ≤ 100000. The missing value can be `0` or `n`.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
public class Main {
    public static void main(String[] args) {
        int n = 5;

        int ops1 = 0;
        for (int i = 0; i < n; i++) ops1++;

        int ops2 = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) ops2++;

        int ops3 = 0;
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++) ops3++;

        int ops4 = 0;
        for (int i = 1; i < 64; i *= 2) ops4++;

        int ops5 = 0;
        int x = 100;
        while (x > 0) {
            x /= 10;
            ops5++;
        }

        int ops6 = 0;
        for (int i = 0; i < n; i++) ops6++;
        for (int j = 0; j < n; j++) ops6++;

        System.out.println(ops1 + " " + ops2 + " " + ops3 + " " + ops4 + " " + ops5 + " " + ops6);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Count the prime numbers from 2 up to `n` using the Sieve of Eratosthenes. For `n = 30` the output is `Primes up to 30: 10`.

The program has a logic error and a runtime error. Identify each, explain why it occurs, and provide the corrected program.

```java
public class Main {
    public static void main(String[] args) {
        int n = 30;
        boolean[] composite = new boolean[n];
        int count = 0;
        for (int i = 1; i <= n; i++) {
            if (!composite[i]) {
                count++;
                for (int j = i * i; j <= n; j += i) {
                    composite[j] = true;
                }
            }
        }
        System.out.println("Primes up to " + n + ": " + count);
    }
}
```

---

<a id="day28"></a>
## Day 28 — Multi-Step Business Scenarios

**Learning objectives:** Combine conditions, loops, strings, arrays, methods, and collections to solve problems that involve several rules in sequence.

### Question 1: Mixed Real-World Scenario
**Title:** Hotel Booking Bill

**Problem statement:** A hotel has three room types: `S` (Standard, 2000 per night), `D` (Deluxe, 3500 per night), and `L` (Luxury, 5000 per night). Rules, applied in this order:
1. Room charge = rate × nights.
2. Discount on the room charge: 10% for 3 or more nights, 20% for 7 or more nights (the larger one applies, they do not add).
3. Optional breakfast: 300 per night (not discounted).
4. GST of 12% on (room charge − discount + breakfast).
5. Total = taxable amount + GST.

If the room type is not `S`, `D`, or `L`, print `Invalid room type`.

**Real-world context:** Hotel billing systems apply tiered discounts, add-ons, and tax in a fixed order.

**Input requirements:** One line: `<roomType> <nights> <breakfast>` where breakfast is `Y` or `N`.

**Expected output:**
```
Room charge: <value>
Discount: <value>
Breakfast: <value>
GST: <value>
Total: <value>
```
(all with 2 decimal places)

**Sample cases:**
```
Input: D 3 Y
Output:
Room charge: 10500.00
Discount: 1050.00
Breakfast: 900.00
GST: 1242.00
Total: 11592.00
```
```
Input: S 1 N
Output:
Room charge: 2000.00
Discount: 0.00
Breakfast: 0.00
GST: 240.00
Total: 2240.00
```
```
Input: L 7 N
Output:
Room charge: 35000.00
Discount: 7000.00
Breakfast: 0.00
GST: 3360.00
Total: 31360.00
```

**Constraints:** 1 ≤ nights ≤ 30. Test `2`, `3`, `6`, and `7` nights.

### Question 2: Interview-Based Coding
**Title:** First Non-Repeating Character

**Problem statement:** Read a lowercase word and print the first character that appears exactly once in it. If every character repeats, print `None`.

**Input and output requirements:** Input: one word. Output: the character or `None`.

**Sample cases:**
```
Input: swiss
Output: w
```
```
Input: leetcode
Output: l
```
```
Input: aabb
Output: None
```

**Constraints:** 1 ≤ length ≤ 1000; lowercase letters only.

### Question 3: Company-Style Programming
**Title:** Inventory Restock Planner

**Problem statement:** A store tracks `n` products. Each has a name, current stock, and a minimum level. A product needs restocking if its stock is below its minimum level; the quantity to order is `minimum − stock`. Print the products that need restocking in the order they were given, then the total units to order. If no product needs restocking print `All stocked`.

**Input and output requirements:** Line 1: `n`. Then `n` lines: `<name> <stock> <minimum>`. Output: `<name>: order <quantity>` for each product to restock, then `Total units to order: <sum>`; or `All stocked`.

**Sample cases:**
```
Input:
4
pen 50 20
ink 5 10
pad 0 5
cap 30 30
Output:
ink: order 5
pad: order 5
Total units to order: 10
```
```
Input:
2
a 10 5
b 8 8
Output: All stocked
```

**Constraints and edge cases:** 1 ≤ n ≤ 50; stock and minimum are between 0 and 10000. A product with stock equal to its minimum is not restocked.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        String text = "to be or not to be";
        String[] words = text.split(" ");

        Map<String, Integer> counts = new TreeMap<>();
        for (String w : words) {
            counts.put(w, counts.getOrDefault(w, 0) + 1);
        }
        System.out.println(counts);

        List<String> unique = new ArrayList<>();
        for (String w : words) {
            if (!unique.contains(w)) unique.add(w);
        }
        System.out.println(unique);

        int total = 0;
        for (int c : counts.values()) total += c;
        System.out.println(total + " " + words.length + " " + unique.size());

        StringBuilder sb = new StringBuilder();
        for (int i = words.length - 1; i >= 0; i--) {
            sb.append(words[i].charAt(0));
        }
        System.out.println(sb);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Read `n` items (`name price quantity`), add up the bill, and apply a discount: 15% if the total is 5000 or more, 10% if the total is 2000 or more, otherwise none. For the input `2`, then `pen 100 3`, then `bag 2500 2`, the output is:
```
Total: 5300.00
Discount: 795.00
Final: 4505.00
```

The program has logic errors only (it compiles). Identify each, explain why it occurs, and provide the corrected program.

```java
import java.util.Scanner;

public class Main {
    static double discountFor(double amount) {
        if (amount >= 5000) return 15;
        if (amount > 2000) return 0.10;
        else return 0;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        double total = 0;
        for (int i = 0; i < n; i++) {
            String item = sc.next();
            double price = sc.nextDouble();
            int qty = sc.nextInt();
            total =+ price * qty;
        }
        double discount = total * discountFor(total);
        double finalAmount = total - discount;
        System.out.printf("Total: %.2f%n", total);
        System.out.printf("Discount: %.2f%n", discount);
        System.out.printf("Final: %.2f%n", finalAmount);
    }
}
```

---

<a id="day29"></a>
## Day 29 — Mixed-Topic Revision

**Learning objectives:** Revise loops, strings, arrays, methods, collections, and tracing skills together. Treat each question as if you were in a timed practice test.

### Question 1: Mixed Real-World Scenario
**Title:** Library Fine Calculator

**Problem statement:** A library charges late fees per returned book: 2 per day for the first 7 days late, then 5 per day for every day after that. The fine for one book is capped at 100 (apply the cap first). Library members then get 50% off that capped fine. Read the member flag and the days late for each book, and print each fine and the total.

**Real-world context:** Libraries use tiered late fees with member benefits.

**Input requirements:** Line 1: `Y` (member) or `N` (not a member). Line 2: `n`, the number of books. Line 3: `n` integers, the days late for each book.

**Expected output:** One line `Book <i> fine: <fine>` per book, then `Total fine: <total>` (all with 2 decimal places).

**Sample cases:**
```
Input:
N
3
0 5 12
Output:
Book 1 fine: 0.00
Book 2 fine: 10.00
Book 3 fine: 39.00
Total fine: 49.00
```
```
Input:
Y
2
30 3
Output:
Book 1 fine: 50.00
Book 2 fine: 3.00
Total fine: 53.00
```

**Constraints:** 1 ≤ n ≤ 20; 0 ≤ days late ≤ 365.

### Question 2: Interview-Based Coding
**Title:** FizzBuzz

**Problem statement:** Print the numbers from 1 to `n`, one per line. For multiples of 3 print `Fizz` instead of the number, for multiples of 5 print `Buzz`, and for multiples of both 3 and 5 print `FizzBuzz`.

**Input and output requirements:** Input: one `int` `n`. Output: `n` lines.

**Sample cases:**
```
Input: 3
Output:
1
2
Fizz
```
```
Input: 5
Output:
1
2
Fizz
4
Buzz
```

**Constraints:** 1 ≤ n ≤ 100. Also check `15`, where the last line must be `FizzBuzz`.

### Question 3: Company-Style Programming
**Title:** Run-Length Encoding

**Problem statement:** Compress a lowercase word by replacing each run of the same letter with the letter followed by the length of the run.

**Input and output requirements:** Input: one word. Output: the encoded string.

**Sample cases:**
```
Input: aaabbc
Output: a3b2c1
```
```
Input: abc
Output: a1b1c1
```
```
Input: zzzzzzzzzzzz
Output: z12
```

**Constraints and edge cases:** 1 ≤ length ≤ 100; lowercase letters only. Runs can be longer than 9. A letter that appears in separate runs (`abab`) is encoded run by run (`a1b1a1b1`).

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
public class Main {
    static int mystery(int[] a) {
        int r = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] % 2 == 0) r += a[i];
            else r -= a[i];
        }
        return r;
    }

    public static void main(String[] args) {
        int[] data = {4, 7, 2, 9, 6};
        System.out.println(mystery(data));

        String s = "Java17";
        int letters = 0, digits = 0;
        for (char ch : s.toCharArray()) {
            if (Character.isLetter(ch)) letters++;
            else if (Character.isDigit(ch)) digits++;
        }
        System.out.println(letters + "-" + digits);

        int x = 5;
        x = x++ * 2 + --x;
        System.out.println(x);

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 3; i++) {
            for (int j = i; j <= 3; j++) {
                sb.append(i * j).append(',');
            }
        }
        System.out.println(sb);

        System.out.println(10 / 4 + 10 % 4 + 10 / 4.0);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Read `n` words and print how many of them are palindromes (ignoring letter case), followed by those words in uppercase, in the order given. For `4` and `level java noon a` the output is:
```
Palindromes: 3
LEVEL
NOON
A
```
A word like `Madam` must also count as a palindrome.

The program has compile-time and logic errors. Identify each, explain why it occurs, and provide the corrected program.

```java
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static boolean isPalindrome(String s) {
        int i = 0, j = s.length();
        while (i < j) {
            if (s.charAt(i) != s.charAt(j)) return false;
            i++;
            j--;
        }
        return true;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        List<String> pals = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String w = sc.next();
            if (isPalindrome(w)) pals.add(w);
        }
        System.out.println("Palindromes: " + pals.size());
        for (String p : pals) System.out.println(p.toUpperCase);
    }
}
```

---

<a id="day30"></a>
## Day 30 — Mock Coding Assessment

**Learning objectives:** Complete a full beginner-to-intermediate mock assessment on your own. Suggested time: 90 minutes. Write your own complete program for each coding question, test every sample, and check the edge cases before moving on. This day reviews the whole month: input/output, conditions, loops, methods, strings, arrays, OOP, collections, exceptions, and recursion.

### Question 1: Mixed Real-World Scenario (Multi-Step)
**Title:** Exam Result Processor

**Problem statement:** A college stores the marks of `n` students in 3 subjects. For each student compute the total and the average. A student **passes** only if every subject mark is at least 35. The grade is based on the average: `A` for 85 or more, `B` for 70–84.99, `C` for 50–69.99, and `D` below 50. A student who does not pass receives grade `F` regardless of the average. Rank the students by total marks from highest to lowest; students with equal totals keep their input order. Print the ranked table and how many students passed. Design your program with at least one `Student` class and sensible methods.

**Real-world context:** Universities process result sheets by combining validation, grading, and ranking.

**Input requirements:** Line 1: `n`. Then `n` lines: `<name> <mark1> <mark2> <mark3>`.

**Expected output:** For each student in ranked order: `<rank>. <name> total=<total> avg=<average with 2 decimals> grade=<grade>`, then `Passed: <p>/<n>`.

**Sample cases:**
```
Input:
4
Asha 90 85 95
Ravi 40 30 80
Meena 70 75 65
Kiran 50 60 55
Output:
1. Asha total=270 avg=90.00 grade=A
2. Meena total=210 avg=70.00 grade=B
3. Kiran total=165 avg=55.00 grade=C
4. Ravi total=150 avg=50.00 grade=F
Passed: 3/4
```
```
Input:
2
Tom 35 35 35
Sam 35 35 35
Output:
1. Tom total=105 avg=35.00 grade=D
2. Sam total=105 avg=35.00 grade=D
Passed: 2/2
```

**Constraints:** 1 ≤ n ≤ 30; marks are between 0 and 100; names contain no spaces.

### Question 2: Interview-Based Coding
**Title:** Longest Common Prefix

**Problem statement:** Read `n` words and print the longest prefix that all of them share. If there is none, print `No common prefix`.

**Input and output requirements:** Input: line 1 `n`; line 2 `n` words. Output: the prefix or the message.

**Sample cases:**
```
Input:
3
flower flow flight
Output: fl
```
```
Input:
3
dog racecar car
Output: No common prefix
```
```
Input:
3
interview internet internal
Output: inter
```

**Constraints:** 1 ≤ n ≤ 50; words are lowercase letters, length 1–50. With a single word, the whole word is the prefix.

### Question 3: Company-Style Programming
**Title:** Library Stock Manager

**Problem statement:** Maintain a library's stock of book titles with a `HashMap`. Process `n` commands:
- `ADD <title> <qty>`: increase the stock of that title by `qty` (creating it if needed) and print `Added: <title> x<qty>`.
- `BORROW <title>`: if the title does not exist print `Unknown: <title>`; else if its stock is 0 print `Unavailable: <title>`; otherwise reduce the stock by 1 and print `Borrowed: <title>`.
- `RETURN <title>`: if the title does not exist print `Unknown: <title>`; otherwise increase the stock by 1 and print `Returned: <title>`.
- `REPORT`: print `<title>: <stock>` for every title in alphabetical order.

**Input and output requirements:** Line 1: `n`. Then `n` commands, one per line. Output: the messages described above.

**Sample cases:**
```
Input:
7
ADD java 2
BORROW java
BORROW java
BORROW java
RETURN java
ADD python 1
REPORT
Output:
Added: java x2
Borrowed: java
Borrowed: java
Unavailable: java
Returned: java
Added: python x1
java: 1
python: 1
```
```
Input:
3
BORROW c
ADD c 5
REPORT
Output:
Unknown: c
Added: c x5
c: 5
```

**Constraints and edge cases:** 1 ≤ n ≤ 50; titles are lowercase with no spaces; `qty` is a positive integer. Adding to an existing title increases its stock.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Main {
    static class Box {
        private int size;

        Box(int size) {
            this.size = size;
        }

        Box grow(int by) {
            return new Box(size + by);
        }

        int getSize() {
            return size;
        }
    }

    static int calc(int n) {
        if (n < 3) return n;
        return calc(n - 1) + calc(n - 2);
    }

    public static void main(String[] args) {
        Box b = new Box(2);
        Box c = b.grow(3).grow(4);
        System.out.println(b.getSize() + " " + c.getSize());
        System.out.println(calc(5));

        Set<Character> seen = new HashSet<>();
        StringBuilder sb = new StringBuilder();
        for (char ch : "mississippi".toCharArray()) {
            if (seen.add(ch)) sb.append(ch);
        }
        System.out.println(sb);

        int[] arr = {3, 1, 2};
        Arrays.sort(arr);
        System.out.println(Arrays.toString(arr) + Arrays.binarySearch(arr, 3));

        try {
            Object o = "text";
            Integer i = (Integer) o;
            System.out.println(i);
        } catch (ClassCastException e) {
            System.out.println("cast failed");
        }

        System.out.println("A" + 'B' + 1 + 2L + 3.0f);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Keep accounts in a map. Try to withdraw 200 from each of the accounts `A1`, `B2`, and `C3`. `Account.withdraw` must throw an `IllegalStateException` with the message `Insufficient funds` when the balance is too low. `A1` has 500, `B2` has 100, and `C3` does not exist. The expected output is:
```
A1 ok: 300.0
B2 failed: Insufficient funds
C3 not found
```

The program has a compile-time error and a runtime error. Identify each, explain why it occurs, and provide the corrected program.

```java
import java.util.HashMap;
import java.util.Map;

class Account {
    private String id;
    private double balance;

    Account(String id, double balance) {
        this.id = id;
        this.balance = balance;
    }

    String getId() {
        return id;
    }

    void withdraw(double amount) {
        if (amount > balance) {
            throw new Exception("Insufficient funds");
        }
        balance -= amount;
    }

    double getBalance() {
        return balance;
    }
}

public class Main {
    public static void main(String[] args) {
        Map<String, Account> accounts = new HashMap<>();
        accounts.put("A1", new Account("A1", 500));
        accounts.put("B2", new Account("B2", 100));
        for (String id : new String[]{"A1", "B2", "C3"}) {
            Account acc = accounts.get(id);
            try {
                acc.withdraw(200);
                System.out.println(id + " ok: " + acc.getBalance());
            } catch (IllegalStateException e) {
                System.out.println(id + " failed: " + e.getMessage());
            }
        }
    }
}
```

---

*End of workbook — 30 days, 150 questions.*
