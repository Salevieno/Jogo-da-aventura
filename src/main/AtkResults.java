package main;

import utilities.AttackEffects;

public class AtkResults
{
	private String atkType ;
	private AttackEffects effect ;
	private int damage ;
	
	public AtkResults(String atkType, AttackEffects effect, int damage)
	{
		this.atkType = atkType;
		this.effect = effect;
		this.damage = damage;
	}

	public String getAtkType()
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
	
	
}
