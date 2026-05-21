package main ;

import java.awt.EventQueue ;

/**
 * made with love by Salevieno and Larissa
 * @author Salevieno
 * @author Larissa
 * @version 0.10
 * @since 2022
 */
public class MainGame3_4
{
	private static final String version = "0.17" ;
		
	public static void main(String[] args)
	{
		EventQueue.invokeLater(() -> {
			GameFrame.create() ;
			GameFrame gameFrame = GameFrame.getMe() ;
			gameFrame.setVisible(true) ;
		}) ;
	}

	public static String getVersion() { return version ;}

	public static void closeGame()
	{
		System.exit(0) ;
	}
}
