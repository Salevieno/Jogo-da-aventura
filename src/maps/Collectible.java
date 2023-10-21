package maps;

import java.awt.Point;

import graphics.DrawingOnPanel;
import items.Item;
import main.Game;
import utilities.Align;
import utilities.TimeCounter;

public class Collectible extends Item
{
	
	private int itemID ;
	private int level ;
	private Point pos ;
	private TimeCounter counter ;
	
	public Collectible(int itemID, int level, Point pos, int delay)
	{
		super(itemID, Game.getAllItems()[itemID].getName(), Game.getAllItems()[itemID].getDescription(),
					Game.getAllItems()[itemID].getImage(), Game.getAllItems()[itemID].getPrice(),
					Game.getAllItems()[itemID].getDropChance()) ;
		this.itemID = itemID ;
		this.level = level ;
		this.pos = pos ;
		
		counter = new TimeCounter(0, delay) ;
		
	}

	public int getLevel() { return level ; }
	public Point getPos() { return pos ; }
	public TimeCounter getCounter() { return counter ; }
	public Item getItem() {return Game.getAllItems()[itemID] ;}
	
	public int type()
	{
		if (itemID == 220) { return 0 ;}
		
		return itemID % 3 + 1 ;
	}
	
	public void display(DrawingOnPanel DP) {DP.DrawImage(image, pos, Align.center) ;}

	public String toString()
	{
		return "Collectible," + getItem().getName() ;
	}
	
}
