package battle;

import java.util.Map;

import attributes.Attributes;

public class AtkResults
{
	private AtkTypes atkType ;
	private AtkEffects effect ;
	private int damage ;
	private Map<Attributes, Double> status ;
	
	public AtkResults(AtkTypes atkType, AtkEffects effect, int damage, Map<Attributes, Double> status)
	{
		this.atkType = atkType;
		this.effect = effect;
		this.damage = damage;
		this.status = status;
	}

	public AtkResults(AtkTypes atkType, AtkEffects effect, int damage)
	{
		this(atkType, effect, damage, null) ;
	}

	public AtkResults(AtkTypes atkType, int damage)
	{
		this(atkType, AtkEffects.none, damage, null) ;
	}

	public AtkResults(AtkTypes atkType)
	{
		this(atkType, AtkEffects.none, 0, null) ;
	}

	public AtkResults()
	{
		this(null, AtkEffects.none, 0, null) ;
	}

	public AtkTypes getAtkType() { return atkType ;}
	public AtkEffects getEffect() { return effect ;}
	public int getDamage() { return damage ;}
	public Map<Attributes, Double> getStatus() { return status ;}

	@Override
	public String toString()
	{
		return "AtkResults: type = " + atkType + ", effect = " + effect + ", damage = " + damage + " status = " + status ;
	}	
}
