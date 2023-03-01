package attributes;

public class BasicAttribute extends LiveBeingAttribute
{
	private int currentValue ;
	private int maxValue ;
	private double multiplier ;

	public BasicAttribute(int currentValue, int maxValue, double multiplier)
	{
		super(0);
		this.currentValue = currentValue;
		this.maxValue = maxValue;
		this.multiplier = multiplier;
	}

	public int getCurrentValue() { return currentValue ;}
	public int getMaxValue() { return maxValue ;}
	public double getMultiplier() { return multiplier ;}
	public double getRate() { return currentValue / (double) maxValue ;}
	public boolean isMaxed() { return currentValue == maxValue ;}

	public void incCurrentValue(int amount) {currentValue = Math.max(0, Math.min(currentValue + amount, maxValue)) ;}
	public void incMaxValue(int amount) {maxValue += amount ;}
	public void setToMaximum() {currentValue = maxValue ;}

	@Override
	public String toString()
	{
		return "BasicAttribute [currentValue=" + currentValue + ", bonus=" + bonus + ", maxValue=" + maxValue + ", multiplier=" + multiplier+ "]" ;
	}
	
}