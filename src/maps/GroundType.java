package maps ;

import java.awt.Point ;

public class GroundType
{
	
	GroundTypes type ;
	Point pos ;
	
	public GroundType(GroundTypes type, Point pos)
	{
		this.type = type ;
		this.pos = pos ;
	}
	
	public GroundTypes getType() { return type ;}
	public Point getPos() { return pos ;}

	@Override
	public String toString() { return "GroundType [type=" + type + ", pos=" + pos + "]" ;}	
	
}
