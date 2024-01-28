package main;

import utilities.AtkEffects;

public class AtkResults
{
	private AtkTypes atkType ;
	private AtkEffects effect ;
	private int damage ;

	public AtkResults()
	{
		this.atkType = null;
		this.effect = AtkEffects.none;
		this.damage = 0;
	}
	
	public AtkResults(AtkTypes atkType, AtkEffects effect, int damage)
	{
		this.atkType = atkType;
		this.effect = effect;
		this.damage = damage;
	}

	public AtkTypes getAtkType()
	{
		return atkType;
	}
	
	public void setAtkType(AtkTypes atkType)
	{
		this.atkType = atkType ;
	}

	public AtkEffects getEffect()
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
		return "AtkResults: type = " + atkType + ", effect = " + effect + ", damage = " + damage ;
	}
	
	
}
