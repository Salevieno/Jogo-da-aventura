package maps;

import java.awt.Point;
import java.util.List;

import items.Item;

public class TreasureChest extends MapElement
{
	private List<Item> itemRewards ;
	private int goldReward ;

	public TreasureChest(int id, Point pos, List<Item> itemRewards, int goldReward)
	{
		super(id, "treasureChest" + id, pos) ;
		this.itemRewards = itemRewards ;
		this.goldReward = goldReward ;
	}

	public List<Item> getItemRewards() {return itemRewards ;}
	public int getGoldReward() {return goldReward ;}

	@Override
	public String toString() {
		return "TreasureChest [itemRewards=" + itemRewards + ", goldReward=" + goldReward + "]";
	}
	
}
