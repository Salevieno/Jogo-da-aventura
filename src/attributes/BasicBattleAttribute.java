package attributes;

import utilities.UtilG;

public class BasicBattleAttribute extends LiveBeingAttribute
{
	private double baseValue ;	// base value of the attribute
	private double train ;		// training value of the attribute (increases with use)
	
	public BasicBattleAttribute(double baseValue, double bonus, double train)
	{
		super(bonus);
		this.baseValue = baseValue;
		this.train = train ;
	}

	public double getBaseValue()
	{
		return baseValue;
	}

	public void setBaseValue(double baseValue)
	{
		this.baseValue = baseValue;
	}
	
	public double getTrain() {return train ;}
	public void setTrain(double newValue) {train = newValue ;}

	public void incBaseValue(double inc) {baseValue += inc ;}
	public void incTrain(double inc) {train += inc ;}
	public String text() {return UtilG.Round(baseValue, 1) + " + " + UtilG.Round(bonus, 1) + " + " + UtilG.Round(train, 1) ;}
	
	public double getTotal() {return baseValue + bonus + train ;}

	@Override
	public String toString()
	{
		return "BasicBattleAttribute [baseValue=" + baseValue + ", bonus=" + bonus + ", train=" + train + "]";
	}
	
	
}
