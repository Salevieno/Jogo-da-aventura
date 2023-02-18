package attributes;

public class BattleSpecialAttribute
{
	private double basicAtkChance ;
	private double basicAtkChanceBonus ;
	private double basicDefChance ;
	private double basicDefChanceBonus ;
	private int duration ;
	
	
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
	
	public double TotalAtkChance()
	{
		return basicAtkChance + basicAtkChanceBonus ;
	}
	public double TotalDefChance()
	{
		return basicDefChance + basicDefChanceBonus ;
	}
	
	public void incAtkChanceBonus(double inc) {basicAtkChanceBonus += inc ;}
	public void incDefChanceBonus(double inc) {basicDefChanceBonus += inc ;}
	public void incDuration(double inc) {duration += inc ;}
}