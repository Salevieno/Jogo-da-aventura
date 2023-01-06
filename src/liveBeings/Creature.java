package liveBeings ;

import java.awt.Color ;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;

import graphics.DrawingOnPanel;
import main.Battle;
import main.Game;
import maps.GameMap;
import screen.Screen;
import utilities.Align;
import utilities.Directions;
import utilities.Scale;
import utilities.TimeCounter;
import utilities.UtilG;
import windows.PlayerAttributesWindow;

public class Creature extends LiveBeing
{
	private CreatureTypes type ;
	private int[] Bag ;
	private int Gold ;
	private Color color ;
	private int[] StatusCounter ;	// [Life, Mp, Phy atk, Phy def, Mag atk, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence]
	private boolean follow ;	
	public int countmove ;
	
	private static Color[] skinColor = new Color[] {Game.ColorPalette[0], Game.ColorPalette[1]} ;
	private static Color[] shadeColor = new Color[] {Game.ColorPalette[2], Game.ColorPalette[3]} ;
	
	public static final String[] SpellKeys = new String[] {"0", "1", "2", "3"} ;
	
 	public Creature(CreatureTypes CT)
	{
 		super(
				CT.getPA(),
				CT.getBA(),
				CT.getMovingAnimations(),
				new PlayerAttributesWindow()
			) ;
		
		this.type = CT ;
		
		this.name = CT.name;
		this.level = CT.level;
		this.size = CT.size;
		this.range = CT.range;
		this.step = CT.step;
		this.elem = CT.elem;
		mpCounter = new TimeCounter(0, CT.mpDuration);
		satiationCounter = new TimeCounter(0, CT.satiationDuration);
		moveCounter = new TimeCounter(0, CT.moveDuration) ;
		this.stepCounter = CT.stepCounter;
		
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
		
		follow = false ;
		countmove = 0 ;
	}

	public CreatureTypes getType() {return type ;}
	public ArrayList<Spell> getSpell() {return spells ;}
	public BasicAttribute getLife() {return PA.getLife() ;}
	public BasicAttribute getMp() {return PA.getMp() ;}
	public BasicBattleAttribute getPhyAtk() {return BA.getPhyAtk() ;}
	public BasicBattleAttribute getMagAtk() {return BA.getMagAtk() ;}
	public BasicBattleAttribute getPhyDef() {return BA.getPhyDef() ;}
	public BasicBattleAttribute getMagDef() {return BA.getMagDef() ;}
	public BasicBattleAttribute getDex() {return BA.getDex() ;}
	public BasicBattleAttribute getAgi() {return BA.getAgi() ;}
	public double[] getCrit() {return BA.getCrit() ;}
	public double[] getStun() {return BA.getStun() ;}
	public double[] getBlock() {return BA.getBlock() ;}
	public double[] getBlood() {return BA.getBlood() ;}
	public double[] getPoison() {return BA.getPoison() ;}
	public double[] getSilence() {return BA.getSilence() ;}
	public BasicAttribute getExp() {return PA.getExp() ;}
	public int[] getBag() {return Bag ;}
	public int getGold() {return Gold ;}
	public Color getColor() {return color ;}
	//public int[][] getActions() {return PA.Actions ;}
	public int[] getStatusCounter() {return StatusCounter ;}
	public boolean getFollow() {return follow ;}
	public void setPos(Point newValue) {pos = newValue ;}
	public void setFollow(boolean F) {follow = F ;}
	public static Color[] getskinColor() {return skinColor ;}
	public static Color[] getshadeColor() {return shadeColor ;}

	public String[] getDefElems()
	{
		return new String[] {elem[0], elem[0]} ;
	}
	public boolean hasEnoughMP(int spellID)
	{
		int MPcost = 10 * spellID ;
		return (MPcost <= PA.getMp().getCurrentValue()) ;
	}
	public boolean actionIsASpell()
	{
		if (UtilG.ArrayContains(Creature.SpellKeys, currentAction))
		{
			return true ;
		}
		return false ;
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
		}
		DP.DrawText(getPos(), "Center", 0, String.valueOf(type.getID()), new Font(Game.MainFontName, Font.BOLD, 24), Color.black) ;
		DrawAttributes(0, DP) ;*/
	}
	

	public void setRandomPos()
	{
		Screen screen = Game.getScreen() ;
		Point MinCoord = new Point(0, (int) (0.2*screen.getSize().height)) ;
		Dimension Range = new Dimension(1, (int) screen.getSize().height / (screen.getBorders()[1] - screen.getBorders()[3])) ;
		Dimension step = new Dimension(1, 1) ;
		setPos(UtilG.RandomPos(MinCoord, Range, step)) ;
	}
	public Point CenterPos()
	{
		return new Point((int) (pos.x + 0.5 * size.width), (int) (pos.y - 0.5 * size.height)) ;
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
			boolean NewPosIsWalkable = map.GroundIsWalkable(NewPos, null) ;
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
	public void act(Point playerPos, GameMap map)
	{
		Think() ;	
		if (state.equals(LiveBeingStates.moving))
		{
			Move(playerPos, getFollow(), map) ;
			if (countmove % 5 == 0)
			{
				setDir(PA.randomDir()) ;	// set random direction
			}
			/*if (getActions()[0][2] == 1)	// If the creature can move
			{
				ResetActions() ;
			}*/
		}
	}
	public void fight()
	{
		int move = -1 ;
		if (10 <= PA.getMp().getCurrentValue())
		{
			move = (int)(3*Math.random() - 0.01) ;
		}
		else
		{
			move = (int)(2*Math.random() - 0.01) ;
		}
		if (move == 0)
		{
			setCurrentAction(BattleKeys[1]) ;	// Physical attack
		}
		if (move == 1)
		{
			setCurrentAction(BattleKeys[3]) ;	// Magical attack
		}
		if (move == 2)
		{
			setCurrentAction(String.valueOf((int)(5 * Math.random() - 0.01))) ;	// Spell
		}
	}
	public int useSpell(int spellID, Player player)
	{
		int magicalType = type.getID() % 5 ;
		int MPCost = 10 ;
		String effect = "" ;
		int damage = -1 ;
		double randomAmp = (double) 0.1 ;
		BattleAttributes playerBA = player.getBA() ;
		
		if (spellID == 0)	// magical atk
		{
			effect = Battle.CalcEffect(BA.TotalDex(), playerBA.TotalAgi(), BA.TotalCritAtkChance(), playerBA.TotalCritDefChance(), player.getBlock()[1]) ;
			damage = Battle.CalcAtk(effect, BA.TotalMagAtk(), playerBA.TotalMagDef(), new String[] {elem[0], "n", "n"}, new String[] {player.getElem()[2], player.getElem()[3]}, 1, randomAmp) ; // player.getElemMult()[UtilS.ElementID(PA.Elem[0])]) ;
		}
		if (magicalType == 0)
		{
			
		}
		if (magicalType == 1)
		{
			if (spellID == 1)	// heal
			{
				PA.getLife().incCurrentValue((int) BA.TotalMagAtk());;
			}
			if (spellID == 2)	// knockback
			{
				player.setPos(Battle.knockback(player.getPos(), 6 * step, PA)) ;
			}
		}
		if (magicalType == 2)
		{
			if (spellID == 1)	// stun
			{
				
			}
			if (spellID == 2)	// blood
			{
				
			}
		}
		if (magicalType == 3)
		{
			if (spellID == 1)	// poison
			{
				
			}
			if (spellID == 2)	// silence
			{
				
			}
		}
		if (magicalType == 4)
		{
			if (spellID == 1)	// double physical atk
			{
				
			}
			if (spellID == 2)	// critical magical atk
			{
				
			}
		}

		PA.getMp().incCurrentValue(-MPCost) ;
		
		return damage ;
	}
	public void Follow(Point Pos, Point Target, int step, double mindist)
	{
		Point pos = new Point(Pos.x, Pos.y) ; // Prevent the method from modifying the original variable Pos
		double verdist = Math.abs(pos.y - Target.y), hordist = Math.abs(pos.x - Target.x) ;
		if (mindist < pos.distance(Target))
		{
			if (verdist < hordist)
			{
				if (pos.x < Target.x)
				{
					pos.x += step ;
				}
				else
				{
					pos.x += -step ;
				}
			}
			else
			{
				if (pos.y < Target.y)
				{
					pos.y += step ;
				}
				else
				{
					pos.y += -step ;
				}
			}
		}
		setPos(pos) ;
	}
	public void Move(Point PlayerPos, boolean FollowPlayer, GameMap map)
	{
		//if (PA.Actions[0][2] == 1 & !PA.getName().equals("Dragï¿½o") & !PA.getName().equals("Dragon"))	// If the creature can move
		//{
			countmove = (countmove + 1) % 5 ;
			if (FollowPlayer)
			{
				Follow(pos, PlayerPos, step, range) ;
			}
			else
			{
				updatePos(dir, pos, step, map) ;
			}				
		//}
	}

	public void Dies()
	{
		PA.getLife().setToMaximum() ;
		PA.getMp().setToMaximum() ;
		follow = false ;
	}
	public void ActivateActionCounters()
	{
		/*if (PA.Actions[0][0] % PA.Actions[0][1] == 0)
		{
			PA.Actions[0][2] = 1 ;	// Creature can move
		}*/
	}
	/*public void ResetActions()
	{
		PA.Actions = new int[][] {{0, PA.Actions[0][1], 0}, {PA.Actions[1][0], PA.Actions[1][1], PA.Actions[1][2]}} ;
	}*/

	public void ActivateBattleActionCounters()
	{
		/*if (BA.getBattleActions()[0][0] == BA.getBattleActions()[0][1])
		{
			BA.getBattleActions()[0][2] = 1 ;	// Creature can atk
		}
		if (PA.Actions[1][0] % PA.Actions[1][1] == 0)
		{
			PA.getMp()[0] = (double)(Math.min(PA.getMp()[0] + 0.02*PA.getMp()[1], PA.getMp()[1])) ;	// Creature heals mp
			PA.Actions[1][0] = 0 ;
		}	*/
	}

	public void ApplyBuffsAndNerfs(String action, String type, int att, int BuffNerfLevel, Spell spells, boolean SpellIsActive)
	{
		int ActionMult = 1 ;
		double[][] Buff = new double[14][5] ;	// [Life, Mp, PhyAtk, MagAtk, Phy def, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence][effect]
		double[] OriginalValue = new double[14] ;	// [Life, Mp, PhyAtk, MagAtk, Phy def, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence]
		double[][] Buffs = null ;
		if (type.equals("buffs"))
		{
			Buffs = spells.getBuffs() ;
		}
		else if (type.equals("nerfs"))
		{
			Buffs = spells.getNerfs() ;
		}
		OriginalValue = new double[] {PA.getLife().getMaxValue(), PA.getMp().getMaxValue(), BA.getPhyAtk().getBaseValue(), BA.getMagAtk().getBaseValue(), BA.getPhyDef().getBaseValue(), BA.getMagDef().getBaseValue(), BA.getDex().getBaseValue(), BA.getAgi().getBaseValue(),
				BA.getCrit()[0],
				BA.getStun()[0], BA.getBlock()[0], BA.getBlood()[0], BA.getBlood()[2], BA.getBlood()[4], BA.getBlood()[6], BA.getPoison()[0], BA.getPoison()[2], BA.getPoison()[4], BA.getPoison()[6], BA.getSilence()[0]} ;
		if (action.equals("deactivate"))
		{
			ActionMult = -1 ;
		}
		if (att == 11 | att == 12)
		{
			if (action.equals("deactivate"))
			{
				Buff[att][0] += (OriginalValue[att]*Buffs[att][0] + Buffs[att][1])*BuffNerfLevel*ActionMult ;
				Buff[att][1] += (OriginalValue[att]*Buffs[att][3] + Buffs[att][4])*BuffNerfLevel*ActionMult ;
				Buff[att][2] += (OriginalValue[att]*Buffs[att][6] + Buffs[att][7])*BuffNerfLevel*ActionMult ;
				Buff[att][3] += (OriginalValue[att]*Buffs[att][9] + Buffs[att][10])*BuffNerfLevel*ActionMult ;
			}
			else
			{
				if (Math.random() <= Buffs[att][2])
				{
					Buff[att][1] += (OriginalValue[att]*Buffs[att][0] + Buffs[att][1])*BuffNerfLevel*ActionMult ;
				}
				if (Math.random() <= Buffs[att][5])
				{
					Buff[att][1] += (OriginalValue[att]*Buffs[att][3] + Buffs[att][4])*BuffNerfLevel*ActionMult ;
				}
				if (Math.random() <= Buffs[att][8])
				{
					Buff[att][2] += (OriginalValue[att]*Buffs[att][6] + Buffs[att][7])*BuffNerfLevel*ActionMult ;
				}
				if (Math.random() <= Buffs[att][11])
				{
					Buff[att][3] += (OriginalValue[att]*Buffs[att][9] + Buffs[att][10])*BuffNerfLevel*ActionMult ;
				}
			}
		}
		else if (action.equals("deactivate") | Math.random() <= Buffs[att][2])
		{
			Buff[att][0] += (OriginalValue[att]*Buffs[att][0] + Buffs[att][1])*BuffNerfLevel*ActionMult ;
		}
		if (!SpellIsActive)
		{
			PA.getLife().incCurrentValue((int) Buff[0][0]) ;
			PA.getMp().incCurrentValue((int) Buff[1][0]) ;
			BA.getPhyAtk().incBonus(Buff[2][0]) ;
			BA.getMagAtk().incBonus(Buff[3][0]) ;
			BA.getPhyDef().incBonus(Buff[4][0]) ;
			BA.getMagDef().incBonus(Buff[5][0]) ;
			BA.getDex().incBonus(Buff[6][0]) ;
			BA.getAgi().incBonus(Buff[7][0]) ;
			BA.getCrit()[1] += Buff[8][0] ;
			BA.getStun()[1] += Buff[9][0] ;
			BA.getBlock()[1] += Buff[10][0] ;
			BA.getBlood()[1] += Buff[11][0] ;
			BA.getBlood()[3] += Buff[11][1] ;
			BA.getBlood()[5] += Buff[11][2] ;
			BA.getBlood()[7] += Buff[11][3] ;
			BA.getBlood()[8] += Buff[11][4] ;
			BA.getPoison()[1] += Buff[12][0] ;
			BA.getPoison()[3] += Buff[12][1] ;
			BA.getPoison()[5] += Buff[12][2] ;
			BA.getPoison()[7] += Buff[12][3] ;
			BA.getPoison()[8] += Buff[12][4] ;
			BA.getSilence()[1] += Buff[13][0] ;
		}	
	}
	public void TakeBloodAndPoisonDamage(Player player, boolean[][] SpellBuffIsActive)
	{
		int BloodDamage = 0 ;
		int PoisonDamage = 0 ;
		if (0 < BA.getSpecialStatus()[2])	// Blood
		{
			BloodDamage = (int) Math.max(player.getBA().TotalBloodAtk() - BA.TotalBloodDef(), 0) ;
		}
		if (0 < BA.getSpecialStatus()[3])	// Poison
		{
			PoisonDamage = (int) Math.max(player.getBA().TotalPoisonAtk() - BA.TotalPoisonDef(), 0) ;
		}
		PA.getLife().incCurrentValue(-BloodDamage - PoisonDamage); ;
		if (0 < BloodDamage)
		{
			player.getStats()[17] += BloodDamage ;
		}
		if (0 < PoisonDamage)
		{
			player.getStats()[20] += PoisonDamage ;
		}
		if (player.getJob() == 4 & 0 < player.getSpell().get(6).getLevel() & SpellBuffIsActive[6][0])	// Tasty
		{
			player.getLife().incCurrentValue(BloodDamage) ;	// TODO add ALL blood damage?
		}
	}
	public void TakeBloodAndPoisonDamage(Pet pet)
	{
		int BloodDamage = 0 ;
		int PoisonDamage = 0 ;
		if (0 < BA.getSpecialStatus()[2])	// Blood
		{
			BloodDamage = (int) Math.max(pet.getBA().TotalBloodAtk() - BA.TotalBloodDef(), 0) ;
		}
		if (0 < BA.getSpecialStatus()[3])	// Poison
		{
			PoisonDamage = (int) Math.max(pet.getBA().TotalPoisonAtk() - BA.TotalPoisonDef(), 0) ;
		}
		PA.getLife().incCurrentValue(-BloodDamage - PoisonDamage); ;
	}
	public void Think()
	{
		if (0.3 < Math.random())
		{
			if (state.equals(LiveBeingStates.idle))
			{
				setState(LiveBeingStates.moving) ;
			}
			else if (state.equals(LiveBeingStates.moving))
			{
				setState(LiveBeingStates.idle) ;
			}
		}
	}

	
}
