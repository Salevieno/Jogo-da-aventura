package Main ;

import java.awt.Color ;
import java.awt.Font ;
import java.awt.Graphics ;
import java.awt.Image ;
import java.awt.Toolkit ;
import java.awt.event.ActionEvent ;
import java.awt.event.ActionListener ;
import java.awt.event.KeyAdapter ;
import java.awt.event.KeyEvent ;
import java.awt.event.MouseEvent ;
import java.awt.event.MouseListener ;
import java.io.File ;
import java.util.Arrays ;

import javax.sound.sampled.Clip ;
import javax.swing.ImageIcon ;
import javax.swing.JPanel ;
import javax.swing.Timer ;

import Actions.Battle ;
import Actions.BattleActions;
import GameComponents.BattleAttributes ;
import GameComponents.Buildings ;
import GameComponents.CreatureTypes ;
import GameComponents.Creatures ;
import GameComponents.Icon ;
import GameComponents.Items ;
import GameComponents.MapElements ;
import GameComponents.Maps ;
import GameComponents.NPCs ;
import GameComponents.PersonalAttributes ;
import GameComponents.Pet ;
import GameComponents.PetSkills ;
import GameComponents.Player ;
import GameComponents.Projectiles ;
import GameComponents.Quests ;
import GameComponents.Screen ;
import GameComponents.Skills ;
import GameComponents.SkyComponents ;
import Graphics.Animations ;
import Graphics.DrawFunctions ;
import Graphics.DrawPrimitives ;

public class Game extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L ;
	
	private Timer timer ;		// Main timer of the game
    private int[] MousePos ;
	private JPanel mainpanel = this ;

	public static String CSVPath, ImagesPath, MusicPath ;
	public static String MainFontName ;
	public static Color[] ColorPalette ;
	
    private boolean OpeningIsOn, TutorialIsOn, CustomizationIsOn, LoadingGameIsOn, InitializationIsOn, MusicIsOn, SoundEffectsAreOn ;
    private boolean RunGame ;
    private int OPSelectedButton ;
	private int LoadingSelectedSlot, LoadingGameTab ;
    private int[] DamageAnimationType ;
    private int DayCounter, DayDuration ;
	private int OpeningStep, TutorialStep ;
    private String GameLanguage, PlayerName, PlayerSex ;
    private int PlayerJob ;
    private String[][] AllText ;
    private int[] AllTextCat ;
	
	private Image SkillCooldownImage, SkillSlotImage ;
	private Image CoinIcon ;
	private Image ElementalCircle ;
	private Image LoadingGif ;
	private Image OpeningBG, OpeningGif ;
	private Clip MusicIntro ;
	private Clip[] Music, SoundEffects ;

	private SkyComponents[] Cloud, Star ;
	private NPCsMethods NPC ;
	private DrawFunctions DF ;
	private Animations Ani ;
	private Battle B ;
	private Screen screen ;
	private Icon[] OPbuttons, SideBarIcons, plusSignIcon ;
	private CreatureTypes[] creatureTypes ;
	private Player player ;
	private Pet pet ;
	private Creatures[] creature ;
	private NPCs[] npc ;
	private Items[] items ;
	private Buildings[] buildings ;
	private Maps[] maps ;
	private Skills[] skills ;
	private PetSkills[] petskills ;
	private Quests[] quest ;
	private Projectiles[] proj ;	
	
	public Game(int[] WinDim) 
	{         	
		FirstInitialization(WinDim) ;
		timer.start() ;	// Game will start checking for keyboard events and go to the method paintComponent every "timer" miliseconds
		addMouseListener(new MouseEventDemo()) ;
		addKeyListener(new TAdapter()) ;
		setFocusable(true) ;
	}
    
    public void FirstInitialization(int[] WinDim)
    {
		timer = new Timer(10, this) ;	// timer of the game, first number = delay
    	screen = new Screen(new int[] {WinDim[0] - 40 - 15, WinDim[1] - 39}, new int[] {0, 140, WinDim[0] - 40 - 15, WinDim[1] - 39}) ;
		CSVPath = ".\\csv files\\" ;
		ImagesPath = ".\\images\\" ;
		MusicPath = ".\\music\\" ;
		MainFontName = "Scheherazade Bold" ;
		ColorPalette = Uts.ReadColorPalette(new ImageIcon(ImagesPath + "ColorPalette.png").getImage(), "Normal") ;
		GameLanguage = "P" ;
		ElementalCircle = new ImageIcon(ImagesPath + "ElementalCicle.png").getImage() ;
		LoadingGif = new ImageIcon(ImagesPath + "Loading.gif").getImage() ;
    	OpeningBG = new ImageIcon(ImagesPath + "Opening.png").getImage() ;
		OpeningGif = new ImageIcon(ImagesPath + "Opening.gif").getImage() ;
    	
    	LoadingSelectedSlot = 0 ;
		LoadingGameTab = 0 ;
		OPSelectedButton = 2 ;
		
    	MusicIntro = Utg.MusicFileToClip(new File(MusicPath + "1-Intro.wav").getAbsoluteFile()) ;
    	MusicIsOn = true ;
    	OpeningIsOn = true ; 
    	SoundEffectsAreOn = true ;
    	
    	player = InitializePlayer("", 0, "", "") ;
    	
    	int ScreenW = screen.getDimensions()[0], ScreenH = screen.getDimensions()[1] ;
		Image ButtonPort = new ImageIcon(ImagesPath + "ButtonPort.png").getImage() ;
		Image ButtonEn = new ImageIcon(ImagesPath + "ButtonEn.png").getImage() ;
		Image ButtonPortSelected = new ImageIcon(ImagesPath + "ButtonPortSelected.png").getImage() ;
		Image ButtonEnSelected = new ImageIcon(ImagesPath + "ButtonEnSelected.png").getImage() ;
		Image ButtonNewGame = new ImageIcon(ImagesPath + "Button_newGame.png").getImage() ;
		Image ButtonNewGameSelected = new ImageIcon(ImagesPath + "Button_newGameSelected.gif").getImage() ;
		Image ButtonLoadGame = new ImageIcon(ImagesPath + "Button_loadGame.png").getImage() ;
		Image ButtonLoadGameSelected = new ImageIcon(ImagesPath + "Button_loadGameSelected.gif").getImage() ;
    	OPbuttons = new Icon[14] ;
    	OPbuttons[0] = new Icon(0, "Port", new int[] {ScreenW - 50, 30}, null, ButtonPort, ButtonPortSelected) ;
    	OPbuttons[1] = new Icon(1, "En", new int[] {ScreenW - 0, 30}, null, ButtonEn, ButtonEnSelected) ;
    	OPbuttons[2] = new Icon(2, "New game", new int[] {ScreenW / 2 - 80, ScreenH / 4}, null, ButtonNewGame, ButtonNewGameSelected) ;
    	OPbuttons[3] = new Icon(3, "Load game", new int[] {ScreenW / 2 + 80, ScreenH / 4}, null, ButtonLoadGame, ButtonLoadGameSelected) ;
    	OPbuttons[4] = new Icon(4, "Male", new int[] {ScreenW / 2 - 50, ScreenH / 4}, null, null, null) ;
    	OPbuttons[5] = new Icon(5, "Female", new int[] {ScreenW / 2 + 50, ScreenH / 4}, null, null, null) ;
    	OPbuttons[6] = new Icon(6, "Baixo", new int[] {ScreenW / 2 - 100, ScreenH / 4}, null, null, null) ;
    	OPbuttons[7] = new Icon(7, "Médio", new int[] {ScreenW / 2 + 0, ScreenH / 4}, null, null, null) ;
    	OPbuttons[8] = new Icon(8, "Alto", new int[] {ScreenW / 2 + 100, ScreenH / 4}, null, null, null) ;
    	OPbuttons[9] = new Icon(9, "Cavaleiro", new int[] {ScreenW / 2 - 200, ScreenH / 4}, null, null, null) ;
    	OPbuttons[10] = new Icon(10, "Mago", new int[] {ScreenW / 2 - 100, ScreenH / 4}, null, null, null) ;
    	OPbuttons[11] = new Icon(11, "Arqueiro", new int[] {ScreenW / 2 + 0, ScreenH / 4}, null, null, null) ;
    	OPbuttons[12] = new Icon(12, "Animal", new int[] {ScreenW / 2 + 100, ScreenH / 4}, null, null, null) ;
    	OPbuttons[13] = new Icon(13, "Ladrão", new int[] {ScreenW / 2 + 200, ScreenH / 4}, null, null, null) ;
    	
    	Ani = new Animations(screen.getDimensions(), null, null) ;
		Ani.SetAniVars(20, new Object[] {147, OpeningGif}) ;
		Ani.StartAni(20) ;
    }
    public void InitializeGeneralVariables(int[] screenDim)
    {
    	Cloud = new SkyComponents[5] ;
    	Star = new SkyComponents[50] ;
		for (int c = 0 ; c <= Cloud.length - 1 ; c += 1)
		{
			Image CloudImage = new ImageIcon(ImagesPath + "Cloud" + String.valueOf(1 + (int) (3 * Math.random())) + ".png").getImage() ;
			int[] InitialCloudPos = new int[] {(int)(Math.random() * screenDim[0]), 2 + (int) ((screen.SkyHeight - CloudImage.getHeight(mainpanel)) * Math.random())} ;
			float[] CloudSpeed = new float[] {(float)(1 + 1.5 * Math.random()), 0} ;
	    	Cloud[c] = new SkyComponents(CloudImage, "Cloud", InitialCloudPos, CloudSpeed, new Color[] {ColorPalette[4]}) ;
		}
		Image StarImage = new ImageIcon(ImagesPath + "Star.png").getImage() ;
		for (int s = 0 ; s <= Star.length - 1 ; s += 1)
		{
			int[] StarPos = new int[] {(int)(Math.random() * screenDim[0]), (int)(Math.random() * screen.SkyHeight)} ;
			Color[] StarColor = new Color[] {ColorPalette[(int)((ColorPalette.length - 1)*Math.random())]} ;
	    	Star[s] = new SkyComponents(StarImage, "Cloud", StarPos, new float[2], StarColor) ;
		}	
    	DayCounter = 60000 ;
    	DayDuration = 120000 ;
    	
    	OpeningStep = 0 ;
    	TutorialStep = 0 ;
		DamageAnimationType = new int[] {1, 1} ;
		MusicIsOn = true ;
		Quests.CalcNumberOfQuests(CSVPath) ;
		Items.CalcNumberOfCraftingItems(CSVPath) ;
    	Items.CalcNumberOfAllItems() ;
		Items.CalcBagIDs() ;
		Items.CalcItemEffects(CSVPath) ;
		Items.CalcCrafting(CSVPath) ;
		Items.CalcItemsWithEffects(CSVPath) ;
		
		// Initialize images and music
		SkillCooldownImage = new ImageIcon(ImagesPath + "Cooldown.png").getImage() ;	
		SkillSlotImage = new ImageIcon(ImagesPath + "SkillSlot.png").getImage() ;	
		CoinIcon = new ImageIcon(ImagesPath + "CoinIcon.png").getImage() ;
    	Maps.InitializeStaticVars(ImagesPath) ;
    	Items.InitializeStaticVars(ImagesPath) ;
    	BattleActions.InitializeStaticVars(CSVPath) ;
    	
    	Clip MusicKnightCity = Utg.MusicFileToClip(new File(MusicPath + "2-Knight city.wav").getAbsoluteFile()) ;
    	Clip MusicMageCity = Utg.MusicFileToClip(new File(MusicPath + "3-Mage city.wav").getAbsoluteFile()) ;
    	Clip MusicArcherCity = Utg.MusicFileToClip(new File(MusicPath + "4-Archer city.wav").getAbsoluteFile()) ;
    	Clip MusicAnimalCity = Utg.MusicFileToClip(new File(MusicPath + "5-Animal city.wav").getAbsoluteFile()) ;
    	Clip MusicAssassinCity = Utg.MusicFileToClip(new File(MusicPath + "6-Assassin city.wav").getAbsoluteFile()) ;
    	Clip MusicForest = Utg.MusicFileToClip(new File(MusicPath + "7-Forest.wav").getAbsoluteFile()) ;
    	Clip MusicCave = Utg.MusicFileToClip(new File(MusicPath + "8-Cave.wav").getAbsoluteFile()) ;
    	Clip MusicIsland = Utg.MusicFileToClip(new File(MusicPath + "9-Island.wav").getAbsoluteFile()) ;
    	Clip MusicVolcano = Utg.MusicFileToClip(new File(MusicPath + "10-Volcano.wav").getAbsoluteFile()) ;
    	Clip MusicSnowland = Utg.MusicFileToClip(new File(MusicPath + "11-Snowland.wav").getAbsoluteFile()) ;
    	Clip MusicSpecial = Utg.MusicFileToClip(new File(MusicPath + "12-Special.wav").getAbsoluteFile()) ;
    	Clip MusicSailing = Utg.MusicFileToClip(new File(MusicPath + "13-Sailing.wav").getAbsoluteFile()) ;
    	Clip MusicPlayerEvolution = Utg.MusicFileToClip(new File(MusicPath + "14-Player evolution.wav").getAbsoluteFile()) ;
    	Clip MusicDrumRoll = Utg.MusicFileToClip(new File(MusicPath + "15-Drumroll.wav").getAbsoluteFile()) ;
    	Clip SoundEffectSwordHit = Utg.MusicFileToClip(new File(MusicPath + "16-Hit.wav").getAbsoluteFile()) ;
    	Music = new Clip[] {MusicKnightCity, MusicMageCity, MusicArcherCity, MusicAnimalCity, MusicAssassinCity, MusicForest, MusicCave, MusicIsland, MusicVolcano, MusicSnowland, MusicSpecial, MusicSailing, MusicPlayerEvolution, MusicDrumRoll} ;
    	SoundEffects = new Clip[] {SoundEffectSwordHit} ;
	}
    
    public Player InitializePlayer(String Name, int Job, String GameLanguage, String Sex)
    {
    	Player player = new Player(Name, GameLanguage, Sex, Job) ;
		
		Arrays.fill(player.getQuest(), -1) ;
		Arrays.fill(player.getElemMult(), 1) ;
		Arrays.fill(player.getBattleAtt().getSpecialStatus(), -1) ;
		if (player.getJob() == 2)
		{
			player.getBag()[Items.BagIDs[4]] = 100 ;
		}
		player.getSpell()[0] = 1 ;
		player.ResetPosition() ;		
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
    public Buildings[] InitializeBuildings(int[] screenDim)
    {
		int NumberOfBuildings = 30 ;
    	Buildings[] buildings = new Buildings[NumberOfBuildings] ;
		String[][] BuildingsInput = Utg.ReadTextFile(CSVPath + "Buildings.csv", NumberOfBuildings, 5) ;
		int[] ColorID = new int[] {6, 3, 13, 3, 0, 0} ;
		/*Image Dock = new ImageIcon(ImagesPath + "Dock.png").getImage() ;
		Image ForgingTable = new ImageIcon(ImagesPath + "BuildingForgingTable.png").getImage() ;
		Image Sign = new ImageIcon(ImagesPath + "Sign.png").getImage() ;*/
		for (int i = 0 ; i <= NumberOfBuildings - 1 ; i += 1)
		{
			int ID = Integer.parseInt(BuildingsInput[i][0]) ;
			String Name = BuildingsInput[i][1] ;
			int Map = Integer.parseInt(BuildingsInput[i][2]) ;
			int[] Pos = new int[] {(int)(Float.parseFloat(BuildingsInput[i][3])*screenDim[0]), (int)(Float.parseFloat(BuildingsInput[i][4])*screenDim[1])} ;
			Image[] Images = new Image[] {new ImageIcon(ImagesPath + "Building.png").getImage(), new ImageIcon(ImagesPath + "Building" + Name + "Inside.png").getImage()} ;
			Image[] OrnamentImages = new Image[] {new ImageIcon(ImagesPath + "Building" + Name + "Ornament.png").getImage()} ;
			Color color = ColorPalette[ColorID[i % 6]] ;
			buildings[i] = new Buildings(ID, Name, Map, Pos, Images, OrnamentImages, color) ;
		}
		return buildings ;
    }
    public CreatureTypes[] InitializeCreatureTypes(String Language, float DiffMult)
    {
    	CreatureTypes.setNumberOfCreatureTypes(CSVPath);
		CreatureTypes[] creatureTypes = new CreatureTypes[CreatureTypes.getNumberOfCreatureTypes()] ;
		String[][] Input = Utg.ReadTextFile(CSVPath + "CreatureTypes.csv", CreatureTypes.getNumberOfCreatureTypes(), 44) ;
		String Name = "" ;
		Color[] color = new Color[creatureTypes.length] ;
		/*for (int ct = 0 ; ct <= creatureTypes.length - 1 ; ct += 1)
		{
		}*/
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
			Image[] animations = new Image[] {new ImageIcon(ImagesPath + "creature_idle.gif").getImage(), new ImageIcon(ImagesPath + "creature_movingup.gif").getImage(), new ImageIcon(ImagesPath + "creature_movingdown.gif").getImage(), new ImageIcon(ImagesPath + "creature_movingleft.gif").getImage(), new ImageIcon(ImagesPath + "creature_movingright.gif").getImage()} ;
			int Level = Integer.parseInt(Input[ct][3]) ;
			int Map = Integer.parseInt(Input[ct][4]) ;
			int[] Size = new int[] {42, 30} ;
			int[] Skill = new int[] {Integer.parseInt(Input[ct][5])} ;
			float[] Life = new float[] {Float.parseFloat(Input[ct][6]) * DiffMult, Float.parseFloat(Input[ct][6]) * DiffMult} ;
			float[] MP = new float[] {Float.parseFloat(Input[ct][7]) * DiffMult, Float.parseFloat(Input[ct][7]) * DiffMult} ;
			float Range = Float.parseFloat(Input[ct][8]) * DiffMult ;
			float[] PhyAtk = new float[] {Float.parseFloat(Input[ct][9]) * DiffMult, 0, 0} ;
			float[] MagAtk = new float[] {Float.parseFloat(Input[ct][10]) * DiffMult, 0, 0} ;
			float[] PhyDef = new float[] {Float.parseFloat(Input[ct][11]) * DiffMult, 0, 0} ;
			float[] MagDef = new float[] {Float.parseFloat(Input[ct][12]) * DiffMult, 0, 0} ;
			float[] Dex = new float[] {Float.parseFloat(Input[ct][13]) * DiffMult, 0, 0} ;
			float[] Agi = new float[] {Float.parseFloat(Input[ct][14]) * DiffMult, 0, 0} ;
			float[] Crit = new float[] {Float.parseFloat(Input[ct][15]) * DiffMult, 0, Float.parseFloat(Input[ct][16]) * DiffMult, 0} ;
			float[] Stun = new float[] {Float.parseFloat(Input[ct][17]) * DiffMult, 0, Float.parseFloat(Input[ct][18]) * DiffMult, 0, Float.parseFloat(Input[ct][19]) * DiffMult} ;
			float[] Block = new float[] {Float.parseFloat(Input[ct][20]) * DiffMult, 0, Float.parseFloat(Input[ct][21]) * DiffMult, 0, Float.parseFloat(Input[ct][22]) * DiffMult} ;
			float[] Blood = new float[] {Float.parseFloat(Input[ct][23]) * DiffMult, 0, Float.parseFloat(Input[ct][24]) * DiffMult, 0, Float.parseFloat(Input[ct][25]) * DiffMult, 0, Float.parseFloat(Input[ct][26]) * DiffMult, 0, Float.parseFloat(Input[ct][27]) * DiffMult} ;
			float[] Poison = new float[] {Float.parseFloat(Input[ct][28]) * DiffMult, 0, Float.parseFloat(Input[ct][29]) * DiffMult, 0, Float.parseFloat(Input[ct][30]) * DiffMult, 0, Float.parseFloat(Input[ct][31]) * DiffMult, 0, Float.parseFloat(Input[ct][32]) * DiffMult} ;
			float[] Silence = new float[] {Float.parseFloat(Input[ct][33]) * DiffMult, 0, Float.parseFloat(Input[ct][34]) * DiffMult, 0, Float.parseFloat(Input[ct][35]) * DiffMult} ;						
			String[] Elem = new String[] {Input[ct][36]} ;
			int Exp = Integer.parseInt(Input[ct][37]) ;
			int[] Bag = new int[] {Integer.parseInt(Input[ct][38]), Integer.parseInt(Input[ct][39]), Integer.parseInt(Input[ct][40]), Integer.parseInt(Input[ct][41]), Integer.parseInt(Input[ct][42]), Integer.parseInt(Input[ct][43]), Integer.parseInt(Input[ct][44]), Integer.parseInt(Input[ct][45]), Integer.parseInt(Input[ct][46]), Integer.parseInt(Input[ct][47])} ;
			int Gold = Integer.parseInt(Input[ct][48]) ;
			int Step = Integer.parseInt(Input[ct][49]) ;
			int[] Status = new int[8] ;
			int[] SpecialStatus = new int[5] ;
			int[][] Actions = new int[][] {{0, Integer.parseInt(Input[ct][50]), 0}, {0, Integer.parseInt(Input[ct][51]), 0}} ;
			int[][] BattleActions = new int[][] {{0, Integer.parseInt(Input[ct][52]), 0}} ;
			int[] StatusCounter = new int[8] ;
			image = Utg.ChangeImageColor(image, new float[] {0, 0, 1, 1}, color[ct], ColorPalette[20]) ;
			image = Utg.ChangeImageColor(image, new float[] {0, 0, 1, 1}, Creatures.getshadeColor()[colorid], ColorPalette[19]) ;
			creatureTypes[ct] = new CreatureTypes(ct, image, animations, Name, Level, Map, Size, Skill, Life, MP, Range, PhyAtk, MagAtk, PhyDef, MagDef, Dex, Agi, Crit, Stun, Block, Blood, Poison, Silence, Elem, Exp, Bag, Gold, Step, Status, SpecialStatus, color[ct], Actions, BattleActions, StatusCounter) ;	
		}
		return creatureTypes ;
    }
    public Creatures[] InitializeCreatures(CreatureTypes[] creatureTypes, int[] screenDim, Maps[] maps, int PlayerStep)
    {    	
    	int NumberOfCreatures = 575 ;
		Creatures[] creature = new Creatures[NumberOfCreatures] ;
		String Name = "" ;
		for (int c = 0 ; c <= creature.length - 1 ; c += 1)
		{
			int type = -1 ;
			for (int map = 0 ; map <= maps.length - 1 ; map += 1)
			{
				if (maps[map].getCreatureIDs() != null)	// Map has creatures
				{
					for (int j = 0 ; j <= maps[map].getCreatureIDs().length - 1 ; j += 1)
					{
						if (c == maps[map].getCreatureIDs()[j])
						{
							type = maps[map].getCreatureTypes()[j] ;
						}
					}
				}
			}			
			Image image = creatureTypes[type].getimage() ;
			Image idleGif = creatureTypes[type].getanimations()[0]; 
			Image movingUpGif = creatureTypes[type].getanimations()[1]; 
			Image movingDownGif = creatureTypes[type].getanimations()[2]; 
			Image movingLeftGif = creatureTypes[type].getanimations()[3]; 
			Image movingRightGif = creatureTypes[type].getanimations()[4] ;
			Color color = creatureTypes[type].getColor() ;	
			int[] Skill = creatureTypes[type].getSkill() ;

			Name = creatureTypes[type].getName() ;
			int Level = creatureTypes[type].getLevel() ;
			int Map = creatureTypes[type].getMap() ;
			int Continent = maps[Map].getContinent() ;
			int[] Pos = Utg.RandomPos(screenDim, new float[] {0, (float)(0.2)}, new float[] {1, 1 - (float)(screen.SkyHeight)/screenDim[1]}, new int[] {PlayerStep, PlayerStep}) ;
			if (Map == 13 | Map == 17)	// Shore creatures
			{
				Pos = Utg.RandomPos(new int[] {(int)(0.8*screenDim[0]), screenDim[1]}, new float[] {0, (float)0.2}, new float[] {1, 1 - (float)(screen.SkyHeight)/screenDim[1]}, new int[] {PlayerStep, PlayerStep}) ;
			}
			String dir = Player.MoveKeys[0] ;
			String Thought = "Exist" ;
			int[] Size = creatureTypes[type].getSize() ;
			float[] Life = creatureTypes[type].getLife() ;
			float[] Mp = creatureTypes[type].getMp() ;
			float Range = creatureTypes[type].getRange() ;
			int Step = creatureTypes[type].getStep() ;
			float[] Exp = new float[] {creatureTypes[type].getExp()} ;
			float[] Satiation = new float[] {100, 100, 1} ;
			float[] Thirst = new float[] {100, 100, 0} ;			
			PersonalAttributes PA = new PersonalAttributes(Name, new Image[] {image}, Level, Continent, Map, Pos, dir, Thought, Size, Life, Mp, Range, Step, Exp, Satiation, Thirst) ;

			float[] PhyAtk = creatureTypes[type].getPhyAtk() ;
			float[] MagAtk = creatureTypes[type].getMagAtk() ;
			float[] PhyDef = creatureTypes[type].getPhyDef() ;
			float[] MagDef = creatureTypes[type].getMagDef() ;
			float[] Dex = creatureTypes[type].getDex() ;
			float[] Agi = creatureTypes[type].getAgi() ;
			float[] Crit = creatureTypes[type].getCrit() ;
			float[] Stun = creatureTypes[type].getStun() ;
			float[] Block = creatureTypes[type].getBlock() ;
			float[] Blood = creatureTypes[type].getBlood() ;
			float[] Poison = creatureTypes[type].getPoison() ;
			float[] Silence = creatureTypes[type].getSilence() ;
			int[] Status = creatureTypes[type].getStatus() ;
			int[] SpecialStatus = creatureTypes[type].getSpecialStatus() ;
			int[][] BattleActions = creatureTypes[type].getBattleActions() ;
			BattleAttributes BA = new BattleAttributes(PhyAtk, MagAtk, PhyDef, MagDef, Dex, Agi, Crit, Stun, Block, Blood, Poison, Silence, Status, SpecialStatus, BattleActions) ;
			
			String[] Elem = creatureTypes[type].getElem() ;
			int[] Bag = creatureTypes[type].getBag() ;
			int Gold = creatureTypes[type].getGold() ;
			int[][] Actions = creatureTypes[type].getActions() ;
			int[] StatusCounter = creatureTypes[type].getStatusCounter() ;
			String[] Combo = new String[1] ;
			creature[c] = new Creatures(type, image, idleGif, movingUpGif, movingDownGif, movingLeftGif, movingRightGif, Map, Size, Skill, PA, BA, Elem, Bag, Gold, color, Actions, StatusCounter, Combo) ;	
			if (creature[c].getName().equals("Dragão") | creature[c].getName().equals("Dragon"))
			{
				creature[c].getPersonalAtt().setPos(new int[] {(int)(0.5*screenDim[0]), (int)(0.5*screenDim[1])}) ;
			}
		}
		return creature ;
    }
    public Skills[] InitializeSkills(String Language)
    {
    	int NumberOfAllSkills = 178 ;
    	int NumberOfSkills = Player.NumberOfSkillsPerJob[player.getJob()] ;
    	int NumberOfAtt = 14 ;
    	int NumberOfBuffs = 12 ;
		Skills[] skills = new Skills[NumberOfSkills] ;
		String[][] SkillsInput = Utg.ReadTextFile(CSVPath + "Skills.csv", NumberOfAllSkills, 46) ;	
		String[][] SkillsBuffsInput = Utg.ReadTextFile(CSVPath + "SkillsBuffs.csv", NumberOfAllSkills, 63) ;
		String[][] SkillsNerfsInput = Utg.ReadTextFile(CSVPath + "SkillsNerfs.csv", NumberOfAllSkills, 63) ;
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
			skills[i] = new Skills(Name, MaxLevel, MpCost, Type, PreRequisites, Cooldown, Duration, SkillBuffs[i], SkillNerfs[i], Atk, Def, Dex, Agi, AtkCrit, DefCrit, Stun, Block, Blood, Poison, Silence, Elem, SkillsInfo[i]) ;	
		}
		//System.out.println(Arrays.toString(ActivePlayerSkills)) ;
		return skills ;
    }
    public PetSkills[] InitializePetSkills()
    {
		PetSkills[] petskills = new PetSkills[Pet.NumberOfPetSkills] ;
		String[][] PetSkillsInput = Utg.ReadTextFile(CSVPath + "PetSkills.csv", 20, 19) ;	
		String[][] PetSkillsBuffsInput = Utg.ReadTextFile(CSVPath + "PetSkillsBuffs.csv", 20, 77) ;
		String[][] PetSkillsNerfsInput = Utg.ReadTextFile(CSVPath + "PetSkillsNerfs.csv", 20, 77) ;
		float[][][] PetSkillBuffs = new float[Pet.NumberOfPetSkills][14][13] ;	// [Life, MP, PhyAtk, MagAtk, PhyDef, MagDef, Dex, Agi, Crit, Stun, Block, Blood, Poison, Silence][atk chance %, atk chance, chance, def chance %, def chance, chance, atk %, atk, chance, def %, def, chance, duration]		
		float[][][] PetSkillNerfs = new float[Pet.NumberOfPetSkills][14][13] ;	// [Life, MP, PhyAtk, MagAtk, PhyDef, MagDef, Dex, Agi, Crit, Stun, Block, Blood, Poison, Silence][atk chance %, atk chance, chance, def chance %, def chance, chance, atk %, atk, chance, def %, def, chance, duration]	
		for (int i = 0 ; i <= Pet.NumberOfPetSkills - 1 ; i += 1)
		{
			int ID = i + pet.getJob()*Pet.NumberOfPetSkills ;
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
			petskills[i] = new PetSkills(PetSkillsInput[ID][4], Integer.parseInt(PetSkillsInput[ID][5]), Float.parseFloat(PetSkillsInput[ID][6]), PetSkillsInput[ID][7], new int[][] {{Integer.parseInt(PetSkillsInput[ID][8]), Integer.parseInt(PetSkillsInput[ID][9])}, {Integer.parseInt(PetSkillsInput[ID][10]), Integer.parseInt(PetSkillsInput[ID][11])}, {Integer.parseInt(PetSkillsInput[ID][12]), Integer.parseInt(PetSkillsInput[ID][13])}}, Integer.parseInt(PetSkillsInput[ID][14]), PetSkillBuffs[i], PetSkillNerfs[i], PetSkillsInput[ID][16], new String[] {PetSkillsInput[ID][17], PetSkillsInput[ID][18]}) ;	
		}
		return petskills ;
    }
    public NPCs[] InitializeNPCs(String Language, int[] screenDim)
    {
    	int NumberOfNPCs = 149 ;
    	// Doutor, Equips Seller, Items Seller, Smuggle Seller, Banker, Alchemist, Woodcrafter, Forger, Crafter, Elemental, Saver, Master, Quest 0, Citizen 0, Citizen 1, Citizen 2, Citizen 3
		NPCs[] npc = new NPCs[NumberOfNPCs] ;
		String[][] NPCsInput = Utg.ReadTextFile(CSVPath + "NPCs.csv", NumberOfNPCs, 9) ;
		String Name = "" ;
		String Info = "" ;
		int[] ColorID = new int[] {6, 10, 20, 16, 3, 23, 27, 0, 25, 7, 5, 0, 26, 18, 15, 14, 21} ;
		for (int i = 0 ; i <= NumberOfNPCs - 1 ; i += 1)
		{
			int ID = Integer.parseInt(NPCsInput[i][0]) ;
			int[] Pos = new int[] {(int)(Utg.Round(Float.parseFloat(NPCsInput[i][3]) * screenDim[0] / player.getStep(), 0) * player.getStep()), (int)(Utg.Round(Float.parseFloat(NPCsInput[i][4]) * screenDim[1] / player.getStep(), 0) * player.getStep())} ;
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
				image = Utg.ChangeImageColor(new ImageIcon(ImagesPath + "NPC.png").getImage(), new float[] {0, 0, 1, 1}, color, ColorPalette[6]) ;
			}
			if (Name.equals("Master") | Name.equals("Mestre"))
			{
				image = Utg.ChangeImageColor(new ImageIcon(ImagesPath + "PlayerFront.png").getImage(), new float[] {0, 0, 1, 1}, color, ColorPalette[6]) ;
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
    public Maps[] InitializeMaps(int[] screenDim, Buildings[] buildings)
    {
    	int NumberOfMaps = 67 ;
		Maps[] maps = new Maps[NumberOfMaps] ;
		String[][] MapsInput = Utg.ReadTextFile(CSVPath + "Maps.csv", NumberOfMaps, 22) ;	
		for (int map = 0 ; map <= NumberOfMaps - 1 ; map += 1)
		{
			String Name = MapsInput[map][1] ;
			int Continent = Integer.parseInt(MapsInput[map][2]) ;
			Image image = new ImageIcon(ImagesPath + "Map" + String.valueOf(map) + ".png").getImage() ;
			int CollectibleLevel = Integer.parseInt(MapsInput[map][3]) ;
			int[] CollectibleDelay = new int[] {Integer.parseInt(MapsInput[map][4]), Integer.parseInt(MapsInput[map][5]), Integer.parseInt(MapsInput[map][6]), Integer.parseInt(MapsInput[map][7])} ;
			int[] Connections = new int[] {Integer.parseInt(MapsInput[map][8]), Integer.parseInt(MapsInput[map][9]), Integer.parseInt(MapsInput[map][10]), Integer.parseInt(MapsInput[map][11]), Integer.parseInt(MapsInput[map][12]), Integer.parseInt(MapsInput[map][13]), Integer.parseInt(MapsInput[map][14]), Integer.parseInt(MapsInput[map][15])} ;
			int[] CreatureTypes = null ;
			int[] CreatureIDs = null ;
			for (int c = 0 ; c <= 10 - 1 ; c += 1)
			{
				if (-1 < Integer.parseInt(MapsInput[map][16 + c]))
				{
					CreatureTypes = Utg.AddVectorElem(CreatureTypes, Integer.parseInt(MapsInput[map][16 + c])) ;
				}
			}
			
			// Creating map elements
			MapElements[] MapElem = null ;
			if (!Name.contains("City"))
			{
				MapElem = new MapElements[5] ;
				float MinX = (float) 0.1 ;
				float MinY = (float)((float)(screen.SkyHeight)/screenDim[1] + 0.1) ;
				float RangeX = (float) 0.8 ;
				float RangeY = (float)(1 - MinY) ;
				for (int j = 0 ; j <= 4 ; ++j)
				{
					int[] randomPos = new int[] {Utg.RandomCoord1D(screenDim[0], MinX, RangeX, 1), Utg.RandomCoord1D(screenDim[1], MinY, RangeY, 1)} ;
					MapElem[j] = new MapElements(j, "ForestTree", randomPos, new ImageIcon(ImagesPath + "MapElem6_TreeForest.png").getImage()) ;				
				}	
			}
			
			
			maps[map] = new Maps(Name, map, Continent, image, MapElem, CollectibleLevel, CollectibleDelay, Connections, CreatureTypes, CreatureIDs) ;
			maps[map].InitializeGroundTypes(screen.SkyHeight, screen.getDimensions()) ;
			
			// Creating collectibles
			if (!maps[map].IsACity() & maps[map].getContinent() != 5 & map != 60)
			{
				for (int i = 0 ; i <= Maps.CollectibleTypes.length - 1 ; i += 1)
				{
					maps[map].CreateCollectible(screen, map, i) ;
				}
			}
		}
		
		// Setting creatures in map
		int[][] CreaturesInMapID = new int[maps.length][] ;
		int cont = 0 ;
		for (int map = 0 ; map <= maps.length - 1 ; map += 1)
		{
			if (maps[map].getCreatureTypes() != null)
			{
				for (int c = 0 ; c <= maps[map].getCreatureTypes().length - 1 ; c += 1)
				{
					if (-1 < maps[map].getCreatureTypes()[c])
					{
						CreaturesInMapID[map] = Utg.AddVectorElem(CreaturesInMapID[map], cont) ;
						maps[map].setCreatureIDs(CreaturesInMapID[map]) ;
						cont += 1 ;
					}
				}
			}
		}
		return maps ;
    }
    public Items[] InitializeItems(int DifficultLevel, String Language)
    {
    	Items[] items = new Items[Items.NumberOfAllItems] ;
		String[][] Input = Utg.ReadTextFile(CSVPath + "Items.csv", Items.NumberOfAllItems, 8) ;
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
		for (int i = 0 ; i <= Items.ItemsWithEffects.length - 1 ; ++i)
		{
			items[Items.ItemsWithEffects[i]].setBuffs(Items.ItemsBuffs[i]) ;
		}
		Items.CalcItemsWithEffects(CSVPath) ;
		
		return items ;
    }
    public Quests[] InitializeQuests(String Language, int PlayerJob)
    {
		Quests[] quest = new Quests[Quests.NumberOfQuests] ;
		String[][] Input = Utg.ReadTextFile(CSVPath + "Quests.csv", Quests.NumberOfQuests, 32) ;
		for (int i = 0 ; i <= Quests.NumberOfQuests - 1 ; i += 1)
		{
			quest[i] = new Quests(Integer.parseInt(Input[i][0])) ;
			quest[i].Initialize(CSVPath, Language, Integer.parseInt(Input[i][0]), PlayerJob) ;
		}
		
		return quest ;
    }
    public Icon[] InitializeIcons(int[] WinDim)
    {
		/* Icons' position */
		Image IconOptions = new ImageIcon(ImagesPath + "Icon0_Options.png").getImage() ;
		Image IconBag = new ImageIcon(ImagesPath + "Icon1_Bag.png").getImage() ;
		Image IconQuest = new ImageIcon(ImagesPath + "Icon2_Quest.png").getImage() ;
		Image IconMap = new ImageIcon(ImagesPath + "Icon3_Map.png").getImage() ;
		Image IconBook = new ImageIcon(ImagesPath + "Icon4_Book.png").getImage() ;
    	Image IconTent = new ImageIcon(ImagesPath + "Icon5_Tent.png").getImage() ;
    	Image PlayerImage = new ImageIcon(ImagesPath + "Player.png").getImage() ;
    	Image PetImage = new ImageIcon(ImagesPath + "PetType" + pet.getJob() + ".png").getImage() ;
		Image IconSkillsTree = new ImageIcon(ImagesPath + "Icon8_SkillsTree.png").getImage() ;
		Image IconSelectedOptions = new ImageIcon(ImagesPath + "Icon0_OptionsSelected.png").getImage() ;
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
    	int[][] SBpos = new int[SBname.length][2] ;
		int sy = 20 ;
		SBpos[0] = new int[] {WinDim[0] + 20, WinDim[1] - 230} ;
    	for (int i = 1 ; i <= 6 - 1 ; i += 1)
    	{
    		SBpos[i] = new int[] {SBpos[0][0], SBpos[i - 1][1] - SideBarIconsImages[i - 1].getHeight(null) / 2 - sy} ;
    	}
    	SBpos[6] = new int[] {SBpos[0][0], SBpos[5][1] - SideBarIconsImages[5].getHeight(null)/2 - sy} ;
    	SBpos[7] = new int[] {SBpos[0][0], SBpos[6][1] - (int)PlayerImage.getHeight(mainpanel) - SideBarIconsImages[4].getHeight(null)/2 - 20/2 - sy} ;
     	for (int i = 0 ; i <= SideBarIcons.length - 1 ; i += 1)
    	{
     		SideBarIcons[i] = new Icon(i, SBname[i], SBpos[i], null, SideBarIconsImages[i], SideBarIconsSelectedImages[i]) ;
    	}

		/* Plus sign icons */
     	plusSignIcon = new Icon[8] ;
     	int[][] PlusSignPos = new int[][] {{175, 206}, {175, 225}, {175, 450}, {175, 475}, {175, 500}, {175, 525}, {175, 550}, {175, 575}} ;
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
 		InitializeGeneralVariables(screen.getDimensions()) ;
 		player = InitializePlayer(PlayerName, PlayerJob, GameLanguage, PlayerSex) ;
		items = InitializeItems(player.DifficultLevel, GameLanguage) ;
		creatureTypes = InitializeCreatureTypes(GameLanguage, player.DifficultLevel) ;
		pet = InitializePet() ;
		buildings = InitializeBuildings(screen.getDimensions()) ;
		maps = InitializeMaps(screen.getDimensions(), buildings) ;
		creature = InitializeCreatures(creatureTypes, screen.getDimensions(), maps, player.getStep()) ;
		skills = InitializeSkills(GameLanguage) ;
		petskills = InitializePetSkills() ;
		npc = InitializeNPCs(GameLanguage, screen.getDimensions()) ;
		for (int map = 0 ; map <= maps.length - 1 ; map += 1)
		{
			maps[map].CalcNPCsInMap(npc) ;
		}
		quest = InitializeQuests(GameLanguage, player.getJob()) ;
		plusSignIcon = InitializeIcons(screen.getDimensions()) ;
		Uts.FindMaxItemNameLength(items) ;

		// Initialize classes
    	Ani = new Animations(screen.getDimensions(), AllTextCat, AllText) ;
    	B = new Battle(CSVPath, ImagesPath, skills, petskills, items, DamageAnimationType, SoundEffects, new int[] {player.getBattleAtt().getBattleActions()[0][1]/2, pet.getBattleAtt().getBattleActions()[0][1]/2}, Ani) ;
		NPC = new NPCsMethods(player, screen.getDimensions(), ImagesPath, AllText, AllTextCat, plusSignIcon) ;
	}
  	
	public void Opening(Image OpeningBG, Image OpeningGif)
	{
		DrawPrimitives DP = DF.getDrawPrimitives() ;
		Font font = new Font(MainFontName, Font.BOLD, 14) ;
		int ScreenW = screen.getDimensions()[0], ScreenH = screen.getDimensions()[1] ;
		float Textangle = DrawPrimitives.OverallAngle ;
		int TextCat = -1 ;
		Color TextColor = ColorPalette[5] ;
		if (AllTextCat != null)
		{
			TextCat = AllTextCat[2] ;
		}
		if (!Ani.isActive(20))
		{
	    	OPbuttons[0].activate() ;
	    	OPbuttons[1].activate() ;
	    	OPbuttons[2].activate() ;
	    	OPbuttons[3].activate() ;
		}
		DP.DrawImage(OpeningBG, new int[2], 0, new float[] {1, 1}, new boolean[] {false, false}, "TopLeft", 1) ;
		for (int i = 0 ; i <= OPbuttons.length - 1 ; i += 1)
		{
			if (OPbuttons[i].isActive)
			{
				OPbuttons[i].DrawImage(0, OPSelectedButton, MousePos, DP) ;
			}
		}
		if (player.action.equals("Enter"))
		{
			player.action = (String) OPbuttons[OPSelectedButton].startaction() ;
		}
		OPSelectedButton = Uts.MenuSelection(Player.ActionKeys[1], Player.ActionKeys[3], player.action, OPSelectedButton, OPbuttons.length) ;
		if (OpeningStep == 0)
		{
			if (MusicIsOn)
			{
				Utg.PlayMusic(MusicIntro) ;
			}
			OpeningStep += 1 ;
		}
		if (OpeningStep == 1)
		{
			if (player != null)
			{
				if (player.action.equals("N"))
				{
					//TutorialIsOn = true ;
					AllText = Utg.ReadTextFile(GameLanguage) ;
					AllTextCat = Uts.FindAllTextCat(AllText, GameLanguage) ;
			    	OPbuttons[2].deactivate() ;
			    	OPbuttons[3].deactivate() ;
					OpeningStep += 1 ;
				}	
				if (player.action.equals("L"))
				{
					player = new Player("", GameLanguage, PlayerSex, PlayerJob) ;
					Items.InitializeStaticVars(ImagesPath) ;
					pet = new Pet(0) ;
			    	OPbuttons[2].deactivate() ;
			    	OPbuttons[3].deactivate() ;
					LoadingGameIsOn = true ;
					InitializationIsOn = false ;
					OpeningIsOn = false ;
				}
			}
		}
		else if (OpeningStep == 2)
		{
			Font Largefont = new Font(MainFontName, Font.BOLD, 13) ;
			DP.DrawText(new int[] {(int)(0.5*ScreenW) + 20, (int)(0.25*ScreenH)}, "Center", Textangle, AllText[TextCat][1], font, TextColor) ;
			if (player.action.equals("Enter"))
			{
				Uts.LiveTyping(new int[] {(int)(0.4*ScreenW), (int)(0.3*ScreenH)}, Textangle, player.action, Largefont, TextColor, DP) ;
		    	OPbuttons[4].activate() ;
		    	OPbuttons[5].activate() ;
				OpeningStep += 1 ;
			}
			else
			{
				PlayerName = Uts.LiveTyping(new int[] {(int)(0.4*ScreenW), (int)(0.3*ScreenH)}, Textangle, player.action, Largefont, TextColor, DP) ;
			}
		}
		else if (OpeningStep == 3)
		{
			if (player.action.equals("M") | player.action.equals("F"))
			{
		    	OPbuttons[4].deactivate() ;
		    	OPbuttons[5].deactivate() ;
		    	OPbuttons[6].activate() ;
		    	OPbuttons[7].activate() ;
		    	OPbuttons[8].activate() ;
				OpeningStep += 1 ;	
				PlayerSex = player.action ;
			}
		}
		else if (OpeningStep == 4)
		{
			if (player.action.equals("0") | player.action.equals("1") | player.action.equals("2"))
			{
		    	OPbuttons[6].deactivate() ;
		    	OPbuttons[7].deactivate() ;
		    	OPbuttons[8].deactivate() ;
		    	OPbuttons[9].activate() ;
		    	OPbuttons[10].activate() ;
		    	OPbuttons[11].activate() ;
		    	OPbuttons[12].activate() ;
		    	OPbuttons[13].activate() ;
				player.DifficultLevel = Integer.parseInt(player.action) ;
				OpeningStep += 1 ;
			}
		}
		else if (OpeningStep == 5)
		{
			int TextL = 15 ;
			int sx = (int)(0.21*ScreenW), sy = 20 ;
			Font smallfont = new Font("BoldSansSerif", Font.BOLD, 12) ;
			DP.DrawText(new int[] {(int)(0.35*ScreenW), (int)(0.1*ScreenH)}, "BotLeft", Textangle, AllText[TextCat][5], font, TextColor) ;		
			for (int i = 0 ; i <= 5 - 1 ; i += 1)
			{
				int[] Pos = {(int) (0.01 * ScreenW + i * sx), (int) (0.28 * ScreenH)} ;
				int L = TextL * font.getSize() * 9 / 20, H = (int) (12.4 * sy);
				DF.DrawMenuWindow(Pos, L, H, null, 0, Player.ClassColors[i], ColorPalette[7]) ;
				DP.DrawFitText(new int[] {Pos[0] + 5, Pos[1] + 5}, sy, "TopLeft", AllText[TextCat][11 + i], smallfont, TextL, TextColor) ;	
			}
			if (player.action.equals("0") | player.action.equals("1") | player.action.equals("2") | player.action.equals("3") | player.action.equals("4"))
			{
		    	OPbuttons[9].deactivate() ;
		    	OPbuttons[10].deactivate() ;
		    	OPbuttons[11].deactivate() ;
		    	OPbuttons[12].deactivate() ;
		    	OPbuttons[13].deactivate() ;
				OpeningStep += 1 ;	
				PlayerJob = Integer.parseInt(player.action) ;
				OPbuttons[0].deactivate() ;
				OPbuttons[1].deactivate() ;
				OpeningIsOn = false ;
			}			
		}
	}
	
	public void Loading(int LoadingGameTab)
	{
		DrawPrimitives DP = DF.getDrawPrimitives() ;
		Font font = new Font("BoldSansSerif", Font.BOLD, 20) ;
		int ScreenL = screen.getDimensions()[0], ScreenH = screen.getDimensions()[1] ;
		int NumberOfSlots = 3 ;
		int NumberOfTabs = 2 ;
		int NumberOfUsedSlots = 0 ;
		//Object[] LoadingResult = new Object[2] ;
		for (int i = 0 ; i <= NumberOfSlots - 1 ; ++i)
		{
			if (Utg.FindFile("save" + (i + 1) + ".txt"))
			{
				NumberOfUsedSlots += 1 ;
			}
		}
		if (Utg.FindFile("save" + (LoadingSelectedSlot + 1) + ".txt"))
		{
			player = null ;
			pet = null ;
			player.Load("save" + (LoadingSelectedSlot + 1) + ".txt", pet, maps) ;
			//Items.EquipsBonus = (float[][]) Utg.ConvertDoubleArray(Utg.deepToString(ReadFile[2*(NumberOfPlayerAttributes + 33)], Items.EquipsBonus[0].length), "String", "float") ;
			//FirstNPCContact = (int[]) UtilGeral.ConvertArray(UtilGeral.toString(ReadFile[2*(NumberOfPlayerAttributes + 34)]), "String", "int") ;
			//DifficultLevel = Integer.parseInt(ReadFile[2*(NumberOfPlayerAttributes + 35)][0]) ;
			if (player == null)
			{
				DP.DrawText(new int[] {(int) (0.5*ScreenL), (int) (0.5*ScreenH)}, "Center", DrawPrimitives.OverallAngle, "Save version is old, do you want to convert to a new version?", font, ColorPalette[5]) ;
			}
			else
			{
		 		//pet = (Pet)LoadingResult[1] ;	
		 		//Items.EquipsBonus = (float[][]) LoadingResult[2] ;
		 		//player.DifficultLevel = (int)LoadingResult[3] ;
		 		GameLanguage = player.getLanguage() ;
		 		AllText = Utg.ReadTextFile(GameLanguage) ;
				AllTextCat = Uts.FindAllTextCat(AllText, GameLanguage) ;
		 		buildings = InitializeBuildings(screen.getDimensions()) ;
				maps = InitializeMaps(screen.getDimensions(), buildings) ;
				creatureTypes = InitializeCreatureTypes(GameLanguage, player.DifficultLevel) ;
				creature = InitializeCreatures(creatureTypes, screen.getDimensions(), maps, player.getStep()) ;
				skills = InitializeSkills(GameLanguage) ;
				petskills = InitializePetSkills() ;
				npc = InitializeNPCs(GameLanguage, screen.getDimensions()) ;
				for (int map = 0 ; map <= maps.length - 1 ; map += 1)
				{
					maps[map].CalcNPCsInMap(npc) ;
				}
				items = InitializeItems(player.DifficultLevel, GameLanguage) ;
				quest = InitializeQuests(GameLanguage, player.getJob()) ;
				Uts.FindMaxItemNameLength(items) ;
				DF.DrawLoadingGameScreen(player, pet, items, plusSignIcon, screen.getDimensions(), LoadingSelectedSlot, NumberOfUsedSlots, CoinIcon) ;
			}
	 	}
		else
		{
			DF.DrawEmptyLoadingSlot(screen.getDimensions(), LoadingSelectedSlot, NumberOfSlots - 1) ;
		}
		LoadingSelectedSlot = Uts.MenuSelection(Player.ActionKeys[1], Player.ActionKeys[3], player.action, LoadingSelectedSlot, NumberOfSlots - 1) ;
		LoadingGameTab = Uts.MenuSelection(Player.ActionKeys[0], Player.ActionKeys[2], player.action, LoadingGameTab, NumberOfTabs) ;
		if (player.action.equals("Enter"))
		{
			LoadingSelectedSlot += 1 ;
			if (0 < LoadingSelectedSlot & LoadingSelectedSlot <= NumberOfSlots)
			{
				if (Utg.FindFile("save" + LoadingSelectedSlot + ".txt"))
				{
					//LoadingResult = Uts.Load("save" + LoadingSelectedSlot + ".txt") ;
					//player = (Player)LoadingResult[0] ;
			 		//pet = (Pet)LoadingResult[1] ;
			 		//Items.EquipsBonus = (float[][]) LoadingResult[2] ;
			 		PlayerSex = player.getSex() ;		
			 		GameLanguage = player.getLanguage() ;	
			 		AllText = Utg.ReadTextFile(GameLanguage) ;		
				}
				LoadingGameIsOn = false ;
				CustomizationIsOn = true ;
			}
		}
	}
	
	public void Tutorial(Player player, int[] meet)
	{
		DrawPrimitives DP = DF.getDrawPrimitives() ;
		int TextCat = AllTextCat[3] ;
		int L = (int)(screen.getDimensions()[0]), H = (int)(0.12*screen.getDimensions()[1]) ;
		int[] Pos = new int[] {0, H} ;
		Font font = new Font("SansSerif", Font.BOLD, 15) ;
		int TextL = 95 ;
		int sy = Utg.TextH(font.getSize()) + 2 ;
		if (TutorialStep <= 13)
		{
			DP.DrawRoundRect(Pos, "BotLeft", L, H, 1, ColorPalette[7], ColorPalette[8], true) ;
			DP.DrawText(new int[] {Pos[0] + 5, Pos[1] + 5 - Utg.TextH(font.getSize())}, "BotLeft", DrawPrimitives.OverallAngle, AllText[TextCat][1], font, ColorPalette[5]) ;
			DP.DrawFitText(new int[] {Pos[0] + 5, Pos[1] - H + 5 + Utg.TextH(font.getSize())}, sy, "BotLeft", AllText[TextCat][TutorialStep + 2], font, TextL, ColorPalette[5]) ;					
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
			if (player.action.equals(Player.ActionKeys[4]))
			{
				player.getBag()[0] += 1 ;
				int ItemID = 0 ;		// obtained item: hp potion lv 0
				Ani.SetAniVars(0, new Object[] {100, items, new int[] {ItemID}}) ;
				Ani.StartAni(0) ;
				TutorialStep += 1 ;
			}
		}
		else if (TutorialStep == 4)
		{
			if (player.getBag()[0] == 0)
			{
				int ItemID = Items.BagIDs[5] + (Items.BagIDs[6] - Items.BagIDs[5]) / 5 * player.getJob() ;		// obtained item: first equip
				player.getBag()[ItemID] += 1 ;
				Ani.SetAniVars(0, new Object[] {100, items, new int[] {ItemID}}) ;
				Ani.StartAni(0) ;
				TutorialStep += 1 ;
			}
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
			DF.DrawNPCsIntro(player, npc, new float[] {1, 1}, maps[player.getMap()].NPCsInMap) ;
			if (player.action.equals("Enter"))
			{
				TutorialStep += 1 ;
			}
		}
		else if (TutorialStep == 9)
		{
			//Ani.SetAniVars(17, new Object[] {10, player.getMap(), CrazyArrowImage}) ;
			//Ani.StartAni(17) ;
			if (!maps[player.getMap()].IsACity())
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
			if (0 < player.getBag()[Items.BagIDs[0]] | 0 < player.getBag()[Items.BagIDs[0] + 1] | 0 < player.getBag()[Items.BagIDs[0] + 2] | 0 < player.getBag()[Items.BagIDs[3]] | player.action.equals("Space"))
			{
				player.getBag()[Items.BagIDs[3]] += 5 ;
				Ani.SetAniVars(0, new Object[] {100, items, new int[] {Items.BagIDs[3]}}) ;
				Ani.StartAni(0) ;
				TutorialStep += 1 ;
			}
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
				/*
				 * This is an animation
				 * 
				 * DP.DrawRect(Pos, "Left", L, H, 1, ColorPalette[7], ColorPalette[9], true) ;
					DP.DrawText(new int[] {Pos[0] + 5, Pos[1] + 5 - Utg.TextH(FontSize)}, "Left", DrawPrimitives.OverallAngle, AllText[TextCat][1], TextFont, FontSize, ColorPalette[5]) ;		
					DP.DrawFitText(new int[] {Pos[0] + 5, Pos[1] - H + 5 + Utg.TextH(FontSize)}, sy, "Left", AllText[TextCat][16], TextFont, TextL, TextFont.getSize(), ColorPalette[5]) ;
				else if (NPcContactAnimationIsOver)
				{
					TutorialStep += 1 ;
				}*/
			}
			else if (meet[0] == 1 & meet[1] == 7)	// First time meeting the forger
			{
				if (npc[meet[1]].Firstcontact)
				{
					player.getBag()[Items.BagIDs[1]] += 1 ;
					Ani.SetAniVars(0, new Object[] {100, items, new int[] {Items.BagIDs[1]}}) ;
					Ani.StartAni(0) ;
				}
				npc[meet[1]].Firstcontact = false ;
				/*
				 * This is an animation
				 * 
				 * if (FirstNPCContact[npc[meet[1]].getID()] <= 300)
				{
					DP.DrawRect(Pos, "Left", L, H, 1, ColorPalette[7], ColorPalette[9], true) ;
					DP.DrawText(new int[] {Pos[0] + 5, Pos[1] + 5 - Utg.TextH(FontSize)}, "Left", DrawPrimitives.OverallAngle, AllText[TextCat][1], TextFont, FontSize, ColorPalette[5]) ;		
					DP.DrawFitText(new int[] {Pos[0] + 5, Pos[1] - H + 5 + Utg.TextH(FontSize)}, sy, "Left", AllText[TextCat][17], TextFont, TextL, TextFont.getSize(), ColorPalette[5]) ;					
				}
				else if (FirstNPCContact[npc[meet[1]].getID()] == 301)
				{
					TutorialStep += 1 ;
				}*/
			}
			else if (meet[0] == 1 & meet[1] == 11)	// First time meeting the master
			{
				if (npc[meet[1]].Firstcontact)
				{
					player.addSkillPoint(1) ;
				}
				npc[meet[1]].Firstcontact = false ;
				/*
				 * This is an animation
				 * 
				 * if (FirstNPCContact[npc[meet[1]].getID()] <= 300)
				{
					DP.DrawRect(Pos, "Left", L, H, 1, ColorPalette[7], ColorPalette[9], true) ;
					DP.DrawText(new int[] {Pos[0] + 5, Pos[1] + 5 - Utg.TextH(FontSize)}, "Left", DrawPrimitives.OverallAngle, AllText[TextCat][1], TextFont, FontSize, ColorPalette[5]) ;		
					DP.DrawFitText(new int[] {Pos[0] + 5, Pos[1] - H + 5 + Utg.TextH(FontSize)}, sy, "Left", AllText[TextCat][18], TextFont, TextL, TextFont.getSize(), ColorPalette[5]) ;					
				}
				else if (FirstNPCContact[npc[meet[1]].getID()] == 301)
				{
					TutorialStep += 1 ;
				}*/
			}
		}
		else if (TutorialStep == 17)
		{
			TutorialIsOn = false ;
		}
	}
		
	public void Customization()
	{
		//player.Customization(Player.ActionKeys, Player.MoveKeys) ;
		player.setSize(new int[] {player.getPersonalAtt().getimage()[0].getWidth(mainpanel), player.getPersonalAtt().getimage()[0].getHeight(mainpanel)}) ;
		int SelectedOption = player.SelectedOption ;
		int[] CurrentColorValues = new int[] {player.getColors()[0].getRed(), player.getColors()[0].getGreen(), player.getColors()[0].getBlue()} ;
		DF.DrawCustomizationMenu(player, SelectedOption, CurrentColorValues) ;	
		if (player.action.equals("Enter"))
		{
			CustomizationIsOn = false ;
			RunGame = true ;
			if (MusicIsOn)
			{
				Utg.SwitchMusic(MusicIntro, Music[Maps.Music[player.getMap()]]) ;
			}
		}
	}
		
	public void IncrementCounters(Player player, Pet pet, Creatures[] creature)
	{
		DayCounter += 1 ;
		for (int c = 0 ; c <= Cloud.length - 1 ; c += 1)
		{
			Cloud[c].IncCounter(700) ;
		}
		player.IncActionCounters() ;
		player.SupSkillCounters(skills, creature, player.CreatureInBattle) ;
		pet.IncActionCounters() ;
		if (maps[player.getMap()].hasCreatures())
		{
			for (int c = 0 ; c <= maps[player.getMap()].getCreatureIDs().length - 1 ; c += 1)
			{
				int ID = maps[player.getMap()].getCreatureIDs()[c] ;
				creature[ID].IncActionCounters() ;
			}
		}
		if (player.isInBattle())
		{
			player.IncBattleActionCounters() ;
			pet.IncBattleActionCounters() ;
			/*for (int c = 0 ; c <= maps[player.getMap()].getCreatureIDs().length - 1 ; c += 1)
			{
				int ID = maps[player.getMap()].getCreatureIDs()[c] ;
				creature[ID].IncBattleActionCounters() ;
			}*/
			creature[player.CreatureInBattle].IncBattleActionCounters() ;
		}
		if (!maps[player.getMap()].IsACity())
		{
			maps[player.getMap()].IncCollectiblesCounter() ;
		}
	}
	
	public void ActivateCounters(Player player, Pet pet, Creatures[] creature, int DayCounter, int DayDuration)
	{	
		if (DayCounter % DayDuration == 0)
		{
			DayCounter = 0 ;
		}
		player.ActivateActionCounters(Ani.SomeAnimationIsActive()) ;
		if (0 < pet.getLife()[0])
		{
			pet.ActivateActionCounters(Ani.SomeAnimationIsActive()) ;
		}
		if (!maps[player.getMap()].IsACity())	// player is out of the cities
		{
			for (int i = 0 ; i <= maps[player.getMap()].getCreatureIDs().length - 1 ; ++i)
			{
				int ID = maps[player.getMap()].getCreatureIDs()[i] ;
				if (!Ani.isActive(12) & !Ani.isActive(13) & !Ani.isActive(14) & !Ani.isActive(18))
				{
					creature[ID].ActivateActionCounters() ;
				}
			}
		}
		if (!maps[player.getMap()].IsACity())
		{
			maps[player.getMap()].CreateCollectibles(screen) ;
		}
	}
	
	public void KonamiCode()
	{
		//OpeningScreenImages[2] = ElementalCircle ;
		ColorPalette = Uts.ReadColorPalette(new ImageIcon(ImagesPath + "ColorPalette.png").getImage(), "Konami") ;
		if (DayCounter % 1200 <= 300)
		{
			DrawPrimitives.OverallAngle += 0.04 ;
		}
		else if (DayCounter % 1200 <= 900)
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
		ActivateCounters(player, pet, creature, DayCounter, DayDuration) ;
		if (Uts.KonamiCodeActivated(player.getCombo()))
		{
			KonamiCode() ;
		}
		DF.DrawFullMap(screen, player, pet, maps, npc, buildings, skills, SideBarIcons, items, MousePos, DayCounter, Cloud, Star, SkillCooldownImage, SkillSlotImage) ;
		/*if (TutorialIsOn)
		{
			Tutorial(player, meet) ;
		}*/
		
		// Creatures move
		if (maps[player.getMap()].hasCreatures())
		{
			for (int c = 0 ; c <= maps[player.getMap()].getCreatureIDs().length - 1 ; c += 1)
			{
				int id = maps[player.getMap()].getCreatureIDs()[c] ;
				creature[id].Think() ;	
				if (creature[id].getPersonalAtt().getThought().equals("Move"))
				{
					creature[id].Move(player.getPos(), creature[id].getFollow(), player.getMap(), screen, maps) ;
					if (creature[id].countmove % 5 == 0)
					{
						creature[id].getPersonalAtt().setdir(creature[id].getPersonalAtt().randomDir()) ;	// set random direction
					}
					if (creature[id].getActions()[0][2] == 1)	// If the creature can move
					{
						creature[id].ResetActions() ;
					}
				}
				creature[id].Draw(DF) ;
				//repaint() ;
			}
		}
		
		// Player acts
		player.act(pet, maps, skills, screen, MousePos, OPbuttons[OPSelectedButton], SideBarIcons, Ani) ;
		if (player.ActionIsAMove(player.action) | 0 < player.countmove)	// countmove becomes 0 < when the player moves, then starts to increase 1 by 1 and returns to 0 when it reaches 20
		{
			player.Move(pet, maps, screen, Music, MusicIsOn, Ani) ;
		}
		
		// Draw player stuff
		DF.DrawPlayerAttributes(player, (int)player.OptionStatus[3]) ;
		player.DrawPlayer(player.getPos(), new float[] {1, 1}, player.getDir(), (boolean)player.OptionStatus[2], DF.getDrawPrimitives()) ;
		if (0 < player.getEquips()[0])	// If the player is equipped
		{
			DF.DrawPlayerWeapon(player, player.getPos(), new float[] {1, 1}) ;
		}
		
		// Draw pet stuff
		if (0 < pet.getLife()[0])		// If the pet is alive
		{
			pet.setCombo(Uts.RecordCombo(pet.getCombo(), pet.action, 1)) ;
			pet.Move(player, maps) ;
			if (player.isInBattle())
			{
				pet.action = pet.Action(Player.ActionKeys) ;
			}
			DF.DrawPetAttributes(pet) ;
			DF.DrawPet(pet.getPos(), new float[] {1, 1}, pet.getPersonalAtt().getimage()[0]) ;
		}
		
		
		player.ClosestCreature = Uts.ClosestCreatureInRange(player, creature, maps, screen) ;	// find the closest creature to the player
		
		// check if the player met something
		if (!player.isInBattle())
		{
			int[] meet = player.meet(creature, npc, maps[player.getMap()], player.CreatureInBattle, Ani) ;	// meet[0] is the encounter and meet[1] is its id
			if (meet[0] == 0 & 0 <= meet[1])	// meet with creature
			{
				creature[meet[1]].setFollow(true) ;
				player.setCurrentAction("Fighting") ;	
				player.CreatureInBattle = meet[1] ;
				player.AddCreatureToBestiary(meet[1]) ;
			}
			if (meet[0] == 1 & 0 <= meet[1])	// meet with npc
			{
				NPC.Contact(player, pet, creatureTypes, creature, skills, maps, npc[meet[1]], items, quest, MousePos, TutorialIsOn, CoinIcon, Ani, DF) ;				
			}
			if (meet[0] == 2 & 0 <= meet[1])	// meet with collectibles
			{
				player.Collect(meet[1], screen, maps, items, AllText, AllTextCat, DF, Ani) ;
			}
			if (meet[0] == 3 & 0 <= meet[1])	// meet with chest
			{
				player.TreasureChestReward(meet[1], CoinIcon, maps, items, Ani) ;
			}
		}
		
		// if the player is in battle, run battle
		if (player.isInBattle() & !Ani.isActive(12) & !Ani.isActive(13) & !Ani.isActive(14) & !Ani.isActive(16))	// only enter battle if the animations for win (12), level up (13), pet level up (14), and pterodactile (16) are off
		{
			B.RunBattle(player, pet, creature[player.CreatureInBattle], screen, skills, petskills, player.activeSpells(skills), items, quest, SoundEffectsAreOn, MousePos, DF) ;
		}
		
		// check if the player and the pet have leveled up
		if (!Ani.isActive(12) & Uts.CheckLevelUP(player, Ani))
		{
			float[] AttributesIncrease = Uts.LevelUpIncAtt(player.getAttIncrease()[player.getProJob()], player.getChanceIncrease()[player.getProJob()], player.getLevel()) ;
			player.LevelUp(AttributesIncrease) ;
			Ani.SetAniVars(13, new Object[] {150, AttributesIncrease, player.getLevel(), player.getColors()[0]}) ;
			Ani.StartAni(13) ;
		}
		if (Uts.CheckLevelUP(pet, Ani))
		{
			float[] AttributesIncrease = Uts.LevelUpIncAtt(Pet.AttributeIncrease, Pet.ChanceIncrease, pet.getLevel()) ;
			pet.LevelUp(AttributesIncrease) ;
			Ani.SetAniVars(14, new Object[] {150, pet, AttributesIncrease}) ;
			Ani.StartAni(14) ;
		}
		
		// Draw player windows
		//ShowPlayerWindows() ;
		player.ShowWindows(pet, creature, creatureTypes, quest, screen, items, maps, plusSignIcon, B, AllText, AllTextCat, Music, MusicIsOn, SoundEffectsAreOn, MousePos, CoinIcon, DF) ;
		
		// if tutorial is on, draw tutorial animations
		if (TutorialIsOn)
		{
			DF.TutorialAnimations(player, TutorialStep) ;
		}		
		
		// move the active projectiles and check if they collide with something
		if (proj != null)
		{
			for (int p = 0 ; p <= proj.length - 1 ; p += 1)
			{
				proj[p].go(player, maps[player.getMap()].creaturesinmap(creature), pet, DF.getDrawPrimitives()) ;
			}
			if (proj[0].collidedwith(player, maps[player.getMap()].creaturesinmap(creature), pet) != - 3)
			{
				proj = null ;
			}
		}
		//repaint() ;
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
        super.paintComponent(g) ;
		MousePos = Utg.GetMousePos(mainpanel) ;
        DF = new DrawFunctions(g, screen.getDimensions(), ColorPalette, DayDuration, AllText, AllTextCat) ;
        DF.InitializeVariables(ImagesPath) ;
		
    	if (OpeningIsOn)
		{
			//Opening(OpeningBG, OpeningGif) ;
        	
    		// Quick start
        	GameLanguage = "P" ;
    		AllText = Utg.ReadTextFile(GameLanguage) ;
    		AllTextCat = Uts.FindAllTextCat(AllText, GameLanguage) ;
        	PlayerName = "" ;
        	PlayerJob = 2 ;
        	PlayerSex = "N" ;
    		MainInitialization() ;
        	player.setMap(2, maps) ;
        	//Arrays.fill(player.getSkill(), 5) ;
    		Arrays.fill(player.getQuestSkills(), true) ;
        	Arrays.fill(player.getBag(), 30) ;
        	player.setPos(new int[] {screen.getDimensions()[0] / 2 + 160, screen.getDimensions()[1] / 2}) ;
        	OpeningIsOn = false ;
        	InitializationIsOn = false ;
        	LoadingGameIsOn = false ;
        	CustomizationIsOn = false ;
        	MusicIsOn = false ;
        	if (MusicIsOn)
			{
				Utg.PlayMusic(Music[Maps.Music[player.getMap()]]) ;
			}
        	RunGame = true ;
    		
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
			DF.DrawLoadingText(LoadingGif, screen.getCenter()) ;
			MainInitialization() ;
			InitializationIsOn = false ;
			CustomizationIsOn = true ;
		} 
		else if (LoadingGameIsOn)
		{
	 		InitializeGeneralVariables(screen.getDimensions()) ;
			Loading(LoadingGameTab) ;
			//InitializeClasses() ;
		}
		else if (CustomizationIsOn)
		{
			Customization() ;
		} else if (RunGame)
		{
			RunGame(DF) ;
			/*IncrementCounters(player, pet, creature) ;
			ActivateCounters(player, pet, creature, DayCounter, DayDuration) ;
        	B.RunBattle(player, pet, creature[player.CreatureInBattle], screen, skills, petskills, player.activeSpells(skills), items, quest, SoundEffectsAreOn, MousePos, DF) ;*/
        	//player.AttWindow(AllText, AllTextCat, items, plusSignIcon, screen.getDimensions(), player.getAttIncrease()[player.getProJob()], MousePos, CoinIcon, DF.getDrawPrimitives()) ;			
    	}
		Ani.RunAnimation(DF) ;	// run all the active animations
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
							AllText = Utg.ReadTextFile(GameLanguage) ;
							AllTextCat = Uts.FindAllTextCat(AllText, GameLanguage) ;
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
