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


public class Alchemy extends Item
{
	private final double lifeHeal ;
	private final double MPHeal ;
	
	private static final Alchemy[] all = new Alchemy[60] ;
	
	private static final String pathImg = "\\Windows\\bagIcons\\" ;
	private static final Image HerbIcon = ImageLoader.loadImage(pathImg + "IconHerb.png") ;
	private static final Image WoodIcon = ImageLoader.loadImage(pathImg + "IconWood.png") ;
	private static final Image MetalIcon = ImageLoader.loadImage(pathImg + "IconMetal.png") ;
	
	public Alchemy(int id, int price, double lifeHeal, double mpHeal)
	{		
		super(id, "", "", imageFromID(id), price, 0.0) ;
		this.lifeHeal = lifeHeal ;
		this.MPHeal = mpHeal ;
		all[id] = this ;
	}

	public static void updateText(String language)
	{
		List<String[]> data = Util.readcsvFile(Path.DADOS + language + "/AlchemyText.csv") ;
		for (String[] line : data)
		{
			int id = Integer.parseInt(line[0]) ;
			all[id].setName(line[1]) ;
			all[id].setDescription(line[2]) ;
		}
	}
	
	public static boolean isHerb(int id) {return id % 3 == 0 ;}
	public static boolean isWood(int id) {return id % 3 == 1 ;}
	public static boolean isMetal(int id) {return id % 3 == 2 ;}
	
	public static Image imageFromID(int id) { return isHerb(id) ? HerbIcon : isWood(id) ? WoodIcon : MetalIcon ;}

	public static Alchemy[] getAll() {return all ;}
	
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
		return "Alchemy," + id + "," + name;
	}
}
