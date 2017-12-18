import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
/**
 * Glues everything together into one program.
 * 
 * @author (Martin Baroody) 
 * @version (2015-03-03)
 */
public class FunctionApparatus implements ActionListener
{
    private JButton jbnSolver, jbnQGen, jbnQuit;
    private JPanel buttonPanel;
    private JFrame buttonFrame;
    private JFrame functionFrame;
    
    public FunctionApparatus()
    {
        jbnSolver = new JButton("Solver");
        jbnSolver.setVerticalTextPosition(AbstractButton.CENTER);
        jbnSolver.setHorizontalTextPosition(AbstractButton.LEADING);
        jbnSolver.setActionCommand("solver");
        jbnSolver.setToolTipText("Use the equation solver.");
        
        jbnQGen = new JButton("Question Generator");
        jbnQGen.setVerticalTextPosition(AbstractButton.CENTER);
        jbnQGen.setHorizontalTextPosition(AbstractButton.LEADING);
        jbnQGen.setActionCommand("qGen");
        jbnQGen.setToolTipText("Use the question generator to test yourself.");
        
        jbnQuit = new JButton("Quit");
        jbnQuit.setVerticalTextPosition(AbstractButton.CENTER);
        jbnQuit.setHorizontalTextPosition(AbstractButton.LEADING);
        jbnQuit.setActionCommand("quit");
        jbnQuit.setToolTipText("Quit the function apparatus.");
        
        buttonPanel = new JPanel();
        
        jbnSolver.addActionListener(this);
        jbnQGen.addActionListener(this);
        jbnQuit.addActionListener(this);
        
        buttonPanel.add(jbnSolver);
        buttonPanel.add(jbnQGen);
        buttonPanel.add(jbnQuit);
        
        buttonFrame = new JFrame("Function Apparatus");
        buttonFrame.add(buttonPanel);
        buttonFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void buttonScreen()
    {
        buttonFrame.setVisible(true);
        buttonFrame.setLocationRelativeTo(null);
        buttonFrame.requestFocus();
        buttonFrame.validate();
        buttonFrame.pack();
        buttonFrame.repaint();
    }
    
    public void solver()
    {
        functionFrame = new JFrame("Function Solver");
        
        String fx = new String();
        Function function = new Function("");
        FunctionGraphics gr;
        boolean valid = false;
        while(valid == false) //This will loop until the user enters correct input.
        {
            fx = JOptionPane.showInputDialog("Enter a function (either a quadratic or linear equation) in the form ax^2+bx+c.");
            if(fx != null)
            {
                function = new Function(fx);
                if(!function.isValid())
                {
                    JOptionPane.showMessageDialog(null, "Invalid function");
                }
                else
                {
                    
                    gr = new FunctionGraphics(function);
                    functionFrame.getContentPane().add(gr); //Draws the graph and outputs the values of the intercepts and derivative.
                    break;
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Please enter something.");
            } //This prevents the user from entering nothing, which would normally break the program and cause an exception to occur.
        }
        
        functionFrame.pack();
        functionFrame.validate();
        functionFrame.repaint();

        functionFrame.setVisible(true);
        
        JOptionPane.showMessageDialog(null, "Question solved. Press OK to go back to the main screen.");
        
        buttonScreen(); //Goes back to the button screen.
        
        functionFrame.dispatchEvent(new WindowEvent(functionFrame, WindowEvent.WINDOW_CLOSING)); //Closes the solver window.
    }
    
    public void questionGenerator()
    {
        functionFrame = new JFrame("Question Generator");
        boolean correctInput = false;
        int difficultyLevel = 0;
        
        while(correctInput == false) //Will loop until the user enters correct input.
        {
            try
            {
                difficultyLevel = Integer.parseInt(JOptionPane.showInputDialog("Enter difficulty level (1, 2, or 3)."));
                if((difficultyLevel >= 1) && (difficultyLevel <= 3))
                {
                    break;
                }
                else //If the user entered anything other than 1, 2, or 3.
                {
                    JOptionPane.showMessageDialog(null, "You entered an invalid number! Try again.");
                }
            }
            catch(NumberFormatException e) //Here the user didn't enter an integer.
            {
                JOptionPane.showMessageDialog(null, "You entered an invalid number! Try again.");
            }
            catch(NullPointerException e) //In this case the user entered nothing.
            {
                JOptionPane.showMessageDialog(null, "You entered an invalid number! Try again.");
            }
        }
        
        
        QuestionGenerator qGen = new QuestionGenerator(difficultyLevel);        
        String equation = qGen.questionGenerator();  
        boolean playAgain = true;
        while(playAgain == true)
        {
            String answer = JOptionPane.showInputDialog("Find the roots of " + equation + ". Write your answer in the form x1,x2 or write \"none\" if there are no roots. Round your answer(s) to 3 decimal places.");
            boolean check;
            if(answer == null)
            {
                check = false;
            }
            else
            {
                check = qGen.checkAnswer(answer, equation);
            }
            
            if(check == true)
            {
                JOptionPane.showMessageDialog(null, "Correct! Press OK to return to the main screen.");
                break;
            }
            else
            {
                boolean yn = false;
                String yesNo = new String();
                try
                {
                    while(yn == false)
                    {
                        yesNo = JOptionPane.showInputDialog("Incorrect answer. Would you like to try again (Y/N)?");
                        if(yesNo.equalsIgnoreCase("y"))
                        {
                            yn = true;
                        }
                        else if(yesNo.equalsIgnoreCase("n"))
                        {
                            double[] answers = qGen.getAnswers();
                            //The following if block will display the answer.
                            if(answers == null)
                            {
                                JOptionPane.showMessageDialog(null, "The function was " + equation + " and it doesn't have any roots.");
                            }
                            else
                            {
                                JOptionPane.showMessageDialog(null, "The function was " + equation + " and it has 2 roots at " + answers[0] + " and " + answers[1] + ".");
                            }
                            
                            playAgain = false;
                            break;
                        }
                    }
                }
                catch(NullPointerException e)
                {
                }
            }
        }
        
        buttonScreen(); //Goes back to the button screen.
        functionFrame.dispatchEvent(new WindowEvent(functionFrame, WindowEvent.WINDOW_CLOSING)); //Closes the question generator window.
    }
    
    public void quit()
    {
        functionFrame = new JFrame("Quit?");
        
        boolean validInput = false;
        while(validInput == false)
        {
            try
            {
                String quitStr = JOptionPane.showInputDialog("Do you really want to quit (Y/N)?");
                if(quitStr.equalsIgnoreCase("y"))
                {
                    functionFrame.dispatchEvent(new WindowEvent(functionFrame, WindowEvent.WINDOW_CLOSING));
                    buttonFrame.dispatchEvent(new WindowEvent(buttonFrame, WindowEvent.WINDOW_CLOSING));
                    System.exit(0);
                }
                else if(quitStr.equalsIgnoreCase("n"))
                {
                    buttonScreen();
                    break;
                }
            }
            catch(NullPointerException e)
            {
            }
        }
        
    }
    
    public void actionPerformed(ActionEvent event)
    {
        if("solver".equals(event.getActionCommand())) //If the user presses the solver button then the solver method is called.
        {
            solver();
        }
        else if("qGen".equals(event.getActionCommand())) //If the user presses the question generator button then the question generator is launched.
        {
            questionGenerator();
        }
        else if("quit".equals(event.getActionCommand())) //If the user presses the quit button then the quit option is launched.
        {
            quit();
        }
    }
    
    public static void main(String[] args)
    {
        FunctionApparatus f = new FunctionApparatus();
        f.buttonScreen();
    }
}
