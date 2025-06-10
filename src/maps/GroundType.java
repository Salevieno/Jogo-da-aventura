package maps ;

import java.awt.Dimension;
import java.awt.Point ;

import graphics.Align;
import main.Game;
import main.GamePanel;
import utilities.Util;

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

	public boolean containsPoint(Point point) { return Util.isInside(point, topLeftPos, size) ;}

	public void display()
	{
		switch (type)
		{
			case wall: GamePanel.DP.drawRect(topLeftPos, Align.topLeft, size, Game.palette[2], null) ; return ;
			case water: GamePanel.DP.drawRect(topLeftPos, Align.topLeft, size, Game.palette[20], null) ; return ;
			case lava: GamePanel.DP.drawRect(topLeftPos, Align.topLeft, size, Game.palette[7], null) ; return ;
			default: return ;
		}
	}
	
	@Override
	public String toString() { return "GroundType [type=" + type + ", pos=" + topLeftPos + ", size=" + size + "]" ;}	
	
}
