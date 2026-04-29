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
	private static final Image runeAtk = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconRuneAtk.png") ;
	private static final Image runeDef = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconRuneDef.png") ;
	private static final Image specialRuneAtk = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconSpecialRuneAtk.png") ;
	private static final Image specialRuneDef = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconSpecialRuneDef.png") ;
	private static final Forge[] all = new Forge[40] ;
	
	public Forge(int id, int price, double dropChance)
	{
		super(id, "", "", imageFromID(id), price, dropChance) ;
		all[id] = this ;
	}

	public static void updateText(String language)
	{
		List<String[]> data = Util.readcsvFile(Path.DADOS + language + "/ForgeText.csv") ;
		for (String[] line : data)
		{
			int id = Integer.parseInt(line[0]) ;
			all[id].setName(line[1]) ;
			all[id].setDescription(line[2]) ;
		}
	}

	public static Forge[] getAll() {return all ;}

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
		if (id % 2 == 0 & id <= 19) { return runeAtk ;}
		if (id % 2 == 1 & id <= 19) { return runeDef ;}
		if (id % 2 == 0) { return specialRuneAtk ;}
		if (id % 2 == 1) { return specialRuneDef ;}
		
		return null ;
	}

	public void displayInfo(Point pos, Align align)
	{
		Draw.menu(pos, align, Util.getSize(infoMenu)) ;
	}
	
	public String toString()
	{
		return "Forge " + id + ": " + name;
	}
	
}
