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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import components.NPCType;
import components.NPCs;
import graphics.DrawPrimitives;
import items.Item;
import liveBeings.Creature;
import liveBeings.CreatureType;
import main.Game;
import screen.Screen;
import screen.Sky;
import utilities.GameTimer;
import utilities.Util;
import utilities.UtilS;

public class FieldMap extends GameMap
{
	private List<Collectible> collectibles ;
	private List<Creature> creatures ;
	private int level ;
	private Map<CollectibleTypes, GameTimer> collectibleCounters ;
	
	private static final int numberTrees = 5 ;
	private static final int numberGrass = 30 ;
	private static final int numberRocks = 10 ;
	private static final Image treeImage ;
	private static final Image grassImage ;
	private static final Image grassImage2 ;
	private static final Image rockImage ;

	public static final List<Image> images ;
	
	static
	{
		images = new ArrayList<>() ;
		for (int i = 5 ; i <= 67 - 1 ; i += 1)
		{
			if (i == 39 | i == 60) { continue ;}
			
			images.add(UtilS.loadImage("\\Maps\\" + "Map" + String.valueOf(i) + ".png")) ;
		}
		treeImage = UtilS.loadImage("\\MapElements\\" + "MapElem6_TreeForest.png") ;
		grassImage = UtilS.loadImage("\\MapElements\\" + "MapElem8_Grass.png") ;
		grassImage2 = UtilS.loadImage("\\MapElements\\" + "MapElem8_Grass2.png") ;
		rockImage = UtilS.loadImage("\\MapElements\\" + "MapElem9_Rock.png") ;
	}
	
	public FieldMap(String name, Continents continent, int[] connections, Image image, Clip music, int collectibleLevel, List<NPCs> npcs)
	{
		super(name, continent, connections, image, music, null, npcs) ;
		this.level = collectibleLevel ;
		this.npcs = npcs ;
	}


	public static FieldMap[] load(NPCType[] npcTypes)
	{
		JSONArray input = Util.readJsonArray(dadosPath + "mapsField.json") ;
		FieldMap[] fieldMaps = new FieldMap[input.size()] ;

		for (int id = 0 ; id <= input.size() - 1 ; id += 1)
		{

			JSONObject mapData = (JSONObject) input.get(id) ;

			String name = (String) mapData.get("Name") ;
			int continentID = (int) (long) mapData.get("Continent") ;
			Continents continent = Continents.values()[continentID] ;
			JSONObject connectionIDs = (JSONObject) mapData.get("Connections") ;
			int[] connections = new int[8] ;
			connections[0] = (int) (long) connectionIDs.get("topRight") ;
			connections[1] = (int) (long) connectionIDs.get("topLeft") ;
			connections[2] = (int) (long) connectionIDs.get("leftTop") ;
			connections[3] = (int) (long) connectionIDs.get("leftBottom") ;
			connections[4] = (int) (long) connectionIDs.get("bottomLeft") ;
			connections[5] = (int) (long) connectionIDs.get("bottomRight") ;
			connections[6] = (int) (long) connectionIDs.get("rightBottom") ;
			connections[7] = (int) (long) connectionIDs.get("rightTop") ;

			Image image = FieldMap.images.get(id) ;
			Clip music = GameMap.musicForest ;

			JSONObject collectibles = (JSONObject) mapData.get("Collectibles") ;
			int collectibleLevel = (int) (long) collectibles.get("level") ;

			List<Long> creatures = (List<Long>) mapData.get("Creatures") ;
			int[] creatureIDs = new int[creatures.size()] ;
			for (int i = 0 ; i <= creatures.size() - 1 ; i += 1)
			{
				creatureIDs[i] = (int) (long) creatures.get(i) ;
			}

			JSONArray listNPCs = (JSONArray) mapData.get("NPCs") ;
			List<NPCs> npcs = FieldMap.createQuestNPCs(id, npcTypes) ;
//			List<NPCs> npcs = new ArrayList<>() ;
//			for (int i = 0 ; i <= listNPCs.size() - 1 ; i += 1)
//			{
//				npcs.add(readNPCfromJson((JSONObject) listNPCs.get(i))) ;
//			}

			FieldMap map = new FieldMap(name, continent, connections, image, music, collectibleLevel, npcs) ;

			switch (id)
			{
				case 2:
					map.addGroundType(new GroundType(GroundTypes.water, new Point(551, 96 + 269), new Dimension(49, 83))) ;
					break ;
					
				case 3:
					map.addGroundType(new GroundType(GroundTypes.water, new Point(0, 96 + 269), new Dimension(64, 83))) ;
					break ;
					
				case 8, 12:
					map.addGroundType(new GroundType(GroundTypes.water, new Point(500, Sky.height), new Dimension(140, 480 - Sky.height))) ;
					break ;
					
				case 22:
					map.addGroundType(new GroundType(GroundTypes.water, new Point(50, 250), new Dimension(120, 210))) ;
					break ;
					
				default:
					break ;
			}

			map.addCollectibles() ;
			map.addCreatures(creatureIDs) ;
			map.addMapElements() ;
			map.addDiggingItems() ;
			fieldMaps[id] = map ;
		}

		return fieldMaps ;
	}

	
	public void addMapElements()
	{
		// TODO map elements caindo dentro da água
		Screen screen = Game.getScreen() ;
		Point minCoord = new Point(20, Sky.height + 20) ;
		Dimension range = new Dimension(screen.getSize().width - 100, screen.getSize().height - Sky.height - 100) ;
		Dimension step = new Dimension(1, 1) ;
		Set<Image> grassImages = new HashSet<>(Set.of(grassImage, grassImage2)) ;
		
		for (int i = 0 ; i <= numberRocks - 1 ; i += 1)
		{
			Point randomPos = randomPosOnLand(minCoord, range, step) ;
			mapElems.add(new MapElement(i, "rock", rockImage, randomPos)) ;			
		}
		for (int i = 0 ; i <= numberTrees - 1 ; i += 1)
		{
			Point randomPos = randomPosOnLand(minCoord, range, step) ;
			mapElems.add(new MapElement(i, "ForestTree", treeImage, randomPos)) ;				
		}
		for (int i = 0 ; i <= numberGrass - 1 ; i += 1)
		{
			Point randomPos = randomPosOnLand(minCoord, range, step) ;
			mapElems.add(new MapElement(i, "grass", grassImages.stream().skip(Util.randomInt(0, grassImages.size() - 1)).findFirst().get(), randomPos)) ;				
		}
	}

	public void addCollectibles()
	{
		collectibles = new ArrayList<Collectible>() ;
		collectibleCounters = new HashMap<>() ;
		for (CollectibleTypes type : CollectibleTypes.values())
		{
			addCollectible(type) ;
			GameTimer collectibleCounter = new GameTimer(type.getSpawnTime()) ;
			collectibleCounter.start();
			collectibleCounters.put(type, collectibleCounter) ;
		}
	}

	public void addCreatures(int[] creatureTypeIDs)
	{
		creatures = new ArrayList<Creature>() ;
		
		for (int creatureTypeID : creatureTypeIDs)
		{
			if (creatureTypeID <= -1) { continue ;}
			
			CreatureType creatureType = CreatureType.all.get(creatureTypeID);
			Creature creature = new Creature(creatureType, randomPosOnLand()) ;
			creatures.add(creature) ;
		}
	}
	
	public void addDiggingItems()
	{
		for (Item item : allDiggingItems.keySet())
		{
			if (!containsItem(item)) { continue ;}
			
			diggingItems.put(item, allDiggingItems.get(item)) ;
		}
		calcDigItemChances() ;
	}
	
	public static List<NPCs> createQuestNPCs(int mapID, NPCType[] npcTypes)
	{
		
		if (npcTypes == null) { System.out.println("Erro ao criar npcs de quest: tipos de npc nulo") ; return null ;}
		if (npcTypes.length <= 0) { System.out.println("Erro ao criar npcs de quest: sem tipos de npc") ; return null ;}
		
		NPCs questExp = new NPCs(npcTypes[12], Game.getScreen().pos(0.27, 0.73)) ;
		NPCs questItem = new NPCs(npcTypes[13], Game.getScreen().pos(0.87, 0.63)) ;
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
			GameTimer spawnCounter = entry.getValue() ;
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
			pos = randomPosOnLand() ;
		}
		
		switch (type)
		{
			case berry: collectibles.add(new Collectible(220, level, pos)) ; return ;
			case herb: collectibles.add(new Collectible(60 + 3 * (level - 1), level, pos)) ; return ;
			case wood: collectibles.add(new Collectible(61 + 3 * (level - 1), level, pos)) ; return ;
			case metal: collectibles.add(new Collectible(62 + 3 * (level - 1), level, pos)) ; return ;
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
	
 	public void printItems()
 	{
 		System.out.println(name);
 		creatures.forEach(creature -> creature.getBag().forEach(System.out::println));
 		System.out.println();
 	}
	 	
}