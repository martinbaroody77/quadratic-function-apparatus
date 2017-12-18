import java.util.Random;
import java.math.*;
/**
 * Write a description of class QuestionGenerator here.
 * 
 * @author (Martin Baroody) 
 * @version (2015-02-21)
 */
public class QuestionGenerator
{
    private int difficultyLevel; //There will be 3 difficulty levels (1, 2, or 3) which the user should be able to enter.
    private double[] answers = new double[2];
    
    public QuestionGenerator(int level)
    {
        difficultyLevel = level;
        answers = null;
    }
    
    public int[] interceptGenerator() //This generator will be used to generate intercepts for the various difficulty levels.
    {
        Random generator = new Random();
        int[] intercepts = new int[2]; 
        if(difficultyLevel == 1) //If the difficulty level is 1 then an integer intercept will be generated between -6 and 6.
        {
            intercepts[0] = generator.nextInt(13)-6; 
            intercepts[1] = generator.nextInt(13)-6;
        }
        else if(difficultyLevel == 2) /* Here an intercept will be generated between -10 and 10. In this case these aren't really "intercepts"
        because I will be multiplying them to another set of random numbers. */
        {
            intercepts[0] = generator.nextInt(21)-10;
            intercepts[1] = generator.nextInt(21)-10;
        }
        else if(difficultyLevel == 3) //Here no intercepts will be generated at all; each coefficient will be randomly generated later on.
        {
            intercepts = null;
        }
        
         return intercepts; //Returns the intercepts.
    }
    
    public String questionGenerator()
    {
        Random generator = new Random();
        
        int[] intercepts;
        intercepts = interceptGenerator();
        int a = 0;
        int b = 0;
        int c = 0;
        int[] coefficients = new int[2];
        if(difficultyLevel == 1)
        {
            
            a = 1;
            b = intercepts[0]+intercepts[1]; 
            c = intercepts[0]*intercepts[1]; 
            //This makes for very straightforward intercept finding, which can be done easily using factorization.
        }
        else if(difficultyLevel == 2) //This one is harder than the first, with many cases requiring the quadratic formula to be used. 
        {
            coefficients[0] = generator.nextInt(5)+1;
            coefficients[1] = generator.nextInt(5)+1;
            
            a = coefficients[0]*coefficients[1];
            b = (intercepts[0]*coefficients[1])+(intercepts[1]*coefficients[0]);
            c = intercepts[0]*intercepts[1];
        }
        else if(difficultyLevel == 3) /* This can be the hardest difficulty level because many of the generated equations will involve big 
        numbers and will thus entail more time-consuming calculations. */
        {
            a = generator.nextInt(1001)-500;
            b = generator.nextInt(1001)-500;
            c = generator.nextInt(1001)-500;
        }
        
        
        
        String equation = new String();
        if(a != 0) //If a is equal to zero then it will not be concatenated in order to be syntatically correct.
        {
            if(a != 1)
            {
                equation = equation.concat(a + "x^2");
            }
            else
            {
                equation = equation.concat("x^2"); //If a coefficient is 1 then the coefficient isn't written.
            }
        }
        
        if((b > 0) && (a != 0))
        {
            if(b != 1)
            {
                equation = equation.concat("+" + b + "x");
            }
            else
            {
                equation = equation.concat("+" + "x"); //If a coefficient is 1 then the coefficient isn't written.
            }
            
        }
        else
        {
            if(b == 1)
            {
                equation = equation.concat("x"); /* If a coefficient is 1 then the coefficient isn't written. Also since this is the first term
                of the equation, I avoid putting the plus sign because it would be syntatically incorrect. */
            }
            else if(b == -1)
            {
                equation = equation.concat("-x");
            }
            else if(b != 0)
            {
                equation = equation.concat(b + "x");
            }
        }
        
        if((c > 0) && ((a != 0) || (b != 0)))
        {
            equation = equation.concat("+" + c);
        }
        else if(c != 0)
        {
            equation = equation.concat(c + "");
        }
        
        return equation;
    }
    
    public boolean checkAnswer(String input, String equation) //Input will be in form of two roots with comma between them.
    {
        Function fx = new Function(equation);
        Solution calculator = new Solution(fx);
        
        input = input.replaceAll("\\s", ""); //Removes all the spaces in the equation in order to ensure better parsing.
        
        answers = calculator.getXIntercepts(); //Calculates the theoretical x-intercepts of the equation.
        
        if(answers != null)
        {
            //Same idea as the previous algorithm, This arranges the array of theoretical values in numerical order.
            if(answers[0] > answers[1])
            {
                double temp = answers[0];
                answers[0] = answers[1];
                answers[1] = temp;
                
                for(int i = 0; i < answers.length; i++) //This will round the doubles to three decimal places.
                /* The input might not have been three decimal places, however, in the program I will notify the user that their answer should
                be rounded to three decimal places so that they don't get confused as to what number of decimal places they should provide. */
                {
                    BigDecimal bd = new BigDecimal(answers[i]);
                    bd = bd.setScale(3, RoundingMode.HALF_UP);
                    answers[i] = bd.doubleValue();
                }
            }
        }
        
        double[] inputIntercepts = new double[2];
        
        String[] interceptStr = new String[2];
        
        int index = input.indexOf(","); //Since the input will be in the form x1,x2 we will use the comma to delimit the two inputted intercepts.
        if(index == 0) //If the comma is the first thing in the string then the user put invalid input. I'll count this as a wrong answer.
        {
            return false;
        }
        else if((index > 0) && (input.length() > index+1)) //In this case there is a comma separating two character sets from each other.
        {
            interceptStr[0] = input.substring(0, index); 
            interceptStr[1] = input.substring(index+1);
            //The character sets on each side of the comma are stored as the intercepts.
        }
        else if((index > 0) && (input.length() == index+1)) //In this case the comma is the final character of the string.
        //In this case I'll assume that the user only inputted one intercept and that the comma is there accidentally.
        {
            interceptStr[0] = input.substring(0, index); 
            interceptStr[1] = input.substring(0, index); 
        }
        else if(index < 0) //In this case there is no comma, meaning that the user only inputted one intercept.
        {
            interceptStr[0] = input;
            interceptStr[1] = input;
        }
        
        if((interceptStr[0].equalsIgnoreCase("none")) && (interceptStr[1].equalsIgnoreCase("none")))
        {
            inputIntercepts = null; //If the user enters none then the inputIntercepts will be equal to null.
        }
        else //We cannot perform calculations with null therefore it is wise to put an else here.
        {
            try
            {
                inputIntercepts[0] = Double.parseDouble(interceptStr[0]);
                inputIntercepts[1] = Double.parseDouble(interceptStr[1]);
            }
            catch(NumberFormatException e) /* If the inputted intercepts are not doubles, the user put incorrect input and I will count this as
            a wrong answer. */
            {
                return false;
            }
        }
        
        if(inputIntercepts != null) //We cannot perform calculations with null.
        {
            if((inputIntercepts[1] < inputIntercepts[0])) /* Here I am arranging the inputted intercepts in numerical order in order to more
            easily compare the inputted roots with the theoretical roots. */
            {
                double temp = inputIntercepts[0];
                inputIntercepts[0] = inputIntercepts[1];
                inputIntercepts[1] = temp;
            }
        }
        
        
        
        
        
        
        final double TOLERANCE = 0.0001;
        if((inputIntercepts != null) && (answers != null)) 
        {
            for(int i = 0; i < answers.length; i++) /* This will compare the input roots to the theoretical roots to see if there is a 
            significant difference between them. */
            {
                if(!(Math.abs(inputIntercepts[i] - answers[i]) < TOLERANCE))
                {
                    return false; //If the difference between the two is greater than 0.0001, then the user entered the wrong answer.
                }
            }
        }
        else if(((inputIntercepts == null) && (answers != null)) || ((inputIntercepts != null) && (answers == null)))
        {
            return false; //If one array is null but the other one isn't, then the user screwed up somewhere.
        }
        
        return true; //This will occur if both equations are null, in which case, they are equal and the user entered the correct answer.
    }
    
    public double[] getAnswers()
    {
        return answers; //Returns the answers of the function.
    }
}
