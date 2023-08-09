package items;

import java.awt.Image;
import java.util.List;

import liveBeings.Pet;
import main.Game;
import utilities.UtilG;

public class PetItem extends Item
{
	private float lifeHeal ;
	private float MPHeal ;
	private int SatiationHeal ;
	
	private static PetItem[] AllPetItems ;

	private static final Image petLifePotion = UtilG.loadImage(Game.ImagesPath + "\\Windows\\bagIcons\\" + "IconPetLifePotion.png") ;
	private static final Image petMPPotion = UtilG.loadImage(Game.ImagesPath + "\\Windows\\bagIcons\\" + "IconPetMPPotion.png") ;
	private static final Image petFood = UtilG.loadImage(Game.ImagesPath + "\\Windows\\bagIcons\\" + "IconPetFood.png") ;
	private static final Image petSet = UtilG.loadImage(Game.ImagesPath + "\\Windows\\bagIcons\\" + "IconPetSet.png") ;
	
	static
	{
		List<String[]> input = UtilG.ReadcsvFile(Game.CSVPath + "Item_PetItem.csv") ;
		AllPetItems = new PetItem[input.size()] ;
		for (int p = 0; p <= AllPetItems.length - 1; p += 1)
		{
			AllPetItems[p] = new PetItem(Integer.parseInt(input.get(p)[0]), input.get(p)[1], input.get(p)[3], Integer.parseInt(input.get(p)[5]), Float.parseFloat(input.get(p)[6]), Float.parseFloat(input.get(p)[7]), Float.parseFloat(input.get(p)[8]), Integer.parseInt(input.get(p)[9]));
		}	
	}
	public PetItem(int id, String Name, String Description, int price, float dropChance, float lifeHeal, float MPHeal, int SatiationHeal)
	{
		super(id, Name, Description, imageFromID(id), price, dropChance) ;
		this.lifeHeal = lifeHeal ;
		this.MPHeal = MPHeal ;
		this.SatiationHeal = SatiationHeal ;
	}

	public float getLifeHeal() {return lifeHeal ;}
	public float getMPHeal() {return MPHeal ;}	
	public int getSatiationHeal() {return SatiationHeal ;}	
	public static PetItem[] getAll() {return AllPetItems ;}

	public static Image imageFromID(int id)
	{
		if (id % 4 == 0) { return petLifePotion ;}
		if (id % 4 == 1) { return petMPPotion ;}
		if (id % 4 == 2) { return petFood ;}
		if (id % 4 == 3) { return petSet ;}
		
		return null ;
	}	
	
	public void use(Pet pet)
	{
		
	}

	@Override
	public String toString()
	{
//		return "PetItem [lifeHeal=" + lifeHeal + ", MPHeal=" + MPHeal + ", SatiationHeal=" + SatiationHeal + ", id=" + id + ", name=" + name + ", description=" + description + ", image=" + image + ", price=" + price + ", dropChance=" + dropChance + "]";
		return "PetItem," + id + "," + name;
	}

	
	
//	public void printAtt()
//	{
//		System.out.println("pet item id: " + AllPetItems[id].getId() +
//				"   name: " + AllPetItems[id].getName() +
//				"   description: " + AllPetItems[id].getDescription() +
//				"   price: " + AllPetItems[id].getPrice() +
//				"   drop chance: " + AllPetItems[id].getDropChance() + "%" + 
//				"   life heal: " + 100 * AllPetItems[id].getLifeHeal() + "%" + 
//				"   mp heal: " + 100 * AllPetItems[id].getMPHeal() + "%" + 
//				"   satiation heal: " + AllPetItems[id].getSatiationHeal());
//	}
	
}
