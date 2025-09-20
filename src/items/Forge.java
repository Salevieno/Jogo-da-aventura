package items;

import java.awt.Image;
import java.awt.Point;
import java.util.List;

import graphics.Align;
import graphics2.Draw;
import main.Game;
import main.Path;
import utilities.Util;


public class Forge extends Item
{
	
	private static final Forge[] allForge ;
	
	private static final Image runeAtk = Game.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconRuneAtk.png") ;
	private static final Image runeDef = Game.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconRuneDef.png") ;
	private static final Image specialRuneAtk = Game.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconSpecialRuneAtk.png") ;
	private static final Image specialRuneDef = Game.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconSpecialRuneDef.png") ;
	
	static
	{
		List<String[]> input = Util.readcsvFile(Path.CSV + "Item_Forge.csv") ;
		allForge = new Forge[input.size()] ;
		for (int p = 0; p <= allForge.length - 1; p += 1)
		{
			allForge[p] = new Forge(Integer.parseInt(input.get(p)[0]), input.get(p)[1], input.get(p)[3], Integer.parseInt(input.get(p)[5]), Float.parseFloat(input.get(p)[6]));
		}	
	}
	
	public Forge(int id, String Name, String Description, int price, float dropChance)
	{
		super(id, Name, Description, imageFromID(id), price, dropChance) ;
	}

	public static Forge[] getAll() {return allForge ;}

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
//		return "forge id: " + AllForge[id].getId() + "   name: " + AllForge[id].getName() + "   description: " + AllForge[id].getDescription() + "   price: " + AllForge[id].getPrice() + "   drop chance: " + AllForge[id].getDropChance() + "%" ;
		return "Forge," + id + "," + name;
	}
	
}
