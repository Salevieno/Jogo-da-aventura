package Items;

import java.io.IOException;
import Main.Game;
import Main.Utg;

public class Potion extends Item
{
	private int id ;
	private float lifeHeal ;
	private float MPHeal ;
	
	private static Potion[] AllPotions ;
	public Potion(int id, String Name, String Description, int price, float dropChance, float lifeHeal, float MPHeal)
	{
		super(Name, Description, price, dropChance) ;
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
		int NumPotions = Utg.count(Game.CSVPath + "Item_Potions.csv") ;
		String[][] Input = Utg.ReadTextFile(Game.CSVPath + "Item_Potions.csv", NumPotions) ;
		AllPotions = new Potion[NumPotions] ;
		for (int p = 0; p <= NumPotions - 1; p += 1)
		{
			AllPotions[p] = new Potion(Integer.parseInt(Input[p][0]), Input[p][1], Input[p][3], Integer.parseInt(Input[p][5]), Float.parseFloat(Input[p][6]), Float.parseFloat(Input[p][7]), Float.parseFloat(Input[p][8]));
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
