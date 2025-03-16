import java.util.Scanner;

public class test1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] numbers = new int[20];

        // Read 20 numbers from the user
        System.out.println("Enter 20 integers between -32768 and 32767:");
        for (int i = 0; i < 20; i++) {
            numbers[i] = scanner.nextInt();
        }

        // Ask for the target number
        System.out.println("Enter a target number:");
        int target = scanner.nextInt();

        // Find the closest number
        int closest = numbers[0];
        int minDiff = Math.abs(numbers[0] - target);

        for (int i = 1; i < 20; i++) {
            int diff = Math.abs(numbers[i] - target);
            if (diff < minDiff) {
                minDiff = diff;
                closest = numbers[i];
            }
        }

        // Print results
        System.out.println("Target number: " + target);
        System.out.println("Closest number: " + closest);

        scanner.close();
    }
}
