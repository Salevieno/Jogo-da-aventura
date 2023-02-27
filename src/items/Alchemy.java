package items;

import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import attributes.PersonalAttributes;
import liveBeings.LiveBeing;
import main.Game;
import maps.Collectible;
import utilities.UtilG;

public class Alchemy extends Item
{
	private int id ;
	private float lifeHeal ;
	private float MPHeal ;
	
	private static Alchemy[] AllAlchemy ;
	
	private static Image HerbIcon = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "IconHerb.png") ;
	private static Image WoodIcon = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "IconWood.png") ;
	private static Image MetalIcon = UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "IconMetal.png") ;
	
	public Alchemy(int id, String Name, String Description, int price, float dropChance, float lifeHeal, float MPHeal)
	{
		super(Name, Description, imageFromID(id), price, dropChance) ;
		this.lifeHeal = lifeHeal ;
		this.MPHeal = MPHeal ;
		
	}
	
	public static Image imageFromID(int id)
	{
		return id % 3 == 0 ? HerbIcon :
		id % 3 == 1 ? WoodIcon :
		MetalIcon ;
	}
	
	public int getId() {return id ;}
	public float getLifeHeal() {return lifeHeal ;}
	public float getMPHeal() {return MPHeal ;}
	public static Alchemy[] getAll() {return AllAlchemy ;}
	
	public static void Initialize() throws IOException
	{
		ArrayList<String[]> input = UtilG.ReadcsvFile(Game.CSVPath + "Item_Alchemy.csv") ;
		AllAlchemy = new Alchemy[input.size()] ;
		for (int a = 0; a <= AllAlchemy.length - 1; a += 1)
		{
			AllAlchemy[a] = new Alchemy(Integer.parseInt(input.get(a)[0]), input.get(a)[1], input.get(a)[3], Integer.parseInt(input.get(a)[5]), Float.parseFloat(input.get(a)[6]), Float.parseFloat(input.get(a)[7]), Float.parseFloat(input.get(a)[8]));
		}
	}
	
	public void use(LiveBeing target, double powerMult)
	{		
		PersonalAttributes PA = target.getPA() ;
		PA.getLife().incCurrentValue((int) (lifeHeal * PA.getLife().getMaxValue() * powerMult)); ;
		PA.getMp().incCurrentValue((int) (MPHeal * PA.getMp().getMaxValue() * powerMult)); ;
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
