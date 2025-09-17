package components;

import java.awt.Dimension;
import java.awt.Point;

import graphics.Align;
import main.Game;
import main.GamePanel;

public class HitboxRectangle implements Hitbox
{
	private Point center ;
	private Dimension size ;

	public HitboxRectangle(Point center, Dimension size, double scale)
	{
		this.center = center ;
		this.size = new Dimension((int) (scale * size.width), (int) (scale * size.height)) ;
	}
	
	public HitboxRectangle(Point center, Dimension size)
	{
		this(center, size, 1.0);
	}
	
	public boolean pointIsInside(Point point)
	{		
		double distX = Math.abs(center.x - point.x) ;
		double distY = Math.abs(center.y - point.y) ;
		
		return distX <= size.width / 2 && distY <= size.height / 2 ;
	}
	
	public boolean overlaps(Hitbox box2)
	{
		if (box2 instanceof HitboxRectangle)
		{
			HitboxRectangle rectBox2 = (HitboxRectangle) box2 ;
			
			double distX = Math.abs(center.x - rectBox2.getCenter().x) ;
			double distY = Math.abs(center.y - rectBox2.getCenter().y) ;

			return distX <= (size.width + rectBox2.getSize().width) / 2 && distY <= (size.height + rectBox2.getSize().height) / 2 ;
		}

		if (box2 instanceof HitboxCircle)
		{
			HitboxCircle circleBox2 = (HitboxCircle) box2 ;

			double distX = Math.abs(center.x - circleBox2.getCenter().x) ;
			double distY = Math.abs(center.y - circleBox2.getCenter().y) ;
			
			if (size.width / 2 + circleBox2.getRadius() <= distX || size.height / 2 + circleBox2.getRadius() <= distY) { return false ;}
			if (distX <= size.width / 2 || distY <= size.height / 2) { return true ;}
			
			double cornerDistanceSq = (distX - (size.width / 2)) * (distX - (size.width / 2)) + (distY - (size.height / 2)) * (distY - (size.height / 2)) ;
	
			return cornerDistanceSq <= (circleBox2.getRadius() * circleBox2.getRadius());
		}
		
		return false ;
	}
	
	public void display()
	{
		GamePanel.DP.drawRect(center, Align.center, size, null, Game.palette[3]);
	}

	public Point getCenter() { return center ;}
	public Dimension getSize() { return size ;}
	public void setCenter(Point center) { this.center = center ;}
}
