package maps;

import java.awt.Point;

import graphics.Draw;
import graphics.DrawPrimitives;
import items.Item;
import main.Game;
import utilities.Align;
import utilities.FrameCounter;
import utilities.UtilG;

public class Collectible extends Item
{
	
	private int itemID ;
	private int level ;
	private Point pos ;
	private FrameCounter counter ;
	
	public Collectible(int itemID, int level, Point pos, int delay)
	{
		super(itemID, Game.getAllItems()[itemID].getName(), Game.getAllItems()[itemID].getDescription(),
				type(itemID).getImage(), Game.getAllItems()[itemID].getPrice(),
					Game.getAllItems()[itemID].getDropChance()) ;
		this.itemID = itemID ;
		this.level = level ;
		this.pos = pos ;
		
		counter = new FrameCounter(0, delay) ;
		
	}

	public int getLevel() { return level ; }
	public Point getPos() { return pos ; }
	public FrameCounter getCounter() { return counter ; }
	public Item getItem() {return Game.getAllItems()[itemID] ;}

	public int typeNumber()
	{
		return typeID(itemID) ;
	}
	
	public static int typeID(int itemID)
	{
		if (itemID == 220) { return 0 ;}
		
		return itemID % 3 + 1 ;
	}
	
	private static CollectibleTypes type(int itemID)
	{
		return CollectibleTypes.values()[typeID(itemID)] ;
	}

	public void displayInfo(Point pos, Align align, DrawPrimitives DP)
	{
		Draw.menu(pos, align, UtilG.getSize(infoMenu)) ;
	}
	
	public void display(DrawPrimitives DP) {DP.drawImage(image, pos, Align.center) ;}

	public String toString()
	{
		return "Collectible " + getItem().getName() ;
	}
	
}
