package items;

import java.awt.Image;
import java.util.List;

import attributes.PersonalAttributes;
import liveBeings.LiveBeing;
import main.Game;
import utilities.UtilG;

public class Food extends Item
{
	private float lifeHeal ;
	private float MPHeal ;
	private int SatiationHeal ;
	
	private static Food[] AllFood ;
	
	private static final Image iconFoodBerry ;
	
	static
	{
		List<String[]> input = UtilG.ReadcsvFile(Game.CSVPath + "Item_Food.csv") ;
		AllFood = new Food[input.size()] ;
		iconFoodBerry = UtilG.loadImage(Game.ImagesPath + "\\Windows\\bagIcons\\" + "IconFoodBerry.png") ;
		for (int p = 0; p <= AllFood.length - 1; p += 1)
		{
			AllFood[p] = new Food(Integer.parseInt(input.get(p)[0]), input.get(p)[1], input.get(p)[3],
					Integer.parseInt(input.get(p)[5]), Float.parseFloat(input.get(p)[6]),
					Float.parseFloat(input.get(p)[7]), Float.parseFloat(input.get(p)[8]),
					Integer.parseInt(input.get(p)[9]));
		}
	}
	
	public Food(int id, String Name, String Description, int price,
			float dropChance, float lifeHeal, float MPHeal, int SatiationHeal)
	{
		super(id, Name, Description, imageFromID(id), price, dropChance) ;
		this.lifeHeal = lifeHeal ;
		this.MPHeal = MPHeal ;
		this.SatiationHeal = SatiationHeal ;
	}

	public float getLifeHeal() {return lifeHeal ;}
	public float getMPHeal() {return MPHeal ;}	
	public int getSatiationHeal() {return SatiationHeal ;}	
	public static Food[] getAll() {return AllFood ;}

	public static Image imageFromID(int id) { return iconFoodBerry ;}
	
	public void use(LiveBeing user)
	{
		PersonalAttributes PA = user.getPA() ;
		PA.getLife().incCurrentValue((int) (lifeHeal * PA.getLife().getMaxValue())) ;
		PA.getMp().incCurrentValue((int) (MPHeal * PA.getMp().getMaxValue())) ;
		PA.getSatiation().incCurrentValue((int) (SatiationHeal / 310.0 * PA.getSatiation().getMaxValue())) ;
	}

	@Override
	public String toString()
	{
//		return "Food [lifeHeal=" + lifeHeal + ", MPHeal=" + MPHeal + ", SatiationHeal=" + SatiationHeal + ", id=" + id + ", name=" + name + ", description=" + description + ", image=" + image + ", price=" + price + ", dropChance=" + dropChance + "]";
		return "Food," + id + "," + name;
	}
	
	

//	public void printAtt()
//	{
//		System.out.println("food id: " + AllFood[id].getId() +
//				"   name: " + AllFood[id].getName() +
//				"   description: " + AllFood[id].getDescription() +
//				"   price: " + AllFood[id].getPrice() +
//				"   drop chance: " + AllFood[id].getDropChance() + "%" + 
//				"   life heal: " + 100 * AllFood[id].getLifeHeal() + "%" + 
//				"   mp heal: " + 100 * AllFood[id].getMPHeal() + "%" + 
//				"   satiation heal: " + AllFood[id].getSatiationHeal());
//	}
	
}
