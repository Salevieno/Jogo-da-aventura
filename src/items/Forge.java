package items;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import main.Game;
import utilities.UtilG;

public class Forge extends Item
{
	private int id ;
	
	private static Forge[] AllForge ;
	public Forge(int id, String Name, String Description, int price, float dropChance)
	{
		super(Name, Description, new ImageIcon(Game.ImagesPath + "items.png").getImage(), price, dropChance) ;
		this.id = id ;
	}

	public int getId() {return id ;}
	public static Forge[] getAll() {return AllForge ;}
	
	
	public static void Initialize() throws IOException
	{
		ArrayList<String[]> input = UtilG.ReadcsvFile(Game.CSVPath + "Item_Forge.csv") ;
		AllForge = new Forge[input.size()] ;
		for (int p = 0; p <= AllForge.length - 1; p += 1)
		{
			AllForge[p] = new Forge(Integer.parseInt(input.get(p)[0]), input.get(p)[1], input.get(p)[3], Integer.parseInt(input.get(p)[5]), Float.parseFloat(input.get(p)[6]));
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
