package liveBeings ;

import java.awt.Color ;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import attributes.PersonalAttributes;
import attributes.BasicAttribute;
import attributes.BasicBattleAttribute;
import attributes.BattleSpecialAttribute;
import attributes.BattleSpecialAttributeWithDamage;
import graphics.DrawingOnPanel;
import items.Item;
import main.AtkResults;
import main.AtkTypes;
import main.Battle;
import main.Game;
import maps.GameMap;
import screen.Screen;
import utilities.Align;
import utilities.AttackEffects;
import utilities.Directions;
import utilities.Elements;
import utilities.Scale;
import utilities.TimeCounter;
import utilities.UtilG;
import windows.CreatureAttributesWindow;
import windows.PlayerAttributesWindow;

public class Creature extends LiveBeing
{
	private CreatureType type ;
	private Set<Item> Bag ;
	private int Gold ;
	private Color color ;
	private double[] genes ;	// TODO considerar mover pra CreatureType
	private int[] StatusCounter ;	// [Life, Mp, Phy atk, Phy def, Mag atk, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence]
	private boolean follow ;
//	public int countmove ;
	
	private static Color[] skinColor = new Color[] {Game.colorPalette[0], Game.colorPalette[1]} ;
	private static Color[] shadeColor = new Color[] {Game.colorPalette[2], Game.colorPalette[3]} ;
	
	public static final String[] SpellKeys = new String[] {"0", "1", "2", "3"} ;
	
 	public Creature(CreatureType CT)
	{
 		super(CT.getPA(), CT.getBA(), CT.getMovingAnimations(), new CreatureAttributesWindow()) ;
		
		this.type = CT ;		
		this.name = CT.name;
		this.level = CT.level;
		this.size = CT.size;
		this.range = CT.range;
		this.step = CT.step;
		this.elem = CT.elem;
		mpCounter = new TimeCounter(0, CT.mpDuration);
		satiationCounter = new TimeCounter(0, CT.satiationDuration);
		actionCounter = new TimeCounter(0, CT.moveDuration) ;
		battleActionCounter = new TimeCounter(0, CT.battleActionDuration) ;
		this.stepCounter = new TimeCounter(0, CT.moveDuration) ;
		combo = new ArrayList<>() ;
		
		dir = Directions.up ;
		state = LiveBeingStates.idle ;
		currentAction = "" ;
		spells = CT.getSpell() ;
		this.Bag = CT.getBag() ;
		this.Gold = CT.getGold() ;
		this.color = CT.getColor() ;
		this.StatusCounter = CT.getStatusCounter() ;
		
		Point minCoord = new Point(0, (int) (0.2*Game.getScreen().getSize().height)) ;
		Dimension range = new Dimension(Game.getScreen().getSize().width, (int) ((1 - (double)(Game.getSky().height)/Game.getScreen().getSize().height) * Game.getScreen().getSize().height)) ;
		Point initialPos = UtilG.RandomPos(minCoord, range, new Dimension(1, 1)) ;
		setPos(initialPos) ;
		

		if (getName().equals("Dragão") | getName().equals("Dragon"))
		{
			setPos(Game.getScreen().getCenter()) ;
		}
		genes = new double[] {0.14606542271035106, 0.11939312567256516} ;
		
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
	public double[] getCrit() {return BA.getCrit() ;}
	public BattleSpecialAttribute getStun() {return BA.getStun() ;}
	public BattleSpecialAttribute getBlock() {return BA.getBlock() ;}
	public BattleSpecialAttributeWithDamage getBlood() {return BA.getBlood() ;}
	public BattleSpecialAttributeWithDamage getPoison() {return BA.getPoison() ;}
	public BattleSpecialAttribute getSilence() {return BA.getSilence() ;}
	public BasicAttribute getExp() {return PA.getExp() ;}
	public Set<Item> getBag() {return Bag ;}
	public int getGold() {return Gold ;}
	public Color getColor() {return color ;}
	public double[] getGenes() {return genes ;}
	//public int[][] getActions() {return PA.Actions ;}
	public int[] getStatusCounter() {return StatusCounter ;}
	public boolean getFollow() {return follow ;}
	public void setPos(Point newValue) {pos = newValue ;}
	public void setGenes(double[] newGenes) {genes = newGenes ;}
	public void setFollow(boolean F) {follow = F ;}
	public static Color[] getskinColor() {return skinColor ;}
	public static Color[] getshadeColor() {return shadeColor ;}

	public boolean hasEnoughMP(int spellID)
	{
		int MPcost = 10 * spellID ;
		return (MPcost <= PA.getMp().getCurrentValue()) ;
	}
	
	public void displayName(Point pos, Align alignment, Color color, DrawingOnPanel DP)
	{
		Font font = new Font(Game.MainFontName, Font.BOLD, 13) ;
		DP.DrawText(pos, alignment, DrawingOnPanel.stdAngle, name, font, color) ;
	}
	public void display(Point pos, Scale scale, DrawingOnPanel DP)
	{
		DP.DrawImage(type.movingAni.idleGif, pos, scale, Align.center) ;
		/*if (PA.getThought().equals("Exist"))
		{
			DP.DrawImage(type.movingAni.idleGif, pos, scale, "Center") ;
		}
		else if (PA.getThought().equals("Move"))
		{
			if (dir.equals("Acima"))
			{
				DP.DrawImage(type.movingAni.movingUpGif, pos, scale, "Center") ;
			}
			if (dir.equals("Abaixo"))
			{
				DP.DrawImage(type.movingAni.movingDownGif, pos, scale, "Center") ;
			}
			if (dir.equals("Esquerda"))
			{
				DP.DrawImage(type.movingAni.movingLeftGif, pos, scale, "Center") ;
			}
			if (dir.equals("Direita"))
			{
				DP.DrawImage(type.movingAni.movingRightGif, pos, scale, "Center") ;
			}
		}*/
//		DP.DrawText(getPos(), Align.center, 0, String.valueOf(type.getID()), new Font(Game.MainFontName, Font.BOLD, 24), Color.black) ;
	}
	

	public void setRandomPos()
	{
		Screen screen = Game.getScreen() ;
		Point MinCoord = new Point(0, (int) (0.2*screen.getSize().height)) ;
		Dimension Range = new Dimension(screen.getSize().width, (int) (screen.getBorders()[3] - screen.getBorders()[1])) ;
		Dimension step = new Dimension(1, 1) ;
		setPos(UtilG.RandomPos(MinCoord, Range, step)) ;
	}
	public Point CenterPos()
	{
		return new Point((int) (pos.x + 0.5 * size.width), (int) (pos.y - 0.5 * size.height)) ;
	}
	
	public Directions newMoveDirection(Directions originalDir)
	{
		Directions newDir = PersonalAttributes.randomDir() ;
		while (Directions.areOpposite(originalDir, newDir))
		{
			newDir = PersonalAttributes.randomDir() ;
		}
		return newDir ;
	}
	
	public void updatePos(Directions dir, Point CurrentPos, int step, GameMap map)
	{
		Screen screen = Game.getScreen() ;
		Point NewPos = PA.CalcNewPos(dir, CurrentPos, step) ;
		// First check if the new pos is inside the screen, then check if it is walkable
		boolean NewPosIsInsideScreen = (screen.getBorders()[0] < NewPos.x & screen.getBorders()[1] < NewPos.y & NewPos.x < screen.getBorders()[2] & NewPos.y < screen.getBorders()[3]) ;
		// if (rectTopLeftPos.x <= objPos.x & objPos.y <= rectTopLeftPos.y + rectSize.y & objPos.x <= rectTopLeftPos.x + rectSize.x & rectTopLeftPos.y <= objPos.y)
		//boolean NewPosIsInsideScreen = Utg.isInside(NewPos, screen.getBorders(), screen.getBorders()) ;
		if (NewPosIsInsideScreen)
		{
			boolean NewPosIsWalkable = map.groundIsWalkable(NewPos, null) ;
			if (NewPosIsWalkable)
			{
				setPos(NewPos) ;
			}
			else
			{
				//setPos(CurrentPos) ;
			}
		}
		else
		{
			//setPos(CurrentPos) ;
		}
	}
	public String chooseTarget(boolean playerIsAlive, boolean petIsAlive)
	{
		if (!playerIsAlive & !petIsAlive) { return null ;}		
		if (!playerIsAlive) { return "pet"  ;}
		if (!petIsAlive) { return "player" ;}

		if (0.5 <= Math.random()) { return "player" ;}
		else { return "pet" ;}
	}
	

	public void Think()
	{
			// TODO thinking is acting
		if (!state.equals(LiveBeingStates.idle)) { return ;}
		
		boolean startMoving = UtilG.chance(0.3) ;
		
		if (!startMoving) { return ;}

		boolean switchDirection = UtilG.chance(0.5) ;
		if (switchDirection)
		{
			setDir(newMoveDirection(dir)) ;
		}
		setState(LiveBeingStates.moving) ;
		return ;
		
	}
	
	public void Move(Point PlayerPos, GameMap map)
	{

		// TODO quando não está em batalha mas está seguindo, ela só se aproxima, mas não entra na batalha
		if (follow)
		{
			setPos(Follow(pos, PlayerPos, step, range)) ;
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
	
	public int chooseFightMove()
	{
//		if (10 <= PA.getMp().getCurrentValue())
//		{
//			return (int)(3*Math.random() - 0.01) ;
//		}
//		else
//		{
//			return (int)(2*Math.random() - 0.01) ;
//		}
		
//		for (int i = 0 ; i <= genes.length - 1; i += 1)
//		{
//			chances[i] = genes[i] * geneMult[i] ;
//		}
		
		return UtilG.randomFromChanceList(genes) ;
	}
	
	public void act()
	{
		Think() ;
		actionCounter.reset() ;
	}
	public void fight()
	{
		int move = chooseFightMove() ;

		switch (move)
		{
			case 0:	setCurrentAction(BattleKeys[0]) ; break ;	// Physical attack
			case 1:	setCurrentAction(BattleKeys[1]) ; break ;	// Defense
			case 2:	setCurrentAction(String.valueOf(UtilG.randomIntFromTo(0, spells.size() - 1))) ; break ;	// spell
		}
	}
	public AtkResults useSpell(Spell spell, LiveBeing receiver)
	{
		int spellLevel = spell.getLevel() ;
		int damage = -1 ;
		AttackEffects effect = null ;

		double MagAtk = 3*BA.TotalMagAtk() ;
		double MagDef = receiver.getBA().TotalMagDef() ;
		double AtkDex = 3*BA.TotalDex() ;
		double DefAgi = receiver.getBA().TotalAgi() ;
		double AtkCrit = 3*BA.TotalCritAtkChance() ;
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
				DeactivateDef() ;
			}
		}
		setRandomPos() ;
		follow = false ;
	}

	
	@Override
	public String toString()
	{
		return "Creature [type=" + type + ", Bag=" + Bag + ", Gold=" + Gold + ", color=" + color + ", StatusCounter="
				+ Arrays.toString(StatusCounter) + ", follow=" + follow + "]";
	}

	
	
	
	
//	public void ApplyBuffsAndNerfs(String action, String type, int att, int BuffNerfLevel, Spell spells, boolean SpellIsActive)
//	{
//		int ActionMult = 1 ;
//		double[][] Buff = new double[14][5] ;	// [Life, Mp, PhyAtk, MagAtk, Phy def, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence][effect]
//		double[] OriginalValue = new double[14] ;	// [Life, Mp, PhyAtk, MagAtk, Phy def, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence]
//		double[][] Buffs = null ;
//		if (type.equals("buffs"))
//		{
//			Buffs = spells.getBuffs() ;
//		}
//		else if (type.equals("nerfs"))
//		{
//			Buffs = spells.getNerfs() ;
//		}
//		OriginalValue = new double[] {PA.getLife().getMaxValue(), PA.getMp().getMaxValue(), BA.getPhyAtk().getBaseValue(), BA.getMagAtk().getBaseValue(), BA.getPhyDef().getBaseValue(), BA.getMagDef().getBaseValue(), BA.getDex().getBaseValue(), BA.getAgi().getBaseValue(),
//				BA.getCrit()[0],
//				BA.getStun().getBasicAtkChance(),
//				BA.getBlock().getBasicAtkChance(),
//				BA.getBlood().getBasicAtkChance(), BA.getBlood().getBasicDefChance(), BA.getBlood().getBasicAtk(), BA.getBlood().getBasicDef(),
//				BA.getPoison().getBasicAtkChance(), BA.getPoison().getBasicDefChance(), BA.getPoison().getBasicAtk(), BA.getPoison().getBasicDef(),
//				BA.getSilence().getBasicAtkChance()} ;
//		if (action.equals("deactivate"))
//		{
//			ActionMult = -1 ;
//		}
//		if (att == 11 | att == 12)
//		{
//			if (action.equals("deactivate"))
//			{
//				Buff[att][0] += (OriginalValue[att]*Buffs[att][0] + Buffs[att][1])*BuffNerfLevel*ActionMult ;
//				Buff[att][1] += (OriginalValue[att]*Buffs[att][3] + Buffs[att][4])*BuffNerfLevel*ActionMult ;
//				Buff[att][2] += (OriginalValue[att]*Buffs[att][6] + Buffs[att][7])*BuffNerfLevel*ActionMult ;
//				Buff[att][3] += (OriginalValue[att]*Buffs[att][9] + Buffs[att][10])*BuffNerfLevel*ActionMult ;
//			}
//			else
//			{
//				if (Math.random() <= Buffs[att][2])
//				{
//					Buff[att][1] += (OriginalValue[att]*Buffs[att][0] + Buffs[att][1])*BuffNerfLevel*ActionMult ;
//				}
//				if (Math.random() <= Buffs[att][5])
//				{
//					Buff[att][1] += (OriginalValue[att]*Buffs[att][3] + Buffs[att][4])*BuffNerfLevel*ActionMult ;
//				}
//				if (Math.random() <= Buffs[att][8])
//				{
//					Buff[att][2] += (OriginalValue[att]*Buffs[att][6] + Buffs[att][7])*BuffNerfLevel*ActionMult ;
//				}
//				if (Math.random() <= Buffs[att][11])
//				{
//					Buff[att][3] += (OriginalValue[att]*Buffs[att][9] + Buffs[att][10])*BuffNerfLevel*ActionMult ;
//				}
//			}
//		}
//		else if (action.equals("deactivate") | Math.random() <= Buffs[att][2])
//		{
//			Buff[att][0] += (OriginalValue[att]*Buffs[att][0] + Buffs[att][1])*BuffNerfLevel*ActionMult ;
//		}
//		if (!SpellIsActive)
//		{
//			PA.getLife().incCurrentValue((int) Buff[0][0]) ;
//			PA.getMp().incCurrentValue((int) Buff[1][0]) ;
//			BA.getPhyAtk().incBonus(Buff[2][0]) ;
//			BA.getMagAtk().incBonus(Buff[3][0]) ;
//			BA.getPhyDef().incBonus(Buff[4][0]) ;
//			BA.getMagDef().incBonus(Buff[5][0]) ;
//			BA.getDex().incBonus(Buff[6][0]) ;
//			BA.getAgi().incBonus(Buff[7][0]) ;
//			BA.getCrit()[1] += Buff[8][0] ;
//			BA.getStun().incAtkChanceBonus(Buff[9][0]);  ;
//			BA.getBlock().incAtkChanceBonus(Buff[10][0]) ;
//			BA.getBlood()[1] += Buff[11][0] ;
//			BA.getBlood()[3] += Buff[11][1] ;
//			BA.getBlood()[5] += Buff[11][2] ;
//			BA.getBlood()[7] += Buff[11][3] ;
//			BA.getBlood()[8] += Buff[11][4] ;
//			BA.getPoison()[1] += Buff[12][0] ;
//			BA.getPoison()[3] += Buff[12][1] ;
//			BA.getPoison()[5] += Buff[12][2] ;
//			BA.getPoison()[7] += Buff[12][3] ;
//			BA.getPoison()[8] += Buff[12][4] ;
//			BA.getSilence().incAtkChanceBonus(Buff[13][0]) ;
//		}	
//	}
//	public void TakeBloodAndPoisonDamage(LiveBeing attacker)
//	{
//		int BloodDamage = 0 ;
//		int PoisonDamage = 0 ;
//		if (0 < BA.getStatus().getBlood())
//		{
//			BloodDamage = (int) Math.max(attacker.getBA().getBlood().TotalAtk() - BA.getBlood().TotalDef(), 0) ;
//		}
//		if (0 < BA.getStatus().getPoison())
//		{
//			PoisonDamage = (int) Math.max(attacker.getBA().getPoison().TotalAtk() - BA.getPoison().TotalDef(), 0) ;
//		}
//		PA.getLife().incCurrentValue(-BloodDamage - PoisonDamage) ;
//		if (attacker instanceof Player)
//		{
//			Player player = (Player) attacker ;
//			if (0 < BloodDamage)
//			{
//				player.getStats()[17] += BloodDamage ;
//			}
//			if (0 < PoisonDamage)
//			{
//				player.getStats()[20] += PoisonDamage ;
//			}
//			if (player.getJob() == 4 & 0 < player.getSpells().get(6).getLevel() & player.getSpells().get(6).isActive())	// Tasty
//			{
//				player.getLife().incCurrentValue(BloodDamage) ;
//			}
//		}
//	}

	
}
