package liveBeings;

public class BasicAttribute
{
	private int currentValue ;
	private int maxValue ;
	private double multiplier ;
	private double rate ;

	public BasicAttribute(int currentValue, int maxValue, double multiplier)
	{
		this.currentValue = currentValue;
		this.maxValue = maxValue;
		this.multiplier = multiplier;
		rate = currentValue / (double) maxValue ;
	}

	public int getCurrentValue() {return currentValue ;}
	public int getMaxValue() {return maxValue ;}
	public double getMultiplier() {return multiplier ;}
	public double getRate() {return rate ;}

	public void incCurrentValue(int amount) {Math.max(0, Math.min(currentValue + amount, maxValue)) ;}
	public void incMaxValue(int amount) {maxValue += amount ;}
	public void setToMaximum() {currentValue = maxValue ;}

	@Override
	public String toString()
	{
		return "BasicAttribute [currentValue=" + currentValue + ", maxValue=" + maxValue + ", multiplier=" + multiplier+ "]" ;
	}
	
}