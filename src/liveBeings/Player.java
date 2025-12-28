package liveBeings ;

import java.awt.Dimension;
import java.awt.Image ;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList ;
import java.util.Arrays ;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import attributes.AttributeIncrease;
import attributes.Attributes;
import attributes.BasicAttribute;
import attributes.BattleAttributes;
import attributes.PersonalAttributes;
import battle.AtkEffects;
import battle.AtkResults;
import battle.AtkTypes;
import battle.Battle;
import components.Building;
import components.HitboxRectangle;
import components.NPC;
import components.Quest;
import components.QuestSkills;
import components.SpellTypes;
import graphics.Align;
import graphics.Scale;
import graphics2.Animation;
import graphics2.AnimationTypes;
import graphics2.Draw;
import graphics2.SpriteAnimation;
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
import main.Directions;
import main.Elements;
import main.Game;
import main.GamePanel;
import main.GameTimer;
import main.Path;
import maps.Collectible;
import maps.Continents;
import maps.FieldMap;
import maps.GameMap;
import maps.GroundType;
import maps.TreasureChest;
import screen.Screen;
import screen.Sky;
import utilities.Util;
import windows.BagWindow;
import windows.BankWindow;
import windows.BestiaryWindow;
import windows.CraftWindow;
import windows.ElementalWindow;
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
	private String sex ;
	
	private BagWindow bag ;
	private SettingsWindow settings ;
	private SpellsTreeWindow spellsTree ;
	private MapWindow mapWindow ;
	private List<Quest> quests ;
	private QuestWindow questWindow ;
	private HintsWindow hintsWindow ;
	private BestiaryWindow bestiary ;
	private GameWindow focusWindow ;
	private List<GameWindow> openWindows ;
	
	private int attPoints ;
	private int spellPoints ;
	private AttributeIncrease attInc ;
	private List<Double> collectLevel ;	// 0: herb, 1: wood, 2: metal
	private Equip[] equips ;			// 0: weapon, 1: shield, 2: armor, 3: emblem
	private Arrow equippedArrow ;
	private double goldMultiplier ;
	private double digBonus ;
	private Elements superElem ;
	private Map<QuestSkills, Boolean> questSkills ;
	private Set<String> keysPressed ;
	private boolean isRiding ;
	
	private Creature closestCreature ;
    private Creature opponent ;
    private NPC npcInContact ;
    private Collectible currentCollectible ;
    private TreasureChest currentChest ;
    private List<Item> hotItems ;
	private Statistics stats ;
    
	public static final int maxLevel = 99 ;
	public static final double stepDuration = 0.25 ;
	private static final double fishingChance = 0.1 ;
    private static final Image tigerImg = Game.loadImage(Path.PLAYER_IMG + "Tiger.png") ;
	private static final Image coinImg = Game.loadImage(Path.PLAYER_IMG + "CoinIcon.png") ;
	// private static final SpriteAnimation magicBlissAni = new SpriteAnimation(Path.PLAYER_IMG + "MagicBlissSprite.png", new Point(), Align.center, 5, 0.15) ;
    private static final SpriteAnimation[] collectingAnimations ;

	private static final SpriteAnimation tentAni = new SpriteAnimation(Path.PLAYER_IMG + "Tent.png", new Point(), Align.bottomCenter, false, 1, 5) ;
	private static final SpriteAnimation diggingAni = new SpriteAnimation(Path.PLAYER_IMG + "DiggingSprite.png", new Point(), Align.center, false, 7, 2/7.0) ;
	private static final SpriteAnimation fishingAni = new SpriteAnimation(Path.PLAYER_IMG + "FishingSprite.png", new Point(), Align.center, false, 11, 2/11.0) ;
    
	public static final List<String[]> initialAttributes = Util.readcsvFile(Path.CSV + "PlayerInitialStats.csv") ;
	public static final List<String[]> attributeIncreaseOnLevelUp = Util.readcsvFile(Path.CSV + "PlayerEvolution.csv") ;	
	public static final int[] numberOfSpellsPerJob = new int[] {14, 15, 15, 14, 14} ;
	private static final int[] cumNumberOfSpellsPerJob = new int[] {0, 34, 69, 104, 138} ;
    // private static final Color[] jobColors = new Color[] {Game.palette[21], Game.palette[5], Game.palette[2], Game.palette[3], Game.palette[4]} ;

    private static final String[] hotKeys = new String[] {"F", "G", "V"} ;

	private static final MovingAnimations movingAnimations ;
	
	static
	{
		collectingAnimations = new SpriteAnimation[] {
			new SpriteAnimation(Path.COLLECTABLES_IMG + "CollectingFruit.png", new Point(300, 300), Align.center, false, 12, 0.3),
			new SpriteAnimation(Path.COLLECTABLES_IMG + "CollectingHerb.png", new Point(300, 300), Align.center, false, 12, 0.3),
			new SpriteAnimation(Path.COLLECTABLES_IMG + "CollectingWood.png", new Point(300, 300), Align.center, false, 12, 0.3),
			new SpriteAnimation(Path.COLLECTABLES_IMG + "CollectingMetal.png", new Point(300, 300), Align.center, false, 12, 0.3)
		} ;
		
		movingAnimations = new MovingAnimations(
			new SpriteAnimation(Path.PLAYER_IMG + "PlayerIdle.png", new Point(0, 0), Align.bottomCenter, 1, 0.1),
			new SpriteAnimation(Path.PLAYER_IMG + "PlayerFront.png", new Point(0, 0), Align.bottomCenter, 1, 0.1),
			new SpriteAnimation(Path.PLAYER_IMG + "PlayerBack.png", new Point(0, 0), Align.bottomCenter, 1, 0.1),
			new SpriteAnimation(Path.PLAYER_IMG + "PlayerLeft.png", new Point(0, 0), Align.bottomCenter, 1, 0.1),
			new SpriteAnimation(Path.PLAYER_IMG + "PlayerRight.png", new Point(0, 0), Align.bottomCenter, 1, 0.1)
		);
	}
	
	public Player(String name, String sex, int job)
	{
		super(InitializePersonalAttributes(job), new BattleAttributes(initialAttributes.get(job), 1, initialAttributes.get(job)[41], initialAttributes.get(job)[42]), movingAnimations, new PlayerAttributesWindow()) ;

		((PlayerAttributesWindow) attWindow).initializeAttIncButtons(this) ;
		
		this.name = name ;
		this.job = job ;
		this.proJob = 0 ;
		this.level = 1 ;
		
		if (Game.getMaps() != null)
		{
			map = Game.getMaps()[job] ;
		}

		pos = new Point2D.Double();
		dir = Directions.up;
		state = LiveBeingStates.idle;
	    size = movingAni.spriteIdle.getFrameSize() ;
		range = Integer.parseInt(initialAttributes.get(job)[4]) ;
		step = Integer.parseInt(initialAttributes.get(job)[33]);
	    atkElem = Elements.neutral ;
		satiationCounter = new GameTimer(Double.parseDouble(initialAttributes.get(job)[38])) ;
		thirstCounter = new GameTimer(Double.parseDouble(initialAttributes.get(job)[39])) ;
		mpCounter = new GameTimer(Double.parseDouble(initialAttributes.get(job)[40]) / 1.0) ;
		battleActionCounter = new GameTimer(Double.parseDouble(initialAttributes.get(job)[41]) / 1.0) ;
		movingTimer = new GameTimer(stepDuration) ;
		combo = new ArrayList<>() ;
		hitbox = new HitboxRectangle(getPos(), size, 0.8) ;
	    
		this.sex = sex ;
		
		
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
		mapWindow = new MapWindow() ;
		hintsWindow = new HintsWindow() ;
		spellsTree = new SpellsTreeWindow(job) ;
		bestiary = new BestiaryWindow() ;
		equips = new Equip[4] ;
		equippedArrow = null ;
		spellPoints = 0 ;
    	
		collectLevel = new ArrayList<>(List.of(0.0, 0.0, 0.0));
		goldMultiplier = Double.parseDouble(initialAttributes.get(job)[32]) ;
		digBonus = 0 ;
		questSkills = new HashMap<QuestSkills, Boolean>() ;
		for (QuestSkills questSkill : QuestSkills.values())
		{
			questSkills.put(questSkill, false) ;
		}
		keysPressed = new HashSet<>() ;
		isRiding = false ;
		stats = new Statistics() ;
		attPoints = 0 ;
		
		attInc = calcAttributeIncrease(job, proJob) ;

		closestCreature = null ;
	    opponent = null ;
	    npcInContact = null ;
	    currentCollectible = null ;
	    currentChest = null ;
		settings = new SettingsWindow(false, true, false, 1, 1) ;
		hotItems = Arrays.asList(null, null, null) ;
	}
	

	public static PersonalAttributes InitializePersonalAttributes(int job)
	{
	    BasicAttribute life = new BasicAttribute(Integer.parseInt(initialAttributes.get(job)[2]), Integer.parseInt(initialAttributes.get(job)[2]), 1) ;
	    BasicAttribute mp = new BasicAttribute(Integer.parseInt(initialAttributes.get(job)[3]), Integer.parseInt(initialAttributes.get(job)[3]), 1) ;
		BasicAttribute exp = new BasicAttribute(0, 5, Double.parseDouble(initialAttributes.get(job)[34])) ;
		BasicAttribute satiation = new BasicAttribute(100, 100, Integer.parseInt(initialAttributes.get(job)[35])) ;
		BasicAttribute thirst = new BasicAttribute(100, 100, Integer.parseInt(initialAttributes.get(job)[36])) ;
		return new PersonalAttributes(life, mp, exp, satiation,	thirst) ;
	}
		
	
	public static List<Spell> jobSpells(int job)
	{
		List<Spell> spells = new ArrayList<>() ;	
    	int numberSpells = numberOfSpellsPerJob[job] ;

    	for (int i = 0 ; i <= numberSpells - 1 ; i += 1)
		{
    		int spellID = cumNumberOfSpellsPerJob[job] + i ;
    		
			spells.add(Spell.all.get(spellID)) ;
		}
    	
		spells.get(0).incLevel(1) ;
		
		return spells ;
	}
	
	private static AttributeIncrease calcAttributeIncrease(int job, int proJob)
	{
		List<Double> attIncrements = Arrays.asList(attributeIncreaseOnLevelUp.get(3 * job + proJob)).subList(2, 10).stream().map(p -> Double.parseDouble(p)).collect(Collectors.toList()) ;
		List<Double> incChances = Arrays.asList(attributeIncreaseOnLevelUp.get(3 * job + proJob)).subList(10, 18).stream().map(p -> Double.parseDouble(p)).collect(Collectors.toList()) ;
		AttributeIncrease attInc = new AttributeIncrease(attIncrements, incChances) ;

		return attInc ;
	}
	
	public void updateAttributeIncrease()
	{
		attInc = calcAttributeIncrease(job, proJob) ;
	}

	public List<Quest> getQuests() {return quests ;}
	public BagWindow getBag() {return bag ;}
	public Equip[] getEquips() {return equips ;}
	public Arrow getEquippedArrow() {return equippedArrow ;}
	public int getSpellPoints() {return spellPoints ;}
	public AttributeIncrease getAttInc() { return attInc ;}
	public BasicAttribute getLife() {return PA.getLife() ;}
	public BasicAttribute getMp() {return PA.getMp() ;}
	public NPC getNPCInContact() { return npcInContact ;}
	public List<Double> getCollect() {return collectLevel ;}
	public BasicAttribute getExp() {return PA.getExp() ;}
	public BasicAttribute getSatiation() {return PA.getSatiation() ;}
	public BasicAttribute getThirst() {return PA.getThirst() ;}
	public Map<QuestSkills, Boolean> getQuestSkills() {return questSkills ;}
	public Elements getSuperElem() { return superElem ;}
	public int getAttPoints() {return attPoints ;}
	public void setEquippedArrow(Arrow equippedArrow) {this.equippedArrow = equippedArrow ;}
	public SettingsWindow getSettings() {return settings ;}
	public QuestWindow getQuestWindow() {return questWindow ;}
	public MapWindow getMapWindow() {return mapWindow ;}
	public SpellsTreeWindow getSpellsTreeWindow() {return spellsTree ;}
	public Creature getOpponent() { return opponent ;}
	public List<Item> getHotItems() { return hotItems ;}
	public Statistics getStatistics() { return stats ;}
	public void setAttInc(AttributeIncrease newAttInc) { attInc = newAttInc ;}
	public void setClosestCreature(Creature creature) { closestCreature = creature ;}
	public void setGoldMultiplier(double goldMultiplier) { this.goldMultiplier = goldMultiplier ;}
	public double getDigBonus() { return digBonus ;}
	public void setFocusWindow(GameWindow W) { focusWindow = W ;}
	public void setSpells(List<Spell> spells) { this.spells = spells ;}

	public static String[] getHotKeys() { return hotKeys ;}
	public static Image getCoinImg() { return coinImg ;}

	public Point center() { return new Point((int) (pos.x), (int) (pos.y - 0.5 * size.height)) ;}
	public Point headPos() { return new Point((int) (pos.x), (int) (pos.y - size.height)) ;}

	public static boolean setIsFormed(Equip[] EquipID)
	{
		if (EquipID[0] == null | EquipID[1] == null | EquipID[2] == null) { return false ;}
		
		return (EquipID[0].getId() + 1) == EquipID[1].getId() & (EquipID[1].getId() + 1) == EquipID[2].getId() ;
	}
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
		int firstSpellID = spells.size() + cumNumberOfSpellsPerJob[job] ;
		firstSpellID += proJob == 1 ? 0 : 10 ;
		spells.addAll(Spell.all.subList(firstSpellID - 1, firstSpellID + 10)) ;
	}
	public void learnSpell(Spell spell)
	{
		if (spell == null) { System.out.println("Warn: Trying to learn a null spell") ; return ;}
		if (spell.getLevel() != 0) { System.out.println("Warn: Trying to learn a spell whose level is not 0"); return ;}
		
		spell.incLevel(1) ;
    	SpellsBar.updateSpells(getActiveSpells()) ;
	}
	
	public boolean isTalkingToNPC() { return npcInContact != null ;}
	private boolean isFocusedOnWindow() { if (focusWindow == null) { return false ;} return focusWindow.isOpen() ;}
	public boolean weaponIsEquipped() { return (equips[0] != null) ;}
	public boolean arrowIsEquipped() { return (equippedArrow != null) ;}
	private boolean actionIsAMove() { return Game.arrowKeys.contains(currentAction) || List.of("W", "A", "S", "D").contains(currentAction) ;}
	private boolean hitCreature() { return (usedPhysicalAtk() | usedSpell()) & closestCreature != null ;}	
	public boolean shouldLevelUP() {return getExp().getMaxValue() <= getExp().getCurrentValue() ;}
	private boolean canThrowItem(GeneralItem item)
	{
		if (job != 4) { return false ;}
		if (!item.isThrowable()) { return false ;}
		
		if (64 <= Arrays.asList(GeneralItem.getAll()).indexOf(item) & spells.get(8).getLevel() <= 0) { return false ;}
		if (70 <= Arrays.asList(GeneralItem.getAll()).indexOf(item) & spells.get(8).getLevel() <= 1) { return false ;}
		if (114 <= Arrays.asList(GeneralItem.getAll()).indexOf(item) & spells.get(8).getLevel() <= 2) { return false ;}
		if (123 <= Arrays.asList(GeneralItem.getAll()).indexOf(item) & spells.get(8).getLevel() <= 3) { return false ;}
		if (132 <= Arrays.asList(GeneralItem.getAll()).indexOf(item) & spells.get(8).getLevel() <= 4) { return false ;}
		
		return true ;
	}
	public boolean hasSuperElement()
	{
		if (equips[0] == null | equips[1] == null | equips[2] == null) { return false ;}
		
		return equips[0].getElem().equals(equips[1].getElem()) & equips[1].getElem().equals(equips[2].getElem()) ;
	}
	public boolean hasWindowOpen() { return focusWindow != null && focusWindow.isOpen() ;}

	public Elements[] atkElems() { return new Elements[] {atkElem, equips[0] != null ? equips[0].getElem() : null, superElem} ;}
	public Elements[] defElems() { return new Elements[] {equips[1] != null ? equips[1].getElem() : null, equips[2] != null ? equips[2].getElem() : null} ;}
	public void updateSuperElem()
	{
		superElem = hasSuperElement() ? equips[0].getElem() : Elements.neutral ;
	}
	public Creature closestCreatureInRange()
	{
		if (!map.isField()) { return null ;}

		FieldMap fieldMap = (FieldMap) map ;
		if (!fieldMap.hasCreatures()) { return null ;}

		Creature closestCreature = null ;
		double minDist = Game.getScreen().getSize().width ;
		for (Creature creature : fieldMap.getCreatures())
		{
			double dist = pos.distance(creature.getPos()) ;
			if (dist <= range && dist < minDist)
			{
				minDist = dist ;
				closestCreature = creature ;
			}
		}

		return closestCreature ;
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
		
		double currentLevel = collectLevel.get(collectible.typeNumber() - 1) ;
		collectLevel.set(collectible.typeNumber() - 1, currentLevel + 0.25 / (currentLevel + 1)) ;
		
	}
	
	private void removeCollectibleFromMap(Collectible collectible)
	{        
		if (!map.isField()) { return ;}

		((FieldMap) map).removeCollectible(collectible) ;
	}

	public void collect(Collectible collectible)
    {
		
		SpriteAnimation collectingAnimation = collectingAnimations[collectible.typeNumber()] ;
		if (!collectingAnimation.isActive() && !collectingAnimation.hasFinished())
		{
			collectingAnimation.setPos(new Point((int) pos.x, (int) (pos.y - size.height))) ;
			collectingAnimation.activate() ;
			return ;
		}

		if (collectingAnimation.isActive() && !collectingAnimation.hasFinished()) { return ;}
        
        boolean isBerry = collectible.typeNumber() == 0 ;
        boolean collectSuccessful = isBerry ? true : Util.chance(collectible.chance(level)) ;        
    	String msg = collectSuccessful ? collectible.getName() : "Falha na coleta" ;
        
        if (collectSuccessful)
        {
        	bag.add(collectible.getItem(), 1) ;
        	if (job == 3)
        	{
            	useAutoSpell(true, spells.get(4)) ;
        	}
        	trainCollecting(collectible) ;
        	Animation.start(AnimationTypes.obtainedItem, new Object[] {new Point((int)pos.x, (int)(pos.y - 20 - size.height)), msg, Game.palette[0]});
        }
        else
        {
        	Animation.start(AnimationTypes.message, new Object[] {Game.getScreen().pos(0.2, 0.2), msg, Game.palette[0]});
        }

    	removeCollectibleFromMap(collectible) ;

    	setState(LiveBeingStates.idle) ;
        currentCollectible = null ;

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
//			obtainGoldAnimation(currentChest.getGoldReward()) ; proTODO arte - chest animation
		}
		
		if (!currentChest.getItemRewards().isEmpty())
		{
//			obtainItemsAnimation(currentChest.getItemRewards()) ;
		}
    	
        state = LiveBeingStates.idle ;
        currentChest = null ;
    }
	
	public void decAttPoints(int amount) {attPoints += -amount ;}

	public void applyAdjacentGroundEffect()
	{
		if (isInside(GroundType.lava) & superElem != null)
		{
			if (!superElem.equals(Elements.fire))
			{
				PA.getLife().decTotalValue(5) ;
			}
		}
		if (isTouching(GroundType.water))
		{
			PA.getThirst().incCurrentValue(1) ;
		}
	}
	
	private static int calcNewYGoingLeft(boolean newMapIsAtTop, Double posY)
	{
		int screenH = Game.getScreen().getSize().height ;
		if (newMapIsAtTop)
		{
			double a = (screenH - Sky.height) / (double) (screenH / 2 - Sky.height) ;
			return (int) (Sky.height + a * (posY - Sky.height)) ;
		}

		double a = (screenH - Sky.height) / (double) (screenH / 2) ;
		return (int) (Sky.height + (screenH - Sky.height) * (1 - a) + a * (posY - Sky.height)) ;
	}
	
	private static int calcNewYGoingRight(boolean currentMapIsAtTop, Double posY)
	{
		int screenH = Game.getScreen().getSize().height ;
		if (currentMapIsAtTop)
		{
			double a = (screenH / 2 - Sky.height) / (double) (screenH - Sky.height) ;
			return (int) (Sky.height + a * (posY - Sky.height)) ;
		}

		double a = (screenH / 2) / (double) (screenH - Sky.height) ;
		return (int) (Sky.height + (screenH / 2 - Sky.height) + a * (posY - Sky.height)) ;
	}
	
	private static Point2D.Double calcNewMapPos(Point2D.Double pos, Directions dir, GameMap currentMap, GameMap newMap)
	{
		int[] screenBorder = Game.getScreen().getBorders() ;
		Dimension screenSize = Game.getScreen().mapSize() ;
		boolean leftSide = pos.x <= Game.getScreen().getSize().width / 2 ;
		int stepOffset = Screen.borderOffset ;

		switch (dir)
		{
			case up:
				if (!currentMap.meetsTwoMapsUp()) { return new Point2D.Double(pos.x, screenBorder[3] - stepOffset) ;}
				return leftSide ? new Point2D.Double(pos.x + screenSize.width / 2 - 1, screenBorder[3] - stepOffset) : new Point2D.Double(pos.x - screenSize.width / 2, screenBorder[3] - stepOffset) ;				
			
			case left:
				int newX = screenBorder[2] - stepOffset ;
				if (!currentMap.meetsTwoMapsLeft())
				{
					if (!newMap.meetsTwoMapsRight())
					{
						return new Point2D.Double(newX, pos.y) ;
					}
				}
				int newY = calcNewYGoingLeft(Arrays.asList(Game.getMaps()).indexOf(newMap) == currentMap.getConnections()[2], pos.y) ;

				return new Point2D.Double(newX, newY) ;
			
			case down:
				if (!currentMap.meetsTwoMapsDown()) { return new Point2D.Double(pos.x, screenBorder[1] + stepOffset) ;}
				return leftSide ? new Point2D.Double(pos.x + screenSize.width / 2 - 1, screenBorder[1] + stepOffset) : new Point2D.Double(pos.x - screenSize.width / 2, screenBorder[1] + stepOffset) ;			
			
			case right:
				int newRX = screenBorder[0] + stepOffset ;
				if (!currentMap.meetsTwoMapsRight())
				{
					if (!newMap.meetsTwoMapsLeft())
					{
						return new Point2D.Double(newRX, pos.y) ;
					}
					
					int newRY = calcNewYGoingRight(Arrays.asList(Game.getMaps()).indexOf(currentMap) == newMap.getConnections()[2], pos.y) ;
					return new Point2D.Double(newRX, newRY) ;
				}
//				return topSide ? new Point(newRX, currentPos.y + screenH / 2 - 1) : new Point(screenBorder[0] + stepOffset, currentPos.y - screenH / 2) ;
			
			default: return null ;
		}
	}
	
	private void moveToNewMap(Point2D.Double pos, Directions dir, GameMap currentMap)
	{
		GameMap newMap = calcNewMap(pos, dir, currentMap) ;
		
		if (newMap == null) { System.out.println("Warn: trying to move to null map") ; return ;}
		if (!newMap.getContinent().equals(Continents.forest)) { System.out.println("Warn: trying to leave the forest"); return ;}
		
		Point2D.Double newPos = calcNewMapPos(pos, dir, currentMap, newMap) ;

		leaveBattle() ;
		setMap(newMap) ;
		setPos(newPos) ;
	}
	
	public void addKeyPressed(String key) { keysPressed.add(key) ;}
	public void removeKeyPressed(String key) { keysPressed.remove(key) ;}

	public boolean isMoving() { return keysPressed.stream().anyMatch(Game.arrowKeys::contains) ;}

	public void move(Pet pet, double dt)
	{
		Point2D.Double newPos = calcNewPos(dt) ;

		if (Game.getScreen().posIsWithinBorders(newPos))
		{
			if (!map.groundIsWalkable(new Point((int) newPos.x, (int) newPos.y), superElem)) { return ;}
			
			setPos(newPos) ;
			if (levelUpAni.isActive())
			{
				levelUpAni.setPos(getPos());
			}
			
			return ;
		}

		moveToNewMap(pos, dir, map) ;

		if (pet != null)
		{
			pet.setPos(pos) ;
		}
		if (opponent != null)
		{
			opponent.setChasePlayer(false) ;
		}
		resetClosestCreature() ;
		resetOpponent() ;		
	}

	public void engageInFight(Creature newOpponent)
	{
		opponent = newOpponent ;
		setState(LiveBeingStates.fighting) ;
		battleActionCounter.start() ;
		
		opponent.setChasePlayer(true) ;
		opponent.setState(LiveBeingStates.fighting);
		opponent.getBattleActionCounter().start() ;
		if (Game.getPet() != null)
		{
			Game.getPet().getBattleActionCounter().start() ;
		}
	}

	public void leaveBattle()
	{
		if (opponent != null)
		{
			opponent.leaveBattle() ;
		}
		setState(LiveBeingStates.idle) ;
		resetOpponent() ;
		resetClosestCreature() ;
	}
	
	public void fish()
	{
		if (!fishingAni.isActive() && !fishingAni.hasFinished())
		{
			Point fishingPos = switch (dir)
			{
				case left -> Util.translate(getPos(), -Player.fishingAni.getFrameSize().width, 0) ;
				case right -> Util.translate(getPos(), Player.fishingAni.getFrameSize().width, 0) ;
				case up -> Util.translate(getPos(), 0, -Player.fishingAni.getFrameSize().height) ;
				case down -> Util.translate(getPos(), 0, Player.fishingAni.getFrameSize().height) ;
			};
			fishingAni.setPos(fishingPos);
			fishingAni.activate() ;
		}
		
		if (!Player.fishingAni.hasFinished()) { return ;}

		Item worm = GeneralItem.getAll()[25] ;
		double successChance = fishingChance ;
		if (bag.contains(worm))
		{
			successChance += 0.1 ;
			bag.remove(worm, 1) ;
		}
		
    	setState(LiveBeingStates.idle) ;
    	
		if (!Util.chance(successChance))
		{
			Animation.start(AnimationTypes.message, new Object[] {Game.getScreen().pos(0.3, 0.2), "Não pegou peixe", Game.palette[0]}) ;
			return ;
		}
		
		int fishType = Util.randomInt(6, 8) ;
		Item fish = Food.getAll()[fishType] ;
		bag.add(fish, 1) ;
		Animation.start(AnimationTypes.obtainedItem, new Object[] {Game.getScreen().pos(0.3, 0.2), fish.getName(), Game.palette[0]}) ;
		
	}

	private Item determineDiggedItem()
	{
		List<Item> listItems = new ArrayList<Item>(map.getDiggingItems().keySet()) ;
		List<Double> listChances = new ArrayList<Double>(map.getDiggingItems().values()) ;

		int itemID = Util.randomFromChanceList(listChances) ;
		if (job == 4 & 1 <= spells.get(6).getLevel())
		{
			while (3 <= listChances.get(itemID))
			{
				if (Util.chance(0.04 * spells.get(6).getLevel()))
				{
					itemID = Util.randomFromChanceList(listChances) ;
				}
			}
		}
		
		return listItems.get(itemID) ;
	}
	
	private void dig()
	{
		if (diggingAni.isActive() && !diggingAni.hasFinished()) { return ;}

		setState(LiveBeingStates.idle) ;
		
		if (map.getDiggingItems().isEmpty()) { return ;}
		
//		List<Item> listItems = new ArrayList<Item>(map.getDiggingItems().keySet()) ;
//		List<Double> listChances = new ArrayList<Double>(map.getDiggingItems().values()) ;
//
//		int itemID = Util.randomFromChanceList(listChances) ;
//		if (job == 4 & 1 <= spells.get(6).getLevel())
//		{
//			while (3 <= listChances.get(itemID))
//			{
//				if (Util.chance(0.04 * spells.get(6).getLevel()))
//				{
//					itemID = Util.randomFromChanceList(listChances) ;
//				}
//			}
//		}

		Item diggedItem = determineDiggedItem() ;
		
		bag.add(diggedItem, 1) ;
		Animation.start(AnimationTypes.obtainedItem, new Object[] {Game.getScreen().pos(0.2, 0.2), diggedItem.getName(), Game.palette[0]}) ;
		
		if (superElem == Elements.earth)
		{
			Item diggedItemExtra = determineDiggedItem() ;
			
			bag.add(diggedItemExtra, 1) ;
			Animation.start(AnimationTypes.obtainedItem, new Object[] {Game.getScreen().pos(0.2, 0.25), diggedItemExtra.getName(), Game.palette[0]}) ;
		}
	}
	
	public void tent()
	{
		if (tentAni.isActive()) { return ;}
		
		tentAni.setPos(getPos()) ;
		tentAni.activate() ;
		
		if (!tentAni.hasFinished()) { return ;}
		
		PA.getLife().setToMaximum() ;
		PA.getMp().setToMaximum() ;
		setState(LiveBeingStates.idle) ;
	}
	
	private void playerActions(Pet pet)
	{

		PlayerActions action = PlayerActions.actionOfKey(currentAction) ;
		
		if (action == null) { return ;}

		switch (action)
		{
			case bag: switchOpenClose(bag) ; return ;
			
			case attWindow:
				((PlayerAttributesWindow) attWindow).setPlayer(this) ;
				((PlayerAttributesWindow) attWindow).updateAttIncButtons(this) ;
				switchOpenClose(attWindow) ;
				return ;
				
			case interact:
				if (!bag.contains(Item.allItems.get(1340)) | !isTouching(GroundType.water)) { return ;}
				setState(LiveBeingStates.fishing) ; return ;
				
			case map:
				if (!questSkills.get(QuestSkills.getContinentMap(map.getContinent().name()))) { return ;}
				mapWindow.setPlayerPos(getPos()) ;
				mapWindow.setCurrentMap(map) ;
				switchOpenClose(mapWindow) ; return ;
				
			case pet:
				if (pet == null) { return ;}
				((PetAttributesWindow) pet.getAttWindow()).setPet(pet) ;
				switchOpenClose(pet.getAttWindow()) ; return ;
				
			case quest: 
				questWindow.setQuests(quests) ;
				questWindow.setBag(bag) ;
				switchOpenClose(questWindow) ; return ;
				
			case hints: switchOpenClose(hintsWindow) ; return ;
			
			case ride:
				if (!questSkills.get(QuestSkills.ride)) { return ;}				
				activateRide() ;				
				return ;
				
			case tent:
				if (isFighting()) { return ;}		
				setState(LiveBeingStates.sleeping) ;
				return ;
				
			case dig: 
				if (!questSkills.get(QuestSkills.dig)) { return ;}
				diggingAni.setPos(getPos()) ;
				diggingAni.activate() ; 
				setState(LiveBeingStates.digging) ;
				return ;
			
			case bestiary: 
				if (!questSkills.get(QuestSkills.bestiary)) { return ;}
				switchOpenClose(bestiary) ; return ;

			default: return ;
		}
		
	}
	
	public void doCurrentAction()
	{		
		switch (state)
		{
			case collecting: collect(currentCollectible) ; return ;
			case fishing: fish() ; return ;
			case openingChest: openChest() ; return ;
			case digging: dig() ; return ;
			case sleeping: tent() ; return ;
			default: return ;
		}
	}
	
	public void chooseDirection(String action)
	{
		if (action == null) { return ;}

		if (action.equals(KeyEvent.getKeyText(KeyEvent.VK_UP)) | action.equals("W"))
		{
			setDir(Directions.up) ;
		}
		if (action.equals(KeyEvent.getKeyText(KeyEvent.VK_LEFT)) | action.equals("A"))
		{
			setDir(Directions.left) ;
		}
		if (action.equals(KeyEvent.getKeyText(KeyEvent.VK_DOWN)) | action.equals("S"))
		{
			setDir(Directions.down) ;
		}
		if (action.equals(KeyEvent.getKeyText(KeyEvent.VK_RIGHT)) | action.equals("D"))
		{
			setDir(Directions.right) ;
		}
	}

	public void acts(Pet pet, Point mousePos)
	{
		if (currentAction == null) { return ;}
		
		// I like to move it, move it!
		if (actionIsAMove())
		{
			chooseDirection(currentAction) ;
			if (!isFocusedOnWindow())
			{
				restartMoving() ;
			}
			
		}

		playerActions(pet) ;
		
		
		// using spells
		if (actionIsSpell() & !isFighting())
		{
			Spell spell = getActiveSpells().get(spellKeys.indexOf(currentAction));
			if (canUseSpell(spell))
			{
				useSpell(spell, this) ;
			}
		}

		if (hitCreature() & !isFighting())
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
		
		// navigating through open windows
		if (focusWindow != null)
		{
			if (focusWindow.isOpen())
			{
				if (currentAction.equals("Escape"))
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
				((CraftWindow) focusWindow).act(bag, mousePos, currentAction, this) ;
			}
			if (focusWindow instanceof ElementalWindow)
			{
				((ElementalWindow) focusWindow).act(bag, currentAction, this) ;
				return ;
			}
			if (focusWindow instanceof ForgeWindow)
			{
				((ForgeWindow) focusWindow).act(currentAction) ;
			}
		}
		
		
		// using hotItems
		for (int i = 0; i <= hotKeys.length - 1 ; i += 1)
		{
			if (!currentAction.equals(hotKeys[i]) | hotItems.get(i) == null) { continue ;}
			
			useItem(hotItems.get(i)) ;
		}
		
		
		updateCombo() ;
		
	}

	public void meetWithTreasureChests()
	{
		if (!map.isSpecial()) { return ;}

		List<TreasureChest> chests = map.getMapElem().stream().filter(elem -> elem instanceof TreasureChest).map(elem -> (TreasureChest) elem).collect(Collectors.toList()) ;
		for (TreasureChest chest : chests)
		{

			if (!hitbox.overlaps(chest.getHitbox())) { continue ;}

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
			
			Collectible collectible = collectibles.get(i) ;
			if (!hitbox.overlaps(collectible.getHitbox())) { continue ;}
			
			if (0 < collectible.typeNumber())
			{
				if (collectLevel.get(collectible.typeNumber() - 1) + 1 < map.getLevel())
				{
					if (Animation.getAll().isEmpty())
					{
						Animation.start(AnimationTypes.message, new Object[] {new Point((int)pos.x, (int)(pos.y - 20 - size.height)), "Nível de coleta insuficiente", Game.palette[4]}) ;
					}
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
		List<Creature> creaturesInMap = map.getCreatures() ;
		for (Creature creature : creaturesInMap)
		{
			if (isInCloseRange(creature.getPosAsDouble()))
			{
				engageInFight(creature) ;
				bestiary.addDiscoveredCreature(opponent.getType()) ;
			}
		}
	}

	public void meetWithNPCs(Point mousePos)
	{
		if (npcInContact != null)
		{
			if (!hitbox.overlaps(npcInContact.getHitbox()))
			{
				npcInContact = null ;
			}
		}

		if (PlayerActions.actionOfKey(currentAction) == null) { return ;}
		if (!PlayerActions.actionOfKey(currentAction).equals(PlayerActions.interact)) { return ;}		

		if (npcInContact != null) { npcInContact = null ; return ;}
		
		if (map.getNPCs() != null)
		{
			for (NPC npc : map.getNPCs())
			{
				if (!hitbox.overlaps(npc.getHitbox())) { npc.resetMenu() ; continue ;}
				
				npcInContact = npc ;
				npcInContact.resetMenu();
				
				break ;
			}
		}
		
		if (map.getBuildings() == null) { return ;}
		
		for (Building building : map.getBuildings())
		{
			for (NPC npc : building.getNPCs())
			{				
				if (!hitbox.overlaps(npc.getHitbox())) { npc.resetMenu() ; continue ;}

				npcInContact = npc ;
				npcInContact.resetMenu();
				
				break ;
			}
		}
	}
	
	public void talkToNPC(Point mousePos)
	{
		npcInContact.action(this, Game.getPet(), mousePos) ;
	}
	
	public void checkMeet(Point mousePos)
	{
		if (isFighting()) { return ;}
		if (state != LiveBeingStates.idle & !isMoving() ) { return ;}

		meetWithTreasureChests() ;
		
		if (map.isField())
		{
			meetWithCollectibles((FieldMap) map) ;
			meetWithCreatures((FieldMap) map) ;			
		}	
		
		meetWithNPCs(mousePos) ;
	}

	public AtkResults useSpell(Spell spell, LiveBeing receiver)
	{
		if (!canUseSpell(spell)) { return null ;}
		
		spell.getCooldownCounter().start() ;
		trainOffensive(new AtkResults(AtkTypes.magical)) ;
		stats.incNumberMagAtk() ;
		
		spell.activate() ;
		PA.getMp().decTotalValue(spell.getMpCost()) ;
		if (job == 1 & 1 <= spells.get(8).getLevel())
		{
			useAutoSpell(true, spells.get(8)) ;
		}
		
		switch (spell.getType())
		{
			case support : useSupportSpell(spell, receiver) ; return new AtkResults(AtkTypes.magical) ;
			case offensive : return useOffensiveSpell(spell, receiver) ;
			default : return new AtkResults(AtkTypes.magical) ;
		}
		
	}
	
	public void applyPassiveSpell(Spell spell)
	{
		switch (spell.getId())
		{
			case 1: PA.getLife().incMaxValue(10 + (int) (0.05 * PA.getLife().getMaxValue())) ; PA.getLife().setToMaximum() ; return ;
			case 2: BA.getPhyAtk().incBaseValue(2 + (int) (0.04 * BA.getPhyAtk().getBaseValue())) ; return ;
			case 3: BA.getPhyDef().incBaseValue(2 + (int) (0.04 * BA.getPhyDef().getBaseValue())) ; return ;
			case 7: BA.getBlood().incBasicDef(0.4 + 0.05 * BA.getBlood().getBasicDef()) ; return ;
			case 10: BA.setElemResistance(Elements.neutral, 1 - 0.03 * spell.getLevel()); return ;
			case 11: BA.getPhyAtk().incBaseValue(2) ; BA.getDex().incBaseValue(1) ; BA.getAgi().incBaseValue(1) ; return ;
			case 14: PA.getLife().incMaxValue(202) ; PA.getLife().setToMaximum() ; BA.getPhyAtk().incBaseValue(9) ; BA.getPhyDef().incBaseValue(5) ; return ;
			case 18: BA.getDex().incBaseValue(3) ; BA.getAgi().incBaseValue(3) ; BA.getStun().incAtkChance(0.04) ; return ;
//			case 19: return ; // proTODO aumenta bônus do atk físico da arma em até 50%
//			case 23: return ; // proTODO aumenta o efeito do treinamento em 20%
			case 24: PA.getLife().incMaxValue(202) ; PA.getLife().setToMaximum() ; BA.getPhyAtk().incBaseValue(5) ; BA.getPhyDef().incBaseValue(9) ; return ;
			case 26: BA.getBlock().incAtkChance(0.06) ; return ;
			case 27: BA.getMagDef().incBaseValue(0.1 * BA.getMagDef().getBaseValue()) ; return ;
			case 28: BA.getCritDef().incBaseValue(0.15) ; return ;
			case 29: BA.getPoison().incDefChance(0.1) ; return ;
			
			case 36: BA.getMagAtk().incBaseValue(2 + (int) (0.04 * BA.getMagAtk().getBaseValue())) ; BA.getMagDef().incBaseValue(2 + (int) (0.04 * BA.getMagDef().getBaseValue())) ; return ;
			case 49: PA.getMp().incMaxValue(202) ; PA.getMp().setToMaximum() ; BA.getMagAtk().incBaseValue(9) ; BA.getMagDef().incBaseValue(5) ; return ;
//			case 54: return ; // proTODO reduz o cooldown das magias em 50%
//			case 57: return ; // proTODO descobre o próximo movimento da criatura com chance de 70%
			case 59: PA.getMp().incMaxValue(202) ; PA.getMp().setToMaximum() ; BA.getMagAtk().incBaseValue(5) ; BA.getMagDef().incBaseValue(9) ; return ;
//			case 62: return ; // proTODO regen de até 5% da vida, consome mp
//			case 65: return ; // proTODO chance de converter até 100% do dano mágico recebido em vida
			
			case 70: BA.getDex().incBaseValue(1) ; BA.getAgi().incBaseValue(0.4) ; return ;
			case 73:  return ; // permite o uso de flechas mais poderosas
			case 76: BA.getMagAtk().incBaseValue(1 + (int) (0.02 * BA.getMagAtk().getBaseValue())) ; BA.getPhyAtk().incBaseValue(1 + (int) (0.02 * BA.getPhyAtk().getBaseValue())) ; return ;
			case 77:  return ; // permite a fabricação de flechas elementais
			case 79: BA.getBlood().incDefChance(0.05) ; BA.getPoison().incDefChance(0.05) ; return ;
			case 82:  return ; // chance permanente de recuperar a flecha de até 50%
			case 84: BA.getPhyAtk().incBaseValue(7) ; BA.getDex().incBaseValue(10) ; BA.getAgi().incBaseValue(3) ; return ;
			case 86: range *= 1.148698 ; return ;
//			case 88: return ; // proTODO permite fabricar flechas especiais
//			case 89: return ; // proTODO permite se mover durante a batalha com redução de destreza
//			case 92: return ; // proTODO aumenta a saciedade, comidas recuperam vida
//			case 94: PA.getMp().incMaxValue(68) ; PA.getMp().setToMaximum() ; BA.getMagAtk().incBaseValue(7) ; BA.getDex().incBaseValue(3) ; BA.getAgi().incBaseValue(2) ; return ;
//			case 96: return ; // proTODO aumenta a resistência aos elementos em 30%
//			case 99: return ; // proTODO vê o elemento das criaturas
			
			case 105: BA.getDex().incBaseValue(2) ; BA.getAgi().incBaseValue(1) ; return ;
			case 107: BA.getCritAtk().incBaseValue(0.03) ; return ;
			case 108:  return ; // chance de coleta em dobro em até 70%
//			case 111:  return ; // efeito das poções até + 30%
			case 113:
				PA.getLife().incMaxValue((int) (0.03 * PA.getLife().getMaxValue())) ;
				PA.getLife().setToMaximum() ;
				BA.getPhyAtk().incBaseValue(2) ; BA.getPhyDef().incBaseValue(2) ;
				return ;
			case 117: 
				Pet pet = Game.getPet() ;
				if (pet == null) { return ;}
				pet.applyPassiveSpell(spell);
				return ;
			case 118: PA.getMp().incMaxValue(48) ; PA.getMp().setToMaximum() ; BA.getPhyAtk().incBaseValue(7) ; BA.getMagAtk().incBaseValue(4) ; BA.getDex().incBaseValue(2) ; BA.getAgi().incBaseValue(3) ; return ;
			case 128: PA.getLife().incMaxValue(28) ; PA.getLife().setToMaximum() ; BA.getPhyAtk().incBaseValue(11) ; BA.getPhyDef().incBaseValue(4) ; BA.getDex().incBaseValue(3) ; BA.getAgi().incBaseValue(5) ; return ;	
//			case 129: return ; // proTODO aumenta a velocidade de ataque
//			case 133: return ; // proTODO Se a criatura estiver com a vida abaixo de 30%, os ataques físicos e mágicos do Selvagem aumentam em 30%
			
			case 139: BA.getAgi().incBaseValue(1) ; return ;
			case 142: BA.getPhyAtk().incBaseValue(3) ; BA.getDex().incBaseValue(1) ; return ;
			case 144: digBonus += 0.04 ; return ;
			case 146:  return ; // permite o uso de itens na batalha
			case 147:  return ; // permite a fabricação de poções venenosas
			case 151: BA.getPoison().incBasicDef(1); return ;
//			case 152: PA.getLife().incMaxValue(10) ; PA.getLife().setToMaximum() ; BA.getPhyAtk().incBaseValue(6) ; BA.getPhyDef().incBaseValue(6) ; BA.getDex().incBaseValue(3) ; BA.getAgi().incBaseValue(10) ; return ;
//			case 155: return ; // proTODO chance de 30% de atacar sem gastar o turno
//			case 156: BA.getStun().incDefChance(0.1) ; return ;
//			case 162: PA.getLife().incMaxValue(16) ; PA.getLife().setToMaximum() ; BA.getPhyAtk().incBaseValue(3) ; BA.getPhyDef().incBaseValue(3) ; BA.getDex().incBaseValue(5) ; BA.getAgi().incBaseValue(6) ; return ;
//			case 163: return ; // proTODO aumenta o ganho de ouro em 20%
//			case 165: return ; // proTODO 50% do dano recebido de envenenamento é convertido em vida
//			case 168: return ; // proTODO aumenta o multiplicador de ataque em 5%
			
			default: return ;
			
		}
	}

	public void useAutoSpell(boolean activate, Spell spell)
	{
		if (!spell.getType().equals(SpellTypes.auto)) { return ;}
		if (spell.getLevel() <= 0) { return ;}
		if (spell.isActive() & activate) { return ;}
		if (!spell.isActive() & !activate) { return ;}

//		Log.spellUsed(this, spell.getName(), spell.getLevel());
		switch (spell.getId())
		{
			case 42:
				if (Util.chance(0.5)) { return ;}
				Spell lastSpellUsed = getActiveSpells().get(Integer.parseInt(currentAction)) ;
				PA.getMp().incCurrentValue((int) (0.04 * spell.getLevel()* lastSpellUsed.getMpCost())) ;
				
			case 82:
				if (Util.chance(0.1 * spell.getLevel())) { return ;}
				bag.add(equippedArrow, 1) ;

				return ;
				
			case 108:
				if (!Util.chance(0.14 * spell.getLevel())) { return ;}
	        	bag.add(currentCollectible.getItem(), 1) ;
				return ;
			
			case 116:
				if (!(PA.getLife().getRate() <= 0.2) & spell.isActive())
				{
					spell.deactivate();
					spell.applyBuffs(false, this) ;
					spell.applyDebuffs(false, this) ;
					return ;
				}
				spell.activate();
				spell.applyBuffs(true, this) ;
				spell.applyDebuffs(true, this) ;

				return ;
			
			case 149:
				if (opponent == null) { return ;}
				if (!Util.chance(0.2 * spell.getLevel())) { return ;}
				opponent.takeDamage((int) (0.4 * spell.getLevel() * BA.getPhyAtk().getTotal())) ;
				return ;
				
			default: return ;
		}
	}
	
	public AtkResults useOffensiveSpell(Spell spell, LiveBeing receiver)
	{
		if (spell == null) { return null ;}
		if (receiver == null) { return null ;}
		
		int spellLevel = spell.getLevel() ;
		int spellID = spells.indexOf(spell) ;
		
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
		double[] stunMod = new double[] {spell.getStunMod()[0] * spellLevel, 1 + spell.getStunMod()[1] * spellLevel} ;
		double[] blockMod = new double[] {spell.getBlockMod()[0] * spellLevel, 1 + spell.getBlockMod()[1] * spellLevel} ;
		double[] bloodMod = new double[] {spell.getBloodMod()[0] * spellLevel, 1 + spell.getBloodMod()[1] * spellLevel} ;
		double[] poisonMod = new double[] {spell.getPoisonMod()[0] * spellLevel, 1 + spell.getPoisonMod()[1] * spellLevel} ;
		double[] silenceMod = new double[] {spell.getSilenceMod()[0] * spellLevel, 1 + spell.getSilenceMod()[1] * spellLevel} ;
		double[] atkChances = new double[] {stunMod[0], blockMod[0], bloodMod[0], poisonMod[0], silenceMod[0]} ;
		
		double AtkCritMod = spell.getAtkCritMod()[0] * spellLevel ;
		double DefCritMod = spell.getDefCritMod()[0] * spellLevel ;
		double BlockDef = receiver.getBA().getBlock().TotalDefChance() ;
		
		double BasicAtk = 0 ;
		double BasicDef = 0 ;
		
		Elements weaponElem = equips[0] != null ? equips[0].getElem() : null ;
		Elements[] AtkElem = new Elements[] {spell.getElem(), weaponElem, superElem} ;
		Elements[] DefElem = receiver.defElems() ;
		double receiverElemMod = 1 ;
		
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

		AtkEffects effect = Battle.calcEffect(DexMod[0] + AtkDex * DexMod[1], AgiMod[0] + DefAgi * AgiMod[1], AtkCrit + AtkCritMod, DefCrit + DefCritMod, BlockDef) ;
		int damage = Battle.calcDamage(effect, AtkMod[0] + BasicAtk * AtkMod[1], DefMod[0] + BasicDef * DefMod[1], AtkElem, DefElem, receiverElemMod) ;
		double[] inflictedStatus = Battle.calcStatus(atkChances, receiver.getBA().baseDefChances(), BA.baseDurations()) ;				
		
		spell.applyBuffs(true, this) ;
		spell.applyDebuffs(true, receiver) ;
		return new AtkResults(AtkTypes.magical, effect, damage, inflictedStatus) ;
		
	}
	
	private void throwItem(GeneralItem item, LiveBeing receiver)
	{
		double itemPower = item.getPower() * (1 + 0.1 * spells.get(9).getLevel()) ;
		Battle.throwItem(this, receiver, itemPower, item.getAtkMod(), item.getElem()) ;
		bag.remove(item, 1) ;
	}
	
	public void useItem(Item item)
	{
		if (item == null) { System.out.println("Warn: Tentando usar item nulo!"); return ;}
		if (!bag.contains(item)) { System.out.println("Warn: Tentando usar item que não tem na mochila!"); return ;}

		if (item instanceof Potion)
		{
			Potion pot = (Potion) item ;
			double powerMult = job == 3 & 1 <= spells.get(7).getLevel() ? 1 + 0.06 * spells.get(7).getLevel() : 1 ;
			
			pot.use(this, powerMult) ;
			bag.remove(pot, 1) ;
			
			return ;			
		}
		if (item instanceof Alchemy)
		{
			Alchemy alch = (Alchemy) item ;
			double powerMult = job == 3 & 1 <= spells.get(7).getLevel() ? 1 + 0.06 * spells.get(7).getLevel() : 1 ;
			
			if (job == 3 & 0 < spells.get(10).getLevel() & !spells.get(10).isActive())
			{
				int alchBuffId = Alchemy.isHerb(alch.getId()) ? 10 : Alchemy.isWood(alch.getId()) ? 11 : 12 ;
				Buff alchBuff = Buff.allBuffs.get(alchBuffId) ;
				alchBuff.apply(1, spells.get(10).getLevel(), Game.getPet());
				spells.get(10).activate();
				Game.getPet().setAlchBuffId(alchBuffId) ;
			}
			
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
			if (Game.getPet() == null) { return ;}
			PetItem petItem = (PetItem) item ;
			
			petItem.use(Game.getPet()) ;
			
			return ;			
		}
		if (item instanceof Arrow)
		{
			if (job != 2) { return ;}
			
			Arrow arrow = (Arrow) item ;
			if (1 <= arrow.getId() & spells.get(4).getLevel() <= 0) { return ;}
			if (4 <= arrow.getId() & spells.get(4).getLevel() <= 1) { return ;}
			if (5 <= arrow.getId() & spells.get(4).getLevel() <= 2) { return ;}
			if (6 <= arrow.getId() & spells.get(4).getLevel() <= 3) { return ;}
			if (15 <= arrow.getId() & spells.get(4).getLevel() <= 4) { return ;}

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
			
			if (isFighting() & canThrowItem(genItem))
			{
				throwItem(genItem, opponent) ;
				
				return ;
			}
			
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
	public void showWindows(Pet pet, Point mousePos)
	{
		openWindows.forEach(win -> win.display(mousePos)) ;
	}
		
	
	public void spendArrow()
	{
		if (equippedArrow == null) { return ;}		
		
		if (!Util.chance(0.1 * spells.get(13).getLevel()))
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
			if (!Util.chance(0.01 * item.getDropChance())) { continue ;}
			
			itemsObtained.add(item) ;
			bag.add(item, 1) ;
		}		
		
		bag.addGold((int) (creature.getGold() * Util.randomMult(0.1 * goldMultiplier))) ;
		PA.getExp().incCurrentValue((int) (creature.getExp().getCurrentValue() * PA.getExp().getMultiplier())) ;
		
		for (Quest quest : quests)
		{
			quest.IncReqCreaturesCounter(creature.getType()) ;
			quest.checkCompletion(bag) ;
		};
		if (showAnimation)
		{
			Animation.start(AnimationTypes.win, new Object[] {itemsObtained}) ;
		}
		
	}
	
	public void levelUp()
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
		PA.getExp().incMaxValue((int) attIncrease[8]) ;
		PA.getLife().setToMaximum() ;
		PA.getMp().setToMaximum() ;
		spellPoints += 1 ;
		attPoints += 2 ;
		
		((PlayerAttributesWindow) attWindow).activateIncAttButtons(attPoints) ;
		
		// Animation.start(AnimationTypes.levelUp, new Object[] {attIncrease, level});
		levelUpAni.setPos(getPos()) ;
		levelUpAni.activate() ;
	}
	
	private double[] calcAttributesIncrease()
	{
		double[] increase = new double[attInc.getIncrement().basic().length + 1] ;

		for (int i = 0 ; i <= attInc.getIncrement().basic().length - 1 ; i += 1)
		{
			if (attInc.getChance().basic()[i] <= Math.random()) { continue ;}
			
			increase[i] = attInc.getIncrement().basic()[i] ;
		}
		
		increase[attInc.getIncrement().basic().length] = calcExpToLevelUp(level) ;
		
		return increase ;
	}

	private void resetPosition()
	{
		setMap(Game.getMaps()[job]) ;
		//setContinent(0) ;
		switch (job)
		{
			case 0: setPos(new Point2D.Double(340, 540)) ; break ;
			case 1: setPos(new Point2D.Double(340, 380)) ; break ;
			case 2: setPos(new Point2D.Double(40, 420)) ; break ;
			case 3: setPos(new Point2D.Double(340, 540)) ; break ;
			case 4: setPos(new Point2D.Double(340, 840)) ; break ;
		}
	}
	
	public void dies()
	{
		spells.forEach(spell -> useAutoSpell(false, spell));
		bag.addGold((int) (-0.2 * bag.getGold())) ;
		PA.getLife().setToMaximum(); ;
		PA.getMp().setToMaximum(); ;
		PA.getSatiation().setToMaximum() ;
		PA.getThirst().setToMaximum() ;
		resetStatus() ;
		state = LiveBeingStates.idle ;
		if (opponent != null)
		{
			opponent.setState(LiveBeingStates.idle) ;
			opponent.setChasePlayer(false) ;
		}
		resetOpponent() ;
		resetPosition() ;
		if (!Game.getPet().isAlive())
		{
			Game.getPet().revives() ;
		}
	}
		
	
	// TODO save and load
/*
	salvar um gameState ao invés de playerState, pois tem coisas que são do jogo:
	 - Settings
	 - Windows

	Para salvar e carregar:
	protected Map<Attributes, LiveBeingStatus> status ;
	private BagWindow bag ;
	private Map<QuestSkills, Boolean> questSkills ;
	private List<Recipe> knownRecipes ;
	private BestiaryWindow bestiary ;	
	private List<Quest> quests ;
	private Equip[] equips ;
    private Item[] hotItems ;
	private SettingsWindow settings ;

	Para inicializar:
	protected Elements[] elem ;
	private double goldMultiplier ;
	protected Hitbox hitbox ;
	protected GameMap map ;
	protected Point pos ;
	protected Directions dir ;
	protected LiveBeingStates state ;
	protected String currentAction ;
	protected AtkTypes currentAtkType ;
	private Creature closestCreature ;
   	private Creature opponent ;
	private List<GameWindow> openWindows ;
	private GameWindow focusWindow ;
    private NPC npcInContact ;
 */
	
	@SuppressWarnings("unchecked")
	public void save(int slot)
	{

        JSONObject content = new JSONObject();
        content.put("name", name);
        content.put("sex", sex);
        content.put("level", level);
        content.put("job", job);
		content.put("proJob", proJob);
		content.put("combo", combo) ;
        content.put("range", range);
		content.put("step", step) ;
		content.put("hpCounter", hpCounter.toJson()) ;
		content.put("mpCounter", mpCounter.toJson()) ;
		content.put("satiationCounter", satiationCounter.toJson()) ;
		content.put("thirstCounter", thirstCounter.toJson()) ;
		content.put("battleActionCounter", battleActionCounter.toJson()) ;
		content.put("stepCounter", movingTimer.toJson()) ;
		content.put("drunk", drunk.toJson()) ;
		content.put("digBonus", digBonus) ;
		content.put("isRiding", isRiding) ;
		content.put("collectLevel", collectLevel) ;
		content.put("attPoints", attPoints) ;
		content.put("spellPoints", spellPoints) ;
		content.put("spells", spells.stream().map(spell -> spell.toJson()).toList()) ;
		
		if (equippedArrow != null)
		{			
			content.put("equippedArrowID", equippedArrow.getId()) ;
		}
		content.put("statistics", stats.toJson()) ;
		
        content.put("PA", PA.toJsonObject()) ;
        content.put("BA", BA.toJsonObject()) ;
        for (Attributes att : Attributes.getSpecial())
        {
            content.put("status", status.get(att).toJsonObject());
        }
        Util.writeJson(content, "save " + slot) ;
        
		
	}

	@SuppressWarnings("unchecked")
	public static Player load(int slot)
	{
		
		JSONObject jsonData = Util.readJsonObject("save " + slot + ".json") ;
		if (jsonData == null) { return null ;}

		String name = (String) jsonData.get("name") ;
		String sex = (String) jsonData.get("sex") ;
		int job = (int) (long) jsonData.get("job") ;
		int level = (int) (long) jsonData.get("level") ;
		int range = (int) (long) jsonData.get("range") ;
		int step = (int) (long) jsonData.get("step") ;
		int proJob = (int) (long) jsonData.get("proJob") ;
		List<String> combo = (List<String>) jsonData.get("combo") ;
		List<Double> collectLevel = (List<Double>) jsonData.get("collectLevel") ;
		JSONObject hpCounterData = (JSONObject) jsonData.get("hpCounter") ;
		JSONObject mpCounterData = (JSONObject) jsonData.get("mpCounter") ;
		JSONObject satiationCounterData = (JSONObject) jsonData.get("satiationCounter") ;
		JSONObject thirstCounterData = (JSONObject) jsonData.get("thirstCounter") ;
		JSONObject battleActionCounterData = (JSONObject) jsonData.get("battleActionCounter") ;
		JSONObject stepCounterData = (JSONObject) jsonData.get("stepCounter") ;
		JSONObject drunkData = (JSONObject) jsonData.get("drunk") ;
		JSONObject PAData = (JSONObject) jsonData.get("PA") ;
		JSONObject BAData = (JSONObject) jsonData.get("BA") ;
		double digBonus = (double) (Double) jsonData.get("digBonus") ;
		boolean isRiding = (boolean) jsonData.get("isRiding") ;
		int attPoints = (int) (long) jsonData.get("attPoints") ;
		int spellPoints = (int) (long) jsonData.get("spellPoints") ;
		JSONArray spellsData = (JSONArray) jsonData.get("spells") ;
		Arrow equippedArrow = jsonData.get("equippedArrowID") != null ? Arrow.getAll()[(int) (long) jsonData.get("equippedArrowID")] : null ;
		Statistics stats = Statistics.fromJson((JSONObject) jsonData.get("statistics")) ;
		
		Player newPlayer = new Player(name, sex, job) ;
		newPlayer.setLevel(level);
		newPlayer.setRange(range);
		newPlayer.step = step ;
		newPlayer.proJob = proJob ;
		newPlayer.combo = combo ;
		newPlayer.collectLevel = collectLevel ;
		newPlayer.hpCounter = GameTimer.fromJson(hpCounterData) ;
		newPlayer.mpCounter = GameTimer.fromJson(mpCounterData) ;
		newPlayer.satiationCounter = GameTimer.fromJson(satiationCounterData) ;
		newPlayer.thirstCounter = GameTimer.fromJson(thirstCounterData) ;
		newPlayer.battleActionCounter = GameTimer.fromJson(battleActionCounterData) ;
		newPlayer.movingTimer = GameTimer.fromJson(stepCounterData) ;
		newPlayer.drunk = GameTimer.fromJson(drunkData) ;
		newPlayer.digBonus = digBonus ;
		newPlayer.isRiding = isRiding ;
		newPlayer.attPoints = attPoints ;
		newPlayer.spellPoints = spellPoints ;
		newPlayer.equippedArrow = equippedArrow ;
		newPlayer.stats = stats ;
		newPlayer.setSpells(spellsData.stream().map(spellData -> Spell.fromJson((JSONObject) spellData)).toList()) ;
		newPlayer.setPA(PersonalAttributes.fromJson(PAData));
		newPlayer.setBA(BattleAttributes.fromJson(BAData));

//		Map<Attributes, LiveBeingStatus> status = new HashMap<>() ; 
//		status = LiveBeingStatus.fromJson((JSONObject) jsonData.get("status")) ;
		
		return newPlayer ;
		
	}
	
	private void drawRange()
	{
		GamePanel.DP.drawCircle(getPos(), (int)(2 * range), 2, null, Game.palette[10]) ;
	}

	public void drawWeapon(Point pos, Scale scale)
	{
		if (equips[0] == null) { return ;}
		
		double[] angle = new double[] {-50, 30, 0, 0, 0} ;
		Point offset = new Point((int) (0.16 * size.width * scale.x), (int) (0.4 * size.height * scale.y)) ;
		Point eqPos = Util.translate(pos, offset.x, -offset.y) ;
		
		equips[0].display(eqPos, angle[job], new Scale(0.6, 0.6), Align.center) ;
		
	}
	
	public void display(Point pos, Scale scale, Directions direction, boolean showRange)
	{

		displayAttributes(settings.getAttDisplay()) ;
		if (isRiding)
		{
			Point ridePos = Util.translate(pos, -tigerImg.getWidth(null) / 2, tigerImg.getHeight(null) / 2) ;
			GamePanel.DP.drawImage(tigerImg, ridePos, Draw.stdAngle , scale, Align.bottomLeft) ;
		}
		if (isDrunk())
		{
			displayDrunk() ;
		}
		if (isDefending())
		{
			displayDefending() ;
		}
		if (isMoving())
		{
			movingAni.displayMoving(direction, pos, Draw.stdAngle , Scale.unit, Align.bottomCenter) ;
		}
		else
		{
			movingAni.displayIdle(pos, Draw.stdAngle , Scale.unit, Align.bottomCenter) ;
		}
		if (questSkills.get(QuestSkills.dragonAura))
		{
//			Point auraPos = Util.translate(pos, -size.width / 2, 0) ; proTODO arte - dragon aura
//			GamePanel.DP.drawImage(DragonAuraImage, auraPos, angle, scale, false, false, Align.bottomLeft, 0.5) ;					
		}
		if (showRange)
		{
			drawRange() ;
		}
		if (isFighting())
		{
			displayBattleActionCounter() ;
		}

		if (weaponIsEquipped())
		{
			drawWeapon(pos, Scale.unit) ;
		}

		displayStatus() ;
		if (Game.debugMode)
		{
			displayState() ;
			hitbox.display() ;
		}
	}
	
	public void display()
	{
		display(getPos(), Scale.unit, dir, settings.getShowAtkRange()) ;
	}


	
	public void applySuperElementEffect(Elements elem, boolean apply)
	{
		// proTODO superelementos luz: ilumina a caverna, escuridão: aura escura, trovão e neve
		Animation.start(AnimationTypes.message, new Object[] {Game.getScreen().pos(0.4, 0.2), "Super element " + (apply ? elem : Elements.neutral.toString()), Game.palette[7]}) ;
		switch (elem)
		{
			case fire:
				if (!apply) { return ;}
				
				bag.setClothOnFire() ;
				return ;
				
			case plant:
				double mult = apply ? 1.5 : 1 / 1.5 ;
				satiationCounter.setDuration(satiationCounter.getDuration() * mult) ;
				return ;
			
			default: return ;
		}
	}


	public void updateBloodAndPoisonStatistics()
	{
		double bloodMult = 1, poisonMult = 1 ;
		if (job == 4)
		{
			poisonMult += -0.1 * spells.get(13).getLevel() ;
		}
		int bloodDamage = (int) (status.get(Attributes.blood).getIntensity() * bloodMult) ;
		int poisonDamage = (int) (status.get(Attributes.poison).getIntensity() * poisonMult) ;
		if (job == 4 & 1 < spells.get(12).getLevel())
		{
			PA.getLife().incCurrentValue(bloodDamage) ;
		}
		stats.updateInflictedBlood(bloodDamage) ;		
		stats.updateInflictedPoison(poisonDamage) ;
	}



	
}