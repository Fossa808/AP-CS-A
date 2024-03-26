//Jyotsna
//Period 6
//AP Computer Science
//FracCalc
import java.util.Scanner;
import java.util.Arrays;
public class FracCalc{
    public static void main(String[] args){
        Scanner userInput = new Scanner(System.in);
        boolean notQuit = true;
        //User Input Loop
        System.out.print("Welcome to FracCalc!\nType 'help' for instructions on how to use FracCalc.\nEnter: ");
        String equation = userInput.nextLine();
        while (notQuit){
            //Quit Case: If the user types quit, fracCalc will stop running
            if (equation.equalsIgnoreCase("quit")){
                notQuit = false;
            }
            /*Help Case: If the user types help the Help Method will run,
             * providing a set of instructions*/
            else if(equation.equalsIgnoreCase("help")){
                help();
                equation = userInput.nextLine();
            }
            /*Multiple operators can be inputted.
             *The equation will then be separated into an array
             *The array will then be checked for certain operators and
             *the respective methods will be run to solve the equation.*/
            else{
                String[] fullEquation = equation.split(" ");
                for(int i = 1; i < fullEquation.length; i += 2){
                    String term1 = fullEquation[i - 1];
                    String operator = fullEquation[i];
                    String term2 = fullEquation[i + 1];
                    term1 = conversion(term1);
                    term2 = conversion(term2);
                    int numer1 = Integer.parseInt(term1.substring(0, term1.indexOf("/")));
                    int numer2 = Integer.parseInt(term2.substring(0, term2.indexOf("/")));
                    int denom1 = Math.abs(Integer.parseInt(term1.substring(term1.indexOf("/") + 1)));
                    int denom2 = Math.abs(Integer.parseInt(term2.substring(term2.indexOf("/") + 1)));
                    if (operator.equals("+")){
                        fullEquation[i + 1] = addition(term1, term2, numer1, numer2, denom1, denom2) + "";
                    }
                    else if (operator.equals("-")){
                        fullEquation[i + 1] = subtraction(term1, term2, numer1, numer2, denom1, denom2) + "";
                    }
                    else if (operator.equals("*")){
                        fullEquation[i + 1] = multiplication(term1, term2, numer1, numer2, denom1, denom2) + "";
                    }
                    else if (operator.equals("/")){
                        fullEquation[i + 1] = division(term1, term2, numer1, numer2, denom1, denom2) + "";
                    }
                }
                //Removes the "/1" from the answer if it is a whole number. 
                //Then the answer is printed.
                String lastNum = fullEquation[fullEquation.length - 1];
                if (lastNum.substring(lastNum.indexOf("/") + 1).equals("1")){
                    System.out.print(lastNum.substring(0, lastNum.indexOf("/")) + "\nEnter: ");
                }
                else{
                    System.out.print(lastNum +"\nEnter: ");
                }
                equation = userInput.nextLine();
            }            
        }
        System.out.println("Thank You!");
    }
    
    //This method converts all terms into a proper/improper fraction
    public static String conversion(String term){
        if (term.contains("_") && term.contains("/")){
            int wholeNum = Integer.parseInt(term.substring(0, term.indexOf("_")));
            int origNumer = Integer.parseInt(term.substring(term.indexOf("_") + 1, term.indexOf("/")));
            int denom = Integer.parseInt(term.substring(term.indexOf("/") + 1));
            int numerator = (wholeNum * denom) + origNumer;
            String finalTerm = numerator + term.substring(term.indexOf("/"));
            return finalTerm;
        }
        else if (!term.contains("/")){
            String finalTerm = term + "/1";
            return finalTerm;
        }
        return term;
    }
    
    //method for when user asks for help
    public static void help(){
        System.out.println("When inputting your equation, please make sure to write it in \nthe format shown below:");
        System.out.println("1 + 3 --> Please type spaces between the term and the operator.");
        System.out.println("When typing fractions please make sure that there isn't a space \nbetween the number and the '/' sign.");
        System.out.println("To write mixed fractions such as two and a half write in the \nfollowing format given:");
        System.out.println("2_1/2 --> make sure that there are no spaces between the numbers \nand the '_' or the '/' signs.");
        System.out.println("Type 'quit' once you're done.");
        System.out.print("Enter: ");
    }
    
    //operator methods
    //Fraction Addition
    public static String addition(String term1, String term2, int numer1, int numer2, int denom1, int denom2){
        int sumNumer = 0;
        int newDenom = 0;
        String sum = "";
        if(denom1 == denom2){
            newDenom = denom1;
            sumNumer = (numer1) + (numer2);
        }
        else {
            newDenom = denom1 * denom2;
            sumNumer = ((numer1) * denom2) + ((numer2) * denom1);
        }
        //simplification
        int div = simplify(sumNumer, newDenom);
        sum = (sumNumer/div) + "/" + (newDenom/div);
        return sum;        
    }
    
    //Fraction Subtraction
    public static String subtraction(String term1, String term2,int numer1,int numer2,int denom1,int denom2){
        int difNumer = 0;
        int newDenom = 0;
        String difference = "";
        if(denom1 == denom2){
            newDenom = denom1;
            difNumer = (numer1) - (numer2);
        }
        else {
            newDenom = denom1 * denom2;
            difNumer = ((numer1) * denom2) - ((numer2) * denom1);
        }
        //simplification
        int div = simplify(difNumer, newDenom);
        difference = (difNumer / div) + "/" + (newDenom / div);
        return difference;
    }
    
    //Fraction Multiplication
    public static String multiplication(String term1,  String term2,int numer1, int numer2, int denom1, int denom2){
        int prodNumer = 1;
        int prodDenom = 1;
        String product = "";
        prodNumer = numer1 * numer2;
        prodDenom = denom1 * denom2;
        //simplification
        int div = simplify(prodNumer, prodDenom);
        product = (prodNumer / div) + "/" + (prodDenom / div);
        return product;
    }
    
    //Fraction Division
    public static String division(String term1, String term2, int numer1, int numer2, int denom1, int denom2){
        int divNumer = 1;
        int divDenom = 1;
        String quotient = "";
        if (numer2 == 0){
            return "Cannot divide by zero";
        }
        else{
            divNumer = numer1 * denom2;
            divDenom = denom1 * numer2;
        }
        //simplification
        int divisible = simplify(divNumer, divDenom);
        quotient = (divNumer/divisible) + "/" + (divDenom/divisible);
        return quotient;
    }
        
    //Simplification of Fractions
    public static int simplify(int numer, int denom){
        int div = 1;
        for(int i = 2; i <= denom; i++){
            if (numer % i == 0 && denom % i == 0){
                div = i;
            }
        }
        return div;
    }
}
