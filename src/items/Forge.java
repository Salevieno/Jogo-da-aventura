package items;

import java.awt.Image;
import java.awt.Point;
import java.util.List;

import graphics.Align;
import graphics2.Draw;
import main.Game;
import utilities.Util;
import utilities.UtilS;

public class Forge extends Item
{
	
	private static final Forge[] allForge ;
	
	private static final Image runeAtk = UtilS.loadImage("\\Windows\\bagIcons\\" + "IconRuneAtk.png") ;
	private static final Image runeDef = UtilS.loadImage("\\Windows\\bagIcons\\" + "IconRuneDef.png") ;
	private static final Image specialRuneAtk = UtilS.loadImage("\\Windows\\bagIcons\\" + "IconSpecialRuneAtk.png") ;
	private static final Image specialRuneDef = UtilS.loadImage("\\Windows\\bagIcons\\" + "IconSpecialRuneDef.png") ;
	
	static
	{
		List<String[]> input = Util.ReadcsvFile(Game.CSVPath + "Item_Forge.csv") ;
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
