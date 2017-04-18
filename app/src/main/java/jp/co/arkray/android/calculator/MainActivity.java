package jp.co.arkray.android.calculator;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;
import java.util.Stack;


import jp.co.arkray.android.calculator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityMainBinding binder;
    private StringBuffer collection = new StringBuffer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binder.btnClr.setOnClickListener(this);
        binder.btnDelete.setOnClickListener(this);
        binder.btnDot.setOnClickListener(this);
        binder.btnEqual.setOnClickListener(this);
        binder.btnDivide.setOnClickListener(this);
        binder.btnMinus.setOnClickListener(this);
        binder.btnMultiply.setOnClickListener(this);
        binder.btnPlus.setOnClickListener(this);
        binder.btnZero.setOnClickListener(this);
        binder.btnOne.setOnClickListener(this);
        binder.btnTwo.setOnClickListener(this);
        binder.btnThree.setOnClickListener(this);
        binder.btnFour.setOnClickListener(this);
        binder.btnFive.setOnClickListener(this);
        binder.btnSix.setOnClickListener(this);
        binder.btnSeven.setOnClickListener(this);
        binder.btnEight.setOnClickListener(this);
        binder.btnNine.setOnClickListener(this);
        //collection.append("-1.23/-2.45*-1.56+2019-5.123*5/7-22-0.24+55-2.5/2*5*3-5.4+55*53.6");
        //binder.tvInput.setText(String.valueOf(collection));
        //binder.hscrollview.fullScroll(HorizontalScrollView.FOCUS_RIGHT);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_clr:
                collection.replace(0, collection.length(), "0");
                binder.tvInput.setText(String.valueOf(collection));
                collection.delete(0, collection.length());

                break;
            case R.id.btn_delete:
                if (collection.length() > 0) {
                    collection.delete(collection.length() - 1, collection.length());
                    binder.tvInput.setText(String.valueOf(collection));
                } else {
                    collection.replace(0, 0, "0");
                    binder.tvInput.setText(String.valueOf(collection));
                }
                break;
            case R.id.btn_equal:
                String result = calculate(collection.toString());
                binder.tvInput.setText(result);
                binder.tvOutput.setText("");
                collection = new StringBuffer();
                collection.append(result);

                break;
            case R.id.btn_divide:
            case R.id.btn_minus:
            case R.id.btn_multiply:
            case R.id.btn_plus:
            case R.id.btn_dot:
            case R.id.btn_zero:
            case R.id.btn_one:
            case R.id.btn_two:
            case R.id.btn_three:
            case R.id.btn_four:
            case R.id.btn_five:
            case R.id.btn_six:
            case R.id.btn_seven:
            case R.id.btn_eight:
            case R.id.btn_nine:
                addDataToCollection(((Button) v).getText().toString());
                String temp = calculate(collection.toString());
                binder.tvOutput.setText(temp);
                break;
        }
    }

    private void addDataToCollection(String value) {
        int index = collection.length() - 1;

        if (value.matches("\\d")) {
            collection.append(value);
        } else if (value.matches("/|\\*|-|\\+")) {
            if (collection.length() > 0) {
                String lastValue = collection.substring(index);

                if (lastValue.matches("-") &&
                        collection.length() == 1 &&
                        value.matches("-|/|\\*|\\+")) {
                    //Do nothing
                } else if (lastValue.matches("/|\\*") && value.matches("-")) {
                    collection.append(value);
                } else if (lastValue.matches("-")) {
                    String prevValue = collection.substring(index - 1, index);
                    if (prevValue.matches("/|\\*") && value.matches("/|\\*|\\+")) {
                        collection.replace(index - 1, index + 1, value);
                    } else {
                        collection.replace(index, index + 1, value);
                    }

                } else if (lastValue.matches("\\+|/|\\*|\\.")) {
                    collection.replace(index, index + 1, value);
                } else if (lastValue.matches("\\d")) {
                    collection.append(value);
                }
            } else {
                if (value.contentEquals("-")) {
                    collection.append("-");
                }
            }
        } else {
            if (collection.length() > 0) {
                if (collection.toString().matches("-?\\d*")) {
                    collection.append(".");
                } else {
                    String[] arrayValue = collection.toString().split("/|\\*|-|\\+");
                    if (arrayValue.length > 0 && arrayValue[arrayValue.length - 1].matches("-?\\d*")) {
                        collection.append(".");
                    }
                }
            } else {
                collection.append("0.");
            }
        }

        binder.tvInput.setText(String.valueOf(collection));
        binder.hscrollview.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
    }

    public String calculate(String data) {

        String[] temp1 = data.split("(?<=/|\\*)-(\\d+\\.\\d+)|(?<=/|\\*)-(\\d+)|(?<=/|\\*)-(\\.\\d+)|(\\d+\\.\\d+)|(\\d+)|(\\.\\d+)");
        String[] temp2 = data.split("(?<=\\d|\\.)-|/|\\*|\\+");
        Log.d("full", data);

        Stack<String> operations = new Stack<>();
        Stack<String> values = new Stack<>();
        Stack<String> computation = new Stack<>();

        Collections.addAll(operations, temp1);
        Collections.addAll(values, temp2);

        if (data.substring(0, 1).contentEquals("-")) {
            operations.remove(0);
        }

        for (int i = 0; i < values.size(); i++) {
            computation.push(values.get(i).concat(" "));
            if (operations.size() > i) computation.push(operations.get(i).concat(" "));
        }
        ScientificCalculator rp = new ScientificCalculator(true);

        List<String> asPostfix = rp.convertToPostfix(computation);
        String asPostfix_str = asPostfix.toString().replaceAll("[|]|,", " ");
        asPostfix_str = asPostfix_str.substring(1, asPostfix_str.length() - 1);

        System.out.println("asPostfix " + asPostfix_str);
        String result = "NaN";
        try {
            rp.parseRPN(asPostfix_str);
            result = String.valueOf(rp.getResult());
            Log.d("RESULT", String.valueOf(rp.getResult()));
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            result = String.valueOf(e.getMessage());
        }
        return String.valueOf(result);

    }


}
