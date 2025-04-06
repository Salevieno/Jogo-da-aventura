package components;

import java.awt.Point;

import main.Game;
import main.GamePanel;
import utilities.Util;

public class HitboxCircle implements Hitbox
{
	private Point center ;
	private int radius ;
	
	public HitboxCircle(Point center, int radius)
	{
		this.center = center;
		this.radius = radius;
	}

	public boolean overlaps(Hitbox box2)
	{
		if (box2 instanceof HitboxRectangle)
		{
			HitboxRectangle rectBox2 = (HitboxRectangle) box2 ;

			double distX = Math.abs(center.x - rectBox2.getCenter().x) ;
			double distY = Math.abs(center.y - rectBox2.getCenter().y) ;
			
			if (rectBox2.getSize().width / 2 + radius <= distX || rectBox2.getSize().height / 2 + radius <= distY) { return false ;}
			if (distX <= rectBox2.getSize().width / 2 || distY <= rectBox2.getSize().height / 2) { return true ;}
			
			double cornerDistanceSq = (distX - (rectBox2.getSize().width / 2)) * (distX - (rectBox2.getSize().width / 2)) + (distY - (rectBox2.getSize().height / 2)) * (distY - (rectBox2.getSize().height / 2)) ;
	
			return cornerDistanceSq <= (radius * radius);	
		}

		if (box2 instanceof HitboxCircle)
		{
			HitboxCircle circleBox2 = (HitboxCircle) box2 ;
			
			double dist = Util.dist(center, circleBox2.getCenter()) ;

			return dist <= radius + circleBox2.getRadius() ;
		}
		
		return false ;
	}
	
	public void display()
	{
		GamePanel.DP.drawCircle(center, 2 * radius, null, Game.palette[3]);
	}

	public Point getCenter() { return center ;}
	public int getRadius() { return radius ;}
	public void setCenter(Point center) { this.center = center ;}

}
