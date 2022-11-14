package Items;

import java.io.IOException;
import java.util.ArrayList;

import Main.Game;
import Utilities.UtilG;

public class QuestItem extends Item
{
private int id ;
	
	private static QuestItem[] AllQuests ;
	public QuestItem(int id, String Name, String Description, int price, float dropChance)
	{
		super(Name, Description, price, dropChance) ;
		this.id = id ;
	}

	public int getId() {return id ;}
	public static QuestItem[] getAll() {return AllQuests ;}
	
	
	public static void Initialize() throws IOException
	{
		ArrayList<String[]> input = UtilG.ReadcsvFile(Game.CSVPath + "Item_Quest.csv") ;
		AllQuests = new QuestItem[input.size()] ;
		for (int p = 0; p <= AllQuests.length - 1; p += 1)
		{
			AllQuests[p] = new QuestItem(Integer.parseInt(input.get(p)[0]), input.get(p)[1], input.get(p)[3], Integer.parseInt(input.get(p)[5]), Float.parseFloat(input.get(p)[6]));
		}		
	}

	public void printAtt()
	{
		System.out.println("Quest id: " + AllQuests[id].getId() +
				"   name: " + AllQuests[id].getName() +
				"   description: " + AllQuests[id].getDescription() +
				"   price: " + AllQuests[id].getPrice() +
				"   drop chance: " + AllQuests[id].getDropChance() + "%");
	}
	
}
