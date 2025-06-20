package items;

import java.awt.Image;
import java.awt.Point;
import java.util.List;

import attributes.PersonalAttributes;
import graphics.Align;
import graphics2.Draw;
import liveBeings.LiveBeing;
import main.Game;
import utilities.Util;
import utilities.UtilS;

public class Alchemy extends Item
{
	private final float lifeHeal ;
	private final float MPHeal ;
	
	private static final Alchemy[] AllAlchemy ;
	
	private static final String pathImg = "\\Windows\\bagIcons\\" ;
	private static final Image HerbIcon = UtilS.loadImage(pathImg + "IconHerb.png") ;
	private static final Image WoodIcon = UtilS.loadImage(pathImg + "IconWood.png") ;
	private static final Image MetalIcon = UtilS.loadImage(pathImg + "IconMetal.png") ;
	
	static
	{
		List<String[]> input = Util.ReadcsvFile(Game.CSVPath + "Item_Alchemy.csv") ;
		AllAlchemy = new Alchemy[input.size()] ;
		for (int a = 0; a <= AllAlchemy.length - 1; a += 1)
		{
			AllAlchemy[a] = new Alchemy(Integer.parseInt(input.get(a)[0]), input.get(a)[1], input.get(a)[3], Integer.parseInt(input.get(a)[5]), Float.parseFloat(input.get(a)[6]), Float.parseFloat(input.get(a)[7]), Float.parseFloat(input.get(a)[8]));
		}
	}
	
	public Alchemy(int id, String Name, String Description, int price, float dropChance, float lifeHeal, float MPHeal)
	{
		
		super(id, Name, Description, imageFromID(id), price, dropChance) ;
		this.lifeHeal = lifeHeal ;
		this.MPHeal = MPHeal ;
		
	}
	
	public static boolean isHerb(int id) {return id % 3 == 0 ;}
	public static boolean isWood(int id) {return id % 3 == 1 ;}
	public static boolean isMetal(int id) {return id % 3 == 2 ;}
	
	public static Image imageFromID(int id) { return isHerb(id) ? HerbIcon : isWood(id) ? WoodIcon : MetalIcon ;}
	
	public float getLifeHeal() {return lifeHeal ;}
	public float getMPHeal() {return MPHeal ;}
	public static Alchemy[] getAll() {return AllAlchemy ;}
	
	public void use(LiveBeing target, double powerMult)
	{		
		PersonalAttributes PA = target.getPA() ;
		PA.getLife().incCurrentValue((int) (lifeHeal * PA.getLife().getMaxValue() * powerMult)); ;
		PA.getMp().incCurrentValue((int) (MPHeal * PA.getMp().getMaxValue() * powerMult)); ;
	}
	
	public void displayInfo(Point pos, Align align)
	{
		Draw.menu(pos, align, Util.getSize(infoMenu)) ;
	}

	@Override
	public String toString()
	{
//		return "Alchemy [lifeHeal=" + lifeHeal + ", MPHeal=" + MPHeal + ", id=" + id + ", name=" + name + ", description=" + description + ", image=" + image + ", price=" + price + ", dropChance=" + dropChance + "]";
		return "Alchemy," + id + "," + name;
	}
}
