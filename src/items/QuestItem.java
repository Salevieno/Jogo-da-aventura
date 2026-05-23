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
	private static final Image QUEST_ITEM_ICON = ImageLoader.loadImage(Path.WINDOWS_IMG + "bagIcons\\" + "IconQuestItem.png") ;
	private static final QuestItem[] ALL = new QuestItem[200] ;

	public QuestItem(int id, int price, double dropChance)
	{
		super(id, "", "", imageFromID(id), price, dropChance) ;
		ALL[id] = this ;
	}

	public static void updateText(String language)
	{
		List<String[]> data = Util.readcsvFile(Path.DADOS + language + "/QuestText.csv") ;
		for (String[] line : data)
		{
			int id = Integer.parseInt(line[0]) ;
			ALL[id].setName(line[1]) ;
			ALL[id].setDescription(line[2]) ;
		}
	}

	public static QuestItem[] getAll() {return ALL ;}

	public static Image imageFromID(int id)
	{		
		return QUEST_ITEM_ICON ;		
	}

	public void displayInfo(Point pos, Align align)
	{
		Draw.menu(pos, align, Util.getSize(INFO_MENU_IMAGE)) ;
	}
	
	@Override
	public String toString()
	{
		return "QuestItem," + id + "," + name;
	}
}
