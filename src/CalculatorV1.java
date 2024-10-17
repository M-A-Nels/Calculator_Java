import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Version - Simple input - type the expression in form accepted by logic in README - left to right input
// Works on digits 1 - 9 gives error if expression contains numbers larger than 9 in expression statement
public class CalculatorV1 implements ActionListener {
    static JTextField display;

    String expression = "";
    String displayExpression = "";
    String prevAns = "";

    public static void main(String[] args) {
        CalculatorV1 c = new CalculatorV1();
        c.Calculator();
    }

    public void Calculator() {
        Pattern Digit =  Pattern.compile("[0-9]{1}");

        JFrame frame = new JFrame("CalculatorV1");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 400);
        frame.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        display = new JTextField();
        display.setPreferredSize(new Dimension(300, 50));
        display.setEditable(true);
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
        if(command.equals("C"))
        {
            expression = "";
            displayExpression = "";
            display.setText(displayExpression);
        }
        // evaluate button
        else if(command.equals("="))
        {//try to evaluate
            try
            {
                String result;
                //display previous answer
                if(expression.equals(prevAns) && displayExpression.equals("Ans"))
                {
                    result = prevAns;
                }
                else // calculate expression
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
        {//when Ans pressed
            if(command.equals("Ans"))
            {
                expression = expression + prevAns;
                displayExpression = displayExpression + "Ans";
            }
            //input number or operator
            else
            {
                expression = expression + command;
                displayExpression = displayExpression + command;
            }
            display.setText(displayExpression);
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
        if(Number > 0)
        {
            return Number +sum(Number-1);
        }
        else
        {
            return Number;
        }
    }

    //calculate expression - based on Lab 9 Q2
    public int calculate(String expression) throws IllegalExpression
    {
        Pattern Digit =  Pattern.compile("[0-9]");
        Pattern SymbolDigPattern = Pattern.compile("([+\\-*/^%][0-9])");
        Pattern SymbolNonDig_DigPattern = Pattern.compile("(&("+SymbolDigPattern+"|"+Digit+"))");
        Pattern pattern = Pattern.compile(SymbolDigPattern+"|"+SymbolNonDig_DigPattern);

        int ans;
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
        else
        {
            Matcher matcher = pattern.matcher(expression);
            if(matcher.lookingAt())
            {
                if(expression.charAt(0) == '&')
                {
                    ans = calculate(expression.substring(1));
                }
                else
                {
                    ans = calculate(expression.substring(2));
                }
            }
            else
            {
                throw new IllegalExpression("Error");
            }

            try{
                if(expression.charAt(0) == '&')
                {
                    ans = sum(ans);
                }
                else
                {
                    int digit = Character.getNumericValue(expression.charAt(1));
                    if(expression.charAt(0) == '+')
                    {
                        ans += digit;
                    }
                    else if(expression.charAt(0) == '-')
                    {
                        ans = digit - ans;
                    }
                    else if(expression.charAt(0) == '*')
                    {
                        ans = digit * ans;
                    }
                    else if(expression.charAt(0) == '/')
                    {
                        ans = digit / ans;
                    }
                    else if(expression.charAt(0) == '^')
                    {
                        ans = (int)Math.pow(digit, ans);
                    }
                    else if(expression.charAt(0) == '%')
                    {
                        ans = digit % ans;
                    }
                }
            }
            catch(ArithmeticException e)
            {
                throw new IllegalExpression("Error");
            }

        }
        return ans;
    }

}