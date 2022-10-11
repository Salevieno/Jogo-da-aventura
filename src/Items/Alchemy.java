package Items;

import java.io.IOException;

import Main.Game;
import Main.Utg;

public class Alchemy extends Item
{
	private int id ;
	private float lifeHeal ;
	private float MPHeal ;
	
	private static Alchemy[] AllAlchemy ;
	public Alchemy(int id, String Name, String Description, int price, float dropChance, float lifeHeal, float MPHeal)
	{
		super(Name, Description, price, dropChance) ;
		this.lifeHeal = lifeHeal ;
		this.MPHeal = MPHeal ;
		
	}
	public int getId() {return id ;}
	public float getLifeHeal() {return lifeHeal ;}
	public float getMPHeal() {return MPHeal ;}
	public static Alchemy[] getAll() {return AllAlchemy ;}
	
	public static void Initialize() throws IOException
	{
		int NumAlchemy = Utg.count(Game.CSVPath + "Item_Alchemy.csv") ;
		String[][] Input = Utg.ReadTextFile(Game.CSVPath + "Item_Alchemy.csv", NumAlchemy) ;
		AllAlchemy = new Alchemy[NumAlchemy] ;
		for (int a = 0; a <= NumAlchemy - 1; a += 1)
		{
			AllAlchemy[a] = new Alchemy(Integer.parseInt(Input[a][0]), Input[a][1], Input[a][3], Integer.parseInt(Input[a][5]), Float.parseFloat(Input[a][6]), Float.parseFloat(Input[a][7]), Float.parseFloat(Input[a][8]));
		}
	}
	
	public void printAtt()
	{
		System.out.println("alchemy id: " + AllAlchemy[id].getId() +
				"   name: " + AllAlchemy[id].getName() +
				"   description: " + AllAlchemy[id].getDescription() +
				"   price: " + AllAlchemy[id].getPrice() +
				"   drop chance: " + AllAlchemy[id].getDropChance() + "%" + 
				"   life heal: " + 100 * AllAlchemy[id].getLifeHeal() + "%" + 
				"   mp heal: " + 100 * AllAlchemy[id].getMPHeal() + "%");
	}
	
}
