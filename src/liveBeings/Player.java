package liveBeings ;

import java.awt.Color ;
import java.awt.Image ;
import java.awt.Point;
import java.awt.event.KeyEvent ;
import java.util.ArrayList ;
import java.util.Arrays ;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import maps.Collectible;
import maps.FieldMap;
import maps.GroundTypes;
import maps.MapElement;
import maps.TreasureChest;
import screen.SideBar;
import utilities.Align;
import utilities.AtkEffects;
import utilities.Directions;
import utilities.Elements;
import utilities.Scale;
import utilities.TimeCounter;
import utilities.UtilG;
import utilities.UtilS;
import windows.BagWindow;
import windows.BankWindow;
import windows.BestiaryWindow;
import windows.CraftWindow;
import windows.ElementalWindow;
import windows.FabWindow;
import windows.ForgeWindow;
import windows.GameWindow;
import windows.HintsWindow;
import windows.MapWindow;
import windows.PetAttributesWindow;
import windows.PlayerAttributesWindow;
import windows.QuestWindow;
import windows.SettingsWindow;
import windows.ShoppingWindow;
import windows.SpellsTreeWindow;

public class Player extends LiveBeing
{
//	private String language ;
	private String sex ;
	private Color color ;
	
	private GameWindow focusWindow ;
	private List<GameWindow> openWindows ;
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
	private TimeCounter digCounter ;		// counts the progress of digging
	private TimeCounter tentCounter ;		// counts the progress of sleeping at the tent
	private Equip[] equips ;		// 0: weapon, 1: shield, 2: armor
	private Arrow equippedArrow ;
	private int storedGold ;
	private double goldMultiplier ;	// multiplies the amount of gold the player wins
	private Map<QuestSkills, Boolean> questSkills ;	// skills gained with quests
	private boolean isRiding ;		// true if the player is riding

	private AttributeBonus attIncrease ;	// Amount of increase in each attribute when the player levels up
	private AttributeBonus attChanceIncrease ;	// Chance of increase of these attributes
	
	private Creature closestCreature ;		// creature that is currently closest to the player
    private Creature opponent ;				// creature that is currently in battle with the player
    private Collectible currentCollectible ;
    private TreasureChest currentChest ;
    private Item[] hotItems ;				// items on the hotkeys
	private Statistics stats ;
    
	public static final int maxLevel = 99 ;
    public static final Image CollectingMessage = UtilS.loadImage("\\Collect\\" + "CollectingMessage.gif") ;   
    public static final Image collectingGif = UtilS.loadImage("\\Collect\\" + "Collecting.gif") ;
//    public static final Image TentImage = UtilS.loadImage("\\SideBar\\" + "Icon5_tent.png") ;
    public static final Gif TentGif = new Gif(UtilS.loadImage("Tent.png"), 1000, false, false) ;
    public static final Image DragonAuraImage = UtilS.loadImage("\\Player\\" + "dragonAura.gif") ;
    public static final Image RidingImage = UtilS.loadImage("\\Player\\" + "Tiger.png") ;
	public static final Image CoinIcon = UtilS.loadImage("\\Player\\" + "CoinIcon.png") ;    
	public static final Image DiggingGif = UtilS.loadImage("\\Player\\" + "Digging.gif") ;   
	public static final Image MagicBlissGif = UtilS.loadImage("\\Player\\" + "MagicBliss.gif") ;
    public static final Gif FishingGif = new Gif(UtilS.loadImage("\\Player\\" + "Fishing.gif"), 220, false, false) ;
    
	public final static List<String[]> Properties = UtilG.ReadcsvFile(Game.CSVPath + "PlayerInitialStats.csv") ;
	public final static List<String[]> EvolutionProperties = UtilG.ReadcsvFile(Game.CSVPath + "PlayerEvolution.csv") ;	
	public final static int[] NumberOfSpellsPerJob = new int[] {14, 15, 15, 14, 14} ;
	public final static int[] CumNumberOfSpellsPerJob = new int[] {0, 34, 69, 104, 138} ;
    public final static Color[] ClassColors = new Color[] {Game.colorPalette[21], Game.colorPalette[5], Game.colorPalette[2], Game.colorPalette[3], Game.colorPalette[4]} ;
    public final static Gif levelUpGif = new Gif(UtilS.loadImage("\\Player\\" + "LevelUp.gif"), 320, false, false) ;
    
    public static String[] ActionKeys = new String[] {"W", "A", "S", "D", "B", "C", "F", "M", "P", "Q", "H", "R", "T", "X", "Z"} ;	// [Up, Left, Down, Right, Bag, Char window, Fab, Map, Pet window, Quest, Hint, Ride, Tent, Dig, Bestiary]
	public static final String[] MoveKeys = new String[] {"W", "A", "S", "D", KeyEvent.getKeyText(KeyEvent.VK_UP), KeyEvent.getKeyText(KeyEvent.VK_LEFT), KeyEvent.getKeyText(KeyEvent.VK_DOWN), KeyEvent.getKeyText(KeyEvent.VK_RIGHT)} ;
	public static final String[] HotKeys = new String[] {"T", "Y", "U"} ;

	public final static Image[] AttWindowImages = new Image[]
								{
									UtilS.loadImage("\\Windows\\" + "PlayerAttWindow1.png"),
									UtilS.loadImage("\\Windows\\" + "PlayerAttWindow2.png"),
									UtilS.loadImage("\\Windows\\" + "PlayerAttWindow3.png")
								} ;
    public final static Image settingsWindowImage = UtilS.loadImage("\\Windows\\" + "windowSettings.png") ;
	
	
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
	    size = UtilG.getSize(UtilS.loadImage("\\Player\\" + "PlayerBack.gif")) ;
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
		openWindows = new ArrayList<>() ;
		bag = new BagWindow() ;
		if (job == 2)
		{
			bag.add(Arrow.getAll()[0], 100) ;
		}
		questWindow = new QuestWindow() ;
		quests = new ArrayList<>() ;
		knownRecipes = new ArrayList<>() ;
		fabWindow = new FabWindow() ;
		mapWindow = new MapWindow() ;
		hintsWindow = new HintsWindow() ;
		spellsTree = new SpellsTreeWindow(job) ;
		bestiary = new BestiaryWindow() ;
		equips = new Equip[3] ;
		equippedArrow = null ;
		spellPoints = 0 ;
		color = Game.colorPalette[12] ;
    	
		collectLevel = new double[3] ;
		storedGold = 0 ;
		goldMultiplier = Double.parseDouble(Properties.get(job)[32]) ; 
		questSkills = new HashMap<QuestSkills, Boolean>() ;
		for (QuestSkills questSkill : QuestSkills.values())
		{
			questSkills.put(questSkill, false) ;
		}
		isRiding = false ;
		stats = new Statistics() ;
		collectCounter = new TimeCounter(0, 240) ;
		digCounter = new TimeCounter(0, 200) ;
		tentCounter = new TimeCounter(0, 200) ;
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
	    currentChest = null ;
		settings = new SettingsWindow(settingsWindowImage, false, true, false, 1, 1) ;
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
		BasicBattleAttribute critAtk = new BasicBattleAttribute (Double.parseDouble(Properties.get(job)[11]), 0, 0) ;
		BasicBattleAttribute critDef = new BasicBattleAttribute (Double.parseDouble(Properties.get(job)[12]), 0, 0) ;
		BattleSpecialAttribute stun = new BattleSpecialAttribute(Double.parseDouble(Properties.get(job)[13]), 0, Double.parseDouble(Properties.get(job)[14]), 0, Integer.parseInt(Properties.get(job)[15])) ;
		BattleSpecialAttribute block = new BattleSpecialAttribute(Double.parseDouble(Properties.get(job)[16]), 0, Double.parseDouble(Properties.get(job)[17]), 0, Integer.parseInt(Properties.get(job)[18])) ;
		BattleSpecialAttributeWithDamage blood = new BattleSpecialAttributeWithDamage(Double.parseDouble(Properties.get(job)[19]), 0, Double.parseDouble(Properties.get(job)[20]), 0, Integer.parseInt(Properties.get(job)[21]), 0, Integer.parseInt(Properties.get(job)[22]), 0, Integer.parseInt(Properties.get(job)[23])) ;
		BattleSpecialAttributeWithDamage poison = new BattleSpecialAttributeWithDamage(Double.parseDouble(Properties.get(job)[24]), 0, Double.parseDouble(Properties.get(job)[25]), 0, Integer.parseInt(Properties.get(job)[26]), 0, Integer.parseInt(Properties.get(job)[27]), 0, Integer.parseInt(Properties.get(job)[28])) ;
		BattleSpecialAttribute silence = new BattleSpecialAttribute(Double.parseDouble(Properties.get(job)[29]), 0, Double.parseDouble(Properties.get(job)[30]), 0, Integer.parseInt(Properties.get(job)[31])) ;
		LiveBeingStatus status = new LiveBeingStatus() ;

		return new BattleAttributes(phyAtk, magAtk, phyDef, magDef, dex, agi, critAtk, critDef, stun, block, blood, poison, silence, status) ;
	
	}
	
	private static MovingAnimations InitializeMovingAnimations()
	{
	    Image idleGif = UtilS.loadImage("\\Player\\" + "PlayerIdle.gif") ;
	    Image movingUpGif = UtilS.loadImage("\\Player\\" + "PlayerBack.gif") ;
		Image movingDownGif = UtilS.loadImage("\\Player\\" + "PlayerFront.gif") ;
		Image movingLeftGif = UtilS.loadImage("\\Player\\" + "PlayerMovingLeft.gif") ;
		Image movingRightGif = UtilS.loadImage("\\Player\\" + "PlayerRight.gif") ;
		
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
	
	public String getSex() {return sex ;}
	public Directions getDir() {return dir ;}
	public Color getColor() {return color ;}
	public List<Quest> getQuests() {return quests ;}
	public BagWindow getBag() {return bag ;}
	public Equip[] getEquips() {return equips ;}
	public Arrow getEquippedArrow() {return equippedArrow ;}
	public int getSpellPoints() {return spellPoints ;}
	public BasicAttribute getLife() {return PA.getLife() ;}
	public BasicAttribute getMp() {return PA.getMp() ;}
	public BasicBattleAttribute getPhyAtk() {return BA.getPhyAtk() ;}
	public BasicBattleAttribute getMagAtk() {return BA.getMagAtk() ;}
	public BasicBattleAttribute getPhyDef() {return BA.getPhyDef() ;}
	public BasicBattleAttribute getMagDef() {return BA.getMagDef() ;}
	public BasicBattleAttribute getDex() {return BA.getDex() ;}
	public BasicBattleAttribute getAgi() {return BA.getAgi() ;}
	public BasicBattleAttribute getCritAtk() {return BA.getCritAtk() ;}
	public BasicBattleAttribute getCritDef() {return BA.getCritDef() ;}
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
	public void setEquippedArrow(Arrow equippedArrow) {this.equippedArrow = equippedArrow ;}	
	public AttributeBonus getChanceIncrease() {return attChanceIncrease ;}
	public SettingsWindow getSettings() {return settings ;}
	public QuestWindow getQuestWindow() {return questWindow ;}
	public MapWindow getMapWindow() {return mapWindow ;}
	public FabWindow getFabWindow() {return fabWindow ;}
	public List<Recipe> getKnownRecipes() { return knownRecipes ;}
	public SpellsTreeWindow getSpellsTreeWindow() {return spellsTree ;}
	public HintsWindow getHintsindow() {return hintsWindow ;}
	public Creature getOpponent() { return opponent ;}
	public Item[] getHotItems() { return hotItems ;}
	public Statistics getStatistics() { return stats ;}
	public void setSex(String sex) { this.sex = sex ;}
	public void setClosestCreature(Creature creature) { closestCreature = creature ;}
	public void setHotItem(Item item, int slot) { hotItems[slot] = item ;}	
	public void setGoldMultiplier(double goldMultiplier) { this.goldMultiplier = goldMultiplier ;}
	public void setFocusWindow(GameWindow W) { focusWindow = W ;}

	public Point center()
	{
		return new Point((int) (pos.x), (int) (pos.y - 0.5 * size.height)) ;
	}
	
	public static Spell[] getKnightSpells() { return Arrays.copyOf(Game.getAllSpells(), 14) ;}
	public static Spell[] getMageSpells() { return Arrays.copyOfRange(Game.getAllSpells(), 34, 49) ;}
	public static Spell[] getArcherSpells() { return Arrays.copyOfRange(Game.getAllSpells(), 70, 84) ;}
	public static Spell[] getAnimalSpells() { return Arrays.copyOfRange(Game.getAllSpells(), 105, 118) ;}
	public static Spell[] getThiefSpells() { return Arrays.copyOfRange(Game.getAllSpells(), 139, 152) ;}
	
	

	public static double calcExpToLevelUp(int level)
	{
		return 10 * (3 * Math.pow(level - 1, 2) + 3 * (level - 1) + 1) - 5 ;
	}
	
	public void setAttPoints(int attPoints)
	{
		this.attPoints = attPoints;
	}
	public void addProSpells()
	{
		int firstSpellID = spells.size() + CumNumberOfSpellsPerJob[job] ;
		firstSpellID += proJob == 1 ? 0 : 10 ;
		spells.addAll(Arrays.asList(Arrays.copyOfRange(Game.getAllSpells(), firstSpellID - 1, firstSpellID + 10))) ;
	}

	public boolean isFocusedOnWindow() { if (focusWindow == null) { return false ;} return focusWindow.isOpen() ;}
	public boolean isDoneMoving() { return stepCounter.finished() ;}
	public boolean weaponIsEquipped() { return (equips[0] != null) ;}
	public boolean arrowIsEquipped() { return (equippedArrow != null) ;}
	private boolean actionIsAMove() { return Arrays.asList(MoveKeys).contains(currentAction) ;}
	private boolean hitsCreature() { return (usedPhysicalAtk() | actionIsSpell()) & closestCreature != null ;}
	public boolean isInBattle() { return opponent != null | state.equals(LiveBeingStates.fighting) ;}
	public boolean isCollecting() { return state.equals(LiveBeingStates.collecting) ;}
	public boolean isOpeningChest() { return state.equals(LiveBeingStates.openingChest) ;}
	public boolean shouldLevelUP() {return getExp().getMaxValue() <= getExp().getCurrentValue() ;}
	public static boolean setIsFormed(Equip[] EquipID)
	{
		if (EquipID[0] == null | EquipID[1] == null | EquipID[2] == null) { return false ;}
		
		return (EquipID[0].getId() + 1) == EquipID[1].getId() & (EquipID[1].getId() + 1) == EquipID[2].getId() ;
	}

	
//	private Point feetPos() {return new Point(pos.x, pos.y) ;}	

	public Creature closestCreatureInRange()
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
	
	public void discoverCreature(CreatureType creatureType) { bestiary.addDiscoveredCreature(creatureType) ;}
	
	public void addQuest(Quest newQuest) { quests.add(newQuest) ;}
	
	public void decSpellPoints() { spellPoints += -1 ;}
	
	public void resetClosestCreature() { closestCreature = null ;}
	
	public void resetOpponent() { opponent = null ;}
		
	private void activateRide()
	{
		step = isRiding ? step / 2 : 2 * step ;
		isRiding = !isRiding ;
	}
	
	private void trainCollecting(Collectible collectible)
	{
		
		if (collectible.typeNumber() <= 0) { return ;}
		
		collectLevel[collectible.typeNumber() - 1] += 0.25 / (collectLevel[collectible.typeNumber() - 1] + 1) ;
		
	}
	
	private void addCollectibleToBag(Collectible collectible, BagWindow bag)
	{
		bag.add(collectible.getItem(), 1) ;
	}
	
	private void removeCollectibleFromMap(Collectible collectible)
	{        
		if (!map.isAField()) { return ;}
		// TODO essa função devia estar em fieldMap
		List<Collectible> collectibles = ((FieldMap) map).getCollectibles() ;
		collectibles.remove(collectible) ;
	}
	
	public void finishCollecting()
	{
		collectCounter.reset() ;
        collectingGif.flush() ;
    	state = LiveBeingStates.idle ;
        currentCollectible = null ;
	}
	
	public void collect(DrawingOnPanel DP)
    {
		
		collectCounter.inc() ;
        DP.DrawGif(collectingGif, getPos(), Align.center);
        
        if (!collectCounter.finished()) { return ;}
        
        boolean isBerry = currentCollectible.typeNumber() == 0 ;
        int mapLevel = ((FieldMap) map).getLevel() ;
        double collectChance = isBerry ? 1 : 1 - 1 / (1 + Math.pow(1.1, collectLevel[currentCollectible.typeNumber() - 1] - mapLevel)) ;
        String msg = "Falha na coleta!" ;        
    	
        if (UtilG.chance(collectChance))
        {
        	msg = "Coleta com sucesso!" ;
        	addCollectibleToBag(currentCollectible, bag) ;
        	trainCollecting(currentCollectible) ;
        }

    	removeCollectibleFromMap(currentCollectible) ;
        finishCollecting() ;
    	Game.getAnimations().get(12).start(200, new Object[] {Game.getScreen().pos(0.2, 0.1), msg, Game.colorPalette[4]}) ;      

    }
	
	private void addChestContentToBag(TreasureChest chest, BagWindow bag)
	{
		chest.getItemRewards().forEach(item -> bag.add(item, 1)) ;
		bag.addGold(chest.getGoldReward()) ;
	}
	
	public void openChest()
    {
    	addChestContentToBag(currentChest, bag) ;
		map.removeMapElem(currentChest) ;
		
		if (0 < currentChest.getGoldReward())
		{
			obtainGoldAnimation(currentChest.getGoldReward()) ;
		}
		
		if (!currentChest.getItemRewards().isEmpty())
		{
			obtainItemsAnimation(currentChest.getItemRewards()) ;
		}
    	
        state = LiveBeingStates.idle ;
        currentChest = null ;
    }
	
	public void decAttPoints(int amount) {attPoints += -amount ;}

	public void applyAdjacentGroundEffect()
	{
		if (isInside(GroundTypes.lava) & !elem[4].equals(Elements.fire))
		{
			PA.getLife().decTotalValue(5) ;
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
		
		Point newPos = calcNewPos() ;
		
		if (!Game.getScreen().posIsInMap(newPos))
		{
			moveToNewMap(pos, dir, map, step) ;

			if (pet != null) { pet.setPos(pos) ;}
			
			if (opponent != null) { opponent.setFollow(false) ;}
			closestCreature = null ;
			opponent = null ;
			return ;
		}
		
		if (!map.groundIsWalkable(newPos, elem[4])) { return ;}
		
		setPos(newPos) ;
		
	}

	private void obtainItemsAnimation(List<Item> itemsObtained)
	{		
		Game.getAnimations().get(3).start(300, new Object[] {itemsObtained.toArray(new Item[0])}) ;
	}

	private void obtainGoldAnimation(int amount)
	{
		Game.getAnimations().get(10).start(200, new Object[] {amount}) ;
	}
	
	public void engageInFight(Creature Opponent)
	{
		opponent = Opponent ;
		opponent.setFollow(true) ;
		state = LiveBeingStates.fighting ;
	}
	
	private void fish()
	{
		
		Point offset = new Point() ;
		offset.x =  dir.equals(Directions.left) ? -size.width : dir.equals(Directions.right) ? size.width : 0 ;
		offset.y =  dir.equals(Directions.up) ? -size.height : dir.equals(Directions.down) ? size.height : 0 ;

		setState(LiveBeingStates.fishing) ;
		
		Animation fishingAnimation = Game.getAnimations().get(9) ;
		fishingAnimation.start(FishingGif.getDuration(), new Object[] {pos, dir}) ;
		
	}

	private void dig(DrawingOnPanel DP)
	{		
		digCounter.inc() ;
		DP.DrawImage(DiggingGif, pos, Align.center) ;
		
		if (!digCounter.finished()) { return ;}
		
		digCounter.reset() ;		
		setState(LiveBeingStates.idle) ;
		
		if (map.getDiggingItems().isEmpty()) { return ;}
		
		List<Item> listItems = new ArrayList<Item>(map.getDiggingItems().keySet()) ;
		List<Double> listChances = new ArrayList<Double>(map.getDiggingItems().values()) ;

		int itemID = UtilG.randomFromChanceList(listChances) ;
		bag.add(listItems.get(itemID), 1) ;
		obtainItemsAnimation(Arrays.asList(listItems.get(itemID))) ;
		
	}
	
	public void tent(DrawingOnPanel DP)
	{
		TentGif.play(pos, Align.bottomCenter, DP) ;
		tentCounter.inc() ;
		if (tentCounter.finished())
		{
			tentCounter.reset() ;
			PA.getLife().setToMaximum() ;
			PA.getMp().setToMaximum() ;
			setState(LiveBeingStates.idle) ;
		}
	}
	
	private void keyboardActions(Pet pet)
	{
		int actionId = Arrays.asList(ActionKeys).indexOf(currentAction) ;
		switch (actionId)
		{
			case 4: switchOpenClose(bag) ; return ;
			
			case 5:
				((PlayerAttributesWindow) attWindow).setPlayer(this) ;
				((PlayerAttributesWindow) attWindow).updateAttIncButtons(this) ;
				switchOpenClose(attWindow) ;
				return ;
				
			case 6:
				if (!bag.contains(Game.getAllItems()[1340]) | !isTouching(GroundTypes.water)) { return ;}
				fish() ; return ;
				
			case 7:
				if (!questSkills.get(QuestSkills.getContinentMap(map.getContinentName(this).name()))) { return ;}
				mapWindow.setPlayerPos(pos) ;
				mapWindow.setCurrentMap(map) ;
				switchOpenClose(mapWindow) ; return ;
				
			case 8:
				if (pet == null) { return ;}
				((PetAttributesWindow) pet.getAttWindow()).setPet(pet) ;
				switchOpenClose(pet.getAttWindow()) ; return ;
				
			case 9: 
				questWindow.setQuests(quests) ;
				questWindow.setBag(bag) ;
				switchOpenClose(questWindow) ; return ;
				
			case 10: switchOpenClose(hintsWindow) ; return ;
			
			case 11:
				if (!questSkills.get(QuestSkills.ride)) { return ;}				
				activateRide() ;				
				return ;
				
			case 12:
				if (isInBattle()) { return ;}		
				return ;
				
			case 13: setState(LiveBeingStates.digging) ; return ;
			
			case 14: 
				if (!questSkills.get(QuestSkills.bestiary)) { return ;}
				switchOpenClose(bestiary) ; return ;
				
			default: return ;
		}
		
	}
	
	private boolean actionIsArrowKeys() { return currentAction.equals("Acima") | currentAction.equals("Abaixo") | currentAction.equals("Esquerda") | currentAction.equals("Direita") ;}
	
	public void doCurrentAction(DrawingOnPanel DP)
	{
		switch (state)
		{
			case digging: dig(DP) ; return ;
			case sleeping: tent(DP) ; return ;
			default: return ;
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

			if (actionIsArrowKeys() | (!isFocusedOnWindow() & !metAnyNPC()))
			{
				startMove() ;
			}
			
		}

		keyboardActions(pet) ;
		
		
		// using spells
		if (actionIsSpell() & !isInBattle())
		{
			Spell spell = spells.get(SpellKeys.indexOf(currentAction));
			useSpell(spell, this) ;
		}		
		
		// if hits creature, enters battle
		if (hitsCreature() & !isInBattle())
		{
			engageInFight(closestCreature) ;
		}

		if (bag.isOpen())
		{
			bag.act(currentAction, mousePos, this) ;
		}
		
		if (attWindow.isOpen() & GameWindow.actionIsForward(currentAction))
		{
			((PlayerAttributesWindow) attWindow).act(this, mousePos, currentAction) ;
		}
//		System.out.println("focus = " + focusWindow);
		
		// navigating through open windows
		if (focusWindow != null)
		{
			if (focusWindow.isOpen())
			{
				if (currentAction.equals("Escape"))	// TODO bag: tab = 0, other windows: menu = 0
				{
					switchOpenClose(focusWindow) ;
				}
				else
				{
					focusWindow.navigate(currentAction) ;
				}
			}
			if (focusWindow instanceof ShoppingWindow)
			{
				((ShoppingWindow) focusWindow).act(currentAction, bag) ;
			}
			if (focusWindow instanceof SpellsTreeWindow)
			{
				((SpellsTreeWindow) focusWindow).act(this) ;
			}
			if (focusWindow instanceof BankWindow)
			{
				((BankWindow) focusWindow).act(bag, currentAction) ;
			}
			if (focusWindow instanceof CraftWindow)
			{
				((CraftWindow) focusWindow).act(bag, currentAction) ;
			}
			if (focusWindow instanceof ElementalWindow)
			{
				((ElementalWindow) focusWindow).act(bag, currentAction) ;
			}
			if (focusWindow instanceof ForgeWindow)
			{
				((ForgeWindow) focusWindow).act(currentAction) ;
			}
		}
		
		
		// using hotItems
		for (int i = 0; i <= HotKeys.length - 1 ; i += 1)
		{
			if (!currentAction.equals(HotKeys[i]) | hotItems[i] == null) { continue ;}
			
			useItem(hotItems[i]) ;
		}
		
		
		updateCombo() ;
		
	}
	
	private boolean metNPC(NPCs npc) { return isInCloseRange(npc.getPos()) ;}
	
	private boolean metAnyNPC()
	{
		if (map.getNPCs() == null) { return false ;}
		
		for (NPCs npc : map.getNPCs())
		{
			if (metNPC(npc)) { return true ;}
		}

		if (map.getBuildings() == null) { return false ;}
		
		for (Building building : map.getBuildings())
		{
			for (NPCs npc : building.getNPCs())
			{
				if (metNPC(npc)) { return true ;}
			}
		}
		
		return false ;
	}

	public void meetWithTreasureChests()
	{
		if (!map.isSpecial()) { return ;}

		List<MapElement> chests = map.getMapElem().stream().filter(elem -> elem instanceof TreasureChest).
				collect(Collectors.toList()) ;
		for (MapElement chest : chests)
		{

			if (!isInCloseRange(chest.getPos())) { continue ;}

			setState(LiveBeingStates.openingChest);
			currentChest = (TreasureChest) chest ;
			break ;
			
		}
	}

	public void meetWithCollectibles(FieldMap map)
	{
		List<Collectible> collectibles = map.getCollectibles() ;

		if (collectibles == null) { return ;}
		if (collectibles.isEmpty()) { return ;}
		
		for (int i = 0 ; i <= collectibles.size() - 1 ; i += 1)
		{
			
			if (collectibles.size() - 1 < i) { break ;}
			
			Collectible collectible = collectibles.get(i) ;
			if (!isInCloseRange(collectible.getPos())) { continue ;}
			
			if (0 < collectible.typeNumber())
			{
				if (collectLevel[collectible.typeNumber() - 1] + 1 < ((FieldMap) map).getLevel())
				{
					Game.getAnimations().get(12).start(200, new Object[] {Game.getScreen().pos(0.2, 0.1), "Nível de coleta insuficiente", Game.colorPalette[4]}) ;
					break ;
				}
			}
			
			setState(LiveBeingStates.collecting);
			currentCollectible = collectible ;
			break ;		
			
		}
	}

	public void meetWithCreatures(FieldMap map)
	{
//		System.out.println("checking meet with creatures") ;
		List<Creature> creaturesInMap = map.getCreatures() ;
		for (Creature creature : creaturesInMap)
		{
			if (isInCloseRange(creature.getPos()))
			{
				engageInFight(creature) ;
				bestiary.addDiscoveredCreature(opponent.getType()) ;
			}
		}
	}

	public void meetWithNPCs(Point mousePos, DrawingOnPanel DP)
	{
		if (map.getNPCs() != null)
		{
			for (NPCs npc : map.getNPCs())
			{
				if (!metNPC(npc)) { npc.resetMenu() ; continue ;}
				
				npc.action(this, Game.getPet(), mousePos, DP) ;
				
				break ;
			}
		}
		
		if (map.getBuildings() == null) { return ;}
		
		for (Building building : map.getBuildings())
		{
			for (NPCs npc : building.getNPCs())
			{				
				if (!metNPC(npc)) { npc.resetMenu() ; continue ;}
				
				npc.action(this, Game.getPet(), mousePos, DP) ;
				
				break ;
			}
		}
	}
	
	public void checkMeet(Point mousePos, DrawingOnPanel DP)
	{
		if (state == LiveBeingStates.collecting | isInBattle() | Game.someAnimationIsActive()) { return ;}

		meetWithTreasureChests() ;
		
		if (map.isAField())
		{
			meetWithCollectibles((FieldMap) map) ;
			meetWithCreatures((FieldMap) map) ;			
		}	
		
		meetWithNPCs(mousePos, DP) ;
	}

	public AtkResults useSpell(Spell spell, LiveBeing receiver)
	{
		// TODO move useSpell para Spell
		if (spell.getLevel() <= 0) { return null ;}
		if (!spell.isReady()) { return null ;}
		if (!hasEnoughMP(spell)) { return null ;}
		
		spell.getCooldownCounter().reset() ;
		train(new AtkResults(AtkTypes.magical, AtkEffects.none, 0)) ;
		stats.incNumberMagAtk() ;
		System.out.println("used spell " + spell.getName());
		switch (spell.getType())
		{
			case support : useSupportSpell(spell) ;
				return null ;
				
			case offensive : if (!isInBattle()) { return null ;}
				return useOffensiveSpell(spell, receiver) ;
				
			default : return null ;
		}
		
	}
	
	public void applyPassiveSpell(Spell spell)
	{
//		System.out.println("spell id = " + spell.getId());
		switch (spell.getId())
		{
			case 1: PA.getLife().incMaxValue(10 + (int) (0.05 * PA.getLife().getMaxValue())) ; PA.getLife().setToMaximum() ; return ;
			case 2: BA.getPhyAtk().incBaseValue(2 + (int) (0.04 * BA.getPhyAtk().getBaseValue())) ; return ;
			case 3: BA.getPhyDef().incBaseValue(2 + (int) (0.04 * BA.getPhyDef().getBaseValue())) ; return ;
			case 7: BA.getBlood().incBasicDef(0.4 + 0.05 * BA.getBlood().getBasicDef()) ; return ;
			case 10:  return ; // TODO resistência a neutros + 15%
			case 11: BA.getPhyAtk().incBaseValue(2) ; BA.getDex().incBaseValue(1) ; BA.getAgi().incBaseValue(1) ; return ;
			case 14: PA.getLife().incMaxValue(202) ; PA.getLife().setToMaximum() ; BA.getPhyAtk().incBaseValue(9) ; BA.getPhyDef().incBaseValue(5) ; return ;
			case 18: BA.getDex().incBaseValue(3) ; BA.getAgi().incBaseValue(3) ; BA.getStun().incAtkChance(0.04) ; return ;
			case 19: return ; // TODO aumenta bônus do atk físico da arma em até 50%
			case 23: return ; // TODO aumenta o efeito do treinamento em 20%
			case 24: PA.getLife().incMaxValue(202) ; PA.getLife().setToMaximum() ; BA.getPhyAtk().incBaseValue(5) ; BA.getPhyDef().incBaseValue(9) ; return ;
			case 26: BA.getBlock().incAtkChance(0.06) ; return ; // TODO teste
			case 27: BA.getMagDef().incBaseValue(0.1 * BA.getMagDef().getBaseValue()) ; return ;
			case 28: BA.getCritDef().incBaseValue(0.15) ; return ;
			case 29: BA.getPoison().incDefChance(0.1) ; return ;
			
			case 36: BA.getMagAtk().incBaseValue(2 + (int) (0.04 * BA.getMagAtk().getBaseValue())) ; BA.getMagDef().incBaseValue(2 + (int) (0.04 * BA.getMagDef().getBaseValue())) ; return ;
			case 42:  return ; // TODO lança uma magia a depender do resultado das cartas, sem uso de mana, a cada 100 frames
			case 49: PA.getMp().incMaxValue(202) ; PA.getMp().setToMaximum() ; BA.getMagAtk().incBaseValue(9) ; BA.getMagDef().incBaseValue(5) ; return ;
			case 54: return ; // TODO reduz o cooldown das magias em 50%
			case 57: return ; // TODO descobre o próximo movimento da criatura com chance de 70%
			case 59: PA.getMp().incMaxValue(202) ; PA.getMp().setToMaximum() ; BA.getMagAtk().incBaseValue(5) ; BA.getMagDef().incBaseValue(9) ; return ;
			case 62: return ; // TODO regen de até 5% da vida, consome mp
			case 65: return ; // TODO chance de converter até 100% do dano mágico recebido em vida
			
			case 70: BA.getDex().incBaseValue(1) ; BA.getAgi().incBaseValue(0.4) ; return ;
			case 73:  return ; // TODO permite o uso de flechas mais poderosas
			case 76: BA.getMagAtk().incBaseValue(1 + (int) (0.02 * BA.getMagAtk().getBaseValue())) ; BA.getPhyAtk().incBaseValue(1 + (int) (0.02 * BA.getPhyAtk().getBaseValue())) ; return ;
			case 77:  return ; // TODO permite a fabricação de flechas elementais
			case 79: BA.getBlood().incDefChance(0.05) ; BA.getPoison().incDefChance(0.05) ; return ;
			case 82:  return ; // TODO chance permanente de recuperar a flecha de até 50%
			case 84: BA.getPhyAtk().incBaseValue(7) ; BA.getDex().incBaseValue(10) ; BA.getAgi().incBaseValue(3) ; return ;
			case 86: range *= 1.148698 ; return ;
			case 88: return ; // TODO permite fabricar flechas especiais
			case 89: return ; // TODO permite se mover durante a batalha com redução de destreza
			case 92: return ; // TODO aumenta a saciedade, comidas recuperam vida
			case 94: PA.getMp().incMaxValue(68) ; PA.getMp().setToMaximum() ; BA.getMagAtk().incBaseValue(7) ; BA.getDex().incBaseValue(3) ; BA.getAgi().incBaseValue(2) ; return ;
			case 96: return ; // TODO aumenta a resistência aos elementos em 30%
			case 99: return ; // TODO vê o elemento das criaturas
			
			case 105: BA.getDex().incBaseValue(2) ; BA.getAgi().incBaseValue(1) ; return ;
			case 107: BA.getCritAtk().incBaseValue(0.03) ; return ;
			case 108:  return ; // TODO chance de coleta em dobro em até 70%
			case 111:  return ; // TODO efeito das poções até + 30%
			case 113:
				PA.getLife().incMaxValue((int) (0.03 * PA.getLife().getMaxValue())) ;
				PA.getLife().setToMaximum() ;
				BA.getPhyAtk().incBaseValue(2) ; BA.getPhyDef().incBaseValue(2) ;
				return ;
			case 117: 
				Pet pet = Game.getPet() ;
				if (pet == null) { return ;}
				pet.getPA().getLife().incMaxValue(10) ;
				pet.getPA().getLife().setToMaximum() ;
				pet.getPA().getMp().incMaxValue(10) ;
				pet.getPA().getMp().setToMaximum() ;
				pet.getBA().getPhyAtk().incBaseValue(2) ;
				pet.getBA().getMagAtk().incBaseValue(2) ;
				pet.getBA().getDex().incBaseValue(1) ;
				pet.getBA().getAgi().incBaseValue(1) ;
				return ;
			case 118: PA.getMp().incMaxValue(48) ; PA.getMp().setToMaximum() ; BA.getPhyAtk().incBaseValue(7) ; BA.getMagAtk().incBaseValue(4) ; BA.getDex().incBaseValue(2) ; BA.getAgi().incBaseValue(3) ; return ;
			case 128: PA.getLife().incMaxValue(28) ; PA.getLife().setToMaximum() ; BA.getPhyAtk().incBaseValue(11) ; BA.getPhyDef().incBaseValue(4) ; BA.getDex().incBaseValue(3) ; BA.getAgi().incBaseValue(5) ; return ;	
			case 129: return ; // TODO aumenta a velocidade de ataque
			case 133: return ; // TODO Se a criatura estiver com a vida abaixo de 30%, os ataques físicos e mágicos do Selvagem aumentam em 30%
			
			case 139: BA.getAgi().incBaseValue(1) ; return ;
			case 142: BA.getPhyAtk().incBaseValue(3) ; BA.getDex().incBaseValue(1) ; return ;
			case 144:  return ; // TODO aumenta em 20% a chance de obter itens raros na escavação
			case 146:  return ; // TODO permite o uso de itens na batalha
			case 147:  return ; // TODO permite a fabricação de poções venenosas
			case 151: BA.getPoison().incBasicDef(1); return ;
			case 152: PA.getLife().incMaxValue(10) ; PA.getLife().setToMaximum() ; BA.getPhyAtk().incBaseValue(6) ; BA.getPhyDef().incBaseValue(6) ; BA.getDex().incBaseValue(3) ; BA.getAgi().incBaseValue(10) ; return ;
			case 155: return ; // TODO chance de 30% de atacar sem gastar o turno
			case 156: BA.getStun().incDefChance(0.1) ; return ;
			case 162: PA.getLife().incMaxValue(16) ; PA.getLife().setToMaximum() ; BA.getPhyAtk().incBaseValue(3) ; BA.getPhyDef().incBaseValue(3) ; BA.getDex().incBaseValue(5) ; BA.getAgi().incBaseValue(6) ; return ;
			case 163: return ; // TODO aumenta o ganho de ouro em 20%
			case 165: return ; // TODO 50% do dano recebido de envenenamento é convertido em vida
			case 168: return ; // TODO aumenta o multiplicador de ataque em 5%
			
			default: return ;
			
		}
	}

	private void useSupportSpell(Spell spell)
	{	
		
		spell.activate() ;		
		resetBattleActions() ;
		System.out.println("Player used support spell");
		PA.getMp().decTotalValue(spell.getMpCost()) ;
		switch (spell.getId())
		{
			case 4: 
				break ;
			
			default: break ;
		}

		
		spell.applyBuffs(true, this) ;
		
		if (opponent == null) { return ;}
		
		spell.applyNerfs(true, opponent) ;
		// TODO support spells 
//		int spellIndex = spells.indexOf(spell) ;
//		if (job == 0)
//		{
//			if (spellIndex == 4)
//			{
//				// eternal life
//			}
//			if (proJob == 1 & spellIndex == 20)
//			{
//				// nobility
//			}
//			if (proJob == 2 & spellIndex == 20)
//			{
//				// spiky
//			}
//			if (proJob == 2 & spellIndex == 22)
//			{
//				// human shield
//			}
//			if (proJob == 2 & spellIndex == 23)
//			{
//				// salvation
//			}
//		}
//		if (job == 1)
//		{
//			if (spellIndex == 9)
//			{
//				// swamp
//			}
//			if (spellIndex == 10)
//			{
//				// cure
//				getLife().incCurrentValue(5 * spell.getLevel()) ;
//			}
//			if (spellIndex == 11)
//			{
//				// mystical inspiration
//			}
//		}
		
	}

	public void useAutoSpells(boolean activate)
	{
		for (Spell spell : spells)
		{
			if (!spell.getType().equals(SpellTypes.auto)) { continue ;}
			if (spell.getLevel() <= 0) { continue ;}
			if (spell.isActive() & activate) { continue ;}
			if (!spell.isActive() & !activate) { continue ;}
			
			switch (spell.getId())
			{
				case 116:
					if (!(PA.getLife().getRate() <= 0.2)) { return ;}
					spell.activate();
					spell.applyBuffs(activate, this) ;
					spell.applyNerfs(activate, this) ;

					return ;
				
				case 149:
					if (!UtilG.chance(0.2 * spell.getLevel())) { return ;}
					// TODO surprise atk
					return ;
					
				default: return ;
			}
		}

		// TODO steal item
//		private int StealItem(Creature creature, Item[] items, int spellLevel)
//		{	
//			int ID = (int)(UtilG.ArrayWithValuesGreaterThan(creature.getBag(), -1).length*Math.random() - 0.01) ;
//			int StolenItemID = -1 ;
//			if(-1 < creature.getBag()[ID])
//			{
//				if(Math.random() <= 0.01*items[creature.getBag()[ID]].getDropChance() + 0.01*spellLevel)
//				{
//					//Bag[creature.getBag()[ID]] += 1 ;
//					StolenItemID = items[creature.getBag()[ID]].getID() ;
//				}
//			}
//			return StolenItemID ;
			
//			return 0 ;
//		}
	}
	
	public AtkResults useOffensiveSpell(Spell spell, LiveBeing receiver)
	{
		
		int spellLevel = spell.getLevel() ;
		int spellID = spells.indexOf(spell) ;
		int damage = -1 ;
		AtkEffects effect = AtkEffects.none ;
		
		double PhyAtk = BA.TotalPhyAtk() ;
		double MagAtk = BA.TotalMagAtk() ;
		double PhyDef = receiver.getBA().TotalPhyDef() ;
		double MagDef = receiver.getBA().TotalMagDef() ;
		double AtkDex = BA.TotalDex() ;
		double DefAgi = receiver.getBA().TotalAgi() ;
		double AtkCrit = BA.TotalCritAtkChance() ;
		double DefCrit = receiver.getBA().TotalCritDefChance() ;		
		
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
		double receiverElemMod = 1 ;
		System.out.println("Player used offensive spell");
		PA.getMp().decTotalValue(spell.getMpCost()) ;
		spell.activate() ;
		
		switch (job)
		{
			case 0:
			
				BasicAtk = PhyAtk ;
				BasicDef = PhyDef ;
				
				break ;
			
			case 1:
			
				BasicAtk = MagAtk ;
				BasicDef = MagDef ;
				
				break ;
			
			case 2:
			
				double arrowAtkPower = arrowIsEquipped() ? equippedArrow.getAtkPower() : 0 ;
				if (spellID == 0 | spellID == 3 | spellID == 6 | spellID == 9 | spellID == 12)
				{
					BasicAtk = PhyAtk + arrowAtkPower ;
					BasicDef = PhyDef ;
				}
				if (spellID == 2 | spellID == 5 | spellID == 11)
				{
					BasicAtk = (PhyAtk + MagAtk) / 2.0 + arrowAtkPower ;
					BasicDef = (PhyDef + MagDef) / 2.0 ;
				}
				if (spellID == 14)
				{
					BasicAtk = MagAtk + arrowAtkPower ;
					BasicDef = MagDef ;
				}
				
				break ;
			
			case 3:
			
				BasicAtk = PhyAtk ;
				BasicDef = PhyDef ;
				
				break ;
			
			case 4:
			
				BasicAtk = PhyAtk ;
				BasicDef = PhyDef ;
				
				break ;
			
		}
		
		effect = Battle.calcEffect(DexMod[0] + AtkDex * DexMod[1], AgiMod[0] + DefAgi * AgiMod[1], AtkCrit + AtkCritMod, DefCrit + DefCritMod, BlockDef) ;
		damage = Battle.calcDamage(effect, AtkMod[0] + BasicAtk * AtkMod[1], DefMod[0] + BasicDef*DefMod[1], AtkElem, DefElem, receiverElemMod) ;

		spell.applyBuffs(true, this) ;
		spell.applyNerfs(true, opponent) ;
		return new AtkResults(AtkTypes.magical, effect, damage) ;
		
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
			bag.remove(pot, 1) ;
			
			return ;			
		}
		if (item instanceof Alchemy)
		{
			Alchemy alch = (Alchemy) item ;
			double powerMult = job == 3 ? 1.06 * spells.get(7).getLevel() : 1 ;
			
			alch.use(this, powerMult) ;
			bag.remove(alch, 1) ;
			
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
			bag.remove(food, 1);
		}
		if (item instanceof PetItem)
		{
			// TODO use pet item
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
	
	public void switchOpenClose(GameWindow win)
	{
		// Janelas deviam ficar com o Game, não com o player
		if (win.isOpen())
		{
			win.reset() ;
			win.close() ;
			openWindows.remove(win) ;
			if (openWindows.isEmpty()) { setFocusWindow(null) ; return ;}
			setFocusWindow(openWindows.get(openWindows.size() - 1)) ;
			return ;
		}

		win.open() ;
		openWindows.add(win) ;
		setFocusWindow(win) ;
	}	
	
	// called every time the window is repainted
	public void showWindows(Pet pet, Point mousePos, DrawingOnPanel DP)
	{
		openWindows.forEach(win -> win.display(mousePos, DP)) ;
	}
		
	
	// battle functions
	public void spendArrow()
	{
		if (equippedArrow == null) { return ;}		
		
		if (!UtilG.chance(0.1 * spells.get(13).getLevel()))
		{
			bag.remove(equippedArrow, 1) ;
		}

		if (!bag.contains(equippedArrow))
		{
			equippedArrow.use(this) ;
			setEquippedArrow(null) ;
		}
	}
	
	public void win(Creature creature, boolean showAnimation)
	{		
		
		List<Item> itemsObtained = new ArrayList<>() ;
		for (Item item : creature.getBag())
		{
			if (!UtilG.chance(0.01 * item.getDropChance())) { continue ;}
			
			itemsObtained.add(item) ;
			bag.add(item, 1) ;
		}		
		
		bag.addGold((int) (creature.getGold() * UtilG.RandomMult(0.1 * goldMultiplier))) ;
		PA.getExp().incCurrentValue((int) (creature.getExp().getCurrentValue() * PA.getExp().getMultiplier())) ;
		
		for (Quest quest : quests)
		{
			quest.IncReqCreaturesCounter(creature.getType()) ;
			quest.checkCompletion(bag) ;
		}
		
		if (showAnimation)
		{
			obtainItemsAnimation(itemsObtained) ;
		}
		
	}
	
	public void levelUp(Animation attIncAnimation)
	{
		if (level == maxLevel) { return ;}
		
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
		
		Game.getAnimations().get(4).start(levelUpGif.getDuration(), new Object[] {UtilG.Translate(pos, 8, 0)}) ;
		
		if (attIncAnimation == null) { return ;}
		
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
		
		increase[attributeIncrease.length] = calcExpToLevelUp(level) ;
		
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
		useAutoSpells(false) ;
		bag.addGold((int) (-0.2 * bag.getGold())) ;
		PA.getLife().setToMaximum(); ;
		PA.getMp().setToMaximum(); ;
		PA.getSatiation().setToMaximum() ;
		PA.getThirst().setToMaximum() ;
		BA.resetStatus() ;
		state = LiveBeingStates.idle ;
		if (opponent != null) { opponent.setFollow(false) ;}
		resetOpponent() ;
		resetPosition() ;
	}
	
	// TODO itemEffect
//	private void ItemEffect(int ItemID)
//	{
//		if (ItemID == 1381 | ItemID == 1382 | ItemID == 1384)
//		{
//			BA.getStatus().setBlood(0) ;
//		}
//		if (ItemID == 1411)
//		{
//			BA.getStatus().setSilence(0) ;
//		}
//		if (-1 < creatureID & BA.getBattleActions()[0][2] == 1 & isInBattle() & job == 4 & 0 < Spell[8])
//			{		
//				int ItemsWithEffectsID = -1 ;
//				for (int i = 0 ; i <= Items.ItemsWithEffects.length - 1 ; ++i)
//				{
//					if (itemID == Items.ItemsWithEffects[i])
//					{
//						ItemsWithEffectsID = i ;
//					}
//				}
//				if (-1 < ItemsWithEffectsID)
//				{
//					B.ItemEffectInBattle(getBattleAtt(), pet.getBA(), creature[creatureID].getBA(), creature[creatureID].getElem(), creature[creatureID].getLife(), items, ItemsWithEffectsID, Items.ItemsTargets[ItemsWithEffectsID], Items.ItemsElement[ItemsWithEffectsID], Items.ItemsEffects[ItemsWithEffectsID], Items.ItemsBuffs[ItemsWithEffectsID], "activate") ;
//				}
//				//Bag[itemID] += -1 ;
//				BA.getBattleActions()[0][0] = 0 ;
//				BA.getBattleActions()[0][2] = 0 ;
//			}
//			else
//			{
//				ItemEffect(itemID) ;
//				//Bag[itemID] += -1 ;
//			}
//	}
	
	@SuppressWarnings("unchecked")
	public void save(int slot)
	{

        JSONObject content = new JSONObject();
        content.put("name", name);
        content.put("sex", sex);
        content.put("level", level);
        content.put("job", job);
        content.put("PA", PA.toJsonObject()) ;
        content.put("BA", BA.toJsonObject()) ;
        
        UtilG.writeJson(content, "save " + slot) ;
        
		
	}

	public static Player load(int slot)
	{
		
		JSONObject jsonData = UtilG.readJsonObject("save " + slot + ".json") ;
		if (jsonData == null) { return null ;}
		System.out.println("jsonData " + jsonData);
		String name = (String) jsonData.get("name") ;
		String sex = (String) jsonData.get("sex") ;
		int level = (int) (long) jsonData.get("level") ;
		int job = (int) (long) jsonData.get("job") ;
		JSONObject PAData = (JSONObject) jsonData.get("PA") ;
		JSONObject BAData = (JSONObject) jsonData.get("BA") ;
		
		Player newPlayer = new Player(name, sex, job) ;
		newPlayer.setLevel(level);
		newPlayer.setPA(PersonalAttributes.fromJson(PAData));
		newPlayer.setBA(BattleAttributes.fromJson(BAData));
		
		return newPlayer ;
		
	}
	
	private void drawRange(DrawingOnPanel DP)
	{
		DP.DrawCircle(pos, (int)(2 * range), 2, null, Game.colorPalette[job]) ;
	}

	public void drawWeapon(Point pos, Scale scale, DrawingOnPanel DP)
	{
		if (equips[0] == null) { return ;}
		
		double[] angle = new double[] {-50, 30, 0, 0, 0} ;
		Point offset = new Point((int) (0.16 * size.width * scale.x), (int) (0.4 * size.height * scale.y)) ;
		Point eqPos = UtilG.Translate(pos, offset.x, -offset.y) ;
		
		equips[0].display(eqPos, angle[job], new Scale(0.6, 0.6), Align.center, DP) ;
		
	}
	
	public void display(Point pos, Scale scale, Directions direction, boolean showRange, DrawingOnPanel DP)
	{
		double angle = DrawingOnPanel.stdAngle ;
		if (isRiding)
		{
			Point ridePos = UtilG.Translate(pos, -RidingImage.getWidth(null) / 2, RidingImage.getHeight(null) / 2) ;
			DP.DrawImage(RidingImage, ridePos, angle, scale, Align.bottomLeft) ;
		}
		
		movingAni.display(direction, pos, angle, Scale.unit, DP) ;
		if (questSkills.get(QuestSkills.dragonAura))
		{
			Point auraPos = UtilG.Translate(pos, -size.width / 2, 0) ;
			DP.DrawImage(DragonAuraImage, auraPos, angle, scale, false, false, Align.bottomLeft, 0.5) ;					
		}
		if (showRange)
		{
			drawRange(DP) ;
		}
		
		displayStatus(DP) ;
	}

	
}