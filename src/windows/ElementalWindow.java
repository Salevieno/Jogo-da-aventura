package windows;

import java.awt.Point;

import graphics.DrawingOnPanel;
import main.Game;
import utilities.Align;
import utilities.Scale;
import utilities.UtilG;

public class ElementalWindow extends GameWindow
{

	public ElementalWindow()
	{
		super("Elemental", UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "Elemental.png"), 1, 1, 1, 1) ;
	}

	@Override
	public void navigate(String action)
	{

	}

	public void display(DrawingOnPanel DP)
	{
		Point windowPos = new Point((int)(0.4*Game.getScreen().getSize().width), (int)(0.2*Game.getScreen().getSize().height)) ;
		double angle = DrawingOnPanel.stdAngle ;
		DP.DrawImage(image, windowPos, angle, new Scale(1, 1), Align.topLeft) ;
	}

}
