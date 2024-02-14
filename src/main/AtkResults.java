package main;

import utilities.AtkEffects;

public class AtkResults
{
	private AtkTypes atkType ;
	private AtkEffects effect ;
	private int damage ;
	private int[] status ;

	public AtkResults()
	{
		this.atkType = null;
		this.effect = AtkEffects.none;
		this.damage = 0;
		this.status = null ;
	}

	public AtkResults(AtkTypes atkType)
	{
		this.atkType = atkType;
		this.effect = AtkEffects.none;
		this.damage = 0;
		this.status = null ;
	}
	
	public AtkResults(AtkTypes atkType, AtkEffects effect, int damage, int[] status)
	{
		this.atkType = atkType;
		this.effect = effect;
		this.damage = damage;
		this.status = status;
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
	public int[] getStatus()
	{
		return status ;
	}

	@Override
	public String toString()
	{
		return "AtkResults: type = " + atkType + ", effect = " + effect + ", damage = " + damage + " status = " + status ;
	}
	
	
}
