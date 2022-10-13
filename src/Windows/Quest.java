package Windows;

import java.awt.Image;
import java.awt.Point;
import javax.swing.ImageIcon;
import Graphics.DrawFunctions;
import Graphics.DrawPrimitives;
import Main.Game;

public class Quest extends Window
{
	public static Image image = new ImageIcon(Game.ImagesPath + "Quest.png").getImage() ;
	public Quest()
	{
		super(image, 0, 0, 0, 0) ;
	}
	
	public void display(DrawFunctions DF)
	{
		DrawPrimitives DP = DF.getDrawPrimitives() ;
		float OverallAngle = DF.getOverallAngle() ;
		int[] WinDim = Game.getScreen().getDimensions() ;
		Point pos = new Point((int)(0.5*WinDim[0]), (int)(0.55*WinDim[1])) ;
		DP.DrawImage(image, pos, OverallAngle, new float[] {1, 1}, new boolean[] {false, false}, "Center", 1) ;
	}
}
