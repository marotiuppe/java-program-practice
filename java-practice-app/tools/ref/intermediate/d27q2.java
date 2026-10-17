import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        long take = 0, skip = 0;
        for (int i = 0; i < n; i++) {
            long a = sc.nextLong();
            long nt = skip + a;
            skip = Math.max(skip, take);
            take = nt;
        }
        System.out.println(Math.max(take, skip));
    }
}
