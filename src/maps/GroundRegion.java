package maps ;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point ;

import graphics.Align;
import main.Game;
import main.GamePanel;
import utilities.Util;
import utilities.UtilS;

public class GroundRegion
{
	
	private final GroundType type ;
	private final Point topLeftPos ;
	private final Dimension size ;

	private static final Image waterImg = UtilS.loadImage("\\MapElements\\" + "MapElem0_Water.png") ;
	
	public GroundRegion(GroundType type, Point topLeftPos, Dimension size)
	{
		this.type = type ;
		this.topLeftPos = topLeftPos ;
		this.size = size ;
	}
	
	public GroundType getType() { return type ;}
	public Point getTopLeftPos() { return topLeftPos ;}
	public Dimension getSize() { return size ;}

	public boolean containsPoint(Point point) { return Util.isInside(point, topLeftPos, size) ;}

	public void display()
	{
		switch (type)
		{
			case wall: GamePanel.DP.drawRect(topLeftPos, Align.topLeft, size, Game.palette[2], null) ; return ;
			case water: GamePanel.DP.drawRect(topLeftPos, Align.topLeft, size, null, Game.palette[20]) ; return ;
			case lava: GamePanel.DP.drawRect(topLeftPos, Align.topLeft, size, Game.palette[7], null) ; return ;
			case ice: GamePanel.DP.drawRect(topLeftPos, Align.topLeft, size, Game.palette[1], null) ; return ;
			case walkingPath: GamePanel.DP.drawRect(topLeftPos, Align.topLeft, size, Game.palette[10], null) ; return ;
			case invisibleWall: return ;
			default: return ;
		}
	}
	
	@Override
	public String toString() { return "GroundType [type=" + type + ", pos=" + topLeftPos + ", size=" + size + "]" ;}	
	
}
