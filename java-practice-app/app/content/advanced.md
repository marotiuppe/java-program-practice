# 30-Day Java Advanced Practice Workbook — Data Structures, Algorithms, and Modern Java

**Java version: 17** | Level: Advanced | 5 questions per day | 150 questions total | Questions only

## Table of Contents

**Week 1 — Data Structures: Lists, Stacks, Queues, Hashing, Heaps, Collections**
- [Day 1 — Linked Lists I: Reversal and Pointer Surgery](#day1)
- [Day 2 — Linked Lists II: Cycles, Merge Sort, and Reordering](#day2)
- [Day 3 — Stacks: Monotonic Stack and Expression Evaluation](#day3)
- [Day 4 — Queues and Deques](#day4)
- [Day 5 — Hash-Based Designs: LRU Cache and Frequency Maps](#day5)
- [Day 6 — Heaps and PriorityQueue](#day6)
- [Day 7 — Advanced Collections: Contracts, Fail-Fast Iterators, and Immutability](#day7)

**Week 2 — Trees and Graphs**
- [Day 8 — Binary Trees: Traversals and Construction](#day8)
- [Day 9 — Binary Trees: LCA, Height, Diameter, and Path Problems](#day9)
- [Day 10 — Binary Search Trees](#day10)
- [Day 11 — Tries and Prefix Structures](#day11)
- [Day 12 — Graphs I: Representation, BFS, and DFS](#day12)
- [Day 13 — Graphs II: Topological Sort and Cycle Detection](#day13)
- [Day 14 — Shortest Paths and Union-Find](#day14)

**Week 3 — Algorithmic Techniques**
- [Day 15 — Dynamic Programming I: One-Dimensional](#day15)
- [Day 16 — Dynamic Programming II: Grids and Sequences](#day16)
- [Day 17 — Dynamic Programming III: Knapsack and Edit Distance](#day17)
- [Day 18 — Backtracking](#day18)
- [Day 19 — Greedy Algorithms and Intervals](#day19)
- [Day 20 — Binary Search on Answer and Bit Manipulation](#day20)
- [Day 21 — Advanced Strings: KMP, Rolling Hash, and Windows](#day21)

**Week 4 — Modern Java and Engineering**
- [Day 22 — Generics and Bounded Types](#day22)
- [Day 23 — Streams and Collectors in Depth](#day23)
- [Day 24 — Optional, Records, Sealed Classes, and Pattern Matching](#day24)
- [Day 25 — Concurrency Essentials](#day25)
- [Day 26 — Memory, JVM Behavior, and the String Pool](#day26)
- [Day 27 — Design Patterns](#day27)
- [Day 28 — Low-Level Design Mini-Systems and Edge-Case Testing](#day28)

**Consolidation and Challenge**
- [Day 29 — Mixed Hard Revision](#day29)
- [Day 30 — Full Mock Assessment](#day30)

**Conventions used in this workbook:** programs read from standard input (`Scanner` or `BufferedReader`) and write to standard output unless a question says otherwise. All code assumes Java 17 and a class named `Main`. Where input sizes are stated, assume a time limit of about 1-2 seconds, so the constraints are chosen to separate efficient solutions from naive ones. Unless stated otherwise, all indices are 0-based and "O(1) extra space" means no auxiliary structure that grows with the input.

---

<a id="day1"></a>
## Day 1 — Linked Lists I: Reversal and Pointer Surgery

**Learning objectives:** Manipulate singly linked list nodes directly: reverse whole lists and sub-ranges, remove nodes with a single traversal, add numbers stored as lists, and reason about pointer state through recursion.

### Question 1: Mixed Real-World Scenario
**Title:** Conveyor Belt Section Flip

**Problem statement:** A sorting facility models a conveyor belt as a singly linked list of package IDs. To fix a jam, the operators physically reverse the section of the belt from position `L` to position `R` (1-indexed, inclusive), leaving the rest of the belt untouched. Re-link the existing nodes to perform the reversal; do not build a new list or copy the values into an array.

**Real-world context:** Belts and chains in warehouses are loops of physical segments; a reversal of one section must re-use the same physical segments rather than replace them.

**Input requirements:** Line 1: `n`. Line 2: `n` package IDs. Line 3: `L R`.

**Expected output:** One line with the IDs after the reversal, separated by single spaces.

**Sample cases:**
```
Input:
6
1 2 3 4 5 6
2 5
Output:
1 5 4 3 2 6
```
```
Input:
3
7 8 9
1 3
Output:
9 8 7
```
```
Input:
4
4 4 5 5
2 2
Output:
4 4 5 5
```

**Constraints and edge cases:** 1 ≤ n ≤ 100000; 1 ≤ L ≤ R ≤ n; IDs fit in `int`. The case `L == R` must leave the list unchanged. The reversal may start at the head. The solution must use O(1) extra space and a single pass over the list.

### Question 2: Interview-Based Coding
**Title:** Remove the N-th Node From the End

**Problem statement:** Given a singly linked list and an integer `k`, remove the `k`-th node counting from the end of the list (the last node is the 1st from the end) and print the resulting list. Your algorithm must traverse the list only once (you may not first compute the length).

**Input and output requirements:** Line 1: `n`. Line 2: `n` integers. Line 3: `k`. Output the remaining values separated by spaces, or the word `EMPTY` if no nodes remain.

**Sample cases:**
```
Input:
5
1 2 3 4 5
2
Output:
1 2 3 5
```
```
Input:
1
10
1
Output:
EMPTY
```
```
Input:
4
6 7 8 9
4
Output:
7 8 9
```

**Constraints and edge cases:** 1 ≤ n ≤ 100000; 1 ≤ k ≤ n. Removing the head and removing the only node must both work.

### Question 3: Company-Style Programming
**Title:** Add Two Numbers Without Reversing

**Problem statement:** Two non-negative integers are stored as singly linked lists of decimal digits, with the **most significant digit first**. Compute their sum and print it as a list of digits (most significant first). You may not convert the lists to `int`, `long`, or `BigInteger`, and you may not modify or reverse the input lists.

**Input and output requirements:** Line 1: `a` followed by `a` digits (the first number). Line 2: `b` followed by `b` digits (the second number). Output the digits of the sum separated by spaces.

**Sample cases:**
```
Input:
4 7 2 4 3
3 5 6 4
Output:
7 8 0 7
```
```
Input:
2 9 9
1 1
Output:
1 0 0
```
```
Input:
1 0
1 0
Output:
0
```

**Constraints and edge cases:** 1 ≤ a, b ≤ 100000; each list has no leading zeros unless the number is exactly `0`. The sum must not contain leading zeros (except the number `0` itself). A final carry must create a new leading digit.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
public class Main {
    static class Node {
        int val;
        Node next;

        Node(int val, Node next) {
            this.val = val;
            this.next = next;
        }
    }

    static Node build(int... a) {
        Node head = null;
        for (int i = a.length - 1; i >= 0; i--) {
            head = new Node(a[i], head);
        }
        return head;
    }

    static Node twist(Node head) {
        if (head == null || head.next == null) {
            return head;
        }
        Node second = head.next;
        head.next = twist(second.next);
        second.next = head;
        return second;
    }

    static void print(Node n) {
        StringBuilder sb = new StringBuilder();
        for (; n != null; n = n.next) {
            sb.append(n.val);
            if (n.next != null) {
                sb.append("->");
            }
        }
        System.out.println(sb);
    }

    public static void main(String[] args) {
        Node h = build(1, 2, 3, 4, 5);
        Node t = twist(h);
        print(t);
        print(h);
        Node mid = t.next.next;
        mid.next = null;
        print(t);
        print(h);
        print(twist(build(7)));
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Given a **sorted** singly linked list, delete every node whose value appears more than once (not just the extra copies), and return the head of the remaining list. For `1 2 2 3 3 4` the result is `1 4`; for `1 1 1` the result is empty (`null`); for `2 3 3` the result is `2`.

The program below has several bugs (including one that throws an exception on valid input). Identify every bug, explain why it occurs, and write the corrected method.

```java
public class Main {
    static class Node {
        int val;
        Node next;

        Node(int val, Node next) {
            this.val = val;
            this.next = next;
        }
    }

    static Node deleteDuplicates(Node head) {
        Node cur = head;
        while (cur != null) {
            if (cur.next != null && cur.val == cur.next.val) {
                int dup = cur.val;
                while (cur.next.val == dup) {
                    cur.next = cur.next.next;
                }
            }
            cur = cur.next;
        }
        return head;
    }

    public static void main(String[] args) {
        Node list = new Node(1, new Node(2, new Node(2, new Node(3, new Node(3, new Node(4, null))))));
        Node r = deleteDuplicates(list);
        StringBuilder sb = new StringBuilder();
        for (; r != null; r = r.next) {
            sb.append(r.val).append(' ');
        }
        System.out.println(sb.toString().trim());
    }
}
```

---

<a id="day2"></a>
## Day 2 — Linked Lists II: Cycles, Merge Sort, and Reordering

**Learning objectives:** Use fast and slow pointers to detect cycles and find their entry point, split lists at the middle, sort a linked list by merging, reorder lists in place, and reverse groups of nodes.

### Question 1: Mixed Real-World Scenario
**Title:** Delivery Route Loop Inspector

**Problem statement:** A courier app stores a route as a singly linked list of stop IDs. A data-entry bug sometimes makes the last stop point back to an earlier stop, creating an endless route. Given the stop IDs and the index `pos` of the stop that the last stop points back to (`-1` if it points to nothing), your program must discover the loop by walking the nodes only (do not use `pos` to answer the question directly; build the list, link the tail as described, and then inspect the structure). If a loop exists, report the ID of the first stop that is part of the loop and the number of stops in the loop. Stop IDs may repeat, so do not identify nodes by their ID.

**Real-world context:** Routing and workflow engines must detect configuration loops before running them forever.

**Input requirements:** Line 1: `n`. Line 2: `n` stop IDs. Line 3: `pos`.

**Expected output:** `No cycle` or `Cycle starts at stop <id>, length <len>`.

**Sample cases:**
```
Input:
6
3 8 5 9 4 7
2
Output:
Cycle starts at stop 5, length 4
```
```
Input:
3
1 2 3
-1
Output:
No cycle
```
```
Input:
4
2 2 2 2
0
Output:
Cycle starts at stop 2, length 4
```

**Constraints and edge cases:** 1 ≤ n ≤ 100000; -1 ≤ pos < n. A single node pointing to itself is a valid cycle of length 1. Use O(1) extra space for the detection.

### Question 2: Interview-Based Coding
**Title:** Sort a Linked List

**Problem statement:** Sort a singly linked list of integers in ascending order in O(n log n) time. Do not copy the values into an array or other collection; sort by re-linking nodes. Recursion depth must remain O(log n).

**Input and output requirements:** Line 1: `n`. Line 2: `n` integers. Output the sorted values separated by spaces.

**Sample cases:**
```
Input:
6
4 2 9 1 3 3
Output:
1 2 3 3 4 9
```
```
Input:
1
5
Output:
5
```
```
Input:
5
-1 5 3 4 0
Output:
-1 0 3 4 5
```

**Constraints and edge cases:** 1 ≤ n ≤ 200000; values fit in `int` (differences between two values may overflow `int`). Lists with duplicates and already-sorted or reverse-sorted lists must run within the limit.

### Question 3: Company-Style Programming
**Title:** Interleave From Both Ends

**Problem statement:** Reorder a linked list `L0 → L1 → ... → Ln-1` into `L0 → Ln-1 → L1 → Ln-2 → L2 → ...` in place. You may not alter node values; only change links. Use O(1) extra space.

**Input and output requirements:** Line 1: `n`. Line 2: `n` integers. Output the reordered values separated by spaces.

**Sample cases:**
```
Input:
4
1 2 3 4
Output:
1 4 2 3
```
```
Input:
5
1 2 3 4 5
Output:
1 5 2 4 3
```
```
Input:
1
9
Output:
9
```

**Constraints and edge cases:** 1 ≤ n ≤ 100000. Handle odd and even lengths; the middle element of an odd-length list ends up last.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
public class Main {
    static class Node {
        int val;
        Node next;

        Node(int val) {
            this.val = val;
        }
    }

    public static void main(String[] args) {
        Node[] nodes = new Node[8];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new Node(i * 10);
            if (i > 0) {
                nodes[i - 1].next = nodes[i];
            }
        }
        nodes[7].next = nodes[2];

        Node slow = nodes[0];
        Node fast = nodes[0];
        int steps = 0;
        do {
            slow = slow.next;
            fast = fast.next.next;
            steps++;
            System.out.println(steps + ": " + slow.val + " " + fast.val);
        } while (slow != fast);

        slow = nodes[0];
        int walked = 0;
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
            walked++;
        }
        System.out.println("entry=" + slow.val + " walked=" + walked);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Reverse the nodes of a linked list in groups of `k`. If the number of remaining nodes at the end is fewer than `k`, leave them as they are. For `1 2 3 4 5` with `k = 2` the result is `2 1 4 3 5`; for `k = 3` the result is `3 2 1 4 5`; for `k = 1` the list is unchanged.

The program below produces wrong results or loses nodes. Find every bug, explain what goes wrong, and correct the method.

```java
public class Main {
    static class Node {
        int val;
        Node next;

        Node(int val, Node next) {
            this.val = val;
            this.next = next;
        }
    }

    static Node reverseKGroup(Node head, int k) {
        Node dummy = new Node(0, head);
        Node groupPrev = dummy;
        while (true) {
            Node kth = groupPrev;
            for (int i = 0; i <= k && kth != null; i++) {
                kth = kth.next;
            }
            if (kth == null) {
                break;
            }
            Node groupNext = kth.next;
            Node prev = null;
            Node cur = groupPrev.next;
            while (cur != groupNext) {
                Node nx = cur.next;
                cur.next = prev;
                prev = cur;
                cur = nx;
            }
            Node tmp = groupPrev.next;
            groupPrev.next = kth;
            groupPrev = groupPrev.next;
        }
        return dummy.next;
    }

    public static void main(String[] args) {
        Node list = null;
        for (int v = 5; v >= 1; v--) {
            list = new Node(v, list);
        }
        Node r = reverseKGroup(list, 2);
        StringBuilder sb = new StringBuilder();
        for (; r != null; r = r.next) {
            sb.append(r.val).append(' ');
        }
        System.out.println(sb.toString().trim());
    }
}
```

---

<a id="day3"></a>
## Day 3 — Stacks: Monotonic Stack and Expression Evaluation

**Learning objectives:** Use a stack to maintain an order invariant (monotonic stack), compute "next greater" style answers in linear time, compute maximal rectangles, parse and evaluate expressions with precedence and parentheses, and know how `Stack` and `Deque` differ in iteration order.

### Question 1: Mixed Real-World Scenario
**Title:** Warehouse Shelf Lookout

**Problem statement:** A warehouse has `n` shelves in a row, each with a height. For every shelf, a worker standing on it looks to the right and to the left and wants to know how many positions away the nearest **strictly taller** shelf is. Print two lines: first the distances to the right, then the distances to the left. If there is no taller shelf in that direction, print `0` for that shelf.

**Real-world context:** Placing signage or sensors so that each shelf can be seen from the nearest taller structure.

**Input requirements:** Line 1: `n`. Line 2: `n` heights.

**Expected output:** Line 1: `n` right-distances. Line 2: `n` left-distances. Values are separated by spaces.

**Sample cases:**
```
Input:
5
4 2 5 3 6
Output:
2 1 2 1 0
0 1 0 1 0
```
```
Input:
3
3 3 3
Output:
0 0 0
0 0 0
```
```
Input:
6
1 2 3 4 3 2
Output:
1 1 1 0 0 0
0 0 0 0 1 1
```

**Constraints and edge cases:** 1 ≤ n ≤ 200000; heights are integers between 1 and 10^9. Equal heights are not taller. A solution that scans outward from every shelf will exceed the time limit on sorted input.

### Question 2: Interview-Based Coding
**Title:** Largest Rectangle in a Histogram

**Problem statement:** Given the heights of `n` histogram bars, each of width 1 and adjacent to one another, find the area of the largest rectangle that fits entirely inside the histogram.

**Input and output requirements:** Line 1: `n`. Line 2: `n` heights. Output a single integer, the maximum area.

**Sample cases:**
```
Input:
6
2 1 5 6 2 3
Output:
10
```
```
Input:
2
2 4
Output:
4
```
```
Input:
3
1000000000 1000000000 1000000000
Output:
3000000000
```

**Constraints and edge cases:** 1 ≤ n ≤ 200000; 0 ≤ height ≤ 10^9. The answer may exceed the `int` range. Bars of height 0 split the histogram. Target O(n).

### Question 3: Company-Style Programming
**Title:** Calculator With Parentheses

**Problem statement:** Evaluate an arithmetic expression containing non-negative integers, the binary operators `+ - * /`, parentheses, spaces, and a unary minus (a `-` that appears at the start of the expression or right after `(` or another operator). Multiplication and division bind tighter than addition and subtraction and are left-associative. Division truncates toward zero. A unary minus applies to the number or parenthesized group immediately after it. You may not use `ScriptEngine` or any library expression evaluator.

**Input and output requirements:** One line containing the expression. Output the integer value.

**Sample cases:**
```
Input:
2*(5+5*2)/3+(6/2+8)
Output:
21
```
```
Input:
-7/2+1
Output:
-2
```
```
Input:
 10 - ( 2 + 3 ) * -4 / 3
Output:
16
```

**Constraints and edge cases:** Expression length ≤ 100000; every intermediate value fits in a 32-bit signed integer; no division by zero. Whitespace may appear anywhere between tokens. Parentheses may nest deeply (up to 10000 levels), so consider the call stack.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        Stack<Integer> legacy = new Stack<>();
        Deque<Integer> modern = new ArrayDeque<>();
        for (int i = 1; i <= 4; i++) {
            legacy.push(i);
            modern.push(i);
        }
        System.out.println(legacy);
        System.out.println(modern);
        System.out.println(legacy.peek() + " " + modern.peek());

        StringBuilder a = new StringBuilder();
        for (int x : legacy) {
            a.append(x);
        }
        StringBuilder b = new StringBuilder();
        for (int x : modern) {
            b.append(x);
        }
        System.out.println(a + " " + b);

        legacy.pop();
        modern.pop();
        System.out.println(legacy.get(0) + " " + modern.peekLast());
        System.out.println(legacy.search(1) + " " + legacy.search(3) + " " + legacy.search(9));

        Deque<Integer> empty = new ArrayDeque<>();
        System.out.println(empty.peek() + " " + empty.poll());
        try {
            empty.pop();
        } catch (RuntimeException e) {
            System.out.println(e.getClass().getSimpleName());
        }
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Decode an encoded string where `k[encoded]` means `encoded` is repeated exactly `k` times; `k` can have several digits and encodings can nest. For `3[a2[c]]` the result is `accaccacc`; for `2[ab]3[x]` the result is `ababxxx`; for `10[z]` the result is ten `z` characters; `ab2[c]` gives `abcc`.

The code below gives wrong results on valid inputs. Identify every bug, explain the effect of each one, and write the corrected method.

```java
import java.util.ArrayDeque;
import java.util.Deque;

public class Main {
    static String decode(String s) {
        Deque<Integer> counts = new ArrayDeque<>();
        Deque<StringBuilder> stack = new ArrayDeque<>();
        StringBuilder cur = new StringBuilder();
        int num = 0;
        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) {
                num = c - '0';
            } else if (c == '[') {
                counts.push(num);
                stack.push(cur);
                cur = new StringBuilder();
            } else if (c == ']') {
                StringBuilder prev = stack.pop();
                int times = counts.pop();
                for (int i = 1; i < times; i++) {
                    prev.append(cur);
                }
                cur = prev;
            } else {
                cur.append(c);
            }
        }
        return cur.toString();
    }

    public static void main(String[] args) {
        System.out.println(decode("3[a2[c]]"));
        System.out.println(decode("2[ab]3[x]"));
        System.out.println(decode("10[z]"));
        System.out.println(decode("ab2[c]"));
    }
}
```

---

<a id="day4"></a>
## Day 4 — Queues and Deques

**Learning objectives:** Use queues and deques for window problems, maintain a monotonic deque, handle prefix sums with negatives using a deque, build structures from queues and stacks, and know the `Queue`/`Deque` method families (and `remove(int)` versus `remove(Object)`).

### Question 1: Mixed Real-World Scenario
**Title:** Sliding-Window API Rate Limiter

**Problem statement:** An API gateway allows at most `L` accepted requests in any sliding window of `W` seconds. A request arriving at time `t` is **accepted** if fewer than `L` previously accepted requests have timestamps in the interval `(t - W, t]`; otherwise it is **rejected**. Rejected requests do not count toward later windows. Process the request timestamps in order and report the verdict for each.

**Real-world context:** Public APIs throttle clients using sliding-window counters to protect backend capacity.

**Input requirements:** Line 1: `W L`. Line 2: `m`, the number of requests. Line 3: `m` timestamps in non-decreasing order.

**Expected output:** One line of `m` characters separated by spaces, using `A` for accepted and `R` for rejected.

**Sample cases:**
```
Input:
10 3
8
1 2 3 4 11 12 13 14
Output:
A A A R A A A R
```
```
Input:
5 1
4
1 1 6 7
Output:
A R A R
```
```
Input:
3 2
5
1 2 3 4 5
Output:
A A R A A
```

**Constraints and edge cases:** 1 ≤ W ≤ 10^9; 1 ≤ L ≤ 100000; 1 ≤ m ≤ 200000; 0 ≤ timestamps ≤ 10^9. Several requests may share a timestamp. Memory must not grow with the number of rejected requests.

### Question 2: Interview-Based Coding
**Title:** Sliding Window Maximum

**Problem statement:** Given an array of integers and a window size `k`, output the maximum of every contiguous window of size `k`, from left to right.

**Input and output requirements:** Line 1: `n k`. Line 2: `n` integers. Output `n - k + 1` values separated by spaces.

**Sample cases:**
```
Input:
8 3
1 3 -1 -3 5 3 6 7
Output:
3 3 5 5 6 7
```
```
Input:
3 1
9 8 7
Output:
9 8 7
```
```
Input:
4 4
-5 -2 -9 -2
Output:
-2
```

**Constraints and edge cases:** 1 ≤ k ≤ n ≤ 500000; values fit in `int`. A solution that rescans every window is too slow. Windows containing equal maxima must be handled correctly.

### Question 3: Company-Style Programming
**Title:** Shortest Window With a Target Sum

**Problem statement:** Given an array that may contain **negative** numbers and an integer `K`, find the length of the shortest non-empty contiguous subarray whose sum is at least `K`. Print `-1` if no such subarray exists.

**Input and output requirements:** Line 1: `n K`. Line 2: `n` integers. Output a single integer.

**Sample cases:**
```
Input:
3 3
2 -1 2
Output:
3
```
```
Input:
2 4
1 2
Output:
-1
```
```
Input:
5 167
84 -37 32 40 95
Output:
3
```
```
Input:
1 1
1
Output:
1
```

**Constraints and edge cases:** 1 ≤ n ≤ 100000; |a[i]| ≤ 10000; 1 ≤ K ≤ 10^9. Prefix sums can exceed the `int` range for other inputs of this size, so choose your types carefully. A standard two-pointer window does not work because of the negative values.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Main {
    public static void main(String[] args) {
        Deque<Integer> d = new ArrayDeque<>();
        d.addFirst(1);
        d.offerLast(2);
        d.push(3);
        d.addLast(4);
        d.offerFirst(5);
        System.out.println(d);
        System.out.println(d.pollFirst() + " " + d.removeLast() + " " + d.peekFirst() + " " + d.peekLast());
        d.add(1);
        d.add(1);
        d.remove(1);
        System.out.println(d);
        d.removeLastOccurrence(1);
        System.out.println(d);

        List<Integer> list = new LinkedList<>(Arrays.asList(10, 1, 20, 1));
        list.remove(1);
        System.out.println(list);
        list.remove(Integer.valueOf(1));
        System.out.println(list);

        Queue<String> q = new LinkedList<>();
        System.out.println(q.peek() + " " + q.poll() + " " + q.offer("x") + " " + q.size());
        try {
            new LinkedList<String>().remove();
        } catch (RuntimeException e) {
            System.out.println(e.getClass().getSimpleName());
        }
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Implement a FIFO queue using two stacks. `push(x)` adds to the back, `pop()` removes and returns the front element, `peek()` returns the front without removing it, and `empty()` reports whether the queue holds no elements. For the operations `push 1, push 2, peek, pop, push 3, pop, pop, empty` the results must be `1, 1, 2, 3, true` (for `peek`, `pop`, `pop`, `pop`, `empty`). All operations should run in amortized O(1).

The class below returns elements in the wrong order for some operation sequences and gives wrong answers for `empty()` and `peek()`. Find every bug, explain it, and rewrite the class correctly.

```java
import java.util.ArrayDeque;
import java.util.Deque;

public class Main {
    static class MyQueue {
        private final Deque<Integer> in = new ArrayDeque<>();
        private final Deque<Integer> out = new ArrayDeque<>();

        void push(int x) {
            in.push(x);
        }

        int pop() {
            while (!in.isEmpty()) {
                out.push(in.pop());
            }
            return out.pop();
        }

        int peek() {
            if (out.isEmpty()) {
                return in.peek();
            }
            return out.peek();
        }

        boolean empty() {
            return in.isEmpty();
        }
    }

    public static void main(String[] args) {
        MyQueue q = new MyQueue();
        q.push(1);
        q.push(2);
        System.out.println(q.peek());
        System.out.println(q.pop());
        q.push(3);
        System.out.println(q.pop());
        System.out.println(q.pop());
        System.out.println(q.empty());
    }
}
```

---

<a id="day5"></a>
## Day 5 — Hash-Based Designs: LRU Cache and Frequency Maps

**Learning objectives:** Combine a hash map with a hand-written doubly linked list for O(1) caches, use prefix-sum maps to count subarrays, design structures with bucketed frequency stacks, understand `LinkedHashMap` access order, and avoid classic last-index bugs.

### Question 1: Mixed Real-World Scenario
**Title:** CDN Edge Cache (LRU)

**Problem statement:** An edge server caches web objects, identified by integer keys, with a fixed capacity `C`. When the cache is full and a new key is inserted, the **least recently used** key is evicted. Both `GET` and `PUT` count as a use. Implement the cache with O(1) operations. You must build the recency tracking yourself; do not use `LinkedHashMap`.

**Real-world context:** CDNs and browsers keep recently requested resources and evict the stalest ones to stay within memory limits.

**Input requirements:** Line 1: `C` and `q`. Then `q` commands: `PUT key value` or `GET key`.

**Expected output:** For every `GET`, print the stored value or `-1` if the key is absent.

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
PUT 5 10
PUT 5 20
GET 5
PUT 6 1
GET 5
Output:
20
-1
```

**Constraints and edge cases:** 1 ≤ C ≤ 100000; 1 ≤ q ≤ 300000; keys and values fit in `int`. Updating an existing key with `PUT` replaces the value, refreshes recency, and never evicts anything. A `GET` miss does not change the cache.

### Question 2: Interview-Based Coding
**Title:** Count Subarrays With Sum K

**Problem statement:** Given an array of integers (which may be negative or zero) and a target `K`, count the contiguous subarrays whose sum equals `K`.

**Input and output requirements:** Line 1: `n K`. Line 2: `n` integers. Output the count.

**Sample cases:**
```
Input:
3 2
1 1 1
Output:
2
```
```
Input:
3 0
1 -1 0
Output:
3
```
```
Input:
4 0
0 0 0 0
Output:
10
```

**Constraints and edge cases:** 1 ≤ n ≤ 200000; |a[i]| ≤ 10^4; |K| ≤ 10^9. The count may exceed the `int` range for other inputs of this size. A sliding window fails because of the negative values.

### Question 3: Company-Style Programming
**Title:** Most Frequent Stack

**Problem statement:** Design a stack-like structure with two operations: `PUSH x` adds `x`, and `POP` removes and returns the element that is currently **most frequent**. If several values are tied for the highest frequency, remove the one whose most recent push is the latest. Both operations must run in O(1) average time.

**Input and output requirements:** Line 1: `q`. Then `q` commands. For each `POP`, print the removed value. `POP` is never issued on an empty structure.

**Sample cases:**
```
Input:
10
PUSH 5
PUSH 7
PUSH 5
PUSH 7
PUSH 4
PUSH 5
POP
POP
POP
POP
Output:
5
7
5
4
```
```
Input:
4
PUSH 1
PUSH 1
POP
POP
Output:
1
1
```

**Constraints and edge cases:** 1 ≤ q ≤ 300000; values fit in `int`. Frequency means the number of copies currently stored, so it decreases after a pop.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        Map<String, Integer> lru = new LinkedHashMap<String, Integer>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Integer> eldest) {
                return size() > 3;
            }
        };
        lru.put("a", 1);
        lru.put("b", 2);
        lru.put("c", 3);
        lru.get("a");
        System.out.println(lru.keySet());
        lru.put("d", 4);
        System.out.println(lru.keySet());
        lru.containsKey("c");
        lru.getOrDefault("d", 0);
        lru.putIfAbsent("a", 9);
        System.out.println(lru);
        lru.put("e", 5);
        System.out.println(lru);

        Map<String, Integer> counts = new TreeMap<>();
        for (String w : "to be or not to be to".split(" ")) {
            counts.merge(w, 1, Integer::sum);
        }
        System.out.println(counts);
        System.out.println(counts.merge("or", 5, (x, y) -> null));
        System.out.println(counts.put("zed", 1) + " " + counts.put("zed", 2));
        System.out.println(counts.computeIfAbsent("be", k -> 100) + " " + counts.computeIfPresent("nope", (k, v) -> v + 1));
        System.out.println(counts);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Return the length of the longest substring of a string that contains no repeated characters. For `abcabcbb` the answer is `3`; for `pwwkew` it is `3`; for `abba` it is `2`; for the empty string it is `0`.

The code below returns wrong answers for some inputs. Find the bug(s), explain exactly which inputs fail and why, and correct the method.

```java
import java.util.HashMap;
import java.util.Map;

public class Main {
    static int longest(String s) {
        Map<Character, Integer> last = new HashMap<>();
        int left = 0;
        int best = 0;
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            if (last.containsKey(c)) {
                left = last.get(c) + 1;
            }
            last.put(c, right);
            best = Math.max(best, right - left);
        }
        return best;
    }

    public static void main(String[] args) {
        System.out.println(longest("abcabcbb"));
        System.out.println(longest("pwwkew"));
        System.out.println(longest("abba"));
        System.out.println(longest(""));
    }
}
```

---

<a id="day6"></a>
## Day 6 — Heaps and PriorityQueue

**Learning objectives:** Use `PriorityQueue` with custom comparators, apply lazy deletion for priority updates, select top-K in O(n log k), maintain a running median with two heaps, merge sorted sources, and avoid comparator overflow bugs.

### Question 1: Mixed Real-World Scenario
**Title:** Emergency Room Triage Queue

**Problem statement:** An emergency room treats the patient with the highest severity first. Among patients with equal severity, the one who **arrived earlier** is treated first. A patient's condition may worsen while waiting. Process these commands:
- `ARRIVE name severity`: a new patient joins the queue.
- `UPGRADE name severity`: if the patient is currently waiting and the new severity is **greater** than the current one, set it to the new value (the original arrival order does not change). Otherwise ignore the command.
- `TREAT`: remove the next patient and print the name, or print `NONE` if nobody is waiting.

**Real-world context:** Triage systems re-prioritize patients as vitals change, while keeping fairness among equally urgent cases.

**Input requirements:** Line 1: `q`. Then `q` commands, one per line. Names are unique across the whole input and contain no spaces.

**Expected output:** One line for every `TREAT` command.

**Sample cases:**
```
Input:
8
ARRIVE amy 5
ARRIVE bob 7
ARRIVE cat 7
UPGRADE amy 9
TREAT
TREAT
TREAT
TREAT
Output:
amy
bob
cat
NONE
```
```
Input:
7
ARRIVE x 3
ARRIVE y 2
UPGRADE y 3
TREAT
UPGRADE x 10
TREAT
TREAT
Output:
x
y
NONE
```

**Constraints and edge cases:** 1 ≤ q ≤ 200000; 1 ≤ severity ≤ 10^9. `UPGRADE` of a name that was already treated, or never arrived, is ignored. Scanning or rebuilding the whole queue on every `UPGRADE` is too slow at this size.

### Question 2: Interview-Based Coding
**Title:** Top K Frequent Words

**Problem statement:** Given `n` words and an integer `k`, print the `k` most frequent words. Sort the result by frequency from highest to lowest; words with the same frequency are sorted alphabetically (ascending, by `String.compareTo`).

**Input and output requirements:** Line 1: `n k`. Line 2: `n` space-separated lowercase words. Output the `k` words on one line separated by spaces.

**Sample cases:**
```
Input:
6 2
i love leetcode i love coding
Output:
i love
```
```
Input:
10 4
the day is sunny the the the sunny is is
Output:
the is sunny day
```
```
Input:
3 3
b a c
Output:
a b c
```

**Constraints and edge cases:** 1 ≤ n ≤ 200000; 1 ≤ k ≤ number of distinct words; words have length 1-20. The required running time is O(n log k) rather than sorting all distinct words.

### Question 3: Company-Style Programming
**Title:** Running Median Monitor

**Problem statement:** A monitoring service receives integers one at a time and must be able to report the median of everything received so far. For an odd count, the median is the middle value; for an even count, it is the average of the two middle values. Process commands `ADD x` and `MEDIAN`. Both must run in O(log n) or better.

**Input and output requirements:** Line 1: `q`. Then `q` commands. For each `MEDIAN`, print the median with exactly one decimal digit (the median is always a multiple of 0.5). `MEDIAN` is never issued before the first `ADD`.

**Sample cases:**
```
Input:
7
ADD 5
ADD 15
MEDIAN
ADD 1
MEDIAN
ADD 3
MEDIAN
Output:
10.0
5.0
4.0
```
```
Input:
5
ADD -1
MEDIAN
ADD -2
MEDIAN
MEDIAN
Output:
-1.0
-1.5
-1.5
```

**Constraints and edge cases:** 1 ≤ q ≤ 300000; |x| ≤ 2·10^9, so the sum of two values does not fit in an `int`. Repeated `MEDIAN` calls must not change the state. Print `-0.5`-style values correctly for negative averages.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Main {
    public static void main(String[] args) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int x : new int[]{5, 3, 8, 1, 9, 2}) {
            pq.add(x);
        }
        System.out.println(pq);
        System.out.println(pq.poll());
        System.out.println(pq);
        pq.remove(8);
        System.out.println(pq + " " + pq.size());

        PriorityQueue<Integer> max = new PriorityQueue<>(Collections.reverseOrder());
        max.addAll(Arrays.asList(5, 3, 8, 1));
        System.out.println(max.peek() + " " + max.size());

        PriorityQueue<String> words = new PriorityQueue<>(
                Comparator.comparing(String::length).thenComparing(Comparator.reverseOrder()));
        words.addAll(Arrays.asList("pear", "fig", "apple", "kiwi", "date", "plum"));
        StringBuilder sb = new StringBuilder();
        while (!words.isEmpty()) {
            sb.append(words.poll()).append(' ');
        }
        System.out.println(sb.toString().trim());

        PriorityQueue<int[]> pairs = new PriorityQueue<>((a, b) -> a[0] != b[0] ? Integer.compare(b[0], a[0]) : Integer.compare(a[1], b[1]));
        pairs.add(new int[]{2, 9});
        pairs.add(new int[]{7, 4});
        pairs.add(new int[]{7, 1});
        pairs.add(new int[]{2, 3});
        int[] top = pairs.poll();
        System.out.println(top[0] + "," + top[1] + " " + pairs.peek()[0] + "," + pairs.peek()[1]);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Merge `k` sorted integer arrays (some of which may be empty) into one sorted list using a `PriorityQueue`, in O(N log k) time, where `N` is the total number of elements. Values can be anywhere in the `int` range. For `{1,4,7}`, `{2,5}`, `{}` the result is `[1, 2, 4, 5, 7]`; for `{Integer.MIN_VALUE, 5}`, `{Integer.MAX_VALUE}` the result is `[-2147483648, 5, 2147483647]`.

The code below crashes on some inputs and returns wrong results on others. Identify every bug, explain the cause, and correct the program.

```java
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Main {
    static List<Integer> merge(int[][] lists) {
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        for (int i = 0; i < lists.length; i++) {
            pq.add(new int[]{lists[i][0], i, 0});
        }
        List<Integer> out = new ArrayList<>();
        while (!pq.isEmpty()) {
            int[] top = pq.poll();
            out.add(top[0]);
            int next = top[2] + 1;
            if (next < lists[top[1]].length - 1) {
                pq.add(new int[]{lists[top[1]][next], top[1], next});
            }
        }
        return out;
    }

    public static void main(String[] args) {
        try {
            System.out.println(merge(new int[][]{{1, 4, 7}, {2, 5}, {}}));
        } catch (RuntimeException e) {
            System.out.println("crashed: " + e.getClass().getSimpleName());
        }
        System.out.println(merge(new int[][]{{Integer.MIN_VALUE, 5}, {Integer.MAX_VALUE}}));
        System.out.println(merge(new int[][]{{3, 6, 9}, {1, 2}}));
    }
}
```

---

<a id="day7"></a>
## Day 7 — Advanced Collections: Contracts, Fail-Fast Iterators, and Immutability

**Learning objectives:** Build collection-like structures by hand (hash table, LFU cache, versioned store), understand the `equals`/`hashCode` contract and what happens to mutable keys, recognize `ConcurrentModificationException` and views versus copies, and design for immutability.

### Question 1: Mixed Real-World Scenario
**Title:** Versioned Stock Levels

**Problem statement:** A warehouse system tracks stock levels per item and supports snapshots for audits. Process these commands:
- `SET item qty`: set the current quantity of `item` to `qty`.
- `SNAPSHOT`: freeze the current state and print its snapshot id. Ids start at `0` and increase by 1.
- `GET id item`: print the quantity `item` had when snapshot `id` was taken (an item that was never set at that time has quantity `0`).

Snapshots must be independent of later `SET` commands.

**Real-world context:** Auditors need to ask "what was in stock at the end of last month?" without the system copying the entire inventory every night.

**Input requirements:** Line 1: `q`. Then `q` commands. Item names contain no spaces. Every `GET` refers to a snapshot that has already been taken.

**Expected output:** One line for every `SNAPSHOT` (its id) and every `GET` (the quantity).

**Sample cases:**
```
Input:
8
SET apple 5
SNAPSHOT
SET apple 8
SNAPSHOT
GET 0 apple
GET 1 apple
GET 1 pear
SET pear 3
Output:
0
1
5
8
0
```
```
Input:
7
SNAPSHOT
SET a 1
SET a 2
SNAPSHOT
GET 0 a
GET 1 a
GET 1 b
Output:
0
1
0
2
0
```

**Constraints and edge cases:** 1 ≤ q ≤ 200000; up to 100000 distinct items and up to 100000 snapshots; 0 ≤ qty ≤ 10^9. Copying the whole map on every `SNAPSHOT` would need about 10^10 operations in the worst case.

### Question 2: Interview-Based Coding
**Title:** Build Your Own HashMap

**Problem statement:** Implement a hash map from `int` keys to `int` values without using `HashMap`, `Hashtable`, `TreeMap`, or any other built-in map or set. It must support `PUT key value` (insert or overwrite), `GET key` (value or `-1` if absent), and `REMOVE key` (no effect if absent). Keep average O(1) time per operation no matter how many keys are stored.

**Input and output requirements:** Line 1: `q`. Then `q` commands. Print the result of every `GET`.

**Sample cases:**
```
Input:
9
PUT 1 1
PUT 2 2
GET 1
GET 3
PUT 2 1
GET 2
REMOVE 2
GET 2
GET -5
Output:
1
-1
1
-1
-1
```
```
Input:
6
PUT -5 7
GET -5
REMOVE -5
GET -5
REMOVE -5
PUT 1000000000 0
Output:
7
-1
```

**Constraints and edge cases:** 1 ≤ q ≤ 300000; keys are in the range [-10^9, 10^9] (negative keys must work); values are in [0, 10^9]. The test data may contain many keys that are congruent modulo any small power of two.

### Question 3: Company-Style Programming
**Title:** LFU Cache

**Problem statement:** Design a cache with capacity `C`. `GET key` returns the value or `-1`. `PUT key value` inserts or updates a key. When inserting a new key into a full cache, first evict the key with the **lowest use count**; if several keys share the lowest count, evict the one that was **least recently used** among them. Both `GET` hits and `PUT` operations (new or update) count as one use. A newly inserted key starts with a use count of 1. Every operation must run in O(1) average time.

**Input and output requirements:** Line 1: `C q`. Then `q` commands (`PUT key value` or `GET key`). Print the result of every `GET`.

**Sample cases:**
```
Input:
2 10
PUT 1 1
PUT 2 2
GET 1
PUT 3 3
GET 2
GET 3
PUT 4 4
GET 1
GET 3
GET 4
Output:
1
-1
3
-1
3
4
```
```
Input:
0 3
PUT 1 1
GET 1
PUT 2 2
Output:
-1
```

**Constraints and edge cases:** 0 ≤ C ≤ 100000; 1 ≤ q ≤ 300000. Capacity 0 means nothing is ever stored. A `GET` miss changes nothing. Updating an existing key never evicts anything.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    static class Key {
        int id;

        Key(int id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Key && ((Key) o).id == id;
        }

        @Override
        public int hashCode() {
            return id;
        }
    }

    public static void main(String[] args) {
        Set<Key> set = new HashSet<>();
        Key k = new Key(1);
        set.add(k);
        System.out.println(set.add(new Key(1)) + " " + set.size());
        k.id = 2;
        System.out.println(set.contains(k) + " " + set.contains(new Key(1)));
        set.add(new Key(2));
        System.out.println(set.size());
        System.out.println(set.remove(k) + " " + set.size());
        for (Key x : set) {
            System.out.println("left: " + x.id);
        }

        List<Integer> base = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> view = Collections.unmodifiableList(base);
        List<Integer> copy = new ArrayList<>(base);
        base.add(4);
        System.out.println(view + " " + copy);
        try {
            view.add(5);
        } catch (UnsupportedOperationException e) {
            System.out.println("view is read-only");
        }

        List<Integer> fixed = Arrays.asList(7, 8, 9);
        fixed.set(0, 70);
        System.out.println(fixed);
        try {
            fixed.remove(0);
        } catch (UnsupportedOperationException e) {
            System.out.println("fixed size");
        }
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Clean up an employee roster. (1) Keep only the **first** record for each employee id and discard later records with the same id. (2) Remove every employee whose `active` flag is `false`. (3) Keep the original order. For the roster `(1,Ana,active) (2,Bo,inactive) (1,Ana2,active) (3,Cy,active) (4,Di,inactive)` the result must be `Ana, Cy`.

The code below does not behave as intended, and it also fails with an exception for some rosters. Identify every bug (consider the `equals`/`hashCode` contract and collection modification rules), explain each, and write the corrected program.

```java
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    static class Employee {
        final int id;
        final String name;
        final boolean active;

        Employee(int id, String name, boolean active) {
            this.id = id;
            this.name = name;
            this.active = active;
        }

        public boolean equals(Employee other) {
            return other != null && id == other.id;
        }
    }

    static List<Employee> clean(List<Employee> staff) {
        Set<Employee> seen = new HashSet<>();
        List<Employee> unique = new ArrayList<>();
        for (Employee e : staff) {
            if (seen.add(e)) {
                unique.add(e);
            }
        }
        for (Employee e : unique) {
            if (!e.active) {
                unique.remove(e);
            }
        }
        return unique;
    }

    public static void main(String[] args) {
        List<Employee> staff = new ArrayList<>();
        staff.add(new Employee(1, "Ana", true));
        staff.add(new Employee(2, "Bo", false));
        staff.add(new Employee(1, "Ana2", true));
        staff.add(new Employee(3, "Cy", true));
        staff.add(new Employee(4, "Di", false));
        for (Employee e : clean(staff)) {
            System.out.println(e.name);
        }
    }
}
```

---

<a id="day8"></a>
## Day 8 — Binary Trees: Traversals and Construction

**Learning objectives:** Build binary trees from level-order input, perform level-order, vertical, and recursive traversals, reconstruct trees from traversal pairs, solve a greedy tree DP, and recognize iterative-versus-recursive trade-offs for deep trees.

**Tree input format used on Days 8-10:** A tree is given on one line in level-order using the token `null` for a missing child, for example `3 9 20 null null 15 7`. The first token is the root; children of absent nodes are not listed.

### Question 1: Mixed Real-World Scenario
**Title:** Floor-by-Floor Sensor Report

**Problem statement:** A smart building is modeled as a binary tree of rooms: the root is the lobby and each room has at most two rooms directly below it. Every room has a sensor reading. For every level of the tree (the lobby is level 0), print the number of rooms on that level, the maximum reading, and the readings of the leftmost and rightmost rooms on that level.

**Real-world context:** Facility managers summarize readings floor by floor to spot anomalies.

**Input requirements:** One line: the tree in level-order format (see the Day 8 format note).

**Expected output:** One line per level: `L<level>: count=<c> max=<m> left=<l> right=<r>`.

**Sample cases:**
```
Input:
3 9 20 null null 15 7
Output:
L0: count=1 max=3 left=3 right=3
L1: count=2 max=20 left=9 right=20
L2: count=2 max=15 left=15 right=7
```
```
Input:
-5 -10 -3
Output:
L0: count=1 max=-5 left=-5 right=-5
L1: count=2 max=-3 left=-10 right=-3
```
```
Input:
1 2 null 3
Output:
L0: count=1 max=1 left=1 right=1
L1: count=1 max=2 left=2 right=2
L2: count=1 max=3 left=3 right=3
```

**Constraints and edge cases:** 1 ≤ number of nodes ≤ 100000; readings fit in `int` and may be negative. The leftmost and rightmost rooms are determined by position, not by value. The tree may be a long chain.

### Question 2: Interview-Based Coding
**Title:** Rebuild a Tree From Preorder and Inorder

**Problem statement:** Given the preorder and inorder traversals of a binary tree with distinct values, reconstruct the tree and print its **postorder** traversal.

**Input and output requirements:** Line 1: `n`. Line 2: the preorder (`n` values). Line 3: the inorder (`n` values). Output the postorder values separated by spaces.

**Sample cases:**
```
Input:
5
3 9 20 15 7
9 3 15 20 7
Output:
9 15 7 20 3
```
```
Input:
1
1
1
Output:
1
```
```
Input:
3
1 2 3
3 2 1
Output:
3 2 1
```

**Constraints and edge cases:** 1 ≤ n ≤ 100000; values are distinct integers. The tree may be a chain of 100000 nodes, which is deeper than Java's default call stack handles reliably; searching the inorder array linearly for each root would be O(n²). Both traversals are guaranteed to describe the same valid tree.

### Question 3: Company-Style Programming
**Title:** Vertical Order Report

**Problem statement:** Assign each node a position `(row, col)`: the root is `(0, 0)`; a left child of `(r, c)` is at `(r+1, c-1)`; a right child is at `(r+1, c+1)`. Report the tree column by column from the smallest column to the largest. Within one column, list nodes from the smallest row to the largest; if two nodes share the same row and column, list the smaller value first.

**Input and output requirements:** One line: the tree in level-order format. Output one line per column with that column's values separated by spaces.

**Sample cases:**
```
Input:
3 9 20 null null 15 7
Output:
9
3 15
20
7
```
```
Input:
1 2 3 4 6 5 7
Output:
4
2
1 5 6
3
7
```
```
Input:
7
Output:
7
```

**Constraints and edge cases:** 1 ≤ number of nodes ≤ 50000; values fit in `int` and need not be distinct. Two different nodes can occupy the same position, and their level-order input order must not decide their output order.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
public class Main {
    static class Node {
        int val;
        Node left;
        Node right;

        Node(int val, Node left, Node right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    static StringBuilder log = new StringBuilder();
    static int calls = 0;

    static void walk(Node n, int depth) {
        calls++;
        if (n == null) {
            return;
        }
        log.append('[').append(n.val * depth);
        walk(n.left, depth + 1);
        log.append(n.val);
        walk(n.right, depth + 1);
        log.append(']');
    }

    static int sumLeaves(Node n) {
        if (n == null) {
            return 0;
        }
        if (n.left == null && n.right == null) {
            return n.val;
        }
        return sumLeaves(n.left) + sumLeaves(n.right);
    }

    public static void main(String[] args) {
        Node root = new Node(1,
                new Node(2, new Node(4, null, null), new Node(5, new Node(8, null, null), null)),
                new Node(3, null, new Node(6, null, null)));
        walk(root, 0);
        System.out.println(log);
        System.out.println(calls);
        System.out.println(sumLeaves(root));
        calls = 0;
        walk(root.left.right, 5);
        System.out.println(log.substring(log.lastIndexOf("[") - 3) + " " + calls);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Print the level-order traversal of a binary tree in **zigzag** form: level 0 left-to-right, level 1 right-to-left, level 2 left-to-right, and so on. Each level is printed on its own line. For the tree `1 2 3 4 5 6 7` the output is:
```
1
3 2
4 5 6 7
```

The code below prints wrong lines for most trees. Find every bug, explain what goes wrong, and give the corrected method.

```java
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Main {
    static class Node {
        int val;
        Node left;
        Node right;

        Node(int val, Node left, Node right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    static List<List<Integer>> zigzag(Node root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) {
            return res;
        }
        Queue<Node> q = new LinkedList<>();
        q.add(root);
        boolean leftToRight = true;
        while (!q.isEmpty()) {
            LinkedList<Integer> level = new LinkedList<>();
            for (int i = 0; i < q.size(); i++) {
                Node n = q.poll();
                if (leftToRight) {
                    level.addLast(n.val);
                } else {
                    level.addFirst(n.val);
                }
                if (n.left != null) {
                    q.add(n.left);
                }
                if (n.right != null) {
                    q.add(n.right);
                }
            }
            res.add(level);
        }
        return res;
    }

    public static void main(String[] args) {
        Node root = new Node(1,
                new Node(2, new Node(4, null, null), new Node(5, null, null)),
                new Node(3, new Node(6, null, null), new Node(7, null, null)));
        for (List<Integer> level : zigzag(root)) {
            StringBuilder sb = new StringBuilder();
            for (int v : level) {
                sb.append(v).append(' ');
            }
            System.out.println(sb.toString().trim());
        }
    }
}
```

---

<a id="day9"></a>
## Day 9 — Binary Trees: LCA, Height, Diameter, and Path Problems

**Learning objectives:** Answer many lowest-common-ancestor queries efficiently, compute maximum path sums, solve a greedy tree DP, track global answers through recursion, and notice boxed-type comparison pitfalls.

### Question 1: Mixed Real-World Scenario
**Title:** Lowest Common Manager

**Problem statement:** A company is a tree in which every manager has at most two direct reports. Employees are numbered `0` to `n-1` and employee `0` is the CEO. For each query `(a, b)`, report the **lowest common manager** (the deepest employee who is `a`, `b`, or an ancestor of both) and the **number of reporting links** on the path between `a` and `b`.

**Real-world context:** HR tools find the nearest shared manager to route cross-team approvals and to measure organizational distance.

**Input requirements:** Line 1: `n`. Next `n` lines: line `i` has `left right`, the two direct reports of employee `i` (`-1` if absent). Next line: `q`. Then `q` lines `a b`.

**Expected output:** For each query print `<lcm> <distance>`.

**Sample cases:**
```
Input:
7
1 2
3 4
5 6
-1 -1
-1 -1
-1 -1
-1 -1
4
3 4
3 6
1 3
5 5
Output:
1 2
0 4
1 1
5 0
```
```
Input:
4
1 -1
-1 2
3 -1
-1 -1
2
0 3
2 1
Output:
0 3
1 1
```

**Constraints and edge cases:** 1 ≤ n ≤ 100000; 1 ≤ q ≤ 100000. The hierarchy may be a chain 100000 levels deep, so walking up from both employees for every query is too slow, and deep recursion may overflow the stack. A query may have `a == b`, or one employee may be an ancestor of the other.

### Question 2: Interview-Based Coding
**Title:** Maximum Path Sum

**Problem statement:** A path in a binary tree is any sequence of nodes in which each adjacent pair is connected by an edge and no node appears twice. The path does not need to pass through the root or end at a leaf, but must contain at least one node. Find the maximum possible sum of node values over all paths.

**Input and output requirements:** One line: the tree in level-order format. Output the maximum path sum.

**Sample cases:**
```
Input:
-10 9 20 null null 15 7
Output:
42
```
```
Input:
1 2 3
Output:
6
```
```
Input:
-3
Output:
-3
```
```
Input:
2 -1
Output:
2
```

**Constraints and edge cases:** 1 ≤ number of nodes ≤ 20000; -1000 ≤ value ≤ 1000. Trees whose values are all negative must return the largest single value. A path may bend at a node, using both of its children.

### Question 3: Company-Style Programming
**Title:** Minimum Monitors for a Tree Network

**Problem statement:** A tree-shaped network must be watched by monitors. A monitor placed on a node watches itself, its parent, and its direct children. Find the minimum number of monitors needed so that every node is watched.

**Input and output requirements:** One line: the tree in level-order format (the node values are irrelevant, they are all `0`). Output the minimum number of monitors.

**Sample cases:**
```
Input:
0 0 null 0 0
Output:
1
```
```
Input:
0 0 null 0 null 0 null null 0
Output:
2
```
```
Input:
0
Output:
1
```

**Constraints and edge cases:** 1 ≤ number of nodes ≤ 100000. A single-node tree needs one monitor. Placing monitors on every node is valid but not minimal. The tree may be deeper than the default recursion limit allows.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
public class Main {
    static class Node {
        int val;
        Node left;
        Node right;

        Node(int val, Node left, Node right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    static int calls = 0;
    static int best = 0;

    static int height(Node n) {
        calls++;
        if (n == null) {
            return 0;
        }
        int l = height(n.left);
        int r = height(n.right);
        best = Math.max(best, l + r);
        return 1 + Math.max(l, r);
    }

    static Node lca(Node root, int p, int q) {
        if (root == null || root.val == p || root.val == q) {
            return root;
        }
        Node l = lca(root.left, p, q);
        Node r = lca(root.right, p, q);
        if (l != null && r != null) {
            return root;
        }
        return l != null ? l : r;
    }

    public static void main(String[] args) {
        Node root = new Node(10,
                new Node(5, new Node(3, new Node(1, null, null), null), new Node(7, null, new Node(8, null, null))),
                new Node(15, null, new Node(20, null, null)));
        System.out.println(height(root) + " " + best + " " + calls);
        System.out.println(lca(root, 1, 8).val);
        System.out.println(lca(root, 3, 1).val);
        System.out.println(lca(root, 8, 20).val);
        Node missing = lca(root, 1, 99);
        System.out.println(missing == null ? "null" : String.valueOf(missing.val));
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Find the lowest common ancestor of two nodes (given by value) in a binary tree. Both values are guaranteed to be present, and a node may be an ancestor of itself. Node values are arbitrary integers (including values larger than 127). For the tree with root `300`, left child `200` (children `100` and `250`), right child `400`, `lca(100, 250)` must be `200`, `lca(100, 400)` must be `300`, and `lca(200, 250)` must be `200`.

The code below returns wrong nodes for several valid queries. Identify every bug, explain why each one fails, and correct the method.

```java
public class Main {
    static class Node {
        Integer val;
        Node left;
        Node right;

        Node(Integer val, Node left, Node right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    static Node lca(Node root, Integer p, Integer q) {
        if (root == null) {
            return null;
        }
        if (root.val == p || root.val == q) {
            return root;
        }
        Node l = lca(root.left, p, q);
        Node r = lca(root.right, p, q);
        if (l != null) {
            return l;
        }
        return r;
    }

    public static void main(String[] args) {
        Node root = new Node(300,
                new Node(200, new Node(100, null, null), new Node(250, null, null)),
                new Node(400, null, null));
        System.out.println(lca(root, 100, 250).val);
        System.out.println(lca(root, 100, 400).val);
        System.out.println(lca(root, 200, 250).val);
    }
}
```

---

<a id="day10"></a>
## Day 10 — Binary Search Trees

**Learning objectives:** Use ordered structures (`TreeMap`/`TreeSet`) for floor and ceiling queries, validate BST properties without integer-bound bugs, recover a corrupted BST, and implement insertion and deletion with the successor technique.

### Question 1: Mixed Real-World Scenario
**Title:** Conference Room Booking

**Problem statement:** A room accepts bookings as half-open time intervals `[start, end)`. A booking request `BOOK start end` is **accepted** if it does not overlap any previously accepted booking (two intervals overlap if they share at least one instant; touching at an endpoint, such as `[10,20)` and `[20,30)`, is not an overlap) and rejected otherwise. Rejected requests are not stored. Print the verdict for each request.

**Real-world context:** Calendar systems reject double bookings, and they must do so quickly even for rooms with hundreds of thousands of reservations.

**Input requirements:** Line 1: `q`. Then `q` lines `BOOK start end` with `start < end`.

**Expected output:** `YES` or `NO` for each request.

**Sample cases:**
```
Input:
5
BOOK 10 20
BOOK 15 25
BOOK 20 30
BOOK 5 10
BOOK 0 40
Output:
YES
NO
YES
YES
NO
```
```
Input:
3
BOOK 1 2
BOOK 1 2
BOOK 2 3
Output:
YES
NO
YES
```

**Constraints and edge cases:** 1 ≤ q ≤ 200000; 0 ≤ start < end ≤ 10^9. A new booking can contain an existing one, be contained by one, or overlap on either side. Checking all accepted bookings for every request is too slow.

### Question 2: Interview-Based Coding
**Title:** Validate a Binary Search Tree

**Problem statement:** Determine whether a binary tree is a valid BST: for every node, all values in its left subtree are **strictly less** than the node's value and all values in its right subtree are **strictly greater**. Print `true` or `false`.

**Input and output requirements:** One line: the tree in level-order format (`null` marks a missing child). Output `true` or `false`.

**Sample cases:**
```
Input:
2 1 3
Output:
true
```
```
Input:
5 4 6 null null 3 7
Output:
false
```
```
Input:
-2147483648 null 2147483647
Output:
true
```
```
Input:
2 2 2
Output:
false
```

**Constraints and edge cases:** 1 ≤ number of nodes ≤ 100000; values cover the entire `int` range, including `Integer.MIN_VALUE` and `Integer.MAX_VALUE`. Checking only each node against its direct children is incorrect. The tree may be a chain of 100000 nodes.

### Question 3: Company-Style Programming
**Title:** Find the Swapped Pair

**Problem statement:** Exactly two nodes of a valid BST (with distinct values) had their values swapped by a faulty process. Given the corrupted tree, print the two swapped values in ascending order.

**Input and output requirements:** One line: the corrupted tree in level-order format. Output the two values separated by a space, smaller first.

**Sample cases:**
```
Input:
1 3 null null 2
Output:
1 3
```
```
Input:
3 1 4 null null 2
Output:
2 3
```
```
Input:
2 3 1
Output:
1 3
```

**Constraints and edge cases:** 2 ≤ number of nodes ≤ 100000; values are distinct. The swapped nodes can be adjacent in inorder order or far apart. Aim for O(n) time and avoid copying all values into a separate collection.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
public class Main {
    static class Node {
        int val;
        Node left;
        Node right;

        Node(int val) {
            this.val = val;
        }
    }

    static Node insert(Node root, int v) {
        if (root == null) {
            return new Node(v);
        }
        if (v < root.val) {
            root.left = insert(root.left, v);
        } else if (v > root.val) {
            root.right = insert(root.right, v);
        }
        return root;
    }

    static Node removeMin(Node root) {
        if (root.left == null) {
            return root.right;
        }
        root.left = removeMin(root.left);
        return root;
    }

    static void inorder(Node n, StringBuilder sb) {
        if (n == null) {
            return;
        }
        inorder(n.left, sb);
        sb.append(n.val).append(' ');
        inorder(n.right, sb);
    }

    static int height(Node n) {
        return n == null ? 0 : 1 + Math.max(height(n.left), height(n.right));
    }

    public static void main(String[] args) {
        Node root = null;
        for (int v : new int[]{50, 30, 70, 20, 40, 60, 80, 30, 65, 20}) {
            root = insert(root, v);
        }
        StringBuilder sb = new StringBuilder();
        inorder(root, sb);
        System.out.println(sb.toString().trim() + " | h=" + height(root));

        root = removeMin(root);
        root = removeMin(root);
        sb.setLength(0);
        inorder(root, sb);
        System.out.println(sb.toString().trim() + " | h=" + height(root));

        Node cur = root;
        int steps = 0;
        while (cur.right != null) {
            cur = cur.right;
            steps++;
        }
        System.out.println(cur.val + " " + steps + " " + root.left.val + " " + root.right.left.right.val);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Delete a key from a BST (do nothing if the key is absent) and return the new root. A node with no or one child is replaced by that child. A node with two children takes the value of its **inorder successor** (the smallest value in its right subtree), and that successor node is then removed from the right subtree. For the BST built by inserting `50 30 70 20 40 60 80`, deleting `20`, then `30`, then `50` leaves the inorder sequence `40 60 70 80`.

The code below leaves keys in the tree or leaves duplicate values behind. Find every bug, explain it, and write the corrected method.

```java
public class Main {
    static class Node {
        int val;
        Node left;
        Node right;

        Node(int val) {
            this.val = val;
        }
    }

    static Node insert(Node root, int v) {
        if (root == null) {
            return new Node(v);
        }
        if (v < root.val) {
            root.left = insert(root.left, v);
        } else {
            root.right = insert(root.right, v);
        }
        return root;
    }

    static Node delete(Node root, int key) {
        if (root == null) {
            return null;
        }
        if (key < root.val) {
            delete(root.left, key);
        } else if (key > root.val) {
            root.right = delete(root.right, key);
        } else {
            if (root.left == null) {
                return root.right;
            }
            if (root.right == null) {
                return root.left;
            }
            Node succ = root.right;
            while (succ.left != null) {
                succ = succ.left;
            }
            root.val = succ.val;
            root.right = delete(root.right, key);
        }
        return root;
    }

    static void inorder(Node n, StringBuilder sb) {
        if (n == null) {
            return;
        }
        inorder(n.left, sb);
        sb.append(n.val).append(' ');
        inorder(n.right, sb);
    }

    public static void main(String[] args) {
        Node root = null;
        for (int v : new int[]{50, 30, 70, 20, 40, 60, 80}) {
            root = insert(root, v);
        }
        root = delete(root, 20);
        root = delete(root, 30);
        root = delete(root, 50);
        StringBuilder sb = new StringBuilder();
        inorder(root, sb);
        System.out.println(sb.toString().trim());
    }
}
```

---

<a id="day11"></a>
## Day 11 — Tries and Prefix Structures

**Learning objectives:** Build tries with array and map children, store aggregate data in nodes (top suggestions, counts), use binary tries for XOR problems, handle wildcard search with recursion, and reason about memory and time per character.

### Question 1: Mixed Real-World Scenario
**Title:** Search Box Autocomplete

**Problem statement:** An e-commerce search box suggests products as the user types. There are `n` known product names, each with a popularity score. For each typed prefix, print the **top 3** product names that start with that prefix: higher popularity first; ties broken by alphabetical order (ascending). If no product matches, print `NONE`. A product whose name equals the prefix also matches.

**Real-world context:** Autocomplete must respond instantly to every keystroke, so work cannot depend on scanning the whole catalogue per query.

**Input requirements:** Line 1: `n q`. Next `n` lines: `name popularity`. Next `q` lines: one prefix each. Product names are distinct lowercase words.

**Expected output:** For each prefix, up to 3 names separated by spaces, or `NONE`.

**Sample cases:**
```
Input:
5 4
apple 5
app 9
apply 5
apt 7
bat 2
ap
app
b
z
Output:
app apt apple
app apple apply
bat
NONE
```
```
Input:
2 2
a 1
ab 1
a
ab
Output:
a ab
ab
```

**Constraints and edge cases:** 1 ≤ n, q ≤ 100000; total length of all names ≤ 1000000 and total length of all prefixes ≤ 1000000; popularity ≤ 10^9. Scanning every name for every prefix is far too slow.

### Question 2: Interview-Based Coding
**Title:** Longest Buildable Word

**Problem statement:** Given a list of words, find the longest word that can be built one character at a time by other words in the list: every prefix of the word (of every length from 1 up to its full length) must also be in the list. If several words have the same maximum length, print the lexicographically smallest. If no word qualifies, print an empty line.

**Input and output requirements:** Line 1: `n`. Line 2: `n` space-separated lowercase words. Output the word.

**Sample cases:**
```
Input:
5
w wo wor worl world
Output:
world
```
```
Input:
7
a banana app appl ap apply apple
Output:
apple
```
```
Input:
3
yo ew fc
Output:

```

**Constraints and edge cases:** 1 ≤ n ≤ 30000; word length 1-30; words are distinct. A word of length 1 is always buildable. The empty-output case should print just a newline.

### Question 3: Company-Style Programming
**Title:** Maximum Pair XOR

**Problem statement:** Given `n` non-negative integers, find the maximum value of `a[i] XOR a[j]` over all pairs with `0 ≤ i, j < n` (a number may be paired with itself, giving 0).

**Input and output requirements:** Line 1: `n`. Line 2: `n` integers. Output a single integer.

**Sample cases:**
```
Input:
6
3 10 5 25 2 8
Output:
28
```
```
Input:
12
14 70 53 83 49 91 36 80 92 51 66 70
Output:
127
```
```
Input:
1
7
Output:
0
```

**Constraints and edge cases:** 1 ≤ n ≤ 200000; 0 ≤ a[i] < 2^31. Comparing every pair is about 2·10^10 operations. Use `long` or careful bit handling if you shift by 31.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
public class Main {
    static class Trie {
        Trie[] kids = new Trie[26];
        int pass;
        int end;
    }

    static void insert(Trie root, String w) {
        Trie t = root;
        for (char c : w.toCharArray()) {
            int i = c - 'a';
            if (t.kids[i] == null) {
                t.kids[i] = new Trie();
            }
            t = t.kids[i];
            t.pass++;
        }
        t.end++;
    }

    static Trie find(Trie root, String s) {
        Trie t = root;
        for (char c : s.toCharArray()) {
            t = t.kids[c - 'a'];
            if (t == null) {
                return null;
            }
        }
        return t;
    }

    static int countPrefix(Trie root, String p) {
        Trie t = find(root, p);
        return t == null ? 0 : t.pass;
    }

    static int countWord(Trie root, String w) {
        Trie t = find(root, w);
        return t == null ? 0 : t.end;
    }

    static boolean erase(Trie root, String w) {
        if (countWord(root, w) == 0) {
            return false;
        }
        Trie t = root;
        for (char c : w.toCharArray()) {
            t = t.kids[c - 'a'];
            t.pass--;
        }
        t.end--;
        return true;
    }

    public static void main(String[] args) {
        Trie root = new Trie();
        for (String w : new String[]{"car", "card", "care", "cat", "car", "dog"}) {
            insert(root, w);
        }
        System.out.println(countPrefix(root, "car") + " " + countPrefix(root, "ca") + " " + countPrefix(root, "d"));
        System.out.println(countWord(root, "car") + " " + countWord(root, "ca") + " " + countWord(root, "cards"));
        System.out.println(erase(root, "car") + " " + erase(root, "ca"));
        System.out.println(countPrefix(root, "car") + " " + countWord(root, "car"));
        System.out.println(erase(root, "car") + " " + erase(root, "car"));
        System.out.println(countPrefix(root, "car") + " " + countWord(root, "car") + " " + countPrefix(root, "ca"));
        System.out.println(find(root, "cat").kids[0] == null);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Implement a dictionary that supports `addWord(word)` and `search(pattern)`, where the pattern may contain `.` standing for any single letter. `search` must return `true` only if some **whole** stored word matches the pattern exactly (same length). After adding `bad`, `dad`, and `mad`: `search("pad")` is `false`, `search("bad")` is `true`, `search(".ad")` is `true`, `search("b..")` is `true`, `search("ba")` is `false`, `search("b.d.")` is `false`, and `search("...")` is `true`.

The code below gives wrong answers for several of these calls. Find every bug, explain the effect of each, and rewrite the class correctly.

```java
public class Main {
    static class WordDictionary {
        private static class Node {
            Node[] next = new Node[26];
            boolean end;
        }

        private final Node root = new Node();

        void addWord(String word) {
            Node cur = root;
            for (char c : word.toCharArray()) {
                int i = c - 'a';
                if (cur.next[i] == null) {
                    cur.next[i] = new Node();
                }
                cur = cur.next[i];
                cur.end = true;
            }
        }

        boolean search(String word) {
            return dfs(word, 0, root);
        }

        private boolean dfs(String word, int pos, Node node) {
            if (pos == word.length()) {
                return true;
            }
            char c = word.charAt(pos);
            if (c == '.') {
                for (Node child : node.next) {
                    if (child != null) {
                        return dfs(word, pos + 1, child);
                    }
                }
                return false;
            }
            Node child = node.next[c - 'a'];
            return child != null && dfs(word, pos + 1, child);
        }
    }

    public static void main(String[] args) {
        WordDictionary d = new WordDictionary();
        d.addWord("bad");
        d.addWord("dad");
        d.addWord("mad");
        String[] queries = {"pad", "bad", ".ad", "b..", "ba", "b.d.", "..."};
        for (String q : queries) {
            System.out.println(q + " -> " + d.search(q));
        }
    }
}
```

---

<a id="day12"></a>
## Day 12 — Graphs I: Representation, BFS, and DFS

**Learning objectives:** Choose between adjacency lists and matrices, run BFS for shortest paths in unweighted graphs and grids (including multi-source BFS), extend BFS with extra state, and use DFS for connectivity while avoiding stack overflow and off-by-one indexing errors.

### Question 1: Mixed Real-World Scenario
**Title:** Wildfire Spread Simulator

**Problem statement:** A forest is a grid. `F` is a burning cell, `T` is a tree, `.` is bare ground, and `#` is water. Each minute, every burning cell ignites its **4-directional** neighbors that contain trees (fire does not cross ground or water). Fires start at time 0 in all `F` cells simultaneously. Compute the number of minutes until the last tree that can burn has ignited, and the number of trees that never burn.

**Real-world context:** Fire-management tools estimate how long a fire takes to run its course and how much vegetation is cut off by natural barriers.

**Input requirements:** Line 1: `R C`. Then `R` lines, each with `C` characters from `F T . #`.

**Expected output:** Two lines: `Minutes: <m>` and `Unburnt: <u>`. If no tree ever ignites, `Minutes` is `0`.

**Sample cases:**
```
Input:
4 5
FTT#T
T#TT.
TT.TT
#TT.T
Output:
Minutes: 7
Unburnt: 1
```
```
Input:
3 3
F#T
###
T.T
Output:
Minutes: 0
Unburnt: 3
```
```
Input:
1 3
TTT
Output:
Minutes: 0
Unburnt: 3
```

**Constraints and edge cases:** 1 ≤ R, C ≤ 1000. The grid can have multiple fire sources, or none. Recursive flood fill can overflow the stack on a 1000 × 1000 grid.

### Question 2: Interview-Based Coding
**Title:** Word Ladder Length

**Problem statement:** Transform `beginWord` into `endWord` by changing exactly one letter at a time; every intermediate word (and `endWord`) must be in the given word list. Print the number of words in the shortest transformation sequence, counting both `beginWord` and `endWord`, or `0` if no sequence exists. `beginWord` itself does not have to be in the list.

**Input and output requirements:** Line 1: `beginWord endWord`. Line 2: `n`. Line 3: `n` words. All words have the same length. Output a single integer.

**Sample cases:**
```
Input:
hit cog
6
hot dot dog lot log cog
Output:
5
```
```
Input:
hit cog
5
hot dot dog lot log
Output:
0
```
```
Input:
a c
3
a b c
Output:
2
```

**Constraints and edge cases:** 1 ≤ n ≤ 5000; words have length 1-10 and use lowercase letters. If `endWord` is not in the list the answer is `0`. Comparing every pair of words is O(n² · L); generate neighbors more cleverly.

### Question 3: Company-Style Programming
**Title:** Grid Walk With Wall Breaking

**Problem statement:** In a grid of `m` rows and `n` columns, `0` is an open cell and `1` is a wall. Starting at the top-left cell `(0,0)` you want to reach the bottom-right cell `(m-1,n-1)` moving up, down, left, or right. You may pass through (destroy) at most `k` wall cells in total. Print the minimum number of steps, or `-1` if it is impossible. The start and end cells are always open.

**Input and output requirements:** Line 1: `m n k`. Then `m` lines of `n` space-separated 0/1 values. Output one integer.

**Sample cases:**
```
Input:
5 3 1
0 0 0
1 1 0
0 0 0
0 1 1
0 0 0
Output:
6
```
```
Input:
3 3 1
0 1 1
1 1 1
1 0 0
Output:
-1
```
```
Input:
1 1 0
0
Output:
0
```
```
Input:
2 2 0
0 1
1 0
Output:
-1
```

**Constraints and edge cases:** 1 ≤ m, n ≤ 40; 0 ≤ k ≤ 1600. Visiting the same cell with a different number of remaining breaks can lead to different results, so a plain visited grid is not enough. If `k` is very large the answer is just the Manhattan distance, so avoid unnecessary state.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Main {
    static List<List<Integer>> g = new ArrayList<>();
    static StringBuilder out = new StringBuilder();

    static void dfs(int u, boolean[] seen) {
        seen[u] = true;
        out.append(u).append(' ');
        for (int v : g.get(u)) {
            if (!seen[v]) {
                dfs(v, seen);
            }
        }
    }

    public static void main(String[] args) {
        int n = 7;
        for (int i = 0; i < n; i++) {
            g.add(new ArrayList<>());
        }
        int[][] edges = {{0, 1}, {0, 2}, {1, 3}, {2, 3}, {3, 4}, {2, 5}, {4, 5}};
        for (int[] e : edges) {
            g.get(e[0]).add(e[1]);
            g.get(e[1]).add(e[0]);
        }

        dfs(0, new boolean[n]);
        System.out.println(out.toString().trim());

        StringBuilder bfs = new StringBuilder();
        int[] dist = new int[n];
        java.util.Arrays.fill(dist, -1);
        Deque<Integer> q = new ArrayDeque<>();
        q.add(0);
        dist[0] = 0;
        while (!q.isEmpty()) {
            int u = q.poll();
            bfs.append(u).append(':').append(dist[u]).append(' ');
            for (int v : g.get(u)) {
                if (dist[v] < 0) {
                    dist[v] = dist[u] + 1;
                    q.add(v);
                }
            }
        }
        System.out.println(bfs.toString().trim());

        StringBuilder st = new StringBuilder();
        boolean[] seen = new boolean[n];
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(0);
        while (!stack.isEmpty()) {
            int u = stack.pop();
            if (seen[u]) {
                continue;
            }
            seen[u] = true;
            st.append(u).append(' ');
            for (int v : g.get(u)) {
                if (!seen[v]) {
                    stack.push(v);
                }
            }
        }
        System.out.println(st.toString().trim());
        System.out.println(dist[6] + " " + seen[6]);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Count the connected components of an undirected graph with vertices numbered `1` to `n` and the given list of edges. For `n = 5` and edges `(1,2), (2,3), (4,5)` the answer is `2`; for `n = 4` and no edges the answer is `4`; for `n = 6` and edges `(1,2), (2,3), (3,1), (5,6)` the answer is `3`.

The program below throws an exception or returns wrong counts. Identify every bug, explain each, and correct the method.

```java
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Main {
    static int components(int n, int[][] edges) {
        List<List<Integer>> g = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            g.add(new ArrayList<>());
        }
        for (int[] e : edges) {
            g.get(e[0]).add(e[1]);
        }
        boolean[] seen = new boolean[n];
        int count = 0;
        for (int s = 1; s < n; s++) {
            if (seen[s]) {
                continue;
            }
            count++;
            Deque<Integer> queue = new ArrayDeque<>();
            queue.add(s);
            seen[s] = true;
            while (!queue.isEmpty()) {
                int u = queue.poll();
                for (int v : g.get(u)) {
                    if (!seen[v]) {
                        seen[v] = true;
                        queue.add(v);
                    }
                }
            }
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println(components(5, new int[][]{{1, 2}, {2, 3}, {4, 5}}));
        System.out.println(components(4, new int[][]{}));
        System.out.println(components(6, new int[][]{{1, 2}, {2, 3}, {3, 1}, {5, 6}}));
    }
}
```

---

<a id="day13"></a>
## Day 13 — Graphs II: Topological Sort and Cycle Detection

**Learning objectives:** Compute in-degrees and run Kahn's algorithm, find nodes blocked by cycles, produce lexicographically smallest orders with a priority queue, compute longest paths in a DAG (critical path), and detect directed cycles with a correct three-state DFS.

### Question 1: Mixed Real-World Scenario
**Title:** Deployment Pipeline Blocker Finder

**Problem statement:** A release pipeline has tasks `1..n` and dependency rules `a b` meaning "task `a` must finish before task `b` can start". Some rules were configured badly and form loops. A task is **blocked** if it can never be started: it lies on a cycle, or it depends (directly or indirectly) on a task that is blocked. List all blocked tasks in ascending order.

**Real-world context:** CI/CD systems must report which jobs will never run because of circular dependencies, rather than just saying "cycle detected".

**Input requirements:** Line 1: `n m`. Next `m` lines: `a b`.

**Expected output:** `Blocked: <tasks in ascending order separated by spaces>` or `All tasks schedulable` if none are blocked.

**Sample cases:**
```
Input:
6 5
1 2
2 3
3 2
3 4
5 6
Output:
Blocked: 2 3 4
```
```
Input:
4 3
1 2
2 3
1 3
Output:
All tasks schedulable
```
```
Input:
3 1
1 1
Output:
Blocked: 1
```

**Constraints and edge cases:** 1 ≤ n ≤ 100000; 0 ≤ m ≤ 200000. Duplicate rules may appear; self-dependencies (`a a`) are cycles. Task 1 in sample 1 is not blocked even though it leads into a cycle, because it has no unmet dependency.

### Question 2: Interview-Based Coding
**Title:** Alien Alphabet Order

**Problem statement:** A list of words is sorted according to the rules of an unknown alphabet. Deduce an ordering of the letters that appears in the words that is consistent with the list. If several orderings are valid, print the **lexicographically smallest** one (comparing by the normal English letter order). If the list is inconsistent (a cycle in the derived rules, or a longer word appears before its own prefix), print `INVALID`. Only letters that occur in the words are part of the answer.

**Input and output requirements:** Line 1: `n`. Line 2: `n` lowercase words in the given order. Output the letters as one string, or `INVALID`.

**Sample cases:**
```
Input:
5
wrt wrf er ett rftt
Output:
wertf
```
```
Input:
3
z x z
Output:
INVALID
```
```
Input:
2
abc ab
Output:
INVALID
```
```
Input:
4
za zb ca cb
Output:
abzc
```
```
Input:
4
ba bc ac cab
Output:
bac
```

**Constraints and edge cases:** 1 ≤ n ≤ 10000; word lengths 1-100. Identical adjacent words give no information. Only the first differing character of two adjacent words gives a rule; later characters must not create rules.

### Question 3: Company-Style Programming
**Title:** Project Completion Time

**Problem statement:** A project has `n` tasks with durations. Task `a b` means task `b` cannot start until task `a` has finished. There are unlimited workers, so tasks with satisfied dependencies start immediately. Print the total time until all tasks finish and the finish time of every task (in task-number order). If the dependencies contain a cycle, print `CYCLE`.

**Input and output requirements:** Line 1: `n m`. Line 2: `n` durations. Next `m` lines: `a b` (1-indexed). Output two lines `Total: <t>` and `Finish: <f1> <f2> ... <fn>`, or the single line `CYCLE`.

**Sample cases:**
```
Input:
5 4
3 2 4 1 2
1 3
2 3
3 4
3 5
Output:
Total: 9
Finish: 3 2 7 8 9
```
```
Input:
3 0
5 5 5
Output:
Total: 5
Finish: 5 5 5
```
```
Input:
2 2
1 1
1 2
2 1
Output:
CYCLE
```

**Constraints and edge cases:** 1 ≤ n ≤ 100000; 0 ≤ m ≤ 200000; 1 ≤ duration ≤ 10^9. Finish times can exceed the `int` range on long chains.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.PriorityQueue;

public class Main {
    static List<List<Integer>> g = new ArrayList<>();
    static boolean[] seen = new boolean[6];
    static Deque<Integer> post = new ArrayDeque<>();

    static void dfs(int u) {
        seen[u] = true;
        for (int v : g.get(u)) {
            if (!seen[v]) {
                dfs(v);
            }
        }
        post.push(u);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 6; i++) {
            g.add(new ArrayList<>());
        }
        int[][] edges = {{5, 2}, {5, 0}, {4, 0}, {4, 1}, {2, 3}, {3, 1}};
        int[] indeg = new int[6];
        for (int[] e : edges) {
            g.get(e[0]).add(e[1]);
            indeg[e[1]]++;
        }

        for (int i = 0; i < 6; i++) {
            if (!seen[i]) {
                dfs(i);
            }
        }
        System.out.println(post);

        int[] d = indeg.clone();
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int i = 0; i < 6; i++) {
            if (d[i] == 0) {
                pq.add(i);
            }
        }
        StringBuilder sb = new StringBuilder();
        while (!pq.isEmpty()) {
            int u = pq.poll();
            sb.append(u);
            for (int v : g.get(u)) {
                if (--d[v] == 0) {
                    pq.add(v);
                }
            }
        }
        System.out.println(sb);

        d = indeg.clone();
        Deque<Integer> q = new ArrayDeque<>();
        for (int i = 5; i >= 0; i--) {
            if (d[i] == 0) {
                q.add(i);
            }
        }
        sb.setLength(0);
        while (!q.isEmpty()) {
            int u = q.poll();
            sb.append(u);
            for (int v : g.get(u)) {
                if (--d[v] == 0) {
                    q.add(v);
                }
            }
        }
        System.out.println(sb);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Return `true` if and only if a **directed** graph (vertices `0..n-1`, adjacency lists) contains a cycle. The graph may be disconnected. Examples: edges `0→1, 1→2, 2→0` have a cycle; the diamond `0→1, 0→2, 1→3, 2→3` has no cycle; two components `0→1` and `2→3, 3→2` have a cycle (in the component not reachable from vertex 0).

The code below reports a cycle in graphs that are acyclic and misses cycles in other graphs. Find every bug, explain each failure with an example graph, and fix the method.

```java
import java.util.ArrayList;
import java.util.List;

public class Main {
    static boolean hasCycle(int n, List<List<Integer>> g) {
        boolean[] visited = new boolean[n];
        return dfs(0, g, visited);
    }

    static boolean dfs(int u, List<List<Integer>> g, boolean[] visited) {
        if (visited[u]) {
            return true;
        }
        visited[u] = true;
        for (int v : g.get(u)) {
            if (dfs(v, g, visited)) {
                return true;
            }
        }
        return false;
    }

    static List<List<Integer>> build(int n, int[][] edges) {
        List<List<Integer>> g = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            g.add(new ArrayList<>());
        }
        for (int[] e : edges) {
            g.get(e[0]).add(e[1]);
        }
        return g;
    }

    public static void main(String[] args) {
        System.out.println(hasCycle(3, build(3, new int[][]{{0, 1}, {1, 2}, {2, 0}})));
        System.out.println(hasCycle(4, build(4, new int[][]{{0, 1}, {0, 2}, {1, 3}, {2, 3}})));
        System.out.println(hasCycle(4, build(4, new int[][]{{0, 1}, {2, 3}, {3, 2}})));
    }
}
```

---

<a id="day14"></a>
## Day 14 — Shortest Paths and Union-Find

**Learning objectives:** Implement Dijkstra with a priority queue and stale-entry checks, count shortest paths, use bounded Bellman-Ford rounds for "at most K edges" problems, build a disjoint-set union with path compression and union by size, and avoid integer overflow in distances.

### Question 1: Mixed Real-World Scenario
**Title:** Cheapest Courier Routes

**Problem statement:** A courier company has `n` depots (numbered `0..n-1`) connected by `m` two-way roads with positive travel costs. For a delivery from depot `s` to depot `t`, report the minimum total cost and the number of **different** cheapest routes (modulo 1,000,000,007). Two routes are different if the sequences of roads they use differ. Print `UNREACHABLE` if `t` cannot be reached.

**Real-world context:** Dispatchers want the cheapest cost and also how many equally good alternatives exist, in case one road is closed.

**Input requirements:** Line 1: `n m s t`. Next `m` lines: `u v w`.

**Expected output:** `<cost> <count>` or `UNREACHABLE`.

**Sample cases:**
```
Input:
4 4 0 3
0 1 1
0 2 1
1 3 1
2 3 1
Output:
2 2
```
```
Input:
3 1 0 2
0 1 5
Output:
UNREACHABLE
```
```
Input:
5 6 0 4
0 1 2
1 4 2
0 2 1
2 3 1
3 4 2
0 4 4
Output:
4 3
```
```
Input:
1 0 0 0
Output:
0 1
```

**Constraints and edge cases:** 1 ≤ n ≤ 100000; 0 ≤ m ≤ 200000; 1 ≤ w ≤ 10^9, so total cost can exceed the `int` range (use `long`). Parallel roads may exist and each one counts as a different route. When `s == t` the cost is 0 with exactly one route.

### Question 2: Interview-Based Coding
**Title:** Cheapest Flight With Limited Stops

**Problem statement:** There are `n` cities and `m` one-way flights `a b price`. Find the cheapest price from city `src` to city `dst` using **at most `k` stops** (intermediate cities), or `-1` if impossible. A trip with zero stops uses only a direct flight.

**Input and output requirements:** Line 1: `n m`. Next `m` lines: `a b price`. Last line: `src dst k`. Output the price or `-1`.

**Sample cases:**
```
Input:
4 5
0 1 100
1 2 100
2 0 100
1 3 600
2 3 200
0 3 1
Output:
700
```
```
Input:
4 5
0 1 100
1 2 100
2 0 100
1 3 600
2 3 200
0 3 2
Output:
400
```
```
Input:
3 3
0 1 100
1 2 100
0 2 500
0 2 0
Output:
500
```

**Constraints and edge cases:** 1 ≤ n ≤ 100; 0 ≤ m ≤ n·(n-1)/2; 1 ≤ price ≤ 10^4; 0 ≤ k < n. A plain Dijkstra that ignores the stop limit gives the wrong answer; so does one that discards a costlier path with fewer stops.

### Question 3: Company-Style Programming
**Title:** Friend Circle Tracker

**Problem statement:** A social app starts with `n` users (numbered 1..n), none of whom are friends. Process commands:
- `FRIEND a b`: make `a` and `b` friends (friendship is transitive for circle purposes: a circle is a connected group). After the command print the current **number of circles** and the **size of the largest circle**.
- `SAME a b`: print `YES` if `a` and `b` are in the same circle, otherwise `NO`.

**Input and output requirements:** Line 1: `n q`. Then `q` commands. Output one line per command, formatted as described.

**Sample cases:**
```
Input:
5 6
FRIEND 1 2
FRIEND 3 4
SAME 1 3
FRIEND 2 3
SAME 1 4
FRIEND 1 4
Output:
4 2
3 2
NO
2 4
YES
2 4
```
```
Input:
1 2
FRIEND 1 1
SAME 1 1
Output:
1 1
YES
```

**Constraints and edge cases:** 1 ≤ n ≤ 200000; 1 ≤ q ≤ 300000. Befriending two people who are already in the same circle changes nothing but still prints a line. `a` may equal `b`. Adversarial input can make naive group-merging approaches too slow.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.Arrays;

public class Main {
    static int[] p = new int[8];
    static int[] size = new int[8];

    static int find(int x) {
        while (p[x] != x) {
            p[x] = p[p[x]];
            x = p[x];
        }
        return x;
    }

    static boolean union(int a, int b) {
        int ra = find(a);
        int rb = find(b);
        if (ra == rb) {
            return false;
        }
        if (size[ra] < size[rb]) {
            int t = ra;
            ra = rb;
            rb = t;
        }
        p[rb] = ra;
        size[ra] += size[rb];
        return true;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 8; i++) {
            p[i] = i;
            size[i] = 1;
        }
        int[][] ops = {{0, 1}, {2, 3}, {1, 3}, {4, 5}, {6, 7}, {5, 7}, {3, 7}, {0, 4}, {2, 6}};
        StringBuilder sb = new StringBuilder();
        for (int[] op : ops) {
            sb.append(union(op[0], op[1]) ? 'T' : 'F');
        }
        System.out.println(sb);
        System.out.println(Arrays.toString(p));
        System.out.println(Arrays.toString(size));
        System.out.println(find(6) + " " + find(5) + " " + find(1));
        System.out.println(Arrays.toString(p));
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Compute the shortest distance from `src` to every vertex in a directed graph with non-negative weights up to 10^9 and up to 100000 vertices, using Dijkstra's algorithm with a priority queue in O(E log V). Unreachable vertices must be reported as `-1`. For the 6-vertex graph with edges `0→1 (1000000000)`, `1→2 (1000000000)`, `2→3 (1000000000)`, `0→4 (5)` (vertex 5 is isolated), the distances from vertex 0 are `0 1000000000 2000000000 3000000000 5 -1`.

The program below prints wrong values for this input and would also be too slow on large graphs. Identify every bug and explain each, then write the corrected method.

```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class Main {
    static int[] dijkstra(int n, List<int[]>[] g, int src) {
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        pq.add(new int[]{src, 0});
        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int u = cur[0];
            for (int[] e : g[u]) {
                int v = e[0];
                int w = e[1];
                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    pq.add(new int[]{v, dist[v]});
                }
            }
        }
        return dist;
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        int n = 6;
        List<int[]>[] g = new List[n];
        for (int i = 0; i < n; i++) {
            g[i] = new ArrayList<>();
        }
        g[0].add(new int[]{1, 1000000000});
        g[1].add(new int[]{2, 1000000000});
        g[2].add(new int[]{3, 1000000000});
        g[0].add(new int[]{4, 5});
        StringBuilder sb = new StringBuilder();
        for (int d : dijkstra(n, g, 0)) {
            sb.append(d).append(' ');
        }
        System.out.println(sb.toString().trim());
    }
}
```

---

<a id="day15"></a>
## Day 15 — Dynamic Programming I: One-Dimensional

**Learning objectives:** Define a DP state and recurrence, handle circular constraints by splitting cases, speed up LIS with binary search, count decodings with wildcards under a modulus, track both maximum and minimum for products, and read memoized recursion for repeated work.

### Question 1: Mixed Real-World Scenario
**Title:** Festival Stall Allocation

**Problem statement:** A festival is held on a circular road with `n` stalls. Each stall `i` would earn the organizer `profit[i]` if opened. For safety, no two **adjacent** stalls may both be opened, and since the road is a circle, the first and last stalls are adjacent too. Find the maximum total profit.

**Real-world context:** Spacing rules (fire safety, noise, queue crowding) force organizers to skip neighboring stalls.

**Input requirements:** Line 1: `n`. Line 2: `n` profits.

**Expected output:** A single integer.

**Sample cases:**
```
Input:
3
2 3 2
Output:
3
```
```
Input:
4
1 2 3 1
Output:
4
```
```
Input:
1
5
Output:
5
```
```
Input:
4
4 1 1 4
Output:
5
```

**Constraints and edge cases:** 1 ≤ n ≤ 100000; 0 ≤ profit ≤ 10^9, so the total can exceed the `int` range. With a single stall there is no neighbor conflict. With two stalls, they are adjacent to each other.

### Question 2: Interview-Based Coding
**Title:** Longest Increasing Subsequence

**Problem statement:** Find the length of the longest **strictly increasing** subsequence of an integer array (elements need not be contiguous).

**Input and output requirements:** Line 1: `n`. Line 2: `n` integers. Output the length.

**Sample cases:**
```
Input:
8
10 9 2 5 3 7 101 18
Output:
4
```
```
Input:
6
0 1 0 3 2 3
Output:
4
```
```
Input:
3
7 7 7
Output:
1
```

**Constraints and edge cases:** 1 ≤ n ≤ 200000; values fit in `int`. The O(n²) approach is too slow. Equal values do not extend a strictly increasing subsequence.

### Question 3: Company-Style Programming
**Title:** Decode Ways With Wildcards

**Problem statement:** A message of digits and the wildcard `*` was encoded using `A=1, B=2, ..., Z=26`. A `*` stands for any digit from `1` to `9` (never `0`). Count the number of ways to decode the whole string into letters, modulo 1,000,000,007. A digit string may not be split so that a part has a leading zero (for example `06` cannot be decoded as `6`).

**Input and output requirements:** One line: the string. Output the number of ways modulo 1,000,000,007.

**Sample cases:**
```
Input:
*
Output:
9
```
```
Input:
1*
Output:
18
```
```
Input:
2*
Output:
15
```
```
Input:
06
Output:
0
```
```
Input:
**
Output:
96
```

**Constraints and edge cases:** 1 ≤ length ≤ 100000; characters are digits or `*`. The count grows exponentially, so reduce modulo at each step and use `long` for intermediate products. A leading `0` makes decoding impossible unless it is part of `10` or `20`.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
public class Main {
    static long[] memo = new long[60];
    static int calls = 0;

    static long f(int n) {
        calls++;
        if (n < 2) {
            return n;
        }
        if (memo[n] != 0) {
            return memo[n];
        }
        memo[n] = f(n - 1) + f(n - 2);
        return memo[n];
    }

    static int ways(int n, int[] steps) {
        calls++;
        if (n == 0) {
            return 1;
        }
        int total = 0;
        for (int s : steps) {
            if (n >= s) {
                total += ways(n - s, steps);
            }
        }
        return total;
    }

    public static void main(String[] args) {
        System.out.println(f(10) + " " + calls);
        calls = 0;
        System.out.println(f(12) + " " + calls);
        calls = 0;
        System.out.println(f(10) + " " + calls);

        calls = 0;
        System.out.println(ways(6, new int[]{1, 3, 4}) + " " + calls);

        int a = 1;
        int b = 1;
        for (int i = 3; i <= 47; i++) {
            int c = a + b;
            a = b;
            b = c;
        }
        System.out.println(b);
        long x = 1;
        long y = 1;
        for (int i = 3; i <= 47; i++) {
            long z = x + y;
            x = y;
            y = z;
        }
        System.out.println(y + " " + (int) y);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Return the maximum product of any non-empty contiguous subarray. The array can contain positive numbers, negative numbers, and zeros, and the result always fits in an `int`. For `[2,3,-2,4]` the answer is `6`; for `[-2,3,-4]` it is `24`; for `[-3]` it is `-3`; for `[0,2]` it is `2`; for `[-2,0,-1]` it is `0`.

The code below returns wrong answers for several of these inputs. Find every bug, explain which inputs fail and why, and correct the method.

```java
public class Main {
    static int maxProduct(int[] a) {
        int best = 0;
        int cur = 1;
        for (int x : a) {
            cur = Math.max(x, cur * x);
            best = Math.max(best, cur);
        }
        return best;
    }

    public static void main(String[] args) {
        System.out.println(maxProduct(new int[]{2, 3, -2, 4}));
        System.out.println(maxProduct(new int[]{-2, 3, -4}));
        System.out.println(maxProduct(new int[]{-3}));
        System.out.println(maxProduct(new int[]{0, 2}));
        System.out.println(maxProduct(new int[]{-2, 0, -1}));
    }
}
```

---

<a id="day16"></a>
## Day 16 — Dynamic Programming II: Grids and Sequences

**Learning objectives:** Fill 2-D tables for grid paths, count optimal solutions alongside their cost, reconstruct sequences (LCS) with tie-breaking rules, solve backward DP (dungeon), and avoid initialization and overflow errors in path counting.

### Question 1: Mixed Real-World Scenario
**Title:** Warehouse Robot Route Planner

**Problem statement:** A warehouse floor is a grid. Each cell has a traversal cost (a non-negative integer) or is blocked (`-1`). A robot starts at the top-left cell and must reach the bottom-right cell moving only **right** or **down**. The cost of a route is the sum of the costs of all cells visited, including the first and last. Report the minimum cost and the number of different minimum-cost routes, modulo 1,000,000,007. If the robot cannot reach the destination (including when the start or end is blocked), print `IMPOSSIBLE`.

**Real-world context:** Robots with a one-way travel rule (to avoid collisions) must be assigned the cheapest lane, and planners also want to know how many equally cheap lanes exist.

**Input requirements:** Line 1: `R C`. Then `R` lines with `C` integers.

**Expected output:** `<cost> <count>` or `IMPOSSIBLE`.

**Sample cases:**
```
Input:
3 3
1 3 1
1 5 1
4 2 1
Output:
7 1
```
```
Input:
3 3
1 1 1
1 1 1
1 1 1
Output:
5 6
```
```
Input:
2 2
1 -1
-1 1
Output:
IMPOSSIBLE
```

**Constraints and edge cases:** 1 ≤ R, C ≤ 1000; costs are in [0, 1000] or `-1`. Cells with cost 0 can create many equal-cost routes, so counting must be based on minimum-cost transitions only. A 1 × 1 grid has exactly one route (if its cell is not blocked).

### Question 2: Interview-Based Coding
**Title:** Smallest Longest Common Subsequence

**Problem statement:** Given two strings, print the length of their longest common subsequence and, among all common subsequences of that maximum length, the **lexicographically smallest** one.

**Input and output requirements:** Line 1: string `a`. Line 2: string `b`. Output two lines: the length, then the subsequence (an empty line if the length is 0).

**Sample cases:**
```
Input:
abcde
ace
Output:
3
ace
```
```
Input:
abc
def
Output:
0

```
```
Input:
bacd
abcd
Output:
3
acd
```
```
Input:
AGGTAB
GXTXAYB
Output:
4
GTAB
```

**Constraints and edge cases:** 1 ≤ |a|, |b| ≤ 3000; characters are letters (compare using normal character order). A table of 3001 × 3001 ints is acceptable. Reconstruction must follow the stated tie-breaking rule, not "any" valid answer.

### Question 3: Company-Style Programming
**Title:** Minimum Starting Health

**Problem statement:** A hero starts in the top-left cell of a dungeon grid and must reach the bottom-right cell where a princess waits, moving only right or down. Each cell has an integer: negative values are damage (lose that much health), positive values are potions (gain health). The hero's health must **stay at least 1 at all times**, including after the last cell's effect. Find the minimum initial health that makes the trip possible.

**Input and output requirements:** Line 1: `R C`. Then `R` lines with `C` integers. Output the minimum initial health.

**Sample cases:**
```
Input:
3 3
-2 -3 3
-5 -10 1
10 30 -5
Output:
7
```
```
Input:
1 1
0
Output:
1
```
```
Input:
1 1
-5
Output:
6
```
```
Input:
3 3
1 -3 3
0 -2 0
-3 -3 -3
Output:
3
```

**Constraints and edge cases:** 1 ≤ R, C ≤ 500; cell values are in [-1000, 1000]. The minimum is never below 1. Choosing the route with the best total is not always optimal because health may dip below 1 midway.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        String a = "abcb";
        String b = "bdcab";
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        for (int[] row : dp) {
            System.out.println(Arrays.toString(row));
        }

        StringBuilder sb = new StringBuilder();
        int i = a.length();
        int j = b.length();
        while (i > 0 && j > 0) {
            if (a.charAt(i - 1) == b.charAt(j - 1)) {
                sb.append(a.charAt(i - 1));
                i--;
                j--;
            } else if (dp[i - 1][j] >= dp[i][j - 1]) {
                i--;
            } else {
                j--;
            }
        }
        System.out.println(sb.reverse() + " " + dp[a.length()][b.length()]);

        long[][] paths = new long[4][4];
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                paths[r][c] = (r == 0 || c == 0) ? 1 : paths[r - 1][c] + paths[r][c - 1];
            }
        }
        System.out.println(paths[3][3] + " " + paths[2][3] + " " + paths[1][1]);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Count the number of distinct paths from the top-left to the bottom-right of a grid moving only right or down, where `1` marks an obstacle and `0` an open cell. Paths cannot pass through obstacles; if the start or the end is an obstacle the answer is `0`. The count can exceed the `int` range (an open 18 × 18 grid has 2333606220 paths). Expected results: a 3 × 3 grid with an obstacle in the center gives `2`; the grid `{{0,1},{0,0}}` gives `1`; the grid `{{0,0},{1,0}}` gives `1`; `{{1,0}}` gives `0`.

The code below produces wrong answers on several grids. Find every bug, explain which inputs expose each, and correct the method.

```java
public class Main {
    static int uniquePaths(int[][] g) {
        int m = g.length;
        int n = g[0].length;
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            dp[i][0] = 1;
        }
        for (int j = 0; j < n; j++) {
            dp[0][j] = 1;
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (g[i][j] == 1) {
                    dp[i][j] = 0;
                } else {
                    dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
                }
            }
        }
        return dp[m - 1][n - 1];
    }

    public static void main(String[] args) {
        System.out.println(uniquePaths(new int[][]{{0, 0, 0}, {0, 1, 0}, {0, 0, 0}}));
        System.out.println(uniquePaths(new int[][]{{0, 1}, {0, 0}}));
        System.out.println(uniquePaths(new int[][]{{0, 0}, {1, 0}}));
        System.out.println(uniquePaths(new int[][]{{1, 0}}));
        System.out.println(uniquePaths(new int[18][18]));
    }
}
```

---

<a id="day17"></a>
## Day 17 — Dynamic Programming III: Knapsack and Edit Distance

**Learning objectives:** Model 0/1 and unbounded knapsack, reconstruct chosen items with a lexicographic tie-break, compute edit distance, count combinations versus minimizing items, and understand why loop direction in 1-D DP matters.

### Question 1: Mixed Real-World Scenario
**Title:** Drone Payload Optimizer

**Problem statement:** A delivery drone can carry at most `W` kilograms. There are `n` packages; package `i` (numbered from 1) has weight `w[i]` and delivery value `v[i]`. Each package can be taken at most once. Maximize the total value. If several selections reach the maximum value, choose the one whose **sorted list of package numbers is lexicographically smallest** (compare element by element from the smallest number; a list that is a proper prefix of another is smaller).

**Real-world context:** Logistics planners must reproduce the same payload decision every time for audits, so ties must be resolved by a fixed rule.

**Input requirements:** Line 1: `n W`. Next `n` lines: `w v`.

**Expected output:** Line 1: the maximum value. Line 2: `Items: ` followed by the chosen package numbers in ascending order, or `Items: none` if none are chosen.

**Sample cases:**
```
Input:
4 7
1 1
3 4
4 5
5 7
Output:
9
Items: 2 3
```
```
Input:
4 4
2 3
2 3
3 4
1 1
Output:
6
Items: 1 2
```
```
Input:
3 5
2 5
3 5
5 10
Output:
10
Items: 1 2
```
```
Input:
1 4
5 10
Output:
0
Items: none
```

**Constraints and edge cases:** 1 ≤ n ≤ 100; 1 ≤ W ≤ 10000; 1 ≤ w[i] ≤ 10000; 1 ≤ v[i] ≤ 10^6. A package heavier than `W` can never be chosen. Maximum value fits in `int`, but be careful when storing the reconstruction table.

### Question 2: Interview-Based Coding
**Title:** Edit Distance

**Problem statement:** Given two strings `a` and `b`, compute the minimum number of single-character operations (insert a character, delete a character, or replace a character) needed to turn `a` into `b`.

**Input and output requirements:** Line 1: string `a`. Line 2: string `b`. Either line may be empty. Output the minimum number of operations.

**Sample cases:**
```
Input:
horse
ros
Output:
3
```
```
Input:
intention
execution
Output:
5
```
```
Input:

abc
Output:
3
```
```
Input:
abc
abc
Output:
0
```

**Constraints and edge cases:** 0 ≤ |a|, |b| ≤ 2000; lowercase letters. An empty string needs exactly as many insertions as the length of the other string. Read lines carefully, because an empty line is a valid input.

### Question 3: Company-Style Programming
**Title:** Change-Making Report

**Problem statement:** Given `k` coin denominations (unlimited supply of each) and a target `amount`, report two numbers: (1) the number of **distinct combinations** of coins that sum to `amount`, where the order of coins does not matter, modulo 1,000,000,007; and (2) the **minimum number of coins** needed, or `-1` if the amount cannot be formed. The amount `0` has exactly one combination (the empty one) and needs 0 coins.

**Input and output requirements:** Line 1: `amount k`. Line 2: `k` coin values. Output `<combinations> <minCoins>`.

**Sample cases:**
```
Input:
5 3
1 2 5
Output:
4 1
```
```
Input:
3 1
2
Output:
0 -1
```
```
Input:
11 3
1 2 5
Output:
11 3
```
```
Input:
0 1
3
Output:
1 0
```

**Constraints and edge cases:** 0 ≤ amount ≤ 100000; 1 ≤ k ≤ 100; coin values are distinct integers in [1, 100000]. Counting ordered sequences instead of combinations gives different (larger) numbers.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[] w = {2, 3};
        int[] v = {4, 5};
        int cap = 7;

        int[] zeroOne = new int[cap + 1];
        for (int i = 0; i < w.length; i++) {
            for (int c = cap; c >= w[i]; c--) {
                zeroOne[c] = Math.max(zeroOne[c], zeroOne[c - w[i]] + v[i]);
            }
        }
        System.out.println(Arrays.toString(zeroOne));

        int[] unbounded = new int[cap + 1];
        for (int i = 0; i < w.length; i++) {
            for (int c = w[i]; c <= cap; c++) {
                unbounded[c] = Math.max(unbounded[c], unbounded[c - w[i]] + v[i]);
            }
        }
        System.out.println(Arrays.toString(unbounded));

        int[] reused = new int[cap + 1];
        for (int i = 0; i < w.length; i++) {
            for (int c = w[i]; c <= cap; c++) {
                reused[c] = Math.max(reused[c], reused[c - w[i]] + v[i]);
            }
        }
        System.out.println(reused[cap] == zeroOne[cap]);

        int[] ways = new int[8];
        ways[0] = 1;
        for (int coin : new int[]{1, 2, 3}) {
            for (int s = coin; s <= 7; s++) {
                ways[s] += ways[s - coin];
            }
        }
        System.out.println(Arrays.toString(ways));

        int[] ordered = new int[8];
        ordered[0] = 1;
        for (int s = 1; s <= 7; s++) {
            for (int coin : new int[]{1, 2, 3}) {
                if (s >= coin) {
                    ordered[s] += ordered[s - coin];
                }
            }
        }
        System.out.println(Arrays.toString(ordered));
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Compute the edit distance (minimum number of insert, delete, and replace operations) between two strings. Expected: `editDistance("horse","ros")` is `3`; `editDistance("intention","execution")` is `5`; `editDistance("","abc")` is `3`; `editDistance("abc","abc")` is `0`; `editDistance("sunday","saturday")` is `3`.

The program below crashes or returns wrong numbers. Find every bug, explain what each one causes, and write the corrected method.

```java
public class Main {
    static int editDistance(String a, String b) {
        int m = a.length();
        int n = b.length();
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (a.charAt(i) == b.charAt(j)) {
                    dp[i][j] = dp[i - 1][j];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
                }
            }
        }
        return dp[m][n];
    }

    public static void main(String[] args) {
        System.out.println(editDistance("horse", "ros"));
        System.out.println(editDistance("intention", "execution"));
        System.out.println(editDistance("", "abc"));
        System.out.println(editDistance("abc", "abc"));
        System.out.println(editDistance("sunday", "saturday"));
    }
}
```

---

<a id="day18"></a>
## Day 18 — Backtracking

**Learning objectives:** Write recursive search with choose/explore/undo, prune with sorted input and early exits, skip duplicates correctly, enumerate partitions, count constrained walks, and avoid aliasing bugs when storing results.

### Question 1: Mixed Real-World Scenario
**Title:** Lunch Combo Builder

**Problem statement:** A canteen has `n` items with given prices (prices may repeat, and equal prices are interchangeable). A customer wants to spend **exactly** `B`, using each item at most once. List every distinct combination of prices that sums to `B`. Print each combination with its prices in ascending order, and print the combinations in lexicographic order (comparing element by element; a shorter combination that is a prefix of another comes first). Print `NONE` if there is no combination.

**Real-world context:** Gift cards and meal vouchers must be spent exactly, so the canteen suggests every exact-fit bundle.

**Input requirements:** Line 1: `n B`. Line 2: `n` prices.

**Expected output:** One combination per line (prices separated by spaces), or `NONE`.

**Sample cases:**
```
Input:
7 8
10 1 2 7 6 1 5
Output:
1 1 6
1 2 5
1 7
2 6
```
```
Input:
2 1
2 3
Output:
NONE
```
```
Input:
5 5
2 5 2 1 2
Output:
1 2 2
5
```

**Constraints and edge cases:** 1 ≤ n ≤ 20; 1 ≤ price ≤ 50; 1 ≤ B ≤ 200. Duplicate prices must not produce duplicate combinations (the combination `1 2 5` appears once even if two items cost 1).

### Question 2: Interview-Based Coding
**Title:** Palindrome Partitions

**Problem statement:** Partition a string into pieces so that every piece is a palindrome. Print every possible partition. Generate partitions so that, for each position, shorter first pieces come before longer first pieces (the natural left-to-right recursive order). Print the pieces of each partition separated by single spaces.

**Input and output requirements:** One line: the string. Output one partition per line.

**Sample cases:**
```
Input:
aab
Output:
a a b
aa b
```
```
Input:
aaa
Output:
a a a
a aa
aa a
aaa
```
```
Input:
abca
Output:
a b c a
```

**Constraints and edge cases:** 1 ≤ length ≤ 16; lowercase letters. A single character is always a palindrome, so at least one partition always exists. A string like `aaaaaaaaaaaaaaaa` has 32768 partitions.

### Question 3: Company-Style Programming
**Title:** Inspection Tour Counter

**Problem statement:** An inspection robot moves on a rectangular floor plan. Cell `1` is the start, `2` is the end, `0` is a free cell, and `-1` is an obstacle. The robot moves up, down, left, or right, and must visit **every free cell exactly once** while going from the start to the end (the start and end cells are each visited once, and the tour ends the moment the robot reaches the end cell, which must be after all free cells were visited). Count the number of different tours.

**Input and output requirements:** Line 1: `m n`. Then `m` lines of `n` integers. Output the number of valid tours.

**Sample cases:**
```
Input:
3 4
1 0 0 0
0 0 0 0
0 0 2 -1
Output:
2
```
```
Input:
3 4
1 0 0 0
0 0 0 0
0 0 0 2
Output:
4
```
```
Input:
2 2
0 1
2 0
Output:
0
```

**Constraints and edge cases:** 1 ≤ m · n ≤ 20. Exactly one start and one end exist. Reaching the end cell before all free cells are visited does not count and the robot may not continue through the end cell. A fresh visited marker must be restored on backtracking.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.ArrayList;
import java.util.List;

public class Main {
    static int calls = 0;

    static void search(int[] a, int idx, int sum, int target, String cur, List<String> out) {
        calls++;
        if (sum == target) {
            out.add(cur.trim());
            return;
        }
        if (sum > target) {
            return;
        }
        for (int i = idx; i < a.length; i++) {
            search(a, i + 1, sum + a[i], target, cur + " " + a[i], out);
        }
    }

    static int perms = 0;

    static void permute(char[] c, int k, StringBuilder sb) {
        if (k == c.length) {
            perms++;
            if (perms <= 4) {
                sb.append(new String(c)).append(' ');
            }
            return;
        }
        for (int i = k; i < c.length; i++) {
            char t = c[k];
            c[k] = c[i];
            c[i] = t;
            permute(c, k + 1, sb);
            t = c[k];
            c[k] = c[i];
            c[i] = t;
        }
    }

    public static void main(String[] args) {
        List<String> out = new ArrayList<>();
        search(new int[]{1, 2, 3, 4, 5}, 0, 0, 6, "", out);
        System.out.println(out);
        System.out.println(calls);

        StringBuilder sb = new StringBuilder();
        permute("abc".toCharArray(), 0, sb);
        System.out.println(sb.toString().trim() + " " + perms);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Return all distinct subsets of an integer array that may contain duplicates (the power set without repeated subsets). The input is not necessarily sorted. For `[2,1,2]` the result has exactly 6 subsets: `[]`, `[1]`, `[1,2]`, `[1,2,2]`, `[2]`, `[2,2]` (in the order produced by the standard recursive generation after sorting).

The program prints wrong results (empty lists and missing or repeated subsets). Find every bug, explain each, and rewrite the method.

```java
import java.util.ArrayList;
import java.util.List;

public class Main {
    static void backtrack(int[] a, int start, List<Integer> cur, List<List<Integer>> res) {
        res.add(cur);
        for (int i = start; i < a.length; i++) {
            if (i > 0 && a[i] == a[i - 1]) {
                continue;
            }
            cur.add(a[i]);
            backtrack(a, i + 1, cur, res);
            cur.remove(cur.size() - 1);
        }
    }

    static List<List<Integer>> subsetsWithDup(int[] a) {
        List<List<Integer>> res = new ArrayList<>();
        backtrack(a, 0, new ArrayList<>(), res);
        return res;
    }

    public static void main(String[] args) {
        System.out.println(subsetsWithDup(new int[]{2, 1, 2}));
    }
}
```

---

<a id="day19"></a>
## Day 19 — Greedy Algorithms and Intervals

**Learning objectives:** Prove-by-example when a greedy choice is safe, use sweep lines over interval endpoints, select maximum non-overlapping sets, build lexicographically smallest outputs with feasibility checks, and merge intervals without off-by-one mistakes.

### Question 1: Mixed Real-World Scenario
**Title:** Classroom Capacity Planner

**Problem statement:** A school has `n` classes, each occupying the half-open time interval `[start, end)`. Two classes can share a room only if one ends no later than the other starts (a class ending at time 10 and one starting at 10 may share a room). Compute the minimum number of rooms needed, and the **earliest time** at which the maximum number of simultaneous classes is reached.

**Real-world context:** Timetable planners size the building for the worst moment of the day and want to know when that moment first happens.

**Input requirements:** Line 1: `n`. Then `n` lines `start end`.

**Expected output:** Two lines: `Rooms: <r>` and `Peak at: <t>`.

**Sample cases:**
```
Input:
3
0 30
5 10
15 20
Output:
Rooms: 2
Peak at: 5
```
```
Input:
2
1 2
2 3
Output:
Rooms: 1
Peak at: 1
```
```
Input:
4
1 4
2 5
3 6
2 3
Output:
Rooms: 3
Peak at: 2
```

**Constraints and edge cases:** 1 ≤ n ≤ 200000; 0 ≤ start < end ≤ 10^9. Many classes may start or end at the same time; an ending class must be processed before a starting class at the same instant. Comparing every pair is too slow.

### Question 2: Interview-Based Coding
**Title:** Minimum Removals for Non-Overlap

**Problem statement:** Given `n` intervals `[start, end]`, find the minimum number of intervals to remove so that the remaining intervals do not overlap. Intervals that merely touch at an endpoint (like `[1,2]` and `[2,3]`) do **not** overlap.

**Input and output requirements:** Line 1: `n`. Then `n` lines `start end`. Output the minimum number of removals.

**Sample cases:**
```
Input:
4
1 2
2 3
3 4
1 3
Output:
1
```
```
Input:
3
1 2
1 2
1 2
Output:
2
```
```
Input:
2
1 2
2 3
Output:
0
```
```
Input:
4
1 100
11 22
1 11
2 12
Output:
2
```

**Constraints and edge cases:** 1 ≤ n ≤ 100000; -10^9 ≤ start < end ≤ 10^9. Sorting with `a[1] - b[1]` can overflow for values near the limits.

### Question 3: Company-Style Programming
**Title:** Smallest Spaced-Out Arrangement

**Problem statement:** Rearrange the letters of a string so that no two identical letters are adjacent. Among all valid rearrangements, print the **lexicographically smallest** one. If no valid rearrangement exists, print `IMPOSSIBLE`.

**Input and output requirements:** One line: a string of lowercase letters. Output the arrangement or `IMPOSSIBLE`.

**Sample cases:**
```
Input:
aab
Output:
aba
```
```
Input:
aaab
Output:
IMPOSSIBLE
```
```
Input:
bbaac
Output:
ababc
```
```
Input:
a
Output:
a
```

**Constraints and edge cases:** 1 ≤ length ≤ 100000. Trying all permutations is impossible at this size.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        int[][] iv = {{5, 6}, {1, 3}, {2, 4}, {3, 5}, {8, 10}, {9, 9}, {1, 2}};
        int[][] byStart = iv.clone();
        Arrays.sort(byStart, (x, y) -> Integer.compare(x[0], y[0]));
        StringBuilder sb = new StringBuilder();
        for (int[] p : byStart) {
            sb.append(Arrays.toString(p));
        }
        System.out.println(sb);

        List<int[]> merged = new ArrayList<>();
        for (int[] p : byStart) {
            if (merged.isEmpty() || merged.get(merged.size() - 1)[1] < p[0]) {
                merged.add(new int[]{p[0], p[1]});
            } else {
                int[] last = merged.get(merged.size() - 1);
                last[1] = Math.max(last[1], p[1]);
            }
        }
        sb.setLength(0);
        for (int[] p : merged) {
            sb.append(Arrays.toString(p));
        }
        System.out.println(sb + " " + merged.size());

        int[][] byEnd = iv.clone();
        Arrays.sort(byEnd, Comparator.comparingInt((int[] x) -> x[1]).thenComparingInt(x -> x[0]));
        int count = 0;
        int end = Integer.MIN_VALUE;
        sb.setLength(0);
        for (int[] p : byEnd) {
            if (p[0] >= end) {
                count++;
                end = p[1];
                sb.append(Arrays.toString(p));
            }
        }
        System.out.println(sb + " " + count);
        System.out.println(Arrays.toString(iv[0]) + " " + Arrays.toString(byStart[0]));
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Merge all overlapping intervals and return the merged list sorted by start. Intervals that touch at an endpoint (such as `[1,4]` and `[4,5]`) must be merged. The input is not necessarily sorted. For `[[1,3],[8,10],[2,6],[15,18],[17,20]]` the result is `[1,6] [8,10] [15,20]`; for `[[1,4],[4,5]]` it is `[1,5]`; for `[[1,10],[2,3]]` it is `[1,10]`.

The code gives wrong results on all three examples. Find every bug, explain the effect of each, and correct the method.

```java
import java.util.ArrayList;
import java.util.List;

public class Main {
    static List<int[]> merge(int[][] intervals) {
        List<int[]> res = new ArrayList<>();
        int[] cur = intervals[0];
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] < cur[1]) {
                cur[1] = intervals[i][1];
            } else {
                res.add(cur);
                cur = intervals[i];
            }
        }
        return res;
    }

    public static void main(String[] args) {
        int[][][] tests = {
            {{1, 3}, {8, 10}, {2, 6}, {15, 18}, {17, 20}},
            {{1, 4}, {4, 5}},
            {{1, 10}, {2, 3}}
        };
        for (int[][] t : tests) {
            StringBuilder sb = new StringBuilder();
            for (int[] p : merge(t)) {
                sb.append('[').append(p[0]).append(',').append(p[1]).append("] ");
            }
            System.out.println(sb.toString().trim());
        }
    }
}
```

---

<a id="day20"></a>
## Day 20 — Binary Search on Answer and Bit Manipulation

**Learning objectives:** Binary search over a monotonic answer space with a feasibility check, partition-based binary search on two sorted arrays, use XOR and lowest-set-bit tricks, understand shift semantics and overflow for `int` and `long`, and avoid infinite loops in binary search.

### Question 1: Mixed Real-World Scenario
**Title:** Minimum Ship Capacity

**Problem statement:** A port must ship `n` packages in the given order (packages cannot be reordered or split). Each day the ship loads packages in order, never exceeding its capacity, and then sails. Find the smallest ship capacity that delivers all packages within `D` days.

**Real-world context:** Choosing the smallest vessel that meets a delivery deadline reduces chartering cost.

**Input requirements:** Line 1: `n D`. Line 2: `n` package weights.

**Expected output:** A single integer, the minimum capacity.

**Sample cases:**
```
Input:
10 5
1 2 3 4 5 6 7 8 9 10
Output:
15
```
```
Input:
6 3
3 2 2 4 1 4
Output:
6
```
```
Input:
5 4
1 2 3 1 1
Output:
3
```
```
Input:
4 2
1000000000 1000000000 1000000000 1000000000
Output:
2000000000
```

**Constraints and edge cases:** 1 ≤ D ≤ n ≤ 200000; 1 ≤ weight ≤ 10^9. The sum of weights can reach 2·10^14, which does not fit in an `int`. The capacity can never be smaller than the heaviest package.

### Question 2: Interview-Based Coding
**Title:** Median of Two Sorted Arrays

**Problem statement:** Given two sorted arrays of integers, find the median of the combined multiset in O(log(min(m, n))) time. For an even total count, the median is the average of the two middle values. Print the median with exactly one decimal digit.

**Input and output requirements:** Line 1: `m n`. Line 2: the `m` values of the first array (an empty line if `m = 0`). Line 3: the `n` values of the second array (an empty line if `n = 0`). Output the median.

**Sample cases:**
```
Input:
2 1
1 3
2
Output:
2.0
```
```
Input:
2 2
1 2
3 4
Output:
2.5
```
```
Input:
0 1

1
Output:
1.0
```
```
Input:
2 1
-5 3
-2
Output:
-2.0
```

**Constraints and edge cases:** 0 ≤ m, n ≤ 10^6 with m + n ≥ 1; values fit in `int`, so the sum of two values may overflow `int`. Merging the arrays gives O(m + n), which is not the required complexity.

### Question 3: Company-Style Programming
**Title:** The Two Lonely Numbers

**Problem statement:** In an array of `n` integers, every value appears exactly twice except for two distinct values that each appear exactly once. Find those two values and print them in ascending order. Use O(n) time and O(1) extra space; do not use a hash-based structure or sorting.

**Input and output requirements:** Line 1: `n`. Line 2: `n` integers. Output the two values separated by a space, smaller first.

**Sample cases:**
```
Input:
6
1 2 1 3 2 5
Output:
3 5
```
```
Input:
2
0 1
Output:
0 1
```
```
Input:
4
0 7 -2147483648 7
Output:
-2147483648 0
```

**Constraints and edge cases:** 2 ≤ n ≤ 200000 (n is even); values fit in `int` and can be negative. `Integer.MIN_VALUE` may be one of the values, which affects any bit-isolation trick that negates a value.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
public class Main {
    public static void main(String[] args) {
        int a = -17;
        System.out.println(a >> 2);
        System.out.println(a >>> 28);
        System.out.println(a << 30);
        System.out.println((1 << 33) + " " + (1L << 33));
        System.out.println(Integer.bitCount(a) + " " + Integer.toBinaryString(12 & -12));

        byte b = (byte) 200;
        System.out.println(b + " " + (b >> 1) + " " + (b >>> 1) + " " + (b & 0xFF));

        int n = 0b101101;
        int steps = 0;
        while (n != 0) {
            n &= n - 1;
            steps++;
        }
        System.out.println(steps);

        System.out.println(~7 + " " + (-8 ^ 7) + " " + (5 ^ 5 ^ 9));
        System.out.println(Integer.MIN_VALUE == -Integer.MIN_VALUE);
        System.out.println(Math.abs(Integer.MIN_VALUE) + " " + (Integer.MAX_VALUE + 1));

        char c = 'a';
        c += 2;
        c ^= ' ';
        System.out.println(c + " " + (c + 1) + " " + (char) (c + 1));
        long big = 1L << 62;
        System.out.println(big + big + " " + (big << 1) + " " + (big >>> 61));
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Given an array of positive integers and a threshold, find the **smallest** divisor `d >= 1` such that the sum of `ceil(a[i] / d)` over all elements is at most `threshold`. Values are up to 10^9, the array can have 50000 elements, and a valid divisor always exists (the maximum element). Expected: `{1,2,5,9}` with threshold `6` gives `5`; `{44,22,33,11,1}` with threshold `5` gives `44`; five copies of `1000000000` with threshold `5` gives `1000000000`.

The code below loops forever, overflows, or returns wrong values. Find every bug, explain each, and correct the program.

```java
public class Main {
    static int smallestDivisor(int[] a, int threshold) {
        int lo = 1;
        int hi = 0;
        for (int x : a) {
            hi = Math.max(hi, x);
        }
        while (lo <= hi) {
            int mid = (lo + hi) / 2;
            int sum = 0;
            for (int x : a) {
                sum += x / mid;
            }
            if (sum <= threshold) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }
        return lo;
    }

    public static void main(String[] args) {
        System.out.println(smallestDivisor(new int[]{1, 2, 5, 9}, 6));
        System.out.println(smallestDivisor(new int[]{44, 22, 33, 11, 1}, 5));
        System.out.println(smallestDivisor(new int[]{1000000000, 1000000000, 1000000000, 1000000000, 1000000000}, 5));
    }
}
```

---

<a id="day21"></a>
## Day 21 — Advanced Strings: KMP, Rolling Hash, and Windows

**Learning objectives:** Search patterns in linear time with the KMP prefix function, use sliding windows with frequency counters, combine binary search with rolling hashes, understand modular hashing and collision verification, and trace string comparison behavior.

### Question 1: Mixed Real-World Scenario
**Title:** Log Signature Scanner

**Problem statement:** A security tool scans a long log line for a known attack signature. Report how many times the signature occurs in the log (occurrences may overlap) and the starting index of each one, in ascending order.

**Real-world context:** Intrusion detection must scan megabytes of logs, so the scan time cannot grow with the product of log length and signature length.

**Input requirements:** Line 1: the log text (may contain spaces). Line 2: the signature (may contain spaces).

**Expected output:** `Count: <c>` on the first line, then `Positions: <indices separated by spaces>` or `Positions: none` if there are no occurrences.

**Sample cases:**
```
Input:
ababababa
aba
Output:
Count: 4
Positions: 0 2 4 6
```
```
Input:
hello
xyz
Output:
Count: 0
Positions: none
```
```
Input:
aaaa
aa
Output:
Count: 3
Positions: 0 1 2
```

**Constraints and edge cases:** 1 ≤ |log| ≤ 1000000; 1 ≤ |signature| ≤ 500000. A signature longer than the log has no occurrences. The input `aaaa...a` (500000 characters of `a` followed by `b` in the signature) is designed to break naive matching.

### Question 2: Interview-Based Coding
**Title:** Minimum Window Substring

**Problem statement:** Given strings `s` and `t`, find the shortest substring of `s` that contains every character of `t`, **including duplicates** (if `t` has two `a`'s, the window needs at least two `a`'s). If several windows have the minimum length, print the one that starts earliest. If no window exists, print `NONE`.

**Input and output requirements:** Line 1: `s`. Line 2: `t`. Output the window or `NONE`.

**Sample cases:**
```
Input:
ADOBECODEBANC
ABC
Output:
BANC
```
```
Input:
a
aa
Output:
NONE
```
```
Input:
abcabdebac
cda
Output:
cabd
```

**Constraints and edge cases:** 1 ≤ |s|, |t| ≤ 200000; characters are ASCII letters (case-sensitive). `t` can be longer than `s`. Checking every substring is too slow.

### Question 3: Company-Style Programming
**Title:** Longest Repeated Substring Length

**Problem statement:** Given a string, find the length of the longest substring that occurs at least twice. The two occurrences may overlap. If no substring repeats, print `0`.

**Input and output requirements:** One line: the string. Output the length.

**Sample cases:**
```
Input:
banana
Output:
3
```
```
Input:
abcd
Output:
0
```
```
Input:
aaaaa
Output:
4
```
```
Input:
abcabcabc
Output:
6
```

**Constraints and edge cases:** 1 ≤ length ≤ 100000; lowercase letters. Comparing all substring pairs is far too slow.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.Arrays;

public class Main {
    static int[] prefix(String p) {
        int[] lps = new int[p.length()];
        int k = 0;
        for (int i = 1; i < p.length(); i++) {
            while (k > 0 && p.charAt(i) != p.charAt(k)) {
                k = lps[k - 1];
            }
            if (p.charAt(i) == p.charAt(k)) {
                k++;
            }
            lps[i] = k;
        }
        return lps;
    }

    public static void main(String[] args) {
        String p = "abacabab";
        System.out.println(Arrays.toString(prefix(p)));

        String text = "abacabababacabab";
        int[] lps = prefix(p);
        int k = 0;
        StringBuilder hits = new StringBuilder();
        int comparisons = 0;
        for (int i = 0; i < text.length(); i++) {
            while (k > 0 && text.charAt(i) != p.charAt(k)) {
                k = lps[k - 1];
                comparisons++;
            }
            comparisons++;
            if (text.charAt(i) == p.charAt(k)) {
                k++;
            }
            if (k == p.length()) {
                hits.append(i - k + 1).append(' ');
                k = lps[k - 1];
            }
        }
        System.out.println(hits.toString().trim() + " | " + comparisons);

        String s1 = "ab";
        String s2 = "a" + "b";
        String part = "a";
        String s3 = part + "b";
        System.out.println((s1 == s2) + " " + (s1 == s3) + " " + s1.equals(s3) + " " + (s1 == s3.intern()));
        System.out.println("apple".compareTo("apricot") + " " + "b".compareTo("a") + " " + "ab".compareTo("abcd"));
        System.out.println(Arrays.toString(prefix("aaaa")) + " " + Arrays.toString(prefix("abcd")));
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Find the index of the first occurrence of `pat` in `text` using a Rabin-Karp rolling hash, returning `-1` if there is none. The hash uses base 256 and modulus 101. For `indexOf("hello world","world")` the answer is `6`; `indexOf("abcabcabd","abd")` is `6`; `indexOf("aaaaa","ab")` is `-1`; `indexOf("abc","abcd")` is `-1`; `indexOf("xyz","")` is `0`.

The program returns wrong results for several calls (and can also report matches that are not real). Identify every bug (consider modular arithmetic and hash collisions), explain each, and write the corrected method.

```java
public class Main {
    static int indexOf(String text, String pat) {
        int n = text.length();
        int m = pat.length();
        if (m > n) {
            return -1;
        }
        final int base = 256;
        final int mod = 101;
        long hp = 0;
        long ht = 0;
        long pow = 1;
        for (int i = 0; i < m; i++) {
            hp = (hp * base + pat.charAt(i)) % mod;
            ht = (ht * base + text.charAt(i)) % mod;
            pow = pow * base % mod;
        }
        for (int i = 0; i + m <= n; i++) {
            if (hp == ht) {
                return i;
            }
            if (i + m < n) {
                ht = (ht - text.charAt(i) * pow) * base + text.charAt(i + m);
                ht %= mod;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        System.out.println(indexOf("hello world", "world"));
        System.out.println(indexOf("abcabcabd", "abd"));
        System.out.println(indexOf("aaaaa", "ab"));
        System.out.println(indexOf("abc", "abcd"));
        System.out.println(indexOf("xyz", ""));
    }
}
```

---

<a id="day22"></a>
## Day 22 — Generics and Bounded Types

**Learning objectives:** Write generic classes and methods with bounded type parameters, apply the "producer extends, consumer super" rule with wildcards, understand type erasure and heap pollution, use type tokens for type-safe heterogeneous containers, and read overload resolution with generics.

### Question 1: Mixed Real-World Scenario
**Title:** Universal Leaderboard

**Problem statement:** A game platform keeps leaderboards of different value types: integer scores (`INT`), player names ranked alphabetically (`STR`), and decimal ratings (`DBL`). Implement **one** generic class `TopK<T extends Comparable<? super T>>` that keeps only the `k` largest values seen so far (using memory proportional to `k`) and use it for all three modes. After reading all values, print the retained values from largest to smallest.

**Real-world context:** A single reusable leaderboard component avoids three near-identical copies of the same logic.

**Input requirements:** Line 1: `MODE k` where `MODE` is `INT`, `STR`, or `DBL`. Line 2: `n`. Line 3: `n` values (strings contain no spaces).

**Expected output:** One line: the retained values from largest to smallest, separated by spaces. Doubles are printed with `Double.toString`.

**Sample cases:**
```
Input:
INT 3
5
5 1 9 7 3
Output:
9 7 5
```
```
Input:
STR 2
4
pear apple zebra mango
Output:
zebra pear
```
```
Input:
DBL 2
3
1.5 2.25 -1.0
Output:
2.25 1.5
```
```
Input:
INT 5
2
4 4
Output:
4 4
```

**Constraints and edge cases:** 1 ≤ k ≤ 100000; 1 ≤ n ≤ 200000. Duplicates are kept as separate entries. If `k > n`, print all values. Sorting all `n` values and slicing is not allowed because it uses O(n) memory.

### Question 2: Interview-Based Coding
**Title:** Generic Binary Search With Insertion Point

**Problem statement:** Write a generic method `static <T extends Comparable<? super T>> int search(List<? extends T> sorted, T key)` that behaves like `Collections.binarySearch` but, when the list contains duplicates, returns the **leftmost** index of the key. If the key is absent, return `-(insertionPoint) - 1`, where the insertion point is the index at which the key would be inserted to keep the list sorted. Use the method to answer queries for `INT` and `STR` lists.

**Input and output requirements:** Line 1: `MODE` (`INT` or `STR`). Line 2: `n`. Line 3: `n` values in ascending order. Line 4: `q`. Line 5: `q` query values. Output the `q` results separated by spaces on one line.

**Sample cases:**
```
Input:
INT
4
1 3 5 7
4
5 4 0 9
Output:
2 -3 -1 -5
```
```
Input:
STR
3
apple banana cherry
3
banana blueberry zzz
Output:
1 -3 -4
```
```
Input:
INT
5
2 2 2 3 3
2
2 3
Output:
0 3
```

**Constraints and edge cases:** 1 ≤ n, q ≤ 200000. Values fit in `int` (INT mode). The method must be O(log n) per query and must not copy the list. Use `Integer.compare`-style comparisons through `compareTo`, not subtraction.

### Question 3: Company-Style Programming
**Title:** Type-Safe Heterogeneous Registry

**Problem statement:** Build a registry that stores **one value per type**, keyed by the value's class, using a type-safe API of the form `<T> void put(Class<T> type, T value)` and `<T> T get(Class<T> type)`. The registry must reject at compile time a call like `put(Integer.class, "text")`. Support the types `Integer`, `Double`, and `String`. Commands:
- `PUT Type value`: store the value. Print `OK` if the type was empty, or `REPLACED <old value>` if a value was already stored.
- `GET Type`: print the stored value or `MISSING`.
- `REMOVE Type`: print the removed value or `MISSING`.

**Input and output requirements:** Line 1: `q`. Then `q` commands. For `String`, the value is the rest of the line (it may contain spaces). Values are printed using their normal `toString()` (so `Double` 3 prints as `3.0`).

**Sample cases:**
```
Input:
9
PUT Integer 5
PUT String hello
GET Integer
GET Double
PUT Integer 7
GET Integer
REMOVE String
GET String
PUT Double 2.50
Output:
OK
OK
5
MISSING
REPLACED 5
7
hello
MISSING
OK
```
```
Input:
4
PUT Double 3
GET Double
PUT String hello world
GET String
Output:
OK
3.0
OK
hello world
```

**Constraints and edge cases:** 1 ≤ q ≤ 100000; integers fit in `int`. The only unchecked-looking operation allowed is a call to `Class.cast`, so the container's public methods must not need `@SuppressWarnings("unchecked")`.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.ArrayList;
import java.util.List;

public class Main {
    static <T extends Comparable<T>> T max(T a, T b) {
        return a.compareTo(b) >= 0 ? a : b;
    }

    static double sum(List<? extends Number> l) {
        double s = 0;
        for (Number n : l) {
            s += n.doubleValue();
        }
        return s;
    }

    static void fill(List<? super Integer> l, int n) {
        for (int i = 0; i < n; i++) {
            l.add(i);
        }
    }

    static String kind(Object o) {
        return "object";
    }

    static String kind(String s) {
        return "string";
    }

    static <T> String kind2(T t) {
        return "generic";
    }

    static String kind2(String s) {
        return "string";
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void main(String[] args) {
        List<String> ls = new ArrayList<>();
        List<Integer> li = new ArrayList<>();
        System.out.println(ls.getClass() == li.getClass());

        List raw = ls;
        raw.add(42);
        System.out.println(ls.size());
        Object o = ls.get(0);
        System.out.println(o);
        try {
            String s = ls.get(0);
            System.out.println(s);
        } catch (ClassCastException e) {
            System.out.println("CCE");
        }

        System.out.println(max(3, 7) + " " + max("pear", "apple") + " " + max(2.5, -1.0));

        List<Number> nums = new ArrayList<>();
        fill(nums, 3);
        List<Object> objs = new ArrayList<>();
        fill(objs, 2);
        nums.add(1.5);
        System.out.println(sum(nums) + " " + objs);

        Object x = "hi";
        System.out.println(kind(x) + " " + kind("hi") + " " + kind2(x) + " " + kind2("hi"));
        List<? extends Number> ro = nums;
        Number first = ro.get(0);
        System.out.println(first.getClass().getSimpleName() + " " + ro.get(3).getClass().getSimpleName());
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Provide generic utilities. (1) `maxOf` returns the largest element of a list of any type that can be compared, including a list of `Special` objects where `Special extends Item` and only `Item implements Comparable<Item>`; for an empty list it must throw `NoSuchElementException`. (2) `copy` copies all numbers from a source list into a destination list that can accept them (for example, from `List<Integer>` into `List<Number>`). (3) `sum` returns the exact sum of a list of any `Number` subtype as a `double` (the sum of `1.5` and `2.5` is `4.0`).

The program has compile-time errors and logic errors. Find every bug, explain each, and write the corrected utilities.

```java
import java.util.ArrayList;
import java.util.List;

public class Main {
    static class Item implements Comparable<Item> {
        final int w;

        Item(int w) {
            this.w = w;
        }

        @Override
        public int compareTo(Item o) {
            return Integer.compare(w, o.w);
        }

        @Override
        public String toString() {
            return "Item" + w;
        }
    }

    static class Special extends Item {
        Special(int w) {
            super(w);
        }

        @Override
        public String toString() {
            return "Special" + w;
        }
    }

    static <T extends Comparable<T>> T maxOf(List<T> list) {
        T best = null;
        for (T x : list) {
            if (best == null || x.compareTo(best) > 0) {
                best = x;
            }
        }
        return best;
    }

    static void copy(List<? extends Number> src, List<? extends Number> dst) {
        for (Number n : src) {
            dst.add(n);
        }
    }

    static <T extends Number> double sum(List<T> list) {
        int s = 0;
        for (T x : list) {
            s += x.intValue();
        }
        return s;
    }

    public static void main(String[] args) {
        List<Special> specials = new ArrayList<>();
        specials.add(new Special(3));
        specials.add(new Special(9));
        specials.add(new Special(5));
        System.out.println(maxOf(specials));

        List<Integer> ints = new ArrayList<>();
        ints.add(1);
        ints.add(2);
        List<Number> numbers = new ArrayList<>();
        copy(ints, numbers);
        System.out.println(numbers);

        List<Double> ds = new ArrayList<>();
        ds.add(1.5);
        ds.add(2.5);
        System.out.println(sum(ds));
        System.out.println(maxOf(new ArrayList<Integer>()));
    }
}
```

---

<a id="day23"></a>
## Day 23 — Streams and Collectors in Depth

**Learning objectives:** Build stream pipelines with `groupingBy`, `partitioningBy`, `toMap`, `flatMap`, and downstream collectors; understand laziness, short-circuiting, and ordering; avoid reusing streams and side effects in pipelines; and compute grouped statistics correctly.

### Question 1: Mixed Real-World Scenario
**Title:** Regional Sales Digest

**Problem statement:** A retailer records sales as `region product units unitPrice` (all prices are whole currency units). For every region (in alphabetical order) print: the total revenue (sum of `units × unitPrice` over all sales in that region); the **top product**, which is the product with the highest total revenue in that region (ties broken by alphabetical order of product name); and the average number of units per sale record in that region with exactly 2 decimal places.

**Real-world context:** Weekly regional digests summarize revenue and best sellers for store managers.

**Input requirements:** Line 1: `n`. Next `n` lines: `region product units unitPrice`.

**Expected output:** One line per region: `<region>: revenue=<r> top=<product> avgUnits=<a>`.

**Sample cases:**
```
Input:
6
north pen 10 5
north book 2 40
south pen 4 5
north pen 3 5
south ink 1 20
south pen 1 5
Output:
north: revenue=145 top=book avgUnits=5.00
south: revenue=45 top=pen avgUnits=2.00
```
```
Input:
2
w a 1 10
w b 2 5
Output:
w: revenue=20 top=a avgUnits=1.50
```
```
Input:
1
east tea 3 7
Output:
east: revenue=21 top=tea avgUnits=3.00
```

**Constraints and edge cases:** 1 ≤ n ≤ 100000; 1 ≤ units ≤ 10^4; 1 ≤ unitPrice ≤ 10^6, so revenue can exceed the `int` range. A product's revenue must be summed across all of its records before comparing products.

### Question 2: Interview-Based Coding
**Title:** Group the Anagrams

**Problem statement:** Group the given words so that words that are anagrams of each other are in the same group. Print each group on its own line with the words in alphabetical order (duplicates are kept). Order the groups by size, **largest first**; groups of equal size are ordered by their first (alphabetically smallest) word.

**Input and output requirements:** Line 1: `n`. Line 2: `n` lowercase words. Output one group per line, words separated by spaces.

**Sample cases:**
```
Input:
6
eat tea tan ate nat bat
Output:
ate eat tea
nat tan
bat
```
```
Input:
6
ab ba abc cba bca c
Output:
abc bca cba
ab ba
c
```
```
Input:
2
aa aa
Output:
aa aa
```

**Constraints and edge cases:** 1 ≤ n ≤ 100000; word length 1-50. Sorting the letters of each word to build a key costs O(L log L) per word; make sure the grouping and final ordering do not exceed O(n log n) overall.

### Question 3: Company-Style Programming
**Title:** Runner-Up Pay Report

**Problem statement:** For every department (in alphabetical order), find the **second-highest distinct salary** and print all employees of that department who earn exactly that salary (names in alphabetical order, separated by commas). If the department has fewer than two distinct salaries, print `N/A`.

**Input and output requirements:** Line 1: `n`. Next `n` lines: `department name salary`. Output one line per department: `<dept>: <names> (<salary>)` or `<dept>: N/A`.

**Sample cases:**
```
Input:
8
eng ann 90
eng bob 80
eng cat 80
eng dan 90
ops eve 50
ops fay 50
hr gus 70
hr hal 60
Output:
eng: bob,cat (80)
hr: hal (60)
ops: N/A
```
```
Input:
3
a x 5
a y 7
a z 9
Output:
a: y (7)
```

**Constraints and edge cases:** 1 ≤ n ≤ 100000; salaries are positive integers up to 10^9; names and departments contain no spaces. Several employees can share the top salary (that does not make the next distinct salary the "third").

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        List<Integer> log = new ArrayList<>();
        Optional<Integer> first = Stream.of(5, 8, 12, 3, 20)
                .peek(x -> log.add(x))
                .filter(x -> x % 2 == 0)
                .map(x -> x * 10)
                .findFirst();
        System.out.println(first.get() + " " + log);

        Stream<Integer> lazy = Stream.of(1, 2, 3).peek(x -> System.out.print("p" + x + " "));
        System.out.println("built");
        System.out.println(lazy.map(x -> x * x).collect(Collectors.toList()));

        System.out.println(IntStream.rangeClosed(1, 5).boxed()
                .collect(Collectors.partitioningBy(x -> x % 2 == 0, Collectors.summingInt(x -> x))));

        Map<Integer, List<String>> byLen = Stream.of("aa", "b", "cc", "ddd", "e")
                .collect(Collectors.groupingBy(String::length, TreeMap::new, Collectors.toList()));
        System.out.println(byLen);

        System.out.println(Stream.iterate(1, x -> x * 3).limit(5).reduce(0, (a, b) -> a - b));
        System.out.println(Stream.of("x", "y", "z").collect(Collectors.joining("-", "<", ">")));
        System.out.println(IntStream.of(3, 1, 2).sorted().mapToObj(Integer::toString).collect(Collectors.joining()));
        System.out.println(Stream.of(1, 2, 3, 4).flatMap(x -> Stream.of(x, -x)).limit(5).collect(Collectors.toList()));
        System.out.println(Stream.of(1, 2, 3, 4, 5, 6).skip(1).filter(x -> x % 2 == 1).count());
    }
}
```

### Question 5: Debug the Code
**What the program should do:** For a list of words, print (1) a map from each lower-cased word to the number of times it occurs (a `TreeMap`, so it prints in alphabetical order), (2) the number of distinct lower-cased words, and (3) the average word length with exactly 2 decimal places. For the words `Apple, apple, Pear, fig, pear, Fig, apple` the expected output is `{apple=3, fig=2, pear=2}`, `3`, and `4.14`.

The program crashes and also computes one value incorrectly. Find every bug, explain each, and write the corrected program.

```java
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        List<String> words = Arrays.asList("Apple", "apple", "Pear", "fig", "pear", "Fig", "apple");
        Stream<String> lower = words.stream().map(String::toLowerCase);
        Map<String, Integer> counts = lower.collect(Collectors.toMap(w -> w, w -> 1));
        System.out.println(counts);
        long distinct = lower.distinct().count();
        System.out.println(distinct);
        double avg = words.stream().mapToInt(String::length).sum() / words.size();
        System.out.printf("%.2f%n", avg);
    }
}
```

---

<a id="day24"></a>
## Day 24 — Optional, Records, Sealed Classes, and Pattern Matching

**Learning objectives:** Model data with records and sealed hierarchies (Java 17), use `instanceof` pattern matching, handle absent values with `Optional` without `get()` misuse, understand record equality (including array components), and write recursive evaluators over sealed types. Note: pattern matching for `switch` is a preview feature in Java 17, so use `instanceof` patterns and ordinary `switch`.

### Question 1: Mixed Real-World Scenario
**Title:** Payment Fee Calculator

**Problem statement:** A payment gateway handles three kinds of payments. Model them as a **sealed interface** `Payment` permitting three **records** (`Card`, `Upi`, `Cash`), each holding the amount in cents (`long`). Fees (all integer arithmetic, truncating division):
- `CARD`: `max(100, amount × 2 / 100)`
- `UPI`: `0` if `amount ≤ 100000`, otherwise `amount × 5 / 1000`
- `CASH`: a flat `50`

Process the payments, print each with its fee, and print the total fees.

**Real-world context:** Gateways publish fee schedules per payment method; adding a new method should force the compiler to flag every place that computes fees.

**Input requirements:** Line 1: `n`. Next `n` lines: `TYPE amount` where `TYPE` is `CARD`, `UPI`, or `CASH`.

**Expected output:** `n` lines `<TYPE> <amount> fee=<fee>`, then `Total fees: <sum>`.

**Sample cases:**
```
Input:
4
CARD 5000
UPI 150000
CASH 1000
CARD 20000
Output:
CARD 5000 fee=100
UPI 150000 fee=750
CASH 1000 fee=50
CARD 20000 fee=400
Total fees: 1300
```
```
Input:
2
UPI 100000
CARD 10
Output:
UPI 100000 fee=0
CARD 10 fee=100
Total fees: 100
```

**Constraints and edge cases:** 1 ≤ n ≤ 100000; 1 ≤ amount ≤ 10^12, so use `long` (the product `amount × 5` must not overflow). The `UPI` boundary is inclusive at 100000.

### Question 2: Interview-Based Coding
**Title:** Effective City Lookup

**Problem statement:** Each user has an id, an optional city, and an optional manager id. The **effective city** of a user is the user's own city if present; otherwise the effective city of the user's manager (and so on up the management chain); if the chain ends without a city, or a cycle is detected, or the manager id does not refer to a known user, the effective city is unknown. Answer queries by id. Model the optional values with `Optional` and avoid `Optional.get()` without a presence check.

**Input and output requirements:** Line 1: `n`. Next `n` lines: `id city manager` where `city` or `manager` is `-` when absent. Then `q`, then `q` lines with one id each. For each query print the effective city, `UNKNOWN`, or `NO SUCH USER` if the id does not exist.

**Sample cases:**
```
Input:
5
1 Paris -
2 - 1
3 - 2
4 - 5
5 - 4
4
3
1
4
9
Output:
Paris
Paris
UNKNOWN
NO SUCH USER
```
```
Input:
2
10 Oslo 99
20 - 10
2
20
10
Output:
Oslo
Oslo
```

**Constraints and edge cases:** 1 ≤ n, q ≤ 100000; ids are distinct integers in [1, 10^9]. The management chain can be 100000 users long, so resolving every query by walking the whole chain is too slow, and recursion may overflow the stack. A user with a city never looks at the manager.

### Question 3: Company-Style Programming
**Title:** Prefix Expression Renderer

**Problem statement:** Parse a prefix expression made of the tokens `ADD`, `MUL` (binary), `NEG` (unary), and integer literals (possibly negative). Model the expression tree with a **sealed interface** and **records** (`Num`, `Add`, `Mul`, `Neg`). Print the fully parenthesized infix rendering and the value. Rendering rules: a number prints as itself; `Add` as `(left + right)`; `Mul` as `(left * right)`; `Neg` as `neg(operand)`.

**Input and output requirements:** One line of space-separated tokens forming one valid expression. Output two lines: the rendering, then the value.

**Sample cases:**
```
Input:
ADD 3 MUL 4 NEG 2
Output:
(3 + (4 * neg(2)))
-5
```
```
Input:
MUL ADD 1 2 ADD 3 4
Output:
((1 + 2) * (3 + 4))
21
```
```
Input:
NEG NEG 7
Output:
neg(neg(7))
7
```

**Constraints and edge cases:** At most 2000 tokens; literals fit in `int` and all intermediate values fit in `long`. A literal such as `-5` is a single token and is a number, not a `NEG`. The expression may be nested 1000 levels deep.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.Optional;

public class Main {
    sealed interface Shape permits Circle, Rect, Square {}

    record Circle(int r) implements Shape {}

    record Rect(int w, int h) implements Shape {
        Rect {
            if (w < 0 || h < 0) {
                throw new IllegalArgumentException("neg");
            }
        }

        int perimeter() {
            return 2 * (w + h);
        }
    }

    record Square(int s) implements Shape {
        static int made = 0;

        Square {
            made++;
        }
    }

    static int area(Shape s) {
        if (s instanceof Circle c) {
            return 3 * c.r() * c.r();
        } else if (s instanceof Rect r && r.w() == r.h()) {
            return -r.w();
        } else if (s instanceof Rect r) {
            return r.w() * r.h();
        } else if (s instanceof Square q) {
            return q.s() * q.s();
        }
        return 0;
    }

    static String label(int n) {
        return switch (n) {
            case 1, 2 -> "low";
            case 3 -> {
                String t = "mid";
                yield t + "!";
            }
            default -> "high";
        };
    }

    public static void main(String[] args) {
        Shape a = new Rect(2, 3);
        Shape b = new Rect(2, 3);
        System.out.println(a + " " + a.equals(b) + " " + (a == b) + " " + new Circle(4));
        System.out.println(area(new Circle(2)) + " " + area(a) + " " + area(new Rect(5, 5)) + " " + area(new Square(4)));
        try {
            new Rect(-1, 2);
        } catch (IllegalArgumentException e) {
            System.out.println("caught " + e.getMessage() + " " + ((Rect) a).perimeter());
        }
        System.out.println(Square.made + " " + new Square(1).equals(new Square(1)) + " " + Square.made);
        System.out.println(label(2) + label(3) + label(9));

        Optional<String> o = Optional.of("pear");
        System.out.println(o.map(String::length).filter(n -> n > 3).orElse(-1)
                + " " + Optional.<String>empty().map(String::length).orElse(-1)
                + " " + o.filter(s -> s.startsWith("x")).isPresent()
                + " " + Optional.ofNullable((String) null).or(() -> Optional.of("alt")).get());
        o.ifPresentOrElse(v -> System.out.println("has " + v), () -> System.out.println("none"));
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Look up orders by id. `findOrder(id)` returns an `Optional<Order>`; the program prints the order's total or the text `missing` when there is no such order. Two orders are equal when they have the same id and the same list of item prices (content comparison). For ids `1` (items `10, 20`), `2` (items `5`), and a duplicate of order 1 created separately, the expected output is: `30`, `missing`, `5`, and then `true` for the equality check.

The program throws exceptions and prints `false` for the equality check. Find every bug, explain each, and write the corrected program.

```java
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Main {
    record Order(int id, int[] prices) {
        int total() {
            int t = 0;
            for (int p : prices) {
                t += p;
            }
            return t;
        }
    }

    static Map<Integer, Order> store = new HashMap<>();

    static Optional<Order> findOrder(int id) {
        return Optional.of(store.get(id));
    }

    public static void main(String[] args) {
        store.put(1, new Order(1, new int[]{10, 20}));
        store.put(2, new Order(2, new int[]{5}));
        System.out.println(findOrder(1).get().total());
        System.out.println(findOrder(3).isPresent() ? findOrder(3).get().total() : "missing");
        System.out.println(findOrder(2).get().total());
        Order copy = new Order(1, new int[]{10, 20});
        System.out.println(findOrder(1).get().equals(copy));
    }
}
```

---

<a id="day25"></a>
## Day 25 — Concurrency Essentials

**Learning objectives:** Start and join threads, protect shared state with `synchronized`, locks, atomics, and concurrent collections, avoid deadlock with consistent lock ordering, coordinate threads in a fixed order, and use `ExecutorService` with `Future`. All problems here have deterministic output when written correctly.

### Question 1: Mixed Real-World Scenario
**Title:** Concurrent Bank Transfers

**Problem statement:** A bank processes `m` transfers `from to amount` across `n` accounts using a pool of `t` worker threads that take transfers from a shared queue. Each transfer subtracts `amount` from `from` and adds it to `to` as one atomic action: no other thread may observe or modify either balance in between, and two opposite transfers (`0 → 1` and `1 → 0`) running at the same time must never deadlock. Overdrafts are allowed (balances may become negative). After all transfers are complete, print the final balances. A transfer with `from == to` changes nothing.

**Real-world context:** Core banking systems process many transfers concurrently; correctness depends on atomic updates and deadlock-free locking.

**Input requirements:** Line 1: `n m t`. Line 2: `n` initial balances. Next `m` lines: `from to amount` (0-indexed accounts).

**Expected output:** One line with the `n` final balances separated by spaces.

**Sample cases:**
```
Input:
3 3 2
100 100 100
0 1 30
1 2 50
2 0 10
Output:
80 80 140
```
```
Input:
2 4 4
0 0
0 1 5
1 0 5
0 1 7
1 1 100
Output:
-7 7
```

**Constraints and edge cases:** 1 ≤ n ≤ 1000; 1 ≤ m ≤ 200000; 1 ≤ t ≤ 8; amounts and balances fit in `long`. The result must not depend on thread scheduling, and the program must terminate (shut the pool down and wait for completion).

### Question 2: Interview-Based Coding
**Title:** Three Threads, One Counter

**Problem statement:** Create exactly three worker threads named `A`, `B`, and `C`. They must print the numbers `1..n` in order, one number per turn, taking turns in the strict rotation `A, B, C, A, B, C, ...`: thread `A` prints `1`, `B` prints `2`, `C` prints `3`, `A` prints `4`, and so on until `n` is reached. Each line has the form `<thread>:<number>`. The main thread may only start the workers and wait for them. Use `wait/notify`, locks with conditions, or semaphores (no `sleep`-based timing, no busy waiting on a plain field).

**Input and output requirements:** One integer `n`. Output `n` lines.

**Sample cases:**
```
Input:
5
Output:
A:1
B:2
C:3
A:4
B:5
```
```
Input:
1
Output:
A:1
```

**Constraints and edge cases:** 1 ≤ n ≤ 3000. When `n` is not a multiple of 3, the threads that have no more numbers to print must still terminate, so the program must not hang waiting for a turn that will never come.

### Question 3: Company-Style Programming
**Title:** Parallel Word Census

**Problem statement:** Count word frequencies across a large text. Read all lines, split them among 4 worker threads (each worker handles every line whose index modulo 4 equals its worker number), and update **one shared** `ConcurrentHashMap` so that no counts are lost. When all workers have finished, print each distinct word and its count sorted by count (highest first); words with equal counts are sorted alphabetically.

**Input and output requirements:** Line 1: `n`. Next `n` lines of lowercase words separated by single spaces (a line may be empty). Output one line per distinct word: `<word> <count>`.

**Sample cases:**
```
Input:
3
a b a
b c
a
Output:
a 3
b 2
c 1
```
```
Input:
2
x y
y x
Output:
x 2
y 2
```

**Constraints and edge cases:** 1 ≤ n ≤ 100000; total number of words ≤ 1000000. Using `map.put(w, map.get(w) + 1)` loses updates; the update of a count must be atomic. The main thread must wait for the workers before printing. Empty lines contribute no words.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    static int plain = 0;
    static int guarded = 0;
    static final Object LOCK = new Object();

    static synchronized void incPlain() {
        plain++;
    }

    public static void main(String[] args) throws Exception {
        AtomicInteger atomic = new AtomicInteger();
        Thread[] ts = new Thread[4];
        for (int i = 0; i < ts.length; i++) {
            ts[i] = new Thread(() -> {
                for (int k = 0; k < 1000; k++) {
                    atomic.incrementAndGet();
                    incPlain();
                    synchronized (LOCK) {
                        guarded += 2;
                    }
                }
            });
            ts[i].start();
        }
        for (Thread t : ts) {
            t.join();
        }
        System.out.println(atomic.get() + " " + plain + " " + guarded);

        ExecutorService pool = Executors.newFixedThreadPool(3);
        List<Future<Integer>> fs = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            final int n = i;
            fs.add(pool.submit(() -> {
                if (n == 2) {
                    throw new IllegalStateException("boom" + n);
                }
                return n * n;
            }));
        }
        StringBuilder sb = new StringBuilder();
        for (Future<Integer> f : fs) {
            try {
                sb.append(f.get()).append(' ');
            } catch (ExecutionException e) {
                sb.append(e.getCause().getMessage()).append(' ');
            }
        }
        pool.shutdown();
        System.out.println(sb.toString().trim() + " " + pool.awaitTermination(1, TimeUnit.SECONDS));

        ThreadLocal<Integer> tl = ThreadLocal.withInitial(() -> 7);
        tl.set(1);
        int[] seen = new int[1];
        Thread t = new Thread(() -> seen[0] = tl.get());
        t.start();
        t.join();
        System.out.println(seen[0] + " " + tl.get());

        CountDownLatch latch = new CountDownLatch(2);
        ConcurrentHashMap<String, Integer> m = new ConcurrentHashMap<>();
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                for (int k = 0; k < 500; k++) {
                    m.merge("k", 1, Integer::sum);
                }
                latch.countDown();
            }).start();
        }
        latch.await();
        System.out.println(m + " " + latch.getCount());
        System.out.println(Thread.currentThread().getName());
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Four worker threads each add 1 to a shared counter and record a hit in a shared map 100000 times. Each worker also asks a lazily created `Config` singleton for its settings. After **all** workers finish, the program must print exactly `400000` (the counter), `400000` (the number of hits in the map), and `1` (the number of times the `Config` constructor ran).

The program prints different, unpredictable values on different runs and may even throw an exception. Describe the concurrency defects (race conditions, unsafe data structures, missing synchronization of the main thread), explain how each leads to wrong output, and write the corrected program.

```java
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    static int counter = 0;
    static Map<String, Integer> hits = new HashMap<>();
    static int configsCreated = 0;
    static Config config;

    static class Config {
        Config() {
            configsCreated++;
        }
    }

    static Config getConfig() {
        if (config == null) {
            config = new Config();
        }
        return config;
    }

    public static void main(String[] args) throws Exception {
        List<Thread> workers = new ArrayList<>();
        for (int w = 0; w < 4; w++) {
            Thread t = new Thread(() -> {
                getConfig();
                for (int i = 0; i < 100000; i++) {
                    counter++;
                    hits.merge("k", 1, Integer::sum);
                }
            });
            workers.add(t);
            t.start();
        }
        System.out.println(counter);
        System.out.println(hits.get("k"));
        System.out.println(configsCreated);
    }
}
```

---

<a id="day26"></a>
## Day 26 — Memory, JVM Behavior, and the String Pool

**Learning objectives:** Reason about reference semantics and pass-by-value, the string pool and `intern`, the `Integer` cache, class initialization order, bounded-memory data structures, defensive copying for true immutability, and recycling identifiers in a hand-built pool.

### Question 1: Mixed Real-World Scenario
**Title:** Bounded-Memory Event Deduplicator

**Problem statement:** A stream processor receives event ids and must flag duplicates, but it can only remember the **last `K` distinct ids it accepted**. For each incoming id: if it is currently remembered, report `D` (duplicate) and leave memory unchanged; otherwise report `N` (new), remember it, and if more than `K` ids are now remembered, forget the one that was remembered the longest (insertion order, not access order). An id that was forgotten is new again if it reappears.

**Real-world context:** Message brokers deduplicate with a bounded window so that memory does not grow with an unbounded stream.

**Input requirements:** Line 1: `K n`. Line 2: `n` event ids.

**Expected output:** One line of `n` verdicts separated by spaces.

**Sample cases:**
```
Input:
3 7
1 2 3 1 4 1 2
Output:
N N N D N N N
```
```
Input:
1 4
5 5 6 5
Output:
N D N N
```

**Constraints and edge cases:** 1 ≤ K ≤ 100000; 1 ≤ n ≤ 1000000; ids fit in `int`. Memory used must be O(K), not O(n). A duplicate does not refresh the id's position.

### Question 2: Interview-Based Coding
**Title:** Overwriting Ring Buffer

**Problem statement:** Implement a fixed-capacity circular buffer of ints that never grows: it must be backed by a single array of length `cap`. Commands:
- `WRITE x`: append `x`; if the buffer is full, **overwrite the oldest** element.
- `READ`: remove and print the oldest element, or print `EMPTY` if the buffer holds nothing.
- `SNAPSHOT`: print the current contents from oldest to newest separated by spaces, or `EMPTY`.

**Input and output requirements:** Line 1: `cap q`. Then `q` commands. Print one line for every `READ` and `SNAPSHOT`.

**Sample cases:**
```
Input:
3 12
WRITE 1
WRITE 2
WRITE 3
WRITE 4
SNAPSHOT
READ
WRITE 5
SNAPSHOT
READ
READ
READ
READ
Output:
2 3 4
2
3 4 5
3
4
5
EMPTY
```
```
Input:
1 5
WRITE 9
WRITE 8
SNAPSHOT
READ
SNAPSHOT
Output:
8
8
EMPTY
```

**Constraints and edge cases:** 1 ≤ cap ≤ 100000; 1 ≤ q ≤ 300000. Index arithmetic must wrap around correctly after many reads and writes. After the buffer is emptied by reads, further writes must start fresh.

### Question 3: Company-Style Programming
**Title:** Reference-Counted String Pool

**Problem statement:** Build your own string pool. `INTERN s` registers a reference to string `s`: the first time a string is interned it receives the **smallest identifier not currently in use** (starting from 0), and each later `INTERN` of the same string increases its reference count. `RELEASE s` decreases the count; when the count reaches zero the string is removed from the pool and its identifier becomes free for reuse.

Output formats:
- `INTERN s` prints `id=<id> refs=<count>`.
- `RELEASE s` prints `refs=<count>` after decrementing, or `freed id=<id>` if the count reached zero, or `MISSING` if the string is not in the pool.

**Input and output requirements:** Line 1: `q`. Then `q` commands. Strings contain no spaces.

**Sample cases:**
```
Input:
9
INTERN apple
INTERN pear
INTERN apple
RELEASE apple
RELEASE apple
INTERN fig
RELEASE kiwi
RELEASE pear
INTERN plum
Output:
id=0 refs=1
id=1 refs=1
id=0 refs=2
refs=1
freed id=0
id=0 refs=1
MISSING
freed id=1
id=1 refs=1
```
```
Input:
4
INTERN a
RELEASE a
RELEASE a
INTERN b
Output:
id=0 refs=1
freed id=0
MISSING
id=0 refs=1
```

**Constraints and edge cases:** 1 ≤ q ≤ 300000. Finding the smallest free identifier must be O(log n) or better per operation. A string that was freed and interned again is treated as new and gets the smallest free id at that time.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
public class Main {
    static String sf = init("static field");

    static {
        System.out.println("static block");
    }

    static String init(String s) {
        System.out.println("init " + s);
        return s;
    }

    static class Holder {
        static final int CONST = 5;
        static int value = 6;

        static {
            System.out.println("Holder loaded");
        }
    }

    static void change(StringBuilder sb, String s, int[] a, int n) {
        sb.append("!");
        s = s + "!";
        a[0] = 99;
        n = 99;
    }

    public static void main(String[] args) {
        System.out.println("main start");
        String a = "java";
        String b = "ja" + "va";
        String c = new String("java");
        String d = "ja";
        String e = d + "va";
        final String f = "ja";
        String g = f + "va";
        System.out.println((a == b) + " " + (a == c) + " " + (a == e) + " " + (a == g));
        System.out.println((a == c.intern()) + " " + (a == e.intern()) + " " + a.equals(c));

        Integer i1 = 127;
        Integer i2 = 127;
        Integer i3 = 128;
        Integer i4 = 128;
        System.out.println((i1 == i2) + " " + (i3 == i4) + " " + i3.equals(i4));

        StringBuilder sb = new StringBuilder("hi");
        String s = "hi";
        int[] arr = {1};
        int n = 1;
        change(sb, s, arr, n);
        System.out.println(sb + " " + s + " " + arr[0] + " " + n);

        System.out.println(Holder.CONST);
        System.out.println("after const");
        System.out.println(Holder.value);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** `Schedule` must be a truly **immutable** class: once an instance is created, nothing that the caller does afterward (changing the list or array that was passed to the constructor, modifying what a getter returns, or calling `withSlot`) may alter that instance. `withSlot(s)` must return a **new** `Schedule` with the extra slot and leave the original unchanged. Expected output of `main`: `[9am, 10am] [5, 8]`, then `[9am, 10am] [5, 8]` again, then `[9am, 10am, 11am]` for the new schedule while the original still prints `[9am, 10am]`.

The class is not immutable and the demonstration prints altered data. Find every defect, explain how the caller can break the object through each one, and write the corrected class.

```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Main {
    static final class Schedule {
        private final List<String> slots;
        private final int[] capacity;

        Schedule(List<String> slots, int[] capacity) {
            this.slots = slots;
            this.capacity = capacity;
        }

        List<String> getSlots() {
            return slots;
        }

        int[] getCapacity() {
            return capacity;
        }

        Schedule withSlot(String s) {
            slots.add(s);
            return this;
        }
    }

    public static void main(String[] args) {
        List<String> slots = new ArrayList<>(Arrays.asList("9am", "10am"));
        int[] cap = {5, 8};
        Schedule sc = new Schedule(slots, cap);
        slots.add("midnight");
        cap[0] = 0;
        System.out.println(sc.getSlots() + " " + Arrays.toString(sc.getCapacity()));
        sc.getSlots().clear();
        sc.getCapacity()[1] = -1;
        System.out.println(sc.getSlots() + " " + Arrays.toString(sc.getCapacity()));

        Schedule fresh = new Schedule(new ArrayList<>(Arrays.asList("9am", "10am")), new int[]{5, 8});
        Schedule more = fresh.withSlot("11am");
        System.out.println(more.getSlots());
        System.out.println(fresh.getSlots());
    }
}
```

---

<a id="day27"></a>
## Day 27 — Design Patterns

**Learning objectives:** Apply the Strategy, Observer, Builder, Template Method, and Singleton patterns to concrete problems, understand initialization order pitfalls in constructors, avoid shared-mutable-state bugs in builders, and handle observers that change the subscriber list during notification.

### Question 1: Mixed Real-World Scenario
**Title:** Checkout Discount Chain (Strategy)

**Problem statement:** An online store applies a list of coupons, **in the given order**, to the cart subtotal. Model each coupon type as a strategy behind a common interface so new coupon types could be added without changing the checkout code. Coupon types (all amounts are whole currency units, and percentage discounts truncate the discount amount using integer division):
- `PERCENT p`: subtract `current * p / 100`.
- `FLAT a`: subtract `a`, but the total never goes below 0.
- `BULK k p`: if the **total quantity of items in the cart** is at least `k`, subtract `current * p / 100`; otherwise do nothing.

Print the running total after each coupon and the final total.

**Real-world context:** Marketing teams add promotions constantly; a strategy-based design keeps checkout stable while the discount rules change.

**Input requirements:** Line 1: `n` (items). Next `n` lines: `price quantity`. Next line: `m` (coupons). Next `m` lines: a coupon as described.

**Expected output:** `m` lines `After <coupon text>: <total>` and then `Total: <final>`. With zero coupons, print only the `Total` line.

**Sample cases:**
```
Input:
2
100 2
50 1
3
PERCENT 10
FLAT 30
BULK 3 5
Output:
After PERCENT 10: 225
After FLAT 30: 195
After BULK 3 5: 186
Total: 186
```
```
Input:
1
10 1
2
FLAT 50
PERCENT 50
Output:
After FLAT 50: 0
After PERCENT 50: 0
Total: 0
```

**Constraints and edge cases:** 1 ≤ n ≤ 1000; 0 ≤ m ≤ 1000; prices up to 10^6 and quantities up to 1000, so subtotals can exceed the `int` range (use `long`). The order of coupons changes the result. A `BULK` coupon whose threshold is not met still prints its line.

### Question 2: Interview-Based Coding
**Title:** Topic Event Bus (Observer)

**Problem statement:** Implement an event bus. Commands:
- `SUB name topic`: subscribe `name` to `topic` permanently.
- `SUBONCE name topic`: subscribe `name` to `topic` for **one** message only; after receiving its first message it is automatically unsubscribed.
- `UNSUB name topic`: remove the subscription (no effect if absent).
- `PUB topic message`: deliver `message` (the rest of the line, may contain spaces) to every subscriber of the topic in the order in which they subscribed. Print `<name> got <message>` for each delivery, or `no subscribers` if the topic has none.

A `SUB` or `SUBONCE` for a `(name, topic)` pair that already exists is ignored. Subscribers must be removed correctly even though removal happens while a message is being delivered.

**Input and output requirements:** Line 1: `q`. Then `q` commands. Print output only for `PUB` commands.

**Sample cases:**
```
Input:
8
SUB a news
SUB b news
SUBONCE c news
PUB news hello
PUB news again
UNSUB a news
PUB news x
PUB sports y
Output:
a got hello
b got hello
c got hello
a got again
b got again
b got x
no subscribers
```
```
Input:
5
SUB a t
SUB a t
PUB t m1
UNSUB z t
PUB t m2
Output:
a got m1
a got m2
```

**Constraints and edge cases:** 1 ≤ q ≤ 100000; at most 1000 subscribers per topic; names and topics contain no spaces. The total number of deliveries printed is at most 10^6. Removing an element from a list while iterating over it with a for-each loop is a bug.

### Question 3: Company-Style Programming
**Title:** Pizza Order Builder (Builder)

**Problem statement:** Implement a `PizzaBuilder` (fluent builder) that creates immutable `Pizza` objects after validation, and use it to process order lines. An order line consists of space-separated `key=value` pairs:
- `size` (required): `S`, `M`, or `L`.
- `crust` (optional): `thin` (default), `thick`, or `stuffed`.
- `top` (optional): comma-separated toppings (duplicates count once).
- `qty` (optional, default 1): an integer from 1 to 10.

Validation, checked in this order (report the first failure): size missing -> `ERROR size required`; size not `S/M/L` -> `ERROR invalid size`; more than 4 distinct toppings -> `ERROR too many toppings`; `stuffed` crust with size `S` -> `ERROR stuffed not available in S`; qty outside 1-10 -> `ERROR invalid quantity`. Price in cents: base `S=800`, `M=1000`, `L=1200`; `+150` per distinct topping; `+200` for a stuffed crust; the sum is multiplied by `qty`. A valid order prints `OK <price>`.

**Input and output requirements:** Line 1: `n`. Then `n` order lines. Output one line per order.

**Sample cases:**
```
Input:
4
size=M top=olive,onion
crust=thick
size=S crust=stuffed
size=L top=a,a,b crust=stuffed qty=2
Output:
OK 1300
ERROR size required
ERROR stuffed not available in S
OK 3400
```
```
Input:
3
size=L top=a,b,c,d,e
size=XL
size=M qty=11
Output:
ERROR too many toppings
ERROR invalid size
ERROR invalid quantity
```

**Constraints and edge cases:** 1 ≤ n ≤ 50000; each line has at most 20 keys and at most 100 toppings in the list. The `Pizza` class must not be changeable after construction. Keys may appear in any order; a key not listed above does not occur.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.IntBinaryOperator;

public class Main {
    static abstract class Report {
        Report() {
            System.out.println("ctor: " + title());
        }

        abstract String title();

        final String render() {
            return header() + "|" + title() + "|" + footer();
        }

        String header() {
            return "H";
        }

        String footer() {
            return "F";
        }
    }

    static class Sales extends Report {
        private String name = "sales";
        private final int pages = 3;
        private final String tag = new String("T");

        Sales() {
            super();
            System.out.println("after: " + title());
        }

        @Override
        String title() {
            return name + pages + tag;
        }

        @Override
        String header() {
            return "SH";
        }
    }

    enum Registry {
        INSTANCE;

        int count;

        int next() {
            return ++count;
        }
    }

    static class Lazy {
        static int created = 0;

        private Lazy() {
            created++;
        }

        private static class Holder {
            static final Lazy INSTANCE = new Lazy();
        }

        static Lazy get() {
            return Holder.INSTANCE;
        }
    }

    public static void main(String[] args) {
        Report r = new Sales();
        System.out.println(r.render());

        System.out.println(Registry.INSTANCE.next() + " " + Registry.valueOf("INSTANCE").next()
                + " " + (Registry.valueOf("INSTANCE") == Registry.INSTANCE));

        System.out.println(Lazy.created);
        Lazy a = Lazy.get();
        Lazy b = Lazy.get();
        System.out.println(Lazy.created + " " + (a == b));

        Map<String, IntBinaryOperator> ops = new TreeMap<>();
        ops.put("sub", (x, y) -> x - y);
        ops.put("add", (x, y) -> x + y);
        ops.put("mul", (x, y) -> x * y);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, IntBinaryOperator> e : ops.entrySet()) {
            sb.append(e.getKey()).append('=').append(e.getValue().applyAsInt(6, 4)).append(' ');
        }
        System.out.println(sb.toString().trim());

        List<Consumer<String>> subs = new ArrayList<>();
        StringBuilder seen = new StringBuilder();
        subs.add(s -> seen.append("A:").append(s).append(' '));
        subs.add(s -> seen.append("B:").append(s.toUpperCase()).append(' '));
        for (String msg : new String[]{"x", "y"}) {
            for (Consumer<String> c : subs) {
                c.accept(msg);
            }
            if (msg.equals("x")) {
                subs.remove(0);
            }
        }
        System.out.println(seen.toString().trim());
    }
}
```

### Question 5: Debug the Code
**What the program should do:** A `PizzaBuilder` is reused to build several independent pizzas. In `main`, a pizza with `olive` is built, then the same builder adds `onion` and builds a second pizza. The two pizzas must be independent: the first prints `M [olive]` and the second prints `M [olive, onion]`. A pizza must be immutable (its toppings list cannot be changed through any getter), and calling `build()` without a size must throw an `IllegalStateException` with the message `size required`.

The program prints the wrong toppings for the first pizza, allows modification, and accepts invalid builders. Find every bug, explain each, and write the corrected classes.

```java
import java.util.ArrayList;
import java.util.List;

public class Main {
    static class Pizza {
        private final String size;
        private final List<String> toppings;

        Pizza(String size, List<String> toppings) {
            this.size = size;
            this.toppings = toppings;
        }

        List<String> getToppings() {
            return toppings;
        }

        @Override
        public String toString() {
            return size + " " + toppings;
        }
    }

    static class Builder {
        private String size;
        private final List<String> toppings = new ArrayList<>();

        Builder size(String s) {
            this.size = s;
            return this;
        }

        Builder topping(String t) {
            toppings.add(t);
            return this;
        }

        Pizza build() {
            return new Pizza(size, toppings);
        }
    }

    public static void main(String[] args) {
        Builder b = new Builder().size("M").topping("olive");
        Pizza p1 = b.build();
        b.topping("onion");
        Pizza p2 = b.build();
        System.out.println(p1);
        System.out.println(p2);
        p1.getToppings().add("anchovy");
        System.out.println(p1);
        try {
            new Builder().topping("x").build();
            System.out.println("built without size");
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
}
```

---

<a id="day28"></a>
## Day 28 — Low-Level Design Mini-Systems and Edge-Case Testing

**Learning objectives:** Translate a written specification into small, correct systems (parking lot, token-bucket rate limiter, elevator scheduler, library), pay attention to boundary conditions (inclusive/exclusive limits, zero durations, repeated operations), and design test cases for edge conditions.

### Question 1: Mixed Real-World Scenario
**Title:** Parking Lot Manager

**Problem statement:** A parking lot has a fixed row of spots, each of size `S`, `M`, or `L` (given as a string, index 0 is the first spot). Vehicles: `BIKE` fits in any spot, `CAR` fits in `M` or `L`, and `BUS` fits only in `L`. When a vehicle is parked it takes the **smallest** free spot that fits it (size order `S < M < L`); ties go to the lowest index. Commands (timestamps are in minutes and non-decreasing):
- `PARK t plate type`: print `Parked <plate> at <spot>`, or `FULL` if no free spot fits, or `ALREADY PARKED` if that plate is currently parked.
- `LEAVE t plate`: free the spot and print `<plate> paid <fee>`, or `UNKNOWN` if the plate is not parked. The fee is `hours × rate`, where `hours` is the parked duration in minutes divided by 60 **rounded up**, with a minimum of 1 hour, and the rate depends on the **spot size** the vehicle occupied: `S=10`, `M=20`, `L=30`.

**Real-world context:** Parking software must handle capacity, vehicle sizes, and billing boundaries precisely, because disputes arise at the edges (exactly one hour, zero minutes).

**Input requirements:** Line 1: the spots string. Line 2: `q`. Then `q` commands.

**Expected output:** One line per command.

**Sample cases:**
```
Input:
SML
8
PARK 0 B1 BIKE
PARK 5 C1 CAR
PARK 6 B2 BIKE
PARK 7 C2 CAR
LEAVE 125 C1
PARK 130 C2 CAR
LEAVE 190 B1
LEAVE 200 X9
Output:
Parked B1 at 0
Parked C1 at 1
Parked B2 at 2
FULL
C1 paid 40
Parked C2 at 1
B1 paid 40
UNKNOWN
```
```
Input:
L
7
PARK 0 X BUS
PARK 1 Y BIKE
PARK 2 X CAR
LEAVE 60 X
LEAVE 61 X
PARK 61 Y BIKE
LEAVE 61 Y
Output:
Parked X at 0
FULL
ALREADY PARKED
X paid 30
UNKNOWN
Parked Y at 0
Y paid 30
```

**Constraints and edge cases:** 1 ≤ number of spots ≤ 100000; 1 ≤ q ≤ 200000; timestamps up to 10^9. Finding the best spot must be efficient (a scan of all spots per command is too slow at this size). A parked duration of exactly 60 minutes costs 1 hour; 61 minutes costs 2; 0 minutes costs 1.

### Question 2: Interview-Based Coding
**Title:** Token Bucket Rate Limiter

**Problem statement:** Implement a per-user token-bucket rate limiter. Each user has a bucket with capacity `C` tokens that starts **full** (the first time the user is seen, at the time of their first request). Tokens refill continuously at `R` tokens per second, applied lazily on each request: before handling a request at time `t`, add `(t - lastTime) × R` tokens (never exceeding `C`), where `lastTime` is the user's previous request time. A request consumes 1 token and is `ALLOW`ed if at least 1 token is available; otherwise it is `DENY`ed and consumes nothing. Timestamps are whole seconds and non-decreasing.

**Input and output requirements:** Line 1: `C R`. Line 2: `m`. Then `m` lines `t user`. Output `ALLOW` or `DENY` for each request, one per line.

**Sample cases:**
```
Input:
3 1
7
0 u1
0 u1
0 u1
0 u1
1 u1
1 u2
10 u1
Output:
ALLOW
ALLOW
ALLOW
DENY
ALLOW
ALLOW
ALLOW
```
```
Input:
1 2
5
0 a
0 a
1 a
1 a
2 a
Output:
ALLOW
DENY
ALLOW
DENY
ALLOW
```

**Constraints and edge cases:** 1 ≤ C ≤ 10^9; 1 ≤ R ≤ 10^9; 1 ≤ m ≤ 200000; 0 ≤ t ≤ 10^9. The product `(t - lastTime) × R` can reach 10^18 and must not overflow while computing; the refill must be capped at `C`. A user who has been idle for a long time has exactly `C` tokens, not more.

### Question 3: Company-Style Programming
**Title:** Elevator Sweep Planner

**Problem statement:** A single elevator is at floor `s` and is moving in direction `UP` or `DOWN`. A set of requested floors is given (duplicates count once). The elevator uses a sweep policy: it continues in its current direction and serves every requested floor in that direction in order, including the current floor `s` if it is requested (served first, distance 0), then reverses once and serves the remaining floors in the opposite direction in order. Print the order in which floors are served and the total distance travelled (number of floors moved).

**Input and output requirements:** Line 1: `s DIRECTION m`. Line 2: `m` requested floors. Output two lines: `Order: <floors separated by spaces>` and `Distance: <total>`.

**Sample cases:**
```
Input:
5 UP 6
8 2 7 5 1 9
Output:
Order: 5 7 8 9 2 1
Distance: 12
```
```
Input:
5 DOWN 3
8 2 7
Output:
Order: 2 7 8
Distance: 9
```
```
Input:
5 UP 2
3 1
Output:
Order: 3 1
Distance: 4
```
```
Input:
5 DOWN 3
5 5 9
Output:
Order: 5 9
Distance: 4
```

**Constraints and edge cases:** 1 ≤ m ≤ 100000; floors are integers in [0, 10^9]. When the current floor is requested it is served first regardless of direction. If there are no requests in the current direction, the elevator immediately reverses. The distance can exceed the `int` range for the largest inputs when summed, so choose your type with care.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.TreeMap;

public class Main {
    static class Library {
        private final Map<String, Integer> copies = new TreeMap<>();
        private final Map<String, Deque<String>> waitlist = new TreeMap<>();

        void add(String title, int n) {
            copies.merge(title, n, Integer::sum);
        }

        String borrow(String user, String title) {
            int c = copies.getOrDefault(title, 0);
            if (c > 0) {
                copies.put(title, c - 1);
                return user + ":OK";
            }
            Deque<String> q = waitlist.computeIfAbsent(title, k -> new ArrayDeque<>());
            if (q.contains(user)) {
                return user + ":ALREADY";
            }
            q.addLast(user);
            return user + ":WAIT" + q.size();
        }

        String giveBack(String title) {
            Deque<String> q = waitlist.get(title);
            if (q != null && !q.isEmpty()) {
                return "TO " + q.pollFirst();
            }
            copies.merge(title, 1, Integer::sum);
            return "SHELF";
        }
    }

    public static void main(String[] args) {
        Library lib = new Library();
        lib.add("dune", 1);
        lib.add("emma", 2);
        lib.add("dune", 0);
        StringBuilder sb = new StringBuilder();
        sb.append(lib.borrow("ann", "dune")).append(' ');
        sb.append(lib.borrow("bob", "dune")).append(' ');
        sb.append(lib.borrow("cy", "dune")).append(' ');
        sb.append(lib.borrow("bob", "dune")).append(' ');
        sb.append(lib.borrow("dee", "zorro")).append(' ');
        System.out.println(sb.toString().trim());
        sb.setLength(0);
        sb.append(lib.giveBack("dune")).append(' ');
        sb.append(lib.giveBack("dune")).append(' ');
        sb.append(lib.giveBack("dune")).append(' ');
        sb.append(lib.borrow("eve", "dune")).append(' ');
        sb.append(lib.borrow("eve", "emma")).append(' ');
        sb.append(lib.borrow("fay", "emma")).append(' ');
        sb.append(lib.borrow("gus", "emma"));
        System.out.println(sb);
        System.out.println(lib.copies + " " + lib.waitlist);
    }
}
```

### Question 5: Debug the Code
**What the program should do:** Implement a small library with these rules. (1) A member may hold at most **3** books at once; a 4th `borrow` is rejected (`false`). (2) A book that is currently on loan cannot be borrowed by anyone (`false`). (3) The loan period is 14 days: a book borrowed on day `d` is due on day `d + 14`. (4) `giveBack(book, day, priceCents)` returns the fine in cents: `25` per day late (`day - due`), `0` if returned on or before the due day, and never more than `priceCents`; it returns `-1` if the book is not on loan. (5) Returning a book frees the member's slot so they can borrow again. Expected results for the calls in `main`, in order: `true false true true false 0 250 -1 true 300`.

The code produces wrong results and crashes for some calls. Find every bug, explain which rule each violates and with which call, and write the corrected class.

```java
import java.util.HashMap;
import java.util.Map;

public class Main {
    static class Library {
        private final Map<String, Integer> loans = new HashMap<>();
        private final Map<String, String> holder = new HashMap<>();
        private final Map<String, Integer> count = new HashMap<>();

        boolean borrow(String member, String book, int day) {
            if (count.getOrDefault(member, 0) > 3) {
                return false;
            }
            loans.put(book, day);
            holder.put(book, member);
            count.merge(member, 1, Integer::sum);
            return true;
        }

        int giveBack(String book, int day, int priceCents) {
            Integer start = loans.get(book);
            int fine = 25 * (day - (start + 14));
            fine = Math.max(fine, priceCents);
            loans.remove(book);
            return fine;
        }
    }

    public static void main(String[] args) {
        Library lib = new Library();
        System.out.println(lib.borrow("ann", "b1", 0));
        System.out.println(lib.borrow("bob", "b1", 1));
        System.out.println(lib.borrow("ann", "b2", 0));
        System.out.println(lib.borrow("ann", "b3", 0));
        System.out.println(lib.borrow("ann", "b4", 0));
        System.out.println(lib.giveBack("b1", 10, 5000));
        System.out.println(lib.giveBack("b2", 24, 5000));
        System.out.println(lib.giveBack("b2", 25, 5000));
        System.out.println(lib.borrow("ann", "b4", 30));
        System.out.println(lib.giveBack("b3", 500, 300));
    }
}
```

---

<a id="day29"></a>
## Day 29 — Mixed Hard Revision

**Learning objectives:** Combine heaps, stacks, prefix sums with modular arithmetic, dispatch rules, `finally` semantics, and heap-based medians in one session. Treat each problem as if it were the one you meet in a real interview: state the approach, then the edge cases, then code.

### Question 1: Mixed Real-World Scenario
**Title:** Merged Social Feed

**Problem statement:** A social app shows a user the top `N` posts from the feeds of everyone they follow. Each followed feed is already sorted by post time from newest to oldest. The same post (same post id) can appear in several feeds when it was shared. Merge the feeds into one list ordered by time (newest first), show each post id **only once** (its first appearance in the merged order), and print the first `N` posts. If two entries have the same time, the entry from the feed that appears earlier in the input comes first.

**Real-world context:** Feed services merge many sorted sources and de-duplicate shared content without sorting everything again.

**Input requirements:** Line 1: `k N`. Then `k` feeds, each on one line: `m` followed by `m` pairs `time id` (times strictly decreasing within a feed; ids are tokens without spaces).

**Expected output:** One line with up to `N` post ids separated by spaces.

**Sample cases:**
```
Input:
2 4
3 9 p1 5 p2 1 p3
3 8 p4 5 p2 2 p5
Output:
p1 p4 p2 p5
```
```
Input:
1 5
2 7 a 3 b
Output:
a b
```

**Constraints and edge cases:** 1 ≤ k ≤ 1000; the total number of entries ≤ 200000; 1 ≤ N ≤ 200000; times fit in `int`. A duplicate id is skipped and does not count toward `N`. A feed can be empty (`m = 0`). If fewer than `N` distinct posts exist, print them all. Putting all entries in one list and sorting is acceptable only if you handle tie order exactly as stated.

### Question 2: Interview-Based Coding
**Title:** Longest Valid Parentheses

**Problem statement:** Given a string made only of `(` and `)`, find the length of the longest substring that is a well-formed (balanced) parentheses string.

**Input and output requirements:** One line: the string (possibly empty). Output the length.

**Sample cases:**
```
Input:
(()
Output:
2
```
```
Input:
)()())
Output:
4
```
```
Input:
()(())
Output:
6
```
```
Input:
()(()
Output:
2
```

**Constraints and edge cases:** 0 ≤ length ≤ 300000. The empty string gives `0`. Checking every substring is O(n³) or O(n²) and too slow. The answer is a contiguous substring, not a subsequence.

### Question 3: Company-Style Programming
**Title:** Longest Divisible Stretch

**Problem statement:** Given an array of integers (positive, negative, or zero) and an integer `K`, find the length of the longest contiguous subarray whose sum is divisible by `K`. Print `0` if there is none.

**Input and output requirements:** Line 1: `n K`. Line 2: `n` integers. Output the length.

**Sample cases:**
```
Input:
6 5
4 5 0 -2 -3 1
Output:
6
```
```
Input:
3 7
1 2 3
Output:
0
```
```
Input:
3 2
-1 2 9
Output:
3
```
```
Input:
1 7
-7
Output:
1
```

**Constraints and edge cases:** 1 ≤ n ≤ 200000; |a[i]| ≤ 10^9; 1 ≤ K ≤ 10^9. Prefix sums reach 2·10^14, so use `long`. In Java, `%` of a negative number is negative, so remainders must be normalized before they are used as keys.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.Arrays;

public class Main {
    static class A {
        String name = "A";

        static String kind() {
            return "kA";
        }

        String who() {
            return "A";
        }

        String call() {
            return who() + name + kind();
        }

        String over(Object o) {
            return "Ao";
        }

        String over(String s) {
            return "As";
        }
    }

    static class B extends A {
        String name = "B";

        static String kind() {
            return "kB";
        }

        @Override
        String who() {
            return "B";
        }

        @Override
        String over(Object o) {
            return "Bo";
        }
    }

    static int f() {
        try {
            return 1;
        } finally {
            System.out.print("f ");
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
        int x = 1;
        try {
            return x;
        } finally {
            x = 99;
        }
    }

    static String s() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            try {
                if (i == 1) {
                    continue;
                }
                sb.append(i);
            } finally {
                sb.append('f');
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        A x = new B();
        Object o = "s";
        System.out.println(x.name + " " + x.who() + " " + x.call() + " " + ((B) x).name);
        System.out.println(x.over("s") + " " + x.over(o) + " " + new A().over("s"));
        System.out.println(f() + " " + g() + " " + h() + " " + s());

        int i = 5;
        i = i++ + ++i;
        int[] arr = {1, 2, 3};
        int k = 0;
        arr[k++] = arr[k] + 10;
        System.out.println(i + " " + Arrays.toString(arr) + " " + k);

        long big = Integer.MAX_VALUE + 1;
        long big2 = Integer.MAX_VALUE + 1L;
        System.out.println(big + " " + big2);
        System.out.println(1 + 2 + "3" + 4 + 5);
        System.out.println('a' + 'b' + "c");
    }
}
```

### Question 5: Debug the Code
**What the program should do:** `MedianFinder` supports `add(int x)` and `median()` for a stream of `int` values using two heaps: a max-heap `low` for the smaller half and a min-heap `high` for the larger half, kept so that `low.size()` equals `high.size()` or exceeds it by one. `median()` returns a `double`: the top of `low` for an odd count, the exact average of the two tops for an even count. Values may be anywhere in the `int` range. Expected output for `main`: `1.0`, `1.5`, `2.0`, then `2.147483647E9` after adding `Integer.MAX_VALUE` to a fresh finder twice.

The program throws an exception, truncates averages, and can overflow. Find every bug, explain each, and rewrite the class.

```java
import java.util.PriorityQueue;

public class Main {
    static class MedianFinder {
        private final PriorityQueue<Integer> low = new PriorityQueue<>((a, b) -> b - a);
        private final PriorityQueue<Integer> high = new PriorityQueue<>();

        void add(int x) {
            low.add(x);
            high.add(low.poll());
            if (high.size() > low.size() + 1) {
                low.add(high.poll());
            }
        }

        double median() {
            if (low.size() > high.size()) {
                return low.peek();
            }
            return (low.peek() + high.peek()) / 2;
        }
    }

    public static void main(String[] args) {
        MedianFinder m = new MedianFinder();
        m.add(1);
        System.out.println(m.median());
        m.add(2);
        System.out.println(m.median());
        m.add(3);
        System.out.println(m.median());
        MedianFinder big = new MedianFinder();
        big.add(Integer.MAX_VALUE);
        big.add(Integer.MAX_VALUE);
        System.out.println(big.median());
    }
}
```

---

<a id="day30"></a>
## Day 30 — Full Mock Assessment

**Learning objectives:** Complete a full advanced mock assessment on your own. Suggested time: 150 minutes. Write complete programs for the coding questions, test every sample and your own edge cases, and then answer the predict and debug questions without running anything. The day contains two multi-step system problems (Questions 1 and 3), one hard algorithmic problem (Question 2), a tricky output-prediction question, and a hard debugging question on dynamic programming. It reviews the whole month: data structures, graphs, DP, collections behavior, and engineering judgment.

### Question 1: Mixed Real-World Scenario (Multi-Step System)
**Title:** Warehouse Orders With Backorders

**Problem statement:** Build an order-fulfilment engine for a warehouse. It keeps stock per SKU and a set of **backorders** (orders that could not be filled). Commands:
- `STOCK sku qty`: add `qty` units to the SKU's stock (creating the SKU if needed). Then try to fulfil pending backorders **for that SKU**: consider them in order of priority (higher first), then by arrival order (earlier first). Fulfil a backorder only if the current stock covers its **whole** quantity (subtract it and print `FILLED <id>`); otherwise **skip** it and continue with the next backorder (a skipped order may be filled later by a different restock).
- `ORDER id sku qty priority`: if stock of `sku` is at least `qty`, subtract it and print `FILLED <id>`. Otherwise print `BACKORDER <id>` and keep the order pending (an unknown SKU simply has stock 0). Arrival order is the order of `ORDER` commands.
- `CANCEL id`: if the order is a pending backorder, remove it and print `CANCELLED <id>`; otherwise (already filled, already cancelled, or unknown) print `NOTFOUND <id>`.
- `REPORT`: print one line per SKU that has appeared in any `STOCK` or `ORDER` command, in alphabetical order: `<sku> stock=<stock> pending=<number of pending backorders for that SKU>`.

**Real-world context:** Fulfilment systems must honor priority customers fairly, never split an order unexpectedly, and stay fast when thousands of backorders pile up.

**Input requirements:** Line 1: `q`. Then `q` commands, one per line. Order ids are unique tokens.

**Expected output:** The lines described above, in command order.

**Sample cases:**
```
Input:
11
STOCK bolt 10
ORDER o1 bolt 4 1
ORDER o2 bolt 8 1
ORDER o3 bolt 5 9
ORDER o4 nut 2 5
STOCK bolt 3
CANCEL o4
CANCEL o1
STOCK nut 5
STOCK bolt 10
REPORT
Output:
FILLED o1
BACKORDER o2
FILLED o3
BACKORDER o4
CANCELLED o4
NOTFOUND o1
FILLED o2
bolt stock=6 pending=0
nut stock=5 pending=0
```
```
Input:
5
ORDER x a 5 1
ORDER y a 5 3
ORDER z a 5 3
STOCK a 10
REPORT
Output:
BACKORDER x
BACKORDER y
BACKORDER z
FILLED y
FILLED z
a stock=0 pending=1
```

**Constraints and edge cases:** 1 ≤ q ≤ 200000; quantities and priorities are positive integers up to 10^9; stock fits in `long`. A `STOCK` command must not rescan unrelated SKUs' backorders, and a pending backorder that was cancelled must never be filled later. Quantity of a single order may exceed any current stock.

### Question 2: Interview-Based Coding (Hard Algorithmic)
**Title:** Critical Links

**Problem statement:** A network has `n` servers (numbered `0..n-1`) and `m` bidirectional cables. A cable is **critical** (a bridge) if removing it disconnects two servers that were previously connected. Find all critical cables. The network may contain parallel cables (two cables joining the same pair); a cable that has a parallel twin is never critical. Print the number of critical cables and then each one as `a b` with `a < b`, sorted ascending by `a` and then by `b`.

**Input and output requirements:** Line 1: `n m`. Next `m` lines: `a b` (no self-loops). Output: first line the count; then one line per critical cable.

**Sample cases:**
```
Input:
4 4
0 1
1 2
2 0
1 3
Output:
1
1 3
```
```
Input:
5 5
0 1
1 2
2 3
3 4
4 2
Output:
2
0 1
1 2
```
```
Input:
3 3
0 1
0 1
1 2
Output:
1
1 2
```

**Constraints and edge cases:** 1 ≤ n ≤ 100000; 0 ≤ m ≤ 200000. The graph may be disconnected. A path graph of 100000 nodes makes a recursive depth-first search overflow the default stack, so plan for that. Checking each cable by re-running a search is O(m · (n + m)) and too slow.

### Question 3: Company-Style Programming (Multi-Step System)
**Title:** Smart Calendar

**Problem statement:** Implement a calendar for one room. Bookings are half-open intervals `[start, end)` with a unique string id. Commands:
- `BOOK id start end`: if the interval overlaps no existing booking, store it and print `BOOKED`. Otherwise print `CONFLICT <otherId>` where `otherId` is the id of the overlapping booking with the **smallest start time**; the new booking is not stored.
- `CANCEL id`: remove the booking and print `CANCELLED`, or print `NOTFOUND` if there is no such booking.
- `NEXTFREE t d`: print the smallest time `x ≥ t` such that the interval `[x, x + d)` overlaps no booking.

Overlap means sharing at least one instant, so `[10,20)` and `[20,30)` do not overlap.

**Input and output requirements:** Line 1: `q`. Then `q` commands. Output one line per command.

**Sample cases:**
```
Input:
12
BOOK a 10 20
BOOK b 15 25
BOOK c 20 30
BOOK d 5 10
NEXTFREE 0 5
NEXTFREE 0 6
NEXTFREE 12 3
CANCEL c
NEXTFREE 12 3
CANCEL zz
BOOK e 0 100
NEXTFREE 0 1
Output:
BOOKED
CONFLICT a
BOOKED
BOOKED
0
30
30
CANCELLED
20
NOTFOUND
CONFLICT d
0
```
```
Input:
6
BOOK p 0 5
BOOK q 5 10
NEXTFREE 0 1
NEXTFREE 3 2
CANCEL p
NEXTFREE 0 5
Output:
BOOKED
BOOKED
10
10
CANCELLED
0
```

**Constraints and edge cases:** 1 ≤ q ≤ 30000; 0 ≤ start < end ≤ 10^9; 1 ≤ d ≤ 10^9; times can be as large as 2·10^9 for `NEXTFREE` answers, so avoid `int` overflow in `x + d`. The overlap check on `BOOK` should be O(log n) with an ordered structure. Cancelling must really free the interval, and re-booking the same id after cancelling is allowed.

### Question 4: Predict the Output
Predict the exact output of this Java 17 program.

```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Main {
    static class P implements Comparable<P> {
        final String n;
        final int a;

        P(String n, int a) {
            this.n = n;
            this.a = a;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof P p && p.n.equals(n);
        }

        @Override
        public int hashCode() {
            return n.length();
        }

        @Override
        public int compareTo(P o) {
            return Integer.compare(a, o.a);
        }

        @Override
        public String toString() {
            return n + a;
        }
    }

    public static void main(String[] args) {
        P[] ps = {new P("bo", 3), new P("al", 3), new P("bo", 1), new P("cy", 2)};
        Set<P> hs = new HashSet<>(Arrays.asList(ps));
        TreeSet<P> ts = new TreeSet<>(Arrays.asList(ps));
        System.out.println(hs.size() + " " + ts + " " + ts.size());
        System.out.println(hs.contains(new P("al", 99)) + " " + ts.contains(new P("zz", 3))
                + " " + ts.first() + " " + ts.ceiling(new P("q", 2)) + " " + ts.higher(new P("q", 2)));

        Map<P, Integer> byName = new TreeMap<>(Comparator.comparing((P p) -> p.n));
        for (P p : ps) {
            byName.merge(p, 1, Integer::sum);
        }
        System.out.println(byName);

        List<Integer> li = new ArrayList<>(Arrays.asList(5, 6, 7, 8, 9));
        li.removeIf(x -> x % 2 == 0);
        li.replaceAll(x -> x * 2);
        Iterator<Integer> it = li.iterator();
        it.next();
        it.remove();
        System.out.println(li + " " + li.indexOf(18) + " " + li.contains(10));

        int[] a = {3, 1, 2};
        int[] b = a;
        Arrays.sort(b);
        System.out.println(a[0] + " " + Arrays.equals(a, b) + " " + (a == b));
        String s = null;
        System.out.println("x" + s + 1 + 2);
        try {
            li.forEach(v -> li.add(v));
        } catch (RuntimeException e) {
            System.out.println(e.getClass().getSimpleName() + " " + li.size());
        }
    }
}
```

### Question 5: Debug the Code
**What the program should do:** The classic "burst balloons" problem. You are given `n` balloons with values `nums[i]`. Bursting balloon `i` earns `left * nums[i] * right`, where `left` and `right` are the values of the balloons currently adjacent to `i` (an adjacent position beyond the ends counts as a balloon of value 1). Find the maximum total coins obtainable by bursting all balloons in the best order. Expected: `[3,1,5,8]` gives `167`; `[1,5]` gives `10`; `[7]` gives `7`; `[9,76,64,21]` gives `116718`; the empty array gives `0`.

The code returns wrong answers for most inputs. Find every bug, explain the recurrence it should implement, and write the corrected method.

```java
public class Main {
    static int maxCoins(int[] nums) {
        int n = nums.length;
        int[] a = new int[n + 2];
        a[0] = 1;
        a[n + 1] = 1;
        for (int i = 0; i < n; i++) {
            a[i + 1] = nums[i];
        }
        int[][] dp = new int[n + 2][n + 2];
        for (int i = n; i >= 1; i--) {
            for (int j = i; j <= n; j++) {
                for (int k = i; k <= j; k++) {
                    int coins = a[i] * a[k] * a[j] + dp[i][k - 1] + dp[k + 1][j];
                    dp[i][j] = Math.max(dp[i][j], coins);
                }
            }
        }
        return dp[1][n];
    }

    public static void main(String[] args) {
        System.out.println(maxCoins(new int[]{3, 1, 5, 8}));
        System.out.println(maxCoins(new int[]{1, 5}));
        System.out.println(maxCoins(new int[]{7}));
        System.out.println(maxCoins(new int[]{9, 76, 64, 21}));
        System.out.println(maxCoins(new int[]{}));
    }
}
```
