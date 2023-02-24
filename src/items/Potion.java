package items;

import java.io.IOException;
import java.util.ArrayList;

import attributes.PersonalAttributes;
import liveBeings.LiveBeing;
import main.Game;
import utilities.UtilG;

public class Potion extends Item
{
	private int id ;
	private double lifeHeal ;
	private double MPHeal ;
	
	private static Potion[] AllPotions ;
	public Potion(int id, String Name, String Description, int price, double dropChance, double lifeHeal, double MPHeal)
	{
		super(Name, Description, UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "items.png"), price, dropChance) ;
		this.id = id ;
		this.lifeHeal = lifeHeal ;
		this.MPHeal = MPHeal ;
	}

	public int getId() {return id ;}
	public double getLifeHeal() {return lifeHeal ;}
	public double getMPHeal() {return MPHeal ;}	
	public static Potion[] getAll() {return AllPotions ;}
		
	public static void Initialize() throws IOException
	{
		ArrayList<String[]> input = UtilG.ReadcsvFile(Game.CSVPath + "Item_Potions.csv") ;
		AllPotions = new Potion[input.size()] ;
		for (int p = 0; p <= AllPotions.length - 1; p += 1)
		{
			int id = Integer.parseInt(input.get(p)[0]) ;
			String description = input.get(p)[1] ;
			String name = input.get(p)[3] ;
			int price = Integer.parseInt(input.get(p)[5]) ;
			double dropChance = Double.parseDouble(input.get(p)[6]) ;
			double lifeHeal = Double.parseDouble(input.get(p)[7]) ;
			double MPHeal = Double.parseDouble(input.get(p)[8]) ;
			
			AllPotions[p] = new Potion(id, description, name, price, dropChance, lifeHeal, MPHeal) ;
		}		
	}

	public void use(LiveBeing target, double powerMult)
	{
		PersonalAttributes PA = target.getPA() ;
		PA.getLife().incCurrentValue((int) (lifeHeal * PA.getLife().getMaxValue() * powerMult)) ;
		PA.getMp().incCurrentValue((int) (MPHeal * PA.getMp().getMaxValue() * powerMult)) ;
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
