package attributes;

import utilities.UtilG;

public class BattleSpecialAttribute
{
	protected double basicAtkChance ;
	protected double basicAtkChanceBonus ;
	protected double basicDefChance ;
	protected double basicDefChanceBonus ;
	protected int duration ;
	
	
	public BattleSpecialAttribute(double basicAtkChance, double basicAtkChanceBonus, double basicDefChance,
			double basicDefChanceBonus, int duration)
	{
		this.basicAtkChance = basicAtkChance;
		this.basicAtkChanceBonus = basicAtkChanceBonus;
		this.basicDefChance = basicDefChance;
		this.basicDefChanceBonus = basicDefChanceBonus;
		this.duration = duration;
	}
	
	public double getBasicAtkChance()
	{
		return basicAtkChance;
	}
	public double getBasicAtkChanceBonus()
	{
		return basicAtkChanceBonus;
	}
	public double getBasicDefChance()
	{
		return basicDefChance;
	}
	public double getBasicDefChanceBonus()
	{
		return basicDefChanceBonus;
	}
	public int getDuration()
	{
		return duration;
	}
	public double[] attributes() { return new double[] {basicAtkChance, basicDefChance, duration} ;}
	public double[] bonuses() { return new double[] {basicAtkChanceBonus, basicDefChanceBonus} ;}
	public String textAtk() { return (int) (100 * basicAtkChance) + "% + " + (int) (100 * basicAtkChanceBonus) + "%" ;}
	public String textDef() { return (int) (100 * basicDefChance) + "% + " + (int) (100 * basicDefChanceBonus) + "%" ;}
	public String textDuration() { return String.valueOf(duration) ;}
	public String[] texts() { return new String[] {textAtk(), textDef(), textDuration()} ;}
	
	public double TotalAtkChance()
	{
		return basicAtkChance + basicAtkChanceBonus ;
	}
	public double TotalDefChance()
	{
		return basicDefChance + basicDefChanceBonus ;
	}

	public void incAtkChance(double inc) {basicAtkChance += inc ;}
	public void incDefChance(double inc) {basicDefChance += inc ;}
	public void incAtkChanceBonus(double inc) {basicAtkChanceBonus += inc ;}
	public void incDefChanceBonus(double inc) {basicDefChanceBonus += inc ;}
	public void incDuration(double inc) {duration += inc ;}
}