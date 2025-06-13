package maps;

import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.Clip;

import items.Fab;
import items.GeneralItem;
import items.Item;
import main.Game;
import utilities.Util;
import utilities.UtilS;

public class SpecialMap extends GameMap
{
	
	private static final List<Image> images ;
	
	static
	{
		images = new ArrayList<>() ;
		for (int i = 0 ; i <= 2 - 1 ; i += 1)
		{
			images.add(UtilS.loadImage("\\Maps\\" + "MapSpecial" + String.valueOf(i) + ".png")) ;
		}
	}
	
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


	public static SpecialMap[] load(List<Item> allItems)
	{
		List<String[]> input = Util.ReadcsvFile(dadosPath + "MapsSpecial.csv") ;
		SpecialMap[] specialMaps = new SpecialMap[input.size()] ;

		for (int id = 0 ; id <= specialMaps.length - 1 ; id += 1)
		{
			String name = input.get(id)[0] ;
			Continents continent = Continents.values()[Integer.parseInt(input.get(id)[1])] ;
			int[] connections = new int[] {
												Integer.parseInt(input.get(id)[9]), Integer.parseInt(input.get(id)[2]),
												Integer.parseInt(input.get(id)[3]), Integer.parseInt(input.get(id)[4]),
												Integer.parseInt(input.get(id)[5]), Integer.parseInt(input.get(id)[6]),
												Integer.parseInt(input.get(id)[7]), Integer.parseInt(input.get(id)[8])
											} ;

			Image image = SpecialMap.images.get(id) ;
			Clip music = GameMap.musicForest ;
			
			// adding treasure chests
			List<TreasureChest> treasureChests = new ArrayList<>() ;
			for (int chest = 0 ; chest <= 5 - 1 ; chest += 1)
			{
				Point pos = new Point(
										(int) (Double.parseDouble(input.get(id)[10 + 13 * chest]) * Game.getScreen().mapSize().width),
										(int) (Double.parseDouble(input.get(id)[11 + 13 * chest]) * Game.getScreen().mapSize().height)
									) ;
				List<Item> itemRewards = new ArrayList<>() ;
				for (int item = 0 ; item <= 10 - 1 ; item += 1)
				{
					int itemID = Integer.parseInt(input.get(id)[12 + 13 * chest + item]) ;
					if (-1 < itemID)
					{
						itemRewards.add(allItems.get(itemID)) ;
					}
				}
				int goldReward = Integer.parseInt(input.get(id)[22 + 13 * chest]) ;
				treasureChests.add(new TreasureChest(chest, pos, itemRewards, goldReward)) ;
			}
			specialMaps[id] = new SpecialMap(name, continent, connections, image, music, treasureChests) ;
		}

		return specialMaps ;
	}

}