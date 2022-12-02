package Windows;

import java.awt.Image;
import java.awt.Point;
import javax.swing.ImageIcon;

import Graphics.DrawPrimitives;
import Main.Game;
import Utilities.Scale;

public class QuestWindow extends Window
{
	public static Image image = new ImageIcon(Game.ImagesPath + "Quest.png").getImage() ;
	public QuestWindow()
	{
		super(image, 0, 0, 0, 0) ;
	}
	
	public void display(DrawPrimitives DP)
	{
		Point pos = new Point((int)(0.5 * Game.getScreen().getSize().x), (int)(0.55 * Game.getScreen().getSize().y)) ;
		DP.DrawImage(image, pos, DrawPrimitives.OverallAngle, new Scale(1, 1), new boolean[] {false, false}, "Center", 1) ;
	}
}
