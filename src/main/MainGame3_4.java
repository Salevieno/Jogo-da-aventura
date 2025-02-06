package main ;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue ;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame ;
import javax.swing.Timer;

import utilities.GameStates;
import utilities.Util;

/**
 * made with love by Salevieno
 * @author Salevieno
 * @version 3.4
 * @since 2022
 */
public class MainGame3_4 extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L ;
	
	private static Timer timer ;		// Main timer of the game
	private static final Dimension windowSize = new Dimension(1280,960) ;
	private static GameStates previousState ;
	private static final Image icon = Util.loadImage(".\\images\\gameIcon.png") ;
	private static final Game game = new Game() ;

	private MainGame3_4()
    {
		// initialize the UI
	    setIconImage(icon) ;
        setTitle("Jogo da aventura") ;
        setPreferredSize(windowSize) ;
        pack() ;
        Dimension actualWindowSize = getContentPane().getSize() ;
        Dimension extraSize = new Dimension(windowSize.width - actualWindowSize.width, windowSize.height - actualWindowSize.height) ;
        setSize(new Dimension(windowSize.width + extraSize.width, windowSize.height + extraSize.height)) ;      
        setLocation(200, 20) ;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ;
        setVisible(true) ;
        timer = new Timer(0, this) ;			// timer of the game, first number = frame duration
		timer.start() ;							// Game will start checking for keyboard events every "timer" miliseconds
		previousState = GameStates.opening ;
        add(game) ;				// adding game panel on the JFrame
//        System.out.println(Toolkit.getDefaultToolkit().getScreenResolution());
//        System.setProperty("sun.java2d.uiScale", "1.0");
//        System.out.println(Toolkit.getDefaultToolkit().getScreenResolution());
//        System.out.println(getSize());
//        System.out.println(getContentPane().getSize());
//        
//        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
//        System.out.println("Scale Factor: " + gd.getDefaultConfiguration().getDefaultTransform().getScaleX());
//        
//        System.out.println("Frame Size: " + getSize());
//        System.out.println("Content Pane Size: " + getContentPane().getSize());
//        
//        Graphics2D g2 = (Graphics2D) getGraphics();
//        System.out.println("Graphics Scale X: " + g2.getTransform().getScaleX());
//        System.out.println("Graphics Scale Y: " + g2.getTransform().getScaleY());


    }
	
	public static void pauseGame()
	{
        timer.stop() ;
        previousState = Game.getState() ;
        Game.setState(GameStates.paused) ;
	}
	
	public static void resumeGame()
	{
        timer.start() ;
        Game.setState(previousState) ;
	}
	
	public static void main(String[] args)
	{
//		MainGame3_4() ;
		EventQueue.invokeLater(() -> {new MainGame3_4() ;}) ;
	}

	public static void setCursorToDefault()
	{
		game.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)) ;
	}
	
	public static void setCursorToHand()
	{
		game.setCursor(new Cursor(Cursor.HAND_CURSOR)) ;
	}
	
	public static Dimension getWindowsize() { return windowSize ;}

	@Override
    public void actionPerformed(ActionEvent e) 
	{
        if (Game.getShouldRepaint())
        {
        	repaint() ;
        }
		if (e.getSource() != timer)
		{
			System.out.println("action performed = " + e);
			repaint() ;
		}
	}
}
