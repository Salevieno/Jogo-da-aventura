package items;

import java.awt.Image;
import java.awt.Point;
import java.util.List;

import graphics.Align;
import graphics2.Draw;
import main.ImageLoader;
import main.Path;
import utilities.Util;


public class Forge extends Item
{
	private static final Image RUNE_ATK_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconRuneAtk.png") ;
	private static final Image RUNE_DEF_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconRuneDef.png") ;
	private static final Image RUNE_ATK_SPECIAL_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconSpecialRuneAtk.png") ;
	private static final Image RUNE_DEF_SPECIAL_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconSpecialRuneDef.png") ;
	private static final Forge[] ALL = new Forge[40] ;
	
	public Forge(int id, int price, double dropChance)
	{
		super(id, "", "", imageFromID(id), price, dropChance) ;
		ALL[id] = this ;
	}

	public static void updateText(String language)
	{
		List<String[]> data = Util.readcsvFile(Path.DADOS + language + "/ForgeText.csv") ;
		for (String[] line : data)
		{
			int id = Integer.parseInt(line[0]) ;
			ALL[id].setName(line[1]) ;
			ALL[id].setDescription(line[2]) ;
		}
	}

	public static Forge[] getAll() {return ALL ;}

	public static int typeFromID(int id)
	{
		if (id % 2 == 0 & id <= 19) { return 0 ;}
		if (id % 2 == 1 & id <= 19) { return 1 ;}
		if (id % 2 == 0) { return 2 ;}
		if (id % 2 == 1) { return 3 ;}
		
		return -1 ;
	}
	
	public static Image imageFromID(int id)
	{
		if (id % 2 == 0 & id <= 19) { return RUNE_ATK_ICON ;}
		if (id % 2 == 1 & id <= 19) { return RUNE_DEF_ICON ;}
		if (id % 2 == 0) { return RUNE_ATK_SPECIAL_ICON ;}
		if (id % 2 == 1) { return RUNE_DEF_SPECIAL_ICON ;}
		
		return null ;
	}

	public void displayInfo(Point pos, Align align)
	{
		Draw.menu(pos, align, Util.getSize(INFO_MENU_IMAGE)) ;
	}
	
	public String toString()
	{
		return "Forge " + id + ": " + name;
	}
	
}
