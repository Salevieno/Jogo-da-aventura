package main ;

import java.awt.EventQueue ;

/**
 * made with love by Salevieno
 * @author Salevieno
 * @version 3.4
 * @since 2022
 */
public class MainGame3_4
{
	private static final GameFrame gameFrame = GameFrame.getMe() ;

	public static void callResizeWindow()
	{
		gameFrame.resizeWindow() ;
	}
	
	public static void closeGame()
	{
		System.exit(0) ;
	}
	
	public static void main(String[] args)
	{
		EventQueue.invokeLater(() -> {gameFrame.setVisible(true) ;}) ;
	}
}
