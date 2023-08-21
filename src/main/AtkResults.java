package main;

import utilities.AttackEffects;

public class AtkResults
{
	private AtkTypes atkType ;
	private AttackEffects effect ;
	private int damage ;

	public AtkResults()
	{
		this.atkType = null;
		this.effect = AttackEffects.none;
		this.damage = 0;
	}
	
	public AtkResults(AtkTypes atkType, AttackEffects effect, int damage)
	{
		this.atkType = atkType;
		this.effect = effect;
		this.damage = damage;
	}

	public AtkTypes getAtkType()
	{
		return atkType;
	}

	public AttackEffects getEffect()
	{
		return effect;
	}

	public int getDamage()
	{
		return damage;
	}

	@Override
	public String toString()
	{
		return "AtkResults [atkType=" + atkType + ", effect=" + effect + ", damage=" + damage + "]";
	}
	
	
}
