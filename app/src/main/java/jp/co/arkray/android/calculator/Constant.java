package jp.co.arkray.android.calculator;

import java.util.regex.Pattern;

/**
 * Created by admin on 4/18/2017.
 */

public class Constant {
    public static Pattern NUM_PATTERN = Pattern.compile("-?(\\d+\\.\\d+)|-?(\\d+)|-?(\\.\\d+)");
    public static Pattern OPERATOR_PATTERN = Pattern.compile("/|\\*|-|\\+");

    final static int PRECEDENCE_PLUS=  1;
    final static int PRECEDENCE_MINUS=  1;
    final static int PRECEDENCE_MULTIPLIY=  2;
    final static int PRECEDENCE_DIVIDE=  2;
    final static int PRECEDENCE_EXPONENT=  3;
    final static int PRECEDENCE_PARANTHESIS=  4;
}
