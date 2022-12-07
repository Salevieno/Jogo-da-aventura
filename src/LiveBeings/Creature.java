package LiveBeings ;

import java.awt.Color ;
import java.awt.Font;
import java.awt.Image ;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

import Actions.BattleActions;
import Graphics.DrawFunctions ;
import Graphics.DrawPrimitives;
import Main.Game;
import Maps.Maps;
import Screen.Screen;
import Utilities.Scale;
import Utilities.Size;
import Utilities.UtilG;
import Utilities.UtilS;
import Windows.PlayerAttributesWindow;

public class Creature extends LiveBeing
{
	private CreatureTypes type ;
	private int Map ;
	private int[] Bag ;
	private int Gold ;
	private Color color ;
	private int[] StatusCounter ;	// [Life, Mp, Phy atk, Phy def, Mag atk, Mag def, Dex, Agi, Stun, Block, Blood, Poison, Silence]
	private String[] Combo ;		// Record of the last movement
	private boolean Follow ;	
	public int countmove = 0 ;
	
	private static Color[] skinColor = new Color[] {Game.ColorPalette[0], Game.ColorPalette[1]} ;
	private static Color[] shadeColor = new Color[] {Game.ColorPalette[2], Game.ColorPalette[3]} ;
	
	public static String[] SpellKeys = new String[] {"0", "1", "2", "3"} ;
	
 	public Creature(CreatureTypes CT)
	{
 		// int Type, Image image, Image idleGif, Image movingUpGif, Image movingDownGif, Image movingLeftGif, Image movingRightGif, int Map, int[] Size, int[] Spell, PersonalAttributes PA, BattleAttributes BA, int[] Bag, int Gold, Color color, int[] StatusCounter, String[] Combo
		super(CT.getID(), CT.getPA(), CT.getBA(), CT.getMovingAnimations(), new PlayerAttributesWindow()) ;
		this.type = CT ;
		spells = CT.getSpell() ;
		this.Bag = CT.getBag() ;
		this.Gold = CT.getGold() ;
		this.color = CT.getColor() ;
		this.StatusCounter = CT.getStatusCounter() ;
		
		Point minCoord = new Point(0, (int) (0.2*Game.getScreen().getSize().y)) ;
		Size range = new Size(Game.getScreen().getSize().x, (int) ((1 - (double)(Game.getSky().height)/Game.getScreen().getSize().y) * Game.getScreen().getSize().y)) ;
		Point initialPos = UtilG.RandomPos(minCoord, range, new Size(1, 1)) ;
		PA.setPos(initialPos) ;
		

		if (getName().equals("Drag�o") | getName().equals("Dragon"))
		{
			getPA().setPos(Game.getScreen().getCenter()) ;
		}
		
		Follow = false ;
	}

	public CreatureTypes getType() {return type ;}
	public String getName() {return PA.getName() ;}
	public int getLevel() {return PA.getLevel() ;}
	public int getMap() {return Map ;}
	public Size getSize() {return PA.getSize() ;}
	public Point getPos() {return PA.getPos() ;}
	public ArrayList<Spell> getSpell() {return spells ;}
	public double[] getLife() {return PA.getLife() ;}
	public double[] getMp() {return PA.getMp() ;}
	public double getRange() {return PA.getRange() ;}
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
	public String[] getElem() {return PA.Elem ;}
	public int[] getExp() {return PA.getExp() ;}
	public int[] getBag() {return Bag ;}
	public int getGold() {return Gold ;}
	public int getStep() {return PA.getStep() ;}
	public Color getColor() {return color ;}
	//public int[][] getActions() {return PA.Actions ;}
	public int[] getStatusCounter() {return StatusCounter ;}
	public String getAction() {return PA.getCurrentAction() ;}
	public String[] getCombo() {return Combo ;}
	public boolean getFollow() {return Follow ;}
	public void setPos(Point newValue) {PA.setPos(newValue) ;}
	public void setCombo(String[] C) {Combo = C ;}
	public void setFollow(boolean F) {Follow = F ;}
	public static Color[] getskinColor() {return skinColor ;}
	public static Color[] getshadeColor() {return shadeColor ;}
	public void setCurrentAction(String newValue) {PA.currentAction = newValue ;}

	public String[] getDefElems()
	{
		return new String[] {PA.Elem[0], PA.Elem[0]} ;
	}
	public boolean hasEnoughMP(int spellID)
	{
		int MPcost = 10 * spellID ;
		return (MPcost <= PA.getMp()[0]) ;
	}
	public boolean actionIsASpell()
	{
		if (UtilG.ArrayContains(Creature.SpellKeys, PA.getCurrentAction()))
		{
			return true ;
		}
		return false ;
	}
	
	public void display(Point pos, Scale scale, DrawPrimitives DP)
	{
		DP.DrawImage(type.movingAni.idleGif, pos, scale, "Center") ;
		/*if (PA.getThought().equals("Exist"))
		{
			DP.DrawImage(type.movingAni.idleGif, pos, scale, "Center") ;
		}
		else if (PA.getThought().equals("Move"))
		{
			if (PA.getDir().equals("Acima"))
			{
				DP.DrawImage(type.movingAni.movingUpGif, pos, scale, "Center") ;
			}
			if (PA.getDir().equals("Abaixo"))
			{
				DP.DrawImage(type.movingAni.movingDownGif, pos, scale, "Center") ;
			}
			if (PA.getDir().equals("Esquerda"))
			{
				DP.DrawImage(type.movingAni.movingLeftGif, pos, scale, "Center") ;
			}
			if (PA.getDir().equals("Direita"))
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
		Point MinCoord = new Point(0, (int) (0.2*screen.getSize().y)) ;
		Size Range = new Size(1, (int) screen.getSize().y / (screen.getBorders()[1] - screen.getBorders()[3])) ;
		Size step = new Size(1, 1) ;
		PA.setPos(UtilG.RandomPos(MinCoord, Range, step)) ;
	}
	public Point CenterPos()
	{
		return new Point((int) (PA.getPos().x + 0.5 * PA.getSize().x), (int) (PA.getPos().y - 0.5 * PA.getSize().y)) ;
	}
	public void updatePos(String move, Point CurrentPos, int step, Maps map)
	{
		Screen screen = Game.getScreen() ;
		Point NewPos = PA.CalcNewPos(move, CurrentPos, step) ;
		// First check if the new pos is inside the screen, then check if it is walkable
		boolean NewPosIsInsideScreen = (screen.getBorders()[0] < NewPos.x & screen.getBorders()[1] < NewPos.y & NewPos.x < screen.getBorders()[2] & NewPos.y < screen.getBorders()[3]) ;
		// if (rectTopLeftPos.x <= objPos.x & objPos.y <= rectTopLeftPos.y + rectSize.y & objPos.x <= rectTopLeftPos.x + rectSize.x & rectTopLeftPos.y <= objPos.y)
		//boolean NewPosIsInsideScreen = Utg.isInside(NewPos, screen.getBorders(), screen.getBorders()) ;
		if (NewPosIsInsideScreen)
		{
			boolean NewPosIsWalkable = map.GroundIsWalkable(NewPos, null) ;
			if (NewPosIsWalkable)
			{
				PA.setPos(NewPos) ;
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
	public void act(Point playerPos, Maps map)
	{
		Think() ;	
		if (getPA().getState().equals(LiveBeingStates.moving))
		{
			Move(playerPos, getFollow(), map) ;
			if (countmove % 5 == 0)
			{
				getPA().setdir(getPA().randomDir()) ;	// set random direction
			}
			/*if (getActions()[0][2] == 1)	// If the creature can move
			{
				ResetActions() ;
			}*/
		}
	}
	public void fight(String[] ActionKeys)
	{
		int move = -1 ;
		if (10 <= PA.getMp()[0])
		{
			move = (int)(3*Math.random() - 0.01) ;
		}
		else
		{
			move = (int)(2*Math.random() - 0.01) ;
		}
		if (move == 0)
		{
			setCurrentAction(ActionKeys[1]) ;	// Physical attack
		}
		if (move == 1)
		{
			setCurrentAction(ActionKeys[3]) ;	// Magical attack
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
			effect = BattleActions.CalcEffect(BA.TotalDex(), playerBA.TotalAgi(), BA.TotalCritAtkChance(), playerBA.TotalCritDefChance(), player.getBlock()[1]) ;
			damage = BattleActions.CalcAtk(effect, BA.TotalMagAtk(), playerBA.TotalMagDef(), new String[] {PA.Elem[0], "n", "n"}, new String[] {player.getElem()[2], player.getElem()[3]}, 1, randomAmp) ; // player.getElemMult()[UtilS.ElementID(PA.Elem[0])]) ;
		}
		if (magicalType == 0)
		{
			
		}
		if (magicalType == 1)
		{
			if (spellID == 1)	// heal
			{
				PA.incLife(BA.TotalMagAtk());
			}
			if (spellID == 2)	// knockback
			{
				player.setPos(BattleActions.knockback(player.getPos(), 6 * PA.getStep(), PA)) ;
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

		PA.getMp()[0] += -MPCost ;
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
		PA.setPos(pos) ;
	}
	public void Move(Point PlayerPos, boolean FollowPlayer, Maps map)
	{
		//if (PA.Actions[0][2] == 1 & !PA.getName().equals("Drag�o") & !PA.getName().equals("Dragon"))	// If the creature can move
		//{
			countmove = (countmove + 1) % 5 ;
			if (FollowPlayer)
			{
				Follow(PA.getPos(), PlayerPos, PA.getStep(), PA.getRange()) ;
			}
			else
			{
				updatePos(PA.getDir(), PA.getPos(), PA.getStep(), map) ;
			}				
		//}
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
		OriginalValue = new double[] {PA.getLife()[1], PA.getMp()[1], BA.getPhyAtk().getBaseValue(), BA.getMagAtk().getBaseValue(), BA.getPhyDef().getBaseValue(), BA.getMagDef().getBaseValue(), BA.getDex().getBaseValue(), BA.getAgi().getBaseValue(),
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
			PA.getLife()[0] += Buff[0][0] ;
			PA.getMp()[0] += Buff[1][0] ;
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
		double BloodDamage = 0 ;
		double PoisonDamage = 0 ;
		if (0 < BA.getSpecialStatus()[2])	// Blood
		{
			BloodDamage = Math.max(player.getBA().TotalBloodAtk() - BA.TotalBloodDef(), 0) ;
		}
		if (0 < BA.getSpecialStatus()[3])	// Poison
		{
			PoisonDamage = Math.max(player.getBA().TotalPoisonAtk() - BA.TotalPoisonDef(), 0) ;
		}
		PA.getLife()[0] += -BloodDamage - PoisonDamage ;
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
			player.getLife()[0] += BloodDamage ;
			if (player.getLife()[1] < player.getLife()[0])
			{
				player.getLife()[0] = player.getLife()[1] ;
			}
		}
	}
	public void TakeBloodAndPoisonDamage(Pet pet)
	{
		double BloodDamage = 0 ;
		double PoisonDamage = 0 ;
		if (0 < BA.getSpecialStatus()[2])	// Blood
		{
			BloodDamage = Math.max(pet.getBA().TotalBloodAtk() - BA.TotalBloodDef(), 0) ;
		}
		if (0 < BA.getSpecialStatus()[3])	// Poison
		{
			PoisonDamage = Math.max(pet.getBA().TotalPoisonAtk() - BA.TotalPoisonDef(), 0) ;
		}
		PA.getLife()[0] += -BloodDamage - PoisonDamage ;
	}
	public void Think()
	{
		if (0.3 < Math.random())
		{
			if (PA.getState().equals(LiveBeingStates.idle))
			{
				PA.setState(LiveBeingStates.moving) ;
			}
			else if (PA.getState().equals(LiveBeingStates.moving))
			{
				PA.setState(LiveBeingStates.idle) ;
			}
		}
	}

	
}
