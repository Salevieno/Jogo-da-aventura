package liveBeings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import attributes.BattleAttributes;
import attributes.PersonalAttributes;
import components.SpellTypes;
import graphics.DrawPrimitives;
import graphics.Gif;
import main.AtkResults;
import main.AtkTypes;
import main.Game;
import maps.Continents;
import maps.GameMap;
import maps.GroundType;
import maps.GroundTypes;
import utilities.Align;
import utilities.AtkEffects;
import utilities.Directions;
import utilities.Elements;
import utilities.FrameCounter;
import utilities.Log;
import utilities.RelativePos;
import utilities.UtilG;
import utilities.UtilS;
import windows.AttributesWindow;

public abstract class LiveBeing
{
	protected String name ;
	protected int job ;
	protected int proJob ;
	protected int level;
	protected GameMap map ;
	protected Point pos ;						// bottomCenter
	protected Directions dir ;
	protected LiveBeingStates state ;
	protected Dimension size ;
	protected int range ;
	protected int step ;
	protected Elements[] elem ;					// 0: Atk, 1: Weapon, 2: Armor, 3: Shield, 4: SuperElem
	protected FrameCounter mpCounter ;
	protected FrameCounter satiationCounter ;
	protected FrameCounter thirstCounter ;
	protected FrameCounter actionCounter ;
	protected FrameCounter battleActionCounter ;
	protected FrameCounter stepCounter ;			// counts the steps in the movement
	protected String currentAction ;
	protected AtkTypes currentAtkType ;
	protected List<String> combo ;				// record of the last 10 movements
	protected List<Spell> spells ;
	
	protected PersonalAttributes PA ;
	protected BattleAttributes BA ;
	protected MovingAnimations movingAni ;
	protected AttributesWindow attWindow ;
	
	public static final Image AttImage = UtilS.loadImage("\\Player\\" + "Attributes.png") ;
//	public static final Image[] StatusImages = new Image[] {
//			UtilS.loadImage("\\Status\\" + "Stun.png"),
//			UtilS.loadImage("\\Status\\" + "Block.png"),
//			UtilS.loadImage("\\Status\\" + "Blood.png"),
//			UtilS.loadImage("\\Status\\" + "Poison.png"),
//			UtilS.loadImage("\\Status\\" + "Silence.png")
//			};
	public static final Image defendingImage = UtilS.loadImage("\\Battle\\" + "ShieldIcon.png") ;
	public static final Image powerBarImage = UtilS.loadImage("PowerBar.png") ;
	public static final String[] BattleKeys = new String[] {"Y", "U"} ;	
	public static final List<String> SpellKeys = List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "F10", "F11", "F12") ;

	protected final static Gif levelUpGif = new Gif("Level up", UtilS.loadImage("\\Player\\" + "LevelUp.gif"), 170, false, false) ;
	private static final Gif phyHitGif = new Gif("phyHit", UtilS.loadImage("\\Battle\\" + "PhysicalHit.gif"), (int) (75 / 1.5), false, false) ;
	private static final Gif magHitGif = new Gif("magHit", UtilS.loadImage("\\Battle\\" + "SpellHit.gif"), (int) (90 / 1.5), false, false) ;
	
	
	public LiveBeing(PersonalAttributes PA, BattleAttributes BA, MovingAnimations movingAni, AttributesWindow attWindow)
	{
		this.PA = PA;
		this.BA = BA;
		this.movingAni = movingAni;
		this.attWindow = attWindow ;
//		displayDamage = new TimeCounter(0, 100) ;
		currentAction = null ;
		currentAtkType = null ;
	}

	public abstract Point center() ;
	public abstract AtkResults useSpell(Spell spell, LiveBeing receiver) ;
	public abstract void applyPassiveSpell(Spell spell) ;	
	public abstract void useAutoSpells(boolean activate);
	public abstract void dies() ;
	
	public String getName() {return name ;}
	public int getLevel() {return level ;}
	public int getJob() {return job ;}
	public int getProJob() {return proJob ;}
	public Continents getContinent() {return map.getContinent() ;}
	public GameMap getMap() {return map ;}
	public Directions getDir() {return dir ;}
	public LiveBeingStates getState() {return state ;}
	public Point getPos() {return pos ;}
	public Dimension getSize() {return size ;}
	public Elements[] getElem() {return elem ;}
	public double getRange() {return range ;}
	public int getStep() {return step ;}
	public String getCurrentAction() {return currentAction ;}
	public AtkTypes getCurrentAtkType() { return currentAtkType ;}
	public FrameCounter getMpCounter() {return mpCounter ;}
	public FrameCounter getSatiationCounter() {return satiationCounter ;}
	public FrameCounter getThirstCounter() {return thirstCounter ;}
	public FrameCounter getMoveCounter() {return actionCounter ;}
	public FrameCounter getBattleActionCounter() {return battleActionCounter ;}
//	public TimeCounter getDisplayDamage() {return displayDamage ;}
	public FrameCounter getStepCounter() {return stepCounter ;}
	public List<String> getCombo() {return combo ;}
	public List<Spell> getSpells() {return spells ;}
	public AttributesWindow getAttWindow() {return attWindow ;}
	public MovingAnimations getMovingAni() {return movingAni ;}
	public void setCurrentAction(String newValue) {currentAction = newValue ;}
	public void setMpCounter(FrameCounter mpCounter) { this.mpCounter = mpCounter ;}
	public void setActionCounter(FrameCounter actionCounter) { this.actionCounter = actionCounter ;}	
	public void setBattleActionCounter(FrameCounter battleActionCounter) { this.battleActionCounter = battleActionCounter ;}
	public void setSatiationCounter(FrameCounter satiationCounter) { this.satiationCounter = satiationCounter ;}
	public void setThirstCounter(FrameCounter thirstCounter) { this.thirstCounter = thirstCounter ;}
	public void setStepCounter(FrameCounter stepCounter) { this.stepCounter = stepCounter ;}

	public void setName(String newValue) {name = newValue ;}
	public void setLevel(int newValue) {level = newValue ;}
	public void setJob(int newValue) {job = newValue ;}
	public void setProJob(int newValue) {proJob = newValue ;}
	public void setMap(GameMap newValue) {map = newValue ;}
	public void setDir(Directions newValue) {dir = newValue ;}
	public void setState(LiveBeingStates newValue) {state = newValue ;}
	public void setPos(Point newValue) {pos = newValue ;}
	public void setSize(Dimension newValue) {size = newValue ;}
	public void setRange(int newValue) {range = newValue ;}
	public void setStep(int newValue) {step = newValue ;}
	public void setCombo(List<String> newValue) {combo = newValue ;}
	public void setCurrentAtkType(AtkTypes ba) { currentAtkType = ba ;}
	
	public boolean isMoving() { return state.equals(LiveBeingStates.moving) ;}
	public boolean canAct() { return actionCounter.finished() & (state.equals(LiveBeingStates.idle) | state.equals(LiveBeingStates.fighting)) ;}
	
	public void resetAction() { currentAction = null ;}
	public void resetBattleAction() { currentAtkType = null ;}
	
	public PersonalAttributes getPA() {return PA ;}
	public void setPA(PersonalAttributes pA) { PA = pA ;}
	public BattleAttributes getBA() {return BA ;}
	public void setBA(BattleAttributes bA) { BA = bA ;}
	
	public static Directions randomDir() { return Directions.getDir(UtilG.randomIntFromTo(0, 3)) ;}
	
	public static GameMap calcNewMap(Point pos, Directions dir, GameMap currentMap)
	{
		Point currentPos = new Point(pos) ;
		int newMapID = -1 ;
		int[] mapConnections = currentMap.getConnections() ;
		boolean leftSide = currentPos.x <= Game.getScreen().getSize().width / 2 ;
		boolean topSide = currentPos.y <= Game.getScreen().getSize().height / 2 ;
		switch (dir)
		{
			case up:
				
				newMapID = leftSide ? mapConnections[1] : mapConnections[0] ;				
				break ;
			
			case left:
				
				newMapID = topSide ? mapConnections[2] : mapConnections[3] ;				
				break ;
			
			case down:
				
				newMapID = leftSide ? mapConnections[4] : mapConnections[5] ;				
				break ;
			
			case right:
				
				newMapID = topSide ? mapConnections[7] : mapConnections[6] ;				
				break ;			
		}
		
		if (newMapID == -1) { return null ;}
		
		return Game.getMaps()[newMapID] ;
		
	}
	
	public static Point calcNewMapPos(Point pos, Directions dir, GameMap currentMap, int step)
	{
		int[] screenBorder = Game.getScreen().getBorders() ;
		Dimension screenSize = Game.getScreen().getSize() ;
		Point currentPos = new Point(pos) ;
		int[] mapConnections = currentMap.getConnections() ;
		boolean meetsTwoMapsUp = mapConnections[1] != mapConnections[0] ;
		boolean meetsTwoMapsLeft = mapConnections[3] != mapConnections[2] ;
		boolean meetsTwoMapsDown = mapConnections[4] != mapConnections[5] ;
		boolean meetsTwoMapsRight = mapConnections[6] != mapConnections[7] ;
		boolean leftSide = currentPos.x <= Game.getScreen().getSize().width / 2 ;
		boolean topSide = currentPos.y <= Game.getScreen().getSize().height / 2 ;

		switch (dir)
		{
			case up:
				if (!meetsTwoMapsUp) { return new Point(currentPos.x, screenBorder[3] - step) ;}
				return leftSide ? new Point(currentPos.x + screenSize.width / 2 - 1, screenBorder[3] - step) : new Point(currentPos.x - screenSize.width / 2, screenBorder[3] - step) ;				
			
			case left:
				if (!meetsTwoMapsLeft) { return new Point(screenBorder[2] - step, currentPos.y) ;}
				return topSide ? new Point(screenBorder[2] - step, currentPos.y + screenSize.height / 2 - 1) : new Point(screenBorder[2] - step, currentPos.y - screenSize.height / 2) ;
			
			case down:
				if (!meetsTwoMapsDown) { return new Point(currentPos.x, screenBorder[1] + step) ;}
				return leftSide ? new Point(currentPos.x + screenSize.width / 2 - 1, screenBorder[1] + step) : new Point(currentPos.x - screenSize.width / 2, screenBorder[1] + step) ;			
			
			case right:
				if (!meetsTwoMapsRight) { return new Point(screenBorder[0] + step, currentPos.y) ;}
				return topSide ? new Point(screenBorder[0] + step, currentPos.y + screenSize.height / 2 - 1) : new Point(screenBorder[0] + step, currentPos.y - screenSize.height / 2) ;
			
			default: return null ;
		}
	}

	public void displayState(DrawPrimitives DP)
	{
		Point pos = new Point(540, 100) ;
		Dimension size = new Dimension(60, 20) ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
		String stateText = 0 < combo.size() ? state.toString() : "" ;
		
		DP.drawGradRoundRect(pos, Align.center, size, 1, Game.colorPalette[21], Game.colorPalette[21], true);
		DP.drawText(pos, Align.center, 0, stateText, font, Game.colorPalette[0]) ;
	}

	public void displayPowerBar(Point pos, DrawPrimitives DP)
	{
		int maxPower = 1000 ;
		Color color = Game.colorPalette[6] ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 11) ;
		Dimension barSize = new Dimension(21, powerBarImage.getHeight(null) * totalPower() / maxPower) ;
		
		DP.drawRect(pos, Align.bottomCenter, barSize, 0, color, null) ;
		DP.drawImage(powerBarImage, pos, Align.bottomCenter) ;
		DP.drawText(UtilG.Translate(pos, 0, -powerBarImage.getHeight(null) - 10), Align.bottomCenter, 0, String.valueOf(totalPower()), font, color) ;
	}
	
	public Point calcNewPos()
	{
		switch (dir)
		{
			case up: return new Point(pos.x, pos.y - step) ;
			case down: return new Point(pos.x, pos.y + step) ;
			case left: return new Point(pos.x - step, pos.y) ;
			case right: return new Point(pos.x + step, pos.y) ;	
			default: return new Point(pos) ;
		}
	}

	public Point calcNewPos(Directions dir, Point currentPos, int step)
	{
		Point newPos = new Point(0, 0) ;
		step = 1 ;	// TODO step = 1
		switch (dir)
		{
			case up: newPos = new Point(currentPos.x, currentPos.y - step) ; break ;
			case down: newPos = new Point(currentPos.x, currentPos.y + step) ; break ;
			case left: newPos = new Point(currentPos.x - step, currentPos.y) ; break ;
			case right: newPos = new Point(currentPos.x + step, currentPos.y) ; break ;
		}
		
		return newPos ;
	}
	
	protected void moveToNewMap(Point pos, Directions dir, GameMap currentMap, int step)
	{
		GameMap newMap = calcNewMap(pos, dir, currentMap) ;
		
		if (newMap == null) { return ;}
		
		Point newPos = calcNewMapPos(pos, dir, currentMap, step) ;
		
		setMap(newMap) ;
		setPos(newPos) ;
	}
	
	public void incrementCounters()
	{
		incActionCounters() ;
//		if (this instanceof Player) { ((Player) this).spellCounters() ;}
		spellCounters() ;
		BA.getStatus().decreaseStatus() ;
	}

	public void spellCounters()
	{
		for (Spell spell : spells)
		{
			if (spell.isActive())
			{
				Log.counter(spell.getDurationCounter()) ;
			}
			if (spell.getDurationCounter().finished())
			{
				spell.getDurationCounter().reset() ;
				spell.applyBuffs(false, this) ;
				spell.deactivate() ;
			}
			spell.getCooldownCounter().inc() ;
		}
	}
	
	public void incActionCounters()
	{
		mpCounter.inc() ;
		satiationCounter.inc() ;
		if (this instanceof Player) { thirstCounter.inc() ;}
		actionCounter.inc() ;
	}
	public void activateCounters()
	{
		if (mpCounter.finished()) { PA.getMp().incCurrentValue(1) ; mpCounter.reset() ;}
		if (satiationCounter.finished())
		{
			PA.getSatiation().incCurrentValue(-1) ;
			satiationCounter.reset() ;
			if (PA.getSatiation().getCurrentValue() == 0) { PA.getLife().incCurrentValue(-1) ;}
		}
		
		if (this instanceof Player)
		{
			if (thirstCounter.finished())
			{
				PA.getThirst().incCurrentValue(-1) ;
				thirstCounter.reset() ;
				if (PA.getThirst().getCurrentValue() == 0) { PA.getLife().incCurrentValue(-1) ;}
			}
		}
		
		if (!isAlive()) { dies() ;}
	}
	public void incrementBattleActionCounters() {battleActionCounter.inc() ; } // displayDamage.inc() ;
	public void resetBattleActions() {battleActionCounter.reset() ; }
	
	public int totalPower()
	{
		// TODO consider life, dex and agi, special ba, element mult, mp with spells and items
		// Dano = nHits . hitRate . (PhyAtkRate . PhyDam + MagAtkRate . MagDam + BloodRate . BloodDam + PoisonRate . PoisonDam)
		double FPS = 200 / 3.0 ;
		double maxPhyAtkPossible = BA.getPhyAtk().getTotal() ;
		double maxMagAtkPossible = BA.getMagAtk().getTotal() ;
		double maxDamagePossible = Math.max(maxPhyAtkPossible, maxMagAtkPossible) ;
		double maxDPS = FPS * maxDamagePossible / battleActionCounter.getDuration() ;
		int totalPower = (int) maxDPS ;
		
		return totalPower ;
		
	}
	
	public void resetCombo()
	{
		setCombo(new ArrayList<String>()) ;
	}
	
	public void updateCombo()
	{
		if (currentAction != null)
		{
			if (getCombo().size() <= 9)
			{
				getCombo().add(currentAction) ;
			}
			else
			{
				getCombo().add(0, currentAction) ;
				getCombo().remove(getCombo().size() - 1) ;
			}
		}
	}
	
	public List<Spell> getActiveSpells()
	{
		List<Spell> activeSpells = new ArrayList<Spell>() ;
		for (Spell spell : spells)
		{
			if (!spell.getType().equals(SpellTypes.passive) & !spell.getType().equals(SpellTypes.auto) & 0 < spell.getLevel())
			{
				activeSpells.add(spell) ;
			}
		}
		
		return activeSpells ;
	}
	
	public boolean isAlive() {return 0 < PA.getLife().getTotalValue() ;}
	public boolean hasTheSpell(String action) {return Player.SpellKeys.indexOf(action) < getActiveSpells().size() ;}
	public boolean hasEnoughMP(Spell spell)	{return (spell.getMpCost() <= PA.getMp().getCurrentValue()) ;}
	public boolean hasSuperElement()
	{
		if (elem[1] == null | elem[2] == null | elem[3] == null) { return false ;}
		
		return elem[1].equals(elem[2]) & elem[2].equals(elem[3]) ;
	}
	public boolean hasActed() {return currentAction != null ;}
	public boolean actionIsSpell()
	{
		// TODO 1 this can replace usedSpell
		if (!hasActed()) { return false ;}

		if (!Player.SpellKeys.contains(currentAction)) { return false ;}
		
		int spellID = SpellKeys.indexOf(currentAction) ;
		
		if (spells.size() <= spellID) { return false ;}	
		
		Spell spell = spells.get(spellID) ;

		if (!spell.isReady()) { return false ;}
		if (!hasEnoughMP(spell)) { return false ;}
		
		return 1 <= spell.getLevel() ;
	}
	public boolean usedPhysicalAtk() {return hasActed() ? currentAction.equals(BattleKeys[0]) : false ;}
	public boolean usedSpell() { return (actionIsSpell() & !isSilent()) ;}
	public boolean usedDef() {return hasActed() ? currentAction.equals(BattleKeys[1]) : false ;}
	public boolean actionIsArrowAtk()
	{
		if (!( this instanceof Player)) { return false ;}
		// TODO consider spells
		return hasActed() ? currentAction.equals(BattleKeys[0]) & ((Player) this).arrowIsEquipped() : false ;
	}
	
	public boolean canAtk() {return battleActionCounter.finished() & !BA.isStun() ;}
	public boolean isSilent() {return 0 < BA.getStatus().getSilence() ;}
	public boolean isDefending()
	{
		if (combo == null) { return false ;}
		if (combo.isEmpty()) { return false ;}
		
		if (this instanceof Player)
		{
			return (!battleActionCounter.finished() & combo.get(0).equals(BattleKeys[1])) ;
		}
		return usedDef() ;
	}
	public boolean isInCloseRange(Point target) {return pos.distance(target) <= size.getWidth() ;}
	public boolean isInRange(Point target) {return pos.distance(target) <= range ;}
	public boolean isTouching(GroundTypes groundType) { return UtilS.isTouching(pos, map, groundType) ;}
	public boolean isInside(GroundTypes groundType) { return UtilS.isInside(pos, map, groundType) ;}
	
	public RelativePos relPosToGroundType(GroundTypes groundType)
	{

		if (isTouching(groundType)) { return null ;}
		
		RelativePos relPos = null ;
		for (GroundType gt : map.getgroundTypes())
		{
			relPos = UtilS.calcRelativePos(pos, gt.getPos(), gt.getSize()) ;
		}
		
    	return relPos ;
    	
	}
	
	public Elements[] atkElems()
	{
		if (this instanceof Player) { return new Elements[] {elem[0], elem[1], elem[4]} ;}
		else if (this instanceof Pet) { return new Elements[] {elem[0], elem[1], elem[4]} ;}
		else if (this instanceof Creature) { return new Elements[] {elem[0], Elements.neutral, Elements.neutral} ;}
		
		return null ;
	}
	public Elements[] defElems()
	{
		if (this instanceof Player) { return new Elements[] {elem[2], elem[3]} ;}
		else if (this instanceof Pet) { return new Elements[] {elem[2], elem[3]} ;}
		else if (this instanceof Creature) { return new Elements[] {elem[0], elem[0]} ;}
		
		return null ;
	}
	
	public Point follow(Point userPos, Point target, int step, int minDist)
	{
		
		Point pos = new Point(userPos) ;
		step = 1 ;
		double distY = Math.abs(pos.y - target.y) ;
		double distX = Math.abs(pos.x - target.x) ;

		if (pos.distance(target) <= minDist) { return pos ;}
		
		if (distY < distX)
		{
			if (pos.x < target.x) { return new Point(pos.x + step, pos.y) ;}
			
			return new Point(pos.x - step, pos.y) ;
		}
		
		if (pos.y < target.y) { return new Point(pos.x, pos.y + step) ;}
		
		return new Point(pos.x, pos.y - step) ;

	}
	
	public void checkDeactivateDef()
	{
		if (battleActionCounter.finished() & isDefending())
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
	public void train(AtkResults atkResults)
	{
		
		if (atkResults.getAtkType() == null) { return ;}		
		if (atkResults.getEffect().equals(AtkEffects.none)) { return ;}
		
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
	
	public void displayAttributes(int style, DrawPrimitives DP)
	{
		
		List<Double> attRate = new ArrayList<>() ;
		List<Color> attColor = new ArrayList<>() ;
		
		attRate.add(PA.getLife().getRate()) ;
		attColor.add(Game.colorPalette[7]) ;
		attRate.add(PA.getMp().getRate()) ;
		attColor.add(Game.colorPalette[20]) ;
		if (!(this instanceof Creature))
		{
			attRate.add(PA.getExp().getRate()) ;
			attColor.add(Game.colorPalette[5]) ;
			attRate.add(PA.getSatiation().getRate()) ;
			attColor.add(Game.colorPalette[15]) ;
			attRate.add(PA.getThirst().getRate()) ;
			attColor.add(Game.colorPalette[21]) ;
		}
		
		if (style == 0)
		{
			Point Pos = new Point((int)(pos.x - size.width / 2), (int)(pos.y - size.height - 5 * (1 + attRate.size()))) ;
			Dimension screenSize = Game.getScreen().getSize() ;
			Dimension size = new Dimension((int)(0.05*screenSize.width), (int)(0.01*screenSize.height)) ;
			int Sy = (int)(0.01*screenSize.height) ;
			int barthick = 1 ;
			for (int att = 0; att <= attRate.size() - 1; att += 1)
			{
				Point pos =new Point(Pos.x, Pos.y + (att + 1) * Sy) ;
				Dimension size2 = new Dimension((int)(attRate.get(att) * size.width), size.height) ;
				DP.drawRect(pos, Align.topLeft, size2, barthick, attColor.get(att), Game.colorPalette[0]) ;
			}
		}
		if (style == 1)
		{
//			Point topLeft = Game.getScreen().pos(0.01, 0.02) ;
//			Dimension barSize = new Dimension(120, 5) ;
//			int stroke = 1 ;
//			DP.drawImage(AttImage, topLeft, Align.topLeft) ;
//			Point offset = new Point(70, 7) ;
//			Point barPos = UtilG.Translate(topLeft, offset.x, offset.y) ;
//			for (int att = 0; att <= attRate.size() - 1; att += 1)
//			{
//				Dimension rateSize = new Dimension((int)(attRate.get(att) * barSize.width), barSize.height) ;
//				DP.drawRect(barPos, Align.centerLeft, barSize, stroke, null, Game.colorPalette[0]) ;
//				DP.drawRect(barPos, Align.centerLeft, rateSize, stroke, attColor.get(att), null) ;
//				barPos.y += barSize.height + 6 ;
//			}
			
			Point topLeft = Game.getScreen().pos(0.01, 0.02) ;
			Dimension barSize = new Dimension(5, 35) ;
			int stroke = 1 ;
			DP.drawImage(AttImage, topLeft, Align.topLeft) ;
			Point offset = new Point(37, 44) ;
			Point barPos = UtilG.Translate(topLeft, offset.x, offset.y) ;
			for (int att = 0; att <= attRate.size() - 1; att += 1)
			{
				Dimension rateSize = new Dimension(barSize.width, (int) (attRate.get(att) *  barSize.height)) ;
				DP.drawRect(barPos, Align.bottomCenter, barSize, stroke, null, Game.colorPalette[0]) ;
				DP.drawRect(barPos, Align.bottomCenter, rateSize, stroke, attColor.get(att), null) ;
				barPos.x += barSize.width + 15 ;
			}
		}
		
	}	
	
	public void drawTimeBar(String relPos, Color color, DrawPrimitives DP)
	{
		int stroke = DrawPrimitives.stdStroke ;
		double rate = battleActionCounter.rate() ;
		int mirror = UtilS.MirrorFromRelPos(relPos) ;
		Dimension barSize = new Dimension(2 + size.height / 20, size.height) ;
		Dimension offset = new Dimension (barSize.width / 2 + (LiveBeingStatus.stunImage.getWidth(null) + 5), barSize.height / 2) ;
		Dimension fillSize = new Dimension(barSize.width, (int) (barSize.height * rate)) ;
		Point rectPos = UtilG.Translate(center(), mirror * offset.width, offset.height) ;
		
		DP.drawRect(rectPos, Align.bottomLeft, barSize, stroke, null, Game.colorPalette[0]) ;
		DP.drawRect(rectPos, Align.bottomLeft, fillSize, stroke, color, null) ;
	}

	public void takeDamage(int damage)
	{
		if (damage <= 0) { return ;}
		
		PA.getLife().decTotalValue(damage) ;
	}
	
	public void takeBloodAndPoisonDamage(double totalBloodAtk, double totalPoisonAtk)
	{
		int bloodDamage = 0, poisonDamage = 0 ;
		double bloodMult = 1, poisonMult = 1 ;
		if (this instanceof Player & job == 4)
		{
			poisonMult += -0.1*spells.get(13).getLevel() ;
		}
		if (0 < BA.getStatus().getBlood())
		{
			bloodDamage = (int) Math.max(totalBloodAtk * bloodMult - BA.getBlood().TotalDef(), 0) ;
		}
		if (0 < BA.getStatus().getPoison())
		{
			poisonDamage = (int) Math.max(totalPoisonAtk * poisonMult - BA.getPoison().TotalDef(), 0) ;
		}
		
		if (bloodDamage + poisonDamage <= 0) { return ;}
		
		PA.getLife().decTotalValue(bloodDamage + poisonDamage) ;	
	}
	
	public void displayStatus(DrawPrimitives DP)
	{
		BA.getStatus().display(center(), size, dir, DP) ;
	}
	
	public void displayDefending(DrawPrimitives DP)
	{
		Point offset = new Point(size.width / 2 + defendingImage.getWidth(null) / 2 + 2, 0) ;
		Point imagePos = UtilG.Translate(center(), offset.x, 0) ;
		DP.drawImage(defendingImage, imagePos, Align.center) ;
	}

}
