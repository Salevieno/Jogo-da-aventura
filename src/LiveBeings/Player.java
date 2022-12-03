package LiveBeings ;

import java.awt.Color ;
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

import javax.sound.sampled.Clip ;
import javax.swing.ImageIcon ;

import org.json.simple.JSONObject;

import java.util.Map;
import Actions.Battle ;
import GameComponents.Icon;
import GameComponents.Items;
import GameComponents.NPCs;
import GameComponents.Quests;
import Graphics.Animations ;
import Graphics.DrawFunctions ;
import Graphics.DrawPrimitives ;
import Items.Alchemy;
import Items.Arrow;
import Items.Fab;
import Items.Food;
import Items.Forge;
import Items.Equip;
import Items.GeneralItem;
import Items.PetItem;
import Items.Potion;
import Items.QuestItem;
import Items.Recipe;
import Screen.Screen;
import Utilities.Scale;
import Utilities.Size;
import Utilities.UtilG;
import Utilities.UtilS;
import Windows.PlayerAttributesWindow;
import Windows.Bag;
import Windows.BestiaryWindow;
import Windows.FabWindow;
import Windows.HintsWindow;
import Windows.MapWindow;
import Windows.QuestWindow;
import Windows.SettingsWindow;
import Main.Game ;
import Maps.Collectible;
import Maps.FieldMap;
import Maps.Maps;

public class Player extends LiveBeing
{
	private String Language ;
	private String Sex ;
	private Color color ;
	private Bag bag ;
	private SettingsWindow settings ;
	private MapWindow map ;
	private ArrayList<Recipe> recipes ;
	private FabWindow fabWindow ;
	private ArrayList<Quests> quest ;	
	private QuestWindow questWindow ;
	private HintsWindow hintsWindow ;
	public BestiaryWindow bestiary ;
	private Equip[] equips ;		// 0: weapon, 1: shield, 2: armor, 3: arrow
	private int attPoints ;			// attribute points available (to upgrade the attributes)
	private int spellPoints ;		// spell points available (to upgrade the spells)
	private float[] collectLevel ;	// 0: herb, 1: wood, 2: metal
	private float[] gold ;			// 0: current, 1: stored, 2: multiplier
	private boolean isRiding ;		// true if the player is riding
	private Map<String, Boolean> questSkills ;	// 0: Forest map, 1: Cave map, 2: Island map, 3: Volcano map, 4: Snowland map, 5: Shovel, 6: Book, 7: Ride, 8: Dragon's aura, 9: Bestiary	// TODO essa est� como map, enquanto as outras est�o como array
	private String[] combo ;		// record of the last 10 movements
    private int collectingCounter ;	// counts the progress of the player's collection
    private int collectingDelay ;	// time that the player takes to collect
	public Creature closestCreature ;	// creature that is currently closest to the player
    public Creature opponent ;		// creature that is currently in battle with the player
	private ArrayList<Integer> CreaturesDiscovered ;	// Creatures that the player has encountered. Will appear in the bestiary
    public Map<String, String[]> allText ;	// All the text in the game in the player language
	public Items[] hotkeyItem ;
    public int difficultLevel ;
    
    public static Image collectingMessage = new ImageIcon(Game.ImagesPath + "CollectingMessage.gif").getImage() ;
	
	// \*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/
	
	
	//private float[] ElemMult ;	// 0: Neutral, 1: Water, 2: Fire, 3: Plant, 4: Earth, 5: Air, 6: Thunder, 7: Light, 8: Dark, 9: Snow
	
	// TODO essas vari�veis v�o para spells
	private boolean[] spellIsActive ;	// Tells if the spell is currently active
	private int[] spellDurationCounter ;	// [0: skill 0, 1: skill 1...]
	private int[] spellCooldownCounter ;	// [0: skill 0, 1: skill 1...]
	private int[] statusCounter ;// [Life, Mp, Phy atk, Phy def, Mag atk, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence, Drunk]
	
	
	// TODO essa pode virar um Map
	private float[] statistics ;	// 0: Number of phy attacks, 1: number of skills used, 2: number of defenses, 3: total phy damage inflicted, 4: total phy damage received, 5: total mag damage inflicted, 6: total mag damage received, 7: total phy damage defended, 8: total mag damage defended, 9: total hits inflicted, 10: total hits received, 11: total dodges, 12: number of crit, 13: total crit damage, 14: total stun, 15: total block, 16: total blood, 17: total blood damage, 18: total blood def, 19: total poison, 20: total poison damage, 21: total poison def, 22: total silence
	
	
	private float[][] attIncrease ;	// Amount of increase in each attribute when the player levels up
	private float[][] chanceIncrease ;	// Chance of increase of these attributes
	public float[][] equipsBonus ;
	
	public static String[] MoveKeys = new String[] {KeyEvent.getKeyText(KeyEvent.VK_UP), KeyEvent.getKeyText(KeyEvent.VK_LEFT), KeyEvent.getKeyText(KeyEvent.VK_DOWN), KeyEvent.getKeyText(KeyEvent.VK_RIGHT)} ;
	public static String[] BattleKeys = new String[] {"A", "D"} ;
	public static String[] ActionKeys = new String[] {"W", "A", "S", "D", "B", "C", "F", "M", "P", "Q", "H", "R", "T", "Z"} ;	// [Up, Left, Down, Right, Bag, Char window, Pet window, Map, Quest, Hint, Tent, Bestiary]
	public static String[] HotKeys = new String[] {"E", "X", "V"} ;	// [Hotkey 1, Hotkey 2, Hotkey 3]
	public static String[] SpellKeys = new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "F10", "F11", "F12"} ;
	public static ArrayList<String[]> Properties = UtilG.ReadcsvFile(Game.CSVPath + "PlayerInitialStats.csv") ;
	public static ArrayList<String[]> EvolutionProperties = UtilG.ReadcsvFile(Game.CSVPath + "PlayerEvolution.csv") ;	
	public static int[] NumberOfSkillsPerJob = new int[] {14 + 20, 15 + 20, 15 + 20, 14 + 20, 14 + 20} ;
	public static int[] CumNumberOfSkillsPerJob = new int[] {0, 34, 69, 104, 138} ;
    public static Image[] AttWindowImages = new Image[] {new ImageIcon(Game.ImagesPath + "PlayerAttWindow1.png").getImage(), new ImageIcon(Game.ImagesPath + "PlayerAttWindow2.png").getImage(), new ImageIcon(Game.ImagesPath + "PlayerAttWindow3.png").getImage()} ;
    public static Color[] ClassColors = new Color[] {Game.ColorPalette[0], Game.ColorPalette[1], Game.ColorPalette[2], Game.ColorPalette[3], Game.ColorPalette[4]} ;
   
    public static Image TentImage = new ImageIcon(Game.ImagesPath + "Tent.png").getImage() ;   
    public static Image DragonAuraImage = new ImageIcon(Game.ImagesPath + "DragonAura.png").getImage() ;
    public static Image RidingImage = new ImageIcon(Game.ImagesPath + "Tiger.png").getImage() ;
    public static Image PterodactileImage = new ImageIcon(Game.ImagesPath + "Pterodactile.png").getImage() ;
    public static Image SpeakingBubbleImage = new ImageIcon(Game.ImagesPath + "SpeakingBubble.png").getImage() ;
	public static Image CoinIcon = new ImageIcon(Game.ImagesPath + "CoinIcon.png").getImage() ;    
	public static Image MagicBlissGif = new ImageIcon(Game.ImagesPath + "MagicBliss.gif").getImage() ;
    public static Image FishingGif = new ImageIcon(Game.ImagesPath + "Fishing.gif").getImage() ;
	
	public Player(String Name, String Language, String Sex, int Job)
	{
		super(1, InitializePersonalAttributes(Name, Job), InitializeBattleAttributes(Job), InitializeMovingAnimations(), new PlayerAttributesWindow()) ;
		this.Language = Language ;
		this.Sex = Sex ;
		InitializeSpells() ;
		allText = UtilG.ReadTextFile(Language) ;

		//Bag = new int[Items.NumberOfAllItems] ;
		questWindow = new QuestWindow() ;
		bag = new Bag(new HashMap<Potion, Integer>(), new HashMap<Alchemy, Integer>(), new ArrayList<Forge>(), new ArrayList<PetItem>(), new ArrayList<Food>(),
				new ArrayList<Arrow>(), new ArrayList<Equip>(), new ArrayList<GeneralItem>(), new ArrayList<Fab>(), new ArrayList<QuestItem>()) ;
		if (Job == 2)
		{
			for (int i = 0; i <= 100 - 1; i += 1)
			{
				bag.getArrow().add(Arrow.getAll()[0]) ;	// TODO how to add multiple items at the same time?
			}
		}
		quest = new ArrayList<>() ;
		recipes = new ArrayList<>() ;
		fabWindow = new FabWindow() ;
		map = new MapWindow() ;
		hintsWindow = new HintsWindow() ;
		bestiary = new BestiaryWindow() ;
		equips = new Equip[4] ;	// 0: weapon, 1: shield, 2: armor, 3: arrow
		spellPoints = 0 ;
		color = Game.ColorPalette[12] ;

    	
		//ElemMult = new float[10] ;
		//Arrays.fill(ElemMult, 1) ;
		collectLevel = new float[3] ;
		gold = new float[] {0, 0, Float.parseFloat(Properties.get(PA.Job)[32])} ;
		questSkills = new HashMap<String, Boolean>() ;
		questSkills.put("Floresta", false) ;	// TODO parte dos nomes em port, parte em ingl�s
		questSkills.put("Caverna", false) ;
		questSkills.put("Ilha", false) ;
		questSkills.put("Vulc�o", false) ;
		questSkills.put("Terra das neves", false) ;
		questSkills.put("Shovel", false) ;
		questSkills.put("Fab window", false) ;
		questSkills.put("Ride", false) ;
		questSkills.put("Dragon's aura", false) ;
		questSkills.put("Bestiary", false) ;
		isRiding = false ;
		if (spell != null)
		{
			spellIsActive = new boolean[spell.length] ;
			spellDurationCounter = new int[spell.length] ;
			spellCooldownCounter = new int[spell.length] ;
		}
		statusCounter = new int[8] ;
		statistics = new float[23] ;
		combo = null ;
		collectingCounter = 0 ;
		collectingDelay = 300 ;
		Arrays.fill(BA.getSpecialStatus(), -1) ;
		attPoints = 0 ;
		attIncrease = new float[3][8] ;
		chanceIncrease = new float[3][8] ;
		for (int i = 0 ; i <= 3 - 1 ; ++i)	// PA.Job, PA.ProJob 1, PA.ProJob 2
		{
			for (int j = 0 ; j <= 7 ; ++j)
			{
				attIncrease[i][j] = Float.parseFloat(EvolutionProperties.get(3*PA.Job + i)[j + 2]) ;
				chanceIncrease[i][j] = Float.parseFloat(EvolutionProperties.get(3*PA.Job + i)[j + 10]) ;
			}
		}
		CreaturesDiscovered = new ArrayList<Integer>() ;
		RidingImage = new ImageIcon(Game.ImagesPath + "Tiger.png").getImage() ;
		closestCreature = null ;
	    opponent = null ;
	    difficultLevel = 1 ;
		equipsBonus = Items.EquipsBonus ;
		Image windowSettings = new ImageIcon(Game.ImagesPath + "windowSettings.png").getImage() ;
		settings = new SettingsWindow(windowSettings, true, true, false, 1, 1) ;
		hotkeyItem = new Items[3] ;
		
	}
	

	private static PersonalAttributes InitializePersonalAttributes(String Name, int Job)
	{
    	int Level = 1 ;
    	Maps map = null ;
    	if (Game.getMaps() != null)
    	{
    		map = Game.getMaps()[Job] ;
    	}
    	int ProJob = 0 ;
		Point Pos = new Point(0, 0) ;
		String dir = Player.MoveKeys[0] ;
		String Thought = "Exist" ;
	    Image PlayerBack = new ImageIcon(Game.ImagesPath + "PlayerBack.png").getImage() ;
		Size Size = new Size (PlayerBack.getWidth(null), PlayerBack.getHeight(null)) ;
		float[] Life = new float[] {Float.parseFloat(Properties.get(Job)[2]), Float.parseFloat(Properties.get(Job)[2])} ;
		float[] Mp = new float[] {Float.parseFloat(Properties.get(Job)[3]), Float.parseFloat(Properties.get(Job)[3])} ;
		float Range = Float.parseFloat(Properties.get(Job)[4]) ;
		int Step = Integer.parseInt(Properties.get(Job)[33]) ;
		int[] Exp = new int[] {0, 5, Integer.parseInt(Properties.get(Job)[34])} ;
		int[] Satiation = new int[] {100, 100, Integer.parseInt(Properties.get(Job)[35])} ;
		int[] Thirst = new int[] {100, 100, Integer.parseInt(Properties.get(Job)[36])} ;
		String[] Elem = new String[] {"n", "n", "n", "n", "n"} ;
		int[][] Actions = new int[][] {{0, Integer.parseInt(Properties.get(Job)[37]), 0}, {0, Integer.parseInt(Properties.get(Job)[38]), 0}, {0, Integer.parseInt(Properties.get(Job)[39]), 0}, {0, Integer.parseInt(Properties.get(Job)[40]), 0}} ;
		String currentAction = "existing" ;
		int countmove = 0 ;
		return new PersonalAttributes(Name, Level, Job, ProJob, map, Pos, dir, Thought, Size, Life, Mp, Range, Step, Exp, Satiation, Thirst, Elem, Actions, currentAction, countmove) ;
	}
	
	private static BattleAttributes InitializeBattleAttributes(int Job)
	{

		float[] PhyAtk = new float[] {Float.parseFloat(Properties.get(Job)[5]), 0, 0} ;
		float[] MagAtk = new float[] {Float.parseFloat(Properties.get(Job)[6]), 0, 0} ;
		float[] PhyDef = new float[] {Float.parseFloat(Properties.get(Job)[7]), 0, 0} ;
		float[] MagDef = new float[] {Float.parseFloat(Properties.get(Job)[8]), 0, 0} ;
		float[] Dex = new float[] {Float.parseFloat(Properties.get(Job)[9]), 0, 0} ;	
		float[] Agi = new float[] {Float.parseFloat(Properties.get(Job)[10]), 0, 0} ;
		float[] Crit = new float[] {Float.parseFloat(Properties.get(Job)[11]), 0, Float.parseFloat(Properties.get(Job)[12]), 0} ;
		float[] Stun = new float[] {Float.parseFloat(Properties.get(Job)[13]), 0, Float.parseFloat(Properties.get(Job)[14]), 0, Float.parseFloat(Properties.get(Job)[15])} ;
		float[] Block = new float[] {Float.parseFloat(Properties.get(Job)[16]), 0, Float.parseFloat(Properties.get(Job)[17]), 0, Float.parseFloat(Properties.get(Job)[18])} ;
		float[] Blood = new float[] {Float.parseFloat(Properties.get(Job)[19]), 0, Float.parseFloat(Properties.get(Job)[20]), 0, Float.parseFloat(Properties.get(Job)[21]), 0, Float.parseFloat(Properties.get(Job)[22]), 0, Float.parseFloat(Properties.get(Job)[23])} ;
		float[] Poison = new float[] {Float.parseFloat(Properties.get(Job)[24]), 0, Float.parseFloat(Properties.get(Job)[25]), 0, Float.parseFloat(Properties.get(Job)[26]), 0, Float.parseFloat(Properties.get(Job)[27]), 0, Float.parseFloat(Properties.get(Job)[28])} ;
		float[] Silence = new float[] {Float.parseFloat(Properties.get(Job)[29]), 0, Float.parseFloat(Properties.get(Job)[30]), 0, Float.parseFloat(Properties.get(Job)[31])} ;
		int[] Status = new int[9] ;
		int[] SpecialStatus = new int[5] ;
		int[][] BattleActions = new int[][] {{0, Integer.parseInt(Properties.get(Job)[41]), 0}} ;
		return new BattleAttributes(PhyAtk, MagAtk, PhyDef, MagDef, Dex, Agi, Crit, Stun, Block, Blood, Poison, Silence, Status, SpecialStatus, BattleActions) ;
	}
	
	private static MovingAnimations InitializeMovingAnimations()
	{
	    Image idleGif = new ImageIcon(Game.ImagesPath + "PlayerBack.png").getImage() ;
	    Image movingUpGif = new ImageIcon(Game.ImagesPath + "PlayerBack.png").getImage() ;
		Image movingDownGif = new ImageIcon(Game.ImagesPath + "PlayerFront.png").getImage() ;
		Image movingLeftGif = new ImageIcon(Game.ImagesPath + "PlayerLeft.png").getImage() ;
		Image movingRightGif = new ImageIcon(Game.ImagesPath + "PlayerRight.png").getImage() ;
		
		return new MovingAnimations(idleGif, movingUpGif, movingDownGif, movingLeftGif, movingRightGif) ;
	}
	
	private void InitializeSpells()
    {
		spell = new Spells[NumberOfSkillsPerJob[PA.Job]] ;
		
    	//int NumberOfAllSkills = 178 ;
    	int NumberOfSkills = Player.NumberOfSkillsPerJob[PA.Job] ;
    	int NumberOfAtt = 14 ;
    	int NumberOfBuffs = 12 ;
    	ArrayList<String[]> spellsInput = UtilG.ReadcsvFile(Game.CSVPath + "Spells.csv") ;	
    	ArrayList<String[]> spellsBuffsInput = UtilG.ReadcsvFile(Game.CSVPath + "SpellsBuffs.csv") ;
    	ArrayList<String[]> spellsNerfsInput = UtilG.ReadcsvFile(Game.CSVPath + "SpellsNerfs.csv") ;
		float[][][] spellBuffs = new float[NumberOfSkills][NumberOfAtt][NumberOfBuffs] ;	// [Life, MP, PhyAtk, MagAtk, PhyDef, MagDef, Dex, Agi, Crit, Stun, Block, Blood, Poison, Silence][atk chance %, atk chance, chance, def chance %, def chance, chance, atk %, atk, chance, def %, def, chance]		
		float[][][] spellNerfs = new float[NumberOfSkills][NumberOfAtt][NumberOfBuffs] ;	// [Life, MP, PhyAtk, MagAtk, PhyDef, MagDef, Dex, Agi, Crit, Stun, Block, Blood, Poison, Silence][atk chance %, atk chance, chance, def chance %, def chance, chance, atk %, atk, chance, def %, def, chance]		
		String[][] spellsInfo = new String[NumberOfSkills][2] ;
		for (int i = 0 ; i <= NumberOfSkills - 1 ; i += 1)
		{
			int ID = i + Player.CumNumberOfSkillsPerJob[PA.Job] ;
			int BuffCont = 0, NerfCont = 0 ;
			for (int j = 0 ; j <= NumberOfAtt - 1 ; j += 1)
			{
				if (j == 11 | j == 12)
				{
					for (int k = 0 ; k <= NumberOfBuffs - 1 ; k += 1)
					{
						spellBuffs[i][j][k] = Float.parseFloat(spellsBuffsInput.get(ID)[BuffCont + 3]) ;
						spellNerfs[i][j][k] = Float.parseFloat(spellsNerfsInput.get(ID)[NerfCont + 3]) ;
						NerfCont += 1 ;
						BuffCont += 1 ;
					}
				}
				else
				{
					spellBuffs[i][j][0] = Float.parseFloat(spellsBuffsInput.get(ID)[BuffCont + 3]) ;
					spellBuffs[i][j][1] = Float.parseFloat(spellsBuffsInput.get(ID)[BuffCont + 4]) ;
					spellBuffs[i][j][2] = Float.parseFloat(spellsBuffsInput.get(ID)[BuffCont + 5]) ;
					spellNerfs[i][j][0] = Float.parseFloat(spellsNerfsInput.get(ID)[NerfCont + 3]) ;
					spellNerfs[i][j][1] = Float.parseFloat(spellsNerfsInput.get(ID)[NerfCont + 4]) ;
					spellNerfs[i][j][2] = Float.parseFloat(spellsNerfsInput.get(ID)[NerfCont + 5]) ;
					NerfCont += 3 ;
					BuffCont += 3 ;
				}
			}
			if (Language.equals("P"))
			{
				spellsInfo[i] = new String[] {spellsInput.get(ID)[42], spellsInput.get(ID)[43]} ;
			}
			else if (Language.equals("E"))
			{
				spellsInfo[i] = new String[] {spellsInput.get(ID)[44], spellsInput.get(ID)[45]} ;
			}
			String Name = spellsInput.get(ID)[4] ;
			int MaxLevel = Integer.parseInt(spellsInput.get(ID)[5]) ;
			float MpCost = Float.parseFloat(spellsInput.get(ID)[6]) ;
			String Type = spellsInput.get(ID)[7] ;
			int[][] PreRequisites = new int[][] {{Integer.parseInt(spellsInput.get(ID)[8]), Integer.parseInt(spellsInput.get(ID)[9])}, {Integer.parseInt(spellsInput.get(ID)[10]), Integer.parseInt(spellsInput.get(ID)[11])}, {Integer.parseInt(spellsInput.get(ID)[12]), Integer.parseInt(spellsInput.get(ID)[13])}} ;
			int Cooldown = Integer.parseInt(spellsInput.get(ID)[14]) ;
			int Duration = Integer.parseInt(spellsInput.get(ID)[15]) ;
			float[] Atk = new float[] {Float.parseFloat(spellsInput.get(ID)[16]), Float.parseFloat(spellsInput.get(ID)[17])} ;
			float[] Def = new float[] {Float.parseFloat(spellsInput.get(ID)[18]), Float.parseFloat(spellsInput.get(ID)[19])} ;
			float[] Dex = new float[] {Float.parseFloat(spellsInput.get(ID)[20]), Float.parseFloat(spellsInput.get(ID)[21])} ;
			float[] Agi = new float[] {Float.parseFloat(spellsInput.get(ID)[22]), Float.parseFloat(spellsInput.get(ID)[23])} ;
			float[] AtkCrit = new float[] {Float.parseFloat(spellsInput.get(ID)[24])} ;
			float[] DefCrit = new float[] {Float.parseFloat(spellsInput.get(ID)[25])} ;
			float[] Stun = new float[] {Float.parseFloat(spellsInput.get(ID)[26]), Float.parseFloat(spellsInput.get(ID)[27]), Float.parseFloat(spellsInput.get(ID)[28])} ;
			float[] Block = new float[] {Float.parseFloat(spellsInput.get(ID)[29]), Float.parseFloat(spellsInput.get(ID)[30]), Float.parseFloat(spellsInput.get(ID)[31])} ;
			float[] Blood = new float[] {Float.parseFloat(spellsInput.get(ID)[32]), Float.parseFloat(spellsInput.get(ID)[33]), Float.parseFloat(spellsInput.get(ID)[34])} ;
			float[] Poison = new float[] {Float.parseFloat(spellsInput.get(ID)[35]), Float.parseFloat(spellsInput.get(ID)[36]), Float.parseFloat(spellsInput.get(ID)[37])} ;
			float[] Silence = new float[] {Float.parseFloat(spellsInput.get(ID)[38]), Float.parseFloat(spellsInput.get(ID)[39]), Float.parseFloat(spellsInput.get(ID)[40])} ;
			String Elem = spellsInput.get(ID)[41] ;
			spell[i] = new Spells(Name, MaxLevel, MpCost, Type, PreRequisites, Cooldown, Duration, spellBuffs[i], spellNerfs[i], Atk, Def, Dex, Agi, AtkCrit, DefCrit, Stun, Block, Blood, Poison, Silence, Elem, spellsInfo[i]) ;	
		}
		spell[0].incLevel(1) ;
    }
	
	public String getLanguage() {return Language ;}
	public String getName() {return PA.getName() ;}
	public String getSex() {return Sex ;}
	public String getDir() {return PA.getDir() ;}
	public Size getSize() {return PA.getSize() ;}
	public Color getColor() {return color ;}
	public int getJob() {return PA.getJob() ;}
	public int getProJob() {return PA.getProJob() ;}	
	public int getContinent() {return PA.getContinent() ;}
	public Maps getMap() {return PA.getMap() ;}
	public Point getPos() {return PA.getPos() ;}
	public Spells[] getSpell() {return spell ;}
	public ArrayList<Quests> getQuest() {return quest ;}
	public Bag getBag() {return bag ;}
	public Equip[] getEquips() {return equips ;}
	public int getSkillPoints() {return spellPoints ;}
	public float[] getLife() {return PA.getLife() ;}
	public float[] getMp() {return PA.getMp() ;}
	public float getRange() {return PA.getRange() ;}
	public float[] getPhyAtk() {return BA.getPhyAtk() ;}
	public float[] getMagAtk() {return BA.getMagAtk() ;}
	public float[] getPhyDef() {return BA.getPhyDef() ;}
	public float[] getMagDef() {return BA.getMagDef() ;}
	public float[] getDex() {return BA.getDex() ;}
	public float[] getAgi() {return BA.getAgi() ;}
	public float[] getCrit() {return BA.getCrit() ;}
	public float[] getStun() {return BA.getStun() ;}
	public float[] getBlock() {return BA.getBlock() ;}
	public float[] getBlood() {return BA.getBlood() ;}
	public float[] getPoison() {return BA.getPoison() ;}
	public float[] getSilence() {return BA.getSilence() ;}
	public String[] getElem() {return PA.Elem ;}
	//public float[] getElemMult() {return ElemMult ;}
	public float[] getCollect() {return collectLevel ;}
	public int getLevel() {return PA.getLevel() ;}
	public float[] getGold() {return gold ;}
	public int getStep() {return PA.getStep() ;}
	public int[] getExp() {return PA.getExp() ;}
	public int[] getSatiation() {return PA.getSatiation() ;}
	public int[] getThirst() {return PA.getThirst() ;}
	public Map<String, Boolean> getQuestSkills() {return questSkills ;}
	public int[][] getActions() {return PA.Actions ;}	
	public int[] getSkillDurationCounter() {return spellDurationCounter ;}
	public int[] getSkillCooldownCounter() {return spellCooldownCounter ;}
	public int[] getStatusCounter() {return statusCounter ;}
	public float[] getStats() {return statistics ;}
	public String[] getCombo() {return combo ;}
	public int getAttPoints() {return attPoints ;}
	public float[][] getAttIncrease() {return attIncrease ;}
	public float[][] getChanceIncrease() {return chanceIncrease ;}
	public ArrayList<Integer> getCreaturesDiscovered() {return CreaturesDiscovered ;}
	public String getAction() {return PA.currentAction ;}
	public float[][] getEquipsBonus() {return equipsBonus ;}
	public SettingsWindow getSettings() {return settings ;}
	public void setName(String newValue) {PA.setName(newValue) ;}
	public void setLevel(int newValue) {PA.setLevel(newValue) ;}
	public void setSize(Size S) {PA.setSize(S) ;}
	public void setProJob(int PJ) {PA.setProJob(PJ) ;}
	public void setMap(Maps M) {PA.setMap(M) ; PA.setContinent(M.getContinent()) ;}
	public void setPos(Point P) {PA.setPos(P) ;}
	public void setBag(Bag b) {bag = b ;}
	public void setStep(int S) {PA.setStep(S) ;}
	public void setCurrentAction(String CA) {PA.currentAction = CA ;}
	public void setCombo(String[] C) {combo = C ;}

	public String[] getAtkElems()
	{
		return new String[] {PA.Elem[0], PA.Elem[1], PA.Elem[4]} ;
	}
	public boolean weaponIsEquipped()
	{
		return (equips[0] != null) ;
	}
	public boolean arrowIsEquipped()
	{
		return (equips[3] != null) ;
	}
	
	public void resetCollectingCounter() { collectingCounter = 0 ;}
	public void incCollectingCounter() { collectingCounter = (collectingCounter + 1 ) % collectingDelay ;}
	public boolean collectingIsOver() { return (collectingCounter == collectingDelay - 1) ;}
	
	public void Collect(Collectible collectible, DrawPrimitives DP, Animations ani)
    {
		incCollectingCounter() ;
        Image collectingGif = new ImageIcon(Game.ImagesPath + "Collecting.gif").getImage() ;
        DP.DrawGif(collectingGif, getPos(), "Center");
        if (collectingIsOver())
        {
    		resetCollectingCounter() ;
            collectingGif.flush() ;
            
            // remove the collectible from the list
            Maps currentMap = PA.getMap() ;
			if (currentMap.isAField())
			{
				FieldMap fm = (FieldMap) getMap() ;
				ArrayList<Collectible> collectibles = fm.getCollectibles() ;
				collectibles.remove(collectible) ;
			}
        }
    }

	
	
	// \*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/
	

	public int GetNumberOfSpells()
	{
		int NumberOfSkills = 0 ;
		int[] Sequence = GetSpellSequence() ;
		for (int i = 0 ; i <= Sequence.length - 1 ; i += 1)
		{
			NumberOfSkills += Sequence[i] ;
		}
		return NumberOfSkills ;
	}
	public int GetNumberOfProSpells()
	{
		int NumberOfProSkills = 0 ;
		int[] ProSequence = GetProSpellSequence() ;
		for (int i = 0 ; i <= ProSequence.length - 1 ; i += 1)
		{
			NumberOfProSkills += ProSequence[i] ;
		}
		return NumberOfProSkills ;
	}
	public ArrayList<Integer> GetActiveSpells()
	{
		ArrayList<Integer> ActiveSkills = new ArrayList<Integer>() ;
		for (int i = 0 ; i <= spell.length - 1 ; i += 1)
		{
			if ((spell[i].getType().equals("Active") | spell[i].getType().equals("Support") | spell[i].getType().equals("Offensive")) & 0 < spell[i].getLevel())
			{
				ActiveSkills.add(i) ;
			}
		}
		return ActiveSkills ;
	}
	public int[] GetSpellSequence()
	{
		// Sequence: [Player job][Number of skills per line]
		int[][] Sequence = new int[][] {{1, 3, 3, 3, 3, 1}, {3, 3, 3, 3, 3}, {3, 3, 3, 3, 3}, {2, 3, 3, 3, 3}, {2, 3, 3, 3, 3}} ;
		return Sequence[PA.Job] ;
	}
	public int[] GetProSpellSequence()
	{
		// Sequence: [Player job][Number of skills per line]
		int[][] Sequence = new int[][] {{1, 2, 2, 2, 2, 1}, {1, 2, 2, 2, 2, 1}, {1, 2, 2, 2, 2, 1}, {1, 2, 2, 2, 2, 1}, {1, 2, 2, 2, 2, 1}, {1, 2, 2, 2, 2, 1}, {1, 2, 2, 2, 2, 1}, {1, 2, 2, 2, 2, 1}, {1, 2, 2, 2, 2, 1}, {1, 2, 2, 2, 2, 1}} ;
		return Sequence[PA.ProJob] ;
	}
	public ArrayList<Integer> GetActiveQuests()
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
	
	
	
	public void addSkillPoint(int amount)
	{
		spellPoints += amount;
	}
	public void incRange(float incR) {PA.setRange(PA.getRange() + incR) ;}
	public void incAttPoints(int amount) {attPoints += amount ;}
	public void decAttPoints(int amount) {attPoints += -amount ;}
	
	public boolean actionIsAMove()
	{
		if (UtilG.ArrayContains(Player.MoveKeys, PA.getCurrentAction()))
		{
			return true ;
		}
		return false ;
	}
	public boolean actionIsASpell()
	{
		if (UtilG.ArrayContains(Player.SpellKeys, PA.getCurrentAction()))
		{
			return true ;
		}
		return false ;
	}
	public boolean actionIsAnAtk()
	{
		if (PA.getCurrentAction().equals(Player.BattleKeys[0]))
		{
			return true ;
		}
		return false ;
	}
	public boolean hasTheSpell(String action)
	{
		if (UtilG.IndexOf(Player.SpellKeys, action) < GetActiveSpells().size())
		{
			return true ;
		}
		return false ;
	}
	public boolean hasEnoughMP(int spellID)
	{
		return (spell[spellID].getMpCost() <= PA.getMp()[0]);
	}
	public int getSpellID(String action)
	{
		return GetActiveSpells().get(UtilG.IndexOf(SpellKeys, action)) ;
	}
	
	public void updateDir(String move)
	{
		if (move.equals(Player.MoveKeys[0]))	// North
		{
			PA.setdir(Player.MoveKeys[0]) ;
		}
		if (move.equals(Player.MoveKeys[1]))	// West
		{
			PA.setdir(Player.MoveKeys[1]) ;
		}
		if (move.equals(Player.MoveKeys[2]))	// South
		{
			PA.setdir(Player.MoveKeys[2]) ;
		}
		if (move.equals(Player.MoveKeys[3]))	// East
		{
			PA.setdir(Player.MoveKeys[3]) ;
		}
	}
	public void move(Pet pet, Clip[] Music, Animations Ani)
	{
		Point NewPos = PA.CalcNewPos() ;
		int moveRange = 20 ;
		if (Game.getScreen().posIsInMap(NewPos))	// if the player's new position is inside the map
		{	
			if (!Ani.isActive(10) & !Ani.isActive(12) & !Ani.isActive(13) & !Ani.isActive(15) & !Ani.isActive(18))
			{
				boolean NewPosIsWalkable = PA.getMap().GroundIsWalkable(NewPos, PA.Elem[4]) ;
				if (NewPosIsWalkable)
				{
					setPos(NewPos) ;
				}	
			}			
		}
		else
		{
			//boolean NewPosIsWalkable = Uts.GroundIsWalkable(maps[PA.getMap()].getType()[NewPos[0]][NewPos[1]], Elem[4]) ; -> this has to be on the new map
			if (true)
			{
				MoveToNewMap(pet, PA.currentAction, Music, settings.getMusicIsOn(), Ani) ;
			}
		}

		PA.countmove = (PA.countmove + 1) % moveRange ;
	}
	
	public void act(Pet pet, Maps[] maps, Point MousePos, Icon[] sideBarIcons, Animations Ani, DrawFunctions DF)
	{
		if (actionIsAMove())
		{
			updateDir(PA.currentAction) ;
		}
		if (PA.currentAction.equals("MouseLeftClick"))
		{
			for (int i = 0 ; i <= sideBarIcons.length - 1 ; i += 1)	// + 2 to account for player and pet
			{
				if (sideBarIcons[i].ishovered(MousePos))
				{
					if (i == 0)
					{
						settings.open() ;
					}	
					if (i == 1)
					{
						bag.open() ;
					}
					if (i == 2)
					{
						questWindow.open() ;
					}
					if (i == 3 & questSkills.get(PA.getMap().getContinentName(this)))
					{
						map.open() ;
					}
					if (i == 4)	// questSkills.get("Fab window")
					{
						fabWindow.open() ;
					}
					if (i == 5)
					{
						attWindow.open() ;
					}
					if (i == 6 & 0 < pet.getLife()[0])
					{
						pet.getAttWindow().open() ;
					}
				}
			}
		}
		if (PA.currentAction.equals(ActionKeys[4]))	// Bag
		{
			bag.open() ;
		}
		if (PA.currentAction.equals(ActionKeys[5]))	// Player window
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
		if (PA.currentAction.equals(ActionKeys[7]) & questSkills.get(PA.getMap().getContinentName(this)))	// Map
		{
			map.open() ;
		}
		if (PA.currentAction.equals(ActionKeys[8]))				// settings window
		{
			settings.open() ;
		}
		if (PA.currentAction.equals(ActionKeys[8]) & pet != null)				// Pet window
		{
			pet.getAttWindow().open() ;
		}
		if (PA.currentAction.equals(ActionKeys[9]))							// Quest window
		{
			quest.add(Game.getAllQuests()[0]) ;
			questWindow.open() ;
		}
		if (PA.currentAction.equals(ActionKeys[10]))							// Hints window
		{			
			hintsWindow.open() ;
		}
		if (PA.currentAction.equals(ActionKeys[11]) & questSkills.get("Ride"))			// Ride
		{
			ActivateRide() ;
		}
		if (PA.currentAction.equals(ActionKeys[12]) & !isInBattle())			// Tent
		{
			Ani.SetAniVars(11, new Object[] {100, getPos(), TentImage}) ;
			Ani.StartAni(11) ;
		}
		if (PA.currentAction.equals(ActionKeys[13]))	// Bestiary questSkills.get("Bestiary")
		{
			bestiary.open() ;
		}
		
		// support skills
		if (UtilS.ActionIsSkill(SpellKeys, PA.currentAction) & (!isInBattle() | canAtk()) & UtilG.IndexOf(SpellKeys, PA.currentAction) < GetActiveSpells().size())
		{
			SupSpell(pet, GetActiveSpells().get(UtilG.IndexOf(SpellKeys, PA.currentAction))) ;
		}
		
		// navigating through open windows
		if (bag.isOpen())
		{
			bag.navigate(PA.currentAction) ;
		}
		if (fabWindow.isOpen())
		{
			fabWindow.navigate(PA.currentAction) ;
		}
		if (settings.isOpen())
		{
			settings.navigate(PA.currentAction) ;
		}
		if (attWindow.isOpen())
		{
			attWindow.navigate(this, PA.currentAction, MousePos) ;
		}
		
		// if meets creature, enters battle
		if (closestCreature != null & (PA.currentAction.equals(ActionKeys[1]) | UtilS.ActionIsSkill(SpellKeys, PA.currentAction)) & !isInBattle())
		{
			if (PA.Job != 2 | (PA.Job == 2 & equips[3] != null))
			{
				opponent = closestCreature ;
				setCurrentAction("Fighting") ;
			}
		}
		
		// Check if there is an animation on, if not, check if there is some ground effect to apply
		if (!Ani.SomeAnimationIsActive())
		{
			receiveAdjacentGroundEffect(PA.getMap()) ;
		}
		
		setCombo(UtilS.RecordCombo(combo, PA.currentAction, 10)) ;
	}
	

	public void meet(Creature[] creatures, ArrayList<NPCs> npc, DrawPrimitives DP, Animations ani)
	{
		float distx, disty ;
		Maps currentMap = PA.getMap() ;
		if (currentMap.isAField())
		{
			FieldMap fm = (FieldMap) getMap() ;
			
			/* Meeting with collectibles */
			ArrayList<Collectible> collectibles = fm.getCollectibles() ;
			//System.out.println(collectibles) ;
			for (int c = 0 ; c <= collectibles.size() - 1 ; c += 1)
			{
				distx = (float) Math.abs(PA.getPos().x - collectibles.get(c).getPos().x) ;
				disty = (float) Math.abs(PA.getPos().y - collectibles.get(c).getPos().y) ;
				if (distx <= 0.5*PA.getSize().x & disty <= 0.5*PA.getSize().y)
				{
					/*if (!ani.isActive(10))
					{
						// start animation
						ani.SetAniVars(10, new Object[] {100, PA.getPos(), 10, collectibles.get(c).getType(), "Coletando"}) ;
						ani.StartAni(10) ;
					}*/
					setCurrentAction("Collecting") ;
					Collect(collectibles.get(c), DP, ani) ;
					//return new int[] {2, collectibles.get(c).getType()} ;
				}
			}

			/* Meeting with chests */
			String groundType = currentMap.groundTypeAtPoint(PA.getPos()) ;
			if (groundType != null)	// TODO are we going to use groundType?
			{
				if (groundType.contains("Chest"))
				{
					//return new int[] {3, Integer.parseInt(groundType.substring(groundType.length() - 1))} ;
				}
			}
			
			/* Meeting with creatures */
			if (isInBattle())
			{
				distx = Math.abs(PA.getPos().x - opponent.getPos().x) ;
				disty = Math.abs(PA.getPos().y - PA.getSize().y / 2 - opponent.getPos().y) ;
				if (distx <= (PA.getSize().x + opponent.getSize().x) / 2 & disty <= (PA.getSize().y + opponent.getSize().y) / 2 & !ani.isActive(10) & !ani.isActive(19))
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
					distx = UtilG.dist1D(PA.getPos().x, creature.getPos().x) ;
					disty = UtilG.dist1D(PA.getPos().y - PA.getSize().y / 2, creature.getPos().y) ;
					if (distx <= (PA.getSize().x + creature.getSize().x) / 2 & disty <= (PA.getSize().y + creature.getSize().y) / 2 & !ani.isActive(10) & !ani.isActive(19))
					{
						opponent = creaturesInMap.get(i) ;
						opponent.setFollow(true) ;
						getPA().setThought("Fighting") ;
						bestiary.addDiscoveredCreature(opponent) ;
					}
				}
			}
		}	
		
		/* Meeting with NPCs */
		if (currentMap.getNPCs() != null)	// Map has NPCs
		{
			for (int i = 0 ; i <= currentMap.getNPCs().size() - 1 ; i += 1)
			{
				NPCs NPC = currentMap.getNPCs().get(i) ;
				if (-1 < NPC.getID())
				{
					distx = (float) Math.abs(PA.getPos().x - NPC.getPos().x) ;
					disty = (float) Math.abs(PA.getPos().y - NPC.getPos().y) ;
					if (distx <= 0.5*PA.getSize().x & disty <= 0.5*PA.getSize().y)
					{
						//return new int[] {1, NPC.getID()} ;
					}
				}
			}	
		}
		
		//return new int[] {-1, -1} ;
	}
	
	/*public void Collect(int Coltype, Maps[] maps, DrawFunctions DF, Animations Ani)
	{
		String CollectMessage = "";
		if (Coltype == 0)	// Collectible type
		{
			//CollectMessage = AllText[TextCat][6] + " " + items[Items.BagIDs[3]].getName() + "!" ;
		} else
		{
			//CollectMessage = AllText[TextCat][6] + " " + items[Coltype + Items.BagIDs[0] - 1].getName() + "!" ;
		}
		Ani.SetAniVars(10, new Object[] {100, PA.getPos(), 10, Coltype, CollectMessage}) ;
		Ani.StartAni(10) ;
		
		PA.getMap().getType()[PA.getPos().x][PA.getPos().y] = "free" ;	// Make the ground where the player is standing free
		
		// The collecting act itself
		int CollectibleID = -1 ;
		float CollectChance = (float) ((getMap().getCollectibleLevel() + 2)*Math.random()) ;
		int MapCollectLevel = getMap().getCollectibleLevel() ;
		float PlayerCollectLevel = -1 ;
		if (Coltype == 0)	// Berry
		{
			//Bag[Items.BagIDs[3] - 1 + MapCollectLevel + Coltype] += 1 ;
			if (PA.Job == 3 & Math.random() <= 0.14*spell[4].getLevel())	// Double collect
			{
				//Bag[Items.BagIDs[3] - 1 + MapCollectLevel + Coltype] += 1 ;	
			}
			CollectibleID = 0 ;
		}
		else if (MapCollectLevel <= PlayerCollectLevel + 1 & PlayerCollectLevel + 1 < CollectChance)
		{					
			//Bag[Items.BagIDs[0] + 3*(MapCollectLevel - 1) + Coltype - 1] += 1 ;
			collectLevel[Coltype - 1] += 0.25/(PlayerCollectLevel + 1) ;
			if (PA.Job == 3 & Math.random() <= 0.14*spell[4].getLevel())	// Double collect
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
		
		//PA.getMap().CreateCollectible(PA.getMap().getid(), CollectibleID) ;	
	}*/
	
	public void receiveAdjacentGroundEffect(Maps map)
	{
		if (UtilS.CheckAdjacentGround(PA.getPos(), map, "Lava").equals("Inside") & !PA.Elem[4].equals("f"))
		{
			PA.incLife(-5) ;
		}
		if (UtilS.isAdjacentTo(PA.getPos(),  map, "Water"))
		{
			PA.incThirst(1) ;
		}
	}
	public void MoveToNewMap(Pet pet, String action, Clip[] Music, boolean MusicIsOn, Animations Ani)
	{
		Screen screen = Game.getScreen() ;
		int[] borders = screen.getBorders() ;
		int nextMap = -1 ;
		int step = PA.getStep() ;
		Point currentPos = new Point(PA.getPos().x, PA.getPos().y), nextPos = new Point(0, 0) ;
		int[] mapCon = PA.getMap().getConnections() ;
		if (action.equals(MoveKeys[0]))	 // Up
		{
			nextPos = new Point(currentPos.x, borders[3] - step) ;
			if (-1 < mapCon[0] & currentPos.x <= screen.getSize().x / 2)
			{
				nextMap = mapCon[0] ;
			}
			else if (-1 < mapCon[7] & screen.getSize().x / 2 < currentPos.x)
			{
				nextMap = mapCon[7] ;
			}			
		}
		if (action.equals(MoveKeys[1]))	 // Left
		{
			nextPos = new Point(borders[2] - step, currentPos.y) ;
			if (-1 < mapCon[1] & currentPos.y <= screen.getSize().y / 2)
			{
				nextMap = mapCon[1] ;
			}
			else if (-1 < mapCon[2] & screen.getSize().y / 2 < currentPos.y)
			{
				nextMap = mapCon[2] ;
			}		
		}
		if (action.equals(MoveKeys[2]))	 // Down
		{
			nextPos = new Point(currentPos.x, borders[1] + step) ;
			if(-1 < mapCon[3] & currentPos.x <= screen.getSize().x / 2)
			{
				nextMap = mapCon[3] ;
			}
			else if(-1 < mapCon[4] & screen.getSize().x / 2 < currentPos.x)
			{
				nextMap = mapCon[4] ;	
			}
		}
		if (action.equals(MoveKeys[3]))	 // Right
		{			
			nextPos = new Point(borders[0] + step, currentPos.y) ;
			if(-1 < mapCon[5] & screen.getSize().y / 2 < currentPos.y)
			{
				nextMap = mapCon[5] ;
			}
			else if(-1 < mapCon[6] & currentPos.y <= screen.getSize().y / 2)
			{
				nextMap = mapCon[6] ;
			}
		}
		if (-1 < nextMap)
		{
			if (Game.getMaps()[nextMap].GroundIsWalkable(nextPos, PA.Elem[4]))
			{
				/*if (MusicIsOn & Maps.MusicID[PA.getMap().getid()] != Maps.MusicID[nextMap])
				{
					UtilG.SwitchMusic(Music[Maps.MusicID[PA.getMap().getid()]], Music[Maps.MusicID[nextMap]]) ;
				}*/
				PA.setMap(Game.getMaps()[nextMap]) ;
				PA.setContinent(PA.getMap().getContinent()) ;
				PA.setPos(nextPos) ;	
				if (0 < pet.getLife()[0])
				{
					pet.getPA().setPos(nextPos) ;
				}
				if (PA.getMap().IsACity())
				{
					PA.currentAction = "existing" ;
				}
			}
			

			if (!isInBattle())
			{
				if (getContinent() == 3)
				{
					Ani.SetAniVars(16, new Object[] {500, PterodactileImage, SpeakingBubbleImage}) ;
					Ani.StartAni(16) ;
				}
			}
		}
	}
	public void SupSpellCounters(Creature[] creature, Creature creatureID)
	{
		for (int s = 0 ; s <= spell.length - 1 ; s += 1)
		{
			if (spellDurationCounter[s] < spell[s].getDuration() & spellIsActive[s])
			{
				spellDurationCounter[s] += 1 ;
			}
			if (spellDurationCounter[s] == spell[s].getDuration())
			{
				ResetSpellDurationCounter(s) ;
				for (int i = 0 ; i <= spell[s].getBuffs().length - 1 ; ++i)
				{
					ApplyBuffsAndNerfs("deactivate", "buffs", i, s, SpellIsActive(s)) ;
				}
				for (int i = 0 ; i <= spell[s].getNerfs().length - 1 ; ++i)
				{
					if (creatureID != null)
					{
						creatureID.ApplyBuffsAndNerfs("deactivate", "nerfs", i, spell[s].getLevel(), spell[s], SpellIsActive(s)) ;
					}
				}
				spellIsActive[s] = false ;
			}
			if (spellCooldownCounter[s] < spell[s].getCooldown())
			{
				spellCooldownCounter[s] += 1 ;
			}
		}
	}
	public void ResetSpellDurationCounter(int spellID)
	{
		spellDurationCounter[spellID] = 0 ;
	}
	public void ResetSpellCooldownCounter(int spellID)
	{
		spellCooldownCounter[spellID] = 0 ;
	}
	public void Tent()
	{
		PA.setLife(new float[] {PA.getLife()[0], PA.getLife()[1]}) ;
	}
	public void ActivateRide()
	{
		if (isRiding)
		{
			setStep(PA.getStep()/2) ;
		}
		else
		{
			setStep(2*PA.getStep()) ;
		}
		isRiding = !isRiding ;
	}
	public void ActivateActionCounters(boolean SomeAnimationIsOn)
	{
		if (PA.Actions[0][0] % PA.Actions[0][1] == 0 & !SomeAnimationIsOn)
		{
			PA.Actions[0][2] = 1 ;							// Player can move
		}
		if (PA.Actions[1][0] % PA.Actions[1][1] == 0)
		{
			PA.incMP((float) 0.02 * PA.getMp()[1]) ;	// Player heals Mp
			PA.Actions[1][0] = 0 ;
		}
		if (PA.Actions[2][0] % PA.Actions[2][1] == 0)
		{
			PA.incSatiation(-1) ;					// decrease satiation
			if (PA.getSatiation()[0] == 0)				// Player is hungry
			{
				PA.incLife(-1) ;
			}
			PA.Actions[2][0] = 0 ;
		}
		if (PA.Actions[3][0] % PA.Actions[3][1] == 0)
		{
			PA.incThirst(-1) ;						// decrease thrist
			if (PA.getThirst()[0] == 0)					// Player is thirsty
			{
				PA.incLife(-1) ;
			}
			PA.Actions[3][0] = 0 ;
		}
	}
	public void ActivateBattleActionCounters()
	{
		if (BA.getBattleActions()[0][0] == BA.getBattleActions()[0][1])
		{
			BA.getBattleActions()[0][2] = 1 ;	// Player can atk
		}
	}
	
	
	
	
	
	
	
	// called every time the window is repainted
	public void ShowWindows(Pet pet, Creature[] creature, CreatureTypes[] creatureTypes, Maps[] maps, Icon[] icon, Battle B, Clip[] Music, Point MousePos, DrawFunctions DF)
	{
		if (bag.isOpen())
		{
			//Bag(pet, creature, screen.getSize(), AllTextCat, AllText, items, B, MousePos, DF) ;
			bag.display(MousePos, allText.get("* Menus da mochila *"), DF) ;
		}
		if (attWindow.isOpen())
		{
			attWindow.display(this, allText, equips, equipsBonus, attPoints, MousePos, PA, BA, DF.getDrawPrimitives());
			//AttWindow(AllText, AllTextCat, icon, screenDim, getAttIncrease()[PA.ProJob], MousePos, CoinIcon, DF.getDrawPrimitives()) ;
		}
		if (fabWindow.isOpen())
		{		
			fabWindow.display(recipes, MousePos, DF) ;
			//FabBook(items, MousePos, DF) ;
		}
		Tent() ;
		if (pet != null)
		{
			if (pet.getAttWindow().isOpen())
			{
				pet.getAttWindow().display(pet, allText, null, null, 0, MousePos, pet.getPA(), pet.getBA(), DF.getDrawPrimitives());
				//DF.DrawPetWindow(pet, new Point((int)(0.1*screenDim[0]), (int)(0.9*screenDim[1]))) ;
			}
		}
		if (map.isOpen())
		{
			map.display(DF.getDrawPrimitives()) ;
			//DF.DrawMap(PA.getMap(), PA.getDir(), maps) ;
		}		
		if (questWindow.isOpen())
		{
			questWindow.display(quest, DF.getDrawPrimitives()) ;
			//QuestWindow(creatureTypes, creature, quest, items, MousePos, DF) ;
		}
		if (bestiary.isOpen())
		{
			bestiary.display(this, MousePos, DF.getDrawPrimitives()) ;
			//Bestiary(creatureTypes, items, getCreaturesDiscovered(), MousePos, DF) ;
		}
		if (settings.isOpen())
		{
			settings.display(allText.get("* Menu de opções *"), DF.getDrawPrimitives()) ;
			//OptionsWindow(Music, DF) ;
			//Object[] OptionStatus = OptionsWindow(Music, DF) ;
			/*MusicIsOn = (boolean) OptionStatus[0] ;
			SoundEffectsAreOn = (boolean) OptionStatus[1] ;
			
			if (!MusicIsOn)
			{
				Utg.StopMusic(Music[Maps.Music[getMap()]]) ;
			}*/
		}
		if (hintsWindow.isOpen())
		{
			hintsWindow.display(DF.getDrawPrimitives()) ;
			//HintsMenu(MousePos, ClassColors[getJob()], DF) ;
		}
	}
	
	
	
	
	
	
	public void TreasureChestReward(int ChestID, Maps[] maps, Animations Ani)
	{
		int[][] ItemRewards = new int[][] {{}, {455 + (Items.BagIDs[6] - Items.BagIDs[5]) / 5 * PA.Job}, {456 + (Items.BagIDs[6] - Items.BagIDs[5]) / 5 * PA.Job}, {457 + (Items.BagIDs[6] - Items.BagIDs[5]) / 5 * PA.Job}, {}, {134, 134, 134, 134, 134, 135, 135, 135, 135, 135}, {1346, 1348, 1349, 1350, 1351}} ;
		int[][] GoldRewards = new int[][] {{2000, 2000, 2000, 2000, 2000}, {}, {}, {}, {4000, 4000, 4000, 4000, 4000}, {}, {}} ;
		if (PA.Job == 2)
		{
			ItemRewards = new int[][] {{}, {456 + (Items.BagIDs[6] - Items.BagIDs[5]) / 5 * PA.Job}, {457 + (Items.BagIDs[6] - Items.BagIDs[5]) / 5 * PA.Job}, {32, 33, 34, 35, 36, 37, 38, 39}, {}, {134, 134, 134, 134, 134, 135, 135, 135, 135, 135}, {1346, 1348, 1349, 1350, 1351}} ;
		}
		
		for (int j = 0 ; j <= ItemRewards.length - 1 ; j += 1)
		{
			//Bag[ItemRewards[ChestID][j]] += 1 ;
		}
		for (int j = 0 ; j <= GoldRewards.length - 1 ; j += 1)
		{
			gold[0] += GoldRewards[ChestID][j] ;
		}
		PA.getMap().getType()[PA.getPos().x][PA.getPos().y] = "free" ;
		//Ani.SetAniVars(18, new Object[] {100, items, ItemRewards, GoldRewards, color[0], CoinIcon}) ;
		//Ani.StartAni(18) ;
	}
	
	
	// battle functions
	public void SpendArrow()
	{
		//if (0 < Equips[3] & 0 < Bag[Equips[3]])
		//{
			//Bag[Equips[3]] += -1 ;
			if (PA.Job == 2 & Math.random() <= 0.1*spell[13].getLevel())
			{
				//Bag[Equips[3]] += 1 ;
			}
		//}
		//if (Bag[Equips[3]] == 0)
		//{
			equips[3] = null ;
		//}
	}
	public int StealItem(Creature creature, Items[] items, int spellLevel)
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
	public void OffensiveSpell(Creature creature, int SpellID)
	{
		PA.getMp()[0] += -spell[SpellID].getMpCost() ;
		ResetSpellCooldownCounter(SpellID) ;
		spellIsActive[SpellID] = true ;
		if (PA.Job == 0)
		{
			
		}
		if (PA.Job == 1)
		{
			
		}
		if (PA.Job == 2)
		{
			if (SpellID == 3)
			{
				
			}
		}
		for (int i = 0 ; i <= spell[SpellID].getBuffs().length - 1 ; ++i)
		{
			ApplyBuffsAndNerfs("activate", "buffs", i, SpellID, SpellIsActive(SpellID)) ;
			creature.ApplyBuffsAndNerfs("activate", "nerfs", i, spell[SpellID].getLevel(), spell[SpellID], SpellIsActive(SpellID)) ;
		}
	}
	public void SupSpell(Pet pet, int SpellID)
	{
		int skillLevel = spell[SpellID].getLevel() ;
		if (spell[SpellID].getMpCost() <= PA.getMp()[0] & 0 < skillLevel)
		{
			getStats()[1] += 1 ;	// Number of mag atks
			ResetBattleActions() ;			
			PA.getMp()[0] += -spell[SpellID].getMpCost() ;
			if (PA.Job == 0)
			{
				
			}
			if (PA.Job == 1)
			{
				if (SpellID == 10)
				{
					PA.getLife()[0] += (float)Math.min((0.04*skillLevel + 0.002*BA.TotalMagAtk()), 0.3)*PA.getLife()[1] ;
					PA.getLife()[0] = (float)Math.min(PA.getLife()[0], PA.getLife()[1]) ;
					if (0 < pet.getLife()[0] & spell[SpellID].getMpCost() <= pet.getMp()[0])
					{
						pet.getMp()[0] += -spell[SpellID].getMpCost() ;
						pet.getLife()[0] += (float)Math.min(Math.min((0.02*skillLevel + 0.002*(pet.getBA().TotalMagAtk())), 0.15)*pet.getLife()[1], pet.getLife()[1]) ;
						pet.getLife()[0] = (float)Math.min(pet.getLife()[0], pet.getLife()[1]) ;
					}
				}
			}
			for (int i = 0 ; i <= spell[SpellID].getBuffs().length - 1 ; ++i)
			{
				ApplyBuffsAndNerfs("activate", "buffs", i, SpellID, SpellIsActive(SpellID)) ;
			}
			spellIsActive[SpellID] = true ;
		}
	}
	public void autoSpells(Creature creature, Spells[] spell)
	{		
		/*if (PA.Job == 3 & PA.getLife()[0] < 0.2 * PA.getLife()[1] & 0 < Skill[12] & !SkillBuffIsActive[12][0])	// Survivor's instinct
		{
			for (int i = 0 ; i <= skill[12].getBuffs().length - 1 ; ++i)
			{
				BuffsAndNerfs(player, pet, creature, skill[12].getBuffs(), Skill[12], i, false, "Player", "activate") ;
			}
			SkillBuffIsActive[12][0] = true ;
		}
		if (PA.Job == 3 & 0.2*Life[1] <= Life[0] & 0 < Skill[12] & SkillBuffIsActive[12][0])	// Survivor's instinct
		{
			for (int i = 0 ; i <= skill[12].getBuffs().length - 1 ; ++i)
			{
				BuffsAndNerfs(player, pet, creature, skill[12].getBuffs(), Skill[12], i, false, "Player", "deactivate") ;
			}
			SkillBuffIsActive[12][0] = false ;
		}*/
	}
	public void ApplyBuffsAndNerfs(String action, String type, int att, int SkillID, boolean SkillIsActive)
	{
		int ActionMult = 1 ;
		float[][] Buff = new float[14][5] ;	// [PA.getLife(), PA.getMp(), PhyAtk, MagAtk, Phy def, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence][effect]
		float[] OriginalValue = new float[14] ;	// [PA.getLife(), PA.getMp(), PhyAtk, MagAtk, Phy def, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence]
		float[][] Buffs = null ;
		int BuffNerfLevel = spell[SkillID].getLevel() ;
		if (type.equals("buffs"))
		{
			Buffs = spell[SkillID].getBuffs() ;
		}
		else if (type.equals("nerfs"))
		{
			Buffs = spell[SkillID].getNerfs() ;
		}
		OriginalValue = new float[] {PA.getLife()[1], PA.getMp()[1], BA.getPhyAtk()[0], BA.getMagAtk()[0], BA.getPhyDef()[0], BA.getMagDef()[0], BA.getDex()[0], BA.getAgi()[0], BA.getCrit()[0], BA.getStun()[0], BA.getBlock()[0], BA.getBlood()[0], BA.getBlood()[2], BA.getBlood()[4], BA.getBlood()[6], BA.getPoison()[0], BA.getPoison()[2], BA.getPoison()[4], BA.getPoison()[6], BA.getSilence()[0]} ;
		if (action.equals("deactivate"))
		{
			ActionMult = -1 ;
		}
		if (att == 11 | att == 12)
		{
			if (action.equals("deactivate"))
			{
				Buff[att][0] += (OriginalValue[att]*Buffs[att][0] + Buffs[att][1])*BuffNerfLevel*ActionMult ;
				Buff[att][1] += (OriginalValue[att]*Buffs[att][3] + Buffs[att][4])*BuffNerfLevel*ActionMult ;
				Buff[att][2] += (OriginalValue[att]*Buffs[att][6] + Buffs[att][7])*BuffNerfLevel*ActionMult ;
				Buff[att][3] += (OriginalValue[att]*Buffs[att][9] + Buffs[att][10])*BuffNerfLevel*ActionMult ;
			}
			else
			{
				if (Math.random() <= Buffs[att][2])
				{
					Buff[att][1] += (OriginalValue[att]*Buffs[att][0] + Buffs[att][1])*BuffNerfLevel*ActionMult ;
				}
				if (Math.random() <= Buffs[att][5])
				{
					Buff[att][1] += (OriginalValue[att]*Buffs[att][3] + Buffs[att][4])*BuffNerfLevel*ActionMult ;
				}
				if (Math.random() <= Buffs[att][8])
				{
					Buff[att][2] += (OriginalValue[att]*Buffs[att][6] + Buffs[att][7])*BuffNerfLevel*ActionMult ;
				}
				if (Math.random() <= Buffs[att][11])
				{
					Buff[att][3] += (OriginalValue[att]*Buffs[att][9] + Buffs[att][10])*BuffNerfLevel*ActionMult ;
				}
			}
		}
		else if (action.equals("deactivate") | Math.random() <= Buffs[att][2])
		{
			Buff[att][0] += (OriginalValue[att]*Buffs[att][0] + Buffs[att][1])*BuffNerfLevel*ActionMult ;
		}
		if (!SkillIsActive)
		{
			PA.getLife()[0] += Buff[0][0] ;
			PA.getMp()[0] += Buff[1][0] ;
			BA.getPhyAtk()[1] += Buff[2][0] ;
			BA.getMagAtk()[1] += Buff[3][0] ;
			BA.getPhyDef()[1] += Buff[4][0] ;
			BA.getMagDef()[1] += Buff[5][0] ;
			BA.getDex()[1] += Buff[6][0] ;
			BA.getAgi()[1] += Buff[7][0] ;
			BA.getCrit()[1] += Buff[8][0] ;
			BA.getStun()[1] += Buff[9][0] ;
			BA.getBlock()[1] += Buff[10][0] ;
			BA.getBlood()[1] += Buff[11][0] ;
			BA.getBlood()[3] += Buff[11][1] ;
			BA.getBlood()[5] += Buff[11][2] ;
			BA.getBlood()[7] += Buff[11][3] ;
			BA.getBlood()[8] += Buff[11][4] ;
			BA.getPoison()[1] += Buff[12][0] ;
			BA.getPoison()[3] += Buff[12][1] ;
			BA.getPoison()[5] += Buff[12][2] ;
			BA.getPoison()[7] += Buff[12][3] ;
			BA.getPoison()[8] += Buff[12][4] ;
			BA.getSilence()[1] += Buff[13][0] ;
		}	
	}
	public void TakeBloodAndPoisonDamage(float TotalBloodAtk, float TotalPoisonAtk)
	{
		float BloodDamage = 0 ;
		float PoisonDamage = 0 ;
		float BloodMult = 1, PoisonMult = 1 ;
		if (PA.Job == 4)
		{
			PoisonMult += -0.1*spell[13].getLevel() ;
		}
		if (0 < BA.getSpecialStatus()[2])	// Blood
		{
			BloodDamage = Math.max(TotalBloodAtk*BloodMult - BA.TotalBloodDef(), 0) ;
		}
		if (0 < BA.getSpecialStatus()[3])	// Poison
		{
			PoisonDamage = Math.max(TotalPoisonAtk*PoisonMult - BA.TotalPoisonDef(), 0) ;
		}
		PA.getLife()[0] += -BloodDamage - PoisonDamage ;
		if (0 < BloodDamage)
		{
			statistics[18] += BA.TotalBloodDef() ;
		}
		if (0 < PoisonDamage)
		{
			statistics[21] += BA.TotalPoisonDef() ;
		}
	}
	public void updateoffensiveStats(Object[] playerAtkResult, Creature creature)
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
		int damage = (int) playerAtkResult[0] ;
		String effect = (String) playerAtkResult[1] ;
		if (!PA.currentAction.equals(""))				// player has performed an action
		{
			if (0 <= damage)							// player inflicted damage
			{	
				if (BA.actionisskill())					// player performed a magical atk
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
			if (effect.equals("Hit"))							// player performed a successful hit
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
			if (PA.currentAction.equals(Player.ActionKeys[3]))	// player defended
			{
				statistics[2] += 1 ;								// Number of defenses performed by the player
			}
		}
	}
	public void updatedefensiveStats(int damage, String effect, boolean creaturePhyAtk, Creature creature)
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
		if (effect.equals("Hit"))
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
		gold[0] += creature.getGold()*UtilG.RandomMult((float)0.1)*gold[2] ;
		PA.getExp()[0] += creature.getExp()[0]*PA.getExp()[2] ;
		if (GetActiveQuests() != null)
		{
			for (int q = 0 ; q <= GetActiveQuests().size() - 1 ; q += 1)
			{
				quest[GetActiveQuests().get(q)].IncReqCreaturesCounter(creature.getType().getID()) ;
			}
		}
		Ani.SetAniVars(12, new Object[] {100, ItemsObtained, color}) ;
		Ani.StartAni(12) ;
	}
	public boolean ShouldLevelUP()
	{
		if (getExp()[1] <= getExp()[0])
		{
			return true ;
		}
		return false ;
	}	
	public void LevelUp(Animations ani)
	{
		float[] attributesIncrease = CalcAttIncrease() ;
		PA.setLevel(PA.getLevel() + 1) ;
		spellPoints += 1 ;
		PA.getLife()[1] += attributesIncrease[0] ;
		PA.getLife()[0] = PA.getLife()[1] ;
		PA.getMp()[1] += attributesIncrease[1] ;	
		PA.getMp()[0] = PA.getMp()[1] ;
		BA.getPhyAtk()[0] += attributesIncrease[2] ;
		BA.getMagAtk()[0] += attributesIncrease[3] ;
		BA.getPhyDef()[0] += attributesIncrease[4] ;
		BA.getMagDef()[0] += attributesIncrease[5] ;
		BA.getAgi()[0] += attributesIncrease[6] ;
		BA.getDex()[0] += attributesIncrease[7] ;
		PA.getExp()[1] += attributesIncrease[8] ;
		attPoints += 2 ;
		

		ani.SetAniVars(13, new Object[] {150, attributesIncrease, getLevel(), getColor()}) ;
		ani.StartAni(13) ;
	}
	public float[] CalcAttIncrease()
	{
		// Life, Mp, Phyatk, Magatk, Phydef, Magdef, Dex, Agi, Exp
		float[] attributeIncrease = getAttIncrease()[getProJob()] ;
		float[] chanceIncrease = getChanceIncrease()[getProJob()] ;
		float[] increase = new float[attributeIncrease.length + 1] ;
		for (int i = 0 ; i <= attributeIncrease.length - 1 ; ++i)
		{
			if (Math.random() <= chanceIncrease[i])
			{
				increase[i] = attributeIncrease[i] ;
			}
		}
		increase[attributeIncrease.length] = (float) (10*(3*Math.pow(PA.getLevel() - 1, 2) + 3*(PA.getLevel() - 1) + 1) - 5) ;
		return increase ;
	}
	public void ResetPosition()
	{
		PA.setMap(Game.getMaps()[PA.Job]) ;
		PA.setContinent(0) ;
		if (PA.Job == 0)
		{
			PA.setPos(new Point(340, 340)) ;
		}
		if (PA.Job == 1)
		{
			PA.setPos(new Point(340, 180)) ;
		}
		if (PA.Job == 2)
		{
			PA.setPos(new Point(40, 220)) ;
		}
		if (PA.Job == 3)
		{
			PA.setPos(new Point(340, 340)) ;
		}
		if (PA.Job == 4)
		{
			PA.setPos(new Point(340, 640)) ;
		}
		/*if (MusicIsOn)
		{
			UtilGeral.SwitchMusic(Music[MusicInMap[PA.getMap()]], Music[MusicInMap[PA.Job]]) ;
		}*/
	}
	public void Dies()
	{
		gold[0] = (float)(0.8*gold[0]) ;
		PA.getLife()[0] = PA.getLife()[1] ;
		PA.getMp()[0] = PA.getMp()[1] ;
		PA.getSatiation()[0] = PA.getSatiation()[1] ;
		ResetPosition() ;
	}
	

	
	/* Get current state methods */
	public boolean SpellIsActive(int spellID)
	{
		if (spellDurationCounter[spellID] != 0)
		{
			return true ;
		}
		return false ;
	}
	public boolean SpellIsReady(int spellID)
	{
		if (spellCooldownCounter[spellID] == spell[spellID].getCooldown())
		{
			return true ;
		}
		return false ;
	}
	public boolean isEquippable(int ItemID)
	{
		if ((Items.BagIDs[6] + Items.NumberOfItems[6] / 5 * PA.Job) <= ItemID & ItemID <= Items.BagIDs[6] + Items.NumberOfItems[6] / 5 * (PA.Job + 1))
		{
			return true ;
		}
		return false ;
	}
	public boolean IsRiding()
	{
		return isRiding ;
	}
	public boolean isInBattle()
	{
		if (PA.getThought().equals("Fighting"))
		{
			return true ;
		}
		return false ;
	}
	
	
	/* Action windows */
	public void AttWindow(Icon[] icons, int[] MainWinDim, float[] PlayerAttributeIncrease, Point MousePos, Image GoldCoinImage, DrawPrimitives DP)
	{
		//int WindowLimit = 2 ;
		//SelectedWindow[0] = UtilS.MenuSelection(Player.ActionKeys[0], Player.ActionKeys[2], action, SelectedWindow[0], WindowLimit) ;
		for (int selectedItem = 0 ; selectedItem <= 7 - 1 ; selectedItem += 1)
		{
			//if (icons[i].ishovered(MousePos) & (PA.currentAction.equals("Enter") | PA.currentAction.equals("MouseLeftClick")) & 0 < attPoints)
			//{
				if (selectedItem == 0)
				{
					PA.getLife()[0] += PlayerAttributeIncrease[selectedItem] ;
					PA.getLife()[1] += PlayerAttributeIncrease[selectedItem] ;
				}
				else if (selectedItem == 1)
				{
					PA.getMp()[0] += PlayerAttributeIncrease[selectedItem] ;
					PA.getMp()[1] += PlayerAttributeIncrease[selectedItem] ;
				}
				else if (selectedItem == 2)
				{
					BA.getPhyAtk()[0] += PlayerAttributeIncrease[selectedItem] ;
				}
				else if (selectedItem == 3)
				{
					BA.getMagAtk()[0] += PlayerAttributeIncrease[selectedItem] ;
				}
				else if (selectedItem == 4)
				{
					BA.getPhyDef()[0] += PlayerAttributeIncrease[selectedItem] ;
				}
				else if (selectedItem == 5)
				{
					BA.getMagDef()[0] += PlayerAttributeIncrease[selectedItem] ;
				}
				else if (selectedItem == 6)
				{
					BA.getDex()[0] += PlayerAttributeIncrease[selectedItem] ;
				}
				else if (selectedItem == 7)
				{
					BA.getAgi()[0] += PlayerAttributeIncrease[selectedItem] ;
				}
				attPoints += -1 ;
			//}
		}
		//Point WinPos = new Point((int) (0.3 * MainWinDim[0]), (int) (0.2 * MainWinDim[1])) ;
		//DrawAttWindow(MainWinDim, WinPos, MousePos, AllText, AllTextCat, SelectedWindow[0], GoldCoinImage, icons, DP) ;
		attWindow.display(this, allText, equips, equipsBonus, attPoints, MousePos, PA, BA, DP) ;
	}
	public void ApplyEquipsBonus(int ID, float ActionMult)
	{
		PA.getLife()[1] += equipsBonus[ID][2]*ActionMult ;
		PA.getMp()[1] += equipsBonus[ID][3]*ActionMult ;
		BA.getPhyAtk()[1] += equipsBonus[ID][4]*ActionMult ;
		BA.getMagAtk()[1] += equipsBonus[ID][5]*ActionMult ;
		BA.getPhyDef()[1] += equipsBonus[ID][6]*ActionMult ;
		BA.getMagDef()[1] += equipsBonus[ID][7]*ActionMult ;
		BA.getDex()[1] += equipsBonus[ID][8]*ActionMult ;
		BA.getAgi()[1] += equipsBonus[ID][9]*ActionMult ;
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
	public void Equip(Items[] items, int EquipID)
	{
		int NumberOfEquipTypes = 3 ;	// Sword/Staff/Bow/Claws/Dagger, shield, armor/robe (Archers have bow, bandana, and armor)
		int FirstEquipID = Items.BagIDs[6] ;
		int EquipType = (EquipID + PA.Job) % NumberOfEquipTypes ;
		int currentEquipID = equips[EquipType].getId() ;
		//if (0 < Bag[EquipID])
		//{
			if (0 < currentEquipID)	// Unnequip the current equip
			{
				if (UtilS.SetIsFormed(equips))	// if the set was formed, remove the 20% bonus
				{
					ApplyEquipsBonus(equips[0].getId() - FirstEquipID, (float)-0.2) ;
					ApplyEquipsBonus(equips[1].getId() - FirstEquipID, (float)-0.2) ;
					ApplyEquipsBonus(equips[2].getId() - FirstEquipID, (float)-0.2) ;
				}
				equips[EquipType] = null ;
				PA.Elem[EquipType + 1] = "n" ;
				ApplyEquipsBonus(currentEquipID - FirstEquipID, -1) ;
			}
			if (currentEquipID != EquipID & equips[(EquipID + PA.Job) % NumberOfEquipTypes].getId() == 0)	// Equip
			{
				equips[EquipType] = Equip.getAll()[EquipID] ;
				PA.Elem[EquipType + 1] = Items.EquipsElem[EquipID - FirstEquipID] ;
				ApplyEquipsBonus(EquipID - FirstEquipID, 1) ;
				if (UtilS.SetIsFormed(equips))	// if the set is formed, add the 20% bonus
				{
					ApplyEquipsBonus(equips[0].getId() - FirstEquipID, (float)0.2) ;
					ApplyEquipsBonus(equips[1].getId() - FirstEquipID, (float)0.2) ;
					ApplyEquipsBonus(equips[2].getId() - FirstEquipID, (float)0.2) ;
				}
			}
		//}
		if (PA.Elem[1].equals(PA.Elem[2]) & PA.Elem[2].equals(PA.Elem[3]))	// if the elements of all equips are the same, activate the superelement
		{
			PA.Elem[4] = PA.Elem[1] ;
		}
		else
		{
			PA.Elem[4] = "n" ;
		}
	}
	public void ItemEffect(int ItemID)
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
	
	
	/* Drawing methods */
	public void DrawStats(String[][] AllText, int[] AllTextCat, Point Pos, int L, int H, float[] PlayerStats, DrawPrimitives DP)
	{
		Font font = new Font("SansSerif", Font.BOLD, L / 28) ;
		float OverallAngle = DrawPrimitives.OverallAngle ;
		int TextCat = AllTextCat[7] ;
		Point TextPos = new Point((int) (Pos.x + 5 + 0.05*L), (int) (Pos.y + 0.05*H)) ;
		for (int i = 0 ; i <= PlayerStats.length - 1 ; i += 1)
		{
			DP.DrawText(TextPos, "BotLeft", OverallAngle, AllText[TextCat][i + 1] + " " + String.valueOf(UtilG.Round(PlayerStats[i], 1)), font, Game.ColorPalette[5]) ;
			TextPos.y += 0.95*H/PlayerStats.length ;
		}
	}
	public void DrawSpecialAttributesWindow(String[][] AllText, int[] AllTextCat, Point Pos, int L, int H, DrawPrimitives DP)
	{
		Font font = new Font("SansSerif", Font.BOLD, L / 20) ;
		float OverallAngle = DrawPrimitives.OverallAngle ;
		int SpecialAttrPropCat = AllTextCat[8], AttrCat = AllTextCat[6] ;
		int lineW = 2 ;
		float sx = (float)0.15*L, sy = (float)(1.8*UtilG.TextH(font.getSize())) ;
		Color TextColor = Game.ColorPalette[5], LineColor = Game.ColorPalette[9] ;
		Color[] AttributeColor = new Color[] {Game.ColorPalette[5], Game.ColorPalette[5], Game.ColorPalette[6], Game.ColorPalette[3], Game.ColorPalette[9]} ;
		Pos.y += H ;
		DP.DrawLine(new int[] {Pos.x + (int)(0.025*L), Pos.x + (int)(0.025*L)}, new int[] {(int)(Pos.y - H + 0.5*sy), (int)(Pos.y - H + 8.5*sy)}, lineW, LineColor) ;
		DP.DrawLine(new int[] {Pos.x + (int)(0.375*L), Pos.x + (int)(0.375*L)}, new int[] {(int)(Pos.y - H + 0.5*sy), (int)(Pos.y - H + 3.5*sy)}, lineW, LineColor) ;
		DP.DrawLine(new int[] {Pos.x + (int)(0.975*L), Pos.x + (int)(0.975*L)}, new int[] {(int)(Pos.y - H + 0.5*sy), (int)(Pos.y - H + 8.5*sy)}, lineW, LineColor) ;
		DP.DrawLine(new int[] {Pos.x + (int)(0.025*L), Pos.x + (int)(0.975*L)}, new int[] {(int)(Pos.y - H + 0.5*sy), (int)(Pos.y - H + 0.5*sy)}, lineW, LineColor) ;
		DP.DrawLine(new int[] {Pos.x + (int)(0.025*L), Pos.x + (int)(0.975*L)}, new int[] {(int)(Pos.y - H + 8.5*sy), (int)(Pos.y - H + 8.5*sy)}, lineW, LineColor) ;
		DP.DrawLine(new int[] {Pos.x + (int)(0.025*L), Pos.x + (int)(0.975*L)}, new int[] {(int)(Pos.y - H + 1.5*sy), (int)(Pos.y - H + 1.5*sy)}, lineW, LineColor) ;
		DP.DrawLine(new int[] {Pos.x + (int)(0.025*L), Pos.x + (int)(0.975*L)}, new int[] {(int)(Pos.y - H + 2.5*sy), (int)(Pos.y - H + 2.5*sy)}, lineW, LineColor) ;
		DP.DrawLine(new int[] {Pos.x + (int)(0.375*L + 2*sx), Pos.x + (int)(0.375*L + 2*sx)}, new int[] {(int)(Pos.y - H + 1.5*sy), (int)(Pos.y - H + 2.5*sy)}, lineW, LineColor) ;
		DP.DrawText(new Point(Pos.x + (int)(0.65*L), (int)(Pos.y - H + sy)), "Center", OverallAngle, AllText[SpecialAttrPropCat][1], font, TextColor) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.375*L + sx), (int)(Pos.y - H + 2*sy)), "Center", OverallAngle, AllText[SpecialAttrPropCat][2], font, TextColor) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.375*L + 3*sx), (int)(Pos.y - H + 2*sy)), "Center", OverallAngle, AllText[SpecialAttrPropCat][3], font, TextColor) ;
		DP.DrawText(new Point(Pos.x + (int)(0.45*L), (int)(Pos.y - H + 3*sy)), "Center", OverallAngle, AllText[SpecialAttrPropCat][4], font, TextColor) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.45*L + sx), (int)(Pos.y - H + 3*sy)), "Center", OverallAngle, AllText[SpecialAttrPropCat][5], font, TextColor) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.45*L + 2*sx), (int)(Pos.y - H + 3*sy)), "Center", OverallAngle, AllText[SpecialAttrPropCat][4], font, TextColor) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.45*L + 3*sx), (int)(Pos.y - H + 3*sy)), "Center", OverallAngle, AllText[SpecialAttrPropCat][5], font, TextColor) ;	
		for (int i = 0 ; i <= 4 ; ++i)
		{
			DP.DrawLine(new int[] {Pos.x + (int)(0.025*L), Pos.x + (int)(0.975*L)}, new int[] {(int)(Pos.y - H + (i + 3.5)*sy), (int)(Pos.y - H + (i + 3.5)*sy)}, lineW, LineColor) ;
			DP.DrawText(new Point(Pos.x + (int)(0.2*L), (int)(Pos.y - H + (i + 4)*sy)), "Center", OverallAngle, AllText[AttrCat][i + 11], font, AttributeColor[i]) ;	
		}
		for (int i = 0 ; i <= 3 ; ++i)
		{
			DP.DrawLine(new int[] {Pos.x + (int)(0.375*L + i*sx), Pos.x + (int)(0.375*L + i*sx)}, new int[] {(int)(Pos.y - H + 2*sy + sy/2), (int)(Pos.y - H + 8*sy + sy/2)}, lineW, LineColor) ;
			DP.DrawText(new Point(Pos.x + (int)(0.45*L + i*sx), (int)(Pos.y - H + 4*sy)), "Center", OverallAngle, String.valueOf(UtilG.Round(BA.getStun()[i], 2)), font, AttributeColor[0]) ;	
			DP.DrawText(new Point(Pos.x + (int)(0.45*L + i*sx), (int)(Pos.y - H + 5*sy)), "Center", OverallAngle, String.valueOf(UtilG.Round(BA.getBlock()[i], 2)), font, AttributeColor[1]) ;	
			DP.DrawText(new Point(Pos.x + (int)(0.45*L + i*sx), (int)(Pos.y - H + 6*sy)), "Center", OverallAngle, String.valueOf(UtilG.Round(BA.getBlood()[i], 2)), font, AttributeColor[2]) ;	
			DP.DrawText(new Point(Pos.x + (int)(0.45*L + i*sx), (int)(Pos.y - H + 7*sy)), "Center", OverallAngle, String.valueOf(UtilG.Round(BA.getPoison()[i], 2)), font, AttributeColor[3]) ;	
			DP.DrawText(new Point(Pos.x + (int)(0.45*L + i*sx), (int)(Pos.y - H + 8*sy)), "Center", OverallAngle, String.valueOf(UtilG.Round(BA.getSilence()[i], 2)), font, AttributeColor[4]) ;				
		}
		
		DP.DrawLine(new int[] {Pos.x + (int)(0.025*L), Pos.x + (int)(0.025*L)}, new int[] {(int)(Pos.y - H + 9.5*sy), (int)(Pos.y - H + 14.5*sy)}, lineW, LineColor) ;
		DP.DrawLine(new int[] {Pos.x + (int)(0.375*L), Pos.x + (int)(0.375*L)}, new int[] {(int)(Pos.y - H + 9.5*sy), (int)(Pos.y - H + 12.5*sy)}, lineW, LineColor) ;
		DP.DrawLine(new int[] {Pos.x + (int)(0.975*L), Pos.x + (int)(0.975*L)}, new int[] {(int)(Pos.y - H + 9.5*sy), (int)(Pos.y - H + 14.5*sy)}, lineW, LineColor) ;
		DP.DrawLine(new int[] {Pos.x + (int)(0.025*L), Pos.x + (int)(0.975*L)}, new int[] {(int)(Pos.y - H + 9.5*sy), (int)(Pos.y - H + 9.5*sy)}, lineW, LineColor) ;
		DP.DrawLine(new int[] {Pos.x + (int)(0.025*L), Pos.x + (int)(0.975*L)}, new int[] {(int)(Pos.y - H + 14.5*sy), (int)(Pos.y - H + 14.5*sy)}, lineW, LineColor) ;
		DP.DrawLine(new int[] {Pos.x + (int)(0.025*L), Pos.x + (int)(0.975*L)}, new int[] {(int)(Pos.y - H + 10.5*sy), (int)(Pos.y - H + 10.5*sy)}, lineW, LineColor) ;
		DP.DrawLine(new int[] {Pos.x + (int)(0.025*L), Pos.x + (int)(0.975*L)}, new int[] {(int)(Pos.y - H + 11.5*sy), (int)(Pos.y - H + 11.5*sy)}, lineW, LineColor) ;
		DP.DrawLine(new int[] {Pos.x + (int)(0.375*L + 2*sx), Pos.x + (int)(0.375*L + 2*sx)}, new int[] {(int)(Pos.y - H + 10.5*sy), (int)(Pos.y - H + 12.5*sy)}, lineW, LineColor) ;
		DP.DrawText(new Point(Pos.x + (int)(0.65*L), (int)(Pos.y - H + 10*sy)), "Center", OverallAngle, AllText[SpecialAttrPropCat][6], font, TextColor) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.375*L + sx), (int)(Pos.y - H + 11*sy)), "Center", OverallAngle, AllText[SpecialAttrPropCat][2], font, TextColor) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.375*L + 3*sx), (int)(Pos.y - H + 11*sy)), "Center", OverallAngle, AllText[SpecialAttrPropCat][3], font, TextColor) ;
		DP.DrawText(new Point(Pos.x + (int)(0.45*L), (int)(Pos.y - H + 12*sy)), "Center", OverallAngle, AllText[SpecialAttrPropCat][4], font, TextColor) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.45*L + sx), (int)(Pos.y - H + 12*sy)), "Center", OverallAngle, AllText[SpecialAttrPropCat][5], font, TextColor) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.45*L + 2*sx), (int)(Pos.y - H + 12*sy)), "Center", OverallAngle, AllText[SpecialAttrPropCat][4], font, TextColor) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.45*L + 3*sx), (int)(Pos.y - H + 12*sy)), "Center", OverallAngle, AllText[SpecialAttrPropCat][5], font, TextColor) ;	
		for (int i = 0 ; i <= 1 ; ++i)
		{
			DP.DrawLine(new int[] {Pos.x + (int)(0.025*L), Pos.x + (int)(0.975*L)}, new int[] {(int)(Pos.y - H + (i + 12.5)*sy), (int)(Pos.y - H + (i + 12.5)*sy)}, lineW, LineColor) ;
			DP.DrawText(new Point(Pos.x + (int)(0.2*L), (int)(Pos.y - H + (i + 13)*sy)), "Center", OverallAngle, AllText[AttrCat][i + 13], font, AttributeColor[i + 2]) ;	
		}
		for (int i = 0 ; i <= 3 ; ++i)
		{
			DP.DrawLine(new int[] {Pos.x + (int)(0.375*L + i*sx), Pos.x + (int)(0.375*L + i*sx)}, new int[] {(int)(Pos.y - H + 11.5*sy), (int)(Pos.y - H + 14.5*sy)}, lineW, LineColor) ;
			DP.DrawText(new Point(Pos.x + (int)(0.45*L + i*sx), (int)(Pos.y - H + 13*sy)), "Center", OverallAngle, String.valueOf(UtilG.Round(BA.getBlood()[i + 4], 2)), font, AttributeColor[2]) ;	
			DP.DrawText(new Point(Pos.x + (int)(0.45*L + i*sx), (int)(Pos.y - H + 14*sy)), "Center", OverallAngle, String.valueOf(UtilG.Round(BA.getPoison()[i + 4], 2)), font, AttributeColor[3]) ;	
		}
		
		DP.DrawText(new Point(Pos.x + (int)(0.025*L), (int)(Pos.y - H + 16*sy)), "BotLeft", OverallAngle, AllText[AttrCat][11] + " " + AllText[SpecialAttrPropCat][7] + " = " + UtilG.Round(BA.getStun()[4], 2), font, AttributeColor[0]) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.025*L), (int)(Pos.y - H + 17*sy)), "BotLeft", OverallAngle, AllText[AttrCat][12] + " " + AllText[SpecialAttrPropCat][7] + " = " + UtilG.Round(BA.getBlock()[4], 2), font, AttributeColor[1]) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.025*L), (int)(Pos.y - H + 18*sy)), "BotLeft", OverallAngle, AllText[AttrCat][13] + " " + AllText[SpecialAttrPropCat][7] + " = " + UtilG.Round(BA.getBlood()[8], 2), font, AttributeColor[2]) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.025*L), (int)(Pos.y - H + 19*sy)), "BotLeft", OverallAngle, AllText[AttrCat][14] + " " + AllText[SpecialAttrPropCat][7] + " = " + UtilG.Round(BA.getPoison()[8], 2), font, AttributeColor[3]) ;	
		DP.DrawText(new Point(Pos.x + (int)(0.025*L), (int)(Pos.y - H + 20*sy)), "BotLeft", OverallAngle, AllText[AttrCat][15] + " " + AllText[SpecialAttrPropCat][7] + " = " + UtilG.Round(BA.getSilence()[4], 2), font, AttributeColor[4]) ;				
	}
	public void DrawSpellsBar(Player player, Spells[] spells, Image CooldownImage, Image SlotImage, Point MousePos, DrawPrimitives DP)
	{
		Font Titlefont = new Font("SansSerif", Font.BOLD, 10) ;
		Font font = new Font("SansSerif", Font.BOLD, 9) ;
		Screen screen = Game.getScreen() ;
		Color[] colorPalette = Game.ColorPalette ;
		float OverallAngle = DrawPrimitives.OverallAngle ;
		Point Pos = new Point(screen.getSize().x + 1, (int) (0.99 * screen.getSize().y) - 70) ;
		ArrayList<Integer> ActiveSpells = player.GetActiveSpells() ;
		Size size = new Size(36, 130) ;
		int slotW = SlotImage.getWidth(null), slotH = SlotImage.getHeight(null) ;	// Bar size
		int Ncols = Math.max(ActiveSpells.size() / 11 + 1, 1) ;
		int Nrows = ActiveSpells.size() / Ncols + 1 ;
		int Sx = (int) UtilG.spacing(size.x, Ncols, slotW, 3), Sy = (int) UtilG.spacing(size.y, Nrows, slotH, 5) ;		
		String[] Key = Player.SpellKeys ;
		Color BGcolor = Player.ClassColors[player.getJob()] ;
		Color TextColor = player.getColor() ;
		
		DP.DrawRoundRect(Pos, "BotLeft", size, 1, colorPalette[7], BGcolor, true) ;
		DP.DrawText(new Point(Pos.x + size.x / 2, Pos.y - size.y + 3), "TopCenter", OverallAngle, allText.get("* Barra de habilidades *")[1], Titlefont, colorPalette[5]) ;
		for (int i = 0 ; i <= ActiveSpells.size() - 1 ; ++i)
		{
			if (0 < player.getSpell()[ActiveSpells.get(i)].getLevel())
			{
				Point slotCenter = new Point(Pos.x + slotW / 2 + (i / Nrows) * Sx + 3, Pos.y - size.y + slotH / 2 + (i % Nrows) * Sy + Titlefont.getSize() + 5) ;
				if (player.getMp()[0] < spells[ActiveSpells.get(i)].getMpCost())
				{
					DP.DrawImage(SlotImage, slotCenter, "Center") ;
				}
				else
				{
					DP.DrawImage(SlotImage, slotCenter, "Center") ;
				}
				DP.DrawText(slotCenter, "Center", OverallAngle, Key[i], font, TextColor) ;
				Size imgSize = new Size(CooldownImage.getWidth(null), CooldownImage.getHeight(null)) ;
				if (spellCooldownCounter[ActiveSpells.get(i)] < spells[ActiveSpells.get(i)].getCooldown())
				{
					Scale Imscale = new Scale(1, 1 - (double) spellCooldownCounter[ActiveSpells.get(i)] / spells[ActiveSpells.get(i)].getCooldown()) ;
					DP.DrawImage(CooldownImage, new Point(slotCenter.x - imgSize.x / 2, slotCenter.y + imgSize.y / 2), OverallAngle, Imscale, new boolean[] {false, false}, "BotLeft", 1) ;
				}
				if (UtilG.isInside(MousePos, new Point(slotCenter.x - imgSize.x / 2, slotCenter.y - imgSize.y / 2), imgSize))
				{
					DP.DrawText(new Point(slotCenter.x - imgSize.x - 10, slotCenter.y), "CenterRight", OverallAngle, spells[ActiveSpells.get(i)].getName(), Titlefont, TextColor) ;
				}
			}
		}
	}
	public void DrawSideBar(Pet pet, Point MousePos, Icon[] icons, DrawPrimitives DP)
	{
		// icons: 0: Options 1: Bag 2: Quest 3: Map 4: Book, 5: player, 6: pet
		Screen screen = Game.getScreen() ;
		Color[] colorPalette = Game.ColorPalette ;
		float OverallAngle = DrawPrimitives.OverallAngle ;
		Font font = new Font("SansSerif", Font.BOLD, 13) ;
		String[] IconKey = new String[] {Player.ActionKeys[4], Player.ActionKeys[9], Player.ActionKeys[7]} ;
		Color TextColor = colorPalette[7] ;
		
		DP.DrawRect(new Point(screen.getSize().x, screen.getSize().y), "BotLeft", new Size(40, screen.getSize().y), 1, colorPalette[9], colorPalette[9], false) ;	// Background
		DrawSpellsBar(this, spell, Spells.cooldownImage, Spells.slotImage, MousePos, DP) ;
		icons[0].DrawImage(OverallAngle, -1, MousePos, DP) ;		// settings
		icons[1].DrawImage(OverallAngle, -1, MousePos, DP) ;		// bag
		DP.DrawText(icons[1].getPos(), "BotLeft", OverallAngle, IconKey[0], font, TextColor) ;
		icons[2].DrawImage(OverallAngle, -1, MousePos, DP) ;		// quest
		DP.DrawText(icons[2].getPos(), "BotLeft", OverallAngle, IconKey[1], font, TextColor) ;
		if (questSkills.get(PA.getMap().getContinentName(this)))	// map
		{
			icons[3].DrawImage(OverallAngle, 0, MousePos, DP) ;
			DP.DrawText(icons[3].getPos(), "BotLeft", OverallAngle, IconKey[2], font, TextColor) ;
		}
		if (fabWindow != null)										// book
		{
			icons[4].DrawImage(OverallAngle, 0, MousePos, DP) ;
		}
		
		// player
		display(icons[5].getPos(), new Scale(1, 1), Player.MoveKeys[0], false, DP) ;
		DP.DrawText(icons[5].getPos(), "BotLeft", OverallAngle, Player.ActionKeys[5], font, TextColor) ;
		if (0 < attPoints)
		{
			DP.DrawImage(icons[5].getImage(), new Point(icons[5].getPos().x - icons[5].getImage().getWidth(null), icons[5].getPos().y), OverallAngle, new Scale(1, 1), new boolean[] {false, false}, "BotLeft", 1) ;
		}
		
		// pet
		if (pet != null)
		{
			pet.display(icons[6].getPos(), new Scale(1, 1), DP) ;
			DP.DrawText(icons[6].getPos(), "BotLeft", OverallAngle, Player.ActionKeys[8], font, TextColor) ;
		}
		
		// Hotkeys
		DP.DrawRoundRect(new Point(screen.getSize().x + 1, screen.getSize().y - 70), "TopLeft", new Size(36, 60), 1, colorPalette[7], colorPalette[19], true) ;
		for (int i = 0 ; i <= Player.HotKeys.length - 1 ; i += 1)
		{
			Point slotCenter = new Point(screen.getSize().x + 10, screen.getSize().y - 60 + 20 * i) ;
			Size slotSize = new Size(Spells.slotImage.getWidth(null), Spells.slotImage.getHeight(null)) ;
			DP.DrawImage(Spells.slotImage, slotCenter, "Center") ;
			DP.DrawText(new Point(slotCenter.x + slotSize.x / 2 + 5, slotCenter.y + slotSize.y / 2), "BotLeft", OverallAngle, Player.HotKeys[i], font, TextColor) ;
			if (hotkeyItem[i] != null)
			{
				DP.DrawImage(hotkeyItem[i].getImage(), slotCenter, "Center") ;
				if (UtilG.isInside(MousePos, slotCenter, slotSize))
				{
					DP.DrawText(new Point(slotCenter.x - slotSize.x - 10, slotCenter.y), "CenterRight", OverallAngle, hotkeyItem[i].getName(), font, TextColor) ;
				}
			}
		}
	}
	public void DrawRange(DrawPrimitives DP)
	{
		DP.DrawCircle(PA.getPos(), (int)(2 * PA.getRange()), 2, Game.ColorPalette[PA.Job], false, true) ;
	}
	public void DrawEquips(Point Pos, int Job, int equiptype, int EquipID, float[][] EquipsBonus, Scale scale, float angle, DrawPrimitives DP)
	{
		int bonus = 0 ;
		if (EquipsBonus[EquipID][1] == 10)
		{
			bonus = 8 ;
		}
		if (equiptype == 0)
		{
			DP.DrawImage(Items.EquipImage[PA.Job + bonus], Pos, angle, scale, new boolean[] {false, false}, "Center", 1) ;
		}
		if (equiptype == 1)
		{
			DP.DrawImage(Items.EquipImage[5 + bonus], Pos, angle, scale, new boolean[] {false, false}, "Center", 1) ;
		}
		if (equiptype == 2)
		{
			DP.DrawImage(Items.EquipImage[6 + bonus], Pos, angle, scale, new boolean[] {false, false}, "Center", 1) ;
		}
		if (equiptype == 3)
		{
			DP.DrawImage(Items.EquipImage[7], Pos, angle, scale, new boolean[] {false, false}, "Center", 1) ;
		}
	}
	public void DrawPlayerEquips(int[] Pos, float[] playerscale, DrawPrimitives DP)
	{
		Scale scale = new Scale(0.6, 0.6) ;
		float[] angle = new float[] {50, 30, 0, 0, 0} ;
		Point EqPos = new Point((int)(Pos[0] + 0.16 * PA.getSize().x * playerscale[0]), (int)(Pos[1] - 0.4 * PA.getSize().y * playerscale[1])) ;
		if (equips[0] != null)
		{
			DrawEquips(EqPos, PA.Job, 0, equips[0].getId() - Items.BagIDs[5], Items.EquipsBonus, scale, angle[PA.Job], DP) ;
		}	
		
		/*
		 * int bonus = 0 ;
		if (EquipsBonus[EquipID][1] == 10)
		{
			bonus = 8 ;
		}
		if (equiptype == 0)	// 0: weapon
		{
			DP.DrawImage(Items.EquipImage[Job + bonus], Pos, angle, scale, new boolean[] {false, false}, "Center", 1) ;
		}
		if (1 <= equiptype)	// 1: shield, 2: armor, 3: arrow
		{
			DP.DrawImage(Items.EquipImage[equiptype + 1 + bonus], Pos, angle, scale, new boolean[] {false, false}, "Center", 1) ;
		}
		 * */
	}
	public void DrawWeapon(Point Pos, float[] playerscale, DrawPrimitives DP)
	{
		Scale scale = new Scale(0.6, 0.6) ;
		float[] angle = new float[] {50, 30, 0, 0, 0} ;
		Point EqPos = new Point((int)(Pos.x + 0.16*PA.getSize().x*playerscale[0]), (int)(Pos.y - 0.4*PA.getSize().y*playerscale[1])) ;
		if (getEquips()[0] != null)
		{
			DrawEquips(EqPos, PA.Job, 0, getEquips()[0].getId() - Items.BagIDs[6], Items.EquipsBonus, scale, angle[PA.Job], DP) ;
		}	
	}
	public void DrawTimeBar(Creature creature, DrawPrimitives DP)
	{
		String relPos = UtilS.RelPos(PA.getPos(), creature.getPos()) ;
		DrawTimeBar(relPos, Game.ColorPalette[9], DP) ;
	}
	public void DrawSpellsTree(Player player, Spells[] spells, Point MousePos, int SelectedSpell, Icon SpellsTreeIcon, DrawFunctions DF)
	{
		DrawPrimitives DP = DF.getDrawPrimitives() ;
		Screen screen = Game.getScreen() ;
		int[] Sequence = player.GetSpellSequence() ;
		int[] ProSequence = player.GetProSpellSequence() ;
		int NumberOfSpells = player.GetNumberOfSpells() ;
		int NumberOfProSpells = 0 ;
		Font font = new Font("SansSerif", Font.BOLD, 10) ;
		Font Largefont = new Font("SansSerif", Font.BOLD, 12) ;
		Point Pos = new Point((int)(0.1*screen.getSize().x), (int)(0.9*screen.getSize().y)) ;
		Size Size = new Size((int)(0.7*screen.getSize().x), (int)(0.66*screen.getSize().y)) ;
		int TabL = Size.x / 20, TabH = Size.y / 3 ;
		Size size = new Size((int)(0.2*screen.getSize().x), (int)(0.1*screen.getSize().y)) ;
		int Sx = size.x / 10, Sy = size.y / 10 ;
		Color[] SpellsColors = new Color[NumberOfSpells + NumberOfProSpells] ;
		Color[] TabColor = new Color[] {Game.ColorPalette[7], Game.ColorPalette[7]} ;
		Color[] TabTextColor = new Color[] {Game.ColorPalette[5], Game.ColorPalette[5]} ;
		int tab = 0 ;
		if (NumberOfSpells - 1 < SelectedSpell)
		{
			tab = 1 ;
			NumberOfProSpells = player.GetNumberOfProSpells() ;
			Sequence = ProSequence ;
		}

		Color[] color = new Color[spells.length] ;
		for (int i = 0 ; i <= spells.length - 1 ; i += 1)
		{
			color[i] = Game.ColorPalette[4] ;
			if(UtilS.SpellIsAvailable(player, spells, i))
			{
				color[i] = Game.ColorPalette[5] ;
			}
		}
		
		// Main window
		DP.DrawRoundRect(Pos, "BotLeft", Size, 1, Game.ColorPalette[20], Game.ColorPalette[20], true) ;
		TabColor[tab] = Game.ColorPalette[20] ;
		TabTextColor[tab] = Game.ColorPalette[3] ;
		DP.DrawRoundRect(new Point(Pos.x, Pos.y - 2*TabH), "BotRight", new Size(TabL, TabH), 1, TabColor[0], Game.ColorPalette[8], true) ;
		DP.DrawText(new Point(Pos.x + TabL/2 + UtilG.TextH(font.getSize())/2, Pos.y - 2*TabH - TabH/2), "Center", 90, allText.get("* Classes *")[player.getJob() + 1], Largefont, TabTextColor[0]) ;
		if (0 < player.getProJob())
		{
			DP.DrawRoundRect(new Point(Pos.x, Pos.y - TabH), "BotRight", new Size(TabL, TabH), 1, TabColor[1], Game.ColorPalette[8], true) ;	
			DP.DrawText(new Point(Pos.x + TabL/2 + UtilG.TextH(font.getSize())/2, Pos.y - 3*TabH/2), "Center", 90, allText.get("* ProClasses *")[player.getProJob() + 2*player.getJob()], Largefont, TabTextColor[1]) ;
		}
		
		// Organogram
		//String[] SkillNames = null ;
		String[][] SpellNames = new String[2][] ;
		String[] SpellLevels = null ;
		if (tab == 0)
		{
			for (int spell = 0 ; spell <= NumberOfSpells - 1 ; spell += 1)
			{
				SpellNames[0] = UtilG.AddElem(SpellNames[0], spells[spell].getName()) ;
				SpellNames[1] = UtilG.AddElem(SpellNames[1], spells[spell].getType()) ;
				SpellLevels = UtilG.AddElem(SpellLevels, String.valueOf(player.getSpell()[spell])) ;
				SpellsColors[spell] = color[spell] ;
			}
		}
		if (tab == 1)
		{
			for (int spell = NumberOfSpells ; spell <= NumberOfSpells + NumberOfProSpells - 1 ; spell += 1)
			{
				SpellNames[0] = UtilG.AddElem(SpellNames[0], spells[spell].getName()) ;
				SpellNames[1] = UtilG.AddElem(SpellNames[1], spells[spell].getType()) ;
				SpellLevels = UtilG.AddElem(SpellLevels, String.valueOf(player.getSpell()[spell])) ;
				SpellsColors[spell] = color[spell] ;
			}
		}
		SpellsColors[SelectedSpell - tab*NumberOfSpells] = Game.ColorPalette[3] ;
		DF.DrawOrganogram(Sequence, new Point(Pos.x, Pos.y - Size.y), Sx, Sy, size, SpellNames, SpellLevels, SpellsTreeIcon, font, SpellsColors, MousePos) ;
		
		// Skill info
		int TextmaxL = Size.x / 5, sx = 10, sy = UtilG.TextH(font.getSize()) + 2 ;
		String Description = spells[SelectedSpell].getInfo()[0], Effect = spells[SelectedSpell].getInfo()[1] ;
		DP.DrawRoundRect(new Point(Pos.x, Pos.y - Size.y), "BotLeft", new Size(Size.x, Size.y / 4), 1, Game.ColorPalette[7], Game.ColorPalette[7], true) ;
		DP.DrawFitText(new Point(Pos.x + sx, Pos.y - Size.y - Size.y / 5), sy, "BotLeft", Effect, font, TextmaxL, player.getColor()) ;
		DP.DrawFitText(new Point(Pos.x + sx, Pos.y - Size.y - Size.y / 10), sy, "BotLeft", Description, font, TextmaxL - 6, player.getColor()) ;		
		DP.DrawText(new Point(Pos.x + Size.x, Pos.y), "TopRight", DrawPrimitives.OverallAngle, "Pontos: " +  player.getSkillPoints(), font, player.getColor()) ;		
	}	
	public void display(Point PlayerPos, Scale scale, String dir, boolean ShowPlayerRange, DrawPrimitives DP)
	{
		// BP = Body part
		// 0,0 on shoes tip
		// 0: Legs, 1: Shoes, 2: Shirt, 3: Arms, 4: Head, 5: Eyes, 6: Hair

		float OverallAngle = DrawPrimitives.OverallAngle ;
		if (IsRiding())	// If the player is mounted
		{
			DP.DrawImage(RidingImage,
					new Point(PA.getPos().x - RidingImage.getWidth(null)/2, PA.getPos().y + RidingImage.getHeight(null)/2),
					OverallAngle, scale, new boolean[] {false, false}, "BotLeft", 1) ;
		}
		//Image[] PlayerImages = new Image[] {PA.getimage()} ;
		//float[][] BPScale = new float[PA.getimage().length][2] ;
		boolean[] mirror = new boolean[] {false, false} ;
		if (questSkills.get("Dragon's aura"))
		{
			DP.DrawImage(DragonAuraImage, new Point(PlayerPos.x, (int) (PlayerPos.y - 0.5*scale.y * PA.getSize().y)), OverallAngle, scale, mirror, "Center", 0.5) ;					
		}
		if (dir.equals("Acima"))
		{
			DP.DrawImage(movingAni.idleGif, new Point(PlayerPos.x, (int) (PlayerPos.y - 0.5*scale.y * PA.getSize().y)), OverallAngle, scale, mirror, "Center", 1) ;
		}
		if (dir.equals("Abaixo"))
		{
			DP.DrawImage(movingAni.movingDownGif, new Point(PlayerPos.x, (int) (PlayerPos.y - 0.5*scale.y * PA.getSize().y)), OverallAngle, scale, mirror, "Center", 1) ;
		}
		if (dir.equals("Esquerda"))
		{
			DP.DrawImage(movingAni.movingLeftGif, new Point(PlayerPos.x, (int) (PlayerPos.y - 0.5*scale.y * PA.getSize().y)), OverallAngle, scale, mirror, "Center", 1) ;
		}
		if (dir.equals("Direita"))
		{
			if (PA.countmove % 2 == 0)
			{
				DP.DrawImage(movingAni.movingRightGif, new Point(PlayerPos.x, (int) (PlayerPos.y - 0.5*scale.y * PA.getSize().y)), OverallAngle, scale, mirror, "Center", 1) ;
			}
			else
			{
				DP.DrawImage(movingAni.movingUpGif, new Point(PlayerPos.x, (int) (PlayerPos.y - 0.5*scale.y * PA.getSize().y)), OverallAngle, scale, mirror, "Center", 1) ;
			}
		}
		if (ShowPlayerRange)
		{
			DrawRange(DP) ;
		}
		/*for (int i = 0 ; i <= PlayerImages.length - 1 ; i += 1)
		{
			BPScale[i][0] = (float)0.01*scale[0]*BodyPartsSizes[i][0] ;
			BPScale[i][1] = (float)0.01*scale[1]*BodyPartsSizes[i][1] ;
		}*/
		/*for (int i = 0 ; i <= PlayerImages.length - 1 ; i += 1)						
		{
			DP.DrawImage(PlayerImages[i], new int[] {PlayerPos.x, (int) (PlayerPos.y - 0.5*scale[1] * PA.getSize().y)}, OverallAngle, BPScale[i], mirror, "Center") ;
		}*/
		//DP.DrawRect(player.getPos(), "Center", (int)(player.getSize().x*scale[0]), (int)(player.getSize().y*scale[1]), 1, null, ColorPalette[9], true) ;	// Player contour
		//DrawCircle(player.getPos(), 2, 2, ColorPalette[6], false, true) ;	// Player center
	}

	public void ShowEffectsAndStatusAnimation(Creature creature, DrawPrimitives DP)
	{
		int mirror = UtilS.MirrorFromRelPos(UtilS.RelPos(getPos(), creature.getPos())) ;
		Size offset = new Size(8, (int)(0.8*getSize().y)) ;
		ShowEffectsAndStatusAnimation(getPos(), mirror, offset, StatusImages, getBA().getSpecialStatus(), isDefending(), DP) ;
	}
	
	
	/* Save and load methods */
	public void Save(String filePath, Pet pet)
	{
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
			bw.write("\nPlayer PA.ProJob: \n" + PA.ProJob) ;
			bw.write("\nPlayer continent: \n" + getContinent()) ;
			bw.write("\nPlayer map: \n" + getMap()) ;
			bw.write("\nPlayer pos: \n" + getPos()) ;
			bw.write("\nPlayer skill: \n" + Arrays.toString(getSpell())) ;
			//bw.write("\nPlayer quest: \n" + Arrays.toString(getQuest())) ;
			//bw.write("\nPlayer bag: \n" + Arrays.toString(getBag())) ;
			bw.write("\nPlayer equips: \n" + Arrays.toString(getEquips())) ;
			bw.write("\nPlayer skillPoints: \n" + getSkillPoints()) ;
			bw.write("\nPlayer life: \n" + Arrays.toString(getLife())) ;
			bw.write("\nPlayer mp: \n" + Arrays.toString(getMp())) ;
			bw.write("\nPlayer range: \n" + getRange()) ;
			bw.write("\nPlayer phyAtk: \n" + Arrays.toString(getPhyAtk())) ;
			bw.write("\nPlayer magAtk: \n" + Arrays.toString(getMagAtk())) ;
			bw.write("\nPlayer phyDef: \n" + Arrays.toString(getPhyDef())) ;
			bw.write("\nPlayer magDef: \n" + Arrays.toString(getMagDef())) ;
			bw.write("\nPlayer dex: \n" + Arrays.toString(getDex())) ;
			bw.write("\nPlayer agi: \n" + Arrays.toString(getAgi())) ;
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
			bw.write("\nPlayer exp: \n" + Arrays.toString(getExp())) ;
			bw.write("\nPlayer satiation: \n" + Arrays.toString(getSatiation())) ;
			//bw.write("\nPlayer quest skills: \n" + Arrays.toString(questSkills)) ;
			bw.write("\nPlayer status: \n" + Arrays.toString(BA.getSpecialStatus())) ; 
			bw.write("\nPlayer actions: \n" + Arrays.deepToString(getActions())) ; 
			bw.write("\nPlayer battle actions: \n" + Arrays.deepToString(BA.getBattleActions())) ; 
			bw.write("\nPlayer status counter: \n" + Arrays.toString(getStatusCounter())) ; 		
			bw.write("\nPlayer stats: \n" + Arrays.toString(getStats())) ;
			bw.write("\nPlayer available attribute points: \n" + getAttPoints()) ;
			bw.write("\nPlayer attribute increase: \n" + Arrays.deepToString(getAttIncrease())) ;
			bw.write("\nPlayer chance increase: \n" + Arrays.deepToString(getChanceIncrease())) ;
			//bw.write("\nPlayer creatures discovered: \n" + Arrays.toString(getCreaturesDiscovered())) ;
			pet.Save(bw) ;	
			
			bw.write("\nEquips bonus: \n" + Arrays.deepToString(Items.EquipsBonus)) ;
			//bufferedWriter.write("\nNPCs contact: \n" + Arrays.toString(FirstNPCContact)) ;
			bw.write("\nDifficult level: \n" + difficultLevel) ;
			bw.close() ;
		}		
		catch(IOException ex) 
		{
            System.out.println("Error writing to file '" + filePath + "'") ;
        }
	}
	public void Load(String filePath, Pet pet, Maps[] maps)
	{
		JSONObject JsonData = UtilG.readJson(filePath) ;
		setName((String) JsonData.get("Name")) ;
		setLevel(Math.toIntExact((Long) JsonData.get("Level"))) ;
	}
	
	
	
	
	
	
	
	
	/*
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
			PA.Job = Integer.parseInt(ReadFile[2*7][0]) ;
			PA.setProJob(Integer.parseInt(ReadFile[2*8][0])) ;
			setMap(Game.getMaps()[Integer.parseInt(ReadFile[2*10][0])]) ;
			setPos((Point) UtilG.ConvertArray(UtilG.toString(ReadFile[2*11]), "String", "int")) ;
			//spell = (Spell[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*12]), "String", "int") ;
			//QuestWindow = (int[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*13]), "String", "int") ;
			//setBag((int[]) Utg.ConvertArray(Utg.toString(ReadFile[2*14]), "String", "int")) ;
			Equips = (int[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*15]), "String", "int") ;
			SpellPoints = Integer.parseInt(ReadFile[2*16][0]) ;
			PA.setLife((float[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*17]), "String", "float")) ;
			PA.setMp((float[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*18]), "String", "float")) ;
			PA.setRange(Float.parseFloat(ReadFile[2*19][0])) ;
			BA.setPhyAtk((float[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*20]), "String", "float")) ;
			BA.setMagAtk((float[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*21]), "String", "float")) ;
			BA.setPhyDef((float[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*22]), "String", "float")) ;
			BA.setMagDef((float[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*23]), "String", "float")) ;
			BA.setDex((float[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*24]), "String", "float")) ;
			BA.setAgi((float[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*25]), "String", "float")) ;
			BA.setCrit((float[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*26]), "String", "float")) ;
			BA.setStun((float[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*27]), "String", "float")) ;
			BA.setBlock((float[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*28]), "String", "float")) ;
			BA.setBlood((float[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*29]), "String", "float")) ;
			BA.setPoison((float[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*30]), "String", "float")) ;
			BA.setSilence((float[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*31]), "String", "float")) ;
			PA.Elem = UtilG.toString(ReadFile[2*32]) ;
			ElemMult = (float[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*33]), "String", "float") ;
			Collect = (float[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*34]), "String", "float") ;
			PA.setLevel(Integer.parseInt(ReadFile[2*35][0])) ;
			Gold = (float[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*36]), "String", "float") ;
			setStep(Integer.parseInt(ReadFile[2*37][0])) ;
			PA.setExp((float[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*38]), "String", "float")) ;
			PA.setSatiation((float[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*39]), "String", "float")) ;
			QuestSkills = (boolean[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*40]), "String", "boolean") ;
			BA.setSpecialStatus((int[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*41]), "String", "int")) ;
			PA.Actions = (int[][]) UtilG.ConvertDoubleArray(UtilG.deepToString(ReadFile[2*42], 3), "String", "int") ;
			BA.setBattleActions((int[][]) UtilG.ConvertDoubleArray(UtilG.deepToString(ReadFile[2*43], 3), "String", "int")) ;
			SpellIsActive = new boolean[getSpell().length] ;
			SpellCounter = new int[getSpell().length][2] ;
			StatusCounter = (int[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*44]), "String", "int") ;
			Stats = (float[]) UtilG.ConvertArray(UtilG.toString(ReadFile[2*45]), "String", "float") ;
			AttPoints = Integer.parseInt(ReadFile[2*46][0]) ;
			AttIncrease = (float[][]) UtilG.ConvertDoubleArray(UtilG.deepToString(ReadFile[2*47], 8), "String", "float") ;
			ChanceIncrease = (float[][]) UtilG.ConvertDoubleArray(UtilG.deepToString(ReadFile[2*48], 8), "String", "float") ;
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
		float CollectChance = (float) ((maps[getMap()].getCollectibleLevel() + 2)*Math.random()) ;
		int MapCollectLevel = maps[getMap()].getCollectibleLevel() ;
		float PlayerCollectLevel = -1 ;
		if (CollectibleType == 0)	// Berry
		{
			Bag[BagIDs[3] - 1 + MapCollectLevel + CollectibleType] += 1 ;
			if (PA.Job == 3 & Math.random() <= 0.14*Skill[4])	// Double collect
			{
				Bag[BagIDs[3] - 1 + MapCollectLevel + CollectibleType] += 1 ;	
			}
			CollectResult = 0 ;
		}
		else if (MapCollectLevel <= PlayerCollectLevel + 1 & PlayerCollectLevel + 1 < CollectChance)
		{					
			Bag[BagIDs[0] + 3*(MapCollectLevel - 1) + CollectibleType - 1] += 1 ;
			Collect[CollectibleType - 1] += 0.25/(PlayerCollectLevel + 1) ;
			if (PA.Job == 3 & Math.random() <= 0.14*Skill[4])	// Double collect
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
		float TextAngle = DrawPrimitives.OverallAngle ;
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
		DP.DrawText(new Point(WindowPos.x + 5, WindowPos.y + (int)(0.05*H)), "Center", 90, AllText[TabsCat][1], Namefont, TabTextColor[0]) ;				// Tab 0 text	
		DP.DrawText(new Point(WindowPos.x + 5, WindowPos.y + (int)(0.15*H)), "Center", 90, AllText[TabsCat][2], Namefont, TabTextColor[1]) ;				// Tab 1 text	
		DP.DrawText(new Point(WindowPos.x + 5, WindowPos.y + (int)(0.25*H)), "Center", 90, AllText[TabsCat][3], Namefont, TabTextColor[2]) ;				// Tab 2 text	
		if (Tab == 0)
		{
			//	Player
			display(PlayerImagePos, new float[] {(float) 1.8, (float) 1.8}, PA.getDir(), false, DP) ;
			DP.DrawText(new Point(WindowPos.x + (int)(0.5*L), WindowPos.y + (int)(0.1*H)), "Center", TextAngle, PA.getName(), Namefont, TextColor) ;						// Name text			
			DP.DrawText(new Point(WindowPos.x + (int)(0.5*L), WindowPos.y + (int)(0.03*H)), "Center", TextAngle, AllText[AttCat][1] + ": " + PA.getLevel(), font, ColorPalette[6]) ;	// Level text		
			if(PA.ProJob == 0)
			{
				DP.DrawText(new Point(WindowPos.x + (int)(0.5*L), WindowPos.y + (int)(0.06*H)), "Center", TextAngle, AllText[ClassesCat][PA.Job + 1], font, ColorPalette[5]) ;	// PA.Job text			
			}
			else
			{
				DP.DrawText(new Point(WindowPos.x + (int)(0.5*L), WindowPos.y + (int)(0.15*H + TextH/2)), "Center", TextAngle, AllText[ProClassesCat][PA.ProJob + 2*PA.Job], font, ColorPalette[5]) ;	// Pro job text					
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
				Image ElemImage = DrawFunctions.ElementImages[Uts.ElementID(PA.Elem[eq + 1])] ;
				if (0 < Equips[eq])
				{
					if (eq <= 2)
					{
						if (0 < EquipsBonus[Equips[eq] - Items.BagIDs[6]][0])
						{
							if (-1 < EquipsCat)
							{
								DP.DrawText(new Point(EqRectPos[eq].x, EqRectPos[eq].y - EqRectH[eq] / 2 - TextH), "Center", TextAngle, AllText[EquipsCat][eq + 1] + " + " + (int)(EquipsBonus[Equips[eq] - Items.BagIDs[6]][1]), font, TextColor) ;					
							}
						}
						DrawEquips(EqRectPos[eq], PA.Job, eq, Equips[eq] - Items.BagIDs[6], EquipsBonus, new float[] {1, 1}, TextAngle, DP) ;
					}
					else if (eq == 3)
					{
						DrawEquips(EqRectPos[eq], PA.Job, eq, Equips[0] - Items.BagIDs[6], EquipsBonus, new float[] {1, 1}, TextAngle, DP) ;
					}
					DP.DrawImage(ElemImage, new Point((int) (EqRectPos[eq].x + 0.3*EqRectL[eq]), (int) (EqRectPos[eq].y + 0.3*EqRectH[eq])), TextAngle, new float[] {(float) 0.12, (float) 0.12}, new boolean[] {false, false}, "Center", 1) ;					
					//DP.DrawTextUntil(new Point(EqRectPos[eq].x - EqRectL[eq] / 2, EqRectPos[eq].y + EqRectH[eq] / 2 + TextH), "BotLeft", TextAngle, items[Equips[eq]].getName(), Equipfont, TextColor, 14, MousePos) ;	// Equip text	
				}
				else
				{
					DP.DrawText(new Point(EqRectPos[eq].x + EqRectL[eq]/2, EqRectPos[eq].y - EqRectH[eq] / 2 - TextH), "Center", TextAngle, AllText[EquipsCat][eq + 1], font, TextColor) ;
				}
			}
			
			// Super element
			if (PA.Elem[1].equals(PA.Elem[2]) & PA.Elem[2].equals(PA.Elem[3]))
			{
				DP.DrawImage(DrawFunctions.ElementImages[Uts.ElementID(PA.Elem[4])], new Point(WindowPos.x + (int)(0.5*L), WindowPos.y + (int)(0.45*H)), TextAngle, new float[] {(float) 0.3, (float) 0.3}, new boolean[] {false, false}, "Center", 1) ;
			}
			
			//	Attributes
			float[] Attributes = new float[] {BA.TotalPhyAtk(), BA.TotalMagAtk(), BA.TotalPhyDef(), BA.TotalMagDef(), BA.TotalDex(), BA.TotalAgi()} ;
			int AttSy = 22 ;
			DP.DrawText(new Point(WindowPos.x + 15, WindowPos.y + 30), "BotLeft", TextAngle, AllText[AttCat][2] + ": " + Utg.Round(PA.getLife()[0], 1), font, ColorPalette[6]) ;	// Life text	
			DP.DrawText(new Point(WindowPos.x + 15, WindowPos.y + 40), "BotLeft", TextAngle, AllText[AttCat][3] + ": " + Utg.Round(PA.getMp()[0], 1), font, ColorPalette[5]) ;	// MP text

			DP.DrawImage(Equip.SwordImage, new Point(WindowPos.x + 30, WindowPos.y + 134 + 1 * AttSy), new float[] {(float) (11 / 38.0), (float) (11 / 38.0)}, "Center") ;	// Draw sword icon
			for (int i = 0; i <= Attributes.length - 1; i += 1)
			{
				DP.DrawText(new Point(WindowPos.x + 45, WindowPos.y + 136 + (i + 1) * AttSy), "BotLeft", TextAngle, AllText[AttCat][4] + ": " + Utg.Round(Attributes[i], 1), font, TextColor) ;
			}	
			DP.DrawText(new Point(WindowPos.x + 45, WindowPos.y + 136 + 7 * AttSy), "BotLeft", TextAngle, AllText[AttCat][10] + ": " + Utg.Round(100 * BA.TotalCritAtkChance(), 1) + "%", font, ColorPalette[6]) ;		
			
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
			DP.DrawImage(GoldCoinImage, new Point(WindowPos.x + (int)(0.04*L), WindowPos.y + (int)(0.975*H)), TextAngle, new float[] {(float) 1.2, (float) 1.2}, new boolean[] {false, false}, "BotLeft", 1) ;
			DP.DrawText(new Point(WindowPos.x + (int)(0.18*L), WindowPos.y + (int)(0.96*H) - TextH/2), "BotLeft", TextAngle, String.valueOf(Utg.Round(Gold[0], 1)), font, ColorPalette[18]) ;	// Gold text
		
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
		float PotMult = 1 ;
		if (PA.Job == 3)
		{
			PotMult += 0.06 * Spell[7] ;
		}
		if (-1 < itemID)	// if the item is valid
		{
			//if (0 < Bag[itemID])	// if the player has the item in the bag
			//{
				if (items[itemID].getType().equals("Potion"))	// potions
				{
					float HealPower = Items.PotionsHealing[itemID][1] * PotMult ;
					
					PA.incLife(HealPower * PA.getLife()[1]) ;
					PA.incMP(HealPower * PA.getMp()[1]) ;
					//Bag[itemID] += -1 ;			
				}
				else if (items[itemID].getType().equals("Alchemy"))	// alchemy (using herbs heal half of their equivalent pot)
				{
					float HealPower = Items.PotionsHealing[itemID / 3 - Items.BagIDs[0] / 3 + 1][1] / 2 * PotMult ;
					
					PA.incLife(HealPower * PA.getLife()[1]) ;
					if (0 < pet.getLife()[0])
					{
						pet.incLife(HealPower * pet.getLife()[1]) ;
						pet.incMP(HealPower * pet.getMp()[1]) ;
						
						// Animal's skill "natural help": gives 20% bonus on the pet attributes. The enhanced attributes depend on the collectible used
						if (-1 < creatureID & BA.getBattleActions()[0][2] == 1 & isInBattle() & PA.Job == 3 & (itemID - Items.BagIDs[1] + 1) % 3 < Spell[10])
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
					//if (0 < Bag[itemID] & PA.Job == 2)
					//{
						if (itemID == Items.BagIDs[5] | itemID <= Items.BagIDs[5] + 3 & 1 <= Spell[4] | itemID == Items.BagIDs[5] + 4 & 2 <= Spell[4]  | itemID == Items.BagIDs[5] + 5 & 3 <= Spell[4]  | Items.BagIDs[5] + 6 <= itemID & itemID <= Items.BagIDs[5] + 14 & 4 <= Spell[4]  | Items.BagIDs[5] + 14 <= itemID & 5 <= Spell[4])
						{
							if (0 == Equips[3])
							{
								Equips[3] = itemID ;	// Equipped arrow
								PA.Elem[0] = bag.getArrow()[itemID - Items.BagIDs[5]].getElem() ;
							}
							else
							{
								Equips[3] = 0 ;	// Unequipped arrow
								PA.Elem[0] = "n" ;
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
					if (-1 < creatureID & BA.getBattleActions()[0][2] == 1 & isInBattle() & PA.Job == 4 & 0 < Spell[8])
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
					Utg.PlayMusic(Music[Maps.Music[PA.getMap()]]) ;
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
						Utg.PlayMusic(Music[Maps.Music[PA.getMap()]]) ;
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
		float OkButtonL = (float) (0.05*L), OkButtonH = (float) (0.125*H) ;
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