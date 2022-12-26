package maps;

import java.awt.Image;
import java.util.ArrayList;

import components.Buildings;
import components.NPCs;

public class CityMap extends Maps
{
	public CityMap(String Name, int Continent, int[] Connections, Image image, ArrayList<Buildings> buildings, ArrayList<NPCs> npcs)
	{
		super(Name, Continent, Connections, image, buildings, npcs) ;		
	}

}
