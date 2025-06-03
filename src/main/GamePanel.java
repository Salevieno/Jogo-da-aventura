package main;

import java.awt.Cursor;
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
import graphics2.Draw;
import utilities.GameTimer;
import utilities.Util;

public class GamePanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private static boolean shouldRepaint ; // tells if the panel should be repainted, created to respond multiple requests only once
	private static Point mousePos ;
	public static final DrawPrimitives DP ;
	private static final GamePanel gamePanel = new GamePanel() ;
	private static final Game game = new Game() ;
	
	static
	{
		DP = new DrawPrimitives() ;
	}
	
	private GamePanel()
	{
		setBackground(Game.palette[0]) ;
		addMouseListener(new MouseEventDemo()) ;
		addMouseWheelListener(new MouseWheelEventDemo()) ;
		addKeyListener(new TAdapter()) ;
		setFocusable(true) ;
	}

	public static GamePanel getMe() { return gamePanel ;}
	
	public static void setCursorToDefault()
	{
		GamePanel.getMe().setCursor(new Cursor(Cursor.DEFAULT_CURSOR)) ;
	}
	
	public static void setCursorToHand()
	{
		GamePanel.getMe().setCursor(new Cursor(Cursor.HAND_CURSOR)) ;
	}
	
	public static Point getMousePos() { return mousePos ;}
	
	public void updateMousePos()
	{
		mousePos = Util.GetMousePos(this) ;
        mousePos.x = (int) (mousePos.x / Game.getScreen().getScale().x) ;
        mousePos.y = (int) (mousePos.y / Game.getScreen().getScale().y) ;
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
		updateMousePos() ;
		
        Graphics2D graphs2D = (Graphics2D) graphs ;
        graphs2D.scale(Game.getScreen().getScale().x, Game.getScreen().getScale().y);
		DP.setGraphics(graphs2D) ;
		GameTimer.updateAll() ;
		game.update() ;

		Toolkit.getDefaultToolkit().sync() ;
		graphs.dispose() ;
	}

	class TAdapter extends KeyAdapter
	{
		@Override
		public void keyPressed(KeyEvent event)
		{
			shouldRepaint = true ;			
			game.keyAction(event) ;
		}

		@Override
		public void keyReleased(KeyEvent e)
		{

		}
	}

	class MouseEventDemo implements MouseListener
	{
		@Override
		public void mouseClicked(MouseEvent evt)
		{
			game.mouseClickedAction(evt) ;
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
			game.mousePressedAction(evt) ;
		}

		@Override
		public void mouseReleased(MouseEvent evt)
		{
			game.mouseReleaseAction(evt) ;
		}
	}

	class MouseWheelEventDemo implements MouseWheelListener
	{
		@Override
		public void mouseWheelMoved(MouseWheelEvent evt)
		{
			game.mouseWheelAction(evt) ;
		}
	}

}
