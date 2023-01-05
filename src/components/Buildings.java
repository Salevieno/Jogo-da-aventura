package components ;

import java.awt.Dimension;
import java.awt.Image ;
import java.awt.Point;

import graphics.DrawingOnPanel;
import utilities.Align;
import utilities.Scale;
import utilities.UtilG;

public class Buildings
{
	private BuildingType type ;
	private Point pos ;
	
	public Buildings(BuildingType type, Point pos)
	{
		this.type = type ;
		this.pos = pos ;
	}

	
	public BuildingType getType() {return type ;}
	public Point getPos() {return pos ;}
	public boolean isInside(Point pos) {return UtilG.isInside(this.pos, pos, new Dimension(type.getImage().getWidth(null), type.getImage().getHeight(null))) ;}
	
	
	/* Drawing methods */
	public void display(Point playerPos, double angle, Scale scale, DrawingOnPanel DP)
	{
		if (isInside(playerPos))
		{				
			Image image = type.getInsideImage() ;
			DP.DrawImage(image, pos, angle, scale, Align.bottomLeft) ;
			if (type.getNPCs() != null)
			{
				for (int n = 0 ; n <= type.getNPCs().size() - 1 ; n += 1)
				{
					type.getNPCs().get(n).display(DP) ;
				}
			}
		}
		else
		{
			Image image = type.getImage() ;
			DP.DrawImage(image, pos, angle, scale, Align.bottomLeft) ;
		}
	}
}