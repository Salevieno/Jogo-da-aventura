package Maps;

import java.awt.Image;
import java.util.ArrayList;

import GameComponents.Buildings;
import GameComponents.NPCs;

public class CityMap extends Maps
{
	public CityMap(String Name, int Continent, int[] Connections, Image image, ArrayList<Buildings> buildings, ArrayList<NPCs> npcs)
	{
		super(Name, Continent, Connections, image, buildings, npcs) ;		
	}

}
