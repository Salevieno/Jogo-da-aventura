package maps ;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image ;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.Clip;

import components.Building;
import components.Collider;
import components.Hitbox;
import components.NPC;
import graphics.Align;
import graphics.Scale;
import graphics2.SpriteAnimation;
import items.Fab;
import items.GeneralItem;
import items.Item;
import main.Game;
import main.GamePanel;
import main.Music;
import main.TextCategories;
import screen.Sky;
import utilities.Elements;
import utilities.Util;
import utilities.UtilS;

public class GameMap 
{
	protected String name ;
	protected Continents continent ;
	protected int[] connections ;
	protected Image image ;
	protected Clip music ;
	
	protected List<GroundRegion> groundTypes ;	
	protected List<MapElement> mapElems ;
	protected List<Building> buildings ;
	protected List<NPC> npcs ;
	protected Map<Item, Double> diggingItems ;
	
	// private static Image[] CollectibleImage ;
	// private static Image[] GroundImage ;	
	
	protected static final SpriteAnimation beachGif ;
	protected static final Image infoWindow ;
	protected static final String dadosPath = Game.dadosPath + "gameMaps\\" ;
	
	public static final Map<Item, Double> allDiggingItems ;
	public static final Clip musicCities ;
	public static final Clip musicForest ;
	public static final Clip musicSpecial ;

	static
	{
		int[] fabItemIDs = new int[] {0,3,4,6,7,11,13,16,17,25,30,48,51,59,60,61,62,63,64,65,75,76,77,78,79,80,86,87,88} ;
		double[] fabItemPotentials = new double[] {5,4,3,3,3,1,4,5,1,5,3,4,4,4,3,5,5,5,4,4,2,2,3,2,1,1,5,3,2} ;

		int[] genItemIDs = new int[] {2,4,8,9,25,33,35,41,45,46,48,50,52,59,60,61,67,68,75,77,80,90,91,109,110,113,116,117,120,122,125,126,131,136,141,142,143,144,147,150,154,155,156,157,161,162,166,167,168,169,170,171,172,173,175,178,179,183,191,193,202,203,205,215,221,223,224,228,230,231,232,233,242,248,249,250,259,267,268,276,277,278,283,286,292,293,294,295,296,299,301,310,311,315,316,317,323,325,327,335,337,338,339,341,343,345,346,350,353,355,358,363,367,368,370,371,375,378,381} ;
		double[] genItemPotentials = new double[] {5,5,5,5,4,4,3,4,4,5,3,2,4,2,1,2,3,4,4,2,3,1,1,3,1,3,2,4,3,1,3,3,1,2,2,1,3,2,3,2,4,4,4,5,2,3,3,5,5,4,4,1,2,3,2,2,3,4,5,2,2,3,4,2,1,5,4,5,5,4,5,5,5,3,2,4,3,4,4,1,4,1,2,5,3,4,5,4,2,1,2,3,4,3,2,3,3,1,1,2,2,3,2,4,3,2,4,5,3,4,4,3,2,2,5,4,3,2,1} ;
		
		allDiggingItems = new HashMap<>() ;
		for (int i = 0 ; i <= fabItemIDs.length - 1; i += 1)
		{
			allDiggingItems.put(Fab.getAll()[fabItemIDs[i]], fabItemPotentials[i]) ;
		}
		
		for (int i = 0 ; i <= genItemIDs.length - 1; i += 1)
		{
			allDiggingItems.put(GeneralItem.getAll()[genItemIDs[i]], genItemPotentials[i]) ;
		}
		beachGif = new SpriteAnimation("\\Maps\\" + "Map2_beach.png", new Point(Game.getScreen().mapSize().width, 192), Align.topRight, new Dimension(80, 384), 12, 15) ;
		infoWindow = UtilS.loadImage("\\Windows\\" + "MapInfo.png") ;

		musicCities = Music.musicFileToClip(new File(Game.MusicPath + "cidade.wav").getAbsoluteFile()) ;
		musicForest = Music.musicFileToClip(new File(Game.MusicPath + "floresta.wav").getAbsoluteFile()) ;
		musicSpecial = Music.musicFileToClip(new File(Game.MusicPath + "12-Special.wav").getAbsoluteFile()) ;
		
//		allDiggingItems.entrySet().forEach(System.out::println);
	}
	
	
	static
	{
//		Image Water = UtilS.loadImage("MapElem0_Water.png") ;
		/*Image Wall = UtilS.loadImage("MapElem1_Wall.png") ;
		Image Berry = UtilS.loadImage("MapElem2_Berry.png") ;
		Image Herb = UtilS.loadImage("MapElem3_Herb.png") ;
		Image Wood = UtilS.loadImage("MapElem4_Wood.png") ;
		Image Metal = UtilS.loadImage("MapElem5_Metal.png") ;
		Image TreeForest = UtilS.loadImage("MapElem6_TreeForest.png") ;
		Image PalmTree = UtilS.loadImage("MapElem7_PalmTree.png") ;
		Image Grass = UtilS.loadImage("MapElem8_Grass.png") ;
		Image Rock = UtilS.loadImage("MapElem9_Rock.png") ;
		Image Crystal = UtilS.loadImage("MapElem10_Crystal.png") ;
		Image Stalactite = UtilS.loadImage("MapElem11_Stalactite.png") ;
		Image Volcano = UtilS.loadImage("MapElem12_Volcano.png") ;*/
//		Image Lava = UtilS.loadImage("MapElem13_Lava.png") ;
//		Image Ice = UtilS.loadImage("MapElem14_Ice.png") ;
		//Image Chest = UtilS.loadImage("MapElem15_Chest.png") ;	
//		GroundImage = new Image[] {Water, Lava, Ice} ;
	}
	
	public GameMap(String Name, Continents continent, int[] Connections, Image image, Clip music, List<Building> building, List<NPC> npc)
	{
		this.name = Name ;
		this.continent = continent ;
		this.connections = Connections ;
		this.image = image ;
		this.music = music ;
		this.buildings = building ;
		this.npcs = npc ;
		diggingItems = new HashMap<>() ;
		
		mapElems = new ArrayList<>() ;
		groundTypes = new ArrayList<>() ;
	}


	public static GameMap[] assemble(CityMap[] cityMaps, FieldMap[] fieldMaps, SpecialMap[] specialMaps)
	{

		GameMap[] allMaps = new GameMap[cityMaps.length + fieldMaps.length + specialMaps.length] ;
		for (int i = 0 ; i <= cityMaps.length - 1 ; i += 1)
		{
			allMaps[i] = cityMaps[i] ;
		}
		for (int i = cityMaps.length ; i <= cityMaps.length + 34 - 1 ; i += 1)
		{
			allMaps[i] = fieldMaps[i - cityMaps.length] ;
		}
		allMaps[39] = specialMaps[0] ;
		for (int i = 40 ; i <= 60 - 1 ; i += 1)
		{
			allMaps[i] = fieldMaps[i - cityMaps.length - 1] ;
		}
		allMaps[60] = specialMaps[1] ;
		for (int i = 61 ; i <= cityMaps.length + fieldMaps.length + specialMaps.length - 1 ; i += 1)
		{
			allMaps[i] = fieldMaps[i - cityMaps.length - 2] ;
		}
//		System.arraycopy(cityMaps, 0, allMaps, 0, cityMaps.length) ;
//		System.arraycopy(fieldMaps, 0, allMaps, cityMaps.length, fieldMaps.length) ;
//		System.arraycopy(specialMaps, 0, allMaps, cityMaps.length + fieldMaps.length, specialMaps.length) ;
		return allMaps ;

	}
	
	public void addGroundType (GroundRegion newGroundType) { groundTypes.add(newGroundType) ;}
	public void removeMapElem (MapElement mapElem) { mapElems.remove(mapElem) ;}
	
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

	public boolean IsACity() { return (this instanceof CityMap) ;}
	public boolean isAField() { return (this instanceof FieldMap) ;}
	public boolean isSpecial() { return (this instanceof SpecialMap) ;}
	public boolean meetsTwoMapsUp() { return connections[1] != connections[0] ;}
	public boolean meetsTwoMapsLeft() { return connections[3] != connections[2] ;}
	public boolean meetsTwoMapsDown() { return connections[4] != connections[5] ;}
	public boolean meetsTwoMapsRight() { return connections[6] != connections[7] ;}
	public boolean groundIsWalkable(Point pos, Elements superElem)
	{
		
		// checking colliders
 		List<Collider> allColliders = allColliders() ;
 		for (Collider collider : allColliders)
 		{
 			if (pos.equals(collider.getPos()) & superElem != Elements.air) { return false ;}
 		}

 		// checking ground types
 		if (groundTypeAtPoint(pos) == null) { return true ;}
 		
 		switch (groundTypeAtPoint(pos))
 		{
 			case water: return superElem == Elements.water ;
 			default: return true ;
 		}
		
	}
	private boolean hasGroundTypes() { return groundTypes != null && !groundTypes.isEmpty() ;}

	protected Point randomPosOnLand(Point minCoord, Dimension range, Dimension step)
	{
		Point randomPos = Util.RandomPos(minCoord, range, step) ;
		while (groundTypeAtPoint(randomPos) != null)
		{
			randomPos = Util.RandomPos(minCoord, range, step) ;
		}
		return randomPos ;
	}
	
	protected Point randomPosOnLand()
	{
		Point minCoord = new Point(0, (int) (0.2*Game.getScreen().getSize().height)) ;
		Dimension range = new Dimension(Game.getScreen().mapSize().width, (int) ((1 - (float)(Sky.height)/Game.getScreen().getSize().height) * Game.getScreen().getSize().height)) ;
		Dimension step = new Dimension(1, 1) ;

		return randomPosOnLand(minCoord, range, step) ;
	}
	
	public Point randomPosInMap()
	{
		Point minCoord = new Point(0, (int) (0.2*Game.getScreen().getSize().height)) ;
		Dimension range = new Dimension(Game.getScreen().mapSize().width, (int) ((1 - (float)(Sky.height)/Game.getScreen().getSize().height) * Game.getScreen().getSize().height)) ;
		
		return Util.RandomPos(minCoord, range, new Dimension(1, 1)) ;
	}
	
	public static GameMap[] inForest()
	{
		GameMap[] forestMaps = new GameMap[30] ;
		GameMap[] allMaps = Game.getMaps() ;

		for (int i = 0 ; i <= 30 - 1 ; i += 1)
		{
			forestMaps[i] = allMaps[i] ;
		}
		
		return forestMaps ;
	}
	
	public static GameMap[] inCave()
	{
		GameMap[] caveMaps = new GameMap[10] ;
		GameMap[] allMaps = Game.getMaps() ;

		for (int i = 0 ; i <= 10 - 1 ; i += 1)
		{
			caveMaps[i] = allMaps[i + 30] ;
		}
		
		return caveMaps ;
	}
	
	public static GameMap[] inIsland()
	{
		GameMap[] islandMaps = new GameMap[5] ;
		GameMap[] allMaps = Game.getMaps() ;

		for (int i = 0 ; i <= 5 - 1 ; i += 1)
		{
			islandMaps[i] = allMaps[i + 40] ;
		}
		
		return islandMaps ;
	}
	
	public static GameMap[] inVolcano()
	{
		GameMap[] volcanoMaps = new GameMap[10] ;
		GameMap[] allMaps = Game.getMaps() ;

		for (int i = 0 ; i <= 10 - 1 ; i += 1)
		{
			volcanoMaps[i] = allMaps[i + 45] ;
		}
		
		return volcanoMaps ;
	}
	
	public static GameMap[] inOcean()
	{
		GameMap[] oceanMaps = new GameMap[6] ;
		GameMap[] allMaps = Game.getMaps() ;

		for (int i = 0 ; i <= 6 - 1 ; i += 1)
		{
			oceanMaps[i] = allMaps[i + 61] ;
		}
		
		return oceanMaps ;
	}
	
	public static GameMap[] inSnowland()
	{
		GameMap[] snowlandMaps = new GameMap[5] ;
		GameMap[] allMaps = Game.getMaps() ;

		for (int i = 0 ; i <= 5 - 1 ; i += 1)
		{
			snowlandMaps[i] = allMaps[i + 55] ;
		}
		
		return snowlandMaps ;
	}
	
	public static GameMap[] inSpecial()
	{
		GameMap[] specialMaps = new GameMap[7] ;
		GameMap[] allMaps = Game.getMaps() ;
		
		for (int i = 0 ; i <= 7 - 1 ; i += 1)
		{
			specialMaps[i] = allMaps[i + 60] ;
		}
		
		return specialMaps ;
	}
	
	public void calcDigItemChances()
	{
		double totalPotencial = 0 ;
		for (Item item : diggingItems.keySet())
		{
			totalPotencial += diggingItems.get(item) ;
		}
		
		for (Item item : diggingItems.keySet())
		{
			diggingItems.put(item, diggingItems.get(item) / totalPotencial) ;
		}
	}
	
 	public List<Collider> allColliders()
 	{
 		List<Collider> allColliders = new ArrayList<>() ;
 		
 		if (buildings != null)
 		{
 			buildings.forEach(building -> allColliders.addAll(building.getColliders())) ;
 		}
 		
 		if (npcs != null)
 		{
 			npcs.forEach(npc -> allColliders.addAll(npc.getColliders())) ;
 		}
 		
 		if (mapElems != null)
 		{
 			mapElems.forEach(mapElem -> allColliders.addAll(mapElem.getColliders())) ; 			
 		}
 		
 		return allColliders ;
 	}

	public GroundType groundTypeAtPoint(Point pos)
	{
		if (!hasGroundTypes()) { return null ;}
		
		for (GroundRegion groundType : groundTypes)
		{
			if (!groundType.containsPoint(pos)) { continue ;}
			
			return groundType.getType() ;
		}
		
		return null ;
	}
 	
 	public void displayElements(Point playerPos)
 	{ 		
 		if (mapElems == null) { return ;}
 		
 		mapElems.forEach(mapElem -> mapElem.display(playerPos)) ;
 	}
 	
 	public void displayBuildings(Hitbox playerHitbox, Point playerPos, int cityID)
 	{
		if (buildings == null) { return ;}
		
		buildings.forEach(building -> building.display(playerHitbox, playerPos, cityID)) ;
 	}
	
	public void displayNPCs(Hitbox playerHitbox)
	{
		if (npcs == null) { return ;}

		npcs.forEach(npc -> npc.display(playerHitbox));
	}
	
	public void displayGroundTypes()
	{
 		if (groundTypes == null) { return ;}
 		
		groundTypes.forEach(groundType -> groundType.display()) ;
	}

	public void displayInfoWindow()
	{

		Point pos = new Point(200, 30) ;
		Dimension size = new Dimension(340, 420) ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 11) ;
		Font largeFont = new Font(Game.MainFontName, Font.BOLD, 12) ;
		Font titleFont = new Font(Game.MainFontName, Font.BOLD, 15) ;
		
		GamePanel.DP.drawImage(infoWindow, pos, Align.topLeft) ;
		
		Point titlePos = Util.Translate(pos, size.width / 2 + 5, 13) ;
		GamePanel.DP.drawText(titlePos, Align.center, 0, name, titleFont, Game.palette[0]) ;
		
		Point diggingItemsPos = Util.Translate(pos, 10, 43) ;
		GamePanel.DP.drawText(diggingItemsPos, Align.centerLeft, 0, "Items de escavação", largeFont, Game.palette[0]) ;
		diggingItemsPos.y += 14 ;
		for (Item item : diggingItems.keySet())
		{
			GamePanel.DP.drawText(diggingItemsPos, Align.centerLeft, 0, item.getName(), font, Game.palette[0]) ;
			diggingItemsPos.y += 10 ;
		}
		
		if (this instanceof FieldMap)
		{
			FieldMap fm = (FieldMap) this ;
			
			Point levelPos = Util.Translate(titlePos, 0, 14) ;
			GamePanel.DP.drawText(levelPos, Align.center, 0, "Nível " + String.valueOf(fm.getLevel()), largeFont, Game.palette[6]) ;
			
			Point allItemsPos = Util.Translate(pos, 160, 43) ;
			GamePanel.DP.drawText(allItemsPos, Align.centerLeft, 0, "Items encontrados", largeFont, Game.palette[0]) ;
			allItemsPos.y += 14 ;
			for (Item item : fm.getItems())
			{
				GamePanel.DP.drawText(allItemsPos, Align.centerLeft, 0, item.getName(), font, Game.palette[0]) ;
				allItemsPos.y += 10 ;
			}
			
			return ;
		}
		
		Point levelPos = Util.Translate(titlePos, 0, 14) ;
		GamePanel.DP.drawText(levelPos, Align.center, 0, "Nível 0", largeFont, Game.palette[6]) ;

	}
 	
 	public void displayTudoEstaBem()
 	{
 		Point pos = new Point(500, 10) ;
 		String text = Game.allText.get(TextCategories.allIsGood)[0] ;
 		Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
 		GamePanel.DP.drawText(pos, Align.topLeft, 0, text, font, Game.palette[19]) ;
 	}
	
 	public void display(Point pos, Scale scale)
 	{
 		GamePanel.DP.drawImage(image, pos, scale, Align.bottomLeft) ;
 	}
 	
 	public void display()
 	{
 		if (name.equals("City of the animals"))
 		{
 			GamePanel.DP.drawImage(image, Game.getScreen().getMapCenter(), new Scale(0.5, 0.5), Align.center) ;
 			return ;
 		}
 		
 		GamePanel.DP.drawImage(image, Game.getScreen().getMapCenter(), Align.center) ;
 		
 		if (name.equals("City of the archers"))
 		{
 			beachGif.display(GamePanel.DP) ;
 		}
 	}
	

	public String getName() {return name ;}
	public Continents getContinent() {return continent ;}
	public Image getimage() {return image ;}
	public Clip getMusic() { return music ;}
	public List<GroundRegion> getgroundTypes() {return groundTypes ;}
	public int[] getConnections() {return connections ;}	
	public List<MapElement> getMapElem() {return mapElems ;}
	public List<NPC> getNPCs() {return npcs ;}
	public List<Building> getBuildings() {return buildings ;}
	public Map<Item, Double> getDiggingItems() { return diggingItems ;}
	
 	
	@Override
	public String toString()
	{
		return "GameMap [name=" + name + ", continent=" + continent + ", connections=" + Arrays.toString(connections)
				+ "]";
	}	

}
