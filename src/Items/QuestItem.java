package Items;

import java.io.IOException;

import Main.Game;
import Main.Utg;

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
		int NumQuests = Utg.count(Game.CSVPath + "Item_Quest.csv") ;
		String[][] Input = Utg.ReadTextFile(Game.CSVPath + "Item_Quest.csv", NumQuests) ;
		AllQuests = new QuestItem[NumQuests] ;
		for (int p = 0; p <= NumQuests - 1; p += 1)
		{
			AllQuests[p] = new QuestItem(Integer.parseInt(Input[p][0]), Input[p][1], Input[p][3], Integer.parseInt(Input[p][5]), Float.parseFloat(Input[p][6]));
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
