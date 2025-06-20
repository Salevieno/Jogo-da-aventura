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
import java.util.stream.Collectors;

import javax.sound.sampled.Clip;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import components.NPC;
import components.NPCType;
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
	private final int level ;
	private List<Creature> creatures ;
	private List<Collectible> collectibles ;
	private Map<CollectibleTypes, GameTimer> collectibleCounters ;
	
	private static final int numberTrees = 5 ;
	private static final int numberGrass = 30 ;
	private static final int numberRocks = 10 ;
	private static final String mapElementsPath = "\\MapElements\\" ;
	private static final Image treeImage = UtilS.loadImage(mapElementsPath + "MapElem6_TreeForest.png") ;
	private static final Image grassImage = UtilS.loadImage(mapElementsPath + "MapElem8_Grass.png") ;
	private static final Image grassImage2 = UtilS.loadImage(mapElementsPath + "MapElem8_Grass2.png") ;
	private static final Image rockImage = UtilS.loadImage(mapElementsPath + "MapElem9_Rock.png") ;

	public FieldMap(String name, Continents continent, int[] connections, Image image, Clip music, int collectibleLevel, List<NPC> npcs, List<GroundRegion> groundRegions)
	{
		super(name, continent, connections, image, music, null, npcs) ;
		this.groundRegions = groundRegions ;
		this.level = collectibleLevel ;
		this.npcs = npcs ;
	}
	public FieldMap(String name, Continents continent, int[] connections, Image image, Clip music, int collectibleLevel, List<NPC> npcs)
	{
		this(name, continent, connections, image, music, collectibleLevel, npcs, null) ;
	}

	@SuppressWarnings("unchecked")
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

			Image image = UtilS.loadImage(pathMaps + "Map" + String.valueOf(id + 5) + ".png") ;
			Clip music = GameMap.musicForest ;

			JSONObject collectibles = (JSONObject) mapData.get("Collectibles") ;
			int collectibleLevel = (int) (long) collectibles.get("level") ;

			List<Long> creatures = (List<Long>) mapData.get("Creatures") ;
			int[] creatureIDs = new int[creatures.size()] ;
			for (int i = 0 ; i <= creatures.size() - 1 ; i += 1)
			{
				creatureIDs[i] = (int) (long) creatures.get(i) ;
			}

			List<NPC> npcs = FieldMap.createQuestNPCs(id, npcTypes) ;
			List<GroundRegion> groundRegions = groundRegionsFromJson((JSONObject) mapData.get("GroundRegions")) ;

			FieldMap map = new FieldMap(name, continent, connections, image, music, collectibleLevel, npcs, groundRegions) ;

			map.addCollectibles() ;
			map.addCreatures(creatureIDs) ;
			map.addMapElements() ;
			map.addDiggingItems() ;
			fieldMaps[id] = map ;
		}

		return fieldMaps ;
	}

// 	public void initializeGroundTypes(int SkyHeight, Dimension screenDim)
// 	{
// 		groundTypes = new ArrayList<>() ;
//		if (continent.equals(Continents.forest))
//		{
//			if (name.equals("City of archers"))
//			{
//				for (int j = SkyHeight ; j <= screenDim.height - 1 ; j += 1)
//				{
//					for (int k = (screenDim.width * 4 / 5) ; k <= screenDim.width - 1 ; k += 1)
//					{
//						groundTypes.add(new GroundType(GroundTypes.water, new Point(j, k), new Dimension(1, 1))) ;
//					}
//				}
//			}
//			if (name.equals("City of thieves"))
//			{
//				for (int j = 3 ; j <= 21 ; j += 1)
//				{
//					groundTypes.add(new GroundType(GroundTypes.water, new Point(j, 10), new Dimension(1, 1))) ;	// outer wall (horizontal top)
//				}
//				for (int k = 10 ; k <= 20 ; k += 1)
//				{
//					groundTypes.add(new GroundType(GroundTypes.water, new Point(3, k), new Dimension(1, 1))) ;	// outer wall (vertical left edge)
//					groundTypes.add(new GroundType(GroundTypes.water, new Point(21, k), new Dimension(1, 1))) ;	// outer wall (vertical left edge)
//				}
//				for (int j = 8 ; j <= 26 ; j += 1)
//				{
//					groundTypes.add(new GroundType(GroundTypes.water, new Point(j, 23), new Dimension(1, 1))) ;	// inner wall (horizontal bottom)
//				}
//				for (int k = 16 ; k <= 23 ; k += 1)
//				{
//					groundTypes.add(new GroundType(GroundTypes.water, new Point(8, k), new Dimension(1, 1))) ;	// inner wall (vertical left edge)
//					groundTypes.add(new GroundType(GroundTypes.water, new Point(26, k), new Dimension(1, 1))) ;	// inner wall (vertical right edge)
//				}
//			}
//			switch(name)
//			{
//				case "Forest 3":
//					groundTypes.add(new GroundType(GroundTypes.water, new Point(551, 269), new Dimension(49, 83))) ;
//					return ;
//					
//				case "Forest 4":
//					groundTypes.add(new GroundType(GroundTypes.water, new Point(0, 269), new Dimension(64, 83))) ;
//					return ;
//					
//				default: return ;
//			}
//			/*if (id == 13 | id == 17)	// shore
//			{ 
//				RangeX = (float)(0.6) ;
//				for (double k = Skyratio ; k <= 1.0 ; k += 1.0 / screenDim[1])
//				{
//					for (double j = 0.8 ; j <= 1.0 ; j += 1.0 / screenDim[0])
//					{
//						if (!Utg.isInside(new double[] {j, k}, new double[] {0.8, 0.8}, 0.1, 0.08) & !Utg.isInside(new double[] {j, k}, new double[] {0.9, 0.808}, 0.05, 0.16))
//						{
//							water = Utg.AddElem(water, new Point((int) (j * screenDim[0]), (int) (k * screenDim[1]))) ;
//						}
//					}
//				}
//			}*/
//		}
//		/*if (Continent == 1)	// cave
//		{
//			if (id == 36)
//			{
//				// Positions of the maze walls are in % of the screen size
//				int[] MazeStartPos = new int[] {10, 15, 51, 15, 39, 2, 35, 17, 77, 92, 67, 17, 57, 88, 49, 4, 81, 2, 5, 54, 71, 15, 58, 11, 39, 12, 80, 59, 5, 3, 0, 54} ;
//				int[] MazeEndPos = new int[] {93, 88, 77, 39, 51, 27, 77, 67, 81, 100, 77, 46, 71, 100, 70, 25, 96, 70, 25, 77, 100, 25, 71, 20, 45, 61, 94, 80, 20, 20, 42, 100} ;
//				int[] MazeHorizontalWallsPos = new int[] {2, 5, 12, 15, 16, 32, 32, 39, 42, 42, 45, 54, 54, 54, 74, 77, 80} ;
//				int[] MazeVerticalWallsPos = new int[] {10, 15, 17, 38, 39, 45, 51, 67, 77, 81, 85, 88, 93, 100, 100} ;
//				for (int k = 0 ; k <= MazeHorizontalWallsPos.length - 1 ; k += 1)
//				{
//					for (int j = MazeStartPos[k] ; j <= MazeEndPos[k] ; j += 1)
//					{
//						invisible_wall = Utg.AddElem(invisible_wall, new Point(j * 35 / 100, MazeHorizontalWallsPos[k] * 35 / 100)) ;		// maze invisible wall - horizontal parts
//					}
//				}
//				for (int j = 0 ; j <= MazeVerticalWallsPos.length - 1 ; j += 1)
//				{
//					for (int k = MazeStartPos[j + MazeHorizontalWallsPos.length] ; k <= MazeEndPos[j + MazeHorizontalWallsPos.length] ; k += 1)
//					{
//						invisible_wall = Utg.AddElem(invisible_wall, new Point(MazeVerticalWallsPos[j] * 35 / 100, k * 35 / 100)) ;			// maze invisible wall - vertical parts
//					}
//				}
//			}
//		}
//		if (Continent == 3)	// volcano
//		{
//			for (int j = 0 ; j <= 40 ; ++j)
//			{
//				int [] randomPos = new int[] {Utg.RandomCoord1D(screenDim[0], MinX, RangeX, 1), Utg.RandomCoord1D(screenDim[1], MinY, RangeY, 1)} ;
//				lava = Utg.AddElem(lava, new Point(randomPos[0], randomPos[1])) ;	// lava
//			}
//		}
//		if (Continent == 4)	// snowland
//		{
//			for (int j = 0 ; j <= 4 ; ++j)
//			{
//				int [] randomPos = new int[] {Utg.RandomCoord1D(screenDim[0], MinX, RangeX, 1), Utg.RandomCoord1D(screenDim[1], MinY, RangeY, 1)} ;
//				ice = Utg.AddElem(ice, new Point(randomPos[0], randomPos[1])) ;	// ice			
//			}
//		}
//		if (60 < id & id <= 66)	// ocean maps
//		{
//			water = new Point[screenDim[0] * screenDim[1]] ;
//			for (int j = 0 ; j <= screenDim[0] - 1 ; ++j)
//			{
//				for (int k = 0 ; k <= screenDim[1] - 1 ; ++k)
//				{
//					water[j * screenDim[1] + k] = new Point(j, k) ;	// water
//				}
//			}
//		}*/
//		
//		// add colliders to map elements
//		/*if (MapElem != null)
//		{
//			for (int me = 0 ; me <= MapElem.length - 1 ; me += 1)
//			{
//				if (MapElem[me].getBlock() != null)
//				{
//					for (int b = 0 ; b <= MapElem[me].getBlock().length - 1 ; b += 1)
//					{
//						int[] Pos = new int[] {MapElem[me].getPos()[0] + MapElem[me].getBlock()[b][0], MapElem[me].getPos()[1] - MapElem[me].getBlock()[b][1]} ;
//						if (Pos[0] <= 400 & Pos[1] <= 400)
//						{
//							invisible_wall = Utg.AddElem(invisible_wall, new Point(Pos[0], Pos[1])) ;	// collider
//						}
//					}
//				}
//			}
//		}*/
// 	}

	private static List<GroundRegion> groundRegionsFromJson(JSONObject json)
	{
		List<GroundRegion> groundRegions = new ArrayList<>() ;

		for (GroundType groundType : GroundType.values())
		{
			List<List<Point>> points = groundRegionPointsFromJson(json, groundType) ;

			if (points == null || points.isEmpty()) { continue ;}

			points.forEach(point -> groundRegions.add(new GroundRegion(groundType, point))) ;
		}

		return groundRegions ;
	}

	private static List<List<Point>> groundRegionPointsFromJson(JSONObject json, GroundType key)
	{
		JSONArray polygons = (JSONArray) json.get(key.toString());
		
		if (polygons == null || polygons.isEmpty()) { return null ;}
		
		List<List<Point>> regionsPoints = new ArrayList<>();
		for (int i = 0 ; i <= polygons.size() - 1 ; i += 1)
		{
			JSONArray points = (JSONArray) polygons.get(i);
			List<Point> polygonPoints = new ArrayList<>() ;
			for (Object pointObj : points)
			{
				int x = ((Long) ((JSONObject) pointObj).get("x")).intValue();
				int y = ((Long) ((JSONObject) pointObj).get("y")).intValue() + Sky.height;
				polygonPoints.add(new Point(x, y));
			}

			regionsPoints.add(polygonPoints) ;
		}

		return regionsPoints ;
	}

	
	public void addMapElements()
	{
		Screen screen = Game.getScreen() ;
		Point minCoord = new Point(20, Sky.height + 20) ;
		Dimension range = new Dimension(screen.mapSize().width - 100, screen.mapSize().height - 100) ;
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
	
	public static List<NPC> createQuestNPCs(int mapID, NPCType[] npcTypes)
	{
		
		if (npcTypes == null) { System.out.println("Erro ao criar npcs de quest: tipos de npc nulo") ; return null ;}
		if (npcTypes.length <= 0) { System.out.println("Erro ao criar npcs de quest: sem tipos de npc") ; return null ;}
		
		NPC questExp = new NPC(npcTypes[12], Game.getScreen().pos(0.27, 0.73)) ;
		NPC questItem = new NPC(npcTypes[13], Game.getScreen().pos(0.87, 0.63)) ;
		switch (mapID)
		{
			case 0: return List.of(questExp, questItem) ;
			default: return List.of(questExp, questItem) ;
		}
	}
	
	public boolean hasCreatures() { return creatures != null && !creatures.isEmpty() ;}
	
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
	
 	public void displayCollectibles()
 	{
 		for (Collectible collectible : collectibles)
		{
 			collectible.display() ;
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


	@Override
	public String toString()
	{
		return "FieldMap " + name + " level " + level + " " + "creatures " + creatures.stream().map(creature -> creature.getType().getID()).collect(Collectors.toList()) ;
	}
 	

	 	
}