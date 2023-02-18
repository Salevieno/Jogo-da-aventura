package attributes;

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
