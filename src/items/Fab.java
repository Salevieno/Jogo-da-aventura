package items;

import java.awt.Image;
import java.awt.Point;
import java.util.List;

import graphics.Align;
import graphics2.Draw;
import main.Game;
import main.Path;
import utilities.Util;


public class Fab extends Item
{
	private static final Fab[] allFabs ;
	
	private static final Image ferkIcon = Game.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconFerk.png") ;
	
	static
	{
		List<String[]> input = Util.readcsvFile(Path.CSV + "Item_Fab.csv") ;
		allFabs = new Fab[input.size()] ;
		for (int p = 0; p <= allFabs.length - 1; p += 1)
		{
			allFabs[p] = new Fab(Integer.parseInt(input.get(p)[0]), input.get(p)[1], input.get(p)[3], Integer.parseInt(input.get(p)[5]), Float.parseFloat(input.get(p)[6]));
		}
	}
	
	public Fab(int id, String Name, String Description, int price, float dropChance)
	{
		super(id, Name, Description, imageFromID(id), price, dropChance) ;
	}

	public static Fab[] getAll() {return allFabs ;}

	public static Image imageFromID(int id)
	{		
		return ferkIcon ;		
	}

	public void displayInfo(Point pos, Align align)
	{
		Draw.menu(pos, align, Util.getSize(infoMenu)) ;
	}
	
	@Override
	public String toString()
	{
		return "Fab," + id + "," + name ;
	}
	
	
}
