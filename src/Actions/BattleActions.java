package Actions;
import GameComponents.PersonalAttributes;
import Main.Utg;

public abstract class BattleActions
{
	private static String[] ElemID ;
	private static float[][] ElemMult ;
	private static float randomAmp ;
	public static void InitializeStaticVars(String CSVPath)
	{
		int NumElem = 10 ;
    	ElemID = new String[NumElem] ;
		ElemMult = new float[NumElem][NumElem] ;
		String[][] ElemInput = Utg.ReadTextFile(CSVPath + "Elem.csv", NumElem, NumElem) ;
		for (int i = 0 ; i <= NumElem - 1 ; ++i)
		{
			ElemID[i] = ElemInput[i][0] ;
			for (int j = 0 ; j <= NumElem - 1 ; ++j)
			{
				ElemMult[i][j] = Float.parseFloat(ElemInput[i][j + 1]) ;
			}				
		}
		
		randomAmp = (float)0.1 ;
	}
	
	
	public static float BasicElemMult(String Atk, String Def)
	{
		return ElemMult[Utg.IndexOf(ElemID, Atk)][Utg.IndexOf(ElemID, Def)] ;
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
	
	public static int CalcEffect(float Dex, float Agi, float CritAtk, float CritDef, float BlockDef)
	{
		int effect = -1 ;
		if (Block(BlockDef))
		{
			effect = 3 ;	// block
		} 
		else if (Hit(Dex, Agi))
		{
			effect = 0 ;	// hit
			if (CriticalAtk(CritAtk, CritDef))
			{
				effect = 1 ;	// crit
			}
		}
		else
		{
			effect = 2 ;	// miss
		}
		return effect;
	}
	
	public static int CalcAtk(int effect, float Atk, float Def, String[] AtkElem, String[] DefElem, float ElemModifier)
	{
		int damage = -1 ;
		if (effect == 2 | effect == 3)	// miss or block
		{
			damage = 0 ;
		} 
		else if (effect == 0)	// hit
		{
			damage = Math.max(0, (int)(Atk - Def)) ;
		}
		else if (effect == 1)	// crit
		{
			double randomMult = Utg.RandomMult(randomAmp) ;
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
	public static int[] knockback(int[] SourcePos, int step, PersonalAttributes PA)
	{
		int angletan = (int)((PA.getPos()[1] - SourcePos[1]) / (float)(PA.getPos()[0] - SourcePos[0])) ;
		int dirx = (int)Math.signum(PA.getPos()[0] - SourcePos[0]) ;
		int diry = (int)Math.signum(PA.getPos()[1] - SourcePos[1]) ;
		//PA.setPos(new int[] {PA.getPos()[0] + step * dirx, PA.getPos()[1] + step * diry * angletan}) ;
		return new int[] {PA.getPos()[0] + step * dirx, PA.getPos()[1] + step * diry * angletan} ;
	}
}
