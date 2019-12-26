package calc;


public class Calculator {

    private String strCurrentNumber = "";
    private String strDisplay = "0";
    private double memory = 0D;
    private double totalNum = 0D;
    private double squareRoot = 0D;
    private double doubleOperationNum = 0D;
    private double percentageFirstNum = 0D; //Used in percentage calculations e.g. 50+25%=, FirstNum is 50, SecondNum is 25, previousOperation is +
    private double percentageSecondNum = 0D;
    private char currentOperation = '+';
    private char previousOperation = '+';
    private char doubleOperation = '='; //Used for double operations, e.g. ++, **
    private boolean equalsButtonPressed = false; //When true, if next button pressed is a number, everything is reset and treated as a new sum
    private boolean zeroNumPressed = false; //When true the chop method will not remove trailing 0's
    private boolean decimalPlaceUsed = false;

    public void objCalculator() {
    }

    protected void memorySave() {
        memory = totalNum; //Saves the total Answer inside memory
    }

    protected void memoryCall() {

        strCurrentNumber = Double.toString(memory); //Sets the current number being entered to number stored in memory
        updateDisplayString();

    }

    protected void checkButtonPressed(char currentChar) {

        switch (currentChar) {

            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case '0':
                checkNumberChosen(currentChar);
                break;
            case '+':
            case '-':
            case '*':
            case '/':
            case '%':
                checkOperationChosen(currentChar);
                break;
            case '.':
                checkDecimalChosen(currentChar);
                break;
            case '=':
                checkEqualsOperation(currentChar);
                break;
            case 'c': resetEverything(); break;

        }

    }

    private void checkNumberChosen(char currentChar) {

        if (equalsButtonPressed) //If equals button was last pressed, treat this as a new calculation (reset everything)
        {
            resetEverything();
        }

        if (!(currentChar == '0' && strCurrentNumber.equals(""))) //If not using 0 as the first number
        {

            strCurrentNumber += currentChar;

            if (currentChar == '0') {
                zeroNumPressed = true; //Stops the chop method removing the current 0 from the display
            }

            updateDisplayString();

        }

    }

    private void checkOperationChosen(char currentChar) {

        previousOperation = currentOperation; //Last operation chosen (one about to be used) is put in previous variable
        currentOperation = currentChar;

        equalsButtonPressed = false;

        if (strCurrentNumber.equals("") && (previousOperation == currentOperation || totalNum == 0)) { //Empty, ignore the entered operation			
        } else if (strCurrentNumber.equals("") && previousOperation != currentOperation) {
            previousOperation = currentOperation; //Allow operation changing
            doubleOperation = '='; //Turns off double operation flag
        } else if (currentOperation == '%') {

            if (totalNum == 0) {
                strCurrentNumber = "";
            } else {

                percentageFirstNum = totalNum;
                percentageSecondNum = Double.parseDouble(strCurrentNumber);
                totalNum = (percentageFirstNum / 100) * percentageSecondNum;

            }

            doubleOperation = '=';

        } else {

            performCalculation(previousOperation, Double.parseDouble(strCurrentNumber));
            previousOperation = currentOperation;
            doubleOperation = '=';

        }

        strCurrentNumber = Double.toString(totalNum); //Put total answer in strCurrentNumber so it can be displayed
        decimalPlaceUsed = false;
        updateDisplayString();
        strCurrentNumber = ""; //Reset strCurrentNumber so a new number can be entered

    }

    private void checkDecimalChosen(char currentChar) {

        if (equalsButtonPressed) {
            resetEverything();
        }

        if (!decimalPlaceUsed) {

            strCurrentNumber += currentChar;
            decimalPlaceUsed = true;
            updateDisplayString();

        }

    }

    private void checkEqualsOperation(char currentChar) {

        if (currentOperation == '%') {

            totalNum = percentageFirstNum;
            performCalculation(previousOperation, ((percentageFirstNum / 100) * percentageSecondNum));

        } else if (strCurrentNumber.equals("") && previousOperation == currentOperation) {

            doubleOperation = currentOperation;
            doubleOperationNum = totalNum;

        } else if (strCurrentNumber.equals("") && totalNum == 0) {

            strCurrentNumber = "0";
            currentOperation = '+';

        }

        if (doubleOperation != '=') //If double operation is turned on
        {
            performCalculation(doubleOperation, doubleOperationNum);
        } else if (currentOperation != '%') {
            performCalculation(currentOperation, Double.parseDouble(strCurrentNumber));
        }

        strCurrentNumber = Double.toString(totalNum);
        equalsButtonPressed = true;
        decimalPlaceUsed = false;
        currentOperation = '='; //Prevents error if you pressed the (+) next, it would count as a double operation
        updateDisplayString();

    }

    private void performCalculation(char operation, double currentNum) {

        switch (operation) {

            case '+':
                totalNum += currentNum;
                break;
            case '-':
                totalNum -= currentNum;
                break;
            case '*':
                totalNum *= currentNum;
                break;
            case '/':
                if (totalNum == 0) //If dividing zero
                {
                    totalNum = 1;
                }
                totalNum /= currentNum;
                break;
            case '%':
                totalNum %= currentNum;
                break;

        }

    }

    protected String getDisplayString() {
        return strDisplay;
    }
    
    
    
    public void sqrt(String num) {
        double t;
        int number = Integer.parseInt(num);
        squareRoot = number / 2;

        do {
            t = squareRoot;
            squareRoot = (t + (number / t)) / 2;
        } while ((t - squareRoot) != 0);

        strCurrentNumber = "" + squareRoot;
        updateDisplayString();
    }

    
    
    public void negative(String num){
        strCurrentNumber= num;
    }
    

    protected void resetEverything() {

        totalNum = doubleOperationNum = percentageFirstNum = percentageSecondNum = 0D;
        equalsButtonPressed = zeroNumPressed = decimalPlaceUsed = false;
        currentOperation = previousOperation = '+';
        strCurrentNumber = "";
        doubleOperation = '=';

        updateDisplayString();

    }

    protected void deleteLastEnteredNumber() {

        if (!(strCurrentNumber.equals("") || equalsButtonPressed)) {

            if (strCurrentNumber.charAt(strCurrentNumber.length() - 1) == '.') {
                decimalPlaceUsed = false;
            }

            strCurrentNumber = strCurrentNumber.substring(0, strCurrentNumber.length() - 1);
            updateDisplayString();

        }

    }

    private void updateDisplayString() {

        strDisplay = strCurrentNumber;

        if (strDisplay.equals("")) {
            strDisplay = "0";
        } 
        else if (strDisplay.indexOf('.') == -1) //If the number doesn't have a decimal place in it
        {
            strDisplay += "";
        }

        chopDisplayString();

    }

    private void chopDisplayString() {

        if (!zeroNumPressed) {

            //Removes trailing 0 padding
            for (int i = strDisplay.length(); i != 0; i--) {

                if (strDisplay.charAt(i - 1) == '0') {
                    strDisplay = strDisplay.substring(0, (i - 1));
                } else {
                    break;
                }

            }

        }

        zeroNumPressed = false;

        if (strDisplay.length() >= 15) {
            strDisplay.substring(0, 14);
        }

    }

}
