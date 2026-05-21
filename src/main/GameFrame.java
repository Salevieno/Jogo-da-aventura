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

public class GameFrame extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L ;
	
	private Timer timer ;		// Main timer of the game
	private GameStates previousState ;
	private boolean fullScreen = false ;
	private final Dimension windowSize = new Dimension(1280,960) ; // TODO consider 1280 x 720
	private final Image icon = ImageLoader.loadImage(Path.GAME_IMG + "gameIcon.png") ;
	private static GameFrame gameFrame ;

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
		
		resizeWindow();
		setIconImage(icon) ;
        setTitle("Jogo da aventura") ;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ;
        setVisible(true) ;
        timer = new Timer(0, this) ;			// timer of the game, first number = frame duration
		timer.start() ;							// Game will start checking for keyboard events every "timer" miliseconds
		previousState = GameStates.opening ;
		

    }
	
	protected static void create()
	{
		if (gameFrame != null) { return ;}

		gameFrame = new GameFrame() ;
		GamePanel.create(gameFrame.windowSize) ;
        gameFrame.add(GamePanel.getMe()) ;
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
        gameFrame.timer.stop() ;
        gameFrame.previousState = Game.getState() ;
        Game.setState(GameStates.paused) ;
	}

	public static void resumeGame()
	{
        gameFrame.timer.start() ;
        Game.setState(gameFrame.previousState) ;
	}
	
	public static void closeGame()
	{
		System.exit(0) ;
	}
	
	public static boolean isFullscreen() { return gameFrame.fullScreen ;}
	public static Dimension getWindowsize() { return gameFrame.windowSize ;}
	public static int width() { return gameFrame.windowSize.width ;}
	public static int height() { return gameFrame.windowSize.height ;}

	@Override
    public void actionPerformed(ActionEvent e) 
	{
        if (Game.getShouldRepaint())
        {
        	repaint() ;
        }
		if (e.getSource() != timer)
		{
			Log.info("action performed = " + e);
			repaint() ;
		}
	}
}
