
/**
 * Parses the function in order to manipulate it.
 * 
 * @author (Martin Baroody) 
 * @version (2015-02-13)
 */
public class Function
{
    private boolean valid; //This value represents whether or not the function is a valid one.
    //As of yet, the function apparatus should only accept functions in the following formats:
    //ax^2+bx+c
    //bx+c
    //ax^2+c
    //ax^2+bx
    //c
    //Quadratic equations are represented in the format ax^2+bx+c. The following variables represent the corresponding coefficients.
    private double a; 
    private double b;
    private double c;
    private String originalFunction; //This value holds the unparsed, inputted string. I don't use it but I store it just in case.
    
    public Function(String original)
    {
        valid = true; //valid is initially set to true and is changed to false if it is detected as invalid. 
        a = 0;
        b = 0;
        c = 0;
        originalFunction = original;
        functionParse(original);
    }
    
    //There are two parsing methods here: termParse and equationParse.
    //termParse parses individual terms.
    //equationParse parses terms out of the equation.
    
    private void termParse(String term) 
    {
        String coefficientStr = new String(); /*Holds the string value of the coefficient. Since we are parsing a string the value of the
        //coefficient must be held as a string before being converted into an double.*/
        String degreeStr = new String(); //Same thing as coefficientStr except it holds the string value of the degree of the exponent.
        double coefficient; //Holds the numerical value of the coefficient.
        int degree; //Holds the numerical value of the degree of the exponent after the x.
        
        int index = term.indexOf("x");
        if(index == 0) //If the index is 0, that means that there is an x in the equation and that it is the first character in the string.
        //Therefore there is no written coefficient. This means that the coefficient is 1. ex. for x^2 the coefficient is 1.
        {
            coefficientStr = "1";
        }
        else if(index > 0) //If index is greater than zero it means that there is a visisble coefficient before the x.
        {
            coefficientStr = term.substring(0, index); //Taking what comes before the x and storing it as the coefficient.
            if((coefficientStr.equals("+")) || (coefficientStr.equals("-"))) /*In some cases the equation might be written as +x or -x. Thus the 
            coefficientStr will contain + or -. In these cases, the coefficient is really either positive 1 or -1 depending on the sign.*/
            {
                coefficientStr = coefficientStr.concat("1");
            }
        } 
        else if(index < 0) //If there is no x in the equation, then the degree of the x is 0 and the entire term counts as a coefficient here.
        {
            coefficientStr = term;
            degreeStr = "^0";
        }
        if(index >= 0) /*This step will find the degree of the inputted function. It will only execute if there is an x in the equation because
        if there is no x, then the coefficientStr has already been found. */
        {
            if(index < term.length() - 1)
            {
                degreeStr = term.substring(index+1); /*If the term is in the format ax^y, then the index of x is less than the length of the 
                term due to the presence of the exponenent sign and the number after it. Thus, what comes after the x is then parsed off and 
                stored as degreeStr. */
            }
            else //If the index is not smaller than the length of the term minus 1, that means that it is the last character in the term.
            //The term is therefore in the format bx. This is a common way of representing that the degree of the exponent is 1.
            {
                degreeStr = "^1";
            }
        }
        try //Here I detect invalid input.
        {
            coefficient = Double.parseDouble(coefficientStr); //If the coefficient is valid, then Double.parseDouble should execute without fail.
            if((degreeStr.charAt(0) == '^') && (degreeStr.length() > 1)) /* The first char of degreeStr should be '^'. There should also be a 
            number after the exponent sign, meaning that the length of degreeStr should be greater than 1. */
            {
                degreeStr = degreeStr.substring(1); //Slicing off the exponent sign.
                degree = Integer.parseInt(degreeStr); //If valid the characters after the degree sign should be able to undergo parseInt.
            }
            else /*If the first character of the degreeStr isn't the exponent sign or if there is nothing after the exponent sign then the input 
            is invalid. */
            {
                valid = false;
                return;
            }
        }
        catch(NumberFormatException e) /*If a NumberFormatException occured, meaning that the value of the coefficient or the degree isn't a 
        number (an integer in the case of degree) or is too big to fit in the memory space of a double/int then the input is invalid. */
        {
            valid = false;
            return;
        }
        if(degree == 0) //If the degree of the term is 0, then the coefficient refers to the c coefficient in the ax^2+bx+c format.
        {
            c += coefficient;
        }
        else if(degree == 1) //If the degree of x is 1 then it refers to b.
        {
            b += coefficient;
        }
        else if(degree == 2) //If the degree is 2 then it refers to a.
        {
            a += coefficient;
        }
        /*I use the += operator in case the equation isn't simplified, this will simplify the equation if there are multiple instances of a
        degree in the inputted equation. */
        else //If the degree is not 0, 1, or 2, then the input is invalid because this apparatus will only graph quadratic/linear functions.
        {
            valid = false;
            return;
        }
    }
    
    private void functionParse(String function) 
    {
        function = function.replaceAll("\\s", ""); /*If the user put any spaces in the function, it could screw up the parsing. Therefore I 
        remove the spaces from the equation. */
        function = function.toLowerCase(); //Puts everything to lower case so that I can parse both upper case and lower case functions.
        String term;
        
        if(function.length() > 1) /*If there is 1 character in the string then, for sure, that is the term. Here we are uncertain, therefore this
        more complex parsing job must be performed. */
        {
            int indexPlus;
            int indexMinus;
            indexPlus = function.indexOf("+", 1); //This will hold the index of the first occurrence of a separating plus sign.
            indexMinus = function.indexOf("-", 1); //This will hold the index of the first occurrence of a separating minus sign.
            //I am starting these at index 1 because if a + or - sign is the first character of the string, then it wouldn't be the separator.
            //What I aim to do is to separate terms off a function by finding a separator (+ or - sign) and shaving off what comes before it.
            //I want to first find which operator comes first.
            if((indexPlus >= 0) && ((indexPlus < indexMinus) || (indexMinus < 0))) /*If this is true then the plus sign occurs before the minus
            sign and parsing must therefore start at indexPlus. */
            {
                term = function.substring(0, indexPlus); //We store term as whatever comes before the first separating plus sign.
                function = function.substring(indexPlus); /*I remove the term from the function, allowing the method to occur again later on in 
                order to remove all the different terms from the function. */
            }
            else if(indexMinus >= 0) //If the first separating minus sign occurs before the first separating plus sign.
            {
                term = function.substring(0, indexMinus); //We store term as whatever comes before the first separating minus sign.
                function = function.substring(indexMinus); /*I remove the term from the function, allowing the method to occur again later on in 
                order to remove all the different terms from the function. */
                //This is the same as the indexPlus version but here we use indexMinus as a basis instead.
            }
            else //If there are no separating plus or minus signs in the equation.
            {
                term = function; //Since there are no separators this means that the function is a term.
                function = ""; /* function is now an empty string. Meaning that when functionParse() recurs, the length of function will be equal
                to zero. This conditional only runs when the length of function is greater than 1, and the following conditional only runs when
                the length is equal to 1; there are no conditionals for the length of function being equal to zero. Thus, after this, the method 
                will end after a final recursion (which will do nothing). */
            }
            termParse(term); //Term is taken and parsed in termParse().
            functionParse(function); //Recursion: the concatenated string is continually parsed until its length is equal to or less than 1.
        }
        else if(function.length() == 1) /* When this is the case; there is only 1 character in the string, and this can immediately be identified
        as a term. Since this is the only character in the function, parsing will end after this character is parsed and the functionParse() 
        method will end. */
        {
            term = function;
            termParse(term);
        }
    }
    
    public boolean isValid() //Returns whether the function is valid or not.
    {
        return valid;
    }
    
    public double returnA() //Returns the value of a in the equation ax^2+bx+c.
    {
        return a;
    }
    
    public double returnB() //Returns the value of b.
    {
        return b;
    }
    
    public double returnC() //Returns the value of c.
    {
        return c;
    }
}
