public class CalculatorTest {
    public static void main(String[] args) {
        Calculator c1 = new Calculator(30, 72, "*");
        int num1 = c1.calculate();
        Calculator c2 = new Calculator(num1, 3, "/");
        int num2 = c2.calculate();
        System.out.println(num2);
    }
}
