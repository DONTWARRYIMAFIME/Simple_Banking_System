import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int len = scanner.nextInt();

        int[][] mat = new int[2][len];

        for (int i = 0; i < len; i++) {
            mat[0][i] = scanner.nextInt();
        }

        for (int i = 0; i < len; i++) {
            mat[1][i] = scanner.nextInt();
        }

        int maxValue = mat[0][0] * mat[1][0];
        int maxId = 0;
        for (int i = 1; i < len; i++) {
            int currentValue = mat[0][i] * mat[1][i];
            if (maxValue < currentValue) {
                maxValue = currentValue;
                maxId = i;
            }
        }

        System.out.println(maxId + 1);

    }
}