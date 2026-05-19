package spells;

public class BuffPower
{    
	private final double percentIncrease;
	private final double valueIncrease;
	private final double chance;

    protected BuffPower(double percentIncrease, double valueIncrease, double chance)
    {
        this.percentIncrease = percentIncrease ;
        this.valueIncrease = valueIncrease ;
        this.chance = chance ;
    }

    protected double getPercentIncrease() { return percentIncrease ;}
    protected double getValueIncrease() { return valueIncrease ;}
    protected double getChance() { return chance ;} // TODO buff chance não está sendo usado

    @Override
    public String toString()
    {
        return "BuffPower [percentIncrease=" + percentIncrease + ", valueIncrease=" + valueIncrease + ", chance="
                + chance + "]";
    }

}
