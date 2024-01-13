import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
    /**
     * Method to validate the correctness of a mathematical expression.
     *
     * @param expression Mathematical expression to be validated.
     * @return True if the expression is valid; otherwise false.
     */
    public static boolean isValidExpression(String expression) {
        String regex = "(?:sqrt|sin|cos|tan|log|exp)\\(\\d+(\\.\\d+)?\\)|\\^\\d+(\\.\\d+)?|\\d+(\\.\\d+)?[+\\-*/]\\d+(\\.\\d+)?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(expression);
        return matcher.find();
    }

    /**
     * Method to evaluate a mathematical expression and return the result.
     *
     * @param expression Mathematical expression to be evaluated.
     * @return The result of evaluating the mathematical expression.
     * @throws RuntimeException If an unexpected error occurs during expression evaluation.
     */
    public static double evaluateExpression(String expression) {
        return new Object() {
            int pos = -1, ch;
            String expr = expression;

            void nextChar() {
                ch = (++pos < expr.length()) ? expr.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < expr.length()) throw new RuntimeException("An unexpected character: " + (char) ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (eat('*')) x *= parseFactor();
                    else if (eat('/')) {
                        double divisor = parseFactor();
                        if (divisor == 0) {
                            System.out.println("Cannot divide by zero.");
                            System.exit(0);
                        }
                        if (x == 0) {
                            System.out.println("Zero cannot be devided.");
                            System.exit(0);
                        }
                    } else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();

                double x;
                int startPos = pos;
                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(expr.substring(startPos, pos));
                } else if (ch >= 'a' && ch <= 'z') {
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = expr.substring(startPos, pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(x);
                    else if (func.equals("cos")) x = Math.cos(x);
                    else if (func.equals("tan")) x = Math.tan(x);
                    else if (func.equals("log")) x = Math.log(x);
                    else if (func.equals("exp")) x = Math.exp(x);
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("An unexpected character: " + (char) ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor());

                return x;
            }
        }.parse();
    }
}
