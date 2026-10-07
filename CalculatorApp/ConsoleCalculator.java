import java.util.Scanner;

public class ConsoleCalculator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=================================");
        System.out.println("       MINI CLI CALCULATOR       ");
        System.out.println("=================================");

        while (running) {
            System.out.println("\nSelect an operation:");
            System.out.println("  1. Addition (+)");
            System.out.println("  2. Subtraction (-)");
            System.out.println("  3. Multiplication (*)");
            System.out.println("  4. Division (/)");
            System.out.println("  5. Modulo (%)");
            System.out.println("  6. Power (^)");
            System.out.println("  0. Exit");
            System.out.print("Choose [0-6]: ");

            String choice = scanner.next();

            if (choice.equals("0")) {
                running = false;
                System.out.println("Exiting calculator. Goodbye!");
                break;
            }

            if (!choice.matches("[1-6]")) {
                System.out.println("Invalid option! Please enter a number from 0 to 6.");
                continue;
            }

            System.out.print("Enter first number: ");
            while (!scanner.hasNextDouble()) {
                System.out.print("Invalid input. Enter a valid number: ");
                scanner.next();
            }
            double a = scanner.nextDouble();

            System.out.print("Enter second number: ");
            while (!scanner.hasNextDouble()) {
                System.out.print("Invalid input. Enter a valid number: ");
                scanner.next();
            }
            double b = scanner.nextDouble();

            double result = 0;
            boolean error = false;

            switch (choice) {
                case "1":
                    result = a + b;
                    System.out.printf("Result: %.4f + %.4f = %.4f%n", a, b, result);
                    break;
                case "2":
                    result = a - b;
                    System.out.printf("Result: %.4f - %.4f = %.4f%n", a, b, result);
                    break;
                case "3":
                    result = a * b;
                    System.out.printf("Result: %.4f * %.4f = %.4f%n", a, b, result);
                    break;
                case "4":
                    if (b == 0) {
                        System.out.println("Error: Division by zero is not allowed.");
                        error = true;
                    } else {
                        result = a / b;
                        System.out.printf("Result: %.4f / %.4f = %.4f%n", a, b, result);
                    }
                    break;
                case "5":
                    if (b == 0) {
                        System.out.println("Error: Modulo by zero is not allowed.");
                        error = true;
                    } else {
                        result = a % b;
                        System.out.printf("Result: %.4f %% %.4f = %.4f%n", a, b, result);
                    }
                    break;
                case "6":
                    result = Math.pow(a, b);
                    System.out.printf("Result: %.4f ^ %.4f = %.4f%n", a, b, result);
                    break;
            }
        }

        scanner.close();
    }
}
