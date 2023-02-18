package liveBeings;

import java.util.Map;

import attributes.Attributes;

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
		for (Attributes att : Attributes.values())
		{
			percentIncrease.put(att, 0.0);
			valueIncrease.put(att, 0.0);
			chance.put(att, 0.0);
		}
//		percentIncrease.put(Attributes.life, 0.1);
//		valueIncrease.put(Attributes.life, 10.0);
//		chance.put(Attributes.life, 1.0);
//		percentIncrease.put(Attributes.phyAtk, 0.4);
//		valueIncrease.put(Attributes.phyAtk, 10.0);
//		chance.put(Attributes.phyAtk, 1.0);
//		percentIncrease.put(Attributes.magAtk, 0.4);
//		valueIncrease.put(Attributes.magAtk, 10.0);
//		chance.put(Attributes.magAtk, 1.0);
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
