package maps;

import java.awt.Image;
import java.util.List;

import javax.sound.sampled.Clip;

public class SpecialMap extends GameMap
{	
	public SpecialMap(String Name, int Continent, int[] Connections, Image image, Clip music, List<TreasureChest> treasureChests)
	{
		super(Name, Continent, Connections, image, music, null, null) ;
		
		for (TreasureChest chest : treasureChests)
		{
			this.mapElem.add(chest) ;
		}
	}	
}