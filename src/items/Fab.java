package items;

import java.awt.Image;
import java.awt.Point;
import java.util.List;

import graphics.Draw;
import graphics.DrawPrimitives;
import main.Game;
import utilities.Align;
import utilities.UtilG;
import utilities.UtilS;

public class Fab extends Item
{
	private static Fab[] AllFabs ;
	
	private static final Image ferkIcon = UtilS.loadImage("\\Windows\\bagIcons\\" + "IconFerk.png") ;
	
	static
	{
		List<String[]> input = UtilG.ReadcsvFile(Game.CSVPath + "Item_Fab.csv") ;
		AllFabs = new Fab[input.size()] ;
		for (int p = 0; p <= AllFabs.length - 1; p += 1)
		{
			AllFabs[p] = new Fab(Integer.parseInt(input.get(p)[0]), input.get(p)[1], input.get(p)[3], Integer.parseInt(input.get(p)[5]), Float.parseFloat(input.get(p)[6]));
		}
	}
	
	public Fab(int id, String Name, String Description, int price, float dropChance)
	{
		super(id, Name, Description, imageFromID(id), price, dropChance) ;
	}

	public static Fab[] getAll() {return AllFabs ;}

	public static Image imageFromID(int id)
	{		
		return ferkIcon ;		
	}

	public void displayInfo(Point pos, Align align, DrawPrimitives DP)
	{
		Draw.menu(pos, align, UtilG.getSize(infoMenu)) ;
	}
	
	@Override
	public String toString()
	{
		return "Fab," + id + "," + name ;
	}
	
	
}
