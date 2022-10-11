package Items;

import java.io.IOException;

import Main.Game;
import Main.Utg;

public class Food extends Item
{
	private int id ;
	private float lifeHeal ;
	private float MPHeal ;
	private int SatiationHeal ;
	
	private static Food[] AllFood ;
	public Food(int id, String Name, String Description, int price, float dropChance, float lifeHeal, float MPHeal, int SatiationHeal)
	{
		super(Name, Description, price, dropChance) ;
		this.id = id ;
		this.lifeHeal = lifeHeal ;
		this.MPHeal = MPHeal ;
		this.SatiationHeal = SatiationHeal ;
	}

	public int getId() {return id ;}
	public float getLifeHeal() {return lifeHeal ;}
	public float getMPHeal() {return MPHeal ;}	
	public int getSatiationHeal() {return SatiationHeal ;}	
	public static Food[] getAll() {return AllFood ;}
	
	
	public static void Initialize() throws IOException
	{
		int NumFood = Utg.count(Game.CSVPath + "Item_Food.csv") ;
		String[][] Input = Utg.ReadTextFile(Game.CSVPath + "Item_Food.csv", NumFood) ;
		AllFood = new Food[NumFood] ;
		for (int p = 0; p <= NumFood - 1; p += 1)
		{
			AllFood[p] = new Food(Integer.parseInt(Input[p][0]), Input[p][1], Input[p][3], Integer.parseInt(Input[p][5]), Float.parseFloat(Input[p][6]), Float.parseFloat(Input[p][7]), Float.parseFloat(Input[p][8]), Integer.parseInt(Input[p][9]));
		}		
	}

	public void printAtt()
	{
		System.out.println("food id: " + AllFood[id].getId() +
				"   name: " + AllFood[id].getName() +
				"   description: " + AllFood[id].getDescription() +
				"   price: " + AllFood[id].getPrice() +
				"   drop chance: " + AllFood[id].getDropChance() + "%" + 
				"   life heal: " + 100 * AllFood[id].getLifeHeal() + "%" + 
				"   mp heal: " + 100 * AllFood[id].getMPHeal() + "%" + 
				"   satiation heal: " + AllFood[id].getSatiationHeal());
	}
	
}
