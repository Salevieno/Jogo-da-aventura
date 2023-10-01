package attributes;

import utilities.UtilG;

public class BattleSpecialAttributeWithDamage extends BattleSpecialAttribute
{
	private int basicAtk ;
	private int basicAtkBonus ;
	private int basicDef ;
	private int basicDefBonus ;
	
	public BattleSpecialAttributeWithDamage(double basicAtkChance, double basicAtkChanceBonus, double basicDefChance,
			double basicDefChanceBonus, int basicAtk, int basicAtkBonus, int basicDef, int basicDefBonus, int duration)
	{
		super(basicAtkChance, basicAtkChanceBonus, basicDefChance, basicDefChanceBonus, duration) ;
		this.basicAtk = basicAtk ;
		this.basicAtkBonus = basicAtkBonus ;
		this.basicDef = basicDef ;
		this.basicDefBonus = basicDefBonus ;
	}
	
	
	
	public int getBasicAtk()
	{
		return basicAtk;
	}



	public int getBasicAtkBonus()
	{
		return basicAtkBonus;
	}



	public int getBasicDef()
	{
		return basicDef;
	}



	public int getBasicDefBonus()
	{
		return basicDefBonus;
	}

	public double[] attributes() { return new double[] {basicAtkChance, basicDefChance, basicAtk, basicDef, duration} ;}
	public double[] bonuses() { return new double[] {basicAtkChanceBonus, basicDefChanceBonus, basicAtkBonus, basicDefBonus} ;}
	public String textAtkChance() { return UtilG.Round(basicAtkChance, 2) + " + " + UtilG.Round(basicAtkChanceBonus, 2) ;}
	public String textDefChance() { return UtilG.Round(basicDefChance, 2) + " + " + UtilG.Round(basicDefChanceBonus, 2) ;}
	public String textAtk() { return UtilG.Round(basicAtk, 2) + " + " + UtilG.Round(basicAtkBonus, 2) ;}
	public String textDef() { return UtilG.Round(basicDef, 2) + " + " + UtilG.Round(basicDefBonus, 2) ;}
	public String textDuration() { return String.valueOf(duration) ;}
	public String[] texts() { return new String[] {textAtkChance(), textDefChance(), textAtk(), textDef(), textDuration()} ;}


	public double TotalAtk()
	{
		return basicAtk + basicAtkBonus ;
	}
	public double TotalDef()
	{
		return basicDef + basicDefBonus ;
	}
	public void incAtkBonus(double inc) {basicAtkBonus += inc ;}
	public void incDefBonus(double inc) {basicDefBonus += inc ;}
}
