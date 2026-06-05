package items;

import java.awt.Image;
import java.awt.Point;
import java.util.List;

import attributes.PersonalAttributes;
import graphics.Align;
import liveBeings.LiveBeing;
import main.ImageLoader;
import main.Path;
import utilities.Util;


public class Potion extends Item
{
	private final double lifeHeal ;
	private final double MPHeal ;
	
	private static final Image LIFE_POTION_SMALL_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconLifePotionSmall.png") ;
	private static final Image LIFE_POTION_MEDIUM_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconLifePotionMedium.png") ;
	private static final Image LIFE_POTION_LARGE_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconLifePotionLarge.png") ;
	private static final Image MP_POTION_SMALL_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconMpPotionSmall.png") ;
	private static final Image MP_POTION_MEDIUM_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconMpPotionMedium.png") ;
	private static final Image MP_POTION_LARGE_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconMpPotionLarge.png") ;
	private static final Potion[] ALL = new Potion[60] ;
	
	public Potion(int id, int price, double dropChance, double lifeHeal, double MPHeal)
	{
		super(id, "", "", imageFromID(id), price, dropChance) ;
		this.lifeHeal = lifeHeal ;
		this.MPHeal = MPHeal ;
		ALL[id] = this ;
	}

	public static void updateText(String language)
	{
		List<String[]> data = Util.readcsvFile(Path.DADOS + language + "/PotionText.csv") ;
		for (String[] line : data)
		{
			int id = Integer.parseInt(line[0]) ;
			ALL[id].setName(line[1]) ;
			ALL[id].setDescription(line[2]) ;
		}
	}

	public static Image imageFromID(int id)
	{
		if (id % 6 == 0) { return LIFE_POTION_SMALL_ICON ;}
		if (id % 4 == 0) { return LIFE_POTION_MEDIUM_ICON ;}
		if (id % 2 == 0) { return LIFE_POTION_LARGE_ICON ;}
		if (id % 5 == 0) { return MP_POTION_SMALL_ICON ;}
		if (id % 3 == 0) { return MP_POTION_MEDIUM_ICON ;}
		if (id % 1 == 0) { return MP_POTION_LARGE_ICON ;}
		
		return null ;
	}
	
	public double getLifeHeal() {return lifeHeal ;}
	public double getMPHeal() {return MPHeal ;}	
	public static Potion[] getAll() {return ALL ;}
		
	public void use(LiveBeing target, double powerMult)
	{
		PersonalAttributes PA = target.getPA() ;
		PA.getLife().incCurrentValue((int) (lifeHeal * PA.getLife().getMaxValue() * powerMult)) ;
		PA.getMp().incCurrentValue((int) (MPHeal * PA.getMp().getMaxValue() * powerMult)) ;
		// proTODO - bebidas
	}

	public void displayInfo(Point pos, Align align)
	{
		drawMenu(pos, align, Util.getSize(INFO_MENU_IMAGE)) ;
	}
		
	@Override
	public String toString()
	{
		return "Potion," + id + "," + name;
	}	
}
