/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package calculations;

import java.util.Stack;


/**
 *
 * @author Craig Isenor
 */
public class CalcBrain implements calculations.Calculations{
    
    
    Stack<Float> stack = new Stack<>();
    private boolean decimalEntered = false;
    String currentNums = "";
        
        
    @Override
    public String digit(String digit) {
        currentNums += digit;
        return digit;
    }
    
    
    

    @Override
    public String operator(String op) {
        
        float digit1;
        float digit2;
        float result;
        
        
        decimalEntered = false;
        
        
        //is the stack big enough?
        if ((stack.size() == 1 && currentNums.equals("")) || (stack.size() < 1) ){
            return "";
        }
        
        // if there are numbers still in the currentNums var, push them.
        if ( currentNums.equals("") == false){
            stack.push(Float.parseFloat(currentNums));
            currentNums = "";
        }
        
        switch (op){
            
            case "+":
                
                digit1 = stack.pop();
                digit2 = stack.pop();
                result = digit1 + digit2;
                stack.push(result);
                return "\n = " + Float.toString(result) + "\n";
            case "-":
                
                digit1 = stack.pop();
                digit2 = stack.pop();
                result = digit2 - digit1;
                stack.push(result);
                return "\n = " + Float.toString(result) + "\n";
            case "/":
                
                digit1 = stack.pop();
                digit2 = stack.pop();
                result = digit2 / digit1;
                stack.push(result);
                return "\n = " + Float.toString(result) + "\n";
            case "*":
                
                digit1 = stack.pop();
                digit2 = stack.pop();
                result = digit1 * digit2;
                stack.push(result);
                return "\n = " + Float.toString(result) + "\n";
            case "^":
                
                digit1 = stack.pop();
                digit2 = stack.pop();
                result = (float) Math.pow(digit2, digit1);
                stack.push(result);
                return "\n = " + Float.toString(result) + "\n";
            
        }return "";
    } 

    @Override
    public String clearEntry() {
        currentNums = "";
        return "\nEntry Cleared\n";
    }

    @Override
    public String clear() {
       currentNums = "";
       stack.clear();
       return "\n\n\n\n\n\n\n\n\n\n";
    }

    @Override
    public String enterPressed() {
        
        decimalEntered = false;
        if (currentNums.equals("")){
            return "";
        }
        stack.push(Float.parseFloat(currentNums));
        currentNums = "";
        return " ";
        
    }

    @Override
    public String addDecimal() {
        if(!decimalEntered){
            decimalEntered = true;
            currentNums += ".";
            return ".";
        }
       return "";
    }
    
}
