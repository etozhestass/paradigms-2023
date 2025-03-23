package expression.exceptions;

import expression.TripleExpression;

import java.util.Scanner;

public class MyTest {
    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        TripleParser parser = new ExpressionParser();
        TripleExpression expression = parser.parse(in.nextLine());
        System.out.println(expression.getClass());
        System.out.println(expression.toMiniString());
        System.out.print(expression.evaluate(1, 1, 1));
    }
}
