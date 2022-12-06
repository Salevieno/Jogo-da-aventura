package LiveBeings;

public class BasicBattleAttribute
{
	private double baseValue ;	// basic value of the attribute
	private double bonus ;		// bonus of the attribute (given by equipment, spells, etc)
	private double train ;		// training value of the attribute (increases with use)
	
	public BasicBattleAttribute(double baseValue, double bonus, double train)
	{
		this.baseValue = baseValue ;
		this.bonus = bonus ;
		this.train = train ;
	}
	
	public double getBaseValue() {return baseValue ;}
	public double getBonus() {return bonus ;}
	public double getTrain() {return train ;}
	public void setBonus(double newValue) {bonus = newValue ;}
	public void setTrain(double newValue) {train = newValue ;}

	public void incBaseValue(double inc) {baseValue += inc ;}
	public void incBonus(double inc) {bonus += inc ;}
	public void incTrain(double inc) {train += inc ;}
	
	public double getTotal() {return baseValue + bonus + train ;}
}
