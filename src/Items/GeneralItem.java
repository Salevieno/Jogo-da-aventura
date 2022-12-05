package Items;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import Main.Game;
import Utilities.UtilG;

public class GeneralItem extends Item
{
	private int id ;
	
	private static GeneralItem[] AllGeneralItems ;
	public GeneralItem(int id, String Name, String Description, int price, float dropChance)
	{
		super(Name, Description, new ImageIcon(Game.ImagesPath + "items.png").getImage(), price, dropChance) ;
		this.id = id ;
	}

	public int getId() {return id ;}
	public static GeneralItem[] getAll() {return AllGeneralItems ;}
	
	
	public static void Initialize() throws IOException
	{
		ArrayList<String[]> input = UtilG.ReadcsvFile(Game.CSVPath + "Item_GeneralItem.csv") ;
		AllGeneralItems = new GeneralItem[input.size()] ;
		for (int p = 0; p <= AllGeneralItems.length - 1; p += 1)
		{
			AllGeneralItems[p] = new GeneralItem(Integer.parseInt(input.get(p)[0]), input.get(p)[1], input.get(p)[3], Integer.parseInt(input.get(p)[5]), Float.parseFloat(input.get(p)[6]));
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
