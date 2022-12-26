package components ;

import java.awt.Dimension;
import java.awt.Image ;
import java.awt.Point;
import java.util.ArrayList;

import graphics.DrawingOnPanel;
import utilities.AlignmentPoints;
import utilities.Scale;
import utilities.UtilG;

public class Buildings
{
	private BuildingType buildingType ;
	private Point pos ;
	private ArrayList<NPCs> npc ;	// NPCs in the building
	
	public Buildings(BuildingType type, Point pos, ArrayList<NPCs> npc)
	{
		this.buildingType = type ;
		this.pos = pos ;
		this.npc = npc ;
	}

	
	public BuildingType getType() {return buildingType ;}
	public Point getPos() {return pos ;}
	public ArrayList<NPCs> getNPCs() {return npc ;}
	
	public boolean hasNPCs()
	{
		return (npc != null) ;
	}
	public boolean playerIsInside(Point playerPos)
	{
		Image image = buildingType.getOutsideImage() ;
		Dimension imgSize = new Dimension(image.getWidth(null), image.getHeight(null)) ;
		boolean PlayerIsInside = false ;
		Point PlayerPos = new Point(playerPos.x, playerPos.y) ;
		if (UtilG.isInside(PlayerPos, pos, imgSize))
		{
			PlayerIsInside = true ;
		}
		return PlayerIsInside ;
	}
	
	
	/* Drawing methods */
	public void display(Point playerPos, double angle, Scale scale, DrawingOnPanel DP)
	{
		if (playerIsInside(playerPos))
		{				
			Image image = buildingType.getInsideImage() ;
			DP.DrawImage(image, pos, angle, scale, AlignmentPoints.bottomLeft) ;
			if (npc != null)
			{
				for (int n = 0 ; n <= npc.size() - 1 ; n += 1)
				{
					npc.get(n).display(DP) ;
				}
			}
		}
		else
		{
			Image image = buildingType.getOutsideImage() ;
			DP.DrawImage(image, pos, angle, scale, AlignmentPoints.bottomLeft) ;
		}
	}
}