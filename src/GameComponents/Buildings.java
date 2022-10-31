package GameComponents ;

import java.awt.Color ;
import java.awt.Image ;
import java.awt.Point;
import java.util.Arrays;

import Graphics.DrawPrimitives ;
import Utilities.Size;
import Utilities.UtilG;

public class Buildings
{
	private int ID ;
	private String Name ;
	private int Map ;
	private Point Pos ;
	private NPCs[] npc ;	// NPCs in the building
	private Color color ;	// [Outside, inside, door, top]

	public Image[] Images ;
	public Image[] OrnamentImages ;
	public Buildings(int ID, String Name, int Map, Point Pos, Image[] Images, Image[] OrnamentImages, NPCs[] npc, Color color)
	{
		this.ID = ID ;
		this.Name = Name ;
		this.Map = Map ;
		this.Pos = Pos ;
		this.Images = Images ;
		this.OrnamentImages = OrnamentImages ;
		this.npc = npc ;
		this.color = color ;
	}

	public int getID() {return ID ;}
	public String getName() {return Name ;}
	public int getMap() {return Map ;}
	public Point getPos() {return Pos ;}
	public NPCs[] getNPCs() {return npc ;}
	public Color getColors() {return color ;}
	public void setID(int I) {ID = I ;}
	public void setName(String N) {Name = N ;}
	public void setMap(int M) {Map = M ;}
	public void setPos(Point P) {Pos = P ;}
	public void setColors(Color C) {color = C ;}
	
	public boolean hasNPCs()
	{
		return (npc != null) ;
	}
	public boolean playerIsInside(Point playerPos)
	{
		Size imgSize = new Size(Images[0].getWidth(null), Images[0].getHeight(null)) ;
		boolean PlayerIsInside = false ;
		Point PlayerPos = new Point(playerPos.x, playerPos.y) ;
		if (UtilG.isInside(PlayerPos, Pos, imgSize))
		{
			PlayerIsInside = true ;
		}
		return PlayerIsInside ;
	}
	
	
	/* Drawing methods */
	public void display(Point playerPos, float angle, float[] scale, DrawPrimitives DP)
	{
		if (playerIsInside(playerPos))
		{				
			DP.DrawImage(Images[1], Pos, angle, scale, new boolean[] {false, false}, "BotLeft", 1) ;
			if (npc != null)
			{
				for (int n = 0 ; n <= npc.length - 1 ; n += 1)
				{
					npc[n].display(DP) ;
				}
			}
		}
		else
		{
			DP.DrawImage(Images[0], Pos, angle, scale, new boolean[] {false, false}, "BotLeft", 1) ;
		}
	}

	@Override
	public String toString() {
		return "Buildings [ID=" + ID + ", Name=" + Name + ", Map=" + Map + ", Pos=" + Pos + ", npc="
				+ Arrays.toString(npc) + ", color=" + color + ", Images=" + Arrays.toString(Images)
				+ ", OrnamentImages=" + Arrays.toString(OrnamentImages) + "]";
	}
}