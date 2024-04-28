package maps;

import java.awt.Point;

import graphics.Draw;
import graphics.DrawPrimitives;
import items.Item;
import libUtil.Align;
import libUtil.Util;
import utilities.FrameCounter;

public class Collectible extends Item
{
	
	private int itemID ;
	private int level ;
	private Point pos ;
	private FrameCounter counter ;
	
	public Collectible(int itemID, int level, Point pos, int delay)
	{
		super(itemID, Item.allItems.get(itemID).getName(), Item.allItems.get(itemID).getDescription(),
				type(itemID).getImage(), Item.allItems.get(itemID).getPrice(),
					Item.allItems.get(itemID).getDropChance()) ;
		this.itemID = itemID ;
		this.level = level ;
		this.pos = pos ;
		
		counter = new FrameCounter(0, delay) ;
		
	}

	public int getLevel() { return level ; }
	public Point getPos() { return pos ; }
	public FrameCounter getCounter() { return counter ; }
	public Item getItem() {return Item.allItems.get(itemID) ;}

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

	public double chance(int playerLevel) { return 1 - 1 / (1 + Math.pow(1.1, playerLevel - level));}
	
	public void displayInfo(Point pos, Align align, DrawPrimitives DP)
	{
		Draw.menu(pos, align, Util.getSize(infoMenu)) ;
	}
	
	public void display(DrawPrimitives DP) {DP.drawImage(image, pos, Align.center) ;}

	public String toString()
	{
		return "Collectible " + getItem().getName() ;
	}
	
}
