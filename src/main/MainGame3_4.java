package main ;

import java.awt.EventQueue ;

/**
 * made with love by Salevieno and Larissa
 * @author Salevieno
 * @author Larissa
 * @version 0.7
 * @since 2022
 */
public class MainGame3_4
{
	private static final GameFrame gameFrame = GameFrame.getMe() ;
	private static final String version = "0.7 - última antes das artes feitas por Lari" ;
	
	public static void closeGame()
	{
		System.exit(0) ;
	}

	public static String getVersion() { return version ;}
	
	public static void main(String[] args)
	{
		EventQueue.invokeLater(() -> {gameFrame.setVisible(true) ;}) ;
	}
}
