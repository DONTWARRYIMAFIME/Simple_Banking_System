import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int len = scanner.nextInt();
        int[] arr = new int[len];

        for (int i = 0; i < len; i++) {
            arr[i] = scanner.nextInt();
        }

        int maxProduct = 0;

        for (int i = 0; i < len - 1; i++) {
            int currentProduct = arr[i] * arr[i + 1];
            if (currentProduct > maxProduct) {
                maxProduct = currentProduct;
            }
        }

        System.out.println(maxProduct);

    }
}