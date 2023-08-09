package items;

import java.awt.Image;
import java.util.List;

import main.Game;
import utilities.UtilG;

public class QuestItem extends Item
{
	private static QuestItem[] AllQuests ;
	
	private static final Image questItemIcon = UtilG.loadImage(Game.ImagesPath + "\\Windows\\bagIcons\\" + "IconQuestItem.png") ;
	
	static
	{
		List<String[]> input = UtilG.ReadcsvFile(Game.CSVPath + "Item_Quest.csv") ;
		AllQuests = new QuestItem[input.size()] ;
		for (int p = 0; p <= AllQuests.length - 1; p += 1)
		{
			AllQuests[p] = new QuestItem(Integer.parseInt(input.get(p)[0]), input.get(p)[1], input.get(p)[3], Integer.parseInt(input.get(p)[5]), Float.parseFloat(input.get(p)[6]));
		}
	}
	public QuestItem(int id, String Name, String Description, int price, float dropChance)
	{
		super(id, Name, Description, imageFromID(id), price, dropChance) ;
	}

	public static QuestItem[] getAll() {return AllQuests ;}

	public static Image imageFromID(int id)
	{		
		return questItemIcon ;		
	}

	@Override
	public String toString()
	{
//		return "QuestItem [id=" + id + ", name=" + name + ", description=" + description + ", image=" + image + ", price=" + price + ", dropChance=" + dropChance + "]";
		return "QuestItem," + id + "," + name;
	}
	
	
	
//	public void printAtt()
//	{
//		System.out.println("Quest id: " + AllQuests[id].getId() +
//				"   name: " + AllQuests[id].getName() +
//				"   description: " + AllQuests[id].getDescription() +
//				"   price: " + AllQuests[id].getPrice() +
//				"   drop chance: " + AllQuests[id].getDropChance() + "%");
//	}
	
}
