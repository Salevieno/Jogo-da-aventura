package GameComponents ;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image ;
import java.awt.Point;
import java.util.Arrays;

import javax.swing.ImageIcon ;

import Graphics.DrawPrimitives;
import LiveBeings.CreatureTypes;
import LiveBeings.Creatures;
import Main.Game;
import Screen.Screen;
import Utilities.Size;
import Utilities.UtilG;
import Utilities.UtilS;

public class Maps 
{
	private String Name ;
	private int id ;
	private int Continent ;
	private Image image ;
	private MapElements[] MapElem ;		// 0 = free, 1 = wall, 2 = water, 3 = tree, 4 = grass, 5 = rock, 6 = crystal, 7 = stalactite, 8 = volcano, 9 = lava, 10 = ice, 11 = chest, 12 = berry, 13 = herb, 14 = wood, 15 = metal, 16 = invisible wall
	private String[][] Type ;			// 2 = water, 9 = lava, 10 = ice, 12 = berry, 13 = herb, 14 = wood, 15 = metal
	private Object[] groundType ;
	private int CollectibleLevel ;
	private int[] CollectibleCounter ;	// [Berry, herb, wood, metal]
    private int[] CollectibleDelay ;	// [Berry, herb, wood, metal]
	private int[] Connections ;
	private CreatureTypes[] creatureTypes ;
	private Creatures[] creatures ;
	public Buildings[] building ;
	public NPCs[] NPCsInMap ;
	
	public static int[] MusicID = new int[] {0, 1, 2, 3, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 9, 9, 9, 9, 9, 10, 11, 11, 11, 11, 11, 11} ;  	 
	public static Image[] CollectibleImage ;
	public static Image[] GroundImage ;
	public static String[] CollectibleTypes = new String[] {"Berry", "Herb", "Wood", "Metal"} ;
	
	public Maps(String Name, int id, int Continent, Image image, MapElements[] MapElem, int CollectibleLevel, int[] CollectibleDelay, int[] Connections, CreatureTypes[] creatureTypes, Creatures[] creatures)
	{
		this.Name = Name ;
		this.id = id ;
		this.Continent = Continent ;
		this.image = image ;
		this.MapElem = MapElem ;
		this.CollectibleLevel = CollectibleLevel ;
		CollectibleCounter = new int[CollectibleTypes.length] ;
		this.CollectibleDelay = CollectibleDelay ;
		this.Connections = Connections ;
		this.creatureTypes = creatureTypes ;
		this.creatures = creatures ;
		building = null ;
		
	    Image BerryImage = new ImageIcon(UtilG.ReadTextFile("Paths.txt", 3)[1][0] + "Col0_Berry.png").getImage() ;
	    Image HerbImage = new ImageIcon(UtilG.ReadTextFile("Paths.txt", 3)[1][0] + "Col1_Herb.png").getImage() ;
	    Image WoodImage = new ImageIcon(UtilG.ReadTextFile("Paths.txt", 3)[1][0] + "Col2_Wood.png").getImage() ;
	    Image MetalImage = new ImageIcon(UtilG.ReadTextFile("Paths.txt", 3)[1][0] + "Col3_Metal.png").getImage() ;
	    CollectibleImage = new Image[] {BerryImage, HerbImage, WoodImage, MetalImage} ;
	}

	public String getName() {return Name ;}
	public int getid() {return id ;}
	public int getContinent() {return Continent ;}
	public Image getimage() {return image ;}
	public MapElements[] getMapElem() {return MapElem ;}
	public String[][] getType() {return Type ;}
	public Object[] getgroundType() {return groundType ;}
	public int getCollectibleLevel() {return CollectibleLevel ;}
	public int[] getConnections() {return Connections ;}	
	public int[] getCollectibleCounter() {return CollectibleCounter ;}	
	public int[] getCollectibleDelay() {return CollectibleDelay ;}
	public CreatureTypes[] getCreatureTypes() {return creatureTypes ;}
	public Creatures[] getCreatures() {return creatures ;}
	public NPCs[] getNPCs() {return NPCsInMap ;}
	public Buildings[] getBuildings() {return building ;}
	public void setCreatures(Creatures[] newValue) {creatures = newValue ;}
	
	public static void InitializeStaticVars(String ImagesPath)
	{
		Image Water = new ImageIcon(ImagesPath + "MapElem0_Water.png").getImage() ;
		/*Image Wall = new ImageIcon(ImagesPath + "MapElem1_Wall.png").getImage() ;
		Image Berry = new ImageIcon(ImagesPath + "MapElem2_Berry.png").getImage() ;
		Image Herb = new ImageIcon(ImagesPath + "MapElem3_Herb.png").getImage() ;
		Image Wood = new ImageIcon(ImagesPath + "MapElem4_Wood.png").getImage() ;
		Image Metal = new ImageIcon(ImagesPath + "MapElem5_Metal.png").getImage() ;
		Image TreeForest = new ImageIcon(ImagesPath + "MapElem6_TreeForest.png").getImage() ;
		Image PalmTree = new ImageIcon(ImagesPath + "MapElem7_PalmTree.png").getImage() ;
		Image Grass = new ImageIcon(ImagesPath + "MapElem8_Grass.png").getImage() ;
		Image Rock = new ImageIcon(ImagesPath + "MapElem9_Rock.png").getImage() ;
		Image Crystal = new ImageIcon(ImagesPath + "MapElem10_Crystal.png").getImage() ;
		Image Stalactite = new ImageIcon(ImagesPath + "MapElem11_Stalactite.png").getImage() ;
		Image Volcano = new ImageIcon(ImagesPath + "MapElem12_Volcano.png").getImage() ;*/
		Image Lava = new ImageIcon(ImagesPath + "MapElem13_Lava.png").getImage() ;
		Image Ice = new ImageIcon(ImagesPath + "MapElem14_Ice.png").getImage() ;
		//Image Chest = new ImageIcon(ImagesPath + "MapElem15_Chest.png").getImage() ;	
		GroundImage = new Image[] {Water, Lava, Ice} ;
	}
	
 	public void InitializeGroundTypes(int SkyHeight, Size screenDim)
 	{	
		//float Skyratio = SkyHeight / (float) screenDim[1] ;
		//float MinX = (float)(0.1), MinY = (float)(Skyratio + 0.1) ; 
		//float RangeX = (float)(0.8), RangeY = (float)(1 - MinY) ;
		if (Continent == 0)
		{
			if (id == 2)	// archers' city
			{
				groundType = new Object[(screenDim.y - SkyHeight) * (screenDim.x * 1 / 5)] ;
				for (int j = SkyHeight ; j <= screenDim.y - 1 ; j += 1)
				{
					for (int k = (screenDim.x * 4 / 5) ; k <= screenDim.x - 1 ; k += 1)
					{
						groundType[(j - SkyHeight) * (screenDim.x * 1 / 5) + k - (screenDim.x * 4 / 5)] = new Object[] {"water", new Point(j, k)} ;
					}
				}
			}
			if (id == 4)	// thieves' city
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
	
 	public void display(DrawPrimitives DP)
 	{
 		DP.DrawImage(image, Game.getScreen().getMapCenter(), "Center") ;
 	}
 	
 	public void displayElements(DrawPrimitives DP)
 	{
 		if (groundType != null)
		{
			for (int gt = 0 ; gt <= groundType.length - 1 ; gt += 1)
			{
				Object[] o = (Object[]) groundType[gt] ;
				String type = (String) o[0] ;
				Point p = (Point) o[1] ;
				if (type.equals("water"))
				{
					DP.DrawRect(new Point(p.x, p.y), "center", new Size(10, 10), 0, Color.blue, null, false);
				}
			}
		}
		if (MapElem != null)
		{
			for (int me = 0 ; me <= MapElem.length - 1 ; me += 1)
			{
				MapElem[me].DrawImage(DrawPrimitives.OverallAngle, DP) ;
			}
		}
 	}
	
 	public void displayBuildings(Point playerPos, String[] AllText, DrawPrimitives DP)
 	{
 		float overallAngle = DrawPrimitives.OverallAngle ;
 		Color[] colorPalette = Game.ColorPalette ;
		//int[][] NPCsInBuildings = Uts.NPCsInBuildings(npc, building, id, BuildingsInCity) ;
		Font font = new Font("SansSerif", Font.BOLD, 13) ;
		if (building != null)
		{
			for (int b = 0 ; b <= building.length - 1 ; b += 1)
			{
				building[id].display(playerPos, overallAngle, new float[] {1, 1}, DP) ;
			}
			
			//TODO essa é uma função da sign building
			Point SignPos = UtilS.BuildingPos(building, id, "Sign") ;
			if (building[5].playerIsInside(playerPos))
			{			
				int[][] SignTextPos = new int[][] {{SignPos.x - 200, SignPos.y - 150}, {SignPos.x + 50, SignPos.y - 50}, {SignPos.x + 50, SignPos.y - 50}, {SignPos.x + 100, SignPos.y - 50}, {SignPos.x - 540, SignPos.y - 50}} ;
				Point Pos = new Point(SignTextPos[id][0], SignTextPos[id][1]) ;			
				//Size menuSize = new Size((int)(0.25*Utg.TextL(AllText[id + 1], font, G)), (int)(7*Utg.TextH(font.getSize()))) ;
				Size menuSize = new Size(200, 200) ;
				DP.DrawRoundRect(Pos, "TopLeft", menuSize, 3, colorPalette[4], colorPalette[4], true) ;			
				DP.DrawFitText(new Point(Pos.x + 10, Pos.y - (int)(5.5*UtilG.TextH(font.getSize()))), UtilG.TextH(font.getSize()), "BotLeft", AllText[id + 1], font, 35, colorPalette[5]) ;		
			}
		}
 	}
	
	public void displayNPCs(DrawPrimitives DP)
	{
		if (NPCsInMap != null)	// Map has NPCs
		{
			for (int i = 0 ; i <= NPCsInMap.length - 1 ; i += 1)
			{
				if (NPCsInMap[i].getPosRelToBuilding().equals("Outside"))
				{
					NPCsInMap[i].display(DP) ;		
				}
			}
		}
	}
	
	// \*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/
 	
	public boolean IsACity() {if (Name.contains("City")) {return true ;} else {return false ;}}
	public boolean hasCreatures()
	{
		return (getCreatures() != null) ;
	}
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
	public boolean GroundIsWalkable(Point Pos, String SuperElem)
	{
		Point point = new Point(Pos.x, Pos.y) ;
		if (groundType == null)
		{
			return true ;
		}
		else
		{
			for (int i = 0; i <= groundType.length - 1; i += 1)
			{
				Object[] o = (Object[]) groundType[i] ;
				if (point.equals((Point) o[1]))
				{
					String gtype = (String) o[0] ;
					if ((gtype.equals("water") & !SuperElem.equals("w")) | ((gtype.equals("tree") | (gtype.equals("rock"))) & !SuperElem.equals("a")))
					{
						return false ;
					}
				}
			}
		}
		return true ;
	}
	
	public void InitializeNPCsInMap(NPCs[] npc)
    {
		for (int j = 0 ; j <= npc.length - 1 ; j += 1)
		{
			if (npc[j].getMap() == id)
			{
				NPCsInMap = UtilG.AddElem(NPCsInMap, npc[j]) ;
			}
		}
    }
	public void InitializeBuildings(Buildings[] AllBuildings)
	{
		building = UtilS.BuildingsInCity(AllBuildings, id) ;
	}
	
	public void IncCollectiblesCounter()
	{
		for (int m = 0 ; m <= CollectibleCounter.length - 1 ; m += 1)
		{
			if (CollectibleCounter[m] < CollectibleDelay[m])
			{
				CollectibleCounter[m] += 1 ;
			}	
		}
	}
	public void CreateCollectible(int MapID, int CollectibleID)
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
	}
	public void CreateCollectibles()
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
	}
	/*public Creatures[] creaturesinmap(Creatures[] creature)
	{
		Creatures[] creaturesinmap = null ;
		for (int i = 0 ; i <= creatures.length - 1 ; i += 1)
		{
			creaturesinmap = Utg.AddElem(creaturesinmap, creatures[i]) ;
		}

		return creaturesinmap ;
	}*/
	
	
	/* print methods */
	
	public void printProperties()
	{
		for (int i = 0 ; i <= Type.length - 1 ; i += 1)
		{
			for (int j = 0 ; j <= Type[i].length - 1 ; j += 1)
			{
				if (!Type[i][j].equals("free"))
				{
					System.out.println(i + " " + j + " " + Type[i][j]) ;
				}
			}
		}
	}


	@Override
	public String toString() {
		return "Maps [Name=" + Name + ", id=" + id + ", Continent=" + Continent + ", image=" + image + ", MapElem="
				+ Arrays.toString(MapElem) + ", Type=" + Arrays.toString(Type) + ", groundType="
				+ Arrays.toString(groundType) + ", CollectibleLevel=" + CollectibleLevel + ", CollectibleCounter="
				+ Arrays.toString(CollectibleCounter) + ", CollectibleDelay=" + Arrays.toString(CollectibleDelay)
				+ ", Connections=" + Arrays.toString(Connections) + ", creatureTypes=" + Arrays.toString(creatureTypes)
				+ ", creatures=" + Arrays.toString(creatures) + ", building=" + Arrays.toString(building)
				+ ", NPCsInMap=" + Arrays.toString(NPCsInMap) + "]";
	}
}
