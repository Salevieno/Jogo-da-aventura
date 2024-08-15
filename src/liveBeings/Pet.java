package liveBeings ;

import java.awt.Color ;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import attributes.AttributeIncrease;
import attributes.Attributes;
import attributes.BasicAttribute;
import attributes.BasicBattleAttribute;
import attributes.BattleAttributes;
import attributes.BattleSpecialAttribute;
import attributes.BattleSpecialAttributeWithDamage;
import attributes.PersonalAttributes;
import graphics.Animation;
import graphics.AnimationTypes;
import graphics.Draw;
import graphics.DrawPrimitives;
import items.PetItem;
import libUtil.Align;
import libUtil.Util;
import main.AtkResults;
import main.AtkTypes;
import main.Battle;
import main.Game;
import maps.GameMap;
import utilities.AtkEffects;
import utilities.Directions;
import utilities.Elements;
import utilities.Scale;
import utilities.TimeCounter;
import windows.PetAttributesWindow;

public class Pet extends LiveBeing
{
	private Color color ;
	private int job ;
	private int spellPoints ;
	private double[] ElemMult ;		// [Neutral, Water, Fire, Plant, Earth, Air, Thunder, Light, Dark, Snow]
	private PetItem equip ;
	private int alchBuffId ;
	private AttributeIncrease attInc ;
	
	private static final List<String[]> initialAttributes = Util.ReadcsvFile(Game.CSVPath + "PetInitialStats.csv") ;
	private static final List<String[]> attEvolution = Util.ReadcsvFile(Game.CSVPath + "PetEvolution.csv") ;
    	
	public Pet(int Job)
	{
		super(InitializePersonalAttributes(Job), InitializeBattleAttributes(Job), initializeMovingAnimations(Job), new PetAttributesWindow()) ;
		
		name = initialAttributes.get(Job)[0] ;
		level = 1 ;
		proJob = 0 ;
		map = null ;
		pos = new Point(0, 0) ;
		dir = Directions.up ;
		state = LiveBeingStates.idle ;
		size = new Dimension (movingAni.idleGif.getWidth(null), movingAni.idleGif.getHeight(null)) ;	
		range = Integer.parseInt(initialAttributes.get(Job)[4]) ;
		step = Integer.parseInt(initialAttributes.get(Job)[32]) ;
		elem = new Elements[] {Elements.neutral, null, null, null, null} ;
		actionCounter = new TimeCounter(Double.parseDouble(initialAttributes.get(Job)[33])) ;
		satiationCounter = new TimeCounter(Double.parseDouble(initialAttributes.get(Job)[34])) ;
		mpCounter = new TimeCounter(Double.parseDouble(initialAttributes.get(Job)[35])) ;
		battleActionCounter = new TimeCounter(Double.parseDouble(initialAttributes.get(Job)[36])) ;
		stepCounter = new TimeCounter(20) ;
		combo = new ArrayList<>();
		equip = null ;
		alchBuffId = -1 ;
		
		this.job = Job ;
		Color[] colorPalette = Game.colorPalette ;
		Color[] petColors = new Color[] {colorPalette[3], colorPalette[5], colorPalette[21], colorPalette[21]} ;
		color = petColors[Job] ;
		spells = InitializePetSpells();
		spellPoints = 0 ;


		startCounters() ;
		ElemMult = new double[10] ;
		attInc = calcAttributeIncrease(job) ;
		
	}
	
	public static PersonalAttributes InitializePersonalAttributes(int Job)
	{
		BasicAttribute life = new BasicAttribute(Integer.parseInt(initialAttributes.get(Job)[2]), Integer.parseInt(initialAttributes.get(Job)[2]), 1) ;
		BasicAttribute mp = new BasicAttribute(Integer.parseInt(initialAttributes.get(Job)[3]), Integer.parseInt(initialAttributes.get(Job)[3]), 1) ;
		BasicAttribute exp = new BasicAttribute(0, 50, 1) ;
		BasicAttribute satiation = new BasicAttribute(100, 100, 1) ;
		BasicAttribute thirst = new BasicAttribute(100, 100, 1) ;
		return new PersonalAttributes(life, mp, exp, satiation, thirst) ;
	}
	
	public static BattleAttributes InitializeBattleAttributes(int Job)
	{
		BasicBattleAttribute phyAtk = new BasicBattleAttribute(Double.parseDouble(initialAttributes.get(Job)[5]), 0, 0) ;
		BasicBattleAttribute magAtk = new BasicBattleAttribute(Double.parseDouble(initialAttributes.get(Job)[6]), 0, 0) ;
		BasicBattleAttribute phyDef = new BasicBattleAttribute(Double.parseDouble(initialAttributes.get(Job)[7]), 0, 0) ;
		BasicBattleAttribute magDef = new BasicBattleAttribute(Double.parseDouble(initialAttributes.get(Job)[8]), 0, 0) ;
		BasicBattleAttribute dex = new BasicBattleAttribute(Double.parseDouble(initialAttributes.get(Job)[9]), 0, 0) ;
		BasicBattleAttribute agi = new BasicBattleAttribute(Double.parseDouble(initialAttributes.get(Job)[10]), 0, 0) ;
		BasicBattleAttribute critAtk = new BasicBattleAttribute(Double.parseDouble(initialAttributes.get(Job)[11]), 0, 0) ;
		BasicBattleAttribute critDef = new BasicBattleAttribute(Double.parseDouble(initialAttributes.get(Job)[12]), 0, 0) ;
		BattleSpecialAttribute stun = new BattleSpecialAttribute(Double.parseDouble(initialAttributes.get(Job)[13]), 0, Double.parseDouble(initialAttributes.get(Job)[14]), 0, Integer.parseInt(initialAttributes.get(Job)[15])) ;
		BattleSpecialAttribute block = new BattleSpecialAttribute(Double.parseDouble(initialAttributes.get(Job)[16]), 0, Double.parseDouble(initialAttributes.get(Job)[17]), 0, Integer.parseInt(initialAttributes.get(Job)[18])) ;
		BattleSpecialAttributeWithDamage blood = new BattleSpecialAttributeWithDamage(Double.parseDouble(initialAttributes.get(Job)[19]), 0, Double.parseDouble(initialAttributes.get(Job)[20]), 0, Integer.parseInt(initialAttributes.get(Job)[21]), 0, Integer.parseInt(initialAttributes.get(Job)[22]), 0, Integer.parseInt(initialAttributes.get(Job)[23])) ;
		BattleSpecialAttributeWithDamage poison = new BattleSpecialAttributeWithDamage(Double.parseDouble(initialAttributes.get(Job)[24]), 0, Double.parseDouble(initialAttributes.get(Job)[25]), 0, Integer.parseInt(initialAttributes.get(Job)[26]), 0, Integer.parseInt(initialAttributes.get(Job)[27]), 0, Integer.parseInt(initialAttributes.get(Job)[28])) ;
		BattleSpecialAttribute silence = new BattleSpecialAttribute(Double.parseDouble(initialAttributes.get(Job)[29]), 0, Double.parseDouble(initialAttributes.get(Job)[30]), 0, Integer.parseInt(initialAttributes.get(Job)[31])) ;
		Map<Attributes, LiveBeingStatus> status = new HashMap<>() ;

		for (Attributes att : Attributes.values())
		{
			status.put(att, new LiveBeingStatus(att)) ;
		}
		return new BattleAttributes(phyAtk, magAtk, phyDef, magDef, dex, agi, critAtk, critDef, stun, block, blood, poison, silence, status) ;
	}
	
	public static MovingAnimations initializeMovingAnimations(int Job)
	{
		String filePath = Game.ImagesPath + "\\Pet\\" + "PetType" ;
		return new MovingAnimations(Util.loadImage(filePath + String.valueOf(Job) + ".png"),
				Util.loadImage(filePath + String.valueOf(Job) + ".png"),
				Util.loadImage(filePath + String.valueOf(Job) + ".png"),
				Util.loadImage(filePath + String.valueOf(Job) + ".png"),
				Util.loadImage(filePath + String.valueOf(Job) + ".png")) ;
	}

	public List<Spell> InitializePetSpells()
    {
		int[] spellIDs = switch (job)
		{
			case 0 -> new int[] {0, 5, 6, 12, 13} ;
			case 1 -> new int[] {35, 37, 45, 47, 49} ;
			case 2 -> new int[] {72, 74, 75, 83, 148} ;
			case 3 -> new int[] {106, 109, 110, 115, 116} ;
			default -> new int[] {} ;
		};
		List<Spell> spells = new ArrayList<>() ;
		for (int id : spellIDs)
		{
			spells.add(new Spell(Spell.all.get(id))) ;
		}
		spells.get(0).incLevel(1) ;
		return spells ;
    }
	
	private static AttributeIncrease calcAttributeIncrease(int job)
	{
		List<Double> attIncrements = Arrays.asList(attEvolution.get(job)).subList(1, 9).stream().map(p -> Double.parseDouble(p)).collect(Collectors.toList()) ;
		List<Double> incChances = Arrays.asList(attEvolution.get(job)).subList(9, 17).stream().map(p -> Double.parseDouble(p)).collect(Collectors.toList()) ;
		AttributeIncrease attInc = new AttributeIncrease(attIncrements, incChances) ;
		
		return attInc ;
	}
	
	public Color getColor() {return color ;}
	public int getJob() {return job ;}
	public MovingAnimations getMovingAnimations() {return movingAni ;}
	public List<Spell> getSpells() {return spells ;}
	public int getSpellPoints() {return spellPoints ;}
	public AttributeIncrease getAttInc() { return attInc ;}
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
	public double[] getElemMult() {return ElemMult ;}
	public BasicAttribute getExp() {return PA.getExp() ;}
	public BasicAttribute getSatiation() {return PA.getSatiation() ;}
	public PetItem getEquip() { return equip ;}
	public void setEquip(PetItem equip) { this.equip = equip ;}
	public int getAlchBuffId() { return alchBuffId ;}
	public void setAlchBuffId(int alchBuffId) { this.alchBuffId = alchBuffId ;}

//	public boolean isAlive() { return 0 < PA.getLife().getCurrentValue() ;}
	public boolean shouldLevelUP() {return getExp().getMaxValue() <= getExp().getCurrentValue() ;}
	public boolean closeToPlayer(Point playerPos) { return Util.dist(pos, playerPos) <= 40 ; }
	
	public Point center() { return new Point((int) (pos.x), (int) (pos.y - 0.5 * size.height)) ;}
	public Point headPos() { return new Point((int) (pos.x), (int) (pos.y - size.height)) ;}

	public Directions newMoveDirection(Directions originalDir)
	{
		Directions newDir = randomDir() ;
		while (Directions.areOpposite(originalDir, newDir))
		{
			newDir = randomDir() ;
		}
		return newDir ;
	}
	
	private Point findNextPos(Point playerPos, Creature opponent)
	{
		if (opponent != null)
		{
			return follow(pos, opponent.getPos(), step, step) ;
		}
		else if (closeToPlayer(playerPos))
		{
			if (Util.chance(0.8)) { return pos ;}
			
			if (Util.chance(0.2))
			{
				dir = randomDir() ;
			}
			return calcNewPos(dir, pos, step) ;
		}
		else
		{
			return follow(pos, playerPos, step, step) ;
		}
	}
	
	public void think(boolean isInBattle, Point playerPos)
	{
		if (!isInBattle)
		{
			if (!closeToPlayer(playerPos)) { setState(LiveBeingStates.moving) ; return ;}
			
			if (Util.chance(0.8)) { setState(LiveBeingStates.idle) ; return ;}
			else { setState(LiveBeingStates.moving) ; return ;}
		}
		
		setState(LiveBeingStates.fighting) ;
	}
	
	public void act(Player player)
	{
		if (state.equals(LiveBeingStates.moving))
		{
			stepCounter.start() ;
			move(player.getPos(), player.getMap(), player.getOpponent(), player.getElem()[4]) ;
			return ;
		}
		
		if (state.equals(LiveBeingStates.fighting))
		{
			if (!player.getOpponent().isInRange(pos))
			{
				move(player.getPos(), player.getMap(), player.getOpponent(), player.getElem()[4]) ;
				return ;
			}
			fight() ;
		}
		
	}
	
	public void move(Point playerPos, GameMap playerMap, Creature opponent, Elements playerElem)
	{
		Point nextPos = findNextPos(playerPos, opponent) ;
		if (playerMap.groundIsWalkable(nextPos, playerElem))
		{
			setPos(nextPos) ;
		}
	}
	
	public void fight()
	{
		int move = Util.randomIntFromTo(0, 1 + getActiveSpells().size()) ;
		switch (move)
		{
			case 0: currentAction = BattleKeys[0] ; return ;
			case 1: currentAction = BattleKeys[1] ; return ;
			default: currentAction = String.valueOf(move - 2) ; return ;
		}
	}
	
	public void dies()
	{
		resetBattleActions() ;
		setPos(Game.getPlayer().getPos()) ;
	}
	
	public void applyPassiveSpell(Spell spell)
	{
		switch (spell.getId())
		{
			case 117: 
				PA.getLife().incMaxValue(10) ;
				PA.getLife().setToMaximum() ;
				PA.getMp().incMaxValue(10) ;
				PA.getMp().setToMaximum() ;
				BA.getPhyAtk().incBaseValue(2) ;
				BA.getMagAtk().incBaseValue(2) ;
				BA.getDex().incBaseValue(1) ;
				BA.getAgi().incBaseValue(1) ;
				return ;
		}
	}
	
	public void useAutoSpell(boolean activate, Spell spell)
	{
		
	}
	
	public AtkResults useSpell(Spell spell, LiveBeing receiver)
	{
		if (spell == null) { return null ;}
		if (receiver == null) { return null ;}
		
		int spellLevel = spell.getLevel() ;
		PA.getMp().decTotalValue(spell.getMpCost()) ;

		double MagAtk = BA.TotalMagAtk() ;
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
		double[] stunMod = new double[] {spell.getStunMod()[0] * spellLevel, 1 + spell.getStunMod()[1] * spellLevel} ;
		double[] blockMod = new double[] {spell.getBlockMod()[0] * spellLevel, 1 + spell.getBlockMod()[1] * spellLevel} ;
		double[] bloodMod = new double[] {spell.getBloodMod()[0] * spellLevel, 1 + spell.getBloodMod()[1] * spellLevel} ;
		double[] poisonMod = new double[] {spell.getPoisonMod()[0] * spellLevel, 1 + spell.getPoisonMod()[1] * spellLevel} ;
		double[] silenceMod = new double[] {spell.getSilenceMod()[0] * spellLevel, 1 + spell.getSilenceMod()[1] * spellLevel} ;
		double[] atkChances = new double[] {stunMod[0], blockMod[0], bloodMod[0], poisonMod[0], silenceMod[0]} ;
		
		double AtkCritMod = spell.getAtkCritMod()[0] * spellLevel ;	// Critical atk modifier
		double DefCritMod = spell.getDefCritMod()[0] * spellLevel ;	// Critical def modifier
		double BlockDef = receiver.getBA().getBlock().TotalDefChance() ;
		double BasicAtk = 0 ;
		double BasicDef = 0 ;
		Elements[] AtkElem = new Elements[] {spell.getElem(), elem[1], elem[4]} ;
		Elements[] DefElem = receiver.defElems() ;
		
		BasicAtk = MagAtk ;
		BasicDef = MagDef ;

		AtkEffects effect = Battle.calcEffect(DexMod[0] + AtkDex*DexMod[1], AgiMod[0] + DefAgi*AgiMod[1], AtkCrit + AtkCritMod, DefCrit + DefCritMod, BlockDef) ;
		int damage = Battle.calcDamage(effect, AtkMod[0] + BasicAtk*AtkMod[1], DefMod[0] + BasicDef*DefMod[1], AtkElem, DefElem, receiverElemMod) ;
		int[] inflictedStatus = Battle.calcStatus(atkChances, receiver.getBA().baseDefChances(), BA.baseDurations()) ;				
		
		displayUsedSpellMessage(spell, Game.getScreen().pos(0.23, 0.2), Game.colorPalette[12]);
		return new AtkResults(AtkTypes.magical, effect, damage, inflictedStatus) ;
	}
	
	public void win(Creature creature)
	{
		PA.getExp().incCurrentValue((int) (creature.getExp().getCurrentValue() * PA.getExp().getMultiplier())); ;
	}
	
	public void levelUp()
	{
		double[] attIncrease = calcAttributesIncrease() ;
		setLevel(level + 1) ;
		spellPoints += 1 ;
		PA.getLife().incMaxValue((int) attIncrease[0]) ;
		PA.getLife().setToMaximum() ;
		PA.getMp().incMaxValue((int) attIncrease[1]) ;	
		PA.getMp().setToMaximum() ;
		BA.getPhyAtk().incBaseValue(attIncrease[2]) ;
		BA.getMagAtk().incBaseValue(attIncrease[3]) ;
		BA.getPhyDef().incBaseValue(attIncrease[4]) ;
		BA.getMagDef().incBaseValue(attIncrease[5]) ;
		BA.getAgi().incBaseValue(attIncrease[6]) ;
		BA.getDex().incBaseValue(attIncrease[7]) ;
		PA.getExp().incMaxValue((int) attIncrease[8]) ;
		
		Animation.start(AnimationTypes.levelUp, new Object[] {attIncrease, level});
	}
	
	public double[] calcAttributesIncrease()
	{
		double[] increase = new double[attInc.getIncrement().basic().length + 1] ;

		for (int i = 0 ; i <= attInc.getIncrement().basic().length - 1 ; i += 1)
		{
			if (attInc.getChance().basic()[i] <= Math.random()) { continue ;}
			
			increase[i] = attInc.getIncrement().basic()[i] ;
		}
		
		increase[attInc.getIncrement().basic().length] = (double) (10 * (3 * Math.pow(level - 1, 2) + 3 * (level - 1) + 1) - 5) ;
		return increase ;
	}

	public void Load(String[][] ReadFile)
	{
	}
	
	
	public void display(Point pos, Scale scale, DrawPrimitives DP)
	{
		movingAni.displayMoving(dir, pos, Draw.stdAngle, scale, Align.bottomCenter, DP) ;
		
		displayStatus(DP) ;
	}
	
}