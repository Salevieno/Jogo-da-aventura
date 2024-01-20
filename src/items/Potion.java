package items;

import java.awt.Image;
import java.awt.Point;
import java.util.List;

import attributes.PersonalAttributes;
import graphics.Draw;
import graphics.DrawPrimitives;
import liveBeings.LiveBeing;
import main.Game;
import utilities.Align;
import utilities.UtilG;
import utilities.UtilS;

public class Potion extends Item
{
	private double lifeHeal ;
	private double MPHeal ;
	
	private static Potion[] AllPotions ;
	
	private static Image lifePotionSmall = UtilS.loadImage("\\Windows\\bagIcons\\" + "IconLifePotionSmall.png") ;
	private static Image lifePotionMedium = UtilS.loadImage("\\Windows\\bagIcons\\" + "IconLifePotionMedium.png") ;
	private static Image lifePotionLarge = UtilS.loadImage("\\Windows\\bagIcons\\" + "IconLifePotionLarge.png") ;
	private static Image mpPotionSmall = UtilS.loadImage("\\Windows\\bagIcons\\" + "IconMpPotionSmall.png") ;
	private static Image mpPotionMedium = UtilS.loadImage("\\Windows\\bagIcons\\" + "IconMpPotionMedium.png") ;
	private static Image mpPotionLarge = UtilS.loadImage("\\Windows\\bagIcons\\" + "IconMpPotionLarge.png") ;
	
	static
	{
		List<String[]> input = UtilG.ReadcsvFile(Game.CSVPath + "Item_Potions.csv") ;
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
	
	public Potion(int id, String Name, String Description, int price, double dropChance, double lifeHeal, double MPHeal)
	{
		super(id, Name, Description, imageFromID(id), price, dropChance) ;
		this.lifeHeal = lifeHeal ;
		this.MPHeal = MPHeal ;
	}

	public static Image imageFromID(int id)
	{
		if (id % 6 == 0) { return lifePotionSmall ;}
		if (id % 4 == 0) { return lifePotionMedium ;}
		if (id % 2 == 0) { return lifePotionLarge ;}
		if (id % 5 == 0) { return mpPotionSmall ;}
		if (id % 3 == 0) { return mpPotionMedium ;}
		if (id % 1 == 0) { return mpPotionLarge ;}
		
		return null ;
	}
	
	public double getLifeHeal() {return lifeHeal ;}
	public double getMPHeal() {return MPHeal ;}	
	public static Potion[] getAll() {return AllPotions ;}
		
	public void use(LiveBeing target, double powerMult)
	{
		PersonalAttributes PA = target.getPA() ;
		PA.getLife().incCurrentValue((int) (lifeHeal * PA.getLife().getMaxValue() * powerMult)) ;
		PA.getMp().incCurrentValue((int) (MPHeal * PA.getMp().getMaxValue() * powerMult)) ;
	}

	public void displayInfo(Point pos, Align align, DrawPrimitives DP)
	{
		Draw.menu(pos, align, UtilG.getSize(infoMenu)) ;
	}
		
	@Override
	public String toString()
	{
//		return "Potion [id=" + id + ", lifeHeal=" + lifeHeal + ", MPHeal=" + MPHeal + "]";
		return "Potion," + id + "," + name;
	}
	

//	public void printAtt()
//	{
//		System.out.println("potion id: " + AllPotions[id].getId() +
//				"   name: " + AllPotions[id].getName() +
//				"   description: " + AllPotions[id].getDescription() +
//				"   price: " + AllPotions[id].getPrice() +
//				"   drop chance: " + AllPotions[id].getDropChance() + "%" + 
//				"   life heal: " + 100 * AllPotions[id].getLifeHeal() + "%" + 
//				"   mp heal: " + 100 * AllPotions[id].getMPHeal() + "%");
//	}
	
}
