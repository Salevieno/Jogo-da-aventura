package Main ;

import java.awt.Color ;
import java.awt.Graphics ;
import java.awt.Image ;
import java.awt.Point;
import java.awt.Toolkit ;
import java.awt.event.ActionEvent ;
import java.awt.event.ActionListener ;
import java.awt.event.KeyAdapter ;
import java.awt.event.KeyEvent ;
import java.awt.event.MouseEvent ;
import java.awt.event.MouseListener ;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon ;
import javax.swing.JPanel ;
import javax.swing.Timer ;

import Actions.Battle ;
import GameComponents.BuildingType;
import GameComponents.Buildings ;
import GameComponents.Icon ;
import GameComponents.NPCType;
import GameComponents.NPCs ;
import GameComponents.Projectiles ;
import GameComponents.Quests ;
import Graphics.Animations ;
import Graphics.DrawFunctions ;
import Graphics.DrawPrimitives ;
import Items.Alchemy;
import Items.Arrow;
import Items.Equip;
import Items.Fab;
import Items.Food;
import Items.Forge;
import Items.GeneralItem;
import Items.PetItem;
import Items.Potion;
import Items.QuestItem;
import LiveBeings.BattleAttributes;
import LiveBeings.CreatureTypes;
import LiveBeings.Creature;
import LiveBeings.MovingAnimations;
import LiveBeings.PersonalAttributes;
import LiveBeings.Pet;
import LiveBeings.Player;
import LiveBeings.Spells;
import Maps.CityMap;
import Maps.FieldMap;
import Maps.Maps;
import Screen.Screen;
import Screen.Sky;
import Utilities.Scale;
import Utilities.Size;
import Utilities.UtilG;
import Utilities.UtilS;
import Windows.FabWindow;

public class Game extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L ;
	
	private Timer timer ;		// Main timer of the game
    private Point mousePos ;
	private JPanel mainpanel = this ;

	public static String CSVPath, ImagesPath, MusicPath ;
	public static String MainFontName ;
	public static Color[] ColorPalette ;	
	public static int DayDuration ;
    public static float[] DifficultMult ;
	
	private boolean OpeningIsOn, LoadingGameIsOn, InitializationIsOn, CustomizationIsOn, TutorialIsOn ;
    private boolean RunGame ;
	private String GameLanguage ;

	private Music music ;
	private DrawFunctions DF ;
	private Icon[] SideBarIcons, plusSignIcon ;
	private Player player ;
	private Pet pet ;
	private Creature[] creature ;
	private Projectiles[] proj ;	

	private static Screen screen ;
	private static Sky sky ;
	private static Opening opening ;
	private static Loading loading ;
	private static CreatureTypes[] creatureTypes ;
	private static CityMap[] cityMaps;
	private static FieldMap[] fieldMaps;
	private static Maps[] allMaps ;
	private static BuildingType[] buildingTypes ;
	private static NPCType[] NPCTypes ;
	//private static NPCs[] allNPCs ;
	private static Quests[] allQuests ;
	private static Battle bat ;
	private static Animations ani ;
	
	public Game(int[] WinDim) 
	{         	
		FirstInitialization(WinDim) ;
		timer.start() ;	// Game will start checking for keyboard events and go to the method paintComponent every "timer" miliseconds
		addMouseListener(new MouseEventDemo()) ;
		addKeyListener(new TAdapter()) ;
		setFocusable(true) ;
	}
	
	public static Screen getScreen(){return screen ;}
	public static Sky getSky(){return sky ;}
	public static CreatureTypes[] getCreatureTypes(){return creatureTypes ;}
	public static BuildingType[] getBuildingTypes(){return buildingTypes ;}
	public static Maps[] getMaps(){return allMaps ;}
	//public static NPCs[] getNPCs(){return allNPCs ;}
	public static Quests[] getQuests(){return allQuests ;}	
	
    public void FirstInitialization(int[] WinDim)
    {
    	// "Scheherazade Bold"
		timer = new Timer(10, this) ;	// timer of the game, first number = delay
    	screen = new Screen(new Size(WinDim[0] - 40 - 15, WinDim[1] - 39), null) ;
    	screen.setCenter() ;
		CSVPath = ".\\csv files\\" ;
		ImagesPath = ".\\images\\" ;
		MusicPath = ".\\music\\" ;
		MainFontName = "Comics" ;
		ColorPalette = UtilS.ReadColorPalette(new ImageIcon(ImagesPath + "ColorPalette.png").getImage(), "Normal") ;    	
    	ani = new Animations() ;
		opening = new Opening() ;
		GameLanguage = "P" ;
		
    	OpeningIsOn = true ; 

    	player = InitializePlayer("", 0, "", "") ;
    	music = new Music() ;
    }
    
    public NPCType[] InitializeNPCTypes(String Language)
    {
    	// Doutor, Equips Seller, Items Seller, Smuggle Seller, Banker, Alchemist, Woodcrafter, Forger, Crafter, Elemental, Saver, Master, Quest 0, Citizen 0, Citizen 1, Citizen 2, Citizen 3
		ArrayList<String[]> input = UtilG.ReadcsvFile(CSVPath + "NPCTypes.csv") ;
		NPCType[] npcType = new NPCType[input.size()] ;
		//int[] ColorID = new int[] {6, 10, 20, 16, 3, 23, 27, 0, 25, 7, 5, 0, 26, 18, 15, 14, 21} ;
		for (int i = 0 ; i <= npcType.length - 1 ; i += 1)
		{
			//int ID = Integer.parseInt(input.get(i)[0]) ;
			//int step = 1;
			//Point Pos = new Point((int)(UtilG.Round(Float.parseFloat(input.get(i)[3]) * screenSize.x / step, 0) * step), (int)(UtilG.Round(Float.parseFloat(input.get(i)[4]) * screenSize.y / step, 0) * step)) ;
			//int Map = Integer.parseInt(input.get(i)[5]) ;
			//String PosReltoBuilding = input.get(i)[6] ;
			String Name = "" ;
			String Info = "" ;
			//if (Language.equals("P"))
			//{
			//	Name = input.get(i)[0] ;
			//	Info = input.get(i)[3] ;
			//}
			//else if (Language.equals("E"))
			//{
				Name = input.get(i)[1] ;
				Info = input.get(i)[4] ;
			//}
			Color color = ColorPalette[0] ;
			Image image = new ImageIcon(ImagesPath + "NPC_" + Name + ".png").getImage() ;
			/*if (i < 17*4)
			{
				color = ColorPalette[ColorID[i % 17]] ;
			}
			else
			{
				color = ColorPalette[(int)((ColorPalette.length - 1)*Math.random())] ;
			}*/
			/*if (!Name.equals("Master") & !Name.equals("Mestre") & i != 85 & i != 87)
			{
				image = UtilG.ChangeImageColor(new ImageIcon(ImagesPath + "NPC.png").getImage(), new float[] {0, 0, 1, 1}, color, ColorPalette[6]) ;
			}
			if (Name.equals("Master") | Name.equals("Mestre"))
			{
				image = UtilG.ChangeImageColor(new ImageIcon(ImagesPath + "PlayerFront.png").getImage(), new float[] {0, 0, 1, 1}, color, ColorPalette[6]) ;
			}*/
			/*if (i == 85)
			{
				image = new ImageIcon(ImagesPath + "NPCHole.png").getImage() ;
			}
			if (i == 87)
			{
				image = new ImageIcon(ImagesPath + "NPCHoleInCave.png").getImage() ;
			}*/
			//String[] options = new String[] {"Sim", "N�o1545145415"} ;
			npcType[i] = new NPCType(Name, Info, color, image) ;
		}
    	
		return npcType ;
    }
    
    public BuildingType[] InitializeBuildingTypes()
    {
		ArrayList<String[]> BuildingsInput = UtilG.ReadcsvFile(CSVPath + "Buildings.csv") ;
		BuildingType[] buildings = new BuildingType[BuildingsInput.size()] ;
		for (int type = 0 ; type <= buildings.length - 1 ; type += 1)
		{
			String name = BuildingsInput.get(type)[1] ;
			Image outsideImage = new ImageIcon(ImagesPath + "Building" + type + "_" + name + ".png").getImage() ;
			Image insideImage = new ImageIcon(ImagesPath + "Building" + type + "_" + name + "Inside.png").getImage() ;
			Image[] OrnamentImages = new Image[] {new ImageIcon(ImagesPath + "Building" + name + "Ornament.png").getImage()} ;
			buildings[type] = new BuildingType(name, outsideImage, insideImage, OrnamentImages) ;
		}
		
		return buildings ;
    }
    
    public CreatureTypes[] InitializeCreatureTypes(String Language, float DiffMult)
    {
		ArrayList<String[]> Input = UtilG.ReadcsvFile(CSVPath + "CreatureTypes.csv") ;
    	CreatureTypes.setNumberOfCreatureTypes(Input.size());
		CreatureTypes[] creatureTypes = new CreatureTypes[CreatureTypes.getNumberOfCreatureTypes()] ;
		String Name = "" ;
		Color[] color = new Color[creatureTypes.length] ;
		for (int ct = 0 ; ct <= creatureTypes.length - 1 ; ct += 1)
		{			
			if (Language.equals("P"))
			{
				Name = Input.get(ct)[1] ;
			}
			else if (Language.equals("E"))
			{
				Name = Input.get(ct)[2] ;
			}
			int colorid = (int)((Creature.getskinColor().length - 1)*Math.random()) ;
			color[ct] = Creature.getskinColor()[colorid] ;
			if (270 < ct & ct <= 299)	// Ocean creatures
			{
				color[ct] = ColorPalette[5] ;
			}
			
			MovingAnimations moveAni = new MovingAnimations(new ImageIcon(ImagesPath + "creature2_idle.gif").getImage(),
					new ImageIcon(ImagesPath + "creature2_movingup.gif").getImage(),
					new ImageIcon(ImagesPath + "creature2_movingdown.gif").getImage(),
					new ImageIcon(ImagesPath + "creature2_movingleft.gif").getImage(),
					new ImageIcon(ImagesPath + "creature2_movingright.gif").getImage()) ;
			
			int Level = Integer.parseInt(Input.get(ct)[3]) ;			
			String dir = Player.MoveKeys[0] ;
			String Thought = "Exist" ;
			Size Size = new Size(moveAni.idleGif.getWidth(null), moveAni.idleGif.getHeight(null)) ;
			float[] Life = new float[] {Float.parseFloat(Input.get(ct)[5]) * DiffMult, Float.parseFloat(Input.get(ct)[5]) * DiffMult} ;
			float[] Mp = new float[] {Float.parseFloat(Input.get(ct)[6]) * DiffMult, Float.parseFloat(Input.get(ct)[6]) * DiffMult} ;
			float Range = Float.parseFloat(Input.get(ct)[7]) * DiffMult ;
			int Step = Integer.parseInt(Input.get(ct)[48]) ;
			int[] Exp = new int[] {Integer.parseInt(Input.get(ct)[36])} ;
			int[] Satiation = new int[] {100, 100, 1} ;
			int[] Thirst = new int[] {100, 100, 0} ;		
			String[] Elem = new String[] {Input.get(ct)[35]} ;
			int[][] Actions = new int[][] {{0, Integer.parseInt(Input.get(ct)[49]), 0}, {0, Integer.parseInt(Input.get(ct)[50]), 0}} ;
			String currentAction = "" ;
			int countmove = 0 ;
			PersonalAttributes PA = new PersonalAttributes(Name, Level, ct, 0, null, null, dir, Thought, Size, Life, Mp, Range, Step, Exp, Satiation, Thirst, Elem, Actions, currentAction, countmove) ;

			float[] PhyAtk = new float[] {Float.parseFloat(Input.get(ct)[8]) * DiffMult, 0, 0} ;
			float[] MagAtk = new float[] {Float.parseFloat(Input.get(ct)[9]) * DiffMult, 0, 0} ;
			float[] PhyDef = new float[] {Float.parseFloat(Input.get(ct)[10]) * DiffMult, 0, 0} ;
			float[] MagDef = new float[] {Float.parseFloat(Input.get(ct)[11]) * DiffMult, 0, 0} ;
			float[] Dex = new float[] {Float.parseFloat(Input.get(ct)[12]) * DiffMult, 0, 0} ;
			float[] Agi = new float[] {Float.parseFloat(Input.get(ct)[13]) * DiffMult, 0, 0} ;
			float[] Crit = new float[] {Float.parseFloat(Input.get(ct)[14]) * DiffMult, 0, Float.parseFloat(Input.get(ct)[15]) * DiffMult, 0} ;
			float[] Stun = new float[] {Float.parseFloat(Input.get(ct)[16]) * DiffMult, 0, Float.parseFloat(Input.get(ct)[17]) * DiffMult, 0, Float.parseFloat(Input.get(ct)[18]) * DiffMult} ;
			float[] Block = new float[] {Float.parseFloat(Input.get(ct)[19]) * DiffMult, 0, Float.parseFloat(Input.get(ct)[20]) * DiffMult, 0, Float.parseFloat(Input.get(ct)[21]) * DiffMult} ;
			float[] Blood = new float[] {Float.parseFloat(Input.get(ct)[22]) * DiffMult, 0, Float.parseFloat(Input.get(ct)[23]) * DiffMult, 0, Float.parseFloat(Input.get(ct)[24]) * DiffMult, 0, Float.parseFloat(Input.get(ct)[25]) * DiffMult, 0, Float.parseFloat(Input.get(ct)[26]) * DiffMult} ;
			float[] Poison = new float[] {Float.parseFloat(Input.get(ct)[27]) * DiffMult, 0, Float.parseFloat(Input.get(ct)[28]) * DiffMult, 0, Float.parseFloat(Input.get(ct)[29]) * DiffMult, 0, Float.parseFloat(Input.get(ct)[30]) * DiffMult, 0, Float.parseFloat(Input.get(ct)[31]) * DiffMult} ;
			float[] Silence = new float[] {Float.parseFloat(Input.get(ct)[32]) * DiffMult, 0, Float.parseFloat(Input.get(ct)[33]) * DiffMult, 0, Float.parseFloat(Input.get(ct)[34]) * DiffMult} ;
			int[] Status = new int[8] ;
			int[] SpecialStatus = new int[5] ;
			int[][] BattleActions = new int[][] {{0, Integer.parseInt(Input.get(ct)[51]), 0}} ;
			BattleAttributes BA = new BattleAttributes(PhyAtk, MagAtk, PhyDef, MagDef, Dex, Agi, Crit, Stun, Block, Blood, Poison, Silence, Status, SpecialStatus, BattleActions) ;
						
			Spells[] spell = new Spells[] {
											new Spells("Spell " + Integer.parseInt(Input.get(ct)[4]), 5, 10, "Offensive", null, 100, -1, null, null, null, null, null, null, null, null, null, null, null, null, null, "n", new String[] {"Spell info"})
										} ;
			int[] Bag = new int[] {Integer.parseInt(Input.get(ct)[37]), Integer.parseInt(Input.get(ct)[38]), Integer.parseInt(Input.get(ct)[39]), Integer.parseInt(Input.get(ct)[40]), Integer.parseInt(Input.get(ct)[41]), Integer.parseInt(Input.get(ct)[42]), Integer.parseInt(Input.get(ct)[43]), Integer.parseInt(Input.get(ct)[44]), Integer.parseInt(Input.get(ct)[45]), Integer.parseInt(Input.get(ct)[46])} ;
			int Gold = Integer.parseInt(Input.get(ct)[47]) ;
			int[] StatusCounter = new int[8] ;
			creatureTypes[ct] = new CreatureTypes(ct, moveAni, PA, BA, spell, Bag, Gold, color[ct], StatusCounter) ;	
		}
		return creatureTypes ;
    }
    
    public CityMap[] InitializeCityMaps()
    {
		ArrayList<String[]> input = UtilG.ReadcsvFile(CSVPath + "MapsCity.csv") ;
		CityMap[] cityMap = new CityMap[input.size()] ;
		
		for (int id = 0 ; id <= cityMap.length - 1 ; id += 1)
		{
			String name = input.get(id)[0] ;
			int continent = Integer.parseInt(input.get(id)[1]) ;
			int[] connections = new int[] {
											Integer.parseInt(input.get(id)[2]),
											Integer.parseInt(input.get(id)[3]),
											Integer.parseInt(input.get(id)[4]),
											Integer.parseInt(input.get(id)[5]),
											Integer.parseInt(input.get(id)[6]),
											Integer.parseInt(input.get(id)[7]),
											Integer.parseInt(input.get(id)[8]),
											Integer.parseInt(input.get(id)[9])
											} ;
			Image image = new ImageIcon(ImagesPath + "Map" + String.valueOf(id) + ".png").getImage() ;
			ArrayList<Buildings> buildings = new ArrayList<>() ;
			for (int i = 0; i <= 6 - 1; i += 1)
			{
				String buildingName = input.get(id)[10 + 3 * i] ;
				BuildingType buildingType = null ;
				for (int j = 0 ; j <= buildingTypes.length - 1 ; j += 1)
				{
					if (buildingName.equals(buildingTypes[j].getName()))
					{
						buildingType = buildingTypes[j] ;
					}
				}
				
				int buildingPosX = (int) (screen.getSize().x * Double.parseDouble(input.get(id)[11 + 3 * i])) ;
				int buildingPosY = (int) (screen.getSize().y * Double.parseDouble(input.get(id)[12 + 3 * i])) ;
				Point buildingPos = new Point(buildingPosX, buildingPosY) ;
				ArrayList<NPCs> buildingNPCs = null ;
				// TODO adicionar npcs nas buildings
				/*for (int j = 0; j <= 6 - 1; j += 1)
				{
					buildingNPCs.add(new NPCs(id, type, Pos, options)) ;
				}*/
				
				buildings.add(new Buildings(buildingType, buildingPos, buildingNPCs)) ;
			}

			ArrayList<NPCs> npcs = new ArrayList<NPCs>() ;
			for (int i = 0; i <= 17 - 1; i += 1)
			{
				String NPCName = input.get(id)[28 + 3 * i] ;
				NPCType NPCType = null ;
				for (int j = 0 ; j <= NPCTypes.length - 1 ; j += 1)
				{
					if (NPCName.equals(NPCTypes[j].getName()))
					{
						NPCType = NPCTypes[j] ;
					}
				}
				
				int NPCPosX = (int) (screen.getSize().x * Double.parseDouble(input.get(id)[29 + 3 * i])) ;
				int NPCPosY = (int) (screen.getSize().y * Double.parseDouble(input.get(id)[30 + 3 * i])) ;
				Point NPCPos = new Point(NPCPosX, NPCPosY) ;
				npcs.add(new NPCs(i, NPCType, NPCPos, new String[] {"Sim", "N�o"})) ;
			}
			
			cityMap[id] = new CityMap(name, continent, connections, image, buildings, npcs);
		}
		
		return cityMap ;
    }
    
    public FieldMap[] InitializeFieldMaps()
    {
    	ArrayList<String[]> input = UtilG.ReadcsvFile(CSVPath + "MapsField.csv") ;
    	FieldMap[] fieldMap = new FieldMap[input.size()] ;
		
		for (int id = 0 ; id <= fieldMap.length - 1 ; id += 1)
		{
			String name = input.get(id)[0] ;
			int continent = Integer.parseInt(input.get(id)[1]) ;
			int collectibleLevel = Integer.parseInt(input.get(id)[2]) ;
			int berryDelay = Integer.parseInt(input.get(id)[3]) ;
			int herbDelay = Integer.parseInt(input.get(id)[4]) ;
			int woodDelay = Integer.parseInt(input.get(id)[5]) ;
			int metalDelay = Integer.parseInt(input.get(id)[6]) ;
			int[] Connections = new int[] {
											Integer.parseInt(input.get(id)[7]),
											Integer.parseInt(input.get(id)[8]),
											Integer.parseInt(input.get(id)[9]),
											Integer.parseInt(input.get(id)[10]),
											Integer.parseInt(input.get(id)[11]),
											Integer.parseInt(input.get(id)[12]),
											Integer.parseInt(input.get(id)[13]),
											Integer.parseInt(input.get(id)[14])
											} ;
			int[] creatureIDs = new int[] {
											Integer.parseInt(input.get(id)[15]),
											Integer.parseInt(input.get(id)[16]),
											Integer.parseInt(input.get(id)[17]),
											Integer.parseInt(input.get(id)[18]),
											Integer.parseInt(input.get(id)[19]),
											Integer.parseInt(input.get(id)[20]),
											Integer.parseInt(input.get(id)[21]),
											Integer.parseInt(input.get(id)[22]),
											Integer.parseInt(input.get(id)[23]),
											Integer.parseInt(input.get(id)[24])
											} ;
			Image image = new ImageIcon(ImagesPath + "Map" + String.valueOf(id + cityMaps.length) + ".png").getImage() ;
			fieldMap[id] = new FieldMap(name, continent, Connections, image, collectibleLevel, new int[] {berryDelay, herbDelay, woodDelay, metalDelay}, creatureIDs) ;
		}
		
		return fieldMap ;
    }
                
    public Player InitializePlayer(String Name, int Job, String GameLanguage, String Sex)
    {
		return new Player(Name, GameLanguage, Sex, Job) ;
    }
    
    public Pet InitializePet()
    {
    	Pet pet = new Pet((int) (4 * Math.random())) ;
		return pet ;
    }
    
    /*public Creatures[] InitializeCreatures(CreatureTypes[] creatureTypes, Size screenSize, FieldMap[] fieldMap)
    {    	
    	int NumberOfCreatures = 0 ;
    	
    	// calculate number of creatures based on the creatures in each map
    	for (int map = 0 ; map <= fieldMap.length - 1 ; map += 1)
		{
			if (fieldMap[map].getCreatures() != null)	// Map has creatures
			{
				NumberOfCreatures += fieldMap[map].getCreatures().size() ;
			}
		}
    	
    	// create the creatures in each map
		Creatures[] creature = new Creatures[NumberOfCreatures] ;
    	int creatureID = 0 ;
    	for (int map = 0 ; map <= fieldMap.length - 1 ; map += 1)
		{
			if (fieldMap[map].getCreatures() != null)	// Map has creatures
			{
				for (int j = 0 ; j <= fieldMap[map].getCreatures().size() - 1 ; j += 1)
				{
					CreatureTypes cType = fieldMap[map].getCreatures().get(j).getType() ;					
					creature[creatureID] = new Creatures(cType) ;					
					
					creatureID += 1;
				}
			}
		}
    	
		return creature ;
    }*/
    
    public Quests[] InitializeQuests(String Language, int PlayerJob)
    {
		ArrayList<String[]> input = UtilG.ReadcsvFile(CSVPath + "Quests.csv") ;
		Quests[] quest = new Quests[input.size()] ;
		for (int i = 0 ; i <= quest.length - 1 ; i += 1)
		{
			int id = Integer.parseInt(input.get(i)[0]) ;
			quest[i] = new Quests(Integer.parseInt(input.get(i)[0])) ;
			quest[i].Initialize(input.get(id), Language, id, PlayerJob) ;
		}
		
		return quest ;
    }
   
    public Icon[] InitializeIcons(Size screenSize)
    {
		/* Icons' position */
		Image IconOptions = new ImageIcon(ImagesPath + "Icon_settings.png").getImage() ;
		Image IconBag = new ImageIcon(ImagesPath + "Icon1_Bag.png").getImage() ;
		Image IconQuest = new ImageIcon(ImagesPath + "Icon2_Quest.png").getImage() ;
		Image IconMap = new ImageIcon(ImagesPath + "Icon3_Map.png").getImage() ;
		Image IconBook = new ImageIcon(ImagesPath + "Icon4_Book.png").getImage() ;
    	Image IconTent = new ImageIcon(ImagesPath + "Icon5_Tent.png").getImage() ;
    	Image PlayerImage = new ImageIcon(ImagesPath + "Player.png").getImage() ;
    	Image PetImage = new ImageIcon(ImagesPath + "PetType" + 0 + ".png").getImage() ;
		Image IconSkillsTree = new ImageIcon(ImagesPath + "Icon8_SkillsTree.png").getImage() ;
		Image IconSelectedOptions = new ImageIcon(ImagesPath + "Icon_settingsSelected.png").getImage() ;
		Image IconSelectedBag = new ImageIcon(ImagesPath + "Icon1_BagSelected.png").getImage() ;
		Image IconSelectedQuest = new ImageIcon(ImagesPath + "Icon2_QuestSelected.png").getImage() ;
		Image IconSelectedMap = new ImageIcon(ImagesPath + "Icon3_MapSelected.png").getImage() ;
		Image IconSelectedBook = new ImageIcon(ImagesPath + "Icon4_BookSelected.png").getImage() ;
    	Image IconSelectedTent = new ImageIcon(ImagesPath + "Icon5_TentSelected.png").getImage() ;
    	Image PlayerSelectedImage = new ImageIcon(ImagesPath + "Player.png").getImage() ;
    	Image PetSelectedImage = new ImageIcon(ImagesPath + "PetType" + 0 + ".png").getImage() ;
		Image IconSelectedSkillsTree = new ImageIcon(ImagesPath + "Icon8_SelectedSkillsTree.png").getImage() ;
		Image[] SideBarIconsImages = new Image[] {IconOptions, IconBag, IconQuest, IconMap, IconBook, IconTent, PlayerImage, PetImage, IconSkillsTree} ;
		Image[] SideBarIconsSelectedImages = new Image[] {IconSelectedOptions, IconSelectedBag, IconSelectedQuest, IconSelectedMap, IconSelectedBook, IconSelectedTent, PlayerSelectedImage, PetSelectedImage, IconSelectedSkillsTree} ;

		/* Side bar icons */
		SideBarIcons = new Icon[8] ;
		String[] SBname = new String[] {"Options", "Bag", "Quest", "Map", "Book", "Tent", "Player", "Pet"} ;
		Point[] SBpos = new Point[SBname.length] ;
		int sy = 20 ;
		SBpos[0] = new Point(screenSize.x + 20, screenSize.y - 230) ;
    	for (int i = 1 ; i <= 6 - 1 ; i += 1)
    	{
    		if (SideBarIconsImages[i - 1] != null)
    		{
        		SBpos[i] = new Point(SBpos[0].x, SBpos[i - 1].y - SideBarIconsImages[i - 1].getHeight(null) / 2 - sy) ;
    		}
    		else
    		{

        		SBpos[i] = new Point(SBpos[0].x, SBpos[i - 1].y - sy) ;
    		}
    	}
    	SBpos[6] = new Point(SBpos[0].x, SBpos[5].y - SideBarIconsImages[5].getHeight(null)/2 - sy) ;
    	SBpos[7] = new Point(SBpos[0].x, SBpos[6].y - (int)PlayerImage.getHeight(mainpanel) - SideBarIconsImages[4].getHeight(null)/2 - 20/2 - sy) ;
     	for (int i = 0 ; i <= SideBarIcons.length - 1 ; i += 1)
    	{
     		SideBarIcons[i] = new Icon(i, SBname[i], SBpos[i], null, SideBarIconsImages[i], SideBarIconsSelectedImages[i]) ;
    	}

		/* Plus sign icons */
     	plusSignIcon = new Icon[8] ;
     	Point[] PlusSignPos = new Point[] {new Point(175, 206), new Point(175, 225), new Point(175, 450),
     			new Point(175, 475), new Point(175, 500), new Point(175, 525), new Point(175, 550), new Point(175, 575)} ;
		Image PlusSignImage = new ImageIcon(ImagesPath + "PlusSign.png").getImage() ;
		Image SelectedPlusSignImage = new ImageIcon(ImagesPath + "ShiningPlusSign.png").getImage() ;
		for (int i = 0 ; i <= PlusSignPos.length - 1 ; i += 1)
    	{
    		plusSignIcon[i] = new Icon(i + SBname.length, "Plus sign", PlusSignPos[i], null, PlusSignImage, SelectedPlusSignImage) ;
    	}
		
    	return plusSignIcon ;
    }
 	
    public void MainInitialization()
	{
 		DayDuration = 120000 ;
    	sky = new Sky() ;
    	screen.setBorders(new int[] {0, sky.height, screen.getSize().x, screen.getSize().y});
    	screen.setMapCenter() ;    			
    	Maps.InitializeStaticVars(ImagesPath) ;
		//allNPCs = InitializeNPCTypes(GameLanguage, screen.getSize()) ;
		buildingTypes = InitializeBuildingTypes() ;
		creatureTypes = InitializeCreatureTypes(GameLanguage, 1) ;
		cityMaps = InitializeCityMaps() ;
		fieldMaps = InitializeFieldMaps() ;
		allMaps = new Maps[cityMaps.length + fieldMaps.length] ;
		System.arraycopy(cityMaps, 0, allMaps, 0, cityMaps.length) ;
		System.arraycopy(fieldMaps, 0, allMaps, cityMaps.length, fieldMaps.length) ;
		DifficultMult = new float[] {(float) 0.5, (float) 0.7, (float) 1.0} ;
 		//player = InitializePlayer(PlayerInitialName, PlayerInitialJob, GameLanguage, PlayerInitialSex) ;
		//pet = InitializePet() ;
		//creature = InitializeCreatures(creatureTypes, screen.getSize(), fieldMaps) ;
		for (int map = 0 ; map <= allMaps.length - 1 ; map += 1)
		{
			//allMaps[map].InitializeNPCsInMap(allNPCs) ;
		}
		allQuests = InitializeQuests(GameLanguage, player.getJob()) ;
		plusSignIcon = InitializeIcons(screen.getSize()) ;

		// Initialize classes
    	bat = new Battle(player.getSpell(), pet.getSpells(), music.getSoundEffect(), new int[] {player.getBA().getBattleActions()[0][1]/2, pet.getBA().getBattleActions()[0][1]/2}, ani) ;
	}
  	
	public void Opening()
	{
		int openingStatus = opening.Run(player.allText.get("* Novo jogo *"), player.getPA().getCurrentAction(), mousePos, music, ani, DF) ;
		if (openingStatus == 1)
		{
			LoadingGameIsOn = true ;
			InitializationIsOn = false ;
			OpeningIsOn = false ;
		}
		else if (openingStatus == 2)
		{
			OpeningIsOn = false ;
		}
	}
	
	public void Tutorial(Player player, int[] meet)
	{
		/*DrawPrimitives DP = DF.getDrawPrimitives() ;
		int TextCat = AllTextCat[3] ;
		int L = (int)(screen.getSize().x), H = (int)(0.12*screen.getSize().y) ;
		Point Pos = new Point(0, H) ;
		Font font = new Font("SansSerif", Font.BOLD, 15) ;
		int TextL = 95 ;
		int sy = Utg.TextH(font.getSize()) + 2 ;
		if (TutorialStep <= 13)
		{
			DP.DrawRoundRect(Pos, "BotLeft", new Size(L, H), 1, ColorPalette[7], ColorPalette[8], true) ;
			DP.DrawText(new Point(Pos.x + 5, Pos.y + 5 - Utg.TextH(font.getSize())), "BotLeft", DrawPrimitives.OverallAngle, AllText[TextCat][1], font, ColorPalette[5]) ;
			DP.DrawFitText(new Point(Pos.x + 5, Pos.y - H + 5 + Utg.TextH(font.getSize())), sy, "BotLeft", AllText[TextCat][TutorialStep + 2], font, TextL, ColorPalette[5]) ;					
		}
		if (player.action.equals("Space"))
		{
			TutorialStep += 1 ;
		}
		if (TutorialStep == 0)
		{
			if (player.action.equals(Player.MoveKeys[0]) | player.action.equals(Player.MoveKeys[2]) | player.action.equals(Player.MoveKeys[3]) | player.action.equals(Player.MoveKeys[1]))
			{
				TutorialStep += 1 ;
			}
		}
		else if (TutorialStep == 1)
		{
			if (player.action.equals("Enter"))
			{
				TutorialStep += 1 ;
			}
		}
		else if (TutorialStep == 2)
		{
			if (player.action.equals("Enter"))
			{
				TutorialStep += 1 ;
			}
		}
		else if (TutorialStep == 3)
		{
			//if (player.action.equals(Player.ActionKeys[4]))
			//{
			//	player.getBag()[0] += 1 ;
			//	int ItemID = 0 ;		// obtained item: hp potion lv 0
			//	Ani.SetAniVars(0, new Object[] {100, items, new int[] {ItemID}}) ;
			//	Ani.StartAni(0) ;
			//	TutorialStep += 1 ;
			//}
		}
		else if (TutorialStep == 4)
		{
			//if (player.getBag()[0] == 0)
			//{
			//	int ItemID = Items.BagIDs[5] + (Items.BagIDs[6] - Items.BagIDs[5]) / 5 * player.getJob() ;		// obtained item: first equip
			//	player.getBag()[ItemID] += 1 ;
			//	Ani.SetAniVars(0, new Object[] {100, items, new int[] {ItemID}}) ;
			//	Ani.StartAni(0) ;
			//	TutorialStep += 1 ;
			//}
		}
		else if (TutorialStep == 5)
		{
			if (0 < player.getEquips()[0])
			{
				TutorialStep += 1 ;
			}
		}
		else if (TutorialStep == 6)
		{
			if (player.action.equals(Player.ActionKeys[5]))
			{
				TutorialStep += 1 ;
			}
		}
		else if (TutorialStep == 7)
		{
			if (player.action.equals(Player.ActionKeys[5]))
			{
				TutorialStep += 1 ;
			}
		}
		else if (TutorialStep == 8)
		{
			//DF.DrawNPCsIntro(player, npc, new float[] {1, 1}, player.currentMap(maps).NPCsInMap) ;
			if (player.action.equals("Enter"))
			{
				TutorialStep += 1 ;
			}
		}
		else if (TutorialStep == 9)
		{
			//Ani.SetAniVars(17, new Object[] {10, player.getMap(), CrazyArrowImage}) ;
			//Ani.StartAni(17) ;
			if (!player.getMap().IsACity())
			{
				TutorialStep += 1 ;
			}
		}
		else if (TutorialStep == 10)
		{
			if (player.action.equals("Enter"))
			{
				TutorialStep += 1 ;
			}
		}
		else if (TutorialStep == 11)
		{
			//if (0 < player.getBag()[Items.BagIDs[0]] | 0 < player.getBag()[Items.BagIDs[0] + 1] | 0 < player.getBag()[Items.BagIDs[0] + 2] | 0 < player.getBag()[Items.BagIDs[3]] | player.action.equals("Space"))
			//{
			//	player.getBag()[Items.BagIDs[3]] += 5 ;
			//	Ani.SetAniVars(0, new Object[] {100, items, new int[] {Items.BagIDs[3]}}) ;
			//	Ani.StartAni(0) ;
			//	TutorialStep += 1 ;
			//}
		}
		else if (TutorialStep == 12)
		{
			if (player.action.equals("Enter"))
			{
				TutorialStep += 1 ;
			}
		}
		else if (TutorialStep == 13)
		{
			if (player.action.equals("Enter"))
			{
				TutorialStep += 1 ;
			}
		}
		else if (14 <= TutorialStep & TutorialStep < 17)
		{
			if (meet[0] == 1 & meet[1] == 2)	// First time meeting the items seller
			{
				if (npc[meet[1]].Firstcontact)
				{
					player.getGold()[0] += 100 ;
				}
				npc[meet[1]].Firstcontact = false ;
			}
			else if (meet[0] == 1 & meet[1] == 7)	// First time meeting the forger
			{
				npc[meet[1]].Firstcontact = false ;
			}
			else if (meet[0] == 1 & meet[1] == 11)	// First time meeting the master
			{
				if (npc[meet[1]].Firstcontact)
				{
					player.addSkillPoint(1) ;
				}
				npc[meet[1]].Firstcontact = false ;
			}
		}
		else if (TutorialStep == 17)
		{
			TutorialIsOn = false ;
		}*/
	}
		
	public void Customization()
	{
		//player.Customization(Player.ActionKeys, Player.MoveKeys) ;
		//player.setSize(new int[] {player.getPersonalAtt()()[0].getWidth(mainpanel), player.getPersonalAtt().getimage()[0].getHeight(mainpanel)}) ;
		//int SelectedOption = player.SelectedOption ;
		//int[] CurrentColorValues = new int[] {player.getColor().getRed(), player.getColor().getGreen(), player.getColor().getBlue()} ;
		//DF.DrawCustomizationMenu(player, SelectedOption, CurrentColorValues) ;	
		/*if (player.getPA().getCurrentAction().equals("Enter"))
		{
			CustomizationIsOn = false ;
			RunGame = true ;
			if (player.getSettings().getMusicIsOn())
			{
				music.SwitchMusic(music.getMusicIntro(), music.getMusicClip()[Maps.MusicID[player.getMap().getid()]]) ;
			}
		}*/
	}
		
	public void IncrementCounters()
	{
		sky.incDayTime();
		player.IncActionCounters() ;
		player.SupSpellCounters(creature, player.opponent) ;
		if (pet != null)
		{
			pet.IncActionCounters() ;
		}
		if (!player.getMap().IsACity())	// player is out of the cities
		{
			FieldMap fm = (FieldMap) player.getMap() ;
			for (int c = 0 ; c <= fm.getCreatures().size() - 1 ; c += 1)
			{
				fm.getCreatures().get(c).IncActionCounters() ;
			}
		}
		if (player.isInBattle())
		{
			player.IncBattleActionCounters() ;
			pet.IncBattleActionCounters() ;
			/*for (int c = 0 ; c <= player.currentMap(maps).getCreatureIDs().length - 1 ; c += 1)
			{
				int ID = player.currentMap(maps).getCreatureIDs()[c] ;
				creature[ID].IncBattleActionCounters() ;
			}*/
			player.opponent.IncBattleActionCounters() ;
		}
		for (int i = 0; i <= fieldMaps.length - 1; i += 1)
		{
			fieldMaps[i].IncCollectiblesCounter() ;
		}
	}
	
	public void ActivateCounters()
	{	
		player.ActivateActionCounters(ani.SomeAnimationIsActive()) ;
		if (pet != null)
		{
			if (0 < pet.getLife()[0])
			{
				pet.ActivateActionCounters(ani.SomeAnimationIsActive()) ;
			}
		}
		if (!player.getMap().IsACity())
		{
			FieldMap fm = (FieldMap) player.getMap() ;
			for (int i = 0 ; i <= fm.getCreatures().size() - 1 ; ++i)
			{
				if (!ani.isActive(12) & !ani.isActive(13) & !ani.isActive(14) & !ani.isActive(18))
				{
					fm.getCreatures().get(i).ActivateActionCounters() ;
				}
			}
		}
		for (int i = 0; i <= fieldMaps.length - 1; i += 1)
		{
			fieldMaps[i].ActivateCollectiblesCounter() ;
		}
	}
	
	public void KonamiCode()
	{
		//OpeningScreenImages[2] = ElementalCircle ;
		ColorPalette = UtilS.ReadColorPalette(new ImageIcon(ImagesPath + "ColorPalette.png").getImage(), "Konami") ;
		if (sky.dayTime % 1200 <= 300)
		{
			DrawPrimitives.OverallAngle += 0.04 ;
		}
		else if (sky.dayTime % 1200 <= 900)
		{
			DrawPrimitives.OverallAngle -= 0.04 ;
		}
		else
		{
			DrawPrimitives.OverallAngle += 0.04 ;
		}
	}
	
	public void RunGame(DrawFunctions DF)
	{	
		// increment and activate counters
		IncrementCounters() ;
		ActivateCounters() ;
		
		
		// check for the Konami code
		if (UtilS.KonamiCodeActivated(player.getCombo()))
		{
			KonamiCode() ;
		}
		
		
		// draw the map (cities, forest, etc.)
		DF.DrawFullMap(player.getPos(), pet, player.getMap(), sky, mousePos) ;
		player.DrawSideBar(pet, mousePos, SideBarIcons, DF.getDrawPrimitives()) ;
		
		
		// creatures act
		if (!player.getMap().IsACity())
		{
			FieldMap fm = (FieldMap) player.getMap() ;
			ArrayList<Creature> creaturesInMap = fm.getCreatures() ;
			for (int c = 0 ; c <= creaturesInMap.size() - 1 ; c += 1)
			{				
				//System.out.println(creaturesInMap.get(c).getPos()) ;
				Creature creature = creaturesInMap.get(c) ;
				creature.act(player.getPos(), player.getMap()) ;
				creature.display(creature.getPos(), new Scale(1, 1), DF.getDrawPrimitives()) ;
			}
		}
		
		
		// player acts
		player.act(pet, allMaps, mousePos, SideBarIcons, ani, DF) ;
		if (player.actionIsAMove() | 0 < player.getPA().getCountmove())	// countmove becomes greater than 0 when the player moves, then starts to increase by 1 and returns to 0 when it reaches 20
		{
			player.move(pet, music.getMusicClip(), ani) ;
		}
		player.DrawAttributes(0, DF.getDrawPrimitives()) ;
		player.display(player.getPos(), new Scale(1, 1), player.getDir(), player.getSettings().getShowPlayerRange(), DF.getDrawPrimitives()) ;
		if (player.weaponIsEquipped())	// if the player is equipped with a weapon
		{
			player.DrawWeapon(player.getPos(), new float[] {1, 1}, DF.getDrawPrimitives()) ;
		}
		
		
		// pet acts
		if (pet != null)
		{
			if (0 < pet.getLife()[0])		// if the pet is alive
			{
				pet.setCombo(UtilS.RecordCombo(pet.getCombo(), pet.action, 1)) ;
				pet.Move(player, allMaps) ;
				if (player.isInBattle())
				{
					pet.action = pet.Action(Player.ActionKeys) ;
				}
				//pet.display(player.getPos(), new float[] {1, 1}, DF.getDrawPrimitives()) ;
				//pet.drawAttributes(0, DF.getDrawPrimitives()) ;
			}
		}
		
		
		// find the closest creature to the player
		if (!player.getMap().IsACity())
		{
			player.closestCreature = UtilS.ClosestCreatureInRange(player, creature, allMaps) ;
		}
		
		
		// check if the player met something
		if (!player.isInBattle())
		{
			player.meet(creature, player.getMap().getNPCs(), DF.getDrawPrimitives(), ani) ;	// meet[0] is the encounter and meet[1] is its id
			/*if (meet[0] == 0 & 0 <= meet[1])	// meet with creature
			{
				FieldMap fm = (FieldMap) player.getMap() ;
				ArrayList<Creatures> creaturesInMap = fm.getCreatures() ;
				Creatures opponent = creaturesInMap.get(meet[1]) ;
				opponent.setFollow(true) ;
				player.getPA().setThought("Fighting") ;	
				player.opponent = opponent ;
				player.AddCreatureToBestiary(meet[1]) ;
			}
			if (meet[0] == 1 & 0 <= meet[1])	// meet with npc
			{
				player.getMap().getNPCs().get(meet[1]).Contact(player, pet, creature, allMaps, allQuests, mousePos, TutorialIsOn, ani, DF) ;				
			}
			if (meet[0] == 2 & 0 <= meet[1])	// meet with collectibles
			{
                //player.Collect(meet[1], DF.getDrawPrimitives(), ani) ;
			}
			if (meet[0] == 3 & 0 <= meet[1])	// meet with chest
			{
				player.TreasureChestReward(meet[1], allMaps, ani) ;
			}*/
		}
		
		
		// if the player is in battle, run battle
		if (player.isInBattle() & !ani.isActive(12) & !ani.isActive(13) & !ani.isActive(14) & !ani.isActive(16))	// only enter battle if the animations for win (12), level up (13), pet level up (14), and pterodactile (16) are off
		{
			bat.RunBattle(player, pet, player.opponent, allQuests, mousePos, DF) ;
		}
		
		
		// level up the player and the pet if they should
		if (!ani.isActive(12) & player.ShouldLevelUP())
		{
			player.LevelUp(ani) ;
		}
		if (pet != null)
		{
			if (!ani.isActive(13) & pet.ShouldLevelUP())
			{
				pet.LevelUp(ani) ;
			}
		}
		
		
		// show the active player windows
		player.ShowWindows(pet, creature, creatureTypes, allQuests, allMaps, plusSignIcon, bat, music.getMusicClip(), mousePos, DF) ;
		
		
		// if tutorial is on, draw tutorial animations
		if (TutorialIsOn)
		{
			DF.TutorialAnimations() ;
		}		
		
		
		// move the active projectiles and check if they collide with something
		if (proj != null)
		{
			ArrayList<Creature> creaturesInMap = new ArrayList<>() ;
			if (!player.getMap().IsACity())
			{
				FieldMap fm = (FieldMap) player.getMap() ;
				creaturesInMap = fm.getCreatures() ;
			}
			for (int p = 0 ; p <= proj.length - 1 ; p += 1)
			{
				proj[p].go(player, creaturesInMap, pet, DF.getDrawPrimitives()) ;
			}
			if (proj[0].collidedwith(player, creaturesInMap, pet) != - 3)	// if the projectile has hit something
			{
				proj[0] = null ;	// destroy the projectile
			}
		}
	}
	
	public void battleSimulation()
	{
		// Battle simulation
    	/*GameLanguage = "P" ;
		AllText = Utg.ReadTextFile(GameLanguage) ;
		AllTextCat = Uts.FindAllTextCat(AllText, GameLanguage) ;
    	PlayerName = "" ;
    	PlayerJob = 1 ;
    	PlayerSex = "N" ;
    	MainInitialization() ;
    	OpeningIsOn = false ;
    	RunGame = true ;
		//player.PrintAllAttributes();
		player.CreatureInBattle = 85;
		//creatureTypes[creature[player.CreatureInBattle].getType()].printAtt() ;
		player.setPos(creature[player.CreatureInBattle].getPos()) ;
		player.setPos(new int[] {player.getPos()[0] + 20, player.getPos()[1] + 15});
		//player.getBA().setStun(new float[] {1, 0, 0, 0, 500});
		player.getEquips()[0] = 301 ;
		player.getEquips()[1] = 302 ;
		player.getEquips()[2] = 303 ;
		player.setCurrentAction("Fighting");*/
	}
	
	public void testingInitialization()
	{
		// Quick start
		
		try {Potion.Initialize() ;} catch (IOException e) {e.printStackTrace() ;} // Initialize the list with all the potions
		try {Alchemy.Initialize() ;} catch (IOException e) {e.printStackTrace() ;} // Initialize the list with all the alchemy items
		try {Forge.Initialize() ;} catch (IOException e) {e.printStackTrace() ;} // Initialize the list with all the forge items
		try {PetItem.Initialize() ;} catch (IOException e) {e.printStackTrace() ;} // Initialize the list with all the pet items
		try {Food.Initialize() ;} catch (IOException e) {e.printStackTrace() ;} // Initialize the list with all the food items
		try {Arrow.Initialize() ;} catch (IOException e) {e.printStackTrace() ;} // Initialize the list with all the arrow items
		try {Equip.Initialize() ;} catch (IOException e) {e.printStackTrace() ;} // Initialize the list with all the equip items
		try {GeneralItem.Initialize() ;} catch (IOException e) {e.printStackTrace() ;} // Initialize the list with all the general items
		try {Fab.Initialize() ;} catch (IOException e) {e.printStackTrace() ;} // Initialize the list with all the fabrication items
		try {QuestItem.Initialize() ;} catch (IOException e) {e.printStackTrace() ;} // Initialize the list with all the quest items	
    	
    	// Minimum initialization
    	DayDuration = 120000 ;
    	sky = new Sky() ;
    	screen.setBorders(new int[] {0, sky.height, screen.getSize().x, screen.getSize().y});
    	screen.setMapCenter() ;
		creatureTypes = InitializeCreatureTypes(GameLanguage, 1) ;
		plusSignIcon = InitializeIcons(screen.getSize()) ;
		//allNPCs = InitializeNPCTypes(GameLanguage, screen.getSize()) ;
		NPCTypes = InitializeNPCTypes(GameLanguage) ;
		buildingTypes = InitializeBuildingTypes() ;
		
		//allBuildings = InitializeBuildings(screen.getSize()) ;
    	//allMaps = InitializeMaps(screen, allCreatureTypes, sky) ;
		cityMaps = InitializeCityMaps() ;
		fieldMaps = InitializeFieldMaps() ;
		allMaps = new Maps[cityMaps.length + fieldMaps.length] ;
		System.arraycopy(cityMaps, 0, allMaps, 0, cityMaps.length) ;
		System.arraycopy(fieldMaps, 0, allMaps, cityMaps.length, fieldMaps.length) ;
    	/*for (int map = 0 ; map <= allMaps.length - 1 ; map += 1)
		{
			allMaps[map].InitializeNPCsInMap(allNPCs) ;
			allMaps[map].InitializeBuildingsInMap(allBuildings) ;
		}*/
    	pet = InitializePet() ;
    	pet.getPA().setLife(new float[] {100, 100, 0});
    	pet.getPA().setPos(player.getPos());
    	bat = new Battle(player.getSpell(), pet.getSpells(), music.getSoundEffect(), new int[] {player.getBA().getBattleActions()[0][1]/2, pet.getBA().getBattleActions()[0][1]/2}, ani) ;
    	
    	
    	
    	player.setMap(fieldMaps[9]) ;
    	player.setPos(new Point(60, screen.getSize().y / 2)) ;
    	OpeningIsOn = false ;
    	InitializationIsOn = false ;
    	LoadingGameIsOn = false ;
    	CustomizationIsOn = false ;
    	player.getSettings().setMusicIsOn(false) ;
    	for (int i = 0; i <= 2 - 1; i += 1)
    	{
    		player.getBag().Add(Potion.getAll()[i], 1) ;
    	}
    	FabWindow fabWindow = new FabWindow() ;
    	fabWindow.LoadCraftingRecipes() ;
    	
    	FieldMap fm = (FieldMap) player.getMap() ;
    	ArrayList<Creature> fmCreatures = fm.getCreatures();
    	for (int i = 0; i <= fieldMaps.length - 1 ; i += 1)
    	{
        	player.bestiary.addDiscoveredCreature(fieldMaps[i].getCreatures().get(0)) ;
    	}
    	/*System.out.println(player.getBag().getPotions()) ;
    	player.getBag().Add(Potion.getAll()[0], 4) ;
    	player.getBag().Add(Alchemy.getAll()[0], 1) ;
    	System.out.println(player.getBag().getPotions()) ;
    	player.getBag().Remove(Potion.getAll()[0], 3) ;
    	System.out.println(player.getBag().getPotions()) ;
    	player.getBag().useItem(player, Potion.getAll()[0]) ;
    	System.out.println(player.getBag().getPotions()) ;*/
    	if (player.getSettings().getMusicIsOn())
		{
			//music.PlayMusic(music.getMusicClip()[Maps.MusicID[player.getMap().getid()]]) ;
		}  	
	}
	
	public void testing()
	{
		//player.setPos(new Point(503, 221));
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
        super.paintComponent(g) ;
		mousePos = UtilG.GetMousePos(mainpanel) ;
        DF = new DrawFunctions(g) ;
        DF.InitializeVariables(ImagesPath) ;
		
    	if (OpeningIsOn)
		{
    		opening.Animation(DF) ;
    		testingInitialization() ;
        	RunGame = true ;  
		/*} else if (LoadingScreenIsOn)
		{
			LoadingScreenIsOn = false ;
			InitializationIsOn = true ;*/
		} 
		else if (InitializationIsOn)
		{
			loading.displayText(DF) ;
			MainInitialization() ;
			InitializationIsOn = false ;
			CustomizationIsOn = true ;
		} 
		else if (LoadingGameIsOn)
		{
			DayDuration = 120000 ;
	    	sky = new Sky() ;
	    	screen.setBorders(new int[] {0, sky.height, screen.getSize().x, screen.getSize().y});
	    	screen.setMapCenter() ;    	
			//Quests.CalcNumberOfQuests(CSVPath) ;		
	    	Maps.InitializeStaticVars(ImagesPath) ;
	    	
			loading.Run(DF) ;
		}
		else if (CustomizationIsOn)
		{
			Customization() ;
		}
		else if (RunGame)
		{
			RunGame(DF) ;
			ani.RunAnimation(DF) ;	// run all the active animations
			testing() ;
			player.setCurrentAction("") ;
		}
        Toolkit.getDefaultToolkit().sync() ;
        g.dispose() ;  
    }
		
	class TAdapter extends KeyAdapter 
	{	
	    @Override
	    public void keyPressed(KeyEvent e) 
	    {
	        int key = e.getKeyCode() ;
            if (key == KeyEvent.VK_LEFT | key == KeyEvent.VK_UP | key == KeyEvent.VK_DOWN | key == KeyEvent.VK_RIGHT) 
            {
            	if (CustomizationIsOn | player.getActions()[0][2] == 1)	// If the player can act
    			{
            		player.setCurrentAction(KeyEvent.getKeyText(key)) ;
                }
            }
            else if (key == KeyEvent.VK_ENTER | key == KeyEvent.VK_ESCAPE | key == KeyEvent.VK_BACK_SPACE | key == KeyEvent.VK_F1 | key == KeyEvent.VK_F2 | key == KeyEvent.VK_F3 | key == KeyEvent.VK_F4 | key == KeyEvent.VK_F5 | key == KeyEvent.VK_F6 | key == KeyEvent.VK_F7 | key == KeyEvent.VK_F8 | key == KeyEvent.VK_F9 | key == KeyEvent.VK_F10 | key == KeyEvent.VK_F11 | key == KeyEvent.VK_F12 | key == KeyEvent.VK_A | key == KeyEvent.VK_B | key == KeyEvent.VK_C | key == KeyEvent.VK_D | key == KeyEvent.VK_E | key == KeyEvent.VK_F | key == KeyEvent.VK_G | key == KeyEvent.VK_H | key == KeyEvent.VK_I | key == KeyEvent.VK_J | key == KeyEvent.VK_K | key == KeyEvent.VK_L | key == KeyEvent.VK_M | key == KeyEvent.VK_N | key == KeyEvent.VK_O | key == KeyEvent.VK_P | key == KeyEvent.VK_Q | key == KeyEvent.VK_R | key == KeyEvent.VK_S | key == KeyEvent.VK_T | key == KeyEvent.VK_U | key == KeyEvent.VK_V | key == KeyEvent.VK_W | key == KeyEvent.VK_X | key == KeyEvent.VK_Y | key == KeyEvent.VK_Z | key == KeyEvent.VK_0 | key == KeyEvent.VK_1 | key == KeyEvent.VK_2 | key == KeyEvent.VK_3 | key == KeyEvent.VK_4 | key == KeyEvent.VK_5 | key == KeyEvent.VK_6 | key == KeyEvent.VK_7 | key == KeyEvent.VK_8 | key == KeyEvent.VK_9) 
            {
        		player.setCurrentAction(KeyEvent.getKeyText(key)) ;
            }
            else if (key == KeyEvent.VK_SPACE)
            {
        		player.setCurrentAction(KeyEvent.getKeyText(key)) ;
            }
            else if (key == KeyEvent.VK_NUMPAD0 | key == KeyEvent.VK_NUMPAD1 | key == KeyEvent.VK_NUMPAD2 | key == KeyEvent.VK_NUMPAD3 | key == KeyEvent.VK_NUMPAD4 | key == KeyEvent.VK_NUMPAD5 | key == KeyEvent.VK_NUMPAD6 | key == KeyEvent.VK_NUMPAD7 | key == KeyEvent.VK_NUMPAD8 | key == KeyEvent.VK_NUMPAD9)
            {
        		player.setCurrentAction(String.valueOf(key - 96)) ;
            }
            else if (key == KeyEvent.VK_PAUSE) 
            {
                if (timer.isRunning()) 
                {
                   timer.stop() ;
                }
                else 
                {
                   timer.start() ;
                }
            }
            //repaint() ;
	    }
	
	    @Override
	    public void keyReleased(KeyEvent e) 
	    {
    		player.setCurrentAction("") ;
	    }
	}

	public class MouseEventDemo implements MouseListener 
	{
		@Override
		public void mouseClicked(MouseEvent evt)
		{
			if (evt.getButton() == 1)	// Left click
			{
        		player.setCurrentAction("MouseLeftClick") ;
				/*for (int i = 0 ; i <= OPbuttons.length - 1 ; i += 1)
				{
					if (OPbuttons[i].ishovered(mousePos) & OPbuttons[i].isActive)
					{
						OPbuttons[i].startaction() ;
						if (OPbuttons[i].getid() == 0 | OPbuttons[i].getid() == 1)
						{
							GameLanguage = (String) OPbuttons[i].startaction() ;
							// TODO update text
							//AllText = UtilG.ReadTextFile(GameLanguage) ;
							//AllTextCat = UtilS.FindAllTextCat(AllText, GameLanguage) ;
						}
						if (2 <= OPbuttons[i].getid() & OPbuttons[i].getid() <= 13)
						{
			        		player.setCurrentAction((String) OPbuttons[i].startaction()) ;
						}
					}
				}*/
			}
			if (evt.getButton() == 3)	// Right click
			{
        		player.setCurrentAction("MouseRightClick") ;
			}
			//repaint() ;
		}

		@Override
		public void mouseEntered(MouseEvent arg0)
		{
			
		}

		@Override
		public void mouseExited(MouseEvent arg0)
		{
			
		}

		@Override
		public void mousePressed(MouseEvent evt)
		{
		}

		@Override
		public void mouseReleased(MouseEvent e) 
		{
    		player.setCurrentAction("") ;
		}		
	}
	
	@Override
    public void actionPerformed(ActionEvent e) 
	{
		repaint() ;
	}

}
