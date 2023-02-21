package liveBeings;

import java.util.Map;

import attributes.Attributes;

public class Buff
{
	Map<Attributes, Double> percentIncrease;
	Map<Attributes, Double> valueIncrease;
	Map<Attributes, Double> chance;
	
	public Buff(Map<Attributes, Double> percentIncrease, Map<Attributes, Double> valueIncrease, Map<Attributes, Double> chance)
	{
		this.percentIncrease = percentIncrease;
		this.valueIncrease = valueIncrease;
		this.chance = chance;
	}
	public Map<Attributes, Double> getPercentIncrease()
	{
		return percentIncrease;
	}
	public void setPercentIncrease(Map<Attributes, Double> percentIncrease)
	{
		this.percentIncrease = percentIncrease;
	}
	public Map<Attributes, Double> getValueIncrease()
	{
		return valueIncrease;
	}
	public void setValueIncrease(Map<Attributes, Double> valueIncrease)
	{
		this.valueIncrease = valueIncrease;
	}
	public Map<Attributes, Double> getChance()
	{
		return chance;
	}
	public void setChance(Map<Attributes, Double> chance)
	{
		this.chance = chance;
	}
	
	@Override
	public String toString()
	{
		return "Buff [percentIncrease=" + percentIncrease + ", valueIncrease=" + valueIncrease + ", chance=" + chance
				+ "]";
	}
	
	
}
