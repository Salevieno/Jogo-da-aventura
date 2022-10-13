package GameComponents ;

import java.awt.Color ;
import java.awt.Image ;
import java.awt.Point;

import Graphics.DrawPrimitives ;
import Main.Utg ;

public class Buildings
{
	private int ID ;
	private String Name ;
	private int Map ;
	private Point Pos ;		// [x, y]
	private Color color ;	// [Outside, inside, door, top]

	public Image[] Images ;
	public Image[] OrnamentImages ;
	public Buildings(int ID, String Name, int Map, Point Pos, Image[] Images, Image[] OrnamentImages, Color color)
	{
		this.ID = ID ;
		this.Name = Name ;
		this.Map = Map ;
		this.Pos = Pos ;
		this.Images = Images ;
		this.OrnamentImages = OrnamentImages ;
		this.color = color ;
	}

	public int getID() {return ID ;}
	public String getName() {return Name ;}
	public int getMap() {return Map ;}
	public Point getPos() {return Pos ;}
	public Color getColors() {return color ;}
	public void setID(int I) {ID = I ;}
	public void setName(String N) {Name = N ;}
	public void setMap(int M) {Map = M ;}
	public void setPos(Point P) {Pos = P ;}
	public void setColors(Color C) {color = C ;}
	
	public boolean PlayerIsInsideBuilding(Point playerPos)
	{
		int[] Size = new int[] {Images[0].getWidth(null), Images[0].getHeight(null)} ;
		boolean PlayerIsInside = false ;
		Point PlayerPos = new Point(playerPos.x, playerPos.y) ;
		//if (Pos[0] < PlayerPos[0] & PlayerPos[0] < Pos[0] + Size[0] & PlayerPos[1] < Pos[1] & Pos[1] - Size[1] < PlayerPos[1])
		if (Utg.isInside(PlayerPos, Pos, Size[0], Size[1]))
		{
			PlayerIsInside = true ;
		}
		return PlayerIsInside ;
	}
	
	
	/* Drawing methods */
	public void DrawBuilding(Point playerPos, float angle, float[] scale, DrawPrimitives DP)
	{
		if (PlayerIsInsideBuilding(playerPos))
		{				
			DP.DrawImage(Images[1], Pos, angle, scale, new boolean[] {false, false}, "BotLeft", 1) ;
		}
		else
		{
			DP.DrawImage(Images[0], Pos, angle, scale, new boolean[] {false, false}, "BotLeft", 1) ;
		}
	}
}