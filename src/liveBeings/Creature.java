package liveBeings ;

import java.awt.Color ;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import attributes.BasicAttribute;
import attributes.BattleAttributes;
import attributes.PersonalAttributes;
import battle.AtkResults;
import battle.AtkTypes;
import components.HitboxCircle;
import components.HitboxRectangle;
import graphics.Align;
import graphics.Scale;
import items.Item;
import main.Elements;
import main.Game;
import main.GamePanel;
import main.GameTimer;
import main.Log;
import maps.GameMap;
import screen.Screen;
import screen.Sky;
import spells.Spell;
import utilities.Util;

public class Creature extends LiveBeing
{
	private final CreatureType type ;
	private final Set<Item> items ;
	private final int gold ;
	private GameTimer idleTimer ;

 	public Creature(CreatureType CT, Point2D.Double pos)
 	{
 		super(new PersonalAttributes(CT.getPA()), new BattleAttributes(CT.getBA()), CT.getMovingAnimations(), CT.getAttributesWindow()) ;
		
		this.type = CT ;
		this.name = CT.getName();
		this.level = CT.getLevel();
		this.size = new Dimension(CT.getSize());
		this.range = CT.getRange();
		this.step = CT.getStep();
		this.atkElem = CT.getAtkElem();
		this.mpCounter = new GameTimer(CT.getMpTimerDuration() / 1.0);
		this.satiationCounter = new GameTimer(CT.getSatiationTimerDuration());
		this.battleActionCounter = new GameTimer(CT.getBattleActionTimerDuration() * 5.0) ;
		this.movingTimer = new GameTimer(CT.getMovingTimerDuration()) ;
		this.combo = new ArrayList<>() ;
		this.hitbox = CT.getHitboxType().equals("circle") ? new HitboxCircle(new Point(), size.width / 2) : new HitboxRectangle(new Point(), size) ;
		this.dir = LiveBeing.randomDir() ;
		this.state = LiveBeingStates.idle ;
		this.currentAction = "" ;
		this.spells = List.copyOf(CT.getSpell()) ;
		this.items = Set.copyOf(CT.getItems()) ;
		this.gold = CT.getGold() ;

		if (getName().equals("Dragão") | getName().equals("Dragon"))
		{
			setPos(Screen.getMe().getCenterAsDouble()) ;
		}
		startCounters() ;

		this.idleTimer = new GameTimer(CT.getMovingAnimations().getIdleDuration()) ;
 		setPos(pos) ;
        this.attributeBar = new AttributeBar(0, new Point(headPos().x - size.width / 2, headPos().y)) ;
 	}
 	
 	public Creature(CreatureType CT)
	{
 		this(CT, Util.randomPosDouble(new Point2D.Double(0, (int) (0.2*Screen.getMe().mapSize().height)), new Dimension(Screen.getMe().getSize().width, (int) ((1 - (double)(Sky.getHeight())/Screen.getMe().getSize().height) * Screen.getMe().getSize().height)), new Dimension(1, 1))) ;
	}

	public CreatureType getType() {return type ;}
	public BasicAttribute getLife() {return PA.getLife() ;}
	public BasicAttribute getExp() {return PA.getExp() ;}
	public Set<Item> getBag() {return items ;}
	public int getGold() {return gold ;}
	public Point center() { return new Point((int) pos.x, (int) pos.y) ;}

	public Point headPos() { return new Point((int) (pos.x), (int) (pos.y - 0.5 * size.height)) ;}

    public boolean playerIsInVisionRange() { return Game.getPlayer().isAlive() && Util.dist(Game.getPlayer().getPos(), getPos()) <= type.getVision() ;}
	
	public void leaveBattle()
	{
		setState(LiveBeingStates.idle) ;
		PA.getLife().setToMaximum() ;
		PA.getMp().setToMaximum() ;
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
		Screen screen = Screen.getMe() ;
		Point2D.Double minCoord = new Point2D.Double(0, (int) (0.2*screen.mapSize().height)) ;
		Dimension range = new Dimension(screen.mapSize().width, (int) (screen.getBorders()[3] - screen.getBorders()[1])) ;
		Dimension step = new Dimension(1, 1) ;
		setPos(Util.randomPosDouble(minCoord, range, step)) ;
	}
	
	private void switchDirection() { setDir(randomNewDirection(dir)) ;}
    
	private void move(GameMap playerMap, double dt)
	{
		Point2D.Double newPos = MovePattern.calcNewPos(type.getMovePattern(), dir, step, dt, pos, movingTimer.rate()) ;

		if (!Screen.getMe().posIsWithinBorders(newPos)) { return ;}
		if (!map.groundIsWalkable(new Point((int) newPos.x, (int) newPos.y), null)) { return ;}

		setPos(newPos) ;
	}

    public void makeMoveSound()
    {
        if (type.getMoveSound() == null) { return ;}

        double dist = Util.dist(Game.getPlayer().getPos(), getPos()) ;
        double volume = 1.0 - Math.pow(dist / Game.getPlayer().getHearingRange(), 2) ;  
        type.getMoveSound().playAtVolume(volume) ;
    }

	private LiveBeing chooseTarget()
	{
		if (Game.getPlayer().isAlive() && Game.getPet() != null && Game.getPet().isAlive()) { return 0.5 <= Math.random() ? Game.getPlayer() : Game.getPet() ;}		
		if (Game.getPlayer().isAlive()) { return Game.getPlayer()  ;}
		if (Game.getPet() != null && Game.getPet().isAlive()) { return Game.getPet() ;}

		return null ;
	}
		
	public void chooseFightMove(String playerMove)
	{		
		List<Double> modifiedGenes = type.getGenes().getModifiedGenes(playerMove) ;

		int move = Util.randomFromChanceList(modifiedGenes) ;
		switch (move)
		{
			case 0:	setCurrentAction(BATTLE_KEYS[0]) ; return ;	// Physical attack
			case 1:	setCurrentAction(BATTLE_KEYS[1]) ; return ;	// Defense
			case 2:	setCurrentAction(String.valueOf(Util.randomInt(0, spells.size() - 1))) ; return ;	// spell
		}
	}

	private void doBattleAction()
	{		
        if (!isAlive()) { return ;}
        if (!canAtk()) { return ;}

		LiveBeing creatureTarget = chooseTarget() ;
		
		if (!isInRange(creatureTarget.getPosAsDouble())) { return ;}
		
		chooseFightMove(creatureTarget.getCurrentAction()) ;

		if (!hasActed()) { return ;}

        AtkTypes atkType = atkTypeFromAction() ;
		
		if (atkType == null) { return ;}

		setCurrentAtkType(atkType) ;
		AtkResults atkResults = performAtk(atkType, creatureTarget) ;
		creatureTarget.trainDefensive(atkResults) ;
		if (creatureTarget instanceof Player)
		{
			((Player) creatureTarget).getStatistics().updateDefensive(atkResults, creatureTarget.getBA().getPhyDef().getTotal(), creatureTarget.getBA().getMagDef().getTotal()) ;
		}

		updateCombo() ;
		battleActionCounter.restart() ;
		Log.debug("=======================================================\n") ;
	}
	
    public void restartIdleCounter() { idleTimer.restartAtRate(Math.random()) ;}
    public void stopIdleCounter() { idleTimer.stop() ;}

	public void act(Point2D.Double playerPosAsDouble, GameMap playerMap, double dt)
	{
		switch (state)
		{
			case idle:
                if (Aggressiveness.semiAggressive.equals(type.getAggressiveness()) && playerIsInVisionRange() && !Game.getPlayer().isFighting() && Util.chance(0.005))
                {
                    Game.getPlayer().engageInFight(this) ;
                    return ;
                }
                if (Aggressiveness.aggressive.equals(type.getAggressiveness()) && playerIsInVisionRange() && !Game.getPlayer().isFighting())
                {
                    Game.getPlayer().engageInFight(this) ;
                    return ;
                }
				if (idleTimer.hasFinished())
				{
					setState(LiveBeingStates.moving) ;
					movingTimer.restart() ;
                    makeMoveSound() ;
				}

				return ;

			case moving:
                move(playerMap, dt) ;
                if (movingTimer.hasFinished())
                {
                    setState(LiveBeingStates.idle) ;
                    switchDirection() ;
                    idleTimer.restart();
                }
                return;

			case fighting:          
                if (!isInRange(playerPosAsDouble))
                {
                    Point2D.Double newPos = chase(pos, playerPosAsDouble, range) ;
                    if (Screen.getMe().posIsWithinBorders(newPos) && playerMap.groundIsWalkable(new Point((int)newPos.x, (int)newPos.y), null))
                    {
                        setPos(newPos) ;
                    }
                }
                else
                {
                    doBattleAction() ;
                }
                return ;

			default: return ;
		}
	}
	
	public void useAutoSpell(boolean activate, Spell spell)
	{
		
	}

	public Elements[] atkElems() { return new Elements[] {atkElem, Elements.neutral, Elements.neutral} ;}
	public Elements[] defElems() { return new Elements[] {atkElem, atkElem} ;}

	public void display(Point pos, Scale scale)
	{
		if (!idleTimer.isActive())
		{
			type.getMovingAnimations().displayMoving(dir, pos, scale, Align.center) ;
		}
		else
		{
			type.getMovingAnimations().displayIdle(pos, scale, Align.center) ;
		}
		if (isFighting())
		{
			displayBattleActionCounter() ;
		}		
		if (isDefending())
		{
			displayDefending() ;
		}
		if (isDrunk())
		{
			displayDrunk() ;
		}
		displayStatus() ;
		if (isFighting())
		{
			displayAttributes(new Point(headPos().x - size.width / 2, headPos().y), false) ;
		}
        GamePanel.getDP().drawText(Util.translate(pos, 0, -20), Align.bottomCenter, name + ": " + type.getMovePattern().toString(), Color.black) ;
		if (Game.DEBUG_MODE)
        {
            GamePanel.getDP().drawText(Util.translate(pos, 0, -30), Align.bottomCenter, state.toString(), Color.black) ;
			GamePanel.getDP().drawText(Util.translate(pos, 0, -40), Align.bottomCenter, !idleTimer.isActive() ? "is moving: " + dir : "", Color.black) ;
            displayState() ;
			displayPowerBar(Util.translate(pos, 0, -50)) ;
			hitbox.display();
		}
	}	
	
	public void display()
	{
		display(getPos(), Scale.unit) ;
	}
	
	public void dies()
	{
		setState(LiveBeingStates.idle) ;
		PA.getLife().setToMaximum() ;
		PA.getMp().setToMaximum() ;
		if (AtkTypes.defense.equals(currentAtkType))
		{
			deactivateDef() ;
		}
		setRandomPos() ;
	}
	
	@Override
	public String toString()
	{
		return String.format("Creature:\n  Name: %s\n  Level: %d\n  Color: %s\n  Gold: %d\n  Bag: %s\n  Type: %s\n",
				name, level, gold, items.toString(), type) ;
	}	
}
