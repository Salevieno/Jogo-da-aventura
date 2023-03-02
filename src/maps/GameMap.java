package maps ;

import java.awt.Dimension;
import java.awt.Image ;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
	private String Name ;
	private int Continent ;
	private int[] Connections ;
	private Image image ;
	private Clip music ;
	//private MapElements[] MapElem ;		// 0 = free, 1 = wall, 2 = water, 3 = tree, 4 = grass, 5 = rock, 6 = crystal, 7 = stalactite, 8 = volcano, 9 = lava, 10 = ice, 11 = chest, 12 = berry, 13 = herb, 14 = wood, 15 = metal, 16 = invisible wall
	private String[][] Type ;			// 2 = water, 9 = lava, 10 = ice, 12 = berry, 13 = herb, 14 = wood, 15 = metal
	private Object[] groundType ;
	private int CollectibleLevel ;
	private int[] CollectibleCounter ;	// [Berry, herb, wood, metal]
    private int[] CollectibleDelay ;	// [Berry, herb, wood, metal]
	protected List<MapElements> mapElems ;
	public List<Building> buildings ;
	public List<NPCs> npcs ;
	
	
	//public static int[] MusicID = new int[] {0, 1, 2, 3, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 9, 9, 9, 9, 9, 10, 11, 11, 11, 11, 11, 11} ;  	 
	public static Image[] CollectibleImage ;
	public static Image[] GroundImage ;
	
	
	private static final Image beachGif = UtilG.loadImage(Game.ImagesPath + "\\Maps\\" + "Map2_beach.gif") ;
	
	/*
	 * buildings pos
	 * ID	Name	Map	Pos x	Pos y
0	Hospital	0	0.1	0.7
1	Hospital	1	0.5	0.7
2	Hospital	2	0.18	0.4
3	Hospital	3	0.06	0.46
4	Hospital	4	0.2	0.7
5	Store	0	0.05	0.64
6	Store	1	0.1	0.8
7	Store	2	0.2	0.8
8	Store	3	0.45	0.8
9	Store	4	0.26	0.58
10	Craft	0	0.36	0.44
11	Craft	1	0.03	0.46
12	Craft	2	0.3	0.64
13	Craft	3	0.74	0.71
14	Craft	4	0.14	0.8
15	Bank	0	0.6	0.48
16	Bank	1	0.26	0.49
17	Bank	2	0.6	0.72
18	Bank	3	0.71	0.46
19	Bank	4	0.56	0.58
20	Forge	0	0.8	0.35
21	Forge	1	0.8	0.6
22	Forge	2	0.67	0.9
23	Forge	3	0.26	0.6
24	Forge	4	0.79	0.42
25	Sign	0	0.8	0.62
26	Sign	1	0.55	0.26
27	Sign	2	0.05	0.55
28	Sign	3	0.09	0.54
29	Sign	4	0.7	0.26
	 * */
	public GameMap(String Name, int Continent, int[] Connections, Image image, Clip music, List<Building> building, List<NPCs> npc)
	{
		this.Name = Name ;
		this.Continent = Continent ;
		this.image = image ;
		this.music = music ;
		this.buildings = building ;
		this.npcs = npc ;
		this.Connections = Connections ;
		
		mapElems = new ArrayList<MapElements>() ;
	}

	public String getName() {return Name ;}
	public int getContinent() {return Continent ;}
	public Image getimage() {return image ;}
	public Clip getMusic() { return music ;}

	public String[][] getType() {return Type ;}
	public Object[] getgroundType() {return groundType ;}
	public int getCollectibleLevel() {return CollectibleLevel ;}
	public int[] getConnections() {return Connections ;}	
	public int[] getCollectibleCounter() {return CollectibleCounter ;}	
	public int[] getCollectibleDelay() {return CollectibleDelay ;}
	public List<MapElements> getMapElem() {return mapElems ;}
	//public ArrayList<CreatureTypes> getCreatureTypes() {return creatureTypes ;}
	public List<NPCs> getNPCs() {return npcs ;}
	public List<Building> getBuildings() {return buildings ;}
	public Continents getContinentName(Player player)
	{ 
		return Continents.getAll()[Continent + 1] ;
	}
	
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
	
 	public void InitializeGroundTypes(int SkyHeight, Dimension screenDim)
 	{	
		//float Skyratio = SkyHeight / (float) screenDim[1] ;
		//float MinX = (float)(0.1), MinY = (float)(Skyratio + 0.1) ; 
		//float RangeX = (float)(0.8), RangeY = (float)(1 - MinY) ;
		if (Continent == 0)
		{
			if (Name.equals("City of archers"))
			{
				groundType = new Object[(screenDim.height - SkyHeight) * (screenDim.width * 1 / 5)] ;
				for (int j = SkyHeight ; j <= screenDim.height - 1 ; j += 1)
				{
					for (int k = (screenDim.width * 4 / 5) ; k <= screenDim.width - 1 ; k += 1)
					{
						groundType[(j - SkyHeight) * (screenDim.width * 1 / 5) + k - (screenDim.width * 4 / 5)] = new Object[] {"water", new Point(j, k)} ;
					}
				}
			}
			if (Name.equals("City of thieves"))
			{
				groundType = new Object[76] ;
				for (int j = 3 ; j <= 21 ; j += 1)
				{
					groundType[j - 3] = new Object[] {"water", new Point(j, 10)} ;	// outer wall (horizontal top)
				}
				for (int k = 10 ; k <= 20 ; k += 1)
				{
					groundType[k + 9] = new Object[] {"water", new Point(3, k)} ;	// outer wall (vertical left edge)
					groundType[k + 20] = new Object[] {"water", new Point(21, k)} ;	// outer wall (vertical left edge)
				}
				for (int j = 8 ; j <= 26 ; j += 1)
				{
					groundType[j + 33] = new Object[] {"water", new Point(j, 23)} ;	// inner wall (horizontal bottom)
				}
				for (int k = 16 ; k <= 23 ; k += 1)
				{
					groundType[k + 44] = new Object[] {"water", new Point(8, k)} ;	// inner wall (vertical left edge)
					groundType[k + 52] = new Object[] {"water", new Point(26, k)} ;	// inner wall (vertical right edge)
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

 	public List<Collider> allColliders()
 	{
 		List<Collider> allColliders = new ArrayList<>() ;
 		if (buildings != null)
 		{
 			for (Building building : buildings)
 	 		{
 	 			allColliders.addAll(building.getColliders()) ;
 	 		}
 		}
 		
 		if (npcs != null)
 		{
 			for (NPCs npc : npcs)
 	 		{
 	 			allColliders.addAll(npc.getColliders()) ;
 	 		}
 		}
 		
 		if (mapElems != null)
 		{
 			for (MapElements mapElem : mapElems)
 	 		{
 	 			allColliders.addAll(mapElem.getColliders()) ;
 	 		}
 			
 		}
 		
 		return allColliders ;
 	}
 	
 	public void display(Point pos, Scale scale, DrawingOnPanel DP)
 	{
 		DP.DrawImage(image, pos, scale, Align.bottomLeft) ;
 	}
 	
 	public void display(DrawingOnPanel DP)
 	{
 		DP.DrawImage(image, Game.getScreen().getMapCenter(), Align.center) ;
 		if (Name.equals("City of the archers"))
 		{
 	 		DP.DrawImage(beachGif, new Point(Game.getScreen().getSize().width, 96), Align.topRight) ;
 		}
 	}
 	
 	public void displayElements(DrawingOnPanel DP)
 	{ 		
 		for (int me = 0 ; me <= mapElems.size() - 1 ; me += 1)
		{
			mapElems.get(me).DrawImage(DrawingOnPanel.stdAngle, DP) ;
		}
 	}
 	
 	public void displayBuildings(Point playerPos, DrawingOnPanel DP)
 	{
		if (buildings == null) { return ;}
		
		for (Building building : buildings)
		{
			building.display(playerPos, DrawingOnPanel.stdAngle, new Scale(1, 1), DP) ;
		}
 	}
	
	public void displayNPCs(DrawingOnPanel DP)
	{
		if (npcs == null) { return ;}
		
		npcs.forEach(npc -> npc.display(DP));
	}
	 	
	public boolean IsACity()
	{
		return (this instanceof CityMap) ;
	}
	public boolean isAField()
	{
		return (this instanceof FieldMap) ;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameMap other = (GameMap) obj;
		return Arrays.equals(CollectibleCounter, other.CollectibleCounter)
				&& Arrays.equals(CollectibleDelay, other.CollectibleDelay) && CollectibleLevel == other.CollectibleLevel
				&& Arrays.equals(Connections, other.Connections) && Continent == other.Continent
				&& Objects.equals(Name, other.Name) && Arrays.deepEquals(Type, other.Type)
				&& Objects.equals(buildings, other.buildings) && Arrays.deepEquals(groundType, other.groundType)
				&& Objects.equals(image, other.image) && Objects.equals(mapElems, other.mapElems)
				&& Objects.equals(music, other.music) && Objects.equals(npcs, other.npcs);
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(CollectibleCounter);
		result = prime * result + Arrays.hashCode(CollectibleDelay);
		result = prime * result + Arrays.hashCode(Connections);
		result = prime * result + Arrays.deepHashCode(Type);
		result = prime * result + Arrays.deepHashCode(groundType);
		result = prime * result + Objects.hash(CollectibleLevel, Continent, Name, buildings, image, mapElems, music, npcs);
		return result;
	}

	// \*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/
	
	public String groundTypeAtPoint(Point pos)
	{
		if (groundType != null)
		{
			for (int i = 0; i <= groundType.length - 1; i += 1)
			{
				Object[] o = (Object[]) groundType[i] ;
				String type = (String) o[0] ;	// ground type at the point
				Point p = (Point) o[1] ;		// point on the map
				if (p.x == pos.x & p.y == pos.y)
				{
					return type ;
				}			
			}
		}
		
		return null ;
	}

	public boolean groundIsWalkable(Point pos, Elements superElem)
	{
		// TODO ground is walkable
//		if (groundType == null) { return true ; }
//
//		Point point = new Point(Pos) ;
//		for (int i = 0; i <= groundType.length - 1; i += 1)
//		{
//			Object[] o = (Object[]) groundType[i] ;
//			if (point.equals((Point) o[1]))
//			{
//				String gtype = (String) o[0] ;
//				if ((gtype.equals("water") & !SuperElem.equals(Elements.water)) |
//						((gtype.equals("tree") |
//						(gtype.equals("rock"))) & !SuperElem.equals(Elements.air)))
//				{
//					return false ;
//				}
//			}
//		}
		
		if (superElem != null) { if (superElem.equals(Elements.air)) { return true ;}}
		
 		List<Collider> allColliders = allColliders() ;
 		for (Collider collider : allColliders)
 		{
 			if (pos.equals(collider.getPos())) { return false ;}
 		}
		
		return true ;
	}

	@Override
	public String toString()
	{
		return "GameMap [Name=" + Name + ", Continent=" + Continent + ", Connections=" + Arrays.toString(Connections)
				+ ", image=" + image + ", music=" + music + ", Type=" + Arrays.toString(Type) + ", groundType="
				+ Arrays.toString(groundType) + ", CollectibleLevel=" + CollectibleLevel + ", CollectibleCounter="
				+ Arrays.toString(CollectibleCounter) + ", CollectibleDelay=" + Arrays.toString(CollectibleDelay)
				+ ", mapElem=" + mapElems + ", buildings=" + buildings + ", npcs=" + npcs + "]";
	}

	/*public void CreateCollectible(int MapID, int CollectibleID)
	{
		Screen screen = Game.getScreen() ;
		float MinX = (float) (0.1), MinY = (float) ((float) screen.getSize().y / (screen.getBorders()[1] - screen.getBorders()[3]) + 0.1) ; 
    	float RangeX = (float) (0.8), RangeY = (float) (1 - MinY) ;
    	if (MapID == 13 | MapID == 17)
		{ 
			RangeX = (float) (0.6) ;
		}
    	//String[] CollectibleNames = new String[] {"Berry", "Herb", "Wood", "Metal"} ;
    	//int step = 1 ;
    	if (-1 < CollectibleID)
    	{ 	
    		//int[] randomPos = new int[] {Utg.RandomCoord1D(screen.getDimensions()[0], MinX, RangeX, 1), Utg.RandomCoord1D(screen.getDimensions()[1], MinY, RangeY, 1)} ;
			//invisible_wall = Utg.AddElem(invisible_wall, new Point(randomPos[0], randomPos[1])) ;	// collider
    	}
	}*/
	/*public void CreateCollectibles()
	{
		if (id <= 59 & id != 36 & id != 39)
		{
			for (int i = 0 ; i <= CollectibleCounter.length - 1 ; i += 1)	// Collectible ID 0: herb, 1: wood, 2: metal
			{
				if (CollectibleCounter[i] % CollectibleDelay[i] == 0)
				{	
					CollectibleCounter[i] = 0 ;
					CreateCollectible(id, i) ;
				}	
			}
		}
	}*/
	/*public Creatures[] creaturesinmap(Creatures[] creature)
	{
		Creatures[] creaturesinmap = null ;
		for (int i = 0 ; i <= creatures.length - 1 ; i += 1)
		{
			creaturesinmap = Utg.AddElem(creaturesinmap, creatures[i]) ;
		}

		return creaturesinmap ;
	}*/

}
