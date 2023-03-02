package components;

import java.awt.Point;

public class Collider
{
	private Point pos ;

	public Collider(Point pos)
	{
		this.pos = pos;
	}

	public Point getPos() { return pos ;}

	@Override
	public String toString()
	{
		return "Collider [pos=" + pos + "]";
	}
	
	
}
