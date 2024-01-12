package liveBeings ;

import java.awt.Color ;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import attributes.BasicAttribute;
import attributes.BasicBattleAttribute;
import attributes.BattleAttributes;
import attributes.BattleSpecialAttribute;
import attributes.BattleSpecialAttributeWithDamage;
import attributes.PersonalAttributes;
import graphics.Animation;
import graphics.DrawingOnPanel;
import graphics.Gif;
import main.AtkResults;
import main.AtkTypes;
import main.Battle;
import main.Game;
import maps.GameMap;
import utilities.AtkEffects;
import utilities.Directions;
import utilities.Elements;
import utilities.Scale;
import utilities.FrameCounter;
import utilities.UtilG;
import utilities.UtilS;
import windows.PetAttributesWindow;

public class Pet extends LiveBeing
{
	private Color color ;
	private int job ;
	private int spellPoints ;
	private double[] ElemMult ;		// [Neutral, Water, Fire, Plant, Earth, Air, Thunder, Light, Dark, Snow]
//	private int[] StatusCounter ;	// [Life, Mp, Phy atk, Phy def, Mag atk, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence]
	
	public static int NumberOfSpells = 5 ;
	public static double[] AttributeIncrease ;
	public static double[] ChanceIncrease ;
	
	private static List<String[]> PetProperties = UtilG.ReadcsvFile(Game.CSVPath + "PetInitialStats.csv") ;
	private static List<String[]> PetEvolutionProperties = UtilG.ReadcsvFile(Game.CSVPath + "PetEvolution.csv") ;
	
	public final static Gif levelUpGif = new Gif(UtilS.loadImage("\\Player\\" + "LevelUp.gif"), 170, false, false) ;
    	
	public Pet(int Job)
	{
		super(InitializePersonalAttributes(Job), InitializeBattleAttributes(Job), initializeMovingAnimations(Job), new PetAttributesWindow()) ;
		
		name = PetProperties.get(Job)[0] ;
		level = 1 ;
		proJob = 0 ;
		map = null ;
		pos = new Point(0, 0) ;
		dir = Directions.up ;
		state = LiveBeingStates.idle ;
		size = new Dimension (movingAni.idleGif.getWidth(null), movingAni.idleGif.getHeight(null)) ;	
		range = Integer.parseInt(PetProperties.get(Job)[4]) ;
		step = Integer.parseInt(PetProperties.get(Job)[32]) ;
		elem = new Elements[] {Elements.neutral, Elements.neutral, Elements.neutral, Elements.neutral, Elements.neutral} ;
		actionCounter = new FrameCounter(0, Integer.parseInt(PetProperties.get(Job)[33])) ;
		satiationCounter = new FrameCounter(0, Integer.parseInt(PetProperties.get(Job)[34])) ;
		mpCounter = new FrameCounter(0, Integer.parseInt(PetProperties.get(Job)[35])) ;
		battleActionCounter = new FrameCounter(0, Integer.parseInt(PetProperties.get(Job)[36])) ;
		stepCounter = new FrameCounter(0, 20) ;
		combo = new ArrayList<>();
		
		this.job = Job ;
		Color[] colorPalette = Game.colorPalette ;
		Color[] petColors = new Color[] {colorPalette[3], colorPalette[5], colorPalette[21], colorPalette[21]} ;
		color = petColors[Job] ;
		spells = InitializePetSpells();
		spellPoints = 0 ;

		
		ElemMult = new double[10] ;
//		StatusCounter = new int[8] ;
			
    	AttributeIncrease = new double[8] ;
    	ChanceIncrease = new double[8] ;
		for (int i = 0 ; i <= 7 ; ++i)
		{
			AttributeIncrease[i] = Double.parseDouble(PetEvolutionProperties.get(Job)[i + 1]) ;
			ChanceIncrease[i] = Double.parseDouble(PetEvolutionProperties.get(Job)[i + 9]) ;
		}
	}
	
	private static PersonalAttributes InitializePersonalAttributes(int Job)
	{
		BasicAttribute life = new BasicAttribute(Integer.parseInt(PetProperties.get(Job)[2]), Integer.parseInt(PetProperties.get(Job)[2]), 1) ;
		BasicAttribute mp = new BasicAttribute(Integer.parseInt(PetProperties.get(Job)[3]), Integer.parseInt(PetProperties.get(Job)[3]), 1) ;
		BasicAttribute exp = new BasicAttribute(0, 50, 1) ;
		BasicAttribute satiation = new BasicAttribute(100, 100, 1) ;
		BasicAttribute thirst = new BasicAttribute(100, 100, 1) ;
		return new PersonalAttributes(life, mp, exp, satiation, thirst) ;
	}
	
	private static BattleAttributes InitializeBattleAttributes(int Job)
	{
		BasicBattleAttribute phyAtk = new BasicBattleAttribute(Double.parseDouble(PetProperties.get(Job)[5]), 0, 0) ;
		BasicBattleAttribute magAtk = new BasicBattleAttribute(Double.parseDouble(PetProperties.get(Job)[6]), 0, 0) ;
		BasicBattleAttribute phyDef = new BasicBattleAttribute(Double.parseDouble(PetProperties.get(Job)[7]), 0, 0) ;
		BasicBattleAttribute magDef = new BasicBattleAttribute(Double.parseDouble(PetProperties.get(Job)[8]), 0, 0) ;
		BasicBattleAttribute dex = new BasicBattleAttribute(Double.parseDouble(PetProperties.get(Job)[9]), 0, 0) ;
		BasicBattleAttribute agi = new BasicBattleAttribute(Double.parseDouble(PetProperties.get(Job)[10]), 0, 0) ;
		BasicBattleAttribute critAtk = new BasicBattleAttribute(Double.parseDouble(PetProperties.get(Job)[11]), 0, 0) ;
		BasicBattleAttribute critDef = new BasicBattleAttribute(Double.parseDouble(PetProperties.get(Job)[12]), 0, 0) ;
		BattleSpecialAttribute stun = new BattleSpecialAttribute(Double.parseDouble(PetProperties.get(Job)[13]), 0, Double.parseDouble(PetProperties.get(Job)[14]), 0, Integer.parseInt(PetProperties.get(Job)[15])) ;
		BattleSpecialAttribute block = new BattleSpecialAttribute(Double.parseDouble(PetProperties.get(Job)[16]), 0, Double.parseDouble(PetProperties.get(Job)[17]), 0, Integer.parseInt(PetProperties.get(Job)[18])) ;
		BattleSpecialAttributeWithDamage blood = new BattleSpecialAttributeWithDamage(Double.parseDouble(PetProperties.get(Job)[19]), 0, Double.parseDouble(PetProperties.get(Job)[20]), 0, Integer.parseInt(PetProperties.get(Job)[21]), 0, Integer.parseInt(PetProperties.get(Job)[22]), 0, Integer.parseInt(PetProperties.get(Job)[23])) ;
		BattleSpecialAttributeWithDamage poison = new BattleSpecialAttributeWithDamage(Double.parseDouble(PetProperties.get(Job)[24]), 0, Double.parseDouble(PetProperties.get(Job)[25]), 0, Integer.parseInt(PetProperties.get(Job)[26]), 0, Integer.parseInt(PetProperties.get(Job)[27]), 0, Integer.parseInt(PetProperties.get(Job)[28])) ;
		BattleSpecialAttribute silence = new BattleSpecialAttribute(Double.parseDouble(PetProperties.get(Job)[29]), 0, Double.parseDouble(PetProperties.get(Job)[30]), 0, Integer.parseInt(PetProperties.get(Job)[31])) ;
		LiveBeingStatus status = new LiveBeingStatus() ;
		return new BattleAttributes(phyAtk, magAtk, phyDef, magDef, dex, agi, critAtk, critDef, stun, block, blood, poison, silence, status) ;
	}
	
	public static MovingAnimations initializeMovingAnimations(int Job)
	{
		String filePath = Game.ImagesPath + "\\Pet\\" + "PetType" ;
		return new MovingAnimations(UtilG.loadImage(filePath + String.valueOf(Job) + ".png"),
				UtilG.loadImage(filePath + String.valueOf(Job) + ".png"),
				UtilG.loadImage(filePath + String.valueOf(Job) + ".png"),
				UtilG.loadImage(filePath + String.valueOf(Job) + ".png"),
				UtilG.loadImage(filePath + String.valueOf(Job) + ".png")) ;
	}

	public List<Spell> InitializePetSpells()
    {
		Spell[] allSpells = Game.getAllSpells() ;		
		ArrayList<Spell> petspells = new ArrayList<>() ;
		
		for (int i = 0 ; i <= Pet.NumberOfSpells - 1 ; i += 1)
		{
			int ID = i + job * Pet.NumberOfSpells ;
			
			petspells.add(allSpells[ID]) ;	
		}
		
		petspells.get(0).incLevel(1) ;
		return petspells ;
    }
	
	public Color getColor() {return color ;}
	public int getJob() {return job ;}
	public MovingAnimations getMovingAnimations() {return movingAni ;}
	public List<Spell> getSpells() {return spells ;}
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
	public double[] getElemMult() {return ElemMult ;}
	public BasicAttribute getExp() {return PA.getExp() ;}
	public BasicAttribute getSatiation() {return PA.getSatiation() ;}

	public boolean isAlive() { return 0 < PA.getLife().getCurrentValue() ;}
	public boolean shouldLevelUP() {return getExp().getMaxValue() <= getExp().getCurrentValue() ;}
	public boolean closeToPlayer(Point playerPos) { return UtilG.dist(pos, playerPos) <= 40 ; }
	
	public Point center()
	{
		return new Point((int) (pos.x), (int) (pos.y - 0.5 * size.height)) ;
	}

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
			if (UtilG.chance(0.8)) { return pos ;}
			
			if (UtilG.chance(0.2))
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
			
			if (UtilG.chance(0.8)) { setState(LiveBeingStates.idle) ; return ;}
			else { setState(LiveBeingStates.moving) ; return ;}
		}
		
		setState(LiveBeingStates.fighting) ;
	}
	
	public void act(Player player)
	{
		if (state.equals(LiveBeingStates.moving))
		{
			move(player.getPos(), player.getMap(), player.getOpponent(), player.getElem()[4]) ;
		}
		else if (state.equals(LiveBeingStates.fighting))
		{
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
		int move = -1 ;
		if (10 <= PA.getMp().getCurrentValue())	// if there is enough mp
		{
			move = UtilG.randomIntFromTo(0, 3) ;	// consider using spell
		}
		else
		{
			move = UtilG.randomIntFromTo(0, 2) ;	// only physical atk of def
		}
		if (move == 0)
		{
			currentAction = BattleKeys[0] ;
		}
		if (move == 1)
		{
			currentAction = BattleKeys[1] ;
		}
		if (move == 2)
		{
			currentAction = String.valueOf(UtilG.randomIntFromTo(0, 4)) ;
		}
	}
	
	public void dies()
	{
		// TODO pet dies

		resetBattleActions() ;
		setPos(Game.getPlayer().getPos()) ;
	}
	
	public void applyPassiveSpell(Spell spell)
	{
		
	}
	
	public void useAutoSpells(boolean activate)
	{
		
	}
	
	public AtkResults useSpell(Spell spell, LiveBeing receiver)
	{
		int spellLevel = spell.getLevel() ;
		int damage = -1 ;
		AtkEffects effect = null ;

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
		double AtkCritMod = spell.getAtkCritMod()[0] * spellLevel ;	// Critical atk modifier
		double DefCritMod = spell.getDefCritMod()[0] * spellLevel ;	// Critical def modifier
		double BlockDef = receiver.getBA().getStatus().getBlock() ;
		double BasicAtk = 0 ;
		double BasicDef = 0 ;
		Elements[] AtkElem = new Elements[] {spell.getElem(), elem[1], elem[4]} ;
		Elements[] DefElem = receiver.defElems() ;
		
		BasicAtk = MagAtk ;
		BasicDef = MagDef ;
		
		effect = Battle.calcEffect(DexMod[0] + AtkDex*DexMod[1], AgiMod[0] + DefAgi*AgiMod[1], AtkCrit + AtkCritMod, DefCrit + DefCritMod, BlockDef) ;
		damage = Battle.calcDamage(effect, AtkMod[0] + BasicAtk*AtkMod[1], DefMod[0] + BasicDef*DefMod[1], AtkElem, DefElem, receiverElemMod) ;
		
		return new AtkResults(AtkTypes.magical, effect, damage) ;
	}
	
	public void win(Creature creature)
	{
		PA.getExp().incCurrentValue((int) (creature.getExp().getCurrentValue() * PA.getExp().getMultiplier())); ;
	}
	
	public void levelUp(Animation attIncAnimation)
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

//		Game.getAnimations().get(5).start(levelUpGif.getDuration(), new Object[] {pos}) ;
		
		if (attIncAnimation == null) { return ;}
		
		attIncAnimation.start(150, new Object[] {Arrays.copyOf(attIncrease, attIncrease.length - 1), level}) ;
	}
	
	public double[] calcAttributesIncrease()
	{
		double[] Increase = new double[AttributeIncrease.length + 1] ;
		for (int i = 0 ; i <= AttributeIncrease.length - 1 ; ++i)
		{
			if (Math.random() <= ChanceIncrease[i])
			{
				Increase[i] = AttributeIncrease[i] ;
			}
		}
		Increase[AttributeIncrease.length] = (double) (10 * (3 * Math.pow(level - 1, 2) + 3 * (level - 1) + 1) - 5) ;
		return Increase ;
	}

	public void Load(String[][] ReadFile)
	{
	}
	
	
	public void display(Point pos, Scale scale, DrawingOnPanel DP)
	{
		movingAni.display(dir, pos, DrawingOnPanel.stdAngle, scale, DP) ;
		
		displayStatus(DP) ;
	}
	
}