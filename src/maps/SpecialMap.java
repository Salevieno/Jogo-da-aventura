package maps;

import java.awt.Image;
import java.util.List;

import javax.sound.sampled.Clip;

import items.Fab;
import items.GeneralItem;

public class SpecialMap extends GameMap
{	
	public SpecialMap(String Name, Continents Continent, int[] Connections, Image image, Clip music, List<TreasureChest> treasureChests)
	{
		super(Name, Continent, Connections, image, music, null, null) ;
		
		treasureChests.forEach(chest -> mapElems.add(chest)) ;

		if (Continent.equals(Continents.cave))
		{
			diggingItems.put(Fab.getAll()[0], allDiggingItems.get(Fab.getAll()[0])) ;
			diggingItems.put(Fab.getAll()[59], allDiggingItems.get(Fab.getAll()[59])) ;
			diggingItems.put(Fab.getAll()[60], allDiggingItems.get(Fab.getAll()[60])) ;
			diggingItems.put(Fab.getAll()[61], allDiggingItems.get(Fab.getAll()[61])) ;
			diggingItems.put(Fab.getAll()[62], allDiggingItems.get(Fab.getAll()[62])) ;
			diggingItems.put(GeneralItem.getAll()[166], allDiggingItems.get(GeneralItem.getAll()[166])) ;
			diggingItems.put(GeneralItem.getAll()[171], allDiggingItems.get(GeneralItem.getAll()[171])) ;
			diggingItems.put(GeneralItem.getAll()[172], allDiggingItems.get(GeneralItem.getAll()[172])) ;
			diggingItems.put(GeneralItem.getAll()[175], allDiggingItems.get(GeneralItem.getAll()[175])) ;
			diggingItems.put(GeneralItem.getAll()[221], allDiggingItems.get(GeneralItem.getAll()[221])) ;
		}
		if (Continent.equals(Continents.special))
		{
			diggingItems.put(Fab.getAll()[0], allDiggingItems.get(Fab.getAll()[0])) ;
			diggingItems.put(Fab.getAll()[63], allDiggingItems.get(Fab.getAll()[63])) ;
			diggingItems.put(Fab.getAll()[65], allDiggingItems.get(Fab.getAll()[65])) ;
			diggingItems.put(GeneralItem.getAll()[230], allDiggingItems.get(GeneralItem.getAll()[230])) ;
			diggingItems.put(GeneralItem.getAll()[233], allDiggingItems.get(GeneralItem.getAll()[233])) ;
			diggingItems.put(GeneralItem.getAll()[242], allDiggingItems.get(GeneralItem.getAll()[242])) ;
			diggingItems.put(GeneralItem.getAll()[249], allDiggingItems.get(GeneralItem.getAll()[249])) ;
			diggingItems.put(GeneralItem.getAll()[250], allDiggingItems.get(GeneralItem.getAll()[250])) ;
		}
		calcDigItemChances() ;
	}	
}