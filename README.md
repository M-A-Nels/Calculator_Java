# Calculator
Overview:
This is a simple calculator application written in Java that supports basic mathematical operations and a few special operations. It features a graphical user interface (GUI) with buttons for digits (0-9), operators (+, -, *, /, %, ^, and &), and functionality to evaluate mathematical expressions.

The calculator operates in two modes:

Polish Notation Mode: Users must input expressions in a specific format (Polish notation), where operators come before the operands (e.g., +12 instead of 1+2). Done in CalculatorV1

Standard Mode: Users can input traditional math expressions, which will be converted into the required format and evaluated. - Done in CalculatorV2

IMPORTANT:
Polish Notation Rules -
Operators precede the operands.
Only single digits are allowed in each expression.
No spaces are allowed in the expressions.

Features:

Digits and Operators: 
Buttons 1 through 9 and operators (+, -, *, /, %, ^, &) append their respective symbols to the expression displayed in the text box.
 Special operators meaning(%: Modulus, ^: Exponentiation, &:Sum up to given number inclusive)

Clear Button (C): Clears the expression currently displayed in the text box.
 
Answer Button (Ans): Recalls the result from the last evaluated expression.

Equals Button (=): Evaluates the expression in the text box and displays the result. If the expression is invalid, it will display Error.

Error Handling: If an invalid expression is input, or an error occurs during evaluation (such as division by zero), the calculator will display "Error" in the text box and gracefully handle the situation.
