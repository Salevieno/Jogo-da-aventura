package attributes;

import utilities.UtilG;

public class BattleSpecialAttributeWithDamage extends BattleSpecialAttribute
{
	private double basicAtk ;
	private double basicAtkBonus ;
	private double basicDef ;
	private double basicDefBonus ;
	
	public BattleSpecialAttributeWithDamage(double basicAtkChance, double basicAtkChanceBonus, double basicDefChance,
			double basicDefChanceBonus, double basicAtk, double basicAtkBonus, double basicDef, double basicDefBonus, int duration)
	{
		super(basicAtkChance, basicAtkChanceBonus, basicDefChance, basicDefChanceBonus, duration) ;
		this.basicAtk = basicAtk ;
		this.basicAtkBonus = basicAtkBonus ;
		this.basicDef = basicDef ;
		this.basicDefBonus = basicDefBonus ;
	}
	
	
	
	public double getBasicAtk()
	{
		return basicAtk;
	}



	public double getBasicAtkBonus()
	{
		return basicAtkBonus;
	}



	public double getBasicDef()
	{
		return basicDef;
	}



	public double getBasicDefBonus()
	{
		return basicDefBonus;
	}

	public double[] attributes() { return new double[] {basicAtkChance, basicAtk, basicDefChance, basicDef, duration} ;}
	public double[] bonuses() { return new double[] {basicAtkChanceBonus, basicAtkBonus, basicDefChanceBonus, basicDefBonus} ;}
	public String textAtkChance() { return (int) (100 * basicAtkChance) + "% + " + (int) (100 * basicAtkChanceBonus) + "%" ;}
	public String textAtk() { return UtilG.Round(basicAtk, 2) + " + " + UtilG.Round(basicAtkBonus, 2) ;}
	public String textDefChance() { return (int) (100 * basicDefChance) + "% + " + (int) (100 * basicDefChanceBonus) + "%" ;}
	public String textDef() { return UtilG.Round(basicDef, 2) + " + " + UtilG.Round(basicDefBonus, 2) ;}
	public String textDuration() { return String.valueOf(duration) ;}
	public String[] texts() { return new String[] {textAtkChance(), textAtk(), textDefChance(), textDef(), textDuration()} ;}


	public double TotalAtk()
	{
		return basicAtk + basicAtkBonus ;
	}
	public double TotalDef()
	{
		return basicDef + basicDefBonus ;
	}
	public void incBasicAtk(double inc) {basicAtk += inc ;}
	public void incBasicDef(double inc) {basicDef += inc ;}
	public void incAtkBonus(double inc) {basicAtkBonus += inc ;}
	public void incDefBonus(double inc) {basicDefBonus += inc ;}



	@Override
	public String toString() {
		return "BattleSpecialAttributeWithDamage [basicAtk=" + basicAtk + ", basicAtkBonus=" + basicAtkBonus
				+ ", basicDef=" + basicDef + ", basicDefBonus=" + basicDefBonus + ", basicAtkChance=" + basicAtkChance
				+ ", basicAtkChanceBonus=" + basicAtkChanceBonus + ", basicDefChance=" + basicDefChance
				+ ", basicDefChanceBonus=" + basicDefChanceBonus + ", duration=" + duration + "]";
	}
	
}
