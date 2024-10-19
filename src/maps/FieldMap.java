package maps;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sound.sampled.Clip;

import components.NPCs;
import graphics.DrawPrimitives;
import items.Item;
import libUtil.Util;
import liveBeings.Creature;
import liveBeings.CreatureType;
import main.Game;
import screen.Screen;
import screen.Sky;
import utilities.TimeCounter;
import utilities.UtilS;

public class FieldMap extends GameMap
{
	private List<Collectible> collectibles ;
	private List<Creature> creatures ;
	private int level ;
	private Map<CollectibleTypes, TimeCounter> collectibleCounters ;
	
	private static final int numberTrees = 5 ;
	private static final int numberGrass = 30 ;
	private static final int numberRocks = 10 ;

	public static final List<Image> images ;
	
	static
	{
		images = new ArrayList<>() ;
		for (int i = 5 ; i <= 67 - 1 ; i += 1)
		{
			if (i == 39 | i == 60) { continue ;}
			
			images.add(UtilS.loadImage("\\Maps\\" + "Map" + String.valueOf(i) + ".png")) ;
		}
	}
	
	public FieldMap(String name, Continents continent, int[] connections, Image image, Clip music, int collectibleLevel, int[] creatureTypeIDs, List<NPCs> npcs)
	{
		super(name, continent, connections, image, music, null, npcs) ;
		this.level = collectibleLevel ;
		
		
		// add map elements
		Screen screen = Game.getScreen() ;
		Point minCoord = new Point(20, Sky.height + 20) ;
		Dimension range = new Dimension(screen.getSize().width - 100, screen.getSize().height - Sky.height - 100) ;
		Dimension step = new Dimension(1, 1) ;

		for (int i = 0 ; i <= numberRocks - 1 ; i += 1)
		{
			Point randomPos = Util.RandomPos(minCoord, range, step) ;
			mapElems.add(new MapElement(i, "rock", randomPos)) ;				
		}
		for (int i = 0 ; i <= numberTrees - 1 ; i += 1)
		{
			Point randomPos = Util.RandomPos(minCoord, range, step) ;
			mapElems.add(new MapElement(i, "ForestTree", randomPos)) ;				
		}
		for (int i = 0 ; i <= numberGrass - 1 ; i += 1)
		{
			Point randomPos = Util.RandomPos(minCoord, range, step) ;
			mapElems.add(new MapElement(i, "grass", randomPos)) ;				
		}
		
		
		// add collectbiles
		collectibles = new ArrayList<Collectible>() ;
		collectibleCounters = new HashMap<>() ;
		for (CollectibleTypes type : CollectibleTypes.values())
		{
			addCollectible(type) ;
			TimeCounter collectibleCounter = new TimeCounter(type.getSpawnTime()) ;
			collectibleCounter.start();
			collectibleCounters.put(type, collectibleCounter) ;
		}
		
		
		// add creatures
		creatures = new ArrayList<Creature>() ;
		for (int creatureTypeID : creatureTypeIDs)
		{
			if (creatureTypeID <= -1) { continue ;}
			
			CreatureType creatureType = CreatureType.all.get(creatureTypeID);
			Creature creature = new Creature(creatureType) ;
			creatures.add(creature) ;
		}
		
		for (Item item : allDiggingItems.keySet())
		{
			if (!containsItem(item)) { continue ;}
			
			diggingItems.put(item, allDiggingItems.get(item)) ;
		}
		calcDigItemChances() ;
		
		
//		System.out.println(name);
//		System.out.println(diggingItems);
		
		// add npcs
		this.npcs = npcs ;
	}
	
	public int getLevel() { return level ;}
	public List<Creature> getCreatures() {return creatures ;}
	public List<Collectible> getCollectibles() {return collectibles ;}
	public Set<Item> getItems()
	{
		Set<Item> allItems = new HashSet<>() ;
		
		creatures.forEach(creature -> creature.getBag().forEach(item -> allItems.add(item))) ;
		
		return allItems ;
	}
	public void setCreatures(List<Creature> newValue) {creatures = newValue ;}
	
	public static List<NPCs> createQuestNPCs(int mapID)
	{
		NPCs questExp = new NPCs(Game.getNPCTypes()[12], Game.getScreen().pos(0.27, 0.73)) ;
		NPCs questItem = new NPCs(Game.getNPCTypes()[13], Game.getScreen().pos(0.87, 0.63)) ;
		switch (mapID)
		{
			case 0: return List.of(questExp, questItem) ;
			default: return List.of(questExp, questItem) ;
		}
	}
	
	public boolean hasCreatures() { return creatures != null ;}
	
	public void activateCollectiblesCounter()
	{
		collectibleCounters.entrySet().forEach(entry -> 
		{
			CollectibleTypes type = entry.getKey() ;
			TimeCounter spawnCounter = entry.getValue() ;
			if (spawnCounter.finished())
			{
				addCollectible(type) ;
				spawnCounter.start() ;
			}
		}) ;

	}
	
	public void addCollectible(CollectibleTypes type)
	{
		Point pos = randomPosInMap() ;
		while (groundTypeAtPoint(pos) != null)
		{
			pos = randomPosInMap() ;
		}
		
		switch (type)
		{
			case berry: collectibles.add(new Collectible(1220, level, pos)) ; return ;
			case herb: collectibles.add(new Collectible(1060 + 3 * (level - 1), level, pos)) ; return ;
			case wood: collectibles.add(new Collectible(1061 + 3 * (level - 1), level, pos)) ; return ;
			case metal: collectibles.add(new Collectible(1062 + 3 * (level - 1), level, pos)) ; return ;
			default: return ;
		}
		
	} 	
	
	public void removeCollectible(Collectible collectible) { collectibles.remove(collectible) ;}
	
 	public void displayCollectibles(DrawPrimitives DP)
 	{
 		for (Collectible collectible : collectibles)
		{
 			collectible.display(DP) ;
		}
 	}

 	public boolean containsItem(Item item)
 	{
 		for (Creature creature : creatures)
 		{
 			if (creature.getBag().contains(item))
 			{
 				return true ;
 			}
 		}
 		
 		return false ;
 	} 	
 	
 	public void printItems()
 	{
 		System.out.println(name);
 		creatures.forEach(creature -> creature.getBag().forEach(System.out::println));
 		System.out.println();
 	}
	 	
}