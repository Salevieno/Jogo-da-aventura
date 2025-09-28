package battle;

import java.util.Arrays;

public class AtkResults
{
	private AtkTypes atkType ;
	private AtkEffects effect ;
	private int damage ;
	private double[] status ;
	
	public AtkResults(AtkTypes atkType, AtkEffects effect, int damage, double[] status)
	{
		this.atkType = atkType;
		this.effect = effect;
		this.damage = damage;
		this.status = status;
	}

	public AtkResults()
	{
		this(null, AtkEffects.none, 0, null) ;
	}

	public AtkResults(AtkTypes atkType)
	{
		this(atkType, AtkEffects.none, 0, null) ;
	}

	public AtkTypes getAtkType() { return atkType ;}
	public AtkEffects getEffect() { return effect ;}
	public int getDamage() { return damage ;}
	public double[] getStatus() { return status ;}

	@Override
	public String toString()
	{
		return "AtkResults: type = " + atkType + ", effect = " + effect + ", damage = " + damage + " status = " + Arrays.toString(status) ;
	}	
}
