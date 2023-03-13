package maps ;

import java.awt.Dimension;
import java.awt.Point ;

public class GroundType
{
	
	GroundTypes type ;
	Point pos ;
	Dimension size ;
	
	public GroundType(GroundTypes type, Point pos, Dimension size)
	{
		this.type = type ;
		this.pos = pos ;
		this.size = size ;
	}
	
	public GroundTypes getType() { return type ;}
	public Point getPos() { return pos ;}
	public Dimension getSize() { return size ;}

	@Override
	public String toString() { return "GroundType [type=" + type + ", pos=" + pos + ", size=" + size + "]" ;}	
	
}
