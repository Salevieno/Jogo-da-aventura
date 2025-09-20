package liveBeings ;

import java.awt.Color ;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import attributes.AttributeIncrease;
import attributes.BasicAttribute;
import attributes.BasicBattleAttribute;
import attributes.BattleAttributes;
import attributes.BattleSpecialAttribute;
import attributes.BattleSpecialAttributeWithDamage;
import attributes.PersonalAttributes;
import battle.AtkEffects;
import battle.AtkResults;
import battle.AtkTypes;
import battle.Battle;
import components.HitboxRectangle;
import graphics.Align;
import graphics.Scale;
import graphics2.Animation;
import graphics2.AnimationTypes;
import graphics2.Draw;
import graphics2.SpriteAnimation;
import items.PetItem;
import main.Directions;
import main.Elements;
import main.Game;
import main.GameTimer;
import main.Path;
import maps.GameMap;
import utilities.Util;
import windows.PetAttributesWindow;

public class Pet extends LiveBeing
{
	private final Color color ;
	private final int job ;
	private final AttributeIncrease attInc ;
	private int spellPoints ;
	private PetItem equip ;
	private int alchBuffId ;
	
	public static final List<String[]> InitialAtts = Util.readcsvFile(Path.CSV + "PetInitialStats.csv") ;
	private static final List<String[]> attEvolution = Util.readcsvFile(Path.CSV + "PetEvolution.csv") ;
    	
	public Pet(int Job)
	{
		super(InitializePersonalAttributes(Job),
				new BattleAttributes(InitialAtts.get(Job), 1, InitialAtts.get(Job)[36], InitialAtts.get(Job)[37]),
				initializeMovingAnimations(Job),
				new PetAttributesWindow()) ;
		
		name = InitialAtts.get(Job)[0] ;
		level = 1 ;
		proJob = 0 ;
		map = null ;
		pos = new Point2D.Double(0, 0) ;
		dir = Directions.up ;
		state = LiveBeingStates.idle ;
		size = new Dimension(movingAni.spriteIdle.getFrameSize().width, movingAni.spriteIdle.getFrameSize().height) ;	
		range = Integer.parseInt(InitialAtts.get(Job)[4]) ;
		step = Integer.parseInt(InitialAtts.get(Job)[32]) ;
		atkElem = Elements.neutral ;
		satiationCounter = new GameTimer(Double.parseDouble(InitialAtts.get(Job)[34])) ;
		mpCounter = new GameTimer(Double.parseDouble(InitialAtts.get(Job)[35])) ;
		battleActionCounter = new GameTimer(Double.parseDouble(InitialAtts.get(Job)[36])) ;
		movingTimer = new GameTimer(20) ;
		combo = new ArrayList<>();
		hitbox = new HitboxRectangle(getPos(), size, 0.8) ;
		equip = null ;
		alchBuffId = -1 ;
		
		this.job = Job ;
		Color[] colorPalette = Game.palette ;
		Color[] petColors = new Color[] {colorPalette[3], colorPalette[5], colorPalette[21], colorPalette[21]} ;
		color = petColors[Job] ;
		spells = InitializePetSpells();
		spellPoints = 0 ;


		startCounters() ;
		attInc = calcAttributeIncrease(job) ;
		
	}
	
	public static PersonalAttributes InitializePersonalAttributes(int Job)
	{
		BasicAttribute life = new BasicAttribute(Integer.parseInt(InitialAtts.get(Job)[2]), Integer.parseInt(InitialAtts.get(Job)[2]), 1) ;
		BasicAttribute mp = new BasicAttribute(Integer.parseInt(InitialAtts.get(Job)[3]), Integer.parseInt(InitialAtts.get(Job)[3]), 1) ;
		BasicAttribute exp = new BasicAttribute(0, 50, 1) ;
		BasicAttribute satiation = new BasicAttribute(100, 100, 1) ;
		BasicAttribute thirst = new BasicAttribute(100, 100, 1) ;
		return new PersonalAttributes(life, mp, exp, satiation, thirst) ;
	}
	
	public static MovingAnimations initializeMovingAnimations(int Job)
	{
		return new MovingAnimations(
			new SpriteAnimation(Path.PET_IMG + "PetMovingRight0.png", new Point(0, 0), Align.bottomCenter, 1, 5),
			new SpriteAnimation(Path.PET_IMG + "PetMovingRight0.png", new Point(0, 0), Align.bottomCenter, 1, 5),
			new SpriteAnimation(Path.PET_IMG + "PetMovingRight0.png", new Point(0, 0), Align.bottomCenter, 1, 5),
			new SpriteAnimation(Path.PET_IMG + "PetMovingRight0.png", new Point(0, 0), Align.bottomCenter, 1, 5),
			new SpriteAnimation(Path.PET_IMG + "PetMovingRight0.png", new Point(0, 0), Align.bottomCenter, 1, 5)
		);
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
	public BasicAttribute getExp() {return PA.getExp() ;}
	public BasicAttribute getSatiation() {return PA.getSatiation() ;}
	public PetItem getEquip() { return equip ;}
	public void setEquip(PetItem equip) { this.equip = equip ;}
	public int getAlchBuffId() { return alchBuffId ;}
	public void setAlchBuffId(int alchBuffId) { this.alchBuffId = alchBuffId ;}

//	public boolean isAlive() { return 0 < PA.getLife().getCurrentValue() ;}
	public boolean shouldLevelUP() {return getExp().getMaxValue() <= getExp().getCurrentValue() ;}
	public boolean closeToPlayer(Point2D.Double playerPos) { return pos.distance(playerPos) <= 40 ; }
	
	public Point center() { return new Point((int) pos.x, (int) pos.y - size.height / 2) ;}
	public Point headPos() { return new Point((int) pos.x, (int) pos.y - size.height) ;}

	public Directions randomNewDirection(Directions originalDir)
	{
		Directions newDir = randomDir() ;
		while (Directions.areOpposite(originalDir, newDir))
		{
			newDir = randomDir() ;
		}
		return newDir ;
	}
	
	private Point2D.Double findNextPos(Point2D.Double playerPos, Creature opponent, double dt)
	{
		if (opponent != null)
		{
			return chase(pos, opponent.getPosAsDouble(), step) ;
		}
		else if (closeToPlayer(playerPos))
		{
			if (Util.chance(0.8)) { return pos ;}
			
			if (Util.chance(0.2))
			{
				dir = randomDir() ;
			}
			return MovePattern.calcNewPos(MovePattern.pattern0, dir, pos, step, dt) ;
		}
		else
		{
			return chase(pos, playerPos, step) ;
		}
	}


	public void think(boolean isInBattle, Point2D.Double playerPos)
	{
		if (isInBattle)
		{
			setState(LiveBeingStates.fighting) ;
			return ;
		}
		
		if (!closeToPlayer(playerPos))
		{
			restartMoving() ;
			return ;
		}
		
		if (Util.chance(0.8))
		{
			setState(LiveBeingStates.idle) ;
			return ;
		}
		else
		{
			restartMoving() ;
			return ;
		}
		
	}
	
	public void act(Player player, double dt)
	{
		if (isMoving())
		{
			startMoving() ;
			move(player.getPosAsDouble(), player.getMap(), player.getOpponent(), player.getSuperElem(), dt) ;
			return ;
		}
		
		if (isFighting())
		{
			if (!player.getOpponent().isInRange(pos))
			{
				move(player.getPosAsDouble(), player.getMap(), player.getOpponent(), player.getSuperElem(), dt) ;
				return ;
			}
			fight() ;
		}
		
	}


	public void move(Point2D.Double playerPos, GameMap playerMap, Creature opponent, Elements playerElem, double dt)
	{
		Point2D.Double nextPos = findNextPos(playerPos, opponent, dt) ;
		if (playerMap.groundIsWalkable(new Point((int) nextPos.x, (int) nextPos.y), playerElem))
		{
			setPos(nextPos) ;
		}
	}
	
	public void fight()
	{
		int move = Util.randomInt(0, 1 + getActiveSpells().size()) ;
		switch (move)
		{
			case 0: currentAction = battleKeys[0] ; return ;
			case 1: currentAction = battleKeys[1] ; return ;
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
		/// TODO weapon elem, second in the array below, should come from equip.getelem()
		Elements[] AtkElem = new Elements[] {spell.getElem(), null, null} ;
		Elements[] DefElem = receiver.defElems() ;
		
		BasicAtk = MagAtk ;
		BasicDef = MagDef ;

		AtkEffects effect = Battle.calcEffect(DexMod[0] + AtkDex*DexMod[1], AgiMod[0] + DefAgi*AgiMod[1], AtkCrit + AtkCritMod, DefCrit + DefCritMod, BlockDef) ;
		int damage = Battle.calcDamage(effect, AtkMod[0] + BasicAtk*AtkMod[1], DefMod[0] + BasicDef*DefMod[1], AtkElem, DefElem, receiverElemMod) ;
		double[] inflictedStatus = Battle.calcStatus(atkChances, receiver.getBA().baseDefChances(), BA.baseDurations()) ;				
		
		displayUsedSpellMessage(spell, Game.getScreen().pos(0.23, 0.2), Game.palette[12]);
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
	
	public Elements[] atkElems() { return new Elements[] {atkElem, atkElem} ;}
	public Elements[] defElems() { return new Elements[] {Elements.neutral, Elements.neutral} ;} // TODO pegar elem do equip

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
	
	public void display(Point pos, Scale scale)
	{
		movingAni.displayMoving(dir, pos, Draw.stdAngle, scale, Align.bottomCenter) ;

		if (isDrunk())
		{
			displayDrunk() ;
		}

		if (isFighting())
		{
			displayBattleActionCounter() ;
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
		display(getPos(), Scale.unit) ;
	}
	
}