package spells;

public class AttMod
{
    private final double percentualIncrease;
    private final double valueIncrease;

    public AttMod(double percentualIncrease, double valueIncrease)
    {
        this.percentualIncrease = percentualIncrease;
        this.valueIncrease = valueIncrease;
    }

    public double getPercentualIncrease() { return percentualIncrease ;}
    public double getValueIncrease() { return valueIncrease ;}
}
