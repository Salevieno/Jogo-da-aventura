package liveBeings ;

import java.awt.Color ;
import java.awt.Dimension;
import java.awt.Image ;
import java.awt.Point;
import java.awt.event.KeyEvent ;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList ;
import java.util.Arrays ;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import attributes.AttributeBonus;
import attributes.BasicAttribute;
import attributes.BasicBattleAttribute;
import attributes.BattleAttributes;
import attributes.BattleSpecialAttribute;
import attributes.BattleSpecialAttributeWithDamage;
import attributes.PersonalAttributes;
import components.Building;
import components.NPCs;
import components.Quest;
import components.QuestSkills;
import components.SpellTypes;
import graphics.Animations;
import graphics.DrawingOnPanel;
import graphics.Gif;
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
import main.AtkTypes;
import main.Battle;
import main.Game;
import maps.CityMap;
import maps.Collectible;
import maps.FieldMap;
import maps.GameMap;
import maps.GroundTypes;
import screen.SideBar;
import utilities.Align;
import utilities.AttackEffects;
import utilities.Directions;
import utilities.Elements;
import utilities.Scale;
import utilities.TimeCounter;
import utilities.UtilG;
import utilities.UtilS;
import windows.BagWindow;
import windows.BestiaryWindow;
import windows.FabWindow;
import windows.GameWindow;
import windows.HintsWindow;
import windows.MapWindow;
import windows.PlayerAttributesWindow;
import windows.PetAttributesWindow;
import windows.QuestWindow;
import windows.SettingsWindow;
import windows.SpellsTreeWindow;

public class Player extends LiveBeing
{
	private String language ;
	private String sex ;
	private Color color ;
	
	private GameWindow focusWindow ;
	private BagWindow bag ;
	private SettingsWindow settings ;
	private SpellsTreeWindow spellsTree ;
	private MapWindow mapWindow ;
	private List<Recipe> knownRecipes ;
	private FabWindow fabWindow ;
	private List<Quest> quests ;	
	private QuestWindow questWindow ;
	private HintsWindow hintsWindow ;
	private BestiaryWindow bestiary ;
	
	private int attPoints ;			// attribute points available (to upgrade the attributes)
	private int spellPoints ;		// spell points available (to upgrade the spells)
	private double[] collectLevel ;	// 0: herb, 1: wood, 2: metal
	private TimeCounter collectCounter ;	// counts the progress of the player's collection
	private Equip[] equips ;		// 0: weapon, 1: shield, 2: armor, 3: arrow
	private int storedGold ;
	private double goldMultiplier ;	// multiplies the amount of gold the player wins
	private Map<QuestSkills, Boolean> questSkills ;	// skills gained with quests
	private boolean isRiding ;		// true if the player is riding
	private AtkTypes currentBattleAction ;

	private AttributeBonus attIncrease ;	// Amount of increase in each attribute when the player levels up
	private AttributeBonus attChanceIncrease ;	// Chance of increase of these attributes
	
	private Creature closestCreature ;		// creature that is currently closest to the player
    private Creature opponent ;				// creature that is currently in battle with the player
    private Collectible currentCollectible ;
    private Item[] hotItems ;				// items on the hotkeys
	private Statistics stats ;
    
    public static final Image CollectingMessage = UtilG.loadImage(Game.ImagesPath + "\\Collect\\" + "CollectingMessage.gif") ;   
    public static final Image collectingGif = UtilG.loadImage(Game.ImagesPath + "\\Collect\\" + "Collecting.gif") ;
//    public static final Image TentImage = UtilG.loadImage(Game.ImagesPath + "\\SideBar\\" + "Icon5_tent.png") ;
    public static final Gif TentGif = new Gif(UtilG.loadImage(Game.ImagesPath + "\\Player\\" + "Icon5_Tent.gif"), 1000, false, false) ;
    public static final Image DragonAuraImage = UtilG.loadImage(Game.ImagesPath + "\\Player\\" + "DragonAura.png") ;
    public static final Image RidingImage = UtilG.loadImage(Game.ImagesPath + "\\Player\\" + "Tiger.png") ;
	public static final Image CoinIcon = UtilG.loadImage(Game.ImagesPath + "\\Player\\" + "CoinIcon.png") ;    
	public static final Image MagicBlissGif = UtilG.loadImage(Game.ImagesPath + "\\Player\\" + "MagicBliss.gif") ;
    public static final Image FishingGif = UtilG.loadImage(Game.ImagesPath + "\\Player\\" + "Fishing.gif") ;
    
	public final static List<String[]> Properties = UtilG.ReadcsvFile(Game.CSVPath + "PlayerInitialStats.csv") ;
	public final static List<String[]> EvolutionProperties = UtilG.ReadcsvFile(Game.CSVPath + "PlayerEvolution.csv") ;	
	public final static int[] NumberOfSpellsPerJob = new int[] {14, 15, 15, 14, 14} ;
	public final static int[] CumNumberOfSpellsPerJob = new int[] {0, 34, 69, 104, 138} ;
    public final static Color[] ClassColors = new Color[] {Game.ColorPalette[0], Game.ColorPalette[1], Game.ColorPalette[2], Game.ColorPalette[3], Game.ColorPalette[4]} ;
    public final static Gif levelUpAnimation = new Gif(UtilG.loadImage(Game.ImagesPath + "\\Player\\" + "LevelUp.gif"), 170, false, false) ;
    
    public static String[] ActionKeys = new String[] {"W", "A", "S", "D", "B", "C", "F", "M", "P", "Q", "H", "R", "T", "Z"} ;	// [Up, Left, Down, Right, Bag, Char window, Fab, Map, Pet window, Quest, Hint, Ride, Tent, Bestiary]
	public static final String[] MoveKeys = new String[] {"W", "A", "S", "D", KeyEvent.getKeyText(KeyEvent.VK_UP), KeyEvent.getKeyText(KeyEvent.VK_LEFT), KeyEvent.getKeyText(KeyEvent.VK_DOWN), KeyEvent.getKeyText(KeyEvent.VK_RIGHT)} ;
	public static final String[] HotKeys = new String[] {"T", "Y", "U"} ;
	public static final List<String> SpellKeys = new ArrayList<>(Arrays.asList(new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "F10", "F11", "F12"})) ;

	public final static Image[] AttWindowImages = new Image[] {UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "PlayerAttWindow1.png"), UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "PlayerAttWindow2.png"), UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "PlayerAttWindow3.png")} ;
    
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
		level = 1 ;
		if (Game.getMaps() != null) { map = Game.getMaps()[job] ;}
		
		pos = new Point();
		dir = Directions.up;
		state = LiveBeingStates.idle;
		Image PlayerBack = UtilG.loadImage(Game.ImagesPath + "\\Player\\" + "PlayerBack.gif") ;
	    size = new Dimension (PlayerBack.getWidth(null), PlayerBack.getHeight(null));
		range = Integer.parseInt(Properties.get(job)[4]) ;
		step = Integer.parseInt(Properties.get(job)[33]);
	    elem = new Elements[] {Elements.neutral, Elements.neutral, Elements.neutral, Elements.neutral, Elements.neutral};
		actionCounter = new TimeCounter(0, Integer.parseInt(Properties.get(job)[37])) ;
		satiationCounter = new TimeCounter(0, Integer.parseInt(Properties.get(job)[38])) ;
		thirstCounter = new TimeCounter(0, Integer.parseInt(Properties.get(job)[39])) ;
		mpCounter = new TimeCounter(0, Integer.parseInt(Properties.get(job)[40])) ;
		battleActionCounter = new TimeCounter(0, Integer.parseInt(Properties.get(job)[41])) ;
		stepCounter = new TimeCounter(0, 20) ;
		combo = new ArrayList<>() ;
	    
		this.language = Language ;
		this.sex = Sex ;
		
		
		spells = new ArrayList<Spell>() ;

		focusWindow = null ;
		bag = new BagWindow(new LinkedHashMap<Potion, Integer>(),
				new LinkedHashMap<Alchemy, Integer>(),
				new LinkedHashMap<Forge, Integer>(),
				new LinkedHashMap<PetItem, Integer>(),
				new LinkedHashMap<Food, Integer>(),
				new LinkedHashMap<Arrow, Integer>(),
				new LinkedHashMap<Equip, Integer>(),
				new LinkedHashMap<GeneralItem, Integer>(),
				new LinkedHashMap<Fab, Integer>(),
				new LinkedHashMap<QuestItem, Integer>()) ;
		if (job == 2)
		{
			bag.Add(Arrow.getAll()[0], 100) ;
		}
		questWindow = new QuestWindow() ;
		quests = new ArrayList<>() ;
		knownRecipes = new ArrayList<>() ;
		fabWindow = new FabWindow() ;
		mapWindow = new MapWindow() ;
		hintsWindow = new HintsWindow() ;
		bestiary = new BestiaryWindow() ;
		equips = new Equip[4] ;	// 0: weapon, 1: shield, 2: armor, 3: arrow
		spellPoints = 0 ;
		color = Game.ColorPalette[12] ;
    	
		collectLevel = new double[3] ;
		storedGold = 0 ;
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
		currentBattleAction = null ;
		stats = new Statistics() ;
		collectCounter = new TimeCounter(0, 240) ;
		attPoints = 0 ;
		
		
		double [] basicAttInc = new double[8] ;
		double [] basicAttChanceInc = new double[8] ;
		for (int att = 0 ; att <= basicAttInc.length - 1 ; att += 1)
		{
			basicAttInc[att] = Double.parseDouble(EvolutionProperties.get(3 * job)[att + 2]) ;
			basicAttChanceInc[att] = Double.parseDouble(EvolutionProperties.get(3 * job)[att + 10]) ;
		}
		attIncrease = new AttributeBonus() ;
		attIncrease.setBasic(basicAttInc) ;
		attChanceIncrease = new AttributeBonus() ;
		attChanceIncrease.setBasic(basicAttChanceInc) ;

		closestCreature = null ;
	    opponent = null ;
	    currentCollectible = null ;
		settings = new SettingsWindow(UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "windowSettings.png"), false, true, false, 1, 1) ;
		hotItems = new Item[3] ;
		
	}
	

	private static PersonalAttributes InitializePersonalAttributes(String Name, int Job)
	{
	    BasicAttribute life = new BasicAttribute(Integer.parseInt(Properties.get(Job)[2]), Integer.parseInt(Properties.get(Job)[2]), 1) ;
	    BasicAttribute mp = new BasicAttribute(Integer.parseInt(Properties.get(Job)[3]), Integer.parseInt(Properties.get(Job)[3]), 1) ;
		BasicAttribute exp = new BasicAttribute(0, 5, Integer.parseInt(Properties.get(Job)[34])) ;
		BasicAttribute satiation = new BasicAttribute(100, 100, Integer.parseInt(Properties.get(Job)[35])) ;
		BasicAttribute thirst = new BasicAttribute(100, 100, Integer.parseInt(Properties.get(Job)[36])) ;
		return new PersonalAttributes(life, mp, exp, satiation,	thirst) ;
	}
	
	private static BattleAttributes InitializeBattleAttributes(int Job)
	{

		BasicBattleAttribute phyAtk = new BasicBattleAttribute (Double.parseDouble(Properties.get(Job)[5]), 0, 0) ;
		BasicBattleAttribute magAtk = new BasicBattleAttribute (Double.parseDouble(Properties.get(Job)[6]), 0, 0) ;
		BasicBattleAttribute phyDef = new BasicBattleAttribute (Double.parseDouble(Properties.get(Job)[7]), 0, 0) ;
		BasicBattleAttribute magDef = new BasicBattleAttribute (Double.parseDouble(Properties.get(Job)[8]), 0, 0) ;
		BasicBattleAttribute dex = new BasicBattleAttribute (Double.parseDouble(Properties.get(Job)[9]), 0, 0) ;	
		BasicBattleAttribute agi = new BasicBattleAttribute (Double.parseDouble(Properties.get(Job)[10]), 0, 0) ;
		double[] crit = new double[] {Double.parseDouble(Properties.get(Job)[11]), 0, Double.parseDouble(Properties.get(Job)[12]), 0} ;
		BattleSpecialAttribute stun = new BattleSpecialAttribute(Double.parseDouble(Properties.get(Job)[13]), 0, Double.parseDouble(Properties.get(Job)[14]), 0, Integer.parseInt(Properties.get(Job)[15])) ;
		BattleSpecialAttribute block = new BattleSpecialAttribute(Double.parseDouble(Properties.get(Job)[16]), 0, Double.parseDouble(Properties.get(Job)[17]), 0, Integer.parseInt(Properties.get(Job)[18])) ;
		BattleSpecialAttributeWithDamage blood = new BattleSpecialAttributeWithDamage(Double.parseDouble(Properties.get(Job)[19]), 0, Double.parseDouble(Properties.get(Job)[20]), 0, Integer.parseInt(Properties.get(Job)[21]), 0, Integer.parseInt(Properties.get(Job)[22]), 0, Integer.parseInt(Properties.get(Job)[23])) ;
		BattleSpecialAttributeWithDamage poison = new BattleSpecialAttributeWithDamage(Double.parseDouble(Properties.get(Job)[24]), 0, Double.parseDouble(Properties.get(Job)[25]), 0, Integer.parseInt(Properties.get(Job)[26]), 0, Integer.parseInt(Properties.get(Job)[27]), 0, Integer.parseInt(Properties.get(Job)[28])) ;
		BattleSpecialAttribute silence = new BattleSpecialAttribute(Double.parseDouble(Properties.get(Job)[29]), 0, Double.parseDouble(Properties.get(Job)[30]), 0, Integer.parseInt(Properties.get(Job)[31])) ;
		LiveBeingStatus status = new LiveBeingStatus() ;

		return new BattleAttributes(phyAtk, magAtk, phyDef, magDef, dex, agi, crit, stun, block, blood, poison, silence, status) ;
	
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
    	int numberSpells = NumberOfSpellsPerJob[job] ;

    	for (int i = 0 ; i <= numberSpells - 1 ; i += 1)
		{
    		int spellID = CumNumberOfSpellsPerJob[job] + i ;
    		
			spells.add(Game.getAllSpells()[spellID]) ;
		}
    	
		spells.get(0).incLevel(1) ;
		
    }
	
	public String getLanguage() {return language ;}
	public String getSex() {return sex ;}
	public Directions getDir() {return dir ;}
	public Color getColor() {return color ;}
	public List<Quest> getQuests() {return quests ;}
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
	public BattleSpecialAttribute getStun() {return BA.getStun() ;}
	public BattleSpecialAttribute getBlock() {return BA.getBlock() ;}
	public BattleSpecialAttributeWithDamage getBlood() {return BA.getBlood() ;}
	public BattleSpecialAttributeWithDamage getPoison() {return BA.getPoison() ;}
	public BattleSpecialAttribute getSilence() {return BA.getSilence() ;}
	public double[] getCollect() {return collectLevel ;}
	public Integer getStoredGold() {return storedGold ;}
	public BasicAttribute getExp() {return PA.getExp() ;}
	public BasicAttribute getSatiation() {return PA.getSatiation() ;}
	public BasicAttribute getThirst() {return PA.getThirst() ;}
	public Map<QuestSkills, Boolean> getQuestSkills() {return questSkills ;}
	public AtkTypes getCurrentBattleAction() { return currentBattleAction ;}
	public Statistics getStats() {return stats ;}
	public int getAttPoints() {return attPoints ;}
	public AttributeBonus getAttIncrease() {return attIncrease ;}
	public AttributeBonus getChanceIncrease() {return attChanceIncrease ;}
	public SettingsWindow getSettings() {return settings ;}
	public SpellsTreeWindow getSpellsTreeWindow() {return spellsTree ;}
	public Creature getOpponent() { return opponent ;}
	public Item[] getHotItems() { return hotItems ;}
	public Statistics getStatistics() { return stats ;}
	public void setClosestCreature(Creature creature) { closestCreature = creature ;}
	public void setBattleAction(AtkTypes ba) { currentBattleAction = ba ;}
	public void setHotItem(Item item, int slot) { hotItems[slot] = item ;}
	
	public void addQuest(Quest newQuest) { quests.add(newQuest) ;}
	
	
	private Point feetPos() {return new Point(pos.x, (int) (pos.y - size.height)) ;}
	
	public static Spell[] getKnightSpells() { return Arrays.copyOf(Game.getAllSpells(), 14) ;}
	public static Spell[] getMageSpells() { return Arrays.copyOfRange(Game.getAllSpells(), 34, 49) ;}
	public static Spell[] getArcherSpells() { return Arrays.copyOfRange(Game.getAllSpells(), 70, 84) ;}
	public static Spell[] getAnimalSpells() { return Arrays.copyOfRange(Game.getAllSpells(), 105, 118) ;}
	public static Spell[] getThiefSpells() { return Arrays.copyOfRange(Game.getAllSpells(), 139, 152) ;}
	
	public void discoverCreature(CreatureType creatureType) { bestiary.addDiscoveredCreature(creatureType) ;}
	public void resetClosestCreature() { closestCreature = null ;}
	public void resetOpponent() { opponent = null ;}
	public void decSpellPoints() { spellPoints += -1 ;}
	
	public boolean isDoneMoving() { return stepCounter.finished() ;}
	public boolean weaponIsEquipped() { return (equips[0] != null) ;}
	public boolean arrowIsEquipped() { return (equips[3] != null) ;}
	private boolean actionIsAMove() { return Arrays.asList(MoveKeys).contains(currentAction) ;}
	public boolean isInBattle() { return state.equals(LiveBeingStates.fighting) ;}
	public boolean isCollecting() { return state.equals(LiveBeingStates.collecting) ;}
	public boolean shouldLevelUP() {return getExp().getMaxValue() <= getExp().getCurrentValue() ;}

	public static boolean setIsFormed(Equip[] EquipID)
	{
		if (EquipID[0] == null | EquipID[1] == null | EquipID[2] == null) { return false ;}
		
		return (EquipID[0].getId() + 1) == EquipID[1].getId() & (EquipID[1].getId() + 1) == EquipID[2].getId() ;
	}
	
	private void activateRide()
	{
		step = isRiding ? step / 2 : 2 * step ;
		isRiding = !isRiding ;
	}
	
	private void finishCollecting()
	{
		collectCounter.reset() ;
        collectingGif.flush() ;
        state = LiveBeingStates.idle ;
        
        // remove the collectible from the list
		if (map.isAField())
		{
			FieldMap fm = (FieldMap) map ;
			List<Collectible> collectibles = fm.getCollectibles() ;
			collectibles.remove(currentCollectible) ;
		}
		
        currentCollectible = null ;
	}
	
	public void collect(DrawingOnPanel DP)
    {
		collectCounter.inc() ;
        DP.DrawGif(collectingGif, getPos(), Align.center);
        
        if (collectCounter.finished())
        {
        	finishCollecting() ;
        }
    }
	
	public void resetAction() { currentAction = null ;}
	public void resetBattleAction() { currentBattleAction = null ;}
	public void decAttPoints(int amount) {attPoints += -amount ;}

	public void applyAdjacentGroundEffect()
	{
		if (isInside(GroundTypes.lava) & !elem[4].equals(Elements.fire))
		{
			PA.getLife().incCurrentValue(-5) ;
		}
		if (isTouching(GroundTypes.water))
		{
			PA.getThirst().incCurrentValue(1) ;
		}
	}
	
	private void startMove() { state = LiveBeingStates.moving ; stepCounter.reset() ;}

	public void move(Pet pet)
	{
		
		stepCounter.inc() ;
		
		Point newPos = CalcNewPos() ;
		
		if (!Game.getScreen().posIsInMap(newPos)) {	moveToNewMap() ; return ;}
		
		if (!map.groundIsWalkable(newPos, elem[4])) { return ;}
		
		setPos(newPos) ;
		
	}
	
	private void moveToNewMap()
	{
		
		int[] screenBorder = Game.getScreen().getBorders() ;
		Point currentPos = new Point(pos) ;
		Point newPos = new Point() ;
		int newMapID = -1 ;
		int[] mapConnections = map.getConnections() ;
		boolean leftSide = currentPos.x <= Game.getScreen().getSize().width / 2 ;
		boolean bottomSide = currentPos.y <= Game.getScreen().getSize().height / 2 ;

		switch (dir)
		{
			case up:
				newPos = new Point(currentPos.x, screenBorder[3] - step) ;
				newMapID = leftSide ? mapConnections[0] : mapConnections[7] ;
				
				break ;
			
			case left:
				newPos = new Point(screenBorder[2] - step, currentPos.y) ;
				newMapID = bottomSide ? mapConnections[1] : mapConnections[2] ;
				
				break ;
			
			case down:
				newPos = new Point(currentPos.x, screenBorder[1] + step) ;
				newMapID = leftSide ? mapConnections[3] : mapConnections[4] ;
				
				break ;
			
			case right:
				newPos = new Point(screenBorder[0] + step, currentPos.y) ;
				newMapID = bottomSide ? mapConnections[5] : mapConnections[6] ;
				
				break ;			
		}
		
		if (newMapID == -1) { return ;}
		
		GameMap newMap = Game.getMaps()[newMapID] ;
		setMap(newMap) ;
		setPos(newPos) ;
		if (newMap instanceof CityMap) { closestCreature = null ; opponent = null ;}
		
	}

//	private List<Quest> getActiveQuests() { return quests.stream().filter(quest -> quest.isActive()).toList() ;}	


	public Creature ClosestCreatureInRange()
	{			
		
		if (!map.isAField()) { return null ;}
		
		FieldMap fieldMap = (FieldMap) map ;
		if (!fieldMap.hasCreatures()) { return null ;}
		
		int NumberOfCreaturesInMap = fieldMap.getCreatures().size() ;
		double[] dist = new double[NumberOfCreaturesInMap] ;
		double MinDist = Game.getScreen().getSize().width + Game.getScreen().getSize().height ;
		for (int i = 0 ; i <= NumberOfCreaturesInMap - 1 ; ++i)
		{
			Creature creature = fieldMap.getCreatures().get(i) ;
			if (fieldMap.getCreatures().get(i) != null)
			{
				dist[i] = (double) new Point(pos.x, pos.y).distance(new Point(creature.getPos().x, creature.getPos().y)) ;				
				MinDist = Math.min(MinDist, dist[i]) ;
			}	
		}
		for (int i = 0 ; i <= NumberOfCreaturesInMap - 1 ; ++i)
		{
			Creature creature = fieldMap.getCreatures().get(i) ;
			if (dist[i] == MinDist & fieldMap.getCreatures() != null & dist[i] <= range)
			{
				return creature ;	// Closest creature ID
			}
		}
		
		return null ;
		
	}
	
	public void engageInFight(Creature Opponent)
	{
		opponent = Opponent ;
		opponent.setFollow(true) ;
		state = LiveBeingStates.fighting ;
	}
	
	// \*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/
		
	public void acts(Pet pet, Point mousePos, SideBar sideBar)
	{
		
		// I like to move it, move it!
		if (actionIsAMove())
		{
			switch (currentAction)
			{
				case "Acima": case "W": setDir(Directions.up) ; break ;
				case "Abaixo": case "S": setDir(Directions.down) ; break ;
				case "Esquerda": case "A": setDir(Directions.left) ; break ;
				case "Direita": case "D": setDir(Directions.right) ; break ;
			}
			
			if (focusWindow != null)
			{
				if (!focusWindow.isOpen())
				{
					startMove() ;
				}
			}
			else
			{
				startMove() ;
			}
			
		}
		
		
		// clicking icons
		if (currentAction.equals("MouseLeftClick"))
		{
			sideBar.getIcons().forEach(icon ->
			{
				if (icon.ishovered(mousePos))
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
		}
		
		
		// doing keyboard actions
		if (currentAction.equals(ActionKeys[4]))
		{
			focusWindow = bag ;
			bag.open() ;
		}
		if (currentAction.equals(ActionKeys[5]))
		{
			focusWindow = attWindow ;
			((PlayerAttributesWindow) attWindow).updateAttIncButtons(this) ;
			attWindow.open() ;
		}
		if (currentAction.equals(ActionKeys[6]))
		{
			if (bag.contains(Game.getAllItems()[1340]) & (isInside(GroundTypes.water) | isTouching(GroundTypes.water)))
			{
				Point offset = new Point() ;
				offset.x =  dir.equals(Directions.left) ? -size.width : dir.equals(Directions.right) ? size.width : 0 ;
				offset.y =  dir.equals(Directions.up) ? -size.height : dir.equals(Directions.down) ? size.height : 0 ;
				Point gifPos = UtilG.Translate(pos, offset.x, offset.y) ;
				// TODO fishing gif
			}
		}
		// TODO add dig
		if (currentAction.equals(ActionKeys[7]))	// Map  & questSkills.get(QuestSkills.getContinentMap(map.getContinentName(this).name()))
		{
			focusWindow = mapWindow ;
			mapWindow.open() ;
		}
		if (currentAction.equals(ActionKeys[8]) & pet != null)
		{
			focusWindow = pet.getAttWindow() ;
			pet.getAttWindow().open() ;
		}
		if (currentAction.equals(ActionKeys[9]))
		{
			focusWindow = questWindow ;
			questWindow.open() ;
		}
		if (currentAction.equals(ActionKeys[10]))
		{			
			focusWindow = hintsWindow ;
			hintsWindow.open() ;
		}
		if (currentAction.equals(ActionKeys[11]) & questSkills.get(QuestSkills.ride))
		{
			activateRide() ;
		}
		if (currentAction.equals(ActionKeys[12]) & !isInBattle())
		{
			PA.getLife().setToMaximum() ;
			TentGif.start() ;
		}
		if (currentAction.equals(ActionKeys[13]) & questSkills.get(QuestSkills.bestiary))
		{
			focusWindow = bestiary ;
			bestiary.open() ;
		}
		
		
		// using spells
		if (actionIsSpell())
		{
			Spell spell = spells.get(SpellKeys.indexOf(currentAction));
			
			if (spell.getType().equals(SpellTypes.support))
			{
				if (spell.isReady() & hasEnoughMP(spell) & 0 < spell.getLevel())
				{
					useSupportSpell(pet, spell) ;
					train(new AtkResults(AtkTypes.magical, null, 0)) ;
					stats.incNumberMagAtk() ;
				}
			}
			else if (closestCreature != null & !isInBattle())
			{
				engageInFight(closestCreature) ;
			}
		}
		
		
		// if hits creature, enters battle
		if (actionIsAtk() & closestCreature != null & !isInBattle())
		{
			engageInFight(closestCreature) ;
		}
		
		if (bag.isOpen())
		{
			if (bag.getTab() == 1 & (currentAction.equals("Enter") | currentAction.equals("MouseLeftClick")))
			{
				useItem(bag.getSelectedItem()) ;
			}
		}
		
		if (attWindow.isOpen())
		{
			((PlayerAttributesWindow) attWindow).act(this, mousePos, currentAction) ;
		}

		
		// navigating through open windows
		if (focusWindow != null)
		{
			if (focusWindow.isOpen())
			{
				focusWindow.navigate(currentAction) ;
			}
		}
		
		
		// using hotItems
		for (int i = 0; i <= HotKeys.length - 1 ; i += 1)
		{
			if (currentAction.equals(HotKeys[i]) & hotItems[i] != null)
			{
				useItem(hotItems[i]) ;
			}
		}
		
		
		updateCombo() ;
		
	}
	
	public void meet(Point mousePos, DrawingOnPanel DP)
	{
		if (state == LiveBeingStates.collecting) { return ;}
		
		if (map.isAField())
		{
			FieldMap fm = (FieldMap) map ;
			
			// meeting with collectibles
			List<Collectible> collectibles = fm.getCollectibles() ;
			int numberCollectibles = collectibles.size() ;
			for (int i = 0 ; i <= numberCollectibles - 1 ; i += 1)
			{
				
				if (collectibles.size() - 1 < i) { break ;}
				
				Collectible collectible = collectibles.get(i) ;
				if (!isInCloseRange(collectible.getPos())) { continue ;}
				
				setState(LiveBeingStates.collecting);
				currentCollectible = collectible ;
				break ;		
				
			}
			
//			collectibles.forEach(collectible -> {
//				double distx = Math.abs(pos.x - collectible.getPos().x) ;
//				double disty = Math.abs(pos.y - collectible.getPos().y) ;
//				if (distx <= 0.5*size.width & disty <= 0.5*size.height)
//				{
//					setState(LiveBeingStates.collecting);
//					collect(collectible, DP) ;
//				}
//			});

			// meeting with chests
			// TODO meet with chests
//			String groundType = currentMap.groundTypeAtPoint(pos) ;
//			if (groundType != null)
//			{
//				if (groundType.contains("Chest"))
//				{
//					//return new int[] {3, Integer.parseInt(groundType.substring(groundType.length() - 1))} ;
//				}
//			}
			
			// meeting with creatures
			if (!isInBattle())
			{
				ArrayList<Creature> creaturesInMap = fm.getCreatures() ;
				for (Creature creature : creaturesInMap)
				{
					double distx = UtilG.dist1D(pos.x, creature.getPos().x) ;
					double disty = UtilG.dist1D(pos.y - size.height / 2, creature.getPos().y) ;
					if (distx <= (size.width + creature.getSize().width) / 2 & disty <= (size.height + creature.getSize().height) / 2) //  & !ani.isActive(10) & !ani.isActive(19)
					{
//						opponent = creature ;
//						opponent.setFollow(true) ;
//						setState(LiveBeingStates.fighting) ;
//						bestiary.addDiscoveredCreature(opponent.getType()) ;
					}
				}
			}
		}	
		
		// meeting with NPCs
		if (map.getNPCs() != null)
		{
			for (NPCs npc : map.getNPCs())
			{
				boolean metNPC = UtilG.isInside(this.getPos(), UtilG.getPosAt(npc.getPos(), Align.topLeft, this.getSize()), this.getSize()) ;
				
				if (!metNPC) { continue ;}
//				System.out.println(npc.getType().getName());
				npc.action(this, Game.getPet(), mousePos, DP) ;
				
				break ;
			}
		}
		
		if (map.getBuildings() == null) { return ;}
		
		for (Building building : map.getBuildings())
		{
			for (NPCs npc : building.getNPCs())
			{
				boolean metNPC = UtilG.isInside(this.getPos(), UtilG.getPosAt(npc.getPos(), Align.topLeft, this.getSize()), this.getSize()) ;
				
				if (!metNPC) { continue ;}
				
				npc.action(this, Game.getPet(), mousePos, DP) ;
				
				break ;
			}
		}
	}
		
	
	public void useItem(Item item)
	{
		if (item == null) { return ;}

		System.out.println("player used " + item.getName());
		if (item instanceof Potion)
		{
			Potion pot = (Potion) item ;
			double powerMult = job == 3 ? 1.06 * spells.get(7).getLevel() : 1 ;
			
			pot.use(this, powerMult) ;
			bag.Remove(pot, 1) ;
			
			return ;			
		}
		if (item instanceof Alchemy)
		{
			Alchemy alch = (Alchemy) item ;
			double powerMult = job == 3 ? 1.06 * spells.get(7).getLevel() : 1 ;
			
			alch.use(this, powerMult) ;
			bag.Remove(alch, 1) ;
			
			return ;			
		}
		if (item instanceof Forge)
		{
			return ;
		}
		if (item instanceof Food)
		{
			Food food = (Food) item ;
			
			food.use(this) ;
			bag.Remove(food, 1);
		}
		if (item instanceof PetItem)
		{
//			PetItem petItem = (PetItem) item ;
			
//			petItem.use(pet) ;
			
			return ;			
		}
		if (item instanceof Arrow)
		{
			return ;
		}
		if (item instanceof Equip)
		{
//			Equip(((Equip) item).getId()) ;
			Equip equip = (Equip) item ;
			
			equip.use(this) ;
			
			return ;			
		}
		if (item instanceof GeneralItem)
		{
			GeneralItem genItem = (GeneralItem) item ;
			
			genItem.use(this) ;
			
			return ;
		}
		if (item instanceof Fab)
		{
			return ;
		}
		if (item instanceof QuestItem)
		{
			return ;
		}
	}
	
	
	public void supSpellCounters()
	{
		for (Spell spell : spells)
		{
			if (spell.isActive())
			{
				spell.getDurationCounter().inc() ;
			}
			if (spell.getDurationCounter().finished())
			{
				spell.getDurationCounter().reset() ;
				spell.applyBuffs(false, this) ;
				spell.deactivate() ;
			}
			spell.getCooldownCounter().inc() ;
		}
	}
			
	
	// called every time the window is repainted
	public void showWindows(Pet pet, CreatureType[] creatureTypes, GameMap[] maps, Battle B, Point MousePos, DrawingOnPanel DP)
	{
		if (bag.isOpen())
		{
			bag.display(MousePos, Game.allText.get("Menus da mochila"), DP) ;
		}
		if (attWindow.isOpen())
		{
			((PlayerAttributesWindow) attWindow).display(this, equips, MousePos, DP);
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
				((PetAttributesWindow) pet.getAttWindow()).display(pet, DP);
			}
		}
		if (mapWindow.isOpen())
		{
			mapWindow.display(map, DP) ;
		}		
		if (questWindow.isOpen())
		{
			questWindow.display(quests, bag, DP) ;
		}
		if (bestiary.isOpen())
		{
			bestiary.display(this, MousePos, DP) ;
		}
		if (settings.isOpen())
		{
			settings.display(DP) ;
		}
		if (hintsWindow.isOpen())
		{
			hintsWindow.display(this, DP) ;
		}
	}
		
	
	// battle functions
	public void spendArrow()
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
	private int StealItem(Creature creature, Item[] items, int spellLevel)
	{	
		// TODO steal item
//		int ID = (int)(UtilG.ArrayWithValuesGreaterThan(creature.getBag(), -1).length*Math.random() - 0.01) ;
//		int StolenItemID = -1 ;
//		if(-1 < creature.getBag()[ID])
//		{
//			if(Math.random() <= 0.01*items[creature.getBag()[ID]].getDropChance() + 0.01*spellLevel)
//			{
//				//Bag[creature.getBag()[ID]] += 1 ;
//				StolenItemID = items[creature.getBag()[ID]].getID() ;
//			}
//		}
//		return StolenItemID ;
		
		return 0 ;
	}
	private void useSupportSpell(Pet pet, Spell spell)
	{	
		
		spell.getCooldownCounter().reset();
		spell.activate() ;		
		resetBattleActions() ;
		PA.getMp().incCurrentValue(-spell.getMpCost()) ;

		// TODO support spells 
		int spellIndex = spells.indexOf(spell) ;
		if (job == 0)
		{
			if (spellIndex == 4)
			{
				// eternal life
			}
			if (proJob == 1 & spellIndex == 20)
			{
				// nobility
			}
			if (proJob == 2 & spellIndex == 20)
			{
				// spiky
			}
			if (proJob == 2 & spellIndex == 22)
			{
				// human shield
			}
			if (proJob == 2 & spellIndex == 23)
			{
				// salvation
			}
		}
		if (job == 1)
		{
			if (spellIndex == 9)
			{
				// swamp
			}
			if (spellIndex == 10)
			{
				// cure
				getLife().incCurrentValue(5 * spell.getLevel()) ;
			}
			if (spellIndex == 11)
			{
				// mystical inspiration
			}
		}
		
		spell.applyBuffs(true, this) ;
		
		if (opponent == null) { return ;}
		
		spell.applyNerfs(true, opponent) ;
		
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
	public void win(Creature creature, Animations winAnimation)
	{		
		List<String> GetItemsObtained = new ArrayList<>() ;
		for (Item item : creature.getBag())
		{
			if (Math.random() <= 0.01 * item.getDropChance())
			{
				GetItemsObtained.add(item.getName()) ;
				bag.Add(item, 1) ;				
			}
		}		
		
		bag.addGold((int) (creature.getGold() * UtilG.RandomMult(0.1 * goldMultiplier))) ;
		PA.getExp().incCurrentValue((int) (creature.getExp().getCurrentValue() * PA.getExp().getMultiplier())) ;
		
		for (Quest quest : quests)
		{
			quest.IncReqCreaturesCounter(creature.getType()) ;
			quest.checkCompletion(bag) ;
		}
		
		String[] ItemsObtained = GetItemsObtained.toArray(new String[] {}) ;
		winAnimation.start(new Object[] {300, ItemsObtained}) ;
	}
	public void levelUp(Animations ani)
	{
		double[] attIncrease = calcAttributesIncrease() ;
		setLevel(level + 1) ;
		PA.getLife().incMaxValue((int) attIncrease[0]) ;
		PA.getMp().incMaxValue((int) attIncrease[1]); ;	
		BA.getPhyAtk().incBaseValue(attIncrease[2]) ;
		BA.getMagAtk().incBaseValue(attIncrease[3]) ;
		BA.getPhyDef().incBaseValue(attIncrease[4]) ;
		BA.getMagDef().incBaseValue(attIncrease[5]) ;
		BA.getAgi().incBaseValue(attIncrease[6]) ;
		BA.getDex().incBaseValue(attIncrease[7]) ;
		PA.getExp().incMaxValue((int) attIncrease[8]); ;
		PA.getLife().setToMaximum() ;
		PA.getMp().setToMaximum() ;
		spellPoints += 1 ;
		attPoints += 2 ;
		
		((PlayerAttributesWindow) attWindow).activateIncAttButtons(attPoints) ;
		ani.start(new Object[] {600, Arrays.copyOf(attIncrease, attIncrease.length - 1), level, pos}) ;
	}
	private double[] calcAttributesIncrease()
	{
		double[] attributeIncrease = attIncrease.basic() ;
		double[] chanceIncrease = attChanceIncrease.basic() ;
		double[] increase = new double[attributeIncrease.length + 1] ;

		for (int i = 0 ; i <= attributeIncrease.length - 1 ; i += 1)
		{
			if (chanceIncrease[i] <= Math.random()) { continue ;}
			
			increase[i] = attributeIncrease[i] ;
		}
		
		increase[attributeIncrease.length] = (double) (10 * (3 * Math.pow(level - 1, 2) + 3 * (level - 1) + 1) - 5) ;
		
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
		bag.addGold((int) (-0.2 * bag.getGold())) ;
		PA.getLife().setToMaximum(); ;
		PA.getMp().setToMaximum(); ;
		PA.getSatiation().setToMaximum() ;
		PA.getThirst().setToMaximum() ;
		BA.resetStatus() ;
		state = LiveBeingStates.idle ;
		resetPosition() ;
	}
	public AtkResults useSpell(Spell spell, LiveBeing receiver)
	{
		PA.getMp().incCurrentValue(-spell.getMpCost()) ;
		spell.activate() ;
		
		int spellLevel = spell.getLevel() ;
		int spellID = spells.indexOf(spell) ;
		int damage = -1 ;
		AttackEffects effect = null ;
		double PhyAtk = BA.TotalPhyAtk() ;
		double MagAtk = BA.TotalMagAtk() ;
		double PhyDef = receiver.getBA().TotalPhyDef() ;
		double MagDef = receiver.getBA().TotalMagDef() ;
		double AtkDex = BA.TotalDex() ;
		double DefAgi = receiver.getBA().TotalAgi() ;
		double AtkCrit = BA.TotalCritAtkChance() ;
		double DefCrit = receiver.getBA().TotalCritDefChance() ;
		double receiverElemMod = 1 ;
		double[] AtkMod = new double[] {spell.getAtkMod()[0] * spellLevel, 1 + spell.getAtkMod()[1] * spellLevel} ;
		double[] DefMod = new double[] {spell.getDefMod()[0] * spellLevel, 1 + spell.getDefMod()[1] * spellLevel} ;
		double[] DexMod = new double[] {spell.getDexMod()[0] * spellLevel, 1 + spell.getDexMod()[1] * spellLevel} ;
		double[] AgiMod = new double[] {spell.getAgiMod()[0] * spellLevel, 1 + spell.getAgiMod()[1] * spellLevel} ;
		double AtkCritMod = spell.getAtkCritMod()[0] * spellLevel ;	// Critical atk modifier
		double DefCritMod = spell.getDefCritMod()[0] * spellLevel ;	// Critical def modifier
		double BlockDef = receiver.getBA().getStatus().getBlock() ;
		double BasicAtk = 0 ;
		double BasicDef = 0 ;
		Elements[] AtkElem = new Elements[] {spell.getElem(), elem[1], elem[4]} ;
		Elements[] DefElem = receiver.defElems() ;
		
		switch (job)
		{
			case 0:
			{
				BasicAtk = PhyAtk ;
				BasicDef = PhyDef ;
				
				break ;
			}
			case 1:
			{
				BasicAtk = MagAtk ;
				BasicDef = MagDef ;
				
				break ;
			}
			case 2:
			{
				double arrowAtk = arrowIsEquipped() ? Arrow.getAll()[equips[3].getId()].getAtkPower() : 0 ;
				if (spellID == 0 | spellID == 3 | spellID == 6 | spellID == 9 | spellID == 12)
				{
					BasicAtk = PhyAtk + arrowAtk ;
					BasicDef = PhyDef ;
				}
				if (spellID == 2 | spellID == 5 | spellID == 11)
				{
					BasicAtk = (double) ((PhyAtk + MagAtk) / 2.0 + arrowAtk) ;
					BasicDef = (double) ((PhyDef + MagDef) / 2.0) ;
				}
				if (spellID == 14)
				{
					BasicAtk = MagAtk + arrowAtk ;
					BasicDef = MagDef ;
				}
				
				break ;
			}
			case 3:
			{
				BasicAtk = PhyAtk ;
				BasicDef = PhyDef ;
				
				break ;
			}
			case 4:
			{
				BasicAtk = PhyAtk ;
				BasicDef = PhyDef ;
				
				break ;
			}
		}
		
		effect = Battle.calcEffect(DexMod[0] + AtkDex*DexMod[1], AgiMod[0] + DefAgi*AgiMod[1], AtkCrit + AtkCritMod, DefCrit + DefCritMod, BlockDef) ;
		damage = Battle.calcDamage(effect, AtkMod[0] + BasicAtk*AtkMod[1], DefMod[0] + BasicDef*DefMod[1], AtkElem, DefElem, receiverElemMod) ;
		
		spell.applyBuffsAndNerfs("activate", receiver) ;
		
		return new AtkResults(AtkTypes.magical, effect, damage) ;
		
	}
	
	private void ItemEffect(int ItemID)
	{
		if (ItemID == 1381 | ItemID == 1382 | ItemID == 1384)
		{
			BA.getStatus().setBlood(0) ;
		}
		if (ItemID == 1411)
		{
			BA.getStatus().setSilence(0) ;
		}
	}
	
	private void drawRange(DrawingOnPanel DP)
	{
		DP.DrawCircle(pos, (int)(2 * range), 2, null, Game.ColorPalette[job]) ;
	}
	private void drawEquips(Point pos, int job, int type, Scale scale, double angle, DrawingOnPanel DP)
	{
		// TODO add shining equips for bonus 10
		switch (type)
		{
			case 0: DP.DrawImage(Equip.SwordImage, pos, angle, scale, Align.center) ; return ;
			case 1: DP.DrawImage(Equip.ShieldImage, pos, angle, scale, Align.center) ; return ;
			case 2: DP.DrawImage(Equip.ArmorImage, pos, angle, scale, Align.center) ; return ;
			case 3: DP.DrawImage(Equip.ArrowImage, pos, angle, scale, Align.center)  ; return ;
		}
	}
	public void drawWeapon(Point Pos, double[] playerscale, DrawingOnPanel DP)
	{
		Scale scale = new Scale(0.6, 0.6) ;
		double[] angle = new double[] {50, 30, 0, 0, 0} ;
		Point EqPos = new Point((int)(Pos.x + 0.16*size.width*playerscale[0]), (int)(Pos.y - 0.4*size.height*playerscale[1])) ;
		if (getEquips()[0] != null)
		{
			drawEquips(EqPos, job, 0, scale, angle[job], DP) ;
		}	
	}
	public void drawTimeBar(Creature creature, DrawingOnPanel DP)
	{
		String relPos = UtilS.RelPos(pos, creature.getPos()) ;
		DrawTimeBar(relPos, Game.ColorPalette[9], DP) ;
	}
	public void display(Point pos, Scale scale, Directions direction, boolean showRange, DrawingOnPanel DP)
	{
		double angle = DrawingOnPanel.stdAngle ;
		if (isRiding)
		{
			Point ridePos = UtilG.Translate(pos, -RidingImage.getWidth(null) / 2, RidingImage.getHeight(null) / 2) ;
			DP.DrawImage(RidingImage, ridePos, angle, scale, Align.bottomLeft) ;
		}
		
		movingAni.display(direction, pos, angle, scale, DP) ;
		
		if (questSkills.get(QuestSkills.dragonAura))
		{
			DP.DrawImage(DragonAuraImage, feetPos(), angle, scale, Align.center) ;					
		}
		if (showRange)
		{
			drawRange(DP) ;
		}

		BA.getStatus().display(UtilG.Translate(pos, 0, -size.height), dir, DP);
	}

	
	// Save and load methods
	public void save(int slot)
	{
		
		try (FileWriter file = new FileWriter("save " + slot + ".json"))
        {
            file.write("name: " + name + "\n"); 
            file.write("level: " + level); 
            file.flush();
    		// TODO
 
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
		
//		try
//		{	
//			FileWriter fileWriter = new FileWriter(filePath) ;
//			BufferedWriter bw = new BufferedWriter(fileWriter) ; 
//			bw.write("Save version: 3.41 \n" + getName()) ;
//			bw.write("\nPlayer name: \n" + getName()) ;
//			bw.write("\nPlayer language: \n" + getLanguage()) ;
//			bw.write("\nPlayer sex: \n" + getSex()) ;
//			bw.write("\nPlayer size: \n" + getSize()) ;
//			bw.write("\nPlayer colors: \n" + getColor()) ;
//			bw.write("\nPlayer job: \n" + getJob()) ;
//			//bw.write("\nPlayer PA.ProJob: \n" + PA.ProJob) ;
//			bw.write("\nPlayer continent: \n" + getContinent()) ;
//			bw.write("\nPlayer map: \n" + getMap()) ;
//			bw.write("\nPlayer pos: \n" + getPos()) ;
//			//bw.write("\nPlayer skill: \n" + Arrays.toString(getSpell())) ;
//			//bw.write("\nPlayer quest: \n" + Arrays.toString(getQuest())) ;
//			//bw.write("\nPlayer bag: \n" + Arrays.toString(getBag())) ;
//			bw.write("\nPlayer equips: \n" + Arrays.toString(getEquips())) ;
//			bw.write("\nPlayer skillPoints: \n" + getSpellPoints()) ;
//			//bw.write("\nPlayer life: \n" + Arrays.toString(getLife())) ;
//			//bw.write("\nPlayer mp: \n" + Arrays.toString(getMp())) ;
//			bw.write("\nPlayer range: \n" + getRange()) ;
//			//bw.write("\nPlayer phyAtk: \n" + Arrays.toString(getPhyAtk())) ;
//			//bw.write("\nPlayer magAtk: \n" + Arrays.toString(getMagAtk())) ;
//			//bw.write("\nPlayer phyDef: \n" + Arrays.toString(getPhyDef())) ;
//			//bw.write("\nPlayer magDef: \n" + Arrays.toString(getMagDef())) ;
//			//bw.write("\nPlayer dex: \n" + Arrays.toString(getDex())) ;
//			//bw.write("\nPlayer agi: \n" + Arrays.toString(getAgi())) ;
//			bw.write("\nPlayer crit: \n" + Arrays.toString(getCrit())) ;
////			bw.write("\nPlayer stun: \n" + Arrays.toString(getStun())) ;
////			bw.write("\nPlayer block: \n" + Arrays.toString(getBlock())) ;
////			bw.write("\nPlayer blood: \n" + Arrays.toString(getBlood())) ;
////			bw.write("\nPlayer poison: \n" + Arrays.toString(getPoison())) ;
////			bw.write("\nPlayer silence: \n" + Arrays.toString(getSilence())) ;
//			bw.write("\nPlayer elem: \n" + Arrays.toString(getElem())) ;
//			//bw.write("\nPlayer elem mult: \n" + Arrays.toString(getElemMult())) ;
//			bw.write("\nPlayer collect: \n" + Arrays.toString(getCollect())) ;
//			bw.write("\nPlayer level: \n" + getLevel()) ;
////			bw.write("\nPlayer gold: \n" + Arrays.toString(getGold())) ;
//			bw.write("\nPlayer step: \n" + getStep()) ;
//			//bw.write("\nPlayer exp: \n" + Arrays.toString(getExp())) ;
//			//bw.write("\nPlayer satiation: \n" + Arrays.toString(getSatiation())) ;
//			//bw.write("\nPlayer quest skills: \n" + Arrays.toString(questSkills)) ;
////			bw.write("\nPlayer status: \n" + Arrays.toString(BA.getSpecialStatus())) ; 
//			//bw.write("\nPlayer actions: \n" + Arrays.deepToString(getActions())) ; 
//			//bw.write("\nPlayer battle actions: \n" + Arrays.deepToString(BA.getBattleActions())) ; 
//			//bw.write("\nPlayer status counter: \n" + Arrays.toString(getStatusCounter())) ; 		
////			bw.write("\nPlayer stats: \n" + Arrays.toString(getStats())) ;
//			bw.write("\nPlayer available attribute points: \n" + getAttPoints()) ;
////			bw.write("\nPlayer attribute increase: \n" + Arrays.deepToString(getAttIncrease())) ;
////			bw.write("\nPlayer chance increase: \n" + Arrays.deepToString(getChanceIncrease())) ;
//			//bw.write("\nPlayer creatures discovered: \n" + Arrays.toString(getCreaturesDiscovered())) ;
//			pet.Save(bw) ;	
//			
////			bw.write("\nEquips bonus: \n" + Arrays.deepToString(Items.EquipsBonus)) ;
//			//bufferedWriter.write("\nNPCs contact: \n" + Arrays.toString(FirstNPCContact)) ;
//			bw.write("\nDifficult level: \n" + Game.difficultLevel) ;
//			bw.close() ;
//		}		
//		catch(IOException ex) 
//		{
//            System.out.println("Error writing to file '" + filePath + "'") ;
//        }
	}
	private static Player load(String filePath)
	{
		
		// TODO
		JSONObject jsonData = UtilG.readJsonObject(filePath) ;
		Player newPlayer = new Player((String) jsonData.get("name"), filePath, filePath, 0) ;
		newPlayer.setLevel((int) jsonData.get("level"));
		
		return newPlayer ;
		
	}
	
	
	

	
	
//	private void addSpellPoint(int amount) {spellPoints += amount ;}
//	private void incRange(double incR) {PA.setRange(PA.getRange() + incR) ;}
//	private void incAttPoints(int amount) {attPoints += amount ;}
	
//	private void DrawPlayerEquips(int[] Pos, double[] playerscale, DrawingOnPanel DP)
//	{
//		Scale scale = new Scale(0.6, 0.6) ;
//		double[] angle = new double[] {50, 30, 0, 0, 0} ;
//		Point EqPos = new Point((int)(Pos[0] + 0.16 * size.width * playerscale[0]), (int)(Pos[1] - 0.4 * size.height * playerscale[1])) ;
//		if (equips[0] != null)
//		{
//			DrawEquips(EqPos, job, 0, scale, angle[job], DP) ;
//		}	
		
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
//	}
	// Action windows
//	private void AttWindow(GameIcon[] icons, int[] MainWinDim, double[] PlayerAttributeIncrease, Point MousePos, Image GoldCoinImage, DrawingOnPanel DP)
//	{
//		//int WindowLimit = 2 ;
//		//SelectedWindow[0] = UtilS.MenuSelection(Player.ActionKeys[0], Player.ActionKeys[2], action, SelectedWindow[0], WindowLimit) ;
//		for (int selectedItem = 0 ; selectedItem <= 7 - 1 ; selectedItem += 1)
//		{
//			//if (icons[i].ishovered(MousePos) & (currentAction.equals("Enter") | currentAction.equals("MouseLeftClick")) & 0 < attPoints)
//			//{
//				if (selectedItem == 0)
//				{
//					PA.getLife().incCurrentValue((int) PlayerAttributeIncrease[selectedItem]); ;
//					PA.getLife().incMaxValue((int) PlayerAttributeIncrease[selectedItem]); ;
//				}
//				else if (selectedItem == 1)
//				{
//					PA.getMp().incCurrentValue((int) PlayerAttributeIncrease[selectedItem]); ;
//					PA.getMp().incMaxValue((int) PlayerAttributeIncrease[selectedItem]); ;
//				}
//				else if (selectedItem == 2)
//				{
//					BA.getPhyAtk().incBaseValue(PlayerAttributeIncrease[selectedItem]) ;
//				}
//				else if (selectedItem == 3)
//				{
//					BA.getMagAtk().incBaseValue(PlayerAttributeIncrease[selectedItem]) ;
//				}
//				else if (selectedItem == 4)
//				{
//					BA.getPhyDef().incBaseValue(PlayerAttributeIncrease[selectedItem]) ;
//				}
//				else if (selectedItem == 5)
//				{
//					BA.getMagDef().incBaseValue(PlayerAttributeIncrease[selectedItem]) ;
//				}
//				else if (selectedItem == 6)
//				{
//					BA.getDex().incBaseValue(PlayerAttributeIncrease[selectedItem]) ;
//				}
//				else if (selectedItem == 7)
//				{
//					BA.getAgi().incBaseValue(PlayerAttributeIncrease[selectedItem]) ;
//				}
//				attPoints += -1 ;
//			//}
//		}
//		//Point WinPos = new Point((int) (0.3 * MainWinDim[0]), (int) (0.2 * MainWinDim[1])) ;
//		//DrawAttWindow(MainWinDim, WinPos, MousePos, AllText, AllTextCat, SelectedWindow[0], GoldCoinImage, icons, DP) ;
//
//		attWindow.display(this, allText, equips, MousePos, DP) ;
//	}
//	private void Equip(int EquipID)
//	{
//		int NumberOfEquipTypes = 3 ;	// Sword/Staff/Bow/Claws/Dagger, shield, armor/robe (Archers have bow, bandana, and armor)
//		int EquipType = (EquipID + job) % NumberOfEquipTypes ;
//		Equip currentEquip = equips[EquipType] ;
//		//if (0 < Bag[EquipID])
//		//{
//			if (currentEquip != null)	// Unnequip the current equip
//			{
//				if (SetIsFormed(equips))	// if the set was formed, remove the 20% bonus
//				{
//					ApplyEquipsBonus(equips[0], (double)-0.2) ;
//					ApplyEquipsBonus(equips[1], (double)-0.2) ;
//					ApplyEquipsBonus(equips[2], (double)-0.2) ;
//				}
//				equips[EquipType] = null ;
//				elem[EquipType + 1] = Elements.neutral ;
//				ApplyEquipsBonus(currentEquip, -1) ;
//			}
//			
//			if (currentEquip == null)
//			{
////				if (equips[(EquipID + job) % NumberOfEquipTypes].getId() == 0)	// Equip
////				{
////					
////				}
//				equips[EquipType] = Equip.getAll()[EquipID] ;
//				elem[EquipType + 1] = equips[EquipType].getElem() ;
//				ApplyEquipsBonus(equips[EquipType], 1) ;
//				if (SetIsFormed(equips))	// if the set is formed, add the 20% bonus
//				{
//					ApplyEquipsBonus(equips[0], (double)0.2) ;
//					ApplyEquipsBonus(equips[1], (double)0.2) ;
//					ApplyEquipsBonus(equips[2], (double)0.2) ;
//				}
//			}
////			else
////			{
////				if (currentEquip.getId() != EquipID)
////				{
////					
////				}
////			}
//			
//		//}
//			
//		elem[4] = hasSuperElement() ? elem[1] : Elements.neutral ;
//	}
	// Drawing methods
//	private void DrawStats(String[][] AllText, int[] AllTextCat, Point Pos, int L, int H, double[] PlayerStats, DrawingOnPanel DP)
//	{
//		Font font = new Font("SansSerif", Font.BOLD, L / 28) ;
//		double OverallAngle = DrawingOnPanel.stdAngle ;
//		int TextCat = AllTextCat[7] ;
//		Point TextPos = new Point((int) (Pos.x + 5 + 0.05*L), (int) (Pos.y + 0.05*H)) ;
//		for (int i = 0 ; i <= PlayerStats.length - 1 ; i += 1)
//		{
//			DP.DrawText(TextPos, Align.bottomLeft, OverallAngle, AllText[TextCat][i + 1] + " " + String.valueOf(UtilG.Round(PlayerStats[i], 1)), font, Game.ColorPalette[5]) ;
//			TextPos.y += 0.95*H/PlayerStats.length ;
//		}
//	}
	//	public void ApplyBuffsAndNerfs(String action, String type, int att, Buff buff, int spellID, boolean spellIsActive)
//	{
//		int ActionMult = 1 ;
//		double[][] Buff = new double[14][5] ;	// [PA.getLife(), PA.getMp(), PhyAtk, MagAtk, Phy def, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence][effect]
//		double[] OriginalValue = new double[14] ;	// [PA.getLife(), PA.getMp(), PhyAtk, MagAtk, Phy def, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence]
//		double[][] Buffs = null ;
//		List<Buff> buffs = spells.get(spellID).getBuffs() ;
//		System.out.println("buffs = " + buffs);
//		int BuffNerfLevel = spells.get(spellID).getLevel() ;
//		if (type.equals("buffs"))
//		{
//			//Buffs = spells.get(spellID).getBuffs() ;
//		}
////		else if (type.equals("nerfs"))
////		{
////			Buffs = spells.get(spellID).getNerfs() ;
////		}
//		OriginalValue = new double[] {PA.getLife().getMaxValue(), PA.getMp().getMaxValue(), BA.getPhyAtk().getBaseValue(), BA.getMagAtk().getBaseValue(),
//				BA.getPhyDef().getBaseValue(), BA.getMagDef().getBaseValue(), BA.getDex().getBaseValue(), BA.getAgi().getBaseValue(),
//				BA.getCrit()[0],
//				BA.getStun().getBasicAtkChance(),
//				BA.getBlock().getBasicAtkChance(),
//				BA.getBlood().getBasicAtkChance(), BA.getBlood().getBasicDefChance(), BA.getBlood().getBasicAtk(), BA.getBlood().getBasicDef(),
//				BA.getPoison().getBasicAtkChance(), BA.getPoison().getBasicDefChance(),
//				BA.getPoison().getBasicAtk(), BA.getPoison().getBasicDef(),
//				BA.getSilence().getBasicAtkChance()} ;
//		if (action.equals("deactivate"))
//		{
//			ActionMult = -1 ;
//		}
//		
//		if (!spellIsActive)
//		{
//			int level = 1 ;
//			double increment = PA.getLife().getMaxValue() * buff.getPercentIncrease().get(Attributes.life)
//					+ buff.getValueIncrease().get(Attributes.life) ;
//			PA.getLife().incCurrentValue((int) increment * level * ActionMult);
//		}
////		if (att == 11 | att == 12)
////		{
////			if (action.equals("deactivate"))
////			{
////				Buff[att][0] += (OriginalValue[att]*Buffs[att][0] + Buffs[att][1])*BuffNerfLevel*ActionMult ;
////				Buff[att][1] += (OriginalValue[att]*Buffs[att][3] + Buffs[att][4])*BuffNerfLevel*ActionMult ;
////				Buff[att][2] += (OriginalValue[att]*Buffs[att][6] + Buffs[att][7])*BuffNerfLevel*ActionMult ;
////				Buff[att][3] += (OriginalValue[att]*Buffs[att][9] + Buffs[att][10])*BuffNerfLevel*ActionMult ;
////			}
////			else
////			{
////				if (Math.random() <= Buffs[att][2])
////				{
////					Buff[att][1] += (OriginalValue[att]*Buffs[att][0] + Buffs[att][1])*BuffNerfLevel*ActionMult ;
////				}
////				if (Math.random() <= Buffs[att][5])
////				{
////					Buff[att][1] += (OriginalValue[att]*Buffs[att][3] + Buffs[att][4])*BuffNerfLevel*ActionMult ;
////				}
////				if (Math.random() <= Buffs[att][8])
////				{
////					Buff[att][2] += (OriginalValue[att]*Buffs[att][6] + Buffs[att][7])*BuffNerfLevel*ActionMult ;
////				}
////				if (Math.random() <= Buffs[att][11])
////				{
////					Buff[att][3] += (OriginalValue[att]*Buffs[att][9] + Buffs[att][10])*BuffNerfLevel*ActionMult ;
////				}
////			}
////		}
////		else if (action.equals("deactivate") | Math.random() <= Buffs[att][2])
////		{
////			Buff[att][0] += (OriginalValue[att]*Buffs[att][0] + Buffs[att][1])*BuffNerfLevel*ActionMult ;
////		}
////		if (!spellIsActive)
////		{
////			PA.getLife().incCurrentValue((int) Buff[0][0]); ;
////			PA.getMp().incCurrentValue((int) Buff[1][0]); ;
////			BA.getPhyAtk().incBonus(Buff[2][0]) ;
////			BA.getMagAtk().incBonus(Buff[3][0]) ;
////			BA.getPhyDef().incBonus(Buff[4][0]) ;
////			BA.getMagDef().incBonus(Buff[5][0]) ;
////			BA.getDex().incBonus(Buff[6][0]) ;
////			BA.getAgi().incBonus(Buff[7][0]) ;
////			BA.getCrit()[1] += Buff[8][0] ;
////			BA.getStun()[1] += Buff[9][0] ;
////			BA.getBlock()[1] += Buff[10][0] ;
////			BA.getBlood()[1] += Buff[11][0] ;
////			BA.getBlood()[3] += Buff[11][1] ;
////			BA.getBlood()[5] += Buff[11][2] ;
////			BA.getBlood()[7] += Buff[11][3] ;
////			BA.getBlood()[8] += Buff[11][4] ;
////			BA.getPoison()[1] += Buff[12][0] ;
////			BA.getPoison()[3] += Buff[12][1] ;
////			BA.getPoison()[5] += Buff[12][2] ;
////			BA.getPoison()[7] += Buff[12][3] ;
////			BA.getPoison()[8] += Buff[12][4] ;
////			BA.getSilence()[1] += Buff[13][0] ;
////		}	
//	}
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