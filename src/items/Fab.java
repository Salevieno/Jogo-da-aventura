package items;

import java.awt.Image;
import java.awt.Point;
import java.util.List;

import graphics.Align;
import graphics2.Draw;
import main.ImageLoader;
import main.Path;
import utilities.Util;


public class Fab extends Item
{
	private static final Image ferkIcon = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconFerk.png") ;
	private static final Fab[] all = new Fab[100] ;

	public Fab(int id, int price, double dropChance)
	{
		super(id, "", "", imageFromID(id), price, dropChance) ;
		all[id] = this ;
	}

	public static void updateText(String language)
	{
		List<String[]> data = Util.readcsvFile(Path.DADOS + language + "/FabText.csv") ;
		for (String[] line : data)
		{
			int id = Integer.parseInt(line[0]) ;
			all[id].setName(line[1]) ;
			all[id].setDescription(line[2]) ;
		}
	}

	public static Fab[] getAll() {return all ;}

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
