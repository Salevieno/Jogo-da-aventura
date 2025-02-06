package components;

import java.awt.Point;

public interface Hitbox
{
	public Point getCenter() ;
	public void setCenter(Point center) ;
	public boolean overlaps(Hitbox box2) ;
	public void display() ;
}
