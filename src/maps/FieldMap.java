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
import graphics.DrawingOnPanel;
import items.Item;
import liveBeings.Creature;
import liveBeings.CreatureType;
import main.Game;
import screen.Screen;
import utilities.TimeCounter;
import utilities.UtilG;

public class FieldMap extends GameMap
{
	private List<Collectible> collectibles ;
	private List<Creature> creatures ;
	private int level ;
	private int[] collectibleDelay ;
	private Map<CollectibleTypes, TimeCounter> collectibleCounter ;
	
	private static final Image treeImage = UtilG.loadImage(Game.ImagesPath + "\\MapElements\\" + "MapElem6_TreeForest.png") ;
	private static final Image grassImage = UtilG.loadImage(Game.ImagesPath + "\\MapElements\\" + "MapElem8_Grass.png") ;

	public FieldMap(String name, Continents continent, int[] connections, Image image, Clip music, int collectibleLevel, int[] collectibleDelay, int[] creatureTypeIDs, List<NPCs> npcs)
	{
		super(name, continent, connections, image, music, null, npcs) ;
		this.level = collectibleLevel ;
		this.collectibleDelay = collectibleDelay ;
		
		
		// add map elements
		Screen screen = Game.getScreen() ;
		Point minCoord = new Point(20, Game.getSky().height + 20) ;
		Dimension range = new Dimension(screen.getSize().width - 40, screen.getSize().height - Game.getSky().height - 40) ;
		Dimension step = new Dimension(1, 1) ;
		
		int numberTrees = 5 ;
		int numberGrass = 20 ;	

		for (int i = 0 ; i <= numberTrees - 1 ; i += 1)
		{
			Point randomPos = UtilG.RandomPos(minCoord, range, step) ;
			mapElems.add(new MapElements(i, "ForestTree", randomPos, treeImage)) ;				
		}
		for (int i = 0 ; i <= numberGrass - 1 ; i += 1)
		{
			Point randomPos = UtilG.RandomPos(minCoord, range, step) ;
			mapElems.add(new MapElements(i, "grass", randomPos, grassImage)) ;				
		}
		
		
		// add collectbiles
		collectibles = new ArrayList<Collectible>() ;
		collectibleCounter = new HashMap<>() ;
		for (CollectibleTypes type : CollectibleTypes.values())
		{
			addCollectible(type) ;
			collectibleCounter.put(type, new TimeCounter(0, type.getSpawnTime())) ;
		}
		
		
		// add creatures
		creatures = new ArrayList<Creature>() ;			
		for (int creatureTypeID : creatureTypeIDs)
		{
			if (creatureTypeID <= -1) { continue ;}
			
			CreatureType creatureType = Game.getCreatureTypes()[creatureTypeID];
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
	
	public boolean hasCreatures() { return creatures != null ;}
	
	public void IncCollectiblesCounter() { collectibleCounter.values().forEach(TimeCounter::inc) ;}
	
	public void ActivateCollectiblesCounter()
	{
		// TODO use filter ;)
		collectibleCounter.entrySet().forEach(entry -> 
		{
			if (entry.getValue().finished())
			{
				addCollectible(entry.getKey()) ;
				entry.getValue().reset() ;
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
			case berry: collectibles.add(new Collectible(220, level, pos, collectibleDelay[0])) ; return ;
			case herb: collectibles.add(new Collectible(60 + 3 * (level - 1), level, pos, collectibleDelay[1])) ; return ;
			case wood: collectibles.add(new Collectible(61 + 3 * (level - 1), level, pos, collectibleDelay[2])) ; return ;
			case metal: collectibles.add(new Collectible(62 + 3 * (level - 1), level, pos, collectibleDelay[3])) ; return ;
			default: return ;
		}
		
	} 	
	
 	public void displayCollectibles(DrawingOnPanel DP)
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