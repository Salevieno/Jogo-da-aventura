package liveBeings ;

import java.awt.Color ;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import attributes.BasicAttribute;
import attributes.BasicBattleAttribute;
import attributes.BattleSpecialAttribute;
import attributes.BattleSpecialAttributeWithDamage;
import graphics.Draw;
import graphics.DrawPrimitives;
import items.Item;
import main.AtkResults;
import main.AtkTypes;
import main.Battle;
import main.Game;
import maps.GameMap;
import screen.Screen;
import screen.Sky;
import utilities.Align;
import utilities.AtkEffects;
import utilities.Directions;
import utilities.Elements;
import utilities.FrameCounter;
import utilities.Scale;
import utilities.UtilG;
import windows.CreatureAttributesWindow;

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
 		super(CT.getPA(), CT.getBA(), CT.getMovingAnimations(), CreatureType.attWindow) ;
		
		this.type = CT ;		
		this.name = CT.name;
		this.level = CT.level;
		this.size = CT.size;
		this.range = CT.range;
		this.step = CT.step;
		this.elem = CT.elem;
		mpCounter = new FrameCounter(0, CT.mpDuration);
		satiationCounter = new FrameCounter(0, CT.satiationDuration);
		actionCounter = new FrameCounter(0, CT.numberSteps) ;
		battleActionCounter = new FrameCounter(0, CT.battleActionDuration) ;
		this.stepCounter = new FrameCounter(0, CT.numberSteps) ;
		combo = new ArrayList<>() ;
		
		dir = Directions.up ;
		state = LiveBeingStates.idle ;
		currentAction = "" ;
		spells = CT.getSpell() ;
		this.items = CT.getItems() ;
		this.gold = CT.getGold() ;
		this.color = CT.getColor() ;
		
		Point minCoord = new Point(0, (int) (0.2*Game.getScreen().getSize().height)) ;
		Dimension range = new Dimension(Game.getScreen().getSize().width, (int) ((1 - (double)(Sky.height)/Game.getScreen().getSize().height) * Game.getScreen().getSize().height)) ;
		Point initialPos = UtilG.RandomPos(minCoord, range, new Dimension(1, 1)) ;
		setPos(initialPos) ;
		

		if (getName().equals("Dragão") | getName().equals("Dragon"))
		{
			setPos(Game.getScreen().getCenter()) ;
		}
		
		follow = false ;
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
	
	public boolean hasEnoughMP(int spellID)
	{
		int MPcost = 10 * spellID ;
		return (MPcost <= PA.getMp().getCurrentValue()) ;
	}
	
	public void displayName(Point pos, Align alignment, Color color, DrawPrimitives DP)
	{
		Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
		DP.drawText(pos, alignment, Draw.stdAngle, name, font, color) ;
	}
	
	public void display(Point pos, Scale scale, DrawPrimitives DP)
	{
		DP.drawImage(type.movingAni.idleGif, pos, scale, Align.center) ;
		displayAttributes(0, DP) ;
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
		setPos(UtilG.RandomPos(minCoord, range, step)) ;
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
	
	public void updatePos(Directions dir, Point CurrentPos, int step, GameMap map)
	{

		Point newPos = calcNewPos(dir, CurrentPos, step) ;

		if (!Game.getScreen().posIsWithinBorders(newPos)) { return ;}
		if (!map.groundIsWalkable(newPos, null)) { return ;}

		setPos(newPos) ;
	
	}
	
	public String chooseTarget(boolean playerIsAlive, boolean petIsAlive)
	{// TODO refatorar creature choose target
		if (!playerIsAlive & !petIsAlive) { return null ;}		
		if (!playerIsAlive) { return "pet"  ;}
		if (!petIsAlive) { return "player" ;}

		if (0.5 <= Math.random()) { return "player" ;}
		else { return "pet" ;}
	}
		
	public void move(Point PlayerPos, GameMap map)
	{

		if (!state.equals(LiveBeingStates.moving)) { return ;}
		
		// TODO quando não está em batalha mas está seguindo, ela só se aproxima, mas não entra na batalha
		if (follow)
		{
			setPos(follow(pos, PlayerPos, step, range)) ;
			return ;
		}

		int numberSwitchDirection = UtilG.randomIntFromTo(1, 5) ;
		boolean switchDirection = stepCounter.getCounter() % (stepCounter.getDuration() / numberSwitchDirection) == 0 ;
		if (switchDirection)
		{
			setDir(newMoveDirection(dir)) ;
		}
	
		updatePos(dir, pos, step, map) ;
		stepCounter.inc() ;
		if (stepCounter.finished())
		{
			setState(LiveBeingStates.idle) ;
			stepCounter.reset() ;
		}
		
	}
	
	public void chooseFightMove(String playerMove)
	{
		List<Double> modifiedGenes = type.getGenes().getModifiedGenes(playerMove) ;
		
		int move = UtilG.randomFromChanceList(modifiedGenes) ;
		switch (move)
		{
			case 0:	setCurrentAction(BattleKeys[0]) ; return ;	// Physical attack
			case 1:	setCurrentAction(BattleKeys[1]) ; return ;	// Defense
			case 2:	setCurrentAction(String.valueOf(UtilG.randomIntFromTo(0, spells.size() - 1))) ; return ;	// spell
		}		
	}
	
	public void think()
	{
		if (UtilG.chance(0.6)) { setState(LiveBeingStates.idle) ; return ;}
		else { setState(LiveBeingStates.moving) ; return ;}
	}
	
	public void act()
	{
		if (!state.equals(LiveBeingStates.idle)) { return ;}
		
//		boolean startMoving = UtilG.chance(0.3) ;
//		
//		if (!startMoving) { return ;}
//
//		boolean switchDirection = UtilG.chance(0.5) ;
//		if (switchDirection)
//		{
//			setDir(newMoveDirection(dir)) ;
//		}
//		setState(LiveBeingStates.moving) ;
		
		actionCounter.reset() ;
		return ;
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
		Elements[] AtkElem = new Elements[] {spell.getElem(), elem[0], Elements.neutral} ;
		Elements[] DefElem = receiver.defElems() ;
		
		BasicAtk = MagAtk ;
		BasicDef = MagDef ;
		
		effect = Battle.calcEffect(DexMod[0] + AtkDex*DexMod[1], AgiMod[0] + DefAgi*AgiMod[1], AtkCrit + AtkCritMod, DefCrit + DefCritMod, BlockDef) ;
		damage = Battle.calcDamage(effect, AtkMod[0] + BasicAtk*AtkMod[1], DefMod[0] + BasicDef*DefMod[1], AtkElem, DefElem, receiverElemMod) ;
		
		return new AtkResults(AtkTypes.magical, effect, damage) ;
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
