package maps ;

import java.awt.Dimension;
import java.awt.Point ;

import graphics.DrawPrimitives;
import libUtil.Align;
import main.Game;

public class GroundType
{
	
	GroundTypes type ;
	Point topLeftPos ;
	Dimension size ;
	
	public GroundType(GroundTypes type, Point topLeftPos, Dimension size)
	{
		this.type = type ;
		this.topLeftPos = topLeftPos ;
		this.size = size ;
	}
	
	public GroundTypes getType() { return type ;}
	public Point getTopLeftPos() { return topLeftPos ;}
	public Dimension getSize() { return size ;}

	public void display(DrawPrimitives DP)
	{
		switch (type)
		{
			case water: DP.drawRect(topLeftPos, Align.topLeft, size, Game.palette[20], null) ; return ;
			case lava: DP.drawRect(topLeftPos, Align.topLeft, size, Game.palette[7], null) ; return ;
			default: return ;
		}
	}
	
	@Override
	public String toString() { return "GroundType [type=" + type + ", pos=" + topLeftPos + ", size=" + size + "]" ;}	
	
}
