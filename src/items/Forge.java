package items;

import java.awt.Image;
import java.util.List;

import main.Game;
import utilities.UtilG;

public class Forge extends Item
{
	private int id ;
	
	private static Forge[] AllForge ;
	
	private static final Image runeAtk = UtilG.loadImage(Game.ImagesPath + "\\Windows\\bagIcons\\" + "IconRuneAtk.png") ;
	private static final Image runeDef = UtilG.loadImage(Game.ImagesPath + "\\Windows\\bagIcons\\" + "IconRuneDef.png") ;
	private static final Image specialRuneAtk = UtilG.loadImage(Game.ImagesPath + "\\Windows\\bagIcons\\" + "IconSpecialRuneAtk.png") ;
	private static final Image specialRuneDef = UtilG.loadImage(Game.ImagesPath + "\\Windows\\bagIcons\\" + "IconSpecialRuneDef.png") ;
	
	static
	{
		List<String[]> input = UtilG.ReadcsvFile(Game.CSVPath + "Item_Forge.csv") ;
		AllForge = new Forge[input.size()] ;
		for (int p = 0; p <= AllForge.length - 1; p += 1)
		{
			AllForge[p] = new Forge(Integer.parseInt(input.get(p)[0]), input.get(p)[1], input.get(p)[3], Integer.parseInt(input.get(p)[5]), Float.parseFloat(input.get(p)[6]));
		}	
	}
	
	public Forge(int id, String Name, String Description, int price, float dropChance)
	{
		super(Name, Description, imageFromID(id), price, dropChance) ;
		this.id = id ;
	}

	public int getId() {return id ;}
	public static Forge[] getAll() {return AllForge ;}
	
	public static Image imageFromID(int id)
	{
		if (id % 2 == 0 & id <= 19) { return runeAtk ;}
		if (id % 2 == 1 & id <= 19) { return runeDef ;}
		if (id % 2 == 0) { return specialRuneAtk ;}
		if (id % 2 == 1) { return specialRuneDef ;}
		
		return null ;
	}
	
	public String toString()
	{
		return "forge id: " + AllForge[id].getId() +
				"   name: " + AllForge[id].getName() +
				"   description: " + AllForge[id].getDescription() +
				"   price: " + AllForge[id].getPrice() +
				"   drop chance: " + AllForge[id].getDropChance() + "%" ;
	}
	
}
