package items;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import liveBeings.Pet;
import main.Game;
import utilities.UtilG;

public class PetItem extends Item
{
	private int id ;
	private float lifeHeal ;
	private float MPHeal ;
	private int SatiationHeal ;
	
	private static PetItem[] AllPetItems ;
	public PetItem(int id, String Name, String Description, int price, float dropChance, float lifeHeal, float MPHeal, int SatiationHeal)
	{
		super(Name, Description, UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "items.png"), price, dropChance) ;
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
		ArrayList<String[]> input = UtilG.ReadcsvFile(Game.CSVPath + "Item_PetItem.csv") ;
		AllPetItems = new PetItem[input.size()] ;
		for (int p = 0; p <= AllPetItems.length - 1; p += 1)
		{
			AllPetItems[p] = new PetItem(Integer.parseInt(input.get(p)[0]), input.get(p)[1], input.get(p)[3], Integer.parseInt(input.get(p)[5]), Float.parseFloat(input.get(p)[6]), Float.parseFloat(input.get(p)[7]), Float.parseFloat(input.get(p)[8]), Integer.parseInt(input.get(p)[9]));
		}		
	}
	
	public void use(Pet pet)
	{
		
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
