package main ;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame ;
import javax.swing.Timer;

import utilities.GameStates;
import utilities.Util;

public class GameFrame extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L ;
	
	private static Timer timer ;		// Main timer of the game
	private static GameStates previousState ;
	private static final Dimension screenSizeSmall = new Dimension(640,480) ;
	private static final Dimension screenSizeBig = new Dimension(1280,960) ;
	private static final Dimension windowSize = screenSizeBig ;
	private static final Image icon = Util.loadImage(".\\images\\gameIcon.png") ;
	private static final GameFrame gameFrame = new GameFrame() ;
	public static boolean fullScreen = false ;

	private GameFrame()
    {
		
        System.setProperty("sun.java2d.uiScale", "1.0");
        
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		
		if (fullScreen && gd.isFullScreenSupported())
		{
            setUndecorated(true) ;
            gd.setFullScreenWindow(this);
            pack() ;
            setSize(Toolkit.getDefaultToolkit().getScreenSize()); 
        }
		else
        {
			pack() ;
			Insets insets = getInsets();
			setSize(windowSize.width + insets.left + insets.right, windowSize.height + insets.top + insets.bottom);
			setLocation(200, 20) ;
        }
		
		setIconImage(icon) ;
        setTitle("Jogo da aventura") ;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ;
        setVisible(true) ;
        timer = new Timer(0, this) ;			// timer of the game, first number = frame duration
		timer.start() ;							// Game will start checking for keyboard events every "timer" miliseconds
		previousState = GameStates.opening ;

        add(GamePanel.getMe()) ;								// adding game panel on the JFrame
//        add(game) ;								// adding game panel on the JFrame

    }
	
	public static GameFrame getMe() { return gameFrame ;}
	
	public void resizeWindow()
	{
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		
	    // Dispose the frame before changing decoration settings
	    dispose();
	    
		if (!fullScreen && gd.isFullScreenSupported())
		{
            setUndecorated(true) ;
            gd.setFullScreenWindow(this);
            pack() ;
            setSize(Toolkit.getDefaultToolkit().getScreenSize()); 
		}
		else
		{
			setUndecorated(false);
	        gd.setFullScreenWindow(null); // Handle exiting fullscreen properly before restoring window decorations
			pack() ;
			Insets insets = getInsets();
			setSize(windowSize.width + insets.left + insets.right, windowSize.height + insets.top + insets.bottom);
			setLocation(200, 20) ;
		}
		
	    setVisible(true);
	    
		fullScreen = !fullScreen ;
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
	
	public static void closeGame()
	{
		System.exit(0) ;
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
