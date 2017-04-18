package jp.co.arkray.android.calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by admin on 4/18/2017.
 */

public class ScientificCalculator {

    boolean debug;
    final String operands = "^*+-/";
    double result;


    public ScientificCalculator(boolean db) {
        debug = db;

    }

    public boolean isOperand(String s, boolean allowParanethesis) {
        s = s.trim();
        if (s.length() != 1)
            return false;
        if (allowParanethesis && (s.equals("(") || s.equals(")")))
            return true;
        else return operands.indexOf(s) != -1;
    }


    public boolean isNumber(String s) {
        String master = "-0123456789.";
        s = s.trim();

        for (int i = 0; i < s.length(); i++) {
            String lttr = s.substring(i, i + 1);
            if (master.indexOf(lttr) == -1)
                return false;
        }
        return true;
    }


    public void parseRPN(String input) {

        String rpnStr = input;
        String[] tokens = rpnStr.split("\\s+");//remove all white space
        Stack<Double> numberStack = new Stack<Double>();

        boolean bAllowParenthesis = false;
        for (String token : tokens) {
            if (token.equals("-") == false && isNumber(token)) {
                double d = Double.parseDouble(token);
                numberStack.push(d);
            } else if (isOperand(token, bAllowParenthesis)) {
                if (numberStack.size() < 2) {
                    System.out.println("Invalid Syntax, operator " + token + " must be preceeded by at least two operands");
                    return;
                }
                double num1 = numberStack.pop();
                double num2 = numberStack.pop();
                double result = this.calculate(num2, num1, token);
                numberStack.push(result);
            } else if (token.trim().length() > 0)
                System.out.println(token + " is invalid, only use numbers or operators ");
        }
        result = numberStack.pop();

    }


    double getResult() {
        return result;
    }


    private Double calculate(double num1, double num2, String op) {
        if (op.equals("+"))
            return num1 + num2;
        else if (op.equals("-"))
            return num1 - num2;
        else if (op.equals("*"))
            return num1 * num2;
        else if (op.equals("^"))
            return Math.pow(num1, num2);
        else if (op.equals("/")) {
            if (num2 == 0)
                throw new ArithmeticException("Division by zero!");
            return num1 / num2;
        } else {
            System.out.println(op + " is not a supported operand");
            return null;
        }
    }

    public List<String> convertToPostfix(Stack data){
        int priority = 0;
        String postfixBuffer = "";
        Stack<String> stack = new Stack<String>();
        List<String> postfixArray = new ArrayList<String>();

        for (int i = 0; i < data.size(); i++) {
            String op = data.get(i).toString();
            if (op.matches(Constant.OPERATOR_PATTERN.pattern())) {

                if (postfixBuffer.length() > 0) {
                    postfixArray.add(postfixBuffer);
                }
                postfixBuffer = "";
                if (stack.size() <= 0)
                    stack.push(op);
                else {
                    String opTop = stack.peek();
                    if (opTop.contentEquals("*") || opTop.contentEquals("/"))
                        priority = 1;
                    else
                        priority = 0;
                    if (priority == 1) {
                        if (op.contentEquals("+") || op.contentEquals("-")) {
                            postfixArray.add(String.valueOf(stack.pop()));
                            i--;
                        } else {
                            postfixArray.add(String.valueOf(stack.pop()));
                            i--;
                        }
                    } else {
                        if (op.contentEquals("+") || op.contentEquals("-")) {
                            postfixArray.add(String.valueOf(stack.pop()));
                            stack.push(op);
                        } else
                            stack.push(op);
                    }
                }
            } else {
                postfixBuffer += op;
            }
        }
        postfixArray.add(postfixBuffer);
        int len = stack.size();
        for (int j = 0; j < len; j++)
            postfixArray.add(stack.pop());

        return postfixArray;
    }
}
