import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Version 2 - translate normal maths input into form accepted by logic from Lab 9 Q2 EG type 3-5 -> displays -35
// builds expression right to left
// Works on digits 1 - 9 gives error if expression contains numbers larger than 9 in expression statement
public class CalculatorV2 implements ActionListener {
    static JTextField display;

    String expression = "";
    String displayExpression = "";
    String prevAns = "";
    String currentexp = "";
    String displayCurrentexp = "";

    public static void main(String[] args) {
        CalculatorV2 c = new CalculatorV2();
        c.Calculator();
    }

    public void Calculator() {
        Pattern Digit =  Pattern.compile("[0-9]{1}");

        JFrame frame = new JFrame("CalculatorV2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 400);
        frame.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        display = new JTextField();
        display.setPreferredSize(new Dimension(300, 50));
        display.setEditable(false);
        display.setHorizontalAlignment(JTextField.RIGHT);
        panel.add(display, BorderLayout.NORTH);

        //set display answer field background
        display.setBackground(Color.lightGray);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(5, 4, 5, 5));
        String[] buttonLabels = {
                "Ans", "&", "^", "%",
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "C", "0", "=", "+"
        };
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            buttonsPanel.add(button);
            button.addActionListener(this);
            //set button colour
            button.setBackground(Color.pink);

            if(label.equals("C")) //set clear button colour
            {
                Color color = new Color(159, 45, 48);
                button.setBackground(color);
                button.setForeground(Color.white);
            }
            else if(label.equals("Ans"))
            {//set Ans button colour
                button.setBackground(Color.darkGray);
                button.setForeground(Color.white);
            }
            else if (label.equals("="))
            {// set equals button colour
                Color color = new Color(178,194,72);
                button.setBackground(color);
            }
            else
            {   //Set numbers' button colour
                Matcher matcher = Digit.matcher(label);
                if(matcher.find())
                {
                    Color color = new Color(173, 216, 230);
                    button.setBackground(color);
                }
            }
        }

        panel.add(buttonsPanel, BorderLayout.CENTER);

        frame.getContentPane().add(panel);
        frame.setVisible(true);

        //set button panel background colour
        buttonsPanel.setBackground(Color.gray);
    }

    //button action
    @Override
    public void actionPerformed(ActionEvent e) {
        //reset display settings
        display.setBackground(Color.lightGray);
        display.setForeground(Color.black);

        String command = e.getActionCommand();
        display.setText(command);

        //clear button
        if (command.equals("C")) {
            expression = "";
            displayExpression = "";
            currentexp = "";
            displayCurrentexp = "";
            display.setText(displayExpression);
        }
        //evaluate button
        else if (command.equals("=")){
            //if the input not yet added
            if (currentexp != "")
            {
                expression = currentexp + expression;
                currentexp = "";
                displayCurrentexp = "";
            }

            //try to evaluate
            try
            {
                String result;
                //display prev answer
                if(expression.equals(prevAns)&& displayExpression.equals("Ans"))
                {
                    result = prevAns;
                }
                else // calc expression
                {
                    result = Integer.toString(calculate(expression));
                }
                prevAns = result;
                display.setText(result);
            }//if something goes wrong
            catch(IllegalExpression IE)
            {
                display.setText(IE.getMessage());
            }
            //reset calc
            expression = "";
            displayExpression = "";
        }
        else
        {
            //input for expression - operator
            if (currentexp.equals("^")||currentexp.equals("%")||command.equals("/")||command.equals("*")||command.equals("-")||command.equals("+"))
            {
                displayCurrentexp = command + displayCurrentexp;
                currentexp = command + currentexp;
                display.setText(displayCurrentexp+displayExpression);
            }// & operator
            else if(command.equals("&"))
            {
                currentexp = "&" + currentexp;
                displayCurrentexp = "&" + displayCurrentexp;
                display.setText(displayCurrentexp+displayExpression);
            }
            else
            {// when ANS pressed
                if (command.equals("Ans"))
                {
                    currentexp = currentexp + prevAns;
                    displayCurrentexp = displayCurrentexp + "Ans";
                }
                else //input number
                {
                    currentexp = currentexp + command;
                    displayCurrentexp = displayCurrentexp + command;
                }
                display.setText(displayCurrentexp+displayExpression);
            }

            //adds first singular input to final expression
            if(expression.equals(""))
            {
                expression = currentexp;
                displayExpression = displayCurrentexp;
                currentexp = "";
                displayCurrentexp = "";
                display.setText(displayExpression);
            }
            else
            {
                //format add &
                if(currentexp.equals("&"))
                {
                    expression = currentexp + expression;
                    currentexp = "";
                    displayExpression = displayCurrentexp + displayExpression;
                    displayCurrentexp = "";
                    display.setText(displayExpression);

                }//format add current expression to first single input
                else if (currentexp.length() == 2 && expression.length() == 1)
                {
                    expression = currentexp.substring(0,1) + expression + currentexp.substring(1);
                    currentexp = "";
                    displayExpression = displayCurrentexp.substring(0, 1) + displayExpression + displayCurrentexp.substring(1);
                    displayCurrentexp = "";
                    display.setText(displayExpression);

                }// add current expression to rest
                else if (currentexp.length() == 2)
                {
                    expression = currentexp + expression;
                    displayExpression =  displayCurrentexp + displayExpression;
                    currentexp = "";
                    displayCurrentexp = "";
                    display.setText(displayExpression);
                }
            }
        }

        //set display to red if error in evaluation
        if(display.getText().equals("Error"))
        {
            display.setBackground(Color.red);
            display.setForeground(Color.white);
        }

    }

    //sum up to
    public static int sum(int Number)
    {
        if (Number > 0)
        {
            return Number + sum(Number - 1);
        }
        else
        {
            return Number;
        }
    }

    //calculate expression
    public int calculate(String expression) throws IllegalExpression
    {
        Pattern Digit =  Pattern.compile("[0-9]{1}");
        Pattern SymbolDigPattern = Pattern.compile("([\\+\\-\\*\\/\\^\\%]{1}[0-9]{1})");
        Pattern SymbolNonDig_DigPattern = Pattern.compile("([\\&]{1}("+SymbolDigPattern+"|"+Digit+"))");
        Pattern pattern = Pattern.compile(SymbolDigPattern+"|"+SymbolNonDig_DigPattern);

        int ans = -1;
        if(expression.length() == 1)
        {
            Matcher matcher = Digit.matcher(expression);
            if(matcher.find())
            {
                return Integer.parseInt(expression);
            }
            else
            {
                throw new IllegalExpression("Error");
            }
        }
        else {
            Matcher matcher = pattern.matcher(expression);
            if (matcher.lookingAt()) {
                if (expression.charAt(0) == '&') {
                    ans = calculate(expression.substring(1));
                } else {
                    ans = calculate(expression.substring(2));
                }
            } else {
                throw new IllegalExpression("Error");
            }

            try {
                if (expression.charAt(0) == '&') {
                    ans = sum(ans);
                } else {
                    int digit = Character.getNumericValue(expression.charAt(1));
                    if (expression.charAt(0) == '+') {
                        ans += digit;
                    } else if (expression.charAt(0) == '-') {
                        ans = digit - ans;
                    } else if (expression.charAt(0) == '*') {
                        ans = digit * ans;
                    } else if (expression.charAt(0) == '/') {
                        ans = digit / ans;
                    } else if (expression.charAt(0) == '^') {
                        ans = (int) Math.pow(digit, ans);
                    } else if (expression.charAt(0) == '%') {
                        ans = digit % ans;
                    }
                }
            } catch (ArithmeticException e) {
                throw new IllegalExpression("Error");
            }
        }
        return ans;



    }


}
