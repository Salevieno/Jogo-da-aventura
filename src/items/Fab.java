package items;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import main.Game;
import utilities.UtilG;

public class Fab extends Item
{
	private int id ;
	
	private static Fab[] AllFabs ;
	public Fab(int id, String Name, String Description, int price, float dropChance)
	{
		super(Name, Description, new ImageIcon(Game.ImagesPath + "items.png").getImage(), price, dropChance) ;
		this.id = id ;
	}

	public int getId() {return id ;}
	public static Fab[] getAll() {return AllFabs ;}
	
	
	public static void Initialize() throws IOException
	{
		ArrayList<String[]> input = UtilG.ReadcsvFile(Game.CSVPath + "Item_Fab.csv") ;
		AllFabs = new Fab[input.size()] ;
		for (int p = 0; p <= AllFabs.length - 1; p += 1)
		{
			AllFabs[p] = new Fab(Integer.parseInt(input.get(p)[0]), input.get(p)[1], input.get(p)[3], Integer.parseInt(input.get(p)[5]), Float.parseFloat(input.get(p)[6]));
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