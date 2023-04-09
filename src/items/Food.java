package items;

import java.io.IOException;
import java.util.List;

import attributes.PersonalAttributes;
import liveBeings.LiveBeing;
import main.Game;
import utilities.UtilG;

public class Food extends Item
{
	private int id ;
	private float lifeHeal ;
	private float MPHeal ;
	private int SatiationHeal ;
	
	private static Food[] AllFood ;
	public Food(int id, String Name, String Description, int price, float dropChance, float lifeHeal, float MPHeal, int SatiationHeal)
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
	public static Food[] getAll() {return AllFood ;}
	
	
	public static void Initialize() throws IOException
	{
		List<String[]> input = UtilG.ReadcsvFile(Game.CSVPath + "Item_Food.csv") ;
		AllFood = new Food[input.size()] ;
		for (int p = 0; p <= AllFood.length - 1; p += 1)
		{
			AllFood[p] = new Food(Integer.parseInt(input.get(p)[0]), input.get(p)[1], input.get(p)[3], Integer.parseInt(input.get(p)[5]), Float.parseFloat(input.get(p)[6]), Float.parseFloat(input.get(p)[7]), Float.parseFloat(input.get(p)[8]), Integer.parseInt(input.get(p)[9]));
		}		
	}
	
	public void use(LiveBeing user)
	{
		PersonalAttributes PA = user.getPA() ;
		PA.getLife().incCurrentValue((int) (lifeHeal * PA.getLife().getMaxValue())) ;
		PA.getMp().incCurrentValue((int) (MPHeal * PA.getMp().getMaxValue())) ;
		PA.getSatiation().incCurrentValue((int) (SatiationHeal * PA.getMp().getMaxValue())) ;
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
