package liveBeings ;

import java.awt.Color ;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import attributes.BasicAttribute;
import attributes.BattleAttributes;
import attributes.PersonalAttributes;
import battle.AtkEffects;
import battle.AtkResults;
import battle.AtkTypes;
import battle.Battle;
import components.HitboxCircle;
import components.HitboxRectangle;
import graphics.Align;
import graphics.Scale;
import graphics2.Draw;
import items.Item;
import main.Directions;
import main.Elements;
import main.Game;
import main.GamePanel;
import main.GameStates;
import main.GameTimer;
import maps.GameMap;
import screen.Screen;
import screen.Sky;
import utilities.Util;

public class Creature extends LiveBeing
{
	private final CreatureType type ;
	private final Set<Item> items ;
	private final int gold ;
	private final Color color ;
	private boolean chasePlayer ;
	// private boolean hasSwitchedDirection ;
	private GameTimer idleTimer ;
	
	private static final Color[] skinColor = new Color[] {Game.palette[0], Game.palette[5]} ;
	private static final Color[] shadeColor = new Color[] {Game.palette[2], Game.palette[3]} ;
	
 	public Creature(CreatureType CT)
	{
 		this(CT, Util.randomPos(new Point(0, (int) (0.2*Game.getScreen().mapSize().height)), new Dimension(Game.getScreen().getSize().width, (int) ((1 - (double)(Sky.height)/Game.getScreen().getSize().height) * Game.getScreen().getSize().height)), new Dimension(1, 1))) ;
	}

 	public Creature(CreatureType CT, Point pos)
 	{
 		super(new PersonalAttributes(CT.getPA()), new BattleAttributes(CT.getBA()), CT.getMovingAnimations(), CreatureType.attWindow) ;
		
		this.type = CT ;
		this.name = CT.name;
		this.level = CT.level;
		this.size = new Dimension(CT.size);
		this.range = CT.range;
		this.step = CT.step;
		this.atkElem = CT.atkElem;
		this.mpCounter = new GameTimer(CT.mpDuration / 1.0);
		this.satiationCounter = new GameTimer(CT.satiationDuration);
		this.battleActionCounter = new GameTimer(CT.battleActionDuration / 1.0) ;
		this.movingTimer = new GameTimer(CT.movePattern.getDuration()) ;
		this.combo = new ArrayList<>() ;
		this.hitbox = CT.getHitboxType().equals("circle") ? new HitboxCircle(new Point(), size.width / 2) : new HitboxRectangle(new Point(), size) ;
		
		this.dir = Directions.up ;
		this.state = LiveBeingStates.idle ;
		this.currentAction = "" ;
		this.spells = List.copyOf(CT.getSpell()) ;
		this.items = Set.copyOf(CT.getItems()) ;
		this.gold = CT.getGold() ;
		this.color = CT.getColor() ;

		if (getName().equals("Dragão") | getName().equals("Dragon"))
		{
			setPos(Game.getScreen().getCenter()) ;
		}
		startCounters() ;
		
		this.chasePlayer = false ;
		this.idleTimer = new GameTimer(CT.getMovingAnimations().getIdleDuration()) ;
		this.idleTimer.start() ;
 		setPos(pos) ;
 	}
 	
	public CreatureType getType() {return type ;}
	public List<Spell> getSpell() {return spells ;}
	public BasicAttribute getLife() {return PA.getLife() ;}
	public BasicAttribute getExp() {return PA.getExp() ;}
	public Set<Item> getBag() {return items ;}
	public int getGold() {return gold ;}
	public GameTimer getIdleTimer() {return idleTimer ;}
	public void setChasePlayer(boolean F) {chasePlayer = F ;}
	public static Color[] getskinColor() {return skinColor ;}
	public static Color[] getshadeColor() {return shadeColor ;}

	public Point center() { return new Point((int) pos.x, (int) pos.y) ;}

	public Point headPos() { return new Point((int) (pos.x), (int) (pos.y - 0.5 * size.height)) ;}
	
	public void leaveBattle()
	{
		setState(LiveBeingStates.idle) ;
		PA.getLife().setToMaximum() ;
		PA.getMp().setToMaximum() ;
		setChasePlayer(false) ;
	}

	public boolean hasEnoughMP(int spellID)
	{
		int MPcost = 10 * spellID ;
		return (MPcost <= PA.getMp().getCurrentValue()) ;
	}
	
	public Item getRandomElemFromBag()
	{
		int i = 0 ;
		int itemID = Util.randomInt(0, items.size() - 1) ;
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
	
	public void setRandomPos()
	{
		Screen screen = Game.getScreen() ;
		Point minCoord = new Point(0, (int) (0.2*screen.mapSize().height)) ;
		Dimension range = new Dimension(screen.mapSize().width, (int) (screen.getBorders()[3] - screen.getBorders()[1])) ;
		Dimension step = new Dimension(1, 1) ;
		setPos(Util.randomPos(minCoord, range, step)) ;
	}
	
	private void switchDirection() { setDir(randomNewDirection(dir)) ;}

	public void updatePos(Directions dir, Point2D.Double CurrentPos, int step, double dt, double moveRate, GameMap map)
	{
		Point2D.Double newPos = MovePattern.calcNewPos(type.movePattern, dir, step, dt, CurrentPos, moveRate) ;
 
		if (!Game.getScreen().posIsWithinBorders(newPos)) { return ;}
		if (!map.groundIsWalkable(new Point((int) newPos.x, (int) newPos.y), null)) { return ;}

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
			int move = Util.randomInt(0, qtdAvailableMoves) ;
			switch (move)
			{
				case 0:	setCurrentAction(battleKeys[0]) ; return ;	// Physical attack
				case 1:	setCurrentAction(battleKeys[1]) ; return ;	// Defense
				case 2:	setCurrentAction(String.valueOf(Util.randomInt(0, spells.size() - 1))) ; return ;	// spell
			}
			return ;
		}
		
		List<Double> modifiedGenes = type.getGenes().getModifiedGenes(playerMove) ;

		int move = Util.randomFromChanceList(modifiedGenes) ;
		switch (move)
		{
			case 0:	setCurrentAction(battleKeys[0]) ; return ;	// Physical attack
			case 1:	setCurrentAction(battleKeys[1]) ; return ;	// Defense
			case 2:	setCurrentAction(String.valueOf(Util.randomInt(0, spells.size() - 1))) ; return ;	// spell
		}

	}

	public void move(Point2D.Double PlayerPos, GameMap map, double dt)
	{
		
		if (chasePlayer)
		{
			setPos(chase(pos, PlayerPos, range)) ;
			return ;
		}

		updatePos(dir, pos, step, dt, movingTimer.rate(), map) ;
		
	}
	
	public void act(Point2D.Double playerPosAsDouble, GameMap playerMap, double dt)
	{
		switch (state)
		{
			case idle:
				// does nothing
				if (idleTimer.hasFinished())
				{
					setState(LiveBeingStates.moving) ;
					movingTimer.restart() ;
				}

				return ;

			case moving:
				move(playerPosAsDouble, playerMap, dt) ;
				if (movingTimer.hasFinished())
				{
					setState(LiveBeingStates.idle) ;
					switchDirection() ;
					idleTimer.restart();
				}

				return;
			
			default: return ;
		}
	}
	
	public void applyPassiveSpell(Spell spell) { }
	
	public void useAutoSpell(boolean activate, Spell spell)
	{
		
	}

	public Elements[] atkElems() { return new Elements[] {atkElem, Elements.neutral, Elements.neutral} ;}
	public Elements[] defElems() { return new Elements[] {atkElem, atkElem} ;}

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
		Elements[] AtkElem = new Elements[] {spell.getElem(), atkElem, Elements.neutral} ;
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

		displayUsedSpellMessage(spell, Game.getScreen().pos(0.63, 0.2), Game.palette[0]);
		PA.getMp().decTotalValue(spell.getMpCost()) ;
		switch (spell.getType())
		{
			case support : useSupportSpell(spell, receiver) ; return new AtkResults(AtkTypes.magical) ;
			case offensive : return useOffensiveSpell(spell, receiver) ;
			default : return new AtkResults(AtkTypes.magical) ;
		}
		
	}
	
	public void displayName(Point pos, Align alignment, Color color)
	{
		Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
		GamePanel.DP.drawText(pos, alignment, Draw.stdAngle, name, font, color) ;
	}
	
	public void display(Point pos, Scale scale)
	{
		if (!idleTimer.isActive())
		{
			type.movingAni.displayMoving(dir, pos, 0, scale, Align.center) ;
		}
		else
		{
			type.movingAni.displayIdle(pos, 0, scale, Align.center) ;
		}
		if (isFighting())
		{
			displayBattleActionCounter() ;
		}
		if (isDrunk())
		{
			displayDrunk() ;
		}
		displayStatus() ;
		GamePanel.DP.drawText(Util.translate(pos, 0, -20), Align.bottomCenter, name + ": " + type.movePattern.toString(), Color.black) ;
		GamePanel.DP.drawText(Util.translate(pos, 0, -30), Align.bottomCenter, state.toString(), Color.black) ;
		GamePanel.DP.drawText(Util.translate(pos, 0, -40), Align.bottomCenter, !idleTimer.isActive() ? "is moving: " + dir : "", Color.black) ;
		if (Game.debugMode)
		{
			displayState() ;
			hitbox.display();
		}
	}	
	
	public void display()
	{
		display(getPos(), Scale.unit) ;
	}
	
	public void displayAdditionalInfo()
	{
		GamePanel.DP.drawText(new Point((int) pos.x, (int) pos.y + 20), Align.center, 0, String.valueOf(this.totalPower()), new Font(Game.MainFontName, Font.BOLD, 14), Color.black) ;
		GamePanel.DP.drawText(getPos(), Align.center, 0, String.valueOf(type.getID()), new Font(Game.MainFontName, Font.BOLD, 24), Color.black) ;
	}
	
	public void dies()
	{
		setState(LiveBeingStates.idle) ;
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
		chasePlayer = false ;
	}
	
	@Override
	public String toString()
	{
		return "Creature [type=" + type + ", Bag=" + items + ", Gold=" + gold + ", color=" + color + ", follow=" + chasePlayer + "]";
	}

	
}
