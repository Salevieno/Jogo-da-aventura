package liveBeings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import attributes.Attributes;
import attributes.BattleAttributes;
import attributes.PersonalAttributes;
import battle.AtkEffects;
import battle.AtkResults;
import battle.AtkTypes;
import components.Hitbox;
import graphics.Align;
import graphics2.Animation;
import graphics2.AnimationTypes;
import graphics2.Drawable;
import graphics2.SpriteAnimation;
import main.Directions;
import main.Elements;
import main.Game;
import main.GamePanel;
import main.GameTimer;
import main.Path;
import main.RelativePos;
import maps.GameMap;
import maps.GroundRegion;
import maps.GroundType;
import utilities.Util;

import windows.AttributesWindow;

public abstract class LiveBeing implements Drawable
{
	protected String name ;
	protected int job ;
	protected int proJob ;
	protected int level;
	protected GameMap map ;
	protected Point2D.Double pos ;
	protected Directions dir ;
	protected LiveBeingStates state ;
	protected Dimension size ;
	protected int range ;
	protected int step ;
	protected Hitbox hitbox ;
	protected Elements atkElem ;
	protected GameTimer hpCounter ;
	protected GameTimer mpCounter ;
	protected GameTimer satiationCounter ;
	protected GameTimer thirstCounter ;
	protected GameTimer battleActionCounter ;
	protected GameTimer movingTimer ;
	protected String currentAction ;
	protected AtkTypes currentAtkType ;
	protected List<String> combo ;				// record of the last 10 movements
	protected List<Spell> spells ;
	protected GameTimer drunk ;
	protected Map<Attributes, LiveBeingStatus> status ;
	
	protected PersonalAttributes PA ;
	protected BattleAttributes BA ;
	protected MovingAnimations movingAni ;
	protected AttributesWindow attWindow ;
	
	private static int damageStyle = 0 ;
	private static final Image attImage = Game.loadImage(Path.PLAYER_IMG + "Attributes.png") ;
	private static final Image drunkImage = Game.loadImage(Path.STATUS_IMG + "Drunk.png") ;
	private static final Image defendingImage = Game.loadImage(Path.BATTLE_IMG + "ShieldIcon.png") ;
	private static final Image powerBarImage = Game.loadImage(Path.LIVE_BEINGS_IMG + "PowerBar.png") ;
	public static final String[] battleKeys = new String[] {"Y", "U"} ;	
	public static final List<String> spellKeys = List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "F10", "F11", "F12") ;
	protected static final SpriteAnimation levelUpAni = new SpriteAnimation(Path.PLAYER_IMG + "LevelUpSprite.png", new Point(), Align.bottomCenter, false, 45, 0.2) ;
	
	public LiveBeing(PersonalAttributes PA, BattleAttributes BA, MovingAnimations movingAni, AttributesWindow attWindow)
	{
		this.PA = PA;
		this.BA = BA;
		this.movingAni = movingAni;
		this.attWindow = attWindow ;
		currentAction = null ;
		currentAtkType = null ;
		hpCounter = new GameTimer(20) ;
		drunk = new GameTimer(20) ;
		status = new HashMap<>() ;
		for (Attributes att : Attributes.values())
		{
			status.put(att, new LiveBeingStatus(att)) ;
		}
	}

	public abstract Point center() ;
	public abstract Point headPos() ;
	public abstract AtkResults useSpell(Spell spell, LiveBeing receiver) ;
	public abstract void applyPassiveSpell(Spell spell) ;
	

	protected void useSupportSpell(Spell spell, LiveBeing receiver)
	{
		// apenas efeitos permanentes entram aqui. Efeitos temporários são causados pelos buffs
		switch (spell.getId())
		{
			case 44:
				double perc = 0.04 * spell.getLevel() * magicAtkBonus() ;
				PA.getLife().incCurrentValue((int) (perc * PA.getLife().getMaxValue())) ;
				break ;
				
			case 112:
				perc = 0.04 * spell.getLevel() * magicAtkBonus() ;
				receiver.getPA().getLife().incCurrentValue((int) (perc * receiver.getPA().getLife().getCurrentValue())) ;
				break ;
				
			case 141:
				if (!(this instanceof Player)) { return ;}
				if (!(receiver instanceof Creature)) { return ;}
				Creature creature = (Creature) receiver ;
				((Player) this).getBag().add(creature.getRandomElemFromBag(), 1) ;
				break ;
			
			default: break ;
		}

		
		spell.applyBuffs(true, this) ;
		
		if (receiver == null) { return ;}
		
		spell.applyDebuffs(true, receiver) ;
		
	}
	
	public abstract void useAutoSpell(boolean activate, Spell spell);
	public abstract void dies() ;
	
	public String getName() {return name ;}
	public int getLevel() {return level ;}
	public int getJob() {return job ;}
	public int getProJob() {return proJob ;}
	public GameMap getMap() {return map ;}
	public Point getPos() { return new Point((int) pos.x, (int)pos.y) ;}
	public Point2D.Double getPosAsDouble() {return pos ;}
	public Elements getAtkElem() { return atkElem ;}
	public String getCurrentAction() {return currentAction ;}
	public AtkTypes getCurrentAtkType() { return currentAtkType ;}
	public GameTimer getHpCounter() {return hpCounter ;}
	public GameTimer getBattleActionCounter() {return battleActionCounter ;}
	public List<String> getCombo() {return combo ;}
	public List<Spell> getSpells() {return spells ;}
	public AttributesWindow getAttWindow() {return attWindow ;}
	public MovingAnimations getMovingAni() {return movingAni ;}
	public Map<Attributes, LiveBeingStatus> getStatus() {return status ;}
	public Hitbox getHitbox() {return hitbox ;}
	public void setCurrentAction(String newValue) {currentAction = newValue ;}
	public void setMpCounter(GameTimer mpCounter) { this.mpCounter = mpCounter ;}
	public void setBattleActionCounter(GameTimer battleActionCounter) { this.battleActionCounter = battleActionCounter ;}
	public void setSatiationCounter(GameTimer satiationCounter) { this.satiationCounter = satiationCounter ;}
	public void setThirstCounter(GameTimer thirstCounter) { this.thirstCounter = thirstCounter ;}

	public void setName(String newValue) {name = newValue ;}
	public void setLevel(int newValue) {level = newValue ;}
	public void setJob(int newValue) {job = newValue ;}
	public void setProJob(int newValue) {proJob = newValue ;}
	public void setMap(GameMap newValue) {map = newValue ;}
	public void setDir(Directions newValue) {dir = newValue ;}
	public void setState(LiveBeingStates newValue) {state = newValue ;}
	public void setPos(Point2D.Double newValue)
	{
		pos = newValue ;
		hitbox.setCenter(center()) ;
	}
	public void moveIfWalkable(Point2D.Double dist)
	{
		Point2D.Double newPos = new Point2D.Double(pos.x + dist.x, pos.y + dist.y) ;
		Elements superElem = this instanceof Player ? ((Player) this).getSuperElem() : null ;
		GameMap map = Game.getPlayer().getMap() ;
		// TODO melhorar para pegar ponto "walkable" mais próximo e mover para ele, jogando para a projeção movível
		if (map.groundIsWalkable(new Point((int)newPos.x, (int)newPos.y), superElem))
		{
			setPos(newPos) ;
		}
		else
		{
			newPos = new Point2D.Double(pos.x + dist.x, pos.y) ;
			if (map.groundIsWalkable(new Point((int)newPos.x, (int)newPos.y), superElem))
			{
				setPos(newPos) ;
			}
			else
			{
				newPos = new Point2D.Double(pos.x, pos.y + dist.y) ;
				if (map.groundIsWalkable(new Point((int)newPos.x, (int)newPos.y), superElem))
				{
					setPos(newPos) ;
				}
			}
		}
	}
	public void setRange(int newValue) {range = newValue ;}
	public void setStep(int newValue) {step = newValue ;}
	public void setCombo(List<String> newValue) {combo = newValue ;}
	public void setCurrentAtkType(AtkTypes ba) { currentAtkType = ba ;}
	
	public boolean isMoving() { return movingTimer.isActive() ;}
	public boolean canAct() { return (state.equals(LiveBeingStates.idle) | isMoving() | isFighting()) ;}
	
	public boolean isPlayerAlly() { return (this instanceof Player | this instanceof Pet) ;}
	
	public void resetAction() { currentAction = null ;}
	public void resetBattleAction() { currentAtkType = null ;}
	protected void startMoving() { movingTimer.start() ;}
	protected void restartMoving() { movingTimer.restart() ;}
	
	public static void updateDamageAnimation(int newDamageStyle) { damageStyle = newDamageStyle ;}

	public void resetStatus()
	{
		for (Attributes att : Attributes.values())
		{
			status.get(att).reset() ;
		}
	}
	public void finishStatus()
	{
		for (Attributes att : Attributes.values())
		{
			switch (att)
			{			
				case phyAtk:
					if (0 < status.get(att).getTimer().getDuration() & status.get(att).getTimer().hasFinished())
					{
						BA.getPhyAtk().incBonus(-status.get(att).getIntensity()) ;
						status.get(att).reset() ;
					}
					continue ;
			
				case magAtk:
					if (0 < status.get(att).getTimer().getDuration() & status.get(att).getTimer().hasFinished())
					{
						BA.getMagAtk().incBonus(-status.get(att).getIntensity()) ;
						status.get(att).reset() ;
					}
					continue ;
					
				case magDef:
					if (0 < status.get(att).getTimer().getDuration() & status.get(att).getTimer().hasFinished())
					{
						BA.getMagDef().incBonus(-status.get(att).getIntensity()) ;
						status.get(att).reset() ;
					}
					continue ;
					
				case phyDef:
					if (0 < status.get(att).getTimer().getDuration() & status.get(att).getTimer().hasFinished())
					{
						BA.getPhyDef().incBonus(-status.get(att).getIntensity()) ;
						status.get(att).reset() ;
					}
					continue ;
					
				case dex:
					if (0 < status.get(att).getTimer().getDuration() & status.get(att).getTimer().hasFinished())
					{
						BA.getDex().incBonus(-status.get(att).getIntensity()) ;
						status.get(att).reset() ;
					}
					continue ;
					
				case agi:
					if (0 < status.get(att).getTimer().getDuration() & status.get(att).getTimer().hasFinished())
					{
						BA.getAgi().incBonus(-status.get(att).getIntensity()) ;
						status.get(att).reset() ;
					}
					continue ;
					
				case atkSpeed:
					if (0 < status.get(att).getTimer().getDuration() & status.get(att).getTimer().hasFinished())
					{
						BA.getAtkSpeed().incBonus(-status.get(att).getIntensity()) ;
						battleActionCounter.setDuration(BA.TotalAtkSpeed()) ;
						status.get(att).reset() ;
					}
					continue ;
					
				case stun, block, blood, poison, silence:
					if (0 < status.get(att).getTimer().getDuration() & status.get(att).getTimer().hasFinished())
					{
						status.get(att).reset() ;
					}
					continue ;
					
				default: continue ;
			}
		}
	}
	
	public PersonalAttributes getPA() {return PA ;}
	public void setPA(PersonalAttributes pA) { PA = pA ;}
	public BattleAttributes getBA() {return BA ;}
	public void setBA(BattleAttributes bA) { BA = bA ;}
	
	protected double magicAtkBonus() { return 1 + Math.min(0.25, 0.25 * BA.TotalMagAtk() / 200.0) ;}
	
	public static Directions randomDir() { return Directions.getDir(Util.randomInt(0, 3)) ;}
	
	public static Directions randomPerpendicularDir(Directions originalDir)
	{
		int side = Util.randomInt(0, 1) ;
		return switch (originalDir)
		{
			case up, down -> side == 0 ? Directions.left : Directions.right ;
			case left, right -> side == 0 ? Directions.up : Directions.down ;
		};
	}
	
	public Directions randomNewDirection(Directions originalDir)
	{
		List<Directions> allowedDirections = new ArrayList<>(Arrays.asList(Directions.values())) ;
		allowedDirections.remove(originalDir) ;
		return allowedDirections.get(Util.randomInt(0, allowedDirections.size() - 1)) ;	
	}
	
	public static GameMap calcNewMap(Point2D.Double pos, Directions dir, GameMap currentMap)
	{
		int[] mapConnections = currentMap.getConnections() ;
		boolean leftSide = pos.x <= Game.getScreen().mapSize().width / 2 ;
		boolean topSide = pos.y <= Game.getScreen().getBorders()[1] + Game.getScreen().mapSize().height / 2 ;
		int newMapID = switch (dir)
		{
			case up -> newMapID = leftSide ? mapConnections[1] : mapConnections[0] ;			
			case left -> newMapID = topSide ? mapConnections[2] : mapConnections[3] ;
			case down -> newMapID = leftSide ? mapConnections[4] : mapConnections[5] ;
			case right -> newMapID = topSide ? mapConnections[7] : mapConnections[6] ;
		};
		
		if (newMapID == -1) { return null ;}
		
		return Game.getMaps()[newMapID] ;		
	}

	public void displayState()
	{
		Point displayPos = Util.translate(getPos(), 0, size.height + 10) ;
		// Dimension size = new Dimension(60, 20) ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
		String stateText = state.toString() ;
		
//		GamePanel.DP.drawRoundRect(displayPos, Align.center, size, 1, Game.palette[21], Game.palette[0], true);
		GamePanel.DP.drawText(displayPos, Align.center, 0, stateText, font, Game.palette[0]) ;
	}

	public void displayUsedSpellMessage(Spell spell, Point pos, Color color)
	{
		Animation.start(AnimationTypes.message, battleActionCounter.getDuration(), new Object[] {pos, spell.getName(), color}) ;
	}
	
	public void displayPowerBar(Point pos)
	{
		int maxPower = 10000 ;
		Color color = Game.palette[6] ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 11) ;
		Dimension barSize = new Dimension(15, powerBarImage.getHeight(null) * totalPower() / maxPower) ;
		
		GamePanel.DP.drawRect(Util.translate(pos, 0, -13), Align.bottomCenter, barSize, 0, color, null, 1.0) ;
		GamePanel.DP.drawImage(powerBarImage, pos, Align.bottomCenter) ;
		GamePanel.DP.drawText(Util.translate(pos, 0, -powerBarImage.getHeight(null) - 10), Align.bottomCenter, 0, String.valueOf(totalPower()), font, color) ;
	}

	private static Directions calcDrunkDir(Directions dir, GameTimer drunk)
	{
		boolean changeDir = Util.chance(0.8 * (1 - drunk.rate())) ;
		return changeDir ? randomPerpendicularDir(dir) : dir ;
	}
	
	public Point2D.Double calcNewPos()
	{
		Directions moveDir = isDrunk() ? calcDrunkDir(dir, drunk) : dir ;		
		return MovePattern.calcNewPos(moveDir, pos, step) ;
	}

	public void startCounters()
	{
		mpCounter.start() ;
		satiationCounter.start() ;
		if (this instanceof Player) { thirstCounter.start() ;}
		spells.forEach(spell -> spell.getCooldownCounter().start()) ;
	}
	
	public void activateSpellCounters()
	{
		for (Spell spell : spells)
		{
			if (spell.getDurationCounter().getDuration() <= 0 | !spell.getDurationCounter().hasFinished())
			{
				continue ;
			}
			
			if (this instanceof Player & job == 3 & spell.getId() == 114)
			{
				if (10 <= Game.getPet().getAlchBuffId() & Game.getPet().getAlchBuffId() <= 12)
				{
					Buff.allBuffs.get(Game.getPet().getAlchBuffId()).apply(-1, spells.get(10).getLevel(), Game.getPet()) ;
					Game.getPet().setAlchBuffId(-1) ;
				}
			}
			spell.getDurationCounter().reset() ;
			spell.applyBuffs(false, this) ;
			spell.deactivate() ;
		}
	}
	
	public void activateCounters()
	{
		if (hpCounter.isActive() & hpCounter.crossedTime(0.75))
		{
			PA.getLife().incCurrentValue(2) ;
		}
		if (mpCounter.hasFinished())
		{
			PA.getMp().incCurrentValue(1) ;
			mpCounter.start() ;
		}
		if (!(this instanceof Creature))
		{
			if (satiationCounter.hasFinished())
			{
				PA.getSatiation().incCurrentValue(-1) ;
				satiationCounter.start() ;
				if (PA.getSatiation().getCurrentValue() == 0)
				{
					takeDamage(1) ;
				}
			}
		}
		
		if (this instanceof Player)
		{
			if (thirstCounter.hasFinished())
			{
				PA.getThirst().incCurrentValue(-1) ;
				thirstCounter.start() ;
				if (PA.getThirst().getCurrentValue() == 0)
				{
//					PA.getLife().incCurrentValue(-1) ;
					takeDamage(1) ;
				}
			}
		}
		
//		if (!isAlive()) { dies() ;}
	}
	
	public void resetBattleActions() { battleActionCounter.start() ; }
	
	public int totalPower()
	{
		// TODO optional - consider special ba, element mult, mp with spells and items
		// Dano = nHits . hitRate . (PhyAtkRate . PhyDam + MagAtkRate . MagDam + BloodRate . BloodDam + PoisonRate . PoisonDam)
		
		LiveBeing defender = new Creature(CreatureType.all.get(0)) ;

//		new CreatureType(0, "", 0, new Dimension(0, 0), 60, 0, new Elements[] {Elements.water}, 0, 0, 0, 0, 0, null,
//				new PersonalAttributes(new BasicAttribute(100, 100, 1), new BasicAttribute(100, 100, 1), new BasicAttribute(100, 100, 1), new BasicAttribute(100, 100, 1), new BasicAttribute(100, 100, 1)),
//				new BattleAttributes(new BasicBattleAttribute(3, 0, 0), new BasicBattleAttribute(3, 0, 0), new BasicBattleAttribute(3, 0, 0), new BasicBattleAttribute(3, 0, 0), new BasicBattleAttribute(3, 0, 0), new BasicBattleAttribute(3, 0, 0),
//						new BasicBattleAttribute(0.1, 0, 0), new BasicBattleAttribute(0, 0, 0),
//						new BattleSpecialAttribute(0, 0, 0, 0, 0), new BattleSpecialAttribute(0, 0, 0, 0, 0), new BattleSpecialAttributeWithDamage(0, 0, 0, 0, 0, 0, 0, 0, 0), new BattleSpecialAttributeWithDamage(0, 0, 0, 0, 0, 0, 0, 0, 0), new BattleSpecialAttribute(0, 0, 0, 0, 0),
//						new BasicBattleAttribute(1, 0, 0)),
//				new ArrayList<>(),
//				new HashSet<>(),
//				0, null, new int[] {})

		// double attackerAtkRate = 1 ;
		// double defenderAtkRate = 1 ;
		// double critRate = this.getBA().getCritAtk().getTotal() - defender.getBA().getCritDef().getTotal() ;
		// double hitRate = 1 - 1 / (1 + Math.pow(1.1, this.getBA().getDex().getTotal() - defender.getBA().getAgi().getTotal())) ;
		// int phyDamBase = Math.max((int) Util.round(this.getBA().getPhyAtk().getTotal() - defender.getBA().getPhyDef().getTotal(), 0), 0) ;
		// int phyDamInDefense = Math.max((int) Util.round(this.getBA().getPhyAtk().getTotal() - defender.getBA().getPhyDef().getTotal() - defender.getBA().getPhyDef().getBaseValue(), 0), 0) ;
		// int phyDamCrit = (int) Util.round(this.getBA().getPhyAtk().getTotal(), 0) ;


		// double movesPerSec = 1 / this.getBattleActionCounter().getDuration() ;
		// double damAvr = (1 - critRate) * (defenderAtkRate * phyDamBase + (1 - defenderAtkRate) * phyDamInDefense) + critRate * phyDamCrit ;
		// double damPerSec = damAvr * hitRate * attackerAtkRate * movesPerSec ;
		// double timeToWin = defender.getPA().getLife().getMaxValue() / damPerSec ;
		
		int totalPower = (int) (10000 / timeToWin(defender)) ;
		return totalPower ;
		
	}
	
	private double timeToWin(LiveBeing defender)
	{		
		double rateMagAtkAttacker =	this instanceof Player ? 0.4147 :  0.366;
		double ratePhyAtkAttacker = 0.5 - rateMagAtkAttacker / 2.0  ;
		double rateDefDefender = 0.5 - rateMagAtkAttacker / 2.0 ;

		double critRate = this.getBA().getCritAtk().getTotal() - defender.getBA().getCritDef().getTotal() ;
		double hitRate = 1 - 1 / (1 + Math.pow(1.1, this.getBA().getDex().getTotal() - defender.getBA().getAgi().getTotal())) ;
		double bloodRate = 0 ; // Math.max(this.getBA().getBlood().getBasicAtkChance() - defender.getBA().getBlood().getBasicDefChance(), 0)
		
		double spellDam = this instanceof Player ? 2.0 + this.getBA().getMagAtk().getTotal() - defender.getBA().getMagDef().getTotal() : 1 + 1.02 * this.getBA().getMagAtk().getTotal() - defender.getBA().getMagDef().getTotal() ;
		
		int phyDamBase = Math.max((int) Util.round(this.getBA().getPhyAtk().getTotal() - defender.getBA().getPhyDef().getTotal(), 0), 0) ;
		int spellDamBase = Math.max((int) Util.round(spellDam, 0), 0) ;
		int phyDamInDefense = Math.max((int) Util.round(this.getBA().getPhyAtk().getTotal() - defender.getBA().getPhyDef().getTotal() - defender.getBA().getPhyDef().getBaseValue(), 0), 0) ;
		int magDamInDefense = Math.max((int) Util.round(spellDam - defender.getBA().getMagDef().getBaseValue(), 0), 0) ;
		int bloodDam = Math.max((int) Util.round(this.getBA().getBlood().TotalAtk() - defender.getBA().getBlood().TotalDef(), 0), 0) ;
		int phyDamCrit = (int) Util.round(this.getBA().getPhyAtk().getTotal(), 0) ;
		int spellDamCrit = (int) Util.round(this instanceof Player ? 2.0 + this.getBA().getMagAtk().getTotal() : 1 + 1.02 * this.getBA().getMagAtk().getTotal(), 0) ;
		
		double bloodDuration = this.getBA().getBlood().getDuration() ;
		double damPerMove = ratePhyAtkAttacker * hitRate * ((1 - critRate) * ((1 - rateDefDefender) * phyDamBase + rateDefDefender * phyDamInDefense) + critRate * phyDamCrit) +
							rateMagAtkAttacker * hitRate * ((1 - critRate) * ((1 - rateDefDefender) * spellDamBase + rateDefDefender * magDamInDefense) + critRate * spellDamCrit) +
							bloodRate * bloodDam * bloodDuration ;
		
		
		double movesPerSec = 1 / this.getBattleActionCounter().getDuration() ;
		double damPerSec = damPerMove * hitRate  * movesPerSec ;
		double timeToWin = defender.getPA().getLife().getMaxValue() / damPerSec ;

		return timeToWin ;
	}
	
	public void resetCombo()
	{
		setCombo(new ArrayList<String>()) ;
	}
	
	public void updateCombo()
	{
		if (currentAction != null)
		{
			if (combo.size() <= 9)
			{
				combo.add(currentAction) ;
			}
			else
			{
				combo.add(0, currentAction) ;
				combo.remove(combo.size() - 1) ;
			}
		}
	}
	
	public List<Spell> getActiveSpells() { return spells.stream().filter(Spell::isUsable).collect(Collectors.toList()) ;}
	
	public boolean isAlive() { return 0 < PA.getLife().getTotalValue() ;}
	public boolean hasTheSpell(String action) {return Player.spellKeys.indexOf(action) < getActiveSpells().size() ;}
	public boolean hasEnoughMP(Spell spell)	{return (spell.getMpCost() <= PA.getMp().getCurrentValue()) ;}
	public boolean hasActed() {return currentAction != null ;}
	public boolean actionIsSpell()
	{
		if (!hasActed()) { return false ;}

		if (!spellKeys.contains(currentAction)) { return false ;}
		
		int spellID = spellKeys.indexOf(currentAction) ;
		
		if (getActiveSpells().size() <= spellID) { return false ;}
		
		return true ;
	}
	public boolean usedSpell()
	{
		if (!actionIsSpell()) { return false ;}

		int spellID = spellKeys.indexOf(currentAction) ;
		Spell spell = getActiveSpells().get(spellID);
		
		if (!canUseSpell(spell)) { return false ;}
		
		return true ;
	}
	public boolean canUseSpell(Spell spell)
	{

		if (isSilent()) { return false ;}
		if (!spell.isReady()) { return false ;}
		if (!hasEnoughMP(spell)) { return false ;}
		
		return 1 <= spell.getLevel() ;
		
	}
	public boolean usedPhysicalAtk() {return hasActed() ? currentAction.equals(battleKeys[0]) : false ;}
	public boolean usedDef() {return hasActed() ? currentAction.equals(battleKeys[1]) : false ;}
	public boolean actionIsArrowAtk()
	{
		if (!( this instanceof Player)) { return false ;}
		return (usedPhysicalAtk() | usedSpell()) & ((Player) this).arrowIsEquipped() ;
	}
	
	public boolean canAtk() {return battleActionCounter.hasFinished() & !isStun() ;}
	public boolean isStun() { return status.get(Attributes.stun).isActive() ;}
	public boolean isSilent() {return status.get(Attributes.silence).isActive() ;}
	public boolean isDefending()
	{
		if (combo == null) { return false ;}
		if (combo.isEmpty()) { return false ;}
		
		if (this instanceof Player)
		{
			return (!battleActionCounter.hasFinished() & combo.get(0).equals(battleKeys[1])) ;
		}
		return usedDef() ;
	}
	public boolean isDrunk() {return drunk.isActive() ;}
	public boolean isInCloseRange(Point2D.Double target) {return pos.distance(target) <= size.getWidth() ;}
	public boolean isInRange(Point2D.Double target) {return pos.distance(target) <= range ;}

	public boolean isTouching(GroundType groundType)
	{		
 		RelativePos adjGround = checkAdjacentGround(pos, map, groundType, step) ;
 		
 		if (adjGround == null) { return false ;}
 		
 		List<RelativePos> adjPositions = Arrays.asList(RelativePos.values()) ;
 		
 		return adjPositions.contains(adjGround) ;
	}

	public boolean isInside(GroundType groundType)
	{ 
 		RelativePos adjGround = checkAdjacentGround(pos, map, groundType, step) ;
 		
 		if (adjGround == null) { return false ;}
 		
 		return adjGround.equals(RelativePos.inside) ;
	}

	public boolean isFighting() { return state.equals(LiveBeingStates.fighting) ;}
		
		
 	
	public static RelativePos checkAdjacentGround(Point2D.Double pos, GameMap map, GroundType targetGroundType, int step)
	{ 		
 		Point2D.Double userPos = new Point2D.Double(pos.x, pos.y) ;

		if (map == null) { return null ;}
		
		if (map.getgroundTypes() == null || map.getgroundTypes().isEmpty()) { return null ;}

		for (GroundRegion groundType : map.getgroundTypes())
		{
			if (!groundType.getType().equals(targetGroundType)) { continue ;}	
			
			return posRelativeToPolygon(userPos, groundType.getRegion(), step) ;
		}
		
		return null ;
	}

	private static RelativePos posRelativeToPolygon(Point2D.Double pos, Polygon polygon, int step)
	{
		if (polygon.contains(pos)) { return RelativePos.inside ;}
		if (polygon.contains(MovePattern.calcNewPos(Directions.down, pos, step))) { return RelativePos.above ;}
		if (polygon.contains(MovePattern.calcNewPos(Directions.right, pos, step))) { return RelativePos.left ;}
		if (polygon.contains(MovePattern.calcNewPos(Directions.left, pos, step))) { return RelativePos.right ;}
		if (polygon.contains(MovePattern.calcNewPos(Directions.up, pos, step))) { return RelativePos.below ;}

		return null ;
	}

	public abstract Elements[] atkElems() ;
	public abstract Elements[] defElems() ;
	
	public Point2D.Double chase(Point2D.Double userPos, Point2D.Double target, int minDist)
	{
		
		Point2D.Double pos = (Point2D.Double) userPos.clone() ;
		int step = 1 ;
		double distY = Math.abs(pos.y - target.y) ;
		double distX = Math.abs(pos.x - target.x) ;

		if (pos.distance(target) <= minDist) { return pos ;}
		
		if (distY < distX)
		{
			if (pos.x < target.x) { return new Point2D.Double(pos.x + step, pos.y) ;}
			
			return new Point2D.Double(pos.x - step, pos.y) ;
		}
		
		if (pos.y < target.y) { return new Point2D.Double(pos.x, pos.y + step) ;}
		
		return new Point2D.Double(pos.x, pos.y - step) ;

	}
	
	public void checkDeactivateDef()
	{
		if (battleActionCounter.hasFinished() & isDefending())
		{
			deactivateDef() ;
		}
	}
	
	public void activateDef()
	{
		BA.getPhyDef().incBonus(BA.getPhyDef().getBaseValue()) ;
		BA.getMagDef().incBonus(BA.getMagDef().getBaseValue()) ;
	}
	public void deactivateDef()
	{
		BA.getPhyDef().incBonus(-BA.getPhyDef().getBaseValue()) ;
		BA.getMagDef().incBonus(-BA.getMagDef().getBaseValue()) ;
	}
	public void trainOffensive(AtkResults atkResults)
	{

		if (atkResults.getAtkType() == null) { return ;}		
		
		AtkEffects effect = atkResults.getEffect() ;
		AtkTypes atkType = atkResults.getAtkType() ;
		if (atkType.equals(AtkTypes.physical))
		{
			BA.getPhyAtk().incTrain(0.025 / (BA.getPhyAtk().getTrain() + 1)) ;					
		}
		if (effect != AtkEffects.none)
		{
			if (effect.equals(AtkEffects.crit))
			{
				if (job == 2)
				{
					BA.getCritAtk().incBonus(0.025 * 0.000212 / (BA.getCritAtk().getBonus() + 1)) ;	// 100% after 10,000 hits starting from 0.12
				}
			}
			if (effect.equals(AtkEffects.hit))
			{
				BA.getDex().incTrain(0.025 / (BA.getDex().getTrain() + 1)) ;
			}
		}
		if (atkType.equals(AtkTypes.magical))
		{
			BA.getMagAtk().incTrain(0.025 / (BA.getMagAtk().getTrain() + 1)) ;
		}
		if (atkType.equals(AtkTypes.defense))
		{
			BA.getPhyDef().incTrain(0.025 / (BA.getPhyDef().getTrain() + 1)) ;
			BA.getMagDef().incTrain(0.025 / (BA.getMagDef().getTrain() + 1)) ;
		}
		
	}
	
	public void trainDefensive(AtkResults atkResults)
	{
		
		if (atkResults.getAtkType() == null) { return ;}
		
		AtkEffects effect = atkResults.getEffect() ;
		AtkTypes atkType = atkResults.getAtkType() ;
		if (!atkType.equals(AtkTypes.defense) & effect.equals(AtkEffects.miss))
		{
			BA.getAgi().incTrain(0.0125 / (BA.getAgi().getTrain() + 1)) ;					
		}
		
	}
	
	public void displayAttributes(int style)
	{
		
		List<Double> attRate = new ArrayList<>() ;
		List<Color> attColor = new ArrayList<>() ;
		
		attRate.add(PA.getLife().getRate()) ;
		attColor.add(Game.palette[7]) ;
		attRate.add(PA.getMp().getRate()) ;
		attColor.add(Game.palette[20]) ;
		if (!(this instanceof Creature))
		{
			attRate.add(PA.getExp().getRate()) ;
			attColor.add(Game.palette[5]) ;
			attRate.add(PA.getSatiation().getRate()) ;
			attColor.add(Game.palette[15]) ;
			attRate.add(PA.getThirst().getRate()) ;
			attColor.add(Game.palette[21]) ;
		}
		
		if (style == 0)
		{
			Dimension barSize = new Dimension(32, 4) ;
			int sy = barSize.height;
			int clearSpace = 2 ;
			for (int i = 0; i <= attRate.size() - 1; i += 1)
			{
				Point barPos = Util.translate(getPos(), -size.width / 2, -size.height - attRate.size() * barSize.height - clearSpace + i * sy) ;
				Dimension filledSize = new Dimension((int)(attRate.get(i) * barSize.width), barSize.height) ;
				GamePanel.DP.drawRect(barPos, Align.topLeft, filledSize, 1, attColor.get(i), Game.palette[0], 1.0) ;
			}
		}
		if (style == 1)
		{			
			Point topLeft = Game.getScreen().pos(0.01, 0.02) ;
			Dimension barSize = new Dimension(5, 35) ;
			int stroke = 1 ;
			GamePanel.DP.drawImage(attImage, topLeft, Align.topLeft) ;
			Point offset = new Point(37, 44) ;
			Point barPos = Util.translate(topLeft, offset.x, offset.y) ;
			for (int att = 0; att <= attRate.size() - 1; att += 1)
			{
				Dimension rateSize = new Dimension(barSize.width, (int) (attRate.get(att) *  barSize.height)) ;
				GamePanel.DP.drawRect(barPos, Align.bottomCenter, barSize, stroke, null, Game.palette[0], 1.0) ;
				GamePanel.DP.drawRect(barPos, Align.bottomCenter, rateSize, stroke, attColor.get(att), null, 1.0) ;
				barPos.x += barSize.width + 15 ;
			}
		}
		
	}
	
	public void displayBattleActionCounter()
	{
		double rate = battleActionCounter.rate() ;
		int stroke = 1 ;
		int angleStart = 135 ;
		int angleMaxSpan = 235 ;
		int angleFilledSpan = (int) (angleMaxSpan * rate) ;
		int innerRadius = 10 ;
		int outerRadius = 15 ;
		
		// draw fill
		GamePanel.DP.drawAnnularSector(getPos(), innerRadius, outerRadius, angleStart, angleFilledSpan, stroke, Game.palette[21], null) ;
		
		// draw contour
		GamePanel.DP.drawAnnularSector(getPos(), innerRadius, outerRadius, angleStart, angleMaxSpan, stroke, null, Game.palette[0]) ;
	}

	public void takeDamage(int damage)
	{
		if (damage <= 0) { return ;}
		
		PA.getLife().decTotalValue(damage) ;
		// playDamageAnimation(damageStyle, new AtkResults(1), Game.palette[7]) ;
	}
	
	public void takeBloodAndPoisonDamage()
	{
		int bloodDamage = 0, poisonDamage = 0 ;
		double bloodMult = 1, poisonMult = 1 ;
		LiveBeingStatus bloodStatus = status.get(Attributes.blood) ;
		LiveBeingStatus poisonStatus = status.get(Attributes.poison) ;
		if (this instanceof Player & job == 4)
		{
			poisonMult += -0.1 * spells.get(13).getLevel() ;
		}
		if (bloodStatus.isActive() & bloodStatus.getTimer().crossedTime(0.5))
		{
			bloodDamage = (int) (bloodStatus.getIntensity() * bloodMult) ;
			if (this instanceof Player) {((Player) this).getStatistics().updateReceivedBlood(bloodDamage, BA.getBlood().TotalDef()) ;}
			playDamageAnimation(damageStyle, new AtkResults(AtkTypes.physical, AtkEffects.hit, bloodDamage, null), Game.palette[7]) ;
		}
		if (poisonStatus.isActive() & poisonStatus.getTimer().crossedTime(0.5))
		{
			poisonDamage = (int) (poisonStatus.getIntensity() * poisonMult) ;
			if (this instanceof Player) {((Player) this).getStatistics().updateReceivedPoison(poisonDamage, BA.getPoison().TotalDef()) ;}
			playDamageAnimation(damageStyle, new AtkResults(AtkTypes.physical, AtkEffects.hit, poisonDamage, null), Game.palette[18]) ;
		}
		
		if (bloodDamage + poisonDamage <= 0) { return ;}
		
		PA.getLife().decTotalValue(bloodDamage + poisonDamage) ;
	}

	public void playDamageAnimation(int damageStyle, AtkResults atkResults, Color color)
	{
		Animation.start(AnimationTypes.damage, new Object[] {headPos(), damageStyle, atkResults, color});
	}

	public void playDamageAnimation(AtkResults atkResults, Color color) { playDamageAnimation(damageStyle, atkResults, color) ;}
	
	public void displayStatus()
	{
		Point offset = new Point(-size.width / 2 + 4, size.height / 2 + 4) ;
		Point imgPos = Util.translate(center(), offset.x, -offset.y) ;
		
		for (Attributes att : Attributes.values())
		{
			if (!status.get(att).isActive()) { continue ;}
			
			status.get(att).display(imgPos, size, dir) ;
			imgPos.y += 22 ;
		}
	}
	
	public void displayDrunk()
	{
		Point offset = new Point(size.width / 2 + drunkImage.getWidth(null) / 2 + 2, defendingImage.getHeight(null) + 2) ;
		Point imagePos = Util.translate(center(), -offset.x, 0) ;
		GamePanel.DP.drawImage(drunkImage, imagePos, Align.center) ;
	}
	
	public void displayDefending()
	{
		Point offset = new Point(size.width / 2 + defendingImage.getWidth(null) / 2 + 2, 0) ;
		Point imagePos = Util.translate(center(), -offset.x, 0) ;
		GamePanel.DP.drawImage(defendingImage, imagePos, Align.center) ;
	}

	public void getsDrunk(int duration)
	{
		drunk.setDuration(duration) ;
		drunk.start() ;
	}

}
