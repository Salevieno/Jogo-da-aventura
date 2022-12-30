package maps;

import java.awt.Image;

import javax.sound.sampled.Clip;

public class SpecialMap extends GameMap
{
	public SpecialMap(String Name, int Continent, int[] Connections, Image image, Clip music)
	{
		super(Name, Continent, Connections, image, music, null, null) ;
	}
	
	
}
