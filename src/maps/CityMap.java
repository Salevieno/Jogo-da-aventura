package maps;

import java.awt.Image;
import java.awt.Point;
import java.util.List;

import javax.sound.sampled.Clip;

import components.Building;
import components.NPCs;
import main.Game;
import utilities.UtilG;

public class CityMap extends GameMap
{
	public CityMap(String Name, Continents Continent, int[] Connections, Image image, Clip music,
			List<Building> buildings, List<NPCs> npcs)
	{
		super(Name, Continent, Connections, image, music, buildings, npcs) ;
		
		if (Name.equals("City of the knights"))
		{
			Image knightsCityWallImage = UtilG.loadImage(Game.ImagesPath + "\\MapElements\\" + "Knight'sCityWall.png") ;
			mapElems.add(new MapElements(0, "Knight'sCityWall", new Point(0, 96 - knightsCityWallImage.getHeight(null)), knightsCityWallImage)) ;
			mapElems.add(new MapElements(0, "Knight'sCityWall", new Point(0, 480 - knightsCityWallImage.getHeight(null)), knightsCityWallImage)) ;
		}
	}

}
