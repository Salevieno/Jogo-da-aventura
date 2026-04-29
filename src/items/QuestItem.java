package items;

import java.awt.Image;
import java.awt.Point;
import java.util.List;

import graphics.Align;
import graphics2.Draw;
import main.ImageLoader;
import main.Path;
import utilities.Util;


public class QuestItem extends Item
{	
	private static final Image questItemIcon = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconQuestItem.png") ;
	private static final QuestItem[] all = new QuestItem[200] ;

	public QuestItem(int id, int price, double dropChance)
	{
		super(id, "", "", imageFromID(id), price, dropChance) ;
		all[id] = this ;
	}

	public static void updateText(String language)
	{
		List<String[]> data = Util.readcsvFile(Path.DADOS + language + "/QuestText.csv") ;
		for (String[] line : data)
		{
			int id = Integer.parseInt(line[0]) ;
			all[id].setName(line[1]) ;
			all[id].setDescription(line[2]) ;
		}
	}

	public static QuestItem[] getAll() {return all ;}

	public static Image imageFromID(int id)
	{		
		return questItemIcon ;		
	}

	public void displayInfo(Point pos, Align align)
	{
		Draw.menu(pos, align, Util.getSize(infoMenu)) ;
	}
	
	@Override
	public String toString()
	{
		return "QuestItem," + id + "," + name;
	}
}
