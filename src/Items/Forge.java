package Items;

import java.io.IOException;

import Main.Game;
import Utilities.UtilG;

public class Forge extends Item
{
	private int id ;
	
	private static Forge[] AllForge ;
	public Forge(int id, String Name, String Description, int price, float dropChance)
	{
		super(Name, Description, price, dropChance) ;
		this.id = id ;
	}

	public int getId() {return id ;}
	public static Forge[] getAll() {return AllForge ;}
	
	
	public static void Initialize() throws IOException
	{
		int NumForge = UtilG.count(Game.CSVPath + "Item_Forge.csv") ;
		String[][] Input = UtilG.ReadTextFile(Game.CSVPath + "Item_Forge.csv", NumForge) ;
		AllForge = new Forge[NumForge] ;
		for (int p = 0; p <= NumForge - 1; p += 1)
		{
			AllForge[p] = new Forge(Integer.parseInt(Input[p][0]), Input[p][1], Input[p][3], Integer.parseInt(Input[p][5]), Float.parseFloat(Input[p][6]));
		}		
	}

	public void printAtt()
	{
		System.out.println("forge id: " + AllForge[id].getId() +
				"   name: " + AllForge[id].getName() +
				"   description: " + AllForge[id].getDescription() +
				"   price: " + AllForge[id].getPrice() +
				"   drop chance: " + AllForge[id].getDropChance() + "%");
	}
	
}
