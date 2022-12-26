package items;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import main.Game;
import utilities.UtilG;

public class Potion extends Item
{
	private int id ;
	private float lifeHeal ;
	private float MPHeal ;
	
	private static Potion[] AllPotions ;
	public Potion(int id, String Name, String Description, int price, float dropChance, float lifeHeal, float MPHeal)
	{
		super(Name, Description, new ImageIcon(Game.ImagesPath + "items.png").getImage(), price, dropChance) ;
		this.id = id ;
		this.lifeHeal = lifeHeal ;
		this.MPHeal = MPHeal ;
	}

	public int getId() {return id ;}
	public float getLifeHeal() {return lifeHeal ;}
	public float getMPHeal() {return MPHeal ;}	
	public static Potion[] getAll() {return AllPotions ;}
	
	
	public static void Initialize() throws IOException
	{
		ArrayList<String[]> input = UtilG.ReadcsvFile(Game.CSVPath + "Item_Potions.csv") ;
		AllPotions = new Potion[input.size()] ;
		for (int p = 0; p <= AllPotions.length - 1; p += 1)
		{
			AllPotions[p] = new Potion(Integer.parseInt(input.get(p)[0]), input.get(p)[1], input.get(p)[3], Integer.parseInt(input.get(p)[5]), Float.parseFloat(input.get(p)[6]), Float.parseFloat(input.get(p)[7]), Float.parseFloat(input.get(p)[8]));
		}		
	}

	public void printAtt()
	{
		System.out.println("potion id: " + AllPotions[id].getId() +
				"   name: " + AllPotions[id].getName() +
				"   description: " + AllPotions[id].getDescription() +
				"   price: " + AllPotions[id].getPrice() +
				"   drop chance: " + AllPotions[id].getDropChance() + "%" + 
				"   life heal: " + 100 * AllPotions[id].getLifeHeal() + "%" + 
				"   mp heal: " + 100 * AllPotions[id].getMPHeal() + "%");
	}
	
}
