package expression.parser;

import expression.TripleExpression;
import expression.exceptions.TripleParser;

import java.util.Scanner;

public class MyTest {
    public static void main(String[] argv) throws Exception {
        Scanner in = new Scanner(System.in);
        TripleParser parser = new ExpressionParser();
        TripleExpression expression = parser.parse(in.nextLine());
        System.out.println(expression.toString());
        System.out.println(expression.toMiniString());
        System.out.println(expression.evaluate(1, 1, 1));
    }
}
