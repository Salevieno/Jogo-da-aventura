package liveBeings;

import java.util.HashMap;
import java.util.Map;

import attributes.Attributes;
import attributes.BasicAttribute;
import attributes.BasicBattleAttribute;
import attributes.BattleSpecialAttribute;

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
	
	public void apply(int mult, int level, LiveBeing receiver)
	{
		if (receiver == null) { System.out.println("Tentando usar buffs de magia em ninguém!") ; return ;}

		for (Attributes att : Attributes.values())
		{
			if (att.equals(Attributes.exp) | att.equals(Attributes.satiation) | att.equals(Attributes.thirst))
			{
				continue ;
			}
			
			BasicAttribute personalAttribute = receiver.getPA().mapAttributes(att) ;
			if (personalAttribute != null)
			{
				double increment = personalAttribute.getMaxValue() * percentIncrease.get(att) + valueIncrease.get(att) ;
				personalAttribute.incBonus((int) Math.round(increment * level * mult));
				
				continue ;
			}
			
			BasicBattleAttribute battleAttribute = receiver.getBA().mapAttributes(att) ;
			if (battleAttribute != null)
			{
				double increment = battleAttribute.getBaseValue() * percentIncrease.get(att) + valueIncrease.get(att) ;
				battleAttribute.incBonus(Math.round(increment * level * mult));

				continue ;
			}
			
			BattleSpecialAttribute battleSpecialAttribute = receiver.getBA().mapSpecialAttributes(att) ;
			if (battleSpecialAttribute != null)
			{
				battleSpecialAttribute.incAtkChanceBonus(Math.round(percentIncrease.get(att) * level * mult));
				battleSpecialAttribute.incAtkChanceBonus(Math.round(valueIncrease.get(att) * level * mult));
			}
		}
	}
	
	public static Buff load(String[] spellsBuffsInp)
	{
		Map<Attributes, Double> percentIncrease = new HashMap<>() ;
		Map<Attributes, Double> valueIncrease = new HashMap<>() ;
		Map<Attributes, Double> chance = new HashMap<>() ;
		int BuffCont = 0 ;

		for (Attributes att : Attributes.values())
		{
			if (att.equals(Attributes.exp) | att.equals(Attributes.satiation) | att.equals(Attributes.thirst))
			{
				continue ;
			}
			if (att.equals(Attributes.blood) | att.equals(Attributes.poison))
			{
				percentIncrease.put(att, Double.parseDouble(spellsBuffsInp[BuffCont + 3])) ;
				valueIncrease.put(att, Double.parseDouble(spellsBuffsInp[BuffCont + 4])) ;
				chance.put(att, Double.parseDouble(spellsBuffsInp[BuffCont + 5])) ;
				BuffCont += 12 ;
			}
			else
			{
				percentIncrease.put(att, Double.parseDouble(spellsBuffsInp[BuffCont + 3])) ;
				valueIncrease.put(att, Double.parseDouble(spellsBuffsInp[BuffCont + 4])) ;
				chance.put(att, Double.parseDouble(spellsBuffsInp[BuffCont + 5])) ;
				BuffCont += 3 ;
			}
		}
		
		return new Buff(percentIncrease, valueIncrease, chance) ;
	}
	
	@Override
	public String toString()
	{
		return "Buff: inc% = " + percentIncrease + "\n incValue = " + valueIncrease + "\n chance = " + chance ;
	}
	
	
}
