package liveBeings;

import java.util.Map;

public class Buff
{
	Map<Attributes, Double> percentIncrease;
	Map<Attributes, Double> valueIncrease;
	Map<Attributes, Double> chance;
//	List<SpecialBattleAttribute> duration;
	public Buff(Map<Attributes, Double> percentIncrease, Map<Attributes, Double> valueIncrease,
			Map<Attributes, Double> chance)
	{
		this.percentIncrease = percentIncrease;
		this.valueIncrease = valueIncrease;
		this.chance = chance;
		percentIncrease.put(Attributes.life, 0.1);
		valueIncrease.put(Attributes.life, 10.0);
		chance.put(Attributes.life, 1.0);
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
