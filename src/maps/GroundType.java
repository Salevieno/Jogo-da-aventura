package maps ;

import java.awt.Dimension;
import java.awt.Point ;

import graphics.Align;
import main.Game;
import main.GamePanel;

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

	public void display()
	{
		switch (type)
		{
			case water: GamePanel.DP.drawRect(topLeftPos, Align.topLeft, size, Game.palette[20], null) ; return ;
			case lava: GamePanel.DP.drawRect(topLeftPos, Align.topLeft, size, Game.palette[7], null) ; return ;
			default: return ;
		}
	}
	
	@Override
	public String toString() { return "GroundType [type=" + type + ", pos=" + topLeftPos + ", size=" + size + "]" ;}	
	
}
