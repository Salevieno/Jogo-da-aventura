package maps;

import java.awt.Image;
import java.util.ArrayList;

import javax.sound.sampled.Clip;

import components.Buildings;
import components.NPCs;

public class CityMap extends GameMap
{
	public CityMap(String Name, int Continent, int[] Connections, Image image, Clip music, ArrayList<Buildings> buildings, ArrayList<NPCs> npcs)
	{
		super(Name, Continent, Connections, image, music, buildings, npcs) ;		
	}

}