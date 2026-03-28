package Buildings;

import java.awt.Image;
import java.awt.Point;

import graphics.Align;
import graphics.Scale;
import graphics2.Draw;
import main.GamePanel;
import main.ImageLoader;
import main.Path;

public class Forge
{    
	private final Point pos ;
	private static final Image image = ImageLoader.loadImage(Path.BUILDINGS_IMG + "Forge.png") ;

	public Forge(Point pos)
	{
		this.pos = pos ;
	}

    public void display()
    {
		GamePanel.DP.drawImage(image, pos, Draw.stdAngle, Scale.unit, Align.center) ;
    }
}
