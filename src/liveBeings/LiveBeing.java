package liveBeings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Map;

import attributes.Attributes;
import attributes.BattleAttributes;
import attributes.PersonalAttributes;
import graphics.DrawingOnPanel;
import main.AtkResults;
import main.Game;
import maps.GameMap;
import utilities.Align;
import utilities.AttackEffects;
import utilities.Directions;
import utilities.TimeCounter;
import utilities.UtilG;
import utilities.UtilS;
import windows.PlayerAttributesWindow;

public class LiveBeing
{
	protected String name ;
	protected int job ;
	protected int proJob ;
	protected int level;
	//private int continent ;
	protected GameMap map ;
	protected Point pos ;					// bottomCenter of the liveBeing
	protected Directions dir ;				// direction of the movement
	protected LiveBeingStates state ;		// current state
	protected Dimension size ;
	protected double range ;
	protected int step ;
	protected String[] elem ;					// 0: Atk, 1: Weapon, 2: Armor, 3: Shield, 4: SuperElem
	protected TimeCounter mpCounter ;			// counts the mp reduction
	protected TimeCounter satiationCounter ;	// counts the satiation reduction
	protected TimeCounter moveCounter ;			// counts the move
	protected TimeCounter battleActionCounter ;	// counts the actions in battle
	protected TimeCounter displayDamage ;		// counts the time the damage taken is on the screen
	protected int stepCounter ;					// counts the steps in the movement	TODO -> TimeCounter ? (n�o � tempo, � step)
	protected String currentAction; 
	protected ArrayList<String> combo ;			// record of the last 10 movements
	protected ArrayList<Spell> spells ;
	
	protected PersonalAttributes PA ;		// Personal attributes
	protected BattleAttributes BA ;			// Battle attributes
	protected MovingAnimations movingAni ;	// Moving animations
	protected PlayerAttributesWindow attWindow ;	// Attributes window
	
	public static final Image[] StatusImages = new Image[] {
			UtilG.loadImage(Game.ImagesPath + "\\Battle\\" + "ShieldIcon.png"),
			UtilG.loadImage(Game.ImagesPath + "\\Status\\" + "Stun.png"),
			UtilG.loadImage(Game.ImagesPath + "\\Status\\" + "Block.png"),
			UtilG.loadImage(Game.ImagesPath + "\\Status\\" + "Blood.png"),
			UtilG.loadImage(Game.ImagesPath + "\\Status\\" + "Poison.png"),
			UtilG.loadImage(Game.ImagesPath + "\\Status\\" + "Silence.png")
			};
	public static final String[] BattleKeys = new String[] {"A", "D"} ;	
	

	public String getName() {return name ;}
	/*public LiveBeing(String name, int job, int proJob, int level, GameMap map, Point pos, Directions dir,
			LiveBeingStates state, Dimension size, double range, int step, String[] elem, TimeCounter mpCounter,
			TimeCounter satiationCounter, TimeCounter moveCounter, TimeCounter battleActionCounter, 
			ArrayList<String> combo, ArrayList<Spell> spells, PersonalAttributes PA, BattleAttributes BA,
			MovingAnimations movingAni, PlayerAttributesWindow attWindow)
	{
		this.name = name;
		this.job = job;
		this.proJob = proJob;
		this.level = level;
		this.map = map;
		this.pos = pos;
		this.dir = dir;
		this.state = state;
		this.size = size;
		this.range = range;
		this.step = step;
		this.elem = elem;
		this.mpCounter = mpCounter;
		this.satiationCounter = satiationCounter;
		this.moveCounter = moveCounter;
		this.battleActionCounter = battleActionCounter ;
		this.stepCounter = 0;
		this.currentAction = "";
		this.combo = combo;
		this.spells = spells;
		this.PA = PA;
		this.BA = BA;
		this.movingAni = movingAni;
		this.attWindow = attWindow;
	}*/
	
	public LiveBeing(PersonalAttributes PA, BattleAttributes BA,
			MovingAnimations movingAni, PlayerAttributesWindow attWindow)
	{
		this.PA = PA;
		this.BA = BA;
		this.movingAni = movingAni;
		this.attWindow = attWindow;
		displayDamage = new TimeCounter(0, 100) ;
		currentAction = null ;
	}
	
	public int getLevel() {return level ;}
	public int getJob() {return job ;}
	public int getProJob() {return proJob ;}
	public int getContinent() {return map.getContinent() ;}
	public GameMap getMap() {return map ;}
	public Directions getDir() {return dir ;}
	public LiveBeingStates getState() {return state ;}
	public Point getPos() {return pos ;}
	public Dimension getSize() {return size ;}
	public String[] getElem() {return elem ;}
	public double getRange() {return range ;}
	public int getStep() {return step ;}
	public String getCurrentAction() {return currentAction ;}
	public TimeCounter getMpCounter() {return mpCounter ;}
	public TimeCounter getSatiationCounter() {return satiationCounter ;}
	public TimeCounter getMoveCounter() {return moveCounter ;}
	public TimeCounter getBattleActionCounter() {return battleActionCounter ;}
	public TimeCounter getDisplayDamage() {return displayDamage ;}
	public int getStepCounter() {return stepCounter ;}
	public ArrayList<String> getCombo() {return combo ;}
	public void setCurrentAction(String newValue) {currentAction = newValue ;}

	public void setName(String newValue) {name = newValue ;}
	public void setLevel(int newValue) {level = newValue ;}
	public void setJob(int newValue) {job = newValue ;}
	public void setProJob(int newValue) {proJob = newValue ;}
	public void setMap(GameMap newValue) {map = newValue ;}
	public void setDir(Directions newValue) {dir = newValue ;}
	public void setState(LiveBeingStates newValue) {state = newValue ;}
	public void setPos(Point newValue) {pos = newValue ;}
	public void setSize(Dimension newValue) {size = newValue ;}
	public void setRange(double newValue) {range = newValue ;}
	public void setStep(int newValue) {step = newValue ;}
	public void setCombo(ArrayList<String> newValue) {combo = newValue ;}
	
	public PersonalAttributes getPA() {return PA ;}
	public BattleAttributes getBA() {return BA ;}
	public PlayerAttributesWindow getAttWindow() {return attWindow ;}
	public MovingAnimations getMovingAni() {return movingAni ;}

	public void displayState(DrawingOnPanel DP)
	{
		Point pos = new Point(540, 100) ;
		Dimension size = new Dimension(60, 20) ;
		Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
		DP.DrawRoundRect(pos, Align.center, size, 1, Game.ColorPalette[8], Game.ColorPalette[8], true);
		DP.DrawText(pos, Align.center, 0, state.toString(), font, Game.ColorPalette[9]) ;
	}
	
	public Point CalcNewPos()
	{
		Point newPos = new Point(0, 0) ;
		step = 1 ;
		switch (dir)
		{
			case up: newPos = new Point(pos.x, pos.y - step) ; break ;
			case down: newPos = new Point(pos.x, pos.y + step) ; break ;
			case left: newPos = new Point(pos.x - step, pos.y) ; break ;
			case right: newPos = new Point(pos.x + step, pos.y) ; break ;		
		}
		
		return newPos ;
	}
	
	public void IncActionCounters()
	{
		/*for (int a = 0 ; a <= PA.Actions.length - 1 ; a += 1)
		{
			if (PA.Actions[a][0] < PA.Actions[a][1])
			{
				PA.Actions[a][0] += 1 ;
			}	
		}*/
		mpCounter.inc() ;
		satiationCounter.inc() ;
		moveCounter.inc() ;
	}
	public void activateCounters()
	{
		if (mpCounter.finished())
		{
			PA.getMp().incCurrentValue(1);
		}
	}
	public void IncBattleActionCounters() {battleActionCounter.inc() ; displayDamage.inc() ;}
	public void ResetBattleActions() {battleActionCounter.reset() ;}
	
	
	public void resetCombo()
	{
		setCombo(new ArrayList<String>()) ;
	}
	
	public void UpdateCombo()
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
	
	public boolean isAlive() {return 0 < PA.getLife().getCurrentValue() ;}
	public boolean hasActed() {return currentAction != null ;}
	public boolean actionIsSpell()	{return hasActed() ? UtilG.ArrayContains(Player.SpellKeys, currentAction) : false ;}
	public boolean actionIsAtk() {return hasActed() ? currentAction.equals(Player.BattleKeys[0]) : false ;}
	public boolean actionIsDef() {return hasActed() ? currentAction.equals(Player.BattleKeys[1]) : false ;}
	
	public boolean canAtk() {return battleActionCounter.finished() & !BA.isStun() ;}
	public boolean isSilent() {return BA.getSpecialStatus()[4] <= 0 ;}
	public boolean isDefending() {return hasActed() ? (getCurrentAction().equals(BattleKeys[1])) : false ;}
	
	public void ActivateBattleActionCounters()
	{
		// TODO battle action counters
	}
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
	
	public void applyBuff(boolean activate, Buff buff, int level)
	{
		int mult = 1 ;
		if (!activate)
		{
			mult = -1 ;
		}
		Map<Attributes, Double> percIncrease = buff.getPercentIncrease() ;
		Map<Attributes, Double> valueIncrease = buff.getValueIncrease() ;
		double increment = PA.getLife().getMaxValue() * percIncrease.get(Attributes.life) + valueIncrease.get(Attributes.life) ;
		PA.getLife().incCurrentValue((int) Math.round(increment * level * mult));
		increment = BA.getPhyAtk().getBaseValue() * percIncrease.get(Attributes.phyAtk) + valueIncrease.get(Attributes.phyAtk) ;
		BA.getPhyAtk().incBonus(increment * level * mult);
		increment = BA.getMagAtk().getBaseValue() * percIncrease.get(Attributes.magAtk) + valueIncrease.get(Attributes.magAtk) ;
		BA.getMagAtk().incBonus(increment * level * mult);
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
	public void train(AtkResults playerAtkResult)
	{
		AttackEffects effect = (AttackEffects) playerAtkResult.getEffect() ;
		String atkType = (String) playerAtkResult.getAtkType() ;
		if (atkType.equals("Physical"))	// Physical atk
		{
			BA.getPhyAtk().incTrain(0.025 / (BA.getPhyAtk().getTrain() + 1)) ;					
		}
		if (effect != null)
		{
			if (effect.equals(AttackEffects.crit))
			{
				if (job == 2)
				{
					BA.getCrit()[1] += 0.025 * 0.000212 / (BA.getCrit()[1] + 1) ;	// 100% after 10,000 hits starting from 0.12
				}
			}
			if (effect.equals(AttackEffects.hit))
			{
				BA.getDex().incTrain(0.025 / (BA.getDex().getTrain() + 1)) ;
			}
		}
		if (atkType.equals("Spell"))
		{
			BA.getMagAtk().incTrain(0.025 / (BA.getMagAtk().getTrain() + 1)) ;
		}
		if (atkType.equals("Defense"))
		{
			BA.getPhyDef().incTrain(0.025 / (BA.getPhyDef().getTrain() + 1)) ;
			BA.getMagDef().incTrain(0.025 / (BA.getMagDef().getTrain() + 1)) ;
		}
	}
	
	public void DrawAttributes(int style, DrawingOnPanel DP)
	{
		Color[] colorPalette = Game.ColorPalette ;
		Dimension screenSize = Game.getScreen().getSize() ;
		
		ArrayList<Double> attRate = new ArrayList<>() ;
		ArrayList<Color> attColor = new ArrayList<>() ;
		
		attRate.add(PA.getLife().getRate()) ;
		attColor.add(colorPalette[6]) ;
		attRate.add(PA.getMp().getRate()) ;
		attColor.add(colorPalette[5]) ;
		attRate.add(PA.getExp().getRate()) ;
		attColor.add(colorPalette[1]) ;
		attRate.add(PA.getSatiation().getRate()) ;
		attColor.add(colorPalette[2]) ;
		attRate.add(PA.getThirst().getRate()) ;
		attColor.add(colorPalette[0]) ;
		
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
		Point rectPos = new Point(pos.x + mirror * offset.width, pos.y + offset.height) ;
		
		DP.DrawRect(rectPos, Align.bottomLeft, barSize, stroke, null, Game.ColorPalette[9]) ;
		DP.DrawRect(rectPos, Align.bottomLeft, fillSize, stroke, color, null) ;
	}
	public void ShowEffectsAndStatusAnimation(Point Pos, int mirror, Dimension offset, Image[] IconImages, int[] effect, boolean isDefending, DrawingOnPanel DP)
	{
		// effect 0: Stun, 1: Block, 2: Blood, 3: Poison, 4: Silence
		int Sy = (int)(1.1 * IconImages[0].getHeight(null)) ;
		if (isDefending)	// Defending
		{
			int ImageW = IconImages[0].getWidth(null) ;
			Point ImagePos = new Point(Pos.x + mirror * (ImageW + offset.width), Pos.y - offset.height) ;
			DP.DrawImage(IconImages[0], ImagePos, Align.center) ;
		}
		if (0 < effect[0])	// Stun
		{
			Point ImagePos = new Point(Pos.x, Pos.y + mirror * offset.height) ;
			DP.DrawImage(IconImages[1], ImagePos, Align.center) ;
		}
		for (int e = 1 ; e <= 4 - 1 ; e += 1)	// Block, blood, poison and silence
		{
			if (0 < effect[e])
			{
				int ImageW = IconImages[e + 1].getWidth(null) ;
				Point ImagePos = new Point(Pos.x + mirror * (ImageW + offset.width), Pos.y - offset.height + Sy) ;
				DP.DrawImage(IconImages[e + 1], ImagePos, Align.center) ;
				Sy += IconImages[e + 1].getHeight(null) + 2 ;
			}
		}
	}
}
