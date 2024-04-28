package maps ;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image ;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import components.Collider;
import graphics.Draw;
import graphics.DrawPrimitives;
import libUtil.Align;
import libUtil.Util;
import utilities.Scale;
import utilities.UtilS;

public class MapElement
{
	private int id ;
	private String name ;
	private Image image ;
	private Point topLeft ;
	private List<Collider> colliders ;

	private static final Image knightsCityWallImage ;
	private static final Image treeImage ;
	private static final Image grassImage ;
	private static final Image grassImage2 ;
	private static final Image rockImage ;
	private static final Image treasureChestsImage ;
	
	static
	{
		knightsCityWallImage = UtilS.loadImage("\\MapElements\\" + "Knight'sCityWall.png") ;
		treeImage = UtilS.loadImage("\\MapElements\\" + "MapElem6_TreeForest.png") ;
		grassImage = UtilS.loadImage("\\MapElements\\" + "MapElem8_Grass.png") ;
		grassImage2 = UtilS.loadImage("\\MapElements\\" + "MapElem8_Grass2.png") ;
		rockImage = UtilS.loadImage("\\MapElements\\" + "MapElem9_Rock.png") ;
		treasureChestsImage = UtilS.loadImage("\\MapElements\\" + "MapElem15_Chest.png") ;
	}

	public MapElement(int id, String name, Point pos)
	{
		this.id = id ;
		this.name = name ;
		this.topLeft = pos ;
		image = getImage(name) ;
		addColliders(name) ;
	}

	public int getid() {return id ;}
	public String getName() {return name ;}
	public Point getPos() {return topLeft ;}
	public List<Collider> getColliders() {return colliders ;}
	public void setid(int I) {id = I ;}
	public void setName(String N) {name = N ;}
	public void setPos(Point P) {topLeft = P ;}
	
	private boolean playerIsBehind(Point playerPos) { return Util.isInside(playerPos, topLeft, Util.getSize(image)) ;}

	private static Image getImage(String name)
	{
//		Image[] grassImages = new Image[] {grassImage, grassImage2} ;
		Set<Image> grassImages = new HashSet<>(Set.of(grassImage, grassImage2)) ;
		switch(name)
		{
			case "Knight'sCityWall": return knightsCityWallImage ;
			case "ForestTree": return treeImage ;
			case "grass": return grassImages.stream().skip(Util.randomIntFromTo(0, grassImages.size() - 1)).findFirst().get() ;
			case "rock": return rockImage ;
			case "treasureChest": return treasureChestsImage ;
			default: return null;
		}
	}
	
	private void addColliders(String name)
	{

		colliders = new ArrayList<>() ;
		switch(name)
		{
			case "ForestTree":
				
				return ;
				
			case "grass": 

				return ;
				
			case "rock":
				for (int i = 5 ; i <= 11 ; i += 1)
				{
					colliders.add(new Collider(Util.Translate(topLeft, i, image.getHeight(null) - 2))) ;
				}
				
				return ;
				
			default: return ;
		}
	}
	
	public void displayColliders(DrawPrimitives DP)
	{
		for (Collider collider : colliders)
		{
			DP.drawRect(collider.getPos(), Align.center, new Dimension(1, 1), 1, Color.red, null) ;
		}
	}
	
	public void display(Point playerPos, DrawPrimitives DP)
	{
		double alpha = playerIsBehind(playerPos) ? 0.5 : 1.0 ;
//		if (!playerIsBehind(playerPos))
//		{
//			DP.DrawImage(getImage(name), topLeft, DrawingOnPanel.stdAngle, Scale.unit, Align.topLeft) ;
//
//			
//			return ;
//		}
		
		DP.drawImage(image, topLeft, Draw.stdAngle, Scale.unit, false, false, Align.topLeft, alpha) ;
		
	}
}