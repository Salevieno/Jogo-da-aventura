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
import graphics.UtilAlignment;
import graphics2.Draw;
import graphics2.Drawable;
import main.Game;
import main.GamePanel;
import utilities.Util;

public class MapElement implements Drawable
{
	private final int id ;
	private final String name ;
	private final Image image ;
	private final Point topLeft ;
	protected Hitbox hitbox ;
	private List<Collider> colliders ;

	public MapElement(int id, String name, Image image, Point topLeftPos)
	{
		this.id = id ;
		this.name = name ;
		this.topLeft = topLeftPos ;
		this.image = image ;
		addColliders(name) ;
	}

	public int getid() {return id ;}
	public String getName() {return name ;}
	public Point getPos() {return UtilAlignment.getPosAt(topLeft, Align.topCenter, Align.bottomCenter, Util.getSize(image)) ;}
	public Hitbox getHitbox() { return hitbox ;}
	public List<Collider> getColliders() {return colliders ;}

	public Point center() { return new Point((int) (topLeft.x + 0.5 * image.getHeight(null)), (int) (topLeft.y + 0.5 * image.getHeight(null))) ;}
	private boolean playerIsBehind(Point playerPos) { return Util.isInside(playerPos, topLeft, Util.getSize(image)) ;}

	private void addColliders(String name)
	{
		colliders = new ArrayList<>() ;
		switch(name)
		{
			case "ForestTree": return ;				
			case "grass": return ;				
			case "rock":
				for (int i = 2 ; i <= 20 ; i += 1)
				{
					colliders.add(new Collider(Util.translate(topLeft, i, image.getHeight(null) - 2))) ;
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
	
	public void display()
	{
		double alpha = playerIsBehind(Game.getPlayer().getPos()) ? 0.5 : 1.0 ;
		
		GamePanel.DP.drawImage(image, topLeft, Draw.stdAngle, Scale.unit, false, false, Align.topLeft, alpha) ;
		if (Game.debugMode)
		{
			displayColliders() ;
		}
	}
}