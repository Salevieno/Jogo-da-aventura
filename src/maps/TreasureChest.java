package maps;

import java.awt.Image;
import java.awt.Point;
import java.util.List;

import items.Item;

public class TreasureChest extends MapElements
{
	private List<Item> itemRewards ;
	private int goldReward ;

	public TreasureChest(int id, Point pos, Image image, List<Item> itemRewards, int goldReward)
	{
		super(id, "treasure chest" + id, pos, image) ;
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
