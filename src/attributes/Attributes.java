package attributes;

public enum Attributes
{
	life,
	mp,
	exp,
	satiation,
	thirst,
	phyAtk,
	magAtk,
	phyDef,
	magDef,
	dex,
	agi,
	crit,
	stun,
	block,
	blood,
	poison,
	silence;

	public static Attributes[] getBasicPersonal()
	{
		return new Attributes[] {life, mp} ;
	}
	
	public static Attributes[] getPersonal()
	{
		return new Attributes[] {life, mp, exp, satiation, thirst} ;
	}
	
	public static Attributes[] getBattle()
	{
		return new Attributes[] {phyAtk, magAtk, phyDef, magDef, dex, agi} ;
	}
	
	public static Attributes[] getSpecial()
	{
		return new Attributes[] {stun, block, blood, poison, silence} ;
	}

}
