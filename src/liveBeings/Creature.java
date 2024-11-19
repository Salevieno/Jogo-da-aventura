package liveBeings ;

import java.awt.Color ;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import attributes.BasicAttribute;
import attributes.BasicBattleAttribute;
import attributes.BattleAttributes;
import attributes.BattleSpecialAttribute;
import attributes.BattleSpecialAttributeWithDamage;
import attributes.PersonalAttributes;
import graphics.Draw;
import graphics.DrawPrimitives;
import items.Item;
import libUtil.Align;
import libUtil.Util;
import main.AtkResults;
import main.AtkTypes;
import main.Battle;
import main.Game;
import maps.GameMap;
import screen.Screen;
import screen.Sky;
import utilities.AtkEffects;
import utilities.Directions;
import utilities.Elements;
import utilities.GameStates;
import utilities.Scale;
import utilities.TimeCounter;

public class Creature extends LiveBeing
{
	private CreatureType type ;
	private Set<Item> items ;
	private int gold ;
	private Color color ;
	private boolean follow ;
	
	private static Color[] skinColor = new Color[] {Game.colorPalette[0], Game.colorPalette[5]} ;
	private static Color[] shadeColor = new Color[] {Game.colorPalette[2], Game.colorPalette[3]} ;
	
 	public Creature(CreatureType CT)
	{
 		super(new PersonalAttributes(CT.getPA()), new BattleAttributes(CT.getBA()), CT.getMovingAnimations(), CreatureType.attWindow) ;
		
		this.type = CT ;		
		this.name = CT.name;
		this.level = CT.level;
		this.size = new Dimension(CT.size);
		this.range = CT.range;
		this.step = CT.step;
		this.elem = Arrays.copyOf(CT.elem, CT.elem.length);
		mpCounter = new TimeCounter(CT.mpDuration / 1.0);
		satiationCounter = new TimeCounter(CT.satiationDuration);
		actionCounter = new TimeCounter(CT.numberSteps) ;
		battleActionCounter = new TimeCounter(CT.battleActionDuration / 1.0) ;
		this.stepCounter = new TimeCounter(CT.numberSteps) ;
		combo = new ArrayList<>() ;
		
		dir = Directions.up ;
		state = LiveBeingStates.idle ;
		currentAction = "" ;
		spells = List.copyOf(CT.getSpell()) ;
		this.items = Set.copyOf(CT.getItems()) ;
		this.gold = CT.getGold() ;
		this.color = CT.getColor() ;
		
		Point minCoord = new Point(0, (int) (0.2*Game.getScreen().getSize().height)) ;
		Dimension range = new Dimension(Game.getScreen().getSize().width, (int) ((1 - (double)(Sky.height)/Game.getScreen().getSize().height) * Game.getScreen().getSize().height)) ;
		Point initialPos = Util.RandomPos(minCoord, range, new Dimension(1, 1)) ;
		setPos(initialPos) ;
		

		if (getName().equals("Dragão") | getName().equals("Dragon"))
		{
			setPos(Game.getScreen().getCenter()) ;
		}
		startCounters() ;
		
		follow = false ;
	}

 	public Creature(CreatureType CT, Point pos)
 	{
 		this(CT) ;
 		this.pos = pos ;
 	}
 	
	public CreatureType getType() {return type ;}
	public List<Spell> getSpell() {return spells ;}
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
	public Set<Item> getBag() {return items ;}
	public int getGold() {return gold ;}
	public Color getColor() {return color ;}
	public boolean getFollow() {return follow ;}
	public void setPos(Point newValue) {pos = newValue ;}
	public void setFollow(boolean F) {follow = F ;}
	public static Color[] getskinColor() {return skinColor ;}
	public static Color[] getshadeColor() {return shadeColor ;}

	public Point center() { return new Point(pos) ;}

	public Point headPos() { return new Point((int) (pos.x), (int) (pos.y - 0.5 * size.height)) ;}
	
	public boolean hasEnoughMP(int spellID)
	{
		int MPcost = 10 * spellID ;
		return (MPcost <= PA.getMp().getCurrentValue()) ;
	}
	
	public Item getRandomElemFromBag()
	{
		int i = 0 ;
		int itemID = Util.randomIntFromTo(0, items.size() - 1) ;
		for (Item item : items)
		{
			if (i == itemID)
			{
				return item ;
			}
			
			i += 1 ;
		}
		
		return null ;
	}
	
	public void displayName(Point pos, Align alignment, Color color, DrawPrimitives DP)
	{
		Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
		DP.drawText(pos, alignment, Draw.stdAngle, name, font, color) ;
	}
	
	public void display(Point pos, Scale scale, DrawPrimitives DP)
	{
		if (state.equals(LiveBeingStates.moving))
		{
			type.movingAni.displayMoving(dir, pos, 0, scale, Align.center, DP) ;
		}
		else
		{
			type.movingAni.displayIdle(pos, 0, scale, Align.center, DP) ;
		}
		if (isDrunk())
		{
			displayDrunk(DP) ;
		}
//		displayAttributes(0, DP) ;
		displayStatus(DP) ;
	}
	
	public void displayAdditionalInfo(DrawPrimitives DP)
	{
		DP.drawText(new Point(pos.x, pos.y + 20), Align.center, 0, String.valueOf(this.totalPower()), new Font(Game.MainFontName, Font.BOLD, 14), Color.black) ;
		DP.drawText(getPos(), Align.center, 0, String.valueOf(type.getID()), new Font(Game.MainFontName, Font.BOLD, 24), Color.black) ;
	}
	
	public void setRandomPos()
	{
		Screen screen = Game.getScreen() ;
		Point minCoord = new Point(0, (int) (0.2*screen.getSize().height)) ;
		Dimension range = new Dimension(screen.getSize().width, (int) (screen.getBorders()[3] - screen.getBorders()[1])) ;
		Dimension step = new Dimension(1, 1) ;
		setPos(Util.RandomPos(minCoord, range, step)) ;
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
	
	public void updatePos(Directions dir, Point CurrentPos, int step, int movePattern, double moveRate, GameMap map)
	{
		Point newPos = switch (movePattern)
		{
			case 0 -> calcNewPos(dir, CurrentPos, step) ;
			case 1 -> calcNewPos(dir, step, CurrentPos, moveRate) ;
			default -> calcNewPos(dir, CurrentPos, step) ;
		} ;

		if (!Game.getScreen().posIsWithinBorders(newPos)) { return ;}
		if (!map.groundIsWalkable(newPos, null)) { return ;}

		setPos(newPos) ;
	
	}
	
	public String chooseTarget(boolean playerIsAlive, boolean petIsAlive)
	{// TODO optional - retornar liveBeing
		if (!playerIsAlive & !petIsAlive) { return null ;}		
		if (!playerIsAlive) { return "pet"  ;}
		if (!petIsAlive) { return "player" ;}

		if (0.5 <= Math.random()) { return "player" ;}
		else { return "pet" ;}
	}
		
	public void chooseFightMove(String playerMove)
	{
		
		if (Game.getState().equals(GameStates.simulation))
		{
			int qtdAvailableMoves = canUseSpell(spells.get(0)) ? 2 : 1 ;
			int move = Util.randomIntFromTo(0, qtdAvailableMoves) ;
//			System.out.println(move);
			switch (move)
			{
				case 0:	setCurrentAction(BattleKeys[0]) ; return ;	// Physical attack
				case 1:	setCurrentAction(BattleKeys[1]) ; return ;	// Defense
				case 2:	setCurrentAction(String.valueOf(Util.randomIntFromTo(0, spells.size() - 1))) ; return ;	// spell
			}
			return ;
		}
		
		List<Double> modifiedGenes = type.getGenes().getModifiedGenes(playerMove) ;

		int move = Util.randomFromChanceList(modifiedGenes) ;
		switch (move)
		{
			case 0:	setCurrentAction(BattleKeys[0]) ; return ;	// Physical attack
			case 1:	setCurrentAction(BattleKeys[1]) ; return ;	// Defense
			case 2:	setCurrentAction(String.valueOf(Util.randomIntFromTo(0, spells.size() - 1))) ; return ;	// spell
		}

	}
	
	public void move(Point PlayerPos, GameMap map)
	{

		if (!state.equals(LiveBeingStates.moving)) { return ;}

		if (stepCounter.finished())
		{
			setState(LiveBeingStates.idle) ;
			stepCounter.reset() ;
			return ;
		}
		
		if (follow)
		{
			setPos(follow(pos, PlayerPos, range)) ;
			return ;
		}

		boolean switchDirection = 0.47 <= stepCounter.rate() & stepCounter.rate() <= 0.5 ;
		if (switchDirection)
		{
			setDir(newMoveDirection(dir)) ;
		}
		
		int movePattern = type.getID() % CreatureType.numberCreatureTypesImages == 0 ? 1 : 0 ;
		updatePos(dir, pos, step, movePattern, stepCounter.rate(), map) ;
		
	}
	
	public void think()
	{
		if (Util.chance(0.6)) { setState(LiveBeingStates.idle) ; return ;}
		else { setState(LiveBeingStates.moving) ; stepCounter.start() ; return ;}
	}
	
	public void act()
	{
		if (!state.equals(LiveBeingStates.idle)) { return ;}
//		boolean startMoving = Util.chance(0.3) ;
//		
//		if (!startMoving) { return ;}
//
//		boolean switchDirection = Util.chance(0.5) ;
//		if (switchDirection)
//		{
//			setDir(newMoveDirection(dir)) ;
//		}
		
		actionCounter.reset() ;
		return ;
	}
	
	public void applyPassiveSpell(Spell spell)
	{
		
	}
	
	public void useAutoSpell(boolean activate, Spell spell)
	{
		
	}
	
	public AtkResults useOffensiveSpell(Spell spell, LiveBeing receiver)
	{
		int spellLevel = spell.getLevel() ;

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
		
		double AtkCritMod = spell.getAtkCritMod()[0] * spellLevel ;
		double DefCritMod = spell.getDefCritMod()[0] * spellLevel ;
		double BlockDef = receiver.getBA().getBlock().TotalDefChance() ;
		double BasicAtk = 0 ;
		double BasicDef = 0 ;
		Elements[] AtkElem = new Elements[] {spell.getElem(), elem[0], Elements.neutral} ;
		Elements[] DefElem = receiver.defElems() ;
		
		BasicAtk = MagAtk ;
		BasicDef = MagDef ;

		AtkEffects effect = Battle.calcEffect(DexMod[0] + AtkDex*DexMod[1], AgiMod[0] + DefAgi*AgiMod[1], AtkCrit + AtkCritMod, DefCrit + DefCritMod, BlockDef) ;
		int damage = Battle.calcDamage(effect, AtkMod[0] + BasicAtk*AtkMod[1], DefMod[0] + BasicDef*DefMod[1], AtkElem, DefElem, receiverElemMod) ;
		double[] inflictedStatus = Battle.calcStatus(atkChances, receiver.getBA().baseDefChances(), BA.baseDurations()) ;				
		
		return new AtkResults(AtkTypes.magical, effect, damage, inflictedStatus) ;
	}
	
	public AtkResults useSpell(Spell spell, LiveBeing receiver)
	{
		if (spell == null) { return null ;}
		if (receiver == null) { return null ;}
		if (!hasEnoughMP(spell)) { return new AtkResults() ;}

		displayUsedSpellMessage(spell, Game.getScreen().pos(0.63, 0.2), Game.colorPalette[0]);
		PA.getMp().decTotalValue(spell.getMpCost()) ;
		switch (spell.getType())
		{
			case support : useSupportSpell(spell, receiver) ; return new AtkResults(AtkTypes.magical) ;
			case offensive : return useOffensiveSpell(spell, receiver) ;
			default : return new AtkResults(AtkTypes.magical) ;
		}
		
	}
	
	public void dies()
	{
		PA.getLife().setToMaximum() ;
		PA.getMp().setToMaximum() ;
		if (currentAtkType != null)
		{
			if (currentAtkType.equals(AtkTypes.defense))
			{
				deactivateDef() ;
			}
		}
		setRandomPos() ;
		follow = false ;
	}
	
	@Override
	public String toString()
	{
		return "Creature [type=" + type + ", Bag=" + items + ", Gold=" + gold + ", color=" + color + ", follow=" + follow + "]";
	}

	
}
