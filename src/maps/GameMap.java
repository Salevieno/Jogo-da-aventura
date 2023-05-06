package maps ;

import java.awt.Dimension;
import java.awt.Image ;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sound.sampled.Clip;

import components.Building;
import components.Collider;
import components.NPCs;
import graphics.DrawingOnPanel;
import liveBeings.Player;
import main.Game;
import utilities.Align;
import utilities.Elements;
import utilities.Scale;
import utilities.UtilG;

public class GameMap 
{
	private String name ;
	private int continent ;
	private int[] connections ;
	private Image image ;
	private Clip music ;
	
	protected List<GroundType> groundTypes ;	
	protected List<MapElements> mapElems ;
	protected List<Building> buildings ;
	protected List<NPCs> npcs ;	
	
	public static Image[] CollectibleImage ;
	public static Image[] GroundImage ;	
	
	private static final Image beachGif = UtilG.loadImage(Game.ImagesPath + "\\Maps\\" + "Map2_beach.gif") ;
	
	public GameMap(String Name, int Continent, int[] Connections, Image image, Clip music, List<Building> building, List<NPCs> npc)
	{
		this.name = Name ;
		this.continent = Continent ;
		this.image = image ;
		this.music = music ;
		this.buildings = building ;
		this.npcs = npc ;
		this.connections = Connections ;
		
		mapElems = new ArrayList<>() ;
		groundTypes = new ArrayList<>() ;
	}

	public String getName() {return name ;}
	public int getContinent() {return continent ;}
	public Image getimage() {return image ;}
	public Clip getMusic() { return music ;}
//	public String[][] getType() {return type ;}
	public List<GroundType> getgroundTypes() {return groundTypes ;}
	public int[] getConnections() {return connections ;}	
	public List<MapElements> getMapElem() {return mapElems ;}
	public List<NPCs> getNPCs() {return npcs ;}
	public List<Building> getBuildings() {return buildings ;}
	public Continents getContinentName(Player player)
	{ 
		return Continents.getAll()[continent + 1] ;
	}
	
	public void addGroundType (GroundType newGroundType) { groundTypes.add(newGroundType) ;}
	
	public static void InitializeStaticVars(String ImagesPath)
	{
		Image Water = UtilG.loadImage(ImagesPath + "MapElem0_Water.png") ;
		/*Image Wall = UtilG.loadImage(ImagesPath + "MapElem1_Wall.png") ;
		Image Berry = UtilG.loadImage(ImagesPath + "MapElem2_Berry.png") ;
		Image Herb = UtilG.loadImage(ImagesPath + "MapElem3_Herb.png") ;
		Image Wood = UtilG.loadImage(ImagesPath + "MapElem4_Wood.png") ;
		Image Metal = UtilG.loadImage(ImagesPath + "MapElem5_Metal.png") ;
		Image TreeForest = UtilG.loadImage(ImagesPath + "MapElem6_TreeForest.png") ;
		Image PalmTree = UtilG.loadImage(ImagesPath + "MapElem7_PalmTree.png") ;
		Image Grass = UtilG.loadImage(ImagesPath + "MapElem8_Grass.png") ;
		Image Rock = UtilG.loadImage(ImagesPath + "MapElem9_Rock.png") ;
		Image Crystal = UtilG.loadImage(ImagesPath + "MapElem10_Crystal.png") ;
		Image Stalactite = UtilG.loadImage(ImagesPath + "MapElem11_Stalactite.png") ;
		Image Volcano = UtilG.loadImage(ImagesPath + "MapElem12_Volcano.png") ;*/
		Image Lava = UtilG.loadImage(ImagesPath + "MapElem13_Lava.png") ;
		Image Ice = UtilG.loadImage(ImagesPath + "MapElem14_Ice.png") ;
		//Image Chest = UtilG.loadImage(ImagesPath + "MapElem15_Chest.png") ;	
		GroundImage = new Image[] {Water, Lava, Ice} ;
	}
	
 	public void initializeGroundTypes(int SkyHeight, Dimension screenDim)
 	{
 		groundTypes = new ArrayList<>() ;
		if (continent == 0)
		{
			if (name.equals("City of archers"))
			{
				for (int j = SkyHeight ; j <= screenDim.height - 1 ; j += 1)
				{
					for (int k = (screenDim.width * 4 / 5) ; k <= screenDim.width - 1 ; k += 1)
					{
						groundTypes.add(new GroundType(GroundTypes.water, new Point(j, k), new Dimension(1, 1))) ;
					}
				}
			}
			if (name.equals("City of thieves"))
			{
				for (int j = 3 ; j <= 21 ; j += 1)
				{
					groundTypes.add(new GroundType(GroundTypes.water, new Point(j, 10), new Dimension(1, 1))) ;	// outer wall (horizontal top)
				}
				for (int k = 10 ; k <= 20 ; k += 1)
				{
					groundTypes.add(new GroundType(GroundTypes.water, new Point(3, k), new Dimension(1, 1))) ;	// outer wall (vertical left edge)
					groundTypes.add(new GroundType(GroundTypes.water, new Point(21, k), new Dimension(1, 1))) ;	// outer wall (vertical left edge)
				}
				for (int j = 8 ; j <= 26 ; j += 1)
				{
					groundTypes.add(new GroundType(GroundTypes.water, new Point(j, 23), new Dimension(1, 1))) ;	// inner wall (horizontal bottom)
				}
				for (int k = 16 ; k <= 23 ; k += 1)
				{
					groundTypes.add(new GroundType(GroundTypes.water, new Point(8, k), new Dimension(1, 1))) ;	// inner wall (vertical left edge)
					groundTypes.add(new GroundType(GroundTypes.water, new Point(26, k), new Dimension(1, 1))) ;	// inner wall (vertical right edge)
				}
			}
			/*if (id == 13 | id == 17)	// shore
			{ 
				RangeX = (float)(0.6) ;
				for (double k = Skyratio ; k <= 1.0 ; k += 1.0 / screenDim[1])
				{
					for (double j = 0.8 ; j <= 1.0 ; j += 1.0 / screenDim[0])
					{
						if (!Utg.isInside(new double[] {j, k}, new double[] {0.8, 0.8}, 0.1, 0.08) & !Utg.isInside(new double[] {j, k}, new double[] {0.9, 0.808}, 0.05, 0.16))
						{
							water = Utg.AddElem(water, new Point((int) (j * screenDim[0]), (int) (k * screenDim[1]))) ;
						}
					}
				}
			}*/
		}
		/*if (Continent == 1)	// cave
		{
			if (id == 36)
			{
				// Positions of the maze walls are in % of the screen size
				int[] MazeStartPos = new int[] {10, 15, 51, 15, 39, 2, 35, 17, 77, 92, 67, 17, 57, 88, 49, 4, 81, 2, 5, 54, 71, 15, 58, 11, 39, 12, 80, 59, 5, 3, 0, 54} ;
				int[] MazeEndPos = new int[] {93, 88, 77, 39, 51, 27, 77, 67, 81, 100, 77, 46, 71, 100, 70, 25, 96, 70, 25, 77, 100, 25, 71, 20, 45, 61, 94, 80, 20, 20, 42, 100} ;
				int[] MazeHorizontalWallsPos = new int[] {2, 5, 12, 15, 16, 32, 32, 39, 42, 42, 45, 54, 54, 54, 74, 77, 80} ;
				int[] MazeVerticalWallsPos = new int[] {10, 15, 17, 38, 39, 45, 51, 67, 77, 81, 85, 88, 93, 100, 100} ;
				for (int k = 0 ; k <= MazeHorizontalWallsPos.length - 1 ; k += 1)
				{
					for (int j = MazeStartPos[k] ; j <= MazeEndPos[k] ; j += 1)
					{
						invisible_wall = Utg.AddElem(invisible_wall, new Point(j * 35 / 100, MazeHorizontalWallsPos[k] * 35 / 100)) ;		// maze invisible wall - horizontal parts
					}
				}
				for (int j = 0 ; j <= MazeVerticalWallsPos.length - 1 ; j += 1)
				{
					for (int k = MazeStartPos[j + MazeHorizontalWallsPos.length] ; k <= MazeEndPos[j + MazeHorizontalWallsPos.length] ; k += 1)
					{
						invisible_wall = Utg.AddElem(invisible_wall, new Point(MazeVerticalWallsPos[j] * 35 / 100, k * 35 / 100)) ;			// maze invisible wall - vertical parts
					}
				}
			}
		}
		if (Continent == 3)	// volcano
		{
			for (int j = 0 ; j <= 40 ; ++j)
			{
				int [] randomPos = new int[] {Utg.RandomCoord1D(screenDim[0], MinX, RangeX, 1), Utg.RandomCoord1D(screenDim[1], MinY, RangeY, 1)} ;
				lava = Utg.AddElem(lava, new Point(randomPos[0], randomPos[1])) ;	// lava
			}
		}
		if (Continent == 4)	// snowland
		{
			for (int j = 0 ; j <= 4 ; ++j)
			{
				int [] randomPos = new int[] {Utg.RandomCoord1D(screenDim[0], MinX, RangeX, 1), Utg.RandomCoord1D(screenDim[1], MinY, RangeY, 1)} ;
				ice = Utg.AddElem(ice, new Point(randomPos[0], randomPos[1])) ;	// ice			
			}
		}
		if (60 < id & id <= 66)	// ocean maps
		{
			water = new Point[screenDim[0] * screenDim[1]] ;
			for (int j = 0 ; j <= screenDim[0] - 1 ; ++j)
			{
				for (int k = 0 ; k <= screenDim[1] - 1 ; ++k)
				{
					water[j * screenDim[1] + k] = new Point(j, k) ;	// water
				}
			}
		}*/
		
		// add colliders to map elements
		/*if (MapElem != null)
		{
			for (int me = 0 ; me <= MapElem.length - 1 ; me += 1)
			{
				if (MapElem[me].getBlock() != null)
				{
					for (int b = 0 ; b <= MapElem[me].getBlock().length - 1 ; b += 1)
					{
						int[] Pos = new int[] {MapElem[me].getPos()[0] + MapElem[me].getBlock()[b][0], MapElem[me].getPos()[1] - MapElem[me].getBlock()[b][1]} ;
						if (Pos[0] <= 400 & Pos[1] <= 400)
						{
							invisible_wall = Utg.AddElem(invisible_wall, new Point(Pos[0], Pos[1])) ;	// collider
						}
					}
				}
			}
		}*/
 	}

	public boolean IsACity() { return (this instanceof CityMap) ;}
	public boolean isAField() { return (this instanceof FieldMap) ;}
	public boolean groundIsWalkable(Point pos, Elements superElem)
	{

		if (superElem != null) { if (superElem.equals(Elements.air)) { return true ;}}
		
 		List<Collider> allColliders = allColliders() ;
 		for (Collider collider : allColliders)
 		{
 			if (pos.equals(collider.getPos())) { return false ;}
 		}
		
		return true ;
		
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
	
	public static GameMap[] inSecretIsland()
	{
		GameMap[] secretIslandMaps = new GameMap[7] ;
		GameMap[] allMaps = Game.getMaps() ;
		
		for (int i = 0 ; i <= 7 - 1 ; i += 1)
		{
			secretIslandMaps[i] = allMaps[i + 60] ;
		}
		
		return secretIslandMaps ;
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

	public GroundTypes groundTypeAtPoint(Point pos)
	{
		if (groundTypes == null) { return null ;}		
		
//		for (GroundType groundType : groundTypes)
//		{
//			if (groundType.getPos().equals(pos)) { return groundType.getType() ;}			
//		}
		for (GroundType groundType : groundTypes)
		{
			if (UtilG.isInside(pos, groundType.getPos(), groundType.getSize())) { return groundType.getType() ;}			
		}
		
		return null ;
	}
	
 	public void display(Point pos, Scale scale, DrawingOnPanel DP)
 	{
 		DP.DrawImage(image, pos, scale, Align.bottomLeft) ;
 	}
 	
 	public void display(DrawingOnPanel DP)
 	{
 		if (name.contains("Cave")) { DP.DrawImage(image, Game.getScreen().getCenter(), Align.center) ;}
 		else { DP.DrawImage(image, Game.getScreen().getMapCenter(), Align.center) ;}
 		
 		if (name.equals("City of the archers"))
 		{
 	 		DP.DrawImage(beachGif, new Point(Game.getScreen().getSize().width, 96), Align.topRight) ;
 		}
 	}
 	
 	public void displayElements(Point playerPos, DrawingOnPanel DP)
 	{ 		
 		if (mapElems == null) { return ;}
 		
 		mapElems.forEach(mapElem -> mapElem.display(playerPos, DP)) ;
 	}
 	
 	public void displayBuildings(Point playerPos, int cityID, DrawingOnPanel DP)
 	{
		if (buildings == null) { return ;}
		
		buildings.forEach(building -> building.display(playerPos, cityID, DP)) ;
 	}
	
	public void displayNPCs(DrawingOnPanel DP)
	{
		if (npcs == null) { return ;}

		npcs.forEach(npc -> npc.display(DP));
	}
	
	public void displayGroundTypes(DrawingOnPanel DP)
	{
 		if (groundTypes == null) { return ;}
 		
		groundTypes.forEach(groundType -> {
			switch (groundType.type)
			{
				case water: DP.DrawRect(groundType.pos, Align.topLeft, groundType.size, 1, Game.colorPalette[13], null) ; break ;
				case lava: DP.DrawRect(groundType.pos, Align.topLeft, groundType.size, 1, Game.colorPalette[6], null) ; break ;
				default: break ;
			}
		});
	}

	
	@Override
	public String toString()
	{
		return "GameMap [name=" + name + ", continent=" + continent + ", connections=" + Arrays.toString(connections)
				+ "]";
	}	

}
