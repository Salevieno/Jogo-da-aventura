package windows;

import java.awt.Dimension;
import java.awt.Point;

import graphics.DrawingOnPanel;
import main.Game;
import utilities.Align;
import utilities.UtilG;

public class CreatureAttributesWindow extends AttributesWindow
{
	public CreatureAttributesWindow()
	{
		super(UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "CreatureAttWindow.png"), 1);
	}

	public void display(DrawingOnPanel DP)
	{

		Dimension screenSize = Game.getScreen().getSize() ;
		Point windowPos = new Point((int) (0.2 * screenSize.width), (int)(0.2 * screenSize.height)) ;
		DP.DrawImage(image, windowPos, Align.topLeft) ;
		
	}
}
