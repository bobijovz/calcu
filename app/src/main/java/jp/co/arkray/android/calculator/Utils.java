package jp.co.arkray.android.calculator;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * Created by admin on 4/19/2017.
 */

public class Utils {


    public ArrayList<String> convertToPostfix(String data) {
        Pattern op = Pattern.compile("(?<=/|\\*)-(\\d+\\.\\d+)|(?<=/|\\*)-(\\d+)|(?<=/|\\*)-(\\.\\d+)|(\\d+\\.\\d+)|(\\d+)|(\\.\\d+)");
        Pattern val = Pattern.compile("(?<=\\d|\\.)-|/|\\*|\\+");

        Stack<String> dataSplit = new Stack<>();
        Stack<String> tempOperatorStack = new Stack<>();
        Stack<String> opArray = new Stack<>();
        Stack<String> valArray = new Stack<>();
        ArrayList<String> postFix = new ArrayList<>();

        Collections.addAll(valArray, data.split(val.pattern()));
        Collections.addAll(opArray, data.split(op.pattern()));


        for (int i = 0; i < valArray.size(); i++) {
            dataSplit.push(valArray.get(i));
            if (opArray.size() > i + 1)dataSplit.push(opArray.get(i + 1));

        }

        for (String token : dataSplit) {
            if (token.matches(Constant.OPERATOR_PATTERN.pattern())) {
                if (tempOperatorStack.empty()){
                    tempOperatorStack.push(token);
                } else {
                    if (opToPrecedence(tempOperatorStack.peek()) >= opToPrecedence(token)){
                        while (tempOperatorStack.size() > 0 && opToPrecedence(tempOperatorStack.peek()) >= opToPrecedence(token)){
                            postFix.add(tempOperatorStack.pop());
                        }
                        tempOperatorStack.push(token);
                    } else if (opToPrecedence(tempOperatorStack.peek()) < opToPrecedence(token)){
                        tempOperatorStack.push(token);
                    }
                }
            } else {
                postFix.add(token);

            }
        }
        while (tempOperatorStack.size() > 0 ){
            postFix.add(tempOperatorStack.pop());
        }
        return postFix;
    }

    private int opToPrecedence(String op) {
        switch (op) {
            case "+":
                return Constant.PRECEDENCE_PLUS;
            case "-":
                return Constant.PRECEDENCE_MINUS;
            case "*":
                return Constant.PRECEDENCE_MULTIPLIY;
            case "/":
                return Constant.PRECEDENCE_DIVIDE;
            default:
                return 0;
        }

    }

    private Double calculate(double num1, double num2, String op) {
        switch (op) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
                return num1 * num2;
            case "/":
                if (num2 == 0)
                    throw new ArithmeticException("Division by zero!");
                return num1 / num2;
            default:
                System.out.println(op + " is not a supported operand");
                return null;
        }
    }

    public Double parseData(ArrayList<String> data){
        Stack<Double> numberStack =new Stack<Double>();
        for (String token: data){
            if ( token.matches(Constant.NUM_PATTERN.pattern())) {
                double d = Double.parseDouble(token);
                numberStack.push(d);
            } else {
                if (numberStack.size() < 2) {
                    System.out.println("Invalid Syntax, operator " + token + " must be preceeded by at least two operands");
                    return 0.0;
                }
                double num1 = numberStack.pop();
                double num2 = numberStack.pop();
                double result = calculate(num2, num1, token);
                numberStack.push(result);
            }

        }
        return numberStack.pop();
    }
}
