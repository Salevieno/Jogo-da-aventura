package main;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;

import graphics.DrawPrimitives;
import screen.Screen;
import utilities.Util;

public class GamePanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private static Point mousePos ;
	private static DrawPrimitives DP ;
	private static GamePanel gamePanel ;
	
	private GamePanel()
	{
		setBackground(Palette.colors[0]) ;
		addMouseListener(new MouseEventDemo()) ;
		addMouseWheelListener(new MouseWheelEventDemo()) ;
		addKeyListener(new TAdapter()) ;
		setFocusable(true) ;
	}
	
	protected static void create(Dimension size)
	{
		if (gamePanel != null) { return ;}

		DP = new DrawPrimitives() ;
		gamePanel = new GamePanel() ;
		Screen.create(size, GameFrame.isFullscreen());
		Game.create() ;
	}

	public static GamePanel getMe() { return gamePanel ;}
	public static DrawPrimitives getDP() { return DP ;}
	
	public static void setCursorToDefault()
	{
		gamePanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)) ;
	}
	
	public static void setCursorToHand()
	{
		gamePanel.setCursor(new Cursor(Cursor.HAND_CURSOR)) ;
	}
	
	public static Point getMousePos() { return mousePos ;}
	
	private static void updateMousePos(JPanel panel)
	{
		mousePos = Util.getMousePos(panel) ;
        mousePos.x = (int) (mousePos.x / Screen.getMe().getScale().x) ;
        mousePos.y = (int) (mousePos.y / Screen.getMe().getScale().y) ;
	}

	// Ensure focused when added
	@Override
    public void addNotify()
	{
        super.addNotify();
        requestFocusInWindow();
    }
	
	@Override
	protected void paintComponent(Graphics graphs)
	{
		super.paintComponent(graphs) ;
		updateMousePos(gamePanel) ;
		
        Graphics2D graphs2D = (Graphics2D) graphs ;
        graphs2D.scale(Screen.getMe().getScale().x, Screen.getMe().getScale().y);
		DP.setGraphics(graphs2D) ;
		Game.getMe().update() ;

		Toolkit.getDefaultToolkit().sync() ;
		graphs.dispose() ;
	}

	class TAdapter extends KeyAdapter
	{
		@Override
		public void keyPressed(KeyEvent event)
		{
			Game.getMe().keyPressedAction(event) ;
		}

		@Override
		public void keyReleased(KeyEvent event)
		{
			Game.getMe().keyReleasedAction(event) ;
		}
	}

	class MouseEventDemo implements MouseListener
	{
		@Override
		public void mouseClicked(MouseEvent evt)
		{
			Game.getMe().mouseClickedAction(evt) ;
		}

		@Override
		public void mouseEntered(MouseEvent arg0)
		{
		}

		@Override
		public void mouseExited(MouseEvent arg0)
		{
		}

		@Override
		public void mousePressed(MouseEvent evt)
		{
			Game.getMe().mousePressedAction(evt) ;
		}

		@Override
		public void mouseReleased(MouseEvent evt)
		{
			Game.getMe().mouseReleaseAction(evt) ;
		}
	}

	class MouseWheelEventDemo implements MouseWheelListener
	{
		@Override
		public void mouseWheelMoved(MouseWheelEvent evt)
		{
			Game.getMe().mouseWheelAction(evt) ;
		}
	}

}
