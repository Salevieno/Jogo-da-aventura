package maps;

import java.awt.Point;

import components.Hitbox;
import components.HitboxRectangle;
import graphics.Align;
import graphics.DrawPrimitives;
import graphics2.Draw;
import items.Item;
import main.Game;
import utilities.Util;

public class Collectible extends Item
{
	
	private int itemID ;
	private int level ;
	private Point pos ;
	private Hitbox hitbox ;
	
	public Collectible(int itemID, int level, Point pos)
	{
		super(itemID, Item.allItems.get(itemID).getName(), Item.allItems.get(itemID).getDescription(),
				type(itemID).getImage(), Item.allItems.get(itemID).getPrice(), Item.allItems.get(itemID).getDropChance()) ;		
		this.itemID = itemID ;
		this.level = level ;
		this.pos = pos ;
		this.hitbox = new HitboxRectangle(pos, Util.getSize(type(itemID).getImage()), 0.8) ;
	}

	public int getLevel() { return level ; }
	public Point getPos() { return pos ; }
	public Hitbox getHitbox() { return hitbox ;}
	public Item getItem() {return Item.allItems.get(itemID) ;}

	public int typeNumber() { return typeID(itemID) ;}
	
	public static int typeID(int itemID)
	{
		if (itemID == 220) { return 0 ;}
		
		return itemID % 3 + 1 ;
	}
	
	private static CollectibleTypes type(int itemID) { return CollectibleTypes.values()[typeID(itemID)] ;}

	public double chance(int playerLevel) { return 1 - 1 / (1 + Math.pow(1.1, playerLevel - level)) ;}
	
	public void displayInfo(Point pos, Align align, DrawPrimitives DP)
	{
		Draw.menu(pos, align, Util.getSize(infoMenu)) ;
	}
	
	public void display(DrawPrimitives DP)
	{
		DP.drawImage(image, pos, Align.center) ;
		if (Game.displayHitboxes)
		{
			hitbox.display(DP) ;
		}
	}

	public String toString()
	{
		return "Collectible " + getItem().getName() ;
	}
	
}
