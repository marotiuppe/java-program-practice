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
        return data[--top];
    }

    int peek() {
        if (top == 0) {
            return -1;
        }
        return data[top - 1];
    }

    boolean isEmpty() {
        return top == 0;
    }

    int size() {
        return top;
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
