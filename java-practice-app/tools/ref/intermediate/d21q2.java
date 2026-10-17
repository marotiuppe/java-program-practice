import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int r = sc.nextInt(), c = sc.nextInt();
        int[][] m = new int[r][c];
        for (int i = 0; i < r; i++) for (int j = 0; j < c; j++) m[i][j] = sc.nextInt();
        int top = 0, bot = r - 1, left = 0, right = c - 1;
        List<Integer> out = new ArrayList<>();
        while (top <= bot && left <= right) {
            for (int j = left; j <= right; j++) out.add(m[top][j]);
            top++;
            for (int i = top; i <= bot; i++) out.add(m[i][right]);
            right--;
            if (top <= bot) {
                for (int j = right; j >= left; j--) out.add(m[bot][j]);
                bot--;
            }
            if (left <= right) {
                for (int i = bot; i >= top; i--) out.add(m[i][left]);
                left++;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < out.size(); i++) {
            if (i > 0) sb.append(' ');
            sb.append(out.get(i));
        }
        System.out.println(sb);
    }
}
