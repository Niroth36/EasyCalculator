package com.example.easycalculator;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.Stack;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView resultTv, solutionTv;
    MaterialButton buttonC, buttonBrackOpen, buttonBrackClose;
    MaterialButton buttonDivide, buttonMultiply, buttonPlus, buttonMinus, buttonEquals;
    MaterialButton button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    MaterialButton buttonAC, buttonDot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        resultTv = findViewById(R.id.result_tv);
        solutionTv = findViewById(R.id.result_tv);

        assignId(buttonC, R.id.button_c);
        assignId(buttonBrackOpen, R.id.button_open_bracket);
        assignId(buttonBrackClose, R.id.button_close_bracket);
        assignId(buttonDivide, R.id.button_divide);
        assignId(buttonMultiply, R.id.button_multiply);
        assignId(buttonPlus, R.id.button_plus);
        assignId(buttonMinus, R.id.button_minus);
        assignId(buttonEquals, R.id.button_equals);
        assignId(button0, R.id.button_0);
        assignId(button1, R.id.button_1);
        assignId(button2, R.id.button_2);
        assignId(button3, R.id.button_3);
        assignId(button4, R.id.button_4);
        assignId(button5, R.id.button_5);
        assignId(button6, R.id.button_6);
        assignId(button7, R.id.button_7);
        assignId(button8, R.id.button_8);
        assignId(button9, R.id.button_9);
        assignId(buttonAC, R.id.button_ac);
        assignId(buttonDot, R.id.button_dot);


    }

    void assignId(MaterialButton btn, int id) {
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        MaterialButton button = (MaterialButton) view;
        String buttonText = button.getText().toString();
        String dataToCalculate = solutionTv.getText().toString();

        // Clear everything on AC button
        if (buttonText.equals("AC")) {
            solutionTv.setText("");
            resultTv.setText("0");
            return;
        }

        // Backspace functionality for C button
        if (buttonText.equals("C")) {
            if (!dataToCalculate.isEmpty()) {
                dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1);
                solutionTv.setText(dataToCalculate);
            }
            return;
        }

        // Perform calculation only when "=" is pressed
        if (buttonText.equals("=")) {
            String finalResult = getResult(dataToCalculate);
            resultTv.setText(finalResult);
            return;
        }

        // Prevent leading zero for the first number
        if (dataToCalculate.isEmpty() && buttonText.equals("0")) {
            return;
        }

        // Handle multiple decimals: Allow one decimal per number
        if (buttonText.equals(".")) {
            if (dataToCalculate.isEmpty() || dataToCalculate.endsWith(" ")) {
                // Automatically prepend a leading zero for a decimal starting a number
                dataToCalculate += "0.";
            } else {
                // Check if the current number already contains a decimal
                String[] parts = dataToCalculate.split("[+\\-*/]");
                String currentNumber = parts[parts.length - 1];
                if (currentNumber.contains(".")) {
                    return; // Skip if the current number already has a decimal
                }
                dataToCalculate += ".";
            }
            solutionTv.setText(dataToCalculate);
            return;
        }

        // Append input for other buttons
        if ("+-*/".contains(buttonText)) {
            dataToCalculate += " " + buttonText + " "; // Add spaces around operators for clarity
        } else {
            dataToCalculate += buttonText;
        }

        solutionTv.setText(dataToCalculate);
    }

    String getResult(String data) {
        try {
            // Replace invalid symbols for consistency
            data = data.replace("÷", "/").replace("×", "*");

            // Create stacks for numbers and operators
            Stack<Double> numbers = new Stack<>();
            Stack<Character> operators = new Stack<>();
            int i = 0;

            while (i < data.length()) {
                char ch = data.charAt(i);

                if (Character.isDigit(ch) || ch == '.') {
                    // Parse the number
                    StringBuilder num = new StringBuilder();
                    while (i < data.length() && (Character.isDigit(data.charAt(i)) || data.charAt(i) == '.')) {
                        num.append(data.charAt(i));
                        i++;
                    }
                    numbers.push(Double.parseDouble(num.toString()));
                    continue;
                } else if (ch == '(') {
                    operators.push(ch);
                } else if (ch == ')') {
                    // Solve the expression inside parentheses
                    while (operators.peek() != '(') {
                        numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
                    }
                    operators.pop();
                } else if (isOperator(ch)) {
                    // Handle operator precedence
                    while (!operators.isEmpty() && precedence(ch) <= precedence(operators.peek())) {
                        numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
                    }
                    operators.push(ch);
                }
                i++;
            }

            // Solve the remaining operations
            while (!operators.isEmpty()) {
                numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
            }

            // Format the result
            double result = numbers.pop();
            if (result == (long) result) {
                return String.valueOf((long) result);
            } else {
                return String.valueOf(result);
            }
        } catch (Exception e) {
            return "Err";
        }
    }

    boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    int precedence(char operator) {
        if (operator == '+' || operator == '-') return 1;
        if (operator == '*' || operator == '/') return 2;
        return -1;
    }

    double applyOperation(char operator, double b, double a) {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                return b != 0 ? a / b : Double.POSITIVE_INFINITY;
            default:
                return 0;
        }
    }
}
