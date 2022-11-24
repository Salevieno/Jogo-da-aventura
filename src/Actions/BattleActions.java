package Actions;
import java.awt.Point;

import LiveBeings.PersonalAttributes;
import Utilities.UtilG;

public abstract class BattleActions
{	
	public static float BasicElemMult(String Atk, String Def)
	{
		return Battle.ElemMult[UtilG.IndexOf(Battle.ElemID, Atk)][UtilG.IndexOf(Battle.ElemID, Def)] ;
	}
	
	public static boolean Hit(float Dex, float Agi)
	{
		boolean hit = false ;
		
		if (Math.random() <= 1 - 1/(1 + Math.pow(1.1, Dex - Agi)))
		{
			hit = true ;
		}
		
		return hit ;
	}
	
	public static boolean Block(float BlockDef)
	{
		if (Math.random() < BlockDef)
		{
			return true ;
		}
		return false ;
	}

	public static boolean CriticalAtk(float CritAtk, float CritDef)
	{
		boolean Crit = false ;
		if (Math.random() + CritDef <= CritAtk)
		{
			Crit = true ;
		}
		return Crit ;
	}
		
	public static float CalcElemMult(String Atk, String Weapon, String Armor, String Shield, String SuperElem)
	{
		float mult = 1 ;
		mult = BasicElemMult(Atk, Armor)*mult ;
		mult = BasicElemMult(Atk, Shield)*mult ;
		mult = BasicElemMult(Weapon, Armor)*mult ;
		mult = BasicElemMult(Weapon, Shield)*mult ;
		mult = BasicElemMult(SuperElem, Armor)*mult ;
		mult = BasicElemMult(SuperElem, Shield)*mult ;
		return mult ;
	}
	
	public static String CalcEffect(float Dex, float Agi, float CritAtk, float CritDef, float BlockDef)
	{
		String effect = "" ;
		if (Block(BlockDef))
		{
			effect = "Block" ;
		} 
		else if (Hit(Dex, Agi))
		{
			effect = "Hit" ;
			if (CriticalAtk(CritAtk, CritDef))
			{
				effect = "Crit" ;
			}
		}
		else
		{
			effect = "Miss" ;
		}
		return effect;
	}
	
	public static int CalcAtk(String effect, float Atk, float Def, String[] AtkElem, String[] DefElem, float ElemModifier, float randomAmp)
	{
		int damage = -1 ;
		if (effect.equals("Miss") | effect.equals("Block"))
		{
			damage = 0 ;
		} 
		else if (effect.equals("Hit"))
		{
			damage = Math.max(0, (int)(Atk - Def)) ;
		}
		else if (effect.equals("Crit"))
		{
			double randomMult = UtilG.RandomMult(randomAmp) ;
			float elemMult = CalcElemMult(AtkElem[0], AtkElem[1], DefElem[0], DefElem[0], AtkElem[2]) ;
			damage = (int)(randomMult*elemMult*ElemModifier*Atk) ;
		}
		return damage ;
	}
	
	public static int[] CalcStatus(float[] Stun, float[] Block, float[] Blood, float[] Poison, float[] Silence)
	{
		// for each status 0: atk, 1: def, 2: duration
		int[] Status = new int[5] ;	// Stun, Block, Blood, Poison, Silence
		if (Math.random() <= Stun[0] - Stun[1])
		{
			Status[0] = (int)Stun[2] ;
		}
 		if (Math.random() <= Block[0] - Block[1])
 		{
 			Status[1] = (int)Block[2] ;
 		}
		if (Math.random() <= Blood[0] - Blood[1])
		{
			Status[2] = (int)Blood[2] ;
		}
		if (Math.random() <= Poison[0] - Poison[1])
		{
			Status[3] = (int)Poison[2] ;
		}
		if (Math.random() <= Silence[0] - Silence[1])
		{
			Status[4] = (int)Silence[2] ;
		}
		return Status ;
	}
	
	
	/* Attack and spell effects */
	public static Point knockback(Point SourcePos, int step, PersonalAttributes PA)
	{
		int angletan = (int)((PA.getPos().y - SourcePos.y) / (float)(PA.getPos().x - SourcePos.x)) ;
		int dirx = (int)Math.signum(PA.getPos().x - SourcePos.x) ;
		int diry = (int)Math.signum(PA.getPos().y - SourcePos.y) ;
		//PA.setPos(new int[] {PA.getPos()[0] + step * dirx, PA.getPos()[1] + step * diry * angletan}) ;
		return new Point(PA.getPos().x + step * dirx, PA.getPos().y + step * diry * angletan) ;
	}
}
