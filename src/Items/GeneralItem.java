package Items;

import java.io.IOException;

import Main.Game;
import Utilities.UtilG;

public class GeneralItem extends Item
{
	private int id ;
	
	private static GeneralItem[] AllGeneralItems ;
	public GeneralItem(int id, String Name, String Description, int price, float dropChance)
	{
		super(Name, Description, price, dropChance) ;
		this.id = id ;
	}

	public int getId() {return id ;}
	public static GeneralItem[] getAll() {return AllGeneralItems ;}
	
	
	public static void Initialize() throws IOException
	{
		int NumGeneralItems = UtilG.count(Game.CSVPath + "Item_GeneralItem.csv") ;
		String[][] Input = UtilG.ReadTextFile(Game.CSVPath + "Item_GeneralItem.csv", NumGeneralItems) ;
		AllGeneralItems = new GeneralItem[NumGeneralItems] ;
		for (int p = 0; p <= NumGeneralItems - 1; p += 1)
		{
			AllGeneralItems[p] = new GeneralItem(Integer.parseInt(Input[p][0]), Input[p][1], Input[p][3], Integer.parseInt(Input[p][5]), Float.parseFloat(Input[p][6]));
		}		
	}

	public void printAtt()
	{
		System.out.println("General item id: " + AllGeneralItems[id].getId() +
				"   name: " + AllGeneralItems[id].getName() +
				"   description: " + AllGeneralItems[id].getDescription() +
				"   price: " + AllGeneralItems[id].getPrice() +
				"   drop chance: " + AllGeneralItems[id].getDropChance() + "%");
	}
	
}
