package Main ;

import java.awt.Color ;
import java.awt.Font ;
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
import java.io.File ;
import java.io.IOException;
import java.util.Arrays ;

import javax.sound.sampled.Clip ;
import javax.swing.ImageIcon ;
import javax.swing.JPanel ;
import javax.swing.Timer ;

import Actions.Battle ;
import Actions.BattleActions;
import GameComponents.Buildings ;
import GameComponents.Icon ;
import GameComponents.MapElements ;
import GameComponents.Maps ;
import GameComponents.NPCs ;
import GameComponents.PetSpells ;
import GameComponents.Projectiles ;
import GameComponents.Quests ;
import GameComponents.Spells ;
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
import LiveBeings.Creatures;
import LiveBeings.MovingAnimations;
import LiveBeings.PersonalAttributes;
import LiveBeings.Pet;
import LiveBeings.Player;
import Screen.Screen;
import Screen.Sky;
import Screen.SkyComponent;
import Utilities.Size;
import Utilities.UtilG;
import Utilities.UtilS;

public class Game extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L ;
	
	private Timer timer ;		// Main timer of the game
    private Point MousePos ;
	private JPanel mainpanel = this ;

	public static String CSVPath, ImagesPath, MusicPath ;
	public static String MainFontName ;
	public static Color[] ColorPalette ;	
	public static int DayDuration ;
	
	private boolean OpeningIsOn, LoadingGameIsOn, InitializationIsOn, CustomizationIsOn, TutorialIsOn ;
    private boolean RunGame ;
	//private int TutorialStep ;
	private String GameLanguage ;
	
    private String PlayerInitialName, PlayerInitialSex ;
    private int PlayerInitialJob ;
    private String[][] AllText ;
    private int[] AllTextCat ;

	private Sky sky ;
	private Music music ;
	private DrawFunctions DF ;
	private Icon[] OPbuttons, SideBarIcons, plusSignIcon ;
	private Player player ;
	private Pet pet ;
	private Creatures[] creature ;
	//private Items[] items ;
	private Spells[] spells ;	// TODO esses podem ser do player
	private PetSpells[] petspells ;	// TODO esses podem ser do pet
	private Quests[] quest ;
	private Projectiles[] proj ;	

	private static Screen screen ;
	private static Opening opening ;
	private static Loading loading ;
	private static CreatureTypes[] creatureTypes ;
	private static Maps[] maps ;
	private static Buildings[] buildings ;
	private static NPCs[] npc ;
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
	public static CreatureTypes[] getCreatureTypes(){return creatureTypes ;}
	public static Maps[] getMaps(){return maps ;}
	public static Buildings[] getBuildings(){return buildings ;}
	public static NPCs[] getNPCs(){return npc ;}
	
	
    public void FirstInitialization(int[] WinDim)
    {
		timer = new Timer(10, this) ;	// timer of the game, first number = delay
    	screen = new Screen(new Size(WinDim[0] - 40 - 15, WinDim[1] - 39), null) ;
    	screen.setCenter() ;
		CSVPath = ".\\csv files\\" ;
		ImagesPath = ".\\images\\" ;
		MusicPath = ".\\music\\" ;
		MainFontName = "Scheherazade Bold" ;
		ColorPalette = UtilS.ReadColorPalette(new ImageIcon(ImagesPath + "ColorPalette.png").getImage(), "Normal") ;    	
    	ani = new Animations(null, null) ;
		opening = new Opening(ani) ;
		GameLanguage = "P" ;
		
    	OpeningIsOn = true ; 
    	
    	player = InitializePlayer("", 0, "", "") ;
    	music = new Music() ;
    }
    public void InitializeGeneralVariables(Size screenSize)
    {
    	
    	DayDuration = 120000 ;
    	sky = new Sky() ;
    	screen.setBorders(new int[] {0, sky.height, screen.getSize().x, screen.getSize().y});
    	screen.setMapCenter() ;
    	
		Quests.CalcNumberOfQuests(CSVPath) ;		
    	Maps.InitializeStaticVars(ImagesPath) ;
    	BattleActions.InitializeStaticVars(CSVPath) ;
    	
	}
    
    
    public NPCs[] InitializeNPCs(String Language, Size screenSize)
    {
    	int NumberOfNPCs = 149 ;
    	// Doutor, Equips Seller, Items Seller, Smuggle Seller, Banker, Alchemist, Woodcrafter, Forger, Crafter, Elemental, Saver, Master, Quest 0, Citizen 0, Citizen 1, Citizen 2, Citizen 3
		NPCs[] npc = new NPCs[NumberOfNPCs] ;
		String[][] NPCsInput = UtilG.ReadTextFile(CSVPath + "NPCs.csv", NumberOfNPCs) ;
		String Name = "" ;
		String Info = "" ;
		int[] ColorID = new int[] {6, 10, 20, 16, 3, 23, 27, 0, 25, 7, 5, 0, 26, 18, 15, 14, 21} ;
		for (int i = 0 ; i <= NumberOfNPCs - 1 ; i += 1)
		{
			int ID = Integer.parseInt(NPCsInput[i][0]) ;
			int step = 1;
			Point Pos = new Point((int)(UtilG.Round(Float.parseFloat(NPCsInput[i][3]) * screenSize.x / step, 0) * step), (int)(UtilG.Round(Float.parseFloat(NPCsInput[i][4]) * screenSize.y / step, 0) * step)) ;
			Image image = null ;
			int Map = Integer.parseInt(NPCsInput[i][5]) ;
			String PosReltoBuilding = NPCsInput[i][6] ;
			if (Language.equals("P"))
			{
				Name = NPCsInput[i][1] ;
				Info = NPCsInput[i][7] ;
			}
			else if (Language.equals("E"))
			{
				Name = NPCsInput[i][2] ;
				Info = NPCsInput[i][8] ;
			}
			Color color ;
			if (i < 17*4)
			{
				color = ColorPalette[ColorID[i % 17]] ;
			}
			else
			{
				color = ColorPalette[(int)((ColorPalette.length - 1)*Math.random())] ;
			}
			if (!Name.equals("Master") & !Name.equals("Mestre") & i != 85 & i != 87)
			{
				image = UtilG.ChangeImageColor(new ImageIcon(ImagesPath + "NPC.png").getImage(), new float[] {0, 0, 1, 1}, color, ColorPalette[6]) ;
			}
			if (Name.equals("Master") | Name.equals("Mestre"))
			{
				image = UtilG.ChangeImageColor(new ImageIcon(ImagesPath + "PlayerFront.png").getImage(), new float[] {0, 0, 1, 1}, color, ColorPalette[6]) ;
			}
			if (i == 85)
			{
				image = new ImageIcon(ImagesPath + "NPCHole.png").getImage() ;
			}
			if (i == 87)
			{
				image = new ImageIcon(ImagesPath + "NPCHoleInCave.png").getImage() ;
			}
			npc[i] = new NPCs(ID, Name, Pos, image, Map, PosReltoBuilding, Info, color) ;
		}
    	
		return npc ;
    }
    public Buildings[] InitializeBuildings(Size screenSize, NPCs[] npc)
    {
		int NumberOfBuildings = 30 ;
    	Buildings[] buildings = new Buildings[NumberOfBuildings] ;
		String[][] BuildingsInput = UtilG.ReadTextFile(CSVPath + "Buildings.csv", NumberOfBuildings) ;
		int[] ColorID = new int[] {6, 3, 13, 3, 0, 0} ;
		for (int i = 0 ; i <= NumberOfBuildings - 1 ; i += 1)
		{
			int ID = Integer.parseInt(BuildingsInput[i][0]) ;
			String name = BuildingsInput[i][1] ;
			int map = Integer.parseInt(BuildingsInput[i][2]) ;
			Point Pos = new Point((int)(Float.parseFloat(BuildingsInput[i][3])*screenSize.x), (int)(Float.parseFloat(BuildingsInput[i][4])*screenSize.y)) ;
			Image[] Images = new Image[] {new ImageIcon(ImagesPath + "Building.png").getImage(), new ImageIcon(ImagesPath + "Building" + name + "Inside.png").getImage()} ;
			Image[] OrnamentImages = new Image[] {new ImageIcon(ImagesPath + "Building" + name + "Ornament.png").getImage()} ;
			NPCs[] NPCsInBuilding = UtilS.NPCsInBuilding(npc, name, map) ;
			Color color = ColorPalette[ColorID[i % 6]] ;
			buildings[i] = new Buildings(ID, name, map, Pos, Images, OrnamentImages, NPCsInBuilding, color) ;
		}
		return buildings ;
    }
    public CreatureTypes[] InitializeCreatureTypes(String Language, float DiffMult)
    {
    	CreatureTypes.setNumberOfCreatureTypes(CSVPath);
		CreatureTypes[] creatureTypes = new CreatureTypes[CreatureTypes.getNumberOfCreatureTypes()] ;
		String[][] Input = UtilG.ReadTextFile(CSVPath + "CreatureTypes.csv", CreatureTypes.getNumberOfCreatureTypes()) ;
		String Name = "" ;
		Color[] color = new Color[creatureTypes.length] ;
		for (int ct = 0 ; ct <= creatureTypes.length - 1 ; ct += 1)
		{			
			if (Language.equals("P"))
			{
				Name = Input[ct][1] ;
			}
			else if (Language.equals("E"))
			{
				Name = Input[ct][2] ;
			}
			int colorid = (int)((Creatures.getskinColor().length - 1)*Math.random()) ;
			color[ct] = Creatures.getskinColor()[colorid] ;
			if (270 < ct & ct <= 299)	// Ocean creatures
			{
				color[ct] = ColorPalette[5] ;
			}
			
			Image image = new ImageIcon(ImagesPath + "creature.png").getImage() ;
			MovingAnimations animations = new MovingAnimations(new ImageIcon(ImagesPath + "creature_idle.gif").getImage(),
					new ImageIcon(ImagesPath + "creature_movingup.gif").getImage(),
					new ImageIcon(ImagesPath + "creature_movingdown.gif").getImage(),
					new ImageIcon(ImagesPath + "creature_movingleft.gif").getImage(),
					new ImageIcon(ImagesPath + "creature_movingright.gif").getImage()) ;
			
			int Level = Integer.parseInt(Input[ct][3]) ;			
			String dir = Player.MoveKeys[0] ;
			String Thought = "Exist" ;
			int[] Size = new int[] {42, 30} ;
			float[] Life = new float[] {Float.parseFloat(Input[ct][5]) * DiffMult, Float.parseFloat(Input[ct][5]) * DiffMult} ;
			float[] Mp = new float[] {Float.parseFloat(Input[ct][6]) * DiffMult, Float.parseFloat(Input[ct][6]) * DiffMult} ;
			float Range = Float.parseFloat(Input[ct][7]) * DiffMult ;
			int Step = Integer.parseInt(Input[ct][48]) ;
			float[] Exp = new float[] {Integer.parseInt(Input[ct][36])} ;
			float[] Satiation = new float[] {100, 100, 1} ;
			float[] Thirst = new float[] {100, 100, 0} ;		
			String[] Elem = new String[] {Input[ct][35]} ;
			int[][] Actions = new int[][] {{0, Integer.parseInt(Input[ct][49]), 0}, {0, Integer.parseInt(Input[ct][50]), 0}} ;
			String currentAction = "" ;
			int countmove = 0 ;
			PersonalAttributes PA = new PersonalAttributes(Name, Level, ct, 0, null, null, dir, Thought, Size, Life, Mp, Range, Step, Exp, Satiation, Thirst, Elem, Actions, currentAction, countmove) ;

			float[] PhyAtk = new float[] {Float.parseFloat(Input[ct][8]) * DiffMult, 0, 0} ;
			float[] MagAtk = new float[] {Float.parseFloat(Input[ct][9]) * DiffMult, 0, 0} ;
			float[] PhyDef = new float[] {Float.parseFloat(Input[ct][10]) * DiffMult, 0, 0} ;
			float[] MagDef = new float[] {Float.parseFloat(Input[ct][11]) * DiffMult, 0, 0} ;
			float[] Dex = new float[] {Float.parseFloat(Input[ct][12]) * DiffMult, 0, 0} ;
			float[] Agi = new float[] {Float.parseFloat(Input[ct][13]) * DiffMult, 0, 0} ;
			float[] Crit = new float[] {Float.parseFloat(Input[ct][14]) * DiffMult, 0, Float.parseFloat(Input[ct][15]) * DiffMult, 0} ;
			float[] Stun = new float[] {Float.parseFloat(Input[ct][16]) * DiffMult, 0, Float.parseFloat(Input[ct][17]) * DiffMult, 0, Float.parseFloat(Input[ct][18]) * DiffMult} ;
			float[] Block = new float[] {Float.parseFloat(Input[ct][19]) * DiffMult, 0, Float.parseFloat(Input[ct][20]) * DiffMult, 0, Float.parseFloat(Input[ct][21]) * DiffMult} ;
			float[] Blood = new float[] {Float.parseFloat(Input[ct][22]) * DiffMult, 0, Float.parseFloat(Input[ct][23]) * DiffMult, 0, Float.parseFloat(Input[ct][24]) * DiffMult, 0, Float.parseFloat(Input[ct][25]) * DiffMult, 0, Float.parseFloat(Input[ct][26]) * DiffMult} ;
			float[] Poison = new float[] {Float.parseFloat(Input[ct][27]) * DiffMult, 0, Float.parseFloat(Input[ct][28]) * DiffMult, 0, Float.parseFloat(Input[ct][29]) * DiffMult, 0, Float.parseFloat(Input[ct][30]) * DiffMult, 0, Float.parseFloat(Input[ct][31]) * DiffMult} ;
			float[] Silence = new float[] {Float.parseFloat(Input[ct][32]) * DiffMult, 0, Float.parseFloat(Input[ct][33]) * DiffMult, 0, Float.parseFloat(Input[ct][34]) * DiffMult} ;
			int[] Status = new int[8] ;
			int[] SpecialStatus = new int[5] ;
			int[][] BattleActions = new int[][] {{0, Integer.parseInt(Input[ct][51]), 0}} ;
			BattleAttributes BA = new BattleAttributes(PhyAtk, MagAtk, PhyDef, MagDef, Dex, Agi, Crit, Stun, Block, Blood, Poison, Silence, Status, SpecialStatus, BattleActions) ;
						
			int[] Skill = new int[] {Integer.parseInt(Input[ct][4])} ;
			int[] Bag = new int[] {Integer.parseInt(Input[ct][37]), Integer.parseInt(Input[ct][38]), Integer.parseInt(Input[ct][39]), Integer.parseInt(Input[ct][40]), Integer.parseInt(Input[ct][41]), Integer.parseInt(Input[ct][42]), Integer.parseInt(Input[ct][43]), Integer.parseInt(Input[ct][44]), Integer.parseInt(Input[ct][45]), Integer.parseInt(Input[ct][46])} ;
			int Gold = Integer.parseInt(Input[ct][47]) ;
			int[] StatusCounter = new int[8] ;
			image = UtilG.ChangeImageColor(image, new float[] {0, 0, 1, 1}, color[ct], ColorPalette[20]) ;
			image = UtilG.ChangeImageColor(image, new float[] {0, 0, 1, 1}, Creatures.getshadeColor()[colorid], ColorPalette[19]) ;
			creatureTypes[ct] = new CreatureTypes(ct, animations, PA, BA, Skill, Bag, Gold, color[ct], StatusCounter) ;	
		}
		return creatureTypes ;
    }
    public Maps[] InitializeMaps(Screen screen, Buildings[] buildings, CreatureTypes[] creatureTypes, Sky sky)
    {
    	int NumberOfMaps = 67 ;
		Maps[] maps = new Maps[NumberOfMaps] ;
		String[][] MapsInput = UtilG.ReadTextFile(CSVPath + "Maps.csv", NumberOfMaps) ;	
		for (int map = 0 ; map <= NumberOfMaps - 1 ; map += 1)
		{
			String Name = MapsInput[map][1] ;
			int Continent = Integer.parseInt(MapsInput[map][2]) ;
			Image image = new ImageIcon(ImagesPath + "Map" + String.valueOf(map) + ".png").getImage() ;
			int CollectibleLevel = Integer.parseInt(MapsInput[map][3]) ;
			int[] CollectibleDelay = new int[] {Integer.parseInt(MapsInput[map][4]), Integer.parseInt(MapsInput[map][5]), Integer.parseInt(MapsInput[map][6]), Integer.parseInt(MapsInput[map][7])} ;
			int[] Connections = new int[] {Integer.parseInt(MapsInput[map][8]), Integer.parseInt(MapsInput[map][9]), Integer.parseInt(MapsInput[map][10]), Integer.parseInt(MapsInput[map][11]), Integer.parseInt(MapsInput[map][12]), Integer.parseInt(MapsInput[map][13]), Integer.parseInt(MapsInput[map][14]), Integer.parseInt(MapsInput[map][15])} ;
			Creatures[] creatures = null ;
			CreatureTypes[] creatureType = null ;
			for (int c = 0 ; c <= 10 - 1 ; c += 1)
			{
				if (-1 < Integer.parseInt(MapsInput[map][16 + c]))
				{
					creatureType = UtilG.AddVectorElem(creatureType, creatureTypes[Integer.parseInt(MapsInput[map][16 + c])]) ;
				}
			}
			
			// Creating map elements
			MapElements[] MapElem = null ;
			if (!Name.contains("City"))
			{
				MapElem = new MapElements[5] ;
				float MinX = (float) 0.1 ;
				float MinY = (float)((float)(sky.height)/screen.getSize().y + 0.1) ;
				float RangeX = (float) 0.8 ;
				float RangeY = (float)(1 - MinY) ;
				for (int j = 0 ; j <= 4 ; ++j)
				{
					Point randomPos = new Point(UtilG.RandomCoord1D(screen.getSize().x, MinX, RangeX, 1), UtilG.RandomCoord1D(screen.getSize().y, MinY, RangeY, 1)) ;
					MapElem[j] = new MapElements(j, "ForestTree", randomPos, new ImageIcon(ImagesPath + "MapElem6_TreeForest.png").getImage()) ;				
				}	
			}
			
			
			maps[map] = new Maps(Name, map, Continent, image, MapElem, CollectibleLevel, CollectibleDelay, Connections, creatureType, creatures) ;
			maps[map].InitializeGroundTypes(sky.height, screen.getSize()) ;
			
			// Creating collectibles
			if (!maps[map].IsACity() & maps[map].getContinent() != 5 & map != 60)
			{
				for (int i = 0 ; i <= Maps.CollectibleTypes.length - 1 ; i += 1)
				{
					maps[map].CreateCollectible(map, i) ;
				}
			}
		}
		return maps ;
    }
    
        
    public Player InitializePlayer(String Name, int Job, String GameLanguage, String Sex)
    {
    	Player player = new Player(Name, GameLanguage, Sex, Job) ;

		Arrays.fill(player.getQuest(), -1) ;
		Arrays.fill(player.getElemMult(), 1) ;
		Arrays.fill(player.getBattleAtt().getSpecialStatus(), -1) ;
		if (player.getJob() == 2)
		{
			//player.getBag()[Items.BagIDs[4]] = 100 ;
		}
		player.getSpell()[0] = 1 ;
		//player.ResetPosition() ;		
		//player.setPhyAtk(new float[] {100, 0, 0}) ;
		//player.setMagAtk(new float[] {100, 0, 0}) ;
		//player.getBag()[60] = 1 ;
		//player.getBag()[63] = 1 ;
		//player.getBag()[1700] = 1 ;
		//player.setCreaturesDiscovered(new int[] {1, 2, 249}) ;
		//Arrays.fill(player.getQuestSkills(), true) ;
		//player.setMap(27) ;
		//player.setProJob(1) ;
		//player.setSkillPoints(50) ;
		//Arrays.fill(player.getBag(), 3) ;
		//player.setLevel(50) ;
		//Arrays.fill(player.getSkill(), 5) ;
		/*for (int i = 0 ; i <= player.getQuest().length - 1 ; ++i)
		{
			player.getQuest()[i] = i ;
		}
		player.setLife(new float[] {1000, 1000, 1000}) ;
		player.setMp(new float[] {1000, 1000, 1000}) ;*/
		return player ;
    }
    public Pet InitializePet()
    {
    	Pet pet = new Pet((int) (4 * Math.random())) ;
    	pet.getLife()[0] = 0 ;
		return pet ;
    }
    public void InitializeCreaturesInMap()
    {		
		// Setting creatures in map
		for (int map = 0 ; map <= maps.length - 1 ; map += 1)
		{
			if (maps[map].getCreatureTypes() != null)
			{
				Creatures[] creaturesInMap = null ;
				for (int c = 0 ; c <= maps[map].getCreatureTypes().length - 1 ; c += 1)
				{
					CreatureTypes creatureType = maps[map].getCreatureTypes()[c];
					if (-1 < creatureType.getID())
					{
						Creatures creature = new Creatures(creatureType) ;
						creaturesInMap = UtilG.AddVectorElem(creaturesInMap, creature) ;
						maps[map].setCreatures(creaturesInMap) ;
					}
				}
			}
		}
    }
    public Creatures[] InitializeCreatures(CreatureTypes[] creatureTypes, Size screenSize, Maps[] maps, int PlayerStep)
    {    	
    	int NumberOfCreatures = 0 ;
    	
    	// calculate number of creatures based on the creatures in each map
    	for (int map = 0 ; map <= maps.length - 1 ; map += 1)
		{
			if (maps[map].getCreatures() != null)	// Map has creatures
			{
				NumberOfCreatures += maps[map].getCreatures().length ;
			}
		}
    	
    	// create the creatures in each map
		Creatures[] creature = new Creatures[NumberOfCreatures] ;
    	int creatureID = 0 ;
    	for (int map = 0 ; map <= maps.length - 1 ; map += 1)
		{
			if (maps[map].getCreatures() != null)	// Map has creatures
			{
				CreatureTypes cType = null ;
				Maps creatureMap = null ;
				for (int j = 0 ; j <= maps[map].getCreatures().length - 1 ; j += 1)
				{
					cType = creatureTypes[maps[map].getCreatures()[j].getType().getID()] ;
					creatureMap = maps[map] ;
					
					creature[creatureID] = new Creatures(cType) ;
					
					Point Pos = UtilG.RandomPos(screenSize, new float[] {0, (float)(0.2)}, new float[] {1, 1 - (float)(sky.height)/screenSize.y}, new int[] {PlayerStep, PlayerStep}) ;
					if (creatureMap.getid() == 13 | creatureMap.getid() == 17)	// Shore creatures
					{
						Pos = UtilG.RandomPos(new Size((int)(0.8*screenSize.x), screenSize.y), new float[] {0, (float)0.2}, new float[] {1, 1 - (float)(sky.height)/screenSize.y}, new int[] {PlayerStep, PlayerStep}) ;
					}
					creature[creatureID].setPos(Pos) ;
					if (creature[creatureID].getName().equals("Dragão") | creature[creatureID].getName().equals("Dragon"))
					{
						creature[creatureID].getPersonalAtt().setPos(new Point((int)(0.5*screenSize.x), (int)(0.5*screenSize.y))) ;
					}
					
					creatureID += 1;
				}
			}
		}
		return creature ;
    }
    public Spells[] InitializeSpells(String Language)
    {
    	int NumberOfAllSkills = 178 ;
    	int NumberOfSkills = Player.NumberOfSkillsPerJob[player.getJob()] ;
    	int NumberOfAtt = 14 ;
    	int NumberOfBuffs = 12 ;
		Spells[] skills = new Spells[NumberOfSkills] ;
		String[][] SkillsInput = UtilG.ReadTextFile(CSVPath + "Skills.csv", NumberOfAllSkills) ;	
		String[][] SkillsBuffsInput = UtilG.ReadTextFile(CSVPath + "SkillsBuffs.csv", NumberOfAllSkills) ;
		String[][] SkillsNerfsInput = UtilG.ReadTextFile(CSVPath + "SkillsNerfs.csv", NumberOfAllSkills) ;
		float[][][] SkillBuffs = new float[NumberOfSkills][NumberOfAtt][NumberOfBuffs] ;	// [Life, MP, PhyAtk, MagAtk, PhyDef, MagDef, Dex, Agi, Crit, Stun, Block, Blood, Poison, Silence][atk chance %, atk chance, chance, def chance %, def chance, chance, atk %, atk, chance, def %, def, chance]		
		float[][][] SkillNerfs = new float[NumberOfSkills][NumberOfAtt][NumberOfBuffs] ;	// [Life, MP, PhyAtk, MagAtk, PhyDef, MagDef, Dex, Agi, Crit, Stun, Block, Blood, Poison, Silence][atk chance %, atk chance, chance, def chance %, def chance, chance, atk %, atk, chance, def %, def, chance]		
		String[][] SkillsInfo = new String[NumberOfSkills][2] ;
		for (int i = 0 ; i <= NumberOfSkills - 1 ; i += 1)
		{
			int ID = i + Player.CumNumberOfSkillsPerJob[player.getJob()] ;
			int BuffCont = 0, NerfCont = 0 ;
			for (int j = 0 ; j <= NumberOfAtt - 1 ; j += 1)
			{
				if (j == 11 | j == 12)
				{
					for (int k = 0 ; k <= NumberOfBuffs - 1 ; k += 1)
					{
						SkillBuffs[i][j][k] = Float.parseFloat(SkillsBuffsInput[ID][BuffCont + 3]) ;
						SkillNerfs[i][j][k] = Float.parseFloat(SkillsNerfsInput[ID][NerfCont + 3]) ;
						NerfCont += 1 ;
						BuffCont += 1 ;
					}
				}
				else
				{
					SkillBuffs[i][j][0] = Float.parseFloat(SkillsBuffsInput[ID][BuffCont + 3]) ;
					SkillBuffs[i][j][1] = Float.parseFloat(SkillsBuffsInput[ID][BuffCont + 4]) ;
					SkillBuffs[i][j][2] = Float.parseFloat(SkillsBuffsInput[ID][BuffCont + 5]) ;
					SkillNerfs[i][j][0] = Float.parseFloat(SkillsNerfsInput[ID][NerfCont + 3]) ;
					SkillNerfs[i][j][1] = Float.parseFloat(SkillsNerfsInput[ID][NerfCont + 4]) ;
					SkillNerfs[i][j][2] = Float.parseFloat(SkillsNerfsInput[ID][NerfCont + 5]) ;
					NerfCont += 3 ;
					BuffCont += 3 ;
				}
			}
			if (Language.equals("P"))
			{
				SkillsInfo[i] = new String[] {SkillsInput[ID][42], SkillsInput[ID][43]} ;
			}
			else if (Language.equals("E"))
			{
				SkillsInfo[i] = new String[] {SkillsInput[ID][44], SkillsInput[ID][45]} ;
			}
			String Name = SkillsInput[ID][4] ;
			int MaxLevel = Integer.parseInt(SkillsInput[ID][5]) ;
			float MpCost = Float.parseFloat(SkillsInput[ID][6]) ;
			String Type = SkillsInput[ID][7] ;
			int[][] PreRequisites = new int[][] {{Integer.parseInt(SkillsInput[ID][8]), Integer.parseInt(SkillsInput[ID][9])}, {Integer.parseInt(SkillsInput[ID][10]), Integer.parseInt(SkillsInput[ID][11])}, {Integer.parseInt(SkillsInput[ID][12]), Integer.parseInt(SkillsInput[ID][13])}} ;
			int Cooldown = Integer.parseInt(SkillsInput[ID][14]) ;
			int Duration = Integer.parseInt(SkillsInput[ID][15]) ;
			float[] Atk = new float[] {Float.parseFloat(SkillsInput[ID][16]), Float.parseFloat(SkillsInput[ID][17])} ;
			float[] Def = new float[] {Float.parseFloat(SkillsInput[ID][18]), Float.parseFloat(SkillsInput[ID][19])} ;
			float[] Dex = new float[] {Float.parseFloat(SkillsInput[ID][20]), Float.parseFloat(SkillsInput[ID][21])} ;
			float[] Agi = new float[] {Float.parseFloat(SkillsInput[ID][22]), Float.parseFloat(SkillsInput[ID][23])} ;
			float[] AtkCrit = new float[] {Float.parseFloat(SkillsInput[ID][24])} ;
			float[] DefCrit = new float[] {Float.parseFloat(SkillsInput[ID][25])} ;
			float[] Stun = new float[] {Float.parseFloat(SkillsInput[ID][26]), Float.parseFloat(SkillsInput[ID][27]), Float.parseFloat(SkillsInput[ID][28])} ;
			float[] Block = new float[] {Float.parseFloat(SkillsInput[ID][29]), Float.parseFloat(SkillsInput[ID][30]), Float.parseFloat(SkillsInput[ID][31])} ;
			float[] Blood = new float[] {Float.parseFloat(SkillsInput[ID][32]), Float.parseFloat(SkillsInput[ID][33]), Float.parseFloat(SkillsInput[ID][34])} ;
			float[] Poison = new float[] {Float.parseFloat(SkillsInput[ID][35]), Float.parseFloat(SkillsInput[ID][36]), Float.parseFloat(SkillsInput[ID][37])} ;
			float[] Silence = new float[] {Float.parseFloat(SkillsInput[ID][38]), Float.parseFloat(SkillsInput[ID][39]), Float.parseFloat(SkillsInput[ID][40])} ;
			String Elem = SkillsInput[ID][41] ;
			skills[i] = new Spells(Name, MaxLevel, MpCost, Type, PreRequisites, Cooldown, Duration, SkillBuffs[i], SkillNerfs[i], Atk, Def, Dex, Agi, AtkCrit, DefCrit, Stun, Block, Blood, Poison, Silence, Elem, SkillsInfo[i]) ;	
		}
		//System.out.println(Arrays.toString(ActivePlayerSkills)) ;
		return skills ;
    }
    public PetSpells[] InitializePetSpells()
    {
		PetSpells[] petskills = new PetSpells[Pet.NumberOfSkills] ;
		String[][] PetSkillsInput = UtilG.ReadTextFile(CSVPath + "PetSkills.csv", 20) ;	
		String[][] PetSkillsBuffsInput = UtilG.ReadTextFile(CSVPath + "PetSkillsBuffs.csv", 20) ;
		String[][] PetSkillsNerfsInput = UtilG.ReadTextFile(CSVPath + "PetSkillsNerfs.csv", 20) ;
		float[][][] PetSkillBuffs = new float[Pet.NumberOfSkills][14][13] ;	// [Life, MP, PhyAtk, MagAtk, PhyDef, MagDef, Dex, Agi, Crit, Stun, Block, Blood, Poison, Silence][atk chance %, atk chance, chance, def chance %, def chance, chance, atk %, atk, chance, def %, def, chance, duration]		
		float[][][] PetSkillNerfs = new float[Pet.NumberOfSkills][14][13] ;	// [Life, MP, PhyAtk, MagAtk, PhyDef, MagDef, Dex, Agi, Crit, Stun, Block, Blood, Poison, Silence][atk chance %, atk chance, chance, def chance %, def chance, chance, atk %, atk, chance, def %, def, chance, duration]	
		for (int i = 0 ; i <= Pet.NumberOfSkills - 1 ; i += 1)
		{
			int ID = i + pet.getJob()*Pet.NumberOfSkills ;
			int BuffCont = 0, NerfCont = 0 ;
			for (int j = 0 ; j <= 14 - 1 ; j += 1)
			{
				if (j == 11 | j == 12)
				{
					for (int k = 0 ; k <= 13 - 1 ; k += 1)
					{
						PetSkillBuffs[i][j][k] = Float.parseFloat(PetSkillsBuffsInput[ID][BuffCont + 3]) ;
						BuffCont += 1 ;
					}
				}
				else
				{
					PetSkillBuffs[i][j][0] = Float.parseFloat(PetSkillsBuffsInput[ID][BuffCont + 3]) ;
					PetSkillBuffs[i][j][1] = Float.parseFloat(PetSkillsBuffsInput[ID][BuffCont + 4]) ;
					PetSkillBuffs[i][j][2] = Float.parseFloat(PetSkillsBuffsInput[ID][BuffCont + 5]) ;
					PetSkillBuffs[i][j][12] = Float.parseFloat(PetSkillsBuffsInput[ID][BuffCont + 6]) ;
					BuffCont += 4 ;
				}
			}
			for (int j = 0 ; j <= 14 - 1 ; j += 1)
			{
				if (j == 11 | j == 12)
				{
					for (int k = 0 ; k <= 13 - 1 ; k += 1)
					{
						PetSkillNerfs[i][j][k] = Float.parseFloat(PetSkillsNerfsInput[ID][NerfCont + 3]) ;
						NerfCont += 1 ;
					}
				}
				else
				{
					PetSkillNerfs[i][j][0] = Float.parseFloat(PetSkillsNerfsInput[ID][NerfCont + 3]) ;
					PetSkillNerfs[i][j][1] = Float.parseFloat(PetSkillsNerfsInput[ID][NerfCont + 4]) ;
					PetSkillNerfs[i][j][2] = Float.parseFloat(PetSkillsNerfsInput[ID][NerfCont + 5]) ;
					PetSkillNerfs[i][j][12] = Float.parseFloat(PetSkillsNerfsInput[ID][NerfCont + 6]) ;
					NerfCont += 4 ;
				}
			}
			petskills[i] = new PetSpells(PetSkillsInput[ID][4], Integer.parseInt(PetSkillsInput[ID][5]), Float.parseFloat(PetSkillsInput[ID][6]), PetSkillsInput[ID][7], new int[][] {{Integer.parseInt(PetSkillsInput[ID][8]), Integer.parseInt(PetSkillsInput[ID][9])}, {Integer.parseInt(PetSkillsInput[ID][10]), Integer.parseInt(PetSkillsInput[ID][11])}, {Integer.parseInt(PetSkillsInput[ID][12]), Integer.parseInt(PetSkillsInput[ID][13])}}, Integer.parseInt(PetSkillsInput[ID][14]), PetSkillBuffs[i], PetSkillNerfs[i], PetSkillsInput[ID][16], new String[] {PetSkillsInput[ID][17], PetSkillsInput[ID][18]}) ;	
		}
		return petskills ;
    }
    /*public Items[] InitializeItems(int DifficultLevel, String Language)
    {
    	Items[] items = new Items[Items.NumberOfAllItems] ;
		String[][] Input = Utg.ReadTextFile(CSVPath + "Items.csv", Items.NumberOfAllItems) ;
		String[] Name = new String[Items.NumberOfAllItems] ;
		String[] Description = new String[Items.NumberOfAllItems] ;
		for (int i = 0 ; i <= Items.NumberOfAllItems - 1 ; ++i)
		{
			if (Language.equals("P"))
			{
				Name[i] = Input[i][2] ;
				Description[i] = Input[i][5] ;
			}
			if (Language.equals("E"))
			{
				Name[i] = Input[i][1] ;
				Description[i] = Input[i][6] ;
			}
			Image image = new ImageIcon(ImagesPath + "items.png").getImage() ;
			int Price = (int)(Integer.parseInt(Input[i][3]) * Player.DifficultMult[DifficultLevel]) ;
			float DropChance = Integer.parseInt(Input[i][4]) / Player.DifficultMult[DifficultLevel] ;
			float[][] Buffs = new float[14][13] ;
			String Type = Input[i][7] ;
			items[i] = new Items(i, Name[i], image, Price, DropChance, Buffs, Description[i], Type) ;
		}
		

		Items.LongestName = "";
		for (int i = 0 ; i <= items.length - 1 ; i += 1)
		{
			if (Items.LongestName.length() < items[i].getName().length())
			{
				Items.LongestName = items[i].getName() ;
			}
		}
		
		return items ;
    }*/
    public Quests[] InitializeQuests(String Language, int PlayerJob)
    {
		Quests[] quest = new Quests[Quests.NumberOfQuests] ;
		String[][] Input = UtilG.ReadTextFile(CSVPath + "Quests.csv", Quests.NumberOfQuests) ;
		for (int i = 0 ; i <= Quests.NumberOfQuests - 1 ; i += 1)
		{
			quest[i] = new Quests(Integer.parseInt(Input[i][0])) ;
			quest[i].Initialize(CSVPath, Language, Integer.parseInt(Input[i][0]), PlayerJob) ;
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
    	Image PetImage = new ImageIcon(ImagesPath + "PetType" + pet.getJob() + ".png").getImage() ;
		Image IconSkillsTree = new ImageIcon(ImagesPath + "Icon8_SkillsTree.png").getImage() ;
		Image IconSelectedOptions = new ImageIcon(ImagesPath + "Icon_settingsSelected.png").getImage() ;
		Image IconSelectedBag = new ImageIcon(ImagesPath + "Icon1_BagSelected.png").getImage() ;
		Image IconSelectedQuest = new ImageIcon(ImagesPath + "Icon2_QuestSelected.png").getImage() ;
		Image IconSelectedMap = new ImageIcon(ImagesPath + "Icon3_MapSelected.png").getImage() ;
		Image IconSelectedBook = new ImageIcon(ImagesPath + "Icon4_BookSelected.png").getImage() ;
    	Image IconSelectedTent = new ImageIcon(ImagesPath + "Icon5_TentSelected.png").getImage() ;
    	Image PlayerSelectedImage = new ImageIcon(ImagesPath + "Player.png").getImage() ;
    	Image PetSelectedImage = new ImageIcon(ImagesPath + "PetType" + pet.getJob() + ".png").getImage() ;
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
 		InitializeGeneralVariables(screen.getSize()) ;
		npc = InitializeNPCs(GameLanguage, screen.getSize()) ;
		/*System.out.println("*** All NPCs ***");
		for (int i = 0; i <= npc.length - 1; i += 1)
		{
			System.out.println(npc[i].toString()) ;
		}*/
		buildings = InitializeBuildings(screen.getSize(), npc) ;
		/*System.out.println("*** All buildings ***");
		for (int i = 0; i <= buildings.length - 1; i += 1)
		{
			System.out.println(buildings[i].toString()) ;
		}*/
		creatureTypes = InitializeCreatureTypes(GameLanguage, 1) ;	// player.DifficultLevel
		/*System.out.println("*** All creature types ***");
		for (int i = 0; i <= creatureTypes.length - 1; i += 1)
		{
			System.out.println(creatureTypes[i].toString()) ;
		}*/
		maps = InitializeMaps(screen, buildings, creatureTypes, sky) ;
		/*System.out.println("*** All maps ***");
		for (int i = 0; i <= maps.length - 1; i += 1)
		{
			System.out.println(maps[i].toString()) ;
		}*/
 		player = InitializePlayer(PlayerInitialName, PlayerInitialJob, GameLanguage, PlayerInitialSex) ;
		//items = InitializeItems(player.DifficultLevel, GameLanguage) ;
		pet = InitializePet() ;
		//System.out.println("*** Pet ***");
		//System.out.println(pet.toString());
		InitializeCreaturesInMap() ;
		creature = InitializeCreatures(creatureTypes, screen.getSize(), maps, player.getStep()) ;
		/*System.out.println("*** All creatures ***");
		for (int i = 0; i <= creature.length - 1; i += 1)
		{
			System.out.println(creature[i].toString()) ;
		}*/
		spells = InitializeSpells(GameLanguage) ;
		petspells = InitializePetSpells() ;
		for (int map = 0 ; map <= maps.length - 1 ; map += 1)
		{
			maps[map].InitializeNPCsInMap(npc) ;
			maps[map].InitializeBuildings(buildings) ;
		}
		quest = InitializeQuests(GameLanguage, player.getJob()) ;
		plusSignIcon = InitializeIcons(screen.getSize()) ;

		// Initialize classes
    	ani = new Animations(AllTextCat, AllText) ;
    	bat = new Battle(CSVPath, ImagesPath, spells, petspells, player.getSettings().getDamageAnimation(), music.getSoundEffect(), new int[] {player.getBattleAtt().getBattleActions()[0][1]/2, pet.getBattleAtt().getBattleActions()[0][1]/2}, ani) ;
	}
  	
	public void Opening()
	{
		int openingStatus = opening.Run(AllTextCat, AllText, player.action, MousePos, music, ani, DF) ;
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
	
	public void runLoading()
	{
		loading.Run(DF) ;
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
		int SelectedOption = player.SelectedOption ;
		int[] CurrentColorValues = new int[] {player.getColors()[0].getRed(), player.getColors()[0].getGreen(), player.getColors()[0].getBlue()} ;
		DF.DrawCustomizationMenu(player, SelectedOption, CurrentColorValues) ;	
		if (player.action.equals("Enter"))
		{
			CustomizationIsOn = false ;
			RunGame = true ;
			if (player.getSettings().getMusicIsOn())
			{
				music.SwitchMusic(music.getMusicIntro(), music.getMusicClip()[Maps.MusicID[player.getMap().getid()]]) ;
			}
		}
	}
		
	public void IncrementCounters(Player player, Pet pet, Creatures[] creatures)
	{
		sky.incDayTime();
		player.IncActionCounters() ;
		player.SupSkillCounters(spells, creatures, player.CreatureInBattle) ;
		pet.IncActionCounters() ;
		if (player.getMap().hasCreatures())
		{
			for (int c = 0 ; c <= player.getMap().getCreatures().length - 1 ; c += 1)
			{
				player.getMap().getCreatures()[c].IncActionCounters() ;
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
			creatures[player.CreatureInBattle].IncBattleActionCounters() ;
		}
		if (!player.getMap().IsACity())
		{
			player.getMap().IncCollectiblesCounter() ;
		}
	}
	
	public void ActivateCounters(Player player, Pet pet, Creatures[] creature)
	{	
		player.ActivateActionCounters(ani.SomeAnimationIsActive()) ;
		if (0 < pet.getLife()[0])
		{
			pet.ActivateActionCounters(ani.SomeAnimationIsActive()) ;
		}
		if (!player.getMap().IsACity())	// player is out of the cities
		{
			for (int i = 0 ; i <= player.getMap().getCreatures().length - 1 ; ++i)
			{
				if (!ani.isActive(12) & !ani.isActive(13) & !ani.isActive(14) & !ani.isActive(18))
				{
					player.getMap().getCreatures()[i].ActivateActionCounters() ;
				}
			}
		}
		if (!player.getMap().IsACity())
		{
			player.getMap().CreateCollectibles() ;
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
		IncrementCounters(player, pet, creature) ;
		ActivateCounters(player, pet, creature) ;
		if (UtilS.KonamiCodeActivated(player.getCombo()))
		{
			KonamiCode() ;
		}
		DF.DrawFullMap(player, pet, player.getMap(), npc, buildings, spells, sky, SideBarIcons, MousePos, sky.dayTime) ;
		//if (TutorialIsOn)
		//{
		//	Tutorial(player, meet) ;
		//}
		
		// Creatures act
		if (player.getMap().hasCreatures())
		{
			for (int c = 0 ; c <= player.getMap().getCreatures().length - 1 ; c += 1)
			{				
				player.getMap().getCreatures()[c].act(player.getPos(), player.getMap()) ;
				player.getMap().getCreatures()[c].display() ;
			}
		}
		// Player acts
		player.act(pet, maps, spells, MousePos, SideBarIcons, ani, DF) ;
		if (player.ActionIsAMove(player.action) | 0 < player.getPersonalAtt().getCountmove())	// countmove becomes greater than 0 when the player moves, then starts to increase by 1 and returns to 0 when it reaches 20
		{
			player.Move(pet, music.getMusicClip(), player.getSettings().getMusicIsOn(), ani) ;
		}
		
		// Draw player stuff
		player.DrawAttributes(DF) ;
		player.display(player.getPos(), new float[] {1, 1}, player.getDir(), player.getSettings().getShowPlayerRange(), DF.getDrawPrimitives()) ;
		if (0 < player.getEquips()[0])	// If the player is equipped
		{
			DF.DrawPlayerWeapon(player, player.getPos(), new float[] {1, 1}) ;
		}
		
		// Draw pet stuff
		if (0 < pet.getLife()[0])		// If the pet is alive
		{
			pet.setCombo(UtilS.RecordCombo(pet.getCombo(), pet.action, 1)) ;
			pet.Move(player, maps) ;
			if (player.isInBattle())
			{
				pet.action = pet.Action(Player.ActionKeys) ;
			}
			DF.DrawPetAttributes(pet) ;
			DF.DrawPet(pet.getPos(), new float[] {1, 1}, pet.getMovingAnimations().idleGif) ;
		}
		
		
		player.ClosestCreature = UtilS.ClosestCreatureInRange(player, creature, maps) ;	// find the closest creature to the player
		
		// check if the player met something
		if (!player.isInBattle())
		{
			int[] meet = player.meet(creature, npc, player.getMap(), player.CreatureInBattle, ani) ;	// meet[0] is the encounter and meet[1] is its id
			if (meet[0] == 0 & 0 <= meet[1])	// meet with creature
			{
				creature[meet[1]].setFollow(true) ;
				player.setCurrentAction("Fighting") ;	
				player.CreatureInBattle = meet[1] ;
				player.AddCreatureToBestiary(meet[1]) ;
			}
			if (meet[0] == 1 & 0 <= meet[1])	// meet with npc
			{
				npc[meet[1]].Contact(player, pet, AllText, creatureTypes, creature, spells, maps, quest, MousePos, TutorialIsOn, ani, DF) ;				
			}
			if (meet[0] == 2 & 0 <= meet[1])	// meet with collectibles
			{
				player.Collect(meet[1], maps, AllText, AllTextCat, DF, ani) ;
			}
			if (meet[0] == 3 & 0 <= meet[1])	// meet with chest
			{
				player.TreasureChestReward(meet[1], maps, ani) ;
			}
		}
		
		// if the player is in battle, run battle
		if (player.isInBattle() & !ani.isActive(12) & !ani.isActive(13) & !ani.isActive(14) & !ani.isActive(16))	// only enter battle if the animations for win (12), level up (13), pet level up (14), and pterodactile (16) are off
		{
			bat.RunBattle(player, pet, creature[player.CreatureInBattle], spells, petspells, quest, MousePos, DF) ;
		}
		
		// check if the player and the pet have leveled up
		if (!ani.isActive(12) & UtilS.CheckLevelUP(player, ani))
		{
			float[] AttributesIncrease = UtilS.LevelUpIncAtt(player.getAttIncrease()[player.getProJob()], player.getChanceIncrease()[player.getProJob()], player.getLevel()) ;
			player.LevelUp(AttributesIncrease) ;
			ani.SetAniVars(13, new Object[] {150, AttributesIncrease, player.getLevel(), player.getColors()[0]}) ;
			ani.StartAni(13) ;
		}
		if (UtilS.CheckLevelUP(pet, ani))
		{
			float[] AttributesIncrease = UtilS.LevelUpIncAtt(Pet.AttributeIncrease, Pet.ChanceIncrease, pet.getLevel()) ;
			pet.LevelUp(AttributesIncrease) ;
			ani.SetAniVars(14, new Object[] {150, pet, AttributesIncrease}) ;
			ani.StartAni(14) ;
		}
		
		// Draw player windows
		//ShowPlayerWindows() ;
		player.ShowWindows(pet, creature, creatureTypes, quest, maps, plusSignIcon, bat, AllText, AllTextCat, music.getMusicClip(), MousePos, DF) ;
		
		// if tutorial is on, draw tutorial animations
		if (TutorialIsOn)
		{
			DF.TutorialAnimations() ;
		}		
		
		// move the active projectiles and check if they collide with something
		/*if (proj != null)
		{
			for (int p = 0 ; p <= proj.length - 1 ; p += 1)
			{
				proj[p].go(player, player.currentMap(maps).creaturesinmap(creature), pet, DF.getDrawPrimitives()) ;
			}
			if (proj[0].collidedwith(player, player.currentMap(maps).creaturesinmap(creature), pet) != - 3)
			{
				proj = null ;
			}
		}*/
		//repaint() ;
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
        super.paintComponent(g) ;
		MousePos = UtilG.GetMousePos(mainpanel) ;
        DF = new DrawFunctions(g, AllText, AllTextCat) ;
        DF.InitializeVariables(ImagesPath) ;
		
    	if (OpeningIsOn)
		{
			Opening() ;
    		
        	/*
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
    		GameLanguage = "P" ;
    		AllText = UtilG.ReadTextFile(GameLanguage) ;
    		AllTextCat = UtilS.FindAllTextCat(AllText, GameLanguage) ;
        	PlayerInitialName = "" ;
        	PlayerInitialJob = 2 ;
        	PlayerInitialSex = "N" ;
    		MainInitialization() ;
        	player.setMap(maps[1]) ;
        	DF.setAllText(AllText) ;
        	DF.setAllTextCat(AllTextCat) ;
        	
        	
        	//Arrays.fill(player.getSkill(), 5) ;
    		Arrays.fill(player.getQuestSkills(), true) ;
        	//Arrays.fill(player.getBag(), 30) ;
        	player.setPos(new Point(screen.getSize().x / 2 + 160, screen.getSize().y / 2)) ;
        	OpeningIsOn = false ;
        	InitializationIsOn = false ;
        	LoadingGameIsOn = false ;
        	CustomizationIsOn = false ;
        	player.getSettings().setMusicIsOn(false) ;
        	for (int i = 0; i <= 10 - 1; i += 1)
        	{
        		player.getBag().Add(Potion.getAll()[i]) ;
        	}
        	if (player.getSettings().getMusicIsOn())
			{
				music.PlayMusic(music.getMusicClip()[Maps.MusicID[player.getMap().getid()]]) ;
			}
        	RunGame = true ;*/
    		
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
    		//player.getBattleAtt().setStun(new float[] {1, 0, 0, 0, 500});
    		player.getEquips()[0] = 301 ;
    		player.getEquips()[1] = 302 ;
    		player.getEquips()[2] = 303 ;
    		player.setCurrentAction("Fighting");*/
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
	 		InitializeGeneralVariables(screen.getSize()) ;
			loading.Run(DF) ;
			//InitializeClasses() ;
		}
		else if (CustomizationIsOn)
		{
			Customization() ;
		}
		else if (RunGame)
		{
			RunGame(DF) ;
			
			
        	// teste de funï¿½ï¿½es
        	//player.getBag().display(screen.getSize(), MousePos, DF) ;
			
			/*IncrementCounters(player, pet, creature) ;
			ActivateCounters(player, pet, creature, DayCounter, DayDuration) ;
        	B.RunBattle(player, pet, creature[player.CreatureInBattle], screen, skills, petskills, player.activeSpells(skills), items, quest, SoundEffectsAreOn, MousePos, DF) ;*/
        	//player.AttWindow(AllText, AllTextCat, items, plusSignIcon, screen.getSize(), player.getAttIncrease()[player.getProJob()], MousePos, CoinIcon, DF.getDrawPrimitives()) ;			
    	}
		ani.RunAnimation(DF) ;	// run all the active animations
    	player.action = "" ;
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
            	if (CustomizationIsOn | player.getActions()[0][2] == 1)	// If the player can move
    			{
                	player.action = KeyEvent.getKeyText(key) ;
                	//player.setActions(new int[][] {{0, player.getActions()[0][1], 0}, {player.getActions()[1][0], player.getActions()[1][1], player.getActions()[1][2]}, {player.getActions()[2][0], player.getActions()[2][1], player.getActions()[2][2]}}) ;
    			}
            } else if (key == KeyEvent.VK_ENTER | key == KeyEvent.VK_ESCAPE | key == KeyEvent.VK_BACK_SPACE | key == KeyEvent.VK_F1 | key == KeyEvent.VK_F2 | key == KeyEvent.VK_F3 | key == KeyEvent.VK_F4 | key == KeyEvent.VK_F5 | key == KeyEvent.VK_F6 | key == KeyEvent.VK_F7 | key == KeyEvent.VK_F8 | key == KeyEvent.VK_F9 | key == KeyEvent.VK_F10 | key == KeyEvent.VK_F11 | key == KeyEvent.VK_F12 | key == KeyEvent.VK_A | key == KeyEvent.VK_B | key == KeyEvent.VK_C | key == KeyEvent.VK_D | key == KeyEvent.VK_E | key == KeyEvent.VK_F | key == KeyEvent.VK_G | key == KeyEvent.VK_H | key == KeyEvent.VK_I | key == KeyEvent.VK_J | key == KeyEvent.VK_K | key == KeyEvent.VK_L | key == KeyEvent.VK_M | key == KeyEvent.VK_N | key == KeyEvent.VK_O | key == KeyEvent.VK_P | key == KeyEvent.VK_Q | key == KeyEvent.VK_R | key == KeyEvent.VK_S | key == KeyEvent.VK_T | key == KeyEvent.VK_U | key == KeyEvent.VK_V | key == KeyEvent.VK_W | key == KeyEvent.VK_X | key == KeyEvent.VK_Y | key == KeyEvent.VK_Z | key == KeyEvent.VK_0 | key == KeyEvent.VK_1 | key == KeyEvent.VK_2 | key == KeyEvent.VK_3 | key == KeyEvent.VK_4 | key == KeyEvent.VK_5 | key == KeyEvent.VK_6 | key == KeyEvent.VK_7 | key == KeyEvent.VK_8 | key == KeyEvent.VK_9) 
            {
            	player.action = KeyEvent.getKeyText(key) ;
            }
            else if (key == KeyEvent.VK_SPACE)
            {
            	player.action = "Space" ;
            } else if (key == KeyEvent.VK_NUMPAD0 | key == KeyEvent.VK_NUMPAD1 | key == KeyEvent.VK_NUMPAD2 | key == KeyEvent.VK_NUMPAD3 | key == KeyEvent.VK_NUMPAD4 | key == KeyEvent.VK_NUMPAD5 | key == KeyEvent.VK_NUMPAD6 | key == KeyEvent.VK_NUMPAD7 | key == KeyEvent.VK_NUMPAD8 | key == KeyEvent.VK_NUMPAD9)
            {
            	player.action = String.valueOf(key - 96) ;
            }
            else if (key == KeyEvent.VK_PAUSE) 
            {
                if (timer.isRunning()) 
                {
                   timer.stop() ;
                } else 
                {
                   timer.start() ;
                }
            }
            //repaint() ;
	    }
	
	    @Override
	    public void keyReleased(KeyEvent e) 
	    {
	        player.action = "" ;
	    }
	}

	public class MouseEventDemo implements MouseListener 
	{
		@Override
		public void mouseClicked(MouseEvent evt)
		{
			if (evt.getButton() == 1)	// Left click
			{
				player.action = "MouseLeftClick" ;
				for (int i = 0 ; i <= OPbuttons.length - 1 ; i += 1)
				{
					if (OPbuttons[i].ishovered(MousePos) & OPbuttons[i].isActive)
					{
						OPbuttons[i].startaction() ;
						if (OPbuttons[i].getid() == 0 | OPbuttons[i].getid() == 1)
						{
							GameLanguage = (String) OPbuttons[i].startaction() ;
							AllText = UtilG.ReadTextFile(GameLanguage) ;
							AllTextCat = UtilS.FindAllTextCat(AllText, GameLanguage) ;
						}
						if (2 <= OPbuttons[i].getid() & OPbuttons[i].getid() <= 13)
						{
							player.action = (String) OPbuttons[i].startaction() ;
						}
					}
				}
			}
			if (evt.getButton() == 3)	// Right click
			{
				player.action = "MouseRightClick" ;
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
			player.action = "" ;
		}		
	}
	
	@Override
    public void actionPerformed(ActionEvent e) 
	{
		repaint() ;
	}
}
