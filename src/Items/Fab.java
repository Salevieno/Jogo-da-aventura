package Items;

import java.io.IOException;

import Main.Game;
import Main.Utg;

public class Fab extends Item
{
	private int id ;
	
	private static Fab[] AllFabs ;
	public Fab(int id, String Name, String Description, int price, float dropChance)
	{
		super(Name, Description, price, dropChance) ;
		this.id = id ;
	}

	public int getId() {return id ;}
	public static Fab[] getAll() {return AllFabs ;}
	
	
	public static void Initialize() throws IOException
	{
		int NumFabs = Utg.count(Game.CSVPath + "Item_Fab.csv") ;
		String[][] Input = Utg.ReadTextFile(Game.CSVPath + "Item_Fab.csv", NumFabs) ;
		AllFabs = new Fab[NumFabs] ;
		for (int p = 0; p <= NumFabs - 1; p += 1)
		{
			AllFabs[p] = new Fab(Integer.parseInt(Input[p][0]), Input[p][1], Input[p][3], Integer.parseInt(Input[p][5]), Float.parseFloat(Input[p][6]));
		}		
	}

	public void printAtt()
	{
		System.out.println("Fab id: " + AllFabs[id].getId() +
				"   name: " + AllFabs[id].getName() +
				"   description: " + AllFabs[id].getDescription() +
				"   price: " + AllFabs[id].getPrice() +
				"   drop chance: " + AllFabs[id].getDropChance() + "%");
	}
	
}
