import java.awt.*;
import javax.swing.*;
/**
 * Draws a graph of the function.
 * 
 * @author (Martin Baroody) 
 * @version (2015-02-16)
 */
public class FunctionGraphics extends JPanel
{
    private Function function;
    private int width;
    private int height;
    
    public FunctionGraphics(Function f)
    {
        function = f;
        
        width = 800; 
        height = 800;
        //I prefer to set the size as 800, 800.
        setPreferredSize(new Dimension(width, height)); //Setting the size to this dimension.
    }
    
    public void paintComponent(Graphics g) //Draws everything to the screen.
    {
        super.paintComponent(g);
        setBackground(Color.white);
        
        drawAxes(g);
        drawGraph(g);
    }
    
    public void drawAxes(Graphics g)
    {
        super.paintComponent(g);
        g.drawLine(0, height/2, width, height/2); //x-axis.
        g.drawLine(width/2, 0, width/2, height); //y-axis.
        
    }
    
    public double getScale()
    {
        Solution calculator = new Solution(function);
        double max;
        
        double[] x;
        double y;
        
        x = calculator.getXIntercepts();
        y = calculator.getYIntercept();
        if(x != null) //When x is null then we cannot perform any calculations on it or else it will throw an error. 
        {
            x[0] = Math.abs(x[0]);
            x[1] = Math.abs(x[1]);
        }
        y = Math.abs(y);
        /*I am getting the absolute value of all the intercepts to see which one is the greatest. I will use the greatest intercept in order to 
        form my scale in order to ensure that every intercept will be shown. */

        if(x != null) //When x is null then we cannot perform any calculations on it or else it will throw an error. 
        {
            //The following algorithm determines which is the greatest value.
            max = x[0];
            if(max < x[1])
            {
                max = x[1];
            }
            if(max < y)
            {
                max = y;
            }
        }
        else
        {
            max = y;
        }
        
        max = max * 4; //The scale will be equal to the greatest value times 4.
        
        if(max == 0) /*If the greatest absolute value is 0, for example the x-intercept and y-intercept are both at 0, then max * 4 would equal 0. This
        would screw up the graph, therefore in this case I will make the scale equal to 4. */
        {
            max = 4;
        }
        
        return max; //Returns the scale.
    }
    
    public void drawGraph(Graphics g)
    {
        if(function.isValid()) /* The graph will only be drawn in the function is valid. I will prevent invalid input from being entered but this
        is here as extra protection. */
        {
            double scale = getScale();
            double increment = (scale * 2) / width; /* If your scale is 4, then the distance of an axis is 8. Since the width is 800px, that 
            means that 8 units = 800px. Therefore 1 unit = 100px and 1px = 1/100 units. Since we will be drawing lines between each pixel, I 
            want to convert from pixels to units. (scale * 2) / width provides that conversion. Each increment is 1 pixel which is
            (scale * 2) / width units. I need to work in units since I want to calculate the y value for each x value in order to actually draw
            the points. */
            double x1, x2, y1, y2;
            
            double a = function.returnA();
            double b = function.returnB();
            double c = function.returnC();
            
            for(double x = -scale; x < scale; x += increment) //This goes for the length of the axis and x increments 1 pixel every time.
            {
                x1 = x; 
                x2 = x + increment;
                //For each loop I will draw a line between two pixels. x1 will be at the position one pixel less than x2.
                y1 = (a * (x1 * x1)) + (b * x1) + c; 
                y2 = (a * (x2 * x2)) + (b * x2) + c;
                //Here I calculated the y units.
                
                x1 = x1 * (width / (scale * 2)) + (width / 2); 
                x2 = x2 * (width / (scale * 2)) + (width / 2);
                y1 = y1 * (width / (scale * 2)) + (width / 2);
                y2 = y2 * (width / (scale * 2)) + (width / 2);
                /* Here I am converting the units to pixels, using the reverse equation of the pixel-unit conversion, so that they can be drawn
                to the screen. I add width / 2 in order to translate the coordinate with respect to the origin of the graph. */
                
                g.setColor(Color.green); //Draws the function in a green color.
                g.drawLine((int)(x1), height - (int)(y1), (int)(x2), height - (int)(y2)); /*Draws the function. For the y-intercepts I subtract
                the y values from height because in the Java graph 0 is the highest point whereas 800 is the lowest point, the reverse is true 
                in a mathematical plane, therefore I subtract height - y in order to convert from the mathematical plane into ther Java plane.
                */
            }
            
            Solution calculator = new Solution(function);
            double[] x = calculator.getXIntercepts();
            
            if(x != null) //When x is null then we cannot perform any calculations on it or else it will throw an error. 
            {
                for(int i = 0; i < x.length; i++)
                {
                    if(x[i] == -0.0)
                    {
                        x[i] = Math.abs(x[i]); /*In some cases the x value might be written as -0.0, this is improper syntax and 0 is 
                        normally written in positive form, therefore the -0.0 values (if any) are found and converted into 0.0 instead. */
                    }
                }
            }
            double y = calculator.getYIntercept();
            
            if(x != null) //When x is null then we cannot perform any calculations on it or else it will throw an error.  
            {
                int xOne = (int)(x[0] * (width / (scale * 2)) + (width / 2)); //Position of the first x-intercept in terms of pixels. 
                int xTwo = (int)(x[1] * (width / (scale * 2)) + (width / 2)); //Position of second x-intercept in terms of pixels.
                int zeroY = (int)(width / 2); //The y-position of the x-axis, where the x-intercepts are.
                g.setColor(Color.blue); //The oval will be drawn blue.
                //This algorithm draws small ovals over the x-intercepts to highlight them.
                g.drawOval(xOne - 3, zeroY - 3, 6, 6); //Draws a small oval over the first x-intercept.
                if(xOne != xTwo)
                {
                    g.drawOval(xTwo - 3, zeroY - 3, 6, 6); /* If the two x-intercepts are not the same, then an oval will be drawn over the 
                    second x-intercept as well. If there is only one unique x-intercept then drawing 2 ovals would be useless. */
                }
            } 
            /* For the above algorith, if x == null then there are no x-intercepts or an infinite number of them and therefore no ovals should be drawn over 
            x-intercepts if this is the case. */
            
            int yLoc; 
            yLoc = (int)(y * (width / (scale * 2)) + (width / 2)); //The position of the y-intercept.
            int zeroX = (int)(width / 2); //The x position of the y-axis.
            g.setColor(Color.blue);
            g.drawOval(zeroX - 3, height - yLoc - 3, 6, 6); //Draws a small oval over the equation's y-intercept.
            
            //The following algorithm outputs the corresponding intercepts and derivative to the screen.
            if((x == null) && (c == 0)) //When this is the case, there is an infinite number of x-intercepts.
            {
                g.drawString("x-intercepts: infinite", width - 250, 50);
                g.drawString("y-intercept: (0.0, " + y + ")", width - 250, 70);
                g.drawString("derivative: " + calculator.getDerivative(), width - 250, 90);
            }
            else if(x == null) //When this is the case and c != 0 there are no real x-intercepts.
            {
                g.drawString("x-intercepts: none", width - 250, 50);
                g.drawString("y-intercept: (0.0, " + y + ")", width - 250, 70);
                g.drawString("derivative: " + calculator.getDerivative(), width - 250, 90);
            }
            else if(x[0] == x[1]) //In this case there is only one distinct x-intercept.
            {
                g.drawString("x-intercept: (" + x[0] + ", 0.0)", width - 250, 50);
                g.drawString("y-intercept: (0.0, " + y + ")", width - 250, 70);
                g.drawString("derivative: " + calculator.getDerivative(), width - 250, 90);
            }
            else //Here there are two distinct x-intercepts.
            {
                g.drawString("left x-intercept: (" + x[1] + ", 0.0)", width - 250, 50);
                g.drawString("right x-intercept: (" + x[0] + ", 0.0)", width - 250, 70);
                g.drawString("y-intercept: (0.0, " + y + ")", width - 250, 90);
                g.drawString("derivative: " + calculator.getDerivative(), width - 250, 110);
            }
        }
    }
}
