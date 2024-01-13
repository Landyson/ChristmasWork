import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean validExpression = false;
        String expression = "";
        System.out.println("!If you want to multilpy you need to use symbol: * !");
        System.out.println("Supported functions: power of ^, sqrt(), sin(), cos(), tan(), log(), exp()");

        while (!validExpression) {
            System.out.print("Enter a mathematical expression: ");
            expression = scanner.nextLine();

            if (Calculator.isValidExpression(expression)) {
                validExpression = true;
            } else {
                System.out.println("Invalid expression. Use only permitted characters for mathematical operations.");
            }
        }

        double result = Calculator.evaluateExpression(expression);
        System.out.println("Result: " + result);

        scanner.close();
    }
}