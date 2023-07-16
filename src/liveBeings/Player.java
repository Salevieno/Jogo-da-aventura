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
import graphics.Animation;
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
import main.TextCategories;
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
//	private String language ;
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
    public static final Gif FishingGif = new Gif(UtilG.loadImage(Game.ImagesPath + "\\Player\\" + "Fishing.gif"), 220, false, false) ;
    
	public final static List<String[]> Properties = UtilG.ReadcsvFile(Game.CSVPath + "PlayerInitialStats.csv") ;
	public final static List<String[]> EvolutionProperties = UtilG.ReadcsvFile(Game.CSVPath + "PlayerEvolution.csv") ;	
	public final static int[] NumberOfSpellsPerJob = new int[] {14, 15, 15, 14, 14} ;
	public final static int[] CumNumberOfSpellsPerJob = new int[] {0, 34, 69, 104, 138} ;
    public final static Color[] ClassColors = new Color[] {Game.colorPalette[0], Game.colorPalette[1], Game.colorPalette[2], Game.colorPalette[3], Game.colorPalette[4]} ;
    public final static Gif levelUpGif = new Gif(UtilG.loadImage(Game.ImagesPath + "\\Player\\" + "LevelUp.gif"), 170, false, false) ;
    
    public static String[] ActionKeys = new String[] {"W", "A", "S", "D", "B", "C", "F", "M", "P", "Q", "H", "R", "T", "Z"} ;	// [Up, Left, Down, Right, Bag, Char window, Fab, Map, Pet window, Quest, Hint, Ride, Tent, Bestiary]
	public static final String[] MoveKeys = new String[] {"W", "A", "S", "D", KeyEvent.getKeyText(KeyEvent.VK_UP), KeyEvent.getKeyText(KeyEvent.VK_LEFT), KeyEvent.getKeyText(KeyEvent.VK_DOWN), KeyEvent.getKeyText(KeyEvent.VK_RIGHT)} ;
	public static final String[] HotKeys = new String[] {"T", "Y", "U"} ;
	public static final List<String> SpellKeys = new ArrayList<>(Arrays.asList(new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "F10", "F11", "F12"})) ;

	public final static Image[] AttWindowImages = new Image[] {UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "PlayerAttWindow1.png"), UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "PlayerAttWindow2.png"), UtilG.loadImage(Game.ImagesPath + "\\Windows\\" + "PlayerAttWindow3.png")} ;
    
	public Player(String name, String Sex, int job)
	{
		super(
				InitializePersonalAttributes(job),
				InitializeBattleAttributes(job),
				InitializeMovingAnimations(),
				new PlayerAttributesWindow(AttWindowImages[0])
				) ;
		((PlayerAttributesWindow) attWindow).initializeAttIncButtons(this) ;
		
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
	    
//		this.language = Language ;
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
		spellsTree = new SpellsTreeWindow() ;
		bestiary = new BestiaryWindow() ;
		equips = new Equip[4] ;	// 0: weapon, 1: shield, 2: armor, 3: arrow
		spellPoints = 0 ;
		color = Game.colorPalette[12] ;
    	
		collectLevel = new double[3] ;
		storedGold = 0 ;
		goldMultiplier = Double.parseDouble(Properties.get(job)[32]) ; 
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
	

	private static PersonalAttributes InitializePersonalAttributes(int job)
	{
	    BasicAttribute life = new BasicAttribute(Integer.parseInt(Properties.get(job)[2]), Integer.parseInt(Properties.get(job)[2]), 1) ;
	    BasicAttribute mp = new BasicAttribute(Integer.parseInt(Properties.get(job)[3]), Integer.parseInt(Properties.get(job)[3]), 1) ;
		BasicAttribute exp = new BasicAttribute(0, 5, Double.parseDouble(Properties.get(job)[34])) ;
		BasicAttribute satiation = new BasicAttribute(100, 100, Integer.parseInt(Properties.get(job)[35])) ;
		BasicAttribute thirst = new BasicAttribute(100, 100, Integer.parseInt(Properties.get(job)[36])) ;
		return new PersonalAttributes(life, mp, exp, satiation,	thirst) ;
	}
	
	private static BattleAttributes InitializeBattleAttributes(int job)
	{

		BasicBattleAttribute phyAtk = new BasicBattleAttribute (Double.parseDouble(Properties.get(job)[5]), 0, 0) ;
		BasicBattleAttribute magAtk = new BasicBattleAttribute (Double.parseDouble(Properties.get(job)[6]), 0, 0) ;
		BasicBattleAttribute phyDef = new BasicBattleAttribute (Double.parseDouble(Properties.get(job)[7]), 0, 0) ;
		BasicBattleAttribute magDef = new BasicBattleAttribute (Double.parseDouble(Properties.get(job)[8]), 0, 0) ;
		BasicBattleAttribute dex = new BasicBattleAttribute (Double.parseDouble(Properties.get(job)[9]), 0, 0) ;	
		BasicBattleAttribute agi = new BasicBattleAttribute (Double.parseDouble(Properties.get(job)[10]), 0, 0) ;
		double[] crit = new double[] {Double.parseDouble(Properties.get(job)[11]), 0, Double.parseDouble(Properties.get(job)[12]), 0} ;
		BattleSpecialAttribute stun = new BattleSpecialAttribute(Double.parseDouble(Properties.get(job)[13]), 0, Double.parseDouble(Properties.get(job)[14]), 0, Integer.parseInt(Properties.get(job)[15])) ;
		BattleSpecialAttribute block = new BattleSpecialAttribute(Double.parseDouble(Properties.get(job)[16]), 0, Double.parseDouble(Properties.get(job)[17]), 0, Integer.parseInt(Properties.get(job)[18])) ;
		BattleSpecialAttributeWithDamage blood = new BattleSpecialAttributeWithDamage(Double.parseDouble(Properties.get(job)[19]), 0, Double.parseDouble(Properties.get(job)[20]), 0, Integer.parseInt(Properties.get(job)[21]), 0, Integer.parseInt(Properties.get(job)[22]), 0, Integer.parseInt(Properties.get(job)[23])) ;
		BattleSpecialAttributeWithDamage poison = new BattleSpecialAttributeWithDamage(Double.parseDouble(Properties.get(job)[24]), 0, Double.parseDouble(Properties.get(job)[25]), 0, Integer.parseInt(Properties.get(job)[26]), 0, Integer.parseInt(Properties.get(job)[27]), 0, Integer.parseInt(Properties.get(job)[28])) ;
		BattleSpecialAttribute silence = new BattleSpecialAttribute(Double.parseDouble(Properties.get(job)[29]), 0, Double.parseDouble(Properties.get(job)[30]), 0, Integer.parseInt(Properties.get(job)[31])) ;
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
	
//	public String getLanguage() {return language ;}
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
	public Statistics getStats() {return stats ;}
	public int getAttPoints() {return attPoints ;}
	public AttributeBonus getAttIncrease() {return attIncrease ;}	
	public void setAttIncrease(AttributeBonus attIncrease) { this.attIncrease = attIncrease ;}	
	public AttributeBonus getAttChanceIncrease() { return attChanceIncrease ;}
	public void setAttChanceIncrease(AttributeBonus attChanceIncrease) { this.attChanceIncrease = attChanceIncrease ;}


	public AttributeBonus getChanceIncrease() {return attChanceIncrease ;}
	public SettingsWindow getSettings() {return settings ;}
	public SpellsTreeWindow getSpellsTreeWindow() {return spellsTree ;}
	public Creature getOpponent() { return opponent ;}
	public Item[] getHotItems() { return hotItems ;}
	public Statistics getStatistics() { return stats ;}
	public void setClosestCreature(Creature creature) { closestCreature = creature ;}
	public void setHotItem(Item item, int slot) { hotItems[slot] = item ;}	
	public void setGoldMultiplier(double goldMultiplier) { this.goldMultiplier = goldMultiplier ;}
	

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
	
	private void trainCollecting(Collectible collectible)
	{
		
		if (collectible.type() <= 0) { return ;}
		
		collectLevel[collectible.type() - 1] += 0.25 / (collectLevel[collectible.type() - 1] + 1) ;
		
	}
	
	private void addCollectibleToBag(Collectible collectible, BagWindow bag)
	{
		bag.Add(collectible.getItem(), 1) ;
	}
	
	private void removeCollectibleFromMap()
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
	}
	
	public void collect(DrawingOnPanel DP)
    {
		collectCounter.inc() ;
        DP.DrawGif(collectingGif, getPos(), Align.center);
        
        if (collectCounter.finished())
        {
        	removeCollectibleFromMap() ;
        	addCollectibleToBag(currentCollectible, bag) ;
        	trainCollecting(currentCollectible) ;
        	
            currentCollectible = null ;
        }
    }
	
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
		
		if (!Game.getScreen().posIsInMap(newPos)) {	moveToNewMap(pet) ; return ;}
		
		if (!map.groundIsWalkable(newPos, elem[4])) { return ;}
		
		setPos(newPos) ;
		
	}
	
	private void moveToNewMap(Pet pet)
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
		
		if (pet != null) { pet.setPos(newPos) ;}
		
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
	
	public void fish()
	{
		
		Point offset = new Point() ;
		offset.x =  dir.equals(Directions.left) ? -size.width : dir.equals(Directions.right) ? size.width : 0 ;
		offset.y =  dir.equals(Directions.up) ? -size.height : dir.equals(Directions.down) ? size.height : 0 ;

		setState(LiveBeingStates.fishing) ;
		
		Animation fishingAnimation = Game.getAnimations()[9] ;
		fishingAnimation.start(FishingGif.getDuration(), new Object[] {pos, dir}) ;
		
	}
	
	// \*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/\*/
		
	private void keyboardActions(Pet pet)
	{

		if (currentAction.equals(ActionKeys[4]))
		{
			focusWindow = bag ;
			// TODO bag.orderItems() ;
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
			if (bag.contains(Game.getAllItems()[1340]) & isTouching(GroundTypes.water))
			{
				fish() ;
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
		if (currentAction.equals(ActionKeys[13]))// & questSkills.get(QuestSkills.bestiary)
		{
			focusWindow = bestiary ;
			bestiary.open() ;
		}
	}
	
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
			sideBar.getButtons().forEach(button ->
			{
				if (button.ishovered(mousePos))
				{
					switch (button.getName())
					{
						case "settings":  settings.open() ; break ;
						case "bag": bag.open() ; break ;
						case "quest": questWindow.open() ; break ;
						case "map": if (questSkills.get(QuestSkills.getContinentMap(map.getContinentName(this).name()))) mapWindow.open() ; break ;
						case "fab": fabWindow.open() ; break ;
						case "player": attWindow.open() ; break ;
						case "pet": if (pet.isAlive()) pet.getAttWindow().open() ; break ;
					}
				}
			});
		}
		
		
		keyboardActions(pet) ;
		
		
		// using spells
		if (actionIsSpell())
		{
			Spell spell = spells.get(SpellKeys.indexOf(currentAction));
			useSpell(spell, this) ;
			
//			if (spell.getType().equals(SpellTypes.offensive) & closestCreature != null & !isInBattle())
//			{
//				engageInFight(closestCreature) ;
//			}
		}
		
		
		// if hits creature, enters battle
		if ((actionIsPhysicalAtk() | actionIsMagicalAtk()) & closestCreature != null & !isInBattle())
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
				List<Creature> creaturesInMap = fm.getCreatures() ;
				for (Creature creature : creaturesInMap)
				{
					double distx = UtilG.dist1D(pos.x, creature.getPos().x) ;
					double disty = UtilG.dist1D(pos.y - size.height / 2, creature.getPos().y) ;
					if (distx <= (size.width + creature.getSize().width) / 2 & disty <= (size.height + creature.getSize().height) / 2) //  & !ani.isActive(10) & !ani.isActive(19)
					{
						opponent = creature ;
						opponent.setFollow(true) ;
						setState(LiveBeingStates.fighting) ;
						bestiary.addDiscoveredCreature(opponent.getType()) ;
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
			Arrow arrow = (Arrow) item ;
			
			arrow.use(this) ;
			
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
	
	public AtkResults useSpell(Spell spell, LiveBeing receiver)
	{
		if (spell.getType().equals(SpellTypes.support))
		{
			if (spell.isReady() & hasEnoughMP(spell) & 0 < spell.getLevel())
			{
				useSupportSpell(spell) ;
				train(new AtkResults(AtkTypes.magical, null, 0)) ;
				stats.incNumberMagAtk() ;
			}
			
			return null ;
		}
		else
		{
			return useOffensiveSpell(spell, receiver) ;
		}
	}
	
			
	
	// called every time the window is repainted
	public void showWindows(Pet pet, Point mousePos, DrawingOnPanel DP)
	{
		if (bag.isOpen())
		{
			bag.display(mousePos, Game.allText.get(TextCategories.bagMenus), DP) ;
		}
		if (attWindow.isOpen())
		{
			((PlayerAttributesWindow) attWindow).display(this, equips, mousePos, DP);
		}
		if (fabWindow.isOpen())
		{		
			fabWindow.display(knownRecipes, mousePos, DP) ;
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
			bestiary.display(this, mousePos, DP) ;
		}
		if (settings.isOpen())
		{
			settings.display(DP) ;
		}
		if (hintsWindow.isOpen())
		{
			hintsWindow.display(this, DP) ;
		}
		if (spellsTree.isOpen())
		{
			spellsTree.display(mousePos, spellPoints, DP) ;
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
	private void useSupportSpell(Spell spell)
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
	public void win(Creature creature, Animation winAnimation)
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
		
		// TODO
		//winAnimation.start(300, new Object[] {ItemsObtained}) ;
	}
	public void levelUp(Animation attIncAnimation)
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
		
		Game.getAnimations()[4].start(levelUpGif.getDuration(), new Object[] {pos}) ;
		
		if (attIncAnimation == null) { return ;}
		
		// TODO
		attIncAnimation.start(600, new Object[] {Arrays.copyOf(attIncrease, attIncrease.length - 1), level}) ;
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
	public AtkResults useOffensiveSpell(Spell spell, LiveBeing receiver)
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
		double AtkCritMod = spell.getAtkCritMod()[0] * spellLevel ;
		double DefCritMod = spell.getDefCritMod()[0] * spellLevel ;
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
		DP.DrawCircle(pos, (int)(2 * range), 2, null, Game.colorPalette[job]) ;
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
		DrawTimeBar(relPos, Game.colorPalette[9], DP) ;
	}
	public void display(Point pos, Scale scale, Directions direction, boolean showRange, DrawingOnPanel DP)
	{
		double angle = DrawingOnPanel.stdAngle ;
		if (isRiding)
		{
			Point ridePos = UtilG.Translate(pos, -RidingImage.getWidth(null) / 2, RidingImage.getHeight(null) / 2) ;
			DP.DrawImage(RidingImage, ridePos, angle, scale, Align.bottomLeft) ;
		}
		
		movingAni.display(direction, pos, angle, new Scale(1,1), DP) ;
		
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
		
	}
	
	private static Player load(String filePath)
	{
		
		// TODO
		JSONObject jsonData = UtilG.readJsonObject(filePath) ;
		Player newPlayer = new Player((String) jsonData.get("name"), "", 0) ;
		newPlayer.setLevel((int) jsonData.get("level"));
		
		return newPlayer ;
		
	}
	
	
}

//public void UseItem(Pet pet, Creatures[] creature, int creatureID, int itemID, Items[] items, Battle B)
//{
//	double PotMult = 1 ;
//	if (job == 3)
//	{
//		PotMult += 0.06 * Spell[7] ;
//	}
//	if (-1 < itemID)	// if the item is valid
//	{
//		//if (0 < Bag[itemID])	// if the player has the item in the bag
//		//{
//			if (items[itemID].getType().equals("Potion"))	// potions
//			{
//				double HealPower = Items.PotionsHealing[itemID][1] * PotMult ;
//				
//				PA.incLife(HealPower * PA.getLife()[1]) ;
//				PA.incMP(HealPower * PA.getMp()[1]) ;
//				//Bag[itemID] += -1 ;			
//			}
//			else if (items[itemID].getType().equals("Alchemy"))	// alchemy (using herbs heal half of their equivalent pot)
//			{
//				double HealPower = Items.PotionsHealing[itemID / 3 - Items.BagIDs[0] / 3 + 1][1] / 2 * PotMult ;
//				
//				PA.incLife(HealPower * PA.getLife()[1]) ;
//				if (0 < pet.getLife()[0])
//				{
//					pet.incLife(HealPower * pet.getLife()[1]) ;
//					pet.incMP(HealPower * pet.getMp()[1]) ;
//					
//					// Animal's skill "natural help": gives 20% bonus on the pet attributes. The enhanced attributes depend on the collectible used
//					if (-1 < creatureID & BA.getBattleActions()[0][2] == 1 & isInBattle() & job == 3 & (itemID - Items.BagIDs[1] + 1) % 3 < Spell[10])
//					{
//						int ItemsWithEffectsID = -1 ;
//						for (int i = 0 ; i <= Items.ItemsWithEffects.length - 1 ; ++i)
//						{
//							if (itemID == Items.ItemsWithEffects[i])
//							{
//								ItemsWithEffectsID = i ;
//							}
//						}
//						if (-1 < ItemsWithEffectsID)
//						{
//							B.ItemEffectInBattle(getBattleAtt(), pet.getBA(), creature[creatureID].getBA(), creature[creatureID].getElem(), creature[creatureID].getLife(), items, ItemsWithEffectsID, Items.ItemsTargets[ItemsWithEffectsID], Items.ItemsElement[ItemsWithEffectsID], Items.ItemsEffects[ItemsWithEffectsID], Items.ItemsBuffs[ItemsWithEffectsID], "activate") ;
//						}
//					}
//				}
//				//Bag[itemID] += -1 ;
//			}
//			else if (items[itemID].getType().equals("Pet"))		// pet items
//			{
//				pet.incLife(Items.PetItems[itemID - Items.BagIDs[3] + 1][1] * pet.getLife()[1]) ;
//				pet.incMP(Items.PetItems[itemID - Items.BagIDs[3] + 1][2] * pet.getMp()[1]) ;
//				pet.incSatiation(Items.PetItems[itemID - Items.BagIDs[3] + 1][3]) ;
//				//Bag[itemID] += -1 ;
//			}
//			else if (items[itemID].getType().equals("Food"))	// food
//			{
//				PA.incSatiation(Items.FoodSatiation[itemID - Items.BagIDs[4] + 1][3] * PA.getSatiation()[2]) ;
//				//Bag[itemID] += -1 ;
//			}
//			else if (items[itemID].getType().equals("Arrow"))	// arrows
//			{
//				//if (0 < Bag[itemID] & job == 2)
//				//{
//					if (itemID == Items.BagIDs[5] | itemID <= Items.BagIDs[5] + 3 & 1 <= Spell[4] | itemID == Items.BagIDs[5] + 4 & 2 <= Spell[4]  | itemID == Items.BagIDs[5] + 5 & 3 <= Spell[4]  | Items.BagIDs[5] + 6 <= itemID & itemID <= Items.BagIDs[5] + 14 & 4 <= Spell[4]  | Items.BagIDs[5] + 14 <= itemID & 5 <= Spell[4])
//					{
//						if (0 == Equips[3])
//						{
//							Equips[3] = itemID ;	// Equipped arrow
//							elem[0] = bag.getArrow()[itemID - Items.BagIDs[5]].getElem() ;
//						}
//						else
//						{
//							Equips[3] = 0 ;	// Unequipped arrow
//							elem[0] = "n" ;
//						}
//					}
//				//}
//			}
//			else if (items[itemID].getType().equals("Equip"))	// equipment
//			{
//				if (isEquippable(itemID))
//				{
//					Equip(items, itemID) ;
//				}
//			}
//			else if (items[itemID].getType().equals("Item"))	// items
//			{
//				if (-1 < creatureID & BA.getBattleActions()[0][2] == 1 & isInBattle() & job == 4 & 0 < Spell[8])
//				{		
//					int ItemsWithEffectsID = -1 ;
//					for (int i = 0 ; i <= Items.ItemsWithEffects.length - 1 ; ++i)
//					{
//						if (itemID == Items.ItemsWithEffects[i])
//						{
//							ItemsWithEffectsID = i ;
//						}
//					}
//					if (-1 < ItemsWithEffectsID)
//					{
//						B.ItemEffectInBattle(getBattleAtt(), pet.getBA(), creature[creatureID].getBA(), creature[creatureID].getElem(), creature[creatureID].getLife(), items, ItemsWithEffectsID, Items.ItemsTargets[ItemsWithEffectsID], Items.ItemsElement[ItemsWithEffectsID], Items.ItemsEffects[ItemsWithEffectsID], Items.ItemsBuffs[ItemsWithEffectsID], "activate") ;
//					}
//					//Bag[itemID] += -1 ;
//					BA.getBattleActions()[0][0] = 0 ;
//					BA.getBattleActions()[0][2] = 0 ;
//				}
//				else
//				{
//					ItemEffect(itemID) ;
//					//Bag[itemID] += -1 ;
//				}
//			}
//		//}			
//	}
//}