package liveBeings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import attributes.BattleAttributes;
import attributes.PersonalAttributes;
import components.SpellTypes;
import graphics.DrawingOnPanel;
import main.AtkResults;
import main.AtkTypes;
import main.Game;
import maps.Continents;
import maps.GameMap;
import maps.GroundType;
import maps.GroundTypes;
import utilities.Align;
import utilities.AttackEffects;
import utilities.Directions;
import utilities.Elements;
import utilities.RelativePos;
import utilities.TimeCounter;
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
	protected TimeCounter mpCounter ;
	protected TimeCounter satiationCounter ;
	protected TimeCounter thirstCounter ;
	protected TimeCounter actionCounter ;
	protected TimeCounter battleActionCounter ;
	protected TimeCounter displayDamage ;
	protected TimeCounter stepCounter ;			// counts the steps in the movement
	protected String currentAction ;
	protected AtkTypes currentAtkType ;
	protected List<String> combo ;				// record of the last 10 movements
	protected List<Spell> spells ;
	
	protected PersonalAttributes PA ;
	protected BattleAttributes BA ;
	protected MovingAnimations movingAni ;
	protected AttributesWindow attWindow ;
	
	public static final Image[] StatusImages = new Image[] {
			UtilG.loadImage(Game.ImagesPath + "\\Status\\" + "Stun.png"),
			UtilG.loadImage(Game.ImagesPath + "\\Status\\" + "Block.png"),
			UtilG.loadImage(Game.ImagesPath + "\\Status\\" + "Blood.png"),
			UtilG.loadImage(Game.ImagesPath + "\\Status\\" + "Poison.png"),
			UtilG.loadImage(Game.ImagesPath + "\\Status\\" + "Silence.png")
			};
	public static final Image defendingImage = UtilG.loadImage(Game.ImagesPath + "\\Battle\\" + "ShieldIcon.png") ;
	public static final Image powerBarImage = UtilG.loadImage(Game.ImagesPath + "PowerBar.png") ;
	public static final String[] BattleKeys = new String[] {"Y", "U"} ;	
	public static final List<String> SpellKeys = new ArrayList<>(Arrays.asList(new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "F10", "F11", "F12"})) ;
	
	
	
	public LiveBeing(PersonalAttributes PA, BattleAttributes BA, MovingAnimations movingAni, AttributesWindow attWindow)
	{
		this.PA = PA;
		this.BA = BA;
		this.movingAni = movingAni;
		this.attWindow = attWindow ;
		displayDamage = new TimeCounter(0, 100) ;
		currentAction = null ;
		currentAtkType = null ;
	}

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
	public AtkTypes getCurrentBattleAction() { return currentAtkType ;}
	public TimeCounter getMpCounter() {return mpCounter ;}
	public TimeCounter getSatiationCounter() {return satiationCounter ;}
	public TimeCounter getThirstCounter() {return thirstCounter ;}
	public TimeCounter getMoveCounter() {return actionCounter ;}
	public TimeCounter getBattleActionCounter() {return battleActionCounter ;}
	public TimeCounter getDisplayDamage() {return displayDamage ;}
	public TimeCounter getStepCounter() {return stepCounter ;}
	public List<String> getCombo() {return combo ;}
	public List<Spell> getSpells() {return spells ;}
	public void setCurrentAction(String newValue) {currentAction = newValue ;}
	public void setMpCounter(TimeCounter mpCounter) { this.mpCounter = mpCounter ;}
	public void setActionCounter(TimeCounter actionCounter) { this.actionCounter = actionCounter ;}	
	public void setBattleActionCounter(TimeCounter battleActionCounter) { this.battleActionCounter = battleActionCounter ;}
	public void setSatiationCounter(TimeCounter satiationCounter) { this.satiationCounter = satiationCounter ;}
	public void setThirstCounter(TimeCounter thirstCounter) { this.thirstCounter = thirstCounter ;}
	public void setStepCounter(TimeCounter stepCounter) { this.stepCounter = stepCounter ;}

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
	
	public boolean isMoving() { return (state.equals(LiveBeingStates.moving)) ;}
	public boolean canAct() { return actionCounter.finished() & (state.equals(LiveBeingStates.idle) | state.equals(LiveBeingStates.fighting)) ;}
	
	public void resetAction() { currentAction = null ;}
	public void resetBattleAction() { currentAtkType = null ;}
	
	public PersonalAttributes getPA() {return PA ;}
	public void setPA(PersonalAttributes pA) { PA = pA ;}
	public BattleAttributes getBA() {return BA ;}
	public void setBA(BattleAttributes bA) { BA = bA ;}

	public AttributesWindow getAttWindow() {return attWindow ;}
	public MovingAnimations getMovingAni() {return movingAni ;}

	public void displayState(DrawingOnPanel DP)
	{
		Point pos = new Point(540, 100) ;
		Dimension size = new Dimension(60, 20) ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
		String stateText = 0 < combo.size() ? state.toString() : "" ;
		
		DP.DrawRoundRect(pos, Align.center, size, 1, Game.colorPalette[8], Game.colorPalette[8], true);
		DP.DrawText(pos, Align.center, 0, stateText, font, Game.colorPalette[9]) ;
	}

	public void displayPowerBar(Point pos, DrawingOnPanel DP)
	{
		int maxPower = 1000 ;
		Color color = Game.colorPalette[6] ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 11) ;
		Dimension barSize = new Dimension(21, powerBarImage.getHeight(null) * totalPower() / maxPower) ;
		
		DP.DrawRect(pos, Align.bottomCenter, barSize, 0, color, null) ;
		DP.DrawImage(powerBarImage, pos, Align.bottomCenter) ;
		DP.DrawText(UtilG.Translate(pos, 0, -powerBarImage.getHeight(null) - 10), Align.bottomCenter, 0, String.valueOf(totalPower()), font, color) ;
	}
	
	public Point CalcNewPos()
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
				spell.getDurationCounter().inc() ;
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
		
		if (PA.getLife().getCurrentValue() <= 0) { dies() ;}
	}
	public void incrementBattleActionCounters() {battleActionCounter.inc() ; displayDamage.inc() ;}
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
			if (!spell.getType().equals(SpellTypes.passive) & 0 < spell.getLevel())
			{
				activeSpells.add(spell) ;
			}
		}
		
		return activeSpells ;
	}
	
	public boolean isAlive() {return 0 < PA.getLife().getCurrentValue() ;}
	public boolean hasTheSpell(String action) {return Player.SpellKeys.indexOf(action) < getActiveSpells().size() ;}
	public boolean hasEnoughMP(Spell spell)	{return (spell.getMpCost() <= PA.getMp().getCurrentValue()) ;}
	public boolean hasSuperElement()
	{
		if (elem[1] == null | elem[2] == null | elem[3] == null) { return false ;}
		
		return elem[1].equals(elem[2]) & elem[2].equals(elem[3]) ;
	}
	public boolean hasActed() {return currentAction != null ;}
	public boolean actionIsSpell()  {return hasActed() ? Player.SpellKeys.contains(currentAction) : false ;}
	public boolean actionIsPhysicalAtk() {return hasActed() ? currentAction.equals(BattleKeys[0]) : false ;}
	public boolean actionIsDef() {return hasActed() ? currentAction.equals(BattleKeys[1]) : false ;}
	public boolean actionIsArrowAtk()
	{
		if (!( this instanceof Player)) { return false ;}
		
		return hasActed() ? currentAction.equals(BattleKeys[0]) & ((Player) this).arrowIsEquipped() : false ;
	}
	
	public boolean canAtk() {return battleActionCounter.finished() & !BA.isStun() ;}
	public boolean isSilent() {return 0 < BA.getStatus().getSilence() ;}
	public boolean isDefending()
	{
		if (combo == null) { return false ;}
		if (combo.size() == 0) { return false ;}
		
		if (this instanceof Player)
		{
			return (!battleActionCounter.finished() & combo.get(0).equals(BattleKeys[1])) ;
		}
		return actionIsDef() ;
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
	
//	public void ActivateBattleActionCounters()
//	{
//		
//	}
//	public void displayDamageTaken(AtkResults atkResults, int animationStyle, DrawingOnPanel DP)
//	{
//		System.out.println(displayDamage);
//		if (!displayDamage.finished())
//		{
////			Point TargetPos = (Point) AniVars1[1] ;
////			Dimension TargetSize = (Dimension) AniVars1[2] ;
////			int damage = (int)((Object[]) AniVars1[3])[0] ;
////			AttackEffects effect = (AttackEffects)((Object[]) AniVars1[3])[1] ;
////			int AnimationStyle = (int) AniVars1[4] ;
////			Point Pos = new Point(TargetPos.x, TargetPos.y - TargetSize.height - 25) ;
//			Point pos = new Point(this.pos.x, this.pos.y - size.height - 20) ;
//			DP.DrawDamageAnimation(pos, atkResults, displayDamage, animationStyle, Game.ColorPalette[6]) ;
//		}
//	}
		
	public Point Follow(Point userPos, Point target, int step, int minDist)
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
	

	public abstract void applyPassiveSpell(Spell spell) ;	
	public abstract AtkResults useSpell(Spell spell, LiveBeing receiver) ;
	public abstract void dies() ;
	
	public void checkDeactivateDef()
	{
		if (battleActionCounter.finished() & isDefending())
		{
			DeactivateDef() ;
		}
	}
	
	public void ActivateDef()
	{
		BA.getPhyDef().incBonus(BA.getPhyDef().getBaseValue()) ;
		BA.getMagDef().incBonus(BA.getMagDef().getBaseValue()) ;
	}
	public void DeactivateDef()
	{
		BA.getPhyDef().incBonus(-BA.getPhyDef().getBaseValue()) ;
		BA.getMagDef().incBonus(-BA.getMagDef().getBaseValue()) ;
	}
	public void train(AtkResults atkResult)
	{
		AttackEffects effect = (AttackEffects) atkResult.getEffect() ;
		AtkTypes atkType = atkResult.getAtkType() ;
		if (atkType.equals(AtkTypes.physical))
		{
			BA.getPhyAtk().incTrain(0.025 / (BA.getPhyAtk().getTrain() + 1)) ;					
		}
		if (effect != AttackEffects.none)
		{
			if (effect.equals(AttackEffects.crit))
			{
				if (job == 2)
				{
					BA.getCritAtk().incBonus(0.025 * 0.000212 / (BA.getCritAtk().getBonus() + 1)) ;	// 100% after 10,000 hits starting from 0.12
				}
			}
			if (effect.equals(AttackEffects.hit))
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
	
	public void DrawAttributes(int style, DrawingOnPanel DP)
	{
		Color[] colorPalette = Game.colorPalette ;
		Dimension screenSize = Game.getScreen().getSize() ;
		
		ArrayList<Double> attRate = new ArrayList<>() ;
		ArrayList<Color> attColor = new ArrayList<>() ;
		
		attRate.add(PA.getLife().getRate()) ;
		attColor.add(colorPalette[6]) ;
		attRate.add(PA.getMp().getRate()) ;
		attColor.add(colorPalette[5]) ;
		if (!(this instanceof Creature))
		{
			attRate.add(PA.getExp().getRate()) ;
			attColor.add(colorPalette[1]) ;
			attRate.add(PA.getSatiation().getRate()) ;
			attColor.add(colorPalette[2]) ;
			attRate.add(PA.getThirst().getRate()) ;
			attColor.add(colorPalette[0]) ;
		}
		
		if (style == 0)
		{
			Point Pos = new Point((int)(pos.x - size.width / 2), (int)(pos.y - size.height - 5 * (1 + attRate.size()))) ;
			Dimension size = new Dimension((int)(0.05*screenSize.width), (int)(0.01*screenSize.height)) ;
			int Sy = (int)(0.01*screenSize.height) ;
			int barthick = 1 ;
			for (int att = 0; att <= attRate.size() - 1; att += 1)
			{
				DP.DrawRect(new Point(Pos.x, Pos.y + (att + 1) * Sy), Align.topLeft, new Dimension((int)(attRate.get(att) * size.width), size.height), barthick, attColor.get(att), colorPalette[9]) ;
			}
		}
		if (style == 1)
		{
			Point Pos = new Point((int)(0.01*screenSize.width), (int)(0.03*screenSize.height)) ;
			Dimension size = new Dimension((int)(0.13*screenSize.width), (int)(0.013*screenSize.height)) ;
			int Sy = size.height ;
			int barthick = 1 ;
			DP.DrawRoundRect(Pos, Align.topLeft, new Dimension((int)(1.4 * size.width), (attRate.size() + 1) * Sy), barthick, colorPalette[8], colorPalette[4], true) ;
			for (int att = 0; att <= attRate.size() - 1; att += 1)
			{
				DP.DrawRect(new Point((int) (Pos.x + 0.3 * size.width), Pos.y + (att + 1) * Sy), Align.centerLeft, size, barthick, null, colorPalette[9]) ;
				DP.DrawRect(new Point((int) (Pos.x + 0.3 * size.width), Pos.y + (att + 1) * Sy), Align.centerLeft, new Dimension((int)(attRate.get(att) * size.width), size.height), barthick, attColor.get(att), colorPalette[9]) ;
			}
		}
	}	
	public void DrawTimeBar(String relPos, Color color, DrawingOnPanel DP)
	{
		int stroke = DrawingOnPanel.stdStroke ;
		double rate = battleActionCounter.rate() ;
		int mirror = UtilS.MirrorFromRelPos(relPos) ;
		Dimension barSize = new Dimension(2 + size.height / 20, size.height) ;
		Dimension offset = new Dimension (barSize.width / 2 + (StatusImages[0].getWidth(null) + 5), -barSize.height / 2) ;
		Dimension fillSize = new Dimension(barSize.width, (int) (barSize.height * rate)) ;
		Point rectPos = new Point(pos.x + mirror * offset.width, pos.y) ;
		
		DP.DrawRect(rectPos, Align.bottomLeft, barSize, stroke, null, Game.colorPalette[9]) ;
		DP.DrawRect(rectPos, Align.bottomLeft, fillSize, stroke, color, null) ;
	}

	public void TakeBloodAndPoisonDamage(double totalBloodAtk, double totalPoisonAtk)
	{
		int BloodDamage = 0, PoisonDamage = 0 ;
		double BloodMult = 1, PoisonMult = 1 ;
		if (job == 4) // TODO isso vale tbm para criaturas e pet
		{
			PoisonMult += -0.1*spells.get(13).getLevel() ;
		}
		if (0 < BA.getStatus().getBlood())
		{
			BloodDamage = (int) Math.max(totalBloodAtk * BloodMult - BA.getBlood().TotalDef(), 0) ;
		}
		if (0 < BA.getStatus().getPoison())
		{
			PoisonDamage = (int) Math.max(totalPoisonAtk * PoisonMult - BA.getPoison().TotalDef(), 0) ;
		}
		PA.getLife().incCurrentValue(-BloodDamage - PoisonDamage) ;
	}
	public void displayDefending(DrawingOnPanel DP)
	{
		int ImageW = defendingImage.getWidth(null) ;
		Point ImagePos = UtilG.Translate(pos, ImageW, 0) ;
		DP.DrawImage(defendingImage, ImagePos, Align.center) ;
	}
}
