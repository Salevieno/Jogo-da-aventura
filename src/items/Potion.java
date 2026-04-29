package items;

import java.awt.Image;
import java.awt.Point;
import java.util.List;

import attributes.PersonalAttributes;
import graphics.Align;
import graphics2.Draw;
import liveBeings.LiveBeing;
import main.ImageLoader;
import main.Path;
import utilities.Util;


public class Potion extends Item
{
	private final double lifeHeal ;
	private final double MPHeal ;
	
	private static final Potion[] all = new Potion[60] ;
	
	private static final Image lifePotionSmall = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconLifePotionSmall.png") ;
	private static final Image lifePotionMedium = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconLifePotionMedium.png") ;
	private static final Image lifePotionLarge = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconLifePotionLarge.png") ;
	private static final Image mpPotionSmall = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconMpPotionSmall.png") ;
	private static final Image mpPotionMedium = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconMpPotionMedium.png") ;
	private static final Image mpPotionLarge = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconMpPotionLarge.png") ;

	public Potion(int id, int price, double dropChance, double lifeHeal, double MPHeal)
	{
		super(id, "", "", imageFromID(id), price, dropChance) ;
		this.lifeHeal = lifeHeal ;
		this.MPHeal = MPHeal ;
		all[id] = this ;
	}

	public static void updateText(String language)
	{
		List<String[]> arrowText = Util.readcsvFile(Path.DADOS + language + "/PotionText.csv") ;
		for (String[] line : arrowText)
		{
			int id = Integer.parseInt(line[0]) ;
			all[id].setName(line[1]) ;
			all[id].setDescription(line[2]) ;
		}
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
	public static Potion[] getAll() {return all ;}
		
	public void use(LiveBeing target, double powerMult)
	{
		PersonalAttributes PA = target.getPA() ;
		PA.getLife().incCurrentValue((int) (lifeHeal * PA.getLife().getMaxValue() * powerMult)) ;
		PA.getMp().incCurrentValue((int) (MPHeal * PA.getMp().getMaxValue() * powerMult)) ;
		// proTODO - bebidas
	}

	public void displayInfo(Point pos, Align align)
	{
		Draw.menu(pos, align, Util.getSize(infoMenu)) ;
	}
		
	@Override
	public String toString()
	{
		return "Potion," + id + "," + name;
	}	
}
