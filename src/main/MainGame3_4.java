package main ;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue ;
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
