package main ;

import java.awt.Dimension;
import java.awt.EventQueue ;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame ;
import javax.swing.Timer;

import utilities.GameStates;

/**
 * made with love by
 * @author Salevieno
 * @version 3.4
 * @since 2022
 */
public class MainGame3_4 extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L ;
	
	private static Timer timer ;		// Main timer of the game
	private static GameStates previousState ;

	public MainGame3_4() 
    {
		// initialize the UI
		timer = new Timer(10, this) ;	// timer of the game, first number = delay
		timer.start() ;	// Game will start checking for keyboard events and go to the method paintComponent every "timer" miliseconds
		Dimension WinDim = new Dimension(600 + 40, 480 + 39) ;	// frame dimensions (0, 39) and give an extra space on the right (40, 0)
        setTitle("Jogo da aventura") ;          
        setSize(WinDim) ;
        setLocation(500, 200) ;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ;
        setVisible(true) ;
        previousState = GameStates.opening ;
        add(new Game(WinDim)) ;							// adding game panel on the JFrame
    }
    
	public static void pauseGame()
	{
        timer.stop() ;
        previousState = Game.state ;
        Game.state = GameStates.paused ;
	}
	
	public static void resumeGame()
	{
        timer.start() ;
        Game.state = previousState ;
	}
	
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(() -> {new MainGame3_4() ;}) ;
	}
	
	@Override
    public void actionPerformed(ActionEvent e) 
	{        
        if (Game.getShouldRepaint())
        {
        	repaint() ;
        	Game.shouldRepaint() ;
        }
		if (e.getSource() != timer)
		{
			System.out.println("action performed = " + e);
			repaint() ;
		}
	}
}
