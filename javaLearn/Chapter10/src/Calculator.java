public class Calculator {
    public int number1;

    public int number2;

    public String operation;

    public Calculator(int number1, int number2, String operation) {
        this.number1 = number1;
        this.number2 = number2;
        this.operation = operation;
    }

    public int calculate() {
        switch (operation) {
            case "+": return number1 + number2;
            case "-": return number1 - number2;
            case "*": return number1 * number2;
            case "/": return number1 / number2;
            default: return 0;
        }
    }
}
