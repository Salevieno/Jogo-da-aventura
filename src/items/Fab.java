package items;

import java.awt.Image;
import java.awt.Point;
import java.util.List;

import graphics.Align;
import main.ImageLoader;
import main.Path;
import utilities.Util;


public class Fab extends Item
{
	private static final Image FERK_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconFerk.png") ;
	private static final Fab[] ALL = new Fab[100] ;

	public Fab(int id, int price, double dropChance)
	{
		super(id, "", "", imageFromID(id), price, dropChance) ;
		ALL[id] = this ;
	}

	public static void updateText(String language)
	{
		List<String[]> data = Util.readcsvFile(Path.DADOS + language + "/FabText.csv") ;
		for (String[] line : data)
		{
			int id = Integer.parseInt(line[0]) ;
			ALL[id].setName(line[1]) ;
			ALL[id].setDescription(line[2]) ;
		}
	}

	public static Fab[] getAll() {return ALL ;}

	public static Image imageFromID(int id)
	{		
		return FERK_ICON ;		
	}

	public void displayInfo(Point pos, Align align)
	{
		drawMenu(pos, align, Util.getSize(INFO_MENU_IMAGE)) ;
	}
	
	@Override
	public String toString()
	{
		return "Fab," + id + "," + name ;
	}
	
	
}
