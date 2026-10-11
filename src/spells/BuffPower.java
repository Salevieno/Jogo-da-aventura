package spells;

public class BuffPower
{    
	private final double percentIncrease;
	private final double valueIncrease;

    protected BuffPower(double percentIncrease, double valueIncrease)
    {
        this.percentIncrease = percentIncrease ;
        this.valueIncrease = valueIncrease ;
    }

    protected double getPercentIncrease() { return percentIncrease ;}
    protected double getValueIncrease() { return valueIncrease ;}

    @Override
    public String toString()
    {
        return "BuffPower [percentIncrease=" + percentIncrease + ", valueIncrease=" + valueIncrease + "]";
    }
}
