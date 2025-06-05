package maps ;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image ;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import components.Collider;
import components.Hitbox;
import graphics.Align;
import graphics.Scale;
import graphics2.Draw;
import graphics2.Drawable;
import main.Game;
import main.GamePanel;
import utilities.Util;

public class MapElement implements Drawable
{
	private int id ;
	private String name ;
	private Image image ;
	private Point topLeft ;
	protected Hitbox hitbox ;
	private List<Collider> colliders ;

	// private static final Image knightsCityWallImage ;
	
	static
	{
		// knightsCityWallImage = UtilS.loadImage("\\MapElements\\" + "Knight'sCityWall.png") ;
	}

	public MapElement(int id, String name, Image image, Point topLeftPos)
	{
		this.id = id ;
		this.name = name ;
		this.topLeft = topLeftPos ;
//		image = getImage(name) ;
		this.image = image ;
		addColliders(name) ;
	}

	public int getid() {return id ;}
	public String getName() {return name ;}
	public Point getPos() {return Util.getPosAt(topLeft, Align.topCenter, Align.bottomCenter, Util.getSize(image)) ;}
	public Hitbox getHitbox() { return hitbox ;}
	public List<Collider> getColliders() {return colliders ;}
	public void setid(int I) {id = I ;}
	public void setName(String N) {name = N ;}
	public void setPos(Point P) {topLeft = P ;}

	public Point center() { return new Point((int) (topLeft.x + 0.5 * image.getHeight(null)), (int) (topLeft.y + 0.5 * image.getHeight(null))) ;}
	private boolean playerIsBehind(Point playerPos) { return Util.isInside(playerPos, topLeft, Util.getSize(image)) ;}

// 	private static Image getImage(String name)
// 	{
// //		Image[] grassImages = new Image[] {grassImage, grassImage2} ;
// //		Set<Image> grassImages = new HashSet<>(Set.of(grassImage, grassImage2)) ;
// 		switch(name)
// 		{
// 			case "Knight'sCityWall": return knightsCityWallImage ;
// //			case "ForestTree": return treeImage ;
// //			case "grass": return grassImages.stream().skip(Util.randomInt(0, grassImages.size() - 1)).findFirst().get() ;
// //			case "rock": return rockImage ;
// //			case "treasureChest": return treasureChestsImage ;
// 			default: return null;
// 		}
// 	}
	
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
	
	public void displayColliders()
	{
		for (Collider collider : colliders)
		{
			GamePanel.DP.drawRect(collider.getPos(), Align.center, new Dimension(1, 1), Color.red, null) ;
		}
	}
	
	public void display(Point playerPos)
	{
		double alpha = playerIsBehind(playerPos) ? 0.5 : 1.0 ;
		
		GamePanel.DP.drawImage(image, topLeft, Draw.stdAngle, Scale.unit, false, false, Align.topLeft, alpha) ;
		if (Game.debugMode && hitbox != null)
		{
			hitbox.display();
		}
		
	}
	
	public void display()
	{
		display(Game.getPlayer().getPos()) ;
	}
}