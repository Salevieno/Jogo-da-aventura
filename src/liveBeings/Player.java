package liveBeings ;

import java.awt.Color ;
import java.awt.Dimension;
import java.awt.Font ;
import java.awt.Image ;
import java.awt.Point;
import java.awt.event.KeyEvent ;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList ;
import java.util.Arrays ;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;

import java.util.Map;

import components.GameIcon;
import components.Items;
import components.NPCs;
import components.QuestSkills;
import components.Quests;
import components.SpellTypes;
import graphics.Animations;
import graphics.DrawingOnPanel;
import items.Alchemy;
import items.Arrow;
import items.Equip;
import items.Fab;
import items.Food;
import items.Forge;
import items.GeneralItem;
import items.Item;
import items.PetItem;
import items.Potion;
import items.QuestItem;
import items.Recipe;
import main.AtkResults;
import main.Battle;
import main.Game;
import maps.Collectible;
import maps.FieldMap;
import maps.GameMap;
import screen.Screen;
import screen.SideBar;
import utilities.Align;
import utilities.AttackEffects;
import utilities.Directions;
import utilities.Scale;
import utilities.TimeCounter;
import utilities.UtilG;
import utilities.UtilS;
import windows.BagWindow;
import windows.BestiaryWindow;
import windows.FabWindow;
import windows.HintsWindow;
import windows.MapWindow;
import windows.PlayerAttributesWindow;
import windows.QuestWindow;
import windows.SettingsWindow;
import windows.SpellsTreeWindow;

public class Player extends LiveBeing
{
	private String language ;
	private String sex ;
	private Color color ;
	
	private BagWindow bag ;
	private SettingsWindow settings ;
	private SpellsTreeWindow spellsTree ;
	private MapWindow mapWindow ;
	private ArrayList<Recipe> knownRecipes ;
	private FabWindow fabWindow ;
	private ArrayList<Quests> quest ;	
	private QuestWindow questWindow ;
	private HintsWindow hintsWindow ;
	public BestiaryWindow bestiary ;
	
	private int attPoints ;			// attribute points available (to upgrade the attributes)
	private int spellPoints ;		// spell points available (to upgrade the spells)
	private double[] collectLevel ;	// 0: herb, 1: wood, 2: metal
	private TimeCounter collectCounter ;	// counts the progress of the player's collection
	private Equip[] equips ;		// 0: weapon, 1: shield, 2: armor, 3: arrow
	private int[] gold ;			// 0: current, 1: stored
	private double goldMultiplier ;	// multiplies the amount of gold the player wins
	private Map<QuestSkills, Boolean> questSkills ;	// skills gained with quests
	private boolean isRiding ;		// true if the player is riding
	private int moveRange ;			// number of steps the player moves per action
    
	public Creature closestCreature ;	// creature that is currently closest to the player
    private Creature opponent ;		// creature that is currently in battle with the player
    public Map<String, String[]> allText ;	// All the text in the game in the player language
	public Items[] hotItem ;		// items on the hotkeys
    
    public static final Image CollectingMessage = UtilG.loadImage(Game.ImagesPath + "\\Collect\\" + "CollectingMessage.gif") ;   
    public static final Image TentImage = UtilG.loadImage(Game.ImagesPath + "\\Icons\\" + "Icon5_Tent.png") ; 
    public static final Image DragonAuraImage = UtilG.loadImage(Game.ImagesPath + "\\Player\\" + "DragonAura.png") ;
    public static final Image RidingImage = UtilG.loadImage(Game.ImagesPath + "\\Player\\" + "Tiger.png") ;
    //public static final Image SpeakingBubbleImage = UtilG.loadImage(Game.ImagesPath + "\\Player\\" + "SpeakingBubble.png") ;
	public static final Image CoinIcon = UtilG.loadImage(Game.ImagesPath + "\\Player\\" + "CoinIcon.png") ;    
	public static final Image MagicBlissGif = UtilG.loadImage(Game.ImagesPath + "\\Player\\" + "MagicBliss.gif") ;
    public static final Image FishingGif = UtilG.loadImage(Game.ImagesPath + "\\Player\\" + "Fishing.gif") ;
	
	// \*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/
    
    
	// TODO essa pode virar um Map
	private double[] statistics ;	// 0: Number of phy attacks, 1: number of skills used, 2: number of defenses, 3: total phy damage inflicted, 4: total phy damage received, 5: total mag damage inflicted, 6: total mag damage received, 7: total phy damage defended, 8: total mag damage defended, 9: total hits inflicted, 10: total hits received, 11: total dodges, 12: number of crit, 13: total crit damage, 14: total stun, 15: total block, 16: total blood, 17: total blood damage, 18: total blood def, 19: total poison, 20: total poison damage, 21: total poison def, 22: total silence
	
	
	private double[][] attIncrease ;	// Amount of increase in each attribute when the player levels up
	private double[][] chanceIncrease ;	// Chance of increase of these attributes
	public double[][] equipsBonus ;
	
	public static final String[] MoveKeys = new String[] {KeyEvent.getKeyText(KeyEvent.VK_UP), KeyEvent.getKeyText(KeyEvent.VK_LEFT), KeyEvent.getKeyText(KeyEvent.VK_DOWN), KeyEvent.getKeyText(KeyEvent.VK_RIGHT)} ;
	public static String[] ActionKeys = new String[] {"W", "A", "S", "D", "B", "C", "F", "M", "P", "Q", "H", "R", "T", "Z"} ;	// [Up, Left, Down, Right, Bag, Char window, Pet window, Map, Quest, Hint, Tent, Bestiary]
	public static String[] HotKeys = new String[] {"E", "X", "V"} ;	// [Hotkey 1, Hotkey 2, Hotkey 3]
	public static String[] SpellKeys = new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "F10", "F11", "F12"} ;
	
	
	
	public final static ArrayList<String[]> Properties = UtilG.ReadcsvFile(Game.CSVPath + "PlayerInitialStats.csv") ;
	public final static ArrayList<String[]> EvolutionProperties = UtilG.ReadcsvFile(Game.CSVPath + "PlayerEvolution.csv") ;	
	public final static int[] NumberOfSpellsPerJob = new int[] {14, 15, 15, 14, 14} ;
	public final static int[] CumNumberOfSpellsPerJob = new int[] {0, 34, 69, 104, 138} ;
    public final static Image[] AttWindowImages = new Image[] {
    		UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "PlayerAttWindow1.png"),
    		UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "PlayerAttWindow2.png"),
    		UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "PlayerAttWindow3.png")} ;
    public static final Color[] ClassColors = new Color[] {Game.ColorPalette[0], Game.ColorPalette[1], Game.ColorPalette[2], Game.ColorPalette[3], Game.ColorPalette[4]} ;
	
	public Player(String name, String Language, String Sex, int job)
	{
		super(
				InitializePersonalAttributes(name, job),
				InitializeBattleAttributes(job),
				InitializeMovingAnimations(),
				new PlayerAttributesWindow(AttWindowImages[0])
			) ;
		this.name = name ;
		this.job = job ;
		proJob = 0 ;
		level = 0 ;
		if (Game.getMaps() != null)
		{
			map = Game.getMaps()[job];
		}
		pos = new Point();
		dir = Directions.up;
		state = LiveBeingStates.idle;
		Image PlayerBack = UtilG.loadImage(Game.ImagesPath + "\\Player\\" + "PlayerBack.gif") ;
	    size = new Dimension (PlayerBack.getWidth(null), PlayerBack.getHeight(null));
		range = Double.parseDouble(Properties.get(job)[4]) ;
		step = Integer.parseInt(Properties.get(job)[33]);
	    elem = new String[] {"n", "n", "n", "n", "n"};
		mpCounter = new TimeCounter(0, Integer.parseInt(Properties.get(job)[37])) ;
		satiationCounter = new TimeCounter(0, Integer.parseInt(Properties.get(job)[38])) ;
		moveCounter = new TimeCounter(0, Integer.parseInt(Properties.get(job)[39])) ;
		battleActionCounter = new TimeCounter(0, Integer.parseInt(Properties.get(job)[41])) ;
		stepCounter = 0 ;
		combo = new ArrayList<>() ;
	    
		this.language = Language ;
		this.sex = Sex ;
		
		
		spells = new ArrayList<Spell>() ;
		allText = UtilG.ReadTextFile(Language) ;

		//Bag = new int[Items.NumberOfAllItems] ;
		questWindow = new QuestWindow() ;
		bag = new BagWindow(new HashMap<Potion, Integer>(), new HashMap<Alchemy, Integer>(), new ArrayList<Forge>(), new ArrayList<PetItem>(), new ArrayList<Food>(),
				new ArrayList<Arrow>(), new ArrayList<Equip>(), new ArrayList<GeneralItem>(), new ArrayList<Fab>(), new ArrayList<QuestItem>()) ;
		if (job == 2)
		{
			for (int i = 0; i <= 100 - 1; i += 1)
			{
				bag.getArrow().add(Arrow.getAll()[0]) ;	// TODO how to add multiple items at the same time?
			}
		}
		quest = new ArrayList<>() ;
		knownRecipes = new ArrayList<>() ;
		fabWindow = new FabWindow() ;
		mapWindow = new MapWindow() ;
		hintsWindow = new HintsWindow() ;
		bestiary = new BestiaryWindow() ;
		equips = new Equip[4] ;	// 0: weapon, 1: shield, 2: armor, 3: arrow
		spellPoints = 0 ;
		color = Game.ColorPalette[12] ;

    	
		//ElemMult = new double[10] ;
		//Arrays.fill(ElemMult, 1) ;
		collectLevel = new double[3] ;
		gold = new int[2] ;
		goldMultiplier = Integer.parseInt(Properties.get(job)[32]) ; 
		questSkills = new HashMap<QuestSkills, Boolean>() ;
		questSkills.put(QuestSkills.forestMap, false) ;
		questSkills.put(QuestSkills.caveMap, false) ;
		questSkills.put(QuestSkills.islandMap, false) ;
		questSkills.put(QuestSkills.volcanoMap, false) ;
		questSkills.put(QuestSkills.snowlandMap, false) ;
		questSkills.put(QuestSkills.shovel, false) ;
		questSkills.put(QuestSkills.craftWindow, false) ;
		questSkills.put(QuestSkills.ride, false) ;
		questSkills.put(QuestSkills.dragonAura, false) ;
		questSkills.put(QuestSkills.bestiary, false) ;
		isRiding = false ;
		moveRange = 20 ;
		/*if (spell != null)
		{
			spellIsActive = new boolean[spell.size()] ;
			spellDurationCounter = new int[spell.size()] ;
			spellCooldownCounter = new int[spell.size()] ;
		}*/
		//statusCounter = new int[8] ;
		statistics = new double[23] ;
		collectCounter = new TimeCounter(0, 300) ;
		Arrays.fill(BA.getSpecialStatus(), -1) ;
		attPoints = 0 ;
		attIncrease = new double[3][8] ;
		chanceIncrease = new double[3][8] ;
		for (int i = 0 ; i <= 3 - 1 ; ++i)	// job, PA.ProJob 1, PA.ProJob 2
		{
			for (int j = 0 ; j <= 7 ; ++j)
			{
				attIncrease[i][j] = Double.parseDouble(EvolutionProperties.get(3 * job + i)[j + 2]) ;
				chanceIncrease[i][j] = Double.parseDouble(EvolutionProperties.get(3 * job + i)[j + 10]) ;
			}
		}

		closestCreature = null ;
	    opponent = null ;
	    //difficultLevel = 1 ;
		equipsBonus = Items.EquipsBonus ;
		settings = new SettingsWindow(UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "windowSettings.png"), false, true, false, 1, 1) ;
		/*switch (Job)
		{
			case 0:
			{
				spellsTree = new SpellsTreeWindow(getKnightSpells(), spellPoints, color) ;
			}
			case 1:
			{
				spellsTree = new SpellsTreeWindow(spells, spellPoints, color) ;
			}
			case 2:
			{
				spellsTree = new SpellsTreeWindow(spells, spellPoints, color) ;
			}
			case 3:
			{
				spellsTree = new SpellsTreeWindow(spells, spellPoints, color) ;
			}
			case 4:
			{
			}
		}*/
		spellsTree = new SpellsTreeWindow(spells.toArray(new Spell[0]), spellPoints, color) ;
		hotItem = new Items[3] ;
		
	}
	

	private static PersonalAttributes InitializePersonalAttributes(String Name, int Job)
	{
	    BasicAttribute Life = new BasicAttribute(Integer.parseInt(Properties.get(Job)[2]), Integer.parseInt(Properties.get(Job)[2]), 1) ;
	    BasicAttribute Mp = new BasicAttribute(Integer.parseInt(Properties.get(Job)[3]), Integer.parseInt(Properties.get(Job)[3]), 1) ;
		BasicAttribute Exp = new BasicAttribute(0, 5, Integer.parseInt(Properties.get(Job)[34])) ;
		BasicAttribute Satiation = new BasicAttribute(100, 100, Integer.parseInt(Properties.get(Job)[35])) ;
		BasicAttribute Thirst = new BasicAttribute(100, 100, Integer.parseInt(Properties.get(Job)[36])) ;
		return new PersonalAttributes(Life, Mp, Exp, Satiation,	Thirst) ;
	}
	
	private static BattleAttributes InitializeBattleAttributes(int Job)
	{

		BasicBattleAttribute PhyAtk = new BasicBattleAttribute (Double.parseDouble(Properties.get(Job)[5]), 0, 0) ;
		BasicBattleAttribute MagAtk = new BasicBattleAttribute (Double.parseDouble(Properties.get(Job)[6]), 0, 0) ;
		BasicBattleAttribute PhyDef = new BasicBattleAttribute (Double.parseDouble(Properties.get(Job)[7]), 0, 0) ;
		BasicBattleAttribute MagDef = new BasicBattleAttribute (Double.parseDouble(Properties.get(Job)[8]), 0, 0) ;
		BasicBattleAttribute Dex = new BasicBattleAttribute (Double.parseDouble(Properties.get(Job)[9]), 0, 0) ;	
		BasicBattleAttribute Agi = new BasicBattleAttribute (Double.parseDouble(Properties.get(Job)[10]), 0, 0) ;
		double[] Crit = new double[] {Double.parseDouble(Properties.get(Job)[11]), 0, Double.parseDouble(Properties.get(Job)[12]), 0} ;
		double[] Stun = new double[] {Double.parseDouble(Properties.get(Job)[13]), 0, Double.parseDouble(Properties.get(Job)[14]), 0, Double.parseDouble(Properties.get(Job)[15])} ;
		double[] Block = new double[] {Double.parseDouble(Properties.get(Job)[16]), 0, Double.parseDouble(Properties.get(Job)[17]), 0, Double.parseDouble(Properties.get(Job)[18])} ;
		double[] Blood = new double[] {Double.parseDouble(Properties.get(Job)[19]), 0, Double.parseDouble(Properties.get(Job)[20]), 0, Double.parseDouble(Properties.get(Job)[21]), 0, Double.parseDouble(Properties.get(Job)[22]), 0, Double.parseDouble(Properties.get(Job)[23])} ;
		double[] Poison = new double[] {Double.parseDouble(Properties.get(Job)[24]), 0, Double.parseDouble(Properties.get(Job)[25]), 0, Double.parseDouble(Properties.get(Job)[26]), 0, Double.parseDouble(Properties.get(Job)[27]), 0, Double.parseDouble(Properties.get(Job)[28])} ;
		double[] Silence = new double[] {Double.parseDouble(Properties.get(Job)[29]), 0, Double.parseDouble(Properties.get(Job)[30]), 0, Double.parseDouble(Properties.get(Job)[31])} ;
		int[] Status = new int[9] ;
		int[] SpecialStatus = new int[5] ;

		return new BattleAttributes(PhyAtk, MagAtk, PhyDef, MagDef, Dex, Agi, Crit, Stun, Block, Blood, Poison, Silence, Status, SpecialStatus) ;
	
	}
	
	private static MovingAnimations InitializeMovingAnimations()
	{
	    Image idleGif = UtilG.loadImage(Game.ImagesPath + "\\Player\\" + "PlayerIdle.gif") ;
	    Image movingUpGif = UtilG.loadImage(Game.ImagesPath + "\\Player\\" + "PlayerBack.gif") ;
		Image movingDownGif = UtilG.loadImage(Game.ImagesPath + "\\Player\\" + "PlayerFront.gif") ;
		Image movingLeftGif = UtilG.loadImage(Game.ImagesPath + "\\Player\\" + "PlayerMovingLeft.gif") ;
		Image movingRightGif = UtilG.loadImage(Game.ImagesPath + "\\Player\\" + "PlayerRight.gif") ;
		
		return new MovingAnimations(idleGif, movingUpGif, movingDownGif, movingLeftGif, movingRightGif) ;
	}
	
	public void InitializeSpells()
    {
		spells = new ArrayList<>() ;
		SpellType[] allSpellTypes = Game.getAllSpellTypes() ;
		
    	//int NumberOfAllSkills = 178 ;
    	int NumberOfSpells = Player.NumberOfSpellsPerJob[job] ;
    	//int NumberOfAtt = 14 ;
    	//int NumberOfBuffs = 12 ;
    	//ArrayList<String[]> spellsInput = UtilG.ReadcsvFile(Game.CSVPath + "Spells.csv") ;	
    	/*ArrayList<String[]> spellsBuffsInput = UtilG.ReadcsvFile(Game.CSVPath + "SpellsBuffs.csv") ;
    	ArrayList<String[]> spellsNerfsInput = UtilG.ReadcsvFile(Game.CSVPath + "SpellsNerfs.csv") ;
		double[][][] spellBuffs = new double[NumberOfSpells][NumberOfAtt][NumberOfBuffs] ;	// [Life, MP, PhyAtk, MagAtk, PhyDef, MagDef, Dex, Agi, Crit, Stun, Block, Blood, Poison, Silence][atk chance %, atk chance, chance, def chance %, def chance, chance, atk %, atk, chance, def %, def, chance]		
		double[][][] spellNerfs = new double[NumberOfSpells][NumberOfAtt][NumberOfBuffs] ;	// [Life, MP, PhyAtk, MagAtk, PhyDef, MagDef, Dex, Agi, Crit, Stun, Block, Blood, Poison, Silence][atk chance %, atk chance, chance, def chance %, def chance, chance, atk %, atk, chance, def %, def, chance]		
		String[][] spellsInfo = new String[NumberOfSpells][2] ;
		*/for (int i = 0 ; i <= NumberOfSpells - 1 ; i += 1)
		{
			/*int ID = i + Player.CumNumberOfSpellsPerJob[job] ;
			int BuffCont = 0, NerfCont = 0 ;
			for (int j = 0 ; j <= NumberOfAtt - 1 ; j += 1)
			{
				if (j == 11 | j == 12)
				{
					for (int k = 0 ; k <= NumberOfBuffs - 1 ; k += 1)
					{
						spellBuffs[i][j][k] = Double.parseDouble(spellsBuffsInput.get(ID)[BuffCont + 3]) ;
						spellNerfs[i][j][k] = Double.parseDouble(spellsNerfsInput.get(ID)[NerfCont + 3]) ;
						NerfCont += 1 ;
						BuffCont += 1 ;
					}
				}
				else
				{
					spellBuffs[i][j][0] = Double.parseDouble(spellsBuffsInput.get(ID)[BuffCont + 3]) ;
					spellBuffs[i][j][1] = Double.parseDouble(spellsBuffsInput.get(ID)[BuffCont + 4]) ;
					spellBuffs[i][j][2] = Double.parseDouble(spellsBuffsInput.get(ID)[BuffCont + 5]) ;
					spellNerfs[i][j][0] = Double.parseDouble(spellsNerfsInput.get(ID)[NerfCont + 3]) ;
					spellNerfs[i][j][1] = Double.parseDouble(spellsNerfsInput.get(ID)[NerfCont + 4]) ;
					spellNerfs[i][j][2] = Double.parseDouble(spellsNerfsInput.get(ID)[NerfCont + 5]) ;
					NerfCont += 3 ;
					BuffCont += 3 ;
				}
			}*/
			/*if (Language.equals("P"))
			{
				spellsInfo[i] = new String[] {spellsInput.get(ID)[42], spellsInput.get(ID)[43]} ;
			}
			else if (Language.equals("E"))
			{
				spellsInfo[i] = new String[] {spellsInput.get(ID)[44], spellsInput.get(ID)[45]} ;
			}
			String Name = spellsInput.get(ID)[4] ;
			int MaxLevel = Integer.parseInt(spellsInput.get(ID)[5]) ;
			double MpCost = Double.parseDouble(spellsInput.get(ID)[6]) ;
			SpellTypes Type ;
			if (spellsInput.get(ID)[7].equals("Active"))
			{
				Type = SpellTypes.active ;
			}
			else if (spellsInput.get(ID)[7].equals("Passive"))
			{
				Type = SpellTypes.passive ;
			}
			else if (spellsInput.get(ID)[7].equals("Offensive"))
			{
				Type = SpellTypes.offensive ;
			}
			else
			{
				Type = SpellTypes.support ;
			}
			Map<Spell, Integer> preRequisites = new HashMap<>() ;
			for (int p = 0 ; p <= 6 - 1 ; p += 2)
			{
				if (-1 < Integer.parseInt(spellsInput.get(ID)[p + 8]))
				{
					preRequisites.put(spell.get(Integer.parseInt(spellsInput.get(ID)[p + 8])), Integer.parseInt(spellsInput.get(ID)[p + 9])) ;
				}
			}
			int Cooldown = Integer.parseInt(spellsInput.get(ID)[14]) ;
			int Duration = Integer.parseInt(spellsInput.get(ID)[15]) ;
			double[] Atk = new double[] {Double.parseDouble(spellsInput.get(ID)[16]), Double.parseDouble(spellsInput.get(ID)[17])} ;
			double[] Def = new double[] {Double.parseDouble(spellsInput.get(ID)[18]), Double.parseDouble(spellsInput.get(ID)[19])} ;
			double[] Dex = new double[] {Double.parseDouble(spellsInput.get(ID)[20]), Double.parseDouble(spellsInput.get(ID)[21])} ;
			double[] Agi = new double[] {Double.parseDouble(spellsInput.get(ID)[22]), Double.parseDouble(spellsInput.get(ID)[23])} ;
			double[] AtkCrit = new double[] {Double.parseDouble(spellsInput.get(ID)[24])} ;
			double[] DefCrit = new double[] {Double.parseDouble(spellsInput.get(ID)[25])} ;
			double[] Stun = new double[] {Double.parseDouble(spellsInput.get(ID)[26]), Double.parseDouble(spellsInput.get(ID)[27]), Double.parseDouble(spellsInput.get(ID)[28])} ;
			double[] Block = new double[] {Double.parseDouble(spellsInput.get(ID)[29]), Double.parseDouble(spellsInput.get(ID)[30]), Double.parseDouble(spellsInput.get(ID)[31])} ;
			double[] Blood = new double[] {Double.parseDouble(spellsInput.get(ID)[32]), Double.parseDouble(spellsInput.get(ID)[33]), Double.parseDouble(spellsInput.get(ID)[34])} ;
			double[] Poison = new double[] {Double.parseDouble(spellsInput.get(ID)[35]), Double.parseDouble(spellsInput.get(ID)[36]), Double.parseDouble(spellsInput.get(ID)[37])} ;
			double[] Silence = new double[] {Double.parseDouble(spellsInput.get(ID)[38]), Double.parseDouble(spellsInput.get(ID)[39]), Double.parseDouble(spellsInput.get(ID)[40])} ;
			String Elem = spellsInput.get(ID)[41] ;*/
			spells.add(new Spell(allSpellTypes[CumNumberOfSpellsPerJob[job] + i])) ;	
			
			// new Spell(Name, MaxLevel, MpCost, Type, preRequisites, Cooldown, Duration, spellBuffs[i], spellNerfs[i],
			//Atk, Def, Dex, Agi, AtkCrit, DefCrit, Stun, Block, Blood, Poison, Silence, Elem, spellsInfo[i])
		}
		spells.get(0).incLevel(1) ;
    }
	
	public String getLanguage() {return language ;}
	public String getSex() {return sex ;}
	public Directions getDir() {return dir ;}
	public Color getColor() {return color ;}
	public ArrayList<Spell> getSpell() {return spells ;}
	public ArrayList<Quests> getQuest() {return quest ;}
	public BagWindow getBag() {return bag ;}
	public Equip[] getEquips() {return equips ;}
	public int getSpellPoints() {return spellPoints ;}
	public BasicAttribute getLife() {return PA.getLife() ;}
	public BasicAttribute getMp() {return PA.getMp() ;}
	public BasicBattleAttribute getPhyAtk() {return BA.getPhyAtk() ;}
	public BasicBattleAttribute getMagAtk() {return BA.getMagAtk() ;}
	public BasicBattleAttribute getPhyDef() {return BA.getPhyDef() ;}
	public BasicBattleAttribute getMagDef() {return BA.getMagDef() ;}
	public BasicBattleAttribute getDex() {return BA.getDex() ;}
	public BasicBattleAttribute getAgi() {return BA.getAgi() ;}
	public double[] getCrit() {return BA.getCrit() ;}
	public double[] getStun() {return BA.getStun() ;}
	public double[] getBlock() {return BA.getBlock() ;}
	public double[] getBlood() {return BA.getBlood() ;}
	public double[] getPoison() {return BA.getPoison() ;}
	public double[] getSilence() {return BA.getSilence() ;}
	public double[] getCollect() {return collectLevel ;}
	public int[] getGold() {return gold ;}
	public BasicAttribute getExp() {return PA.getExp() ;}
	public BasicAttribute getSatiation() {return PA.getSatiation() ;}
	public BasicAttribute getThirst() {return PA.getThirst() ;}
	public Map<QuestSkills, Boolean> getQuestSkills() {return questSkills ;}
	public double[] getStats() {return statistics ;}
	public int getAttPoints() {return attPoints ;}
	public double[][] getAttIncrease() {return attIncrease ;}
	public double[][] getChanceIncrease() {return chanceIncrease ;}
	public double[][] getEquipsBonus() {return equipsBonus ;}
	public SettingsWindow getSettings() {return settings ;}
	public SpellsTreeWindow getSpellsTreeWindow() {return spellsTree ;}
	public Creature getOpponent() { return opponent ;}
	private Point feetPos() {return new Point(pos.x, (int) (pos.y - size.height)) ;}
	
	public static SpellType[] getKnightSpells() { return Arrays.copyOf(Game.getAllSpellTypes(), 14) ;}
	public static SpellType[] getMageSpells() { return Arrays.copyOfRange(Game.getAllSpellTypes(), 34, 49) ;}
	public static SpellType[] getArcherSpells() { return Arrays.copyOfRange(Game.getAllSpellTypes(), 70, 84) ;}
	public static SpellType[] getAnimalSpells() { return Arrays.copyOfRange(Game.getAllSpellTypes(), 105, 118) ;}
	public static SpellType[] getThiefSpells() { return Arrays.copyOfRange(Game.getAllSpellTypes(), 139, 152) ;}
	
	public void resetOpponent() { opponent = null ;}
	
	public String[] getAtkElems()
	{
		return new String[] {elem[0], elem[1], elem[4]} ;
	}
	public boolean weaponIsEquipped()
	{
		return (equips[0] != null) ;
	}
	public boolean arrowIsEquipped()
	{
		return (equips[3] != null) ;
	}
	
	public boolean canAct()
	{
		return moveCounter.finished() ;
	}
	private void Collect(Collectible collectible, DrawingOnPanel DP, Animations ani)
    {
		collectCounter.inc() ;
        Image collectingGif = UtilG.loadImage(Game.ImagesPath + "\\Collect\\" + "Collecting.gif") ;
        DP.DrawGif(collectingGif, getPos(), Align.center);
        if (collectCounter.finished())
        {
        	collectCounter.reset() ;
            collectingGif.flush() ;
            
            // remove the collectible from the list
            GameMap currentMap = map ;
			if (currentMap.isAField())
			{
				FieldMap fm = (FieldMap) getMap() ;
				ArrayList<Collectible> collectibles = fm.getCollectibles() ;
				collectibles.remove(collectible) ;
			}
        }
    }
	public void useItem(Item item)
	{
		if (item != null)	// if the item is valid
		{
			if (item instanceof Potion)	// potions
			{
				Potion pot = (Potion) item ;
				System.out.println("player used " + pot.getName() + " selected item is " + item.getName());
				double PotMult = 1 ;
				if (getJob() == 3)
				{
					PotMult += 0.06 * spells.get(7).getLevel() ;
				}
				
				PA.getLife().incCurrentValue((int) (pot.getLifeHeal() * PA.getLife().getMaxValue() * PotMult)); ;
				PA.getMp().incCurrentValue((int) (pot.getMPHeal() * PA.getMp().getMaxValue() * PotMult)); ;
				
				bag.Remove(pot, 1);				
			}
		}
	}	
	private boolean actionIsAMove()
	{
		List<String> moveKeys = Arrays.asList(MoveKeys) ;
		return moveKeys.contains(currentAction) ;
	}
	
	private void startMoving()
	{
		state = LiveBeingStates.moving ;
		stepCounter = 1 ;
	}
	
	public boolean isMoving()
	{
		return (state.equals(LiveBeingStates.moving)) ;
	}
	
	public boolean isDoneMoving()
	{
		return (moveRange - 1 <= stepCounter) ;
	}
	
	public void ResetAction() {currentAction = null ;}
	

	public void move(Pet pet, Animations Ani)
	{
		if (Ani.isActive(10) | Ani.isActive(12) | Ani.isActive(13) | Ani.isActive(15) | Ani.isActive(18))
		{
			return ;
		}	
		
		Point newPos = CalcNewPos() ;
		if (Game.getScreen().posIsInMap(newPos))
		{	
			if (map.GroundIsWalkable(newPos, elem[4]))
			{
				setPos(newPos) ;
			}	
		}
		else
		{
			MoveToNewMap(pet, currentAction, settings.getMusicIsOn(), Ani) ;
		}
		
		stepCounter = (stepCounter + 1) % moveRange ;
	}
	
	// \*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/
	

	public List<Spell> GetActiveSpells()
	{
		List<Spell> activeSpells = new ArrayList<Spell>() ;
		for (int i = 0 ; i <= spells.size() - 1 ; i += 1)
		{
			if (spells.get(i).getType() != null)	// TODO every spell should have a type, update input file
			{
				if (!spells.get(i).getType().equals(SpellTypes.passive) & 0 < spells.get(i).getLevel())
				{
					activeSpells.add(spells.get(i)) ;
				}
			}
		}
		return activeSpells ;
	}

	private ArrayList<Integer> GetActiveQuests()
	{
		ArrayList<Integer> ActiveQuests = new ArrayList<Integer>() ;
		for (int i = 0 ; i <= quest.size() - 1 ; i += 1)
		{
			if (quest.get(i).isActive())
			{
				ActiveQuests.add(i) ;
			}
		}
		return ActiveQuests ;
	}	
	
	
	
//	private void addSpellPoint(int amount) {spellPoints += amount ;}
//	private void incRange(double incR) {PA.setRange(PA.getRange() + incR) ;}
//	private void incAttPoints(int amount) {attPoints += amount ;}
	
	public void decAttPoints(int amount) {attPoints += -amount ;}
	
	public boolean hasTheSpell(String action) {return UtilG.IndexOf(Player.SpellKeys, action) < GetActiveSpells().size() ;}
	public boolean hasEnoughMP(Spell spell)	{return (spell.getMpCost() <= PA.getMp().getCurrentValue()) ;}
	public boolean shouldLevelUP() {return getExp().getMaxValue() <= getExp().getCurrentValue() ;}	
	

	public void acts(Pet pet, Point MousePos, SideBar sideBar, Animations Ani)
	{
		if (actionIsAMove())
		{
			switch (currentAction)
			{
				case "Acima": setDir(Directions.up) ; break ;
				case "Abaixo": setDir(Directions.down) ; break ;
				case "Esquerda": setDir(Directions.left) ; break ;
				case "Direita": setDir(Directions.right) ; break ;
			}
			startMoving() ;
		}
		if (currentAction.equals("MouseLeftClick"))
		{
			sideBar.getIcons().forEach(icon ->
			{
				if (icon.ishovered(MousePos))
				{
					switch (icon.getName())
					{
						case "Settings":  settings.open() ; break ;
						case "Bag": bag.open() ; break ;
						case "QuestWindow": questWindow.open() ; break ;
						case "QuestSkills": if (questSkills.get(QuestSkills.getContinentMap(map.getContinentName(this).name()))) mapWindow.open() ; break ;
						case "FabWindow": fabWindow.open() ; break ;
						case "AttWindow": attWindow.open() ; break ;
						case "Pet": if (pet.isAlive()) pet.getAttWindow().open() ; break ;
					}
				}
			});
			/*for (int i = 0 ; i <= sideBar.getIcons().size() - 1 ; i += 1)	// + 2 to account for player and pet
			{
				if (sideBar.getIcons()[i].ishovered(MousePos))
				{
					switch (i)
					{
						case 0: settings.open() ; break ;
						case 1: bag.open() ; break ;
						case 2: questWindow.open() ; break ;
						case 3: if (questSkills.get(QuestSkills.getContinentMap(map.getContinentName(this).name()))) mapWindow.open() ; break ;
						case 4: fabWindow.open() ; break ;
						case 5: attWindow.open() ; break ;
						case 6: if (pet.isAlive()) pet.getAttWindow().open() ; break ;
					}
				}
			}*/
		}
		if (currentAction.equals(ActionKeys[4]))	// Bag
		{
			bag.open() ;
		}
		if (currentAction.equals(ActionKeys[5]))	// Player window
		{
			attWindow.open() ;
		}
		/*if (action.equals(ActionKeys[6]))	// Fishing
		{
			String WaterPos = Uts.CheckAdjacentGround(getPos(), screen, maps[getMap()], "Water");
			if (0 < getBag()[1340] & (WaterPos.contains("Touching") | WaterPos.equals("Inside")))
			{
				Ani.SetAniVars(15, new Object[] {200, getPos(), FishingGif, WaterPos}) ;
				Ani.StartAni(15) ;
			}
		}*/
		if (currentAction.equals(ActionKeys[7]))	// Map  & questSkills.get(QuestSkills.getContinentMap(map.getContinentName(this).name()))
		{
			mapWindow.open() ;
		}
		/*if (currentAction.equals(ActionKeys[8]))												// settings window
		{
			settings.open() ;
		}*/
		if (currentAction.equals(ActionKeys[8]) & pet != null)								// Pet window
		{
			pet.getAttWindow().open() ;
		}
		if (currentAction.equals(ActionKeys[9]))												// Quest window
		{
			questWindow.open() ;
		}
		if (currentAction.equals(ActionKeys[10]))											// Hints window
		{			
			hintsWindow.open() ;
		}
		if (currentAction.equals(ActionKeys[11]) & questSkills.get(QuestSkills.ride))		// Ride
		{
			ActivateRide() ;
		}
		if (currentAction.equals(ActionKeys[12]) & !isInBattle())							// Tent
		{
			//	 TODO add tent gif
//			Ani.SetAniVars(11, new Object[] {100, getPos(), TentImage}) ;
//			Ani.StartAni(11) ;
		}
		if (currentAction.equals(ActionKeys[13]) &  questSkills.get(QuestSkills.bestiary))	// Bestiary
		{
			bestiary.open() ;
		}
		
		// support spells
//		System.out.println("isInBattle() = " + isInBattle());
//		System.out.println("canAtk() = " + canAtk());
//		System.out.println("UtilG.IndexOf(SpellKeys, currentAction) < GetActiveSpells().size() = " + (UtilG.IndexOf(SpellKeys, currentAction)));
		if (actionIsSpell())
		{
//			System.out.println("actionIsASpell() = " + actionIsASpell());
			int spellID = UtilG.IndexOf(SpellKeys, currentAction) ;
			Spell spell = spells.get(spellID);
			//System.out.println("spell cooldown " + spell.getCooldownCounter());
			if (spell.isReady() & spell.getMpCost() <= PA.getMp().getCurrentValue() & 0 < spell.getLevel())
			{
				System.out.println("Used a support spell = " + spells.get(spellID));
				UseSupportSpell(pet, spells.get(spellID)) ;
				getStats()[1] += 1 ;	// Number of mag atks
			}
		}
		//		if (actionIsASpell() & (!isInBattle() | canAtk()) & UtilG.IndexOf(SpellKeys, currentAction) < GetActiveSpells().size())
//		{
//			SupSpell(pet, GetActiveSpells().get(UtilG.IndexOf(SpellKeys, currentAction))) ;
//		}

		// navigating through open windows
		if (bag.isOpen())
		{
			bag.navigate(currentAction) ;
			if (currentAction.equals("Enter") | currentAction.equals("MouseLeftClick"))
			{
				useItem(bag.getSelectedItem()) ;
			}
		}
		if (fabWindow.isOpen())
		{
			fabWindow.navigate(currentAction) ;
		}
		if (questWindow.isOpen())
		{
			questWindow.navigate(currentAction) ;
		}
		if (settings.isOpen())
		{
			settings.navigate(currentAction) ;
		}
		if (attWindow.isOpen())
		{
			attWindow.navigate(this, currentAction, MousePos) ;
		}
		if (hintsWindow.isOpen())
		{
			hintsWindow.navigate(currentAction) ;
		}
		
		// if meets creature, enters battle
		if (closestCreature != null & (currentAction.equals(ActionKeys[1]) | actionIsSpell()) & !isInBattle())
		{
			if (job != 2 | (job == 2 & equips[3] != null))
			{
				opponent = closestCreature ;
				setState(LiveBeingStates.fighting) ;
			}
		}
		
		// Check if there is an animation on, if not, check if there is some ground effect to apply
		if (!Ani.SomeAnimationIsActive())
		{
			receiveAdjacentGroundEffect(map) ;
		}
		
		UpdateCombo() ;
		//ResetAction() ;
	}
	public void meet(Creature[] creatures, DrawingOnPanel DP, Animations ani)
	{
		double distx, disty ;
		GameMap currentMap = map ;
		if (currentMap.isAField())
		{
			FieldMap fm = (FieldMap) getMap() ;
			
			// Meeting with collectibles
			List<Collectible> collectibles = fm.getCollectibles() ;
			for (int c = 0 ; c <= collectibles.size() - 1 ; c += 1)
			{
				distx = (double) Math.abs(pos.x - collectibles.get(c).getPos().x) ;
				disty = (double) Math.abs(pos.y - collectibles.get(c).getPos().y) ;
				if (distx <= 0.5*size.width & disty <= 0.5*size.height)
				{
					/*if (!ani.isActive(10))
					{
						// start animation
						ani.SetAniVars(10, new Object[] {100, pos, 10, collectibles.get(c).getType(), "Coletando"}) ;
						ani.StartAni(10) ;
					}*/
					setState(LiveBeingStates.collecting);
					Collect(collectibles.get(c), DP, ani) ;
					//return new int[] {2, collectibles.get(c).getType()} ;
				}
			}

			// Meeting with chests
			String groundType = currentMap.groundTypeAtPoint(pos) ;
			if (groundType != null)	// TODO are we going to use groundType?
			{
				if (groundType.contains("Chest"))
				{
					//return new int[] {3, Integer.parseInt(groundType.substring(groundType.length() - 1))} ;
				}
			}
			
			// Meeting with creatures
			if (isInBattle())
			{
				distx = Math.abs(pos.x - opponent.getPos().x) ;
				disty = Math.abs(pos.y - size.height / 2 - opponent.getPos().y) ;
				if (distx <= (size.width + opponent.getSize().width) / 2 & disty <= (size.height + opponent.getSize().height) / 2 & !ani.isActive(10) & !ani.isActive(19))
				{
					//return new int[] {0, opponent.getType().getID()} ;
				}
			}
			else
			{
				ArrayList<Creature> creaturesInMap = fm.getCreatures() ;
				for (int i = 0 ; i <= creaturesInMap.size() - 1 ; i += 1)
				{
					Creature creature = creaturesInMap.get(i) ;
					distx = UtilG.dist1D(pos.x, creature.getPos().x) ;
					disty = UtilG.dist1D(pos.y - size.height / 2, creature.getPos().y) ;
					if (distx <= (size.width + creature.getSize().width) / 2 & disty <= (size.height + creature.getSize().height) / 2 & !ani.isActive(10) & !ani.isActive(19))
					{
						opponent = creaturesInMap.get(i) ;
						opponent.setFollow(true) ;
						setState(LiveBeingStates.fighting) ;
						bestiary.addDiscoveredCreature(opponent.getType()) ;
					}
				}
			}
		}	
		
		// Meeting with NPCs
		if (currentMap.getNPCs() != null)
		{
			for (NPCs npc : currentMap.getNPCs())
			{
				boolean meetingNPC = UtilG.isInside(this.getPos(), UtilG.getPosAt(npc.getPos(), Align.topLeft, this.getSize()), this.getSize()) ;
				if (meetingNPC)
				{
					npc.Contact(this, null, creatures, null, null, null, false, ani, DP) ;
				}
			}	
		}
	}
	
	private void receiveAdjacentGroundEffect(GameMap map)
	{
		if (UtilS.CheckAdjacentGround(pos, map, "Lava").equals("Inside") & !elem[4].equals("f"))
		{
			PA.getLife().incCurrentValue(-5) ;
		}
		if (UtilS.isAdjacentTo(pos,  map, "Water"))
		{
			PA.getThirst().incCurrentValue(1) ;
		}
	}
	private void MoveToNewMap(Pet pet, String action, boolean MusicIsOn, Animations Ani)
	{
		Screen screen = Game.getScreen() ;
		int nextMapID = -1 ;
		Point currentPos = new Point(pos.x, pos.y) ;
		Point nextPos = new Point(0, 0) ;
		int[] mapCon = map.getConnections() ;

		switch (dir)
		{
			case up:
			nextPos = new Point(currentPos.x, screen.getBorders()[3] - step) ;
			if (-1 < mapCon[0] & currentPos.x <= screen.getSize().width / 2)
			{
				nextMapID = mapCon[0] ;
			}
			else if (-1 < mapCon[7] & screen.getSize().width / 2 < currentPos.x)
			{
				nextMapID = mapCon[7] ;
			}
			
			break ;
			
			case left:
			nextPos = new Point(screen.getBorders()[2] - step, currentPos.y) ;
			if (-1 < mapCon[1] & currentPos.y <= screen.getSize().height / 2)
			{
				nextMapID = mapCon[1] ;
			}
			else if (-1 < mapCon[2] & screen.getSize().height / 2 < currentPos.y)
			{
				nextMapID = mapCon[2] ;
			}
			
			break ;
			
			case down:
			nextPos = new Point(currentPos.x, screen.getBorders()[1] + step) ;
			if(-1 < mapCon[3] & currentPos.x <= screen.getSize().width / 2)
			{
				nextMapID = mapCon[3] ;
			}
			else if(-1 < mapCon[4] & screen.getSize().width / 2 < currentPos.x)
			{
				nextMapID = mapCon[4] ;	
			}
			
			break ;
			
			case right:
			nextPos = new Point(screen.getBorders()[0] + step, currentPos.y) ;
			if(-1 < mapCon[5] & screen.getSize().height / 2 < currentPos.y)
			{
				nextMapID = mapCon[5] ;
			}
			else if(-1 < mapCon[6] & currentPos.y <= screen.getSize().height / 2)
			{
				nextMapID = mapCon[6] ;
			}
			
			break ;
			
		}
		if (-1 < nextMapID)
		{
			if (Game.getMaps()[nextMapID].GroundIsWalkable(nextPos, elem[4]))
			{
				setMap(Game.getMaps()[nextMapID]) ;
				setPos(nextPos) ;	
//				if (pet.isAlive())
//				{
//					pet.setPos(nextPos) ;
//				}
//				if (map.IsACity())
//				{
//					ResetAction() ;
//				}
			}
			

			if (!isInBattle() & getContinent() == 3)
			{
				Pterodactile.speak(Ani) ;
			}
		}
	}
	public void SupSpellCounters(Creature[] creature, Creature creatureID)
	{
		for (Spell spell : spells)
		{
			if (spell.isActive())
			{
				spell.getDurationCounter().inc() ;
			}
			if (spell.getDurationCounter().finished())
			{
				/*ResetSpellDurationCounter(s) ;
				for (int i = 0 ; i <= spells.get(s).getBuffs().length - 1 ; ++i)
				{
					ApplyBuffsAndNerfs("deactivate", "buffs", i, s, SpellIsActive(s)) ;
				}
				for (int i = 0 ; i <= spells.get(s).getNerfs().length - 1 ; ++i)
				{
					if (creatureID != null)
					{
						creatureID.ApplyBuffsAndNerfs("deactivate", "nerfs", i, spells.get(s).getLevel(), spells.get(s), SpellIsActive(s)) ;
					}
				}*/
				spell.getDurationCounter().reset();
				for (Buff buff : spell.getBuffs())
				{
					applyBuff(false, buff, spell.getLevel());
				}
				spell.deactivate() ;
			}
			spell.getCooldownCounter().inc() ;
		}
	}
	private void Tent()
	{
		PA.getLife().setToMaximum() ;
	}
	private void ActivateRide()
	{
		if (isRiding)
		{
			setStep(step / 2) ;
		}
		else
		{
			setStep(2 * step) ;
		}
		isRiding = !isRiding ;
	}
		
	
	
	// called every time the window is repainted
	public void ShowWindows(Pet pet, Creature[] creature, CreatureTypes[] creatureTypes, GameMap[] maps, Battle B, Point MousePos, DrawingOnPanel DP)
	{
		if (bag.isOpen())
		{
			bag.display(MousePos, allText.get("* Menus da mochila *"), DP) ;
		}
		if (attWindow.isOpen())
		{
			attWindow.display(this, allText, equips, equipsBonus,  MousePos, DP);
		}
		if (fabWindow.isOpen())
		{		
			fabWindow.display(knownRecipes, MousePos, DP) ;
		}
		//Tent() ;	// TODO create tent
		if (pet != null)
		{
			if (pet.getAttWindow().isOpen())
			{
				pet.getAttWindow().display(pet, allText, null, null, MousePos, DP);
			}
		}
		if (mapWindow.isOpen())
		{
			mapWindow.display(map, DP) ;
		}		
		if (questWindow.isOpen())
		{
			questWindow.display(quest, DP) ;
		}
		if (bestiary.isOpen())
		{
			bestiary.display(this, MousePos, DP) ;
		}
		if (settings.isOpen())
		{
			settings.display(allText.get("* Menu de opções *"), DP) ;
		}
		if (hintsWindow.isOpen())
		{
			hintsWindow.display(this, DP) ;
		}
	}
	
			
	
	private void TreasureChestReward(int ChestID, GameMap[] maps, Animations Ani)
	{
		int[][] ItemRewards = new int[][] {{}, {455 + (Items.BagIDs[6] - Items.BagIDs[5]) / 5 * job}, {456 + (Items.BagIDs[6] - Items.BagIDs[5]) / 5 * job}, {457 + (Items.BagIDs[6] - Items.BagIDs[5]) / 5 * job}, {}, {134, 134, 134, 134, 134, 135, 135, 135, 135, 135}, {1346, 1348, 1349, 1350, 1351}} ;
		int[][] GoldRewards = new int[][] {{2000, 2000, 2000, 2000, 2000}, {}, {}, {}, {4000, 4000, 4000, 4000, 4000}, {}, {}} ;
		if (job == 2)
		{
			ItemRewards = new int[][] {{}, {456 + (Items.BagIDs[6] - Items.BagIDs[5]) / 5 * job}, {457 + (Items.BagIDs[6] - Items.BagIDs[5]) / 5 * job}, {32, 33, 34, 35, 36, 37, 38, 39}, {}, {134, 134, 134, 134, 134, 135, 135, 135, 135, 135}, {1346, 1348, 1349, 1350, 1351}} ;
		}
		
		for (int j = 0 ; j <= ItemRewards.length - 1 ; j += 1)
		{
			//Bag[ItemRewards[ChestID][j]] += 1 ;
		}
		for (int j = 0 ; j <= GoldRewards.length - 1 ; j += 1)
		{
			gold[0] += GoldRewards[ChestID][j] ;
		}
		map.getType()[pos.x][pos.y] = "free" ;
		//Ani.SetAniVars(18, new Object[] {100, items, ItemRewards, GoldRewards, color[0], CoinIcon}) ;
		//Ani.StartAni(18) ;
	}
	
	
	// battle functions
	public void SpendArrow()
	{
		//if (0 < Equips[3] & 0 < Bag[Equips[3]])
		//{
			//Bag[Equips[3]] += -1 ;
			if (job == 2 & Math.random() <= 0.1*spells.get(13).getLevel())
			{
				//Bag[Equips[3]] += 1 ;
			}
		//}
		//if (Bag[Equips[3]] == 0)
		//{
			equips[3] = null ;
		//}
	}
	private int StealItem(Creature creature, Items[] items, int spellLevel)
	{	
		int ID = (int)(UtilG.ArrayWithValuesGreaterThan(creature.getBag(), -1).length*Math.random() - 0.01) ;
		int StolenItemID = -1 ;
		if(-1 < creature.getBag()[ID])
		{
			if(Math.random() <= 0.01*items[creature.getBag()[ID]].getDropChance() + 0.01*spellLevel)
			{
				//Bag[creature.getBag()[ID]] += 1 ;
				StolenItemID = items[creature.getBag()[ID]].getID() ;
			}
		}
		return StolenItemID ;
	}
	private void OffensiveSpell(Creature creature, Spell spell)
	{
		PA.getMp().incCurrentValue(-spell.getMpCost()) ;
		//ResetSpellCooldownCounter(spellID) ; activate spell
		spell.activate() ;
		if (job == 0)
		{
			
		}
		if (job == 1)
		{
			
		}
		if (job == 2)
		{
			/*if (spellID == 3)
			{
				
			}*/
		}
		for (int i = 0 ; i <= spell.getBuffs().size() - 1 ; ++i)
		{
			//ApplyBuffsAndNerfs("activate", "buffs", i, spellID, SpellIsActive(spellID)) ;
			creature.ApplyBuffsAndNerfs("activate", "nerfs", i, spell.getLevel(), spell, spell.isActive()) ;
		}
	}
	private void UseSupportSpell(Pet pet, Spell spell)
	{	
		spell.getCooldownCounter().reset();
		spell.activate() ;		
		ResetBattleActions() ;
		PA.getMp().incCurrentValue(-spell.getMpCost()) ;
		if (job == 0)
		{
			
		}
		if (job == 1)
		{
			switch (spell.getName())
			{
				case "Cura": getLife().incCurrentValue(5 * spell.getLevel()); break ;
				case "Inspiração mística": break ;
				default: break ;
			}
			/*if (spellID == 10)
			{
				PA.getLife()[0] += (double)Math.min((0.04*spellLevel + 0.002*BA.TotalMagAtk()), 0.3)*PA.getLife()[1] ;
				PA.getLife()[0] = (double)Math.min(PA.getLife()[0], PA.getLife()[1]) ;
				if (0 < pet.getLife()[0] & spell.getMpCost() <= pet.getMp()[0])
				{
					pet.getMp()[0] += -spell.getMpCost() ;
					pet.getLife()[0] += (double)Math.min(Math.min((0.02*spellLevel + 0.002*(pet.getBA().TotalMagAtk())), 0.15)*pet.getLife()[1], pet.getLife()[1]) ;
					pet.getLife()[0] = (double)Math.min(pet.getLife()[0], pet.getLife()[1]) ;
				}
			}*/
		}
		for (int i = 0 ; i <= spell.getBuffs().size() - 1 ; ++i)
		{
			applyBuff(true, spell.getBuffs().get(i), spell.getLevel());
		}
	}
	public void autoSpells(Creature creature, ArrayList<Spell> spell)
	{		
		/*if (job == 3 & PA.getLife()[0] < 0.2 * PA.getLife()[1] & 0 < Skill[12] & !SkillBuffIsActive[12][0])	// Survivor's instinct
		{
			for (int i = 0 ; i <= skill[12].getBuffs().length - 1 ; ++i)
			{
				BuffsAndNerfs(player, pet, creature, skill[12].getBuffs(), Skill[12], i, false, "Player", "activate") ;
			}
			SkillBuffIsActive[12][0] = true ;
		}
		if (job == 3 & 0.2*Life[1] <= Life[0] & 0 < Skill[12] & SkillBuffIsActive[12][0])	// Survivor's instinct
		{
			for (int i = 0 ; i <= skill[12].getBuffs().length - 1 ; ++i)
			{
				BuffsAndNerfs(player, pet, creature, skill[12].getBuffs(), Skill[12], i, false, "Player", "deactivate") ;
			}
			SkillBuffIsActive[12][0] = false ;
		}*/
	}
	public void ApplyBuffsAndNerfs(String action, String type, int att, Buff buff, int spellID, boolean spellIsActive)
	{
		int ActionMult = 1 ;
		double[][] Buff = new double[14][5] ;	// [PA.getLife(), PA.getMp(), PhyAtk, MagAtk, Phy def, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence][effect]
		double[] OriginalValue = new double[14] ;	// [PA.getLife(), PA.getMp(), PhyAtk, MagAtk, Phy def, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence]
		double[][] Buffs = null ;
		List<Buff> buffs = spells.get(spellID).getBuffs() ;
		System.out.println("buffs = " + buffs);
		int BuffNerfLevel = spells.get(spellID).getLevel() ;
		if (type.equals("buffs"))
		{
			//Buffs = spells.get(spellID).getBuffs() ;
		}
		else if (type.equals("nerfs"))
		{
			Buffs = spells.get(spellID).getNerfs() ;
		}
		OriginalValue = new double[] {PA.getLife().getMaxValue(), PA.getMp().getMaxValue(), BA.getPhyAtk().getBaseValue(), BA.getMagAtk().getBaseValue(),
				BA.getPhyDef().getBaseValue(), BA.getMagDef().getBaseValue(), BA.getDex().getBaseValue(), BA.getAgi().getBaseValue(), BA.getCrit()[0],
				BA.getStun()[0], BA.getBlock()[0], BA.getBlood()[0], BA.getBlood()[2], BA.getBlood()[4], BA.getBlood()[6], BA.getPoison()[0], BA.getPoison()[2],
				BA.getPoison()[4], BA.getPoison()[6], BA.getSilence()[0]} ;
		if (action.equals("deactivate"))
		{
			ActionMult = -1 ;
		}
		
		if (!spellIsActive)
		{
			int level = 1 ;
			double increment = PA.getLife().getMaxValue() * buff.getPercentIncrease().get(Attributes.life)
					+ buff.getValueIncrease().get(Attributes.life) ;
			PA.getLife().incCurrentValue((int) increment * level * ActionMult);
		}
//		if (att == 11 | att == 12)
//		{
//			if (action.equals("deactivate"))
//			{
//				Buff[att][0] += (OriginalValue[att]*Buffs[att][0] + Buffs[att][1])*BuffNerfLevel*ActionMult ;
//				Buff[att][1] += (OriginalValue[att]*Buffs[att][3] + Buffs[att][4])*BuffNerfLevel*ActionMult ;
//				Buff[att][2] += (OriginalValue[att]*Buffs[att][6] + Buffs[att][7])*BuffNerfLevel*ActionMult ;
//				Buff[att][3] += (OriginalValue[att]*Buffs[att][9] + Buffs[att][10])*BuffNerfLevel*ActionMult ;
//			}
//			else
//			{
//				if (Math.random() <= Buffs[att][2])
//				{
//					Buff[att][1] += (OriginalValue[att]*Buffs[att][0] + Buffs[att][1])*BuffNerfLevel*ActionMult ;
//				}
//				if (Math.random() <= Buffs[att][5])
//				{
//					Buff[att][1] += (OriginalValue[att]*Buffs[att][3] + Buffs[att][4])*BuffNerfLevel*ActionMult ;
//				}
//				if (Math.random() <= Buffs[att][8])
//				{
//					Buff[att][2] += (OriginalValue[att]*Buffs[att][6] + Buffs[att][7])*BuffNerfLevel*ActionMult ;
//				}
//				if (Math.random() <= Buffs[att][11])
//				{
//					Buff[att][3] += (OriginalValue[att]*Buffs[att][9] + Buffs[att][10])*BuffNerfLevel*ActionMult ;
//				}
//			}
//		}
//		else if (action.equals("deactivate") | Math.random() <= Buffs[att][2])
//		{
//			Buff[att][0] += (OriginalValue[att]*Buffs[att][0] + Buffs[att][1])*BuffNerfLevel*ActionMult ;
//		}
//		if (!spellIsActive)
//		{
//			PA.getLife().incCurrentValue((int) Buff[0][0]); ;
//			PA.getMp().incCurrentValue((int) Buff[1][0]); ;
//			BA.getPhyAtk().incBonus(Buff[2][0]) ;
//			BA.getMagAtk().incBonus(Buff[3][0]) ;
//			BA.getPhyDef().incBonus(Buff[4][0]) ;
//			BA.getMagDef().incBonus(Buff[5][0]) ;
//			BA.getDex().incBonus(Buff[6][0]) ;
//			BA.getAgi().incBonus(Buff[7][0]) ;
//			BA.getCrit()[1] += Buff[8][0] ;
//			BA.getStun()[1] += Buff[9][0] ;
//			BA.getBlock()[1] += Buff[10][0] ;
//			BA.getBlood()[1] += Buff[11][0] ;
//			BA.getBlood()[3] += Buff[11][1] ;
//			BA.getBlood()[5] += Buff[11][2] ;
//			BA.getBlood()[7] += Buff[11][3] ;
//			BA.getBlood()[8] += Buff[11][4] ;
//			BA.getPoison()[1] += Buff[12][0] ;
//			BA.getPoison()[3] += Buff[12][1] ;
//			BA.getPoison()[5] += Buff[12][2] ;
//			BA.getPoison()[7] += Buff[12][3] ;
//			BA.getPoison()[8] += Buff[12][4] ;
//			BA.getSilence()[1] += Buff[13][0] ;
//		}	
	}
	public void TakeBloodAndPoisonDamage(double TotalBloodAtk, double TotalPoisonAtk)
	{
		// TODO this can go to livebeings
		int BloodDamage = 0 ;
		int PoisonDamage = 0 ;
		double BloodMult = 1, PoisonMult = 1 ;
		if (job == 4)
		{
			PoisonMult += -0.1*spells.get(13).getLevel() ;
		}
		if (0 < BA.getSpecialStatus()[2])	// Blood
		{
			BloodDamage = (int) Math.max(TotalBloodAtk * BloodMult - BA.TotalBloodDef(), 0) ;
		}
		if (0 < BA.getSpecialStatus()[3])	// Poison
		{
			PoisonDamage = (int) Math.max(TotalPoisonAtk * PoisonMult - BA.TotalPoisonDef(), 0) ;
		}
		PA.getLife().incCurrentValue(-BloodDamage - PoisonDamage); ;
		if (0 < BloodDamage)
		{
			statistics[18] += BA.TotalBloodDef() ;
		}
		if (0 < PoisonDamage)
		{
			statistics[21] += BA.TotalPoisonDef() ;
		}
	}
	public void updateoffensiveStats(AtkResults playerAtkResult, Creature creature)
	{
		/* 0: Number of phy attacks, 
		 * 1: number of spells used, 
		 * 2: number of defenses, 
		 * 3: total phy damage inflicted, 
		 * 4: total phy damage received, 
		 * 5: total mag damage inflicted, 
		 * 6: total mag damage received, 
		 * 7: total phy damage defended, 
		 * 8: total mag damage defended, 
		 * 9: total hits inflicted, 
		 * 10: total hits received, 
		 * 11: total dodges, 
		 * 12: number of crit, 
		 * 13: total crit damage, 
		 * 14: total stun, 
		 * 15: total block, 
		 * 16: total blood, 
		 * 17: total blood damage, 
		 * 18: total blood def, 
		 * 19: total poison, 
		 * 20: total poison damage, 
		 * 21: total poison def, 
		 * 22: total silence
		*/
		// TODO HighestPlayerInflictedDamage = Math.max(HighestPlayerInflictedDamage, PlayerAtkResult[0]) ;
		int damage = (int) playerAtkResult.getDamage() ;
		AttackEffects effect = (AttackEffects) playerAtkResult.getEffect() ;
		if (currentAction != null)				// player has performed an action
		{
			if (0 <= damage)							// player inflicted damage
			{	
				if (actionIsSpell())					// player performed a magical atk
				{
					statistics[1] += 1 ;							// Total number of magical atks (spells) performed by the player
					statistics[5] += damage ;					// Total magical damage inflicted by the player
				}
				else									// player performed a physical atk
				{
					statistics[0] += 1 ;							// Total number of physical atks performed by the player
					statistics[3] += damage ;					// Total physical damage inflicted by the player
				}
			}		
			if (effect.equals(AttackEffects.hit))							// player performed a successful hit
			{
				statistics[9] += 1 ;								// total number of successful hits performed by the player
				// for the status, dividing the duration of the status by the duration applied to get the number of times the status was applied
				if (0 < BA.getStun()[4])
				{
					statistics[14] += creature.getBA().getSpecialStatus()[0] / BA.getStun()[4] ;	// total number of stun inflicted by the player
				}
				if (0 < BA.getBlock()[4])
				{
					statistics[15] += creature.getBA().getSpecialStatus()[1] / BA.getBlock()[4] ;	// total number of block performed by the player
				}
				if (0 < BA.getBlood()[8])
				{
					statistics[16] += creature.getBA().getSpecialStatus()[2] / BA.getBlood()[8] ;	// total number of blood inflicted by the player
					if (0 < creature.getBA().getSpecialStatus()[2])
					{
						statistics[17] += 1 ;	// total number of blood inflicted by the player
					}
				}
				if (0 < BA.getPoison()[8])
				{
					statistics[19] += creature.getBA().getSpecialStatus()[3] / BA.getPoison()[8] ;	// total number of poison inflicted by the player
					if (0 < creature.getBA().getSpecialStatus()[3])
					{
						statistics[20] += 1 ;	// total number of blood inflicted by the player
					}
				}
				if (0 < BA.getSilence()[4])
				{
					statistics[22] += creature.getBA().getSpecialStatus()[4] / BA.getSilence()[4] ;	// total number of silence inflicted by the player
				}
			}
			if (effect.equals("Crit"))				// player performed a critical atk (physical or magical)
			{
				statistics[12] += 1 ;							// total number of critical hits performed by the player
				statistics[13] += damage ;						// total critical damage (physical + magical) performed by the player
			}
			if (currentAction.equals(Player.ActionKeys[3]))	// player defended
			{
				statistics[2] += 1 ;								// Number of defenses performed by the player
			}
		}
	}
	public void updatedefensiveStats(int damage, AttackEffects effect, boolean creaturePhyAtk, Creature creature)
	{
		/* 0: Number of phy attacks, 
		 * 1: number of spells used, 
		 * 2: number of defenses, 
		 * 3: total phy damage inflicted, 
		 * 4: total phy damage received, 
		 * 5: total mag damage inflicted, 
		 * 6: total mag damage received, 
		 * 7: total phy damage defended, 
		 * 8: total mag damage defended, 
		 * 9: total hits inflicted, 
		 * 10: total hits received, 
		 * 11: total dodges, 
		 * 12: number of crit, 
		 * 13: total crit damage, 
		 * 14: total stun, 
		 * 15: total block, 
		 * 16: total blood, 
		 * 17: total blood damage, 
		 * 18: total blood def, 
		 * 19: total poison, 
		 * 20: total poison damage, 
		 * 21: total poison def, 
		 * 22: total silence
		*/
		if (effect.equals(AttackEffects.hit))
		{			
			statistics[10] += 1 ;						// number of hits the player has taken
			if (creaturePhyAtk)	// Creature physical atk
			{				
				statistics[4] += damage ;				// total phy damage received by the player
				statistics[7] += BA.TotalPhyDef() ;		// total phy damage defended by the player
			}
			else				// Creature magical atk
			{
				statistics[6] += damage ;				// total mag damage received by the player
				statistics[8] += BA.TotalMagDef() ;		// total mag damage defended by the player
			}
			if (0 < BA.getSpecialStatus()[2])
			{
				statistics[18] += BA.getBlood()[3] + BA.getBlood()[4] ;		// total number of blood defended by the player
			}
			if (0 < BA.getSpecialStatus()[3])
			{
				statistics[21] += BA.getPoison()[3] + BA.getPoison()[4] ;	// total number of blood defended by the player
			}
		}
		else
		{
			statistics[11] += 1 ;						// total number of hits the player has dogded
		}
	}
	public void Win(Creature creature, Quests[] quest, Animations Ani)
	{		
		ArrayList<String> GetItemsObtained = new ArrayList<String>(Arrays.asList(new String[0])) ;		
		for (int i = 0 ; i <= 9 ; ++i)
		{
			if(-1 < creature.getBag()[i])
			{
				/*if(Math.random() <= 0.01*tems[creature.getBag()[i]].getDropChance())
				{
					//Bag[creature.getBag()[i]] += 1 ;	
					GetItemsObtained.add(items[creature.getBag()[i]].getName()) ;
				}*/
			}
		}
		String[] ItemsObtained = new String[GetItemsObtained.size()] ;
		ItemsObtained = GetItemsObtained.toArray(ItemsObtained) ;
		gold[0] += creature.getGold()*UtilG.RandomMult( (double) (0.1 * goldMultiplier)) ;
		PA.getExp().incCurrentValue((int) (creature.getExp().getCurrentValue() * PA.getExp().getMultiplier()))  ;
		if (GetActiveQuests() != null)
		{
			for (int q = 0 ; q <= GetActiveQuests().size() - 1 ; q += 1)
			{
				quest[GetActiveQuests().get(q)].IncReqCreaturesCounter(creature.getType()) ;
			}
		}
		Ani.SetAniVars(12, new Object[] {100, ItemsObtained, color}) ;
		Ani.StartAni(12) ;
	}
	public void checkLevelUp(Animations ani)
	{
		if (shouldLevelUP())
		{
			double[] attributesIncrease = CalcAttIncrease() ;
			setLevel(level + 1) ;
			PA.getLife().incMaxValue((int) attributesIncrease[0]) ;
			PA.getMp().incMaxValue((int) attributesIncrease[1]); ;	
			BA.getPhyAtk().incBaseValue(attributesIncrease[2]) ;
			BA.getMagAtk().incBaseValue(attributesIncrease[3]) ;
			BA.getPhyDef().incBaseValue(attributesIncrease[4]) ;
			BA.getMagDef().incBaseValue(attributesIncrease[5]) ;
			BA.getAgi().incBaseValue(attributesIncrease[6]) ;
			BA.getDex().incBaseValue(attributesIncrease[7]) ;
			PA.getExp().incMaxValue((int) attributesIncrease[8]); ;
			PA.getLife().setToMaximum() ;
			PA.getMp().setToMaximum() ;
			spellPoints += 1 ;
			attPoints += 2 ;
			

			ani.SetAniVars(13, new Object[] {150, attributesIncrease, level, color}) ;
			ani.StartAni(13) ;
		}
	}
	private double[] CalcAttIncrease()
	{
		// Life, Mp, Phyatk, Magatk, Phydef, Magdef, Dex, Agi, Exp
		double[] attributeIncrease = getAttIncrease()[getProJob()] ;
		double[] chanceIncrease = getChanceIncrease()[getProJob()] ;
		double[] increase = new double[attributeIncrease.length + 1] ;
		for (int i = 0 ; i <= attributeIncrease.length - 1 ; ++i)
		{
			if (Math.random() <= chanceIncrease[i])
			{
				increase[i] = attributeIncrease[i] ;
			}
		}
		increase[attributeIncrease.length] = (double) (10*(3*Math.pow(level - 1, 2) + 3*(level - 1) + 1) - 5) ;
		return increase ;
	}
	private void resetPosition()
	{
		setMap(Game.getMaps()[job]) ;
		//setContinent(0) ;
		switch (job)
		{
			case 0: setPos(new Point(340, 340)) ; break ;
			case 1: setPos(new Point(340, 180)) ; break ;
			case 2: setPos(new Point(40, 220)) ; break ;
			case 3: setPos(new Point(340, 340)) ; break ;
			case 4: setPos(new Point(340, 640)) ; break ;
		}
	}
	public void dies()
	{
		gold[0] = (int)(0.8*gold[0]) ;
		PA.getLife().setToMaximum(); ;
		PA.getMp().setToMaximum(); ;
		PA.getSatiation().setToMaximum() ;
		state = LiveBeingStates.idle ;
		resetPosition() ;
	}
	

	
	/* Get current state methods 
	private boolean SpellIsActive(int spellID)
	{
		if (spellDurationCounter[spellID] != 0)
		{
			return true ;
		}
		return false ;
	}
	private boolean SpellIsReady(int spellID)
	{
		if (spellCooldownCounter[spellID] == spells.get(spellID).getCooldown())
		{
			return true ;
		}
		return false ;
	}*/
	public boolean isEquippable(int ItemID)
	{
		if ((Items.BagIDs[6] + Items.NumberOfItems[6] / 5 * job) <= ItemID & ItemID <= Items.BagIDs[6] + Items.NumberOfItems[6] / 5 * (job + 1))
		{
			return true ;
		}
		return false ;
	}
	public boolean isInBattle() {return state.equals(LiveBeingStates.fighting) ;}
	
	
	// Action windows
	private void AttWindow(GameIcon[] icons, int[] MainWinDim, double[] PlayerAttributeIncrease, Point MousePos, Image GoldCoinImage, DrawingOnPanel DP)
	{
		//int WindowLimit = 2 ;
		//SelectedWindow[0] = UtilS.MenuSelection(Player.ActionKeys[0], Player.ActionKeys[2], action, SelectedWindow[0], WindowLimit) ;
		for (int selectedItem = 0 ; selectedItem <= 7 - 1 ; selectedItem += 1)
		{
			//if (icons[i].ishovered(MousePos) & (currentAction.equals("Enter") | currentAction.equals("MouseLeftClick")) & 0 < attPoints)
			//{
				if (selectedItem == 0)
				{
					PA.getLife().incCurrentValue((int) PlayerAttributeIncrease[selectedItem]); ;
					PA.getLife().incMaxValue((int) PlayerAttributeIncrease[selectedItem]); ;
				}
				else if (selectedItem == 1)
				{
					PA.getMp().incCurrentValue((int) PlayerAttributeIncrease[selectedItem]); ;
					PA.getMp().incMaxValue((int) PlayerAttributeIncrease[selectedItem]); ;
				}
				else if (selectedItem == 2)
				{
					BA.getPhyAtk().incBaseValue(PlayerAttributeIncrease[selectedItem]) ;
				}
				else if (selectedItem == 3)
				{
					BA.getMagAtk().incBaseValue(PlayerAttributeIncrease[selectedItem]) ;
				}
				else if (selectedItem == 4)
				{
					BA.getPhyDef().incBaseValue(PlayerAttributeIncrease[selectedItem]) ;
				}
				else if (selectedItem == 5)
				{
					BA.getMagDef().incBaseValue(PlayerAttributeIncrease[selectedItem]) ;
				}
				else if (selectedItem == 6)
				{
					BA.getDex().incBaseValue(PlayerAttributeIncrease[selectedItem]) ;
				}
				else if (selectedItem == 7)
				{
					BA.getAgi().incBaseValue(PlayerAttributeIncrease[selectedItem]) ;
				}
				attPoints += -1 ;
			//}
		}
		//Point WinPos = new Point((int) (0.3 * MainWinDim[0]), (int) (0.2 * MainWinDim[1])) ;
		//DrawAttWindow(MainWinDim, WinPos, MousePos, AllText, AllTextCat, SelectedWindow[0], GoldCoinImage, icons, DP) ;

		attWindow.display(this, allText, equips, equipsBonus, MousePos, DP) ;
	}
	private void ApplyEquipsBonus(int ID, double ActionMult)
	{
		PA.getLife().incMaxValue((int) (equipsBonus[ID][2]*ActionMult)); ;
		PA.getMp().incMaxValue((int) (equipsBonus[ID][3]*ActionMult)); ;
		BA.getPhyAtk().incBonus(equipsBonus[ID][4]*ActionMult) ;
		BA.getMagAtk().incBonus(equipsBonus[ID][5]*ActionMult) ;
		BA.getPhyDef().incBonus(equipsBonus[ID][6]*ActionMult) ;
		BA.getMagDef().incBonus(equipsBonus[ID][7]*ActionMult) ;
		BA.getDex().incBonus(equipsBonus[ID][8]*ActionMult) ;
		BA.getAgi().incBonus(equipsBonus[ID][9]*ActionMult) ;
		BA.getCrit()[0] += equipsBonus[ID][10]*ActionMult ;
		BA.getCrit()[2] += equipsBonus[ID][11]*ActionMult ;
		BA.getStun()[0] += equipsBonus[ID][12]*ActionMult ;
		BA.getStun()[2] += equipsBonus[ID][13]*ActionMult ;
		BA.getStun()[4] += equipsBonus[ID][14]*ActionMult ;
		BA.getBlock()[0] += equipsBonus[ID][15]*ActionMult ;
		BA.getBlock()[2] += equipsBonus[ID][16]*ActionMult ;
		BA.getBlock()[4] += equipsBonus[ID][17]*ActionMult ;
		BA.getBlood()[0] += equipsBonus[ID][18]*ActionMult ;
		BA.getBlood()[2] += equipsBonus[ID][19]*ActionMult ;
		BA.getBlood()[4] += equipsBonus[ID][20]*ActionMult ;
		BA.getBlood()[6] += equipsBonus[ID][21]*ActionMult ;
		BA.getBlood()[8] += equipsBonus[ID][22]*ActionMult ;
		BA.getPoison()[0] += equipsBonus[ID][23]*ActionMult ;
		BA.getPoison()[2] += equipsBonus[ID][24]*ActionMult ;
		BA.getPoison()[4] += equipsBonus[ID][25]*ActionMult ;
		BA.getPoison()[6] += equipsBonus[ID][26]*ActionMult ;
		BA.getPoison()[8] += equipsBonus[ID][27]*ActionMult ;
		BA.getSilence()[0] += equipsBonus[ID][28]*ActionMult ;
		BA.getSilence()[2] += equipsBonus[ID][29]*ActionMult ;
		BA.getSilence()[4] += equipsBonus[ID][30]*ActionMult ;
	}
	
	private static boolean SetIsFormed(Equip[] EquipID)
	{
		if ((EquipID[0].getId() + 1) == EquipID[1].getId() & (EquipID[1].getId() + 1) == EquipID[2].getId())
		{
			return true ;
		}
		return false ;
	}
	
	private void Equip(Items[] items, int EquipID)
	{
		int NumberOfEquipTypes = 3 ;	// Sword/Staff/Bow/Claws/Dagger, shield, armor/robe (Archers have bow, bandana, and armor)
		int FirstEquipID = Items.BagIDs[6] ;
		int EquipType = (EquipID + job) % NumberOfEquipTypes ;
		int currentEquipID = equips[EquipType].getId() ;
		//if (0 < Bag[EquipID])
		//{
			if (0 < currentEquipID)	// Unnequip the current equip
			{
				if (SetIsFormed(equips))	// if the set was formed, remove the 20% bonus
				{
					ApplyEquipsBonus(equips[0].getId() - FirstEquipID, (double)-0.2) ;
					ApplyEquipsBonus(equips[1].getId() - FirstEquipID, (double)-0.2) ;
					ApplyEquipsBonus(equips[2].getId() - FirstEquipID, (double)-0.2) ;
				}
				equips[EquipType] = null ;
				elem[EquipType + 1] = "n" ;
				ApplyEquipsBonus(currentEquipID - FirstEquipID, -1) ;
			}
			if (currentEquipID != EquipID & equips[(EquipID + job) % NumberOfEquipTypes].getId() == 0)	// Equip
			{
				equips[EquipType] = Equip.getAll()[EquipID] ;
				elem[EquipType + 1] = Items.EquipsElem[EquipID - FirstEquipID] ;
				ApplyEquipsBonus(EquipID - FirstEquipID, 1) ;
				if (SetIsFormed(equips))	// if the set is formed, add the 20% bonus
				{
					ApplyEquipsBonus(equips[0].getId() - FirstEquipID, (double)0.2) ;
					ApplyEquipsBonus(equips[1].getId() - FirstEquipID, (double)0.2) ;
					ApplyEquipsBonus(equips[2].getId() - FirstEquipID, (double)0.2) ;
				}
			}
		//}
		if (elem[1].equals(elem[2]) & elem[2].equals(elem[3]))	// if the elements of all equips are the same, activate the superelement
		{
			elem[4] = elem[1] ;
		}
		else
		{
			elem[4] = "n" ;
		}
	}
	private void ItemEffect(int ItemID)
	{
		if (ItemID == 1381 | ItemID == 1382 | ItemID == 1384)
		{
			BA.getSpecialStatus()[2] = 0 ;
		}
		if (ItemID == 1411)
		{
			BA.getSpecialStatus()[4] = 0 ;
		}
	}
	
	
	// Drawing methods
	private void DrawStats(String[][] AllText, int[] AllTextCat, Point Pos, int L, int H, double[] PlayerStats, DrawingOnPanel DP)
	{
		Font font = new Font("SansSerif", Font.BOLD, L / 28) ;
		double OverallAngle = DrawingOnPanel.stdAngle ;
		int TextCat = AllTextCat[7] ;
		Point TextPos = new Point((int) (Pos.x + 5 + 0.05*L), (int) (Pos.y + 0.05*H)) ;
		for (int i = 0 ; i <= PlayerStats.length - 1 ; i += 1)
		{
			DP.DrawText(TextPos, Align.bottomLeft, OverallAngle, AllText[TextCat][i + 1] + " " + String.valueOf(UtilG.Round(PlayerStats[i], 1)), font, Game.ColorPalette[5]) ;
			TextPos.y += 0.95*H/PlayerStats.length ;
		}
	}

	private void DrawRange(DrawingOnPanel DP)
	{
		DP.DrawCircle(pos, (int)(2 * range), 2, null, Game.ColorPalette[job]) ;
	}
	private void DrawEquips(Point Pos, int Job, int equiptype, int EquipID, double[][] EquipsBonus, Scale scale, double angle, DrawingOnPanel DP)
	{
		int bonus = 0 ;
		if (EquipsBonus[EquipID][1] == 10)
		{
			bonus = 8 ;
		}
		if (equiptype == 0)
		{
			DP.DrawImage(Equip.SwordImage, Pos, angle, scale, Align.center) ;	// Items.EquipImage[job + bonus]
		}
		if (equiptype == 1)
		{
			DP.DrawImage(Equip.ShieldImage, Pos, angle, scale, Align.center) ;	// Items.EquipImage[5 + bonus]
		}
		if (equiptype == 2)
		{
			DP.DrawImage(Equip.ArmorImage, Pos, angle, scale, Align.center) ;	// Items.EquipImage[6 + bonus]
		}
		if (equiptype == 3)
		{
			DP.DrawImage(Equip.ArrowImage, Pos, angle, scale, Align.center) ;	// Items.EquipImage[7]
		}
	}
	private void DrawPlayerEquips(int[] Pos, double[] playerscale, DrawingOnPanel DP)
	{
		Scale scale = new Scale(0.6, 0.6) ;
		double[] angle = new double[] {50, 30, 0, 0, 0} ;
		Point EqPos = new Point((int)(Pos[0] + 0.16 * size.width * playerscale[0]), (int)(Pos[1] - 0.4 * size.height * playerscale[1])) ;
		if (equips[0] != null)
		{
			DrawEquips(EqPos, job, 0, equips[0].getId() - Items.BagIDs[5], Items.EquipsBonus, scale, angle[job], DP) ;
		}	
		
		/*
		 * int bonus = 0 ;
		if (EquipsBonus[EquipID][1] == 10)
		{
			bonus = 8 ;
		}
		if (equiptype == 0)	// 0: weapon
		{
			DP.DrawImage(Items.EquipImage[Job + bonus], Pos, angle, scale, AlignmentPoints.center) ;
		}
		if (1 <= equiptype)	// 1: shield, 2: armor, 3: arrow
		{
			DP.DrawImage(Items.EquipImage[equiptype + 1 + bonus], Pos, angle, scale, AlignmentPoints.center) ;
		}
		 * */
	}
	public void DrawWeapon(Point Pos, double[] playerscale, DrawingOnPanel DP)
	{
		Scale scale = new Scale(0.6, 0.6) ;
		double[] angle = new double[] {50, 30, 0, 0, 0} ;
		Point EqPos = new Point((int)(Pos.x + 0.16*size.width*playerscale[0]), (int)(Pos.y - 0.4*size.height*playerscale[1])) ;
		if (getEquips()[0] != null)
		{
			DrawEquips(EqPos, job, 0, getEquips()[0].getId() - Items.BagIDs[6], Items.EquipsBonus, scale, angle[job], DP) ;
		}	
	}
	public void DrawTimeBar(Creature creature, DrawingOnPanel DP)
	{
		String relPos = UtilS.RelPos(pos, creature.getPos()) ;
		DrawTimeBar(relPos, Game.ColorPalette[9], DP) ;
	}
	public void display(Point pos, Scale scale, Directions direction, boolean showRange, DrawingOnPanel DP)
	{
		double angle = DrawingOnPanel.stdAngle ;
		if (isRiding)
		{
			Point ridePos = new Point(pos.x - RidingImage.getWidth(null)/2, pos.y + RidingImage.getHeight(null)/2) ;
			DP.DrawImage(RidingImage, ridePos, angle, scale, Align.bottomLeft) ;
		}
		
		movingAni.display(direction, pos, angle, scale, DP) ;
		
		if (questSkills.get(QuestSkills.dragonAura))
		{
			DP.DrawImage(DragonAuraImage, feetPos(), angle, scale, Align.center) ;					
		}
		if (showRange)
		{
			DrawRange(DP) ;
		}
	}

	public void ShowEffectsAndStatusAnimation(Creature creature, DrawingOnPanel DP)
	{
		int mirror = UtilS.MirrorFromRelPos(UtilS.RelPos(getPos(), creature.getPos())) ;
		Dimension offset = new Dimension(8, (int)(0.8*getSize().height)) ;
		ShowEffectsAndStatusAnimation(getPos(), mirror, offset, StatusImages, getBA().getSpecialStatus(), isDefending(), DP) ;
	}
	
	
	// Save and load methods
	private void Save(String filePath, Pet pet)
	{
		// TODO save method
		try
		{	
			FileWriter fileWriter = new FileWriter(filePath) ;
			BufferedWriter bw = new BufferedWriter(fileWriter) ; 
			bw.write("Save version: 3.41 \n" + getName()) ;
			bw.write("\nPlayer name: \n" + getName()) ;
			bw.write("\nPlayer language: \n" + getLanguage()) ;
			bw.write("\nPlayer sex: \n" + getSex()) ;
			bw.write("\nPlayer size: \n" + getSize()) ;
			bw.write("\nPlayer colors: \n" + getColor()) ;
			bw.write("\nPlayer job: \n" + getJob()) ;
			//bw.write("\nPlayer PA.ProJob: \n" + PA.ProJob) ;
			bw.write("\nPlayer continent: \n" + getContinent()) ;
			bw.write("\nPlayer map: \n" + getMap()) ;
			bw.write("\nPlayer pos: \n" + getPos()) ;
			//bw.write("\nPlayer skill: \n" + Arrays.toString(getSpell())) ;
			//bw.write("\nPlayer quest: \n" + Arrays.toString(getQuest())) ;
			//bw.write("\nPlayer bag: \n" + Arrays.toString(getBag())) ;
			bw.write("\nPlayer equips: \n" + Arrays.toString(getEquips())) ;
			bw.write("\nPlayer skillPoints: \n" + getSpellPoints()) ;
			//bw.write("\nPlayer life: \n" + Arrays.toString(getLife())) ;
			//bw.write("\nPlayer mp: \n" + Arrays.toString(getMp())) ;
			bw.write("\nPlayer range: \n" + getRange()) ;
			//bw.write("\nPlayer phyAtk: \n" + Arrays.toString(getPhyAtk())) ;
			//bw.write("\nPlayer magAtk: \n" + Arrays.toString(getMagAtk())) ;
			//bw.write("\nPlayer phyDef: \n" + Arrays.toString(getPhyDef())) ;
			//bw.write("\nPlayer magDef: \n" + Arrays.toString(getMagDef())) ;
			//bw.write("\nPlayer dex: \n" + Arrays.toString(getDex())) ;
			//bw.write("\nPlayer agi: \n" + Arrays.toString(getAgi())) ;
			bw.write("\nPlayer crit: \n" + Arrays.toString(getCrit())) ;
			bw.write("\nPlayer stun: \n" + Arrays.toString(getStun())) ;
			bw.write("\nPlayer block: \n" + Arrays.toString(getBlock())) ;
			bw.write("\nPlayer blood: \n" + Arrays.toString(getBlood())) ;
			bw.write("\nPlayer poison: \n" + Arrays.toString(getPoison())) ;
			bw.write("\nPlayer silence: \n" + Arrays.toString(getSilence())) ;
			bw.write("\nPlayer elem: \n" + Arrays.toString(getElem())) ;
			//bw.write("\nPlayer elem mult: \n" + Arrays.toString(getElemMult())) ;
			bw.write("\nPlayer collect: \n" + Arrays.toString(getCollect())) ;
			bw.write("\nPlayer level: \n" + getLevel()) ;
			bw.write("\nPlayer gold: \n" + Arrays.toString(getGold())) ;
			bw.write("\nPlayer step: \n" + getStep()) ;
			//bw.write("\nPlayer exp: \n" + Arrays.toString(getExp())) ;
			//bw.write("\nPlayer satiation: \n" + Arrays.toString(getSatiation())) ;
			//bw.write("\nPlayer quest skills: \n" + Arrays.toString(questSkills)) ;
			bw.write("\nPlayer status: \n" + Arrays.toString(BA.getSpecialStatus())) ; 
			//bw.write("\nPlayer actions: \n" + Arrays.deepToString(getActions())) ; 
			//bw.write("\nPlayer battle actions: \n" + Arrays.deepToString(BA.getBattleActions())) ; 
			//bw.write("\nPlayer status counter: \n" + Arrays.toString(getStatusCounter())) ; 		
			bw.write("\nPlayer stats: \n" + Arrays.toString(getStats())) ;
			bw.write("\nPlayer available attribute points: \n" + getAttPoints()) ;
			bw.write("\nPlayer attribute increase: \n" + Arrays.deepToString(getAttIncrease())) ;
			bw.write("\nPlayer chance increase: \n" + Arrays.deepToString(getChanceIncrease())) ;
			//bw.write("\nPlayer creatures discovered: \n" + Arrays.toString(getCreaturesDiscovered())) ;
			pet.Save(bw) ;	
			
			bw.write("\nEquips bonus: \n" + Arrays.deepToString(Items.EquipsBonus)) ;
			//bufferedWriter.write("\nNPCs contact: \n" + Arrays.toString(FirstNPCContact)) ;
			bw.write("\nDifficult level: \n" + Game.difficultLevel) ;
			bw.close() ;
		}		
		catch(IOException ex) 
		{
            System.out.println("Error writing to file '" + filePath + "'") ;
        }
	}
	private void Load(String filePath, Pet pet, GameMap[] maps)
	{
		JSONObject JsonData = UtilG.readJsonObject(filePath) ;
		setName((String) JsonData.get("Name")) ;
		setLevel(Math.toIntExact((Long) JsonData.get("Level"))) ;
	}
	
	
	
	
	

	/*private void Collect(int Coltype, Maps[] maps, DrawFunctions DF, Animations Ani)
	{
		String CollectMessage = "";
		if (Coltype == 0)	// Collectible type
		{
			//CollectMessage = AllText[TextCat][6] + " " + items[Items.BagIDs[3]].getName() + "!" ;
		} else
		{
			//CollectMessage = AllText[TextCat][6] + " " + items[Coltype + Items.BagIDs[0] - 1].getName() + "!" ;
		}
		Ani.SetAniVars(10, new Object[] {100, pos, 10, Coltype, CollectMessage}) ;
		Ani.StartAni(10) ;
		
		map.getType()[pos.x][pos.y] = "free" ;	// Make the ground where the player is standing free
		
		// The collecting act itself
		int CollectibleID = -1 ;
		double CollectChance = (double) ((getMap().getCollectibleLevel() + 2)*Math.random()) ;
		int MapCollectLevel = getMap().getCollectibleLevel() ;
		double PlayerCollectLevel = -1 ;
		if (Coltype == 0)	// Berry
		{
			//Bag[Items.BagIDs[3] - 1 + MapCollectLevel + Coltype] += 1 ;
			if (job == 3 & Math.random() <= 0.14*spell[4].getLevel())	// Double collect
			{
				//Bag[Items.BagIDs[3] - 1 + MapCollectLevel + Coltype] += 1 ;	
			}
			CollectibleID = 0 ;
		}
		else if (MapCollectLevel <= PlayerCollectLevel + 1 & PlayerCollectLevel + 1 < CollectChance)
		{					
			//Bag[Items.BagIDs[0] + 3*(MapCollectLevel - 1) + Coltype - 1] += 1 ;
			collectLevel[Coltype - 1] += 0.25/(PlayerCollectLevel + 1) ;
			if (job == 3 & Math.random() <= 0.14*spell[4].getLevel())	// Double collect
			{
				//Bag[Items.BagIDs[0] + 3*(MapCollectLevel - 1) + Coltype - 1] += 1 ;
				collectLevel[Coltype - 1] += 0.25/(PlayerCollectLevel + 1) ;	
			}
			CollectibleID = 0 ;
		}
		else if (MapCollectLevel <= PlayerCollectLevel + 1)
		{
			CollectibleID = 1 ;
		}
		else
		{
			CollectibleID = 2 ;
		}	
		if (!Ani.isActive(10))	// Player is done collecting
		{
			if (CollectibleID == 0)
			{
				CollectibleID = -1 ;
			}
			else
			{
				CollectibleID = Coltype ;
			}
		}
		else
		{
			CollectibleID = -1 ;
		}
		
		//map.CreateCollectible(map.getid(), CollectibleID) ;	
	}*/
	
	/*private void DrawSpellsTree(ArrayList<Spell> spells, Point MousePos, int SelectedSpell, DrawingOnPanel DP)
	{
		Screen screen = Game.getScreen() ;
		int[] Sequence = GetSpellSequence() ;
		int[] ProSequence = GetProSpellSequence() ;
		int NumberOfSpells = GetNumberOfSpells() ;
		int NumberOfProSpells = 0 ;
		Font font = new Font("SansSerif", Font.BOLD, 10) ;
		Font Largefont = new Font("SansSerif", Font.BOLD, 12) ;
		Point Pos = new Point((int)(0.1*screen.getSize().width), (int)(0.9*screen.getSize().height)) ;
		Dimension Size = new Dimension((int)(0.7*screen.getSize().width), (int)(0.66*screen.getSize().height)) ;
		int TabL = Size.width / 20, TabH = Size.height / 3 ;
		Dimension size = new Dimension((int)(0.2*screen.getSize().width), (int)(0.1*screen.getSize().height)) ;
		int Sx = size.width / 10, Sy = size.height / 10 ;
		Color[] SpellsColors = new Color[NumberOfSpells + NumberOfProSpells] ;
		Color[] TabColor = new Color[] {Game.ColorPalette[7], Game.ColorPalette[7]} ;
		Color[] TabTextColor = new Color[] {Game.ColorPalette[5], Game.ColorPalette[5]} ;
		int tab = 0 ;
		if (NumberOfSpells - 1 < SelectedSpell)
		{
			tab = 1 ;
			NumberOfProSpells = GetNumberOfProSpells() ;
			Sequence = ProSequence ;
		}

		Color[] color = new Color[spells.size()] ;
		for (int i = 0 ; i <= spells.size() - 1 ; i += 1)
		{
			color[i] = Game.ColorPalette[4] ;
			if (spells.get(i).hasPreRequisitesMet())
			{
				color[i] = Game.ColorPalette[5] ;
			}
		}
		
		// spell tree window
		DP.DrawRoundRect(Pos, Align.bottomLeft, Size, 1, Game.ColorPalette[20], Game.ColorPalette[20], true) ;
		TabColor[tab] = Game.ColorPalette[20] ;
		TabTextColor[tab] = Game.ColorPalette[3] ;
		DP.DrawRoundRect(new Point(Pos.x, Pos.y - 2*TabH), Align.bottomRight, new Dimension(TabL, TabH), 1, TabColor[0], Game.ColorPalette[8], true) ;
		DP.DrawText(new Point(Pos.x + TabL/2 + UtilG.TextH(font.getSize())/2, Pos.y - 2*TabH - TabH/2), Align.center, 90, allText.get("* Classes *")[getJob() + 1], Largefont, TabTextColor[0]) ;
		if (0 < getProJob())
		{
			DP.DrawRoundRect(new Point(Pos.x, Pos.y - TabH), Align.bottomRight, new Dimension(TabL, TabH), 1, TabColor[1], Game.ColorPalette[8], true) ;	
			DP.DrawText(new Point(Pos.x + TabL/2 + UtilG.TextH(font.getSize())/2, Pos.y - 3*TabH/2), Align.center, 90, allText.get("* ProClasses *")[getProJob() + 2*getJob()], Largefont, TabTextColor[1]) ;
		}
		
		// Organogram
		//String[] SkillNames = null ;
//		String[][] SpellNames = new String[2][] ;
//		String[] SpellLevels = null ;
//		if (tab == 0)
//		{
//			for (int spell = 0 ; spell <= NumberOfSpells - 1 ; spell += 1)
//			{
//				SpellNames[0] = UtilG.AddElem(SpellNames[0], spells[spell].getName()) ;
//				SpellNames[1] = UtilG.AddElem(SpellNames[1], spells[spell].getType()) ;
//				SpellLevels = UtilG.AddElem(SpellLevels, String.valueOf(player.getSpell()[spell])) ;
//				SpellsColors[spell] = color[spell] ;
//			}
//		}
//		if (tab == 1)
//		{
//			for (int spell = NumberOfSpells ; spell <= NumberOfSpells + NumberOfProSpells - 1 ; spell += 1)
//			{
//				SpellNames[0] = UtilG.AddElem(SpellNames[0], spells[spell].getName()) ;
//				SpellNames[1] = UtilG.AddElem(SpellNames[1], spells[spell].getType()) ;
//				SpellLevels = UtilG.AddElem(SpellLevels, String.valueOf(player.getSpell()[spell])) ;
//				SpellsColors[spell] = color[spell] ;
//			}
//		}
//		SpellsColors[SelectedSpell - tab*NumberOfSpells] = Game.ColorPalette[3] ;
//		DF.DrawOrganogram(Sequence, new Point(Pos.x, Pos.y - Size.y), Sx, Sy, size, SpellNames, SpellLevels, SpellsTreeIcon, font, SpellsColors, MousePos) ;
		
		
		// spell info
		int TextmaxL = Size.width / 5, sx = 10, sy = UtilG.TextH(font.getSize()) + 2 ;
		String Description = spells.get(SelectedSpell).getInfo()[0], Effect = spells.get(SelectedSpell).getInfo()[1] ;
		DP.DrawRoundRect(new Point(Pos.x, Pos.y - Size.height), Align.bottomLeft, new Dimension(Size.width, Size.height / 4), 1, Game.ColorPalette[7], Game.ColorPalette[7], true) ;
		DP.DrawFitText(new Point(Pos.x + sx, Pos.y - Size.height - Size.height / 5), sy, Align.bottomLeft, Effect, font, TextmaxL, getColor()) ;
		DP.DrawFitText(new Point(Pos.x + sx, Pos.y - Size.height - Size.height / 10), sy, Align.bottomLeft, Description, font, TextmaxL - 6, getColor()) ;		
		DP.DrawText(new Point(Pos.x + Size.width, Pos.y), Align.topRight, DrawingOnPanel.stdAngle, "Pontos: " +  getSpellPoints(), font, getColor()) ;		
	}	*/
	
	/*private void DrawSpecialAttributesWindow(String[][] AllText, int[] AllTextCat, Point Pos, int L, int H, DrawingOnPanel DP)
	{
		Font font = new Font("SansSerif", Font.BOLD, L / 20) ;
		double OverallAngle = DrawingOnPanel.stdAngle ;
		int SpecialAttrPropCat = AllTextCat[8], AttrCat = AllTextCat[6] ;
		int lineW = 2 ;
		double sx = (double)0.15*L, sy = (double)(1.8*UtilG.TextH(font.getSize())) ;
		Color TextColor = Game.ColorPalette[5], LineColor = Game.ColorPalette[9] ;
		Color[] AttributeColor = new Color[] {Game.ColorPalette[5], Game.ColorPalette[5], Game.ColorPalette[6], Game.ColorPalette[3], Game.ColorPalette[9]} ;
		Pos.y += H ;
		//DP.DrawLine(new int[] {Pos.x + (int)(0.025*L), Pos.x + (int)(0.025*L)}, new int[] {(int)(Pos.y - H + 0.5*sy), (int)(Pos.y - H + 8.5*sy)}, lineW, LineColor) ;
		//DP.DrawLine(new int[] {Pos.x + (int)(0.375*L), Pos.x + (int)(0.375*L)}, new int[] {(int)(Pos.y - H + 0.5*sy), (int)(Pos.y - H + 3.5*sy)}, lineW, LineColor) ;
		//DP.DrawLine(new int[] {Pos.x + (int)(0.975*L), Pos.x + (int)(0.975*L)}, new int[] {(int)(Pos.y - H + 0.5*sy), (int)(Pos.y - H + 8.5*sy)}, lineW, LineColor) ;
		//DP.DrawLine(new int[] {Pos.x + (int)(0.025*L), Pos.x + (int)(0.975*L)}, new int[] {(int)(Pos.y - H + 0.5*sy), (int)(Pos.y - H + 0.5*sy)}, lineW, LineColor) ;
		//DP.DrawLine(new int[] {Pos.x + (int)(0.025*L), Pos.x + (int)(0.975*L)}, new int[] {(int)(Pos.y - H + 8.5*sy), (int)(Pos.y - H + 8.5*sy)}, lineW, LineColor) ;
		//DP.DrawLine(new int[] {Pos.x + (int)(0.025*L), Pos.x + (int)(0.975*L)}, new int[] {(int)(Pos.y - H + 1.5*sy), (int)(Pos.y - H + 1.5*sy)}, lineW, LineColor) ;
		//DP.DrawLine(new int[] {Pos.x + (int)(0.025*L), Pos.x + (int)(0.975*L)}, new int[] {(int)(Pos.y - H + 2.5*sy), (int)(Pos.y - H + 2.5*sy)}, lineW, LineColor) ;
		//DP.DrawLine(new int[] {Pos.x + (int)(0.375*L + 2*sx), Pos.x + (int)(0.375*L + 2*sx)}, new int[] {(int)(Pos.y - H + 1.5*sy), (int)(Pos.y - H + 2.5*sy)}, lineW, LineColor) ;
		DP.DrawText(new Point(Pos.x + (int)(0.65*L), (int)(Pos.y - H + sy)), Align.center, OverallAngle, AllText[SpecialAttrPropCat][1], font, TextColor) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.375*L + sx), (int)(Pos.y - H + 2*sy)), Align.center, OverallAngle, AllText[SpecialAttrPropCat][2], font, TextColor) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.375*L + 3*sx), (int)(Pos.y - H + 2*sy)), Align.center, OverallAngle, AllText[SpecialAttrPropCat][3], font, TextColor) ;
		DP.DrawText(new Point(Pos.x + (int)(0.45*L), (int)(Pos.y - H + 3*sy)), Align.center, OverallAngle, AllText[SpecialAttrPropCat][4], font, TextColor) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.45*L + sx), (int)(Pos.y - H + 3*sy)), Align.center, OverallAngle, AllText[SpecialAttrPropCat][5], font, TextColor) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.45*L + 2*sx), (int)(Pos.y - H + 3*sy)), Align.center, OverallAngle, AllText[SpecialAttrPropCat][4], font, TextColor) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.45*L + 3*sx), (int)(Pos.y - H + 3*sy)), Align.center, OverallAngle, AllText[SpecialAttrPropCat][5], font, TextColor) ;	
		for (int i = 0 ; i <= 4 ; ++i)
		{
			//DP.DrawLine(new int[] {Pos.x + (int)(0.025*L), Pos.x + (int)(0.975*L)}, new int[] {(int)(Pos.y - H + (i + 3.5)*sy), (int)(Pos.y - H + (i + 3.5)*sy)}, lineW, LineColor) ;
			DP.DrawText(new Point(Pos.x + (int)(0.2*L), (int)(Pos.y - H + (i + 4)*sy)), Align.center, OverallAngle, AllText[AttrCat][i + 11], font, AttributeColor[i]) ;	
		}
		for (int i = 0 ; i <= 3 ; ++i)
		{
			//DP.DrawLine(new int[] {Pos.x + (int)(0.375*L + i*sx), Pos.x + (int)(0.375*L + i*sx)}, new int[] {(int)(Pos.y - H + 2*sy + sy/2), (int)(Pos.y - H + 8*sy + sy/2)}, lineW, LineColor) ;
			DP.DrawText(new Point(Pos.x + (int)(0.45*L + i*sx), (int)(Pos.y - H + 4*sy)), Align.center, OverallAngle, String.valueOf(UtilG.Round(BA.getStun()[i], 2)), font, AttributeColor[0]) ;	
			DP.DrawText(new Point(Pos.x + (int)(0.45*L + i*sx), (int)(Pos.y - H + 5*sy)), Align.center, OverallAngle, String.valueOf(UtilG.Round(BA.getBlock()[i], 2)), font, AttributeColor[1]) ;	
			DP.DrawText(new Point(Pos.x + (int)(0.45*L + i*sx), (int)(Pos.y - H + 6*sy)), Align.center, OverallAngle, String.valueOf(UtilG.Round(BA.getBlood()[i], 2)), font, AttributeColor[2]) ;	
			DP.DrawText(new Point(Pos.x + (int)(0.45*L + i*sx), (int)(Pos.y - H + 7*sy)), Align.center, OverallAngle, String.valueOf(UtilG.Round(BA.getPoison()[i], 2)), font, AttributeColor[3]) ;	
			DP.DrawText(new Point(Pos.x + (int)(0.45*L + i*sx), (int)(Pos.y - H + 8*sy)), Align.center, OverallAngle, String.valueOf(UtilG.Round(BA.getSilence()[i], 2)), font, AttributeColor[4]) ;				
		}
		
		//DP.DrawLine(new int[] {Pos.x + (int)(0.025*L), Pos.x + (int)(0.025*L)}, new int[] {(int)(Pos.y - H + 9.5*sy), (int)(Pos.y - H + 14.5*sy)}, lineW, LineColor) ;
		//DP.DrawLine(new int[] {Pos.x + (int)(0.375*L), Pos.x + (int)(0.375*L)}, new int[] {(int)(Pos.y - H + 9.5*sy), (int)(Pos.y - H + 12.5*sy)}, lineW, LineColor) ;
		//DP.DrawLine(new int[] {Pos.x + (int)(0.975*L), Pos.x + (int)(0.975*L)}, new int[] {(int)(Pos.y - H + 9.5*sy), (int)(Pos.y - H + 14.5*sy)}, lineW, LineColor) ;
		//DP.DrawLine(new int[] {Pos.x + (int)(0.025*L), Pos.x + (int)(0.975*L)}, new int[] {(int)(Pos.y - H + 9.5*sy), (int)(Pos.y - H + 9.5*sy)}, lineW, LineColor) ;
		//DP.DrawLine(new int[] {Pos.x + (int)(0.025*L), Pos.x + (int)(0.975*L)}, new int[] {(int)(Pos.y - H + 14.5*sy), (int)(Pos.y - H + 14.5*sy)}, lineW, LineColor) ;
		//DP.DrawLine(new int[] {Pos.x + (int)(0.025*L), Pos.x + (int)(0.975*L)}, new int[] {(int)(Pos.y - H + 10.5*sy), (int)(Pos.y - H + 10.5*sy)}, lineW, LineColor) ;
		//DP.DrawLine(new int[] {Pos.x + (int)(0.025*L), Pos.x + (int)(0.975*L)}, new int[] {(int)(Pos.y - H + 11.5*sy), (int)(Pos.y - H + 11.5*sy)}, lineW, LineColor) ;
		//DP.DrawLine(new int[] {Pos.x + (int)(0.375*L + 2*sx), Pos.x + (int)(0.375*L + 2*sx)}, new int[] {(int)(Pos.y - H + 10.5*sy), (int)(Pos.y - H + 12.5*sy)}, lineW, LineColor) ;
		DP.DrawText(new Point(Pos.x + (int)(0.65*L), (int)(Pos.y - H + 10*sy)), Align.center, OverallAngle, AllText[SpecialAttrPropCat][6], font, TextColor) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.375*L + sx), (int)(Pos.y - H + 11*sy)), Align.center, OverallAngle, AllText[SpecialAttrPropCat][2], font, TextColor) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.375*L + 3*sx), (int)(Pos.y - H + 11*sy)), Align.center, OverallAngle, AllText[SpecialAttrPropCat][3], font, TextColor) ;
		DP.DrawText(new Point(Pos.x + (int)(0.45*L), (int)(Pos.y - H + 12*sy)), Align.center, OverallAngle, AllText[SpecialAttrPropCat][4], font, TextColor) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.45*L + sx), (int)(Pos.y - H + 12*sy)), Align.center, OverallAngle, AllText[SpecialAttrPropCat][5], font, TextColor) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.45*L + 2*sx), (int)(Pos.y - H + 12*sy)), Align.center, OverallAngle, AllText[SpecialAttrPropCat][4], font, TextColor) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.45*L + 3*sx), (int)(Pos.y - H + 12*sy)), Align.center, OverallAngle, AllText[SpecialAttrPropCat][5], font, TextColor) ;	
		for (int i = 0 ; i <= 1 ; ++i)
		{
			//DP.DrawLine(new int[] {Pos.x + (int)(0.025*L), Pos.x + (int)(0.975*L)}, new int[] {(int)(Pos.y - H + (i + 12.5)*sy), (int)(Pos.y - H + (i + 12.5)*sy)}, lineW, LineColor) ;
			DP.DrawText(new Point(Pos.x + (int)(0.2*L), (int)(Pos.y - H + (i + 13)*sy)), Align.center, OverallAngle, AllText[AttrCat][i + 13], font, AttributeColor[i + 2]) ;	
		}
		for (int i = 0 ; i <= 3 ; ++i)
		{
			//DP.DrawLine(new int[] {Pos.x + (int)(0.375*L + i*sx), Pos.x + (int)(0.375*L + i*sx)}, new int[] {(int)(Pos.y - H + 11.5*sy), (int)(Pos.y - H + 14.5*sy)}, lineW, LineColor) ;
			DP.DrawText(new Point(Pos.x + (int)(0.45*L + i*sx), (int)(Pos.y - H + 13*sy)), Align.center, OverallAngle, String.valueOf(UtilG.Round(BA.getBlood()[i + 4], 2)), font, AttributeColor[2]) ;	
			DP.DrawText(new Point(Pos.x + (int)(0.45*L + i*sx), (int)(Pos.y - H + 14*sy)), Align.center, OverallAngle, String.valueOf(UtilG.Round(BA.getPoison()[i + 4], 2)), font, AttributeColor[3]) ;	
		}
		
		DP.DrawText(new Point(Pos.x + (int)(0.025*L), (int)(Pos.y - H + 16*sy)), Align.bottomLeft, OverallAngle, AllText[AttrCat][11] + " " + AllText[SpecialAttrPropCat][7] + " = " + UtilG.Round(BA.getStun()[4], 2), font, AttributeColor[0]) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.025*L), (int)(Pos.y - H + 17*sy)), Align.bottomLeft, OverallAngle, AllText[AttrCat][12] + " " + AllText[SpecialAttrPropCat][7] + " = " + UtilG.Round(BA.getBlock()[4], 2), font, AttributeColor[1]) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.025*L), (int)(Pos.y - H + 18*sy)), Align.bottomLeft, OverallAngle, AllText[AttrCat][13] + " " + AllText[SpecialAttrPropCat][7] + " = " + UtilG.Round(BA.getBlood()[8], 2), font, AttributeColor[2]) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.025*L), (int)(Pos.y - H + 19*sy)), Align.bottomLeft, OverallAngle, AllText[AttrCat][14] + " " + AllText[SpecialAttrPropCat][7] + " = " + UtilG.Round(BA.getPoison()[8], 2), font, AttributeColor[3]) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.025*L), (int)(Pos.y - H + 20*sy)), Align.bottomLeft, OverallAngle, AllText[AttrCat][15] + " " + AllText[SpecialAttrPropCat][7] + " = " + UtilG.Round(BA.getSilence()[4], 2), font, AttributeColor[4]) ;				
	}*/
//	private void DrawSpellsBar(Player player, ArrayList<Spell> spells, Image CooldownImage, Image SlotImage, Point MousePos, DrawingOnPanel DP)
//	{
//		Font Titlefont = new Font("SansSerif", Font.BOLD, 10) ;
//		Font font = new Font("SansSerif", Font.BOLD, 9) ;
//		Screen screen = Game.getScreen() ;
//		Color[] colorPalette = Game.ColorPalette ;
//		double OverallAngle = DrawingOnPanel.stdAngle ;
//		Point Pos = new Point(screen.getSize().width + 1, (int) (0.99 * screen.getSize().height) - 70) ;
//		List<Spell> ActiveSpells = player.GetActiveSpells() ;
//		Dimension size = new Dimension(36, 130) ;
//		int slotW = SlotImage.getWidth(null), slotH = SlotImage.getHeight(null) ;	// Bar size
//		int Ncols = Math.max(ActiveSpells.size() / 11 + 1, 1) ;
//		int Nrows = ActiveSpells.size() / Ncols + 1 ;
//		int Sx = (int) UtilG.spacing(size.width, Ncols, slotW, 3), Sy = (int) UtilG.spacing(size.height, Nrows, slotH, 5) ;		
//		String[] Key = Player.SpellKeys ;
//		Color BGcolor = Player.ClassColors[player.getJob()] ;
//		Color TextColor = player.getColor() ;
//		
//		DP.DrawRoundRect(Pos, Align.bottomLeft, size, 1, colorPalette[7], BGcolor, true) ;
//		DP.DrawText(new Point(Pos.x + size.width / 2, Pos.y - size.height + 3), Align.topCenter, OverallAngle, allText.get("* Barra de habilidades *")[1], Titlefont, colorPalette[5]) ;
//		for (int i = 0 ; i <= ActiveSpells.size() - 1 ; ++i)
//		{
//			Spell spell = spells.get(i) ;
//			if (0 < player.getSpell().get(i).getLevel())
//			{
//				Point slotCenter = new Point(Pos.x + slotW / 2 + (i / Nrows) * Sx + 3, Pos.y - size.height + slotH / 2 + (i % Nrows) * Sy + Titlefont.getSize() + 5) ;
//				if (player.getMp().getCurrentValue() < spell.getMpCost())
//				{
//					DP.DrawImage(SlotImage, slotCenter, Align.center) ;
//				}
//				else
//				{
//					DP.DrawImage(SlotImage, slotCenter, Align.center) ;
//				}
//				DP.DrawText(slotCenter, Align.center, OverallAngle, Key[i], font, TextColor) ;
//				Dimension imgSize = new Dimension(CooldownImage.getWidth(null), CooldownImage.getHeight(null)) ;
//				if (spell.getCooldownCounter() < spell.getCooldown())
//				{
//					Scale Imscale = new Scale(1, 1 - (double) spell.getCooldownCounter() / spell.getCooldown()) ;
//					DP.DrawImage(CooldownImage, new Point(slotCenter.x - imgSize.width / 2, slotCenter.y + imgSize.height / 2), OverallAngle, Imscale, Align.bottomLeft) ;
//				}
//				if (UtilG.isInside(MousePos, new Point(slotCenter.x - imgSize.width / 2, slotCenter.y - imgSize.height / 2), imgSize))
//				{
//					DP.DrawText(new Point(slotCenter.x - imgSize.width - 10, slotCenter.y), Align.centerRight, OverallAngle, spell.getName(), Titlefont, TextColor) ;
//				}
//			}
//		}
//	}
//	public void DrawSideBar(Pet pet, Point MousePos, GameIcon[] icons, DrawingOnPanel DP)
//	{
//		// icons: 0: Options 1: Bag 2: Quest 3: Map 4: Book, 5: player, 6: pet
//		Point barPos = new Point(Game.getScreen().getSize().width, Game.getScreen().getSize().height);
//		Dimension size = new Dimension(40, Game.getScreen().getSize().height) ;
//		Color[] colorPalette = Game.ColorPalette ;
//		double stdAngle = DrawingOnPanel.stdAngle ;
//		Font font = new Font("SansSerif", Font.BOLD, 13) ;
//		String[] IconKey = new String[] {Player.ActionKeys[4], Player.ActionKeys[9], Player.ActionKeys[7]} ;
//		Color TextColor = colorPalette[7] ;
//		
//		DP.DrawRect(barPos, Align.bottomLeft, size, 1, colorPalette[9], null) ;
//		DrawSpellsBar(this, spells, SpellType.cooldownImage, SpellType.slotImage, MousePos, DP) ;
//		icons[0].display(stdAngle, Align.topLeft, MousePos, DP) ;		// settings
//		icons[1].display(stdAngle, Align.topLeft, MousePos, DP) ;		// bag
//		DP.DrawText(icons[1].getPos(), Align.bottomLeft, stdAngle, IconKey[0], font, TextColor) ;
//		icons[2].display(stdAngle, Align.topLeft, MousePos, DP) ;		// quest
//		DP.DrawText(icons[2].getPos(), Align.bottomLeft, stdAngle, IconKey[1], font, TextColor) ;
//		
//		if (questSkills.get(QuestSkills.getContinentMap(map.getContinentName(this).name())))	// map
//		{
//			icons[3].display(stdAngle, Align.topLeft, MousePos, DP) ;
//			DP.DrawText(icons[3].getPos(), Align.bottomLeft, stdAngle, IconKey[2], font, TextColor) ;
//		}
//		
//		if (fabWindow != null)										// book
//		{
//			icons[4].display(stdAngle, Align.topLeft, MousePos, DP) ;
//		}
//		
//		// player
//		//display(UtilG.Translate(icons[5].getPos(), icons[5].getWidth(null) / 2, 0), new Scale(1, 1), Directions.up, false, DP) ;
//		DP.DrawText(icons[5].getPos(), Align.bottomLeft, stdAngle, Player.ActionKeys[5], font, TextColor) ;
////		if (0 < attPoints)
////		{
////			DP.DrawImage(icons[5], UtilG.Translate(icons[5].getPos(), icons[5].getWidth(null) / 2, 0), stdAngle, new Scale(1, 1), Align.bottomLeft) ;
////		}
//		
//		// pet
//		if (pet != null)
//		{
//			//pet.display(UtilG.Translate(icons[6].getPos(), icons[6].getWidth(null) / 2, 0), new Scale(1, 1), DP) ;
//			DP.DrawText(icons[6].getPos(), Align.bottomLeft, stdAngle, Player.ActionKeys[8], font, TextColor) ;
//		}
//		
//		// Hotkeys
//		DP.DrawRoundRect(new Point(Game.getScreen().getSize().width + 1, Game.getScreen().getSize().height - 70), Align.topLeft, new Dimension(36, 60), 1, colorPalette[7], colorPalette[19], true) ;
//		for (int i = 0 ; i <= Player.HotKeys.length - 1 ; i += 1)
//		{
//			Point slotCenter = new Point(Game.getScreen().getSize().width + 10, Game.getScreen().getSize().height - 60 + 20 * i) ;
//			Dimension slotSize = new Dimension(SpellType.slotImage.getWidth(null), SpellType.slotImage.getHeight(null)) ;
//			DP.DrawImage(SpellType.slotImage, slotCenter, Align.center) ;
//			DP.DrawText(new Point(slotCenter.x + slotSize.width / 2 + 5, slotCenter.y + slotSize.height / 2), Align.bottomLeft, stdAngle, Player.HotKeys[i], font, TextColor) ;
//			if (hotItem[i] != null)
//			{
//				DP.DrawImage(hotItem[i].getImage(), slotCenter, Align.center) ;
//				if (UtilG.isInside(MousePos, slotCenter, slotSize))
//				{
//					DP.DrawText(new Point(slotCenter.x - slotSize.width - 10, slotCenter.y), Align.centerRight, stdAngle, hotItem[i].getName(), font, TextColor) ;
//				}
//			}
//		}
//	}
	/*
	 * 

//	private int GetNumberOfSpells()
//	{
//		int NumberOfSpells = 0 ;
//		int[] Sequence = GetSpellSequence() ;
//		for (int i = 0 ; i <= Sequence.length - 1 ; i += 1)
//		{
//			NumberOfSpells += Sequence[i] ;
//		}
//		return NumberOfSpells ;
//	}
//	private int GetNumberOfProSpells()
//	{
//		int NumberOfProSpells = 0 ;
//		int[] ProSequence = GetProSpellSequence() ;
//		for (int i = 0 ; i <= ProSequence.length - 1 ; i += 1)
//		{
//			NumberOfProSpells += ProSequence[i] ;
//		}
//		return NumberOfProSpells ;
//	}
//	private int[] GetSpellSequence()
//	{
//		// Sequence: [Player job][Number of spells per line]
//		int[][] Sequence = new int[][] {{1, 3, 3, 3, 3, 1}, {3, 3, 3, 3, 3}, {3, 3, 3, 3, 3}, {2, 3, 3, 3, 3}, {2, 3, 3, 3, 3}} ;
//		return Sequence[job] ;
//	}
//	private int[] GetProSpellSequence()
//	{
//		// Sequence: [Player job][Number of spells per line]
//		int[][] Sequence = new int[][] {{1, 2, 2, 2, 2, 1}, {1, 2, 2, 2, 2, 1}, {1, 2, 2, 2, 2, 1}, {1, 2, 2, 2, 2, 1}, {1, 2, 2, 2, 2, 1}, {1, 2, 2, 2, 2, 1}, {1, 2, 2, 2, 2, 1}, {1, 2, 2, 2, 2, 1}, {1, 2, 2, 2, 2, 1}, {1, 2, 2, 2, 2, 1}} ;
//		return Sequence[PA.ProJob] ;
//	}
	 * public void Load(String FileName, Pet pet, Maps[] maps)
	{
		int Nrows = 1600 ;
		String[][] ReadFile = UtilG.ReadTextFile(FileName, Nrows) ;
		if (ReadFile[2*0][0].equals("3.41"))	// Save version
		{
			PA.setName(ReadFile[2*1][0]) ;
			Language = ReadFile[2*2][0] ;
			Sex = ReadFile[2*3][0] ;
			setSize((int[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*4]), "String", "int")) ;
			//color = UtilG.toColor(ReadFile[2*6]) ;
			job = Integer.parseInt(ReadFile[2*7][0]) ;
			PA.setProJob(Integer.parseInt(ReadFile[2*8][0])) ;
			setMap(Game.getMaps()[Integer.parseInt(ReadFile[2*10][0])]) ;
			setPos((Point) UtilG.ConvertArray(UtilG.toString(ReadFile[2*11]), "String", "int")) ;
			//spell = (Spell[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*12]), "String", "int") ;
			//QuestWindow = (int[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*13]), "String", "int") ;
			//setBag((int[]) Utg.ConvertArray(Utg.toString(ReadFile[2*14]), "String", "int")) ;
			Equips = (int[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*15]), "String", "int") ;
			SpellPoints = Integer.parseInt(ReadFile[2*16][0]) ;
			PA.setLife((double[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*17]), "String", "double")) ;
			PA.setMp((double[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*18]), "String", "double")) ;
			PA.setRange(Double.parseDouble(ReadFile[2*19][0])) ;
			BA.setPhyAtk((double[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*20]), "String", "double")) ;
			BA.setMagAtk((double[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*21]), "String", "double")) ;
			BA.setPhyDef((double[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*22]), "String", "double")) ;
			BA.setMagDef((double[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*23]), "String", "double")) ;
			BA.setDex((double[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*24]), "String", "double")) ;
			BA.setAgi((double[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*25]), "String", "double")) ;
			BA.setCrit((double[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*26]), "String", "double")) ;
			BA.setStun((double[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*27]), "String", "double")) ;
			BA.setBlock((double[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*28]), "String", "double")) ;
			BA.setBlood((double[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*29]), "String", "double")) ;
			BA.setPoison((double[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*30]), "String", "double")) ;
			BA.setSilence((double[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*31]), "String", "double")) ;
			elem = UtilG.toString(ReadFile[2*32]) ;
			ElemMult = (double[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*33]), "String", "double") ;
			Collect = (double[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*34]), "String", "double") ;
			PA.setLevel(Integer.parseInt(ReadFile[2*35][0])) ;
			Gold = (double[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*36]), "String", "double") ;
			setStep(Integer.parseInt(ReadFile[2*37][0])) ;
			PA.setExp((double[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*38]), "String", "double")) ;
			PA.setSatiation((double[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*39]), "String", "double")) ;
			QuestSkills = (boolean[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*40]), "String", "boolean") ;
			BA.setSpecialStatus((int[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*41]), "String", "int")) ;
			PA.Actions = (int[][]) UtilG.ConvertDoubleArray(UtilG.deepToString(ReadFile[2*42], 3), "String", "int") ;
			BA.setBattleActions((int[][]) UtilG.ConvertDoubleArray(UtilG.deepToString(ReadFile[2*43], 3), "String", "int")) ;
			SpellIsActive = new boolean[getSpell().length] ;
			SpellCounter = new int[getSpell().length][2] ;
			StatusCounter = (int[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*44]), "String", "int") ;
			Stats = (double[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*45]), "String", "double") ;
			AttPoints = Integer.parseInt(ReadFile[2*46][0]) ;
			AttIncrease = (double[][]) UtilG.ConvertDoubleArray(UtilG.deepToString(ReadFile[2*47], 8), "String", "double") ;
			ChanceIncrease = (double[][]) UtilG.ConvertDoubleArray(UtilG.deepToString(ReadFile[2*48], 8), "String", "double") ;
			if (!UtilG.toString(ReadFile[2*49])[0].equals("null"))
			{
				//CreaturesDiscovered = (int[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*49]), "String", "int") ;
			}
			else
			{
				CreaturesDiscovered = null ;
			}
			pet.Load(ReadFile) ;
		}
	}
	 * */

	
	/*public void QuestWindow(CreatureTypes[] creatureTypes, Creatures[] creatures, Quests[] quest, Items[] items, Point MousePos, DrawFunctions DF)
	{
		int[] windowLimit = new int[] {8, 0, 0, 1, 0, 0} ;
		int[] ActiveQuests = Utg.ArrayWithValuesGreaterThan(Quest, -1) ;
		//windowLimit[player.getContinent()] = Math.max(ActiveQuests.length - 1, 0)/5 ;
		SelectedWindow[3] = Uts.MenuSelection(ActionKeys[1], ActionKeys[3], action, SelectedWindow[3], windowLimit[PA.getContinent()]) ;
		//DF.DrawQuestWindow(creatureTypes, creatures, quest, items, Language, PA.getContinent(), Bag, MousePos, ActiveQuests, SelectedWindow[3], windowLimit, QuestImage) ;
	}*/
	/*public int Collect(Maps[] maps, Items[] item, int[] Pos, int CollectibleType, int[] BagIDs, int CollectResult, boolean PlayerIsCollecting, DrawFunctions DF)
	{	
		double CollectChance = (double) ((maps[getMap()].getCollectibleLevel() + 2)*Math.random()) ;
		int MapCollectLevel = maps[getMap()].getCollectibleLevel() ;
		double PlayerCollectLevel = -1 ;
		if (CollectibleType == 0)	// Berry
		{
			Bag[BagIDs[3] - 1 + MapCollectLevel + CollectibleType] += 1 ;
			if (job == 3 & Math.random() <= 0.14*Skill[4])	// Double collect
			{
				Bag[BagIDs[3] - 1 + MapCollectLevel + CollectibleType] += 1 ;	
			}
			CollectResult = 0 ;
		}
		else if (MapCollectLevel <= PlayerCollectLevel + 1 & PlayerCollectLevel + 1 < CollectChance)
		{					
			Bag[BagIDs[0] + 3*(MapCollectLevel - 1) + CollectibleType - 1] += 1 ;
			Collect[CollectibleType - 1] += 0.25/(PlayerCollectLevel + 1) ;
			if (job == 3 & Math.random() <= 0.14*Skill[4])	// Double collect
			{
				Bag[BagIDs[0] + 3*(MapCollectLevel - 1) + CollectibleType - 1] += 1 ;
				Collect[CollectibleType - 1] += 0.25/(PlayerCollectLevel + 1) ;	
			}
			CollectResult = 0 ;
		}
		else if (MapCollectLevel <= PlayerCollectLevel + 1)
		{
			CollectResult = 1 ;
		}
		else
		{
			CollectResult = 2 ;
		}	
		if (!PlayerIsCollecting)
		{
			if (CollectResult == 0)
			{
				return -1 ;
			}
			else
			{
				return CollectibleType ;
			}
		}
		return -1 ;
	}*/
	/*public boolean usedSkill()
	{
		return Utg.isNumeric(BA.getCurrentAction()) ;
	}*/
	/*public void DrawAttWindow(int[] MainWinDim, Point WindowPos, Point MousePos, String[][] AllText, int[] AllTextCat, int Tab, Image GoldCoinImage, Icon[] icons, DrawPrimitives DP)
	{
		double TextAngle = DrawPrimitives.OverallAngle ;
		int L = Player.AttWindow[0].getWidth(null), H = Player.AttWindow[0].getHeight(null) ;
		Point PlayerImagePos = new Point(WindowPos.x + (int) (0.55 * L), WindowPos.y - (int) (0.85 * H)) ;
		int ClassesCat = -1, ProClassesCat = -1, AttCat = -1, CollectCat = -1, EquipsCat = -1, TabsCat = -1 ;
		Font Namefont = new Font("GothicE", Font.BOLD, L / 28 + 1) ;
		Font font = new Font("GothicE", Font.BOLD, L / 28 + 2) ;
		Font Equipfont = new Font("GothicE", Font.BOLD, L / 28) ;
		Color[] ColorPalette = Game.ColorPalette ;
		Color[] TabColor = new Color[] {ColorPalette[7], ColorPalette[7], ColorPalette[7]}, TabTextColor = new Color[] {ColorPalette[5], ColorPalette[5], ColorPalette[5]} ;
		Color TextColor = ColorPalette[2] ;
		int TextH = Utg.TextH(font.getSize()) ;
		if (AllTextCat != null)
		{
			ClassesCat = AllTextCat[4] ;
			ProClassesCat = AllTextCat[5] ;
			AttCat = AllTextCat[6] ;
			CollectCat = AllTextCat[9] ;
			EquipsCat = AllTextCat[11] ;
			TabsCat = AllTextCat[40] ;
		}
		TabColor[Tab] = ColorPalette[19] ;
		TabTextColor[Tab] = ColorPalette[3] ;
		
		// Main window
		DP.DrawImage(Player.AttWindow[Tab], WindowPos, "TopLeft") ;
		DP.DrawText(new Point(WindowPos.x + 5, WindowPos.y + (int)(0.05*H)), AlignmentPoints.center, 90, AllText[TabsCat][1], Namefont, TabTextColor[0]) ;				// Tab 0 text	
		DP.DrawText(new Point(WindowPos.x + 5, WindowPos.y + (int)(0.15*H)), AlignmentPoints.center, 90, AllText[TabsCat][2], Namefont, TabTextColor[1]) ;				// Tab 1 text	
		DP.DrawText(new Point(WindowPos.x + 5, WindowPos.y + (int)(0.25*H)), AlignmentPoints.center, 90, AllText[TabsCat][3], Namefont, TabTextColor[2]) ;				// Tab 2 text	
		if (Tab == 0)
		{
			//	Player
			display(PlayerImagePos, new double[] {(double) 1.8, (double) 1.8}, dir, false, DP) ;
			DP.DrawText(new Point(WindowPos.x + (int)(0.5*L), WindowPos.y + (int)(0.1*H)), AlignmentPoints.center, TextAngle, PA.getName(), Namefont, TextColor) ;						// Name text			
			DP.DrawText(new Point(WindowPos.x + (int)(0.5*L), WindowPos.y + (int)(0.03*H)), AlignmentPoints.center, TextAngle, AllText[AttCat][1] + ": " + level, font, ColorPalette[6]) ;	// Level text		
			if(PA.ProJob == 0)
			{
				DP.DrawText(new Point(WindowPos.x + (int)(0.5*L), WindowPos.y + (int)(0.06*H)), AlignmentPoints.center, TextAngle, AllText[ClassesCat][job + 1], font, ColorPalette[5]) ;	// job text			
			}
			else
			{
				DP.DrawText(new Point(WindowPos.x + (int)(0.5*L), WindowPos.y + (int)(0.15*H + TextH/2)), AlignmentPoints.center, TextAngle, AllText[ProClassesCat][PA.ProJob + 2*job], font, ColorPalette[5]) ;	// Pro job text					
			}
			
			//	Equips
			int[] EqRectL = new int[] {51, 51, 51, 8} ;
			int[] EqRectH = new int[] {51, 51, 51, 24} ;
			Point[] EqRectPos = new Point[] {new Point(WindowPos.x + 62, WindowPos.y + 78),
					new Point(WindowPos.x + 200, WindowPos.y + 43),
					new Point(WindowPos.x + 200, WindowPos.y + 96), 
					new Point(WindowPos.x + 53, WindowPos.y + 90)} ;	// Weapon, armor, shield, arrow
			for (int eq = 0 ; eq <= 4 - 1 ; eq += 1)
			{
				Image ElemImage = DrawFunctions.ElementImages[Uts.ElementID(elem[eq + 1])] ;
				if (0 < Equips[eq])
				{
					if (eq <= 2)
					{
						if (0 < EquipsBonus[Equips[eq] - Items.BagIDs[6]][0])
						{
							if (-1 < EquipsCat)
							{
								DP.DrawText(new Point(EqRectPos[eq].x, EqRectPos[eq].y - EqRectH[eq] / 2 - TextH), AlignmentPoints.center, TextAngle, AllText[EquipsCat][eq + 1] + " + " + (int)(EquipsBonus[Equips[eq] - Items.BagIDs[6]][1]), font, TextColor) ;					
							}
						}
						DrawEquips(EqRectPos[eq], job, eq, Equips[eq] - Items.BagIDs[6], EquipsBonus, new double[] {1, 1}, TextAngle, DP) ;
					}
					else if (eq == 3)
					{
						DrawEquips(EqRectPos[eq], job, eq, Equips[0] - Items.BagIDs[6], EquipsBonus, new double[] {1, 1}, TextAngle, DP) ;
					}
					DP.DrawImage(ElemImage, new Point((int) (EqRectPos[eq].x + 0.3*EqRectL[eq]), (int) (EqRectPos[eq].y + 0.3*EqRectH[eq])), TextAngle, new double[] {(double) 0.12, (double) 0.12}, AlignmentPoints.center) ;					
					//DP.DrawTextUntil(new Point(EqRectPos[eq].x - EqRectL[eq] / 2, EqRectPos[eq].y + EqRectH[eq] / 2 + TextH), AlignmentPoints.bottomLeft, TextAngle, items[Equips[eq]].getName(), Equipfont, TextColor, 14, MousePos) ;	// Equip text	
				}
				else
				{
					DP.DrawText(new Point(EqRectPos[eq].x + EqRectL[eq]/2, EqRectPos[eq].y - EqRectH[eq] / 2 - TextH), AlignmentPoints.center, TextAngle, AllText[EquipsCat][eq + 1], font, TextColor) ;
				}
			}
			
			// Super element
			if (elem[1].equals(elem[2]) & elem[2].equals(elem[3]))
			{
				DP.DrawImage(DrawFunctions.ElementImages[Uts.ElementID(elem[4])], new Point(WindowPos.x + (int)(0.5*L), WindowPos.y + (int)(0.45*H)), TextAngle, new double[] {(double) 0.3, (double) 0.3}, AlignmentPoints.center) ;
			}
			
			//	Attributes
			double[] Attributes = new double[] {BA.TotalPhyAtk(), BA.TotalMagAtk(), BA.TotalPhyDef(), BA.TotalMagDef(), BA.TotalDex(), BA.TotalAgi()} ;
			int AttSy = 22 ;
			DP.DrawText(new Point(WindowPos.x + 15, WindowPos.y + 30), AlignmentPoints.bottomLeft, TextAngle, AllText[AttCat][2] + ": " + Utg.Round(PA.getLife()[0], 1), font, ColorPalette[6]) ;	// Life text	
			DP.DrawText(new Point(WindowPos.x + 15, WindowPos.y + 40), AlignmentPoints.bottomLeft, TextAngle, AllText[AttCat][3] + ": " + Utg.Round(PA.getMp()[0], 1), font, ColorPalette[5]) ;	// MP text

			DP.DrawImage(Equip.SwordImage, new Point(WindowPos.x + 30, WindowPos.y + 134 + 1 * AttSy), new double[] {(double) (11 / 38.0), (double) (11 / 38.0)}, AlignmentPoints.center) ;	// Draw sword icon
			for (int i = 0; i <= Attributes.length - 1; i += 1)
			{
				DP.DrawText(new Point(WindowPos.x + 45, WindowPos.y + 136 + (i + 1) * AttSy), AlignmentPoints.bottomLeft, TextAngle, AllText[AttCat][4] + ": " + Utg.Round(Attributes[i], 1), font, TextColor) ;
			}	
			DP.DrawText(new Point(WindowPos.x + 45, WindowPos.y + 136 + 7 * AttSy), AlignmentPoints.bottomLeft, TextAngle, AllText[AttCat][10] + ": " + Utg.Round(100 * BA.TotalCritAtkChance(), 1) + "%", font, ColorPalette[6]) ;		
			
			//	Collection
			if (Language.equals("P"))
			{	
				DP.DrawText(new Point(WindowPos.x + (int)(0.95*L), WindowPos.y + (int)(0.895*H)), "TopRight", TextAngle, AllText[AttCat][17] + " de " + AllText[CollectCat][3] + " = " + Utg.Round(Collect[0], 1), font, DrawFunctions.MapsTypeColor[13]) ;		
				DP.DrawText(new Point(WindowPos.x + (int)(0.95*L), WindowPos.y + (int)(0.945*H)), "TopRight", TextAngle, AllText[AttCat][17] + " de " + AllText[CollectCat][4] + " = " + Utg.Round(Collect[1], 1), font, DrawFunctions.MapsTypeColor[14]) ;		
				DP.DrawText(new Point(WindowPos.x + (int)(0.95*L), WindowPos.y + (int)(0.975*H)), "TopRight", TextAngle, AllText[AttCat][17] + " de " + AllText[CollectCat][5] + " = " + Utg.Round(Collect[2], 1), font, DrawFunctions.MapsTypeColor[15]) ;
			}
			else if (Language.equals("E"))
			{
				DP.DrawText(new Point(WindowPos.x + (int)(0.95*L), WindowPos.y + (int)(0.895*H)), "TopRight", TextAngle, AllText[CollectCat][3] + " " + AllText[AttCat][17] + " = " + Utg.Round(Collect[0], 1), font, DrawFunctions.MapsTypeColor[13]) ;		
				DP.DrawText(new Point(WindowPos.x + (int)(0.95*L), WindowPos.y + (int)(0.945*H)), "TopRight", TextAngle, AllText[CollectCat][4] + " " + AllText[AttCat][17] + " = " + Utg.Round(Collect[1], 1), font, DrawFunctions.MapsTypeColor[14]) ;		
				DP.DrawText(new Point(WindowPos.x + (int)(0.95*L), WindowPos.y + (int)(0.975*H)), "TopRight", TextAngle, AllText[CollectCat][5] + " " + AllText[AttCat][17] + " = " + Utg.Round(Collect[2], 1), font, DrawFunctions.MapsTypeColor[15]) ;
			}
			
			//	Gold
			DP.DrawImage(GoldCoinImage, new Point(WindowPos.x + (int)(0.04*L), WindowPos.y + (int)(0.975*H)), TextAngle, new double[] {(double) 1.2, (double) 1.2}, new boolean[] {false, false}, AlignmentPoints.bottomLeft, 1) ;
			DP.DrawText(new Point(WindowPos.x + (int)(0.18*L), WindowPos.y + (int)(0.96*H) - TextH/2), AlignmentPoints.bottomLeft, TextAngle, String.valueOf(Utg.Round(Gold[0], 1)), font, ColorPalette[18]) ;	// Gold text
		
			//	Plus sign
			if (0 < AttPoints)
			{
				for (int i = 9 ; i <= 17 - 1 ; i += 1)
				{
					icons[i].DrawImage(TextAngle, 0, MousePos, DP) ;
				}
			}
		}
		else if (Tab == 1)
		{
			DrawSpecialAttributesWindow(AllText, AllTextCat, WindowPos, L, H, DP) ;
		}
		else if (Tab == 2)
		{
			DrawStats(AllText, AllTextCat, WindowPos, L, H, Stats, DP) ;
		}
	}*/
	/*public void UseItem(Pet pet, Creatures[] creature, int creatureID, int itemID, Items[] items, Battle B)
	{
		double PotMult = 1 ;
		if (job == 3)
		{
			PotMult += 0.06 * Spell[7] ;
		}
		if (-1 < itemID)	// if the item is valid
		{
			//if (0 < Bag[itemID])	// if the player has the item in the bag
			//{
				if (items[itemID].getType().equals("Potion"))	// potions
				{
					double HealPower = Items.PotionsHealing[itemID][1] * PotMult ;
					
					PA.incLife(HealPower * PA.getLife()[1]) ;
					PA.incMP(HealPower * PA.getMp()[1]) ;
					//Bag[itemID] += -1 ;			
				}
				else if (items[itemID].getType().equals("Alchemy"))	// alchemy (using herbs heal half of their equivalent pot)
				{
					double HealPower = Items.PotionsHealing[itemID / 3 - Items.BagIDs[0] / 3 + 1][1] / 2 * PotMult ;
					
					PA.incLife(HealPower * PA.getLife()[1]) ;
					if (0 < pet.getLife()[0])
					{
						pet.incLife(HealPower * pet.getLife()[1]) ;
						pet.incMP(HealPower * pet.getMp()[1]) ;
						
						// Animal's skill "natural help": gives 20% bonus on the pet attributes. The enhanced attributes depend on the collectible used
						if (-1 < creatureID & BA.getBattleActions()[0][2] == 1 & isInBattle() & job == 3 & (itemID - Items.BagIDs[1] + 1) % 3 < Spell[10])
						{
							int ItemsWithEffectsID = -1 ;
							for (int i = 0 ; i <= Items.ItemsWithEffects.length - 1 ; ++i)
							{
								if (itemID == Items.ItemsWithEffects[i])
								{
									ItemsWithEffectsID = i ;
								}
							}
							if (-1 < ItemsWithEffectsID)
							{
								B.ItemEffectInBattle(getBattleAtt(), pet.getBA(), creature[creatureID].getBA(), creature[creatureID].getElem(), creature[creatureID].getLife(), items, ItemsWithEffectsID, Items.ItemsTargets[ItemsWithEffectsID], Items.ItemsElement[ItemsWithEffectsID], Items.ItemsEffects[ItemsWithEffectsID], Items.ItemsBuffs[ItemsWithEffectsID], "activate") ;
							}
						}
					}
					//Bag[itemID] += -1 ;
				}
				else if (items[itemID].getType().equals("Pet"))		// pet items
				{
					pet.incLife(Items.PetItems[itemID - Items.BagIDs[3] + 1][1] * pet.getLife()[1]) ;
					pet.incMP(Items.PetItems[itemID - Items.BagIDs[3] + 1][2] * pet.getMp()[1]) ;
					pet.incSatiation(Items.PetItems[itemID - Items.BagIDs[3] + 1][3]) ;
					//Bag[itemID] += -1 ;
				}
				else if (items[itemID].getType().equals("Food"))	// food
				{
					PA.incSatiation(Items.FoodSatiation[itemID - Items.BagIDs[4] + 1][3] * PA.getSatiation()[2]) ;
					//Bag[itemID] += -1 ;
				}
				else if (items[itemID].getType().equals("Arrow"))	// arrows
				{
					//if (0 < Bag[itemID] & job == 2)
					//{
						if (itemID == Items.BagIDs[5] | itemID <= Items.BagIDs[5] + 3 & 1 <= Spell[4] | itemID == Items.BagIDs[5] + 4 & 2 <= Spell[4]  | itemID == Items.BagIDs[5] + 5 & 3 <= Spell[4]  | Items.BagIDs[5] + 6 <= itemID & itemID <= Items.BagIDs[5] + 14 & 4 <= Spell[4]  | Items.BagIDs[5] + 14 <= itemID & 5 <= Spell[4])
						{
							if (0 == Equips[3])
							{
								Equips[3] = itemID ;	// Equipped arrow
								elem[0] = bag.getArrow()[itemID - Items.BagIDs[5]].getElem() ;
							}
							else
							{
								Equips[3] = 0 ;	// Unequipped arrow
								elem[0] = "n" ;
							}
						}
					//}
				}
				else if (items[itemID].getType().equals("Equip"))	// equipment
				{
					if (isEquippable(itemID))
					{
						Equip(items, itemID) ;
					}
				}
				else if (items[itemID].getType().equals("Item"))	// items
				{
					if (-1 < creatureID & BA.getBattleActions()[0][2] == 1 & isInBattle() & job == 4 & 0 < Spell[8])
					{		
						int ItemsWithEffectsID = -1 ;
						for (int i = 0 ; i <= Items.ItemsWithEffects.length - 1 ; ++i)
						{
							if (itemID == Items.ItemsWithEffects[i])
							{
								ItemsWithEffectsID = i ;
							}
						}
						if (-1 < ItemsWithEffectsID)
						{
							B.ItemEffectInBattle(getBattleAtt(), pet.getBA(), creature[creatureID].getBA(), creature[creatureID].getElem(), creature[creatureID].getLife(), items, ItemsWithEffectsID, Items.ItemsTargets[ItemsWithEffectsID], Items.ItemsElement[ItemsWithEffectsID], Items.ItemsEffects[ItemsWithEffectsID], Items.ItemsBuffs[ItemsWithEffectsID], "activate") ;
						}
						//Bag[itemID] += -1 ;
						BA.getBattleActions()[0][0] = 0 ;
						BA.getBattleActions()[0][2] = 0 ;
					}
					else
					{
						ItemEffect(itemID) ;
						//Bag[itemID] += -1 ;
					}
				}
			//}			
		}
	}*/
	/*public Settings OptionsWindow(Clip[] Music, DrawFunctions DF)
	{
		boolean CustomKeyIsActionKey = false ;
		if (SelectedMenu[0] == 0)
		{
			SelectedItem[0] = Uts.MenuSelection(Player.ActionKeys[0], Player.ActionKeys[2], action, SelectedItem[0], 5) ;
			if (action.equals("Enter") | action.equals("MouseLeftClick"))
			{
				if (SelectedItem[0] == 0)
				{
					settings.setMusicIsOn(!settings.getMusicIsOn());
					Utg.PlayMusic(Music[Maps.Music[map]]) ;
				}
				if (SelectedItem[0] == 1)
				{
					settings.setSoundEffectsAreOn(!settings.getSoundEffectsAreOn());
				}
				if (SelectedItem[0] == 2)
				{
					settings.setShowPlayerRange(!settings.getShowPlayerRange()) ;
				}
				if (SelectedItem[0] == 3)
				{
					settings.setAttDisplay((settings.getAttDisplay() + 1) % 2) ;
				}
				if (SelectedItem[0] == 4)
				{
					settings.setDamageAnimation((settings.getDamageAnimation() + 1) % 4);
				}
				if (4 < SelectedItem[0] & SelectedItem[0] <= 4 + Player.ActionKeys.length)
				{
					SelectedMenu[0] = 1 ;
					SelectedItem[0] = 0 ;
				}
				if (SelectedItem[0] <= 2)
				{
					OptionStatus[SelectedItem[0]] = !(boolean)OptionStatus[SelectedItem[0]] ;
					if (SelectedItem[0] == 0)
					{
						Utg.PlayMusic(Music[Maps.Music[map]]) ;
					}
				}
				else if (SelectedItem[0] == 3)
				{
					OptionStatus[3] = ((int)OptionStatus[3] + 1) % 2 ;
				}
				else if (SelectedItem[0] == 4)
				{
					OptionStatus[4] = ((int)OptionStatus[4] + 1) % 4 ;
				}
				else if (3 < SelectedItem[0] & SelectedItem[0] <= 3 + Player.ActionKeys.length)
				{
					SelectedMenu[0] = 1 ;
					SelectedItem[0] = 0 ;
				}
			}
		}
		else if (1 <= SelectedMenu[0])
		{
			if (SelectedMenu[0] == 1)
			{
				SelectedItem[0] = Uts.MenuSelection(Player.ActionKeys[0], Player.ActionKeys[2], action, SelectedItem[0], Player.ActionKeys.length - 1) ;
			}
			if (!action.equals(""))
			{
				if (SelectedMenu[0] == 2)
				{
					for (int i = 0 ; i <= Player.ActionKeys.length - 1 ; i += 1)
					{
						if (action.equals(Player.ActionKeys[i]) | action.equals("Enter") | action.equals("MouseLeftClick") | action.equals("Escape"))
						{
							CustomKeyIsActionKey = true ;
						}
					}
					if (!CustomKeyIsActionKey)
					{
						CustomKey = action ;
						Player.ActionKeys[SelectedItem[0]] = CustomKey ;
						SelectedMenu[0] = 1 ;
					}
				}
			}
			if (action.equals("Enter"))
			{
				SelectedMenu[0] = 2 ;
			}
			if (action.equals("Escape"))
			{
				if (SelectedMenu[0] == 1)
				{
					SelectedItem[0] = 0 ;
				}
				SelectedMenu[0] += -1 ;
			}
		}
		//DF.DrawOptionsWindow(SelectedItem[0], SelectedMenu[0], Player.ActionKeys, settings) ;
		return settings ;
	}*/
	/*public void HintsMenu(Point MousePos, Color ClassColor, DrawFunctions DF)
	{
		int NumberOfHints = 15 ;
		Size screen.getSize() = Game.getScreen().getSize() ;
		int[] Pos = new int[] {(int)(0.19 * screen.getSize().x), (int)(0.5 * screen.getSize().y)} ;
		int L = (int)(0.62 * screen.getSize().x), H = (int)(0.2 * screen.getSize().y) ;
		int[] OkButtonPos = new int[] {(int) (Pos[0] + 0.7*L), (int) (Pos[1] - 0.125*H)} ;
		double OkButtonL = (double) (0.05*L), OkButtonH = (double) (0.125*H) ;
		if (action.equals(Player.ActionKeys[1]) | action.equals(Player.ActionKeys[3]))
		{
			SelectedWindow[6] = Uts.MenuSelection(Player.ActionKeys[1], Player.ActionKeys[3], action, SelectedWindow[6], NumberOfHints - 1) ;
		}
		if (action.equals("Enter") | (Utg.isInside(MousePos, new Point((int) (OkButtonPos[0] - OkButtonL/2), (int) (OkButtonPos[1] + OkButtonH/2)), (int) (OkButtonL), (int) (OkButtonH)) & action.equals("MouseLeftClick")))
		{
			//return false ;
		}
		DF.DrawHintsMenu(MousePos, SelectedWindow[6], NumberOfHints, ClassColor) ;
	}*/
	/*public void FabBook(Items[] items, Point MousePos, DrawFunctions DF)
	{
		int[][] Ingredients = Items.CraftingIngredients, Products = Items.CraftingProducts ;
		int NumberOfPages = Ingredients.length - 1 ;
		SelectedWindow[2] = Uts.MenuSelection(Player.ActionKeys[1], Player.ActionKeys[3], action, SelectedWindow[2], NumberOfPages) ;
		DF.DrawFabBook(BookImage, items, SelectedWindow[2], Ingredients, Products, MousePos) ;
	}*/
	/*public void Bestiary(CreatureTypes[] creatureTypes, Items[] items, int[] CreaturesDiscovered, Point MousePos, DrawFunctions DF)
	{
		int windowLimit = 0 ;
		if (CreaturesDiscovered != null)
		{
			windowLimit = Math.max(CreaturesDiscovered.length - 1, 0)/5 ;
		}
		SelectedWindow[8] = Uts.MenuSelection(Player.ActionKeys[1], Player.ActionKeys[3], action, SelectedWindow[8], windowLimit) ;
		DF.DrawBestiary(creatureTypes, CreaturesDiscovered, items, MousePos, SelectedWindow[8]) ;
	}*/
}