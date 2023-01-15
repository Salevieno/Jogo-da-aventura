package liveBeings;

public class LiveBeingAttribute
{
	protected double bonus ;		// bonus of the attribute (given by equipment, spells, etc)
	
	public LiveBeingAttribute(double bonus)
	{
		this.bonus = bonus;
	}

	public double getBonus()
	{
		return bonus;
	}

	public void setBonus(double bonus)
	{
		this.bonus = bonus;
	}

	public void incBonus(double inc) {bonus += inc ;}

}
