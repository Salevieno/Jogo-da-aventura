package maps;

import java.awt.Image;
import java.awt.Point;
import java.util.List;

import components.HitboxRectangle;
import items.Item;
import main.Game;
import main.Path;
import utilities.Util;


public class TreasureChest extends MapElement
{
	private final List<Item> items ;
	private final int gold ;
	
	private static final Image treasureChestsImage ;

	static
	{
		treasureChestsImage = Game.loadImage(Path.MAP_ELEMENTS_IMG + "MapElem15_Chest.png") ;
	}
	
	public TreasureChest(int id, Point pos, List<Item> items, int gold)
	{
		super(id, "treasureChest" + id, treasureChestsImage, pos) ;
		hitbox = new HitboxRectangle(center(), Util.getSize(treasureChestsImage), 0.8) ;
		this.items = items ;
		this.gold = gold ;
	}

	public List<Item> getItemRewards() {return items ;}
	public int getGoldReward() {return gold ;}

	@Override
	public String toString() {
		return "TreasureChest [itemRewards=" + items + ", goldReward=" + gold + "]";
	}
	
}
