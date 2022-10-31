package Items;

import java.io.IOException;

import Main.Game;
import Utilities.UtilG;

public class PetItem extends Item
{
	private int id ;
	private float lifeHeal ;
	private float MPHeal ;
	private int SatiationHeal ;
	
	private static PetItem[] AllPetItems ;
	public PetItem(int id, String Name, String Description, int price, float dropChance, float lifeHeal, float MPHeal, int SatiationHeal)
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
	public static PetItem[] getAll() {return AllPetItems ;}
	
	
	public static void Initialize() throws IOException
	{
		int NumPetItems = UtilG.count(Game.CSVPath + "Item_PetItem.csv") ;
		String[][] Input = UtilG.ReadTextFile(Game.CSVPath + "Item_PetItem.csv", NumPetItems) ;
		AllPetItems = new PetItem[NumPetItems] ;
		for (int p = 0; p <= NumPetItems - 1; p += 1)
		{
			AllPetItems[p] = new PetItem(Integer.parseInt(Input[p][0]), Input[p][1], Input[p][3], Integer.parseInt(Input[p][5]), Float.parseFloat(Input[p][6]), Float.parseFloat(Input[p][7]), Float.parseFloat(Input[p][8]), Integer.parseInt(Input[p][9]));
		}		
	}

	public void printAtt()
	{
		System.out.println("pet item id: " + AllPetItems[id].getId() +
				"   name: " + AllPetItems[id].getName() +
				"   description: " + AllPetItems[id].getDescription() +
				"   price: " + AllPetItems[id].getPrice() +
				"   drop chance: " + AllPetItems[id].getDropChance() + "%" + 
				"   life heal: " + 100 * AllPetItems[id].getLifeHeal() + "%" + 
				"   mp heal: " + 100 * AllPetItems[id].getMPHeal() + "%" + 
				"   satiation heal: " + AllPetItems[id].getSatiationHeal());
	}
	
}
