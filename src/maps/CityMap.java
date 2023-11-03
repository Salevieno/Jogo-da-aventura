package maps;

import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.Clip;

import components.Building;
import components.NPCs;
import items.Fab;
import items.GeneralItem;
import utilities.UtilS;

public class CityMap extends GameMap
{

	public static final List<Image> images ;
	
	static
	{
		images = new ArrayList<>() ;
		for (int i = 0 ; i <= 5 - 1 ; i += 1)
		{
			images.add(UtilS.loadImage("\\Maps\\" + "Map" + String.valueOf(i) + ".png")) ;
		}
	}
	
	public CityMap(String Name, Continents Continent, int[] Connections, Image image, Clip music, List<Building> buildings, List<NPCs> npcs)
	{
		super(Name, Continent, Connections, image, music, buildings, npcs) ;
		
		if (Name.equals("City of the knights"))
		{
			Image knightsCityWallImage = UtilS.loadImage("\\MapElements\\" + "Knight'sCityWall.png") ;
			mapElems.add(new MapElements(0, "Knight'sCityWall", new Point(0, 96 - knightsCityWallImage.getHeight(null)), knightsCityWallImage)) ;
			mapElems.add(new MapElements(0, "Knight'sCityWall", new Point(0, 480 - knightsCityWallImage.getHeight(null)), knightsCityWallImage)) ;
		}
		
		diggingItems.put(Fab.getAll()[0], allDiggingItems.get(Fab.getAll()[0])) ;
		diggingItems.put(Fab.getAll()[25], allDiggingItems.get(Fab.getAll()[25])) ;
		diggingItems.put(GeneralItem.getAll()[4], allDiggingItems.get(GeneralItem.getAll()[4])) ;
		diggingItems.put(GeneralItem.getAll()[25], allDiggingItems.get(GeneralItem.getAll()[25])) ;
		diggingItems.put(GeneralItem.getAll()[35], allDiggingItems.get(GeneralItem.getAll()[35])) ;
		diggingItems.put(GeneralItem.getAll()[155], allDiggingItems.get(GeneralItem.getAll()[155])) ;
		calcDigItemChances() ;
	}

}
