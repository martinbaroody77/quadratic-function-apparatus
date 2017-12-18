import java.text.*;
/**
 * Finds the intercepts and derivative of the function.
 * 
 * @author (Martin Baroody) 
 * @version (2015-02-16)
 */
public class Solution
{
    //The following doubles refer to the corresponding variables in the equation ax^2+bx+c.
    private double a; 
    private double b;
    private double c;
    
    public Solution(Function function)
    {
        a = function.returnA(); 
        b = function.returnB();
        c = function.returnC();
        
        //The a, b, and c of the Solution class are equal to the a, b, and c of a given function which must be "solved".
    }
    
    public double[] getXIntercepts() //Returns the x-intercepts of a given function.
    {
        double[] x = new double[2]; //Holds the values of the x-intercepts.
        if(a != 0) /* If a is not equal to 0, then the equation is a quadratic equation and the quadratic formula x = (-b +/- (b^2-4ac))/2a 
        should be employed to find the x-intercepts of the equation. */
        {
            double discriminant = (b*b) - (4*a*c);
            if(discriminant < 0) //When D < 0 then there are no real roots, therefore there are no x-intercepts.
            {
                x = null;
            }
            else
            {
                double squareroot = Math.sqrt(discriminant);
                x[0] = (-b + squareroot) / (2*a);
                x[1] = (-b - squareroot) / (2*a);
            }
        }
        else if((a == 0) && (b!= 0)) //This means that the equation is a linear equation. 
        {
            x[0] = -c/b;
            x[1] = -c/b;
        }
        else if(b == 0) /* If the only present coefficient is c, then there are usually no x-intercepts unless c = 0. In this case, there are an
        infinite number of x-intercepts. But I'll handle that later. */
        {
            x = null;
        }
        return x; //Returns the x-intercepts.
    }
    
    public double getYIntercept()
    {
        return c; //The y-intercept is always the c coefficient.
    }
    
    public String getDerivative()
    {
        double aPrime;
        double bPrime;
        
        aPrime = a * 2;
        bPrime = b;
        //cPrime will always be zero and it is syntatically incorrect to put zeros in an equation.
        
        String derivative = new String("");
        
        if(aPrime != 0) //If aPrime is equal to zero then writing down 0x would be syntatically incorrect.
        {
            derivative = derivative.concat(Double.toString(aPrime) + "x"); //Concatenates the aPrime to the string.
        }
        if(bPrime < 0 || ((bPrime > 0) && (aPrime == 0))) /* If bPrime is smaller than zero then the minus sign is already there and we do not
        have to put a minus sign in front of the bPrime, thus we need simply to concatenate bPrime to the derivative string. Same applies if
        b > 0 but the aPrime is equal to zero; this would make bPrime the first term of the string, and it is syntatically incorrect to put a 
        plus sign in front of the first term of an equation, thus it would only be necessary to concatenate bPrime and no sign in front of it.*/
        {
            derivative = derivative.concat(Double.toString(bPrime)); 
        }
        else if((bPrime > 0) && (aPrime != 0)) /* If aPrime is not equal to zero, this makes bPrime the second term of the string. If bPrime is
        positive, then that means we have to put the plus sign in front of it. */
        {
            derivative = derivative.concat("+" + Double.toString(bPrime));
        }
        
        if(derivative.equals("")) //If derivative is an empty string because everything equals to 0.
        {
            derivative = "0"; //In this case the value of the derivative is 0 and that is what is going to be outputted.
        }
        
        return derivative; //Returns the string of the derivative.
    }
}
